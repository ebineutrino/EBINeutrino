package ebiCRM.gui.component;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.CloseableTabbedPaneListener;

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
        if (EBISystem.getCRMModule().getEBICRMProductPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT")));
        }
    }

    public void closeCampaignContainer() {
        if (EBISystem.getCRMModule().getEBICRMCampaign() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN")));
        }
    }

    public void closeProsolContainer() {
        if (EBISystem.getCRMModule().getProsolPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL")));
        }
    }

    public void closeProjectContainer() {
        if (EBISystem.getCRMModule().getProjectPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT")));
        }
    }

    public void showClosableProductContainer() {
        EBISystem.gui().loadGUI("Product/productGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getEBICRMProductPane();
        EBISystem.getCRMModule().getEBICRMProductPane().showProduct();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableCampaignContainer() {
        EBISystem.gui().loadGUI("Campaign/campaignGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getEBICRMCampaign();
        EBISystem.getCRMModule().getEBICRMCampaign().showCampaign();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableProsolContainer() {
        EBISystem.gui().loadGUI("CRMProblemSolution/problemSolutionGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getProsolPane();
        EBISystem.getCRMModule().getProsolPane().showProSol();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableProjectContainer() {
        EBISystem.gui().loadGUI("Project/projectGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getProjectPane();
        EBISystem.getCRMModule().getProjectPane().showProject();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableInvoiceContainer() {
        EBISystem.gui().loadGUI("Invoice/invoiceGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getInvoicePane();
        EBISystem.getCRMModule().getInvoicePane().dataShow(-1);
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void showClosableAccountContainer() {
        EBISystem.gui().loadGUI("AccountStack/accountGUI.xml");
        EBISystem.gui().showGUI();

        EBISystem.getCRMModule().getAccountPane();
        EBISystem.getCRMModule().getAccountPane().showAccount();
        final int id = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT"));
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(id);
    }

    public void closeInvoiceContainer() {
        if (EBISystem.getCRMModule().getInvoicePane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE")));
            EBISystem.getCRMModule().getInvoicePane().dataControlInvoice.dataNew();
        }
    }

    public void closeAccountContainer() {
        if (EBISystem.getCRMModule().getAccountPane() != null) {
            EBISystem.getInstance().getIEBIContainerInstance().removeContainer(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT")));
            EBISystem.getCRMModule().getAccountPane().dataControlAccount.dataNew();
        }
    }

    @Override
	public boolean closeTab(final String tabToClose) {

        if (EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getCRMModule().getEBICRMProductPane() != null) {
                EBISystem.getCRMModule().getEBICRMProductPane().dataControlProduct.dataNew();
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
            if (EBISystem.getCRMModule().getEBICRMCampaign() != null) {
                EBISystem.getCRMModule().getEBICRMCampaign().dataControlCampaign.dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getCRMModule().getProsolPane() != null) {
                EBISystem.getCRMModule().getProsolPane().dataControlProsol.dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_PROJECT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getCRMModule().getProjectPane() != null) {
                EBISystem.getCRMModule().getProjectPane().dataControlProject.dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getCRMModule().getInvoicePane() != null) {
                EBISystem.getCRMModule().getInvoicePane().dataControlInvoice.dataNew();
            }
        }

        if (EBISystem.i18n("EBI_LANG_C_TAB_ACCOUNT").equals(tabToClose)) {
            if (EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar") != null) {
                ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setSelected(false);
            }
            if (EBISystem.getCRMModule().getAccountPane() != null) {
                EBISystem.getCRMModule().getAccountPane().dataControlAccount.dataNew();
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
        if (EBISystem.getInstance().company != null) {
            ix = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.getInstance().company.getName());
        } else {
            ix = EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_COMPANY"));
        }
        if (ix != -1) {
            this.companyPOSID = ix;
        }
    }
}
