package ebiCRM;

import ebiCRM.functionality.EBICRMAutomate;
import ebiCRM.functionality.EBICRMDynamicFunctionalityMethods;
import ebiCRM.gui.component.EBICRMTabcontrol;
import ebiCRM.gui.component.EBICRMToolBar;
import ebiCRM.gui.panels.*;
import ebiCRM.utils.EBIAllertTimer;
import ebiCRM.utils.EBICRMHistoryCreator;
import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiCRM.utils.EBITimerTaskFixRate;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.arbitration.EBIArbCallback;
import ebiNeutrinoSDK.arbitration.EBIArbitration;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBIExtension;
import ebiNeutrinoSDK.interfaces.IEBIModule;
import ebiNeutrinoSDK.interfaces.IEBIStoreInterface;
import ebiNeutrinoSDK.model.hibernate.Company;
import ebiNeutrinoSDK.model.hibernate.Companyhirarchie;
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

public class EBICRMModule implements IEBIModule, IEBIExtension, IEBIStoreInterface {

	private EBICRMCompanyPane companyPane = null;
	private EBICRMContactPane contactPane = null;
	private EBIMeetingProtocol meetingReport = null;
	private EBICRMAddressPane addressPane = null;
	private EBICRMOpportunityPane opportunityPane = null;
	private EBICRMCompanyActivity activitiesPane = null;
	private EBICRMPlanningPane projectPane = null;
	private EBICRMBankPane bankPane = null;
	private EBICRMOffer offerPane = null;
	private EBICRMOrder orderPane = null;
	private EBICRMLeads leadsPane = null;
	private EBICRMAccountStack accountPane = null;
	private EBICRMSummaryPane summaryPane = null;
	private EBICRMProduct productPane = null;
	private EBICRMCampaign campaignPane = null;
	private EBICRMService servicePane = null;
	private EBICRMProblemSolution prosolPane = null;
	private EBICRMInvoice invoicePane = null;
	public String beginChar = "";
	public static boolean RELOAD = false;
	public static boolean isExistCompany = false;
	public EBICRMTabcontrol ebiContainer = null;
	public EBICRMDynamicFunctionalityMethods dynMethod = null;
	public EBICRMToolBar crmToolBar = null;
	public int EBICRM_SESSION = 0;
	public EBICRMHistoryCreator hcreator = null;
	public static final Logger logger = Logger.getLogger(EBICRMModule.class.getName());
	public EBICRMAutomate storeAutomate = null;
	public EBIAllertTimer allertTimer = null;

	@Override
	public Object getActiveModule() {
		return EBICRMModule.this;
	}

