package ebiCRM.gui.dialogs;

import ebiCRM.data.control.EBIDataControlCampaign;
import ebiCRM.data.control.EBIDataControlOffer;
import ebiCRM.data.control.EBIDataControlOrder;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.Companyofferreceiver;
import ebiNeutrinoSDK.model.hibernate.Companyorderreceiver;
import ebiNeutrinoSDK.model.hibernate.Crmcampaignreceiver;

import javax.swing.*;
import java.util.Date;

public class EBICRMAddContactAddressType {

    public boolean isSaved = false;
    private boolean isOrder = false;
    private boolean isCampaign = false;
    private EBIDataControlOffer dataControlOffer = null;
    private EBIDataControlOrder dataControlOrder = null;
    private EBIDataControlCampaign dataControlCampaign = null;
    private final JTextField phoneForContact = new JTextField();
    private Companyofferreceiver receiver = null;
    private Companyorderreceiver receiver1 = null;
    private Crmcampaignreceiver receiver2 = null;

    public EBICRMAddContactAddressType(final EBIDataControlOffer dataControlOffer) {
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOffer = dataControlOffer;
    }

    public EBICRMAddContactAddressType(final EBIDataControlOffer dataControlOffer, final Companyofferreceiver rec) {
        this.receiver = rec;
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOffer = dataControlOffer;
    }

    public EBICRMAddContactAddressType(final EBIDataControlOrder dataControlOrder) {
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOrder = dataControlOrder;
        isOrder = true;
    }

    public EBICRMAddContactAddressType(final EBIDataControlOrder dataControlOrder, final Companyorderreceiver rec) {
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.receiver1 = rec;
        this.dataControlOrder = dataControlOrder;
        isOrder = true;
    }

