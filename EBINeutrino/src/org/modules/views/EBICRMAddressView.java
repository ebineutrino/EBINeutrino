package org.modules.views;

import org.modules.controls.ControlAddress;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelCRMAddress;
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
import lombok.Getter;
import lombok.Setter;

public class EBICRMAddressView {

    @Getter
    @Setter
    private ModelCRMAddress tabModel = null;
    public static String[] AddressType = null;
    @Getter
    @Setter
    private ControlAddress addressDataControl = new ControlAddress();
    @Getter
    @Setter
    private int selectedRow = -1;

    public void initializeAction() {
        EBISystem.getInstance().getIEBIGUIRendererInstance().textField("filterTableText", "Address").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.getInstance().getIEBIGUIRendererInstance().table("companyAddess", "Address")
                        .setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui()
                                .textField("filterTableText", "Address").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.getInstance().getIEBIGUIRendererInstance().table("companyAddess", "Address")
                        .setRowFilter(RowFilters
                                .regexFilter("(?i)" + EBISystem.getInstance().getIEBIGUIRendererInstance()
                                        .textField("filterTableText", "Address").getText()));
            }
        });

        EBISystem.gui().table("companyAddess", "Address").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("companyAddess", "Address").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    if (lsm.getMinSelectionIndex() < tabModel.data.length) {
                        selectedRow = EBISystem.gui().table("companyAddess", "Address")
                                .convertRowIndexToModel(lsm.getMinSelectionIndex());
                    }
                }

                if (tabModel.data.length > selectedRow) {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("editAddress", "Address").setEnabled(false);
                        EBISystem.gui().button("deleteAddress", "Address").setEnabled(false);
                        EBISystem.gui().button("historyAddress", "Address").setEnabled(false);
                        EBISystem.gui().button("copyAddress", "Address").setEnabled(false);
                    } else if (!tabModel.data[selectedRow][0].toString()
                            .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.gui().button("editAddress", "Address").setEnabled(true);
                        EBISystem.gui().button("deleteAddress", "Address").setEnabled(true);
                        EBISystem.gui().button("historyAddress", "Address").setEnabled(true);
                        EBISystem.gui().button("copyAddress", "Address").setEnabled(true);
                    }
                }
            }
        });

        EBISystem.gui().table("companyAddess", "Address").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedRow = selRow;
                editAddress();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedRow = selRow;
                editAddress();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedRow = selRow;
                if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString())) {
                    return;
                }
                editAddress();
            }
        });

        EBISystem.gui().table("companyAddess", "Address").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("companyAddess", "Address")
                        .rowAtPoint(e.getPoint()) != -1) {
                    selectedRow = EBISystem.gui().table("companyAddess", "Address")
                            .convertRowIndexToModel(EBISystem.gui()
                                    .table("companyAddess", "Address").rowAtPoint(e.getPoint()));
                }
                editAddress();
            }
        });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModel = new ModelCRMAddress();
            EBISystem.gui().table("companyAddess", "Address").setModel(tabModel);
        }

        EBISystem.gui().vpanel("Address").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.gui().vpanel("Address").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Address").setChangedDate("");
        EBISystem.gui().vpanel("Address").setChangedFrom("");
        EBISystem.gui().combo("addressTypeText", "Address").setModel(new javax.swing.DefaultComboBoxModel(AddressType));
        EBISystem.gui().combo("addressTypeText", "Address").setEditable(true);
        EBISystem.gui().textField("streetText", "Address").setText("");
        EBISystem.gui().textField("zipText", "Address").setText("");
        EBISystem.gui().textField("LocationText", "Address").setText("");
        EBISystem.gui().textField("postcodeText", "Address").setText("");
        EBISystem.gui().textField("countryText", "Address").setText("");
    }

    public void editAddress() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Address");
        addressDataControl.dataNew();
        addressDataControl.dataEdit(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
        addressDataControl.isEdit = true;
    }

    public void copyAddress() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Address");
        Integer id = addressDataControl.dataCopy(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
        addressDataControl.dataEdit(id);
        addressDataControl.dataShow(id);
    }

    public void deleteAddress() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Address");
        addressDataControl.dataDelete(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
        addressDataControl.dataNew();
        addressDataControl.dataShow(-1);
        addressDataControl.isEdit = false;
    }

    public void historyAddress() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.
                retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Address")).setVisible();
    }

    public boolean saveAddress() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Address");
        int row = EBISystem.gui().table("companyAddess", "Address").getSelectedRow();
        Integer id = addressDataControl.dataStore();
        addressDataControl.isEdit = true;
        addressDataControl.dataShow(id);
        EBISystem.gui().table("companyAddess", "Address").changeSelection(row, 0, false, false);
        return true;
    }

    public void newAddress() {
        EBISystem.showInActionStatus("Address");
        addressDataControl.dataNew();
        addressDataControl.dataShow(-1);
        addressDataControl.isEdit = false;
    }

    private boolean validateInput() {
        boolean ret = true;
        if (EBISystem.gui().combo("addressTypeText", "Address").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_ADRESS_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.gui().textField("streetText", "Address").getText().equals("")) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_STREET"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().textField("LocationText", "Address").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_LOCATION"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().textField("zipText", "Address").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_ZIP"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
