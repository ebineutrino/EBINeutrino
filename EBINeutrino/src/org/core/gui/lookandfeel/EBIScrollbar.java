package org.core.gui.lookandfeel;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class EBIScrollbar extends BasicScrollBarUI {

    @Override
    protected void paintTrack(final Graphics g, final JComponent c, final Rectangle trackBounds) {
    }

    @Override
    protected void paintThumb(final Graphics g, final JComponent c, final Rectangle thumbBounds) {
        final Graphics2D g2 = (Graphics2D) g.create();

        final Color startColor = new Color(34, 34, 34);
        g2.setPaint(startColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 20, 20);
        g2.dispose();

    }

    /**
     * Creates a decrease button.
     *
     * @param orientation the orientation
     * @return a decrease button
     */
    @Override
    protected JButton createDecreaseButton(final int orientation) {
        final JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        jbutton.setMinimumSize(new Dimension(0, 0));
        jbutton.setMaximumSize(new Dimension(0, 0));
        return jbutton;
    }

    /**
     * Creates an increase button.
     *
     * @param orientation the orientation
     * @return an increase button
     */
    @Override
    protected JButton createIncreaseButton(final int orientation) {
        final JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        jbutton.setMinimumSize(new Dimension(0, 0));
        jbutton.setMaximumSize(new Dimension(0, 0));
        return jbutton;
    }

    public static ComponentUI createUI(final JComponent c) {
        return new EBIScrollbar();
    }

}
