package org.modules.views;

import org.modules.controls.ControlOrder;
import org.modules.views.dialogs.EBICRMAddContactAddressType;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIOfferSelectionDialog;
import org.modules.models.ModelCRMProduct;
import org.modules.models.ModelDoc;
import org.modules.models.ModelOrder;
import org.modules.models.ModelReceiver;
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
import java.awt.event.*;
import lombok.Getter;
import lombok.Setter;

public class EBICRMOrderView {
    @Getter @Setter
    private ModelDoc tabModDoc = null;
    @Getter @Setter
    private ModelReceiver tabModReceiver = null;
    @Getter @Setter
    private ModelCRMProduct tabModProduct = null;
    @Getter @Setter
    private ModelOrder tabModOrder = null;
    public static String[] orderStatus = null;
    @Getter @Setter
    private ControlOrder dataControlOrder = new ControlOrder();
    @Getter @Setter
    private int selectedOrderRow = -1;
    @Getter @Setter
    private int selectedDocRow = -1;
    @Getter @Setter
    private int selectedReceiverRow = -1;
    @Getter @Setter
    private int selectedProductRow = -1;

    public void initializeAction() {
        EBISystem.gui().textField("filterTableText", "Order").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("companyorderTable", "Order").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Order").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("companyorderTable", "Order").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Order").getText()));
            }
        });

        /**
         * ***********************************************************************************
         */
        //  ORDER TABLE DOCUMENT
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("tableorderDocument", "Order").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableorderDocument", "Order").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("tableorderDocument", "Order").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showorderDoc", "Order").setEnabled(false);
                    EBISystem.gui().button("deleteorderDoc", "Order").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showorderDoc", "Order").setEnabled(true);
                    EBISystem.gui().button("deleteorderDoc", "Order").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  ORDER TABLE PRODUCT
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("tableorderProduct", "Order").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableorderProduct", "Order").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.gui().table("tableorderProduct", "Order").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (tabModProduct.data.length > 0) {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("deleteorderProduct", "Order").setEnabled(false);
                    } else if (!tabModProduct.data[selectedProductRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.gui().button("deleteorderProduct", "Order").setEnabled(true);
                    }
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  ORDER TABLE RECEIVER
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("tableOrderReceiver", "Order").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableOrderReceiver", "Order").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedReceiverRow = EBISystem.gui().table("tableOrderReceiver", "Order").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteorderReceiver", "Order").setEnabled(false);
                    EBISystem.gui().button("editOrderReceiver", "Order").setEnabled(false);
                } else if (!tabModReceiver.data[selectedReceiverRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteorderReceiver", "Order").setEnabled(true);
                    EBISystem.gui().button("editOrderReceiver", "Order").setEnabled(true);
                }
            }
        });

        /**
         * ***********************************************************************************
         */
        //  AVAILABLE ORDER TABLE 
        /**
         * ***********************************************************************************
         */
        EBISystem.gui().table("companyorderTable", "Order").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jTableAvalOrder.setDefaultRenderer(Object.class, new MyOwnCellRederer(false));
        EBISystem.gui().table("companyorderTable", "Order").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedOrderRow = EBISystem.gui().table("companyorderTable", "Order").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editorder", "Order").setEnabled(false);
                    EBISystem.gui().button("reportorder", "Order").setEnabled(false);
                    EBISystem.gui().button("deleteorder", "Order").setEnabled(false);
                    EBISystem.gui().button("historyorder", "Order").setEnabled(false);
                    EBISystem.gui().button("mailOrder", "Order").setEnabled(false);
                    EBISystem.gui().button("createService", "Order").setEnabled(false);
                    EBISystem.gui().button("createInvoice", "Order").setEnabled(false);
                    EBISystem.gui().button("copyorder", "Order").setEnabled(false);
                } else if (!tabModOrder.data[selectedOrderRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editorder", "Order").setEnabled(true);
                    EBISystem.gui().button("reportorder", "Order").setEnabled(true);
                    EBISystem.gui().button("deleteorder", "Order").setEnabled(true);
                    EBISystem.gui().button("historyorder", "Order").setEnabled(true);
                    EBISystem.gui().button("mailOrder", "Order").setEnabled(true);
                    EBISystem.gui().button("createService", "Order").setEnabled(true);
                    EBISystem.gui().button("createInvoice", "Order").setEnabled(true);
                    EBISystem.gui().button("copyorder", "Order").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("companyorderTable", "Order").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedOrderRow = selRow;
                editOrder();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedOrderRow = selRow;
                editOrder();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedOrderRow = selRow;
                if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModOrder.data[selectedOrderRow][0].toString())) {
                    return;
                }
                editOrder();
            }
        });

        EBISystem.gui().table("companyorderTable", "Order").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("companyorderTable", "Order").rowAtPoint(e.getPoint()) != -1) {
                    selectedOrderRow = EBISystem.gui().table("companyorderTable", "Order")
                            .convertRowIndexToModel(EBISystem.gui().table("companyorderTable", "Order").rowAtPoint(e.getPoint()));
                }
                if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModOrder.data[selectedOrderRow][0].toString())) {
                    return;
                }
                editOrder();
            }
        });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModDoc = new ModelDoc();
            tabModReceiver = new ModelReceiver();
            tabModProduct = new ModelCRMProduct();
            tabModOrder = new ModelOrder();
            EBISystem.gui().table("tableorderDocument", "Order").setModel(tabModDoc);
            EBISystem.gui().table("tableorderProduct", "Order").setModel(tabModProduct);
            EBISystem.gui().table("tableOrderReceiver", "Order").setModel(tabModReceiver);
            EBISystem.gui().table("companyorderTable", "Order").setModel(tabModOrder);
        }

        EBISystem.gui().combo("orderStatusText", "Order").setModel(new javax.swing.DefaultComboBoxModel(orderStatus));

        EBISystem.gui().vpanel("Order").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.gui().vpanel("Order").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Order").setChangedDate("");
        EBISystem.gui().vpanel("Order").setChangedFrom("");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.gui().table("tableorderProduct", "Order").getColumnModel().getColumn(5);
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

        EBISystem.gui().combo("orderStatusText", "Order").setSelectedIndex(0);

        EBISystem.gui().textField("orderNrText", "Order").setText("");
        EBISystem.gui().textField("orderNameText", "Order").setText("");
        EBISystem.gui().textField("orderOfferText", "Order").setText("");
        EBISystem.gui().textArea("orderDescription", "Order").setText("");

        EBISystem.gui().timePicker("orderCreatedText", "Order").setDate(null);
        EBISystem.gui().timePicker("orderCreatedText", "Order").setFormats(EBISystem.DateFormat);

        EBISystem.gui().timePicker("orderReceiveText", "Order").setDate(null);
        EBISystem.gui().timePicker("orderReceiveText", "Order").setFormats(EBISystem.DateFormat);
    }

    public void newDocs() {
        dataControlOrder.dataNewDoc();
        EBISystem.showInActionStatus("Order");
        dataControlOrder.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlOrder.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }

    public void newOrder() {
        EBISystem.showInActionStatus("Order");
        dataControlOrder.dataNew();
        dataControlOrder.dataShowDoc();
        dataControlOrder.dataShowProduct();
        dataControlOrder.dataShowReceiver();
        dataControlOrder.isEdit = false;
    }

    public boolean saveOrder() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Order");
        int row = EBISystem.gui().table("companyorderTable", "Order").getSelectedRow();
        Integer id = dataControlOrder.dataStore();
        dataControlOrder.dataShow(id);
        dataControlOrder.dataShowProduct();
        dataControlOrder.dataShowDoc();
        dataControlOrder.dataShowReceiver();
        dataControlOrder.isEdit = true;
        EBISystem.gui().table("companyorderTable", "Order").changeSelection(row, 0, false, false);
        return true;
    }

    public void copyOrder() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Order");
        Integer orderID = dataControlOrder.dataCopy(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
        dataControlOrder.dataEdit(orderID);
        dataControlOrder.dataShow(orderID);
        dataControlOrder.dataShowProduct();
        dataControlOrder.dataShowDoc();
        dataControlOrder.dataShowReceiver();
        dataControlOrder.isEdit = true;
    }

    public void editOrder() {
        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        editOrderRemote(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
    }

    public void editOrderRemote(final int id) {
        if (id < 0) {
            return;
        }
        EBISystem.showInActionStatus("Order");
        dataControlOrder.dataNew();
        dataControlOrder.dataEdit(id);
        dataControlOrder.dataShowDoc();
        dataControlOrder.dataShowProduct();
        dataControlOrder.dataShowReceiver();
        dataControlOrder.isEdit = true;
    }

    public void deleteOrder() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Order");
            dataControlOrder.dataDelete(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
            dataControlOrder.dataNew();
            dataControlOrder.dataShow(-1);
            dataControlOrder.dataShowDoc();
            dataControlOrder.dataShowProduct();
            dataControlOrder.dataShowReceiver();
            dataControlOrder.isEdit = false;
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("orderNrText", "Order").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.gui().combo("orderStatusText", "Order").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (dataControlOrder.isEdit == false) {
            for (int i = 0; i < this.tabModOrder.data.length; i++) {
                if (this.tabModOrder.data[i][0].equals(EBISystem.gui().textField("orderNrText", "Order").getText())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_ORDER_EXIST_WITH_SAME_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                    ret = false;
                }
            }
        }
        return ret;
    }

    public void showProduct() {
        dataControlOrder.dataShowProduct();
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlOrder.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
            EBISystem.showInActionStatus("Order");
            dataControlOrder.dataShowDoc();
        }
    }

    public void deleteReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlOrder.dataDeleteReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][11].toString()));
            EBISystem.showInActionStatus("Order");
            dataControlOrder.dataShowDoc();
        }
    }

    public void newReceiver() {
        final EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(dataControlOrder);
        addCo.setVisible();
    }

    public void editReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        dataControlOrder.dataEditReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][11].toString()));
        dataControlOrder.dataShowReceiver();
    }

    public void newProduct() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlOrder.getCompOrder());
        product.setVisible();
    }

    public void deleteProduct() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Order");
            dataControlOrder.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
            dataControlOrder.dataShowProduct();
        }
    }

    public void showOrderReport() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }

        if (dataControlOrder.getCompOrder().getCompanyorderpositionses() != null
                && dataControlOrder.getCompOrder().getCompanyorderpositionses().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }

        if (dataControlOrder.getCompOrder().getCompanyorderreceivers() != null
                && dataControlOrder.getCompOrder().getCompanyorderreceivers().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_CONTACT")).Show(EBIMessage.ERROR_MESSAGE);
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
            EBISystem.showInActionStatus("Order");
            dataControlOrder.dataShowReport(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
        }
    }

    public void createService() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        dataControlOrder.createServiceFromOrder(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
    }

    public void createInvoice() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        dataControlOrder.createInvoiceFromOrder(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
    }

    public void mailOrder() {
        if (selectedOrderRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModOrder.data[selectedOrderRow][0].toString())) {
            return;
        }
        EBISystem.gui().loadGUI("CRMDialog/sendMailDialogGUI.xml");
        EBISystem.gui().dialog("sendEMailMessage").setTitle(EBISystem.i18n("EBI_LANG_DIALOG_SEND_EMAIL_MESSAGE"));
        EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SHOW_REPORT_BEFORE_SEND"));
        EBISystem.gui().label("SubjectEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SUBJECT"));
        EBISystem.gui().label("template", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_TEMPLATE"));
        EBISystem.gui().combo("templateText", "sendEMailMessage").setModel(new DefaultComboBoxModel(EBISystem.getModule().dynMethod.getEMailTemplateNames()));
        EBISystem.gui().combo("templateText", "sendEMailMessage").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(
                        EBISystem.gui().combo("templateText", "sendEMailMessage").getSelectedItem().toString())) {

                    EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage").setText(
                            EBISystem.getModule().dynMethod.getEMailTemplate(
                                    EBISystem.gui().combo("templateText", "sendEMailMessage")
                                            .getSelectedItem().toString()));
                }
            }
        });
        EBISystem.gui().button("sendEmail", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SEND"));
        EBISystem.gui().button("closeEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        mailOrder(Integer.parseInt(tabModOrder.data[selectedOrderRow][7].toString()));
        EBISystem.gui().showGUI();
    }

    public void searchOffer() {
        final EBIOfferSelectionDialog dialog
                = new EBIOfferSelectionDialog(EBISystem.getModule()
                        .getOfferPane().getDataControlOffer().getOfferList());
        dialog.setVisible();
        if (dialog.shouldSave) {
            EBISystem.gui().textField("orderOfferText", "Order").setText(dialog.name);
            dataControlOrder.setOfferID(dialog.id);
        }
    }

    public void showOffer() {
        if (!"".equals(EBISystem.gui().textField("orderOfferText", "Order").getText())) {
            EBISystem.getModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_OFFER")));
            EBISystem.getModule().getOfferPane().editOfferRemote(Integer.parseInt(tabModOrder.data[selectedOrderRow][3].toString()));
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NO_OFFER_SELECTED")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
    }

    public void historyOrder() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Order")).setVisible();
    }

    public void mailOrder(final int id) {
        EBISystem.gui().button("sendEmail", "sendEMailMessage").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                boolean pass;
                if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                        || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
                    pass = true;
                } else {
                    pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
                }
                if (pass) {
                    if (!validateEMailInput()) {
                        return;
                    }
                    EBISystem.showInActionStatus("Order");
                    final String fileName = dataControlOrder.dataShowAndMailReport(id, EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
                    //todo send report using filename as attachment
                    EBISystem.gui().dialog("sendEMailMessage").setVisible(false);
                }
            }
        });
        EBISystem.gui().button("closeEMailDialog", "sendEMailMessage").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("sendEMailMessage").setVisible(false);
            }
        });
    }

    private boolean validateEMailInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("SubjectText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_SUBJECT")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_BODY_TEXT")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
