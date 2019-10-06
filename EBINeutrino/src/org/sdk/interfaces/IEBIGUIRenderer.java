package org.sdk.interfaces;

import org.core.guiRenderer.EBIButton;
import org.core.guiRenderer.EBIGUIWidgetsBean;
import org.core.gui.component.EBITableExt;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.gui.dialogs.EBIDialog;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.text.Document;
import java.util.HashMap;
import java.util.TreeMap;

public interface IEBIGUIRenderer {

    EBIGUIWidgetsBean getGUIComponents(String root);

    void addScriptBean(String type, String path, String name, String clsName, String cmpNamespace);

    void excScript(final String cmpNamespace, HashMap<String, String> PARAM);

    void addUndoManager(Document doc, JComponent comp);

    void loadGUI(String path);

    String loadGUIPlus(String path);

    void loadProject(String path);

    void showGUI();

    void initScripts();

    void init();

    void showToolBar(String name, boolean toVisualPanel);

    EBIButton button(String name, String packages);

    JTextField textField(String name, String packages);

    JFormattedTextField FormattedField(String name, String packages);

    JLabel label(String name, String packages);

    JTextArea textArea(String name, String packages);

    EBITableExt table(String name, String packages);

    JXTreeTable getTreeTable(String name, String packages);

    JEditorPane getEditor(String name, String packages);

    JXDatePicker timePicker(String name, String packages);

    JList getList(String name, String packages);

    JPanel getPanel(String name, String packages);

    JToolBar getToolBar(String name);

    JComponent getToolBarComponent(String name, String packages);

    JButton getToolBarButton(String name, String packages);

    JComboBox combo(String name, String packages);

    JCheckBox getCheckBox(String name, String packages);

    JRadioButton getRadioButton(String name, String packages);

    EBIVisualPanelTemplate vpanel(String name);

    JProgressBar getProgressBar(String name, String packages);

    EBIDialog dialog(String name);

    boolean existView(String name);

    JSpinner getSpinner(String name, String packages);

    void removeFileFromTab(int selIndex);

    void removeGUIObject(String strID);

    TreeMap<Integer, String> getHashTabtoFile();

    int getProjectModuleCount();

    int getProjectModuleEnabled();

    boolean isToolBarEmpty();

    String getFileToPath();

    EBIGUIWidgetsBean getComponentWidgetBean(final String namespace);
}
