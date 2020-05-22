package org.modules.models;

import org.modules.utils.EBISearchTreeNodeProduct;
import org.sdk.EBISystem;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class ModelCRMProductSearch extends AbstractTreeTableModel {

    protected boolean asksAllowsChildren;

    public ModelCRMProductSearch() {
        this(new EBISearchTreeNodeProduct(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", ""), false);
        initData();
    }

    public ModelCRMProductSearch(final TreeTableNode root) {
        this(root, false);
    }

    public ModelCRMProductSearch(final TreeTableNode root, final boolean asksAllowsChildren) {
        super(root);
        this.asksAllowsChildren = asksAllowsChildren;
    }

    public void initData() {
        final EBISearchTreeNodeProduct root = (EBISearchTreeNodeProduct) this.getRoot();
        final EBISearchTreeNodeProduct topLevel = new EBISearchTreeNodeProduct(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "");
        root.add(topLevel);
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        final TreeTableNode fileNode = ((TreeTableNode) parent);
        return fileNode.getChildAt(index);
    }

    @Override
    public int getChildCount(final Object parent) {
        final TreeTableNode fileNode = ((TreeTableNode) parent);
        return fileNode.getChildCount();
    }

    @Override
    public int getColumnCount() {
        /**
         * @todo Implement this org.jdesktopx.swing.treetable.TreeTableModel
         * abstract method
         */
        return 7;
    }

    @Override
    public String getColumnName(final int column) {

        switch (column) {
            case 1:
                return EBISystem.i18n("EBI_LANG_ID");
            case 2:
                return EBISystem.i18n("EBI_LANG_NUMBER");
            case 3:
                return EBISystem.i18n("EBI_LANG_NAME");
            case 4:
                return EBISystem.i18n("EBI_LANG_CATEGORY");
            case 5:
                return EBISystem.i18n("EBI_LANG_TYPE");
            default:
                return "";

        }
    }

    @Override
    public Object getValueAt(final Object node, final int column) {

        try {
            switch (column) {
                case 1:
                    return ((EBISearchTreeNodeProduct) node).getProductID();
                case 2:
                    return ((EBISearchTreeNodeProduct) node).getProductNr();
                case 3:
                    return ((EBISearchTreeNodeProduct) node).getProductName();
                case 4:
                    return ((EBISearchTreeNodeProduct) node).getCategory();
                case 5:
                    return ((EBISearchTreeNodeProduct) node).getType();
                default:
                    break;

            }
        } catch (final Exception ex) {

        }
        return null;
    }

    @Override
    public int getIndexOfChild(final Object arg0, final Object arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

}
