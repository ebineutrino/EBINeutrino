package org.modules.utils;

import org.sdk.EBISystem;

public class EBICRMAutomate {
    
    public EBICRMAutomate() {}

    public boolean storeFromSelectedTab() {

        if (!EBISystem.getInstance().getIEBISystemUserRights().isCanSave()
                || !EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            if (!EBISystem.getInstance().getIEBISecurityInstance().secureModule()) {
                return false;
            }
        }

        boolean ret = true;
        String title = EBISystem.getModule().ebiContainer.getTabInstance().getTitleAt(EBISystem.getModule().ebiContainer.getSelectedTab());
        boolean canSaveCompany = true;
        boolean checkCompany = true;
        if (title.equals(EBISystem.i18n("EBI_LANG_C_CONTACT"))) {
            ret = EBISystem.getModule().getContactPane().saveContact(); // store contact
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ADRESS"))) { // Store address
            ret = EBISystem.getModule().getAddressPane().saveAddress();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_BANK_DATA"))) { // Store bank
            ret = EBISystem.getModule().getBankdataPane().saveBank();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"))) { // Store meeting
            ret = EBISystem.getModule().getMeetingProtocol().saveMeeting();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ACTIVITIES"))) { // Store activity
            ret = EBISystem.getModule().getActivitiesPane().saveActivity();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"))) { // Store opportunity
            ret = EBISystem.getModule().getOpportunityPane().saveOpportunity();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OFFER"))) { // Store Offer
            ret = EBISystem.getModule().getOfferPane().saveOffer();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ORDER"))) { // Store Order
            ret = EBISystem.getModule().getOrderPane().saveOrder();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_SERVICE"))) { // Store Service
            ret = EBISystem.getModule().getServicePane().saveService();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"))) { // Store Product
            ret = EBISystem.getModule().getEBICRMProductPane().saveProduct();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"))) { // Store ProSol
            ret = EBISystem.getModule().getProsolPane().saveprosol();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"))) { // Store Invoice
            ret = EBISystem.getModule().getInvoicePane().saveInvoice();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"))) { // Store Project
            ret = EBISystem.getModule().getProjectPane().saveProject();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_LEADS"))) { // Store Leads
            ret = EBISystem.getModule().getLeadPane().saveLeads();
            canSaveCompany = false;
        }
        if (canSaveCompany && ret) {
            EBISystem.getModule().saveCompany(checkCompany);
        }
        return ret;
    }

    public boolean deleteFromSelectedTab(final int id) {
        boolean ret = false;
        if (!EBISystem.getInstance().getIEBISystemUserRights().isCanDelete()
                || !EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            if (!EBISystem.getInstance().getIEBISecurityInstance().secureModule()) {
                return false;
            }
        }

        String title = EBISystem.getModule().ebiContainer.getTabInstance().getTitleAt(EBISystem.getModule().ebiContainer.getSelectedTab());
        if(EBISystem.getInstance().getCompany() != null && EBISystem.getInstance().getCompany().getName().equals(title)){
            EBISystem.getModule().delete();
        }else if (title.equals(EBISystem.i18n("EBI_LANG_C_CONTACT"))) {
            EBISystem.getModule().getContactPane().deleteContact();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ADRESS"))) { // Store address
            EBISystem.getModule().getAddressPane().deleteAddress();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_BANK_DATA"))) { // Store bank
            EBISystem.getModule().getBankdataPane().deleteBank();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"))) { // Store meeting
            EBISystem.getModule().getMeetingProtocol().deleteMeeting();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ACTIVITIES"))) { // Store activity
            EBISystem.getModule().getActivitiesPane().deleteActivity();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"))) { // Store opportunity
            EBISystem.getModule().getOpportunityPane().deleteOpportunity();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OFFER"))) { // Store Offer
            EBISystem.getModule().getOfferPane().deleteOffer();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ORDER"))) { // Store Order
            EBISystem.getModule().getOrderPane().deleteOrder();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_SERVICE"))) { // Store Service
            EBISystem.getModule().getServicePane().deleteService();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"))) { // Store Product
            EBISystem.getModule().getEBICRMProductPane().deleteProduct();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"))) { // Store ProSol
            EBISystem.getModule().getProsolPane().deleteprosol();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"))) { // Store Invoice
            EBISystem.getModule().getInvoicePane().deleteInvoice();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"))) { // Store Project
            EBISystem.getModule().getProjectPane().deleteProject();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_LEADS"))) { // Store Leads
            EBISystem.getModule().getLeadPane().deleteLead();
        } else if (EBISystem.getInstance().getCompany() != null && title.equals(title)) {
            ret = EBISystem.getModule().delete();
        }

        return ret;
    }

}
