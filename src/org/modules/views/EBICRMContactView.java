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

        EBISystem.gui().textField("telefonText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.gui().textField("faxText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.gui().textField("mobileText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.gui().textField("emailText", "Contact").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.EMAIL));

        EBISystem.gui().textField("filterTableText", "Contact").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("companyContacts", "Contact").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Contact").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("companyContacts", "Contact").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Contact").getText()));
            }
        });

        EBISystem.gui().table("companyContacts", "Contact").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("companyContacts", "Contact").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() > -1) {
                    selectedContactRow = EBISystem.gui().
                            table("companyContacts", "Contact").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editContact", "Contact").setEnabled(false);
                    EBISystem.gui().button("deleteContact", "Contact").setEnabled(false);
                    EBISystem.gui().button("historyContact", "Contact").setEnabled(false);
                    EBISystem.gui().button("copyContact", "Contact").setEnabled(false);
                } else if (!tableModel.data[selectedContactRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editContact", "Contact").setEnabled(true);
                    EBISystem.gui().button("deleteContact", "Contact").setEnabled(true);
                    EBISystem.gui().button("historyContact", "Contact").setEnabled(true);
                    EBISystem.gui().button("copyContact", "Contact").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("companyContacts", "Contact").addKeyAction(new EBIUICallback() {
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
                if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tableModel.data[selectedContactRow][0].toString())) {
                    return;
                }
                editContact();
            }
        });

        EBISystem.gui().table("companyContacts", "Contact").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("companyContacts", "Contact").rowAtPoint(e.getPoint()) != -1) {
                    selectedContactRow = EBISystem.gui().table("companyContacts", "Contact")
                            .convertRowIndexToModel(EBISystem.gui().table("companyContacts", "Contact").rowAtPoint(e.getPoint()));
                }
                if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tableModel.data[selectedContactRow][0].toString())) {
                    return;
                }
                editContact();
            }
        });

        EBISystem.gui().table("contactTableAddress", "Contact").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedAddressRow = EBISystem.gui().table("contactTableAddress", "Contact")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteContactAddress", "Contact").setEnabled(false);
                } else if (!addressModel.data[selectedAddressRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteContactAddress", "Contact").setEnabled(true);
                }
            }
        });
    }

    public void initialize(boolean reload) {
        if (reload) {
            addressModel = new ModelCRMAddress();
            tableModel = new ModelCRMContact();
            EBISystem.gui().table("contactTableAddress", "Contact").setModel(addressModel);
            EBISystem.gui().table("companyContacts", "Contact").setModel(tableModel);
        }

        EBISystem.gui().vpanel("Contact").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Contact").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Contact").setChangedDate("");
        EBISystem.gui().vpanel("Contact").setChangedFrom("");
        EBISystem.gui().combo("genderTex", "Contact").setModel(new DefaultComboBoxModel(gendersList));
        EBISystem.gui().combo("genderTex", "Contact").setEditable(true);
        EBISystem.gui().textField("positionText", "Contact").setText("");
        EBISystem.gui().textField("nameText", "Contact").setText("");
        EBISystem.gui().textField("titleText", "Contact").setText("");

        EBISystem.gui().textField("surnameText", "Contact").setText("");
        EBISystem.gui().textField("middleNameText", "Contact").setText("");
        EBISystem.gui().timePicker("birthdateText", "Contact").setFormats(EBISystem.DateFormat);
        EBISystem.gui().timePicker("birthdateText", "Contact").setDate(new Date());
        EBISystem.gui().timePicker("birthdateText", "Contact").getEditor().setText("");
        EBISystem.gui().textField("telefonText", "Contact").setText("");
        EBISystem.gui().textField("faxText", "Contact").setText("");
        EBISystem.gui().textField("mobileText", "Contact").setText("");
        EBISystem.gui().textField("emailText", "Contact").setText("");
        EBISystem.gui().textArea("contactDescription", "Contact").setText("");
        EBISystem.gui().getCheckBox("mainContactText", "Contact").setSelected(false);
    }

    public boolean saveContact() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Contact");
        int row = EBISystem.gui().table("companyContacts", "Contact").getSelectedRow();
        Integer id = controlContact.dataStore();
        controlContact.dataShow(id);
        controlContact.showCompanyContactAddress();
        EBISystem.gui().table("companyContacts", "Contact").changeSelection(row, 0, false, false);
        return true;
    }

    public void editContact() {
        if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedContactRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Contact");
        controlContact.dataNew();
        controlContact.dataEdit(Integer.parseInt(tableModel.data[selectedContactRow][tableModel.columnNames.length].toString()));
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
        if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedContactRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Contact");
        Integer id = controlContact.dataCopy(Integer.parseInt(tableModel.data[selectedContactRow][tableModel.columnNames.length].toString()));
        controlContact.dataEdit(id);
        controlContact.dataShow(id);
        controlContact.showCompanyContactAddress();
        controlContact.isEdit = true;
    }

    public void deleteContact() {
        if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedContactRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Contact");
            controlContact.dataDelete(Integer.parseInt(tableModel.data[selectedContactRow][tableModel.columnNames.length].toString()));
            controlContact.dataShow(-1);
            controlContact.showCompanyContactAddress();
            controlContact.dataNew();
            controlContact.isEdit = false;
        }
    }

    public void newContact() {
        EBISystem.showInActionStatus("Contact");
        controlContact.dataNew();
        controlContact.dataShow(-1);
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
        if (EBISystem.gui().combo("genderTex", "Contact").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().textField("surnameText", "Contact").getText())) {
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
