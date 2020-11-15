package org.sdk.gui.dialogs;

import org.sdk.EBISystem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Set a value for the specified combobox
 *
 */
public class EBIDialogValueSetter {

    private MyTableModelValueSetter tabMod = null;
    private String Tab = "";
    private int id = -1;
    private boolean isSaveOrUpdate = false;
    private int selRow = -1;

    public EBIDialogValueSetter(final String tab) {

        tabMod = new MyTableModelValueSetter();
        EBISystem.gui().loadGUI("CRMDialog/valueSetDialog.xml");
        Tab = tab.toUpperCase();
        initialize();
        load();
        EBISystem.gui().button("deleteBnt", "valueSetterDialog").setEnabled(false);
        EBISystem.gui().button("editBnt", "valueSetterDialog").setEnabled(false);
        EBISystem.gui().button("deleteBnt", "valueSetterDialog").requestFocus();
    }

    private void initialize() {

        final ListSelectionModel rowSM = EBISystem.gui().table("valueTable", "valueSetterDialog")
                .getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                // Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("deleteBnt", "valueSetterDialog").setEnabled(false);
                    EBISystem.gui().button("editBnt", "valueSetterDialog").setEnabled(false);
                } else {
                    selRow = lsm.getMinSelectionIndex();
                    if (selRow <= tabMod.data.length && selRow != -1) {
                        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui()
                                .table("valueTable", "valueSetterDialog").getValueAt(selRow, 0).toString())) {
                            EBISystem.gui().button("deleteBnt", "valueSetterDialog").setEnabled(true);
                            EBISystem.gui().button("editBnt", "valueSetterDialog").setEnabled(true);
                            isSaveOrUpdate = true;
                            loadEdit();
                            EBISystem.gui().textField("nameValue", "valueSetterDialog").requestFocus();
                        }
                    }
                }
            }
        });
    }

    public void setVisible() {
        EBISystem.gui().dialog("valueSetterDialog")
                .setTitle(EBISystem.i18n("EBI_LANG_SETTINGS"));
        EBISystem.gui().vpanel("valueSetterDialog")
                .setModuleTitle(EBISystem.i18n("EBI_LANG_SETTINGS"));

        EBISystem.gui().label("name", "valueSetterDialog").setText(EBISystem.i18n("EBI_LANG_NAME"));

        EBISystem.gui().button("saveValue", "valueSetterDialog")
                .setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.gui().button("saveValue", "valueSetterDialog")
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        if (isSaveOrUpdate == false) {
                            save();
                        } else {
                            update();
                        }

                    }
                });

        EBISystem.gui().button("closeDialog", "valueSetterDialog")
                .setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.gui().button("closeDialog", "valueSetterDialog")
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        EBISystem.gui().dialog("valueSetterDialog").setVisible(false);
                    }
                });

        EBISystem.gui().button("newBnt", "valueSetterDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.gui().button("newBnt", "valueSetterDialog")
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        newRecord();
                    }
                });

        EBISystem.gui().button("editBnt", "valueSetterDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.gui().button("editBnt", "valueSetterDialog")
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui()
                                .table("valueTable", "valueSetterDialog").getValueAt(selRow, 0).toString())) {
                            loadEdit();
                        }
                    }
                });

        EBISystem.gui().button("deleteBnt", "valueSetterDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.gui().button("deleteBnt", "valueSetterDialog")
                .addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(final java.awt.event.ActionEvent e) {
                        delete();
                        newRecord();
                    }
                });

        EBISystem.gui().table("valueTable", "valueSetterDialog").setModel(tabMod);

        EBISystem.gui().textField("nameValue", "valueSetterDialog")
                .addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyPressed(final KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (isSaveOrUpdate == false) {
                                save();
                            } else {
                                update();
                            }

                            load();
                            newRecord();
                            EBISystem.gui().textField("nameValue", "valueSetterDialog").requestFocus();
                        }
                    }
                });

        EBISystem.gui().showGUI();
    }

    private boolean validateInput() {
        if ("".equals(EBISystem.gui().textField("nameValue", "valueSetterDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME"))
                    .Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if (!isSaveOrUpdate) {
            for (int i = 0; i < tabMod.data.length; i++) {
                if (tabMod.data[i][1].toString().toLowerCase().equals(EBISystem.gui()
                        .textField("nameValue", "valueSetterDialog").getText().toLowerCase())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SAME_RECORD_EXSIST"))
                            .Show(EBIMessage.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        return true;
    }

    private void save() {
        if (!validateInput()) {
            return;
        }
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("INSERT INTO " + Tab + "  (NAME) VALUES(?)");
            ps1.setString(1, EBISystem.gui().textField("nameValue", "valueSetterDialog").getText());
            EBISystem.getInstance().iDB().executePreparedStmt(ps1);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            load();
            newRecord();
        }
    }

    private void update() {
        if (!validateInput()) {
            return;
        }
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("UPDATE " + Tab + "  SET  NAME=? where id=?");
            ps1.setString(1, EBISystem.gui().textField("nameValue", "valueSetterDialog").getText());
            ps1.setInt(2, id);
            EBISystem.getInstance().iDB().executePreparedStmt(ps1);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            load();
            newRecord();
        }
    }

    private void delete() {
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB()
                    .initPreparedStatement("DELETE FROM " + Tab + " WHERE id=?");
            ps1.setInt(1, id);
            if (!EBISystem.getInstance().iDB().executePreparedStmt(ps1)) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_DELETE_RECORD"))
                        .Show(EBIMessage.ERROR_MESSAGE);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB()
                    .initPreparedStatement("SELECT * FROM " + this.Tab);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            set.last();
            if (set.getRow() > 0) {
                tabMod.data = new Object[set.getRow()][2];
                set.beforeFirst();
                int i = 0;
                while (set.next()) {
                    tabMod.data[i][0] = set.getString("ID");
                    tabMod.data[i][1] = set.getString("NAME");
                    i++;
                }
            } else {
                tabMod.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
            }
            tabMod.fireTableDataChanged();

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
            return;
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

    private void loadEdit() {
        ResultSet set = null;
        try {

            final PreparedStatement ps1 = EBISystem.getInstance().iDB()
                    .initPreparedStatement("SELECT * FROM " + this.Tab + " WHERE ID=?");
            ps1.setString(1, tabMod.data[EBISystem.gui().table("valueTable", "valueSetterDialog")
                    .convertRowIndexToModel(selRow)][0].toString());
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                set.next();
                this.id = set.getInt("ID");
                EBISystem.gui().textField("nameValue", "valueSetterDialog")
                        .setText(set.getString("NAME"));
            }
            set.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
            return;
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

    private void newRecord() {
        isSaveOrUpdate = false;
        EBISystem.gui().textField("nameValue", "valueSetterDialog").setText("");
        this.id = 0;
        load();
    }
}
