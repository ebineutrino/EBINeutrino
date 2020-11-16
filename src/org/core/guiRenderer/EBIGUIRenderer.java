package org.core.guiRenderer;

import org.core.gui.component.EBIButton;
import org.core.EBIMain;
import org.core.gui.component.EBITableExt;
import org.core.gui.lookandfeel.MoodyBlueTheme;
import org.core.reflection.EBIReflect;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.gui.dialogs.EBIDialog;
import org.sdk.gui.dialogs.EBIDialogExt;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIGUIRenderer;
import org.sdk.utils.EBIAbstractTableModel;
import org.sdk.utils.EBIPropertiesDialogRW;
import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.core.gui.component.EBITextfield;
import org.modules.utils.EBICRMDynamicFunctionalityMethods;
import org.modules.views.dialogs.EBIDialogInternalNumberAdministration;
import org.modules.views.dialogs.EBIDialogTaxAdministration;
import org.sdk.gui.dialogs.EBIDialogValueSetter;

public final class EBIGUIRenderer implements IEBIGUIRenderer {

    private EBIMain ebiMain = null;
    public TreeMap<String, Object> toToolbar = null;
    public TreeMap<String, EBIGUIWidgetsBean> componentsTable = null;
    public TreeMap<String, List<Object>> resizeContainer = null;
    private TreeMap<String, HashMap<String, EBIGUIScripts>> scriptContainer = null;
    public TreeMap<Integer, String> tabtoFile = new TreeMap<Integer, String>();
    public TreeMap<Integer, String> tabToComponentNamespaces = new TreeMap<Integer, String>();
    private TreeMap<String, Integer> componentGet = null;
    private String componentNamespace = "";
    private boolean isInit = false;
    public String fileToTabPath = "";
    public boolean canShow = true;
    private EBIFocusTraversalPolicy focusTraversal = null;
    private int projectCount = 0;
    private int projectCountEnabled = 0;
    private GroovyScriptEngine gse = null;
    private Binding binding = null;
    private boolean observeChanges = false;
    private JPopupMenu lastPopup = null;

    private String resourcePath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator;

    public EBIGUIRenderer(final EBIMain main) {
        ebiMain = main;
        toToolbar = new TreeMap();
        focusTraversal = new EBIFocusTraversalPolicy();
        componentGet = new TreeMap();
        binding = new Binding();
        binding.setVariable("system", EBISystem.getInstance());
        try {
            gse = new GroovyScriptEngine(new String[]{resourcePath + "views/"});
        } catch (IOException ex) {
            Logger.getLogger(EBIGUIRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public final void init() {
        componentsTable = new TreeMap<String, EBIGUIWidgetsBean>();
        toToolbar = new TreeMap<String, Object>();
        resizeContainer = new TreeMap<String, List<Object>>();
        scriptContainer = new TreeMap<String, HashMap<String, EBIGUIScripts>>();
        componentGet = new TreeMap<String, Integer>();
        focusTraversal = new EBIFocusTraversalPolicy();
        fileToTabPath = "";
        isInit = true;
        projectCount = 0;
        projectCountEnabled = 0;
    }

    @Override
    public final void loadProject(final String path) {

        final EBIXMLGUIReader xmlGui1 = new EBIXMLGUIReader();
        xmlGui1.setXmlPath("views/" + path);
        init();
        if (xmlGui1.loadXMLGUI()) {

            final Iterator ix = xmlGui1.getCompObjects().getSubWidgets().iterator();

            while (ix.hasNext()) {
                final EBIGUIWidgetsBean bx = (EBIGUIWidgetsBean) ix.next();
                if (bx.getType().toLowerCase().equals("includetoolbar")) {
                    if (EBISystem.getUserRight().isAdministrator()) {
                        addXMLToolBarGUI(bx);
                    } else if (EBISystem.registeredModule.contains(bx.getPath())) {
                        addXMLToolBarGUI(bx);
                    }
                } else if ("codecontrol".equals(bx.getType().toLowerCase())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            addScriptBean(bx.getName(), bx.getPath(), bx.getName(), bx.getClassName(), "Startup");
                            excScript("Startup", null);
                        }
                    }).start();
                } else {
                    if (EBISystem.getUserRight().isAdministrator()) {
                        addXMLGUI(bx, path);
                    } else if (EBISystem.registeredModule.contains(bx.getPath())) {
                        addXMLGUI(bx, path);
                    }
                }
            }
        } else {
            canShow = false;
        }
    }

    private void addXMLToolBarGUI(final EBIGUIWidgetsBean bx) {
        if (!"".equals(bx.getPath())) {
            loadGUIExt("views/" + bx.getPath());
            showToolBar(bx.getName(), true);
        }
        canShow = true;
    }

    private void addXMLGUI(final EBIGUIWidgetsBean bx, final String path) {
        if (!"".equals(bx.getPath())) {
            if (bx.getType().toLowerCase().equals("visualpanel")) {
                fileToTabPath = path;
            }

            loadGUI(bx.getPath());
            showGUI();

            if (bx.isEnabled()) {
                projectCountEnabled++;
            }
            projectCount++;
            fileToTabPath = "";
        }
    }

    @Override
    public final String loadGUI(final String path) {
        if (EBISystem.getUserRight().isAdministrator()) {
            loadGUIExt("views/" + path);
            canShow = true;
        } else if (EBISystem.registeredModule.contains(path)) {
            loadGUIExt("views/" + path);
            canShow = true;
        }
        return componentNamespace;
    }

    @Override
    public final void removeGUIObject(final String strID) {
        if (componentsTable.containsKey(strID)) {
            componentsTable.remove(strID);
        }
        if (resizeContainer.containsKey(strID)) {
            resizeContainer.remove(strID);
        }
        if (scriptContainer.containsKey(strID)) {
            scriptContainer.remove(strID);
        }
    }

    public final void loadGUIExt(final String path) {
        componentNamespace = "";

        final EBIXMLGUIReader xmlGui = new EBIXMLGUIReader();
        xmlGui.setXmlPath(path);

        if (xmlGui.loadXMLGUI()) {
            componentNamespace = xmlGui.getCompObjects().getName();

            if (!isInit) {
                removeGUIObject(componentNamespace);
            }

            focusTraversal = new EBIFocusTraversalPolicy();
            componentsTable.put(componentNamespace, xmlGui.getCompObjects());

            final List<Object> toResize = new ArrayList<Object>();
            resizeContainer.put(componentNamespace, toResize);

            if (!"ebibar".equals(xmlGui.getCompObjects().getType().toLowerCase())) {
                renderGUI(null, null);
                if (xmlGui.getCompObjects().getType().toLowerCase().equals("visualpanel")) {
                    fileToTabPath = path;
                }
            }

        } else {
            canShow = false;
        }
    }

