package org.modules.controls;

import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companycontactaddress;
import org.sdk.model.hibernate.Companycontacts;

import java.util.*;

public class ControlContact {

    private Companycontacts contact = null;
    public boolean isEdit = false;

    public ControlContact() {
        contact = new Companycontacts();
    }

    public Integer dataStore() {
        Integer contactID =-1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();

            if (isEdit == true) {
                dataHistory(EBISystem.getInstance().getCompany());
                contact.setChangeddate(new Date());
                contact.setChangedfrom(EBISystem.ebiUser);
            } else {
                contact.setCreateddate(new Date());
                contact.setCreatedfrom(EBISystem.ebiUser);
                contact.setCompany(EBISystem.getInstance().getCompany());
            }

            if (EBISystem.gui().combo("genderTex", "Contact").getSelectedItem() != null) {
                contact.setGender(EBISystem.gui().combo("genderTex", "Contact").getSelectedItem().toString());
            }

            contact.setPosition(EBISystem.gui().textField("positionText", "Contact").getText());
            contact.setSurname(EBISystem.gui().textField("surnameText", "Contact").getText());
            contact.setName(EBISystem.gui().textField("nameText", "Contact").getText());
            contact.setMittelname(EBISystem.gui().textField("middleNameText", "Contact").getText());
            contact.setTitle(EBISystem.gui().textField("titleText", "Contact").getText());

            if (EBISystem.gui().timePicker("birthdateText", "Contact").getDate() != null) {
                contact.setBirddate(EBISystem.gui().timePicker("birthdateText", "Contact").getDate());
            }

            contact.setPhone(EBISystem.gui().textField("telefonText", "Contact").getText());
            contact.setFax(EBISystem.gui().textField("faxText", "Contact").getText());
            contact.setMobile(EBISystem.gui().textField("mobileText", "Contact").getText());
            contact.setEmail(EBISystem.gui().textField("emailText", "Contact").getText());
            contact.setDescription(EBISystem.gui().textArea("contactDescription", "Contact").getText());
            contact.setMaincontact(EBISystem.gui().getCheckBox("mainContactText", "Contact").isSelected());

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(contact);

            // Save contact address
            if (!this.contact.getCompanycontactaddresses().isEmpty()) {
                final Iterator<Companycontactaddress> iter = this.contact.getCompanycontactaddresses().iterator();
                while (iter.hasNext()) {
                    Companycontactaddress conAddress = iter.next();
                    if (conAddress.getAddressid() != null && conAddress.getAddressid() < 0) {
                        conAddress.setAddressid(null);
                    }
                    conAddress.setCompanycontacts(contact);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(conAddress);
                }
            }

            EBISystem.getInstance().getDataStore("Contact", "ebiSave");
            if (!isEdit) {
                EBISystem.gui().vpanel("Contact").setID(contact.getContactid());
            }

            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            EBISystem.getInstance().getCompany().getCompanycontactses().add(contact);
            
            contactID = contact.getContactid();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        if (contact != null && contact.getCompany() != null
                && contact.getCompany().getIsactual() != null && contact.getCompany().getIsactual()) {
            EBISystem.getInstance().loadStandardCompanyData();
        }

        return contactID;
    }

