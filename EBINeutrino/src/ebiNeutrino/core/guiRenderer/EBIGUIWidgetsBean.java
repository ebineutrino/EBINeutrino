package ebiNeutrino.core.guiRenderer;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;


public class EBIGUIWidgetsBean {

    private String type = "";
    private Dimension dimension = new Dimension();
    private Point point = new Point();
    private String name = "";
    private String title = "";
    private int x = 0;
    private int y = 0;
    private ImageIcon icon = null;
    private File image = new File(".");
    private String path = "";
    private String className = "";
    private int tabIndex = -1;
    private int min = -1;
    private int max = -1;
    private java.util.List<EBIGUIWidgetsBean> subWidgets = new ArrayList<EBIGUIWidgetsBean>();
    private Color color = null;
    private String colorName = "";
    private String bind = "";
    private int KeyEvent = 0;
    private KeyStroke keyStroke = null;
    private boolean heightAutoResize = false;
    private boolean widthAutoResize = false;
    private boolean fitOnResize = false;
    private int originalX = 0;
    private int originalY = 0;
    private int originalWidth = 0;
    private int originalHeight = 0;
    private boolean isCeckable = false;
    private boolean visualProperties = false;
    private boolean modal = false;
    private boolean resizable = false;
    private boolean isEnabled = true;
    private boolean pessimistic = true;
    private boolean isVisible = true;
    private boolean isEditable = false;
    private boolean isShowTimer = false;
    private Object component = null;
    private String i18NToolTip = "";
    private boolean assignable = false;
    private String value = "";
    private boolean storable = false;
    private String actionListener = "";
    private String mappedBean = "";

    public void setSubWidgets(final java.util.List<EBIGUIWidgetsBean> subWidgets) {
        this.subWidgets = subWidgets;
    }
    public java.util.List<EBIGUIWidgetsBean> getSubWidgets() {
        return subWidgets;
    }

    public File getImage() {
        return image;
    }

    public void setImage(final File image) {
        this.image = image;
        this.icon = new ImageIcon(image.getAbsolutePath());
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
        getPoint().x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
        getPoint().y = y;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(final Point point) {
        this.x = point.x;
        this.y = point.y;
        this.point = point;
    }


    public String getType() {
        return type;
    }


    public void setType(final String type) {
        this.type = type;
    }


    public Dimension getDimension() {
        return dimension;
    }


    public void setDimension(final Dimension dimension) {
        this.dimension = dimension;
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

    public String getIconPath() {
        return image.getAbsolutePath();
    }

    public void setIcon(final String icon) {
        this.icon = new ImageIcon("images/" + icon);
        this.image = new File(icon);
    }

    public Color getColor() {
        return color;
    }

    public String getColorName() {
        return this.colorName;
    }

    public void setColor(final String col) {
        try {
            color = Color.decode("".equals(col) ? "#000000" : col);
            colorName = col;
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public boolean isHeightAutoResize() {
        return heightAutoResize;
    }

    public void setHeightAutoResize(final boolean hightAutoResize) {
        this.heightAutoResize = hightAutoResize;
    }

    public boolean isWidthAutoResize() {
        return widthAutoResize;
    }

    public void setWidthAutoResize(final boolean widthAutoRisize) {
        this.widthAutoResize = widthAutoRisize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
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

    public void setKeyStroke(final int keyStroke) {
        this.keyStroke = KeyStroke.getKeyStroke(keyStroke, InputEvent.CTRL_DOWN_MASK, true);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    public boolean isCeckable() {
        return isCeckable;
    }

    public void setCeckable(final boolean ceckable) {
        isCeckable = ceckable;
    }

    public boolean isVisualProperties() {
        return visualProperties;
    }

    public void setVisualProperties(final boolean visualProperties) {
        this.visualProperties = visualProperties;
    }

    public int getTabIndex() {
        return this.tabIndex;
    }

    public void setTabIndex(final int index) {
        this.tabIndex = index;
    }

    public boolean isModal() {
        return modal;
    }

    public void setModal(final boolean modal) {
        this.modal = modal;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(final boolean resizable) {
        this.resizable = resizable;
    }

    public Object getComponent() {
        return component;
    }

    public void setComponent(final Object component) {
        this.component = component;
    }

    public void setMin(final int min) {
        this.min = min;
    }

    public int getMin() {
        return this.min;
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public int getMax() {
        return this.max;
    }

    public void setI18NToolTip(final String toolTip) {
        this.i18NToolTip = toolTip;
    }

    public String getI18NToolTip() {
        return this.i18NToolTip;
    }

    public boolean isFitOnResize() {
        return fitOnResize;
    }

    public void setFitOnResize(final boolean fitOnResize) {
        this.fitOnResize = fitOnResize;
    }

    public int getOriginalX() {
        return originalX;
    }

    public void setOriginalX(final int originalX) {
        this.originalX = originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public void setOriginalY(final int originalY) {
        this.originalY = originalY;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(final int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(final int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(final boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isAssignable() {
        return assignable;
    }

    public void setAssignable(final boolean assignable) {
        this.assignable = assignable;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(final boolean visible) {
        isVisible = visible;
    }

    public boolean isPessimistic() {
        return pessimistic;
    }

    public void setPessimistic(final boolean pessimistic) {
        this.pessimistic = pessimistic;
    }

    public boolean isShowTimer() {
        return isShowTimer;
    }

    public void setShowTimer(final boolean isShowTimer) {
        this.isShowTimer = isShowTimer;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(final boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean isStorable() {
        return storable;
    }

    public void setStorable(final boolean storable) {
        this.storable = storable;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getActionListener() {
        return actionListener;
    }

    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    public String getMappedBean() {
        return mappedBean;
    }

    public void setMappedBean(String mappedBean) {
        this.mappedBean = mappedBean;
    }
}
