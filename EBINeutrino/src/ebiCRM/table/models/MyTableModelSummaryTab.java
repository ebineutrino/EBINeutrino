package ebiCRM.table.models;

import ebiNeutrinoSDK.EBISystem;

import javax.swing.table.AbstractTableModel;

public class MyTableModelSummaryTab extends AbstractTableModel {

    public String[] columnNames = {
        EBISystem.i18n("EBI_LANG_TYPE"),
        EBISystem.i18n("EBI_LANG_C_COMPANY"),
        EBISystem.i18n("EBI_LANG_NAME"),
        EBISystem.i18n("EBI_LANG_CREATED"),
        EBISystem.i18n("EBI_LANG_CHANGED"),
        EBISystem.i18n("EBI_LANG_C_STATUS")
    };

    public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "",""}};


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
