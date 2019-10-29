package org.modules.views;

import org.modules.controls.ControlInvoice;
import org.modules.views.dialogs.EBICRMDialogAddProduct;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIDialogSearchContact;
import org.modules.models.ModelCRMProduct;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIAbstractTableModel;
import org.sdk.utils.EBIPropertiesRW;
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
import java.util.Arrays;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMInvoiceView {

    @Getter
    @Setter
    private ModelCRMProduct tabModProduct = null;
    public static String[] invoiceStatus = null;
    public static String[] invoiceCategory = null;
    @Getter
    @Setter
    private ControlInvoice dataControlInvoice = new ControlInvoice();
    @Getter
    @Setter
    private int selectedInvoiceRow = -1;
    @Getter
    @Setter
    private int selectedProductRow = -1;
    @Getter
    @Setter
    private String beginChar = "";
    @Getter
    @Setter
    private int invoiceNr = -1;
    @Getter
    @Setter
    private String selectedYear = "";
    @Getter
    @Setter
    private EBIAbstractTableModel model = null;
    private final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

    public void initializeAction() {
        try {

            if (!"null".equals(properties.getValue("SELECTED_INVOICEYEAR_TEXT")) && !"".equals(properties.getValue("SELECTED_INVOICEYEAR_TEXT"))) {
                EBISystem.gui().combo("invoiceYearText", "Invoice").setSelectedItem(properties.getValue("SELECTED_INVOICEYEAR_TEXT"));
                dataControlInvoice.dataShow(properties.getValue("SELECTED_INVOICEYEAR_TEXT"), -1);
            } else {
                dataControlInvoice.dataShow("", -1);
            }

            if (!"null".equals(properties.getValue("INVOICEYEAR_TEXT")) && !"".equals(properties.getValue("INVOICEYEAR_TEXT"))) {

                final String[] years = properties.getValue("INVOICEYEAR_TEXT").split(",");
                if (years != null) {
                    for (int i = 0; i < years.length; i++) {
                        if (years[i] != null) {
                            EBISystem.gui().combo("invoiceYearText", "Invoice")
                                    .insertItemAt(years[i], i);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        model = (EBIAbstractTableModel) EBISystem.gui().table("tableTotalInvoice", "Invoice").getModel();
        EBISystem.gui().textField("filterTableText", "Invoice").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("tableTotalInvoice", "Invoice").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Invoice").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("tableTotalInvoice", "Invoice").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Invoice").getText()));
            }
        });

        EBISystem.gui().combo("invoiceStatusText", "Invoice").setEditable(true);
        EBISystem.gui().combo("categoryText", "Invoice").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                if (!EBISystem.gui().combo("categoryText", "Invoice").getSelectedItem()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {

                    if (!dataControlInvoice.isEdit) {

                        final Object[] obj = EBISystem.getModule().dynMethod.getInternNumber(EBISystem.gui()
                                .combo("categoryText", "Invoice").getSelectedItem().toString(), true);

                        beginChar = obj[1].toString();
                        invoiceNr = Integer.parseInt(obj[0].toString());

                        EBISystem.gui().textField("invoiceNrText", "Invoice")
                                .setText(obj[1].toString() + obj[0].toString());

                    }
                }

            }
        });

        /**
         * ************************************
         */
        // Contact Information
        /**
         * ***********************************
         */
        EBISystem.gui().combo("genderText", "Invoice").setEditable(true);

        EBISystem.gui().table("invoicePositionTable", "Invoice").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("invoicePositionTable", "Invoice").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.gui().table("invoicePositionTable", "Invoice")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (tabModProduct.data.length > 0) {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("deletePosition", "Invoice").setEnabled(false);
                    } else if (!tabModProduct.data[selectedProductRow][0].toString()
                            .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.gui().button("deletePosition", "Invoice").setEnabled(true);
                    }
                }
            }
        });

        EBISystem.gui().table("tableTotalInvoice", "Invoice").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableTotalInvoice", "Invoice").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedInvoiceRow = EBISystem.gui().table("tableTotalInvoice", "Invoice").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                try {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("editInvoice", "Invoice").setEnabled(false);
                        EBISystem.gui().button("deleteInvoice", "Invoice").setEnabled(false);
                        EBISystem.gui().button("historyInvoice", "Invoice").setEnabled(false);
                        EBISystem.gui().button("reportInvoice", "Invoice").setEnabled(false);
                        EBISystem.gui().button("sendEmail", "Invoice").setEnabled(false);
                    } else if (!model.data[selectedInvoiceRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.gui().button("editInvoice", "Invoice").setEnabled(true);
                        EBISystem.gui().button("deleteInvoice", "Invoice").setEnabled(true);
                        EBISystem.gui().button("historyInvoice", "Invoice").setEnabled(true);
                        EBISystem.gui().button("reportInvoice", "Invoice").setEnabled(true);
                        EBISystem.gui().button("sendEmail", "Invoice").setEnabled(true);
                    }
                } catch (final ArrayIndexOutOfBoundsException ex) {
                }
            }
        });

        EBISystem.gui().table("tableTotalInvoice", "Invoice").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedInvoiceRow = selRow;
                editInvoice();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedInvoiceRow = selRow;
                editInvoice();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedInvoiceRow = selRow;

                if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(model.data[selectedInvoiceRow][0].toString())) {
                    return;
                }
                editInvoice();
            }
        });

        EBISystem.gui().table("tableTotalInvoice", "Invoice")
                .setMouseCallback(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final java.awt.event.MouseEvent e) {
                        if (EBISystem.gui().table("tableTotalInvoice", "Invoice")
                                .rowAtPoint(e.getPoint()) != -1) {
                            selectedInvoiceRow = EBISystem.gui().table("tableTotalInvoice", "Invoice")
                                    .convertRowIndexToModel(EBISystem.gui()
                                            .table("tableTotalInvoice", "Invoice").rowAtPoint(e.getPoint()));
                        }
                        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                                .equals(model.data[selectedInvoiceRow][0].toString())) {
                            return;
                        }
                        editInvoice();
                    }
                });

        EBISystem.gui().combo("invoiceYearText", "Invoice").addActionListener(new ActionListener() {
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
            tabModProduct = new ModelCRMProduct();
            EBISystem.gui().table("invoicePositionTable", "Invoice").setModel(tabModProduct);
        }

        EBISystem.hibernate().openHibernateSession("EBIINVOICE_SESSION");
        EBISystem.gui().combo("invoiceStatusText", "Invoice").setModel(new DefaultComboBoxModel(invoiceStatus));
        EBISystem.gui().combo("categoryText", "Invoice").setModel(new DefaultComboBoxModel(invoiceCategory));
        EBISystem.gui().combo("genderText", "Invoice").setModel(new DefaultComboBoxModel(EBICRMContactView.gendersList));

        beginChar = "";
        EBISystem.gui().vpanel("Invoice").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Invoice").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Invoice").setChangedDate("");
        EBISystem.gui().vpanel("Invoice").setChangedFrom("");

        final NumberFormat taxFormat = NumberFormat.getNumberInstance();
        taxFormat.setMinimumFractionDigits(2);
        taxFormat.setMaximumFractionDigits(3);

        EBISystem.gui().combo("invoiceYearText", "Invoice").setEditable(true);
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setValue(null);
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setEditable(false);
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().FormattedField("taxText", "Invoice").setValue(null);
        EBISystem.gui().FormattedField("taxText", "Invoice").setEditable(false);
        EBISystem.gui().FormattedField("taxText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));

        EBISystem.gui().FormattedField("taxText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.gui().FormattedField("taxText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);

        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice").setValue(null);
        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice").setEditable(false);
        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);

        EBISystem.gui().FormattedField("deductionText", "Invoice").setValue(null);
        EBISystem.gui().FormattedField("deductionText", "Invoice").setEditable(false);
        EBISystem.gui().FormattedField("deductionText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.gui().FormattedField("deductionText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.gui().FormattedField("deductionText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().button("selectOrder", "Invoice").setEnabled(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.gui().table("invoicePositionTable", "Invoice").getColumnModel().getColumn(5);
                col7.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
                            final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });
            }
        });

        EBISystem.gui().textField("invoiceNrText", "Invoice").setEditable(false);
        EBISystem.gui().combo("invoiceStatusText", "Invoice").setSelectedIndex(0);
        EBISystem.gui().combo("categoryText", "Invoice").setSelectedIndex(0);

        EBISystem.gui().textField("invoiceNrText", "Invoice").setText("");
        EBISystem.gui().textField("invoiceNameText", "Invoice").setText("");
        EBISystem.gui().textField("orderText", "Invoice").setEnabled(false);
        EBISystem.gui().textField("orderText", "Invoice").setText("");

        EBISystem.gui().combo("genderText", "Invoice").setSelectedIndex(0);
        EBISystem.gui().textField("titleText", "Invoice").setText("");
        EBISystem.gui().textField("companyNameText", "Invoice").setText("");
        EBISystem.gui().textField("nameText", "Invoice").setText("");
        EBISystem.gui().textField("surnameText", "Invoice").setText("");
        EBISystem.gui().textField("streetNrText", "Invoice").setText("");
        EBISystem.gui().textField("zipText", "Invoice").setText("");
        EBISystem.gui().textField("locationText", "Invoice").setText("");
        EBISystem.gui().textField("postCodeText", "Invoice").setText("");
        EBISystem.gui().textField("countryText", "Invoice").setText("");
        EBISystem.gui().textField("telefonText", "Invoice").setText("");
        EBISystem.gui().textField("faxText", "Invoice").setText("");
        EBISystem.gui().textField("emailText", "Invoice").setText("");
        EBISystem.gui().textField("internetText", "Invoice").setText("");

        EBISystem.gui().textArea("recDescription", "Invoice").setText("");
        EBISystem.gui().timePicker("invoiceDateText", "Invoice").getEditor().setText("");
        EBISystem.gui().timePicker("invoiceDateText", "Invoice").setDate(new Date());
        EBISystem.gui().timePicker("invoiceDateText", "Invoice").setFormats(EBISystem.DateFormat);

        EBISystem.gui().button("deletePosition", "Invoice").setEnabled(false);
    }

    public void newInvoice() {
        EBISystem.showInActionStatus("Invoice");
        dataControlInvoice.dataNew();
        dataShow(-1);
        dataControlInvoice.dataShowProduct();
        dataControlInvoice.isEdit = false;
    }

    public void dataShow(final int invoiceID) {
        dataControlInvoice.dataShow(EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem().toString(), invoiceID);
    }

    public void dataShowProduct() {
        dataControlInvoice.dataShowProduct();
    }

    public boolean saveInvoice() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Invoice");
        int row = EBISystem.gui().table("tableTotalInvoice", "Invoice").getSelectedRow();
        Integer id = dataControlInvoice.dataStore();
        dataShow(id);
        dataControlInvoice.dataShowProduct();
        dataControlInvoice.isEdit = true;
        EBISystem.gui().table("tableTotalInvoice", "Invoice").changeSelection(row, 0, false, false);
        return true;
    }

    public void editInvoice() {
        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(model.data[selectedInvoiceRow][0].toString())) {
            return;
        }

        EBISystem.showInActionStatus("Invoice");
        dataControlInvoice.dataNew();
        dataControlInvoice.dataEdit(Integer.parseInt(model.data[selectedInvoiceRow][9].toString()));
        dataControlInvoice.dataShowProduct();
        dataControlInvoice.isEdit = true;
    }

    public void deleteInvoice() {
        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(model.data[selectedInvoiceRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Invoice");
            dataControlInvoice.dataDelete(Integer.parseInt(model.data[selectedInvoiceRow][9].toString()));
            dataControlInvoice.dataNew();
            dataShow(-1);
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("invoiceNrText", "Invoice").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_INVOICE_NR")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.gui().combo("invoiceStatusText", "Invoice").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }

        if (dataControlInvoice.isEdit == false) {
            final EBIAbstractTableModel tabModel = (EBIAbstractTableModel) EBISystem.gui().table("tableTotalInvoice", "Invoice").getModel();
            for (int i = 0; i < tabModel.data.length; i++) {
                if (tabModel.data[i][0].equals(EBISystem.gui().textField("invoiceNrText", "Invoice").getText())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INVOICE_EXIST_WITH_SAME_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                    ret = false;
                }
            }
        }
        return ret;
    }

    private void showInvoiceReport(final int id) {
        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            EBISystem.showInActionStatus("Invoice");
            dataControlInvoice.dataShowReport(id);
        }
    }

    public void historyInvoice() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(
                Integer.parseInt(model.data[selectedInvoiceRow][9].toString()), "Invoice"))
                .setVisible();
    }

    public void reportInvoice() {
        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(model.data[selectedInvoiceRow][0].toString())) {
            return;
        }

        if (dataControlInvoice.getInvoice().getCrminvoicepositions() != null
                && dataControlInvoice.getInvoice().getCrminvoicepositions().size() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_POSITION"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            return;
        }

        showInvoiceReport(Integer.parseInt(model.data[selectedInvoiceRow][9].toString()));
    }

    public void mailInvoice() {

        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(model.data[selectedInvoiceRow][0].toString())) {
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
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("templateText", "sendEMailMessage").getSelectedItem().toString())) {
                    EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage")
                            .setText(EBISystem.getModule().dynMethod.getEMailTemplate(EBISystem.gui().combo("templateText", "sendEMailMessage").getSelectedItem().toString()));
                }
            }
        });
        EBISystem.gui().button("sendEmail", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SEND"));
        EBISystem.gui().button("closeEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.gui().button("sendEmail", "sendEMailMessage").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
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
                    EBISystem.showInActionStatus("Invoice");
                    dataControlInvoice.dataShowAndMailReport(Integer.parseInt(model.data[selectedInvoiceRow][9].toString()),
                            EBISystem.gui().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
                    EBISystem.gui().dialog("sendEMailMessage").setVisible(false);
                }
            }
        });

        EBISystem.gui().button("closeEMailDialog", "sendEMailMessage").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                EBISystem.gui().dialog("sendEMailMessage").setVisible(false);
            }
        });
        EBISystem.gui().showGUI();
    }

    public boolean validateDateInput() {
        boolean ret = true;
        if (!"".equals(EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem().toString())) {
            if (EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem().toString().length() < 4) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_YEAR")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
            try {
                Integer.parseInt(EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
            } catch (final NumberFormatException ex) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_YEAR")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
        }
        return ret;
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

    public void updateyear() {
        if (!validateDateInput()) {
            return;
        }

        if (EBISystem.gui().combo("invoiceYearText", "Invoice").getItemCount() >= 1) {
            boolean isAvailable = false;
            if (!"".equals(
                    EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem())) {
                for (int i = 0; i <= EBISystem.gui().combo("invoiceYearText", "Invoice")
                        .getItemCount(); i++) {
                    if (EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem()
                            .equals(EBISystem.gui().combo("invoiceYearText", "Invoice")
                                    .getItemAt(i))) {
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable) {
                    EBISystem.gui().combo("invoiceYearText", "Invoice")
                            .addItem(EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor()
                                    .getItem().toString());
                }
            } else {
                if (!"".equals(selectedYear)) {
                    EBISystem.gui().combo("invoiceYearText", "Invoice").removeItem(selectedYear);
                }
            }
        } else {
            if (!"".equals(
                    EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem())) {
                EBISystem.gui().combo("invoiceYearText", "Invoice").addItem(EBISystem.gui()
                        .combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
            }
        }

        // create comma separated value
        String vSave = "";
        if (EBISystem.gui().combo("invoiceYearText", "Invoice").getItemCount() > 0) {

            for (int i = 0; i < EBISystem.gui().combo("invoiceYearText", "Invoice")
                    .getItemCount(); i++) {

                if (i < EBISystem.gui().combo("invoiceYearText", "Invoice").getItemCount() - 1) {
                    vSave += EBISystem.gui().combo("invoiceYearText", "Invoice").getItemAt(i).toString() + ",";
                } else {
                    vSave += EBISystem.gui().combo("invoiceYearText", "Invoice").getItemAt(i).toString();
                }
            }
        }

        // Sort
        if (EBISystem.gui().combo("invoiceYearText", "Invoice").getItemCount() > 0) {

            final String[] avalItems = vSave.split(",");
            Arrays.sort(avalItems);
            final String selected = EBISystem.gui().combo("invoiceYearText", "Invoice").getSelectedItem().toString();
            EBISystem.gui().combo("invoiceYearText", "Invoice").removeAllItems();
            vSave = "";
            for (int i = 0; i < avalItems.length; i++) {
                EBISystem.gui().combo("invoiceYearText", "Invoice").addItem(avalItems[i]);
                if (i < avalItems.length - 1) {
                    vSave += avalItems[i] + ",";
                } else {
                    vSave += avalItems[i];
                }
            }
            EBISystem.gui().combo("invoiceYearText", "Invoice").setSelectedItem(selected);
            properties.setValue("INVOICEYEAR_TEXT", vSave);
        }

        properties.setValue("SELECTED_INVOICEYEAR_TEXT", EBISystem.gui().combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
        properties.saveEBINeutrinoProperties();
        dataShow(-1);
    }

    public void selectOrder() {
        final String[] spl = EBISystem.gui().textField("orderText", "Invoice").getText().split(":");
        if (spl != null) {
            if (spl[0].equals(EBISystem.i18n("EBI_LANG_C_SERVICE"))) {
                if (dataControlInvoice.loadCompanyService(Integer.parseInt(spl[1].trim()))) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance()
                            .getIndexByTitle(EBISystem.i18n("EBI_LANG_C_SERVICE")));
                    EBISystem.getModule().getServicePane().remoteEditService(Integer.parseInt(spl[1].trim()));
                } else {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                            .Show(EBIMessage.ERROR_MESSAGE);
                }
            } else {
                if (dataControlInvoice.loadCompanyOrder(Integer.parseInt(spl[1].trim()))) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance()
                            .getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ORDER")));
                    EBISystem.getModule().getOrderPane().editOrderRemote(Integer.parseInt(spl[1].trim()));
                } else {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                            .Show(EBIMessage.ERROR_MESSAGE);
                }
            }
        }
    }

    public void searchContact() {
        final EBIDialogSearchContact addCon = new EBIDialogSearchContact(false);

        addCon.setValueToComponent(EBISystem.gui().combo("genderText", "Invoice"), "Gender");
        addCon.setValueToComponent(EBISystem.gui().textField("titleText", "Invoice"), "Position");
        addCon.setValueToComponent(EBISystem.gui().textField("surnameText", "Invoice"), "Surname");
        addCon.setValueToComponent(EBISystem.gui().textField("nameText", "Invoice"), "contact.Name");
        addCon.setValueToComponent(EBISystem.gui().textField("streetNrText", "Invoice"), "Street");
        addCon.setValueToComponent(EBISystem.gui().textField("zipText", "Invoice"), "Zip");
        addCon.setValueToComponent(EBISystem.gui().textField("locationText", "Invoice"), "Location");
        addCon.setValueToComponent(EBISystem.gui().textField("countryText", "Invoice"), "Country");
        addCon.setValueToComponent(EBISystem.gui().textField("postCodeText", "Invoice"), "PBox");
        addCon.setValueToComponent(EBISystem.gui().textField("emailText", "Invoice"), "EMail");
        addCon.setValueToComponent(EBISystem.gui().textField("faxText", "Invoice"), "Fax");
        addCon.setValueToComponent(EBISystem.gui().textField("telefonText", "Invoice"), "phone");
        addCon.setValueToComponent(EBISystem.gui().textArea("recDescription", "Invoice"),
                "contact.description");
        addCon.setValueToComponent(EBISystem.gui().textField("companyNameText", "Invoice"),
                "company.NAME");
        addCon.setValueToComponent(EBISystem.gui().textField("internetText", "Invoice"),
                "company.WEB");

        addCon.setVisible();
    }

    public void newPosition() {
        final EBICRMDialogAddProduct product = new EBICRMDialogAddProduct(dataControlInvoice.getInvoice());
        product.setVisible();
        dataControlInvoice.calculateTotalAmount();
    }

    public void deletePosition() {
        if (selectedProductRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModProduct.data[selectedProductRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            boolean pass;
            if (EBISystem.getInstance().getIEBISystemUserRights().isCanDelete()
                    || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
                pass = true;
            } else {
                pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
            }
            if (pass) {
                dataControlInvoice.dataDeleteProduct(Integer.parseInt(tabModProduct.data[selectedProductRow][8].toString()));
                dataControlInvoice.calculateTotalAmount();
            }
        }
    }

}