    public Integer dataCopy(final int id) {
        Integer contactID = -1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (EBISystem.getInstance().getCompany().getCompanycontactses().size() > 0) {
                Companycontacts con = null;
                for (Companycontacts objCnts : EBISystem.getInstance().getCompany().getCompanycontactses()) {
                    if (objCnts.getContactid() == id) {
                        con = objCnts;
                        break;
                    }
                }
                
                final Companycontacts conx = new Companycontacts();
                conx.setCreateddate(new Date());
                conx.setCreatedfrom(EBISystem.ebiUser);
                conx.setCompany(con.getCompany());
                conx.setGender(con.getGender());
                conx.setPosition(con.getPosition() + " - (Copy)");
                conx.setSurname(con.getSurname());
                conx.setName(con.getName());
                conx.setMittelname(con.getMittelname());
                conx.setTitle(con.getTitle());
                conx.setBirddate(con.getBirddate());
                conx.setPhone(con.getPhone());
                conx.setFax(con.getFax());
                conx.setMobile(con.getMobile());
                conx.setEmail(con.getEmail());
                conx.setDescription(con.getDescription());
                conx.setMaincontact(con.getMaincontact());

                // Save contact address
                if (!con.getCompanycontactaddresses().isEmpty()) {

                    final Iterator itx = con.getCompanycontactaddresses().iterator();

                    while (itx.hasNext()) {
                        final Companycontactaddress cadd = (Companycontactaddress) itx.next();

                        final Companycontactaddress adc = new Companycontactaddress();
                        adc.setCreateddate(new Date());
                        adc.setCreatedfrom(EBISystem.ebiUser);
                        adc.setAddresstype(cadd.getAddresstype());
                        adc.setCountry(cadd.getCountry());
                        adc.setLocation(cadd.getLocation());
                        adc.setStreet(cadd.getStreet());
                        adc.setPbox(cadd.getPbox());
                        adc.setZip(cadd.getZip());
                        adc.setCompanycontacts(conx);
                        conx.getCompanycontactaddresses().add(adc);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(adc);
                    }
                }

                EBISystem.getInstance().getDataStore("Contact", "ebiSave");
                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(conx);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                
                EBISystem.getInstance().getCompany().getCompanycontactses().add(conx);
                contactID = conx.getContactid();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return contactID;
    }

    public void dataEdit(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanycontactses().size() > 0) {

            for (Companycontacts objCnts : EBISystem.getInstance().getCompany().getCompanycontactses()) {
                if (objCnts.getContactid() == id) {
                    contact = objCnts;
                    break;
                }
            }

            EBISystem.gui().vpanel("Contact").setID(contact.getContactid());
            if (contact.getGender() != null) {
                EBISystem.gui().combo("genderTex", "Contact").setSelectedItem(contact.getGender());
            }

            EBISystem.gui().textField("surnameText", "Contact").setText(contact.getSurname());
            EBISystem.gui().textField("nameText", "Contact").setText(contact.getName());
            EBISystem.gui().textField("middleNameText", "Contact").setText(contact.getMittelname());
            EBISystem.gui().textField("positionText", "Contact").setText(contact.getPosition());
            EBISystem.gui().textField("titleText", "Contact").setText(contact.getTitle());

            if (contact.getBirddate() != null) {
                EBISystem.gui().timePicker("birthdateText", "Contact").setDate(contact.getBirddate());
                EBISystem.gui().timePicker("birthdateText", "Contact").getEditor().setValue(contact.getBirddate());
            }

            EBISystem.gui().textField("telefonText", "Contact").setText(contact.getPhone());
            EBISystem.gui().textField("faxText", "Contact").setText(contact.getFax());
            EBISystem.gui().textField("mobileText", "Contact").setText(contact.getMobile());
            EBISystem.gui().textField("emailText", "Contact").setText(contact.getEmail());
            EBISystem.gui().textArea("contactDescription", "Contact").setText(contact.getDescription());

            if (contact.getMaincontact() != null && contact.getMaincontact() == true) {
                EBISystem.gui().getCheckBox("mainContactText", "Contact").setSelected(true);
            }

            EBISystem.gui().vpanel("Contact").setCreatedDate(EBISystem.getInstance().getDateToString(contact.getCreateddate() == null ? new Date() : contact.getCreateddate()));
            EBISystem.gui().vpanel("Contact").setCreatedFrom(contact.getCreatedfrom() == null ? "" : contact.getCreatedfrom());

            if (contact.getChangeddate() != null) {
                EBISystem.gui().vpanel("Contact").setChangedDate(EBISystem.getInstance().getDateToString(contact.getChangeddate()));
                EBISystem.gui().vpanel("Contact").setChangedFrom(contact.getChangedfrom());
            } else {
                EBISystem.gui().vpanel("Contact").setChangedDate("");
                EBISystem.gui().vpanel("Contact").setChangedFrom("");
            }

            EBISystem.getInstance().getDataStore("Contact", "ebiEdit");
            
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                    .Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {
        if (EBISystem.getInstance().getCompany().getCompanycontactses().size() > 0) {
            for (Companycontacts objCnts : EBISystem.getInstance().getCompany().getCompanycontactses()) {
                if (objCnts.getContactid() == id) {
                    contact = objCnts;
                    break;
                }
            }

            if (contact != null) {
                EBISystem.getInstance().getCompany().getCompanycontactses().remove(contact);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(contact);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            }
        }
    }

    public void dataShow(Integer id) {
        
        int srow = EBISystem.gui().table("companyContacts", "Contact").getSelectedRow();
        final int size = EBISystem.getInstance().getCompany().getCompanycontactses().size();

        if (size > 0) {

            EBISystem.getModule().getContactPane().getTableModel().data = new Object[size][9];

            final Iterator<Companycontacts> itr = EBISystem.getInstance().getCompany().getCompanycontactses().iterator();
            int i = 0;
            String mainContactMarker;
            while (itr.hasNext()) {
                final Companycontacts obj = itr.next();
                if (obj.getMaincontact() != null && obj.getMaincontact() == true) {
                    mainContactMarker = "**";
                } else {
                    mainContactMarker = "";
                }
                EBISystem.getModule().getContactPane().getTableModel().data[i][0] = obj.getPosition() == null ? "" : obj.getPosition();
                EBISystem.getModule().getContactPane().getTableModel().data[i][1] = obj.getGender() == null ? "" : mainContactMarker + obj.getGender();
                EBISystem.getModule().getContactPane().getTableModel().data[i][2] = obj.getSurname() == null ? "" : obj.getSurname();
                EBISystem.getModule().getContactPane().getTableModel().data[i][3] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getContactPane().getTableModel().data[i][4] = obj.getPhone() == null ? "" : obj.getPhone();
                EBISystem.getModule().getContactPane().getTableModel().data[i][5] = obj.getMobile() == null ? "" : obj.getMobile();
                EBISystem.getModule().getContactPane().getTableModel().data[i][6] = obj.getEmail() == null ? "" : obj.getEmail();
                EBISystem.getModule().getContactPane().getTableModel().data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getModule().getContactPane().getTableModel().data[i][EBISystem.getModule().getContactPane().getTableModel().columnNames.length] = obj.getContactid();
                if(id != -1 && id == obj.getContactid()){
                    srow = EBISystem.gui().table("companyContacts", "Contact").convertRowIndexToView(i);
                }
                i++;
            }
        } else {
            EBISystem.getModule().getContactPane().getTableModel().data 
                    = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", ""}};
        }

        if (EBISystem.getModule().getCompanyPane() != null) {
            EBISystem.getModule().getCompanyPane().ctabModel.data = EBISystem.getModule().getContactPane().getTableModel().data;
            EBISystem.getModule().getCompanyPane().ctabModel.fireTableDataChanged();
        }

        EBISystem.getModule().getContactPane().getTableModel().fireTableDataChanged();
        if(srow > -1){
            EBISystem.gui().table("companyContacts", "Contact").changeSelection(srow, 0, false, false);
        }
    }

    public void dataNew() {
        contact = new Companycontacts();
        EBISystem.getModule().getContactPane().initialize(false);
        EBISystem.getInstance().getDataStore("Contact", "ebiNew");
        EBISystem.gui().vpanel("Contact").setID(-1);
    }

    private void dataHistory(final Company com) {

        final List<String> list = new ArrayList<String>();
        if (contact.getCreateddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                    + EBISystem.getInstance().getDateToString(contact.getCreateddate()));
        }
        if (contact.getCreatedfrom() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + contact.getCreatedfrom());
        }
        if (contact.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                    + EBISystem.getInstance().getDateToString(contact.getChangeddate()));
        }
        if (contact.getChangedfrom() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + contact.getChangedfrom());
        }
        if (contact.getGender() != null) {
            list.add(
                    EBISystem.i18n("EBI_LANG_C_GENDER") + ": "
                    + (contact.getGender()
                            .equals(EBISystem.gui().combo("genderTex", "Contact")
                                    .getSelectedItem().toString()) == true ? contact.getGender()
                            : contact.getGender() + "$"));
        }
        if (contact.getSurname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_SURNAME") + ": "
                    + (contact.getSurname()
                            .equals(EBISystem.gui().textField("surnameText", "Contact").getText()) == true
                    ? contact.getSurname()
                    : contact.getSurname() + "$"));
        }
        if (contact.getName() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_CNAME") + ": "
                    + (contact.getName()
                            .equals(EBISystem.gui().textField("nameText", "Contact").getText()) == true
                    ? contact.getName()
                    : contact.getName() + "$"));
        }
        if (contact.getMittelname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_MITTEL_NAME") + ": "
                    + (contact.getMittelname().equals(
                            EBISystem.gui().textField("middleNameText", "Contact").getText()) == true
                    ? contact.getMittelname()
                    : contact.getMittelname() + "$"));
        }
        if (contact.getPosition() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_POSITION") + ": "
                    + (contact.getPosition().equals(
                            EBISystem.gui().textField("positionText", "Contact").getText()) == true
                    ? contact.getPosition()
                    : contact.getPosition() + "$"));
        }
        if (contact.getTitle() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_TITLE") + ": "
                    + (contact.getTitle()
                            .equals(EBISystem.gui().textField("titleText", "Contact").getText()) == true
                    ? contact.getTitle()
                    : contact.getTitle() + "$"));
        }
        if (contact.getBirddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_BIRDDATE") + ": "
                    + (EBISystem.getInstance().getDateToString(contact.getBirddate())
                            .equals(EBISystem.gui().timePicker("birthdateText", "Contact").getEditor()
                                    .getText()) == true ? EBISystem.getInstance().getDateToString(contact.getBirddate())
                            : EBISystem.getInstance().getDateToString(contact.getBirddate()) + "$"));
        }
        if (contact.getPhone() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": "
                    + (contact.getPhone()
                            .equals(EBISystem.gui().textField("telefonText", "Contact").getText()) == true
                    ? contact.getPhone()
                    : contact.getPhone() + "$"));
        }
        if (contact.getFax() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_FAX") + ": "
                    + (contact.getFax()
                            .equals(EBISystem.gui().textField("faxText", "Contact").getText()) == true
                    ? contact.getFax()
                    : contact.getFax() + "$"));
        }
        if (contact.getMobile() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ": "
                    + (contact.getMobile()
                            .equals(EBISystem.gui().textField("mobileText", "Contact").getText()) == true
                    ? contact.getMobile()
                    : contact.getMobile() + "$"));
        }
        if (contact.getEmail() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_EMAIL") + ": "
                    + (contact.getEmail()
                            .equals(EBISystem.gui().textField("emailText", "Contact").getText()) == true
                    ? contact.getEmail()
                    : contact.getEmail() + "$"));
        }
        if (contact.getDescription() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": "
                    + (contact.getDescription().equals(
                            EBISystem.gui().textArea("contactDescription", "Contact").getText()) == true
                    ? contact.getDescription()
                    : contact.getDescription() + "$"));
        }
        list.add("*EOR*"); // END OF RECORD

        if (!contact.getCompanycontactaddresses().isEmpty()) {

            final Iterator iter = contact.getCompanycontactaddresses().iterator();
            while (iter.hasNext()) {
                final Companycontactaddress obj = (Companycontactaddress) iter.next();
                if (obj.getAddresstype() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_C_ADRESS_TYPE") + ": " + obj.getAddresstype() == null ? ""
                            : EBISystem.i18n("EBI_LANG_C_ADRESS_TYPE") + ": " + obj.getAddresstype());
                }
                if (obj.getStreet() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": " + obj.getStreet() == null ? ""
                            : EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": " + obj.getStreet());
                }
                if (obj.getZip() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + obj.getZip() + " "
                            + obj.getLocation() == null ? ""
                            : EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + obj.getZip() + " "
                            + obj.getLocation());
                }
                if (obj.getPbox() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": " + obj.getPbox() == null ? ""
                            : EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": " + obj.getPbox());
                }
                if (obj.getCountry() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": " + obj.getCountry() == null ? ""
                            : EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": " + obj.getCountry());
                }
                list.add("*EOR*"); // END OF RECORD
            }
        }

        try {
            EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Contact", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void showCompanyContactAddress() {
        if (contact.getCompanycontactaddresses().size() > 0) {
            EBISystem.getModule().getContactPane().getAddressModel().data = new Object[this.contact.getCompanycontactaddresses().size()][7];
            final Iterator itr = this.contact.getCompanycontactaddresses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companycontactaddress obj = (Companycontactaddress) itr.next();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][0] = obj.getAddresstype() == null ? "" : obj.getAddresstype();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][1] = obj.getStreet() == null ? "" : obj.getStreet();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][2] = obj.getZip() == null ? "" : obj.getZip();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][3] = obj.getLocation() == null ? "" : obj.getLocation();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][4] = obj.getPbox() == null ? "" : obj.getPbox();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][5] = obj.getCountry() == null ? "" : obj.getCountry();
                EBISystem.getModule().getContactPane().getAddressModel().data[i][6] = obj.getAddressid();
                i++;
            }
        } else {
            EBISystem.getModule().getContactPane().getAddressModel().data
                    = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
        }

        EBISystem.getModule().getContactPane().getAddressModel().fireTableDataChanged();
    }

    public void dataAddressDelete(final int id) {
        final Iterator iter = this.contact.getCompanycontactaddresses().iterator();
        Companycontactaddress contactAddress = null;
        while (iter.hasNext()) {
            final Companycontactaddress address = (Companycontactaddress) iter.next();
            if (address.getAddressid() == id) {
                contactAddress = address;
                break;
            }
        }
        if (contactAddress != null) {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").delete(contactAddress);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            contact.getCompanycontactaddresses().remove(contactAddress);
        }
    }

    public Companycontacts getContact() {
        return contact;
    }

    public Set<Companycontacts> getContactList() {
        return EBISystem.getInstance().getCompany().getCompanycontactses();
    }

    public Set<Companycontactaddress> getCoaddressList() {
        return contact.getCompanycontactaddresses();
    }
}
