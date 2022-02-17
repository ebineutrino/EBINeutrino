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
        EBISystem.getInstance().getIEBIBuilderInstance().textField("filterTableText", "Address").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.getInstance().getIEBIBuilderInstance().table("companyAddess", "Address")
                        .setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder()
                                .textField("filterTableText", "Address").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.getInstance().getIEBIBuilderInstance().table("companyAddess", "Address")
                        .setRowFilter(RowFilters
                                .regexFilter("(?i)" + EBISystem.getInstance().getIEBIBuilderInstance()
                                        .textField("filterTableText", "Address").getText()));
            }
        });

        EBISystem.builder().table("companyAddess", "Address").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("companyAddess", "Address").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedRow = EBISystem.builder().table("companyAddess", "Address")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (tabModel.data.length > selectedRow) {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("editAddress", "Address").setEnabled(false);
                        EBISystem.builder().button("deleteAddress", "Address").setEnabled(false);
                        EBISystem.builder().button("historyAddress", "Address").setEnabled(false);
                        EBISystem.builder().button("copyAddress", "Address").setEnabled(false);
                    } else if (!tabModel.data[selectedRow][0].toString()
                            .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.builder().button("editAddress", "Address").setEnabled(true);
                        EBISystem.builder().button("deleteAddress", "Address").setEnabled(true);
                        EBISystem.builder().button("historyAddress", "Address").setEnabled(true);
                        EBISystem.builder().button("copyAddress", "Address").setEnabled(true);
                    }
                }
            }
        });

        EBISystem.builder().table("companyAddess", "Address").addKeyAction(new EBIUICallback() {
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

        EBISystem.builder().table("companyAddess", "Address").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyAddess", "Address")
                        .rowAtPoint(e.getPoint()) != -1) {
                    selectedRow = EBISystem.builder().table("companyAddess", "Address")
                            .convertRowIndexToModel(EBISystem.builder()
                                    .table("companyAddess", "Address").rowAtPoint(e.getPoint()));
                }
                editAddress();
            }
        });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModel = new ModelCRMAddress();
            EBISystem.builder().table("companyAddess", "Address").setModel(tabModel);
        }

        EBISystem.builder().vpanel("Address").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.builder().vpanel("Address").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Address").setChangedDate("");
        EBISystem.builder().vpanel("Address").setChangedFrom("");
        EBISystem.builder().combo("addressTypeText", "Address").setModel(new javax.swing.DefaultComboBoxModel(AddressType));
        EBISystem.builder().combo("addressTypeText", "Address").setEditable(true);
        EBISystem.builder().textField("streetText", "Address").setText("");
        EBISystem.builder().textField("zipText", "Address").setText("");
        EBISystem.builder().textField("LocationText", "Address").setText("");
        EBISystem.builder().textField("postcodeText", "Address").setText("");
        EBISystem.builder().textField("countryText", "Address").setText("");
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
        addressDataControl.isEdit = true;
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
        int row = EBISystem.builder().table("companyAddess", "Address").getSelectedRow();
        Integer id = addressDataControl.dataStore();
        addressDataControl.dataShow(id);
        EBISystem.builder().table("companyAddess", "Address").changeSelection(row, 0, false, false);
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
        if (EBISystem.builder().combo("addressTypeText", "Address").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_ADRESS_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.builder().textField("streetText", "Address").getText().equals("")) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_STREET"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("LocationText", "Address").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_LOCATION"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("zipText", "Address").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_ZIP"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
