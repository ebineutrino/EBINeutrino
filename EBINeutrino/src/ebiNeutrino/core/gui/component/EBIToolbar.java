package ebiNeutrino.core.gui.component;

import ebiCRM.EBICRMModule;
import ebiNeutrino.core.EBIMain;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBISecurity;
import ebiNeutrinoSDK.interfaces.IEBIToolBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * EBI Neutrino Toolbar component
 */
public class EBIToolbar extends JToolBar implements IEBIToolBar {

    private HashMap<Object, Object> buttonList = null;
    private int buttonID = -1;
    private EBIMain ebiMain = null;
    private IEBISecurity iSecurity = null;
    private String ebiNeutrinoTitle = null;
    private boolean isShowDesigner = true;
    private InputMap tmpMap = null;
    private EBICRMModule backupCRMModule = null;
    private int companyID = -1;

    /**
     * Constructor
     *
     * @param main
     */
    public EBIToolbar(final EBIMain main, final boolean showDesigner) {
        ebiMain = main;
        isShowDesigner = showDesigner;
        setBorderPainted(true);
        setRollover(true);
        setFocusable(false);
        setFocusCycleRoot(false);
        setFocusTraversalKeysEnabled(false);
        iSecurity = EBISystem.getInstance().getIEBISecurityInstance();
        ebiNeutrinoTitle = ebiMain.appTitle;
        buttonList = new HashMap<Object, Object>();
        setBackground( new Color(34, 34, 34));
    }


    /**
     * Show the addToolBarToEBIMain
     */
    public void addToolBarToEBIMain() {
        super.setOrientation(SwingConstants.HORIZONTAL);
        ebiMain.getContentPane().add(this, BorderLayout.NORTH);

    }

    /**
     * Add the system home button to the toolBar
     */

    public JButton addLogoutButton() {
        final JButton logout = new JButton(new ImageIcon(getClass().getClassLoader().getResource("agt_action_fail.png")));
        logout.setOpaque(true);
        logout.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (iSecurity.checkCanReleaseModules()) {
                    ebiMain.addLoginModul();
                }
            }
        });

        logout.setToolTipText("<html><body><br><b>EBI Neutrino Logout</b><br><br></body></html>");
        return logout;
    }

    public JButton addUserSettingButton() {

        final JButton usetting = new JButton(new ImageIcon(getClass().getClassLoader().getResource("add_userkl.png")));
        usetting.setFocusable(false);
        usetting.setOpaque(true);
        usetting.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (iSecurity.secureModule()) {
                    ebiMain.addUsermanagement();
                }
            }
        });

        usetting.setToolTipText("<html><body><br><b>" + EBISystem.i18n("EBI_LANG_USER_SETTING") + "</b><br><br></body></html>");
        return usetting;
    }

    public JButton addSystemSettingButton() {
        final JButton ssetting = new JButton(new ImageIcon(getClass().getClassLoader().getResource("tsetting.png")));
        ssetting.setFocusable(false);
        ssetting.setOpaque(true);
        ssetting.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (iSecurity.secureModule()) {
                    ebiMain.addSystemSetting(-1);
                }
            }
        });

        ssetting.setToolTipText("<html><body><br><b>" + EBISystem.i18n("EBI_LANG_SETTING") + "</b><br><br></body></html>");
        return ssetting;
    }

    @Override
	public JToolBar getJToolBar() {
        return this;
    }

    /**
     * Remove all component from the toolbar
     */
    @Override
	public void resetToolBar() {
        buttonList = new HashMap();
        getJToolBar().removeAll();
        buttonID = -1;
    }

    /**
     * Add a toolBar button
     *
     * @param icon
     * @param listener
     * @return Inserted ID
     */
    @Override
	public int addToolButton(final ImageIcon icon, final java.awt.event.ActionListener listener) {
        JButton jBarButton;
        if (icon != null) {
            jBarButton = new JButton(icon);
        } else {
            jBarButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("new.png")));
        }
        jBarButton.setOpaque(true);
        jBarButton.setBorder(null);
        jBarButton.addActionListener(listener);
        jBarButton.setFocusable(false);
        buttonList.put(++buttonID, jBarButton);
        return buttonID;
    }

    /**
     * Insert a custom component
     *
     * @param component JComponent parameter
     * @return Inserted ID
     */

    @Override
	public int addCustomToolBarComponent(final JComponent component) {
        if (component != null) {
            component.setFocusable(false);
            component.setSize(24, 24);
            component.setOpaque(true);
            buttonList.put(++buttonID, component);
        }
        return buttonID;
    }

    /**
     * Add toolbar separator
     */
    @Override
	public void addButtonSeparator() {
        buttonList.put(++buttonID, "-");
    }

    /**
     * add tooltipp
     *
     * @param id
     * @param text
     */
    @Override
	public void setComponentToolTipp(final int id, final String text) {
        try {
            final JComponent component = (JComponent) buttonList.get(id);
            component.setToolTipText(text);
            buttonList.put(id, component);
        } catch (final NullPointerException ex) {
            EBIExceptionDialog.getInstance("Toolbar point to an unavailable component").Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    /**
     * Enable or disable a toolbar component
     *
     * @param id
     * @param enabled
     */
    @Override
	public void setComponentToolBarEnabled(final int id, final boolean enabled) {
        try {
            final JComponent component = (JComponent) buttonList.get(id);
            component.setEnabled(enabled);
            buttonList.put(id, component);
        } catch (final NullPointerException ex) {
            EBIExceptionDialog.getInstance("Toolbar point to an unavailable component").Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    /**
     * Return a toolbar component
     *
     * @param id
     * @return
     */
    @Override
	public JComponent getToolbarComponent(final int id) {
        JComponent comp = null;
        try {
            comp = (JComponent) buttonList.get(id);
        } catch (final NullPointerException ex) {
            EBIExceptionDialog.getInstance("Toolbar point to an unavailable component").Show(EBIMessage.ERROR_MESSAGE);
        }
        return comp;
    }

    /**
     * Return a toolbar button
     *
     * @param id
     * @return
     */
    @Override
	public JButton getToolbarButton(final int id) {
        JButton comp = null;
        try {
            comp = (JButton) buttonList.get(id);
        } catch (final NullPointerException ex) {
            EBIExceptionDialog.getInstance("Toolbar point to an unavailable component").Show(EBIMessage.ERROR_MESSAGE);
        }
        return comp;
    }

    /**
     * Show the toolbar
     */
    @Override
	public void showToolBar(final boolean mainWindows) {
        if (mainWindows) {
            getJToolBar().add(addLogoutButton());
            getJToolBar().addSeparator(new Dimension(10, 10));
        }
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i) != null) {
                if (buttonList.get(i) instanceof JComponent) {
                    getJToolBar().add((JComponent) buttonList.get(i));
                } else if ("-".equals(buttonList.get(i).toString())) {
                    getJToolBar().addSeparator(new Dimension(10, 10));
                }
            }
        }
        if (mainWindows) {
            //Add User and System Setting
            getJToolBar().addSeparator(new Dimension(10, 10));
            if (EBISystem.getInstance().iuserRights.isAdministrator()) {
                getJToolBar().add(addUserSettingButton());
                getJToolBar().add(addSystemSettingButton());
            }
        }
        getJToolBar().updateUI();
    }
}
