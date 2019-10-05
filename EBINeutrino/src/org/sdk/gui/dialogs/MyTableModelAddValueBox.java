package org.sdk.gui.dialogs;

import org.sdk.EBISystem;

import javax.swing.table.AbstractTableModel;

public class MyTableModelAddValueBox extends AbstractTableModel {

		public String[] columnNames = {EBISystem.i18n("EBI_LANG_VALUE")};
		public Object[][] data = {{""}};	

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
	        if (col <= 1) { 
	            return true;
	        } else {
	            return false;
	        }
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
	                data[row][col] = new Integer(value.toString());
	                fireTableCellUpdated(row, col);
	            } catch (final NumberFormatException e) {
	          
	            }
	        } else {
	            data[row][col] = value;
	            fireTableCellUpdated(row, col);
	        }

	    }

	}