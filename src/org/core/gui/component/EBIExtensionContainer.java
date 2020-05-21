package org.core.gui.component;

import org.core.EBIMain;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.CloseableTabbedPaneListener;
import org.sdk.interfaces.IEBIContainer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * EBI Neutrino component container This class is managing the jTabbedPane
 * component
 */
public class EBIExtensionContainer implements IEBIContainer {

    public EBIMain ebiMain = null;
    private JTabbedPane jTabbedPane = null;
    public static final int NON_MNEMO = -1;
    private JScrollPane jscrollPane = null;
    private final CloseableTabbedPaneListener lis = null;
    public final List<Object> registeredTabs = new ArrayList<Object>();

    public EBIExtensionContainer(final EBIMain main) {
        ebiMain = main;
    }

    /**
     * Initialize the ebiNeutrino container
     *
     * @return void
     */
    public void initContainer() {

        jTabbedPane = new JTabbedPane();
        jTabbedPane.setName("jTabbedPane");
        jTabbedPane.setTabPlacement(SwingConstants.TOP);
        jTabbedPane.setFocusable(false);
        jTabbedPane.setFocusCycleRoot(false);
        jTabbedPane.setFocusTraversalKeysEnabled(false);
        jTabbedPane.setBorder(BorderFactory.createEmptyBorder());
        ebiMain.getContentPane().add(this.jTabbedPane, BorderLayout.CENTER);
    }

    public void initContainer(final JPanel panel) {

        jTabbedPane = new JTabbedPane();
        jTabbedPane.setName("jTabbedPane");
        jTabbedPane.setTabPlacement(SwingConstants.TOP);
        jTabbedPane.setFocusable(false);
        jTabbedPane.setFocusCycleRoot(false);
        jTabbedPane.setFocusTraversalKeysEnabled(false);

        panel.add(this.jTabbedPane, BorderLayout.CENTER);
    }

    /**
     * Add a component to container
     *
     * @return index
     */
    @Override
    public int addContainer(final String title, final JComponent component, final ImageIcon icon, final int mnemo_key) {
        jTabbedPane.addTab(title, icon, component);

        if (mnemo_key != NON_MNEMO) {
            this.jTabbedPane.setMnemonicAt((this.jTabbedPane.getTabCount() - 1), mnemo_key);
        }
        // Used for the GUIDesigner to identify the selected component xml file
        EBISystem.gui().getHashTabtoFile().put((this.jTabbedPane.getTabCount() - 1),
                "".equals(EBISystem.gui().getFileToPath())
                ? title : EBISystem.gui().getFileToPath());

        this.registeredTabs.add((this.jTabbedPane.getTabCount() - 1), new String[]{component.getName(), title});

        return (this.jTabbedPane.getTabCount() - 1);
    }

    /**
     * Add scrollable component to container
     *
     * @return index
     */
    @Override
    public int addScrollableContainer(final String title, final JComponent component, final ImageIcon icon, final int mnemo_key) {
        component.setPreferredSize(new Dimension(component.getWidth(), component.getHeight()));
        jscrollPane = new JScrollPane();
        jscrollPane.setViewportView(component);
        jscrollPane.getVerticalScrollBar().setUnitIncrement(150);
        jTabbedPane.addTab(title, icon, jscrollPane);

        if (ebiMain.getSize().width < 1100 && ebiMain.getSize().height < 800) {
            if (component.getComponentListeners().length > 1 && component.getComponentListeners()[1] != null) {
                component.getComponentListeners()[1].componentResized(new ComponentEvent(component, ComponentEvent.COMPONENT_RESIZED));
            }
        }

        if (mnemo_key != NON_MNEMO) {
            this.jTabbedPane.setMnemonicAt((this.jTabbedPane.getTabCount() - 1), mnemo_key);
        }

        // Used for the GUIDesigner to identify the selected xml component
        final int index = this.jTabbedPane.getTabCount() - 1 == -1 ? 0 : this.jTabbedPane.getTabCount() - 1;
        EBISystem.gui().getHashTabtoFile().put(index,
                "".equals(EBISystem.gui().getFileToPath())
                ? title : EBISystem.gui().getFileToPath());

        this.registeredTabs.add(index, new String[]{component.getName(), title});

        return index;
    }

    /**
     * Add scrollable and closable component to container
     *
     * @return index
     */
    @Override
    public int addScrollableClosableContainer(final String title, final JComponent component, final ImageIcon icon, final int mnemo_key, final CloseableTabbedPaneListener l) {

        component.setPreferredSize(new Dimension(component.getWidth(), component.getHeight()));
        jscrollPane = new JScrollPane();
        jscrollPane.getVerticalScrollBar().setUnitIncrement(150);
        jscrollPane.setViewportView(component);
        jscrollPane.setPreferredSize(new Dimension(component.getWidth(), component.getHeight()));
        jTabbedPane.addTab(title, icon, jscrollPane);

        if (mnemo_key != NON_MNEMO) {
            this.jTabbedPane.setMnemonicAt((this.jTabbedPane.getTabCount() - 1), mnemo_key);
        }

        // Used for the GUIDesigner to identify the selected component xml file
        EBISystem.gui().getHashTabtoFile().put((this.jTabbedPane.getTabCount() - 1),
                "".equals(EBISystem.gui().getFileToPath())
                ? title : EBISystem.gui().getFileToPath());

        this.registeredTabs.add((this.jTabbedPane.getTabCount() - 1), new String[]{component.getName(), title});

        return (this.jTabbedPane.getTabCount() - 1);
    }

    @Override
    public int getIndexByTitle(final String title) {
        int index = -1;

        for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
            final String tlt = jTabbedPane.getTitleAt(i);
            if (tlt.equals(title)) {
                index = i;
                break;
            }
        }

        return index;

    }

    @Override
    public void removeContainer(final int index) {
        try {
            EBISystem.gui().removeFileFromTab(index);
            this.jTabbedPane.remove(index);

            for (int i = index - 1; i > 1; i--) {
                if (this.jTabbedPane.isEnabledAt(i)) {
                    this.jTabbedPane.setSelectedIndex(i);
                    break;
                }
            }

            this.registeredTabs.remove(index);

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeAllFromContainer() {
        try {
            this.jTabbedPane.removeAll();
            this.registeredTabs.clear();
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
        }
    }

    @Override
    public int getSelectedTab() {
        return this.jTabbedPane.getSelectedIndex();
    }

    @Override
    public void setSelectedTab(final int index) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (index <= getTabCount() && jTabbedPane.getTabCount() > 0) {
                        jTabbedPane.setSelectedIndex(index);
                    }
                }
            });

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getComponentName(final int index) {
        String componentName = "";
        if (registeredTabs.get(index) != null) {
            String[] cmpObj = ((String[]) registeredTabs.get(index));
            componentName = cmpObj[0] != null ? cmpObj[0] : "";
        }
        return componentName;
    }

    @Override
    public int getTabCount() {
        return this.jTabbedPane.getTabCount();
    }

    public List<Object> getRegisteredTab() {
        return this.registeredTabs;
    }

    @Override
    public JTabbedPane getTabInstance() {
        return jTabbedPane;
    }
}
