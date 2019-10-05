package org.core.guiRenderer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EBIGUIMenus {

    private int id = 0;
    private String name ="";
    private String type = "";
    private String title ="";
    private ImageIcon icon=null;
    private int KeyEvent = 0;
    private boolean isVisualPanel = false; 
    private javax.swing.KeyStroke keyStroke = null;
    private List<EBIGUIMenus> subMenu = null;
    private JComponent component = null;
    private Boolean isCheckable = false;

    public EBIGUIMenus(){
        subMenu = new ArrayList<EBIGUIMenus>();
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(final ImageIcon icon) {
        this.icon = icon;
    }

    public int getKeyEvent() {
        return KeyEvent;
    }

    public void setKeyEvent(final int keyEvent) {
        KeyEvent = keyEvent;
    }

    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public void setKeyStroke(final KeyStroke keyStroke ) {
        this.keyStroke =  keyStroke;
    }

    public List<EBIGUIMenus> getMenuItem() {
        return subMenu;
    }

    public void setMenuItem(final EBIGUIMenus menuItem) {
        this.subMenu.add(menuItem);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
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

    public Boolean getCheckable() {
        return isCheckable;
    }

    public void setCheckable(final Boolean checkable) {
        isCheckable = checkable;
    }
}
