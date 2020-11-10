package org.modules.views.dialogs;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Companymeetingcontacts;
import org.sdk.model.hibernate.Companyopportunitycontact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.ImageIcon;

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
        EBISystem.gui().loadGUI("CRMDialog/addNewContactDialog.xml");
        EBISystem.gui().combo("genderText", "addNewContactDialog").setModel(
                new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_C_MALE"), EBISystem.i18n("EBI_LANG_C_FEMALE")}));
    }

    public void setVisible() {

        EBISystem.gui().label("email", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("mail.png"));

        EBISystem.gui().timePicker("birddateText", "addNewContactDialog").setFormats(EBISystem.DateFormat);
        EBISystem.gui().timePicker("birddateText", "addNewContactDialog").setDate(new java.util.Date());

        EBISystem.gui().button("newContact", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.gui().button("newContact", "addNewContactDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                resetFields();
            }
        });

        EBISystem.gui().button("searchContact", "addNewContactDialog").setIcon(EBISystem.getInstance().getIconResource("find.png"));
        EBISystem.gui().button("searchContact", "addNewContactDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogSearchContact addCon = new EBIDialogSearchContact(false);

                addCon.setValueToComponent(EBISystem.gui().combo("genderText", "addNewContactDialog"), "Gender");
                addCon.setValueToComponent(EBISystem.gui().textField("positionText", "addNewContactDialog"), "Position");

                addCon.setValueToComponent(EBISystem.gui().textField("surnameText", "addNewContactDialog"), "Surname");
                addCon.setValueToComponent(EBISystem.gui().textField("nameText", "addNewContactDialog"), "contact.Name");

                addCon.setValueToComponent(EBISystem.gui().textField("streetNrText", "addNewContactDialog"), "Street");
                addCon.setValueToComponent(EBISystem.gui().textField("zipText", "addNewContactDialog"), "Zip");
                addCon.setValueToComponent(EBISystem.gui().textField("locationText", "addNewContactDialog"), "Location");

                addCon.setValueToComponent(EBISystem.gui().textField("countryText", "addNewContactDialog"), "Country");
                addCon.setValueToComponent(EBISystem.gui().textField("pboxText", "addNewContactDialog"), "PBox");

                addCon.setValueToComponent(EBISystem.gui().textField("emailText", "addNewContactDialog"), "contact.EMail");
                addCon.setValueToComponent(EBISystem.gui().textField("faxText", "addNewContactDialog"), "contact.Fax");

                addCon.setValueToComponent(EBISystem.gui().textField("telephoneText", "addNewContactDialog"), "contact.PHONE");
                addCon.setValueToComponent(EBISystem.gui().textField("mobileText", "addNewContactDialog"), "contact.MOBILE");

                addCon.setValueToComponent(EBISystem.gui().timePicker("birddateText", "addNewContactDialog"), "contact.BIRDDATE");
                addCon.setValueToComponent(EBISystem.gui().textArea("contactDescription", "addNewContactDialog"), "contact.DESCRIPTION");
                EBISystem.gui().dialog("addNewContactDialog").setVisible(false);
                addCon.setVisible();
                EBISystem.gui().dialog("addNewContactDialog").setVisible(true);
            }
        });

        EBISystem.gui().button("closeButton", "addNewContactDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("addNewContactDialog").setVisible(false);
            }
        });

        EBISystem.gui().button("applyButton", "addNewContactDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateInput()) {
                    return;
                }
                if (isMeeting) {
                    if (isEdit) {
                        contact.setPos(EBISystem.gui().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().addContact(EBIMeetingAddContactDialog.this, contact);
                    } else {
                        final Companymeetingcontacts contact = new Companymeetingcontacts();
                        contact.setMeetingcontactid((EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().getMeetingContactlist().size() + 1) * -1);
                        contact.setCreateddate(new Date());
                        contact.setPos(EBISystem.gui().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        contact.setCreatedfrom(EBISystem.ebiUser);
                        EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().addContact(EBIMeetingAddContactDialog.this, contact);
                    }
                } else {
                    if (isEdit) {
                        ocontact.setPos(EBISystem.gui().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().addContact(EBIMeetingAddContactDialog.this, ocontact);
                    } else {
                        final Companyopportunitycontact contact = new Companyopportunitycontact();
                        contact.setOpportunitycontactid((EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().getOpportunity().getCompanyopportunitycontacts().size() + 1) * -1);
                        contact.setCreateddate(new Date());
                        contact.setPos(EBISystem.gui().getCheckBox("mainContact", "addNewContactDialog").isSelected() ? 1 : 0);
                        contact.setCreatedfrom(EBISystem.ebiUser);
                        EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().addContact(EBIMeetingAddContactDialog.this, contact);
                    }
                }
                EBISystem.gui().dialog("addNewContactDialog").setVisible(false);
            }
        });
        EBISystem.gui().showGUI();
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
        return EBISystem.gui().textField("surnameText", "addNewContactDialog").getText();
    }

    public void setSurnameText(final String surname) {
        EBISystem.gui().textField("surnameText", "addNewContactDialog").setText(surname);
    }

    public String getNameText() {
        return EBISystem.gui().textField("nameText", "addNewContactDialog").getText();
    }

    public void setNameText(final String name) {
        EBISystem.gui().textField("nameText", "addNewContactDialog").setText(name);
    }

    public Date getBirddateText() {
        return EBISystem.gui().timePicker("birddateText", "addNewContactDialog").getDate();
    }

    public void setBirddateText(final Date date) {
        EBISystem.gui().timePicker("birddateText", "addNewContactDialog").setDate(date);
    }

    public String getGenderText() {
        return EBISystem.gui().combo("genderText", "addNewContactDialog").getEditor().getItem() == null ? ""
                : EBISystem.gui().combo("genderText", "addNewContactDialog").getEditor().getItem().toString();
    }

    public void setGenderText(final String gender) {
        EBISystem.gui().combo("genderText", "addNewContactDialog").setSelectedItem(gender);
    }

    public String getPositionText() {
        return EBISystem.gui().textField("positionText", "addNewContactDialog").getText();
    }

    public void setPositionText(final String position) {
        EBISystem.gui().textField("positionText", "addNewContactDialog").setText(position);
    }

    public String getTelephoneText() {
        return EBISystem.gui().textField("telephoneText", "addNewContactDialog").getText();
    }

    public void setTelephoneText(final String telephone) {
        EBISystem.gui().textField("telephoneText", "addNewContactDialog").setText(telephone);
    }

    public String getMobileText() {
        return EBISystem.gui().textField("mobileText", "addNewContactDialog").getText();
    }

    public void setMobileText(final String mobile) {
        EBISystem.gui().textField("mobileText", "addNewContactDialog").setText(mobile);
    }

    public String getEMailText() {
        return EBISystem.gui().textField("emailText", "addNewContactDialog").getText();
    }

    public void setEMailText(final String email) {
        EBISystem.gui().textField("emailText", "addNewContactDialog").setText(email);
    }

    public String getFaxText() {
        return EBISystem.gui().textField("faxText", "addNewContactDialog").getText();
    }

    public void setFaxText(final String fax) {
        EBISystem.gui().textField("faxText", "addNewContactDialog").setText(fax);
    }

    public String getDescriptionText() {
        return EBISystem.gui().textArea("contactDescription", "addNewContactDialog").getText();
    }

    public void setDescriptionText(final String description) {
        EBISystem.gui().textArea("contactDescription", "addNewContactDialog").setText(description);
    }

    public void setMainContact(final boolean isSelected) {
        EBISystem.gui().getCheckBox("mainContact", "addNewContactDialog").setSelected(isSelected);
    }

    public void setStreet(final String street) {
        EBISystem.gui().textField("streetNrText", "addNewContactDialog").setText(street);
    }

    public String getStreet() {
        return EBISystem.gui().textField("streetNrText", "addNewContactDialog").getText();
    }

    public void setZip(final String zip) {
        EBISystem.gui().textField("zipText", "addNewContactDialog").setText(zip);
    }

    public String getZip() {
        return EBISystem.gui().textField("zipText", "addNewContactDialog").getText();
    }

    public void setLocation(final String location) {
        EBISystem.gui().textField("locationText", "addNewContactDialog").setText(location);
    }

    public String getLocation() {
        return EBISystem.gui().textField("locationText", "addNewContactDialog").getText();
    }

    public void setPbox(final String pbox) {
        EBISystem.gui().textField("pboxText", "addNewContactDialog").setText(pbox);
    }

    public String getPbox() {
        return EBISystem.gui().textField("pboxText", "addNewContactDialog").getText();
    }

    public void setCountry(final String country) {
        EBISystem.gui().textField("countryText", "addNewContactDialog").setText(country);
    }

    public String getCountry() {
        return EBISystem.gui().textField("countryText", "addNewContactDialog").getText();
    }

    private boolean validateInput() {
        if (EBISystem.gui().combo("genderText", "addNewContactDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.gui().textField("nameText", "addNewContactDialog").getText().trim())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
