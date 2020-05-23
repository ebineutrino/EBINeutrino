package org.modules.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class ModelReceiver extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_C_SEND_TYPE"),
        EBISystem.i18n("EBI_LANG_C_GENDER"),
        EBISystem.i18n("EBI_LANG_SURNAME"),
        EBISystem.i18n("EBI_LANG_C_CNAME"),
        EBISystem.i18n("EBI_LANG_CONTACT_POSITION"),
        EBISystem.i18n("EBI_LANG_C_STREET_NR"),
        EBISystem.i18n("EBI_LANG_C_ZIP"),
        EBISystem.i18n("EBI_LANG_C_LOCATION"),
        EBISystem.i18n("EBI_LANG_C_POST_CODE"),
        EBISystem.i18n("EBI_LANG_C_COUNTRY"),
        EBISystem.i18n("EBI_LANG_EMAIL")
    };

    public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", ""}};

    public ModelReceiver() {
    }

    public Object[] getRow(final int row) {
        if (row < 0 || row > data.length - 1) {
            return null;
        }

        return data[row];
    }

    public void setRow(final int row, final Object[] rowData) {
        if (row < 0 || row > data.length - 1) {
            return;
        }

        data[row] = rowData;
    }

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
        return data[row][col];
    }
}
