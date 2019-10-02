package ebiNeutrinoSDK.gui.dialogs;

import ebiNeutrino.core.EBIMain;
import ebiNeutrinoSDK.gui.component.EBIVisualPanelTemplate;

import javax.swing.*;
import java.awt.*;


/**
 * Show a image file
 *
 */
public class EBIImageViewer extends EBIDialogExt {

	private EBIVisualPanelTemplate jContentPane = null;
	private JScrollPane jScrollPane = null;
	private JLabel imageConstainer = null;

	/**
	 * This is the xxx default constructor
	 */
	public EBIImageViewer(final EBIMain main,final ImageIcon image) {
		super(main);

		this.setResizable(true);
		storeLocation(true);
		storeSize(true);
		initialize();
        jContentPane.setModuleTitle("EBI Image Viewer");
        jContentPane.setEnableChangeComponent(false);
        jContentPane.setModuleIcon(new ImageIcon(getClass().getClassLoader().getResource("new.png")));
		this.imageConstainer.setIcon(image);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(634, 462);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private EBIVisualPanelTemplate getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new EBIVisualPanelTemplate(false);
			jContentPane.getPanel().setLayout(new BorderLayout());
			jContentPane.setBackground(Color.black);
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBackground(Color.black);
			jScrollPane.setViewportView(getImageContainer());
		}
		return jScrollPane;
	}

	private JLabel getImageContainer(){
		if(this.imageConstainer == null){
			imageConstainer = new JLabel();
			imageConstainer.setHorizontalAlignment(SwingConstants.CENTER);
			imageConstainer.setBackground(new Color(20, 17, 17));
		}
		return this.imageConstainer;
	}
}
