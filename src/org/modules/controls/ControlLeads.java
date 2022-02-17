package org.modules.controls;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companyaddress;
import org.sdk.model.hibernate.Companycontacts;
import org.hibernate.query.Query;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

public class ControlLeads {

    private Company company = null;
    private Companycontacts contact = null;
    private Companyaddress address = null;
    public boolean isEdit = false;

    public Integer dataStore() {
        Integer leadID = -1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == true) {
                company.setChangeddate(new Date());
                contact.setChangeddate(new Date());
                address.setChangeddate(new Date());
                company.setChangedfrom(EBISystem.ebiUser);
                contact.setChangedfrom(EBISystem.ebiUser);
                address.setChangedfrom(EBISystem.ebiUser);
            } else {
                company = new Company();
                contact = new Companycontacts();
                address = new Companyaddress();
                company.setCreateddate(new Date());
                contact.setCreateddate(new Date());
                address.setCreateddate(new Date());
                company.setCreatedfrom(EBISystem.ebiUser);
                contact.setCreatedfrom(EBISystem.ebiUser);
                address.setCreatedfrom(EBISystem.ebiUser);
                isEdit = true;
            }

            company.setName(EBISystem.builder().textField("compNameText", "Leads").getText());
            if (!isEdit) {
                company.setCategory("Leads");
                company.setCustomernr("-1");
                company.setCompanynumber(-1);
                company.setBeginchar("");
                company.setCooperation("");
                company.setIslock(false);
                company.setIsactual(false);
            }
            company.setWeb(EBISystem.builder().textField("internetText", "Leads").getText());

            if (EBISystem.builder().combo("classificationText", "Leads").getEditor().getItem() != null) {
                company.setQualification(EBISystem.builder().combo("classificationText", "Leads")
                        .getEditor().getItem().toString());
            }

            company.setDescription(EBISystem.builder().textArea("descriptionText", "Leads").getText());

