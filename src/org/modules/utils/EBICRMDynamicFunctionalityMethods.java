package org.modules.utils;

import org.modules.views.EBICRMInvoiceView;
import org.modules.views.EBICRMProductView;
import org.modules.views.EBICRMProblemSolutionView;
import org.modules.views.EBICRMServiceView;
import org.modules.views.EBICRMCompanyView;
import org.modules.views.EBICRMPlanningView;
import org.modules.views.EBICRMOfferView;
import org.modules.views.EBICRMOrderView;
import org.modules.views.EBIMeetingProtocolView;
import org.modules.views.EBICRMCompanyActivityView;
import org.modules.views.EBICRMOpportunityView;
import org.modules.views.EBICRMAddressView;
import org.modules.views.dialogs.EBINewProjectTaskDialog;
import org.sdk.EBISystem;
import org.sdk.arbitration.EBIArbCallback;
import org.sdk.arbitration.EBIArbitration;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.modules.views.dialogs.EBIDialogProperties;

public final class EBICRMDynamicFunctionalityMethods {

    private static EBICRMDynamicFunctionalityMethods singleTon = null;
    
    
    public final Object[] getInternNumber(final String category, final boolean isInvoice) {
        ResultSet set = null;
        PreparedStatement ps = null;
        final Object[] toRet = new Object[2];

        try {
            String qu;
            if (isInvoice) {
                qu = "CRMINVOICENUMBER";
            } else {
                qu = "COMPANYNUMBER";
            }

            ps = EBISystem.getInstance().iDB().initPreparedStatement("select * from " + qu + " where CATEGORY=?  ");
            ps.setString(1, category);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {

                    toRet[0] = getAvailableInternalNumber(category, isInvoice);

                    if (Integer.parseInt(toRet[0].toString()) == 0) {
                        toRet[0] = set.getInt("NUMBERFROM");
                    } else {
                        toRet[0] = Integer.parseInt(toRet[0].toString()) + 1;
                    }

                    final int nrTo = set.getInt("NUMBERTO");

                    if (Integer.parseInt(toRet[0].toString()) > nrTo) {
                        toRet[0] = -1;
                    }

                    final String bgCahr = set.getString("BEGINCHAR");
                    toRet[1] = bgCahr == null ? "" : bgCahr;
                }
            } else {
                toRet[0] = -1;
                toRet[1] = "";
            }

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INTERNAL_NUMBER_NOTEXIST")).Show(EBIMessage.ERROR_MESSAGE);
            toRet[0] = -1;
        } finally {
            try {
                set.close();
                ps.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }

        return toRet;
    }

    private final int getAvailableInternalNumber(final String category, final boolean isInvoice) {

        ResultSet set = null;
        PreparedStatement ps = null;
        int toRet = 0;

        try {

            String qu;
            String nr;

            if (isInvoice) {
                qu = "CRMINVOICE";
                nr = "INVOICENR";
            } else {
                qu = "COMPANY";
                nr = "COMPANYNUMBER";
            }

            ps = EBISystem.getInstance().iDB().initPreparedStatement("select " + nr + " from " + qu + " where CATEGORY=? order by " + nr + " desc limit 1 ");
            ps.setString(1, category);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                if (set.next()) {
                    toRet = set.getInt(nr);
                }
            }

        } catch (final SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                set.close();
                ps.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }

