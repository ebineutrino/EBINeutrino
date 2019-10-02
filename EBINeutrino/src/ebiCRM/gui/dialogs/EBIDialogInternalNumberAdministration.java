package ebiCRM.gui.dialogs;

import ebiCRM.table.models.MyTableModelInternalNumber;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBIDialogInternalNumberAdministration {

    private MyTableModelInternalNumber tabModel = null;
    private boolean isEdit = false;
    private int id = -1;
    private int selRow = -1;
    private boolean isInvoice = false;

    public EBIDialogInternalNumberAdministration(final boolean isInvoice) {

        this.isInvoice = isInvoice;
        EBISystem.gui().loadGUI("CRMDialog/autoIncNrDialog.xml");
        tabModel = new MyTableModelInternalNumber();
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
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT NAME FROM CRMINVOICECATEGORY ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);

            EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").addItem(set.getString("NAME"));
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
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT NAME FROM COMPANYCATEGORY ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").addItem(set.getString("NAME"));
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
        EBISystem.gui().dialog("autoIncNrDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_FORM_INTERNAL_NUMBER_SETTING"));
        EBISystem.gui().vpanel("autoIncNrDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_FORM_INTERNAL_NUMBER_SETTING"));
        EBISystem.gui().label("numberTo", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_C_NUMBER_TO"));
        EBISystem.gui().label("numberFrom", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_C_NUMBER_FROM"));
        EBISystem.gui().label("category", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));

        EBISystem.gui().textField("numberToText", "autoIncNrDialog").addKeyListener(new KeyAdapter() {

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

        EBISystem.gui().button("saveValue", "autoIncNrDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.gui().button("saveValue", "autoIncNrDialog").addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (isEdit == false) {
                    saveNumber();
                } else {
                    updateNumber();
                }
            }
        });

        EBISystem.gui().table("valueTable", "autoIncNrDialog").setModel(tabModel);
        EBISystem.gui().table("valueTable", "autoIncNrDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selRow = EBISystem.gui().table("valueTable", "autoIncNrDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editBnt", "autoIncNrDialog").setEnabled(false);
                    EBISystem.gui().button("deleteBnt", "autoIncNrDialog").setEnabled(false);
                } else if (!tabModel.getRow(EBISystem.gui().table("valueTable", "autoIncNrDialog").getSelectedRow())[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editBnt", "autoIncNrDialog").setEnabled(true);
                    EBISystem.gui().button("deleteBnt", "autoIncNrDialog").setEnabled(true);
                }
            }
        });
        EBISystem.gui().table("valueTable", "autoIncNrDialog").addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (EBISystem.gui().table("valueTable", "autoIncNrDialog").rowAtPoint(e.getPoint()) != -1) {
                    selRow = EBISystem.gui().table("valueTable", "autoIncNrDialog").convertRowIndexToModel(EBISystem.gui().table("valueTable", "autoIncNrDialog").rowAtPoint(e.getPoint()));
                }
                if (e.getClickCount() == 2 && selRow != -1) {

                    if (!tabModel.data[selRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        editNumber(selRow);
                    }
                }
            }
        });

        EBISystem.gui().button("newBnt", "autoIncNrDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("new.png")));
        EBISystem.gui().button("newBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newNumber();
            }
        });

        EBISystem.gui().button("editBnt", "autoIncNrDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("down.png")));
        EBISystem.gui().button("editBnt", "autoIncNrDialog").setEnabled(false);
        EBISystem.gui().button("editBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                editNumber(selRow);
            }
        });

        EBISystem.gui().button("deleteBnt", "autoIncNrDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("delete.png")));
        EBISystem.gui().button("deleteBnt", "autoIncNrDialog").setEnabled(false);
        EBISystem.gui().button("deleteBnt", "autoIncNrDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                deleteNumber(selRow);
            }
        });

        EBISystem.gui().showGUI();
    }

    private void newNumber() {
        EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").setSelectedIndex(0);
        EBISystem.gui().textField("numberFromText", "autoIncNrDialog").setText("");
        EBISystem.gui().textField("numberToText", "autoIncNrDialog").setText("");
        EBISystem.gui().textField("beginCharText", "autoIncNrDialog").setText("");
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

            final int cid = retriveIDFromCategory(EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").getSelectedItem().toString());
            final String sql = "INSERT INTO " + table + " (CATEGORY,NUMBERFROM,NUMBERTO,BEGINCHAR,CATEGORYID) values(?,?,?,?,?) ";
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
            ps.setString(1, EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").getSelectedItem().toString());
            ps.setInt(2, Integer.parseInt(EBISystem.gui().textField("numberFromText", "autoIncNrDialog").getText()));
            ps.setInt(3, Integer.parseInt(EBISystem.gui().textField("numberToText", "autoIncNrDialog").getText()));
            ps.setString(4, EBISystem.gui().textField("beginCharText", "autoIncNrDialog").getText());
            ps.setInt(5, cid);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        }
        newNumber();
    }

    private int retriveIDFromCategory(final String category) {
        int toRet = -1;
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
                toRet = set.getInt("ID");
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
            final int cid = retriveIDFromCategory(EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").getSelectedItem().toString());
            ps.setString(1, EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").getSelectedItem().toString());
            ps.setInt(2, Integer.parseInt(EBISystem.gui().textField("numberFromText", "autoIncNrDialog").getText()));
            ps.setInt(3, Integer.parseInt(EBISystem.gui().textField("numberToText", "autoIncNrDialog").getText()));
            ps.setInt(4, cid);
            ps.setString(5, EBISystem.gui().textField("beginCharText", "autoIncNrDialog").getText());
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
        EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").setSelectedItem(tabModel.data[row][1].toString());
        EBISystem.gui().textField("numberFromText", "autoIncNrDialog").setText(tabModel.data[row][2].toString());
        EBISystem.gui().textField("numberToText", "autoIncNrDialog").setText(tabModel.data[row][3].toString());
        EBISystem.gui().textField("beginCharText", "autoIncNrDialog").setText(tabModel.data[row][4].toString());

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
        if (EBISystem.gui().combo("categoryCombo", "autoIncNrDialog").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_CATEGORY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.gui().textField("numberFromText", "autoIncNrDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NUMBER_FROM_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(EBISystem.gui().textField("numberFromText", "autoIncNrDialog").getText());
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NUMBER_FROM_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(EBISystem.gui().textField("numberToText", "autoIncNrDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERORR_NUMBER_TO_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(EBISystem.gui().textField("numberToText", "autoIncNrDialog").getText());
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERORR_NUMBER_TO_IS_NOT_VALID")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
