package org.modules.views;

import org.modules.controls.ControlOffer;
import org.modules.views.dialogs.EBICRMAddContactAddressType;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIOpportunitySelectionDialog;
import org.modules.models.ModelCRMProduct;
import org.modules.models.ModelDoc;
import org.modules.models.ModelOffer;
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
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMOfferView {

    @Getter
    @Setter
    private ModelDoc tabModDoc = null;
    @Getter
    @Setter
    private ModelReceiver tabModReceiver = null;
    @Getter
    @Setter
    private ModelCRMProduct tabModProduct = null;
    @Getter
    @Setter
    private ModelOffer tabModoffer = null;
    public static String[] offerStatus = null;
    @Getter
    @Setter
    private ControlOffer dataControlOffer = new ControlOffer();
    @Getter
    @Setter
    private int selectedDocRow = -1;
    @Getter
    @Setter
    private int selectedReceiverRow = -1;
    @Getter
    @Setter
    private int selectedProductRow = -1;
    @Getter
    @Setter
    private int selectedOfferRow = -1;

    public void initializeAction() {
        EBISystem.builder().textField("filterTableText", "Offer").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("companyOfferTable", "Offer").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Offer").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("companyOfferTable", "Offer").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Offer").getText()));
            }
        });

        /**
         * **********************************************************************************
         */
        //   TABLE OFFER DOCUMENT
        /**
         * **********************************************************************************
         */
        EBISystem.builder().table("tableOfferDocument", "Offer").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableOfferDocument", "Offer").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.builder().table("tableOfferDocument", "Offer").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("showOfferDoc", "Offer").setEnabled(false);
                    EBISystem.builder().button("deleteOfferDOc", "Offer").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("showOfferDoc", "Offer").setEnabled(true);
                    EBISystem.builder().button("deleteOfferDOc", "Offer").setEnabled(true);
                }
            }
        });

        /**
         * **********************************************************************************
         */
        //   TABLE OFFER PRODUCTS
        /**
         * **********************************************************************************
         */
        EBISystem.builder().table("tableOfferProduct", "Offer").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableOfferProduct", "Offer").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.builder().table("tableOfferProduct", "Offer").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteOfferProduct", "Offer").setEnabled(false);
                } else if (!tabModProduct.data[selectedProductRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("deleteOfferProduct", "Offer").setEnabled(true);
                }
            }
        });

        /**
         * **********************************************************************************
         */
        //   TABLE OFFER RECEIVER
        /**
         * **********************************************************************************
         */
        EBISystem.builder().table("tableOfferReceiver", "Offer").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableOfferReceiver", "Offer").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedReceiverRow = EBISystem.builder().table("tableOfferReceiver", "Offer").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteOfferReceiver", "Offer").setEnabled(false);
                    EBISystem.builder().button("editOfferReceiver", "Offer").setEnabled(false);
                } else if (!tabModReceiver.data[selectedReceiverRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("deleteOfferReceiver", "Offer").setEnabled(true);
                    EBISystem.builder().button("editOfferReceiver", "Offer").setEnabled(true);
                }
            }
        });

        /**
         * **********************************************************************************
         */
        //   TABLE AVAILABLE OFFER
        /**
         * **********************************************************************************
         */
        EBISystem.builder().table("companyOfferTable", "Offer").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jTableAvalOffer.setDefaultRenderer(Object.class, new MyOwnCellRederer(true));
        EBISystem.builder().table("companyOfferTable", "Offer").addSelectionListener(new EBIUICallback() {

            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedOfferRow = EBISystem.builder().table("companyOfferTable", "Offer").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editOffer", "Offer").setEnabled(false);
                    EBISystem.builder().button("reportOffer", "Offer").setEnabled(false);
                    EBISystem.builder().button("deleteOffer", "Offer").setEnabled(false);
                    EBISystem.builder().button("historyOffer", "Offer").setEnabled(false);
                    EBISystem.builder().button("mailOffer", "Offer").setEnabled(false);
                    EBISystem.builder().button("createOrder", "Offer").setEnabled(false);
                    EBISystem.builder().button("copyOffer", "Offer").setEnabled(false);
                } else if (!tabModoffer.data[selectedOfferRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editOffer", "Offer").setEnabled(true);
                    EBISystem.builder().button("reportOffer", "Offer").setEnabled(true);
                    EBISystem.builder().button("deleteOffer", "Offer").setEnabled(true);
                    EBISystem.builder().button("historyOffer", "Offer").setEnabled(true);
                    EBISystem.builder().button("mailOffer", "Offer").setEnabled(true);
                    EBISystem.builder().button("createOrder", "Offer").setEnabled(true);
                    EBISystem.builder().button("copyOffer", "Offer").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("companyOfferTable", "Offer").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedOfferRow = selRow;
                editOffer();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedOfferRow = selRow;
                editOffer();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedOfferRow = selRow;
                if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModoffer.data[selectedOfferRow][0].toString())) {
                    return;
                }
                editOffer();
            }
        });

        EBISystem.builder().table("companyOfferTable", "Offer").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyOfferTable", "Offer").rowAtPoint(e.getPoint()) != -1) {
                    selectedOfferRow = EBISystem.builder().table("companyOfferTable", "Offer").convertRowIndexToModel(EBISystem.builder().table("companyOfferTable", "Offer").rowAtPoint(e.getPoint()));
                }
                if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModoffer.data[selectedOfferRow][0].toString())) {
                    return;
                }
                editOffer();
            }
        });
    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModDoc = new ModelDoc();
            tabModReceiver = new ModelReceiver();
            tabModProduct = new ModelCRMProduct();
            tabModoffer = new ModelOffer();
            EBISystem.builder().table("tableOfferDocument", "Offer").setModel(tabModDoc);
            EBISystem.builder().table("tableOfferProduct", "Offer").setModel(tabModProduct);
            EBISystem.builder().table("tableOfferReceiver", "Offer").setModel(tabModReceiver);
            EBISystem.builder().table("companyOfferTable", "Offer").setModel(tabModoffer);
        }

        EBISystem.builder().combo("offerStatusText", "Offer").setModel(new javax.swing.DefaultComboBoxModel(offerStatus));

        EBISystem.builder().vpanel("Offer").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Offer").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Offer").setChangedDate("");
        EBISystem.builder().vpanel("Offer").setChangedFrom("");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.builder().table("tableOfferProduct", "Offer").getColumnModel().getColumn(5);
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

        EBISystem.builder().combo("offerStatusText", "Offer").setSelectedIndex(0);

        EBISystem.builder().textField("offerNrText", "Offer").setText("");
        EBISystem.builder().textField("offerNameText", "Offer").setText("");
        EBISystem.builder().textField("offerOpportunityText", "Offer").setText("");
        EBISystem.builder().textArea("offerDescriptionText", "Offer").setText("");

        EBISystem.builder().timePicker("offerReceiverText", "Offer").setDate(null);
        EBISystem.builder().timePicker("validToText", "Offer").setDate(null);
        EBISystem.builder().timePicker("offerReceiverText", "Offer").setFormats(EBISystem.DateFormat);
        EBISystem.builder().timePicker("validToText", "Offer").setFormats(EBISystem.DateFormat);
    }

    public void newDocs() {
        dataControlOffer.dataAddDoc();
        dataControlOffer.dataShowDoc();
    }

    public void saveAndShowDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlOffer.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }

    public void showProduct() {
        dataControlOffer.dataShowProduct();
    }

    public void newOffer() {
        EBISystem.showInActionStatus("Offer");
        dataControlOffer.dataNew();
        dataControlOffer.dataShowDoc();
        dataControlOffer.dataShowProduct();
        dataControlOffer.dataShowReceiver();
        dataControlOffer.isEdit = false;
    }

    public void copyOffer() {
        if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Offer");
        Integer id = dataControlOffer.dataCopy(Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()));
        dataControlOffer.dataEdit(id);
        dataControlOffer.dataShow(id);
        dataControlOffer.dataShowDoc();
        dataControlOffer.dataShowProduct();
        dataControlOffer.dataShowReceiver();
        dataControlOffer.isEdit = true;
    }

    public boolean saveOffer() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Offer");
        int row = EBISystem.builder().table("companyOfferTable", "Offer").getSelectedRow();
        Integer id = dataControlOffer.dataStore();
        dataControlOffer.dataShow(id);
        dataControlOffer.dataShowProduct();
        dataControlOffer.dataShowDoc();
        dataControlOffer.dataShowReceiver();
        EBISystem.builder().table("companyOfferTable", "Offer").changeSelection(row, 0, false, false);
        return true;
    }

    public void editOffer() {
        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return;
        }
        editOfferRemote(Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()));
    }

    public void editOfferRemote(final int id) {
        if (id < 0) {
            return;
        }
        EBISystem.showInActionStatus("Offer");
        dataControlOffer.dataNew();
        dataControlOffer.dataEdit(id);
        dataControlOffer.dataShowDoc();
        dataControlOffer.dataShowProduct();
        dataControlOffer.dataShowReceiver();
        dataControlOffer.isEdit = true;
    }

    public void deleteOffer() {
        if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Offer");
            dataControlOffer.dataDelete(Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()));
            dataControlOffer.dataNew();
            dataControlOffer.dataShow(-1);
            dataControlOffer.dataShowProduct();
            dataControlOffer.dataShowReceiver();
            dataControlOffer.dataShowDoc();
            dataControlOffer.isEdit = false;
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("offerNameText", "Offer").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.builder().combo("offerStatusText", "Offer").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (dataControlOffer.isEdit == false) {
            for (int i = 0; i < this.tabModoffer.data.length; i++) {
                if (this.tabModoffer.data[i][0].equals(EBISystem.builder().textField("offerNameText", "Offer").getText())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_OFFER_EXIST_WITH_SAME_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                    ret = false;
                }
            }
        }
        return ret;
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Offer");
            dataControlOffer.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][0].toString()));
            dataControlOffer.dataShowDoc();
        }
    }

    public void newReceiver() {
        EBISystem.showInActionStatus("Offer");
        final EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(dataControlOffer);
        addCo.setVisible();
    }

    public void deleteReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Offer");
            dataControlOffer.dataDeleteReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][11].toString()));
            dataControlOffer.dataShowReceiver();
        }
    }

    public void newProduct() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlOffer.getCompOffer());
        product.setVisible();
    }

    public void editReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Offer");
        dataControlOffer.dataEditReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][11].toString()));
        dataControlOffer.dataShowReceiver();
    }

    public void deleteProduct() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Offer");
            dataControlOffer.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
            dataControlOffer.dataShowProduct();
        }
    }

    public boolean showOfferReport() {
        if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return false;
        }

        if (dataControlOffer.getCompOffer().getCompanyofferpositionses() != null
                && dataControlOffer.getCompOffer().getCompanyofferpositionses().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (dataControlOffer.getCompOffer().getCompanyofferreceivers() != null
                && dataControlOffer.getCompOffer().getCompanyofferreceivers().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_CONTACT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            EBISystem.showInActionStatus("Offer");
            dataControlOffer.dataShowReport(Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()));
        }
        return pass;
    }

    public void createOrder() {
        if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return;
        }
        dataControlOffer.createOrderFromOffer(Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()));
    }

    public void mailOffer() {

        if (selectedOfferRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModoffer.data[selectedOfferRow][0].toString())) {
            return;
        }

        EBISystem.builder().loadGUI("CRMDialog/sendMailDialogGUI.xml");
        EBISystem.builder().dialog("sendEMailMessage").setTitle(EBISystem.i18n("EBI_LANG_DIALOG_SEND_EMAIL_MESSAGE"));
        EBISystem.builder().getCheckBox("ShowReportBS", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SHOW_REPORT_BEFORE_SEND"));
        EBISystem.builder().label("SubjectEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SUBJECT"));
        EBISystem.builder().label("template", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_TEMPLATE"));
        EBISystem.builder().combo("templateText", "sendEMailMessage").setModel(new DefaultComboBoxModel(EBISystem.getModule().dynMethod.getEMailTemplateNames()));

        EBISystem.builder().combo("templateText", "sendEMailMessage").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(
                        EBISystem.builder().combo("templateText", "sendEMailMessage").getSelectedItem().toString())) {

                    EBISystem.builder().getEditor("MessageAreaText", "sendEMailMessage").setText(
                            EBISystem.getModule().dynMethod.getEMailTemplate(
                                    EBISystem.builder().combo("templateText", "sendEMailMessage")
                                            .getSelectedItem().toString()));
                }
            }
        });

        EBISystem.builder().button("sendEmail", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SEND"));
        EBISystem.builder().button("closeEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_CLOSE"));

        EBISystem.builder().button("sendEmail", "sendEMailMessage").addActionListener(new java.awt.event.ActionListener() {
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
                    
                    EBISystem.showInActionStatus("Offer");
                    dataControlOffer.dataShowAndMailReport(
                            Integer.parseInt(tabModoffer.data[selectedOfferRow][tabModoffer.columnNames.length].toString()),
                            EBISystem.builder().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
                    
                    EBISystem.builder().dialog("sendEMailMessage").setVisible(false);
                }
            }
        });
        EBISystem.builder().button("closeEMailDialog", "sendEMailMessage").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("sendEMailMessage").setVisible(false);
            }
        });
        
        EBISystem.builder().showGUI();
    }

    public void historySearch() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.
                retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Offer")).setVisible();
    }

    public void searchOpportunity() {
        final EBIOpportunitySelectionDialog dialog
                = new EBIOpportunitySelectionDialog(EBISystem.getModule()
                        .getOpportunityPane().getDataOpportuniyControl().getOppportunityList());
        dialog.setVisible();
        if (dialog.shouldSave) {

            EBISystem.builder().textField("offerOpportunityText", "Offer").setText(dialog.name);
            dataControlOffer.opportunityID = dialog.id;
        }
    }

    public void showOpportunity() {
        if (!"".equals(EBISystem.builder().textField("offerOpportunityText", "Offer").getText())) {
            EBISystem.getModule().ebiContainer.setSelectedTab(EBISystem.getInstance()
                    .getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY")));
            EBISystem.getModule().getOpportunityPane().editOpportunity();
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NO_OPPORTUNITY_SELECTED")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
    }

    private boolean validateEMailInput() {
        if ("".equals(EBISystem.builder().textField("SubjectText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_SUBJECT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.builder().getEditor("MessageAreaText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_BODY_TEXT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
