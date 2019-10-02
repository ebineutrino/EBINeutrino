package ebiNeutrinoSDK.gui.component;

import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class EBIVisualPanelTemplate extends EBIVisualPanel implements ComponentListener {

    private final JPanel tPanel = new JXPanel();
    private int drawHeight =21;

    public EBIVisualPanelTemplate(final boolean isVisualPanel){
        super.setVisualPanel(isVisualPanel);
        tPanel.setLayout(null);
        super.drawHeight = drawHeight;
        if(isVisualPanel) {
        	tPanel.setLocation(0, 21);
        }else {
        	tPanel.setLocation(0, 0);
        }
        super.add(tPanel, null);
        addComponentListener(this);
    }

    @Override
	public void componentResized(final ComponentEvent e){
        tPanel.setSize(((EBIVisualPanel)e.getSource()).getWidth(), ((EBIVisualPanel)e.getSource()).getHeight());
    }

    @Override
	public void componentMoved(final ComponentEvent e){

    }

    @Override
	public void componentShown(final ComponentEvent e){

    }

    @Override
	public void componentHidden(final ComponentEvent e){

    }

    public void setEnableChangeComponent(final boolean enabled){
         super.setChangePropertiesVisible(enabled);
    }

    public void add(final JComponent component, final Object obj){
        tPanel.add(component, obj);
    }

    @Override
    public void setSize(final Dimension dim){
        tPanel.setSize(dim);
        super.setSize(dim);
    }

    public void setBackgroundColor(final Color color){
       tPanel.setBackground(color);
    }

    public JPanel getPanel(){
        return this.tPanel;
    }
}
