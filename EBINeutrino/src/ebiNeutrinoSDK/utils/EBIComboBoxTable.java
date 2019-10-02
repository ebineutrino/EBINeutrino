package ebiNeutrinoSDK.utils;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * An extension to JTable with three additional features.
 * <ul>
 * <li>Can highlight cells/rows/columns on mouse-over.</li>
 * <li>Method to show/hide the columns' headers.</li>
 * <li>Method to resize the columns to fit their content.</li>
 * </ul>
 * @author Thomas Bierhance
 */
public class EBIComboBoxTable extends JTable {
    
    /** handler that keeps track of the cell that the mouse is over */
    MouseOverHandler mouseOverHandler = new MouseOverHandler();
    
    /** true if the mouse-over effect should be painted */
    private boolean mouseOverActive;
    
    /**
     * Creates a new ComboBoxTable.
     */
    public EBIComboBoxTable() {
        super();
        setMouseOverActive(true);
        setSize(200,200);
    }
    
    /**
     * Sets whether the table draws headers for its columns.
     * If <code>showHeaders</code> is true it does; if it is false it doesn't.
     * @param showHeaders true if table view should draw headers
     */
    public void setShowHeaders(final boolean showHeaders) {
        if (!showHeaders) {
            // set preferred size to (0,0) to hide the header
            getTableHeader().setPreferredSize(new Dimension(0, 0));
        } else {
            // to show the header the preferred size is recalculated from the actual header data/renderers
            getTableHeader().setPreferredSize(getPreferredTableHeaderSize());
        }
    }
    
    // --------------------- mouse-over effect ---------------------

    /**
     * Activates/Deactivates the mouse-over effect.
     * @param mouseOverActive true to activate the effect; false to deactivate
     */
    public void setMouseOverActive(final boolean mouseOverActive) {
        this.mouseOverActive = mouseOverActive;
        if (mouseOverActive) {
            // register the handler to track the mouse
            addMouseMotionListener(mouseOverHandler);
            addMouseListener(mouseOverHandler);
        } else {
            // remove the handler
            removeMouseMotionListener(mouseOverHandler);
            removeMouseListener(mouseOverHandler);
        }
    }
    
    /**
     * Returns if the mouse-over effect is active.
     * @return true if the effect is active; false otherwise
     */
    public boolean isMouseOverActive() {
        return mouseOverActive;
    }
    
    /**
     * This method has been overridden to add the mouse-over effect.
     * @see javax.swing.JTable#prepareRenderer(TableCellRenderer, int, int)
     */
    @Override
	public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
        // use the super implementation to get a renderer
        final Component component = super.prepareRenderer(renderer, row, column);
        
        // Apply the mouse-over effect:
        // The effect is applied according to the selection mode:
        //   individual cells can be selected -> highlight individual cells
        //   only rows can be selected -> highlight complete rows
        //   only columns can be selected -> highlight complete columns
        // Selected cells are handled by the super implementation and are not touched.
        // Cells that are not selected need to be set to the default colors
        // as they would otherwise show the mouse-over colors.
        
        if (isMouseOverActive()) {
            // individual cell selection
            if (getRowSelectionAllowed() && getColumnSelectionAllowed()) {
                // is cell selected?
                if (!(isSelectedRow(row) && isSelectedColumn(column))) {
                    // is mouse over cell?
                    if (row == mouseOverHandler.row && column == mouseOverHandler.column) {
                        component.setBackground(getMouseOverBackground());
                        component.setForeground(getSelectionForeground());
                    } else {
                        component.setBackground(getBackground());
                        component.setForeground(getForeground());
                    }
                }
                // row selection only
            } else if (getRowSelectionAllowed() && !getColumnSelectionAllowed()) {
                // is row selected?
                if (!isSelectedRow(row)) {
                    // is mouse over row?
                    if (row == mouseOverHandler.row) {
                        component.setBackground(getMouseOverBackground());
                        component.setForeground(getSelectionForeground());
                    } else {
                        component.setBackground(getBackground());
                        component.setForeground(getForeground());
                    }
                }
                // column selection only
            } else if (getColumnSelectionAllowed() && !getRowSelectionAllowed()) {
                // is column selected?
                if (!isSelectedColumn(column)) {
                    // is mouse over column?
                    if (column == mouseOverHandler.column) {
                        component.setBackground(getMouseOverBackground());
                        component.setForeground(getSelectionForeground());
                    } else {
                        component.setBackground(getBackground());
                        component.setForeground(getForeground());
                    }
                }
            }
        }
        
