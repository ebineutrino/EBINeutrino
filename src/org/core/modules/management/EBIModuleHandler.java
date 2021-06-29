package org.core.modules.management;

import org.core.EBIMain;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Module management class
 */
public class EBIModuleHandler {

    private IEBIExtension ebiExtension = null;
    private EBIMain ebiMain = null;
    private Object module = null;

    /**
     * Constructor
     */
    public EBIModuleHandler(final EBIMain main, final Object module) {
        ebiExtension = (IEBIExtension) module;
        this.module = module;
        ebiMain = main;
    }

    /**
     * Release extension method
     */
    public void releaseModule() {
        try {
            if (ebiExtension != null) {
                ebiExtension.ebiRemove();
                ebiMain.container.removeAllFromContainer();
                EBISystem.canRelease = true;
                EBISystem.isSaveOrUpdate = false;
                ebiExtension = null;
                this.module = null;
                EBISystem.gui().init();
                EBISystem.hibernate().removeAllHibernateSessions();
                System.gc();
            }
            removeF5Action();
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public Object releaseModule(final Object obj) {
        Object o = null;
        try {
            if (obj != null) {
                o = ((IEBIExtension) obj).ebiRemove();
                EBISystem.canRelease = true;
                EBISystem.isSaveOrUpdate = false;
                ebiExtension = null;
                this.module = null;
                EBISystem.gui().init();
                EBISystem.hibernate().removeAllHibernateSessions();
                System.gc();
            }
            removeF5Action();
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return o;
    }

    public void onExit() {
        if (ebiExtension != null) {
            ebiExtension.onExit();
        }
    }

    public void onLoad() {
        ebiExtension.onLoad();
    }

    /**
     * Call the main method ebiMain
     */
    public boolean showModule() {
        try {
            EBISystem.getInstance().getIEBIToolBarInstance().resetToolBar();
            if (ebiExtension != null) {
                ebiExtension.onLoad();
            }
            if (!ebiExtension.ebiMain(null)) {
                return false;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    addF5Action();
                    addTAction();
                    addAltShowToolbar();
                    addAltTabNavigation();
                }
            });
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        }
        ebiMain.setVisible(true);
        return true;
    }

    public boolean showModule(final Object module, final Object o, final boolean resetToolBar) {
        try {
            ebiMain.container.removeAllFromContainer();
            if (module != null) {
                ((IEBIExtension) module).onLoad();
            }
            if (resetToolBar) {
                EBISystem.getInstance().getIEBIToolBarInstance().resetToolBar();
            }
            if (!((IEBIExtension) module).ebiMain(o)) {
                return false;
            }
            this.module = module;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    addF5Action();
                    addTAction();
                    addAltShowToolbar();
                    addAltTabNavigation();
                }
            });
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        ebiMain.setVisible(true);
        return true;
    }

    public Object getActiveModule() {
        return this.module;
    }

    public void addAltShowToolbar() {
        final Action showToolBarAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.getInstance().getIEBIToolBarInstance().getJToolBar().isVisible()) {
                    EBISystem.getInstance().getIEBIToolBarInstance().getJToolBar().setVisible(true);
                    ebiMain.stat.setVisible(true);
                } else {
                    EBISystem.getInstance().getIEBIToolBarInstance().getJToolBar().setVisible(false);
                    ebiMain.stat.setVisible(false);
                }
            }
        };
        final InputMap inputMap = ((JPanel) ebiMain.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyStroke.getKeyStroke("F1").getKeyCode(), InputEvent.CTRL_DOWN_MASK, true), "SHOWTOOLBAR");
        ((JPanel) ebiMain.getContentPane()).getActionMap().put("SHOWTOOLBAR", showToolBarAction);
    }

    public void addAltTabNavigation() {
        final Action rightTabSwitch = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                int tabCount = EBISystem.getUIContainer().getTabCount();
                int selectedTabIndex = EBISystem.getUIContainer().getSelectedTab();
                if (selectedTabIndex < tabCount - 1 && EBISystem.getUIContainer().getTabInstance().isEnabledAt(selectedTabIndex + 1)) {
                    EBISystem.getUIContainer().setSelectedTab(selectedTabIndex + 1);
                } else {
                    EBISystem.getUIContainer().setSelectedTab(0);
                }
            }
        };

        final InputMap inputMap = ((JPanel) ebiMain.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyStroke.getKeyStroke("RIGHT").getKeyCode(), InputEvent.ALT_DOWN_MASK, true), "TABSWITCH");
        ((JPanel) ebiMain.getContentPane()).getActionMap().put("TABSWITCH", rightTabSwitch);

        final Action leftTabSwitch = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                int tabCount = EBISystem.getUIContainer().getTabCount();
                int selectedTabIndex = EBISystem.getUIContainer().getSelectedTab();
                if (selectedTabIndex > 0 && EBISystem.getUIContainer().getTabInstance().isEnabledAt(selectedTabIndex - 1)) {
                    EBISystem.getUIContainer().setSelectedTab(selectedTabIndex - 1);
                } else {
                    for (int i = tabCount - 1; i > 1; i--) {
                        if (EBISystem.getUIContainer().getTabInstance().isEnabledAt(i)) {
                            EBISystem.getUIContainer().setSelectedTab(i);
                            break;
                        }
                    }
                }
            }
        };

        final InputMap inputMap1 = ((JPanel) ebiMain.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap1.put(KeyStroke.getKeyStroke(KeyStroke.getKeyStroke("LEFT").getKeyCode(), InputEvent.ALT_DOWN_MASK, true), "TABSWITCH1");
        ((JPanel) ebiMain.getContentPane()).getActionMap().put("TABSWITCH1", leftTabSwitch);
    }

    public void addF5Action() {
        final Action refreshAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                reloadSelectedModule();
            }
        };
        final InputMap inputMap = ((JPanel) ebiMain.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyStroke.getKeyStroke("F5").getKeyCode(), InputEvent.CTRL_DOWN_MASK, true), "REFRESH");
        ((JPanel) ebiMain.getContentPane()).getActionMap().put("REFRESH", refreshAction);
    }

    public void addTAction() {
        EBISystem.gui().addScriptBean("groovy", "Run/tests.groovy", "groovy", "", "Run");
        final Action refreshAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                //todo call script in Run Folder
                EBISystem.gui().excScript("Run", null);
            }
        };
        final InputMap inputMap = ((JPanel) ebiMain.getContentPane()).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK, true), "RunScript");
        ((JPanel) ebiMain.getContentPane()).getActionMap().put("RunScript", refreshAction);
    }

    public void reloadSelectedModule() {
        ebiMain.setCursor(new Cursor(Cursor.WAIT_CURSOR));     
        int selectedTabIndex = EBISystem.getUIContainer().getSelectedTab();
        final Object obj = getActiveModule();
        //Return object to restore
        final Object restore = releaseModule(obj);
        showModule(obj, restore, false);
        ebiMain.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        EBISystem.getUIContainer().setSelectedTab(selectedTabIndex);
        
    }

    private void removeF5Action() {
        ebiMain.container.getTabInstance().getActionMap().remove("REFRESH");
        ebiMain.container.getTabInstance().getActionMap().remove("RunScript");
    }
}
