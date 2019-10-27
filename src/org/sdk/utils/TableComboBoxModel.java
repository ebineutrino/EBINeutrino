package org.sdk.utils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This model is combines a {@link javax.swing.table.TableModel TableModel},
 * a {@link javax.swing.ListSelectionModel ListSelectionModel}
 * and a {@link javax.swing.ComboBoxModel ComboBoxModel} into a  model.
 * When shared by a {@link javax.swing.JComboBox JComboBox}
 * and a {@link javax.swing.JTable JTable} content and selection of the two will be
 * .
 * </p>
 * <p>
 * As a basis, the user needs to provide a TableModel, a ListSelectionModel and
 * the index of the column that should be used for the combobox. TableComboBoxModel
 * then merely passes nearly all messages on to the TableModel and on to the ListSelectionModel.
 * </p>
 * @author Thomas Bierhance
 */
public class TableComboBoxModel implements TableModel, ComboBoxModel {
    TableModel tableModel;
    int comboBoxColumnIndex;
    ListSelectionModel listSelectionModel;
    
    public TableComboBoxModel(final TableModel tableModel, final int comboBoxColumnIndex, final ListSelectionModel listSelectionModel) {
        this.tableModel = tableModel;
        this.comboBoxColumnIndex = comboBoxColumnIndex;
        this.listSelectionModel = listSelectionModel;
        // synchronize the combobox when a new tablerow is selected
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
			public void valueChanged(final ListSelectionEvent event) {
                selectedValueChanged(event.getSource());
            }
        });
    }
    
    private void selectedValueChanged(final Object source) {
        fireItemStateChanged(new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, -1, -1));
    }
    
    // TableModel-------------------------------------
    @Override
	public int getColumnCount() {
        return tableModel.getColumnCount();
    }
    
    @Override
	public int getRowCount() {
        return tableModel.getRowCount();
    }
    
    @Override
	public String getColumnName(final int columnIndex) {
        return tableModel.getColumnName(columnIndex);
    }
    
    @Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
        return tableModel.getValueAt(rowIndex, columnIndex);
    }

    @Override
	public void addTableModelListener(final javax.swing.event.TableModelListener tableModelListener) {
        tableModel.addTableModelListener(tableModelListener);
    }

    @Override
	public Class getColumnClass(final int columnIndex) {
        return tableModel.getColumnClass(columnIndex);
    }

    @Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return tableModel.isCellEditable(rowIndex, columnIndex);
    }

    @Override
	public void removeTableModelListener(final javax.swing.event.TableModelListener tableModelListener) {
        tableModel.removeTableModelListener(tableModelListener);
    }

    @Override
	public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        tableModel.setValueAt(value, rowIndex, columnIndex);
    }

    // ComboBoxModel-------------------------------------
    @Override
	public Object getElementAt(final int index) {
        return getValueAt(index, comboBoxColumnIndex);
    }

    @Override
	public Object getSelectedItem() {
    	int row = 0;
    	int col = 0;
    	if(listSelectionModel.getLeadSelectionIndex() != -1){ // Problem mit JDK 1.5.update 06 liefert -1
    	 row = listSelectionModel.getLeadSelectionIndex();
    	 col = comboBoxColumnIndex;
    	}
    	return getValueAt(row, col);
    }

    @Override
	public int getSize() {
        return getRowCount();
    }

     List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();
    //List listDataListeners = new ArrayList(); // 1.4
    
    private void fireItemStateChanged(final ListDataEvent event) {
        for (final ListDataListener listDataListener : listDataListeners) {
        //for(java.util.Iterator i=listDataListeners.iterator();i.hasNext();) { // 1.4
          //  ListDataListener listDataListener=(ListDataListener)i.next(); // 1.4
            listDataListener.contentsChanged(event);
        }
    }

    @Override
	public void removeListDataListener(final ListDataListener listDataListener) {
        listDataListeners.remove(listDataListener);
    }
    
    @Override
	public void addListDataListener(final ListDataListener listDataListener) {
        listDataListeners.add(listDataListener);
    }
    
    @Override
	public void setSelectedItem(final Object item) {
        // find the row that is corresponding to the selected item and synchronize
    	listSelectionModel.clearSelection();
    	if(!item.equals("")){
	    	for(int rowIndex=0, rowCount=getRowCount(); rowIndex<rowCount; rowIndex++) {	
	           if (getValueAt(rowIndex, comboBoxColumnIndex) == item) {
	                listSelectionModel.setSelectionInterval(rowIndex, rowIndex);
	                selectedValueChanged(this);
	                break;
	            }
	          }
    	}else{
    		listSelectionModel.clearSelection();
    	}
    }
}