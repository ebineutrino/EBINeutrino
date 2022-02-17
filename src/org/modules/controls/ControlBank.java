package org.modules.controls;

import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companybank;
import org.hibernate.HibernateException;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

public class ControlBank {

    private Companybank companyBank = null;

    @Getter
    @Setter
    private boolean isEdit = false;

    public ControlBank() {
        companyBank = new Companybank();
    }

    public Integer dataStore() {
        Integer bankID = -1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == false) {
                companyBank.setCreateddate(new Date());
                companyBank.setCreatedfrom(EBISystem.ebiUser);
                companyBank.setCompany(EBISystem.getInstance().getCompany());
            } else {
                createHistory(EBISystem.getInstance().getCompany());
                companyBank.setChangeddate(new Date());
                companyBank.setChangedfrom(EBISystem.ebiUser);
            }

            companyBank.setBankname(EBISystem.builder().textField("bankNameText", "Bank").getText());
            companyBank.setBankbsb(EBISystem.builder().textField("abaNrText", "Bank").getText());
            companyBank.setBankaccount(EBISystem.builder().textField("accountNrText", "Bank").getText());
            companyBank.setBankbic(EBISystem.builder().textField("bicText", "Bank").getText());
            companyBank.setBankiban(EBISystem.builder().textField("ibanText", "Bank").getText());
            companyBank.setBankcountry(EBISystem.builder().textField("countryBankText", "Bank").getText());

            EBISystem.getInstance().getDataStore("Bank", "ebiSave");
            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(companyBank);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            EBISystem.getInstance().getCompany().getCompanybanks().add(companyBank);

