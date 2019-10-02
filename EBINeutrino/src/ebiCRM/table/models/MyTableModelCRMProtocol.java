package ebiCRM.table.models;

import ebiNeutrinoSDK.EBISystem;

import javax.swing.table.AbstractTableModel;

public class MyTableModelCRMProtocol extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_DATE"),
        EBISystem.i18n("EBI_LANG_C_EMAIL_SUBJECT"),
        EBISystem.i18n("EBI_LANG_C_MEETING_TYPE"),
        EBISystem.i18n("EBI_LANG_C_MEMO_MEMO")
    };

    public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", ""}};

    public Object[] getRow(final int row) {
        if (row < 0 || row > data.length - 1) {
            return null;
        }

        return data[row];
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
        return data[row][col] == null ? "" :  data[row][col] ;
    }
    
    @Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
    	return false;
    }
}