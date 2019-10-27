package org.core.table.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class MyTableModelReportSetting extends AbstractTableModel {

    public String[] columnNames = {
            EBISystem.i18n("EBI_LANG_ACTIVE"),
            EBISystem.i18n("EBI_LANG_NAME"),
            EBISystem.i18n("EBI_LANG_CATEGORY"),
            EBISystem.i18n("EBI_LANG_DATE"),
            EBISystem.i18n("EBI_LANG_SHOW_AS_PDF"),
            EBISystem.i18n("EBI_LANG_SHOW_NORMAL")
    };

    public Object[][] data = {{Boolean.valueOf(false), EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};

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

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
	public Class getColumnClass(final int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
	public boolean isCellEditable(final int row, final int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        return false;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
	public void setValueAt(final Object value, final int row, final int col) {

        if (data[0][col] instanceof Integer
                && !(value instanceof Integer)) {
            try {
                data[row][col] = Integer.valueOf(value.toString());
                fireTableCellUpdated(row, col);
            } catch (final NumberFormatException e) {

            }
        } else {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

    }
}
