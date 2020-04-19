package org.core;


import org.modules.EBIModule;
import org.core.guiRenderer.EBIGUIRenderer;
import org.core.gui.dialogs.EBISplashScreen;
import org.core.gui.component.EBIExtensionContainer;
import org.core.gui.component.EBIStatusBar;
import org.core.gui.component.EBIToolbar;
import org.core.gui.lookandfeel.MoodyBlueTheme;
import org.core.modules.management.EBIModuleHandler;
import org.core.settings.EBISystemSetting;
import org.core.settings.EBISystemSettingPanel;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialogExt;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBISecurity;
import org.sdk.interfaces.IEBIToolBar;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import org.core.run.update.EBISocketDownloader;

/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * Description: This is the main class for EBI Neutrino
 *
 * JVM start point: public static void main(String[] args)
 */
public class EBIMain extends JFrame {

    public static boolean canReleaseUser = false;
    public String appTitle = "EBI Neutrino R1 CRM / ERP Framework";
    public EBISystemSetting systemSetting = null;
    public static Logger logger = Logger.getLogger(EBIMain.class.getName());
    public EBIToolbar ebiBar = null;
    public EBIExtensionContainer container = null;
    protected static boolean showUpdateInfo = false;
    protected IEBISecurity iSecurity = null;
    protected IEBIToolBar bar = null;
    public EBISplashScreen splash = null;
    public EBIModuleHandler mng = null;
    public EBIDialogExt frameSetting = null;
    public EBIStatusBar stat = null;
    public JPanel panAllert = null;
    public JScrollPane pallert = null;
    public int USER_DELETE_ID = -1;
    public EBIToolbar userSysBar = null;

    private EBIModule ebiModule = null;

    private String resourceLoggerPath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator;