	/**
	 * Save a CRM Record
	 */
	@Override
	public boolean ebiSave(boolean check) {
		boolean ret = true;
		EBISystem.showInActionStatus("Company");
		if(check) {
			if (!checkCompany()) {
				return false;
			}
		}
		if (updateCompany()) {
			if (RELOAD) {
				createUI(EBISystem.getInstance().company.getCompanyid(), true);
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
	public boolean ebiUpdate(boolean check){
		EBISystem.showInActionStatus("Company");
		boolean ret = true;

		if(check) {
			if (!checkCompany()) {
				return false;
			}
		}

		if (updateCompany()) {
			if (RELOAD) {
				createUI(EBISystem.getInstance().company.getCompanyid(), true);
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
	 * CRM Module start point
	 */
	@Override
	public boolean ebiMain(final Object obj) {

		try {
			isExistCompany = false;
			RELOAD = false;

			EBISystem.getInstance().getIEBIGUIRendererInstance().loadProject("MODULES/project.xml");
			if (!EBISystem.getInstance().getIEBIGUIRendererInstance().isToolBarEmpty()) {
				crmToolBar.setCRMToolBar();
			}

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().existPackage("Campaign")) {
				this.getEBICRMCampaign();
			} else if ("Campaign/campaignGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
				ebiContainer.showClosableCampaignContainer();
				crmToolBar.enableToolButtonCampaignModule();
			}

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().existPackage("Account")) {
				this.getAccountPane();
			} else if ("AccountStack/accountGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
				ebiContainer.showClosableAccountContainer();
				crmToolBar.enableToolButtonAccount();
			}

			if (EBISystem.gui().existPackage("Prosol")) {
				this.getProsolPane();
			} else if ("CRMProblemSolution/problemSolutionGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
				ebiContainer.showClosableProsolContainer();
				crmToolBar.enableToolButtonProsol();
			}

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().existPackage("Invoice")) {
				this.getInvoicePane();
			} else if ("Invoice/invoiceGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
				ebiContainer.showClosableInvoiceContainer();
				crmToolBar.enableToolButtonInvoice();
			}

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().existPackage("Product")) {
				this.getEBICRMProductPane();
			} else if ("Product/productGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
				ebiContainer.showClosableProductContainer();
				crmToolBar.enableToolButtonProductModule();
			}

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().existPackage("Project")) {
				this.getProjectPane();
			} else if ("Project/projectGUI.xml".equals(EBISystem.moduleToView.get(0).toString())) {
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
							timerFix.scheduleAtFixedRate(new EBITimerTaskFixRate(EBICRMModule.this), 12000, 32000);
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

	private void loadModule(){
		if (EBISystem.gui().existPackage("Summary")) {
			EBISystem.getCRMModule().getSummaryPane();
		}
		if (EBISystem.gui().existPackage("Leads")) {
			EBISystem.getCRMModule().getLeadPane();
		}
		if (EBISystem.gui().existPackage("Company")) {
			EBISystem.getCRMModule().getCompanyPane();
		}
		if (EBISystem.gui().existPackage("Contact")) {
			EBISystem.getCRMModule().getContactPane();
		}
		if (EBISystem.gui().existPackage("Address")) {
			EBISystem.getCRMModule().getAddressPane();
		}
		if (EBISystem.gui().existPackage("Bank")) {
			EBISystem.getCRMModule().getBankdataPane();
		}
		if (EBISystem.gui().existPackage("MeetingCall")) {
			EBISystem.getCRMModule().getMeetingProtocol();
		}
		if (EBISystem.gui().existPackage("Activity")) {
			EBISystem.getCRMModule().getActivitiesPane();
		}
		if (EBISystem.gui().existPackage("Opportunity")) {
			EBISystem.getCRMModule().getOpportunityPane();
		}
		if (EBISystem.gui().existPackage("Offer")) {
			EBISystem.getCRMModule().getOfferPane();
		}
		if (EBISystem.gui().existPackage("Order")) {
			EBISystem.getCRMModule().getOrderPane();
		}
		if (EBISystem.gui().existPackage("Service")) {
			EBISystem.getCRMModule().getServicePane();
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
			} else if (toParse.equals("campaignGUI")) {
				crmToolBar.enableToolButtonCampaignModule();
				ebiContainer.showClosableCampaignContainer();
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
	 * remove crm module
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
		campaignPane = null;
		prosolPane = null;
		return EBISystem.getInstance().company == null ? -1 : EBISystem.getInstance().company.getCompanyid();
	}

	@Override
	public void onLoad() {
		EBISystem.hibernate().openHibernateSession("EBICRM_SESSION");
		hcreator = new EBICRMHistoryCreator(this);
		dynMethod = new EBICRMDynamicFunctionalityMethods();
		crmToolBar = new EBICRMToolBar(this);
		ebiContainer = new EBICRMTabcontrol();
		storeAutomate = new EBICRMAutomate(this);
	}

	@Override
	public void onExit(){}

	public EBICRMProduct getEBICRMProductPane() {
		if (productPane == null) {
			productPane = (EBICRMProduct)EBISystem.getInstance().getMappedBean(EBICRMProduct.class);
		}
		productPane.initialize(true);
		productPane.initializeAction();
		return productPane;
	}

	public EBICRMCompanyPane getCompanyPane() {
		if (companyPane == null) {
			companyPane = (EBICRMCompanyPane)EBISystem.getInstance().getMappedBean(EBICRMCompanyPane.class);
			companyPane.initializeAction();
		}
		return companyPane;
	}

	public EBICRMCampaign getEBICRMCampaign() {
		if (campaignPane == null) {
			campaignPane = (EBICRMCampaign)EBISystem.getInstance().getMappedBean(EBICRMCampaign.class);
		}
		campaignPane.initialize(true);
		campaignPane.initializeAction();
		return campaignPane;
	}

	public EBIMeetingProtocol getMeetingProtocol() {
		if (meetingReport == null) {
			meetingReport = (EBIMeetingProtocol)EBISystem.getInstance().getMappedBean(EBIMeetingProtocol.class);
			meetingReport.initializeAction();
		}
		return meetingReport;
	}

	public EBICRMContactPane getContactPane() {
		if (contactPane == null) {
			contactPane =  (EBICRMContactPane)EBISystem.getInstance().getMappedBean(EBICRMContactPane.class);
			contactPane.initializeAction();
		}
		return contactPane;
	}

	public EBICRMAddressPane getAddressPane() {
		if (addressPane == null) {
			addressPane =  (EBICRMAddressPane)EBISystem.getInstance().getMappedBean(EBICRMAddressPane.class);
			addressPane.initializeAction();
		}
		return addressPane;
	}

	public EBICRMOpportunityPane getOpportunityPane() {
		if (opportunityPane == null) {
			opportunityPane = (EBICRMOpportunityPane)EBISystem.getInstance().getMappedBean(EBICRMOpportunityPane.class);
			opportunityPane.initializeAction();
		}
		return opportunityPane;
	}

	public EBICRMCompanyActivity getActivitiesPane() {
		if (activitiesPane == null) {
			activitiesPane = (EBICRMCompanyActivity)EBISystem.getInstance().getMappedBean(EBICRMCompanyActivity.class);
			activitiesPane.initializeAction();
		}
		return activitiesPane;
	}

	public EBICRMOffer getOfferPane() {
		if (offerPane == null) {
			offerPane = (EBICRMOffer)EBISystem.getInstance().getMappedBean(EBICRMOffer.class);
			offerPane.initializeAction();
		}
		return offerPane;
	}

	public EBICRMLeads getLeadPane() {
		if (leadsPane == null) {
			leadsPane = (EBICRMLeads)EBISystem.getInstance().getMappedBean(EBICRMLeads.class);
			leadsPane.initialize();
			leadsPane.initializeAction();
		}
		return leadsPane;
	}

	public EBICRMOrder getOrderPane(){
		if(orderPane == null){
			orderPane = (EBICRMOrder)EBISystem.getInstance().getMappedBean(EBICRMOrder.class);
			orderPane.initializeAction();
		}
		return orderPane;
	}

	public EBICRMService getServicePane() {
		if (servicePane == null) {
			servicePane = (EBICRMService)EBISystem.getInstance().getMappedBean(EBICRMService.class);
			servicePane.initializeAction();
		}
		return servicePane;
	}

	public EBICRMProblemSolution getProsolPane() {
		if (prosolPane == null) {
			prosolPane = (EBICRMProblemSolution)EBISystem.getInstance().getMappedBean(EBICRMProblemSolution.class);
		}
		prosolPane.initialize(true);
		prosolPane.initializeAction();
		return prosolPane;
	}

	public EBICRMPlanningPane getProjectPane() {
		if (projectPane == null) {
			projectPane = (EBICRMPlanningPane)EBISystem.getInstance().getMappedBean(EBICRMPlanningPane.class);
		}
		projectPane.initialize();
		projectPane.initializeAction();
		return projectPane;
	}

	public EBICRMInvoice getInvoicePane() {
		if (invoicePane == null) {
			invoicePane = (EBICRMInvoice)EBISystem.getInstance().getMappedBean(EBICRMInvoice.class);
		}
		invoicePane.initialize(true);
		invoicePane.initializeAction();
		return invoicePane;
	}

	public EBICRMAccountStack getAccountPane() {
		if (accountPane == null) {
			accountPane = (EBICRMAccountStack)EBISystem.getInstance().getMappedBean(EBICRMAccountStack.class);
		}
		accountPane.initialize(true);
		accountPane.initializeAction();
		return accountPane;
	}

	public EBICRMSummaryPane getSummaryPane() {
		if (summaryPane == null) {
			summaryPane = (EBICRMSummaryPane)EBISystem.getInstance().getMappedBean(EBICRMSummaryPane.class);
			summaryPane.initialize();
			summaryPane.initializeAction();
			summaryPane.restoreProperties();
		}
		return summaryPane;
	}

	public EBICRMBankPane getBankdataPane() {
		if (bankPane == null) {
			bankPane = (EBICRMBankPane)EBISystem.getInstance().getMappedBean(EBICRMBankPane.class);
			bankPane.initializeAction();
		}
		return bankPane;
	}

	public void resetUI(final boolean enableTab, final boolean reloading) {
		try {
			isExistCompany = false;
			EBISystem.getInstance().company = new Company();
			EBISystem.getInstance().company.setCompanyid(-1);
			EBISystem.isSaveOrUpdate = false;
			EBISystem.canRelease = true;

			crmToolBar.enableToolbarButton(false);
			// crmMenu.setDeleteItemEnabled(false);

			final int selectedTab = ebiContainer.getSelectedTab();
			if (EBISystem.gui().existPackage("Company")) {
				getCompanyPane().initialize();
				EBISystem.getInstance().getIEBIGUIRendererInstance().vpanel("Company").setID(-1);
			}
			if (EBISystem.gui().existPackage("Contact")) {
				getContactPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("Address")) {
				getAddressPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("Bank")) {
				getBankdataPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("MeetingCall")) {
				getMeetingProtocol().initialize(true);
			}
			if (EBISystem.gui().existPackage("Activity")) {
				getActivitiesPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("Opportunity")) {
				getOpportunityPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("Offer")) {
				getOfferPane().initialize(true);
			}
			if (EBISystem.gui().existPackage("Order")) {
				getOrderPane().initialize(true);
			}

			if (EBISystem.gui().existPackage("Service")) {
				getServicePane().initialize(true);
			}

			if (reloading) {
				if (EBISystem.gui().existPackage("Summary")) {
					getSummaryPane().initialize();
				}
				if (EBISystem.gui().existPackage("Leads")) {
					getLeadPane().initialize();
				}
			}

			if (ebiContainer.getTabInstance().getTabCount() > 3) {
				for (int i = EBISystem.gui().getProjectModuleEnabled(); i < EBISystem.gui().getProjectModuleCount(); i++){
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
	}

	public boolean saveCompany(boolean checkCompany) {
		try {
			EBISystem.showInActionStatus("Company");
			if (EBISystem.getInstance().company != null) {
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
		if ("".equals(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("nameText", "Company").getText())) {
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME1")).Show(EBIMessage.ERROR_MESSAGE);
			return false;
		}else if (EBISystem.getInstance().getIEBIGUIRendererInstance().combo("categoryText", "Company").getSelectedIndex() == 0) {
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_CATEGORY")).Show(EBIMessage.ERROR_MESSAGE);
			return false;
		}else if ("00".equals(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("internalNrText", "Company").getText())) {
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_CRM_INTERNAL_NUMBER_EXHAUSTED")).Show(EBIMessage.ERROR_MESSAGE);
			return false;
		}else if ("-1".equals(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("internalNrText", "Company").getText())
				&& "".equals(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("custNrText", "Company").getText())) {
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_COMPANY_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
			return false;
		}
		if (EBISystem.isSaveOrUpdate == false) {
			if (dynMethod.findCustomerNumber(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("custNrText", "Company").getText())) {
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
				EBISystem.getInstance().company = new Company();
				RELOAD = true;
				EBISystem.getInstance().company.setCreatedfrom(EBISystem.ebiUser);
				EBISystem.getInstance().company.setCreateddate(new Date());
			} else {
				createHistory(EBISystem.getInstance().company);
				EBISystem.getInstance().company.setChangeddate(new Date());
				EBISystem.getInstance().company.setChangedfrom(EBISystem.ebiUser);
			}

			if (!"-1".equals(EBISystem.gui().textField("internalNrText", "Company").getText())) {
				EBISystem.getInstance().company.setCompanynumber(Integer.parseInt(EBISystem.gui()
						.textField("internalNrText", "Company").getText().replace(beginChar, "")));
			} else {
				EBISystem.getInstance().company.setCompanynumber(-1);
			}

			EBISystem.getInstance().company.setBeginchar(beginChar);
			EBISystem.getInstance().company.setName(EBISystem.gui().textField("nameText", "Company").getText());
			EBISystem.getInstance().company.setName2(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("name1Text", "Company").getText());

			if (EBISystem.getInstance().getIEBIGUIRendererInstance().combo("categoryText", "Company").getSelectedItem() != null) {
				EBISystem.getInstance().company.setCategory(EBISystem.getInstance().getIEBIGUIRendererInstance().combo("categoryText", "Company").getSelectedItem().toString());
				EBISystem.getInstance().company.setCooperation(EBISystem.getInstance().getIEBIGUIRendererInstance().combo("cooperationText", "Company").getSelectedItem().toString());
			}

			EBISystem.getInstance().company.setPhone(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("telephoneText", "Company").getText());
			EBISystem.getInstance().company.setFax(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("faxText", "Company").getText());
			EBISystem.getInstance().company.setTaxnumber(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("taxIDText", "Company").getText());
			EBISystem.getInstance().company.setEmployee(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("employeeText", "Company").getText());
			EBISystem.getInstance().company.setWeb(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("internetText", "Company").getText());
			EBISystem.getInstance().company.setEmail(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("emailText", "Company").getText());
			EBISystem.getInstance().company.setCustomernr(EBISystem.getInstance().getIEBIGUIRendererInstance().textField("custNrText", "Company").getText());
			
			if (EBISystem.getInstance().getIEBIGUIRendererInstance().combo("classificationText", "Company").getSelectedItem() != null) {
				EBISystem.getInstance().company.setQualification(EBISystem.getInstance().getIEBIGUIRendererInstance().combo("classificationText", "Company").getSelectedItem().toString());
			}

			EBISystem.getInstance().company.setIslock(EBISystem.getInstance().getIEBIGUIRendererInstance().getCheckBox("lockCompany", "Company").isSelected());
			// Save company hierarchy
			if (this.companyPane.listH != null && this.companyPane.listH.size() > 0) {
				final Iterator iterH = this.companyPane.listH.iterator();
				while (iterH.hasNext()) {
					final Companyhirarchie hi = (Companyhirarchie) iterH.next();
					EBISystem.getInstance().company.getCompanyhirarchies().add(hi);
					EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(hi);
				}
			}

			EBISystem.getInstance().company.setDescription(EBISystem.gui().textArea("companyDescription", "Company").getText());
			if (!EBISystem.isSaveOrUpdate) {
				if (checkForMainCompany()) {
					EBISystem.getInstance().company.setIsactual(true);
				} else {
					EBISystem.getInstance().company.setIsactual(false);
				}
			}

			if (EBISystem.getInstance().company.getIsactual() != null && EBISystem.getInstance().company.getIsactual()) {
				EBISystem.getInstance().loadStandardCompanyData();
			}

			EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(EBISystem.getInstance().company);

			if (ebiContainer.companyPOSID != -1) {
				ebiContainer.getTabInstance().setTitleAt(ebiContainer.companyPOSID, EBISystem.getInstance().company.getName());
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

		if (EBISystem.getInstance().company.getCreateddate() != null) {
			list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
					+ EBISystem.getInstance().getDateToString(EBISystem.getInstance().company.getCreateddate()));
		}

		if (EBISystem.getInstance().company.getCreatedfrom() != null) {
			list.add(EBISystem.getInstance().company.getCreatedfrom() == null
					? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": "
					: EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": "
							+ EBISystem.getInstance().company.getCreatedfrom());
		}

		if (EBISystem.getInstance().company.getChangeddate() != null) {
			list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
					+ EBISystem.getInstance().getDateToString(EBISystem.getInstance().company.getChangeddate()));
		}

		if (EBISystem.getInstance().company.getChangedfrom() != null) {
			list.add(EBISystem.getInstance().company.getChangedfrom() == null
					? EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": "
					: EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": "
							+ EBISystem.getInstance().company.getChangedfrom());
		}

		String nh = "";
		if (!String.valueOf(EBISystem.getInstance().company.getCompanynumber())
				.equals(EBISystem.gui().textField("internalNrText", "Company").getText())) {
			nh = "$";
		}

		list.add(String.valueOf(EBISystem.getInstance().company.getCompanynumber()) == null
				? EBISystem.i18n("EBI_LANG_C_INTERNAL_NUMBER") + ": " + String.valueOf(-1) + nh
				: EBISystem.i18n("EBI_LANG_C_INTERNAL_NUMBER") + ": "
						+ String.valueOf(EBISystem.getInstance().company.getCompanynumber()) + nh);

		if (EBISystem.getInstance().company.getName() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_NAME1") + ": "
					+ (EBISystem.getInstance().company.getName()
					.equals(EBISystem.gui().textField("nameText", "Company").getText())
									? EBISystem.getInstance().company.getName()
									: EBISystem.getInstance().company.getName() + "$"));
		}
		if (EBISystem.getInstance().company.getName2() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_NAME2") + ": "
					+ (EBISystem.getInstance().company.getName2()
					.equals(EBISystem.gui().textField("name1Text", "Company").getText())
									? EBISystem.getInstance().company.getName2()
									: EBISystem.getInstance().company.getName2() + "$"));
		}
		if (EBISystem.getInstance().company.getCustomernr() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_COMPANY_CUSTOMER_NUMBER") + ": "
					+ (EBISystem.getInstance().company.getCustomernr()
							.equals(EBISystem.gui().textField("custNrText", "Company").getText()) == true
									? EBISystem.getInstance().company.getCustomernr()
									: EBISystem.getInstance().company.getCustomernr() + "$"));
		}
		if (EBISystem.getInstance().company.getCategory() != null) {
			list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": "
					+ (EBISystem.getInstance().company.getCategory()
							.equals(EBISystem.gui().combo("categoryText", "Company").getSelectedItem()
									.toString()) == true ? EBISystem.getInstance().company.getCategory()
											: EBISystem.getInstance().company.getCategory() + "$"));
		}
		if (EBISystem.getInstance().company.getCooperation() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_COOPERATION") + ": "
					+ (EBISystem.getInstance().company.getCooperation()
							.equals(EBISystem.gui().combo("cooperationText", "Company")
									.getSelectedItem().toString()) == true
											? EBISystem.getInstance().company.getCooperation()
											: EBISystem.getInstance().company.getCooperation() + "$"));
		}
		if (EBISystem.getInstance().company.getPhone() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_TELEPHONE") + ": "
					+ (EBISystem.getInstance().company.getPhone().equals(
							EBISystem.gui().textField("telephoneText", "Company").getText()) == true
									? EBISystem.getInstance().company.getPhone()
									: EBISystem.getInstance().company.getPhone() + "$"));
		}
		if (EBISystem.getInstance().company.getFax() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_FAX") + ": "
					+ (EBISystem.getInstance().company.getFax()
							.equals(EBISystem.gui().textField("faxText", "Company").getText()) == true
									? EBISystem.getInstance().company.getFax()
									: EBISystem.getInstance().company.getFax() + "$"));
		}
		if (EBISystem.getInstance().company.getTaxnumber() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_VAT_NR") + ": "
					+ (EBISystem.getInstance().company.getTaxnumber()
							.equals(EBISystem.gui().textField("taxIDText", "Company").getText()) == true
									? EBISystem.getInstance().company.getTaxnumber()
									: EBISystem.getInstance().company.getTaxnumber() + "$"));
		}
		if (EBISystem.getInstance().company.getEmployee() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_EMPLOYEE") + ": "
					+ (EBISystem.getInstance().company.getEmployee().equals(
							EBISystem.gui().textField("employeeText", "Company").getText()) == true
									? EBISystem.getInstance().company.getEmployee()
									: EBISystem.getInstance().company.getEmployee() + "$"));
		}
		if (EBISystem.getInstance().company.getWeb() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_HINTERNET") + ": "
					+ (EBISystem.getInstance().company.getWeb().equals(
							EBISystem.gui().textField("internetText", "Company").getText()) == true
									? EBISystem.getInstance().company.getWeb()
									: EBISystem.getInstance().company.getWeb() + "$"));
		}
		if (EBISystem.getInstance().company.getEmail() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_HEMAIL") + ": "
					+ (EBISystem.getInstance().company.getEmail()
							.equals(EBISystem.gui().textField("emailText", "Company").getText()) == true
									? EBISystem.getInstance().company.getEmail()
									: EBISystem.getInstance().company.getEmail() + "$"));
		}
		if (EBISystem.getInstance().company.getQualification() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_KLASSIFICATION") + ": "
					+ (EBISystem.getInstance().company.getQualification()
							.equals(EBISystem.gui().combo("classificationText", "Company")
									.getSelectedItem()) == true ? EBISystem.getInstance().company.getQualification()
											: EBISystem.getInstance().company.getQualification() + "$"));
		}
		if (EBISystem.getInstance().company.getIslock() != null) {
			String chd = "";
			if (EBISystem.getInstance().company.getIslock() == true
					&& !EBISystem.gui().getCheckBox("lockCompany", "Company").isSelected()
					|| EBISystem.getInstance().company.getIslock() == false
							&& EBISystem.gui().getCheckBox("lockCompany", "Company").isSelected()) {
				chd = "$";
			}

			list.add(EBISystem.getInstance().company.getIslock() == true
					? EBISystem.i18n("EBI_LANG_C_LOCK") + ": " + String.valueOf(true) + chd
					: EBISystem.i18n("EBI_LANG_C_LOCK") + ": " + String.valueOf(false) + chd);
		}
		if (EBISystem.getInstance().company.getDescription() != null) {
			list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": "
					+ (EBISystem.getInstance().company.getDescription().equals(
							EBISystem.gui().textArea("companyDescription", "Company").getText()) == true
									? EBISystem.getInstance().company.getDescription()
									: EBISystem.getInstance().company.getDescription() + "$"));
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

	public boolean deleteCRM() {
		boolean ret = true;
		EBISystem.showInActionStatus("Company");
		try {
			if(EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD"))
							.Show(EBIMessage.WARNING_MESSAGE_YESNO) && EBISystem.getInstance().company != null) {
				try {
					EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
					EBISystem.hibernate().session("EBICRM_SESSION").delete(EBISystem.getInstance().company);
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
					EBISystem.getInstance().company = (Company) it.next();
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

					if (EBISystem.getInstance().company != null) {
						if (EBISystem.getInstance().company.getIsactual() != null && EBISystem.getInstance().company.getIsactual() == false) {
							if (EBISystem.gui().getCheckBox("mainContactText", "Contact") != null) {
								EBISystem.gui().getCheckBox("mainContactText", "Contact").setVisible(false);
							}
						} else {
							if (EBISystem.gui().getCheckBox("mainContactText", "Contact") != null) {
								EBISystem.gui().getCheckBox("mainContactText", "Contact").setVisible(true);
							}
						}
					}

					if (EBISystem.getInstance().company.getName() != null) {
						if (ebiContainer.companyPOSID != -1) {
							ebiContainer.getTabInstance().setTitleAt(ebiContainer.companyPOSID, EBISystem.getInstance().company.getName());
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
		EBISystem.gui().vpanel("Company").setID(EBISystem.getInstance().company.getCompanyid());
		if (EBISystem.getInstance().company.getCreateddate() != null) {
			EBISystem.gui().vpanel("Company").setCreatedDate(EBISystem.getInstance().getDateToString(EBISystem.getInstance().company.getCreateddate()));
			EBISystem.gui().vpanel("Company").setCreatedFrom(EBISystem.getInstance().company.getCreatedfrom());
		}

		if (EBISystem.getInstance().company.getChangeddate() != null) {
			EBISystem.gui().vpanel("Company").setChangedDate(EBISystem.getInstance().getDateToString(EBISystem.getInstance().company.getChangeddate()));
			EBISystem.gui().vpanel("Company").setChangedFrom(EBISystem.getInstance().company.getChangedfrom());
		}

		beginChar = EBISystem.getInstance().company.getBeginchar();
		EBISystem.gui().textField("rootText", "Company").requestFocus();
		EBISystem.gui().textField("internalNrText", "Company").setText(String.valueOf(EBISystem.getInstance().company.getCompanynumber() == null ? -1 : EBISystem.getInstance().company.getCompanynumber()));
		EBISystem.gui().textField("nameText", "Company").setText(EBISystem.getInstance().company.getName());
		EBISystem.gui().textField("name1Text", "Company").setText(EBISystem.getInstance().company.getName2());

		EBISystem.gui().textField("custNrText", "Company")
				.setText(EBISystem.getInstance().company.getCustomernr());

		if (EBISystem.getInstance().company.getCategory() != null) {
			EBISystem.gui().combo("categoryText", "Company").setSelectedItem(EBISystem.getInstance().company.getCategory());
		}

		if (EBISystem.getInstance().company.getCooperation() != null) {
			EBISystem.gui().combo("cooperationText", "Company").setSelectedItem(EBISystem.getInstance().company.getCooperation());
		}

		EBISystem.gui().textField("telephoneText", "Company").setText(EBISystem.getInstance().company.getPhone());
		EBISystem.gui().textField("faxText", "Company").setText(EBISystem.getInstance().company.getFax());

		EBISystem.gui().textField("taxIDText", "Company").setText(EBISystem.getInstance().company.getTaxnumber());
		EBISystem.gui().textField("employeeText", "Company").setText(EBISystem.getInstance().company.getEmployee());
		EBISystem.gui().textField("internetText", "Company").setText(EBISystem.getInstance().company.getWeb());

		EBISystem.gui().textField("emailText", "Company").setText(EBISystem.getInstance().company.getEmail());
		EBISystem.gui().combo("classificationText", "Company").setSelectedItem(EBISystem.getInstance().company.getQualification());

		if (EBISystem.getInstance().company.getIslock() != null) {
			EBISystem.gui().getCheckBox("lockCompany", "Company").setSelected(EBISystem.getInstance().company.getIslock());
		}
		EBISystem.gui().textArea("companyDescription", "Company").setText(EBISystem.getInstance().company.getDescription());
		loadHierarchie();
	}

	private void loadHierarchie() {
		companyPane.listH = EBISystem.getInstance().company.getCompanyhirarchies();
		companyPane.showHierarchies();
	}

	private void loadContactData() {
		if (!EBISystem.gui().existPackage("Contact")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contactPane.controlContact.dataShow();
				EBISystem.gui().combo("genderTex", "Contact").grabFocus();
			}
		});

	}

	private void loadCompanyAdressData() {
		if (!EBISystem.gui().existPackage("Address")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addressPane.addressDataControl.dataShow();
				EBISystem.gui().combo("addressTypeText", "Address").setRequestFocusEnabled(true);
				EBISystem.gui().combo("addressTypeText", "Address").grabFocus();
			}
		});
	}

	private void loadCompanyMeetingProtocol() {
		if (!EBISystem.gui().existPackage("MeetingCall")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				meetingReport.showMeetingProtocol();
				EBISystem.gui().textField("subjectMeetingText", "MeetingCall").grabFocus();
			}
		});
	}

	private void loadOpportunity() {
		if (!EBISystem.gui().existPackage("Opportunity")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				opportunityPane.showOpportunity();
				EBISystem.gui().combo("opportunityNameText", "Opportunity").grabFocus();
			}
		});
	}

	private void loadActivities() {
		if (!EBISystem.gui().existPackage("Activity")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				activitiesPane.showActivity();
				EBISystem.gui().textField("activityNameText", "Activity").grabFocus();
			}
		});
	}

	private void loadBankData() {
		if (!EBISystem.gui().existPackage("Bank")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bankPane.bankDataControl.dataShow();
				EBISystem.gui().textField("bankNameText", "Bank").grabFocus();
			}
		});
	}

	private void loadOfferData() {
		if (!EBISystem.gui().existPackage("Offer")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				offerPane.showOffer();
				EBISystem.gui().textField("offerNrText", "Offer").requestFocus();
			}
		});
	}

	private void loadOrderData() {
		if (!EBISystem.gui().existPackage("Order")) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				orderPane.showOrder();
				EBISystem.gui().textField("orderNrText", "Order").requestFocus();
			}
		});

	}

	private void loadServiceData() {
		if (!EBISystem.gui().existPackage("Service")) {
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