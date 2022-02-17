package org.modules.controls;

import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companyaddress;

import java.util.*;

public class ControlAddress {

    private Companyaddress address = null;
    public boolean isEdit = false;

    public ControlAddress() {
        address = new Companyaddress();
    }

    public Integer dataStore() {
        
        Integer addressID = -1;
        
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == false) {
                address.setCreateddate(new Date());
                address.setCreatedfrom(EBISystem.ebiUser);
                address.setCompany(EBISystem.getInstance().getCompany());
            } else {
                createHistory(EBISystem.getInstance().getCompany());
                address.setChangeddate(new Date());
                address.setChangedfrom(EBISystem.ebiUser);
            }

            if (EBISystem.builder().combo("addressTypeText", "Address").getEditor().getItem() != null) {
                address.setAddresstype(EBISystem.builder().combo("addressTypeText", "Address").getEditor().getItem().toString());
            }

            address.setStreet(EBISystem.builder().textField("streetText", "Address").getText());
            address.setZip(EBISystem.builder().textField("zipText", "Address").getText());
            address.setLocation(EBISystem.builder().textField("LocationText", "Address").getText());
            address.setPbox(EBISystem.builder().textField("postcodeText", "Address").getText());
            address.setCountry(EBISystem.builder().textField("countryText", "Address").getText());

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(address);

            EBISystem.getInstance().getDataStore("Address", "ebiSave");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            EBISystem.getInstance().getCompany().getCompanyaddresses().add(address);
            
            if (!isEdit) {
                EBISystem.builder().vpanel("Address").setID(address.getAddressid());
            }

