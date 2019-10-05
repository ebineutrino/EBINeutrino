package org.modules.views;

import org.modules.models.ModelCampaign;
import org.modules.models.ModelReceiver;
import org.modules.models.ModelDoc;
import org.modules.models.ModelCRMProduct;
import org.modules.models.ModelProperties;
import org.modules.EBIModule;
import org.modules.controls.ControlCampaign;
import org.modules.views.dialogs.EBICRMAddContactAddressType;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
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

public class EBICRMCampaignView {

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
    private ModelCampaign tabModelCampaign = null;

    public static String[] campaignStatus = null;
    @Getter
    @Setter
    private ModelProperties tabModProperties = null;
    @Getter
    @Setter
    private ControlCampaign dataControlCampaign = null;
    @Getter
    @Setter
    private int selectedDocRow = -1;
    @Getter
    @Setter
    private int selectedProductRow = -1;
    @Getter
    @Setter
    private int selectedReceiverRow = -1;
    @Getter
    @Setter
    private int selectedCampaignRow = -1;
    @Getter
    @Setter
    private int selectedPropertiesRow = -1;


    public EBICRMCampaignView() {
        dataControlCampaign = new ControlCampaign();
    }

    public void initializeAction() {

        EBISystem.gui().label("filterTable", "Campaign").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().textField("filterTableText", "Campaign").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {}

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("companyCampaignTable", "Campaign")
                        .setRowFilter(RowFilters.regexFilter("(?i)"
                                + EBISystem.gui().textField("filterTableText", "Campaign").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("companyCampaignTable", "Campaign")
                        .setRowFilter(RowFilters.regexFilter("(?i)"
                                + EBISystem.gui().textField("filterTableText", "Campaign").getText()));
            }
        });

        EBISystem.gui().combo("CampaignStatusText", "Campaign")
                .setModel(new javax.swing.DefaultComboBoxModel(campaignStatus));

        EBISystem.gui().timePicker("campaignValidFromText", "Campaign").setFormats(EBISystem.DateFormat);
        EBISystem.gui().timePicker("campaingValidToText", "Campaign").setFormats(EBISystem.DateFormat);

