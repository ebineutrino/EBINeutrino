package org.modules.views.dialogs;

import org.modules.controls.ControlOffer;
import org.modules.controls.ControlOrder;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Companyofferreceiver;
import org.sdk.model.hibernate.Companyorderreceiver;

import javax.swing.*;

public class EBICRMAddContactAddressType {

    public boolean isSaved = false;
    private boolean isOrder = false;
    private ControlOffer dataControlOffer = null;
    private ControlOrder dataControlOrder = null;
    private final JTextField phoneForContact = new JTextField();
    private Companyofferreceiver receiver = null;
    private Companyorderreceiver receiver1 = null;

    public EBICRMAddContactAddressType(final ControlOffer dataControlOffer) {
        EBISystem.builder().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOffer = dataControlOffer;
    }

    public EBICRMAddContactAddressType(final ControlOffer dataControlOffer, final Companyofferreceiver rec) {
        this.receiver = rec;
        EBISystem.builder().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOffer = dataControlOffer;
    }

    public EBICRMAddContactAddressType(final ControlOrder dataControlOrder) {
        EBISystem.builder().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.dataControlOrder = dataControlOrder;
        isOrder = true;
    }

    public EBICRMAddContactAddressType(final ControlOrder dataControlOrder, final Companyorderreceiver rec) {
        EBISystem.builder().loadGUI("CRMDialog/addnewReceiverDialog.xml");
        this.receiver1 = rec;
        this.dataControlOrder = dataControlOrder;
        isOrder = true;
    }
   
