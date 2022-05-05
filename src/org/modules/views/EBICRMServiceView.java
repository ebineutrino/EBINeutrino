package org.modules.views;

import org.modules.controls.ControlService;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIProblemSolutionSelectionDialog;
import org.modules.models.ModelCRMProduct;
import org.modules.models.ModelDoc;
import org.modules.models.ModelProblemSolution;
import org.modules.models.ModelService;
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
import lombok.Getter;
import lombok.Setter;

public class EBICRMServiceView {

    @Getter
    @Setter
    private ModelDoc tabModDoc = null;
    @Getter
    @Setter
    private ModelProblemSolution tabModProsol = null;
    @Getter
    @Setter
    private ModelCRMProduct tabModProduct = null;
    @Getter
    @Setter
    private ModelService tabModService = null;

    public static String[] serviceStatus = null;
    public static String[] serviceType = null;
    public static String[] serviceCategory = null;
    @Getter
    @Setter
    private ControlService dataControlService = new ControlService();
    @Getter
    @Setter
    private int selectedServiceRow = -1;
    @Getter
    @Setter
    private int selectedDocRow = -1;
    @Getter
    @Setter
    private int selectedProsolRow = -1;
    @Getter
    @Setter
    private int selectedProductRow = -1;

    public void initializeAction() {
        EBISystem.builder().textField("filterTableText", "Service").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("companyServiceTable", "Service").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Service").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("companyServiceTable", "Service").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Service").getText()));
            }
        });

        /**
         * ***********************************************************************************
         */
        //  Service TABLE DOCUMENT
        /**
         * ***********************************************************************************
         */
        EBISystem.builder().table("tableServiceDocument", "Service").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableServiceDocument", "Service").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.builder().table("tableServiceDocument", "Service").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("showServiceDoc", "Service").setEnabled(false);
                    EBISystem.builder().button("deleteServiceDoc", "Service").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("showServiceDoc", "Service").setEnabled(true);
                    EBISystem.builder().button("deleteServiceDoc", "Service").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  Service TABLE PRODUCT
        /**
         * ***********************************************************************************
         */
        EBISystem.builder().table("tableServiceProduct", "Service").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableServiceProduct", "Service").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.builder().table("tableServiceProduct", "Service").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteServiceProduct", "Service").setEnabled(false);
                } else if (!tabModProduct.data[selectedProductRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("deleteServiceProduct", "Service").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  Service TABLE RECEIVER
        /**
         * ***********************************************************************************
         */
        EBISystem.builder().table("tableServiceProsol", "Service").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableServiceProsol", "Service").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedProsolRow = EBISystem.builder().table("tableServiceProsol", "Service").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteServiceProsol", "Service").setEnabled(false);
                } else if (!tabModProsol.data[selectedProsolRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("deleteServiceProsol", "Service").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  AVAILABLE Service TABLE
        /**
         * ***********************************************************************************
         */
        EBISystem.builder().table("companyServiceTable", "Service").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jTableAvalService.setDefaultRenderer(Object.class, new MyOwnCellRederer(false));
        EBISystem.builder().table("companyServiceTable", "Service").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    try {
                        selectedServiceRow = EBISystem.builder().table("companyServiceTable", "Service").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editService", "Service").setEnabled(false);
                    EBISystem.builder().button("reportService", "Service").setEnabled(false);
                    EBISystem.builder().button("deleteService", "Service").setEnabled(false);
                    EBISystem.builder().button("historyService", "Service").setEnabled(false);
                    EBISystem.builder().button("createInvoice", "Service").setEnabled(false);
                    EBISystem.builder().button("copyService", "Service").setEnabled(false);
                } else if (!tabModService.data[selectedServiceRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editService", "Service").setEnabled(true);
                    EBISystem.builder().button("reportService", "Service").setEnabled(true);
                    EBISystem.builder().button("deleteService", "Service").setEnabled(true);
                    EBISystem.builder().button("historyService", "Service").setEnabled(true);
                    EBISystem.builder().button("createInvoice", "Service").setEnabled(true);
                    EBISystem.builder().button("copyService", "Service").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("companyServiceTable", "Service").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedServiceRow = selRow;
                editService();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedServiceRow = selRow;
                editService();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedServiceRow = selRow;

                if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModService.data[selectedServiceRow][0].toString())) {
                    return;
                }
                editService();
            }
        });

        EBISystem.builder().table("companyServiceTable", "Service").setMouseCallback(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyServiceTable", "Service").rowAtPoint(e.getPoint()) > -1) {
                    selectedServiceRow = EBISystem.builder().table("companyServiceTable", "Service").convertRowIndexToModel(EBISystem.builder().table("companyServiceTable", "Service").rowAtPoint(e.getPoint()));
                }
                if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModService.data[selectedServiceRow][0].toString())) {
                    return;
                }
                editService();
            }
        });
    }

    public void initialize(boolean reload) {
        if (reload) {
            tabModDoc = new ModelDoc();
            tabModProsol = new ModelProblemSolution();
            tabModProduct = new ModelCRMProduct();
            tabModService = new ModelService();
            EBISystem.builder().table("tableServiceDocument", "Service").setModel(tabModDoc);
            EBISystem.builder().table("tableServiceProduct", "Service").setModel(tabModProduct);
            EBISystem.builder().table("tableServiceProsol", "Service").setModel(tabModProsol);
            EBISystem.builder().table("companyServiceTable", "Service").setModel(tabModService);
        }

        EBISystem.builder().combo("serviceStatusText", "Service").setModel(new javax.swing.DefaultComboBoxModel(serviceStatus));
        EBISystem.builder().combo("serviceTypeText", "Service").setModel(new javax.swing.DefaultComboBoxModel(serviceType));
        EBISystem.builder().combo("serviceCategoryText", "Service").setModel(new javax.swing.DefaultComboBoxModel(serviceCategory));

        EBISystem.builder().vpanel("Service").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.builder().vpanel("Service").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Service").setChangedDate("");
        EBISystem.builder().vpanel("Service").setChangedFrom("");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.builder().table("tableServiceProduct", "Service").getColumnModel().getColumn(5);
                col7.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });
            }
        });

        EBISystem.builder().combo("serviceStatusText", "Service").setSelectedIndex(0);
        EBISystem.builder().combo("serviceTypeText", "Service").setSelectedIndex(0);
        EBISystem.builder().combo("serviceCategoryText", "Service").setSelectedIndex(0);

        EBISystem.builder().textField("serviceNrText", "Service").setText("");
        EBISystem.builder().textField("serviceNameText", "Service").setText("");
        EBISystem.builder().textArea("serviceDescriptionText", "Service").setText("");
    }

    public void newDocs() {
        dataControlService.dataNewDoc();
        EBISystem.showInActionStatus("Service");
        dataControlService.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlService.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }

    public void newProsol() {
        final EBIProblemSolutionSelectionDialog addCo = new EBIProblemSolutionSelectionDialog(dataControlService.getserviceProSolList());
        addCo.setVisible();
        EBISystem.showInActionStatus("Service");
        dataControlService.dataShowProblemSolution();
    }

    public void newService() {
        EBISystem.showInActionStatus("Service");
        dataControlService.dataNew();
        dataControlService.dataShowDoc();
        dataControlService.dataShowProduct();
        dataControlService.dataShowProblemSolution();
        dataControlService.isEdit = false;
    }

    public boolean saveService() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Service");
        int row = EBISystem.builder().table("companyServiceTable", "Service").getSelectedRow();
        Integer id = dataControlService.dataStore();
        dataControlService.dataShow(id);
        dataControlService.dataShowDoc();
        dataControlService.dataShowProduct();
        dataControlService.dataShowProblemSolution();
        EBISystem.builder().table("companyServiceTable", "Service").changeSelection(row, 0, false, false);
        return true;
    }

    public void editService() {
        if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModService.data[selectedServiceRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Service");
        dataControlService.dataNew();
        dataControlService.dataEdit(Integer.parseInt(tabModService.data[selectedServiceRow][6].toString()));
        dataControlService.dataShowDoc();
        dataControlService.dataShowProduct();
        dataControlService.dataShowProblemSolution();
        dataControlService.isEdit = true;
    }

    public void remoteEditService(final int id) {
        EBISystem.showInActionStatus("Service");
        dataControlService.dataNew();
        dataControlService.dataEdit(id);
        dataControlService.dataShowDoc();
        dataControlService.dataShowProduct();
        dataControlService.dataShowProblemSolution();
        dataControlService.isEdit = true;
    }

    public void copyService() {
        if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModService.data[selectedServiceRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Service");
        Integer serviceID = dataControlService.dataCopy(Integer.parseInt(tabModService.data[selectedServiceRow][6].toString()));
        dataControlService.dataEdit(serviceID);
        dataControlService.dataShow(serviceID);
        dataControlService.dataShowDoc();
        dataControlService.dataShowProduct();
        dataControlService.dataShowProblemSolution();
        dataControlService.isEdit = true;
    }

    public void deleteService() {
        if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModService.data[selectedServiceRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Service");
            dataControlService.dataDelete(Integer.parseInt(tabModService.data[selectedServiceRow][6].toString()));
            dataControlService.dataNew();
            dataControlService.dataShow(-1);
            dataControlService.dataShowDoc();
            dataControlService.dataShowProduct();
            dataControlService.dataShowProblemSolution();
            dataControlService.isEdit = false;
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("serviceNrText", "Service").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.builder().combo("serviceStatusText", "Service").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (dataControlService.isEdit == false) {
            for (int i = 0; i < this.tabModService.data.length; i++) {
                if (this.tabModService.data[i][0].equals(EBISystem.builder().textField("serviceNrText", "Service").getText())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SERVICE_EXIST_WITH_SAME_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                    ret = false;
                }
            }
        }
        return ret;
    }

    public void historyService() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Service")).setVisible();
    }

    public void createInvoice() {
        if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModService.data[selectedServiceRow][0].toString())) {
            return;
        }
        dataControlService.createInvoiceFromService(Integer.parseInt(tabModService.data[selectedServiceRow][6].toString()));
    }

    public void showService() {
        dataControlService.dataShow(-1);
    }

    public void showProduct() {
        dataControlService.dataShowProduct();
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlService.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
            EBISystem.showInActionStatus("Service");
            dataControlService.dataShowDoc();
        }

    }

    public void deleteProSol() {
        if (selectedProsolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModProsol.data[selectedProsolRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlService.dataDeleteProblemSolution(Integer.parseInt(tabModProsol.data[selectedProsolRow][7].toString()));
            EBISystem.showInActionStatus("Service");
            dataControlService.dataShowProblemSolution();
        }
    }

    public void newProduct() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlService.getcompService());
        product.setVisible();
    }

    public void deleteProduct() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlService.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
            EBISystem.showInActionStatus("Service");
            dataControlService.dataShowProduct();
        }
    }

    public void showServiceReport() {
        if (selectedServiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModService.data[selectedServiceRow][0].toString())) {
            return;
        }
        if (dataControlService.getcompService().getCompanyservicepositionses() != null
                && dataControlService.getcompService().getCompanyservicepositionses().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
        if (dataControlService.getcompService().getCompanyservicepsols() != null
                && dataControlService.getcompService().getCompanyservicepsols().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_SERVICE")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint() || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            dataControlService.dataShowReport(Integer.parseInt(tabModService.data[selectedServiceRow][6].toString()));
        }
    }
}