        /**
         * ************************************************************************
         */
        // CAMPAIGN PROPERTIES TABLE
        EBISystem.gui().table("CampaignPropertiesTable", "Campaign").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("CampaignPropertiesTable", "Campaign").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedPropertiesRow = EBISystem.gui()
                            .table("CampaignPropertiesTable", "Campaign")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteCampaingProperties", "Campaign")
                            .setEnabled(false);
                    EBISystem.gui().button("editCampaingProperties", "Campaign")
                            .setEnabled(false);
                } else if (!tabModProperties.data[selectedPropertiesRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteCampaingProperties", "Campaign").setEnabled(true);
                    EBISystem.gui().button("editCampaingProperties", "Campaign").setEnabled(true);
                }
            }
        });

        /**
         * ************************************************************************
         */
        // CAMPAIGN DOCUMENT TABLE
        /**
         * ************************************************************************
         */
        EBISystem.gui().table("tableCampaignDocument", "Campaign").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        EBISystem.gui().table("tableCampaignDocument", "Campaign").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("tableCampaignDocument", "Campaign")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showCampaignDoc", "Campaign").setEnabled(false);
                    EBISystem.gui().button("deleteCampaignDoc", "Campaign").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showCampaignDoc", "Campaign").setEnabled(true);
                    EBISystem.gui().button("deleteCampaignDoc", "Campaign").setEnabled(true);
                }
            }
        });

        /**
         * ************************************************************************
         */
        // CAMPAIGN PRODUCT TABLE
        /**
         * ************************************************************************
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.gui().table("tableCampaignProduct", "Campaign")
                        .getColumnModel().getColumn(5);
                col7.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value,
                            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });
            }
        });

        EBISystem.gui().table("tableCampaignProduct", "Campaign").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableCampaignProduct", "Campaign").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.gui().table("tableCampaignProduct", "Campaign")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteCampaignProduct", "Campaign").setEnabled(false);
                } else if (!tabModProduct.data[selectedProductRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteCampaignProduct", "Campaign").setEnabled(true);
                }
            }
        });

        /**
         * ************************************************************************
         */
        // CAMPAIGN RECEIVER TABLE
        /**
         * ************************************************************************
         */
        EBISystem.gui().table("tableCampaignReceiver", "Campaign").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableCampaignReceiver", "Campaign").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedReceiverRow = EBISystem.gui()
                            .table("tableCampaignReceiver", "Campaign").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteCampaignReceiver", "Campaign").setEnabled(false);
                    EBISystem.gui().button("editCampaignReceiver", "Campaign").setEnabled(false);
                } else if (!tabModReceiver.data[selectedReceiverRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("deleteCampaignReceiver", "Campaign").setEnabled(true);
                    EBISystem.gui().button("editCampaignReceiver", "Campaign").setEnabled(true);
                }
            }
        });

        /**
         * ************************************************************************
         */
        // CAMPAIGN AVAILABLE TABLE
        /**
         * ************************************************************************
         */
        EBISystem.gui().table("companyCampaignTable", "Campaign").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("companyCampaignTable", "Campaign").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    try {
                        selectedCampaignRow = EBISystem.gui()
                                .table("companyCampaignTable", "Campaign")
                                .convertRowIndexToModel(lsm.getMinSelectionIndex());
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editCampaign", "Campaign").setEnabled(false);
                    EBISystem.gui().button("reportCampaign", "Campaign").setEnabled(false);
                    EBISystem.gui().button("deleteCampaign", "Campaign").setEnabled(false);
                    EBISystem.gui().button("historyCampaign", "Campaign").setEnabled(false);
                    EBISystem.gui().button("mailCampaign", "Campaign").setEnabled(false);
                    EBISystem.gui().button("copyCampaign", "Campaign").setEnabled(false);
                } else if (!tabModelCampaign.data[selectedCampaignRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editCampaign", "Campaign").setEnabled(true);
                    EBISystem.gui().button("reportCampaign", "Campaign").setEnabled(true);
                    EBISystem.gui().button("deleteCampaign", "Campaign").setEnabled(true);
                    EBISystem.gui().button("historyCampaign", "Campaign").setEnabled(true);
                    EBISystem.gui().button("mailCampaign", "Campaign").setEnabled(true);
                    EBISystem.gui().button("copyCampaign", "Campaign").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("companyCampaignTable", "Campaign").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedCampaignRow = selRow;
                editCampaign();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedCampaignRow = selRow;
                editCampaign();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedCampaignRow = selRow;
                if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
                    return;
                }
                editCampaign();
            }
        });

        EBISystem.gui().table("companyCampaignTable", "Campaign")
                .setMouseCallback(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final java.awt.event.MouseEvent e) {
                        if (EBISystem.gui().table("companyCampaignTable", "Campaign").rowAtPoint(e.getPoint()) != -1) {
                            selectedCampaignRow = EBISystem.gui()
                                    .table("companyCampaignTable", "Campaign")
                                    .convertRowIndexToModel(EBISystem.gui()
                                            .table("companyCampaignTable", "Campaign").rowAtPoint(e.getPoint()));
                        }
                        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                                .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
                            return;
                        }
                        editCampaign();
                    }
                });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModProperties = new ModelProperties();
            tabModDoc = new ModelDoc();
            tabModReceiver = new ModelReceiver(true);
            tabModProduct = new ModelCRMProduct();
            tabModelCampaign = new ModelCampaign();

            EBISystem.gui().table("companyCampaignTable", "Campaign").setModel(tabModelCampaign);
            EBISystem.gui().table("tableCampaignReceiver", "Campaign").setModel(tabModReceiver);
            EBISystem.gui().table("tableCampaignProduct", "Campaign").setModel(tabModProduct);
            EBISystem.gui().table("tableCampaignDocument", "Campaign").setModel(tabModDoc);
            EBISystem.gui().table("CampaignPropertiesTable", "Campaign").setModel(tabModProperties);
        }

        EBISystem.hibernate().openHibernateSession("CAMPAIGN_SESSION");
        EBISystem.gui().textField("CampaignNrTex", "Campaign").setText("");
        EBISystem.gui().textField("CampaignNameText", "Campaign").setText("");

        EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getEditor().setText("");
        EBISystem.gui().timePicker("campaingValidToText", "Campaign").getEditor().setText("");

        EBISystem.gui().vpanel("Campaign").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Campaign").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));

        EBISystem.gui().vpanel("Campaign").setChangedFrom("");
        EBISystem.gui().vpanel("Campaign").setChangedDate("");
    }

    public void newDocs() {
        dataControlCampaign.dataNewDoc();
        dataControlCampaign.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlCampaign.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }

    public void showProduct() {
        dataControlCampaign.dataShowProduct();
    }

    public void showCampaign() {
        dataControlCampaign.dataShow();
    }

    public void newCampaignProduct() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlCampaign.getCampaign());
        product.setVisible();
    }

    public void newCampaign() {
        EBISystem.showInActionStatus("Campaign");
        dataControlCampaign.dataNew();
        dataControlCampaign.dataShowDoc();
        dataControlCampaign.dataShowProduct();
        dataControlCampaign.dataShowReciever();
        dataControlCampaign.dataShowProperties();
        dataControlCampaign.isEdit = false;
    }

    public boolean saveCampaign() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Campaign");
        dataControlCampaign.dataStore();
        dataControlCampaign.dataShow();
        dataControlCampaign.dataShowDoc();
        dataControlCampaign.dataShowProduct();
        dataControlCampaign.dataShowProperties();
        dataControlCampaign.dataShowReciever();
        dataControlCampaign.isEdit = true;
        return true;
    }

    public void editCampaign() {
        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Campaign");
        dataControlCampaign.dataNew();
        dataControlCampaign.dataEdit(Integer.parseInt(tabModelCampaign.data[selectedCampaignRow][4].toString()));
        dataControlCampaign.dataShowDoc();
        dataControlCampaign.dataShowProduct();
        dataControlCampaign.dataShowProperties();
        dataControlCampaign.dataShowReciever();
        dataControlCampaign.isEdit = true;
    }

    public void copyCampaign() {
        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Campaign");
        dataControlCampaign.dataCopy(Integer.parseInt(tabModelCampaign.data[selectedCampaignRow][4].toString()));
        dataControlCampaign.dataShow();
    }

    public void deleteCampaign() {
        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Campaign");
            dataControlCampaign.dataDelete(Integer.parseInt(tabModelCampaign.data[selectedCampaignRow][4].toString()));
            dataControlCampaign.dataNew();
            dataControlCampaign.dataShow();
            dataControlCampaign.isEdit = false;
        }
    }

    public void historyCampaign() {
        new EBICRMHistoryView(
                ((EBIModule) EBISystem.getInstance().getIEBIModule()).hcreator.retrieveDBHistory(
                        Integer.parseInt(tabModelCampaign.data[selectedCampaignRow][4].toString()),
                        "CRMCampaign")).setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("CampaignNameText", "Campaign").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.gui().combo("CampaignStatusText", "Campaign").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getEditor().getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_FROM")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().timePicker("campaingValidToText", "Campaign").getEditor().getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_TO")).Show(EBIMessage.ERROR_MESSAGE);
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
            EBISystem.showInActionStatus("Campaign");
            dataControlCampaign.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
            dataControlCampaign.dataShowDoc();
        }
    }

    public void newReceiver() {
        final EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(dataControlCampaign);
        addCo.setVisible();
    }

    public void deleteReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Campaign");
            dataControlCampaign.dataDeleteReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][13].toString()));
            dataControlCampaign.dataShowReciever();
        }
    }

    public void editReceiver() {
        if (selectedReceiverRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModReceiver.data[selectedReceiverRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Campaign");
        dataControlCampaign.dataEditReceiver(Integer.parseInt(tabModReceiver.data[selectedReceiverRow][13].toString()));
        dataControlCampaign.dataShowReciever();
    }

    public void deleteProduct() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Campaign");
            dataControlCampaign.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
            dataControlCampaign.dataShowProduct();
        }
    }

    public void showCampaignReport() {
        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
            return;
        }

        if (dataControlCampaign.getCampaign().getCrmcampaignpositions() != null
                && dataControlCampaign.getCampaign().getCrmcampaignpositions().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            return;
        }

        if (dataControlCampaign.getCampaign().getCrmcampaignreceivers() != null
                && dataControlCampaign.getCampaign().getCrmcampaignreceivers().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_CONTACT"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            return;
        }

        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint() || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            dataControlCampaign.dataShowReport(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
        }

    }

    public void newCampaignProperties() {
        dataControlCampaign.dataAddProperties();
        dataControlCampaign.dataShowProperties();
    }

    public void showCampaignProperties() {
        dataControlCampaign.dataShowProperties();
    }

    public void editCampaignProperties() {
        if (selectedPropertiesRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModProperties.data[selectedPropertiesRow][0].toString())) {
            return;
        }
        dataControlCampaign.dataEditPoperties(Integer.parseInt(tabModProperties.data[selectedPropertiesRow][2].toString()));
        dataControlCampaign.dataShowProperties();
    }

    public void deleteCampaignProperties() {
        if (selectedPropertiesRow < 0
                || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModProperties.data[selectedPropertiesRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataControlCampaign.dataDeleteProperties(Integer.parseInt(tabModProperties.data[selectedPropertiesRow][2].toString()));
            dataControlCampaign.dataShowProperties();
        }
    }

    public void mailCampaign() {
        if (selectedCampaignRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModelCampaign.data[selectedCampaignRow][0].toString())) {
            return;
        }

        EBISystem.gui().loadGUI("CRMDialog/sendMailDialogGUI.xml");

        EBISystem.gui().dialog("sendEMailMessage")
                .setTitle(EBISystem.i18n("EBI_LANG_DIALOG_SEND_EMAIL_MESSAGE"));
        EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage")
                .setText(EBISystem.i18n("EBI_LANG_SHOW_REPORT_BEFORE_SEND"));
        EBISystem.gui().label("SubjectEMailDialog", "sendEMailMessage")
                .setText(EBISystem.i18n("EBI_LANG_SUBJECT"));
        EBISystem.gui().label("template", "sendEMailMessage")
                .setText(EBISystem.i18n("EBI_LANG_TEMPALTE"));

        EBISystem.gui().combo("templateText", "sendEMailMessage").setModel(
                new DefaultComboBoxModel(EBISystem.getModule().dynMethod.getEMailTemplateNames()));

        EBISystem.gui().combo("templateText", "sendEMailMessage")
                .addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                                .equals(EBISystem.gui()
                                        .combo("templateText", "sendEMailMessage")
                                        .getSelectedItem().toString())) {

                            EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage")
                                    .setText(EBISystem.getModule().dynMethod
                                            .getEMailTemplate(EBISystem.gui()
                                                    .combo("templateText", "sendEMailMessage")
                                                    .getSelectedItem().toString()));
                        }

                    }
                });
        EBISystem.gui().button("sendEmail", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SEND"));
        EBISystem.gui().button("closeEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.gui().showGUI();
        EBISystem.gui().button("sendEmail", "sendEMailMessage").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateEMailInput()) {
                    return;
                }
                EBISystem.showInActionStatus("Campaign");
                dataControlCampaign.dataShowAndMailReport(Integer.parseInt(tabModelCampaign.data[selectedCampaignRow][4].toString()),
                        EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
                EBISystem.gui().dialog("sendEMailMessage").setVisible(false);
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
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_BODY_TEXT"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