    public void setVisible() {
        EBISystem.builder().dialog("addNewReceiverDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_ADD_CONTACT_SEND_TYPE"));
        EBISystem.builder().vpanel("addNewReceiverDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_ADD_CONTACT_SEND_TYPE"));

        EBISystem.builder().label("fax", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_FAX"));
        EBISystem.builder().label("email", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_EMAIL"));
        EBISystem.builder().label("country", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_COUNTRY"));
        EBISystem.builder().label("postCode", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_POST_CODE"));
        EBISystem.builder().label("zipLocation", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_ZIP") + "/" + EBISystem.i18n("EBI_LANG_C_LOCATION"));
        EBISystem.builder().label("street", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_STREET_NR"));
        EBISystem.builder().label("position", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_POSITION"));
        EBISystem.builder().label("surname", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_SURNAME"));
        EBISystem.builder().label("name", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_NAME"));
        EBISystem.builder().label("gender", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_GENDER"));
        EBISystem.builder().label("typeDispatch", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_C_SEND_TYPE"));

        EBISystem.builder().button("closeButton", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.builder().button("closeButton", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("addNewReceiverDialog").setVisible(false);
                isSaved = false;
            }
        });

        EBISystem.builder().button("applyButton", "addNewReceiverDialog").setText(EBISystem.i18n("EBI_LANG_INSERT"));
        EBISystem.builder().button("applyButton", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateInput()) {
                    return;
                }
                isSaved = true;
                if (!isOrder) {
                    addReciever();
                } else {
                    addReciever1();
                }
                newReciever();
            }
        });

        EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").setEditable(true);
        EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").
                setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_EMAIL"),
            EBISystem.i18n("EBI_LANG_C_POST"),
            EBISystem.i18n("EBI_LANG_C_FAX")
        }));

        EBISystem.builder().combo("genderText", "addNewReceiverDialog").
                setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_C_MALE"),
            EBISystem.i18n("EBI_LANG_C_FEMALE")
        }));

        EBISystem.builder().combo("genderText", "addNewReceiverDialog").setEditable(true);

        EBISystem.builder().button("searchReciever", "addNewReceiverDialog").setIcon(EBISystem.getInstance().getIconResource("find.png"));
        EBISystem.builder().button("searchReciever", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                final EBIDialogSearchContact addCon = new EBIDialogSearchContact(false);
          
                addCon.setValueToComponent(EBISystem.builder().combo("genderText", "addNewReceiverDialog"), "CGender");
                addCon.setValueToComponent(EBISystem.builder().textField("surnameText", "addNewReceiverDialog"), "CSurname");
                addCon.setValueToComponent(EBISystem.builder().textField("nameText", "addNewReceiverDialog"), "CName");
                addCon.setValueToComponent(EBISystem.builder().textField("streetText", "addNewReceiverDialog"), "Street");
                addCon.setValueToComponent(EBISystem.builder().textField("zipText", "addNewReceiverDialog"), "Zip");
                addCon.setValueToComponent(EBISystem.builder().textField("locationText", "addNewReceiverDialog"), "Location");
                addCon.setValueToComponent(EBISystem.builder().textField("countryText", "addNewReceiverDialog"), "Country");
                addCon.setValueToComponent(EBISystem.builder().textField("postcodeText", "addNewReceiverDialog"), "PBox");
                addCon.setValueToComponent(EBISystem.builder().textField("positionText", "addNewReceiverDialog"), "CPosition");
                addCon.setValueToComponent(EBISystem.builder().textField("emailText", "addNewReceiverDialog"), "CEMail");
                addCon.setValueToComponent(EBISystem.builder().textField("faxText", "addNewReceiverDialog"), "CFax");
                addCon.setValueToComponent(phoneForContact, "CPhone");
                EBISystem.builder().dialog("addNewReceiverDialog").setVisible(false);
                addCon.setVisible();
                EBISystem.builder().dialog("addNewReceiverDialog").setVisible(true);
            }
        });

        EBISystem.builder().button("newReciever", "addNewReceiverDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.builder().button("newReciever", "addNewReceiverDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newReciever();
            }
        });

        if (this.receiver != null) {
            EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").setSelectedItem(this.receiver.getReceivervia());
            EBISystem.builder().combo("genderText", "addNewReceiverDialog").setSelectedItem(this.receiver.getGender());
            EBISystem.builder().textField("nameText", "addNewReceiverDialog").setText(this.receiver.getName());
            EBISystem.builder().textField("surnameText", "addNewReceiverDialog").setText(this.receiver.getSurname());
            EBISystem.builder().textField("positionText", "addNewReceiverDialog").setText(this.receiver.getPosition());
            EBISystem.builder().textField("postcodeText", "addNewReceiverDialog").setText(this.receiver.getPbox());
            EBISystem.builder().textField("streetText", "addNewReceiverDialog").setText(this.receiver.getStreet());
            EBISystem.builder().textField("locationText", "addNewReceiverDialog").setText(this.receiver.getLocation());
            EBISystem.builder().textField("zipText", "addNewReceiverDialog").setText(this.receiver.getZip());
            EBISystem.builder().textField("emailText", "addNewReceiverDialog").setText(this.receiver.getEmail());
            EBISystem.builder().textField("faxText", "addNewReceiverDialog").setText(this.receiver.getFax());
            EBISystem.builder().textField("countryText", "addNewReceiverDialog").setText(this.receiver.getCountry());
            if (this.receiver.getCnum() != null) {
                EBISystem.builder().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(this.receiver.getCnum() == 1 ? true : false);
            }
        }

        if (this.receiver1 != null) {
            EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").setSelectedItem(this.receiver1.getReceivervia());
            EBISystem.builder().combo("genderText", "addNewReceiverDialog").setSelectedItem(this.receiver1.getGender());
            EBISystem.builder().textField("nameText", "addNewReceiverDialog").setText(this.receiver1.getName());
            EBISystem.builder().textField("surnameText", "addNewReceiverDialog").setText(this.receiver1.getSurname());
            EBISystem.builder().textField("positionText", "addNewReceiverDialog").setText(this.receiver1.getPosition());
            EBISystem.builder().textField("postcodeText", "addNewReceiverDialog").setText(this.receiver1.getPbox());
            EBISystem.builder().textField("streetText", "addNewReceiverDialog").setText(this.receiver1.getStreet());
            EBISystem.builder().textField("locationText", "addNewReceiverDialog").setText(this.receiver1.getLocation());
            EBISystem.builder().textField("zipText", "addNewReceiverDialog").setText(this.receiver1.getZip());
            EBISystem.builder().textField("emailText", "addNewReceiverDialog").setText(this.receiver1.getEmail());
            EBISystem.builder().textField("faxText", "addNewReceiverDialog").setText(this.receiver1.getFax());
            EBISystem.builder().textField("countryText", "addNewReceiverDialog").setText(this.receiver1.getCountry());
            if (this.receiver1.getCnum() != null) {
                EBISystem.builder().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(this.receiver1.getCnum() == 1 ? true : false);
            }
        }

        EBISystem.builder().showGUI();
    }

    public boolean validateInput() {
        if (EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_SEND_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (EBISystem.i18n("EBI_LANG_EMAIL").equals(
                EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").getEditor().getItem())
                && "".equals(EBISystem.builder().textField("emailText", "addNewReceiverDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (EBISystem.i18n("EBI_LANG_C_FAX").equals(
                EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").getEditor().getItem().toString())
                && "".equals(EBISystem.builder().textField("faxText", "addNewReceiverDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_FAX")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void newReciever() {
        EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").setSelectedIndex(0);
        EBISystem.builder().combo("genderText", "addNewReceiverDialog").setSelectedIndex(0);
        EBISystem.builder().textField("countryText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("locationText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("zipText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("nameText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("surnameText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("postcodeText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("positionText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("streetText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("emailText", "addNewReceiverDialog").setText("");
        EBISystem.builder().textField("faxText", "addNewReceiverDialog").setText("");
        EBISystem.builder().getCheckBox("mainContact", "addNewReceiverDialog").setSelected(false);
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
        of.setCnum(EBISystem.builder().getCheckBox("mainContact", "addNewReceiverDialog").isSelected() ? 1 : 0);
        of.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
        of.setCreatedfrom(EBISystem.ebiUser);
        of.setReceivervia(EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").getEditor().getItem().toString());
        of.setGender(EBISystem.builder().combo("genderText", "addNewReceiverDialog").getEditor().getItem().toString());
        of.setName(EBISystem.builder().textField("nameText", "addNewReceiverDialog").getText());
        of.setSurname(EBISystem.builder().textField("surnameText", "addNewReceiverDialog").getText());
        of.setPosition(EBISystem.builder().textField("positionText", "addNewReceiverDialog").getText());
        of.setPbox(EBISystem.builder().textField("postcodeText", "addNewReceiverDialog").getText());
        of.setStreet(EBISystem.builder().textField("streetText", "addNewReceiverDialog").getText());
        of.setLocation(EBISystem.builder().textField("locationText", "addNewReceiverDialog").getText());
        of.setZip(EBISystem.builder().textField("zipText", "addNewReceiverDialog").getText());
        of.setEmail(EBISystem.builder().textField("emailText", "addNewReceiverDialog").getText());
        of.setFax(EBISystem.builder().textField("faxText", "addNewReceiverDialog").getText());
        of.setPhone(phoneForContact.getText());
        of.setCountry(EBISystem.builder().textField("countryText", "addNewReceiverDialog").getText());
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
        ord.setCnum(EBISystem.builder().getCheckBox("mainContact", "addNewReceiverDialog").isSelected() ? 1 : 0);
        ord.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
        ord.setCreatedfrom(EBISystem.ebiUser);
        ord.setReceivervia(EBISystem.builder().combo("typeDispatchText", "addNewReceiverDialog").getEditor().getItem().toString());
        ord.setGender(EBISystem.builder().combo("genderText", "addNewReceiverDialog").getEditor().getItem().toString());
        ord.setName(EBISystem.builder().textField("nameText", "addNewReceiverDialog").getText());
        ord.setSurname(EBISystem.builder().textField("surnameText", "addNewReceiverDialog").getText());
        ord.setPosition(EBISystem.builder().textField("positionText", "addNewReceiverDialog").getText());
        ord.setPbox(EBISystem.builder().textField("postcodeText", "addNewReceiverDialog").getText());
        ord.setStreet(EBISystem.builder().textField("streetText", "addNewReceiverDialog").getText());
        ord.setLocation(EBISystem.builder().textField("locationText", "addNewReceiverDialog").getText());
        ord.setZip(EBISystem.builder().textField("zipText", "addNewReceiverDialog").getText());
        ord.setEmail(EBISystem.builder().textField("emailText", "addNewReceiverDialog").getText());
        ord.setFax(EBISystem.builder().textField("faxText", "addNewReceiverDialog").getText());
        ord.setCountry(EBISystem.builder().textField("countryText", "addNewReceiverDialog").getText());
        ord.setPhone(phoneForContact.getText());

        if (this.receiver1 == null) {
            dataControlOrder.getCompOrder().getCompanyorderreceivers().add(ord);
        }
        dataControlOrder.dataShowReceiver();
    }
}
