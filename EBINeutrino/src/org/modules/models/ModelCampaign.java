package org.modules.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class ModelCampaign extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_NAME"),
        EBISystem.i18n("EBI_LANG_STATUS"),
        EBISystem.i18n("EBI_LANG_VALID_FROM"),
        EBISystem.i18n("EBI_LANG_VALID_TO"),};

    public Object[][] data = null;

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
        return data == null ? 0 : data.length;
    }

    @Override
    public String getColumnName(final int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(final int row, final int col) {
        return data == null ? "" : data[row][col];
    }

    @Override
    public Class getColumnClass(final int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return false;
    }
}
