package ebiNeutrino.core.setup;

import ebiNeutrino.core.EBINeutrinoSystemInit;
import ebiNeutrinoSDK.gui.dialogs.EBIDialogExt;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EBISetup extends EBIDialogExt {

	private JPanel jContentPane = null;
	private JMenuBar jJMenuBar = null;
	private JMenu fileMenu = null;
	private JMenu helpMenu = null;
	private JMenuItem exitMenuItem = null;
	private JMenuItem aboutMenuItem = null;
	private JTabbedPane jTabbedPane = null;
	public EBISetupDB dbSetup = null;
	public boolean DBConfigured = false;
	public EBINeutrinoSystemInit sysINIT = null;

	public EBISetup( final EBINeutrinoSystemInit init) {
		super(null);
		sysINIT= init;
		setModal(true);
		setResizable(false);
		setName("EBISetup");
		initialize();
		dbSetup = new EBISetupDB(this);
		getJTabbedPane().addTab("EBI Database setup", dbSetup);
	}

	private void initialize() {
		this.setSize(575, 440);
		this.setContentPane(getJContentPane());
		this.setTitle("EBI Neutrino R1 Database Setup");
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	public JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setName("jTabbedPane");
			jTabbedPane.setTabPlacement(SwingConstants.TOP);
		}
		return jTabbedPane;
	}

	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					dispose();
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}


	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About...");
			aboutMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					EBIExceptionDialog.getInstance(EBISetup.this, "EBI Neutrino R1 Database setup")
							.Show(EBIMessage.INFO_MESSAGE);
				}
			});
		}
		return aboutMenuItem;
	}
}