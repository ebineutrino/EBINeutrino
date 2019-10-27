package org.modules.views;

import org.modules.EBIModule;
import org.modules.controls.ControlOpportunity;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIMeetingAddContactDialog;
import org.modules.models.ModelCRMContact;
import org.modules.models.ModelDoc;
import org.modules.models.ModelOpportunity;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIJTextFieldNumeric;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMOpportunityView {

    @Getter
    @Setter
    private ModelOpportunity tabModel = null;
    @Getter
    @Setter
    private ModelCRMContact tabModelContact = null;

    public static String[] oppBussinesType = null;
    public static String[] oppSalesStage = null;
    public static String[] oppEvalStatus = null;
    public static String[] oppBudgetStatus = null;
    public static String[] oppStatus = null;
    @Getter
    @Setter
    private ModelDoc tabOpportunityDoc = null;
    @Getter
    @Setter
    private ControlOpportunity dataOpportuniyControl = new ControlOpportunity();
    @Getter
    @Setter
    private int selectedOpportunityRow = -1;
    @Getter
    @Setter
    private int selectedContactRow = -1;
    @Getter
    @Setter
    private int selectedDocRow = -1;

    public void initializeAction() {
        EBISystem.gui().textField("filterTableText", "Opportunity").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("companyOpportunityTable", "Opportunity").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Opportunity").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("companyOpportunityTable", "Opportunity").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Opportunity").getText()));
            }
        });

        final NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMinimumFractionDigits(2);
        valueFormat.setMaximumFractionDigits(3);

        EBISystem.gui().combo("opportunityNameText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("oppSaleStateText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (EBISystem.gui().combo("oppSaleStateText", "Opportunity") != null) {
                    dataOpportuniyControl.setPurchState(EBISystem.gui().combo("oppSaleStateText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.gui().combo("oppSaleStateText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("oppEvalStatusText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (EBISystem.gui().combo("oppEvalStatusText", "Opportunity") != null) {
                    dataOpportuniyControl.setEvalStatus(EBISystem.gui().combo("oppEvalStatusText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.gui().combo("oppEvalStatusText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("statusOppText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                if (EBISystem.gui().combo("statusOppText", "Opportunity") != null) {
                    dataOpportuniyControl.setSStatus(EBISystem.gui().combo("statusOppText", "Opportunity").getSelectedItem().toString());
                }

            }
        });

        EBISystem.gui().combo("statusOppText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("oppBdgStatusText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                if (EBISystem.gui().combo("oppBdgStatusText", "Opportunity") != null) {
                    dataOpportuniyControl.setBudgetStatus(EBISystem.gui().combo("oppBdgStatusText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.gui().combo("oppBdgStatusText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("oppProbabilityText", "Opportunity").setEditable(true);
        EBISystem.gui().combo("oppBustypeText", "Opportunity").setEditable(true);

        /**
         * ********************************************************************************
         */
        // OPPORTUNITY CONTACT TABLE
        /**
         * ********************************************************************************
         */
        EBISystem.gui().table("contactTableOpportunity", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("contactTableOpportunity", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedContactRow = EBISystem.gui().table("contactTableOpportunity", "Opportunity").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editOppContact", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("deleteOppContact", "Opportunity").setEnabled(false);
                } else if (!tabModelContact.data[selectedContactRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editOppContact", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("deleteOppContact", "Opportunity").setEnabled(true);
                }
            }
        });

        /**
         * ********************************************************************************
         */
        // OPPORTUNITY TABLE
        /**
         * ********************************************************************************
         */
        EBISystem.gui().table("companyOpportunityTable", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("companyOpportunityTable", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() > -1) {
                    try {
                        selectedOpportunityRow = EBISystem.gui().table("companyOpportunityTable", "Opportunity").convertRowIndexToModel(lsm.getMinSelectionIndex());
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("deleteOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("reportOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("historyOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("mailOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("createOffer", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("copyOpportunity", "Opportunity").setEnabled(false);
                } else if (!tabModel.data[selectedOpportunityRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("deleteOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("reportOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("historyOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("mailOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("createOffer", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("copyOpportunity", "Opportunity").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("companyOpportunityTable", "Opportunity").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedOpportunityRow = selRow;
                editOpportunity();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedOpportunityRow = selRow;
                editOpportunity();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedOpportunityRow = selRow;

                if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModel.data[selectedOpportunityRow][0].toString())) {
                    return;
                }
                editOpportunity();
            }
        });

        EBISystem.gui().table("companyOpportunityTable", "Opportunity").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("companyOpportunityTable", "Opportunity").rowAtPoint(e.getPoint()) > -1) {
                    selectedOpportunityRow = EBISystem.gui().table("companyOpportunityTable", "Opportunity").convertRowIndexToModel(EBISystem.gui().table("companyOpportunityTable", "Opportunity").rowAtPoint(e.getPoint()));
                }
                if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModel.data[selectedOpportunityRow][0].toString())) {
                    return;
                }
                editOpportunity();
            }
        });

        EBISystem.gui().FormattedField("oppValueText", "Opportunity").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(valueFormat)));
        EBISystem.gui().FormattedField("oppValueText", "Opportunity").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.FLOAT));
        EBISystem.gui().FormattedField("oppValueText", "Opportunity").setColumns(10);

        /**
         * ********************************************************************************
         */
        // OPPORTUNITY DOCUMENT TABLE
        /**
         * ********************************************************************************
         */
        EBISystem.gui().table("opportunityDoc", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("opportunityDoc", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("opportunityDoc", "Opportunity").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showOppDoc", "Opportunity").setEnabled(false);
                    EBISystem.gui().button("deleteOppDoc", "Opportunity").setEnabled(false);
                } else if (!tabOpportunityDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showOppDoc", "Opportunity").setEnabled(true);
                    EBISystem.gui().button("deleteOppDoc", "Opportunity").setEnabled(true);
                }
            }
        });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModel = new ModelOpportunity();
            tabModelContact = new ModelCRMContact();
            tabOpportunityDoc = new ModelDoc();
            EBISystem.gui().table("contactTableOpportunity", "Opportunity").setModel(tabModelContact);
            EBISystem.gui().table("companyOpportunityTable", "Opportunity").setModel(tabModel);
            EBISystem.gui().table("opportunityDoc", "Opportunity").setModel(tabOpportunityDoc);
        }

        EBISystem.gui().combo("oppBustypeText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppBussinesType));
        EBISystem.gui().combo("statusOppText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppStatus));
        EBISystem.gui().combo("oppBdgStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppBudgetStatus));
        EBISystem.gui().combo("oppEvalStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppEvalStatus));
        EBISystem.gui().combo("oppSaleStateText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppSalesStage));
        EBISystem.gui().combo("oppProbabilityText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"}));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.gui().table("companyOpportunityTable", "Opportunity").getColumnModel().getColumn(4);
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

        EBISystem.gui().vpanel("Opportunity").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Opportunity").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Opportunity").setChangedDate("");
        EBISystem.gui().vpanel("Opportunity").setChangedFrom("");

        EBISystem.gui().FormattedField("oppValueText", "Opportunity").setText("");
        EBISystem.gui().textArea("opportunityDescription", "Opportunity").setText("");

        EBISystem.gui().combo("opportunityNameText", "Opportunity").getEditor().setItem("");
        EBISystem.gui().combo("oppSaleStateText", "Opportunity").setSelectedIndex(0);
        EBISystem.gui().combo("oppEvalStatusText", "Opportunity").setSelectedIndex(0);
        EBISystem.gui().combo("statusOppText", "Opportunity").setSelectedIndex(0);
        EBISystem.gui().combo("oppBdgStatusText", "Opportunity").setSelectedIndex(0);
        EBISystem.gui().combo("oppBustypeText", "Opportunity").setSelectedIndex(0);
        EBISystem.gui().combo("oppProbabilityText", "Opportunity").setSelectedIndex(0);

    }

    public void newDocs() {
        dataOpportuniyControl.dataNewDoc();
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabOpportunityDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataOpportuniyControl.dataViewDoc(Integer.parseInt(tabOpportunityDoc.data[selectedDocRow][3].toString()));
    }

    public void deleteDoc() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabOpportunityDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Opportunity");
            dataOpportuniyControl.dataDeleteDoc(Integer.parseInt(tabOpportunityDoc.data[selectedDocRow][3].toString()));
            dataOpportuniyControl.dataShowDoc();
        }
    }

    public void newContact() {
        EBISystem.showInActionStatus("Opportunity");
        final EBIMeetingAddContactDialog newContact = new EBIMeetingAddContactDialog(false, null, null, false);
        newContact.setVisible();
        dataOpportuniyControl.showOpportunityContacts();
    }

    public void editContact() {
        if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModelContact.data[selectedContactRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataEditContact(Integer.parseInt(tabModelContact.data[selectedContactRow][tabModelContact.columnNames.length].toString()));
        dataOpportuniyControl.showOpportunityContacts();
    }

    public void deleteContact() {
        if (selectedContactRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModelContact.data[selectedContactRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Opportunity");
            dataOpportuniyControl.dataRemoveContact(Integer.parseInt(tabModelContact.data[selectedContactRow][tabModelContact.columnNames.length].toString()));
            dataOpportuniyControl.showOpportunityContacts();
        }
    }

    public void newOpportunity() {
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataNew();
        dataOpportuniyControl.showOpportunityContacts();
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.isEdit = false;
    }

    public boolean saveOpportunity() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Opportunity");
        int row = EBISystem.gui().table("companyOpportunityTable", "Opportunity").getSelectedRow();
        Integer id = dataOpportuniyControl.dataStore();
        dataOpportuniyControl.dataShow(id);
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.showOpportunityContacts();
        dataOpportuniyControl.isEdit = true;
        EBISystem.gui().table("companyOpportunityTable", "Opportunity").changeSelection(row, 0, false, false);
        return true;
    }

    public void editOpportunity() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataNew();
        dataOpportuniyControl.dataEdit(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
        dataOpportuniyControl.showOpportunityContacts();
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.isEdit = true;
    }

    public void remoteEditOpportunity(final int id) {
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataNew();
        dataOpportuniyControl.dataEdit(id);
        dataOpportuniyControl.showOpportunityContacts();
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.isEdit = true;
    }

    public void copyOpportunity() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        Integer id = dataOpportuniyControl.dataCopy(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
        dataOpportuniyControl.dataEdit(id);
        dataOpportuniyControl.dataShow(id);
        dataOpportuniyControl.showOpportunityContacts();
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.isEdit = true;
    }

    public void deleteOpportunity() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Opportunity");
            dataOpportuniyControl.dataDelete(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
            dataOpportuniyControl.dataNew();
            dataOpportuniyControl.dataShow(-1);
            dataOpportuniyControl.isEdit = false;
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().combo("opportunityNameText", "Opportunity").getEditor().getItem())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().combo("oppBustypeText", "Opportunity").getSelectedItem().toString()) || EBISystem.gui().combo("oppBustypeText", "Opportunity").getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_BUSINESS_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void mailOpportunity(final int id) {

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

                    dataOpportuniyControl.dataShowAndMailReport(id, EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
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

        if ("".equals(EBISystem.gui().textField("SubjectText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_SUBJECT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_BODY_TEXT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void showOpportunityReports() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        if (dataOpportuniyControl.getOpportunity().getCompanyopportunitycontacts() != null
                && dataOpportuniyControl.getOpportunity().getCompanyopportunitycontacts().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_CONTACT")).Show(EBIMessage.ERROR_MESSAGE);
            return;
        }
        this.dataOpportuniyControl.dataShowReport(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
    }

    public void historyOpportunity() {
        new EBICRMHistoryView(((EBIModule) EBISystem.getInstance().getIEBIModule()).hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Opportunity")).setVisible();
    }

    public void mailOpportunity() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
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

        mailOpportunity(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
        EBISystem.gui().showGUI();
    }

    public void createOffer() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        dataOpportuniyControl.createOfferFromOpportunity(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
    }
}
