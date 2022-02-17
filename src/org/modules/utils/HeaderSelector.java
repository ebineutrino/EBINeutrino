package org.modules.utils;

import org.sdk.EBISystem;
import org.sdk.utils.EBIAbstractTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.sdk.interfaces.IEBIBuilder;

public class HeaderSelector extends MouseAdapter {

    private final JXTable table;
    public HeaderEditor editor;
    private IEBIBuilder guiRenderer = null;

    public HeaderSelector(final JXTable t) {
        table = t;
        editor = new HeaderEditor(this);
        guiRenderer = EBISystem.builder();
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            final JXTableHeader th = (JXTableHeader) e.getSource();
            final Point p = e.getPoint();
            final int col = getColumn(th, p);
            final TableColumn column = th.getColumnModel().getColumn(col);
            final String oldValue = (String) column.getHeaderValue();
            final Object value = editor.showEditor(th, col, oldValue);
            ((EBIAbstractTableModel) table.getModel()).columnNames[col] = value.toString();
            guiRenderer.combo("keyText", "csvSetImportDialog").removeAllItems();
            guiRenderer.combo("keyText", "csvSetImportDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

            for (int i = 0; i < ((EBIAbstractTableModel) table.getModel()).columnNames.length; i++) {
                if (i <= ((EBIAbstractTableModel) table.getModel()).columnNames.length) {
                    guiRenderer.combo("keyText", "csvSetImportDialog").addItem(((EBIAbstractTableModel) table.getModel()).columnNames[i]);
                }
            }

            if (!"".equals(value)) {
                column.setHeaderValue(value);
            }
            th.resizeAndRepaint();
        }
    }

    private int getColumn(final JXTableHeader th, final Point p) {
        final TableColumnModel model = th.getColumnModel();
        int ret = -1;
        for (int col = 0; col < model.getColumnCount(); col++) {
            if (th.getHeaderRect(col).contains(p)) {
                ret = col;
                break;
            }
        }
        return ret;
    }

}
