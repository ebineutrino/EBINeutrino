package ebiNeutrinoSDK.gui.component;

import javax.swing.*;
import java.awt.*;

public class EBIExtendedPanel extends JPanel {

    private String imagePath = null;
    private String title = null;

    /**
     * This is the default constructor
     */
    public EBIExtendedPanel(final String title, final String img) {
        this.title = title;
        this.imagePath = img;
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        final JLabel jLabel = new JLabel();
        jLabel.setBounds(new java.awt.Rectangle(20, 10, 272, 51));
        jLabel.setText(title);
        jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
        jLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource(imagePath)));
        this.setLayout(null);
        this.setSize(570, 286);
        this.add(jLabel, null);
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        // Draw bg top
        final Color startColor = new Color(41, 47, 54);
        final Color endColor = new Color(41, 47, 54);

        // A non-cyclic gradient
        final GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 10, endColor);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), 60);

        final Color sColor = new Color(34, 34, 34);
        final Color eColor = new Color(34, 34, 34);

        // A non-cyclic gradient
        final GradientPaint gradient1 = new GradientPaint(0, 0, sColor, getWidth(), 60, eColor);
        g2.setPaint(gradient1);
        g.setColor(new Color(34, 34, 34));
        g.fillRect(0, 61, getWidth(), getHeight());
        g.setColor(new Color(34, 34, 34));
        g.drawLine(0, 60, getWidth(), 60);

        setOpaque(true);
    }

}