    public static void main(final String[] args) throws Exception {
        try {

            final EBIMain application = new EBIMain();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ToolTipManager.sharedInstance().setInitialDelay(0);

                    try {
                        application.pack();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    application.setSize(d.width, d.height);
                    application.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            });


            //check for update
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    EBISocketDownloader.getInstance().canUpdate();
                }
            });

        } catch (final Exception exx) {
            exx.printStackTrace();
            logger.error(EBISystem.printStackTrace(exx));
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(exx)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * EBIMain default constructor it initialize the system functionality
     */
    public EBIMain() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new MoodyBlueTheme());
            SwingUtilities.updateComponentTreeUI(this);

            EBISystem.getInstance().addMainFrame(this);

            splash = new EBISplashScreen();
            PropertyConfigurator.configure(resourceLoggerPath + "config/ebiLogger.config");
            splash.setVisible(true);

            final EBIDatabase conn = new EBIDatabase();
            EBISystem.getInstance().setIEBIDatabase(conn);

            new EBINeutrinoSystemInit(splash);

            if (EBINeutrinoSystemInit.isConfigured) {
                initializeTheSystem();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBISystem.getInstance().getMessage().debug(EBISystem.printStackTrace(ex));
            logger.error("Exception", ex.fillInStackTrace());
        }
    }

    public void initializeTheSystem() throws Exception {
        /**
         * ******************
         */
        // Initialize xml gui renderer
        EBISystem.getInstance().setIEBIGUIRendererInstance(new EBIGUIRenderer(EBIMain.this));

        /**
         * *****************
         */
        initialize();
        // Initialize Container, tab panel
        container = new EBIExtensionContainer(EBIMain.this);
        container.initContainer();
        EBISystem.getInstance().setIEBIContainerInstance(container);
        /**
         * ******************
         */
        // Create toolbars
        ebiBar = new EBIToolbar(EBIMain.this);
        ebiBar.addToolBarToEBIMain();
        EBISystem.getInstance().setIEBIToolBarInstance(ebiBar);

        iSecurity = EBISystem.getInstance().getIEBISecurityInstance();
        bar = EBISystem.getInstance().getIEBIToolBarInstance();

        ebiModule = new EBIModule();
        EBISystem.getInstance().setIEBIModule(ebiModule);
        mng = new EBIModuleHandler(EBIMain.this, ebiModule);

        /**
         * *****************
         */
        // Initialize report system
        new Thread(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final EBIReportSystem reportSystem = new EBIReportSystem();
                        EBISystem.getInstance().setIEBIReportSystemInstance(reportSystem);
                    }
                });
            }
        }).start();

        // CREATE TASKBAR
        stat = new EBIStatusBar();
        stat.setSystemVersion(EBIVersion.getInstance().getVersion());
        stat.setSystemHost(EBISystem.host);
        stat.setSystemDatabaseText(EBISystem.DATABASE_SYSTEM);
        pallert = new JScrollPane();
        panAllert = new JPanel();
        panAllert.setVisible(false);
        pallert.setVisible(false);
        pallert.setViewportView(panAllert);
        stat.addAllert(pallert);
        getContentPane().add(stat, BorderLayout.SOUTH);
        EBISystem.getInstance().checkIsValidUser("root", "ebineutrino");
        showBusinessModule();
        splash.setVisible(false);
    }

    private void initialize() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle(appTitle);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent winEvt) {
                try {
                    if (iSecurity.checkCanReleaseModules()) {
                        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_CLOSE"))
                                .Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                            if (mng != null) {
                                mng.onExit();
                            }
                            System.exit(0);
                        }
                    }
                } catch (final Exception ex) {
                    logger.error("Exception", ex.fillInStackTrace());
                    ex.printStackTrace();
                }
            }
        });
    }

    public void showBusinessModule() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mng.showModule();
            }
        }).start();
    }

    public void addSystemSetting(final int selectedList) {
        frameSetting = new EBIDialogExt(this);
        frameSetting.getContentPane().setLayout(new BorderLayout(0, 0));
        systemSetting = new EBISystemSetting(this);
        systemSetting.listName.setStart();
        final EBIToolbar sysBar = new EBIToolbar(this);

        final int NEW_ID = sysBar.addToolButton(EBISystem.getInstance().getIconResource("new.png"), new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                if (EBISystemSetting.selectedModule == -1) {
                    addSystemSetting(selectedList);
                } else {
                    if (EBISystemSetting.selectedModule == 1) {
                        try {
                            systemSetting.listName.report.newReport();
                        } catch (final Exception ex) {
                            logger.error("Exception", ex.fillInStackTrace());
                            ex.printStackTrace();
                        }
                    }
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        ((JButton) sysBar.getToolbarComponent(NEW_ID)).setMnemonic(KeyEvent.VK_N);
        sysBar.setComponentToolTipp(NEW_ID, EBISystem.i18n("EBI_LANG_T_LOAD_ALL_SETTING"));

        final int SAVE_ID = sysBar.addToolButton(EBISystem.getInstance().getIconResource("save.png"), new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                systemSetting.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                if (EBISystemSetting.selectedModule == 0) {

                    systemSetting.listName.einstp.saveSystemSetting();
                    mng.reloadSelectedModule();
                    frameSetting.setVisible(false);

                    addSystemSetting(0);
                    frameSetting.requestFocus();

                } else if (EBISystemSetting.selectedModule == 1) {
                    systemSetting.listName.report.saveReport();
                }
                systemSetting.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        ((JButton) sysBar.getToolbarComponent(SAVE_ID)).setMnemonic(KeyEvent.VK_S);
        sysBar.setComponentToolTipp(SAVE_ID, EBISystem.i18n("EBI_LANG_T_SAVE_SETTING"));

        final int DELETE_ID = sysBar.addToolButton(EBISystem.getInstance().getIconResource("delete.png"), new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                if (EBISystemSetting.selectedModule == 1) {
                    systemSetting.listName.report.deleteReport();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        ((JButton) sysBar.getToolbarComponent(DELETE_ID)).setMnemonic(KeyEvent.VK_D);
        sysBar.setComponentToolTipp(DELETE_ID, EBISystem.i18n("EBI_LANG_T_DELETE_SETTING"));
        sysBar.showToolBar(false);

        systemSetting.setChangePropertiesVisible(false);
        systemSetting.getPanel().add(sysBar.getJToolBar(), BorderLayout.NORTH);

        if (selectedList != -1) {
            systemSetting.listName.jListnames.setSelectedIndex(selectedList);
            systemSetting.listName.cpanel.removeAll();
            systemSetting.listName.einstp = new EBISystemSettingPanel();
            systemSetting.listName.cpanel.add(systemSetting.listName.einstp, java.awt.BorderLayout.CENTER);
        }

        frameSetting.setName("SystemSetting");
        frameSetting.storeLocation(true);
        frameSetting.storeSize(true);
        frameSetting.setSize(new Dimension(1100, 600));
        frameSetting.setResizable(true);
        systemSetting.setModuleTitle(EBISystem.i18n("EBI_LANG_SYSTEM_SETTING"));
        systemSetting.setModuleIcon(new ImageIcon("images/folder_config64.png"));

        JScrollPane scrollPane = new JScrollPane(systemSetting);
        scrollPane.setPreferredSize(new Dimension(1100, 600));
        scrollPane.setSize(new Dimension(1100, 600));
        scrollPane.getViewport().setPreferredSize(new Dimension(1100, 600));
        scrollPane.getViewport().setSize(new Dimension(1100, 600));

        frameSetting.getContentPane().add(scrollPane, BorderLayout.CENTER);

        if (frameSetting != null && !frameSetting.isVisible()) {
            frameSetting.setVisible(true);
        }
    }

    public Object getActiveModule() {
        return mng.getActiveModule();
    }
}
