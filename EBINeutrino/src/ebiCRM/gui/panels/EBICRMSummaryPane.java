package ebiCRM.gui.panels;

import ebiCRM.gui.dialogs.EBIDialogSearchCompany;
import ebiCRM.table.models.MyTableModelSummaryTab;
import ebiNeutrino.core.gui.callbacks.EBIUICallback;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.*;
import ebiNeutrinoSDK.utils.EBIPropertiesRW;
import org.hibernate.query.Query;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Iterator;

public class EBICRMSummaryPane {

    private MyTableModelSummaryTab tabModel = null;
    private int selectedSummaryRow = -1;
    private NumberFormat currency = null;
    
    private Iterator objectIterator = null;
    private Query query=null;
    

    @SuppressWarnings("unchecked")
	public void initializeAction() {
        tabModel = new MyTableModelSummaryTab();
        EBISystem.gui().combo("summarytypeText", "Summary")
                .setModel(new DefaultComboBoxModel<String>(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
                        EBISystem.i18n("EBI_LANG_C_SEARCH_ALL"), EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"),
                        EBISystem.i18n("EBI_LANG_C_ACTIVITIES"), EBISystem.i18n("EBI_LANG_C_OFFER"),
                        EBISystem.i18n("EBI_LANG_C_ORDER"), EBISystem.i18n("EBI_LANG_C_SERVICE"),
                        EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"), EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"),
                        EBISystem.i18n("EBI_LANG_C_TAB_PROSOL")}));

        EBISystem.gui().label("filterTable", "Summary").setHorizontalAlignment(SwingConstants.RIGHT);
        EBISystem.gui().textField("filterTableText", "Summary").addKeyListener(new KeyListener() {
            @Override
			public void keyTyped(final KeyEvent e) {}

            @Override
			public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("companySummaryTable", "Summary").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Summary").getText()));
            }

            @Override
			public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("companySummaryTable", "Summary").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.gui().textField("filterTableText", "Summary").getText()));
            }
        });

        if(EBICRMCompanyPane.categories != null) {
            EBISystem.gui().combo("companyCategoryText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMCompanyPane.categories));
        }

        EBISystem.gui().combo("summarytypeText", "Summary").addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
                setStateForModule();
            }
        });


        /*************************************************************************************/
        // TABLE SEARCH SUMMARY
        /*************************************************************************************/
        EBISystem.gui().table("companySummaryTable", "Summary").setModel(tabModel);
        EBISystem.gui().table("companySummaryTable", "Summary").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("companySummaryTable", "Summary").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selectedSummaryRow = EBISystem.gui().table("companySummaryTable", "Summary")
                            .convertRowIndexToModel(EBISystem.gui()
                                    .table("companySummaryTable", "Summary").getSelectedRow());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showSummary", "Summary").setEnabled(false);
                } else if (!tabModel.data[selectedSummaryRow][0].toString()
                        .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showSummary", "Summary").setEnabled(true);
                }
            }
        });


        EBISystem.gui().table("companySummaryTable", "Summary").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedSummaryRow = selRow;
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedSummaryRow = selRow;
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedSummaryRow = selRow;
                if (selectedSummaryRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedSummaryRow][0].toString())) {
                    return;
                }
                showSummaryObjectView();
            }
        });


        EBISystem.gui().table("companySummaryTable", "Summary").addMouseListener(new MouseAdapter() {
            @Override
			public void mouseClicked(final MouseEvent e) {
                if (EBISystem.gui().table("companySummaryTable", "Summary").rowAtPoint(e.getPoint()) != -1) {
                    selectedSummaryRow = EBISystem.gui().table("companySummaryTable", "Summary")
                            .convertRowIndexToModel(EBISystem.gui().table("companySummaryTable", "Summary")
                                    .rowAtPoint(e.getPoint()));
                }
                if (selectedSummaryRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedSummaryRow][0].toString())) {
                    return;
                }
                if(e.getClickCount() == 2) {
                	showSummaryObjectView();
                }
                
            }
        });
    }

    public void initialize() {
        currency = NumberFormat.getCurrencyInstance();
        EBISystem.hibernate().openHibernateSession("SUMMARY_SESSION");


        EBISystem.gui().textField("summaryNameText", "Summary").setText("");

        if (EBISystem.gui().combo("summaryStatusText", "Summary").getItemCount() > 0) {
            EBISystem.gui().combo("summaryStatusText", "Summary").removeAllItems();
        }

        EBISystem.gui().combo("summaryStatusText", "Summary").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

        EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").setFormats(EBISystem.DateFormat);
        EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getEditor().setText("");
        EBISystem.gui().timePicker("summaryCreatedToText", "Summary").setFormats(EBISystem.DateFormat);
        EBISystem.gui().timePicker("summaryCreatedToText", "Summary").getEditor().setText("");
    }

    public void restoreProperties(){
        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
        EBISystem.gui().textField("companyText", "Summary").setText(properties.getValue("EBIDASHBOARD_COMPANY"));

        EBISystem.gui().combo("companyCategoryText", "Summary").setSelectedItem(properties.getValue("EBIDASHBOARD_CATEGORY"));
        EBISystem.gui().combo("companyCategoryText", "Summary").getEditor().setItem(properties.getValue("EBIDASHBOARD_CATEGORY"));

        EBISystem.gui().combo("summarytypeText", "Summary").setSelectedItem(properties.getValue("EBIDASHBOARD_TYPE"));
        EBISystem.gui().combo("summarytypeText", "Summary").getEditor().setItem(properties.getValue("EBIDASHBOARD_TYPE"));

        EBISystem.gui().combo("summaryStatusText", "Summary").setSelectedItem(properties.getValue("EBIDASHBOARD_STATUS"));
        EBISystem.gui().combo("summaryStatusText", "Summary").getEditor().setItem(properties.getValue("EBIDASHBOARD_STATUS"));

        EBISystem.gui().textField("summaryNameText", "Summary").setText(properties.getValue("EBIDASHBOARD_NAME"));

        if (!"".equals(properties.getValue("EBIDASHBOARD_CREATEDFROM")) && !"null".equals(properties.getValue("EBIDASHBOARD_CREATEDFROM"))) {
            EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").setDate(EBISystem.getInstance().getStringToDate(properties.getValue("EBIDASHBOARD_CREATEDFROM")));
            EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getEditor().setText(properties.getValue("EBIDASHBOARD_CREATEDFROM"));
        }

        if (!"".equals(properties.getValue("EBIDASHBOARD_CREATEDTO")) && !"null".equals(properties.getValue("EBIDASHBOARD_CREATEDTO"))) {
            EBISystem.gui().timePicker("summaryCreatedToText", "Summary").setDate(EBISystem.getInstance().getStringToDate(properties.getValue("EBIDASHBOARD_CREATEDTO")));
            EBISystem.gui().timePicker("summaryCreatedToText", "Summary").getEditor().setText(properties.getValue("EBIDASHBOARD_CREATEDTO"));
        }
    }


    public void setCompanyText(final String text) {
        EBISystem.gui().textField("companyText", "Summary").setText(text);
    }

    /**
     * Fill ComboBox status from the selected category
     */
    private void setStateForModule() {

        final Query query;
        final Iterator objectIterator;
        try {

            EBISystem.hibernate().transaction("SUMMARY_SESSION").begin();
            EBISystem.gui().combo("summaryStatusText", "Summary").removeAllItems();
            EBISystem.gui().combo("summaryStatusText", "Summary").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
            
            String summaryText = EBISystem.gui().combo("summarytypeText", "Summary").getSelectedItem().toString();
            // Companyservice
            if (EBISystem.i18n("EBI_LANG_C_OPPORTUNITY").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMOpportunityPane.oppStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_ACTIVITIES").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMCompanyActivity.actType));
            } else if (EBISystem.i18n("EBI_LANG_C_SERVICE").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMService.serviceStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_OFFER").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMOffer.offerStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_ORDER").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMOrder.orderStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMCampaign.campaignStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMProblemSolution.prosolStatus));
            } else if (EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(summaryText)) {
                EBISystem.gui().combo("summaryStatusText", "Summary").setModel(new DefaultComboBoxModel<String>(EBICRMInvoice.invoiceStatus));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCompany(){
        new EBIDialogSearchCompany(false, true);
    }

    public void showSummaryObjectView() {

        try {
            if (selectedSummaryRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                    .equals(tabModel.data[selectedSummaryRow][0].toString())) {
                return;
            }
            EBISystem.showInActionStatus("Summary");
        	EBISystem.gui().vpanel("Summary").setCursor(new Cursor(Cursor.WAIT_CURSOR));
            String selType = tabModel.data[selectedSummaryRow][0].toString();

            if (!EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(selType) &&
            		!EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(selType) && !EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(selType)) {

                EBISystem.getCRMModule().createUI(Integer.parseInt(tabModel.data[selectedSummaryRow][6].toString()), false);
            }

            if (EBISystem.i18n("EBI_LANG_C_OPPORTUNITY").equals(selType)) {
                if (EBISystem.getCRMModule().getOpportunityPane() != null) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY")));
                    EBISystem.getCRMModule().getOpportunityPane().remoteEditOpportunity(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
                }
            } else if (EBISystem.i18n("EBI_LANG_C_ACTIVITIES").equals(selType)) {
                if (EBISystem.getCRMModule().getActivitiesPane() != null) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ACTIVITIES")));
                    EBISystem.getCRMModule().getActivitiesPane().remoteEditActivity(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
                }
            } else if (EBISystem.i18n("EBI_LANG_C_SERVICE").equals(selType)) {
                if (EBISystem.getCRMModule().getServicePane() != null) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_SERVICE")));
                    EBISystem.getCRMModule().getServicePane().remoteEditService(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
                }
            } else if (EBISystem.i18n("EBI_LANG_C_OFFER").equals(selType)) {
                if (EBISystem.getCRMModule().getOfferPane() != null) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_OFFER")));
                    EBISystem.getCRMModule().getOfferPane().editOfferRemote(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
                }
            } else if (EBISystem.i18n("EBI_LANG_C_ORDER").equals(selType)) {
                if (EBISystem.getCRMModule().getOrderPane() != null) {
                    EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ORDER")));
                    EBISystem.getCRMModule().getOrderPane().editOrderRemote(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
                }
            } else if (EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(selType)) {

                if (!EBISystem.getCRMModule().crmToolBar.isCampaignSelected()) {
                    EBISystem.getCRMModule().crmToolBar.enableToolButtonCampaignModule();
                    EBISystem.getCRMModule().ebiContainer.showClosableCampaignContainer();
                } else {
                    EBISystem.getCRMModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN")));
                }
                EBISystem.getCRMModule().getEBICRMCampaign().dataControlCampaign.dataEdit(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));

            } else if (EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(selType)) {

                if (!EBISystem.getCRMModule().crmToolBar.isProsolSelected()) {
                    EBISystem.getCRMModule().crmToolBar.enableToolButtonProsol();
                    EBISystem.getCRMModule().ebiContainer.showClosableProsolContainer();
                } else {
                    EBISystem.getCRMModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL")));
                }
                EBISystem.getCRMModule().getProsolPane().dataControlProsol.dataEdit(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
            } else if (EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(selType)) {
            	
                if (!EBISystem.getCRMModule().crmToolBar.isInvoiceSelected()) {
                    EBISystem.getCRMModule().crmToolBar.enableToolButtonInvoice();
                    EBISystem.getCRMModule().ebiContainer.showClosableInvoiceContainer();
                } else {
                    EBISystem.getCRMModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE")));
                }
                EBISystem.getCRMModule().getInvoicePane().dataControlInvoice.dataEdit(Integer.parseInt(tabModel.data[selectedSummaryRow][7].toString()));
            }
            searchSummary();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            EBISystem.gui().vpanel("Summary").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private boolean validateInput() {
        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("summarytypeText", "Summary").getSelectedItem())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_TYPE")).Show(EBIMessage.INFO_MESSAGE);
            return false;
        }
        return true;
    }

    public void searchSummary() {
        if (!validateInput()) {
            return;
        }

        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
        properties.setValue("EBIDASHBOARD_COMPANY", EBISystem.gui().textField("companyText", "Summary").getText());
        properties.setValue("EBIDASHBOARD_CATEGORY", EBISystem.gui().combo("companyCategoryText", "Summary").getSelectedItem().toString());
        properties.setValue("EBIDASHBOARD_TYPE", EBISystem.gui().combo("summarytypeText", "Summary").getSelectedItem().toString());
        properties.setValue("EBIDASHBOARD_STATUS", EBISystem.gui().combo("summaryStatusText", "Summary").getSelectedItem().toString());
        properties.setValue("EBIDASHBOARD_NAME", EBISystem.gui().textField("summaryNameText", "Summary").getText());
        properties.setValue("EBIDASHBOARD_CREATEDFROM", EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getEditor().getText());
        properties.setValue("EBIDASHBOARD_CREATEDTO", EBISystem.gui().timePicker("summaryCreatedToText", "Summary").getEditor().getText());

        properties.saveEBINeutrinoProperties();
        try {
            EBISystem.showInActionStatus("Summary");
            EBISystem.hibernate().transaction("SUMMARY_SESSION").begin();
            EBISystem.gui().vpanel("Summary").setCursor(new Cursor(Cursor.WAIT_CURSOR));

            String summaryType = EBISystem.gui().combo("summarytypeText", "Summary").getSelectedItem().toString();

            int i = 0;
            if (EBISystem.i18n("EBI_LANG_C_OPPORTUNITY").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyopportunity"));
                setParamToHQuery(query);
                objectIterator = query.iterate();

                if (query.list().size() > 0) {
                	
                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {

                        final Companyopportunity companyOpportunity = (Companyopportunity) objectIterator.next();
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_OPPORTUNITY");
                        tabModel.data[i][1] = companyOpportunity.getCompany().getName();
                        tabModel.data[i][2] = companyOpportunity.getName() == null ? "" : companyOpportunity.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOpportunity.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOpportunity.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOpportunity.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOpportunity.getChangeddate());
                        tabModel.data[i][5] = companyOpportunity.getOpportunitystatus() == null ? "": companyOpportunity.getOpportunitystatus();
                        tabModel.data[i][6] = companyOpportunity.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOpportunity.getOpportunityid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_SERVICE").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyservice"));
                setParamToHQuery(query);

                objectIterator = query.iterate();

                if (query.list().size() > 0) {
                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {

                        final Companyservice companyService = (Companyservice) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyService);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_SERVICE");
                        tabModel.data[i][1] = companyService.getCompany().getName();
                        tabModel.data[i][2] = companyService.getName() == null ? "" : companyService.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyService.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyService.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyService.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyService.getChangeddate());
                        tabModel.data[i][5] = companyService.getStatus() == null ? "" : companyService.getStatus();
                        tabModel.data[i][6] = companyService.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyService.getServiceid();
                        final Iterator srit = companyService.getCompanyservicepositionses().iterator();
                        i++;
                    }
                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }
                
            } else if (EBISystem.i18n("EBI_LANG_C_ACTIVITIES").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyactivities"));

                setParamToHQuery(query);
                objectIterator = query.iterate();

                if (query.list().size() > 0) {
                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {

                        final Companyactivities companyActivity = (Companyactivities) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyActivity);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_ACTIVITIES");
                        tabModel.data[i][1] = companyActivity.getCompany().getName();
                        tabModel.data[i][2] = companyActivity.getActivityname() == null ? "": companyActivity.getActivityname();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyActivity.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyActivity.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyActivity.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyActivity.getChangeddate());
                        tabModel.data[i][5] = companyActivity.getActivitystatus() == null ? "": companyActivity.getActivitystatus();
                        tabModel.data[i][6] = companyActivity.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyActivity.getActivityid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_OFFER").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyoffer"));

                setParamToHQuery(query);
                objectIterator = query.iterate();
                
                
                if (query.list().size() > 0) {
                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {

                        final Companyoffer companyOffer = (Companyoffer) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyOffer);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_OFFER");
                        tabModel.data[i][1] = companyOffer.getCompany().getName();
                        tabModel.data[i][2] = companyOffer.getName() == null ? "" : companyOffer.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOffer.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOffer.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOffer.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOffer.getChangeddate());
                        tabModel.data[i][5] = companyOffer.getStatus() == null ? "" : companyOffer.getStatus();
                        tabModel.data[i][6] = companyOffer.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOffer.getOfferid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_ORDER").equals(summaryType)) {
            	
                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyorder"));

                setParamToHQuery(query);
                objectIterator = query.iterate();
                if (query.list().size() > 0) {

                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {
                        final Companyorder companyOrder = (Companyorder) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyOrder);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_ORDER");
                        tabModel.data[i][1] = companyOrder.getCompany().getName();
                        tabModel.data[i][2] = companyOrder.getName() == null ? "" : companyOrder.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOrder.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOrder.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOrder.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyOrder.getChangeddate());
                        tabModel.data[i][5] = companyOrder.getStatus() == null ? "" : companyOrder.getStatus();
                        tabModel.data[i][6] = companyOrder.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOrder.getOrderid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Crmcampaign"));

                setParamToHQuery(query);
                objectIterator = query.iterate();

                if (query.list().size() > 0) {

                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {
                        final Crmcampaign crmCampaign = (Crmcampaign) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(crmCampaign);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN");
                        tabModel.data[i][1] = crmCampaign.getCampaignnr();
                        tabModel.data[i][2] = crmCampaign.getName() == null ? "" : crmCampaign.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(crmCampaign.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(crmCampaign.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(crmCampaign.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(crmCampaign.getChangeddate());
                        tabModel.data[i][5] = crmCampaign.getStatus() == null ? "" : crmCampaign.getStatus();
                        tabModel.data[i][6] = crmCampaign.getCampaignid();
                        tabModel.data[i][7] = crmCampaign.getCampaignid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(summaryType)) {
                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Crminvoice"));
                setParamToHQuery(query);
                objectIterator = query.iterate();
                if (query.list().size() > 0) {
                    tabModel.data = new Object[query.list().size()][8];
                    while (objectIterator.hasNext()) {
                        final Crminvoice crmInvoice = (Crminvoice) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(crmInvoice);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_TAB_INVOICE");
                        tabModel.data[i][1] = crmInvoice.getBeginchar() + crmInvoice.getInvoicenr();
                        tabModel.data[i][2] = crmInvoice.getName() == null ? "" : crmInvoice.getName();
                        tabModel.data[i][3] = crmInvoice.getCreateddate() == null ? "": EBISystem.getInstance().getDateToString(crmInvoice.getCreateddate());
                        tabModel.data[i][4] = crmInvoice.getChangeddate() == null ? "": EBISystem.getInstance().getDateToString(crmInvoice.getChangeddate());
                        tabModel.data[i][5] = crmInvoice.getStatus() == null ? "" : crmInvoice.getStatus();
                        tabModel.data[i][6] = crmInvoice.getInvoiceid();
                        tabModel.data[i][7] = crmInvoice.getInvoiceid();
                        i++;
                    }

                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(summaryType)) {

                query = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Crmproblemsolutions"));

                setParamToHQuery(query);
                objectIterator = query.iterate();

                if (query.list().size() > 0) {

                    tabModel.data = new Object[query.list().size()][8];

                    while (objectIterator.hasNext()) {
                        final Crmproblemsolutions crmProsol = (Crmproblemsolutions) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(crmProsol);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_TAB_PROSOL");
                        tabModel.data[i][1] = crmProsol.getServicenr();
                        tabModel.data[i][2] = crmProsol.getName() == null ? "" : crmProsol.getName();
                        tabModel.data[i][3] = crmProsol.getCreateddate() == null ? "": EBISystem.getInstance().getDateToString(crmProsol.getCreateddate());
                        tabModel.data[i][4] = crmProsol.getChangeddate() == null ? "": EBISystem.getInstance().getDateToString(crmProsol.getChangeddate());
                        tabModel.data[i][5] = crmProsol.getStatus() == null ? "" : crmProsol.getStatus();
                        tabModel.data[i][6] = crmProsol.getProsolid();
                        tabModel.data[i][7] = crmProsol.getProsolid();
                        i++;
                    }
                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }

            } else if (EBISystem.i18n("EBI_LANG_C_SEARCH_ALL").equals(summaryType)) {

                final Query query1 = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyorder"));
                setParamToHQuery(query1);
                final Query query2 = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyoffer"));
                setParamToHQuery(query2);
                final Query query3 = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyactivities"));
                setParamToHQuery(query3);
                final Query query4 = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyopportunity"));
                setParamToHQuery(query4);
                final Query query5 = EBISystem.hibernate().session("SUMMARY_SESSION").createQuery(returnBuildQuery("Companyservice"));
                setParamToHQuery(query5);
 
           
                final int size = query1.list().size() + query2.list().size() + query3.list().size() + query4.list().size()+ query5.list().size();

                if (size > 0) {
                    tabModel.data = new Object[size][8];

                    objectIterator = query1.iterate();
                    while (objectIterator.hasNext()) {

                        final Companyorder companyOrder = (Companyorder) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyOrder);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_ORDER");
                        tabModel.data[i][1] = companyOrder.getCompany().getName();
                        tabModel.data[i][2] = companyOrder.getName() == null ? "" : companyOrder.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOrder.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOrder.getChangeddate());
                        tabModel.data[i][5] = companyOrder.getStatus() == null ? "" : companyOrder.getStatus();
                        tabModel.data[i][6] = companyOrder.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOrder.getOrderid();
                        i++;
                    }

                    objectIterator = query2.iterate();
                    while (objectIterator.hasNext()) {
                        final Companyoffer companyOffer = (Companyoffer) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyOffer);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_OFFER");
                        tabModel.data[i][1] = companyOffer.getCompany().getName();
                        tabModel.data[i][2] = companyOffer.getName() == null ? "" : companyOffer.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOffer.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOffer.getChangeddate());
                        tabModel.data[i][5] = companyOffer.getStatus() == null ? "" : companyOffer.getStatus();
                        tabModel.data[i][6] = companyOffer.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOffer.getOfferid();
                        i++;
                    }

                    objectIterator = query3.iterate();
                    while (objectIterator.hasNext()) {
                        final Companyactivities companyActivity = (Companyactivities) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyActivity);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_ACTIVITIES");
                        tabModel.data[i][1] = companyActivity.getCompany() == null ? "": companyActivity.getCompany().getName() == null ? "": companyActivity.getCompany().getName();
                        tabModel.data[i][2] = companyActivity.getActivityname() == null ? "": companyActivity.getActivityname();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyActivity.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyActivity.getChangeddate());
                        tabModel.data[i][5] = companyActivity.getActivitystatus() == null ? "": companyActivity.getActivitystatus();
                        tabModel.data[i][6] = companyActivity.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyActivity.getActivityid();
                        i++;
                    }

                    objectIterator = query4.iterate();
                    while (objectIterator.hasNext()) {
                        final Companyopportunity companyOpportunity = (Companyopportunity) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyOpportunity);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_OPPORTUNITY");
                        tabModel.data[i][1] = companyOpportunity.getCompany().getName();
                        tabModel.data[i][2] = companyOpportunity.getName() == null ? "" : companyOpportunity.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyOpportunity.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyOpportunity.getChangeddate());
                        tabModel.data[i][5] = companyOpportunity.getOpportunitystatus() == null ? "": companyOpportunity.getOpportunitystatus();
                        tabModel.data[i][6] = companyOpportunity.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyOpportunity.getOpportunityid();
                        i++;
                    }

                    objectIterator = query5.iterate();
                    while (objectIterator.hasNext()) {

                        final Companyservice companyService = (Companyservice) objectIterator.next();
                        EBISystem.hibernate().session("SUMMARY_SESSION").refresh(companyService);
                        tabModel.data[i][0] = EBISystem.i18n("EBI_LANG_C_SERVICE");
                        tabModel.data[i][1] = companyService.getCompany().getName();
                        tabModel.data[i][2] = companyService.getName() == null ? "" : companyService.getName();
                        tabModel.data[i][3] = EBISystem.getInstance().getDateToString(companyService.getCreateddate()) == null ? "": EBISystem.getInstance().getDateToString(companyService.getCreateddate());
                        tabModel.data[i][4] = EBISystem.getInstance().getDateToString(companyService.getChangeddate()) == null ? "": EBISystem.getInstance().getDateToString(companyService.getChangeddate());
                        tabModel.data[i][5] = companyService.getStatus() == null ? "" : companyService.getStatus();
                        tabModel.data[i][6] = companyService.getCompany().getCompanyid();
                        tabModel.data[i][7] = companyService.getServiceid();
                        i++;
                    }
 
                } else {
                	tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }
            }
           
        } catch (final org.hibernate.HibernateException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            return;
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            tabModel.fireTableDataChanged();
            EBISystem.gui().vpanel("Summary").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private String returnBuildQuery(final String object) {
        boolean haveCreated = false;

        String toRet = "";
        final String[] SQL = new String[7];

        if (!"".equals(EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getEditor().getText())) {
            haveCreated = true;
        }

        SQL[0] = " FROM ";
        SQL[1] = object + " o ";

        if ("Companyactivities".equals(object)) {
            SQL[2] = "  WHERE o.activityname LIKE (?1) ";
        } else {
            // SQL[2] = "";
            SQL[2] = "  WHERE o.name LIKE (?1)  ";
        }

        if (haveCreated == true) {
            SQL[3] = " AND o.createddate  BETWEEN (?1) AND (?2) ";
        } else {
            SQL[3] = "";
        }

        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(
                EBISystem.gui().combo("summaryStatusText", "Summary").getSelectedItem().toString())) {

            if ("Companyopportunity".equals(object)) {
                SQL[4] = "AND o.opportunitystatus LIKE (?1) ";
            } else if ("Companyactivities".equals(object)) {
                SQL[4] = "AND o.activitystatus LIKE (?1) ";
            } else {
                SQL[4] = "AND o.status LIKE (?1) ";
            }
        } else {
            SQL[4] = "";
        }

        if (!"".equals(EBISystem.gui().textField("companyText", "Summary").getText())
                && !"Crmcampaign".equals(object) && !"Crmproblemsolutions".equals(object)
                && !"Crminvoice".equals(object)) {
            SQL[5] = " AND o.company.customernr LIKE (?1)";
        } else {
            SQL[5] = "";
        }

        if (!"".equals(EBISystem.gui().combo("companyCategoryText", "Summary").getSelectedItem().toString())
                && !"Crmcampaign".equals(object) && !"Crmproblemsolutions".equals(object)
                && !"Crminvoice".equals(object)) {
            SQL[6] = " AND o.company.category LIKE (?1)";
            // SQL[6]="";
        } else {
            SQL[6] = "";
        }
 

        for (int i = 0; i < SQL.length; i++) { // put the query together
            toRet += SQL[i];
        }

        return toRet;
    }

    private Query setParamToHQuery(final Query qr) {

        boolean haveCreated = false;
        final String cmpCatTxt = EBISystem.gui().combo("companyCategoryText", "Summary").getSelectedItem().toString();
        final String srmTypText = EBISystem.gui().combo("summarytypeText", "Summary").getSelectedItem().toString();

        if (!"".equals(EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getEditor().getText())) {
            haveCreated = true;
        }

        int c = 1;
        qr.setParameter(c, EBISystem.gui().textField("summaryNameText", "Summary").getText() + "%");

        if (haveCreated == true) {
            qr.setParameter(c, EBISystem.gui().timePicker("summaryCreatedFromText", "Summary").getDate());
            qr.setParameter(c, EBISystem.gui().timePicker("summaryCreatedToText", "Summary").getDate());
        }

        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("summaryStatusText", "Summary").getSelectedItem().toString())) {
            qr.setParameter(c,EBISystem.gui().combo("summaryStatusText", "Summary").getSelectedItem() + "%");
        }

        if (!"".equals(EBISystem.gui().textField("companyText", "Summary").getText())
                && !EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(srmTypText)
                && !EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(srmTypText)
                && !EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(srmTypText)) {
            qr.setParameter(c, EBISystem.gui().textField("companyText", "Summary").getText() + "%");
        }

        if (!"".equals(cmpCatTxt) && !EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(srmTypText)
                && !EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(srmTypText) && !EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(srmTypText)) {
            if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(cmpCatTxt)) {
                qr.setParameter(c, "%");
            } else if (!EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN").equals(srmTypText)
                    && !EBISystem.i18n("EBI_LANG_C_TAB_PROSOL").equals(srmTypText)
                    && !EBISystem.i18n("EBI_LANG_C_TAB_INVOICE").equals(srmTypText)) {
                qr.setParameter(c, cmpCatTxt + "%");
            }
        }
 
        c++;
        return qr;
    }

}