    public final void renderToolbar(final boolean isVisualPanel) {

        final EBIGUIWidgetsBean list = componentsTable.get(componentNamespace);
        final Iterator iter = list.getSubWidgets().iterator();

        while (iter.hasNext()) {

            final EBIGUIWidgetsBean bean = (EBIGUIWidgetsBean) iter.next();

            if ("codecontrol".equals(bean.getType().toLowerCase())) {

                addScriptBean(bean.getName(), bean.getPath(), bean.getName(), bean.getClassName(), componentNamespace);

            } else if ("toolbar".equals(bean.getType())) {

                final EBIGUIToolbar bar = new EBIGUIToolbar();
                bar.setName(bean.getName());

                if (bean.getSubWidgets().size() > 0) {

                    final Iterator it = bean.getSubWidgets().iterator();

                    while (it.hasNext()) {

                        final EBIGUIWidgetsBean sub = (EBIGUIWidgetsBean) it.next();
                        final EBIGUIToolbar br = new EBIGUIToolbar();

                        if (!"".equals(sub.getI18NToolTip())) {
                            br.setToolTip(EBISystem.i18n(sub.getI18NToolTip()));
                        }
                        br.setKeyStroke(sub.getKeyStroke());
                        br.setName(sub.getName());
                        br.setTitle(sub.getTitle());
                        br.setIcon(sub.getIcon());
                        br.setType(sub.getType());
                        bar.setBarItem(br);
                    }
                }
                bar.setVisualPanel(isVisualPanel);
                toToolbar.put(bean.getName(), bar);
            }
        }
    }

