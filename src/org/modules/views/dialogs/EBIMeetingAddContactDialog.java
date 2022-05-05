package org.modules.views.dialogs;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Companymeetingcontacts;
import org.sdk.model.hibernate.Companyopportunitycontact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class EBIMeetingAddContactDialog {

    private boolean isMeeting = false;
    private boolean isEdit = false;
    private Companymeetingcontacts contact = null;
    private Companyopportunitycontact ocontact = null;

    public EBIMeetingAddContactDialog(final boolean isMeeting,
            final Companymeetingcontacts contact,
            final Companyopportunitycontact ocontact, final boolean isEdit) {
        this.isMeeting = isMeeting;
        this.contact = contact;
        this.ocontact = ocontact;
        this.isEdit = isEdit;
        EBISystem.builder().loadGUI("CRMDialog/addNewContactDialog.xml");
        EBISystem.builder().combo("genderText", "addNewContactDialog").setModel(
                new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_C_MALE"), EBISystem.i18n("EBI_LANG_C_FEMALE")}));
    }

    public void setVisible() {

        EBISystem.builder().label("email", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("mail.png"));

        EBISystem.builder().timePicker("birddateText", "addNewContactDialog").setFormats(EBISystem.DateFormat);
        EBISystem.builder().timePicker("birddateText", "addNewContactDialog").setDate(new java.util.Date());

        EBISystem.builder().button("newContact", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.builder().button("newContact", "addNewContactDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                resetFields();
            }
        });

        EBISystem.builder().button("searchContact", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("find.png"));
        EBISystem.builder().button("searchContact", "addNewContactDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogSearchContact addCon = new EBIDialogSearchContact(false);

                addCon.setValueToComponent(EBISystem.builder().combo("genderText", "addNewContactDialog"), "CGender");
                addCon.setValueToComponent(EBISystem.builder().textField("positionText", "addNewContactDialog"), "CPosition");

                addCon.setValueToComponent(EBISystem.builder().textField("surnameText", "addNewContactDialog"), "CSurname");
                addCon.setValueToComponent(EBISystem.builder().textField("nameText", "addNewContactDialog"), "CName");

                addCon.setValueToComponent(EBISystem.builder().textField("streetNrText", "addNewContactDialog"), "Street");
                addCon.setValueToComponent(EBISystem.builder().textField("zipText", "addNewContactDialog"), "Zip");
                addCon.setValueToComponent(EBISystem.builder().textField("locationText", "addNewContactDialog"), "Location");

                addCon.setValueToComponent(EBISystem.builder().textField("countryText", "addNewContactDialog"), "Country");
                addCon.setValueToComponent(EBISystem.builder().textField("pboxText", "addNewContactDialog"), "PBox");

                addCon.setValueToComponent(EBISystem.builder().textField("emailText", "addNewContactDialog"), "CEMail");
                addCon.setValueToComponent(EBISystem.builder().textField("faxText", "addNewContactDialog"), "CFax");

                addCon.setValueToComponent(EBISystem.builder().textField("telephoneText", "addNewContactDialog"), "CPHONE");
                addCon.setValueToComponent(EBISystem.builder().textField("mobileText", "addNewContactDialog"), "CMOBILE");

                addCon.setValueToComponent(EBISystem.builder().timePicker("birddateText", "addNewContactDialog"), "CBIRDDATE");
                addCon.setValueToComponent(EBISystem.builder().textArea("contactDescription", "addNewContactDialog"), "CDESCRIPTION");
                EBISystem.builder().dialog("addNewContactDialog").setVisible(false);
                addCon.setVisible();
                EBISystem.builder().dialog("addNewContactDialog").setVisible(true);
            }
        });

        EBISystem.builder().button("closeButton", "addNewContactDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("addNewContactDialog").setVisible(false);
            }
        });

        EBISystem.builder().button("applyButton", "addNewContactDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateInput()) {
                    return;
                }
                if (isMeeting) {
                    if (isEdit) {
                        contact.setPos(EBISystem.builder().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().addContact(EBIMeetingAddContactDialog.this, contact, true);
                    } else {
                        final Companymeetingcontacts contact = new Companymeetingcontacts();
                        contact.setMeetingcontactid((EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().getMeetingContactlist().size() + 1) * -1);
                        contact.setCreateddate(new Date());
                        contact.setPos(EBISystem.builder().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        contact.setCreatedfrom(EBISystem.ebiUser);
                        EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().addContact(EBIMeetingAddContactDialog.this, contact, false);
                    }
                } else {
                    if (isEdit) {
                        ocontact.setPos(EBISystem.builder().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().addContact(EBIMeetingAddContactDialog.this, ocontact, true);
                    } else {
                        final Companyopportunitycontact contact = new Companyopportunitycontact();
                        contact.setOpportunitycontactid((EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().getOpportunity().getCompanyopportunitycontacts().size() + 1) * -1);
                        contact.setCreateddate(new Date());
                        contact.setPos(EBISystem.builder().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        contact.setCreatedfrom(EBISystem.ebiUser);
                        EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().addContact(EBIMeetingAddContactDialog.this, contact, false);
                    }
                }
                EBISystem.builder().dialog("addNewContactDialog").setVisible(false);
            }
        });
        EBISystem.builder().showGUI();
    }

    public void resetFields() {
        setBirddateText(null);
        setCountry("");
        setDescriptionText("");
        setEMailText("");
        setFaxText("");
        setGenderText(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
        setLocation("");
        setMainContact(false);
        setMobileText("");
        setNameText("");
        setPbox("");
        setPositionText("");
        setStreet("");
        setSurnameText("");
        setTelephoneText("");
        setZip("");
    }

    public String getSurnameText() {
        return EBISystem.builder().textField("surnameText", "addNewContactDialog").getText();
    }

    public void setSurnameText(final String surname) {
        EBISystem.builder().textField("surnameText", "addNewContactDialog").setText(surname);
    }

    public String getNameText() {
        return EBISystem.builder().textField("nameText", "addNewContactDialog").getText();
    }

    public void setNameText(final String name) {
        EBISystem.builder().textField("nameText", "addNewContactDialog").setText(name);
    }

    public Date getBirddateText() {
        return EBISystem.builder().timePicker("birddateText", "addNewContactDialog").getDate();
    }

    public void setBirddateText(final Date date) {
        EBISystem.builder().timePicker("birddateText", "addNewContactDialog").setDate(date);
    }

    public String getGenderText() {
        return EBISystem.builder().combo("genderText", "addNewContactDialog").getEditor().getItem() == null ? ""
                : EBISystem.builder().combo("genderText", "addNewContactDialog").getEditor().getItem().toString();
    }

    public void setGenderText(final String gender) {
        EBISystem.builder().combo("genderText", "addNewContactDialog").setSelectedItem(gender);
    }

    public String getPositionText() {
        return EBISystem.builder().textField("positionText", "addNewContactDialog").getText();
    }

    public void setPositionText(final String position) {
        EBISystem.builder().textField("positionText", "addNewContactDialog").setText(position);
    }

    public String getTelephoneText() {
        return EBISystem.builder().textField("telephoneText", "addNewContactDialog").getText();
    }

    public void setTelephoneText(final String telephone) {
        EBISystem.builder().textField("telephoneText", "addNewContactDialog").setText(telephone);
    }

    public String getMobileText() {
        return EBISystem.builder().textField("mobileText", "addNewContactDialog").getText();
    }

    public void setMobileText(final String mobile) {
        EBISystem.builder().textField("mobileText", "addNewContactDialog").setText(mobile);
    }

    public String getEMailText() {
        return EBISystem.builder().textField("emailText", "addNewContactDialog").getText();
    }

    public void setEMailText(final String email) {
        EBISystem.builder().textField("emailText", "addNewContactDialog").setText(email);
    }

    public String getFaxText() {
        return EBISystem.builder().textField("faxText", "addNewContactDialog").getText();
    }

    public void setFaxText(final String fax) {
        EBISystem.builder().textField("faxText", "addNewContactDialog").setText(fax);
    }

    public String getDescriptionText() {
        return EBISystem.builder().textArea("contactDescription", "addNewContactDialog").getText();
    }

    public void setDescriptionText(final String description) {
        EBISystem.builder().textArea("contactDescription", "addNewContactDialog").setText(description);
    }

    public void setMainContact(final boolean isSelected) {
        EBISystem.builder().getCheckBox("mainContact", "addNewContactDialog").setSelected(isSelected);
    }

    public void setStreet(final String street) {
        EBISystem.builder().textField("streetNrText", "addNewContactDialog").setText(street);
    }

    public String getStreet() {
        return EBISystem.builder().textField("streetNrText", "addNewContactDialog").getText();
    }

    public void setZip(final String zip) {
        EBISystem.builder().textField("zipText", "addNewContactDialog").setText(zip);
    }

    public String getZip() {
        return EBISystem.builder().textField("zipText", "addNewContactDialog").getText();
    }

    public void setLocation(final String location) {
        EBISystem.builder().textField("locationText", "addNewContactDialog").setText(location);
    }

    public String getLocation() {
        return EBISystem.builder().textField("locationText", "addNewContactDialog").getText();
    }

    public void setPbox(final String pbox) {
        EBISystem.builder().textField("pboxText", "addNewContactDialog").setText(pbox);
    }

    public String getPbox() {
        return EBISystem.builder().textField("pboxText", "addNewContactDialog").getText();
    }

    public void setCountry(final String country) {
        EBISystem.builder().textField("countryText", "addNewContactDialog").setText(country);
    }

    public String getCountry() {
        return EBISystem.builder().textField("countryText", "addNewContactDialog").getText();
    }

    private boolean validateInput() {
        if (EBISystem.builder().combo("genderText", "addNewContactDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.builder().textField("nameText", "addNewContactDialog").getText().trim())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