        return component;
    }
    
    /**
     * Returns the color that is used to paint the background of a cell for
     * the mouse-over effect. The color is a 1:1 mixture between the table's
     * normal background and the selection background.
     * @return the background color for the mouse-over effect
     */
    protected Color getMouseOverBackground() {
        // get the two colors that form the basis: selection & normal background
        final Color selectionBackground = getSelectionBackground();
        final Color normalBackground = getBackground();
        // create a 1:1 mixture between the two
        final int red = (selectionBackground.getRed()+normalBackground.getRed())/2;
        final int green = (selectionBackground.getGreen()+normalBackground.getGreen())/2;
        final int blue = (selectionBackground.getBlue()+normalBackground.getBlue())/2;
        
        return new Color(red, green, blue);
    }
    
    /**
     * Returns if a row is currently selected.
     * @param row the row that should be checked for selection
     * @return true if <code>row</code> is selected; false otherwise
     */
    protected boolean isSelectedRow(final int row) {
        final int[] selectedRows = getSelectedRows();
        for (int i=0, n=selectedRows.length; i<n; i++) {
            if (selectedRows[i]==row) return true;
        }
        return false;
    }
    
    /**
     * Returns if a column is currently selected.
     * @param column the column that should be checked for selection
     * @return true if <code>column</code> is selected; false otherwise
     */
    protected boolean isSelectedColumn(final int column) {
        final int[] selectedColumns = getSelectedColumns();
        for (int i=0, n=selectedColumns.length; i<n; i++) {
            if (selectedColumns[i]==column) return true;
        }
        return false;
    }
    
    /**
     * Handler to track the mouse movements. Keeps the index of the cell (row and column indices)
     * that corresponds to the current mouse position.
     */
    class MouseOverHandler extends MouseAdapter implements MouseMotionListener {
        /** row index of the cell that corresponds to the current mouse position */
        int row=-1;
        /** column index of the cell that corresponds to the current mouse position */
        int column=-1;
        
        @Override
		public void mouseMoved(final MouseEvent e) {
            // update cell indices
            row = rowAtPoint(e.getPoint());
            column = columnAtPoint(e.getPoint());
            // possible optimization: repaint only on a row change and only the affected regions
            repaint();
        }
        
        @Override
		public void mouseExited(final MouseEvent e) {
            // dehighlight all rows when the mouse exited
            // (otherwise the last highlighted row would stay highlighted)
            row = -1;
            column = -1;
            repaint();
        }
        
        @Override
		public void mouseDragged(final MouseEvent mouseEvent) {}
    }
    
    // --------------------- size related methods ---------------------
    
    /**
     * Returns the preferred size for the TableHeader.
     * The size is calculated from the actual header data and renderers.
     * It is taken to be the maximum height and the sum of all widths of all columns' renderers.
     * @return the preferred dimension of the TableHeader
     */
    protected Dimension getPreferredTableHeaderSize() {
        final JTableHeader header = getTableHeader();
        // this renderer is used if no custom renderer has been set for a column
        final TableCellRenderer defaultHeaderRenderer = header.getDefaultRenderer();
        // the max height of all headers
        int maxHeaderHeight=0;
        // the sum of all widths of all headers
        int totalHeaderWidth=0;
        // check headers for all columns
        for (int columnIndex=0, columnCount=header.getColumnModel().getColumnCount(); columnIndex<columnCount; columnIndex++) {
            final TableColumn column = header.getColumnModel().getColumn(columnIndex);
            // get the renderer for the current column, use the default one if a custom one hasn't been set
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer==null) headerRenderer = defaultHeaderRenderer;
            // get the preferred size of the renderer
            final Dimension preferredHeaderSize = headerRenderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, columnIndex).getPreferredSize();
            // update maximum header height
            maxHeaderHeight=Math.max(maxHeaderHeight, preferredHeaderSize.height);
            // update total header width
            totalHeaderWidth+=preferredHeaderSize.width;
        }
        // preferred size equals table's preferred width and maximum column header height
        return new Dimension(totalHeaderWidth, maxHeaderHeight);
    }
    
    /**
     * Calculates and sets all sizes for the table
     * so that the values fit into their cells.
     * @param additionalColumnSpacing additional spacing that is added to each column
     * @param additionalViewportWidth additional width that is added to the preferred viewport size
     * @param maxRowCount maximum number of rows that should be displayed
     */
    protected void resize(final int additionalColumnSpacing, final int additionalViewportWidth, final int maxRowCount) {
        // calculate and set the preferred sizes for all columns and rows
        calcAndSetPreferredCellSizes(additionalColumnSpacing);

        // the tables preferred size now reflects the new columns' width & height
        final Dimension preferredSize = getPreferredSize();

        // add additional spacing that is necessary for the viewport (e.g. a scrollbar)
        preferredSize.width += additionalViewportWidth;
        
        // display at most maxRowCount rows
        final int displayedRowCount = Math.min(maxRowCount, getRowCount());
        
        // calculate and set the preferred height
        final int rowHeight = getRowHeight();
        preferredSize.height = rowHeight*displayedRowCount+16;
        
        setPreferredScrollableViewportSize(preferredSize);
    }
    
    /**
     * Calculates and sets the cell sizes.
     * This is done by iterating over all cell values and calculating
     * the sizes for the corresponding renderers.
     * @param extraColumnSpacing extra space that is added to each column width
     */
    protected void calcAndSetPreferredCellSizes(final int additionalColumnSpacing) {
        final TableModel model = getModel();
        final TableCellRenderer defaultHeaderRenderer = getTableHeader().getDefaultRenderer();
        
        int maxCellHeight = 0;
        
        for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++) {
            final TableColumn column = getColumnModel().getColumn(columnIndex);
            int headerWidth = 0;
            int maxCellWidth = 0;
            
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer==null) headerRenderer = defaultHeaderRenderer;
            
            Component renderComponent = headerRenderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, columnIndex);
            headerWidth = renderComponent.getPreferredSize().width;
            
            TableCellRenderer columnRenderer = column.getCellRenderer();
            if (columnRenderer==null) columnRenderer = getDefaultRenderer(model.getColumnClass(columnIndex));
            
            for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
                renderComponent = columnRenderer.getTableCellRendererComponent(this, model.getValueAt(rowIndex, columnIndex), false, false, rowIndex, columnIndex);
                final int cellWidth = renderComponent.getPreferredSize().width;
                final int cellHeight = renderComponent.getPreferredSize().height;
                if (cellWidth>maxCellWidth) maxCellWidth=cellWidth;
                if (cellHeight>maxCellHeight) maxCellHeight=cellHeight;
            }
            
            int preferredWidth = Math.max(headerWidth, maxCellWidth);
            preferredWidth += additionalColumnSpacing;
            
            column.setPreferredWidth(preferredWidth+50);
        }
        setRowHeight(maxCellHeight + getRowMargin());
    }
}