        return toRet;
    }

    public final void initComboBoxes(final boolean reload) {
        
        final boolean haveModuleChange = EBISystem.canRelease;
        
        EBICRMCompanyView.categories = getStatusProperties("COMPANYCATEGORY");
        
        if (EBISystem.gui().existView("Company")) {
            EBICRMCompanyView.cooperations = getStatusProperties("COMPANYCOOPERATION");
            EBICRMCompanyView.classification = getStatusProperties("COMPANYCLASSIFICATION");
        }

        if (EBISystem.gui().existView("MeetingCall")) {
            EBIMeetingProtocolView.art = getStatusProperties("COMPANYMEETINGTYPE");
        }

        if (EBISystem.gui().existView("Opportunity")) {
            EBICRMOpportunityView.oppBussinesType = getStatusProperties("COMPANYOPPORTUNITYBUSTYP");
            EBICRMOpportunityView.oppSalesStage = getStatusProperties("COMPANYOPPORTUNITYSSTAGE");
            EBICRMOpportunityView.oppStatus = getStatusProperties("COMPANYOPPORTUNITYSTATUS");
            EBICRMOpportunityView.oppBudgetStatus = getStatusProperties("COMPANYOPPORTUNITYBGSTATUS");
            EBICRMOpportunityView.oppEvalStatus = getStatusProperties("COMPANYOPPORTUNITYEVSTATUS");
        }

        if (EBISystem.gui().existView("Address")) {
            EBICRMAddressView.AddressType = getStatusProperties("CRMADDRESSTYPE");
        }

        if (EBISystem.gui().existView("Activity")) {
            EBICRMCompanyActivityView.actType = getStatusProperties("COMPANYACTIVITYTYPE");
            EBICRMCompanyActivityView.actStatus = getStatusProperties("COMPANYACTIVITYSTATUS");
        }

        if (EBISystem.gui().existView("Offer")) {
            EBICRMOfferView.offerStatus = getStatusProperties("COMPANYOFFERSTATUS");
        }

        if (EBISystem.gui().existView("Order")) {
            EBICRMOrderView.orderStatus = getStatusProperties("COMPANYORDERSTATUS");
        }

        if (EBISystem.gui().existView("Service")) {
            EBICRMServiceView.serviceStatus = getStatusProperties("COMPANYSERVICESTATUS");
            EBICRMServiceView.serviceType = getStatusProperties("COMPANYSERVICETYPE");
            EBICRMServiceView.serviceCategory = getStatusProperties("COMPANYSERVICECATEGORY");
        }

        EBIArbitration.arbitrate().begin("LOAD_CLOSABLE_PROPERTIES", new EBIArbCallback() {
            @Override
            public boolean callback(final Thread currentThread) {

                EBICRMProblemSolutionView.prosolStatus = getStatusProperties("CRMPROBLEMSOLSTATUS");
                EBICRMProblemSolutionView.prosolType = getStatusProperties("CRMPROBLEMSOLTYPE");
                EBICRMProblemSolutionView.prosolCategory = getStatusProperties("CRMPROBLEMSOLCATEGORY");
                EBICRMProblemSolutionView.prosolClassification = getStatusProperties("CRMPROBLEMSOLCLASS");
                if (EBISystem.gui().existView("Prosol")) {
                    EBISystem.gui().combo("prosolStatusText", "Prosol").setModel(new DefaultComboBoxModel(EBICRMProblemSolutionView.prosolStatus));
                    EBISystem.gui().combo("prosolTypeText", "Prosol").setModel(new DefaultComboBoxModel(EBICRMProblemSolutionView.prosolType));
                    EBISystem.gui().combo("prosolCategoryText", "Prosol").setModel(new DefaultComboBoxModel(EBICRMProblemSolutionView.prosolCategory));
                    EBISystem.gui().combo("prosolClassificationText", "Prosol").setModel(new DefaultComboBoxModel(EBICRMProblemSolutionView.prosolClassification));
                }

                EBICRMProductView.category = getStatusProperties("COMPANYPRODUCTCATEGORY");
                EBICRMProductView.type = getStatusProperties("COMPANYPRODUCTTYPE");
                EBICRMProductView.taxType = getStatusProperties("COMPANYPRODUCTTAX");
                if (EBISystem.gui().existView("Product")) {
                    EBISystem.gui().combo("ProductCategoryText", "Product").setModel(new DefaultComboBoxModel(EBICRMProductView.category));
                    EBISystem.gui().combo("ProductTypeText", "Product").setModel(new DefaultComboBoxModel(EBICRMProductView.type));
                    EBISystem.gui().combo("productTaxTypeTex", "Product").setModel(new DefaultComboBoxModel(EBICRMProductView.taxType));
                }
                
                EBICRMPlanningView.projectStatus = getStatusProperties("CRMPROJECTSTATUS");
                if (EBISystem.gui().existView("Project")) {
                    EBISystem.gui().combo("prjStatusText", "Project").setModel(new DefaultComboBoxModel(EBICRMPlanningView.projectStatus));
                }

                EBINewProjectTaskDialog.taskStatus = getStatusProperties("CRMPROJECTTASKSTATUS");
                EBINewProjectTaskDialog.taskType = getStatusProperties("CRMPROJECTTASKTYPE");
                if (EBISystem.gui().existView("projectTaskDialog")) {
                    EBISystem.gui().combo("taskStatusText", "projectTaskDialog").setModel(new DefaultComboBoxModel(EBINewProjectTaskDialog.taskStatus));
                    EBISystem.gui().combo("taskTypeText", "projectTaskDialog").setModel(new DefaultComboBoxModel(EBINewProjectTaskDialog.taskType));
                }
                
                EBIDialogProperties.projectCost = getStatusProperties("CRMPROJECTCOSTS");
                if (EBISystem.gui().existView("costValueDialog")) {
                      EBISystem.gui().combo("propertiesText", "costValueDialog").setModel(new DefaultComboBoxModel(EBIDialogProperties.projectCost));
                }
                
                EBIDialogProperties.projectProperty = getStatusProperties("CRMPROJECTPROPS");
                if (EBISystem.gui().existView("projectPropertiesDialog")) {
                      EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").setModel(new DefaultComboBoxModel(EBIDialogProperties.projectProperty));
                }
                
                EBIDialogProperties.productDimension = getStatusProperties("CRMPRODUCTDIMENSIONS");
                if (EBISystem.gui().existView("propertiesDialog")) {
                      EBISystem.gui().combo("propertiesText", "propertiesDialog").setModel(new DefaultComboBoxModel(EBIDialogProperties.productDimension));
                }
                
                EBICRMInvoiceView.invoiceCategory = getStatusProperties("CRMINVOICECATEGORY");
                EBICRMInvoiceView.invoiceStatus = getStatusProperties("CRMINVOICESTATUS");
                if (EBISystem.gui().existView("Invoice")) {
                    EBISystem.gui().combo("invoiceStatusText", "Invoice").setModel(new DefaultComboBoxModel(EBICRMInvoiceView.invoiceStatus));
                    EBISystem.gui().combo("categoryText", "Invoice").setModel(new DefaultComboBoxModel(EBICRMInvoiceView.invoiceCategory));
                }
                return true;
            }
        });

        if (reload) {
            //reload combos values
            if (EBISystem.gui().existView("Company")) {
                EBISystem.gui().combo("companyCategoryText", "Summary").setModel(new DefaultComboBoxModel(EBICRMCompanyView.categories));
                EBISystem.gui().combo("categoryText", "Company").setModel(new DefaultComboBoxModel(EBICRMCompanyView.categories));
                EBISystem.gui().combo("cooperationText", "Company").setModel(new DefaultComboBoxModel(EBICRMCompanyView.cooperations));
                EBISystem.gui().combo("classificationText", "Company").setModel(new DefaultComboBoxModel(EBICRMCompanyView.classification));
            }

            if (EBISystem.gui().existView("MeetingCall")) {
                EBISystem.gui().combo("meetingTypeText", "MeetingCall").setModel(new DefaultComboBoxModel(EBIMeetingProtocolView.art));
            }

            if (EBISystem.gui().existView("Opportunity")) {
                EBISystem.gui().combo("oppBustypeText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOpportunityView.oppBussinesType));
                EBISystem.gui().combo("statusOppText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOpportunityView.oppStatus));
                EBISystem.gui().combo("oppBdgStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOpportunityView.oppBudgetStatus));
                EBISystem.gui().combo("oppEvalStatusText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOpportunityView.oppEvalStatus));
                EBISystem.gui().combo("oppSaleStateText", "Opportunity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOpportunityView.oppSalesStage));
            }

            if (EBISystem.gui().existView("Address")) {
                EBISystem.gui().combo("addressTypeText", "Address").setModel(new javax.swing.DefaultComboBoxModel(EBICRMAddressView.AddressType));
            }

            if (EBISystem.gui().existView("Activity")) {
                EBISystem.gui().combo("activityTypeText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMCompanyActivityView.actType));
                EBISystem.gui().combo("activityStatusText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(EBICRMCompanyActivityView.actStatus));
            }

            if (EBISystem.gui().existView("Offer")) {
                EBISystem.gui().combo("offerStatusText", "Offer").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOfferView.offerStatus));
            }

            if (EBISystem.gui().existView("Order")) {
                EBISystem.gui().combo("orderStatusText", "Order").setModel(new javax.swing.DefaultComboBoxModel(EBICRMOrderView.orderStatus));
            }

            if (EBISystem.gui().existView("Service")) {
                EBISystem.gui().combo("serviceStatusText", "Service").setModel(new javax.swing.DefaultComboBoxModel(EBICRMServiceView.serviceStatus));
                EBISystem.gui().combo("serviceTypeText", "Service").setModel(new javax.swing.DefaultComboBoxModel(EBICRMServiceView.serviceType));
                EBISystem.gui().combo("serviceCategoryText", "Service").setModel(new javax.swing.DefaultComboBoxModel(EBICRMServiceView.serviceCategory));
            }

        }
        EBISystem.canRelease = haveModuleChange;
    }

    public final boolean findCustomerNumber(final String Nr) {
        boolean found = false;
        ResultSet set = null;
        try {
            set = EBISystem.db().execute("SELECT COMPANYID FROM COMPANY WHERE CUSTOMERNR='" + Nr + "'");
            set.last();
            if (set.getRow() > 0) {
                found = true;
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                set.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return found;
    }

    public final String getCompanyNameFromID(final int id) {
        String sCompany = "";
        ResultSet set = null;
        PreparedStatement ps = null;
        try {
            ps = EBISystem.getInstance().iDB().initPreparedStatement("select NAME from COMPANY where COMPANYID=? order by NAME ");
            ps.setInt(1, id);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    sCompany = set.getString("TEMPLATE");
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                set.close();
                ps.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return sCompany;
    }

    public final String getEMailTemplate(final String name) {
        String strTemplate = "";
        ResultSet set = null;
        PreparedStatement ps = null;
        try {
            ps = EBISystem.getInstance().iDB().initPreparedStatement("select TEMPLATE from MAIL_TEMPLATE where NAME=? order by NAME");
            ps.setString(1, name);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    strTemplate = set.getString("TEMPLATE");
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                set.close();
                ps.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return strTemplate;
    }

    public final String[] getEMailTemplateNames() {
        return getStatusProperties("MAIL_TEMPLATE");
    }

    public final double calculatePreTaxPrice(final double pValue, final String quantity, final String deduction) {
        double retValue = 0.0;
        double nVal = 0.0;
        try {
            if (!quantity.equals("")) {
                try {
                    nVal = pValue * Integer.parseInt(quantity);
                    double tVal;
                    if (!deduction.equals("")) {
                        tVal = ((nVal * Integer.parseInt(deduction)) / 100);
                        nVal = nVal - tVal;
                    }
                } catch (final NumberFormatException ex) {
                    ex.printStackTrace();
                }
                final BigDecimal bd = new BigDecimal(nVal);
                final BigDecimal bd_round = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                retValue = bd_round.doubleValue();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    public final int getIdIndexFormArrayInATable(final Object data[][], final int pos, final int id) {
        int i = 0;
        for (i = 0; i < data.length - 1; i++) {
            if (Integer.parseInt(data[i][pos].toString()) == id) {
                break;
            }
        }
        return i;
    }

    public final double getTaxVal(final String cat) {
        double val = 0.0;

        ResultSet set = null;
        PreparedStatement ps = null;
        try {
            ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT NAME, TAXVALUE FROM COMPANYPRODUCTTAX WHERE NAME=?");
            ps.setString(1, cat);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    val = set.getDouble("TAXVALUE");
                }
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                set.close();
                ps.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    public final String[] getStatusProperties(final String Tab) {
        PreparedStatement ps = null;
        ResultSet set = null;
        String[] BUFF = null;
        try {
            ps = EBISystem.db().initPreparedStatement("SELECT * FROM " + Tab + " order by NAME");
            set = ps.executeQuery();
            BUFF = new String[1];
            if (set != null) {
                set.last();
                final int size = set.getRow();
                if (size > 0) {
                    set.beforeFirst();
                    BUFF = new String[size + 1];
                    int i = 1;
                    while (set.next()) {
                        BUFF[i] = set.getString("NAME");
                        i++;
                    }
                }
            }
            BUFF[0] = EBISystem.i18n("EBI_LANG_PLEASE_SELECT");
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                set.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
        return BUFF;
    }
    
    public static EBICRMDynamicFunctionalityMethods getInstance(){
        if(singleTon == null){
            singleTon = new EBICRMDynamicFunctionalityMethods();
        }
        return singleTon;
    }
    
}
