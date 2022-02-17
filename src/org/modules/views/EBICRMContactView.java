package org.modules.views;

import org.modules.controls.ControlContact;
import org.modules.views.dialogs.EBIAddressSelectionDialog;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelCRMAddress;
import org.modules.models.ModelCRMContact;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.sdk.gui.component.EBIJTextFieldNumeric;

public class EBICRMContactView {

    public static final String[] gendersList = {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
        EBISystem.i18n("EBI_LANG_C_MALE"), EBISystem.i18n("EBI_LANG_C_FEMALE")};
    @Getter
    @Setter
    private ModelCRMContact tableModel = null;
    @Getter
    @Setter
    private ModelCRMAddress addressModel = null;
    @Getter
    @Setter
    private ControlContact controlContact = new ControlContact();
    @Getter
    @Setter
    private int selectedContactRow = -1;
    @Getter
    @Setter
    private int selectedAddressRow = -1;

    public void initializeAction() {

        EBISystem.builder().textField("telefonText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.builder().textField("faxText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.builder().textField("mobileText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.builder().textField("emailText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.EMAIL));

        EBISystem.builder().textField("filterTableText", "Contact").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("companyContacts", "Contact").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Contact").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("companyContacts", "Contact").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Contact").getText()));
            }
        });

        EBISystem.builder().table("companyContacts", "Contact").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("companyContacts", "Contact").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() > -1) {
                    selectedContactRow = EBISystem.builder().
                            table("companyContacts", "Contact").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editContact", "Contact").setEnabled(false);
                    EBISystem.builder().button("deleteContact", "Contact").setEnabled(false);
                    EBISystem.builder().button("historyContact", "Contact").setEnabled(false);
                    EBISystem.builder().button("copyContact", "Contact").setEnabled(false);
                } else if (!"".equals(tableModel.getValueAt(selectedContactRow, 0).toString())) {
                    EBISystem.builder().button("editContact", "Contact").setEnabled(true);
                    EBISystem.builder().button("deleteContact", "Contact").setEnabled(true);
                    EBISystem.builder().button("historyContact", "Contact").setEnabled(true);
                    EBISystem.builder().button("copyContact", "Contact").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("companyContacts", "Contact").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedContactRow = selRow;
                editContact();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedContactRow = selRow;
                editContact();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedContactRow = selRow;
                if (selectedContactRow < 0 || "".
                        equals(tableModel.getValueAt(selectedContactRow, 0))) {
                    return;
                }
                editContact();
            }
        });

        EBISystem.builder().table("companyContacts", "Contact").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyContacts", "Contact").rowAtPoint(e.getPoint()) != -1) {
                    selectedContactRow = EBISystem.builder().table("companyContacts", "Contact")
                            .convertRowIndexToModel(EBISystem.builder().table("companyContacts", "Contact").rowAtPoint(e.getPoint()));
                }
                if (selectedContactRow < 0 || "".
                        equals(tableModel.getValueAt(selectedContactRow, 0))) {
                    return;
                }
                editContact();
            }
        });

        EBISystem.builder().table("contactTableAddress", "Contact").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedAddressRow = EBISystem.builder().table("contactTableAddress", "Contact")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteContactAddress", "Contact").setEnabled(false);
                } else if (!addressModel.data[selectedAddressRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("deleteContactAddress", "Contact").setEnabled(true);
                }
            }
        });
    }

    public void initialize(boolean reload) {
        if (reload) {
            addressModel = new ModelCRMAddress();
            tableModel = new ModelCRMContact(ModelCRMContact.CRM_CONTACT);
            EBISystem.builder().table("contactTableAddress", "Contact").setModel(addressModel);
            EBISystem.builder().table("companyContacts", "Contact").setModel(tableModel);
        }

        EBISystem.builder().vpanel("Contact").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Contact").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Contact").setChangedDate("");
        EBISystem.builder().vpanel("Contact").setChangedFrom("");
        EBISystem.builder().combo("genderTex", "Contact").setModel(new DefaultComboBoxModel(gendersList));
        EBISystem.builder().combo("genderTex", "Contact").setEditable(true);
        EBISystem.builder().textField("positionText", "Contact").setText("");
        EBISystem.builder().textField("nameText", "Contact").setText("");
        EBISystem.builder().textField("titleText", "Contact").setText("");

        EBISystem.builder().textField("surnameText", "Contact").setText("");
        EBISystem.builder().textField("middleNameText", "Contact").setText("");
        EBISystem.builder().timePicker("birthdateText", "Contact").setFormats(EBISystem.DateFormat);
        EBISystem.builder().timePicker("birthdateText", "Contact").setDate(new Date());
        EBISystem.builder().timePicker("birthdateText", "Contact").getEditor().setText("");
        EBISystem.builder().textField("telefonText", "Contact").setText("");
        EBISystem.builder().textField("faxText", "Contact").setText("");
        EBISystem.builder().textField("mobileText", "Contact").setText("");
        EBISystem.builder().textField("emailText", "Contact").setText("");
        EBISystem.builder().textArea("contactDescription", "Contact").setText("");
        EBISystem.builder().getCheckBox("mainContactText", "Contact").setSelected(false);
    }

    public boolean saveContact() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Contact");
        boolean newRecord = controlContact.isEdit ? false : true;
        controlContact.dataStore();
        controlContact.dataShow(newRecord);
        controlContact.showCompanyContactAddress();
        return true;
    }

    public void editContact() {
        if (selectedContactRow < 0 || "".
                equals(tableModel.getValueAt(selectedContactRow, 0))) {
            return;
        }
        EBISystem.showInActionStatus("Contact");
        controlContact.dataNew();

        controlContact.dataEdit(tableModel.getId(selectedContactRow));
        controlContact.showCompanyContactAddress();
        controlContact.isEdit = true;
    }

    public void editRemoteContact(final int id) {
        EBISystem.showInActionStatus("Contact");
        controlContact.dataNew();
        controlContact.dataEdit(id);
        controlContact.showCompanyContactAddress();
        controlContact.isEdit = true;
    }

    public void copyContact() {
        if (selectedContactRow < 0 || "".
                equals(tableModel.getValueAt(selectedContactRow, 0))) {
            return;
        }
        EBISystem.showInActionStatus("Contact");
        Integer id = controlContact.dataCopy(tableModel.getId(selectedContactRow));
        controlContact.dataEdit(id);
        controlContact.dataShow(true);
        controlContact.showCompanyContactAddress();
        controlContact.isEdit = true;
    }

    public void deleteContact() {
        if (selectedContactRow < 0 || "".
                equals(tableModel.getValueAt(selectedContactRow, 0))) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Contact");
            controlContact.dataDelete(tableModel.getId(selectedContactRow));
            controlContact.dataShow(true);
            controlContact.showCompanyContactAddress();
            controlContact.dataNew();
            controlContact.isEdit = false;
        }
    }

    public void newContact() {
        EBISystem.showInActionStatus("Contact");
        controlContact.dataNew();
        controlContact.dataShow(false);
        controlContact.showCompanyContactAddress();
        controlContact.isEdit = false;
    }

    public void addContactAddress() {
        final EBIAddressSelectionDialog daddress
                = new EBIAddressSelectionDialog(EBISystem.getModule().
                        getAddressPane().getAddressDataControl()
                        .getAddressList(), controlContact.getCoaddressList());

        daddress.setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        if (EBISystem.builder().combo("genderTex", "Contact").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("surnameText", "Contact").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void removeAddress() {
        if (selectedAddressRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(addressModel.data[selectedAddressRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Contact");
            controlContact.dataAddressDelete(Integer.parseInt(addressModel.data[selectedAddressRow][tableModel.columnNames.length].toString()));
            controlContact.showCompanyContactAddress();
        }
    }

    public void historyContact() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Contact")).setVisible();
    }

    public int getSelectedContactRow() {
        return selectedContactRow;
    }

    public void setSelectedContactRow(int selectedContactRow) {
        this.selectedContactRow = selectedContactRow;
    }

    public int getSelectedAddressRow() {
        return selectedAddressRow;
    }

    public void setSelectedAddressRow(int selectedAddressRow) {
        this.selectedAddressRow = selectedAddressRow;
    }
}