    public EBICRMAddContactAddressType(final EBIDataControlCampaign dataControlCampaign) {
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialogCampaign.xml");
        this.dataControlCampaign = dataControlCampaign;
        EBISystem.gui().label("companyNumber", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_COMPANY_NUMBER"));
        EBISystem.gui().label("companyName", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_COMPANY_NAME"));
        isCampaign = true;
    }

    public EBICRMAddContactAddressType(final EBIDataControlCampaign dataControlCampaign, final Crmcampaignreceiver campRec) {
        this.receiver2 = campRec;
        EBISystem.gui().loadGUI("CRMDialog/addnewReceiverDialogCampaign.xml");
        this.dataControlCampaign = dataControlCampaign;
        isCampaign = true;
    }

    public void setVisible() {
        EBISystem.gui().dialog("addNewReceiverDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_ADD_CONTACT_SEND_TYPE"));
        EBISystem.gui().vpanel("addNewReceiverDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_ADD_CONTACT_SEND_TYPE"));

        EBISystem.gui().label("fax", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_FAX"));
        EBISystem.gui().label("email", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_EMAIL"));
        EBISystem.gui().label("country", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_COUNTRY"));
        EBISystem.gui().label("postCode", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_POST_CODE"));
        EBISystem.gui().label("zipLocation", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_ZIP") + "/" + EBISystem.i18n("EBI_LANG_C_LOCATION"));
        EBISystem.gui().label("street", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_STREET_NR"));
        EBISystem.gui().label("position", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_POSITION"));
        EBISystem.gui().label("surname", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_SURNAME"));
        EBISystem.gui().label("name", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_NAME"));
        EBISystem.gui().label("gender", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_GENDER"));
        EBISystem.gui().label("typeDispatch", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_SEND_TYPE"));

        EBISystem.gui().button("closeButton", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.gui().button("closeButton", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("addNewReceiverDialog").setVisible(false);
                isSaved = false;
            }
        });

        EBISystem.gui().button("applyButton", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_INSERT"));
        EBISystem.gui().button("applyButton", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateInput()) {
                    return;
                }
                isSaved = true;
                if (!isCampaign) {
                    if (!isOrder) {
                        addReciever();
                    } else {
                        addReciever1();
                    }
                } else {
                    addReciever2();
                }
                newReciever();
            }
        });

        EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setEditable(true);
        EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_EMAIL"),
            EBISystem.i18n("EBI_LANG_C_POST"),
            EBISystem.i18n("EBI_LANG_C_FAX")
        }));

        EBISystem.gui().combo("genderText", "addNewReceiverDialog").setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_C_MALE"),
            EBISystem.i18n("EBI_LANG_C_FEMALE")
        }));
        
        EBISystem.gui().combo("genderText", "addNewReceiverDialog").setEditable(true);
        
        EBISystem.gui().button("searchReciever", "addNewReceiverDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("find.png")));
        EBISystem.gui().button("searchReciever", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                final EBIDialogSearchContact addCon = new EBIDialogSearchContact(false);
                addCon.setValueToComponent(EBISystem.gui().combo("genderText", "addNewReceiverDialog"), "Gender");
                addCon.setValueToComponent(EBISystem.gui().textField("surnameText", "addNewReceiverDialog"), "Surname");
                addCon.setValueToComponent(EBISystem.gui().textField("nameText", "addNewReceiverDialog"), "contact.Name");
                addCon.setValueToComponent(EBISystem.gui().textField("streetText", "addNewReceiverDialog"), "Street");
                addCon.setValueToComponent(EBISystem.gui().textField("zipText", "addNewReceiverDialog"), "Zip");
                addCon.setValueToComponent(EBISystem.gui().textField("locationText", "addNewReceiverDialog"), "Location");
                addCon.setValueToComponent(EBISystem.gui().textField("countryText", "addNewReceiverDialog"), "Country");
                addCon.setValueToComponent(EBISystem.gui().textField("postcodeText", "addNewReceiverDialog"), "PBox");
                addCon.setValueToComponent(EBISystem.gui().textField("positionText", "addNewReceiverDialog"), "Position");
                addCon.setValueToComponent(EBISystem.gui().textField("emailText", "addNewReceiverDialog"), "contact.EMail");
                addCon.setValueToComponent(EBISystem.gui().textField("faxText", "addNewReceiverDialog"), "contact.Fax");
                addCon.setValueToComponent(phoneForContact, "contact.Phone");
                if (isCampaign) {
                    addCon.setValueToComponent(EBISystem.gui().textField("companyNrText", "addNewReceiverDialog"), "company.CUSTOMERNR");
                    addCon.setValueToComponent(EBISystem.gui().textField("companyNameText", "addNewReceiverDialog"), "company.NAME");
                }
                addCon.setVisible();
            }
        });

        EBISystem.gui().button("newReciever", "addNewReceiverDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("new.png")));
        EBISystem.gui().button("newReciever", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newReciever();
            }
        });

        if (this.receiver != null) {
            EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setSelectedItem(this.receiver.getReceivervia());
            EBISystem.gui().combo("genderText", "addNewReceiverDialog").setSelectedItem(this.receiver.getGender());
            EBISystem.gui().textField("nameText", "addNewReceiverDialog").setText(this.receiver.getName());
            EBISystem.gui().textField("surnameText", "addNewReceiverDialog").setText(this.receiver.getSurname());
            EBISystem.gui().textField("positionText", "addNewReceiverDialog").setText(this.receiver.getPosition());
            EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").setText(this.receiver.getPbox());
            EBISystem.gui().textField("streetText", "addNewReceiverDialog").setText(this.receiver.getStreet());
            EBISystem.gui().textField("locationText", "addNewReceiverDialog").setText(this.receiver.getLocation());
            EBISystem.gui().textField("zipText", "addNewReceiverDialog").setText(this.receiver.getZip());
            EBISystem.gui().textField("emailText", "addNewReceiverDialog").setText(this.receiver.getEmail());
            EBISystem.gui().textField("faxText", "addNewReceiverDialog").setText(this.receiver.getFax());
            EBISystem.gui().textField("countryText", "addNewReceiverDialog").setText(this.receiver.getCountry());
            if (this.receiver.getCnum() != null) {
                EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(this.receiver.getCnum() == 1 ? true : false);
            }
        }

        if (this.receiver1 != null) {
            EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setSelectedItem(this.receiver1.getReceivervia());
            EBISystem.gui().combo("genderText", "addNewReceiverDialog").setSelectedItem(this.receiver1.getGender());
            EBISystem.gui().textField("nameText", "addNewReceiverDialog").setText(this.receiver1.getName());
            EBISystem.gui().textField("surnameText", "addNewReceiverDialog").setText(this.receiver1.getSurname());
            EBISystem.gui().textField("positionText", "addNewReceiverDialog").setText(this.receiver1.getPosition());
            EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").setText(this.receiver1.getPbox());
            EBISystem.gui().textField("streetText", "addNewReceiverDialog").setText(this.receiver1.getStreet());
            EBISystem.gui().textField("locationText", "addNewReceiverDialog").setText(this.receiver1.getLocation());
            EBISystem.gui().textField("zipText", "addNewReceiverDialog").setText(this.receiver1.getZip());
            EBISystem.gui().textField("emailText", "addNewReceiverDialog").setText(this.receiver1.getEmail());
            EBISystem.gui().textField("faxText", "addNewReceiverDialog").setText(this.receiver1.getFax());
            EBISystem.gui().textField("countryText", "addNewReceiverDialog").setText(this.receiver1.getCountry());
            if (this.receiver1.getCnum() != null) {
                EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(this.receiver1.getCnum() == 1 ? true : false);
            }
        }

        if (this.receiver2 != null) {
            EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setSelectedItem(this.receiver2.getReceivervia());
            EBISystem.gui().combo("genderText", "addNewReceiverDialog").setSelectedItem(this.receiver2.getGender());
            EBISystem.gui().textField("nameText", "addNewReceiverDialog").setText(this.receiver2.getName());
            EBISystem.gui().textField("surnameText", "addNewReceiverDialog").setText(this.receiver2.getSurname());
            EBISystem.gui().textField("positionText", "addNewReceiverDialog").setText(this.receiver2.getPosition());
            EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").setText(this.receiver2.getPbox());
            EBISystem.gui().textField("streetText", "addNewReceiverDialog").setText(this.receiver2.getStreet());
            EBISystem.gui().textField("locationText", "addNewReceiverDialog").setText(this.receiver2.getLocation());
            EBISystem.gui().textField("zipText", "addNewReceiverDialog").setText(this.receiver2.getZip());
            EBISystem.gui().textField("emailText", "addNewReceiverDialog").setText(this.receiver2.getEmail());
            EBISystem.gui().textField("faxText", "addNewReceiverDialog").setText(this.receiver2.getFax());
            EBISystem.gui().textField("countryText", "addNewReceiverDialog").setText(this.receiver2.getCountry());
            EBISystem.gui().textField("companyNameText", "addNewReceiverDialog").setText(this.receiver2.getCompanyname());
            EBISystem.gui().textField("companyNrText", "addNewReceiverDialog").setText(this.receiver2.getCompanynumber());
            EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(this.receiver2.getCnum() == 1 ? true : false);
        }
        EBISystem.gui().showGUI();
    }

