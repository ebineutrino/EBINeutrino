package org.core.guiRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EBIGUIToolbar {

    private int id = 0;
    private String name = "";
    private String title = "";
    private String type = "";
    private String toolTip = "";
    private ImageIcon icon = null;
    private boolean isVisualPanel = false;
    private List<EBIGUIToolbar> barItem = null;
    private JComponent component = null;
    private KeyStroke keyStroke = null;
    
    public EBIGUIToolbar(){
        barItem = new ArrayList<EBIGUIToolbar>();
    }


    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public void setKeyStroke(final KeyStroke keyStroke) {
        this.keyStroke = keyStroke;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(final ImageIcon icon) {
        this.icon = icon;
    }

    public List<EBIGUIToolbar> getBarItem() {
        return barItem;
    }

    public void setBarItem(final EBIGUIToolbar barItem) {
        this.barItem.add(barItem);
    }

    public boolean isVisualPanel() {
        return isVisualPanel;
    }

    public void setVisualPanel(final boolean visualPanel) {
        isVisualPanel = visualPanel;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(final JComponent component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setToolTip(final String toolTip){
        this.toolTip = toolTip;
    }

    public String getToolTip(){
        return this.toolTip;
    }
}
