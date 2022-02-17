package org.modules.controls.component;

import org.modules.controls.component.listener.EBICRMActionListener;
import org.sdk.EBISystem;
import javax.swing.*;

public class EBICRMToolBar extends EBICRMActionListener {

    public EBICRMToolBar() {}

    /**
     * Initialize CRM ToolBar
     */
    public void setCRMToolBar() {

        if (EBISystem.builder().getToolBarButton("toolbarItemNew", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemNew", "ebiToolBar").addActionListener(newListenerAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemSave", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemSave", "ebiToolBar").addActionListener(saveListenerAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemSearchCompany", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemSearchCompany", "ebiToolBar").addActionListener(searchCompanyAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemHistory", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemHistory", "ebiToolBar").setEnabled(false);
            EBISystem.builder().getToolBarButton("toolbarItemHistory", "ebiToolBar").addActionListener(searchCompanyHistoryAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemSearchContact", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemSearchContact", "ebiToolBar").addActionListener(searchContactAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemCompanyReport", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemCompanyReport", "ebiToolBar").addActionListener(printReportAction());
        }
        
        if (EBISystem.builder().getToolBarButton("toolbarItemDelete", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemDelete", "ebiToolBar").setEnabled(false);
            EBISystem.builder().getToolBarButton("toolbarItemDelete", "ebiToolBar").addActionListener(deleteListenerAction());
        }
        
        if (EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Product/productGUI.xml") || EBISystem.getInstance().getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).addActionListener(windowShowProductTab());
            } else {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setVisible(false);
            }
        }
      
        if (EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("CRMProblemSolution/problemSolutionGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).addActionListener(windowShowProsolTab());
            } else {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setVisible(false);
            }
        }
        
        if (EBISystem.builder().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Project/projectGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).addActionListener(windowShowProjectTab());
            } else {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setVisible(false);
            }
        }
        
        if (EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("Invoice/invoiceGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).addActionListener(windowShowInvoiceTab());
            } else {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setVisible(false);
            }
        }
        if (EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
            if (EBISystem.registeredModule.contains("AccountStack/accountGUI.xml") || EBISystem.getUserRight().isAdministrator()) {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).addActionListener(windowsShowAccountTab());
            } else {
                ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setVisible(false);
            }
        }
    }

    public void enableToolButtonProductModule() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isProductEnabled() {
        return EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).isSelected();
    }

    public void enableToolButtonCampaignModule() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isCampaignSelected() {
        return EBISystem.builder().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).isSelected();
    }

    public void enableToolButtonProsol() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonCashRegister() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemCashRegisterModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemCashRegisterModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonProject() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonInvoice() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setSelected(true);
        }
    }

    public void enableToolButtonAccount() {
        if (EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
            ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setSelected(true);
        }
    }

    public boolean isInvoiceSelected() {
        return EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).isSelected();
    }

    public boolean isAccountSelected() {
        return EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).isSelected();
    }

    public boolean isProsolSelected() {
        return EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") == null ? false : ((JToggleButton) EBISystem.builder().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).isSelected();
    }

    /**
     * Enable or disable ToolBar delete button
     *
     * @param enabled
     */
    public void enableToolbarButton(final boolean enabled) {
        if (EBISystem.builder().getToolBarButton("toolbarItemDelete", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemDelete", "ebiToolBar").setEnabled(enabled);
        }
        if (EBISystem.builder().getToolBarButton("toolbarItemHistory", "ebiToolBar") != null) {
            EBISystem.builder().getToolBarButton("toolbarItemHistory", "ebiToolBar").setEnabled(enabled);
        }
    }
}
