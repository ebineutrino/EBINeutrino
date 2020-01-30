package org.core.gui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import javax.swing.JTextField;
import java.awt.Font;

public class EBITextfield extends JTextField implements FocusListener {

    private String placeHolder = "";
    private boolean hasFocus = false;
    private Graphics2D gbiHeader = null;
    private BufferedImage buffImgHeader = null;

    public EBITextfield() {
        addFocusListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(new Color(112, 112, 112));
        if (!"".equals(placeHolder) && "".equals(getText()) && !hasFocus) {
            final FontMetrics fm = g2.getFontMetrics();
            g2.setFont(new Font("Tahoma", Font.PLAIN, 12));
            g2.drawString(placeHolder, 5, ((getHeight() / 2) + (fm.getHeight() / 2)) - 2);
        }
        g2.dispose();
    }

    public void setPlaceHolder(String text) {
        this.placeHolder = text;
    }

    @Override
    public void focusGained(FocusEvent fe) {
        hasFocus = true;
        repaint();
    }

    @Override
    public void focusLost(FocusEvent fe) {
        hasFocus = false;
        repaint();
    }
}
