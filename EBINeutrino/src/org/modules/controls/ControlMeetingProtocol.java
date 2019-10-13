package org.modules.controls;

import org.modules.views.dialogs.EBIMeetingAddContactDialog;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companymeetingcontacts;
import org.sdk.model.hibernate.Companymeetingdoc;
import org.sdk.model.hibernate.Companymeetingprotocol;
import org.hibernate.HibernateException;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class ControlMeetingProtocol {

    public Companymeetingprotocol meetingProtocol = null;
    public boolean isEdit = false;

    public ControlMeetingProtocol() {
        meetingProtocol = new Companymeetingprotocol();
    }

    public Integer dataStore() {
        Integer meetingID = -1;
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == true) {
                createHistory(EBISystem.getInstance().getCompany());
                meetingProtocol.setChangeddate(new Date());
                meetingProtocol.setChangedfrom(EBISystem.ebiUser);
            } else {
                meetingProtocol.setCreateddate(new Date());
                meetingProtocol.setCreatedfrom(EBISystem.gui().vpanel("MeetingCall").getCreatedFrom());
                meetingProtocol.setCompany(EBISystem.getInstance().getCompany());
            }

            if (EBISystem.gui().combo("meetingTypeText", "MeetingCall").getSelectedItem() != null) {
                meetingProtocol.setMeetingtype(EBISystem.gui().combo("meetingTypeText", "MeetingCall").getSelectedItem().toString());
            }

            meetingProtocol.setMeetingsubject(EBISystem.gui().textField("subjectMeetingText", "MeetingCall").getText());
            meetingProtocol.setProtocol(EBISystem.gui().textArea("meetingDescription", "MeetingCall").getText());

            if (EBISystem.gui().timePicker("dateMeetingText", "MeetingCall").getDate() != null) {
                meetingProtocol.setMetingdate(EBISystem.gui().timePicker("dateMeetingText", "MeetingCall").getDate());
            }

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(meetingProtocol);

            //Save meeting contacts
            if (!this.meetingProtocol.getCompanymeetingcontactses().isEmpty()) {
                final Iterator iter = meetingProtocol.getCompanymeetingcontactses().iterator();
                while (iter.hasNext()) {
                    final Companymeetingcontacts cont = (Companymeetingcontacts) iter.next();
                    cont.setCompanymeetingprotocol(meetingProtocol);
                    if (cont.getMeetingcontactid() != null && cont.getMeetingcontactid() < 0) {
                        cont.setMeetingcontactid(null);
                    }
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(cont);
                }
            }
            //Save docs
            if (!this.meetingProtocol.getCompanymeetingdocs().isEmpty()) {
                final Iterator iter = meetingProtocol.getCompanymeetingdocs().iterator();
                while (iter.hasNext()) {
                    final Companymeetingdoc doc = (Companymeetingdoc) iter.next();
                    doc.setCompanymeetingprotocol(meetingProtocol);
                    if (doc.getMeetingdocid() != null && doc.getMeetingdocid() < 0) {
                        doc.setMeetingdocid(null);
                    }
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(doc);
                }
            }

            EBISystem.getInstance().getDataStore("MeetingCall", "ebiSave");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            
            EBISystem.getInstance().getCompany().getCompanymeetingprotocols().add(meetingProtocol);
            
            if (!isEdit) {
                EBISystem.gui().vpanel("MeetingCall").setID(meetingProtocol.getMeetingprotocolid());
            }
            meetingID = meetingProtocol.getMeetingprotocolid();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return meetingID;
    }

    public Integer dataCopy(final int id) {

        Integer meetingID = -1;

        try {
            if (EBISystem.getInstance().getCompany().getCompanymeetingprotocols().size() > 0) {
                Companymeetingprotocol mProtocol = null;
                for (Companymeetingprotocol metObj : EBISystem.getInstance().getCompany().getCompanymeetingprotocols()) {
                    if (metObj.getMeetingprotocolid() == id) {
                        mProtocol = metObj;
                        break;
                    }
                }

                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Companymeetingprotocol mPro = new Companymeetingprotocol();
                mPro.setCreateddate(new Date());
                mPro.setCreatedfrom(EBISystem.ebiUser);

                mPro.setCompany(mProtocol.getCompany());
                mPro.setMeetingtype(mProtocol.getMeetingtype());
                mPro.setMeetingsubject(mProtocol.getMeetingsubject() + " - (Copy)");
                mPro.setProtocol(mProtocol.getProtocol());
                mPro.setMetingdate(mProtocol.getMetingdate());
                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(mPro);

                //Save meeting contacts
                if (!mProtocol.getCompanymeetingcontactses().isEmpty()) {

                    final Iterator itc = mProtocol.getCompanymeetingcontactses().iterator();
                    while (itc.hasNext()) {
                        final Companymeetingcontacts cont = (Companymeetingcontacts) itc.next();

                        final Companymeetingcontacts mcon = new Companymeetingcontacts();
                        mcon.setCompanymeetingprotocol(mPro);
                        mcon.setCreateddate(new Date());
                        mcon.setCreatedfrom(EBISystem.ebiUser);
                        mcon.setGender(cont.getGender());
                        mcon.setName(cont.getName());
                        mcon.setSurname(cont.getSurname());
                        mcon.setPos(cont.getPos());
                        mcon.setPosition(cont.getPosition());
                        mcon.setBirddate(cont.getBirddate());
                        mcon.setCountry(cont.getCountry());
                        mcon.setEmail(cont.getEmail());
                        mcon.setFax(cont.getFax());
                        mcon.setLocation(cont.getLocation());
                        mcon.setMittelname(cont.getMittelname());
                        mcon.setMobile(cont.getMobile());
                        mcon.setPbox(cont.getPbox());
                        mcon.setPhone(cont.getPhone());
                        mcon.setStreet(cont.getStreet());
                        mcon.setZip(cont.getZip());
                        mcon.setDescription(cont.getDescription());
                        mPro.getCompanymeetingcontactses().add(mcon);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(mcon);
                    }
                }
                //Save docs
                if (!mProtocol.getCompanymeetingdocs().isEmpty()) {
                    final Iterator itd = mProtocol.getCompanymeetingdocs().iterator();
                    while (itd.hasNext()) {
                        final Companymeetingdoc doc = (Companymeetingdoc) itd.next();

                        final Companymeetingdoc cdc = new Companymeetingdoc();
                        cdc.setCompanymeetingprotocol(mPro);
                        cdc.setCreateddate(new Date());
                        cdc.setCreatedfrom(EBISystem.ebiUser);
                        cdc.setFiles(doc.getFiles());
                        cdc.setName(doc.getName());
                        mPro.getCompanymeetingdocs().add(cdc);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(cdc);
                    }
                }
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                EBISystem.getInstance().getCompany().getCompanymeetingprotocols().add(mPro);
                meetingID = mPro.getMeetingprotocolid();
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return meetingID;
    }

    public void dataEdit(final int id) {
        if (EBISystem.getInstance().getCompany().getCompanymeetingprotocols().size() > 0) {
            for (Companymeetingprotocol metObj :
                    EBISystem.getInstance().getCompany().getCompanymeetingprotocols()) {
                if (metObj.getMeetingprotocolid() == id) {
                    meetingProtocol = metObj;
                    break;
                }
            }

            EBISystem.gui().vpanel("MeetingCall").setID(meetingProtocol.getMeetingprotocolid());
            if (meetingProtocol.getMeetingtype() != null) {
                EBISystem.gui().combo("meetingTypeText", "MeetingCall").setSelectedItem(meetingProtocol.getMeetingtype());
            }
            EBISystem.gui().textField("subjectMeetingText", "MeetingCall").setText(meetingProtocol.getMeetingsubject() == null ? "" : meetingProtocol.getMeetingsubject());
            EBISystem.gui().textArea("meetingDescription", "MeetingCall").setText(meetingProtocol.getProtocol() == null ? "" : meetingProtocol.getProtocol());

            if (meetingProtocol.getMetingdate() != null) {
                EBISystem.gui().timePicker("dateMeetingText", "MeetingCall").setDate(meetingProtocol.getMetingdate());
                EBISystem.gui().timePicker("dateMeetingText", "MeetingCall").getEditor().setText(EBISystem.getInstance().getDateToString(meetingProtocol.getMetingdate()));
            }

            EBISystem.gui().vpanel("MeetingCall").setCreatedDate(EBISystem.getInstance().getDateToString(meetingProtocol.getCreateddate() == null ? new Date() : meetingProtocol.getCreateddate()));
            EBISystem.gui().vpanel("MeetingCall").setCreatedFrom(meetingProtocol.getCreatedfrom() == null ? EBISystem.ebiUser : meetingProtocol.getCreatedfrom());

            if (meetingProtocol.getChangeddate() != null) {
                EBISystem.gui().vpanel("MeetingCall").setChangedDate(EBISystem.getInstance().getDateToString(meetingProtocol.getChangeddate()));
                EBISystem.gui().vpanel("MeetingCall").setChangedFrom(EBISystem.ebiUser);
            } else {
                EBISystem.gui().vpanel("MeetingCall").setChangedDate("");
                EBISystem.gui().vpanel("MeetingCall").setChangedFrom("");
            }
            EBISystem.getInstance().getDataStore("MeetingCall", "ebiEdit");
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {
        if (EBISystem.getInstance().getCompany().getCompanymeetingprotocols().size() > 0) {
            for (Companymeetingprotocol metObj : EBISystem.getInstance().getCompany().getCompanymeetingprotocols()) {
                if (metObj.getMeetingprotocolid() == id) {
                    meetingProtocol = metObj;
                    break;
                }
            }

            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").delete(meetingProtocol);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            EBISystem.getInstance().getCompany().getCompanymeetingprotocols().remove(meetingProtocol);
            EBISystem.getInstance().getDataStore("MeetingCall", "ebiDelete");
        }
    }

    public void dataShow(Integer id) {
        int srow = EBISystem.gui().table("companyMeetings", "MeetingCall").getSelectedRow();
        final int size = EBISystem.getInstance().getCompany().getCompanymeetingprotocols().size();
        if (size > 0) {
            EBISystem.getModule().getMeetingProtocol().getTableModel().data = new Object[size][5];
            final Iterator<Companymeetingprotocol> iter = EBISystem.getInstance().getCompany().getCompanymeetingprotocols().iterator();
            int i = 0;
            while (iter.hasNext()) {
                final Companymeetingprotocol obj = iter.next();
                EBISystem.getModule().getMeetingProtocol().getTableModel().data[i][0] = obj.getMetingdate() == null ? "" : EBISystem.getInstance().getDateToString(obj.getMetingdate());
                EBISystem.getModule().getMeetingProtocol().getTableModel().data[i][1] = obj.getMeetingsubject() == null ? "" : obj.getMeetingsubject();
                EBISystem.getModule().getMeetingProtocol().getTableModel().data[i][2] = obj.getMeetingtype() == null ? "" : obj.getMeetingtype();
                EBISystem.getModule().getMeetingProtocol().getTableModel().data[i][3] = obj.getProtocol() == null ? "" : obj.getProtocol();
                EBISystem.getModule().getMeetingProtocol().getTableModel().data[i][4] = obj.getMeetingprotocolid();
                if (id != -1 && id == obj.getMeetingprotocolid()) {
                    srow = EBISystem.gui().table("companyMeetings", "MeetingCall").convertRowIndexToView(i);
                }
                i++;
            }
        } else {
            EBISystem.getModule().getMeetingProtocol().getTableModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", ""}};
        }
        EBISystem.getModule().getMeetingProtocol().getTableModel().fireTableDataChanged();
        if(srow > -1){
            EBISystem.gui().table("companyMeetings", "MeetingCall").changeSelection(srow, 0, false, false);
        }
    }

    public void dataShowReport(final int id) {

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);

        EBISystem.getInstance().getIEBIReportSystemInstance().
                useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL")),
                        getMeetingNamefromId(id));
    }

    public void dataNew() {
        meetingProtocol = new Companymeetingprotocol();
        EBISystem.getModule().getMeetingProtocol().initialize(false);
        EBISystem.getInstance().getDataStore("MeetingCall", "ebiNew");
        EBISystem.gui().vpanel("MeetingCall").setID(-1);
    }

    private void createHistory(final Company com) throws Exception {

        final List<String> list = new ArrayList<String>();
        if (meetingProtocol.getCreateddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(meetingProtocol.getCreateddate()));
            list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + EBISystem.ebiUser);
        }

        if (meetingProtocol.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(meetingProtocol.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + EBISystem.ebiUser);
        }

        list.add(EBISystem.i18n("EBI_LANG_C_MEETING_TYPE") + ": " + (meetingProtocol.getMeetingtype().
                equals(EBISystem.gui().combo("meetingTypeText", "MeetingCall").getSelectedItem().toString()) == true
                ? meetingProtocol.getMeetingtype() : meetingProtocol.getMeetingtype() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_C_MEMO_SUBJECT") + ": " + (meetingProtocol.getMeetingsubject().equals(EBISystem.gui().textField("subjectMeetingText", "MeetingCall").getText()) == true ? meetingProtocol.getMeetingsubject() : meetingProtocol.getMeetingsubject() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": " + (meetingProtocol.getProtocol().equals(EBISystem.gui().textArea("meetingDescription", "MeetingCall").getText()) == true ? meetingProtocol.getProtocol() : meetingProtocol.getProtocol() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_DATE") + ": " + (EBISystem.getInstance().getDateToString(meetingProtocol.getMetingdate()).equals(EBISystem.gui().timePicker("dateMeetingText", "MeetingCall").getEditor().getText()) == true ? EBISystem.getInstance().getDateToString(meetingProtocol.getMetingdate()) : EBISystem.getInstance().getDateToString(meetingProtocol.getMetingdate()) + "$"));

        list.add("*EOR*"); // END OF RECORD

        if (!meetingProtocol.getCompanymeetingcontactses().isEmpty()) {
            final Iterator iter = meetingProtocol.getCompanymeetingcontactses().iterator();
            while (iter.hasNext()) {
                final Companymeetingcontacts contact = (Companymeetingcontacts) iter.next();

                list.add(EBISystem.i18n("EBI_LANG_C_GENDER") + ": " + contact.getGender());
                list.add(EBISystem.i18n("EBI_LANG_SURNAME") + ": " + contact.getSurname());
                list.add(EBISystem.i18n("EBI_LANG_C_CNAME") + ": " + contact.getName());
                list.add(EBISystem.i18n("EBI_LANG_C_MITTEL_NAME") + ": " + contact.getMittelname());
                list.add(EBISystem.i18n("EBI_LANG_C_POSITION") + ": " + contact.getPosition());

                list.add(EBISystem.i18n("EBI_LANG_C_BIRDDATE") + ": " + EBISystem.getInstance().getDateToString(contact.getBirddate()));
                list.add(EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": " + contact.getPhone());
                list.add(EBISystem.i18n("EBI_LANG_C_FAX") + ": " + contact.getFax());
                list.add(EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE") + ": " + contact.getMobile());
                list.add(EBISystem.i18n("EBI_LANG_EMAIL") + ": " + contact.getEmail());
                list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": " + contact.getDescription());

            }
        }

        if (!meetingProtocol.getCompanymeetingdocs().isEmpty()) {
            final Iterator iter = meetingProtocol.getCompanymeetingdocs().iterator();
            while (iter.hasNext()) {
                final Companymeetingdoc doc = (Companymeetingdoc) iter.next();

                list.add(doc.getName() == null ? EBISystem.i18n("EBI_LANG_FILENAME") + ": " : EBISystem.i18n("EBI_LANG_FILENAME") + ": " + doc.getName());
                list.add(EBISystem.getInstance().getDateToString(doc.getCreateddate()) == null ? EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " : EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " + EBISystem.getInstance().getDateToString(doc.getCreateddate()));
                list.add(doc.getCreatedfrom() == null ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + doc.getCreatedfrom());
                list.add("*EOR*");
            }
        }

        EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Meeting", list));

    }

    public void dataAddContact() {
        final EBIMeetingAddContactDialog newContact = new EBIMeetingAddContactDialog(true, null, null, false);
        newContact.setVisible();
    }

    public void addContact(final EBIMeetingAddContactDialog newContact, final Companymeetingcontacts contact) {
        try {

            contact.setCompanymeetingprotocol(this.meetingProtocol);
            contact.setGender(newContact.getGenderText());
            contact.setSurname(newContact.getSurnameText());
            contact.setName(newContact.getNameText());
            contact.setPosition(newContact.getPositionText());
            contact.setStreet(newContact.getStreet());
            contact.setZip(newContact.getZip());
            contact.setLocation(newContact.getLocation());
            contact.setPbox(newContact.getPbox());
            contact.setCountry(newContact.getCountry());
            contact.setBirddate(newContact.getBirddateText());
            contact.setPhone(newContact.getTelephoneText());
            contact.setFax(newContact.getFaxText());
            contact.setMobile(newContact.getMobileText());
            contact.setEmail(newContact.getEMailText());
            contact.setDescription(newContact.getDescriptionText());
            meetingProtocol.getCompanymeetingcontactses().add(contact);

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataEditContact(final int id) {

        final Iterator iter = this.meetingProtocol.getCompanymeetingcontactses().iterator();

        while (iter.hasNext()) {

            final Companymeetingcontacts contact = (Companymeetingcontacts) iter.next();

            if (contact.getMeetingcontactid() == id) {
                final EBIMeetingAddContactDialog newContact = new EBIMeetingAddContactDialog(true, contact, null, true);
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

    public void dataDeleteContact(final int id) {
        final Iterator iter = meetingProtocol.getCompanymeetingcontactses().iterator();
        while (iter.hasNext()) {
            final Companymeetingcontacts con = (Companymeetingcontacts) iter.next();
            if (con.getMeetingcontactid() == id) {
                meetingProtocol.getCompanymeetingcontactses().remove(con);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(con);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataShowContact() {
        if (!this.meetingProtocol.getCompanymeetingcontactses().isEmpty()) {
            EBISystem.getModule().getMeetingProtocol().getTabModelContact().data = new Object[this.meetingProtocol.getCompanymeetingcontactses().size()][9];

            final Iterator itr = this.meetingProtocol.getCompanymeetingcontactses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companymeetingcontacts obj = (Companymeetingcontacts) itr.next();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][0] = obj.getPosition() == null ? "" : obj.getPosition();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][1] = obj.getGender() == null ? "" : obj.getGender();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][2] = obj.getSurname() == null ? "" : obj.getSurname();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][3] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][4] = obj.getPhone() == null ? "" : obj.getPhone();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][5] = obj.getMobile() == null ? "" : obj.getMobile();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][6] = obj.getEmail() == null ? "" : obj.getEmail();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getModule().getMeetingProtocol().getTabModelContact().data[i][8] = obj.getMeetingcontactid();
                i++;
            }
        } else {
            EBISystem.getModule().getMeetingProtocol().getTabModelContact().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }
        EBISystem.getModule().getMeetingProtocol().getTabModelContact().fireTableDataChanged();
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                try {
                    final Companymeetingdoc docs = new Companymeetingdoc();
                    docs.setMeetingdocid((meetingProtocol.getCompanymeetingdocs().size() + 1) * -1);
                    docs.setCompanymeetingprotocol(this.meetingProtocol);
                    docs.setName(fs.getName());
                    docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                    docs.setCreatedfrom(EBISystem.ebiUser);
                    docs.setFiles(file);
                    meetingProtocol.getCompanymeetingdocs().add(docs);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void dataEditDoc(final int id) {
        String FileName;
        String FileType;
        OutputStream fos;
        try {
            final Iterator iter = this.meetingProtocol.getCompanymeetingdocs().iterator();
            while (iter.hasNext()) {

                final Companymeetingdoc doc = (Companymeetingdoc) iter.next();
                if (id == doc.getMeetingdocid()) {
                    // Get the BLOB inputstream
                    final String file = doc.getName().replaceAll(" ", "_");
                    //byte buffer[] = doc.getFiles().getBytes(1,(int)doc.getFiles().length());
                    final byte buffer[] = doc.getFiles();
                    FileName = "tmp/" + file;
                    FileType = file.substring(file.lastIndexOf("."));
                    fos = new FileOutputStream(FileName);
                    fos.write(buffer, 0, buffer.length);
                    fos.close();
                    EBISystem.getInstance().resolverType(FileName, FileType);
                    break;
                }
            }
        } catch (final FileNotFoundException exx) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        } catch (final IOException exx1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.meetingProtocol.getCompanymeetingdocs().iterator();
        while (iter.hasNext()) {
            final Companymeetingdoc doc = (Companymeetingdoc) iter.next();
            if (id == doc.getMeetingdocid()) {
                this.meetingProtocol.getCompanymeetingdocs().remove(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataShowDoc() {
        if (!this.meetingProtocol.getCompanymeetingdocs().isEmpty()) {
            EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data = new Object[this.meetingProtocol.getCompanymeetingdocs().size()][4];

            final Iterator itr = this.meetingProtocol.getCompanymeetingdocs().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companymeetingdoc obj = (Companymeetingdoc) itr.next();
                EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data[i][3] = obj.getMeetingdocid();
                i++;
            }
        } else {
            EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getMeetingProtocol().getTabmeetingDoc().fireTableDataChanged();
    }

    public Set<Companymeetingprotocol> getMeetingPlist() {
        return EBISystem.getInstance().getCompany().getCompanymeetingprotocols();
    }

    public Set<Companymeetingcontacts> getMeetingContactlist() {
        return meetingProtocol.getCompanymeetingcontactses();
    }

    public Set<Companymeetingdoc> getMeetingDocList() {
        return meetingProtocol.getCompanymeetingdocs();
    }

    public Companymeetingprotocol getMeetingProtocol() {
        return meetingProtocol;
    }

    public void setMeetingProtocol(final Companymeetingprotocol meetingProtocol) {
        this.meetingProtocol = meetingProtocol;
    }

    private String getMeetingNamefromId(final int id) {
        String name = "";
        final Iterator iter = EBISystem.getInstance().getCompany().getCompanymeetingprotocols().iterator();
        while (iter.hasNext()) {
            final Companymeetingprotocol meeting = (Companymeetingprotocol) iter.next();
            if (meeting.getMeetingprotocolid() == id) {
                name = meeting.getMeetingsubject();
                break;
            }
        }
        return name;
    }
}
