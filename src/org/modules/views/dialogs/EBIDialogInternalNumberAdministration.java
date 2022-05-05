package org.modules.views.dialogs;

import org.modules.models.ModelInternalNumber;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBIDialogInternalNumberAdministration {

    private ModelInternalNumber tabModel = null;
    private boolean isEdit = false;
    private int id = -1;
    private int selRow = -1;
    private boolean isInvoice = false;

    public EBIDialogInternalNumberAdministration(final boolean isInvoice) {
        this.isInvoice = isInvoice;
        EBISystem.builder().loadGUI("CRMDialog/autoIncNrDialog.xml");
        tabModel = new ModelInternalNumber();
        if (isInvoice) {
            fillComboInvoiceCategory();
            showInvoiceNumber();
        } else {
            fillComboCategory();
            showNumber();
        }
    }

    private void fillComboInvoiceCategory() {
        ResultSet set = null;
        try {
            EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").removeAllItems();
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT NAME FROM CRMINVOICECATEGORY ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").addItem(set.getString("NAME"));
                }
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillComboCategory() {
        ResultSet set = null;
        try {
            EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").removeAllItems();
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT NAME FROM COMPANYCATEGORY ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").addItem(set.getString("NAME"));
                }
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    public void setVisible() {
        EBISystem.builder().dialog("autoIncNrDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_FORM_INTERNAL_NUMBER_SETTING"));
        EBISystem.builder().vpanel("autoIncNrDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_FORM_INTERNAL_NUMBER_SETTING"));
        EBISystem.builder().label("numberTo", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_C_NUMBER_TO"));
        EBISystem.builder().label("numberFrom", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_C_NUMBER_FROM"));
        EBISystem.builder().label("category", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));

        EBISystem.builder().textField("numberToText", "autoIncNrDialog").addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (isEdit == false) {
                        saveNumber();
                    } else {
                        updateNumber();
                    }
                }
            }
        });

        EBISystem.builder().button("saveValue", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.builder().button("saveValue", "autoIncNrDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (isEdit == false) {
                    saveNumber();
                } else {
                    updateNumber();
                }
            }
        });

        EBISystem.builder().table("valueTable", "autoIncNrDialog").setModel(tabModel);
        EBISystem.builder().table("valueTable", "autoIncNrDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getLeadSelectionIndex() != -1) {
                    selRow = EBISystem.builder().table("valueTable", "autoIncNrDialog").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editBnt", "autoIncNrDialog").setEnabled(false);
                    EBISystem.builder().button("deleteBnt", "autoIncNrDialog").setEnabled(false);
                } else if (!tabModel.getRow(EBISystem.builder().table("valueTable", "autoIncNrDialog").getSelectedRow())[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editBnt", "autoIncNrDialog").setEnabled(true);
                    EBISystem.builder().button("deleteBnt", "autoIncNrDialog").setEnabled(true);
                }
            }
        });
        EBISystem.builder().table("valueTable", "autoIncNrDialog").addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (EBISystem.builder().table("valueTable", "autoIncNrDialog").rowAtPoint(e.getPoint()) != -1) {
                    selRow = EBISystem.builder().table("valueTable", "autoIncNrDialog").convertRowIndexToModel(EBISystem.builder().table("valueTable", "autoIncNrDialog").rowAtPoint(e.getPoint()));
                }
                if (e.getClickCount() == 2 && selRow != -1) {

                    if (!tabModel.data[selRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        editNumber(selRow);
                    }
                }
            }
        });

        EBISystem.builder().button("newBnt", "autoIncNrDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.builder().button("newBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newNumber();
            }
        });

        EBISystem.builder().button("editBnt", "autoIncNrDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.builder().button("editBnt", "autoIncNrDialog").setEnabled(false);
        EBISystem.builder().button("editBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                editNumber(selRow);
            }
        });

        EBISystem.builder().button("deleteBnt", "autoIncNrDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.builder().button("deleteBnt", "autoIncNrDialog").setEnabled(false);
        EBISystem.builder().button("deleteBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                deleteNumber(selRow);
            }
        });

        EBISystem.builder().showGUI();
    }

    private void newNumber() {
        EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").setSelectedIndex(0);
        EBISystem.builder().textField("numberFromText", "autoIncNrDialog").setText("");
        EBISystem.builder().textField("numberToText", "autoIncNrDialog").setText("");
        EBISystem.builder().textField("beginCharText", "autoIncNrDialog").setText("");
        this.id = 0;
        isEdit = false;
        if (isInvoice) {
            showInvoiceNumber();
        } else {
            showNumber();
        }
    }

    private void saveNumber() {
        if (!validateInput()) {
            return;
        }
        try {
            String table;
            if (isInvoice) {
                table = "CRMINVOICENUMBER";
            } else {
                table = "COMPANYNUMBER";
            }

            String cat = EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").getEditor().getItem().toString();
            final Long cid = retriveIDFromCategory(cat);

            final String sql = "INSERT INTO " + table + " (CATEGORY,NUMBERFROM,NUMBERTO,BEGINCHAR,CATEGORYID) values(?,?,?,?,?) ";

            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
            ps.setString(1, cat);
            ps.setInt(2, Integer.parseInt(EBISystem.builder().textField("numberFromText", "autoIncNrDialog").getText()));
            ps.setInt(3, Integer.parseInt(EBISystem.builder().textField("numberToText", "autoIncNrDialog").getText()));
            ps.setString(4, EBISystem.builder().textField("beginCharText", "autoIncNrDialog").getText());
            ps.setLong(5, cid);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        }
        newNumber();
    }

    private Long retriveIDFromCategory(final String category) {
        Long toRet = -1L;
        ResultSet set = null;
        try {
            String table;
            if (isInvoice) {
                table = "CRMINVOICECATEGORY";
            } else {
                table = "COMPANYCATEGORY";
            }
            final String sql = "SELECT * FROM " + table + " WHERE NAME=?";
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
            ps.setString(1, category);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                set.next();
                toRet = set.getLong("ID");
            } else {
                //insert a category if not available
                final String isql = "INSERT INTO " + table + " (NAME) values(?) ";
                final PreparedStatement ips = EBISystem.getInstance().iDB().initPSGenerateKEY(isql);
                ips.setString(1, category);
                toRet = EBISystem.getInstance().iDB().executePreparedStmtGetKey(ips);
                if(isInvoice){
                    fillComboInvoiceCategory();
                }else{
                    fillComboCategory();
                }
            }

        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }

        return toRet;
    }

    private void updateNumber() {
        if (!validateInput()) {
            return;
        }
        try {

            String table;
            if (isInvoice) {
                table = "CRMINVOICENUMBER";
            } else {
                table = "COMPANYNUMBER";
            }
            final String sql = "UPDATE " + table + " SET CATEGORY=?,NUMBERFROM=?,NUMBERTO=?, CATEGORYID=?, BEGINCHAR=? where ID=?";
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
            final Long cid = retriveIDFromCategory(EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").getEditor().getItem().toString());
            ps.setString(1, EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").getEditor().getItem().toString());
            ps.setInt(2, Integer.parseInt(EBISystem.builder().textField("numberFromText", "autoIncNrDialog").getText()));
            ps.setInt(3, Integer.parseInt(EBISystem.builder().textField("numberToText", "autoIncNrDialog").getText()));
            ps.setLong(4, cid);
            ps.setString(5, EBISystem.builder().textField("beginCharText", "autoIncNrDialog").getText());
            ps.setInt(6, id);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);

        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        }
        newNumber();
    }

    private void editNumber(final int row) {
        if (row < 0) {
            return;
        }
        if (tabModel.data[row][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            return;
        }

        id = Integer.parseInt(tabModel.data[row][0].toString());
        EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").setSelectedItem(tabModel.data[row][1].toString());
        EBISystem.builder().textField("numberFromText", "autoIncNrDialog").setText(tabModel.data[row][2].toString());
        EBISystem.builder().textField("numberToText", "autoIncNrDialog").setText(tabModel.data[row][3].toString());
        EBISystem.builder().textField("beginCharText", "autoIncNrDialog").setText(tabModel.data[row][4].toString());

        isEdit = true;

    }

    private void deleteNumber(final int row) {
        if (row < 0) {
            return;
        }
        if (tabModel.data[row][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            return;
        }

        try {
            String table;
            if (isInvoice) {
                table = "CRMINVOICENUMBER";
            } else {
                table = "COMPANYNUMBER";
            }
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("DELETE FROM " + table + " where ID=? ");
            ps.setString(1, tabModel.data[row][0].toString());

            if (!EBISystem.getInstance().iDB().executePreparedStmt(ps)) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_DELETE_RECORD")).Show(EBIMessage.ERROR_MESSAGE);
            }

            newNumber();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showNumber() {
        final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM COMPANYNUMBER ");
        final ResultSet set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
        try {
            set.last();
            if (set.getRow() > 0) {

                tabModel.data = new Object[set.getRow()][5];
                set.beforeFirst();
                int i = 0;
                while (set.next()) {
                    tabModel.data[i][0] = set.getString("ID");
                    tabModel.data[i][1] = set.getString("CATEGORY");
                    tabModel.data[i][2] = set.getString("NUMBERFROM");
                    tabModel.data[i][3] = set.getString("NUMBERTO");
                    tabModel.data[i][4] = set.getString("BEGINCHAR") == null ? "" : set.getString("BEGINCHAR");
                    i++;
                }
            } else {
                tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", ""}};
            }
            tabModel.fireTableDataChanged();
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showInvoiceNumber() {
        final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM CRMINVOICENUMBER ");
        final ResultSet set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
        try {
            set.last();
            if (set.getRow() > 0) {

                tabModel.data = new Object[set.getRow()][5];
                set.beforeFirst();
                int i = 0;
                while (set.next()) {
                    tabModel.data[i][0] = set.getString("ID");
                    tabModel.data[i][1] = set.getString("CATEGORY");
                    tabModel.data[i][2] = set.getString("NUMBERFROM");
                    tabModel.data[i][3] = set.getString("NUMBERTO");
                    tabModel.data[i][4] = set.getString("BEGINCHAR") == null ? "" : set.getString("BEGINCHAR");
                    i++;
                }
            } else {
                tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", ""}};
            }
            tabModel.fireTableDataChanged();
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean validateInput() {
        if (EBISystem.builder().combo("categoryCombo", "autoIncNrDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_CATEGORY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.builder().textField("numberFromText", "autoIncNrDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NUMBER_FROM_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(EBISystem.builder().textField("numberFromText", "autoIncNrDialog").getText());
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NUMBER_FROM_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.builder().textField("numberToText", "autoIncNrDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERORR_NUMBER_TO_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(EBISystem.builder().textField("numberToText", "autoIncNrDialog").getText());
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERORR_NUMBER_TO_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