            if (address.getCompany().getIsactual() != null && address.getCompany().getIsactual()) {
                EBISystem.getInstance().loadStandardCompanyData();
            }
            addressID = address.getAddressid();
            isEdit = true;
        } catch (final Exception ex) {
            EBISystem.hibernate().session("EBICRM_SESSION").clear();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return addressID;
    }

    public Integer dataCopy(final int id) {
        Integer addressID=-1;
        try {
            if (EBISystem.getInstance().getCompany().getCompanyaddresses().size() > 0) {
                Companyaddress adrs = null;
                for (Companyaddress adrObj : EBISystem.getInstance().getCompany().getCompanyaddresses()) {
                    if (adrObj.getAddressid() == id) {
                        adrs = adrObj;
                    }
                }

                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Companyaddress adrsn = new Companyaddress();
                adrsn.setCreateddate(new Date());
                adrsn.setCreatedfrom(EBISystem.ebiUser);
                adrsn.setCompany(adrs.getCompany());
                adrsn.setAddresstype(adrs.getAddresstype() + " (Copy)");
                adrsn.setStreet(adrs.getStreet());
                adrsn.setZip(adrs.getZip());
                adrsn.setLocation(adrs.getLocation());
                adrsn.setPbox(adrs.getPbox());
                adrsn.setCountry(adrs.getCountry());

                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(adrsn);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                
                EBISystem.getInstance().getCompany().getCompanyaddresses().add(adrsn);
                addressID = adrsn.getAddressid();
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return addressID;
    }

    public void dataEdit(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanyaddresses().size() > 0) {

            for (Companyaddress adrObj : EBISystem.getInstance().getCompany().getCompanyaddresses()) {
                if (adrObj.getAddressid() == id) {
                    address = adrObj;
                }
            }

            EBISystem.builder().vpanel("Address").setID(address.getAddressid());

            if (address.getAddresstype() != null) {
                EBISystem.builder().combo("addressTypeText", "Address")
                        .setSelectedItem(address.getAddresstype());
            }

            EBISystem.builder().textField("streetText", "Address").setText(address.getStreet());
            EBISystem.builder().textField("zipText", "Address").setText(address.getZip());
            EBISystem.builder().textField("LocationText", "Address").setText(address.getLocation());
            EBISystem.builder().textField("postcodeText", "Address").setText(address.getPbox());
            EBISystem.builder().textField("countryText", "Address").setText(address.getCountry());

            EBISystem.builder().vpanel("Address").setCreatedDate(EBISystem.getInstance().getDateToString(address.getCreateddate() == null ? new Date() : address.getCreateddate()));
            EBISystem.builder().vpanel("Address").setCreatedFrom(address.getCreatedfrom() == null ? EBISystem.ebiUser : address.getCreatedfrom());

            if (address.getChangeddate() != null) {
                EBISystem.builder().vpanel("Address").setChangedDate(EBISystem.getInstance().getDateToString(address.getChangeddate()));
                EBISystem.builder().vpanel("Address").setChangedFrom(EBISystem.ebiUser);
            }

            EBISystem.getInstance().getDataStore("Address", "ebiEdit");

        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {
        if (EBISystem.getInstance().getCompany().getCompanyaddresses().size() > 0) {
            for (Companyaddress adrObj : EBISystem.getInstance().getCompany().getCompanyaddresses()) {
                if (adrObj.getAddressid() == id) {
                    address = adrObj;
                }
            }
            EBISystem.getInstance().getDataStore("Address", "ebiDelete");
            EBISystem.getInstance().getCompany().getCompanyaddresses().remove(address);
        }
    }

    public void dataShow(Integer id) {

        int selRow = EBISystem.builder().table("companyAddess", "Address").getSelectedRow() + id;
        final int size = EBISystem.getInstance().getCompany().getCompanyaddresses() == null ? -1 :
                            EBISystem.getInstance().getCompany().getCompanyaddresses().size();

        if (size > 0) {
            EBISystem.getModule().getAddressPane().getTabModel().data = new Object[size][7];
            final Iterator<Companyaddress> iterAddress = EBISystem.getInstance().getCompany().getCompanyaddresses().iterator();
            int i = 0;
            while (iterAddress.hasNext()) {
                final Companyaddress obj = iterAddress.next();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][0] = obj.getAddresstype() == null ? "" : obj.getAddresstype();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][1] = obj.getStreet() == null ? "" : obj.getStreet();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][2] = obj.getZip() == null ? "" : obj.getZip();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][3] = obj.getLocation() == null ? "" : obj.getLocation();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][4] = obj.getPbox() == null ? "" : obj.getPbox();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][5] = obj.getCountry() == null ? "" : obj.getCountry();
                EBISystem.getModule().getAddressPane().getTabModel().data[i][6] = obj.getAddressid();
                if(id != -1 && id == obj.getAddressid()){
                    selRow = i;
                }
                i++;
            }
        } else {
            EBISystem.getModule().getAddressPane().getTabModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
        }

        EBISystem.getModule().getAddressPane().getTabModel().fireTableDataChanged();
        if (EBISystem.getModule().getCompanyPane() != null) {
            EBISystem.getModule().getCompanyPane().getTabModel().data = EBISystem.getModule().getAddressPane().getTabModel().data;
            EBISystem.getModule().getCompanyPane().getTabModel().fireTableDataChanged();
        }
        
        if(selRow > -1){
            selRow = EBISystem.builder().table("companyAddess", "Address").convertRowIndexToView(selRow);
            EBISystem.builder().table("companyAddess", "Address").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataNew() {
        address = new Companyaddress();
        EBISystem.getModule().getAddressPane().initialize(false);
        EBISystem.getInstance().getDataStore("Address", "ebiNew");
        EBISystem.builder().vpanel("Address").setID(-1);
    }

    private void createHistory(final Company com) {

        final List<String> list = new ArrayList<String>();

        if (address.getCreateddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                    + EBISystem.getInstance().getDateToString(address.getCreateddate()));
        }

        if (address.getCreatedfrom() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + address.getCreatedfrom());
        }

        if (address.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                    + EBISystem.getInstance().getDateToString(address.getChangeddate()));
        }

        if (address.getChangedfrom() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + address.getChangedfrom());
        }

        if (address.getAddresstype() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_ADRESS_TYPE") + ": "
                    + (address.getAddresstype()
                            .equals(EBISystem.builder().combo("addressTypeText", "Address")
                                    .getSelectedItem().toString()) == true ? address.getAddresstype()
                            : address.getAddresstype() + "$"));
        }

        if (address.getStreet() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": "
                    + (address.getStreet()
                            .equals(EBISystem.builder().textField("streetText", "Address").getText()) == true
                    ? address.getStreet()
                    : address.getStreet() + "$"));
        }

        if (address.getZip() != null) {
            String zLch = "";
            if (!address.getZip().equals(EBISystem.builder().textField("zipText", "Address").getText())
                    || !address.getLocation()
                            .equals(EBISystem.builder().textField("LocationText", "Address").getText())) {
                zLch = "$";
            }
            list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + ": " + address.getZip() + " "
                    + address.getLocation() + zLch);
        }

        if (address.getPbox() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": "
                    + (address.getPbox().equals(
                            EBISystem.builder().textField("postcodeText", "Address").getText()) == true
                    ? address.getPbox()
                    : address.getPbox() + "$"));
        }

        if (address.getCountry() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": "
                    + (address.getCountry()
                            .equals(EBISystem.builder().textField("countryText", "Address").getText()) == true
                    ? address.getCountry()
                    : address.getCountry() + "$"));
        }

        list.add("*EOR*"); // END OF RECORD

        try {
            EBISystem.getModule().hcreator
                    .setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Address", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Set<Companyaddress> getAddressList() {
        return EBISystem.getInstance().getCompany().getCompanyaddresses();
    }

    public Companyaddress getAddress() {
        return address;
    }

    public void setAddress(final Companyaddress address) {
        this.address = address;
    }
}