    public boolean validateInput() {
        if (EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_SEND_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (EBISystem.i18n("EBI_LANG_EMAIL").equals(
                EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedItem())
                && "".equals(EBISystem.gui().textField("emailText", "addNewReceiverDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (EBISystem.i18n("EBI_LANG_C_FAX").equals(
                EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedItem().toString())
                && "".equals(EBISystem.gui().textField("faxText", "addNewReceiverDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_FAX")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void newReciever() {
        EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").setSelectedIndex(0);
        EBISystem.gui().combo("genderText", "addNewReceiverDialog").setSelectedIndex(0);
        EBISystem.gui().textField("countryText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("locationText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("zipText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("nameText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("surnameText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("positionText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("streetText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("emailText", "addNewReceiverDialog").setText("");
        EBISystem.gui().textField("faxText", "addNewReceiverDialog").setText("");
        EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(false);

        if (isCampaign) {
            EBISystem.gui().textField("companyNrText", "addNewReceiverDialog").setText("");
            EBISystem.gui().textField("companyNameText", "addNewReceiverDialog").setText("");
        }
    }

    public void addReciever() {

        Companyofferreceiver of;
        if (this.receiver == null) {
            of = new Companyofferreceiver();
            of.setReceiverid((dataControlOffer.getOfferRecieverList().size() + 1) * -1);
        } else {
            of = this.receiver;
        }
        of.setCompanyoffer(this.dataControlOffer.getCompOffer());
        of.setCnum(EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").isSelected() ? 1 : 0);
        of.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
        of.setCreatedfrom(EBISystem.ebiUser);
        of.setReceivervia(EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedItem().toString());
        of.setGender(EBISystem.gui().combo("genderText", "addNewReceiverDialog").getSelectedItem().toString());
        of.setName(EBISystem.gui().textField("nameText", "addNewReceiverDialog").getText());
        of.setSurname(EBISystem.gui().textField("surnameText", "addNewReceiverDialog").getText());
        of.setPosition(EBISystem.gui().textField("positionText", "addNewReceiverDialog").getText());
        of.setPbox(EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").getText());
        of.setStreet(EBISystem.gui().textField("streetText", "addNewReceiverDialog").getText());
        of.setLocation(EBISystem.gui().textField("locationText", "addNewReceiverDialog").getText());
        of.setZip(EBISystem.gui().textField("zipText", "addNewReceiverDialog").getText());
        of.setEmail(EBISystem.gui().textField("emailText", "addNewReceiverDialog").getText());
        of.setFax(EBISystem.gui().textField("faxText", "addNewReceiverDialog").getText());
        of.setPhone(phoneForContact.getText());
        of.setCountry(EBISystem.gui().textField("countryText", "addNewReceiverDialog").getText());
        if (this.receiver == null) {
            dataControlOffer.getOfferRecieverList().add(of);
        }
        dataControlOffer.dataShowReceiver();
    }

    public void addReciever1() {
        Companyorderreceiver ord;
        if (this.receiver1 == null) {
            ord = new Companyorderreceiver();
            ord.setReceiverid((dataControlOrder.getCompOrder().getCompanyorderreceivers().size() + 1) * -1);
        } else {
            ord = this.receiver1;
        }
        ord.setCompanyorder(this.dataControlOrder.getCompOrder());
        ord.setCnum(EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").isSelected() ? 1 : 0);
        ord.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
        ord.setCreatedfrom(EBISystem.ebiUser);
        ord.setReceivervia(EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedItem().toString());
        ord.setGender(EBISystem.gui().combo("genderText", "addNewReceiverDialog").getSelectedItem().toString());
        ord.setName(EBISystem.gui().textField("nameText", "addNewReceiverDialog").getText());
        ord.setSurname(EBISystem.gui().textField("surnameText", "addNewReceiverDialog").getText());
        ord.setPosition(EBISystem.gui().textField("positionText", "addNewReceiverDialog").getText());
        ord.setPbox(EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").getText());
        ord.setStreet(EBISystem.gui().textField("streetText", "addNewReceiverDialog").getText());
        ord.setLocation(EBISystem.gui().textField("locationText", "addNewReceiverDialog").getText());
        ord.setZip(EBISystem.gui().textField("zipText", "addNewReceiverDialog").getText());
        ord.setEmail(EBISystem.gui().textField("emailText", "addNewReceiverDialog").getText());
        ord.setFax(EBISystem.gui().textField("faxText", "addNewReceiverDialog").getText());
        ord.setCountry(EBISystem.gui().textField("countryText", "addNewReceiverDialog").getText());
        ord.setPhone(phoneForContact.getText());

        if (this.receiver1 == null) {
            dataControlOrder.getCompOrder().getCompanyorderreceivers().add(ord);
        }
        dataControlOrder.dataShowReceiver();
    }

    public void addReciever2() {
        Crmcampaignreceiver campaignre;
        if (receiver2 == null) {
            campaignre = new Crmcampaignreceiver();
            campaignre.setReceiverid((dataControlCampaign.getCampaignReceiverList().size() + 1) * -1);
        } else {
            campaignre = receiver2;
        }

        campaignre.setCrmcampaign(dataControlCampaign.getCampaign());
        campaignre.setCnum(EBISystem.gui().getCheckBox("mainContact", "addNewReceiverDialog").isSelected() ? 1 : 0);
        campaignre.setCompanynumber(EBISystem.gui().textField("companyNrText", "addNewReceiverDialog").getText());
        campaignre.setCompanyname(EBISystem.gui().textField("companyNameText", "addNewReceiverDialog").getText());
        campaignre.setCreateddate(new Date());
        campaignre.setCreatedfrom(EBISystem.ebiUser);
        campaignre.setReceivervia(EBISystem.gui().combo("typeDispatchText", "addNewReceiverDialog").getSelectedItem().toString());
        campaignre.setGender(EBISystem.gui().combo("genderText", "addNewReceiverDialog").getSelectedItem().toString());
        campaignre.setName(EBISystem.gui().textField("nameText", "addNewReceiverDialog").getText());
        campaignre.setSurname(EBISystem.gui().textField("surnameText", "addNewReceiverDialog").getText());
        campaignre.setPosition(EBISystem.gui().textField("positionText", "addNewReceiverDialog").getText());
        campaignre.setPbox(EBISystem.gui().textField("postcodeText", "addNewReceiverDialog").getText());
        campaignre.setStreet(EBISystem.gui().textField("streetText", "addNewReceiverDialog").getText());
        campaignre.setLocation(EBISystem.gui().textField("locationText", "addNewReceiverDialog").getText());
        campaignre.setZip(EBISystem.gui().textField("zipText", "addNewReceiverDialog").getText());
        campaignre.setEmail(EBISystem.gui().textField("emailText", "addNewReceiverDialog").getText());
        campaignre.setFax(EBISystem.gui().textField("faxText", "addNewReceiverDialog").getText());
        campaignre.setCountry(EBISystem.gui().textField("countryText", "addNewReceiverDialog").getText());
        campaignre.setPhone(phoneForContact.getText());

        if (this.receiver2 == null) {
            dataControlCampaign.getCampaignReceiverList().add(campaignre);
        }
        dataControlCampaign.dataShowReciever();
    }
}
