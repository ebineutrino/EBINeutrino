package org.modules.models;

import org.modules.utils.EBISearchTreeNodeHistory;
import org.sdk.EBISystem;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class CompanyHistory extends DefaultTreeTableModel {

    protected boolean asksAllowsChildren;

    public CompanyHistory() {
        this(new EBISearchTreeNodeHistory(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", ""), false);
        initData();
    }

    public CompanyHistory(final TreeTableNode root) {
        this(root, false);
    }

    public CompanyHistory(final TreeTableNode root, final boolean asksAllowsChildren) {
        super(root);
        this.asksAllowsChildren = asksAllowsChildren;
    }

    public void initData() {
        final EBISearchTreeNodeHistory root = (EBISearchTreeNodeHistory) this.getRoot();
        final EBISearchTreeNodeHistory topLevel = new EBISearchTreeNodeHistory(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "");
        root.add(topLevel);
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        return super.getChild(parent, index);
    }

    @Override
    public int getChildCount(final Object parent) {
        return super.getChildCount(parent);
    }

    @Override
    public int getColumnCount() {
        /**
         * @todo Implement this org.jdesktopx.swing.treetable.TreeTableModel
         * abstract method
         */
        return 10;
    }

    @Override
    public String getColumnName(final int column) {

        switch (column) {
            case 0:
                return EBISystem.i18n("EBI_LANG_C_INTERNAL_NUMBER");
            case 1:
                return EBISystem.i18n("EBI_LANG_NAME");
            case 2:
                return EBISystem.i18n("EBI_LANG_C_STREET_NR");
            case 3:
                return EBISystem.i18n("EBI_LANG_C_ZIP");
            case 4:
                return EBISystem.i18n("EBI_LANG_C_LOCATION");
            case 5:
                return EBISystem.i18n("EBI_LANG_C_COUNTRY");
            case 6:
                return EBISystem.i18n("EBI_LANG_CATEGORY");
            case 7:
                return EBISystem.i18n("EBI_LANG_CHANGED");
            case 8:
                return EBISystem.i18n("EBI_LANG_CHANGED_FROM");
            case 9:
                return EBISystem.i18n("EBI_LANG_C_LOCK");
            default:
                return "";

        }
    }

    @Override
    public Object getValueAt(final Object node, final int column) {

        try {
            switch (column) {
                case 0:
                    return ((EBISearchTreeNodeHistory) node).getIntNr();
                case 1:
                    return ((EBISearchTreeNodeHistory) node).getName();
                case 2:
                    return ((EBISearchTreeNodeHistory) node).getStreet();
                case 3:
                    return ((EBISearchTreeNodeHistory) node).getZip();
                case 4:
                    return ((EBISearchTreeNodeHistory) node).getLocation();
                case 5:
                    return ((EBISearchTreeNodeHistory) node).getCountry();
                case 6:
                    return ((EBISearchTreeNodeHistory) node).getCategory();
                case 7:
                    return ((EBISearchTreeNodeHistory) node).getChanged();
                case 8:
                    return ((EBISearchTreeNodeHistory) node).getChangedFrom();
                case 9:
                    return ((EBISearchTreeNodeHistory) node).getIsLock();

            }
        } catch (final Exception ex) {

        }
        return null;
    }
}