            if (!isEdit) {
                EBISystem.builder().vpanel("Bank").setID(companyBank.getBankid());
            }
            bankID = companyBank.getBankid();
            isEdit = true;
        } catch (final HibernateException e) {
            EBISystem.hibernate().session("EBICRM_SESSION").clear();
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return bankID;
    }

    public Integer dataCopy(final int id) {
        Integer bankID = -1;
        try {
            if (EBISystem.getInstance().getCompany().getCompanybanks().size() > 0) {
                Companybank bank = null;
                for (Companybank bankObj : EBISystem.getInstance().getCompany().getCompanybanks()) {
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

                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(compbank);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

                EBISystem.getInstance().getCompany().getCompanybanks().add(compbank);
                bankID = compbank.getBankid();
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
        }
        return bankID;
    }

    public void dataEdit(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanybanks().size() > 0) {

            for (Companybank bnkObj : EBISystem.getInstance().getCompany().getCompanybanks()) {
                if (bnkObj.getBankid() == id) {
                    companyBank = bnkObj;
                    break;
                }
            }
 
            
            EBISystem.builder().vpanel("Bank").setID(companyBank.getBankid());
            EBISystem.builder().textField("bankNameText", "Bank").setText(companyBank.getBankname());
            EBISystem.builder().textField("abaNrText", "Bank").setText(companyBank.getBankbsb());
            EBISystem.builder().textField("accountNrText", "Bank").setText(companyBank.getBankaccount());
            EBISystem.builder().textField("bicText", "Bank").setText(companyBank.getBankbic());
            EBISystem.builder().textField("ibanText", "Bank").setText(companyBank.getBankiban());
            EBISystem.builder().textField("countryBankText", "Bank").setText(companyBank.getBankcountry());

            EBISystem.builder().vpanel("Bank").setCreatedDate(EBISystem.getInstance().getDateToString(companyBank.getCreateddate() == null ? new Date() : companyBank.getCreateddate()));
            EBISystem.builder().vpanel("Bank").setCreatedFrom(companyBank.getCreatedfrom() == null ? EBISystem.ebiUser : companyBank.getCreatedfrom());

            if (companyBank.getChangeddate() != null) {
                EBISystem.builder().vpanel("Bank").setChangedDate(EBISystem.getInstance().getDateToString(companyBank.getChangeddate()));
                EBISystem.builder().vpanel("Bank").setChangedFrom(EBISystem.ebiUser);
            } else {
                EBISystem.builder().vpanel("Bank").setChangedDate("");
                EBISystem.builder().vpanel("Bank").setChangedFrom("");
            }

            EBISystem.getInstance().getDataStore("Bank", "ebiEdit");
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                    .Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanybanks().size() > 0) {
            for (Companybank bnkObj : EBISystem.getInstance().getCompany().getCompanybanks()) {
                if (bnkObj.getBankid() == id) {
                    companyBank = bnkObj;
                    break;
                }
            }
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").delete(companyBank);
            EBISystem.getInstance().getCompany().getCompanybanks().remove(companyBank);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            EBISystem.getInstance().getDataStore("Bank", "ebiDelete");
        }
    }

    public void dataShow(Integer id) {

        int selRow = EBISystem.builder().table("companyBankTable", "Bank").getSelectedRow() + id;
        final int size = EBISystem.getInstance().getCompany().getCompanybanks().size();

        if (size > 0) {

            EBISystem.getModule().getBankdataPane().getTabModel().data = new Object[size][7];
            final Iterator<Companybank> iter = EBISystem.getInstance().getCompany().getCompanybanks().iterator();
            int i = 0;
            while (iter.hasNext()) {

                final Companybank obj = iter.next();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][0] = obj.getBankname() == null ? "" : obj.getBankname();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][1] = obj.getBankbsb() == null ? "" : obj.getBankbsb();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][2] = obj.getBankaccount() == null ? "" : obj.getBankaccount();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][3] = obj.getBankbic() == null ? "" : obj.getBankbic();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][4] = obj.getBankiban() == null ? "" : obj.getBankiban();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][5] = obj.getBankcountry() == null ? "" : obj.getBankcountry();
                EBISystem.getModule().getBankdataPane().getTabModel().data[i][6] = obj.getBankid();
                if (id != -1 && id == obj.getBankid()) {
                    selRow = i;
                }
                i++;
            }

        } else {
            EBISystem.getModule().getBankdataPane().getTabModel().data
                    = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
        }

        EBISystem.getModule().getBankdataPane().getTabModel().fireTableDataChanged();
        if (selRow > -1) {
            selRow = EBISystem.builder().table("companyBankTable", "Bank").convertRowIndexToView(selRow);
            EBISystem.builder().table("companyBankTable", "Bank").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataNew() {
        companyBank = new Companybank();
        EBISystem.getModule().getBankdataPane().initialize(false);
        EBISystem.getInstance().getDataStore("Bank", "ebiNew");
        EBISystem.builder().vpanel("Bank").setID(-1);
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
                        .equals(EBISystem.builder().textField("bankNameText", "Bank").getText()) == true
                ? companyBank.getBankname()
                : companyBank.getBankname() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_BANK_CODE") + ": "
                + (companyBank.getBankbsb()
                        .equals(EBISystem.builder().textField("abaNrText", "Bank").getText()) == true
                ? companyBank.getBankbsb()
                : companyBank.getBankbsb() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_KONTO_NR") + ": "
                + (companyBank.getBankaccount()
                        .equals(EBISystem.builder().textField("accountNrText", "Bank").getText()) == true
                ? companyBank.getBankaccount()
                : companyBank.getBankaccount() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_BIC") + ": "
                + (companyBank.getBankbic()
                        .equals(EBISystem.builder().textField("bicText", "Bank").getText()) == true
                ? companyBank.getBankbic()
                : companyBank.getBankbic() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_IBAN") + ": "
                + (companyBank.getBankiban()
                        .equals(EBISystem.builder().textField("ibanText", "Bank").getText()) == true
                ? companyBank.getBankiban()
                : companyBank.getBankiban() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": "
                + (companyBank.getBankcountry()
                        .equals(EBISystem.builder().textField("countryBankText", "Bank").getText()) == true
                ? companyBank.getBankcountry()
                : companyBank.getBankcountry() + "$"));

        list.add("*EOR*"); // END OF RECORD

        try {
            EBISystem.getModule().hcreator
                    .setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Bankdata", list));
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
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
        return EBISystem.getInstance().getCompany().getCompanybanks();
    }
}
