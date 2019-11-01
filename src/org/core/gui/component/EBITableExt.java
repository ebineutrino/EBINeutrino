package org.core.gui.component;

import org.modules.utils.AbstractTableKeyAction;
import org.modules.utils.JTableActionMaps;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.utils.EBISaveRestoreTableProperties;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EBITableExt extends JXTable implements MouseListener {

    private MouseAdapter mouseCallback = null;
    public EBISaveRestoreTableProperties tabProp = new EBISaveRestoreTableProperties();

    public EBITableExt() {
        addMouseListener(this);

        this.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                tabProp.saveTableProperties(EBITableExt.this);
            }
        });
    }

    public void addSelectionListener(final EBIUICallback uiCallback) {
        if (uiCallback != null) {
            this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (e.getValueIsAdjusting()) {
                                return;
                            }
                            uiCallback.selectionListenerEvent(e);
                        }
                    });
                }
            });
        }
    }

    public void addKeyAction(final EBIUICallback uiCallback) {
        new JTableActionMaps(this).setTableAction(new AbstractTableKeyAction() {
            @Override
            public void setArrowDownKeyAction(final int selRow) {
                if (uiCallback != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            uiCallback.tableKeyDown(selRow);
                        }
                    });
                }
            }

            @Override
            public void setArrowUpKeyAction(final int selRow) {
                if (uiCallback != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            uiCallback.tableKeyUp(selRow);
                        }
                    });
                }
            }

            @Override
            public void setEnterKeyAction(final int selRow) {
                if (uiCallback != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            uiCallback.tableKeyEnter(selRow);
                        }
                    });
                }
            }
        });
    }

    @Override
    public final Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex, final int vColIndex) {
        Component c = null;
        try {
            c = super.prepareRenderer(renderer, rowIndex, vColIndex);
            if (c instanceof JComponent && getValueAt(rowIndex, vColIndex) != null) {
                final JComponent jc = (JComponent) c;
                jc.setToolTipText("<html><b>"
                        + getValueAt(rowIndex, vColIndex).toString().replaceAll("\n", "<br>")
                        + "</b></html>");

            }
        } catch (final IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseCallback != null) {
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    
                    mouseCallback.mousePressed(mouseEvent);
                }
            });
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseCallback != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mouseCallback.mouseReleased(mouseEvent);
                }
            });
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (mouseCallback != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mouseCallback.mouseEntered(mouseEvent);
                }
            });
        }
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (mouseCallback != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    mouseCallback.mouseExited(mouseEvent);
                }
            });
        }
    }

    public MouseAdapter getMouseCallback() {
        return mouseCallback;
    }

    public void setMouseCallback(MouseAdapter mouseCallback) {
        this.mouseCallback = mouseCallback;
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        dataModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                tabProp.restoreTableProperties(EBITableExt.this);
            }
        });
    }

}
