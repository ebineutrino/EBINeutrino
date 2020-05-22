package org.modules.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class ModelAvailableEmails extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_C_EMAIL_FROM"),
        EBISystem.i18n("EBI_LANG_C_EMAIL_SUBJECT"),
        EBISystem.i18n("EBI_LANG_DATE"),
        EBISystem.i18n("EBI_LANG_ATTACHMENT"),};

    public Object[][] data = {{EBISystem.i18n("EBI_LANG_C_NO_EMAILS"), "", "", "", "", "", ""}};

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(final int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(final int row, final int col) {
        return data[row][col] == null ? data[0][0] : data[row][col];
    }
}
