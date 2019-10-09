package org.modules.views;

import org.modules.controls.ControlAccountStack;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelCRMAccount;
import org.modules.models.ModelCreditDebit;
import org.modules.models.ModelDoc;
import org.modules.utils.AbstractTableKeyAction;
import org.modules.utils.JTableActionMaps;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMAccountStackView {

    @Getter
    @Setter
    private ControlAccountStack dataControlAccount = new ControlAccountStack();
    @Getter
    @Setter
    private ModelCRMAccount tabModAccount = null;
    @Getter
    @Setter
    private int selectedInvoiceRow = -1;
    @Getter
    @Setter
    private int selectedCDRow = -1;
    @Getter
    @Setter
    private int selectedCDDialogRow = -1;
    @Getter
    @Setter
    private ModelDoc tabModDoc = null;
    @Getter
    @Setter
    private ModelCreditDebit creditDebitMod = null;
    @Getter
    @Setter
    private int selectedDocRow = -1;
    @Getter
    @Setter
    private String[] creditDebitType = null;
    @Getter
    @Setter
    private String[] accoutType = null;
    @Getter
    @Setter
    private int showDebitID = -1;
    @Getter
    @Setter
    private int showCreditID = -1;
    @Getter
    @Setter
    private NumberFormat taxFormat = null;
    @Getter
    @Setter
    private String selectedYear = "";
    @Getter
    @Setter
    private int accountDebitCreditType = 0;
    @Getter
    @Setter
    private String accountDebitTaxName = "";

    public static int DEBIT = 1;
    public static int CREDIT = 2;
    public static int DEPOT = 3;

    public void initializeAction() {
        //AVAILABLE TABLE AND BUTTONS
        EBISystem.gui().label("filterTable", "Account").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().textField("filterTableText", "Account").addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                EBISystem.gui().table("accountTable", "Account").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Account").getText()));
            }
        });

        EBISystem.gui().FormattedField("amountText", "Account").addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                dataControlAccount.dataCalculateTax(showDebitID);
                dataControlAccount.dataCalculateTax(showCreditID);
            }
        });

        EBISystem.gui().textField("debitText", "Account").addKeyListener(
                new KeyAdapter() {
            int idx = -1;

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                idx = dataControlAccount.getIDFromNumber(EBISystem.gui().textField("debitText", "Account").getText(), false);
                if (idx != -1) {
                    showDebitCreditToList(idx);
                } else {
                    EBISystem.gui().textField("descriptionDebit", "Account").setText("");
                    EBISystem.gui().FormattedField("debitCal", "Account").setText("");
                    EBISystem.gui().FormattedField("debitCal", "Account").setValue(null);
                }
            }
        });

        EBISystem.gui().textField("creditText", "Account").addKeyListener(
                new KeyAdapter() {
            int idx = -1;

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                idx = dataControlAccount.getIDFromNumber(EBISystem.gui().textField("creditText", "Account").getText(), true);
                if (idx != -1) {
                    showDebitCreditToList(idx);
                } else {
                    EBISystem.gui().textField("descriptionCredit", "Account").setText("");
                    EBISystem.gui().FormattedField("creditCal", "Account").setText("");
                    EBISystem.gui().FormattedField("creditCal", "Account").setValue(null);
                }
            }
        });

        EBISystem.gui().table("accountTable", "Account").setModel(tabModAccount);
        EBISystem.gui().table("accountTable", "Account").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("accountTable", "Account").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedInvoiceRow
                            = EBISystem.gui().table("accountTable", "Account")
                                    .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editAccount", "Account").setEnabled(false);
                    EBISystem.gui().button("deleteAccount", "Account").setEnabled(false);
                    EBISystem.gui().button("historyAccount", "Account").setEnabled(false);
                } else if (!tabModAccount.data[selectedInvoiceRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editAccount", "Account").setEnabled(true);
                    EBISystem.gui().button("deleteAccount", "Account").setEnabled(true);
                    EBISystem.gui().button("historyAccount", "Account").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("accountTable", "Account").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedInvoiceRow = selRow;
                editAccount();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedInvoiceRow = selRow;
                editAccount();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedInvoiceRow = selRow;
                if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModAccount.data[selectedInvoiceRow][0].toString())) {
                    return;
                }
                editAccount();
            }
        });

        EBISystem.gui().table("accountTable", "Account").addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("accountTable", "Account").rowAtPoint(e.getPoint()) != -1) {
                    selectedInvoiceRow = EBISystem.gui().table("accountTable", "Account").convertRowIndexToModel(EBISystem.gui().table("accountTable", "Account").rowAtPoint(e.getPoint()));
                }
                if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModAccount.data[selectedInvoiceRow][0].toString())) {
                    return;
                }
                editAccount();
            }
        });

        //ACCOUNT DOCUMENTS
        EBISystem.gui().table("tableAccountDoc", "Account").setModel(tabModDoc);
        EBISystem.gui().table("tableAccountDoc", "Account").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableAccountDoc", "Account").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("tableAccountDoc", "Account").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showAccountDoc", "Account").setEnabled(false);
                    EBISystem.gui().button("deleteAccountDoc", "Account").setEnabled(false);
                } else if (!tabModDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showAccountDoc", "Account").setEnabled(true);
                    EBISystem.gui().button("deleteAccountDoc", "Account").setEnabled(true);
                }
            }
        });

        EBISystem.gui().combo("accountTypeText", "Account").setModel(new DefaultComboBoxModel(accoutType));
        //CREDIT DEBIT PANEL
        EBISystem.gui().combo("selectCreditDebitText", "Account").setModel(new DefaultComboBoxModel(creditDebitType));
        EBISystem.gui().combo("selectCreditDebitText", "Account").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                dataControlAccount.dataShowCreditDebitExt(((JComboBox) e.getSource()).getSelectedIndex());
            }
        });

        EBISystem.gui().table("debCreditTable", "Account").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("debCreditTable", "Account").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedCDRow = EBISystem.gui().table("debCreditTable", "Account").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editCreditDebit", "Account").setEnabled(false);
                    EBISystem.gui().button("deleteCreditDebit", "Account").setEnabled(false);
                } else if (!creditDebitMod.data[selectedCDRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editCreditDebit", "Account").setEnabled(true);
                    EBISystem.gui().button("deleteCreditDebit", "Account").setEnabled(true);
                }
            }
        });

        // Initialize Action for Account years
        EBISystem.gui().combo("invoiceYearText", "Account").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (((JComboBox) e.getSource()).getSelectedIndex() != -1) {
                    selectedYear = ((JComboBox) e.getSource()).getSelectedItem().toString();
                }
            }
        });
    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModDoc = new ModelDoc();
            creditDebitMod = new ModelCreditDebit();
            tabModAccount = new ModelCRMAccount();

            EBISystem.gui().table("debCreditTable", "Account").setModel(creditDebitMod);
            EBISystem.gui().table("accountTable", "Account").setModel(tabModAccount);
            EBISystem.gui().table("tableAccountDoc", "Account").setModel(tabModDoc);
        }

        EBISystem.hibernate().openHibernateSession("EBIACCOUNT_SESSION");

        creditDebitType = new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            EBISystem.i18n("EBI_LANG_DEBIT"),
            EBISystem.i18n("EBI_LANG_CREDIT"), EBISystem.i18n("EBI_LANG_DEPOSIT")};

        accoutType = new String[]{EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_ACTIVE"),
            EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_PASSIVE"),
            EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_EARNING"),
            EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_LIST_EXPEDITURE")};

        dataControlAccount.dataShowCreditDebit();

        EBISystem.gui().combo("invoiceYearText", "Account").setEditable(true);
        EBISystem.gui().combo("invoiceYearText", "Account").removeAllItems();

        EBISystem.gui().vpanel("Account").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Account").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Account").setChangedDate("");
        EBISystem.gui().vpanel("Account").setChangedFrom("");

        EBISystem.gui().textField("numberText", "Account").setText("");
        EBISystem.gui().textField("nameText", "Account").setText("");

        EBISystem.gui().combo("accountTypeText", "Account").setEditable(true);

        EBISystem.gui().textField("descriptionDebit", "Account").setText("");
        EBISystem.gui().textField("descriptionDebit", "Account").setEditable(false);

        EBISystem.gui().textField("descriptionCredit", "Account").setText("");
        EBISystem.gui().textField("descriptionCredit", "Account").setEditable(false);

        EBISystem.gui().textField("debitText", "Account").setText("");
        EBISystem.gui().textField("creditText", "Account").setText("");

        EBISystem.gui().textArea("descriptionText", "Account").setText("");

        taxFormat = NumberFormat.getNumberInstance();
        taxFormat.setMinimumFractionDigits(2);
        taxFormat.setMaximumFractionDigits(2);

        EBISystem.gui().FormattedField("amountText", "Account").setValue(null);
        EBISystem.gui().FormattedField("amountText", "Account").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("amountText", "Account").setHorizontalAlignment(SwingConstants.RIGHT);

        EBISystem.gui().FormattedField("debitCal", "Account").setValue(null);
        EBISystem.gui().FormattedField("debitCal", "Account").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("debitCal", "Account").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().FormattedField("debitCal", "Account").setEditable(false);

        EBISystem.gui().FormattedField("creditCal", "Account").setValue(null);
        EBISystem.gui().FormattedField("creditCal", "Account").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("creditCal", "Account").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().FormattedField("creditCal", "Account").setEditable(false);

        try {
            if (!"null".equals(EBISystem.properties().getValue("SELECTED_ACCOUNTYEAR_TEXT")) && !"".equals(EBISystem.properties().getValue("SELECTED_ACCOUNTYEAR_TEXT"))) {
                EBISystem.gui().combo("invoiceYearText", "Account").setSelectedItem(EBISystem.properties().getValue("SELECTED_ACCOUNTYEAR_TEXT"));
                dataControlAccount.dataShow(EBISystem.properties().getValue("SELECTED_ACCOUNTYEAR_TEXT"), -1);
            } else {
                dataControlAccount.dataShow("", -1);
            }

            if (!"null".equals(EBISystem.properties().getValue("ACCOUNTYEAR_TEXT")) 
                    && !"".equals(EBISystem.properties().getValue("ACCOUNTYEAR_TEXT"))) {

                final String[] years = EBISystem.properties().getValue("ACCOUNTYEAR_TEXT").split(",");
                if (years != null) {
                    for (int i = 0; i < years.length; i++) {
                        if (years[i] != null) {
                            EBISystem.gui().combo("invoiceYearText", "Account").insertItemAt(years[i], i);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < EBISystem.gui().table("debCreditTable", "Account").getColumnCount(); i++) {
            final TableColumnExt col = EBISystem.gui().table("debCreditTable", "Account").getColumnExt(i);
            if (i != 1) {
                col.setWidth(40);
                col.setPreferredWidth(40);
            } else {
                col.setWidth(500);
                col.setPreferredWidth(500);
            }
        }
    }

    public void showAccountReport() {
        dataControlAccount.dataShowReport();
    }


    public boolean saveAccount() {
        if (!validateInput()) {
            return false;
        }

        EBISystem.showInActionStatus("Account");
        int row = EBISystem.gui().table("accountTable", "Account").getSelectedRow();
        Integer id = dataControlAccount.dataStore();
        dataControlAccount.dataShow(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString(), id);
        dataControlAccount.isEdit = true;
        EBISystem.gui().table("accountTable", "Account").changeSelection(row, 0, false, false);
        return true;
    }

    public void deleteAccount() {
        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModAccount.data[selectedInvoiceRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Account");
            dataControlAccount.dataDelete(Integer.parseInt(tabModAccount.data[selectedInvoiceRow][6].toString()));
            dataControlAccount.dataNew();
            dataControlAccount.dataShow(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString(), -1);
            dataControlAccount.isEdit = false;
        }
    }

    public void editAccount() {
        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModAccount.data[selectedInvoiceRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Account");
        dataControlAccount.dataNew();
        dataControlAccount.dataEdit(Integer.parseInt(tabModAccount.data[selectedInvoiceRow][6].toString()));
        dataControlAccount.dataShowDoc();
        dataControlAccount.isEdit = true;
    }

    public void newAccount() {
        EBISystem.showInActionStatus("Account");
        dataControlAccount.dataNew();
        dataControlAccount.dataShow(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString(), -1);
        dataControlAccount.dataShowDoc();
        dataControlAccount.isEdit = false;
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Account");
            dataControlAccount.dataDeleteDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
            dataControlAccount.dataShowDoc();
        }
    }

    public void saveAndShowDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabModDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlAccount.dataViewDoc(Integer.parseInt(tabModDoc.data[selectedDocRow][3].toString()));
    }

    public void newDocs() {
        dataControlAccount.dataNewDoc();
        dataControlAccount.dataShowDoc();
    }

    public void deleteCreditDebit() {
        if (selectedCDRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(creditDebitMod.data[selectedCDRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Account");
            dataControlAccount.dataDeleteCreditDebit(Integer.parseInt(creditDebitMod.data[selectedCDRow][2].toString()));
        }
    }

    public void editCreditDebit() {
        if (selectedCDRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(creditDebitMod.data[selectedCDRow][0].toString())) {
            return;
        }
        newCreditDebit(Integer.parseInt(creditDebitMod.data[selectedCDRow][2].toString()));
    }

    public void newDebitCredit() {
        newCreditDebit(-1);
    }

    public void newCreditDebit(final int id) {
        EBISystem.gui().loadGUI("CRMDialog/creditDebitDialog.xml");
        EBISystem.gui().FormattedField("valueText", "creditDebitDialog").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (((JComboBox) e.getSource()).getSelectedIndex() == 3) {
                    EBISystem.gui().label("vale", "creditDebitDialog").setVisible(true);
                    EBISystem.gui().label("taxType", "creditDebitDialog").setVisible(false);
                    EBISystem.gui().label("vale", "creditDebitDialog").setLocation(10, 69);
                    EBISystem.gui().FormattedField("valueText", "creditDebitDialog").setVisible(true);
                    EBISystem.gui().FormattedField("valueText", "creditDebitDialog").setLocation(110, 69);
                    EBISystem.gui().combo("taxTypeText", "creditDebitDialog").setVisible(false);
                    EBISystem.gui().label("taxValue", "creditDebitDialog").setVisible(false);
                } else {
                    EBISystem.gui().label("vale", "creditDebitDialog").setVisible(false);
                    EBISystem.gui().label("taxType", "creditDebitDialog").setVisible(true);
                    EBISystem.gui().FormattedField("valueText", "creditDebitDialog").setVisible(false);
                    EBISystem.gui().combo("taxTypeText", "creditDebitDialog").setVisible(true);
                    EBISystem.gui().label("taxValue", "creditDebitDialog").setVisible(true);
                }
            }
        });

        EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").setModel(new DefaultComboBoxModel(creditDebitType));
        EBISystem.gui().combo("taxTypeText", "creditDebitDialog").setModel(new DefaultComboBoxModel(EBICRMProductView.taxType));
        EBISystem.gui().combo("taxTypeText", "creditDebitDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (((JComboBox) e.getSource()).getSelectedItem() != null) {
                    EBISystem.gui().label("taxValue", "creditDebitDialog").setText(dataControlAccount.getTaxValue(((JComboBox) e.getSource()).getSelectedItem().toString()) + "%");
                }
            }
        });

        EBISystem.gui().button("saveValue", "creditDebitDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!validateInputDialog()) {
                    return;
                }

                dataControlAccount.dataStoreCreditDebit(id < 0 ? false : true, id);
            }
        });

        EBISystem.gui().button("closeDialog", "creditDebitDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                EBISystem.gui().dialog("creditDebitDialog").setVisible(false);
            }
        });

        if (id > -1) {
            dataControlAccount.dataEditCreditDebit(id);
        }

        EBISystem.gui().showGUI();
    }

    public void showCreditDebitListDialog() {

        EBISystem.gui().loadGUI("CRMDialog/crmSelectionDialog.xml");
        dataControlAccount.dataShowCreditDebit();

        EBISystem.gui().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_LIST"));

        EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").getText()));
            }
        });

        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setModel(creditDebitMod);
        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedCDDialogRow = EBISystem.gui().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

            }
        });

        new JTableActionMaps(EBISystem.gui().table("abstractTable", "abstractSelectionDialog")).setTableAction(new AbstractTableKeyAction() {

            @Override
            public void setArrowDownKeyAction(final int selRow) {
                selectedCDDialogRow = selRow;
            }

            @Override
            public void setArrowUpKeyAction(final int selRow) {
                selectedCDDialogRow = selRow;
            }

            @Override
            public void setEnterKeyAction(final int selRow) {
                selectedCDDialogRow = selRow;

                if (selectedCDDialogRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(creditDebitMod.data[selectedCDDialogRow][0].toString())) {
                    return;
                }
                showDebitCreditToList(Integer.parseInt(creditDebitMod.data[selectedCDDialogRow][2].toString()));
            }
        });

        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {

                if (EBISystem.gui().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1) {
                    selectedCDDialogRow = EBISystem.gui().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(EBISystem.gui().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()));
                }
                if (selectedCDDialogRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(creditDebitMod.data[selectedCDDialogRow][0].toString())) {
                    return;
                }

                showDebitCreditToList(Integer.parseInt(creditDebitMod.data[selectedCDDialogRow][2].toString()));

            }
        });

        EBISystem.gui().button("closeButton", "abstractSelectionDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
            }
        });

        EBISystem.gui().button("applyButton", "abstractSelectionDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (selectedCDDialogRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(creditDebitMod.data[selectedCDDialogRow][0].toString())) {
                    return;
                }
                showDebitCreditToList(Integer.parseInt(creditDebitMod.data[selectedCDDialogRow][2].toString()));
            }
        });

        EBISystem.gui().showGUI();
        dataControlAccount.dataShowCreditDebit();
    }

    public void showDebitCreditToList(final int idCreditDebit) {
        if (idCreditDebit > -1) {
            dataControlAccount.dataCalculateTax(idCreditDebit);
        }
        if (EBISystem.gui().dialog("abstractSelectionDialog") != null) {
            EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
        }
    }

    public void historyAccount() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(Integer.parseInt(tabModAccount.data[selectedInvoiceRow][6].toString()), "Account")).setVisible();
    }

    public void updateYear() {
        if (EBISystem.gui().combo("invoiceYearText", "Account").getItemCount() >= 1) {
            boolean isAvailable = false;
            if (!"".equals(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem())) {
                for (int i = 0; i <= EBISystem.gui().combo("invoiceYearText", "Account").getItemCount(); i++) {
                    if (EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().equals(EBISystem.gui().combo("invoiceYearText", "Account").getItemAt(i))) {
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable) {
                    EBISystem.gui().combo("invoiceYearText", "Account").addItem(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString());
                }
            } else {
                if (!"".equals(selectedYear)) {
                    EBISystem.gui().combo("invoiceYearText", "Account").removeItem(selectedYear);
                }
            }
        } else {
            if (!"".equals(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem())) {
                EBISystem.gui().combo("invoiceYearText", "Account").addItem(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString());
            }
        }

        // create comma separated value
        String vSave = "";
        if (EBISystem.gui().combo("invoiceYearText", "Account").getItemCount() > 0) {

            for (int i = 0; i < EBISystem.gui().combo("invoiceYearText", "Account").getItemCount(); i++) {

                if (i < EBISystem.gui().combo("invoiceYearText", "Account").getItemCount() - 1) {
                    vSave += EBISystem.gui().combo("invoiceYearText", "Account").getItemAt(i).toString() + ",";
                } else {
                    vSave += EBISystem.gui().combo("invoiceYearText", "Account").getItemAt(i).toString();
                }
            }
        }

        //  Sort
        if (EBISystem.gui().combo("invoiceYearText", "Account").getItemCount() > 0) {
            final String[] avalItems = vSave.split(",");
            Arrays.sort(avalItems);
            final String selected = EBISystem.gui().combo("invoiceYearText", "Account").getSelectedItem().toString();
            EBISystem.gui().combo("invoiceYearText", "Account").removeAllItems();
            vSave = "";
            for (int i = 0; i < avalItems.length; i++) {
                EBISystem.gui().combo("invoiceYearText", "Account").addItem(avalItems[i]);
                if (i < avalItems.length - 1) {
                    vSave += avalItems[i] + ",";
                } else {
                    vSave += avalItems[i];
                }
            }
            EBISystem.gui().combo("invoiceYearText", "Account").setSelectedItem(selected);
            EBISystem.properties().setValue("ACCOUNTYEAR_TEXT", vSave);
        }

        EBISystem.properties().setValue("SELECTED_ACCOUNTYEAR_TEXT", EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString());
        EBISystem.properties().saveEBINeutrinoProperties();
        dataControlAccount.dataShow(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString(), -1);
    }

    public void importInvoice() {
        dataControlAccount.imporInvoicetoAccout(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString());
    }

    private boolean validateInputDialog() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("numberText", "creditDebitDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().textField("nameText", "creditDebitDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    private boolean validateInput() {
        if ("".equals(EBISystem.gui().timePicker("dateText", "Account").getEditor().getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_ILLEGAL_DATE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().textField("numberText", "Account").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().textField("nameText", "Account").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().FormattedField("amountText", "Account").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_INSERT_AMOUNT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().textField("debitText", "Account").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_INSERT_DEBIT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("".equals(EBISystem.gui().textField("creditText", "Account").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_INSERT_CREDIT")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
