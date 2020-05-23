package org.modules.views.component.listener;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBIDialogSearchCompany;
import org.modules.views.dialogs.EBIDialogSearchContact;
import org.modules.views.settings.CRMSetting;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EBICRMActionListener {

    public EBICRMActionListener() {
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
                if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                    EBISystem.getModule().resetUI(false, false);
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
                if (EBISystem.getModule().ebiContainer.getTabInstance().getTitleAt(EBISystem.getModule().ebiContainer.getSelectedTab()) != null) {
                    EBISystem.getModule().storeAutomate.storeFromSelectedTab();
                }
            }
        };
    }

    protected ActionListener windowShowProductTab() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).isSelected()) {
                    EBISystem.getModule().ebiContainer.showClosableProductContainer();
                } else {
                    if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                        EBISystem.getModule().ebiContainer.closeProductContainer();
                    } else {
                        ((JToggleButton) EBISystem.gui().getToolBarComponent("toolbarItemProductModule", "ebiToolBar")).setSelected(true);
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
                    EBISystem.getModule().ebiContainer.showClosableProsolContainer();
                } else {
                    if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                        EBISystem.getModule().ebiContainer.closeProsolContainer();
                    } else {
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
                    EBISystem.getModule().ebiContainer.showClosableProjectContainer();
                } else {
                    if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                        EBISystem.getModule().ebiContainer.closeProjectContainer();
                    } else {
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
                    EBISystem.getModule().ebiContainer.showClosableInvoiceContainer();
                } else {
                    if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                        EBISystem.getModule().ebiContainer.closeInvoiceContainer();
                    } else {
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
                    EBISystem.getModule().ebiContainer.showClosableAccountContainer();
                } else {
                    if (EBISystem.getSecurity().checkCanReleaseModules() == true) {
                        EBISystem.getModule().ebiContainer.closeAccountContainer();
                    } else {
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
                if (EBISystem.getInstance().getCompany() != null) {
                    new EBICRMHistoryView(EBISystem.getModule().
                            hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Company")).setVisible();
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
                if (EBISystem.getInstance().getCompany() != null) {
                    int index = EBISystem.getModule().ebiContainer.getTabInstance().getSelectedIndex();
                    String componentName = EBISystem.getInstance().getIEBIContainerInstance().getComponentName(index);
                    if (EBISystem.getModule().storeAutomate.deleteFromSelectedTab(EBISystem.gui().vpanel(componentName).getID())) {
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
                    pass = EBISystem.getSecurity().secureModule();
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
                if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                        || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
                    pass = true;
                } else {
                    pass = EBISystem.getSecurity().secureModule();
                }
                if (pass) {
                    final Map<String, Object> map = new HashMap();
                    if (EBISystem.getInstance().getCompany() != null) {
                        map.put("COMPANYID", EBISystem.getInstance().getCompany().getCompanyid());
                    }
                    EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map);
                }
            }
        };
    }

    public void showSearchContactDialog() {
        final EBIDialogSearchContact searchContact = new EBIDialogSearchContact(true);
        searchContact.setVisible();
    }
}
