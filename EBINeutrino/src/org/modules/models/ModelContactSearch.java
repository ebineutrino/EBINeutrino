package org.modules.models;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class ModelContactSearch extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_COMPANY_NAME"),
        EBISystem.i18n("EBI_LANG_CONTACT_POSITION"),
        EBISystem.i18n("EBI_LANG_C_GENDER"),
        EBISystem.i18n("EBI_LANG_SURNAME"),
        EBISystem.i18n("EBI_LANG_C_CNAME"),
        EBISystem.i18n("EBI_LANG_C_ZIP"),
        EBISystem.i18n("EBI_LANG_C_LOCATION"),
        EBISystem.i18n("EBI_LANG_EMAIL")
    };
    
    public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),"", "", "", "", "", "", ""}};

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

}
