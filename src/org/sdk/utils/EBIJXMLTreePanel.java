package org.sdk.utils;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Iterator;
import org.sdk.EBISystem;

/**
 * This dialog allow to view a XML file in JTreeView component
 *
 */
public class EBIJXMLTreePanel extends JTree {

    private Document xmlDoc;
    DefaultMutableTreeNode tn;
    private String treeName = null;

    public EBIJXMLTreePanel(final Document doc, final String treeName) {
        super();
        this.treeName = treeName;
        this.xmlDoc = doc;
        setSize(600, 450);
        tn = new DefaultMutableTreeNode(treeName);
        initialize();
    }

    private void initialize() {
        setName(this.treeName);
        setExpandsSelectedPaths(true);
        final ImageIcon tutorialIcon = EBISystem.getInstance().getIconResource("agt_action_success.png");
        final ImageIcon errorIcon = EBISystem.getInstance().getIconResource("agt_action_fail.png");
        if (tutorialIcon != null) {
            setCellRenderer(new MyRenderer(tutorialIcon, errorIcon));
        }
        processElement(xmlDoc.getRootElement(), tn);

        ((DefaultTreeModel) getModel()).setRoot(tn);
        expandTree();
        setVisible(true);
    }

    public void resetUIProtocol(final Document doc) {
        this.xmlDoc = doc;
        tn = new DefaultMutableTreeNode(treeName);
        initialize();
    }

    private void processElement(final Element el, final DefaultMutableTreeNode dmtn) {

        final DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(processAttributes(el));
        final String text = el.getTextNormalize();
        if ((text != null) && (!text.equals(""))) {
            currentNode.add(new DefaultMutableTreeNode(text));
        }

        final Iterator children = el.getChildren().iterator();

        while (children.hasNext()) {
            processElement((Element) children.next(), currentNode);
        }

        dmtn.add(currentNode);
    }

    private String processAttributes(final Element el) {
        final Iterator atts = el.getAttributes().iterator();
        String attributes = el.getName() + " ";
        while (atts.hasNext()) {
            final Attribute att = (Attribute) atts.next();
            attributes += " " + att.getName() + ":" + att.getValue() + "";
        }
        return attributes;
    }

    private void expandTree() {
        expandAPath(getPathForRow(0));
    }

    private void expandAPath(final TreePath p) {
        expandPath(p);
        final DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) p.getLastPathComponent();
        final int numChildren = currNode.getChildCount();
        for (int i = 0; i < numChildren; ++i) {

            final TreePath newPath = p.pathByAddingChild(currNode.getChildAt(i));
            expandAPath(newPath);
        }
    }

    private class MyRenderer extends DefaultTreeCellRenderer {

        Icon tutorialIcon;
        Icon errorIcon;

        public MyRenderer(final Icon icon, final Icon icon1) {
            tutorialIcon = icon;
            errorIcon = icon1;
        }

        @Override
        public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (leaf) {
                final String error = value.toString().substring(value.toString().length() - 1, value.toString().length());
                if (error.equals(" ")) {
                    setIcon(errorIcon);
                } else {
                    setIcon(tutorialIcon);
                }
            } else {
                //setIcon(tutorialIcon);
            }
            return this;
        }
    }
}
