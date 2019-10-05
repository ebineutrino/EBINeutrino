package org.core.gui.component;

import javax.swing.*;
import java.awt.*;



public class EBIStatusBar extends JPanel {

	private JLabel systemHost = null;
	private final JProgressBar jProgressBarLoader = null;
	private final JLabel databaseText=new JLabel();
	private final JLabel systemVersion=new JLabel();
	private final FlowLayout statusBarLayout = new FlowLayout();



	/**
	 * This is the default constructor
	 */
	public EBIStatusBar() {
        setFocusable(false);
        setFocusCycleRoot(false);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		//databaseText.setBounds(new Rectangle(365, 0, 230, 40));
		databaseText.setLocation(-20,0);
		databaseText.setText("Database: ");
		databaseText.setIcon(new ImageIcon("images/db.png"));
		systemHost = new JLabel();
		//systemHost.setBounds(new Rectangle(140, 0, 230, 40));
		systemHost.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
		systemHost.setText("");
		systemHost.setIcon(new ImageIcon("images/agt_runit.png"));
		systemVersion.setText("EBI Neutrino");
		systemVersion.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
		//systemVersion.setBounds(new java.awt.Rectangle(4,0,150,40));
		this.setLayout(statusBarLayout);
		statusBarLayout.setAlignOnBaseline(true);
		statusBarLayout.setAlignment(FlowLayout.LEFT);
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setSize(1000,40);
		statusBarLayout.setHgap(20);
		this.add(systemVersion);
		this.add(systemHost);
		this.add(databaseText);
	}

	public void setSystemVersion(final String version){
		systemVersion.setText(systemVersion.getText()+" "+version);
	}

	public void setSystemDatabaseText(final String host){
		databaseText.setText(databaseText.getText()+host);
	}

	public void setSystemHost(final String host){
		systemHost.setText(host);
	}

	public void addAllert(final JScrollPane component){
		this.add(component);
	}
}
