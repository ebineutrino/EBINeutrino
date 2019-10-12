package org.modules.views;

import org.modules.controls.ControlProblemSolution;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelDoc;
import org.modules.models.ModelProblemSolution;
import org.modules.models.ModelProsolPosition;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMProblemSolutionView {

    @Getter @Setter
    private ModelDoc tabModDoc = null;
    @Getter @Setter
    private ModelProsolPosition tabModProduct = null;
    @Getter @Setter
    private ModelProblemSolution tabModProsol = null;
    public static String[] prosolStatus = null;
    public static String[] prosolType = null;
    public static String[] prosolCategory = null;
    public static String[] prosolClassification = null;
    @Getter @Setter
    public ControlProblemSolution dataControlProsol = null;
    @Getter @Setter
    private int selectedprosolRow = -1;
    @Getter @Setter
    private int selectedDocRow = -1;
    @Getter @Setter
    private int selectedProductRow = -1;

    public void initializeAction() {

        EBISystem.gui().textField("filterTableText", "Prosol").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("prosolTable", "Prosol").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Prosol").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("prosolTable", "Prosol").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Prosol").getText()));
            }
        });

        EBISystem.gui().combo("prosolStatusText", "Prosol").setModel(new DefaultComboBoxModel(prosolStatus));
        EBISystem.gui().combo("prosolTypeText", "Prosol").setModel(new DefaultComboBoxModel(prosolType));
        EBISystem.gui().combo("prosolCategoryText", "Prosol").setModel(new DefaultComboBoxModel(prosolCategory));
        EBISystem.gui().combo("prosolClassificationText", "Prosol").setModel(new DefaultComboBoxModel(prosolClassification));

        /**
         * ***********************************************************************************
         */
        // prosol TABLE DOCUMENT
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("prosolTableDocument", "Prosol").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("prosolTableDocument", "Prosol").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("prosolTableDocument", "Prosol").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showprosolDoc", "Prosol").setEnabled(false);
                    EBISystem.gui().button("deleteprosolDoc", "Prosol").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showprosolDoc", "Prosol").setEnabled(true);
                    EBISystem.gui().button("deleteprosolDoc", "Prosol").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        // prosol TABLE PRODUCT
        /**
         * ***********************************************************************************
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.gui().table("tableprosolProduct", "Prosol").getColumnModel().getColumn(4);
                col7.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                            final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });
            }
        });

        EBISystem.gui().table("tableprosolProduct", "Prosol").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableprosolProduct", "Prosol").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.gui().table("tableprosolProduct", "Prosol")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteprosolProduct", "Prosol").setEnabled(false);
                } else if (!tabModProduct.data[selectedProductRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteprosolProduct", "Prosol").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        // AVAILABLE prosol TABLE
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("prosolTable", "Prosol").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // jTableAvalprosol.setDefaultRenderer(Object.class, new
        // MyOwnCellRederer(false));
        EBISystem.gui().table("prosolTable", "Prosol").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    try {
                        selectedprosolRow = EBISystem.gui().table("prosolTable", "Prosol")
                                .convertRowIndexToModel(lsm.getMinSelectionIndex());
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editprosol", "Prosol").setEnabled(false);
                    EBISystem.gui().button("reportprosol", "Prosol").setEnabled(false);
                    EBISystem.gui().button("deleteprosol", "Prosol").setEnabled(false);
                    EBISystem.gui().button("historyprosol", "Prosol").setEnabled(false);
                    EBISystem.gui().button("copyprosol", "Prosol").setEnabled(false);
                } else if (!tabModProsol.data[selectedprosolRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editprosol", "Prosol").setEnabled(true);
                    EBISystem.gui().button("reportprosol", "Prosol").setEnabled(true);
                    EBISystem.gui().button("deleteprosol", "Prosol").setEnabled(true);
                    EBISystem.gui().button("historyprosol", "Prosol").setEnabled(true);
                    EBISystem.gui().button("copyprosol", "Prosol").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("prosolTable", "Prosol").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedprosolRow = selRow;
                editprosol();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedprosolRow = selRow;
                editprosol();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedprosolRow = selRow;

                if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
                    return;
                }
                editprosol();
            }
        }
        );

        EBISystem.gui().table("prosolTable", "Prosol").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("prosolTable", "Prosol").rowAtPoint(e.getPoint()) > -1) {
                    selectedprosolRow = EBISystem.gui().table("prosolTable", "Prosol").convertRowIndexToModel(
                            EBISystem.gui().table("prosolTable", "Prosol").rowAtPoint(e.getPoint()));
                }
                if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
                    return;
                }
                editprosol();
            }
        });

    }

    public void initialize(boolean reload) {
        if (reload) {
            tabModDoc = new ModelDoc();
            tabModProduct = new ModelProsolPosition();
            tabModProsol = new ModelProblemSolution();
            dataControlProsol = new ControlProblemSolution();
            EBISystem.gui().table("prosolTableDocument", "Prosol").setModel(tabModDoc);
            EBISystem.gui().table("tableprosolProduct", "Prosol").setModel(tabModProduct);
            EBISystem.gui().table("prosolTable", "Prosol").setModel(tabModProsol);
        }

        EBISystem.hibernate().openHibernateSession("PROSOL_SESSION");

        EBISystem.gui().vpanel("Prosol").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Prosol").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Prosol").setChangedFrom("");
        EBISystem.gui().vpanel("Prosol").setChangedDate("");
        EBISystem.gui().textField("prosolNrText", "Prosol").setText("");
        EBISystem.gui().textField("prosolNameText", "Prosol").setText("");
        EBISystem.gui().textArea("prosolDescriptionText", "Prosol").setText("");
        EBISystem.gui().button("showprosolDoc", "Prosol").setEnabled(false);
        EBISystem.gui().button("deleteprosolDoc", "Prosol").setEnabled(false);
        EBISystem.gui().button("deleteprosolProduct", "Prosol").setEnabled(false);
    }

    public void newDocs() {
        dataControlProsol.dataNewDoc();
        EBISystem.showInActionStatus("Prosol");
        dataControlProsol.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlProsol.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }
 

    public void showProsolProduct() {
        dataControlProsol.dataShowProduct();
    }

    public void newprosol() {
        EBISystem.showInActionStatus("Prosol");
        dataControlProsol.isEdit = false;
        dataControlProsol.dataNew();
        dataControlProsol.dataShow(-1);
        dataControlProsol.dataShowDoc();
        dataControlProsol.dataShowProduct();
    }

    public boolean saveprosol() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Prosol");
        Integer id = dataControlProsol.dataStore();
        dataControlProsol.dataShow(id);
        dataControlProsol.dataShowDoc();
        dataControlProsol.dataShowProduct();
        dataControlProsol.isEdit = true;
        return true;
    }

    public void editprosol() {
        if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Prosol");
        dataControlProsol.dataNew();
        dataControlProsol.dataEdit(Integer.parseInt(tabModProsol.data[selectedprosolRow][7].toString()));
        dataControlProsol.dataShowDoc();
        dataControlProsol.dataShowProduct();
        dataControlProsol.isEdit = true;
    }

    public void copyProsol() {
        if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Prosol");
        Integer id = dataControlProsol.dataCopy(Integer.parseInt(tabModProsol.data[selectedprosolRow][7].toString()));
        dataControlProsol.dataEdit(id);
        dataControlProsol.dataShow(id);
        dataControlProsol.dataShowDoc();
        dataControlProsol.dataShowProduct();
    }

    public void deleteprosol() {
        if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Prosol");
            dataControlProsol.dataDelete(Integer.parseInt(tabModProsol.data[selectedprosolRow][7].toString()));
            dataControlProsol.dataNew();
            dataControlProsol.dataShow(-1);
            dataControlProsol.isEdit = false;
        }
    }

    public void mailProsol() {
        new EBICRMHistoryView(
                EBISystem.getModule().hcreator.retrieveDBHistory(
                        Integer.parseInt(tabModProsol.data[selectedprosolRow][7].toString()), "Prosol")).setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("prosolNrText", "Prosol").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.gui().combo("prosolStatusText", "Prosol").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlProsol.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
            EBISystem.showInActionStatus("Prosol");
            dataControlProsol.dataShowDoc();
        }
    }

    public void newProduct() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlProsol.getcompProsol());
        product.setVisible();
    }

    public void deleteProduct() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlProsol.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][6].toString()));
            EBISystem.showInActionStatus("Prosol");
            dataControlProsol.dataShowProduct();
        }
    }

    public void showprosolReport() {
        if (selectedprosolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProsol.data[selectedprosolRow][0].toString())) {
            return;
        }
        if (dataControlProsol.getcompProsol().getCrmproblemsolpositions() != null
                && dataControlProsol.getcompProsol().getCrmproblemsolpositions().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            EBISystem.showInActionStatus("Prosol");
            dataControlProsol.dataShowReport(Integer.parseInt(tabModProsol.data[selectedprosolRow][7].toString()));
        }
    }
}
