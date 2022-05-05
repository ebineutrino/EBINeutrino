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
        EBISystem.builder().textField("filterTableText", "Opportunity").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("companyOpportunityTable", "Opportunity").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Opportunity").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("companyOpportunityTable", "Opportunity").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Opportunity").getText()));
            }
        });

        final NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMinimumFractionDigits(2);
        valueFormat.setMaximumFractionDigits(3);

        EBISystem.builder().combo("opportunityNameText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("oppSaleStateText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (EBISystem.builder().combo("oppSaleStateText", "Opportunity") != null) {
                    dataOpportuniyControl.setPurchState(EBISystem.builder().combo("oppSaleStateText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.builder().combo("oppSaleStateText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("oppEvalStatusText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (EBISystem.builder().combo("oppEvalStatusText", "Opportunity") != null) {
                    dataOpportuniyControl.setEvalStatus(EBISystem.builder().combo("oppEvalStatusText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.builder().combo("oppEvalStatusText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("statusOppText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                if (EBISystem.builder().combo("statusOppText", "Opportunity") != null) {
                    dataOpportuniyControl.setSStatus(EBISystem.builder().combo("statusOppText", "Opportunity").getSelectedItem().toString());
                }

            }
        });

        EBISystem.builder().combo("statusOppText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("oppBdgStatusText", "Opportunity").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                if (EBISystem.builder().combo("oppBdgStatusText", "Opportunity") != null) {
                    dataOpportuniyControl.setBudgetStatus(EBISystem.builder().combo("oppBdgStatusText", "Opportunity").getSelectedItem().toString());
                }
            }
        });

        EBISystem.builder().combo("oppBdgStatusText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("oppProbabilityText", "Opportunity").setEditable(true);
        EBISystem.builder().combo("oppBustypeText", "Opportunity").setEditable(true);

        /**
         * ********************************************************************************
         */
        // OPPORTUNITY CONTACT TABLE
        /**
         * ********************************************************************************
         */
        EBISystem.builder().table("contactTableOpportunity", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("contactTableOpportunity", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedContactRow = EBISystem.builder().table("contactTableOpportunity", "Opportunity").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editOppContact", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("deleteOppContact", "Opportunity").setEnabled(false);
                } else if (!"".equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
                    EBISystem.builder().button("editOppContact", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("deleteOppContact", "Opportunity").setEnabled(true);
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
        EBISystem.builder().table("companyOpportunityTable", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("companyOpportunityTable", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getLeadSelectionIndex() > -1) {
                    try {
                        selectedOpportunityRow = EBISystem.builder().table("companyOpportunityTable", "Opportunity").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("deleteOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("reportOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("historyOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("mailOpportunity", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("createOffer", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("copyOpportunity", "Opportunity").setEnabled(false);
                } else if (!tabModel.data[selectedOpportunityRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("deleteOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("reportOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("historyOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("mailOpportunity", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("createOffer", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("copyOpportunity", "Opportunity").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("companyOpportunityTable", "Opportunity").addKeyAction(new EBIUICallback() {
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

        EBISystem.builder().table("companyOpportunityTable", "Opportunity").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("companyOpportunityTable", "Opportunity").rowAtPoint(e.getPoint()) > -1) {
                    selectedOpportunityRow = EBISystem.builder().table("companyOpportunityTable", "Opportunity").convertRowIndexToModel(EBISystem.builder().table("companyOpportunityTable", "Opportunity").rowAtPoint(e.getPoint()));
                }
                if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModel.data[selectedOpportunityRow][0].toString())) {
                    return;
                }
                editOpportunity();
            }
        });

        EBISystem.builder().FormattedField("oppValueText", "Opportunity").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(valueFormat)));
        EBISystem.builder().FormattedField("oppValueText", "Opportunity").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.FLOAT));
        EBISystem.builder().FormattedField("oppValueText", "Opportunity").setColumns(10);

        /**
         * ********************************************************************************
         */
        // OPPORTUNITY DOCUMENT TABLE
        /**
         * ********************************************************************************
         */
        EBISystem.builder().table("opportunityDoc", "Opportunity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("opportunityDoc", "Opportunity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.builder().table("opportunityDoc", "Opportunity").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("showOppDoc", "Opportunity").setEnabled(false);
                    EBISystem.builder().button("deleteOppDoc", "Opportunity").setEnabled(false);
                } else if (!tabOpportunityDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("showOppDoc", "Opportunity").setEnabled(true);
                    EBISystem.builder().button("deleteOppDoc", "Opportunity").setEnabled(true);
                }
            }
        });

    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModel = new ModelOpportunity();
            tabModelContact = new ModelCRMContact(ModelCRMContact.OPPORTUNITY_CONTACT);
            tabOpportunityDoc = new ModelDoc();
            EBISystem.builder().table("contactTableOpportunity", "Opportunity").setModel(tabModelContact);
            EBISystem.builder().table("companyOpportunityTable", "Opportunity").setModel(tabModel);
            EBISystem.builder().table("opportunityDoc", "Opportunity").setModel(tabOpportunityDoc);
        }

        EBISystem.builder().combo("oppBustypeText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppBussinesType));
        EBISystem.builder().combo("statusOppText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppStatus));
        EBISystem.builder().combo("oppBdgStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppBudgetStatus));
        EBISystem.builder().combo("oppEvalStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppEvalStatus));
        EBISystem.builder().combo("oppSaleStateText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(oppSalesStage));
        EBISystem.builder().combo("oppProbabilityText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"}));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.builder().table("companyOpportunityTable", "Opportunity").getColumnModel().getColumn(4);
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

        EBISystem.builder().vpanel("Opportunity").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Opportunity").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Opportunity").setChangedDate("");
        EBISystem.builder().vpanel("Opportunity").setChangedFrom("");

        EBISystem.builder().FormattedField("oppValueText", "Opportunity").setText("");
        EBISystem.builder().textArea("opportunityDescription", "Opportunity").setText("");

        EBISystem.builder().combo("opportunityNameText", "Opportunity").getEditor().setItem("");
        EBISystem.builder().combo("oppSaleStateText", "Opportunity").setSelectedIndex(0);
        EBISystem.builder().combo("oppEvalStatusText", "Opportunity").setSelectedIndex(0);
        EBISystem.builder().combo("statusOppText", "Opportunity").setSelectedIndex(0);
        EBISystem.builder().combo("oppBdgStatusText", "Opportunity").setSelectedIndex(0);
        EBISystem.builder().combo("oppBustypeText", "Opportunity").setSelectedIndex(0);
        EBISystem.builder().combo("oppProbabilityText", "Opportunity").setSelectedIndex(0);

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
        if (selectedContactRow < 0 || "".
                equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
            return;
        }
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataEditContact(tabModelContact.getId(selectedContactRow));
        dataOpportuniyControl.showOpportunityContacts();
    }

    public void deleteContact() {
        if (selectedContactRow < 0 || "".
                equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Opportunity");
            dataOpportuniyControl.dataRemoveContact(tabModelContact.getId(selectedContactRow));
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
        int row = EBISystem.builder().table("companyOpportunityTable", "Opportunity").getSelectedRow();
        Integer id = dataOpportuniyControl.dataStore();
        dataOpportuniyControl.dataShow(id);
        dataOpportuniyControl.dataShowDoc();
        dataOpportuniyControl.showOpportunityContacts();
        EBISystem.builder().table("companyOpportunityTable", "Opportunity").changeSelection(row, 0, false, false);
        return true;
    }

    public void editOpportunity() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Opportunity");
        dataOpportuniyControl.dataNew();
        dataOpportuniyControl.dataEdit(Integer.parseInt(tabModel.data[selectedOpportunityRow][tabModel.columnNames.length].toString()));
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
        Integer id = dataOpportuniyControl.dataCopy(Integer.parseInt(tabModel.data[selectedOpportunityRow][tabModel.columnNames.length].toString()));
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
            dataOpportuniyControl.dataDelete(Integer.parseInt(tabModel.data[selectedOpportunityRow][tabModel.columnNames.length].toString()));
            dataOpportuniyControl.dataNew();
            dataOpportuniyControl.dataShow(-1);
            dataOpportuniyControl.showOpportunityContacts();
            dataOpportuniyControl.dataShowDoc();
            dataOpportuniyControl.isEdit = false;
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().combo("opportunityNameText", "Opportunity").getEditor().getItem())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().combo("oppBustypeText", "Opportunity").getSelectedItem().toString()) || EBISystem.builder().combo("oppBustypeText", "Opportunity").getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_BUSINESS_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void mailOpportunity(final int id) {

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

                    dataOpportuniyControl.dataShowAndMailReport(id, EBISystem.builder().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
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

        mailOpportunity(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
        EBISystem.builder().showGUI();
    }

    public void createOffer() {
        if (selectedOpportunityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModel.data[selectedOpportunityRow][0].toString())) {
            return;
        }
        dataOpportuniyControl.createOfferFromOpportunity(Integer.parseInt(tabModel.data[selectedOpportunityRow][7].toString()));
    }
}
