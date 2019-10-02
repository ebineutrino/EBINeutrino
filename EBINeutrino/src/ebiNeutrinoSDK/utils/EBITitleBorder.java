package ebiNeutrinoSDK.utils;

import javax.swing.border.Border;
import java.awt.*;


public class EBITitleBorder implements Border {

  protected int ovalWidth = 8;
  protected int ovalHeight = 8;
  protected final Color lightColor = new Color(10,10,174);
  protected final Color darkColor = new Color(255,255,255);
  private boolean isClosable = true;

  public EBITitleBorder(final boolean isClosable) {
    ovalWidth = 15;
    ovalHeight = 15;
    this.isClosable = isClosable;
  }

  @Override
public Insets getBorderInsets(final Component c) {
    return new Insets(1, 2, 2, 2);
  }

  @Override
public boolean isBorderOpaque() {
    return true;
  }

  @Override
public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width,final int height) {

        g.setColor(lightColor);
        g.draw3DRect(x,y,width,height,true);
        g.setColor(darkColor);
        g.fill3DRect(x-1,y-1,width+1,height+1,true);
        final Graphics2D g2 = (Graphics2D)g;
		// Draw bg top
        //Color startColor = lightColor;
        //Color endColor = darkColor;

        // A non-cyclic gradient
        // GradientPaint gradient = new GradientPaint(0, 0, endColor, 0, 60, startColor);
        //g2.setPaint(gradient);
		//g2.fillRoundRect(0, 0, width, 60,8,8);

        final Color sColor = darkColor;
        final Color eColor = lightColor;

        // A non-cyclic gradient
        final GradientPaint gradient1 = new GradientPaint(0, 0, sColor, width, 80, eColor);
        g2.setPaint(gradient1);
		g.fillRect(0, 61, width, height);

  }
}