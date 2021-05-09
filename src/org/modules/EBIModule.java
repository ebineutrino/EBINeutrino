package org.modules;

import org.modules.views.EBICRMInvoiceView;
import org.modules.views.EBICRMProductView;
import org.modules.views.EBICRMAccountStackView;
import org.modules.views.EBICRMProblemSolutionView;
import org.modules.views.EBICRMServiceView;
import org.modules.views.EBICRMContactView;
import org.modules.views.EBICRMCompanyView;
import org.modules.views.EBICRMOfferView;
import org.modules.views.EBICRMPlanningView;
import org.modules.views.EBICRMOrderView;
import org.modules.views.EBIMeetingProtocolView;
import org.modules.views.EBICRMBankView;
import org.modules.views.EBICRMCompanyActivityView;
import org.modules.views.EBICRMOpportunityView;
import org.modules.views.EBICRMLeadsView;
import org.modules.views.EBICRMSummaryView;
import org.modules.views.EBICRMAddressView;
import org.modules.utils.EBICRMAutomate;
import org.modules.utils.EBICRMDynamicFunctionalityMethods;
import org.modules.controls.component.EBICRMTabcontrol;
import org.modules.controls.component.EBICRMToolBar;
import org.modules.utils.EBIAllertTimer;
import org.modules.utils.EBICRMHistoryCreator;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.modules.utils.EBITimerTaskFixRate;
import org.sdk.EBISystem;
import org.sdk.arbitration.EBIArbCallback;
import org.sdk.arbitration.EBIArbitration;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIExtension;
import org.sdk.interfaces.IEBIModule;
import org.sdk.interfaces.IEBIStoreInterface;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companyhirarchie;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class EBIModule implements IEBIModule, IEBIExtension, IEBIStoreInterface {

    private EBICRMCompanyView companyPane = null;
    private EBICRMContactView contactPane = null;
    private EBIMeetingProtocolView meetingReport = null;
    private EBICRMAddressView addressPane = null;
    private EBICRMOpportunityView opportunityPane = null;
    private EBICRMCompanyActivityView activitiesPane = null;
    private EBICRMPlanningView projectPane = null;
    private EBICRMBankView bankPane = null;
    private EBICRMOfferView offerPane = null;
    private EBICRMOrderView orderPane = null;
    private EBICRMLeadsView leadsPane = null;
    private EBICRMAccountStackView accountPane = null;
    private EBICRMSummaryView summaryPane = null;
    private EBICRMProductView productPane = null;
    private EBICRMServiceView servicePane = null;
    private EBICRMProblemSolutionView prosolPane = null;
    private EBICRMInvoiceView invoicePane = null;
    public String beginChar = "";
    public static boolean RELOAD = false;
    public static boolean isExistCompany = false;
    public EBICRMTabcontrol ebiContainer = null;
    public EBICRMDynamicFunctionalityMethods dynMethod = null;
    public EBICRMToolBar crmToolBar = null;
    public int EBICRM_SESSION = 0;
    public EBICRMHistoryCreator hcreator = null;
    public static final Logger logger = Logger.getLogger(EBIModule.class.getName());
    public EBICRMAutomate storeAutomate = null;
    public EBIAllertTimer allertTimer = null;

    @Override
    public Object getActiveModule() {
        return EBIModule.this;
    }

    /**
     * Save a CRM Record
     */
    @Override
    public boolean ebiSave(boolean check) {
        boolean ret = true;
        EBISystem.showInActionStatus("Company");
        if (check) {
            if (!checkCompany()) {
                return false;
            }
        }
        if (updateCompany()) {
            if (RELOAD) {
                createUI(EBISystem.getInstance().getCompany().getCompanyid(), true);
            }
            RELOAD = false;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * Update a CRM Record
     */
    @Override
    public boolean ebiUpdate(boolean check) {
        EBISystem.showInActionStatus("Company");
        boolean ret = true;

        if (check) {
            if (!checkCompany()) {
                return false;
            }
        }

        if (updateCompany()) {
            if (RELOAD) {
                createUI(EBISystem.getInstance().getCompany().getCompanyid(), true);
            }
            RELOAD = false;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * Delete CRM Record
     */
    @Override
    public boolean ebiDelete(boolean check) {
        return true;
    }

    /**
     * CRM EBIModule start point
     */
    @Override
    public boolean ebiMain(final Object obj) {

        try {
            isExistCompany = false;
            RELOAD = false;

            EBISystem.getInstance().gui().loadProject("project.xml");
            if (!EBISystem.getInstance().gui().isToolBarEmpty()) {
                crmToolBar.setCRMToolBar();
            }

            if (EBISystem.gui().existView("Account")) {
                this.getAccountPane();
            } else if ("AccountStack/accountGUI.xml".equals(EBISystem.registeredModule.get(0).toString())) {
                ebiContainer.showClosableAccountContainer();
                crmToolBar.enableToolButtonAccount();
            }

            if (EBISystem.gui().existView("Prosol")) {
                this.getProsolPane();
            } else if ("CRMProblemSolution/problemSolutionGUI.xml".equals(EBISystem.registeredModule.get(0).toString())) {
                ebiContainer.showClosableProsolContainer();
                crmToolBar.enableToolButtonProsol();
            }

            if (EBISystem.getInstance().gui().existView("Invoice")) {
                this.getInvoicePane();
            } else if ("Invoice/invoiceGUI.xml".equals(EBISystem.registeredModule.get(0).toString())) {
                ebiContainer.showClosableInvoiceContainer();
                crmToolBar.enableToolButtonInvoice();
            }

            if (EBISystem.getInstance().gui().existView("Product")) {
                this.getEBICRMProductPane();
            } else if ("Product/productGUI.xml".equals(EBISystem.registeredModule.get(0).toString())) {
                ebiContainer.showClosableProductContainer();
                crmToolBar.enableToolButtonProductModule();
            }

            if (EBISystem.getInstance().gui().existView("Project")) {
                this.getProjectPane();
            } else if ("Project/projectGUI.xml".equals(EBISystem.registeredModule.get(0).toString())) {
                ebiContainer.showClosableProductContainer();
                crmToolBar.enableToolButtonProductModule();
            }

            EBIArbitration.arbitrate().begin("CRM_SETTINGS", new EBIArbCallback() {
                @Override
                public boolean callback(final Thread currentThread) {
                    dynMethod.initComboBoxes(true);
                    ebiContainer.setCompanyPOSID();
                    return true;
                }
            });

            EBIArbitration.arbitrate().begin("INIT_CRM", new EBIArbCallback() {
                @Override
                public boolean callback(final Thread currentThread) {

                    EBIArbitration.arbitrate().waitJobDone("CRM_SETTINGS");

                    if (obj != null) {
                        if (Integer.parseInt(obj.toString()) != -1) {
                            ebiContainer.setTabPanels(true);
                            createUI(Integer.parseInt(obj.toString()), true);
                        } else {
                            ebiContainer.setTabPanels(false);
                        }
                        showClosableTabs();
                    } else {
                        ebiContainer.setTabPanels(false);
                    }

                    //load setted crm / erp modules
                    loadModule();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            if (summaryPane != null) {
                                try {
                                    Thread.sleep(1000);
                                } catch (final InterruptedException e) {
                                    e.printStackTrace();
                                }
                                summaryPane.searchSummary();
                            }

                            allertTimer = new EBIAllertTimer();
                            allertTimer.setUpAvailableTimer();

                            final java.util.Timer timerFix = new java.util.Timer();
                            timerFix.scheduleAtFixedRate(new EBITimerTaskFixRate(EBIModule.this), 12000, 32000);
                        }
                    });
                    return true;
                }
            });

        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error("EBI Neutrino CRM Error:", ex.fillInStackTrace());
        }
        return true;
    }

    private void loadModule() {
        if (EBISystem.gui().existView("Summary")) {
            EBISystem.getModule().getSummaryPane();
        }
        if (EBISystem.gui().existView("Leads")) {
            EBISystem.getModule().getLeadPane();
        }
        if (EBISystem.gui().existView("Company")) {
            EBISystem.getModule().getCompanyPane();
        }
        if (EBISystem.gui().existView("Contact")) {
            EBISystem.getModule().getContactPane();
        }
        if (EBISystem.gui().existView("Address")) {
            EBISystem.getModule().getAddressPane();
        }
        if (EBISystem.gui().existView("Bank")) {
            EBISystem.getModule().getBankdataPane();
        }
        if (EBISystem.gui().existView("MeetingCall")) {
            EBISystem.getModule().getMeetingProtocol();
        }
        if (EBISystem.gui().existView("Activity")) {
            EBISystem.getModule().getActivitiesPane();
        }
        if (EBISystem.gui().existView("Opportunity")) {
            EBISystem.getModule().getOpportunityPane();
        }
        if (EBISystem.gui().existView("Offer")) {
            EBISystem.getModule().getOfferPane();
        }
        if (EBISystem.gui().existView("Order")) {
            EBISystem.getModule().getOrderPane();
        }
        if (EBISystem.gui().existView("Service")) {
            EBISystem.getModule().getServicePane();
        }
    }

    /**
     * restore closable tab / module after reload a CRM
     */
    private void showClosableTabs() {
        final int size = EBISystem.gui().getHashTabtoFile().size();
        for (int i = ebiContainer.getTabInstance().getTabCount(); i < size; i++) {
            final String file = EBISystem.gui().getHashTabtoFile().get(i);
            if (file == null) {
                return;
            }
            String toParse;
            if (file.lastIndexOf('/') != -1) {
                toParse = file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));
            } else {
                toParse = file;
            }

            if (toParse.equals("productGUI")) {
                crmToolBar.enableToolButtonProductModule();
                ebiContainer.showClosableProductContainer();
            } else if (toParse.equals("problemSolutionGUI")) {
                crmToolBar.enableToolButtonProsol();
                ebiContainer.showClosableProsolContainer();
            } else if (toParse.equals("projectGUI")) {
                crmToolBar.enableToolButtonProject();
                ebiContainer.showClosableProjectContainer();
            } else if (toParse.equals("invoiceGUI")) {
                crmToolBar.enableToolButtonInvoice();
                ebiContainer.showClosableInvoiceContainer();
            } else if (toParse.equals("accountGUI")) {
                crmToolBar.enableToolButtonAccount();
                ebiContainer.showClosableAccountContainer();
            } else {
                ebiContainer.showCheckableTab(EBISystem.gui().getHashTabtoFile().get(i));
            }
        }
    }

    /**
     * remove module
     *
     * @return
     */
    @Override
    public Object ebiRemove() {
        companyPane = null;
        contactPane = null;
        addressPane = null;
        bankPane = null;
        projectPane = null;
        meetingReport = null;
        activitiesPane = null;
        opportunityPane = null;
        accountPane = null;
        offerPane = null;
        orderPane = null;
        leadsPane = null;
        summaryPane = null;
        servicePane = null;
        productPane = null;
        invoicePane = null;
        prosolPane = null;
        return EBISystem.getInstance().getCompany() == null ? -1 : EBISystem.getInstance().getCompany().getCompanyid();
    }

    @Override
    public void onLoad() {
        EBISystem.hibernate().openHibernateSession("EBICRM_SESSION");
        hcreator = new EBICRMHistoryCreator(this);
        dynMethod = new EBICRMDynamicFunctionalityMethods();
        crmToolBar = new EBICRMToolBar();
        ebiContainer = new EBICRMTabcontrol();
        storeAutomate = new EBICRMAutomate();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EBISystem.getInstance().getDataStore("CRM", "ebiOnLoad");
            }
        });
    }

    @Override
    public void onAfterLoad() {
        afterLoadDelegate("CRM");
    }

    @Override
    public void onExit() {
    }

    public void invalidateProductPane() {
        productPane = null;
    }

    private void afterLoadDelegate(final String nameSpace) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EBIArbitration.arbitrate().begin("AfterLoadThread", new EBIArbCallback() {
                    @Override
                    public boolean callback(Thread currentThread) {
                        EBIArbitration.arbitrate().waitJobDone("CRM_INITIALIZE");
                        if (EBISystem.getInstance().containDataStore(nameSpace, "ebiAfterLoad")) {
                            EBISystem.getInstance().getDataStore(nameSpace, "ebiAfterLoad");
                            System.out.println("called xy");
                        }
                        return true;
                    }
                });
            }
        });
    }

    public EBICRMProductView getEBICRMProductPane() {
        if (productPane == null) {
            productPane = (EBICRMProductView) EBISystem.getInstance().getMappedBean(EBICRMProductView.class);
            EBISystem.getInstance().getDataStore("Product", "ebiOnLoad");
            productPane.initialize(true);
            productPane.initializeAction();
            afterLoadDelegate("Product");
        }

        return productPane;
    }

    public EBICRMCompanyView getCompanyPane() {
        if (companyPane == null) {
            companyPane = (EBICRMCompanyView) EBISystem.getInstance().getMappedBean(EBICRMCompanyView.class);
            EBISystem.getInstance().getDataStore("Company", "ebiOnLoad");
            companyPane.initializeAction();
            afterLoadDelegate("Company");
        }
        return companyPane;
    }

    public EBIMeetingProtocolView getMeetingProtocol() {
        if (meetingReport == null) {
            meetingReport = (EBIMeetingProtocolView) EBISystem.getInstance().getMappedBean(EBIMeetingProtocolView.class);
            EBISystem.getInstance().getDataStore("Meeting", "ebiOnLoad");
            meetingReport.initializeAction();
            afterLoadDelegate("Meeting");
        }
        return meetingReport;
    }

    public EBICRMContactView getContactPane() {
        if (contactPane == null) {
            contactPane = (EBICRMContactView) EBISystem.getInstance().getMappedBean(EBICRMContactView.class);
            EBISystem.getInstance().getDataStore("Contact", "ebiOnLoad");
            contactPane.initializeAction();
            afterLoadDelegate("Contact");
        }
        return contactPane;
    }

    public EBICRMAddressView getAddressPane() {
        if (addressPane == null) {
            addressPane = (EBICRMAddressView) EBISystem.getInstance().getMappedBean(EBICRMAddressView.class);
            EBISystem.getInstance().getDataStore("Address", "ebiOnLoad");
            addressPane.initializeAction();
            afterLoadDelegate("Address");
        }
        return addressPane;
    }

    public EBICRMOpportunityView getOpportunityPane() {
        if (opportunityPane == null) {
            opportunityPane = (EBICRMOpportunityView) EBISystem.getInstance().getMappedBean(EBICRMOpportunityView.class);
            EBISystem.getInstance().getDataStore("Opportunity", "ebiOnLoad");
            opportunityPane.initializeAction();
            afterLoadDelegate("Opportunity");
        }
        return opportunityPane;
    }

    public EBICRMCompanyActivityView getActivitiesPane() {
        if (activitiesPane == null) {
            activitiesPane = (EBICRMCompanyActivityView) EBISystem.getInstance().getMappedBean(EBICRMCompanyActivityView.class);
            EBISystem.getInstance().getDataStore("Activity", "ebiOnLoad");
            activitiesPane.initializeAction();
            afterLoadDelegate("Activity");
        }
        return activitiesPane;
    }

    public EBICRMOfferView getOfferPane() {
        if (offerPane == null) {
            offerPane = (EBICRMOfferView) EBISystem.getInstance().getMappedBean(EBICRMOfferView.class);
            offerPane.initializeAction();
            afterLoadDelegate("Offer");
        }
        return offerPane;
    }

    public EBICRMLeadsView getLeadPane() {
        if (leadsPane == null) {
            leadsPane = (EBICRMLeadsView) EBISystem.getInstance().getMappedBean(EBICRMLeadsView.class);
            leadsPane.initialize(true);
            leadsPane.initializeAction();
            afterLoadDelegate("Lead");
        }
        return leadsPane;
    }

    public EBICRMOrderView getOrderPane() {
        if (orderPane == null) {
            orderPane = (EBICRMOrderView) EBISystem.getInstance().getMappedBean(EBICRMOrderView.class);
            orderPane.initializeAction();
            afterLoadDelegate("Order");
        }
        return orderPane;
    }

    public EBICRMServiceView getServicePane() {
        if (servicePane == null) {
            servicePane = (EBICRMServiceView) EBISystem.getInstance().getMappedBean(EBICRMServiceView.class);
            servicePane.initializeAction();
            afterLoadDelegate("Service");
        }
        return servicePane;
    }

    public EBICRMProblemSolutionView getProsolPane() {
        if (prosolPane == null) {
            prosolPane = (EBICRMProblemSolutionView) EBISystem.getInstance().getMappedBean(EBICRMProblemSolutionView.class);
            prosolPane.initialize(true);
            prosolPane.initializeAction();
            afterLoadDelegate("Prosol");
        }
        return prosolPane;
    }

    public void invalidateProsolPane() {
        prosolPane = null;
    }

    public EBICRMPlanningView getProjectPane() {
        if (projectPane == null) {
            projectPane = (EBICRMPlanningView) EBISystem.getInstance().getMappedBean(EBICRMPlanningView.class);
            projectPane.initialize();
            projectPane.initializeAction();
            afterLoadDelegate("Project");
        }
        return projectPane;
    }

    public void invalidateProjectPane() {
        projectPane = null;
    }

    public EBICRMInvoiceView getInvoicePane() {
        if (invoicePane == null) {
            invoicePane = (EBICRMInvoiceView) EBISystem.getInstance().getMappedBean(EBICRMInvoiceView.class);
            invoicePane.initialize(true);
            invoicePane.initializeAction();
            afterLoadDelegate("Invoice");
        }
        return invoicePane;
    }

    public void invalidateInvoicePane() {
        invoicePane = null;
    }

    public EBICRMAccountStackView getAccountPane() {
        if (accountPane == null) {
            accountPane = (EBICRMAccountStackView) EBISystem.getInstance().getMappedBean(EBICRMAccountStackView.class);
            accountPane.initialize(true);
            accountPane.initializeAction();
            afterLoadDelegate("Account");
        }
        return accountPane;
    }

    public void invalidateAccoutPane() {
        accountPane = null;
    }

    public EBICRMSummaryView getSummaryPane() {
        if (summaryPane == null) {
            summaryPane = (EBICRMSummaryView) EBISystem.getInstance().getMappedBean(EBICRMSummaryView.class);
            summaryPane.initialize();
            summaryPane.initializeAction();
            summaryPane.restoreProperties();
            afterLoadDelegate("Summary");
        }
        return summaryPane;
    }

    public EBICRMBankView getBankdataPane() {
        if (bankPane == null) {
            bankPane = (EBICRMBankView) EBISystem.getInstance().getMappedBean(EBICRMBankView.class);
            bankPane.initializeAction();
            afterLoadDelegate("Bank");
        }
        return bankPane;
    }

    public void resetUI(final boolean enableTab, final boolean reloading) {

        EBIArbitration.arbitrate().begin("CRM_INITIALIZE", new EBIArbCallback() {
            @Override
            public boolean callback(Thread currentThread) {
                try {
                    isExistCompany = false;
                    EBISystem.getInstance().setCompany(new Company());
                    EBISystem.getInstance().getCompany().setCompanyid(-1);
                    EBISystem.isSaveOrUpdate = false;
                    EBISystem.canRelease = true;

                    crmToolBar.enableToolbarButton(false);
                    // crmMenu.setDeleteItemEnabled(false);

                    final int selectedTab = ebiContainer.getSelectedTab();
                    if (EBISystem.gui().existView("Company")) {
                        getCompanyPane().initialize();
                        EBISystem.getInstance().gui().vpanel("Company").setID(-1);
                    }
                    if (EBISystem.gui().existView("Contact")) {
                        getContactPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("Address")) {
                        getAddressPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("Bank")) {
                        getBankdataPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("MeetingCall")) {
                        getMeetingProtocol().initialize(true);
                    }
                    if (EBISystem.gui().existView("Activity")) {
                        getActivitiesPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("Opportunity")) {
                        getOpportunityPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("Offer")) {
                        getOfferPane().initialize(true);
                    }
                    if (EBISystem.gui().existView("Order")) {
                        getOrderPane().initialize(true);
                    }

                    if (EBISystem.gui().existView("Service")) {
                        getServicePane().initialize(true);
                    }

                    if (reloading) {
                        if (EBISystem.gui().existView("Summary")) {
                            getSummaryPane().initialize();
                        }
                        if (EBISystem.gui().existView("Leads")) {
                            getLeadPane().initialize(true);
                        }
                    }

                    if (ebiContainer.getTabInstance().getTabCount() > 3) {
                        for (int i = EBISystem.gui().getProjectModuleEnabled(); i < EBISystem.gui().getProjectModuleCount(); i++) {
                            ebiContainer.getTabInstance().setEnabledAt(i, enableTab);
                        }

                        if (enableTab) {
                            ebiContainer.setSelectedTab(selectedTab);
                        } else {
                            if (EBISystem.getInstance().getIEBIContainerInstance()
                                    .getIndexByTitle(EBISystem.i18n("EBI_LANG_C_COMPANY")) > -1
                                    && EBISystem.getInstance().getIEBIContainerInstance()
                                            .getIndexByTitle(EBISystem.i18n("EBI_LANG_C_COMPANY")) <= EBISystem.getInstance()
                                    .getIEBIContainerInstance().getTabCount()) {

                                ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_COMPANY")));
                            }
                        }
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage(), ex.fillInStackTrace());
                }
                return true;
            }
        });
    }

    public boolean saveCompany(boolean checkCompany) {
        try {
            EBISystem.showInActionStatus("Company");
            if (EBISystem.getInstance().getCompany() != null) {
                ebiUpdate(checkCompany);
            } else {
                ebiSave(checkCompany);
            }
            EBISystem.canRelease = true;
        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error("Error save record", ex.fillInStackTrace());
        }
        return true;
    }

    /**
     * Check if obligatory company fields are filled
     *
     * @return
     */
    private boolean checkCompany() {
        if ("".equals(EBISystem.getInstance().gui().textField("nameText", "Company").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME1")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if (EBISystem.getInstance().gui().combo("categoryText", "Company").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_CATEGORY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("00".equals(EBISystem.getInstance().gui().textField("internalNrText", "Company").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_CRM_INTERNAL_NUMBER_EXHAUSTED")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } else if ("-1".equals(EBISystem.getInstance().gui().textField("internalNrText", "Company").getText())
                && "".equals(EBISystem.getInstance().gui().textField("custNrText", "Company").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_COMPANY_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if (EBISystem.isSaveOrUpdate == false) {
            if (dynMethod.findCustomerNumber(EBISystem.getInstance().gui().textField("custNrText", "Company").getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NUMBER_EXIST")).Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /**
     * Save or Update a loaded company
     *
     * @return
     */
    public boolean updateCompany() {
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (!EBISystem.isSaveOrUpdate) {
                EBISystem.getInstance().setCompany(new Company());
                RELOAD = true;
                EBISystem.getInstance().getCompany().setCreatedfrom(EBISystem.ebiUser);
                EBISystem.getInstance().getCompany().setCreateddate(new Date());
            } else {
                createHistory(EBISystem.getInstance().getCompany());
                EBISystem.getInstance().getCompany().setChangeddate(new Date());
                EBISystem.getInstance().getCompany().setChangedfrom(EBISystem.ebiUser);
            }

            if (!"-1".equals(EBISystem.gui().textField("internalNrText", "Company").getText())) {
                EBISystem.getInstance().getCompany().setCompanynumber(Integer.parseInt(EBISystem.gui()
                        .textField("internalNrText", "Company").getText().replace(beginChar, "")));
            } else {
                EBISystem.getInstance().getCompany().setCompanynumber(-1);
            }

            EBISystem.getInstance().getCompany().setBeginchar(beginChar);
            EBISystem.getInstance().getCompany().setName(EBISystem.gui().textField("nameText", "Company").getText());
            EBISystem.getInstance().getCompany().setName2(EBISystem.getInstance().gui().textField("name1Text", "Company").getText());

            if (EBISystem.getInstance().gui().combo("categoryText", "Company").getSelectedItem() != null) {
                EBISystem.getInstance().getCompany().setCategory(EBISystem.getInstance().gui().combo("categoryText", "Company").getSelectedItem().toString());
                EBISystem.getInstance().getCompany().setCooperation(EBISystem.getInstance().gui().combo("cooperationText", "Company").getSelectedItem().toString());
            }

            EBISystem.getInstance().getCompany().setPhone(EBISystem.getInstance().gui().textField("telephoneText", "Company").getText());
            EBISystem.getInstance().getCompany().setFax(EBISystem.getInstance().gui().textField("faxText", "Company").getText());
            EBISystem.getInstance().getCompany().setTaxnumber(EBISystem.getInstance().gui().textField("taxIDText", "Company").getText());
            EBISystem.getInstance().getCompany().setEmployee(EBISystem.getInstance().gui().textField("employeeText", "Company").getText());
            EBISystem.getInstance().getCompany().setWeb(EBISystem.getInstance().gui().textField("internetText", "Company").getText());
            EBISystem.getInstance().getCompany().setEmail(EBISystem.getInstance().gui().textField("emailText", "Company").getText());
            EBISystem.getInstance().getCompany().setCustomernr(EBISystem.getInstance().gui().textField("custNrText", "Company").getText());

            if (EBISystem.getInstance().gui().combo("classificationText", "Company").getSelectedItem() != null) {
                EBISystem.getInstance().getCompany().setQualification(EBISystem.getInstance().gui().combo("classificationText", "Company").getSelectedItem().toString());
            }

            EBISystem.getInstance().getCompany().setIslock(EBISystem.getInstance().gui().getCheckBox("lockCompany", "Company").isSelected());
            // Save company hierarchy
            if (this.companyPane.listH != null && this.companyPane.listH.size() > 0) {
                final Iterator iterH = this.companyPane.listH.iterator();
                while (iterH.hasNext()) {
                    final Companyhirarchie hi = (Companyhirarchie) iterH.next();
                    EBISystem.getInstance().getCompany().getCompanyhirarchies().add(hi);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(hi);
                }
            }

            EBISystem.getInstance().getCompany().setDescription(EBISystem.gui().textArea("companyDescription", "Company").getText());
            if (!EBISystem.isSaveOrUpdate) {
                if (checkForMainCompany()) {
                    EBISystem.getInstance().getCompany().setIsactual(true);
                } else {
                    EBISystem.getInstance().getCompany().setIsactual(false);
                }
            }

            if (EBISystem.getInstance().getCompany().getIsactual() != null && EBISystem.getInstance().getCompany().getIsactual()) {
                EBISystem.getInstance().loadStandardCompanyData();
            }

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(EBISystem.getInstance().getCompany());

            if (ebiContainer.companyPOSID != -1) {
                ebiContainer.getTabInstance().setTitleAt(ebiContainer.companyPOSID, EBISystem.getInstance().getCompany().getName());
            }

            EBISystem.isSaveOrUpdate = true;
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

        } catch (final org.hibernate.HibernateException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This method check if the CRM System has a main company
     *
     * @return
     */
    private boolean checkForMainCompany() {

        ResultSet rsSet = null;
        PreparedStatement ps = null;
        try {
            ps = EBISystem.getInstance().iDB()
                    .initPreparedStatement("SELECT ISACTUAL FROM COMPANY WHERE ISACTUAL=?");
            ps.setInt(1, 1);
            rsSet = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            rsSet.last();
            if (rsSet.getRow() == 0) {
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_MAIN_COMPANY_WAS_FOUND_CREATE_ONE"))
                        .Show(EBIMessage.INFO_MESSAGE_YESNO)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (final SQLException e) {
            logger.error("Exception", e.fillInStackTrace());
            e.printStackTrace();
        } finally {

            if (rsSet != null) {
                try {
                    ps.close();
                    rsSet.close();
                } catch (final SQLException e) {
                    logger.error("Exception", e.fillInStackTrace());
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void createHistory(final Company com) {

        final List<String> list = new ArrayList<String>();

        if (EBISystem.getInstance().getCompany().getCreateddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                    + EBISystem.getInstance().getDateToString(EBISystem.getInstance().getCompany().getCreateddate()));
        }

        if (EBISystem.getInstance().getCompany().getCreatedfrom() != null) {
            list.add(EBISystem.getInstance().getCompany().getCreatedfrom() == null
                    ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": "
                    : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": "
                    + EBISystem.getInstance().getCompany().getCreatedfrom());
        }

        if (EBISystem.getInstance().getCompany().getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                    + EBISystem.getInstance().getDateToString(EBISystem.getInstance().getCompany().getChangeddate()));
        }

        if (EBISystem.getInstance().getCompany().getChangedfrom() != null) {
            list.add(EBISystem.getInstance().getCompany().getChangedfrom() == null
                    ? EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": "
                    : EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": "
                    + EBISystem.getInstance().getCompany().getChangedfrom());
        }

        String nh = "";
        if (!String.valueOf(EBISystem.getInstance().getCompany().getCompanynumber())
                .equals(EBISystem.gui().textField("internalNrText", "Company").getText())) {
            nh = "$";
        }

        list.add(String.valueOf(EBISystem.getInstance().getCompany().getCompanynumber()) == null
                ? EBISystem.i18n("EBI_LANG_C_INTERNAL_NUMBER") + ": " + String.valueOf(-1) + nh
                : EBISystem.i18n("EBI_LANG_C_INTERNAL_NUMBER") + ": "
                + String.valueOf(EBISystem.getInstance().getCompany().getCompanynumber()) + nh);

        if (EBISystem.getInstance().getCompany().getName() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_NAME1") + ": "
                    + (EBISystem.getInstance().getCompany().getName()
                            .equals(EBISystem.gui().textField("nameText", "Company").getText())
                    ? EBISystem.getInstance().getCompany().getName()
                    : EBISystem.getInstance().getCompany().getName() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getName2() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_NAME2") + ": "
                    + (EBISystem.getInstance().getCompany().getName2()
                            .equals(EBISystem.gui().textField("name1Text", "Company").getText())
                    ? EBISystem.getInstance().getCompany().getName2()
                    : EBISystem.getInstance().getCompany().getName2() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getCustomernr() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_CUSTOMER_NUMBER") + ": "
                    + (EBISystem.getInstance().getCompany().getCustomernr()
                            .equals(EBISystem.gui().textField("custNrText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getCustomernr()
                    : EBISystem.getInstance().getCompany().getCustomernr() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getCategory() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": "
                    + (EBISystem.getInstance().getCompany().getCategory()
                            .equals(EBISystem.gui().combo("categoryText", "Company").getSelectedItem()
                                    .toString()) == true ? EBISystem.getInstance().getCompany().getCategory()
                            : EBISystem.getInstance().getCompany().getCategory() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getCooperation() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COOPERATION") + ": "
                    + (EBISystem.getInstance().getCompany().getCooperation()
                            .equals(EBISystem.gui().combo("cooperationText", "Company")
                                    .getSelectedItem().toString()) == true
                            ? EBISystem.getInstance().getCompany().getCooperation()
                            : EBISystem.getInstance().getCompany().getCooperation() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getPhone() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": "
                    + (EBISystem.getInstance().getCompany().getPhone().equals(
                            EBISystem.gui().textField("telephoneText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getPhone()
                    : EBISystem.getInstance().getCompany().getPhone() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getFax() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_FAX") + ": "
                    + (EBISystem.getInstance().getCompany().getFax()
                            .equals(EBISystem.gui().textField("faxText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getFax()
                    : EBISystem.getInstance().getCompany().getFax() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getTaxnumber() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_VAT_NR") + ": "
                    + (EBISystem.getInstance().getCompany().getTaxnumber()
                            .equals(EBISystem.gui().textField("taxIDText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getTaxnumber()
                    : EBISystem.getInstance().getCompany().getTaxnumber() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getEmployee() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_EMPLOYEE") + ": "
                    + (EBISystem.getInstance().getCompany().getEmployee().equals(
                            EBISystem.gui().textField("employeeText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getEmployee()
                    : EBISystem.getInstance().getCompany().getEmployee() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getWeb() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_HINTERNET") + ": "
                    + (EBISystem.getInstance().getCompany().getWeb().equals(
                            EBISystem.gui().textField("internetText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getWeb()
                    : EBISystem.getInstance().getCompany().getWeb() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getEmail() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_HEMAIL") + ": "
                    + (EBISystem.getInstance().getCompany().getEmail()
                            .equals(EBISystem.gui().textField("emailText", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getEmail()
                    : EBISystem.getInstance().getCompany().getEmail() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getQualification() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_KLASSIFICATION") + ": "
                    + (EBISystem.getInstance().getCompany().getQualification()
                            .equals(EBISystem.gui().combo("classificationText", "Company")
                                    .getSelectedItem()) == true ? EBISystem.getInstance().getCompany().getQualification()
                            : EBISystem.getInstance().getCompany().getQualification() + "$"));
        }
        if (EBISystem.getInstance().getCompany().getIslock() != null) {
            String chd = "";
            if (EBISystem.getInstance().getCompany().getIslock() == true
                    && !EBISystem.gui().getCheckBox("lockCompany", "Company").isSelected()
                    || EBISystem.getInstance().getCompany().getIslock() == false
                    && EBISystem.gui().getCheckBox("lockCompany", "Company").isSelected()) {
                chd = "$";
            }

            list.add(EBISystem.getInstance().getCompany().getIslock() == true
                    ? EBISystem.i18n("EBI_LANG_C_LOCK") + ": " + String.valueOf(true) + chd
                    : EBISystem.i18n("EBI_LANG_C_LOCK") + ": " + String.valueOf(false) + chd);
        }
        if (EBISystem.getInstance().getCompany().getDescription() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": "
                    + (EBISystem.getInstance().getCompany().getDescription().equals(
                            EBISystem.gui().textArea("companyDescription", "Company").getText()) == true
                    ? EBISystem.getInstance().getCompany().getDescription()
                    : EBISystem.getInstance().getCompany().getDescription() + "$"));
        }
        list.add("*EOR*"); // END OF RECORD
        try {
            hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Company", list));
        } catch (final Exception e) {
            logger.error("Exception", e.fillInStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * Delete a loaded company
     *
     * @return
     */
    public boolean delete() {
        boolean ret = true;
        EBISystem.showInActionStatus("Company");
        try {
            if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD"))
                    .Show(EBIMessage.WARNING_MESSAGE_YESNO) && EBISystem.getInstance().getCompany() != null) {
                try {
                    EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                    EBISystem.hibernate().session("EBICRM_SESSION").delete(EBISystem.getInstance().getCompany());
                    EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                } catch (final HibernateException e) {
                    logger.error("Exception", e.fillInStackTrace());
                    e.printStackTrace();
                } catch (final Exception e) {
                    logger.error("Exception", e.fillInStackTrace());
                    e.printStackTrace();
                }

                resetUI(false, false);
                EBISystem.canRelease = true;
            } else {
                ret = false;
            }
        } catch (final Exception ex) {
            ret = false;
            logger.error("Error delete record: ", ex.fillInStackTrace());
        }
        return ret;
    }

    /**
     * Create GUI and show data
     *
     * @param compNr
     * @param reload
     * @return
     */
    public boolean createUI(final int compNr, final boolean reload) {
        try {

            resetUI(true, reload);
            EBISystem.isSaveOrUpdate = true;
            crmToolBar.enableToolbarButton(true);

            // Load company data
            try {
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Query query = EBISystem.hibernate().session("EBICRM_SESSION").createQuery("from Company c where c.companyid=?1 ");
                query.setParameter(1, compNr);

                final Iterator it = query.iterate();
                if (it.hasNext()) {
                    EBISystem.getInstance().setCompany((Company) it.next());
                    isExistCompany = true;
                    loadCompanyData();
                    loadContactData();
                    loadCompanyAdressData();
                    loadBankData();
                    loadCompanyMeetingProtocol();
                    loadOpportunity();
                    loadActivities();
                    loadOfferData();
                    loadOrderData();
                    loadServiceData();

                    if (EBISystem.getInstance().getCompany() != null) {
                        if (EBISystem.getInstance().getCompany().getIsactual() != null && EBISystem.getInstance().getCompany().getIsactual() == false) {
                            if (EBISystem.gui().getCheckBox("mainContactText", "Contact") != null) {
                                EBISystem.gui().getCheckBox("mainContactText", "Contact").setVisible(false);
                            }
                        } else {
                            if (EBISystem.gui().getCheckBox("mainContactText", "Contact") != null) {
                                EBISystem.gui().getCheckBox("mainContactText", "Contact").setVisible(true);
                            }
                        }
                    }

                    if (EBISystem.getInstance().getCompany().getName() != null) {
                        if (ebiContainer.companyPOSID != -1) {
                            ebiContainer.getTabInstance().setTitleAt(ebiContainer.companyPOSID, EBISystem.getInstance().getCompany().getName());
                        }
                    }
                }

                ebiContainer.getTabInstance().setBackgroundAt(8, new Color(255, 173, 51));
                ebiContainer.getTabInstance().setBackgroundAt(9, new Color(255, 255, 0));
                ebiContainer.getTabInstance().setBackgroundAt(10, new Color(64, 255, 0));
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            } catch (final org.hibernate.HibernateException ex) {
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            } catch (final Exception ex) {
                ex.printStackTrace();
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
            }

            EBISystem.canRelease = true;
        } catch (final Exception ex) {
            logger.error(ex.getMessage(), ex.fillInStackTrace());
        }

        return true;
    }

    /**
     * Load a company
     */
    private void loadCompanyData() {
        EBISystem.gui().vpanel("Company").setID(EBISystem.getInstance().getCompany().getCompanyid());
        if (EBISystem.getInstance().getCompany().getCreateddate() != null) {
            EBISystem.gui().vpanel("Company").setCreatedDate(EBISystem.getInstance().getDateToString(EBISystem.getInstance().getCompany().getCreateddate()));
            EBISystem.gui().vpanel("Company").setCreatedFrom(EBISystem.getInstance().getCompany().getCreatedfrom());
        }

        if (EBISystem.getInstance().getCompany().getChangeddate() != null) {
            EBISystem.gui().vpanel("Company").setChangedDate(EBISystem.getInstance().getDateToString(EBISystem.getInstance().getCompany().getChangeddate()));
            EBISystem.gui().vpanel("Company").setChangedFrom(EBISystem.getInstance().getCompany().getChangedfrom());
        }

        beginChar = EBISystem.getInstance().getCompany().getBeginchar();
        EBISystem.gui().textField("rootText", "Company").requestFocus();
        EBISystem.gui().textField("internalNrText", "Company").setText(String.valueOf(EBISystem.getInstance().getCompany().getCompanynumber() == null ? -1 : EBISystem.getInstance().getCompany().getCompanynumber()));
        EBISystem.gui().textField("nameText", "Company").setText(EBISystem.getInstance().getCompany().getName());
        EBISystem.gui().textField("name1Text", "Company").setText(EBISystem.getInstance().getCompany().getName2());

        EBISystem.gui().textField("custNrText", "Company")
                .setText(EBISystem.getInstance().getCompany().getCustomernr());

        if (EBISystem.getInstance().getCompany().getCategory() != null) {
            EBISystem.gui().combo("categoryText", "Company").setSelectedItem(EBISystem.getInstance().getCompany().getCategory());
        }

        if (EBISystem.getInstance().getCompany().getCooperation() != null) {
            EBISystem.gui().combo("cooperationText", "Company").setSelectedItem(EBISystem.getInstance().getCompany().getCooperation());
        }

        EBISystem.gui().textField("telephoneText", "Company").setText(EBISystem.getInstance().getCompany().getPhone());
        EBISystem.gui().textField("faxText", "Company").setText(EBISystem.getInstance().getCompany().getFax());

        EBISystem.gui().textField("taxIDText", "Company").setText(EBISystem.getInstance().getCompany().getTaxnumber());
        EBISystem.gui().textField("employeeText", "Company").setText(EBISystem.getInstance().getCompany().getEmployee());
        EBISystem.gui().textField("internetText", "Company").setText(EBISystem.getInstance().getCompany().getWeb());

        EBISystem.gui().textField("emailText", "Company").setText(EBISystem.getInstance().getCompany().getEmail());
        EBISystem.gui().combo("classificationText", "Company").setSelectedItem(EBISystem.getInstance().getCompany().getQualification());

        if (EBISystem.getInstance().getCompany().getIslock() != null) {
            EBISystem.gui().getCheckBox("lockCompany", "Company").setSelected(EBISystem.getInstance().getCompany().getIslock());
        }
        EBISystem.gui().textArea("companyDescription", "Company").setText(EBISystem.getInstance().getCompany().getDescription());
        loadHierarchie();
    }

    private void loadHierarchie() {
        companyPane.listH = EBISystem.getInstance().getCompany().getCompanyhirarchies();
        companyPane.showHierarchies();
    }

    private void loadContactData() {
        if (!EBISystem.gui().existView("Contact")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                contactPane.getControlContact().dataShow(false);
                EBISystem.gui().combo("genderTex", "Contact").grabFocus();
            }
        });

    }

    private void loadCompanyAdressData() {
        if (!EBISystem.gui().existView("Address")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                addressPane.getAddressDataControl().dataShow(-1);
                EBISystem.gui().combo("addressTypeText", "Address").setRequestFocusEnabled(true);
                EBISystem.gui().combo("addressTypeText", "Address").grabFocus();
            }
        });
    }

    private void loadCompanyMeetingProtocol() {
        if (!EBISystem.gui().existView("MeetingCall")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                meetingReport.getDataMeetingControl().dataShow(-1);
                EBISystem.gui().textField("subjectMeetingText", "MeetingCall").grabFocus();
            }
        });
    }

    private void loadOpportunity() {
        if (!EBISystem.gui().existView("Opportunity")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                opportunityPane.getDataOpportuniyControl().dataShow(-1);
                EBISystem.gui().combo("opportunityNameText", "Opportunity").grabFocus();
            }
        });
    }

    private void loadActivities() {
        if (!EBISystem.gui().existView("Activity")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                activitiesPane.getDataControlActivity().dataShow(-1);
                EBISystem.gui().textField("activityNameText", "Activity").grabFocus();
            }
        });
    }

    private void loadBankData() {
        if (!EBISystem.gui().existView("Bank")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bankPane.getBankDataControl().dataShow(-1);
                EBISystem.gui().textField("bankNameText", "Bank").grabFocus();
            }
        });
    }

    private void loadOfferData() {
        if (!EBISystem.gui().existView("Offer")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                offerPane.getDataControlOffer().dataShow(-1);
                EBISystem.gui().textField("offerNrText", "Offer").requestFocus();
            }
        });
    }

    private void loadOrderData() {
        if (!EBISystem.gui().existView("Order")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                orderPane.getDataControlOrder().dataShow(-1);
                EBISystem.gui().textField("orderNrText", "Order").requestFocus();
            }
        });

    }

    private void loadServiceData() {
        if (!EBISystem.gui().existView("Service")) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                servicePane.showService();
                EBISystem.gui().textField("serviceNrText", "Service").requestFocus();
            }
        });
    }
}
