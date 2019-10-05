package org.modules.models;

import org.modules.utils.EBISearchTreeNode;
import org.sdk.EBISystem;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class ModelCRMCompany extends DefaultTreeTableModel {
       
        protected boolean asksAllowsChildren;

        public ModelCRMCompany() {
                this(new EBISearchTreeNode(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "","","", "","", "","","","",""), false);
                initData();
        }

        public ModelCRMCompany(final TreeTableNode root) {
                this(root, false);
        }

        public ModelCRMCompany(final TreeTableNode root, final boolean asksAllowsChildren) {
                super(root);
                this.asksAllowsChildren = asksAllowsChildren;
                
        }
       
        public void initData() {
        	    final EBISearchTreeNode root = (EBISearchTreeNode)this.getRoot();
                final EBISearchTreeNode topLevel = new EBISearchTreeNode(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "","","","", "", "","","","","");
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
                /**@todo Implement this org.jdesktopx.swing.treetable.TreeTableModel abstract method*/
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
                        return EBISystem.i18n("EBI_LANG_C_COOPERATION");
                case 8: 
                	    return EBISystem.i18n("EBI_LANG_C_CRM_CLASSIFICATION_TYPE");
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
                            return ((EBISearchTreeNode) node).getIntNr();
                        case 1:
                        	return ((EBISearchTreeNode) node).getName();
                        case 2:
                        	return ((EBISearchTreeNode) node).getStreet();
                        case 3:
                        	return ((EBISearchTreeNode) node).getZip();
                        case 4:
                        	return ((EBISearchTreeNode) node).getLocation();
                        case 5:
                        	return ((EBISearchTreeNode) node).getCountry();
                        case 6:
                        	return ((EBISearchTreeNode) node).getCategory();
                        case 7:
                        	return ((EBISearchTreeNode) node).getCooperation();
                        case 8:
                        	return ((EBISearchTreeNode) node).getQualification();
                        case 9:
                        	return ((EBISearchTreeNode) node).getIsLock();
                        	
                        }
                } catch (final Exception ex) {
                      
                }
                return null;
        }

} 