            contact.setCompany(company);
            if (EBISystem.builder().combo("genderText", "Leads").getEditor().getItem() != null) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(
                        EBISystem.builder().combo("genderText", "Leads").getEditor().getItem().toString())) {
                    contact.setGender(
                            EBISystem.builder().combo("genderText", "Leads").getEditor().getItem().toString());
                }
            }

            contact.setTitle(EBISystem.builder().textField("titleText", "Leads").getText());
            contact.setPosition(EBISystem.builder().textField("positionText", "Leads").getText());
            contact.setName(EBISystem.builder().textField("contactNameText", "Leads").getText());
            contact.setSurname(EBISystem.builder().textField("contactSurnameText", "Leads").getText());
            contact.setPhone(EBISystem.builder().textField("telephoneText", "Leads").getText());
            contact.setMobile(EBISystem.builder().textField("contactMobileText", "Leads").getText());
            contact.setFax(EBISystem.builder().textField("faxText", "Leads").getText());
            contact.setEmail(EBISystem.builder().textField("emailText", "Leads").getText());
            address.setAddresstype(address.getAddresstype() == null ? "" : address.getAddresstype());
            address.setCompany(company);
            address.setZip(EBISystem.builder().textField("addressZipText", "Leads").getText());
            address.setLocation(EBISystem.builder().textField("addressCityText", "Leads").getText());
            address.setStreet(EBISystem.builder().textField("addressStrNrText", "Leads").getText());
            address.setCountry(EBISystem.builder().textField("addressCountryText", "Leads").getText());

            company.getCompanyaddresses().add(address);
            company.getCompanycontactses().add(contact);

            // Fill Visitcard
            EBISystem.builder().label("compNameLabel", "Leads").setText(company.getName() == null ? "" : company.getName());

            String cName = contact.getGender() == null ? "" : contact.getGender() + " ";
            cName += contact.getTitle() == null ? "" : contact.getTitle() + " ";
            cName += contact.getName() == null ? "" : contact.getName() + " ";
            cName += contact.getSurname() == null ? "" : contact.getSurname();
            EBISystem.builder().label("cName", "Leads").setText(cName);

            EBISystem.builder().label("addressLabel", "Leads").setText(address.getStreet() == null ? "" : address.getStreet());

            EBISystem.builder().label("zipLocationLabel", "Leads").setText(address.getZip() == null ? "" : address.getZip() + " " + address.getLocation() == null ? "" : address.getLocation());

            EBISystem.builder().label("phoneLabel", "Leads").setText(contact.getPhone() == null ? "" : EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": " + contact.getPhone());
            EBISystem.builder().label("faxLabel", "Leads").setText(contact.getPhone() == null ? "" : EBISystem.i18n("EBI_LANG_FAX") + ": " + contact.getPhone());
            EBISystem.builder().label("positionLabel", "Leads").setText(contact.getPosition() == null ? "" : contact.getPosition());
            EBISystem.builder().label("webLabel", "Leads").setText(company.getWeb() == null ? "" : company.getWeb());
            EBISystem.builder().label("mobileLabel", "Leads").setText(contact.getMobile() == null ? "" : EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ": " + contact.getMobile());
            EBISystem.builder().label("emailLabel", "Leads").setText(contact.getEmail() == null ? "" : contact.getEmail());

            EBISystem.getInstance().getDataStore("Leads", "ebiSave");
            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(company);

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(address);
            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(contact);

            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            if (!isEdit) {
                EBISystem.builder().vpanel("Leads").setID(company.getCompanyid());
            }
            leadID = company.getCompanyid();
            isEdit = true;
        } catch (final Exception ex) {
            EBISystem.hibernate().session("EBICRM_SESSION").clear();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return leadID;
    }

    public Integer dataCopy(final int id) {
        Integer leadID = -1;
        Query query;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();

            query = EBISystem.hibernate().session("EBICRM_SESSION")
                    .createQuery("from Company where companyid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                final Company cmp = (Company) iter.next();
                final Company cp = new Company();
                cp.setCreateddate(new Date());
                cp.setCreatedfrom(EBISystem.ebiUser);

                cp.setName(cmp.getName() + " - (Copy)");
                cp.setCategory(cmp.getCategory());
                cp.setCustomernr(cmp.getCustomernr());
                cp.setCompanynumber(cmp.getCompanynumber());
                cp.setBeginchar(cmp.getBeginchar());
                cp.setCooperation(cmp.getCooperation());
                cp.setIslock(cmp.getIslock());
                cp.setIsactual(false);

                cp.setWeb(cmp.getWeb());
                cp.setQualification(cmp.getQualification());
                cp.setDescription(cmp.getDescription());
                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(cp);

                // Create new copy of the contact
                final Iterator ico = cmp.getCompanycontactses().iterator();
                while (ico.hasNext()) {
                    final Companycontacts coc = (Companycontacts) ico.next();

                    final Companycontacts co = new Companycontacts();
                    co.setCompany(cp);
                    co.setCreateddate(new Date());
                    co.setCreatedfrom(EBISystem.ebiUser);
                    co.setGender(coc.getGender());
                    co.setTitle(coc.getTitle());
                    co.setPosition(coc.getPosition());
                    co.setName(coc.getName());
                    co.setSurname(coc.getSurname());
                    co.setPhone(coc.getPhone());
                    co.setMobile(coc.getMobile());
                    co.setFax(coc.getFax());
                    co.setEmail(coc.getEmail());
                    cp.getCompanycontactses().add(co);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(co);
                }

                final Iterator iad = cmp.getCompanyaddresses().iterator();
                while (iad.hasNext()) {
                    final Companyaddress adc = (Companyaddress) iad.next();
                    final Companyaddress ad = new Companyaddress();
                    ad.setCreateddate(new Date());
                    ad.setCreatedfrom(EBISystem.ebiUser);
                    ad.setAddresstype(adc.getAddresstype());
                    ad.setCompany(cp);
                    ad.setZip(adc.getZip());
                    ad.setLocation(adc.getLocation());
                    ad.setStreet(adc.getStreet());
                    ad.setCountry(adc.getCountry());
                    cp.getCompanyaddresses().add(ad);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(ad);
                }

                EBISystem.getInstance().getDataStore("Leads", "ebiSave");
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                leadID = cp.getCompanyid();
            }

        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return leadID;
    }

    public void dataEdit(final int id) {

        Query query;
        try {

            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            query = EBISystem.hibernate().session("EBICRM_SESSION").createQuery("from Company where companyid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                company = (Company) iter.next();
                EBISystem.builder().vpanel("Leads").setID(company.getCompanyid());
                EBISystem.hibernate().session("EBICRM_SESSION").refresh(company);
                final Iterator cit = company.getCompanycontactses().iterator();
                while (cit.hasNext()) {
                    contact = (Companycontacts) cit.next();
                    if (Integer.parseInt(EBISystem.getModule().getLeadPane()
                            .getTabModel().data[EBISystem.getModule().
                                getLeadPane().getSelectedRow()][12].toString()) == contact.getContactid()) {

                        String cName = contact.getGender() == null ? "" : contact.getGender() + " ";
                        cName += contact.getTitle() == null ? "" : contact.getTitle() + " ";
                        cName += contact.getName() == null ? "" : contact.getName() + " ";
                        cName += contact.getSurname() == null ? "" : contact.getSurname();

                        EBISystem.builder().label("cName", "Leads").setText(cName);
                        EBISystem.builder().combo("genderText", "Leads").setSelectedItem(contact.getGender() == null ? EBISystem.i18n("EBI_LANG_PLEASE_SELECT") : contact.getGender());
                        EBISystem.builder().textField("titleText", "Leads").setText(contact.getTitle() == null ? "" : contact.getTitle());
                        EBISystem.builder().textField("positionText", "Leads").setText(contact.getPosition() == null ? "" : contact.getPosition());
                        EBISystem.builder().label("positionLabel", "Leads").setText(contact.getPosition() == null ? "" : contact.getPosition());
                        EBISystem.builder().textField("contactNameText", "Leads").setText(contact.getName() == null ? "" : contact.getName());
                        EBISystem.builder().textField("contactSurnameText", "Leads").setText(contact.getSurname() == null ? "" : contact.getSurname());
                        EBISystem.builder().textField("telephoneText", "Leads").setText(contact.getPhone() == null ? "" : contact.getPhone());
                        EBISystem.builder().label("phoneLabel", "Leads").setText(contact.getPhone() == null ? "" : EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": " + contact.getPhone());
                        EBISystem.builder().textField("faxText", "Leads").setText(contact.getFax() == null ? "" : contact.getFax());
                        EBISystem.builder().label("faxLabel", "Leads").setText(contact.getPhone() == null ? "" : EBISystem.i18n("EBI_LANG_FAX") + ": " + contact.getPhone());
                        EBISystem.builder().textField("emailText", "Leads").setText(contact.getEmail() == null ? "" : contact.getEmail());
                        EBISystem.builder().label("emailLabel", "Leads").setText(contact.getEmail() == null ? "" : contact.getEmail());
                        EBISystem.builder().textField("contactMobileText", "Leads").setText(contact.getMobile() == null ? "" : contact.getMobile());
                        EBISystem.builder().label("mobileLabel", "Leads").setText(contact.getMobile() == null ? "" : EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ": " + contact.getMobile());
                        break;
                    }
                }

                final Iterator ait = company.getCompanyaddresses().iterator();
                while (ait.hasNext()) {

                    address = (Companyaddress) ait.next();

                    if (Integer.parseInt(EBISystem.getModule().getLeadPane().
                            getTabModel().data[EBISystem.getModule().
                                    getLeadPane().getSelectedRow()][13].toString()) == address.getAddressid()) {

                        EBISystem.builder().label("addressLabel", "Leads").setText(address.getStreet() == null ? "" : address.getStreet());
                        EBISystem.builder().textField("addressStrNrText", "Leads").setText(address.getStreet() == null ? "" : address.getStreet());

                        String zipLocation = address.getZip() == null ? "" : address.getZip() + " ";
                        zipLocation += address.getLocation() == null ? "" : address.getLocation();
                        EBISystem.builder().label("zipLocationLabel", "Leads").setText(zipLocation);

                        EBISystem.builder().textField("addressZipText", "Leads").setText(address.getZip() == null ? "" : address.getZip());
                        EBISystem.builder().textField("addressCityText", "Leads").setText(address.getLocation() == null ? "" : address.getLocation());
                        EBISystem.builder().textField("addressCountryText", "Leads").setText(address.getCountry() == null ? "" : address.getCountry());
                        break;
                    }
                }

                EBISystem.builder().vpanel("Leads").setCreatedDate(EBISystem.getInstance().getDateToString(company.getCreateddate()));
                EBISystem.builder().vpanel("Leads").setCreatedFrom(company.getCreatedfrom());

                if (company.getCreateddate() != null) {
                    EBISystem.builder().vpanel("Leads").setChangedDate(EBISystem.getInstance().getDateToString(company.getChangeddate()));
                    EBISystem.builder().vpanel("Leads").setChangedFrom(company.getChangedfrom());
                }

                EBISystem.builder().label("compNameLabel", "Leads").setText(company.getName() == null ? "" : company.getName());
                EBISystem.builder().textField("compNameText", "Leads").setText(company.getName() == null ? "" : company.getName());
                EBISystem.builder().textField("internetText", "Leads").setText(company.getWeb() == null ? "" : company.getWeb());
                EBISystem.builder().combo("classificationText", "Leads").setSelectedItem(company.getQualification() == null ? "" : company.getQualification());
                EBISystem.builder().textArea("descriptionText", "Leads").setText(company.getDescription() == null ? "" : company.getDescription());
                EBISystem.builder().label("webLabel", "Leads").setText(company.getWeb() == null ? "" : company.getWeb());
                isEdit = true;

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }

            EBISystem.getInstance().getDataStore("Leads", "ebiEdit");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            EBISystem.builder().getPanel("businessCard", "Leads").updateUI();

        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
            isEdit = false;
        }
    }

    public boolean dataDelete(final int id) {

        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            final Query query = EBISystem.hibernate().session("EBICRM_SESSION").createQuery("from Company where companyid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                final Company comp = (Company) iter.next();
                EBISystem.getInstance().getDataStore("Leads", "ebiDelete");
                EBISystem.hibernate().session("EBICRM_SESSION").delete(comp);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            }

        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void dataShow(int id) {

        ResultSet set = null;
        PreparedStatement ps1 = null;
        int selRow = EBISystem.builder().table("leadsTable", "Leads").getSelectedRow() + id;
        try {
            ps1 = EBISystem.getInstance().iDB().initPreparedStatement(""
                    + " SELECT COMPANY.COMPANYID,COMPANY.NAME,COMPANY.CATEGORY,COMPANY.WEB,COMPANY.QUALIFICATION,COMPANY.DESCRIPTION,COMPANYCONTACTS.GENDER,"
                    + " COMPANYCONTACTS.CONTACTID,COMPANYCONTACTS.TITLE,COMPANYCONTACTS.SURNAME,COMPANYCONTACTS.NAME,COMPANYCONTACTS.POSITION,COMPANYCONTACTS.PHONE,COMPANYCONTACTS.FAX,COMPANYCONTACTS.MOBILE,COMPANYCONTACTS.EMAIL,"
                    + " COMPANYADDRESS.ADDRESSID, COMPANYADDRESS.STREET,COMPANYADDRESS.ZIP,COMPANYADDRESS.LOCATION,COMPANYADDRESS.COUNTRY"
                    + " FROM COMPANY LEFT JOIN COMPANYCONTACTS  ON  "
                    + " COMPANYCONTACTS.COMPANYID=COMPANY.COMPANYID LEFT JOIN COMPANYADDRESS ON COMPANYADDRESS.COMPANYID=COMPANY.COMPANYID ");

            // ps1.setString(1,"Leads");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            if (set != null) {
                set.last();
                EBISystem.getModule().getLeadPane().getTabModel().data = new Object[set.getRow()][14];

                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][0] = set.getString("COMPANY.NAME") == null ? "" : set.getString("COMPANY.NAME");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][1] = set.getString("COMPANYCONTACTS.GENDER") == null ? "" : set.getString("COMPANYCONTACTS.GENDER");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][2] = set.getString("COMPANYCONTACTS.POSITION") == null ? "" : set.getString("COMPANYCONTACTS.POSITION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][3] = set.getString("COMPANYCONTACTS.NAME") == null ? "" : set.getString("COMPANYCONTACTS.NAME");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][4] = set.getString("COMPANYCONTACTS.SURNAME") == null ? "" : set.getString("COMPANYCONTACTS.SURNAME");
                        String zipLoc = set.getString("COMPANYADDRESS.ZIP") == null ? "" : set.getString("COMPANYADDRESS.ZIP") + " ";
                        zipLoc += set.getString("COMPANYADDRESS.LOCATION") == null ? "" : set.getString("COMPANYADDRESS.LOCATION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][5] = zipLoc;
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][6] = set.getString("COMPANYADDRESS.COUNTRY") == null ? "" : set.getString("COMPANYADDRESS.COUNTRY");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][7] = set.getString("COMPANYCONTACTS.PHONE") == null ? "" : set.getString("COMPANYCONTACTS.PHONE");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][8] = set.getString("COMPANYCONTACTS.MOBILE") == null ? "" : set.getString("COMPANYCONTACTS.MOBILE");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][9] = set.getString("COMPANYCONTACTS.EMAIL") == null ? "" : set.getString("COMPANYCONTACTS.EMAIL");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][10] = set.getString("COMPANY.QUALIFICATION") == null ? "" : set.getString("COMPANY.QUALIFICATION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][11] = set.getInt("COMPANY.COMPANYID") == 0 ? 0 : set.getInt("COMPANY.COMPANYID");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][12] = set.getString("COMPANYCONTACTS.CONTACTID") == null ? "" : set.getString("COMPANYCONTACTS.CONTACTID");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][13] = set.getString("COMPANYADDRESS.ADDRESSID") == null ? "" : set.getString("COMPANYADDRESS.ADDRESSID");
                        if (id != -1 && id == set.getInt("COMPANY.COMPANYID")) {
                            selRow = i;
                        }
                        i++;
                    }
                } else {
                    EBISystem.getModule().getLeadPane().getTabModel().data = new Object[][]{
                        {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", "", "", ""}};
                }
            } else {
                EBISystem.getModule().getLeadPane().getTabModel().data = new Object[][]{
                    {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", "", "", ""}};
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            EBISystem.getModule().getLeadPane().getTabModel().fireTableDataChanged();
            if (set != null) {
                try {
                    set.close();
                    ps1.close();
                } catch (final SQLException e) {
                    EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }

        if (selRow > -1) {
            selRow = EBISystem.builder().table("leadsTable", "Leads").convertRowIndexToView(selRow);
            EBISystem.builder().table("leadsTable", "Leads").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataShow(String searchText) {
        ResultSet set = null;
        PreparedStatement ps1 = null;

        final int srow = EBISystem.builder().table("leadsTable", "Leads").getSelectedRow();
        try {
            EBISystem.builder().vpanel("Leads").setCursor(new Cursor(Cursor.WAIT_CURSOR));
            ps1 = EBISystem.getInstance().iDB().initPreparedStatement(""
                    + " SELECT COMPANY.COMPANYID,COMPANY.NAME,COMPANY.COOPERATION,COMPANY.CATEGORY,COMPANY.WEB,COMPANY.QUALIFICATION,COMPANY.DESCRIPTION,COMPANYCONTACTS.GENDER,"
                    + " COMPANYCONTACTS.CREATEDDATE, COMPANYCONTACTS.CONTACTID,COMPANYCONTACTS.TITLE,COMPANYCONTACTS.SURNAME,COMPANYCONTACTS.NAME,COMPANYCONTACTS.POSITION,COMPANYCONTACTS.PHONE,COMPANYCONTACTS.FAX,COMPANYCONTACTS.MOBILE,COMPANYCONTACTS.EMAIL,"
                    + " COMPANYADDRESS.ADDRESSID, COMPANYADDRESS.STREET,COMPANYADDRESS.ZIP,COMPANYADDRESS.LOCATION,COMPANYADDRESS.COUNTRY"
                    + " FROM COMPANY LEFT JOIN COMPANYCONTACTS ON  "
                    + " COMPANYCONTACTS.COMPANYID=COMPANY.COMPANYID LEFT JOIN COMPANYADDRESS ON COMPANYADDRESS.COMPANYID=COMPANY.COMPANYID "
                    + " WHERE COMPANY.NAME LIKE ? OR COMPANY.CATEGORY LIKE ? OR COMPANY.COOPERATION LIKE ? OR COMPANY.QUALIFICATION LIKE ? OR COMPANY.DESCRIPTION LIKE ? OR COMPANY.WEB LIKE ? OR COMPANYCONTACTS.GENDER LIKE ? "
                    + " OR COMPANYCONTACTS.TITLE LIKE ? OR COMPANYCONTACTS.SURNAME LIKE ? OR COMPANYCONTACTS.NAME LIKE ? OR COMPANYCONTACTS.POSITION LIKE ? OR "
                    + " COMPANYCONTACTS.FAX LIKE ? OR COMPANYCONTACTS.MOBILE LIKE ? OR COMPANYCONTACTS.EMAIL LIKE ? OR"
                    + " COMPANYADDRESS.STREET LIKE ? OR COMPANYADDRESS.ZIP LIKE ? OR COMPANYADDRESS.LOCATION LIKE ? OR COMPANYADDRESS.COUNTRY LIKE ? ORDER BY COMPANYCONTACTS.CREATEDDATE DESC ");

            searchText = "%" + searchText + "%";
            ps1.setString(1, searchText);
            ps1.setString(2, searchText);
            ps1.setString(3, searchText);
            ps1.setString(4, searchText);
            ps1.setString(5, searchText);
            ps1.setString(6, searchText);
            ps1.setString(7, searchText);
            ps1.setString(8, searchText);
            ps1.setString(9, searchText);
            ps1.setString(10, searchText);
            ps1.setString(11, searchText);
            ps1.setString(12, searchText);
            ps1.setString(13, searchText);
            ps1.setString(14, searchText);
            ps1.setString(15, searchText);
            ps1.setString(16, searchText);
            ps1.setString(17, searchText);
            ps1.setString(18, searchText);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            if (set != null) {
                set.last();
                EBISystem.getModule().getLeadPane().getTabModel().data = new Object[set.getRow()][14];

                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][0] = set
                                .getString("COMPANY.NAME") == null ? "" : set.getString("COMPANY.NAME");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][1] = set
                                .getString("COMPANYCONTACTS.GENDER") == null ? ""
                                : set.getString("COMPANYCONTACTS.GENDER");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][2] = set
                                .getString("COMPANYCONTACTS.POSITION") == null ? ""
                                : set.getString("COMPANYCONTACTS.POSITION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][3] = set
                                .getString("COMPANYCONTACTS.NAME") == null ? "" : set.getString("COMPANYCONTACTS.NAME");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][4] = set
                                .getString("COMPANYCONTACTS.SURNAME") == null ? ""
                                : set.getString("COMPANYCONTACTS.SURNAME");
                        String zipLoc = set.getString("COMPANYADDRESS.ZIP") == null ? ""
                                : set.getString("COMPANYADDRESS.ZIP") + " ";
                        zipLoc += set.getString("COMPANYADDRESS.LOCATION") == null ? ""
                                : set.getString("COMPANYADDRESS.LOCATION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][5] = zipLoc;
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][6] = set
                                .getString("COMPANYADDRESS.COUNTRY") == null ? ""
                                : set.getString("COMPANYADDRESS.COUNTRY");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][7] = set
                                .getString("COMPANYCONTACTS.PHONE") == null ? ""
                                : set.getString("COMPANYCONTACTS.PHONE");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][8] = set
                                .getString("COMPANYCONTACTS.MOBILE") == null ? ""
                                : set.getString("COMPANYCONTACTS.MOBILE");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][9] = set
                                .getString("COMPANYCONTACTS.EMAIL") == null ? ""
                                : set.getString("COMPANYCONTACTS.EMAIL");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][10] = set
                                .getString("COMPANY.QUALIFICATION") == null ? ""
                                : set.getString("COMPANY.QUALIFICATION");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][11] = set
                                .getString("COMPANY.COMPANYID") == null ? "" : set.getString("COMPANY.COMPANYID");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][12] = set
                                .getString("COMPANYCONTACTS.CONTACTID") == null ? ""
                                : set.getString("COMPANYCONTACTS.CONTACTID");
                        EBISystem.getModule().getLeadPane().getTabModel().data[i][13] = set
                                .getString("COMPANYADDRESS.ADDRESSID") == null ? ""
                                : set.getString("COMPANYADDRESS.ADDRESSID");
                        i++;
                    }
                } else {
                    EBISystem.getModule().getLeadPane().getTabModel().data = new Object[][]{
                        {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", "",
                            "", ""}};
                }
            } else {
                EBISystem.getModule().getLeadPane().getTabModel().data = new Object[][]{
                    {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", "", "",
                        ""}};
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            EBISystem.getModule().getLeadPane().getTabModel().fireTableDataChanged();
            if (set != null) {
                try {
                    set.close();
                    ps1.close();
                } catch (final SQLException e) {
                    EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
            EBISystem.builder().vpanel("Leads").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        EBISystem.builder().table("leadsTable", "Leads").changeSelection(srow, 0, false, false);
    }

    public void dataNew(boolean reload) {
        company = new Company();
        contact = new Companycontacts();
        address = new Companyaddress();
        isEdit = false;
        EBISystem.getModule().getLeadPane().initialize(reload);
        EBISystem.getInstance().getDataStore("Leads", "ebiNew");
    }
}
