/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.modules.utils;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 *
 * @author francesco
 */
public class JTableActionMaps {

    private JXTable table = null;

    public JTableActionMaps(final JXTable tb) {
        this.table = tb;
    }

    public void setTableAction(final AbstractTableKeyAction act) {

        final InputMap im = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        final KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        final KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

        final Action oldDown = table.getActionMap().get(im.get(down));
        final Action downAction = new AbstractAction() {

            @Override
			public void actionPerformed(final ActionEvent e) {
                oldDown.actionPerformed(e);
                final JXTable table = (JXTable) e.getSource();
                final int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());

                act.setArrowDownKeyAction(selectedRow);

            }
        };
        table.getActionMap().put(im.get(down), downAction);

        final Action oldUp = table.getActionMap().get(im.get(up));
        final Action upAction = new AbstractAction() {

            @Override
			public void actionPerformed(final ActionEvent e) {
                oldUp.actionPerformed(e);
                final JXTable table = (JXTable) e.getSource();
                final int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
                act.setArrowUpKeyAction(selectedRow);
            }
        };

        table.getActionMap().put(im.get(up), upAction);


        //final Action oldEnter = table.getActionMap().get(im.get(enter));
        final Action enterAction = new AbstractAction() {

            @Override
			public void actionPerformed(final ActionEvent e) {
                //oldEnter.actionPerformed(e);
                final JXTable table = (JXTable) e.getSource();
                final int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());

                act.setEnterKeyAction(selectedRow);

            }
        };

        table.getActionMap().put(im.get(enter), enterAction);

    }
}
