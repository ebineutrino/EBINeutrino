package org.modules.views.component;

import java.util.Date;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.CloseableTabbedPaneListener;

import javax.swing.*;

public class EBICRMTabcontrol implements CloseableTabbedPaneListener {

    public int companyPOSID = -1;

    public void setTabPanels(final boolean enableTab) {
        try {
            final JTabbedPane cls = EBISystem.getInstance().getIEBIContainerInstance().getTabInstance();
            if (!enableTab && companyPOSID != -1) {
                EBISystem.getInstance().getIEBIContainerInstance().getTabInstance().setTitleAt(companyPOSID, EBISystem.i18n("EBI_LANG_C_COMPANY"));
            }
            if (!enableTab) {
                for (int i = EBISystem.gui().getProjectModuleEnabled(); i < EBISystem.gui().getProjectModuleCount(); i++) {
                    cls.setEnabledAt(i, enableTab);
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getLocalizedMessage()).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public int getSelectedTab() {
        return EBISystem.getInstance().getIEBIContainerInstance().getSelectedTab();
    }

    public void setSelectedTab(final int tab) {
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(tab);
    }

    public JTabbedPane getTabInstance() {
        return EBISystem.getInstance().getIEBIContainerInstance().getTabInstance();
    }

    public void closeProductContainer() {
        if (EBISystem.getModule().getEBICRMProductPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().
                    removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT")));
            EBISystem.getModule().invalidateProductPane();
        }
    }

    public void closeCampaignContainer() {
        if (EBISystem.getModule().getEBICRMCampaign() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().
                    removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN")));
            EBISystem.getModule().invalidateCampaign();
        }
    }

    public void closeProsolContainer() {
        if (EBISystem.getModule().getProsolPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().
                    removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL")));
            EBISystem.getModule().invalidateProsolPane();
        }
    }

    public void closeProjectContainer() {
        if (EBISystem.getModule().getProjectPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().
                    removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT")));
            EBISystem.getModule().invalidateProjectPane();
        }
    }

    public void showClosableProductContainer() {
        EBISystem.gui().loadGUI("Product/productGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getEBICRMProductPane().showProduct();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableCampaignContainer() {
        EBISystem.gui().loadGUI("Campaign/campaignGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getEBICRMCampaign().getDataControlCampaign().dataShow(-1);
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableProsolContainer() {
        EBISystem.gui().loadGUI("CRMProblemSolution/problemSolutionGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getProsolPane().getDataControlProsol().dataShow(-1);
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableProjectContainer() {
        EBISystem.gui().loadGUI("Project/projectGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getProjectPane().showProject();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableInvoiceContainer() {
        EBISystem.gui().loadGUI("Invoice/invoiceGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getInvoicePane().dataShow(-1);
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableAccountContainer() {
        EBISystem.gui().loadGUI("AccountStack/accountGUI.xml");
        EBISystem.gui().showGUI();
        EBISystem.getModule().getAccountPane().
                getDataControlAccount().dataShow(EBISystem.gui().combo("invoiceYearText", "Account").getEditor().getItem().toString(), -1);
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void closeInvoiceContainer() {
        if (EBISystem.getModule().getInvoicePane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().
                    removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE")));
            EBISystem.getModule().getInvoicePane().getDataControlInvoice().dataNew();
            EBISystem.getModule().invalidateInvoicePane();
        }
    }

    public void closeAccountContainer() {
        if (EBISystem.getModule().getAccountPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT")));
            EBISystem.getModule().getAccountPane().getDataControlAccount().dataNew();
            EBISystem.getModule().invalidateAccoutPane();
        }
    }

    @Override
    public boolean closeTab(final String tabToClose) {

        if (EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getEBICRMProductPane() != null) {
                EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_CALENDAR").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemCalendarModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCalendarModule", "ebiToolBar")).setSelected(false);
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getEBICRMCampaign() != null) {
                EBISystem.getModule().getEBICRMCampaign().getDataControlCampaign().dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getProsolPane() != null) {
                EBISystem.getModule().getProsolPane().dataControlProsol.dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_PROJECT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getProjectPane() != null) {
                EBISystem.getModule().getProjectPane().getDataControlProject().dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getInvoicePane() != null) {
                EBISystem.getModule().getInvoicePane().getDataControlInvoice().dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getModule().getAccountPane() != null) {
                EBISystem.getModule().getAccountPane().getDataControlAccount().dataNew();
            }
        }
        return true;
    }

    public void showCheckableTab(final String file) {
        if (file.lastIndexOf(".xml") != -1) {
            EBISystem.gui().loadGUI(file);
            EBISystem.gui().showGUI();
        }
    }

    public void setCompanyPOSID() {
        int ix;
        if (EBISystem.getInstance().getCompany() != null) {
            ix = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.getInstance().getCompany().getName());
        } else {
            ix = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_COMPANY"));
        }
        if (ix != -1) {
            this.companyPOSID = ix;
        }
    }
}
