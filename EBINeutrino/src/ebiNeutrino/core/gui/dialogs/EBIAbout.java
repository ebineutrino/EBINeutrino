package ebiNeutrino.core.gui.dialogs;

import ebiNeutrinoSDK.gui.dialogs.EBIDialogExt;

import javax.swing.*;
import java.awt.*;

public class EBIAbout extends EBIDialogExt {

	private JPanel jContentPane = null;
	private JButton jButtonOk = null;
	private JTabbedPane jTabbedPane = null;
	/**
	 * This is the default constructor
	 */
	public EBIAbout() {
	    super(null);
	    storeLocation(true);
	    storeSize(true);
        setName("EBIAbout");
	    initialize();
	    jTabbedPane.addTab("EBI Neutrino R1", new EBIAboutPanel1());
	    jTabbedPane.addTab("License", new EBIAboutPanel2());
	    this.setResizable(true);
	    this.setModal(true);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(482, 349);
		this.setTitle("About");
		this.setContentPane(getJContentPane());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(final java.awt.event.WindowEvent e) {

			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 457;
			gridBagConstraints1.ipady = 257;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.insets = new Insets(4, 4, 3, 8);
			final GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(4, 371, 18, 8);
			gridBagConstraints.gridy = 1;
			gridBagConstraints.ipadx = 45;
			gridBagConstraints.ipady = 3;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJButtonOk(), gridBagConstraints);
			jContentPane.add(getJTabbedPane(), gridBagConstraints1);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonOk	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOk() {
		if (jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText("Ok");
			jButtonOk.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
					setVisible(false);
					dispose();
				}
			});
		}
		return jButtonOk;
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
		    jTabbedPane = new JTabbedPane();
		}
		return jTabbedPane;
	}

}
