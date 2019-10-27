package org.core.gui.lookandfeel;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


public class EBITabbedPaneUI extends MetalTabbedPaneUI implements MouseListener {

    private boolean isSelected = false;
    private Color startColor = new Color(75, 95, 189);
    private Color endColor = new Color(26, 49, 175);

    public static ComponentUI createUI(final JComponent c) {
        return new EBITabbedPaneUI();
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                  int x, int y, int w, int h, boolean isSelected) {
    }

    @Override
    protected void paintTabArea(final Graphics g, final int tabPlacement, final int selectedIndex) {
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }


    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                      int x, int y, int w, int h, boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {

            BufferedImage buffImgHeader = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gbiHeader = buffImgHeader.createGraphics();

            gbiHeader.setStroke(new BasicStroke(1.0f));
            GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 15, endColor);

            gbiHeader.setPaint(gradient);
            gbiHeader.fillRoundRect(0, 0, w, h, 2, 2);

            ((Graphics2D) g).drawImage(buffImgHeader, null, x, y);
        }
    }

    /**
     * Do not paint a focus indicator.
     *
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintFocusIndicator(java.awt.Graphics, int, java.awt.Rectangle[], int, java.awt.Rectangle, java.awt.Rectangle, boolean)
     */
    @Override
    protected void paintFocusIndicator(Graphics arg0, int arg1, Rectangle[] arg2, int arg3, Rectangle arg4, Rectangle arg5, boolean arg6) {
        // Leave it
    }

    /**
     * We do not want the tab to "lift up" when it is selected.
     *
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#installDefaults()
     */
    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabPane.addMouseListener(this);
    }

    /**
     * Nor do we want the label to move.
     */
    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

    /**
     * Increase the tab height a bit
     */
    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return fontHeight + 10;
    }

    @Override
    protected void layoutLabel(int arg0, FontMetrics arg1, int arg2, String arg3, Icon arg4, Rectangle arg5, Rectangle arg6, Rectangle arg7, boolean arg8) {
        super.layoutLabel(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    /**
     * Selected labels have a white color.
     *
     * @see javax.swing.plaf.basic.BasicTabbedPaneUI#paintText(java.awt.Graphics, int, java.awt.Font, java.awt.FontMetrics, int, java.lang.String, java.awt.Rectangle, boolean)
     */
    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font,
                             FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        int mx = 0;
        Font tabFont = new Font("Tahoma", Font.PLAIN, 12);
        if (tabPane.isEnabledAt(tabIndex)) {
            g.setFont(tabFont);
            if (isSelected && tabPlacement == TOP) {
                g.setColor(new Color(200, 200, 200));
            } else {
                g.setColor(new Color(100, 100, 100));
            }
        } else {
            g.setFont(tabFont);
            g.setColor(new Color(60, 60, 60));
        }
        g.drawString(title, textRect.x - 2, textRect.y + metrics.getAscent());
    }

    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
                                             int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
                                                int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
                                              int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
                                               int selectedIndex, int x, int y, int w, int h) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int sel = tabForCoordinate(tabPane, e.getX(), e.getY());
        if (tabPane.getSelectedIndex() == sel) {

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
