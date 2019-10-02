package ebiNeutrino.core.guiRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;


public class EBIButton extends JButton implements MouseListener, MouseMotionListener, KeyListener, FocusListener {

	private ImageIcon icon =null;
	private boolean isClicked =false;
	private Color color = null;
	private Color foreColor = null;
	private Point corner =new Point(5,5);
	private boolean enabled=true;
	private boolean haveFocus=false;
    private boolean drawBorder = true;
	
	
	public EBIButton(){
		this.addMouseListener(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addFocusListener(this);
		setOpaque(false);
	}
	
	
	@Override
	public void paintComponent(final Graphics gs){
        super.paintComponents(gs);

		final Graphics2D g2  = (Graphics2D) gs;
        final BufferedImage buffImgHeader = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
        final Graphics2D gbiHeader = buffImgHeader.createGraphics();
 
        gbiHeader.setStroke(new BasicStroke(1.0f));
        
        Color startColor;
        Color endColor;
        if(color == null){
        	if(enabled){
	        	if(!isClicked){
		        	if(drawBorder){
						startColor =  new Color(26,29,34);
						endColor = new Color(26,29,34);
                    }else{
                        startColor = new Color(36, 42, 42);
                        endColor = new Color(36, 42, 42);
                    }
		        }else{
					startColor =  new Color(26,29,34);
					endColor = new Color(26,29,34);
		        }
        	}else{
        		startColor = new Color(17, 17, 17);
	        	endColor = new Color(17, 17, 17);
        	}
        }else{
        	 if(!isClicked){
	        	startColor = color.brighter();
	        	endColor = color.darker();
        	 }else{
        		startColor = color.darker(); 
 	        	endColor =  color.brighter();
        	 }
        }
        
        // A non-cyclic gradient
        GradientPaint gradient = null;
        if(drawBorder){
             gradient = new GradientPaint(0, 0, startColor, 0, 43, endColor);
        }else{
            gradient = new GradientPaint(0, 0, startColor, 0, 33, endColor);
        }
        gbiHeader.setPaint(gradient);
        gbiHeader.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), corner.x, corner.y));

        if(drawBorder){
            if(haveFocus){
                gbiHeader.setColor(new Color(17, 17, 17));
                gbiHeader.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, corner.x,corner.y);
            }else{
                gbiHeader.setColor(new Color(36, 42, 42));
                gbiHeader.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, corner.x,corner.y);
            }
        }else{
            gbiHeader.setColor(new Color(36, 42, 42));
            gbiHeader.drawRoundRect(0, 0, getWidth()+1, getHeight(), corner.x,corner.y);
        }
        if(this.getIcon() != null){
        	
        	gbiHeader.drawImage(this.getIcon().getImage(), (getWidth() / 2) -(this.getIcon().getIconWidth() / 2),
        												(getHeight() / 2) - (this.getIcon().getIconHeight() /2), null);
        }
        
        // paint
        if(enabled){
        	g2.drawImage(buffImgHeader, null, 0, 0);
        }else{
        	g2.drawImage(convertToGrayscale(buffImgHeader), null, 0, 0);
        }
        if(foreColor == null){
        	if(enabled){
        		g2.setPaint(new Color(240,240,240));
        	}else{
        		g2.setPaint(new Color(110,110,110));
        	}
        }else{
        	g2.setPaint(getForeColor());
        }
        
        String name = this.getText();
        if(!"".equals(name)){
	        final FontMetrics fm = g2.getFontMetrics();
	        if(name.length() > 100 ){
				name = name.substring(0,100)+"...";
	        }
	        g2.drawString(name,(getWidth() /2) -(fm.stringWidth(name) / 2), ((getHeight() /2) + (fm.getHeight() /2))-2);
        }
        g2.dispose();
        
    }

	
	public static BufferedImage convertToGrayscale(final BufferedImage source) { 
	     final BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null); 
	     return op.filter(source, null);
	}
	
	public void setIcon(final ImageIcon icon){
		this.icon = icon;
	}
	
	@Override
	public ImageIcon getIcon(){
		return icon;
	}


	@Override
	public void keyTyped(final KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(final KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(final KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(final MouseEvent e) {}


	@Override
	public void mouseClicked(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(final MouseEvent e) {
		isClicked=true;
		//repaint();
	}


	@Override
	public void mouseReleased(final MouseEvent e) {
		isClicked=false;
		//repaint();	
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public Color getColor() {
		return color;
	}


	public void setColor(final Color color) {
		this.color = color;
	}


	public Color getForeColor() {
		return foreColor;
	}


	public void setForeColor(final Color foreColor) {
		this.foreColor = foreColor;
	}


	public Point getCorner() {
		return corner;
	}


	public void setCorner(final int x, final int y) {
		this.corner = new Point(x,y);
	}
	
	@Override
	public void setEnabled(final boolean enabled){
		super.setEnabled(enabled);
		this.enabled = enabled;
	}


	@Override
	public void focusGained(final FocusEvent e) {
		haveFocus=true;
		repaint();
	}


	@Override
	public void focusLost(final FocusEvent e) {
		haveFocus=false;
		repaint();
	}
	
    
    public void setDrawBorder(final boolean drawBord){
        this.drawBorder = drawBord;
    }
}
