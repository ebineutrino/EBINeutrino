package ebiCRM.data.control;

import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.Company;
import ebiNeutrinoSDK.model.hibernate.Companybank;
import org.hibernate.HibernateException;

import java.util.*;

public class EBIDataControlBank {

	private Companybank companyBank = null;
	public boolean isEdit = false;

	public EBIDataControlBank() {
		companyBank = new Companybank();
	}

	public boolean dataStore() {
		try {
			EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
			if (isEdit == false) {
				companyBank.setCreateddate(new Date());
				companyBank.setCreatedfrom(EBISystem.ebiUser);
				companyBank.setCompany(EBISystem.getInstance().company);
			} else {
				createHistory(EBISystem.getInstance().company);
				companyBank.setChangeddate(new Date());
				companyBank.setChangedfrom(EBISystem.ebiUser);
			}

			companyBank.setBankname(EBISystem.gui().textField("bankNameText", "Bank").getText());
			companyBank.setBankbsb(EBISystem.gui().textField("abaNrText", "Bank").getText());
			companyBank.setBankaccount(EBISystem.gui().textField("accountNrText", "Bank").getText());
			companyBank.setBankbic(EBISystem.gui().textField("bicText", "Bank").getText());
			companyBank.setBankiban(EBISystem.gui().textField("ibanText", "Bank").getText());
			companyBank.setBankcountry(EBISystem.gui().textField("countryBankText", "Bank").getText());

			EBISystem.getInstance().getDataStore("Bank", "ebiSave");
			EBISystem.getInstance().company.getCompanybanks().add(companyBank);
			EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(companyBank);
			EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
			if (!isEdit) {
				EBISystem.gui().vpanel("Bank").setID(companyBank.getBankid());
			}

		} catch (final HibernateException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public void dataCopy(final int id) {

		try {

			if (EBISystem.getInstance().company.getCompanybanks().size() > 0) {

				Companybank bank = null;
				for (Companybank bankObj : EBISystem.getInstance().company.getCompanybanks()) {
					if (bankObj.getBankid() == id) {
						bank = bankObj;
					}
				}

				EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
				final Companybank compbank = new Companybank();
				compbank.setCreateddate(new Date());
				compbank.setCreatedfrom(EBISystem.ebiUser);

				compbank.setCompany(bank.getCompany());
				compbank.setBankname(bank.getBankname() + " - (Copy)");
				compbank.setBankbsb(bank.getBankbsb());
				compbank.setBankaccount(bank.getBankaccount());
				compbank.setBankbic(bank.getBankbic());
				compbank.setBankiban(bank.getBankiban());
				compbank.setBankcountry(bank.getBankcountry());

				EBISystem.getInstance().company.getCompanybanks().add(compbank);
				EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(compbank);
				EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

				EBISystem.gui().table("companyBankTable", "Bank").changeSelection(EBISystem
						.gui().table("companyBankTable", "Bank")
						.convertRowIndexToView(EBISystem.getCRMModule().dynMethod.getIdIndexFormArrayInATable(
								EBISystem.getCRMModule().getBankdataPane().tabModel.data, 6, compbank.getBankid())),
						0, false, false);
			}

		} catch (final HibernateException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void dataEdit(final int id) {

		if (EBISystem.getInstance().company.getCompanybanks().size() > 0) {

			for (Companybank bnkObj : EBISystem.getInstance().company.getCompanybanks()) {
				if (bnkObj.getBankid() == id) {
					companyBank = bnkObj;
					break;
				}
			}

			EBISystem.gui().vpanel("Bank").setID(companyBank.getBankid());
			EBISystem.gui().textField("bankNameText", "Bank").setText(companyBank.getBankname());
			EBISystem.gui().textField("abaNrText", "Bank").setText(companyBank.getBankbsb());
			EBISystem.gui().textField("accountNrText", "Bank").setText(companyBank.getBankaccount());
			EBISystem.gui().textField("bicText", "Bank").setText(companyBank.getBankbic());
			EBISystem.gui().textField("ibanText", "Bank").setText(companyBank.getBankiban());
			EBISystem.gui().textField("countryBankText", "Bank").setText(companyBank.getBankcountry());

			EBISystem.gui().vpanel("Bank").setCreatedDate(EBISystem.getInstance().getDateToString(companyBank.getCreateddate() == null ? new Date() : companyBank.getCreateddate()));
			EBISystem.gui().vpanel("Bank").setCreatedFrom(companyBank.getCreatedfrom() == null ? EBISystem.ebiUser : companyBank.getCreatedfrom());

			if (companyBank.getChangeddate() != null) {
				EBISystem.gui().vpanel("Bank").setChangedDate(EBISystem.getInstance().getDateToString(companyBank.getChangeddate()));
				EBISystem.gui().vpanel("Bank").setChangedFrom(EBISystem.ebiUser);
			} else {
				EBISystem.gui().vpanel("Bank").setChangedDate("");
				EBISystem.gui().vpanel("Bank").setChangedFrom("");
			}

			EBISystem.getInstance().getDataStore("Bank", "ebiEdit");

			EBISystem.gui().table("companyBankTable", "Bank")
					.changeSelection(
							EBISystem.gui().table("companyBankTable", "Bank").convertRowIndexToView(
									EBISystem.getCRMModule().dynMethod.getIdIndexFormArrayInATable(
											EBISystem.getCRMModule().getBankdataPane().tabModel.data, 6, id)),
							0, false, false);

		} else {
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
					.Show(EBIMessage.INFO_MESSAGE);
		}
	}

	public void dataDelete(final int id) {

		if (EBISystem.getInstance().company.getCompanybanks().size() > 0) {
			for (Companybank bnkObj : EBISystem.getInstance().company.getCompanybanks()) {
				if (bnkObj.getBankid() == id) {
					companyBank = bnkObj;
					break;
				}
			}
			EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
			EBISystem.hibernate().session("EBICRM_SESSION").delete(companyBank);
			EBISystem.getInstance().company.getCompanybanks().remove(companyBank);
			EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
			EBISystem.getInstance().getDataStore("Bank", "ebiDelete");
		}
	}

	public void dataShow() {

		final int srow = EBISystem.gui().table("companyBankTable", "Bank").getSelectedRow();

		final int size = EBISystem.getInstance().company.getCompanybanks().size();

		if (size > 0) {

			EBISystem.getCRMModule().getBankdataPane().tabModel.data = new Object[size][7];
			final Iterator<Companybank> iter = EBISystem.getInstance().company.getCompanybanks().iterator();
			int i = 0;
			while (iter.hasNext()) {

				final Companybank obj = iter.next();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][0] = obj.getBankname() == null ? "" : obj.getBankname();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][1] = obj.getBankbsb() == null ? "" : obj.getBankbsb();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][2] = obj.getBankaccount() == null ? "" : obj.getBankaccount();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][3] = obj.getBankbic() == null ? "" : obj.getBankbic();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][4] = obj.getBankiban() == null ? "" : obj.getBankiban();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][5] = obj.getBankcountry() == null ? "" : obj.getBankcountry();
				EBISystem.getCRMModule().getBankdataPane().tabModel.data[i][6] = obj.getBankid();
				i++;
			}

		} else {
			EBISystem.getCRMModule().getBankdataPane().tabModel.data = new Object[][] {{ EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "" } };
		}

		EBISystem.getCRMModule().getBankdataPane().tabModel.fireTableDataChanged();
		EBISystem.gui().table("companyBankTable", "Bank").changeSelection(srow, 0, false, false);
	}

	public void dataNew() {
		companyBank = new Companybank();
		EBISystem.getCRMModule().getBankdataPane().initialize(false);
		EBISystem.getInstance().getDataStore("Bank", "ebiNew");
		EBISystem.gui().vpanel("Bank").setID(-1);
	}

	private void createHistory(final Company com) {

		final List<String> list = new ArrayList<String>();
		list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
				+ EBISystem.getInstance().getDateToString(companyBank.getCreateddate()));
		list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + EBISystem.ebiUser);

		if (companyBank.getChangeddate() != null) {
			list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
					+ EBISystem.getInstance().getDateToString(companyBank.getChangeddate()));
			list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + EBISystem.ebiUser);
		}
		list.add(EBISystem.i18n("EBI_LANG_C_BANK_NAME") + ": "
				+ (companyBank.getBankname()
						.equals(EBISystem.gui().textField("bankNameText", "Bank").getText()) == true
								? companyBank.getBankname()
								: companyBank.getBankname() + "$"));
		list.add(EBISystem.i18n("EBI_LANG_C_BANK_CODE") + ": "
				+ (companyBank.getBankbsb()
						.equals(EBISystem.gui().textField("abaNrText", "Bank").getText()) == true
								? companyBank.getBankbsb()
								: companyBank.getBankbsb() + "$"));
		list.add(EBISystem.i18n("EBI_LANG_C_KONTO_NR") + ": "
				+ (companyBank.getBankaccount()
						.equals(EBISystem.gui().textField("accountNrText", "Bank").getText()) == true
								? companyBank.getBankaccount()
								: companyBank.getBankaccount() + "$"));
		list.add(EBISystem.i18n("EBI_LANG_C_BIC") + ": "
				+ (companyBank.getBankbic()
						.equals(EBISystem.gui().textField("bicText", "Bank").getText()) == true
								? companyBank.getBankbic()
								: companyBank.getBankbic() + "$"));
		list.add(EBISystem.i18n("EBI_LANG_C_IBAN") + ": "
				+ (companyBank.getBankiban()
						.equals(EBISystem.gui().textField("ibanText", "Bank").getText()) == true
								? companyBank.getBankiban()
								: companyBank.getBankiban() + "$"));
		list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": "
				+ (companyBank.getBankcountry()
						.equals(EBISystem.gui().textField("countryBankText", "Bank").getText()) == true
								? companyBank.getBankcountry()
								: companyBank.getBankcountry() + "$"));

		list.add("*EOR*"); // END OF RECORD

		try {
			EBISystem.getCRMModule().hcreator
					.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Bankdata", list));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public Companybank getCompanyBank() {
		return companyBank;
	}

	public void setCompanyBank(final Companybank companyBank) {
		this.companyBank = companyBank;
	}

	public Set<Companybank> getBankList() {
		return EBISystem.getInstance().company.getCompanybanks();
	}
}