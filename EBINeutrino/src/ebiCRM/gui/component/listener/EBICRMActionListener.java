package ebiCRM.gui.component.listener;

import ebiCRM.EBICRMModule;
import ebiCRM.gui.dialogs.EBICRMHistoryView;
import ebiCRM.gui.dialogs.EBIDialogSearchCompany;
import ebiCRM.gui.dialogs.EBIDialogSearchContact;
import ebiCRMSetting.CRMSetting;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBISecurity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EBICRMActionListener {

    private EBICRMModule ebiModule = null;
    public IEBISecurity iSecurity = null;

    public EBICRMActionListener(final EBICRMModule module) {
        ebiModule = module;
        iSecurity = EBISystem.getInstance().getIEBISecurityInstance();
    }

    /**
     * ActionListener new Company CRM Reset form
     *
     * @return
     */
    protected ActionListener newListenerAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (iSecurity.checkCanReleaseModules() == true) {
                    ebiModule.resetUI(false, false);
                }
            }
        };
    }

    /**
     * ActionListener save company
     *
     * @return
     */
    protected ActionListener saveListenerAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (ebiModule.ebiContainer.getTabInstance().getTitleAt(ebiModule.ebiContainer.getSelectedTab()) != null) {
                    ebiModule.storeAutomate.storeFromSelectedTab();
                }
            }
        };
    }

    protected ActionListener windowShowProductTab() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableProductContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeProductContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };
    }


    protected ActionListener windowShowCampaignTab() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableCampaignContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeCampaignContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemCampaignModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };
    }

    protected ActionListener windowShowProsolTab() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableProsolContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeProsolContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProSolModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };
    }

    protected ActionListener windowShowProjectTab() {

        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableProjectContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeProjectContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProjectModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };
    }

    //windowShowInvoiceTab
    protected ActionListener windowShowInvoiceTab() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableInvoiceContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeInvoiceContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemInvoiceModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };

    }

    //windowShowAccountTab
    protected ActionListener windowsShowAccountTab() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).isSelected()) {
                    ebiModule.ebiContainer.showClosableAccountContainer();
                } else {
                    if (iSecurity.checkCanReleaseModules() == true) {
                        ebiModule.ebiContainer.closeAccountContainer();
                    }else{
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemAccountModule", "ebiToolBar")).setSelected(true);
                    }
                }
            }
        };

    }

    /**
     * ActionListener search company
     *
     * @return
     */
    protected ActionListener searchCompanyAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                new EBIDialogSearchCompany(false, false);
            }
        };
    }

    protected ActionListener searchCompanyHistoryAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if(EBISystem.getInstance().company != null) {
                    new EBICRMHistoryView(EBISystem.getCRMModule().hcreator.retrieveDBHistory(EBISystem.getInstance().company.getCompanyid(), "Company")).setVisible();
                }
            }
        };
    }

    /**
     * ActionListener CRM delete company
     *
     * @return
     */
    protected ActionListener deleteListenerAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (EBISystem.getInstance().company != null) {
                    int index = ebiModule.ebiContainer.getTabInstance().getSelectedIndex();
                    String componentName = EBISystem.getInstance().getIEBIContainerInstance().getComponentName(index);
                    if(ebiModule.storeAutomate.deleteFromSelectedTab(EBISystem.gui().vpanel(componentName).getID())) {
                        EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_RECORD_DELETED")).Show(EBIMessage.INFO_MESSAGE);
                    }
                }
            }
        };
    }

    protected ActionListener crmSettingAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                boolean pass;
                if (EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
                    pass = true;
                } else {
                    pass = iSecurity.secureModule();
                }
                if (pass) {
                    final CRMSetting setting = new CRMSetting();
                    setting.setVisible();
                }
            }
        };
    }

    /**
     * ActionListener search contact
     *
     * @return
     */

    protected ActionListener searchContactAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                showSearchContactDialog();
            }
        };
    }

    /**
     * ActionListener printReport
     *
     * @return
     */
    protected ActionListener printReportAction() {
        return new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                boolean pass;
                if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint() ||
                        EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
                    pass = true;
                } else {
                    pass = iSecurity.secureModule();
                }
                if (pass) {
                    final Map<String, Object> map = new HashMap();
                    if (EBISystem.getInstance().company != null) {
                        map.put("COMPANYID", EBISystem.getInstance().company.getCompanyid());
                    }
                    EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map);
                }
            }
        };
    }

    public void showSearchContactDialog() {
        final EBIDialogSearchContact searchContact = new EBIDialogSearchContact( true);
        searchContact.setVisible();
    }
}
