package ebiNeutrinoSDK.gui.dialogs;

import ebiNeutrino.core.EBIMain;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.utils.EBIPropertiesDialogRW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * 
 * Extends the default JDialog with the possibility to store the location and
 * size
 */

public class EBIDialog extends JDialog {

	private boolean storeLocation = false;
	private boolean storeSize = false;
	private Dimension originalDimension = new Dimension();
	private Dimension originalContentDimension = new Dimension();
	protected EBIPropertiesDialogRW properties = null;
	private final boolean closableEscape = true;
	private EBIMain ebiMain = null;
	private boolean haveSerial = false;
  
	public EBIDialog(final EBIMain owner) {
		super(owner);
		ebiMain = owner;
		initialize();
		properties = EBIPropertiesDialogRW.getProperties();
	}

	public EBIDialog(final EBIMain owner, final EBIPropertiesDialogRW properties) {
		super(owner);
		ebiMain = owner;
		initialize();
		this.properties = properties;
	}

	/**
	 * store the size
	 * 
	 * @param store
	 */
	public void storeSize(final boolean store) {
		this.storeSize = store;
	}

	/**
	 * store the loacation
	 * 
	 * @param store
	 */

	public void storeLocation(final boolean store) {
		this.storeLocation = store;
	}

	private void dialogIn() {

		try {
			originalContentDimension.setSize(this.getContentPane().getWidth(), this.getContentPane().getHeight());
			originalDimension.setSize(this.getWidth(), this.getHeight());

			if (isResizable()) {
				final Dimension dim = new Dimension();
				try {
					dim.width = Integer.valueOf(properties.getValue("EBI_DIALOG_SIZE_" + getName().toUpperCase() + "_WIDTH"));
					dim.height = Integer.valueOf(properties.getValue("EBI_DIALOG_SIZE_" + getName().toUpperCase() + "_HEIGHT"));
					setSize(dim);
				} catch (final java.lang.NumberFormatException ex) {
					final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					final Dimension frameSize = getSize();
					setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
				}
			}
			try {
				final Point pt = new Point();
				pt.x =  Integer.valueOf(properties.getValue("EBI_DIALOG_LOCATION_" + getName().toUpperCase() + "_X"));
				pt.y =  Integer.valueOf(properties.getValue("EBI_DIALOG_LOCATION_" + getName().toUpperCase() + "_Y"));

				if (pt.x > 0 && pt.y > 0) {
					this.setLocation(pt);
				} else {
					final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
					final Dimension frameSize = getSize();
					setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
				}

			} catch (final java.lang.NumberFormatException ex) {
				final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				final Dimension frameSize = getSize();
				setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
			}
		} catch (final NullPointerException ex) {
			final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			final Dimension frameSize = getSize();
			setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
		}
	}

	protected void dialogOut() {
		try {
			if (this.storeSize == true) {
				final Dimension dim = this.getSize();
				properties.setValue("EBI_DIALOG_SIZE_" + this.getName().toUpperCase() + "_WIDTH",  String.valueOf((int)dim.getWidth()));
				properties.setValue("EBI_DIALOG_SIZE_" + this.getName().toUpperCase() + "_HEIGHT", String.valueOf((int)dim.getHeight()));
			}
			if (this.storeLocation == true) {
				final Point pt = this.getLocation();
				properties.setValue("EBI_DIALOG_LOCATION_" + this.getName().toUpperCase() + "_X", String.valueOf((int)pt.getX()));
				properties.setValue("EBI_DIALOG_LOCATION_" + this.getName().toUpperCase() + "_Y", String.valueOf((int)pt.getY()));
			}
			properties.saveProperties();

		} catch (final NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setVisible(final boolean isVisible) {
		if (isVisible == false) {
			dialogOut();
			dispose();
			if (ebiMain != null && isHaveSerial()) {
				EBISystem.gui().removeGUIObject(getName());
			}
		} else {
			dialogIn();
			final InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
			setFocusableWindowState(true);
			setFocusable(true);
			final Action closeAction = new AbstractAction() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					setVisible(false);
				}
			};
			inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "CLOSE1");
			getRootPane().getActionMap().put("CLOSE1", closeAction);
		}
		super.setVisible(isVisible);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				dialogOut();
				setVisible(false);
				dispose();
			}
			}
		});
	}

	@Override
	protected JRootPane createRootPane() {
		final JRootPane rootPane = new JRootPane();
		rootPane.setBorder(null);
		final KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		final Action actionListener = new AbstractAction() {
			@Override
			public void actionPerformed(final ActionEvent actionEvent) {
				if (isClosableEscape()) {
					setVisible(false);
				}
			}
		};
		final InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	public boolean isClosableEscape() {
		return closableEscape;
	}

	public boolean isHaveSerial() {
		return haveSerial;
	}

	public void setHaveSerial(final boolean haveSerial) {
		this.haveSerial = haveSerial;
	}

	public void setOriginalDimension(final Dimension originalDimension) {
		this.originalDimension = originalDimension;
	}
	
	public void setOriginalContentDimension(final Dimension originalContentDimension) {
		this.originalContentDimension = originalContentDimension;
	}
	
	public Dimension getOriginalDimension() {
		return this.originalDimension;
	}
	
	public Dimension getOriginalContentDimension() {
		return this.originalContentDimension;
	}
	
	
}