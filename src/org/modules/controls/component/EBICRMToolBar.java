package org.modules.controls.component;

import org.modules.EBIModule;
import org.modules.controls.component.listener.EBICRMActionListener;
import org.sdk.EBISystem;
import javax.swing.*;

public class EBICRMToolBar extends EBICRMActionListener {

    public EBICRMToolBar() {}

    /**
     * Initialize CRM ToolBar
     */
    public void setCRMToolBar() {

        if (EBISystem.gui().getToolBarButton("toolbarItemNew", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemNew", "ebiToolBar").addActionListener(newListenerAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemSave", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemSave", "ebiToolBar").addActionListener(saveListenerAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemSearchCompany", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemSearchCompany", "ebiToolBar").addActionListener(searchCompanyAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemHistory", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemHistory", "ebiToolBar").setEnabled(false);
            EBISystem.gui().getToolBarButton("toolbarItemHistory", "ebiToolBar").addActionListener(searchCompanyHistoryAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemSearchContact", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemSearchContact", "ebiToolBar").addActionListener(searchContactAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemCompanyReport", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemCompanyReport", "ebiToolBar").addActionListener(printReportAction());
        }
        
        if (EBISystem.gui().getToolBarButton("toolbarItemDelete", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemDelete", "ebiToolBar").setEnabled(false);
            EBISystem.gui().getToolBarButton("toolbarItemDelete", "ebiToolBar").addActionListener(deleteListenerAction());
        }
        
        if (EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Product/productGUI.xml") || EBISystem.getInstance().getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).addActionListener(windowShowProductTab());
            } else {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setVisible(false);
            }
        }
      
        if (EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("CRMProblemSolution/problemSolutionGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).addActionListener(windowShowProsolTab());
            } else {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setVisible(false);
            }
        }
        
        if (EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Project/projectGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).addActionListener(windowShowProjectTab());
            } else {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setVisible(false);
            }
        }
        
        if (EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Invoice/invoiceGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).addActionListener(windowShowInvoiceTab());
            } else {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setVisible(false);
            }
        }
        
        if (EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("AccountStack/accountGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).addActionListener(windowsShowAccountTab());
            } else {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setVisible(false);
            }
        }

    }

    public void enableToolButtonProductModule() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isProductEnabled() {
        return EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).isSelected();
    }

    public void enableToolButtonCampaignModule() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isCampaignSelected() {
        return EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).isSelected();
    }

    public void enableToolButtonProsol() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonCashRegister() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemCashRegisterModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCashRegisterModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonProject() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonInvoice() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonAccount() {
        if (EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isInvoiceSelected() {
        return EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).isSelected();
    }

    public boolean isAccountSelected() {
        return EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).isSelected();
    }

    public boolean isProsolSelected() {
        return EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).isSelected();
    }

    /**
     * Enable or disable ToolBar delete button
     *
     * @param enabled
     */
    public void enableToolbarButton(final boolean enabled) {
        if (EBISystem.gui().getToolBarButton("toolbarItemDelete", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemDelete", "ebiToolBar").setEnabled(enabled);
        }
        if (EBISystem.gui().getToolBarButton("toolbarItemHistory", "ebiToolBar") != null) {
            EBISystem.gui().getToolBarButton("toolbarItemHistory", "ebiToolBar").setEnabled(enabled);
        }
    }
}
