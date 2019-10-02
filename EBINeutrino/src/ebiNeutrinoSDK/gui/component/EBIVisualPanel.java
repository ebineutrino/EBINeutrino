package ebiNeutrinoSDK.gui.component;

import ebiNeutrinoSDK.EBISystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * The EBIVisualPanel
 */
public class EBIVisualPanel extends JPanel {
	
	protected JLabel jTextFieldAdded = null;
	protected JLabel jTextFieldAddedFrom = null;
	protected JLabel jTextFieldChanged = null;
	protected JLabel jTextFieldChangedFrom = null;
	protected ImageIcon moduleIcon = null;
	protected JLabel moduleTitle = null;
	private boolean drawProgressView = false;
	protected int drawHeight;
	private int progressWidth = 0;
	private JLabel createdDate = null;
	private JLabel createdFrom = null;
	private JLabel changedDate = null;
	private JLabel changedFrom = null;
    private boolean visualProperties = true;
    private boolean showTimer = false;
    private String oldText = "";
    private boolean isVisualPanel = true;
    private int ID=-1;
    private Color startColor = new Color(75,95,189);
    private Color endColor = new Color(26,49,175);
    private final GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 30, endColor);

    public EBIVisualPanel(){
    	this.setLayout(null);
        initFocusTravesal();
    }


	@Override
	public void paintComponent(final Graphics g){
		super.paintComponent(g);
		if(isVisualPanel) {
			final Graphics2D g2 = (Graphics2D)g;

			final BufferedImage buffImgHeader = new BufferedImage(getWidth(), drawHeight,BufferedImage.TYPE_INT_ARGB);
			final Graphics2D gbiHeader = buffImgHeader.createGraphics();

			gbiHeader.setStroke(new BasicStroke(1.0f));
			// A non-cyclic gradient

			gbiHeader.setPaint(gradient);
			gbiHeader.fill(new RoundRectangle2D.Double(0, 0, getWidth()+15, drawHeight, 0, 0));
			// paint
			g2.drawImage(buffImgHeader, null, 0, 0);

			g2.drawImage(moduleIcon.getImage(),2,2, null);

			if(drawProgressView){
				g2.setPaint(new Color(255, 66, 41));
				g2.fill3DRect(0, drawHeight-3, progressWidth, 3,true);
			}

			setOpaque(true);
		}
	}


	public  void drawProgress(final boolean drawPView){
		this.drawProgressView = drawPView;
		if(drawPView) {
			for (int i = 0; i < getWidth(); i += 80) {
				repaint();
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressWidth = i;
			}
			progressWidth=0;
			this.drawProgressView = false;
			repaint();
		}
	}

	public void initialize() {
		changedFrom = new JLabel();
		changedFrom.setBounds(new Rectangle(827, 0, 63, 19));
		changedFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		changedFrom.setFont(new Font("Dialog", Font.BOLD, 10));
        changedFrom.setForeground(new Color(255,255,255));
		changedDate = new JLabel();
		changedDate.setBounds(new Rectangle(649, 0, 76, 19));
		changedDate.setHorizontalAlignment(SwingConstants.RIGHT);
		changedDate.setFont(new Font("Dialog", Font.BOLD, 10));
        changedDate.setForeground(new Color(255,255,255));
		createdFrom = new JLabel();
		createdFrom.setBounds(new Rectangle(484, 0, 63, 19));
		createdFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		createdFrom.setFont(new Font("Dialog", Font.BOLD, 10));
        createdFrom.setForeground(new Color(255,255,255));
		createdDate = new JLabel();
		createdDate.setBounds(new Rectangle(306, 0, 74, 19));
		createdDate.setHorizontalAlignment(SwingConstants.RIGHT);
		createdDate.setFont(new Font("Dialog", Font.BOLD, 10));
        createdDate.setForeground(new Color(255,255,255));
		moduleTitle = new JLabel();
		moduleTitle.setBounds(new Rectangle(30, 0, 222, 19));
		moduleTitle.setFont(new Font("Dialog", Font.BOLD, 10));
        moduleTitle.setForeground(new Color(255,255,255));
        this.setBorder(null);
		this.add(getJTextFieldAdded(), null);
		this.add(getJTextFieldAddedFrom(), null);
		this.add(getJTextFieldChanged(), null);
		this.add(getJTextFieldChangedFrom(), null);
		this.add(moduleTitle, null);
		this.add(createdDate, null);
		this.add(createdFrom, null);
		this.add(changedDate, null);
		this.add(changedFrom, null);
	}

    public void initFocusTravesal(){
		this.setFocusCycleRoot(true);
    	final KeyStroke KEYSTROKE_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
    	final KeyStroke KEYSTROKE_SHIFT_TAB =KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK);
    	//forward focus set
    	final Set<KeyStroke> forward = new HashSet<KeyStroke>();
    	forward.add(KEYSTROKE_TAB);
    	this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,forward);
    	//backward focus set
    	final Set<KeyStroke> backward = new HashSet<KeyStroke>();
    	backward.add(KEYSTROKE_SHIFT_TAB);
    	this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,backward);
	}

	private JLabel getJTextFieldAdded() {
		if (jTextFieldAdded == null) {
			jTextFieldAdded = new JLabel();
			jTextFieldAdded.setBounds(new Rectangle(382, 0, 98, 19));
			jTextFieldAdded.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			jTextFieldAdded.setForeground(new Color(255, 255, 255));
			jTextFieldAdded.setOpaque(false);
            jTextFieldAdded.setFocusable(false);
		}
		return jTextFieldAdded;
	}

	private JLabel getJTextFieldAddedFrom() {
		if (jTextFieldAddedFrom == null) {
			jTextFieldAddedFrom = new JLabel();
			jTextFieldAddedFrom.setBounds(new Rectangle(548, 0, 98, 19));
			jTextFieldAddedFrom.setForeground(new Color(255,255,255));
			jTextFieldAddedFrom.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			jTextFieldAddedFrom.setOpaque(false);
			jTextFieldAddedFrom.setFocusable(false);
		}
		return jTextFieldAddedFrom;
	}

    private JLabel getJTextFieldChanged() {
		if (jTextFieldChanged == null) {
			jTextFieldChanged = new JLabel();
			jTextFieldChanged.setBounds(new Rectangle(727, 0, 98, 19));
			jTextFieldChanged.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			jTextFieldChanged.setOpaque(false);
			jTextFieldChanged.setForeground(new Color(255,255,255));
			jTextFieldChanged.setFocusable(false);
		}
		return jTextFieldChanged;
	}

	private JLabel getJTextFieldChangedFrom() {
		if (jTextFieldChangedFrom == null) {
			jTextFieldChangedFrom = new JLabel();
			jTextFieldChangedFrom.setBounds(new Rectangle(892, 0, 98, 19));
			jTextFieldChangedFrom.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10));
			jTextFieldChangedFrom.setOpaque(false);
			jTextFieldChangedFrom.setForeground(new Color(255,255,255));
			jTextFieldChangedFrom.setFocusable(false);
		}
		return jTextFieldChangedFrom;
	}

    public void setModuleTitle(final String psTitle){
    	if(moduleTitle != null) {
			moduleTitle.setText(psTitle == null ? "" : psTitle);
			changedFrom.setText(EBISystem.i18n("EBI_LANG_CHANGED_FROM") == null ? "" : EBISystem.i18n("EBI_LANG_CHANGED_FROM")+":  ");
			changedDate.setText(EBISystem.i18n("EBI_LANG_CHANGED") == null ? "" : EBISystem.i18n("EBI_LANG_CHANGED")+":  ");
			createdFrom.setText(EBISystem.i18n("EBI_LANG_ADDED_FROM") == null ? "" : EBISystem.i18n("EBI_LANG_ADDED_FROM")+":  ");
			createdDate.setText(EBISystem.i18n("EBI_LANG_ADDED") == null ? "" : EBISystem.i18n("EBI_LANG_ADDED")+":  ");
	        oldText = psTitle;
    	}
	}

    public String getModuleTile(){
        return oldText;
    }

    public void setModuleIcon(final ImageIcon pIcon){
		if(pIcon != null ){
			moduleIcon = pIcon;
			repaint();
		}
	}

    public ImageIcon getModuleIcon(){
        return moduleIcon;
    }

	public void setChangePropertiesVisible(final boolean visible){
        visualProperties = visible;
		if(visible == true && jTextFieldAdded != null){
			jTextFieldAdded.setVisible(true);
			jTextFieldChanged.setVisible(true);
            jTextFieldAddedFrom.setVisible(true);
            jTextFieldChangedFrom.setVisible(true);
            createdDate.setVisible(true);
			createdFrom.setVisible(true);
			changedDate.setVisible(true);
			changedFrom.setVisible(true);
		}else{
			if(jTextFieldAdded != null){
				jTextFieldAdded.setVisible(false);
	            jTextFieldAddedFrom.setVisible(false);
				jTextFieldChanged.setVisible(false);
				jTextFieldChangedFrom.setVisible(false);
				createdDate.setVisible(false);
				createdFrom.setVisible(false);
				changedDate.setVisible(false);
				changedFrom.setVisible(false);
			}
		}
	}

   public boolean isVisualProperties(){
       return visualProperties;
   }
  
    public void setCreatedDate(final String text){
        jTextFieldAdded.setText(text);
    }

   public void setCreatedFrom(final String text){
      jTextFieldAddedFrom.setText(text);
   }

   public void setChangedDate(final String text){
       jTextFieldChanged.setText(text);
   }

   public void setChangedFrom(final String text){
       jTextFieldChangedFrom.setText(text);
   }

   public String getCreatedFrom(){
       return jTextFieldAddedFrom.getText();
   }

    @Override 
    public void setBackground(final Color color){
        final Color bgColor = color;
        repaint();
    }

	public boolean isShowTimer() {
		return showTimer;
	}
	public void setShowTimer(final boolean showTimer) {
		this.showTimer = showTimer;
	}
   
	public void setID(final int id){
		ID=id;
		if(id == -1){
			moduleTitle.setText(oldText);
		}else{
			moduleTitle.setText(moduleTitle.getText()+" ID:"+id);
		}
	}
	
	public int getID() {
		return ID;
	}
	public boolean isVisualPanel() {
		return isVisualPanel;
	}
	public void setVisualPanel(final boolean isVisualPanel) {
		this.isVisualPanel = isVisualPanel;
	}
}