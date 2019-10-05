package org.modules.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class ModelCRMContact extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_CONTACT_POSITION"),
        EBISystem.i18n("EBI_LANG_C_GENDER"),
        EBISystem.i18n("EBI_LANG_SURNAME"),
        EBISystem.i18n("EBI_LANG_C_CNAME"),
        EBISystem.i18n("EBI_LANG_C_TELEPHONE"),
        EBISystem.i18n("EBI_LANG_C_MOBILE_PHONE"),
        EBISystem.i18n("EBI_LANG_EMAIL"),
        EBISystem.i18n("EBI_LANG_DESCRIPTION")
    };
    
    public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "","","","",""}};

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
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
