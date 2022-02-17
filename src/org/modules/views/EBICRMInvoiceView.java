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
                EBISystem.builder().combo("invoiceYearText", "Invoice").setSelectedItem(properties.getValue("SELECTED_INVOICEYEAR_TEXT"));
                dataControlInvoice.dataShow(properties.getValue("SELECTED_INVOICEYEAR_TEXT"), -1);
            } else {
                dataControlInvoice.dataShow("", -1);
            }

            if (!"null".equals(properties.getValue("INVOICEYEAR_TEXT")) && !"".equals(properties.getValue("INVOICEYEAR_TEXT"))) {

                final String[] years = properties.getValue("INVOICEYEAR_TEXT").split(",");
                if (years != null) {
                    for (int i = 0; i < years.length; i++) {
                        if (years[i] != null) {
                            EBISystem.builder().combo("invoiceYearText", "Invoice")
                                    .insertItemAt(years[i], i);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        model = (EBIAbstractTableModel) EBISystem.builder().table("tableTotalInvoice", "Invoice").getModel();
        EBISystem.builder().textField("filterTableText", "Invoice").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("tableTotalInvoice", "Invoice").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.builder().textField("filterTableText", "Invoice").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("tableTotalInvoice", "Invoice").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.builder().textField("filterTableText", "Invoice").getText()));
            }
        });

        EBISystem.builder().combo("invoiceStatusText", "Invoice").setEditable(true);
        EBISystem.builder().combo("categoryText", "Invoice").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                if (!EBISystem.builder().combo("categoryText", "Invoice").getSelectedItem()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {

                    if (!dataControlInvoice.isEdit) {

                        final Object[] obj = EBISystem.getModule().dynMethod.getInternNumber(EBISystem.builder()
                                .combo("categoryText", "Invoice").getSelectedItem().toString(), true);

                        beginChar = obj[1].toString();
                        invoiceNr = Integer.parseInt(obj[0].toString());

                        EBISystem.builder().textField("invoiceNrText", "Invoice")
                                .setText(obj[1].toString() + obj[0].toString());

                    }
                }else{
                      EBISystem.builder().textField("invoiceNrText", "Invoice").setText("");
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
        EBISystem.builder().combo("genderText", "Invoice").setEditable(true);

        EBISystem.builder().table("invoicePositionTable", "Invoice").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("invoicePositionTable", "Invoice").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProductRow = EBISystem.builder().table("invoicePositionTable", "Invoice")
                            .convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (tabModProduct.data.length > 0) {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("deletePosition", "Invoice").setEnabled(false);
                    } else if (!tabModProduct.data[selectedProductRow][0].toString()
                            .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.builder().button("deletePosition", "Invoice").setEnabled(true);
                    }
                }
            }
        });

        EBISystem.builder().table("tableTotalInvoice", "Invoice").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableTotalInvoice", "Invoice").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedInvoiceRow = EBISystem.builder().table("tableTotalInvoice", "Invoice").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                try {
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("editInvoice", "Invoice").setEnabled(false);
                        EBISystem.builder().button("deleteInvoice", "Invoice").setEnabled(false);
                        EBISystem.builder().button("historyInvoice", "Invoice").setEnabled(false);
                        EBISystem.builder().button("reportInvoice", "Invoice").setEnabled(false);
                        EBISystem.builder().button("sendEmail", "Invoice").setEnabled(false);
                    } else if (!model.data[selectedInvoiceRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.builder().button("editInvoice", "Invoice").setEnabled(true);
                        EBISystem.builder().button("deleteInvoice", "Invoice").setEnabled(true);
                        EBISystem.builder().button("historyInvoice", "Invoice").setEnabled(true);
                        EBISystem.builder().button("reportInvoice", "Invoice").setEnabled(true);
                        EBISystem.builder().button("sendEmail", "Invoice").setEnabled(true);
                    }
                } catch (final ArrayIndexOutOfBoundsException ex) {
                }
            }
        });

        EBISystem.builder().table("tableTotalInvoice", "Invoice").addKeyAction(new EBIUICallback() {
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

        EBISystem.builder().table("tableTotalInvoice", "Invoice")
                .setMouseCallback(new MouseAdapter() {
                    @Override
                    public void mouseReleased(final java.awt.event.MouseEvent e) {
                        if (EBISystem.builder().table("tableTotalInvoice", "Invoice")
                                .rowAtPoint(e.getPoint()) != -1) {
                            selectedInvoiceRow = EBISystem.builder().table("tableTotalInvoice", "Invoice")
                                    .convertRowIndexToModel(EBISystem.builder()
                                            .table("tableTotalInvoice", "Invoice").rowAtPoint(e.getPoint()));
                        }
                        if (selectedInvoiceRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                                .equals(model.data[selectedInvoiceRow][0].toString())) {
                            return;
                        }
                        editInvoice();
                    }
                });

        EBISystem.builder().combo("invoiceYearText", "Invoice").addActionListener(new ActionListener() {
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
            EBISystem.builder().table("invoicePositionTable", "Invoice").setModel(tabModProduct);
        }

        EBISystem.hibernate().openHibernateSession("EBIINVOICE_SESSION");
        EBISystem.builder().combo("invoiceStatusText", "Invoice").setModel(new DefaultComboBoxModel(invoiceStatus));
        EBISystem.builder().combo("categoryText", "Invoice").setModel(new DefaultComboBoxModel(invoiceCategory));
        EBISystem.builder().combo("genderText", "Invoice").setModel(new DefaultComboBoxModel(EBICRMContactView.gendersList));

        beginChar = "";
        EBISystem.builder().vpanel("Invoice").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Invoice").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Invoice").setChangedDate("");
        EBISystem.builder().vpanel("Invoice").setChangedFrom("");

        final NumberFormat taxFormat = NumberFormat.getNumberInstance();
        taxFormat.setMinimumFractionDigits(2);
        taxFormat.setMaximumFractionDigits(3);

        EBISystem.builder().combo("invoiceYearText", "Invoice").setEditable(true);
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setValue(null);
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setEditable(false);
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.builder().FormattedField("taxText", "Invoice").setValue(null);
        EBISystem.builder().FormattedField("taxText", "Invoice").setEditable(false);
        EBISystem.builder().FormattedField("taxText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));

        EBISystem.builder().FormattedField("taxText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.builder().FormattedField("taxText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);

        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice").setValue(null);
        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice").setEditable(false);
        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);

        EBISystem.builder().FormattedField("deductionText", "Invoice").setValue(null);
        EBISystem.builder().FormattedField("deductionText", "Invoice").setEditable(false);
        EBISystem.builder().FormattedField("deductionText", "Invoice").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        EBISystem.builder().FormattedField("deductionText", "Invoice").setForeground(new Color(255, 40, 40));
        EBISystem.builder().FormattedField("deductionText", "Invoice").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.builder().button("selectOrder", "Invoice").setEnabled(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.builder().table("invoicePositionTable", "Invoice").getColumnModel().getColumn(5);
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

        EBISystem.builder().textField("invoiceNrText", "Invoice").setEditable(false);
        EBISystem.builder().combo("invoiceStatusText", "Invoice").setSelectedIndex(0);
        EBISystem.builder().combo("categoryText", "Invoice").setSelectedIndex(0);

        EBISystem.builder().textField("invoiceNrText", "Invoice").setText("");
        EBISystem.builder().textField("invoiceNameText", "Invoice").setText("");
        EBISystem.builder().textField("orderText", "Invoice").setEnabled(false);
        EBISystem.builder().textField("orderText", "Invoice").setText("");

        EBISystem.builder().combo("genderText", "Invoice").setSelectedIndex(0);
        EBISystem.builder().textField("titleText", "Invoice").setText("");
        EBISystem.builder().textField("companyNameText", "Invoice").setText("");
        EBISystem.builder().textField("nameText", "Invoice").setText("");
        EBISystem.builder().textField("surnameText", "Invoice").setText("");
        EBISystem.builder().textField("streetNrText", "Invoice").setText("");
        EBISystem.builder().textField("zipText", "Invoice").setText("");
        EBISystem.builder().textField("locationText", "Invoice").setText("");
        EBISystem.builder().textField("postCodeText", "Invoice").setText("");
        EBISystem.builder().textField("countryText", "Invoice").setText("");
        EBISystem.builder().textField("telefonText", "Invoice").setText("");
        EBISystem.builder().textField("faxText", "Invoice").setText("");
        EBISystem.builder().textField("emailText", "Invoice").setText("");
        EBISystem.builder().textField("internetText", "Invoice").setText("");

        EBISystem.builder().textArea("recDescription", "Invoice").setText("");
        EBISystem.builder().timePicker("invoiceDateText", "Invoice").getEditor().setText("");
        EBISystem.builder().timePicker("invoiceDateText", "Invoice").setDate(new Date());
        EBISystem.builder().timePicker("invoiceDateText", "Invoice").setFormats(EBISystem.DateFormat);

        EBISystem.builder().button("deletePosition", "Invoice").setEnabled(false);
    }

    public void newInvoice() {
        EBISystem.showInActionStatus("Invoice");
        dataControlInvoice.dataNew();
        dataShow(-1);
        dataControlInvoice.dataShowProduct();
        dataControlInvoice.isEdit = false;
    }

    public void dataShow(final int invoiceID) {
        dataControlInvoice.dataShow(EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem().toString(), invoiceID);
    }

    public void dataShowProduct() {
        dataControlInvoice.dataShowProduct();
    }

    public boolean saveInvoice() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Invoice");
        int row = EBISystem.builder().table("tableTotalInvoice", "Invoice").getSelectedRow();
        Integer id = dataControlInvoice.dataStore();
        dataShow(id);
        dataControlInvoice.dataShowProduct();
        EBISystem.builder().table("tableTotalInvoice", "Invoice").changeSelection(row, 0, false, false);
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
            dataShowProduct();
        }
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("invoiceNrText", "Invoice").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_INVOICE_NR")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.builder().combo("invoiceStatusText", "Invoice").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_STATUS")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }

        if (dataControlInvoice.isEdit == false) {
            final EBIAbstractTableModel tabModel = (EBIAbstractTableModel) EBISystem.builder().table("tableTotalInvoice", "Invoice").getModel();
            for (int i = 0; i < tabModel.data.length; i++) {
                if (tabModel.data[i][0].equals(EBISystem.builder().textField("invoiceNrText", "Invoice").getText())) {
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

        EBISystem.builder().loadGUI("CRMDialog/sendMailDialogGUI.xml");
        EBISystem.builder().dialog("sendEMailMessage").setTitle(EBISystem.i18n("EBI_LANG_DIALOG_SEND_EMAIL_MESSAGE"));
        EBISystem.builder().getCheckBox("ShowReportBS", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SHOW_REPORT_BEFORE_SEND"));
        EBISystem.builder().label("SubjectEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SUBJECT"));
        EBISystem.builder().label("template", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_TEMPLATE"));
        EBISystem.builder().combo("templateText", "sendEMailMessage").setModel(new DefaultComboBoxModel(EBISystem.getModule().dynMethod.getEMailTemplateNames()));

        EBISystem.builder().combo("templateText", "sendEMailMessage").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.builder().combo("templateText", "sendEMailMessage").getSelectedItem().toString())) {
                    EBISystem.builder().getEditor("MessageAreaText", "sendEMailMessage")
                            .setText(EBISystem.getModule().dynMethod.getEMailTemplate(EBISystem.builder().combo("templateText", "sendEMailMessage").getSelectedItem().toString()));
                }
            }
        });
        EBISystem.builder().button("sendEmail", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_SEND"));
        EBISystem.builder().button("closeEMailDialog", "sendEMailMessage").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.builder().button("sendEmail", "sendEMailMessage").addActionListener(new ActionListener() {

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
                            EBISystem.builder().getCheckBox("ShowReportBS", "sendEMailMessage").isSelected());
                    EBISystem.builder().dialog("sendEMailMessage").setVisible(false);
                }
            }
        });

        EBISystem.builder().button("closeEMailDialog", "sendEMailMessage").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                EBISystem.builder().dialog("sendEMailMessage").setVisible(false);
            }
        });
        EBISystem.builder().showGUI();
    }

    public boolean validateDateInput() {
        boolean ret = true;
        if (!"".equals(EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem().toString())) {
            if (EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem().toString().length() < 4) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_YEAR")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
            try {
                Integer.parseInt(EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
            } catch (final NumberFormatException ex) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MESSAGE_VALID_YEAR")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
        }
        return ret;
    }

    private boolean validateEMailInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("SubjectText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_SUBJECT")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().getEditor("MessageAreaText", "sendEMailMessage").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_BODY_TEXT")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void updateyear() {
        if (!validateDateInput()) {
            return;
        }

        if (EBISystem.builder().combo("invoiceYearText", "Invoice").getItemCount() >= 1) {
            boolean isAvailable = false;
            if (!"".equals(
                    EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem())) {
                for (int i = 0; i <= EBISystem.builder().combo("invoiceYearText", "Invoice")
                        .getItemCount(); i++) {
                    if (EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem()
                            .equals(EBISystem.builder().combo("invoiceYearText", "Invoice")
                                    .getItemAt(i))) {
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable) {
                    EBISystem.builder().combo("invoiceYearText", "Invoice")
                            .addItem(EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor()
                                    .getItem().toString());
                }
            } else {
                if (!"".equals(selectedYear)) {
                    EBISystem.builder().combo("invoiceYearText", "Invoice").removeItem(selectedYear);
                }
            }
        } else {
            if (!"".equals(
                    EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem())) {
                EBISystem.builder().combo("invoiceYearText", "Invoice").addItem(EBISystem.builder()
                        .combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
            }
        }

        // create comma separated value
        String vSave = "";
        if (EBISystem.builder().combo("invoiceYearText", "Invoice").getItemCount() > 0) {

            for (int i = 0; i < EBISystem.builder().combo("invoiceYearText", "Invoice")
                    .getItemCount(); i++) {

                if (i < EBISystem.builder().combo("invoiceYearText", "Invoice").getItemCount() - 1) {
                    vSave += EBISystem.builder().combo("invoiceYearText", "Invoice").getItemAt(i).toString() + ",";
                } else {
                    vSave += EBISystem.builder().combo("invoiceYearText", "Invoice").getItemAt(i).toString();
                }
            }
        }

        // Sort
        if (EBISystem.builder().combo("invoiceYearText", "Invoice").getItemCount() > 0) {

            final String[] avalItems = vSave.split(",");
            Arrays.sort(avalItems);
            final String selected = EBISystem.builder().combo("invoiceYearText", "Invoice").getSelectedItem().toString();
            EBISystem.builder().combo("invoiceYearText", "Invoice").removeAllItems();
            vSave = "";
            for (int i = 0; i < avalItems.length; i++) {
                EBISystem.builder().combo("invoiceYearText", "Invoice").addItem(avalItems[i]);
                if (i < avalItems.length - 1) {
                    vSave += avalItems[i] + ",";
                } else {
                    vSave += avalItems[i];
                }
            }
            EBISystem.builder().combo("invoiceYearText", "Invoice").setSelectedItem(selected);
            properties.setValue("INVOICEYEAR_TEXT", vSave);
        }

        properties.setValue("SELECTED_INVOICEYEAR_TEXT", EBISystem.builder().combo("invoiceYearText", "Invoice").getEditor().getItem().toString());
        properties.saveEBINeutrinoProperties();
        dataShow(-1);
    }

    public void selectOrder() {
        final String[] spl = EBISystem.builder().textField("orderText", "Invoice").getText().split(":");
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

        addCon.setValueToComponent(EBISystem.builder().combo("genderText", "Invoice"), "Gender");
        addCon.setValueToComponent(EBISystem.builder().textField("titleText", "Invoice"), "Position");
        addCon.setValueToComponent(EBISystem.builder().textField("surnameText", "Invoice"), "Surname");
        addCon.setValueToComponent(EBISystem.builder().textField("nameText", "Invoice"), "contact.Name");
        addCon.setValueToComponent(EBISystem.builder().textField("streetNrText", "Invoice"), "Street");
        addCon.setValueToComponent(EBISystem.builder().textField("zipText", "Invoice"), "Zip");
        addCon.setValueToComponent(EBISystem.builder().textField("locationText", "Invoice"), "Location");
        addCon.setValueToComponent(EBISystem.builder().textField("countryText", "Invoice"), "Country");
        addCon.setValueToComponent(EBISystem.builder().textField("postCodeText", "Invoice"), "PBox");
        addCon.setValueToComponent(EBISystem.builder().textField("emailText", "Invoice"), "EMail");
        addCon.setValueToComponent(EBISystem.builder().textField("faxText", "Invoice"), "Fax");
        addCon.setValueToComponent(EBISystem.builder().textField("telefonText", "Invoice"), "phone");
        addCon.setValueToComponent(EBISystem.builder().textArea("recDescription", "Invoice"),
                "contact.description");
        addCon.setValueToComponent(EBISystem.builder().textField("companyNameText", "Invoice"),
                "company.NAME");
        addCon.setValueToComponent(EBISystem.builder().textField("internetText", "Invoice"),
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
                dataControlInvoice.dataShowProduct();
            }
        }
    }

}
