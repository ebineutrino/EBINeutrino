package org.modules.views;

import org.modules.controls.ControlBank;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelCRMBankdata;
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

public class EBICRMBankView {

    @Getter
    @Setter
    private ModelCRMBankdata tabModel = null;
    @Getter
    @Setter
    private ControlBank bankDataControl = new ControlBank();
    private int selectedRow = -1;

    public void initializeAction() {
        EBISystem.builder().textField("filterTableText", "Bank").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("companyBankTable", "Bank").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Bank").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("companyBankTable", "Bank").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Bank").getText()));
            }
        });

        EBISystem.builder().table("companyBankTable", "Bank").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("companyBankTable", "Bank").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (EBISystem.builder().table("companyBankTable", "Bank").getSelectedRow() != -1) {
                    selectedRow = EBISystem.builder().table("companyBankTable", "Bank").convertRowIndexToModel(EBISystem.builder().table("companyBankTable", "Bank").getSelectedRow());

                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("editBank", "Bank").setEnabled(false);
                        EBISystem.builder().button("deleteBank", "Bank").setEnabled(false);
                        EBISystem.builder().button("historyBank", "Bank").setEnabled(false);
                        EBISystem.builder().button("copyBank", "Bank").setEnabled(false);
                    } else if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
                        EBISystem.builder().button("editBank", "Bank").setEnabled(true);
                        EBISystem.builder().button("deleteBank", "Bank").setEnabled(true);
                        EBISystem.builder().button("historyBank", "Bank").setEnabled(true);
                        EBISystem.builder().button("copyBank", "Bank").setEnabled(true);

                    }
                }
            }
        });

        EBISystem.builder().table("companyBankTable", "Bank").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedRow = selRow;
                editBank();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedRow = selRow;
                editBank();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedRow = selRow;
                editBank();
            }
        });

        EBISystem.builder().table("companyBankTable", "Bank").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyBankTable", "Bank").rowAtPoint(e.getPoint()) != -1) {
                    selectedRow = EBISystem.builder().table("companyBankTable", "Bank").convertRowIndexToModel(EBISystem.builder().table("companyBankTable", "Bank").rowAtPoint(e.getPoint()));
                }
                editBank();
            }
        });
    }

    public void initialize(boolean reload) {
        if (reload) {
            tabModel = new ModelCRMBankdata();
            EBISystem.builder().table("companyBankTable", "Bank").setModel(tabModel);
        }
        EBISystem.builder().vpanel("Bank").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.builder().vpanel("Bank").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Bank").setChangedDate("");
        EBISystem.builder().vpanel("Bank").setChangedFrom("");

        EBISystem.builder().textField("bankNameText", "Bank").setText("");
        EBISystem.builder().textField("abaNrText", "Bank").setText("");
        EBISystem.builder().textField("accountNrText", "Bank").setText("");
        EBISystem.builder().textField("bicText", "Bank").setText("");
        EBISystem.builder().textField("ibanText", "Bank").setText("");
        EBISystem.builder().textField("countryBankText", "Bank").setText("");
    }

    public void newBank() {
        EBISystem.showInActionStatus("Bank");
        bankDataControl.dataNew();
        bankDataControl.setEdit(false);
    }

    public void editBank() {
        if ((selectedRow < 0
                || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString()))) {
            return;
        }
        EBISystem.showInActionStatus("Bank");
        bankDataControl.dataNew();
        bankDataControl.dataEdit(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
        bankDataControl.setEdit(true);
    }

    public void copyBank() {
        if ((selectedRow < 0
                || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString()))) {
            return;
        }
        EBISystem.showInActionStatus("Bank");
        Integer id = bankDataControl.dataCopy(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
        bankDataControl.dataEdit(id);
        bankDataControl.dataShow(id);
        bankDataControl.setEdit(true);
    }

    public boolean saveBank() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Bank");
        int row = EBISystem.builder().table("companyBankTable", "Bank").getSelectedRow();
        Integer id = bankDataControl.dataStore();
        bankDataControl.dataShow(id);
        EBISystem.builder().table("companyBankTable", "Bank").changeSelection(row, 0, false, false);
        return true;
    }

    public void deleteBank() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Bank");
            bankDataControl.dataDelete(Integer.parseInt(tabModel.data[selectedRow][6].toString()));
            bankDataControl.dataNew();
            bankDataControl.dataShow(-1);
            bankDataControl.setEdit(false);
        }
    }

    public void historyBank() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Bankdata")).setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("bankNameText", "Bank").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_BANK_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("abaNrText", "Bank").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_BANK_CODE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("accountNrText", "Bank").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_BANK_NR")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
