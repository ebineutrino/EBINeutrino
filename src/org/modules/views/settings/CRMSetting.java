package org.modules.views.settings;

import org.modules.views.dialogs.EBIDialogInternalNumberAdministration;
import org.modules.views.dialogs.EBIDialogTaxAdministration;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialogValueSetter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CRMSetting {

    public CRMSetting() {
        EBISystem.gui().loadGUI("CRMDialog/crmSettingDialog.xml");
        initialize();
    }

    public void setVisible() {
        EBISystem.gui().dialog("crmSettingDialog").setTitle(EBISystem.i18n("EBI_LANG_C_SETTING"));
        EBISystem.gui().vpanel("crmSettingDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_SETTING"));

        EBISystem.gui().getPanel("companyPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_COMPANY_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("adressMeetingPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_ADRESS_MEETING_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("opportunityPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_OPPORTUNITY_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("activityPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_ACTIVITY_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("offerOrderPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_OFFER_ORDER_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("taxProductPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_TAX_PRODUCT_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("servicePanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_SERVICE_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("problemSolPanel", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_PROSOL_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("projectSettings", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_PROJECT_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        EBISystem.gui().getPanel("invoiceSettings", "crmSettingDialog").setBorder(BorderFactory.createTitledBorder(null, EBISystem.i18n("EBI_LANG_SETTINGS_FOR_INVOICE_PANEL"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

        EBISystem.gui().label("autoIncAdm", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_CRM_INTERNAL_NUMBER_SETTINGS"));
        EBISystem.gui().label("category", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
        EBISystem.gui().label("cooperation", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_COOPERATION"));
        EBISystem.gui().label("classification", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_KLASSIFICATION"));
        EBISystem.gui().label("addressType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_ADRESS_TYPE"));
        EBISystem.gui().label("reportType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_MEMO_TYPE"));
        EBISystem.gui().label("busType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_BUSINESS_TYP"));
        EBISystem.gui().label("saleStage", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_SALE_STAGE"));
        EBISystem.gui().label("budgetType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_BUDGET_TYPE"));
        EBISystem.gui().label("evalType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_EVALUATING_TYPE"));
        EBISystem.gui().label("oppStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY_STATUS"));
        EBISystem.gui().label("activityStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_ACTIVITY_STATUS"));
        EBISystem.gui().label("activityType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_ACTIVITY_TYPE"));
        EBISystem.gui().label("offerStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_OFFER_STATUS"));
        EBISystem.gui().label("orderStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_ORDER_STATUS"));
        EBISystem.gui().label("productCategory", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_PRODUCT_CATEGORY"));
        EBISystem.gui().label("productType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_PRODUCT_TYPE"));
        EBISystem.gui().label("ProductProperties", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_PRODUCT_PROPERTIES"));
        EBISystem.gui().label("serviceStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_STATUS"));
        EBISystem.gui().label("serviceType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_TYPE"));
        EBISystem.gui().label("serviceCategory", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
        EBISystem.gui().label("prosolStatus", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_STATUS"));
        EBISystem.gui().label("prosolType", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_TYPE"));
        EBISystem.gui().label("prosolCategory", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
        EBISystem.gui().label("prosolClassfication", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_CLASSIFICATION"));

        EBISystem.gui().button("autoIncAdmButton", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_MANAGING"));
        EBISystem.gui().button("autoIncAdmButton", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogInternalNumberAdministration adm = new EBIDialogInternalNumberAdministration(false);
                adm.setVisible();
            }

        });

        EBISystem.gui().button("invoiceNrAdministration", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogInternalNumberAdministration adm = new EBIDialogInternalNumberAdministration(true);
                adm.setVisible();
            }

        });

        EBISystem.gui().button("catBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("catBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyCategory", EBISystem.i18n("EBI_LANG_C_CRM_CATEGORY_TYP"));
                setCategory.setVisible();

            }
        });

        EBISystem.gui().button("coopBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("coopBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory 
                        = new EBIDialogValueSetter("CompanyCooperation", EBISystem.i18n("EBI_LANG_C_CRM_COOPERATION_TYP"));
                setCategory.setVisible();

            }
        });

        EBISystem.gui().button("classBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("classBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyClassification", EBISystem.i18n("EBI_LANG_C_CRM_CLASSIFICATION_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("addressTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("addressTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CRMAddressType", EBISystem.i18n("EBI_LANG_C_CRM_ADRESS_TYP"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("reportTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("reportTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyMeetingType", EBISystem.i18n("EBI_LANG_C_CRM_MEMO_TYP"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("bussTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("bussTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOpportunityBusTyp", EBISystem.i18n("EBI_LANG_C_CRM_BUSINESS_TYP"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("saleStgBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("saleStgBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOpportunitySStage", EBISystem.i18n("EBI_LANG_C_CRM_SALE_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("budTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("budTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOpportunityBgStatus", EBISystem.i18n("EBI_LANG_C_CRM_BUDGET_TYP"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("evalTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("evalTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOpportunityEvStatus", EBISystem.i18n("EBI_LANG_C_CRM_EVALUATING_TYP"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("oppstatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("oppstatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOpportunityStatus", EBISystem.i18n("EBI_LANG_C_CRM_OPPORTUNITY_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("activityStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("activityStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyActivityStatus", EBISystem.i18n("EBI_LANG_C_CRM_ACTIVITY_STATUS"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("activityTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource(("lf_monitoring.png")));
        EBISystem.gui().button("activityTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyActivityType", EBISystem.i18n("EBI_LANG_C_CRM_ACTIVITY_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("offerStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("offerStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOfferStatus", EBISystem.i18n("EBI_LANG_C_CRM_OFFER_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("orderStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("orderStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setCategory = new EBIDialogValueSetter("CompanyOrderStatus", EBISystem.i18n("EBI_LANG_C_CRM_ORDER_TYPE"));
                setCategory.setVisible();
            }
        });

        EBISystem.gui().button("taxAdminBnt", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_CRM_TAX_ADMINISTRATION"));
        EBISystem.gui().button("taxAdminBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogTaxAdministration tadmin = new EBIDialogTaxAdministration();
                tadmin.setVisible();
            }
        });

        EBISystem.gui().button("productCatBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("productCatBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("COMPANYPRODUCTCATEGORY", EBISystem.i18n("EBI_LANG_C_CRM_PRODUCT_CATEGORY_TYP"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("productTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("productTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("COMPANYPRODUCTTYPE", EBISystem.i18n("EBI_LANG_C_CRM_PRODUCT_TYPE"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("productPropBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("productPropBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("CRMPRODUCTDIMENSIONS", EBISystem.i18n("EBI_LANG_C_CRM_PRODUCT_PROPERTIES"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("serviceStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("serviceStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("companyservicestatus", EBISystem.i18n("EBI_LANG_STATUS"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("serviceTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("serviceTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("companyservicetype", EBISystem.i18n("EBI_LANG_TYPE"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("serviceCatBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("serviceCatBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("companyservicecategory", EBISystem.i18n("EBI_LANG_CATEGORY"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("prosolStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("prosolStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmproblemsolstatus", EBISystem.i18n("EBI_LANG_STATUS"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("prosolTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("prosolTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmproblemsoltype", EBISystem.i18n("EBI_LANG_TYPE"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("prosolCateBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("prosolCateBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmproblemsolcategory", EBISystem.i18n("EBI_LANG_CATEGORY"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("prosolClassBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("prosolClassBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmproblemsolclass", EBISystem.i18n("EBI_LANG_CLASSIFICATION"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("prjStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("prjStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmprojectstatus", EBISystem.i18n("EBI_LANG_PROJECT_STATUS"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("taskStatusBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("taskStatusBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmprojecttaskstatus", EBISystem.i18n("EBI_LANG_TASK_STATUS"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("taskTypeBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("taskTypeBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmprojecttasktype", EBISystem.i18n("EBI_LANG_TASK_TYPE"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("costPropBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("costPropBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmprojectcosts", EBISystem.i18n("EBI_LANG_COST_PROPERTY"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("taskPropBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("taskPropBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crmprojectprops", EBISystem.i18n("EBI_LANG_TASK_PROPERTY"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("invoiceStsBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("invoiceStsBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crminvoicestatus", EBISystem.i18n("EBI_LANG_STATUS"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("invoiceCatBnt", "crmSettingDialog").setIcon(EBISystem.getInstance().getIconResource("lf_monitoring.png"));
        EBISystem.gui().button("invoiceCatBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final EBIDialogValueSetter setValueSetter = new EBIDialogValueSetter("Crminvoicecategory", EBISystem.i18n("EBI_LANG_CATEGORY"));
                setValueSetter.setVisible();
            }
        });

        EBISystem.gui().button("eMailTemplateDialogBnt", "crmSettingDialog").setText(EBISystem.i18n("EBI_LANG_C_CRM_EMAIL_TEMPLATE_SETTING"));
        EBISystem.gui().button("eMailTemplateDialogBnt", "crmSettingDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new EBICRMSettingEMailTemplate().setVisible();
            }
        });

        EBISystem.gui().showGUI();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {

        EBISystem.gui().dialog("crmSettingDialog").addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                refreshingCombo();
            }
        });
    }

    public void refreshingCombo() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                EBISystem.getModule().dynMethod.initComboBoxes(true);
            }
        };

        final Thread thread = new Thread(runnable, "refreshComboThread");
        thread.start();
    }
}