    /**
     * Create and Show components readed from a xml file to a container
     *
     * @param obj
     * @param tmpList
     * @return
     */
    public final void renderGUI(final Object obj, final List<EBIGUIWidgetsBean> tmpList) {

        final EBIGUIWidgetsBean list = componentsTable.get(componentNamespace);
        EBIVisualPanelTemplate panel = null;
        EBIDialogExt dialog = null;
        boolean isVisualPanel = false;

        if (obj == null) {

            panel = new EBIVisualPanelTemplate(!"dialog".equals(list.getType()));
            if ("visualpanel".equals(list.getType())) {
                panel.initialize();
            }

            panel.setName(componentNamespace);

            String title;
            if (list.getTitle().indexOf("EBI_LANG") != -1) {
                title = EBISystem.i18n(list.getTitle());
            } else {
                title = list.getTitle();
            }

            panel.setModuleTitle(title);

            if (list.getIcon() == null) {
                panel.setModuleIcon(EBISystem.getInstance().getIconResource("appIcon.png"));
            } else {
                panel.setModuleIcon(list.getIcon());
            }

            panel.setShowTimer(list.isShowTimer());
            panel.setSize(list.getDimension());
            panel.setBorder(BorderFactory.createEmptyBorder());

            if (!"".equals(list.getMappedBean())) {
                EBIReflect.getInstance().mapBean(list.getMappedBean());
            }

            if (!list.isVisualProperties()) {
                panel.setChangePropertiesVisible(false);
            }

            if ("visualpanel".equals(list.getType())) {
                isVisualPanel = true;
                observeChanges = true;
                panel.initialize();
            } else if ("dialog".equals(list.getType())) {

                dialog = new EBIDialogExt(ebiMain, EBIPropertiesDialogRW.getProperties());
                dialog.setName(componentNamespace);
                panel.setVisualPanel(false);
                dialog.storeLocation(true);
                dialog.setModal(list.isModal());
                dialog.setResizable(list.isResizable());
                dialog.storeSize(list.isResizable());
                dialog.setTitle(title);
                if (list.getIcon() != null) {
                    dialog.setIconImage(list.getIcon().getImage());
                }

                dialog.setLocation(list.getPoint());
                dialog.setSize(list.getDimension().width, list.getDimension().height + 30);
                isVisualPanel = false;
                observeChanges = false;
            }
        }

        // check if recursion
        Iterator iter;
        if (obj == null) {
            iter = list.getSubWidgets().iterator();
        } else {
            iter = tmpList.iterator();
        }

        // parse component from Widgetbeans
        while (iter.hasNext()) {
            final EBIGUIWidgetsBean bean = (EBIGUIWidgetsBean) iter.next();

            final EBIGUINBean res = new EBIGUINBean();
            res.setResizeHight(bean.isHeightAutoResize());
            res.setResizeWidth(bean.isWidthAutoResize());
            res.setPercentHeight(bean.getDimension().height);
            res.setPercentWidth(bean.getDimension().width);
            res.setImageName(bean.getImage().toString());
            res.setFitByResize(bean.isFitOnResize());
            res.setName(bean.getName());

            resizeContainer.get(componentNamespace).add(res);
            final String nameSpace = componentNamespace + "." + res.getName();
            componentGet.put(nameSpace, resizeContainer.get(componentNamespace).size());
            bean.setComponent(res);
            res.setBean(bean);

            if ("button".equals(bean.getType())) {

                final EBIButton button = new EBIButton();
                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    button.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    button.setText(bean.getTitle());
                }

                if (bean.getIcon() != null) {
                    button.setIcon(bean.getIcon());
                }
                button.setName(nameSpace);
                button.setEnabled(bean.isEnabled());

                if (!"".equals(bean.getActionListener())) {
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String action = EBISystem.gui().getComponentWidgetBean(((EBIButton) actionEvent.getSource()).getName()).getActionListener();
                            EBIReflect.getInstance().reflectAction(action);
                        }
                    });
                }

                if (!bean.isVisible()) {
                    button.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    button.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        button.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JButton) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), button);
                }

                button.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setComponent(button);

                if (obj == null) {
                    panel.add(button, null);
                } else {
                    ((JPanel) obj).add(button, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("list".equals(bean.getType())) {
                final JList jlist = new JList();
                jlist.setEnabled(bean.isEnabled());
                jlist.setName(nameSpace);
                final JScrollPane scr = new JScrollPane();
                scr.setViewportView(jlist);
                scr.setName(nameSpace);
                scr.addMouseWheelListener(new EBICustomMouseWheelListener(scr));

                if (!bean.isVisible()) {
                    jlist.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    jlist.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        // if index = 1 than set the default focus "workaroud"
                        jlist.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JList) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), jlist);
                }

                scr.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setScrollComponent(jlist);
                res.setComponent(scr);

                if (obj == null) {
                    panel.add(scr, null);
                } else {
                    ((JPanel) obj).add(scr, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("textfield".equals(bean.getType())) {

                final EBITextfield textField = new EBITextfield();
                addUndoManager(textField.getDocument(), textField);

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    textField.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    textField.setText(bean.getTitle());
                }

                if (!"".equals(bean.getPlaceHolder())) {
                    if (bean.getPlaceHolder().indexOf("EBI_LANG") != -1) {
                        textField.setPlaceHolder(EBISystem.i18n(bean.getPlaceHolder()));
                    } else {
                        textField.setPlaceHolder(bean.getPlaceHolder());
                    }
                }

                if (observeChanges) {
                    this.addComponentTextChanged(textField);
                }
                textField.setName(nameSpace);
                textField.setEnabled(bean.isEnabled());

                if (!bean.isVisible()) {
                    textField.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    textField.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        textField.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JTextField) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), textField);
                }

                textField.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setComponent(textField);

                if (obj == null) {
                    panel.add(textField, null);
                } else {
                    ((JPanel) obj).add(textField, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("formattedtextfield".equals(bean.getType())) {
                final JFormattedTextField textField = new JFormattedTextField();
                addUndoManager(textField.getDocument(), textField);

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    textField.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    textField.setText(bean.getTitle());
                }
                if (observeChanges) {
                    this.addComponentTextChanged(textField);
                }
                textField.setName(nameSpace);
                textField.setEnabled(bean.isEnabled());

                if (!bean.isVisible()) {
                    textField.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    textField.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        textField.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JFormattedTextField) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), textField);
                }

                textField.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setComponent(textField);

                if (obj == null) {
                    panel.add(textField, null);
                } else {
                    ((JPanel) obj).add(textField, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("combobox".equals(bean.getType())) {

                final JComboBox combo = new JComboBox();
                combo.setName(nameSpace);
                combo.setEnabled(bean.isEnabled());
                addUndoManager(((JTextField) combo.getEditor().getEditorComponent()).getDocument(), combo);

                final Border line = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
                final Border empty = new EmptyBorder(0, 5, 0, 0);
                final CompoundBorder border = new CompoundBorder(line, empty);

                if (!"".equals(bean.getPropertyBinding()) || bean.isInternalNumberAdmin() || bean.isAutoIncrementalNr() || bean.isTaxAdministration()) {
                    JPopupMenu popupmenu = new JPopupMenu("Combo Settings");

                    if (!"".equals(bean.getPropertyBinding())) {
                        JMenuItem settings = new JMenuItem(EBISystem.i18n("EBI_LANG_ADMINISTRATION"));
                        settings.setName(bean.getPropertyBinding());
                        settings.setIcon(EBISystem.getInstance().getIconResource("settings_small.png"));
                        settings.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JMenuItem ec = (JMenuItem) e.getSource();
                                new EBIDialogValueSetter(ec.getName()).setVisible();
                                EBICRMDynamicFunctionalityMethods.getInstance().initComboBoxes(true);
                            }
                        });
                        popupmenu.add(settings);
                    }

                    if (bean.isAutoIncrementalNr()) {
                        JMenuItem autoIncItem = new JMenuItem(EBISystem.i18n("EBI_LANG_INVOICE_AUTOINC_NR"));
                        autoIncItem.setIcon(EBISystem.getInstance().getIconResource("incremental_small.png"));
                        autoIncItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new EBIDialogInternalNumberAdministration(true).setVisible();
                                EBICRMDynamicFunctionalityMethods.getInstance().initComboBoxes(true);
                            }
                        });
                        popupmenu.add(autoIncItem);
                    }

                    if (bean.isInternalNumberAdmin()) {
                        JMenuItem internalNrItem = new JMenuItem(EBISystem.i18n("EBI_LANG_C_CRM_INTERNAL_NUMBER_SETTINGS"));
                        internalNrItem.setIcon(EBISystem.getInstance().getIconResource("incremental_small.png"));
                        internalNrItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new EBIDialogInternalNumberAdministration(false).setVisible();
                                EBICRMDynamicFunctionalityMethods.getInstance().initComboBoxes(true);
                            }
                        });
                        popupmenu.add(internalNrItem);
                    }

                    if (bean.isTaxAdministration()) {
                        JMenuItem taxAdminItem = new JMenuItem(EBISystem.i18n("EBI_LANG_C_CRM_TAX_ADMINISTRATION"));
                        taxAdminItem.setIcon(EBISystem.getInstance().getIconResource("incremental_small.png"));
                        taxAdminItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                new EBIDialogTaxAdministration().setVisible();
                                EBICRMDynamicFunctionalityMethods.getInstance().initComboBoxes(true);
                            }
                        });
                        popupmenu.add(taxAdminItem);
                    }
                    combo.setComponentPopupMenu(popupmenu);
                }

                ((JTextField) combo.getEditor().getEditorComponent()).addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (lastPopup != null) {
                            if (lastPopup.isVisible()) {
                                lastPopup.setVisible(false);
                                lastPopup = null;
                                 ((JTextField) e.getSource()).requestFocus();
                            }
                        }
                       
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        lastPopup = ((JTextField) e.getSource()).getComponentPopupMenu();
                    }
                });

                ((JTextField) combo.getEditor().getEditorComponent()).setBorder(border);
                ((JTextField) combo.getEditor().getEditorComponent()).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean canAdd = true;
                        JComboBox combo = ((JComboBox) ((JTextField) e.getSource()).getParent());
                        EBIGUIWidgetsBean bean = getComponentWidgetBean(combo.getName());
                        String textToAdd = ((JTextField) e.getSource()).getText();

                        if (!"".equals(textToAdd.trim()) && !"".equals(bean.getBind())) {
                            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo.getModel();
                            for (int i = 0; i < model.getSize(); i++) {
                                if (model.getElementAt(i).equals(textToAdd)) {
                                    canAdd = false;
                                    break;
                                }
                            }
                            if (canAdd) {
                                model.addElement(textToAdd);
                                model.setSelectedItem(textToAdd);
                                dbSave(bean.getBind(), textToAdd);
                            }
                        }
                    }
                });

                combo.setEditable(true);
                if (observeChanges) {
                    this.addComponentTextChanged((JComponent) combo.getEditor().getEditorComponent());
                }

                if (!bean.isVisible()) {
                    combo.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    combo.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        combo.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JComboBox) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), combo);
                }

                combo.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));
                res.setComponent(combo);

                if (obj == null) {
                    panel.add(combo, null);
                } else {
                    ((JPanel) obj).add(combo, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("checkbox".equals(bean.getType())) {

                final JCheckBox box = new JCheckBox();

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    box.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    box.setText(bean.getTitle());
                }

                box.setName(nameSpace);
                box.setEnabled(bean.isEnabled());

                if (!bean.isVisible()) {
                    box.setVisible(bean.isVisible());
                }

                if (observeChanges) {
                    this.addComponentTextChanged(box);
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    box.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        box.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JCheckBox) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), box);
                }

                box.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setComponent(box);

                if (obj == null) {
                    panel.add(box, null);
                } else {
                    ((JPanel) obj).add(box, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("radiobutton".equals(bean.getType())) {

                final JRadioButton radio = new JRadioButton();

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    radio.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    radio.setText(bean.getTitle());
                }
                if (observeChanges) {
                    this.addComponentTextChanged(radio);
                }
                radio.setName(nameSpace);
                radio.setEnabled(bean.isEnabled());

                if (!"".equals(bean.getI18NToolTip())) {
                    radio.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (!bean.isVisible()) {
                    radio.setVisible(bean.isVisible());
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        radio.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JRadioButton) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), radio);
                }

                radio.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setComponent(radio);

                if (obj == null) {
                    panel.add(radio, null);
                } else {
                    ((JPanel) obj).add(radio, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("textarea".equals(bean.getType())) {

                final JTextArea textArea = new JTextArea();
                if (observeChanges) {
                    this.addComponentTextChanged(textArea);
                }
                addUndoManager(textArea.getDocument(), textArea);
                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    textArea.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    textArea.setText(bean.getTitle());
                }

                textArea.setName(nameSpace);
                textArea.setEnabled(bean.isEnabled());

                if (!"".equals(bean.getI18NToolTip())) {
                    textArea.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        textArea.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JTextArea) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), textArea);
                }

                final JScrollPane scr = new JScrollPane();
                scr.addMouseWheelListener(new EBICustomMouseWheelListener(scr));
                scr.setViewportView(textArea);
                scr.setName(bean.getName());

                if (!bean.isVisible()) {
                    scr.setVisible(bean.isVisible());
                }

                scr.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setScrollComponent(textArea);
                res.setComponent(scr);

                if (obj == null) {
                    panel.add(scr, null);
                } else {
                    ((JPanel) obj).add(scr, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("editor".equals(bean.getType())) {
                final JEditorPane editPane = new JEditorPane();

                if (observeChanges) {
                    this.addComponentTextChanged(editPane);
                }

                addUndoManager(editPane.getDocument(), editPane);

                editPane.setContentType("text/html");

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    editPane.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    editPane.setText(bean.getTitle());
                }

                StyleSheet sheet = ((HTMLEditorKit) editPane.getEditorKit()).getStyleSheet();
                sheet.addRule("body {color:#ffffff;font-family:'san serif'; font-size:10px;}");

                editPane.setForeground(MoodyBlueTheme.editorForeground);

                editPane.setMargin(new Insets(10, 10, 10, 10));
                editPane.setName(nameSpace);
                editPane.setEnabled(bean.isEnabled());

                if (!"".equals(bean.getI18NToolTip())) {
                    editPane.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        editPane.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JTextArea) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), editPane);
                }

                final JScrollPane scr = new JScrollPane();
                scr.setViewportView(editPane);
                scr.setName(nameSpace);
                scr.addMouseWheelListener(new EBICustomMouseWheelListener(scr));

                if (!bean.isVisible()) {
                    scr.setVisible(bean.isVisible());
                }

                scr.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setScrollComponent(editPane);
                res.setComponent(scr);

                if (obj == null) {
                    panel.add(scr, null);
                } else {
                    ((JPanel) obj).add(scr, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("label".equals(bean.getType())) {
                final JLabel label = new JLabel();

                if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                    label.setText(EBISystem.i18n(bean.getTitle()));
                } else {
                    label.setText(bean.getTitle());
                }

                label.setName(nameSpace);
                label.setFont(new Font("San Serif", Font.PLAIN, 11));

                if (!bean.isVisible()) {
                    label.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    label.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                label.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setComponent(label);

                if (obj == null) {
                    panel.add(label, null);
                } else {
                    ((JPanel) obj).add(label, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("table".equals(bean.getType())) {
                final EBITableExt jtable = new EBITableExt();
                jtable.setName(nameSpace);
                jtable.getTableHeader().setReorderingAllowed(false);
                jtable.setAutoCreateRowSorter(true);
                jtable.setEnabled(bean.isEnabled());

                jtable.setRowHeight(25);

                if (!"".equals(bean.getI18NToolTip())) {
                    jtable.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                final JScrollPane scr = new JScrollPane();
                scr.setName(bean.getName());
                scr.setViewportView(jtable);
                scr.addMouseWheelListener(new EBICustomMouseWheelListener(scr));

                if (!bean.isVisible()) {
                    scr.setVisible(bean.isVisible());
                }

                if (bean.getTabIndex() != -1) {
                    focusTraversal.addComponent(bean.getTabIndex(), jtable);
                }

                scr.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                final EBIAbstractTableModel abstractModel = new EBIAbstractTableModel();
                jtable.setModel(abstractModel);

                if (bean.getSubWidgets().size() > 0) {
                    final Iterator modIT = bean.getSubWidgets().iterator();

                    abstractModel.data = new Object[1][bean.getSubWidgets().size() + 1];
                    abstractModel.columnNames = new String[bean.getSubWidgets().size()];
                    int i = 0;

                    while (modIT.hasNext()) {
                        final EBIGUIWidgetsBean colBean = (EBIGUIWidgetsBean) modIT.next();

                        if (colBean.getTitle().indexOf("EBI_LANG") != -1) {
                            abstractModel.columnNames[i] = EBISystem
                                    .i18n(colBean.getTitle() == null ? "" : colBean.getTitle());
                        } else {
                            abstractModel.columnNames[i] = colBean.getTitle() == null ? "" : colBean.getTitle();
                        }
                        abstractModel.data[0][i] = "";
                        i++;
                    }
                    abstractModel.data[0][0] = EBISystem.i18n("EBI_LANG_PLEASE_SELECT");
                    abstractModel.fireTableStructureChanged();
                }

                res.setScrollComponent(jtable);
                res.setComponent(scr);

                if (obj == null) {
                    panel.add(scr, null);
                } else {
                    ((JPanel) obj).add(scr, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("treetable".equals(bean.getType())) {
                final JXTreeTable jtable = new JXTreeTable();
                jtable.setName(nameSpace);
                jtable.setEnabled(bean.isEnabled());
                jtable.setRowHeight(25);

                if (!"".equals(bean.getI18NToolTip())) {
                    jtable.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        jtable.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JXTreeTable) e.getSource()).requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), jtable);
                }

                final JScrollPane scr = new JScrollPane();
                scr.setName(bean.getName());
                scr.setViewportView(jtable);
                scr.addMouseWheelListener(new EBICustomMouseWheelListener(scr));

                if (!bean.isVisible()) {
                    scr.setVisible(bean.isVisible());
                }

                scr.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));

                res.setScrollComponent(jtable);
                res.setComponent(scr);

                if (obj == null) {
                    panel.add(scr, null);
                } else {
                    ((JPanel) obj).add(scr, null);
                    res.setParentComponent((JComponent) obj);
                }

            } else if ("timepicker".equals(bean.getType())) {

                final JXDatePicker timePicker = new JXDatePicker();
                timePicker.setName(nameSpace);
                timePicker.setEnabled(bean.isEnabled());
                timePicker.setFormats(EBISystem.DateFormat);

                final Border line = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
                final Border empty = new EmptyBorder(0, 5, 0, 0);
                final CompoundBorder border = new CompoundBorder(line, empty);

                timePicker.getEditor().setBorder(border);
                if (observeChanges) {
                    this.addComponentTextChanged(timePicker.getEditor());
                }
                timePicker.getLinkPanel().setVisible(false);
                timePicker.getMonthView().setDayForeground(1, Color.red);
                timePicker.getMonthView().setFirstDayOfWeek(1);
                timePicker.getMonthView().setDaysOfTheWeekForeground(new Color(200, 200, 200));

                timePicker.getMonthView().setBackground(new Color(41, 47, 54));
                timePicker.getMonthView().setTodayBackground(new Color(34, 34, 34));
                timePicker.getMonthView().setSelectionBackground(new Color(34, 34, 34));
                timePicker.getMonthView().setForeground(new Color(200, 200, 200));

                if (!"".equals(bean.getI18NToolTip())) {
                    timePicker.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (!bean.isVisible()) {
                    timePicker.setVisible(bean.isVisible());
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        timePicker.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JXDatePicker) e.getSource()).getEditor().requestFocus();
                            }
                        });
                    }

                    timePicker.getEditor().setFocusCycleRoot(true);
                    timePicker.getEditor().setFocusTraversalKeysEnabled(true);
                    timePicker.setFocusCycleRoot(true);
                    timePicker.setFocusTraversalKeysEnabled(true);

                    timePicker.setFocusable(true);
                    timePicker.getEditor().setFocusable(true);
                    focusTraversal.addComponent(bean.getTabIndex(), timePicker.getEditor());
                }

                timePicker.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width, bean.getDimension().height));
                res.setComponent(timePicker);

                if (obj == null) {
                    panel.add(timePicker, null);
                } else {
                    ((JPanel) obj).add(timePicker, null);
                }

            } else if ("spinner".equals(bean.getType())) {

                final JSpinner spinner = new JSpinner();
                spinner.setEnabled(bean.isEnabled());

                final SpinnerNumberModel model = new SpinnerNumberModel();
                model.setMaximum(bean.getMax());
                model.setMinimum(bean.getMin());

                if (observeChanges) {
                    this.addComponentTextChanged(spinner.getEditor());
                }
                if (!bean.isVisible()) {
                    spinner.setVisible(bean.isVisible());
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    spinner.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (bean.getTabIndex() != -1) {
                    if (bean.getTabIndex() == 1) {
                        spinner.addComponentListener(new ComponentAdapter() {
                            @Override
                            public final void componentResized(final ComponentEvent e) {
                                ((JSpinner) e.getSource()).getEditor().requestFocus();
                            }
                        });
                    }
                    focusTraversal.addComponent(bean.getTabIndex(), spinner);
                }

                spinner.setBorder(null);

                final Component[] cmps = spinner.getComponents();
                for (int i = 0; i < cmps.length; i++) {
                    if (cmps[i] instanceof JButton) {
                        spinner.remove(cmps[i]);
                        // cmps[i].setPreferredSize(new Dimension(1,1));
                    }
                }

                spinner.setName(nameSpace);
                spinner.setModel(model);
                spinner.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setComponent(spinner);

                if (obj == null) {
                    panel.add(spinner, null);
                } else {
                    ((JPanel) obj).add(spinner, null);
                }

            } else if ("progressbar".equals(bean.getType())) {
                final JProgressBar progressBar = new JProgressBar();
                progressBar.setName(nameSpace);
                progressBar.setEnabled(bean.isEnabled());

                if (!"".equals(bean.getI18NToolTip())) {
                    progressBar.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                if (!bean.isVisible()) {
                    progressBar.setVisible(bean.isVisible());
                }

                progressBar.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setComponent(progressBar);

                if (obj == null) {
                    panel.add(progressBar, null);
                } else {
                    ((JPanel) obj).add(progressBar, null);
                }

            } else if ("panel".equals(bean.getType())) {

                final JPanel p = new JPanel();
                p.setName(nameSpace);
                p.setOpaque(false);
                p.setLayout(null);
                String title = "";
                if (!"".equals(bean.getTitle())) {
                    if (bean.getTitle().indexOf("EBI_LANG") != -1) {
                        title = EBISystem.i18n(bean.getTitle());
                    } else {
                        title = bean.getTitle();
                    }
                    final Border blackline = BorderFactory.createLineBorder(new Color(34, 34, 34));
                    final TitledBorder tltB = BorderFactory.createTitledBorder(blackline, title,
                            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                            new Font("Tahoma", Font.PLAIN, 12), new Color(120, 120, 120));
                    p.setBorder(tltB);
                }

                if (!"".equals(bean.getI18NToolTip())) {
                    p.setToolTipText(EBISystem.i18n(bean.getI18NToolTip()));
                }

                p.setVisible(true);
                p.setBounds(new Rectangle(bean.getPoint().x, bean.getPoint().y, bean.getDimension().width,
                        bean.getDimension().height));

                res.setComponent(p);

                if (obj == null) {
                    panel.add(p, null);
                } else {
                    ((JPanel) obj).add(p);
                    res.setParentComponent((JComponent) obj);
                }

                if (bean.getSubWidgets().size() > 0) {
                    renderGUI(p, bean.getSubWidgets());
                }
            } else if ("codecontrol".equals(bean.getType())) {
                addScriptBean(bean.getName(), bean.getPath(), bean.getName(), bean.getClassName(), componentNamespace);
            }
        }

        if (obj == null) {
            if (isVisualPanel) {
                list.setComponent(panel);
            } else {
                dialog.setContentPane(panel);
                list.setComponent(dialog);
            }

            if (!focusTraversal.isEmpty()) {
                panel.setFocusTraversalKeysEnabled(true);
                panel.setFocusTraversalPolicy(focusTraversal);
            }
            setResizeListener();
        }
    }

    @Override
    public final void addScriptBean(final String type, final String path, final String name, final String clsName, final String cmpNamespace) {
        if (scriptContainer.get(cmpNamespace) == null || !scriptContainer.get(cmpNamespace).containsKey(name)) {
            final EBIGUIScripts script = new EBIGUIScripts();
            script.setType(type); // groovy or java
            script.setPath(path);
            script.setName(name);
            script.setClassName(clsName);
            final HashMap<String, EBIGUIScripts> toScript = new HashMap<String, EBIGUIScripts>();
            scriptContainer.put(cmpNamespace, toScript);
            scriptContainer.get(cmpNamespace).put(name, script);
        }
    }

    @Override
    public final void showToolBar(final String name, final boolean toVisualPanel) {
        if (canShow) {
            renderToolbar(toVisualPanel);
            initToolbar(name);
            initScript(componentNamespace);
            isInit = false;
        }
    }

    @Override
    public final void showGUI() {
        try {
            if (canShow) {
                if (componentsTable.get(componentNamespace) != null) {

                    final EBIGUIWidgetsBean list = componentsTable.get(componentNamespace);
                    tabToComponentNamespaces.put(ebiMain.container.getTabCount(), componentNamespace);

                    if (list.getComponent() instanceof EBIVisualPanelTemplate) {

                        if (list.isCeckable()) {
                            if (list.getTitle().indexOf("EBI_LANG") != -1) {
                                ebiMain.container.addScrollableClosableContainer(EBISystem.i18n(list.getTitle()),
                                        ((EBIVisualPanelTemplate) list.getComponent()), list.getIcon(),
                                        list.getKeyEvent(), null);
                            } else {
                                ebiMain.container.addScrollableClosableContainer(list.getTitle(),
                                        ((EBIVisualPanelTemplate) list.getComponent()), list.getIcon(),
                                        list.getKeyEvent(), null);
                            }
                        } else {
                            if (list.getTitle().indexOf("EBI_LANG") != -1) {
                                ebiMain.container.addScrollableContainer(EBISystem.i18n(list.getTitle()),
                                        ((EBIVisualPanelTemplate) list.getComponent()), list.getIcon(),
                                        list.getKeyEvent());
                            } else {
                                ebiMain.container.addScrollableContainer(list.getTitle(),
                                        ((EBIVisualPanelTemplate) list.getComponent()), list.getIcon(),
                                        list.getKeyEvent());
                            }
                        }

                    } else if (list.getComponent() instanceof EBIDialog) {
                        ((EBIDialog) list.getComponent()).setVisible(true);
                    }

                    final String cmpNamespace = componentNamespace;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    initScript(cmpNamespace);
                                }
                            });
                        }
                    }).start();

                    isInit = false;
                    fileToTabPath = "";
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initToolbar(final String name) {

        JToolBar toolBar = null;
        JButton button;

        final EBIGUIToolbar bar = ((EBIGUIToolbar) toToolbar.get(name));

        if (!bar.isVisualPanel()) {
            toolBar = new JToolBar();
            bar.setComponent(toolBar);
        } else {
            bar.setComponent(EBISystem.getInstance().getIEBIToolBarInstance().getJToolBar());
            EBISystem.getInstance().getIEBIToolBarInstance().resetToolBar();
        }

        if (bar.getBarItem().size() > 0) {
            final Iterator it = bar.getBarItem().iterator();

            while (it.hasNext()) {
                final EBIGUIToolbar b = (EBIGUIToolbar) it.next();

                if (bar.isVisualPanel()) {
                    if ("separator".equals(b.getType().toLowerCase())) {
                        EBISystem.getInstance().getIEBIToolBarInstance().addButtonSeparator();
                    } else if ("toolbaritem".equals(b.getType().toLowerCase())) {

                        final int NEW_ID = EBISystem.getInstance().getIEBIToolBarInstance().addToolButton(b.getIcon(),
                                null);
                        EBISystem.getInstance().getIEBIToolBarInstance().setComponentToolTipp(NEW_ID,
                                "<html><body><br><b>" + b.getToolTip() + "</b><br><br></body></html>");
                        final JButton bx = ((JButton) (EBISystem.getInstance().getIEBIToolBarInstance()
                                .getToolbarComponent(NEW_ID)));

                        InputMap inputMap;
                        if (b.getKeyStroke() != null) {
                            final Action refreshAction = new AbstractAction() {
                                @Override
                                public final void actionPerformed(final ActionEvent e) {
                                    if (bx.getActionListeners()[0] != null) {
                                        bx.getActionListeners()[0].actionPerformed(e);
                                    }
                                }
                            };
                            inputMap = ((JPanel) ebiMain.getContentPane())
                                    .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                            inputMap.put(b.getKeyStroke(), b.getName() + "Action");
                            ((JPanel) ebiMain.getContentPane()).getActionMap().put(b.getName() + "Action",
                                    refreshAction);
                        }
                        b.setComponent(bx);
                        b.setId(NEW_ID);
                    } else if ("toolbaritemcheckable".equals(b.getType().toLowerCase())) {
                        final JToggleButton check = new JToggleButton(b.getIcon());
                        InputMap inputMap;
                        if (b.getKeyStroke() != null) {
                            final Action refreshAction = new AbstractAction() {
                                @Override
                                public final void actionPerformed(final ActionEvent e) {
                                    if (check.getActionListeners()[0] != null) {
                                        if (check.isSelected() == true) {
                                            check.setSelected(false);
                                        } else {
                                            check.setSelected(true);
                                        }
                                        check.getActionListeners()[0].actionPerformed(e);
                                    }
                                }
                            };
                            inputMap = ((JPanel) ebiMain.getContentPane())
                                    .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                            inputMap.put(b.getKeyStroke(), b.getName() + "Action");
                            ((JPanel) ebiMain.getContentPane()).getActionMap().put(b.getName() + "Action",
                                    refreshAction);
                        }

                        final int NEW_ID = EBISystem.getInstance().getIEBIToolBarInstance()
                                .addCustomToolBarComponent(check);
                        EBISystem.getInstance().getIEBIToolBarInstance().setComponentToolTipp(NEW_ID,
                                "<html><body><br><b>" + b.getToolTip() + "</b><br><br></body></html>");
                        b.setComponent(EBISystem.getInstance().getIEBIToolBarInstance().getToolbarComponent(NEW_ID));
                        b.setId(NEW_ID);
                    }
                } else {
                    if ("separator".equals(b.getType().toLowerCase())) {
                        if (toolBar != null) {
                            toolBar.addSeparator();
                        }
                    } else {
                        button = new JButton();
                        button.setIcon(b.getIcon());
                        button.setToolTipText("<html><body><br><b>" + b.getToolTip() + "</b><br><br></body></html>");
                        b.setComponent(button);
                        if (toolBar != null) {
                            toolBar.add(button);
                        }
                    }
                }
            }
        }
        if (!bar.isVisualPanel()) {
            ((EBIDialog) componentsTable.get(componentNamespace).getComponent()).add(toolBar, BorderLayout.NORTH);
        } else {
            EBISystem.getInstance().getIEBIToolBarInstance().showToolBar(true);
        }
    }

    @Override
    public synchronized final void initScripts() {
        final Iterator itr = scriptContainer.keySet().iterator();
        while (itr.hasNext()) {
            excScript((String) itr.next(), null);
        }
    }

    private synchronized final void initScript(final String namespace) {
        excScript(namespace, null);
    }

    @Override
    public synchronized final void excScript(final String cmpNamespace, final HashMap<String, String> PARAM) {

        if (scriptContainer.get(cmpNamespace) != null) {

            final Iterator<String> iter = scriptContainer.get(cmpNamespace).keySet().iterator();

            while (iter.hasNext()) {

                final EBIGUIScripts script = scriptContainer.get(cmpNamespace).get(iter.next());

                if ("groovy".equals(script.getType())) {
                    /*SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public final void run() {*/
                    try {

                        Iterator ixt;
                        String nmSpace;
                        if (resizeContainer.get(cmpNamespace) == null) {
                            nmSpace = tabToComponentNamespaces.get(ebiMain.container.getSelectedTab());
                            ixt = resizeContainer.get(nmSpace).iterator();
                        } else {
                            nmSpace = cmpNamespace;
                            ixt = resizeContainer.get(cmpNamespace).iterator();
                        }

                        binding.setVariable("namespace", nmSpace);
                        while (ixt.hasNext()) {
                            final EBIGUINBean bx = (EBIGUINBean) ixt.next();
                            JComponent cmp = bx.getComponent();
                            if (bx.getComponent() instanceof JScrollPane) {
                                cmp = bx.getScrollComponent();
                            }
                            binding.setVariable(bx.getName(), cmp);
                        }

                        if (PARAM != null) {
                            final Iterator<String> prmItr = PARAM.keySet().iterator();
                            while (prmItr.hasNext()) {
                                final String key = prmItr.next();
                                binding.setVariable(key, PARAM.get(key));
                            }
                        }

                        gse.run(script.getPath(), binding);
                        final Script scr = gse.createScript(script.getPath(), binding);

                        if (scr.getMetaClass().getMetaMethod("ebiEdit", null) != null
                                || scr.getMetaClass().getMetaMethod("ebiDelete", null) != null
                                || scr.getMetaClass().getMetaMethod("ebiNew", null) != null
                                || scr.getMetaClass().getMetaMethod("ebiSave", null) != null) {

                            EBISystem.getInstance().setDataStore(cmpNamespace, scr);
                        }

                    } catch (final ResourceException ex) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } catch (final ScriptException ex) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } catch (final Exception ex) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.ERROR_MESSAGE);
                        ex.printStackTrace();
                    } catch (final NoClassDefFoundError e) {
                        EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                    //  }
                    //});

                } else if ("java".equals(script.getType())) {

                    // Create a File object on the root of the directory containing the class file
                    final File file = new File("ebiExtensions/" + script.getPath());

                    try {
                        // Convert File to a URL
                        final URL url = file.toURL();
                        final URL[] urls = new URL[]{url};

                        // Create a new class loader with the directory
                        final ClassLoader cl = new URLClassLoader(urls);

                        final Class cls = cl.loadClass(script.getClassName());
                        cls.newInstance();

                    } catch (final MalformedURLException e) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(e))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    } catch (final ClassNotFoundException e) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(e))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    } catch (final InstantiationException e) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(e))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    } catch (final IllegalAccessException e) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(e))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    } catch (final Exception e) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(e))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    } catch (final NoClassDefFoundError e) {
                        EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Add resize listener to the Dialog/VisualPanel
     */
    private final void setResizeListener() {

        final Object obj = componentsTable.get(componentNamespace);
        final ComponentAdapter adt;

        adt = new ComponentAdapter() {

            private int oldWidth = 0;
            private int oldHeight = 0;
            private int calX = 0;
            private int calY = 0;

            private int oldPWidth = 0;
            private int oldPHeight = 0;

            private final int calPX = 0;
            private final int calPY = 0;

            @Override
            public final void componentShown(final java.awt.event.ComponentEvent e) {
                // componentResized(e);
            }

            @Override
            public final void componentResized(final java.awt.event.ComponentEvent e) {
                try {
                    String compName = "";
                    EBIGUINBean res;
                    if (e.getSource() instanceof EBIDialog) {
                        compName = ((EBIDialog) e.getSource()).getName();
                        if (oldWidth > 0) {
                            calX = ((EBIDialog) e.getSource()).getContentPane().getWidth() - oldWidth;
                        } else {
                            calX = ((EBIDialog) e.getSource()).getContentPane().getWidth()
                                    - ((EBIDialog) e.getSource()).getOriginalContentDimension().width;
                        }

                        if (oldHeight > 0) {
                            calY = ((EBIDialog) e.getSource()).getContentPane().getHeight() - oldHeight;
                        } else {
                            calY = ((EBIDialog) e.getSource()).getContentPane().getHeight()
                                    - ((EBIDialog) e.getSource()).getOriginalContentDimension().height;
                        }

                        oldWidth = ((EBIDialog) e.getSource()).getContentPane().getWidth();
                        oldHeight = ((EBIDialog) e.getSource()).getContentPane().getHeight();

                    } else if (e.getSource() instanceof EBIVisualPanelTemplate) {
                        compName = ((EBIVisualPanelTemplate) e.getSource()).getName();
                        oldPWidth = ((EBIVisualPanelTemplate) e.getSource()).getWidth();
                        oldPHeight = ((EBIVisualPanelTemplate) e.getSource()).getHeight();
                    }

                    for (int i = 0; i < resizeContainer.get(compName).size(); i++) {
                        res = (EBIGUINBean) resizeContainer.get(compName).get(i);

                        if (res.getComponent() instanceof JComponent) {

                            if (res.isFitByResize()) {
                                if (e.getSource() instanceof EBIDialog) {
                                    if (res.getParentComponent() == null) {
                                        res.getComponent().setLocation(res.getComponent().getX() + calX,
                                                res.getComponent().getY() + calY);
                                    }
                                }
                            }

                            if (res.isResizeHight()) {
                                if (e.getSource() instanceof EBIVisualPanelTemplate) {
                                    if (res.getParentComponent() != null) {
                                        res.getComponent().setSize(new Dimension(res.getParentComponent().getWidth(),
                                                res.getParentComponent().getHeight() - res.getComponent().getY() - 5));
                                    } else {
                                        res.getComponent().setSize(new Dimension(res.getComponent().getWidth(),
                                                oldPHeight - res.getComponent().getY() - 30));
                                    }
                                } else if (e.getSource() instanceof EBIDialog) {
                                    if (res.getParentComponent() != null) {

                                    } else {
                                        res.getComponent().setSize(new Dimension(res.getComponent().getWidth(),
                                                res.getComponent().getHeight() + calY));
                                    }
                                }
                            }

                            if (res.isResizeWidth()) {
                                if (e.getSource() instanceof EBIVisualPanelTemplate) {
                                    if (res.getParentComponent() != null) {
                                        res.getComponent().setSize(new Dimension(
                                                res.getParentComponent().getWidth() - res.getComponent().getX() - 10,
                                                res.getComponent().getHeight()));
                                    } else {
                                        res.getComponent()
                                                .setSize(new Dimension(oldPWidth - res.getComponent().getX() - 10,
                                                        res.getComponent().getHeight()));
                                    }
                                } else if (e.getSource() instanceof EBIDialog) {
                                    if (res.getParentComponent() != null) {

                                    } else {
                                        res.getComponent().setSize(new Dimension(res.getComponent().getWidth() + calX,
                                                res.getComponent().getHeight()));
                                    }
                                }
                            }
                        }
                    }
                } catch (final NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        };

        if (obj != null) {
            if (((EBIGUIWidgetsBean) obj).getComponent() instanceof EBIVisualPanelTemplate) {
                ((EBIVisualPanelTemplate) ((EBIGUIWidgetsBean) obj).getComponent()).addComponentListener(adt);
            } else if (((EBIGUIWidgetsBean) obj).getComponent() instanceof EBIDialog) {
                ((EBIDialog) ((EBIGUIWidgetsBean) obj).getComponent()).addComponentListener(adt);
            }
        }
    }

    @Override
    public final void addUndoManager(final Document doc, final JComponent comp) {
        final UndoManager undo = new UndoManager();

        doc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public final void undoableEditHappened(final UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        comp.getActionMap().put("Undo", new AbstractAction("Undo") {
            @Override
            public final void actionPerformed(final ActionEvent evt) {
                try {
                    if (undo.canUndo()) {
                        undo.undo();
                    }
                } catch (final CannotUndoException e) {
                }
            }
        });

        comp.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        comp.getActionMap().put("Redo", new AbstractAction("Redo") {
            @Override
            public final void actionPerformed(final ActionEvent evt) {
                try {
                    if (undo.canRedo()) {
                        undo.redo();
                    }
                } catch (final CannotRedoException e) {
                }
            }
        });
        comp.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    @Override
    public final EBIVisualPanelTemplate vpanel(final String Name) {
        EBIVisualPanelTemplate retPanel;

        if (componentsTable.get(Name) != null && componentsTable.get(Name).getComponent() instanceof EBIDialog) {
            retPanel = (EBIVisualPanelTemplate) ((EBIDialog) componentsTable.get(Name).getComponent()).getContentPane();
        } else {
            retPanel = componentsTable.get(Name) == null ? null
                    : ((EBIVisualPanelTemplate) componentsTable.get(Name).getComponent());
        }

        return retPanel;
    }

    @Override
    public final EBIDialog dialog(final String Name) {
        return componentsTable.get(Name) == null ? null : ((EBIDialog) componentsTable.get(Name).getComponent());
    }

    @Override
    public final JToolBar getToolBar(final String name) {

        JToolBar bar = null;

        if (toToolbar.get(name) != null) {
            bar = (JToolBar) ((EBIGUIToolbar) toToolbar.get(name)).getComponent();
        }

        return bar;
    }

    @Override
    public final JComponent getToolBarComponent(final String name, final String packages) {
        JComponent comp = null;

        if (toToolbar.get(packages) != null) {

            final Iterator i = ((EBIGUIToolbar) toToolbar.get(packages)).getBarItem().iterator();
            while (i.hasNext()) {
                final EBIGUIToolbar tb = (EBIGUIToolbar) i.next();
                if (name.equals(tb.getName())) {
                    comp = tb.getComponent();
                    break;
                }
            }
        }
        return comp;
    }

    @Override
    public final JButton getToolBarButton(final String name, final String packages) {
        JButton bnt = null;

        final EBIGUIToolbar toolbar = ((EBIGUIToolbar) toToolbar.get(packages));
        if (toolbar != null) {
            final Iterator i = toolbar.getBarItem().iterator();
            while (i.hasNext()) {
                final EBIGUIToolbar tb = (EBIGUIToolbar) i.next();
                if (name.equals(tb.getName())) {
                    bnt = (JButton) tb.getComponent();
                    break;
                }
            }
        }
        return bnt;
    }

    @Override
    public final JRadioButton getRadioButton(final String name, final String packages) {
        JRadioButton actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JRadioButton) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JCheckBox getCheckBox(final String name, final String packages) {
        JCheckBox actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JCheckBox) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JComboBox combo(final String name, final String packages) {
        JComboBox actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JComboBox) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final EBIButton button(final String name, final String packages) {
        EBIButton actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (EBIButton) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JFormattedTextField FormattedField(final String name, final String packages) {
        JFormattedTextField actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JFormattedTextField) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JTextField textField(final String name, final String packages) {
        JTextField actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JTextField) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JLabel label(final String name, final String packages) {
        JLabel actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JLabel) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JTextArea textArea(final String name, final String packages) {
        JTextArea actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JTextArea) res.getScrollComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JEditorPane getEditor(final String name, final String packages) {
        JEditorPane actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JEditorPane) res.getScrollComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final EBITableExt table(final String name, final String packages) {
        EBITableExt actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (EBITableExt) res.getScrollComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JXTreeTable getTreeTable(final String name, final String packages) {
        JXTreeTable actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JXTreeTable) res.getScrollComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JXDatePicker timePicker(final String name, final String packages) {
        JXDatePicker actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JXDatePicker) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JList getList(final String name, final String packages) {
        JList actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages).get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JList) res.getScrollComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JPanel getPanel(final String name, final String packages) {
        JPanel actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages).get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JPanel) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JProgressBar getProgressBar(final String name, final String packages) {
        JProgressBar actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JProgressBar) res.getComponent();
            }
        }
        return actualComponent;
    }

    @Override
    public final JSpinner getSpinner(final String name, final String packages) {
        JSpinner actualComponent = null;
        if (resizeContainer.get(packages) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(packages)
                    .get(componentGet.get(packages + "." + name) - 1);
            if (res != null) {
                actualComponent = (JSpinner) res.getComponent();
            }
        }
        return actualComponent;
    }

    public final EBIGUIWidgetsBean getComponentWidgetBean(final String namespace) {

        final String pkg = namespace.substring(0, namespace.indexOf("."));
        EBIGUIWidgetsBean widgetBean = null;

        if (resizeContainer.get(pkg) != null) {
            final EBIGUINBean res = (EBIGUINBean) resizeContainer.get(pkg).get(componentGet.get(namespace) - 1);
            if (res != null) {
                widgetBean = res.getBean();
            }
        }

        return widgetBean;
    }

    @Override
    public final boolean existView(final String name) {
        return resizeContainer.containsKey(name);
    }

    @Override
    public final boolean isToolBarEmpty() {
        return toToolbar.isEmpty() ? true : false;
    }

    public final String getComponentNamespace() {
        return componentNamespace;
    }

    public final void setComponentNamespace(final String ncspace) {
        this.componentNamespace = ncspace;
    }

    @Override
    public final void removeFileFromTab(final int selIndex) {
        tabtoFile.remove(selIndex);
    }

    @Override
    public TreeMap<Integer, String> getHashTabtoFile() {
        return tabtoFile;
    }

    @Override
    public final String getFileToPath() {
        return this.fileToTabPath;
    }

    @Override
    public final int getProjectModuleCount() {
        return projectCount;
    }

    @Override
    public final int getProjectModuleEnabled() {
        return projectCountEnabled;
    }

    @Override
    public final EBIGUIWidgetsBean getGUIComponents(final String root) {
        return componentsTable.get(root);
    }

    public final String[] dbLoad(final String Tab) {

        PreparedStatement ps = null;
        ResultSet set = null;
        String[] BUFF = null;

        try {

            ps = EBISystem.db().initPreparedStatement("SELECT * FROM " + Tab + " order by NAME");
            set = ps.executeQuery();
            BUFF = new String[1];
            if (set != null) {
                set.last();
                final int size = set.getRow();
                if (size > 0) {
                    set.beforeFirst();
                    BUFF = new String[size + 1];
                    int i = 1;
                    while (set.next()) {
                        BUFF[i] = set.getString("NAME");
                        i++;
                    }
                }
            }
            BUFF[0] = EBISystem.i18n("EBI_LANG_PLEASE_SELECT");
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                set.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return BUFF;
    }

    private void dbSave(String Tab, String value) {
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("INSERT INTO " + Tab + "  (NAME) VALUES(?)");
            ps1.setString(1, value);
            EBISystem.getInstance().iDB().executePreparedStmt(ps1);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addComponentTextChanged(JComponent component) {
        if (component instanceof JCheckBox) {
            ((JCheckBox) component).addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {
                    EBISystem.canRelease = false;
                }
            });
        } else if (component instanceof JRadioButton) {
            ((JRadioButton) component).addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {
                    EBISystem.canRelease = false;
                }
            });
        } else {
            component.addKeyListener(new KeyAdapter() {
                String text = "";

                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getSource() instanceof JTextField) {
                        text = ((JTextField) e.getSource()).getText();
                    } else if (e.getSource() instanceof JTextArea) {
                        text = ((JTextArea) e.getSource()).getText();
                    } else if (e.getSource() instanceof JEditorPane) {
                        text = ((JEditorPane) e.getSource()).getText();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if (e.getSource() instanceof JTextField) {
                        if (!text.equals(((JTextField) e.getSource()).getText())) {
                            EBISystem.canRelease = false;
                        }
                    } else if (e.getSource() instanceof JTextArea) {
                        if (!text.equals(((JTextArea) e.getSource()).getText())) {
                            EBISystem.canRelease = false;
                        }
                    } else if (e.getSource() instanceof JEditorPane) {
                        if (!text.equals(((JEditorPane) e.getSource()).getText())) {
                            EBISystem.canRelease = false;
                        }
                    }
                }
            });
        }
    }
}
