package org.core.guiRenderer;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class EBIXMLGUIReader {

    private EBIGUIWidgetsBean widgetsObject = null;
    private String xmlPath = "";
    public Document xmlDoc = null;
    private SAXBuilder builder = null;
    private boolean stepOne = true;

    private String resourceViewsPath = System.getProperty("user.dir")
                + File.separator+"resources"
                + File.separator;

    public EBIXMLGUIReader(final String xmlPath) {
        builder = new SAXBuilder();
        widgetsObject = new EBIGUIWidgetsBean();
        this.xmlPath = xmlPath;
        stepOne = true;
    }

    public EBIXMLGUIReader() {
        builder = new SAXBuilder();
        widgetsObject = new EBIGUIWidgetsBean();
        stepOne = true;
    }

    public boolean loadXMLGUI() {
        boolean ret = true;
        stepOne = true;
        try {
            xmlDoc = builder.build(resourceViewsPath+xmlPath);
            ret = readXMLGUI(xmlDoc.getRootElement());
        } catch (final IOException ex) {
            ret = false;
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final JDOMException ex) {
            ret = false;
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return ret;
    }

    public boolean loadXMLGUI(final String file) {
        boolean ret = true;
        stepOne = true;
        try {
            final byte[] bytes = file.getBytes("utf-8");
            xmlDoc = builder.build(new ByteArrayInputStream(bytes));
            ret = readXMLGUI(xmlDoc.getRootElement());
        } catch (final IOException ex) {
            ret = false;
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final JDOMException ex) {
            ret = false;
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return ret;
    }

    private boolean readXMLGUI(final Element el) {
        // Read from xml file
        if (!parseXMLGUI(el)) {
            return false;
        }
        final Iterator children = el.getChildren().iterator();
        while (children.hasNext()) {
            readXMLGUI((Element) children.next());
        }
        return true;
    }

    private boolean parseXMLGUI(final Element el) {
        try {
            final Iterator atts = el.getAttributes().iterator();
            final EBIGUIWidgetsBean widg = new EBIGUIWidgetsBean();

            while (atts.hasNext()) {

                final Attribute att = (Attribute) atts.next();
                final String attName = att.getName().trim().toLowerCase();

                switch (attName) {

                    case "name":
                        widg.setName(att.getValue());
                        break;

                    case "location":
                        widg.setPoint(parsePoint(att.getValue()));
                        break;

                    case "size":
                        parseDimension(widg, att.getValue());
                        break;

                    case "title":
                        widg.setTitle(att.getValue());
                        break;

                    case "icon":
                        widg.setIcon(att.getValue());
                        break;

                    case "background":
                        widg.setColor(att.getValue());
                        break;

                    case "path":
                        widg.setPath(att.getValue());
                        break;

                    case "keyevent":
                        if (!"".equals(att.getValue()) && KeyStroke.getKeyStroke(att.getValue()) != null) {
                            widg.setKeyEvent(KeyStroke.getKeyStroke(att.getValue()).getKeyCode());
                        }
                        break;

                    case "keystroke":
                        if (!"".equals(att.getValue()) && KeyStroke.getKeyStroke(att.getValue()) != null) {
                            widg.setKeyStroke(KeyStroke.getKeyStroke(att.getValue()).getKeyCode());
                        }
                        break;

                    case "classname":
                        if (!"".equals(att.getValue())) {
                            widg.setClassName(att.getValue());
                        }
                        break;

                    case "bean":
                        if (!"".equals(att.getValue())) {
                            widg.setMappedBean(att.getValue());
                        }
                        break;

                    case "checkable":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setCeckable(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setCeckable(false);
                            }
                        }
                        break;

                    case "visualproperties":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setVisualProperties(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setVisualProperties(false);
                            }
                        }
                        break;

                    case "pessimistic":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setPessimistic(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setPessimistic(true);
                            }
                        }
                        break;

                    case "assignable":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setAssignable(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setAssignable(false);
                            }
                        }
                        break;

                    case "modal":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setModal(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setModal(false);
                            }
                        }
                        break;

                    case "enabled":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setEnabled(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setEnabled(false);
                            }
                        }
                        break;

                    case "editable":

                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setEditable(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setEnabled(false);
                            }
                        }

                        break;

                    case "visible":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setVisible(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setVisible(true);
                            }
                        }
                        break;

                    case "resizable":

                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setResizable(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setResizable(false);
                            }
                        }
                        break;

                    case "fitonresize":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setFitOnResize(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setFitOnResize(false);
                            }
                        }
                        break;

                    case "resizewidth":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setWidthAutoResize(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setWidthAutoResize(false);
                            }
                        }
                        break;

                    case "resizeheight":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setHeightAutoResize(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setHeightAutoResize(false);
                            }
                        }
                        break;

                    case "timer":
                        if (!"".equals(att.getValue())) {
                            try {
                                widg.setShowTimer(Boolean.parseBoolean(att.getValue()));
                            } catch (final Exception ex) {
                                widg.setShowTimer(false);
                            }
                        }
                        break;

                    case "i18ntooltip":
                        if (!"".equals(att.getValue())) {
                            widg.setI18NToolTip(att.getValue());
                        }
                        break;

                    case "min":
                        try {
                            widg.setMin(Integer.parseInt(att.getValue()));
                        } catch (final NumberFormatException ex) {
                            widg.setMin(0);
                        }
                        break;

                    case "max":
                        try {
                            widg.setMax(Integer.parseInt(att.getValue()));
                        } catch (final NumberFormatException ex) {
                            widg.setMax(0);
                        }
                        break;
                    case "tabindex":
                        try {
                            widg.setTabIndex(Integer.parseInt(att.getValue()));
                        } catch (final NumberFormatException ex) {
                            widg.setTabIndex(0);
                        }
                        break;
                        
                    case "bind":
                    	widg.setBind(att.getValue());
                    	break;

                    case "actionlistener":
                    	widg.setActionListener(att.getValue());
                    	break;
                        
                    case "placeholder":
                    	widg.setPlaceHolder(att.getValue());
                    	break;
                }
            }
            widg.setType(el.getName().trim().toLowerCase());
            if (!stepOne) {
                setParentComponent(el.getParentElement().getAttributeValue(getAttName(el)), widgetsObject, widg);
            } else {
                stepOne = false;
                widgetsObject = widg;
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    private String getAttName(final Element el) {
        String retStr = "";
        final List list = el.getParentElement().getAttributes();
        final Iterator iter = list.iterator();

        while (iter.hasNext()) {
            final Attribute att = (Attribute) iter.next();
            if ("name".equals(att.getName().toLowerCase())) {
                retStr = att.getName();
                break;
            }
        }

        return retStr;
    }

    private Point parsePoint(final String value) {
        Point pt;
        try {
            final String[] val = value.trim().split(",");

            pt = new Point();
            pt.setLocation(Integer.parseInt(val[0].trim()), Integer.parseInt(val[1].trim()));
        } catch (final NumberFormatException ex) {
            pt = new Point(0, 0);
        }
        return pt;
    }

    private void parseDimension(final EBIGUIWidgetsBean widg, final String value) {
        final String[] val = value.trim().split(",");
        final Dimension dim = new Dimension();
        try {
            dim.setSize(Integer.parseInt(val[0].trim()), Integer.parseInt(val[1].trim()));
        } catch (final NumberFormatException ex) {
            dim.setSize(20, 100);
        }
        widg.setOriginalHeight((int) dim.getHeight());
        widg.setOriginalWidth((int) dim.getWidth());
        widg.setDimension(dim);
    }

    private void setParentComponent(final String name, final EBIGUIWidgetsBean runBean, final EBIGUIWidgetsBean toSet) {
        if (name.equals(runBean.getName())) {
            runBean.getSubWidgets().add(toSet);
            return;
        } else {
            final Iterator iter = runBean.getSubWidgets().iterator();
            while (iter.hasNext()) {
                final EBIGUIWidgetsBean bn = (EBIGUIWidgetsBean) iter.next();
                setParentComponent(name, bn, toSet);
            }
        }
    }

    public EBIGUIWidgetsBean getCompObjects() {
        return widgetsObject;
    }

    public void setCompObjects(final EBIGUIWidgetsBean compObjects) {
        this.widgetsObject = compObjects;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(final String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public EBIGUIWidgetsBean getWidgetsObject() {
        return widgetsObject;
    }

    public void setWidgetsObject(final EBIGUIWidgetsBean widgetsObject) {
        this.widgetsObject = widgetsObject;
    }

}
