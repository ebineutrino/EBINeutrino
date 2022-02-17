package org.modules.controls;

import org.sdk.model.hibernate.Companyofferreceiver;
import org.sdk.model.hibernate.Companyopporunitydocs;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companyopportunitycontact;
import org.sdk.model.hibernate.Companyopportunity;
import org.sdk.model.hibernate.Companyofferdocs;
import org.modules.views.dialogs.EBIMeetingAddContactDialog;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class ControlOpportunity {

    private Companyopportunity opportunity = null;
    private String evalStatus = "";
    private String budgetStatus = "";
    private String purchState = "";
    private String sStatus = "";
    public boolean isEdit = false;

    public ControlOpportunity() {
        opportunity = new Companyopportunity();
    }

    public Integer dataStore() {

        Integer opportunityID = -1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == false) {
                opportunity.setCreatedfrom(EBISystem.builder().vpanel("Opportunity").getCreatedFrom());
                opportunity.setCreateddate(new Date());
                opportunity.setCompany(EBISystem.getInstance().getCompany());
            } else {
                createHistory(EBISystem.getInstance().getCompany());
                opportunity.setChangeddate(new Date());
                opportunity.setChangedfrom(EBISystem.ebiUser);
            }

            opportunity.setName(EBISystem.builder().combo("opportunityNameText", "Opportunity").getEditor().getItem().toString());

            if (EBISystem.builder().combo("oppEvalStatusText", "Opportunity").getEditor().getItem() != null) {
                opportunity.setEvaluationstatus(EBISystem.builder().combo("oppEvalStatusText", "Opportunity").getEditor().getItem().toString());
                opportunity.setEvaluetiondate(new java.sql.Date(new Date().getTime()));
            }

            if (EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getEditor().getItem() != null) {
                opportunity.setBudgetstatus(EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getEditor().getItem().toString());
                opportunity.setBudgetdate(new java.sql.Date(new Date().getTime()));
            }

            opportunity.setSalestage(EBISystem.builder().combo("oppSaleStateText", "Opportunity").getEditor().getItem().toString());
            opportunity.setSalestagedate(new java.sql.Date(new Date().getTime()));

            if (EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getEditor().getItem() != null) {
                opportunity.setOpportunitystatus(EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getEditor().getItem().toString());
                opportunity.setOpportunitystatusdate(new java.sql.Date(new Date().getTime()));
            }

            if (EBISystem.builder().combo("oppProbabilityText", "Opportunity").getEditor().getItem() != null) {
                opportunity.setProbability(EBISystem.builder().combo("oppProbabilityText", "Opportunity").getEditor().getItem().toString());
            }

            if (EBISystem.builder().combo("oppBustypeText", "Opportunity").getEditor().getItem() != null) {
                opportunity.setBusinesstype(EBISystem.builder().combo("oppBustypeText", "Opportunity").getEditor().getItem().toString());
            }

            if (!"".equals(EBISystem.builder().FormattedField("oppValueText", "Opportunity").getText())) {
                try {
                    opportunity.setOpportunityvalue(Double.parseDouble(EBISystem.builder().FormattedField("oppValueText", "Opportunity").getValue().toString()));
                } catch (final NumberFormatException ex) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
                    return opportunityID;
                }
            }

            opportunity.setIsclose(EBISystem.builder().getCheckBox("closeOpportunity", "Opportunity").isSelected());
            if (EBISystem.builder().getCheckBox("closeOpportunity", "Opportunity").isSelected()) {
                opportunity.setClosedate(new java.sql.Date(new Date().getTime()));
            }
            opportunity.setDescription(EBISystem.builder().textArea("opportunityDescription", "Opportunity").getText());

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(opportunity);
            //Save contacts
            if (!opportunity.getCompanyopportunitycontacts().isEmpty()) {
                final Iterator iter = opportunity.getCompanyopportunitycontacts().iterator();
                while (iter.hasNext()) {
                    final Companyopportunitycontact contact = (Companyopportunitycontact) iter.next();
                    if (contact.getOpportunitycontactid() != null && contact.getOpportunitycontactid() < 0) {
                        contact.setOpportunitycontactid(null);
                    }
                    contact.setCompanyopportunity(opportunity);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(contact);
                }
            }

            //Save docs
            if (!this.opportunity.getCompanyopporunitydocses().isEmpty()) {
                final Iterator iter = opportunity.getCompanyopporunitydocses().iterator();
                while (iter.hasNext()) {
                    final Companyopporunitydocs doc = (Companyopporunitydocs) iter.next();
                    if (doc.getDocid() != null && doc.getDocid() < 0) {
                        doc.setDocid(null);
                    }
                    doc.setCompanyopportunity(opportunity);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(doc);
                }
            }
            EBISystem.getInstance().getDataStore("Opportunity", "ebiSave");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            EBISystem.getInstance().getCompany().getCompanyopportunities().add(opportunity);

            if (!isEdit) {
                EBISystem.builder().vpanel("Opportunity").setID(opportunity.getOpportunityid());
            }
            opportunityID = opportunity.getOpportunityid();
            isEdit = true;
        } catch (final Exception ex) {
            EBISystem.hibernate().session("EBICRM_SESSION").clear();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return opportunityID;
    }

    public Integer dataCopy(final int id) {

        Integer oppID = -1;
        try {

            if (EBISystem.getInstance().getCompany().getCompanyopportunities().size() > 0) {

                Companyopportunity opp = null;
                for (Companyopportunity oprtObj : EBISystem.getInstance().getCompany().getCompanyopportunities()) {
                    if (oprtObj.getOpportunityid() == id) {
                        opp = oprtObj;
                        break;
                    }
                }

                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Companyopportunity opnew = new Companyopportunity();

                opnew.setCreateddate(new Date());
                opnew.setCreatedfrom(EBISystem.ebiUser);

                opnew.setCompany(opp.getCompany());
                opnew.setName(opp.getName() + " - (Copy)");

                opnew.setEvaluationstatus(opp.getEvaluationstatus());
                opnew.setEvaluetiondate(opp.getEvaluetiondate());
                opnew.setBudgetstatus(opp.getBudgetstatus());
                opnew.setBudgetdate(opp.getBudgetdate());
                opnew.setSalestage(opp.getSalestage());
                opnew.setSalestagedate(opp.getSalestagedate());

                opnew.setOpportunitystatus(opp.getOpportunitystatus());
                opnew.setOpportunitystatusdate(opp.getOpportunitystatusdate());

                opnew.setProbability(opp.getProbability());
                opnew.setBusinesstype(opp.getBusinesstype());

                opnew.setOpportunityvalue(opp.getOpportunityvalue());
                opnew.setIsclose(opp.getIsclose());
                opnew.setClosedate(opp.getClosedate());
                opnew.setDescription(opp.getDescription());
                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(opnew);

                //Save contacts
                if (!opp.getCompanyopportunitycontacts().isEmpty()) {
                    final Iterator itc = opp.getCompanyopportunitycontacts().iterator();
                    while (itc.hasNext()) {
                        final Companyopportunitycontact contact = (Companyopportunitycontact) itc.next();

                        final Companyopportunitycontact cd = new Companyopportunitycontact();
                        cd.setBirddate(contact.getBirddate());
                        cd.setCompanyopportunity(opnew);
                        cd.setCountry(contact.getCountry());
                        cd.setCreateddate(new Date());
                        cd.setCreatedfrom(EBISystem.ebiUser);
                        cd.setDescription(contact.getDescription());
                        cd.setEmail(contact.getEmail());
                        cd.setFax(contact.getFax());
                        cd.setGender(contact.getGender());
                        cd.setLocation(contact.getLocation());
                        cd.setMittelname(contact.getMittelname());
                        cd.setMobile(contact.getMobile());
                        cd.setName(contact.getName());
                        cd.setPbox(contact.getPbox());
                        cd.setPhone(contact.getPhone());
                        cd.setPos(contact.getPos());
                        cd.setPosition(contact.getPosition());
                        cd.setStreet(contact.getStreet());
                        cd.setSurname(contact.getSurname());
                        cd.setZip(contact.getZip());
                        opnew.getCompanyopportunitycontacts().add(cd);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(cd);
                    }
                }

                //Save docs
                if (!opp.getCompanyopporunitydocses().isEmpty()) {
                    final Iterator itd = opp.getCompanyopporunitydocses().iterator();
                    while (itd.hasNext()) {
                        final Companyopporunitydocs doc = (Companyopporunitydocs) itd.next();
                        final Companyopporunitydocs dc = new Companyopporunitydocs();
                        dc.setCompanyopportunity(opnew);
                        dc.setCreateddate(new Date());
                        dc.setCreatedfrom(EBISystem.ebiUser);
                        dc.setFiles(doc.getFiles());
                        dc.setName(doc.getName());
                        opnew.getCompanyopporunitydocses().add(dc);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(dc);
                    }
                }

                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                EBISystem.getInstance().getCompany().getCompanyopportunities().add(opnew);

                oppID = opnew.getOpportunityid();
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return oppID;
    }

    public void dataEdit(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanyopportunities().size() > 0) {

            for (Companyopportunity oprtObj : EBISystem.getInstance().getCompany().getCompanyopportunities()) {
                if (oprtObj.getOpportunityid() == id) {
                    opportunity = oprtObj;
                    break;
                }
            }

            EBISystem.builder().vpanel("Opportunity").setID(opportunity.getOpportunityid());
            if (opportunity.getName() != null) {
                EBISystem.builder().combo("opportunityNameText", "Opportunity").getEditor().setItem(opportunity.getName());
            }

            if (opportunity.getEvaluationstatus() != null) {
                EBISystem.builder().combo("oppEvalStatusText", "Opportunity").setSelectedItem(opportunity.getEvaluationstatus());
                evalStatus = opportunity.getEvaluationstatus();
            }

            if (opportunity.getBudgetstatus() != null) {
                EBISystem.builder().combo("oppBdgStatusText", "Opportunity").setSelectedItem(opportunity.getBudgetstatus());
                budgetStatus = opportunity.getBudgetstatus();
            }

            if (opportunity.getSalestage() != null) {
                EBISystem.builder().combo("oppSaleStateText", "Opportunity").setSelectedItem(opportunity.getSalestage());
                purchState = opportunity.getSalestage();
            }

            if (opportunity.getOpportunitystatus() != null) {
                EBISystem.builder().combo("statusOppText", "Opportunity").setSelectedItem(opportunity.getOpportunitystatus());
                sStatus = opportunity.getOpportunitystatus();
            }

            if (opportunity.getProbability() != null) {
                EBISystem.builder().combo("oppProbabilityText", "Opportunity").setSelectedItem(opportunity.getProbability());
            }

            if (opportunity.getBusinesstype() != null) {
                EBISystem.builder().combo("oppBustypeText", "Opportunity").setSelectedItem(opportunity.getBusinesstype());
            }

            EBISystem.builder().FormattedField("oppValueText", "Opportunity").setValue(Double.valueOf(this.opportunity.getOpportunityvalue() == null ? 0.0 : this.opportunity.getOpportunityvalue()));

            if (opportunity.getIsclose() != null) {
                EBISystem.builder().getCheckBox("closeOpportunity", "Opportunity").setSelected(opportunity.getIsclose());
            }

            EBISystem.builder().textArea("opportunityDescription", "Opportunity").setText(opportunity.getDescription());
            EBISystem.builder().vpanel("Opportunity").setCreatedDate(EBISystem.getInstance().getDateToString(opportunity.getCreateddate() == null ? new Date() : opportunity.getCreateddate()));
            EBISystem.builder().vpanel("Opportunity").setCreatedFrom(opportunity.getCreatedfrom() == null ? EBISystem.ebiUser : opportunity.getCreatedfrom());

            if (opportunity.getChangeddate() != null) {
                EBISystem.builder().vpanel("Opportunity").setChangedDate(EBISystem.getInstance().getDateToString(opportunity.getChangeddate()));
                EBISystem.builder().vpanel("Opportunity").setChangedFrom(opportunity.getChangedfrom());
            } else {
                EBISystem.builder().vpanel("Opportunity").setChangedDate("");
                EBISystem.builder().vpanel("Opportunity").setChangedFrom("");
            }
            EBISystem.getInstance().getDataStore("Opportunity", "ebiEdit");
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void createOfferFromOpportunity(final int id) {

        EBISystem.getModule().getOfferPane().getDataControlOffer().dataNew();

        if (EBISystem.getInstance().getCompany().getCompanyopportunities().size() > 0) {

            Companyopportunity opp = null;
            for (Companyopportunity oprtObj : EBISystem.getInstance().getCompany().getCompanyopportunities()) {
                if (oprtObj.getOpportunityid() == id) {
                    opp = oprtObj;
                    break;
                }
            }

            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.builder().textField("offerNameText", "Offer").setText(opp.getName());
            EBISystem.builder().textArea("offerDescriptionText", "Offer").setText(opp.getDescription());
            EBISystem.builder().textField("offerOpportunityText", "Offer").setText(opp.getName());

            EBISystem.getModule().getOfferPane().getDataControlOffer().opportunityID = id;
            EBISystem.getUIContainer().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_OFFER")));

            if (opp.getCompanyopportunitycontacts().size() > 0) {

                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_IMPORT_CONTACT_DATA")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                    final Iterator it = opp.getCompanyopportunitycontacts().iterator();
                    Companyopportunitycontact contact;
                    while (it.hasNext()) {

                        contact = (Companyopportunitycontact) it.next();

                        final Companyofferreceiver offRec = new Companyofferreceiver();
                        offRec.setReceivervia("EMail");
                        offRec.setGender(contact.getGender());
                        offRec.setSurname(contact.getSurname());
                        offRec.setName(contact.getName());
                        offRec.setPosition(contact.getPosition());
                        offRec.setFax(contact.getFax());
                        offRec.setEmail(contact.getEmail());
                        offRec.setCnum(contact.getPos());
                        EBISystem.getModule().getOfferPane().getDataControlOffer().getCompOffer().getCompanyofferreceivers().add(offRec);
                    }
                    EBISystem.getModule().getOfferPane().getDataControlOffer().dataShowReceiver();
                }
            }
            if (opp.getCompanyopporunitydocses().size() > 0) {
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_IMPORT_DOCUMENT")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                    final Iterator itr = opp.getCompanyopporunitydocses().iterator();
                    while (itr.hasNext()) {
                        final Companyopporunitydocs obj = (Companyopporunitydocs) itr.next();
                        final Companyofferdocs docs = new Companyofferdocs();
                        docs.setName(obj.getName());
                        docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                        docs.setCreatedfrom(EBISystem.ebiUser);
                        docs.setFiles(obj.getFiles());
                        EBISystem.getModule().getOfferPane().getDataControlOffer().getOfferDocList().add(docs);
                    }
                    EBISystem.getModule().getOfferPane().getDataControlOffer().dataShowDoc();
                }
            }
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {

        if (EBISystem.getInstance().getCompany().getCompanyopportunities().size() > 0) {

            for (Companyopportunity oprtObj : EBISystem.getInstance().getCompany().getCompanyopportunities()) {
                if (oprtObj.getOpportunityid() == id) {
                    opportunity = oprtObj;
                    break;
                }
            }

            EBISystem.getInstance().getDataStore("Opportunity", "ebiDelete");
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").delete(opportunity);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            EBISystem.getInstance().getCompany().getCompanyopportunities().remove(opportunity);
        }
    }

    public void dataShow(Integer id) {

        int selRow = EBISystem.builder().table("companyOpportunityTable", "Opportunity").getSelectedRow() + id;
        final int size = EBISystem.getInstance().getCompany().getCompanyopportunities().size();

        if (size > 0) {

            EBISystem.getModule().getOpportunityPane().getTabModel().data = new Object[size][8];
            final Iterator<Companyopportunity> iter = EBISystem.getInstance().getCompany().getCompanyopportunities().iterator();
            int i = 0;

            final NumberFormat currency = NumberFormat.getNumberInstance();
            currency.setMinimumFractionDigits(2);
            currency.setMaximumFractionDigits(2);

            while (iter.hasNext()) {

                final Companyopportunity obj = iter.next();

                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][1] = obj.getSalestage() == null ? "" : obj.getSalestage();
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][2] = obj.getProbability() == null ? "" : obj.getProbability();
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][3] = obj.getBusinesstype() == null ? "" : obj.getBusinesstype();
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][4] = 
                        obj.getOpportunityvalue() == null || "0".equals(obj.getOpportunityvalue().toString())
                            ? "" : currency.format(obj.getOpportunityvalue());
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][5] = obj.getIsclose() == null ? "" : obj.getIsclose();
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][6] = obj.getClosedate() == null ? "" : EBISystem.getInstance().getDateToString(obj.getClosedate());
                EBISystem.getModule().getOpportunityPane().getTabModel().data[i][7] = obj.getOpportunityid();
                if (id != -1 && id == obj.getOpportunityid()) {
                    selRow = i;
                }
                i++;
            }
            
            
            
        } else {
            EBISystem.getModule().getOpportunityPane().getTabModel().data
                    = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }

        EBISystem.getModule().getOpportunityPane().getTabModel().fireTableDataChanged();
        if (selRow > -1) {
            selRow = EBISystem.builder().table("companyOpportunityTable", "Opportunity").convertRowIndexToView(selRow);
            EBISystem.builder().table("companyOpportunityTable", "Opportunity").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataShowReport(final int id) {

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);

        EBISystem.getInstance().getIEBIReportSystemInstance().
                useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY")),
                        getOpportunityNamefromId(id));

    }

    public String dataShowAndMailReport(final int id, final boolean showWindow) {
        String fileName = "";

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);

        try {
            if (EBISystem.getInstance().getCompany().getCompanyopportunities().size() > 0) {
                Companyopportunity opp = null;
                for (Companyopportunity oprtObj : EBISystem.getInstance().getCompany().getCompanyopportunities()) {
                    if (oprtObj.getOpportunityid() == id) {
                        opp = oprtObj;
                        break;
                    }
                }

                final Iterator it = opp.getCompanyopportunitycontacts().iterator();
                String to = "";
                int i = 1;
                final int c = opp.getCompanyopportunitycontacts().size();
                while (it.hasNext()) {
                    final Companyopportunitycontact rec = (Companyopportunitycontact) it.next();
                    to += rec.getEmail();
                    if (i < c - 1) {
                        to += ";";
                    }
                    i++;
                }
                fileName = EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY")),
                        getOpportunityNamefromId(id), showWindow, true, to);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_RECEIVER_WAS_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
        }

        return fileName;
    }

    public void dataNew() {
        opportunity = new Companyopportunity();
        EBISystem.getModule().getOpportunityPane().initialize(false);
        EBISystem.builder().vpanel("Opportunity").setID(-1);
        EBISystem.getInstance().getDataStore("Opportunity", "ebiNew");
    }

    public void dataRemoveContact(final int id) {
        final Iterator iter = this.opportunity.getCompanyopportunitycontacts().iterator();
        while (iter.hasNext()) {
            final Companyopportunitycontact con = (Companyopportunitycontact) iter.next();
            if (con.getOpportunitycontactid() != null && con.getOpportunitycontactid() == id) {
                this.opportunity.getCompanyopportunitycontacts().remove(con);
                if(id > 0){
                    EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                    EBISystem.hibernate().session("EBICRM_SESSION").delete(con);
                    EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                }
                break;
            }
        }
    }

    public void dataEditContact(final int id) {
        final Iterator iter = this.opportunity.getCompanyopportunitycontacts().iterator();
        Companyopportunitycontact contact;
        while (iter.hasNext()) {

            contact = (Companyopportunitycontact) iter.next();

            if (contact.getOpportunitycontactid() != null && contact.getOpportunitycontactid() == id) {
                final EBIMeetingAddContactDialog newContact = new EBIMeetingAddContactDialog(false, null, contact, true);
                newContact.setGenderText(contact.getGender());
                newContact.setSurnameText(contact.getSurname());
                newContact.setNameText(contact.getName());
                newContact.setStreet(contact.getStreet());
                newContact.setZip(contact.getZip());
                newContact.setLocation(contact.getLocation());
                newContact.setPbox(contact.getPbox());
                newContact.setCountry(contact.getCountry());
                newContact.setPositionText(contact.getPosition());
                newContact.setBirddateText(contact.getBirddate());
                newContact.setTelephoneText(contact.getPhone());
                newContact.setFaxText(contact.getFax());
                newContact.setMobileText(contact.getMobile());
                newContact.setEMailText(contact.getEmail());
                newContact.setDescriptionText(contact.getDescription());
                if (contact.getPos() != null) {
                    newContact.setMainContact(contact.getPos() == 1 ? true : false);
                }
                newContact.setVisible();
                break;
            }
        }
    }

    public void addContact(final EBIMeetingAddContactDialog newContact, final Companyopportunitycontact contact) {
        contact.setCompanyopportunity(opportunity);
        contact.setGender(newContact.getGenderText());
        contact.setSurname(newContact.getSurnameText());
        contact.setName(newContact.getNameText());
        contact.setStreet(newContact.getStreet());
        contact.setZip(newContact.getZip());
        contact.setLocation(newContact.getLocation());
        contact.setPbox(newContact.getPbox());
        contact.setCountry(newContact.getCountry());
        contact.setPosition(newContact.getPositionText());
        contact.setBirddate(newContact.getBirddateText());
        contact.setPhone(newContact.getTelephoneText());
        contact.setFax(newContact.getFaxText());
        contact.setMobile(newContact.getMobileText());
        contact.setEmail(newContact.getEMailText());
        contact.setDescription(newContact.getDescriptionText());
        if (contact.getPos() == null) {
            contact.setPos(0);
        }

        this.opportunity.getCompanyopportunitycontacts().add(contact);
    }

    public void showOpportunityContacts() {
        EBISystem.getModule().getOpportunityPane().getTabModelContact().
                    setAvailableOpportunityContacts(this.opportunity.getCompanyopportunitycontacts());
        EBISystem.getModule().getOpportunityPane().getTabModelContact().fireTableDataChanged();
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.opportunity.getCompanyopporunitydocses().iterator();
        while (iter.hasNext()) {
            final Companyopporunitydocs doc = (Companyopporunitydocs) iter.next();
            if (doc.getDocid() != null && id == doc.getDocid()) {
                this.opportunity.getCompanyopporunitydocses().remove(doc);
                if(id > 0){
                    EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                    EBISystem.hibernate().session("EBICRM_SESSION").delete(doc);
                    EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                }
                break;
            }
        }
    }

    public void dataShowDoc() {

        if (this.opportunity.getCompanyopporunitydocses().size() > 0) {
            EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data = new Object[this.opportunity.getCompanyopporunitydocses().size()][4];

            final Iterator itr = this.opportunity.getCompanyopporunitydocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companyopporunitydocs obj = (Companyopporunitydocs) itr.next();
                if(obj.getDocid() == null){
                    obj.setDocid((i + 1) * -1);
                }
                EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data[i][3] = obj.getDocid();
                i++;
            }
        } else {
            EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().data
                    = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getOpportunityPane().getTabOpportunityDoc().fireTableDataChanged();
    }

    public void dataViewDoc(final int id) {
        final Iterator iter = this.opportunity.getCompanyopporunitydocses().iterator();
        while (iter.hasNext()) {
            final Companyopporunitydocs doc = (Companyopporunitydocs) iter.next();
            if (doc.getDocid() != null && id == doc.getDocid()) {
                // Get the BLOB inputstream
                final String file = doc.getName().replaceAll(" ", "_");
                final byte buffer[] = doc.getFiles();
                EBISystem.getInstance().writeBlobToTmp(file, buffer);
                break;
            }
        }
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Companyopporunitydocs docs = new Companyopporunitydocs();
                docs.setDocid((opportunity.getCompanyopporunitydocses().size() + 1) * -1);
                docs.setCompanyopportunity(this.opportunity);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);

                opportunity.getCompanyopporunitydocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    private void createHistory(final Company com) {

        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + (this.opportunity.getName().equals(EBISystem.builder().combo("opportunityNameText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getName() : this.opportunity.getName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_EVALUATING_STATUS") + ": " + (this.opportunity.getEvaluationstatus().equals(EBISystem.builder().combo("oppEvalStatusText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getEvaluationstatus() : this.opportunity.getEvaluationstatus() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_BUDGETSTATUS") + ": " + (this.opportunity.getBudgetstatus().equals(EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getBudgetstatus() : this.opportunity.getBudgetstatus() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_SALE_STAGE") + ": " + (this.opportunity.getSalestage().equals(EBISystem.builder().combo("oppSaleStateText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getSalestage() : this.opportunity.getSalestage() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_C_STATUS") + ": " + (this.opportunity.getOpportunitystatus().equals(EBISystem.builder().combo("statusOppText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getOpportunitystatus() : this.opportunity.getOpportunitystatus() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_PROBABILITY") + ": " + (this.opportunity.getProbability().equals(EBISystem.builder().combo("oppProbabilityText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getProbability() : this.opportunity.getProbability() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_BUSINESS_TYP") + ": " + (this.opportunity.getBusinesstype().equals(EBISystem.builder().combo("oppBustypeText", "Opportunity").getEditor().getItem().toString()) == true ? this.opportunity.getBusinesstype() : this.opportunity.getBusinesstype() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_VALUE") + ": " + (String.valueOf(this.opportunity.getOpportunityvalue()).equals(EBISystem.builder().FormattedField("oppValueText", "Opportunity").getValue().toString()) == true ? String.valueOf(this.opportunity.getOpportunityvalue()) : String.valueOf(this.opportunity.getOpportunityvalue()) + "$"));

        String chCls = "";
        if (EBISystem.builder().getCheckBox("closeOpportunity", "Opportunity").isSelected() != this.opportunity.getIsclose()) {
            chCls = "$";
        }

        list.add(EBISystem.i18n("EBI_LANG_CLOSED") + ": " + (String.valueOf(this.opportunity.getIsclose())) + chCls);

        list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": " + (this.opportunity.getDescription().equals(EBISystem.builder().textArea("opportunityDescription", "Opportunity").getText()) == true ? this.opportunity.getDescription() : this.opportunity.getDescription() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(opportunity.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + opportunity.getCreatedfrom());

        if (opportunity.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(opportunity.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + opportunity.getChangedfrom());
        }
        list.add("*EOR*"); // END OF RECORD

        if (!this.opportunity.getCompanyopportunitycontacts().isEmpty()) {
            final Iterator iter = this.opportunity.getCompanyopportunitycontacts().iterator();
            while (iter.hasNext()) {
                final Companyopportunitycontact obj = (Companyopportunitycontact) iter.next();
                list.add(obj.getPosition() == null ? EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ":" : EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ": " + obj.getPosition());
                list.add(obj.getSurname() == null ? EBISystem.i18n("EBI_LANG_SURNAME") + ":" : EBISystem.i18n("EBI_LANG_SURNAME") + ": " + obj.getSurname());
                list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_C_CNAME") + ":" : EBISystem.i18n("EBI_LANG_C_CNAME") + ": " + obj.getName());
                list.add(obj.getPhone() == null ? EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ":" : obj.getPhone());
                list.add(obj.getMobile() == null ? EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ":" : EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ": " + obj.getMobile());
                list.add("*EOR*"); // END OF RECORD
            }
        }

        try {
            EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Opportunity", list));
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public Set<Companyopportunity> getOppportunityList() {
        return EBISystem.getInstance().getCompany().getCompanyopportunities();
    }

    public Companyopportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(final Companyopportunity opportunity) {
        this.opportunity = opportunity;
    }

    public String getEvalStatus() {
        return evalStatus;
    }

    public void setEvalStatus(final String evalStatus) {
        this.evalStatus = evalStatus;
    }

    public String getBudgetStatus() {
        return budgetStatus;
    }

    public void setBudgetStatus(final String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    public String getPurchState() {
        return purchState;
    }

    public void setPurchState(final String purchState) {
        this.purchState = purchState;
    }

    public String getSStatus() {
        return sStatus;
    }

    public void setSStatus(final String status) {
        sStatus = status;
    }

    private String getOpportunityNamefromId(final int id) {

        String name = "";

        final Iterator iter = EBISystem.getInstance().getCompany().getCompanyopportunities().iterator();

        while (iter.hasNext()) {
            final Companyopportunity opp = (Companyopportunity) iter.next();
            if (opp.getOpportunityid() == id) {
                name = opp.getName();
                break;
            }
        }

        return name;
    }
}
