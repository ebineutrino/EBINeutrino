package ebiNeutrinoSDK.utils;


import ebiNeutrinoSDK.EBISystem;

import javax.swing.table.AbstractTableModel;


public class EBIAbstractTableModel extends AbstractTableModel {

		public String[] columnNames = {
				EBISystem.i18n("EBI_LANG_EMPTY")
	    };

		public Object[][] data = {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),""}};

		public Object[] getRow(final int row){
			if(row < 0 || row > data.length - 1) {
				return null;
			}

			return data[row];
		}

		public void setRow(final int row, final Object[] rowData){
			if(row < 0 || row > data.length - 1) {
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
