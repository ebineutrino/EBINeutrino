package ebiCRM.functionality;

import ebiCRM.EBICRMModule;
import ebiNeutrinoSDK.EBISystem;

public class EBICRMAutomate {

    private EBICRMModule ebiModule = null;

    public EBICRMAutomate(final EBICRMModule ebiMod) {
        this.ebiModule = ebiMod;
    }

    public boolean storeFromSelectedTab() {

        if (!EBISystem.getInstance().getIEBISystemUserRights().isCanSave()
                || !EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            if (!EBISystem.getInstance().getIEBISecurityInstance().secureModule()) {
                return false;
            }
        }

        boolean ret = true;
        String title = ebiModule.ebiContainer.getTabInstance().getTitleAt(ebiModule.ebiContainer.getSelectedTab());
        boolean canSaveCompany = true;
        boolean checkCompany = true;
        if (title.equals(EBISystem.i18n("EBI_LANG_C_CONTACT"))) {
            ret = EBISystem.getCRMModule().getContactPane().saveContact(); // store contact
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ADRESS"))) { // Store address
            ret = EBISystem.getCRMModule().getAddressPane().saveAddress();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_BANK_DATA"))) { // Store bank
            ret = EBISystem.getCRMModule().getBankdataPane().saveBank();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"))) { // Store meeting
            ret = EBISystem.getCRMModule().getMeetingProtocol().saveMeeting();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ACTIVITIES"))) { // Store activity
            ret = EBISystem.getCRMModule().getActivitiesPane().saveActivity();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"))) { // Store opportunity
            ret = EBISystem.getCRMModule().getOpportunityPane().saveOpportunity();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OFFER"))) { // Store Offer
            ret = EBISystem.getCRMModule().getOfferPane().saveOffer();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ORDER"))) { // Store Order
            ret = EBISystem.getCRMModule().getOrderPane().saveOrder();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_SERVICE"))) { // Store Service
            ret = EBISystem.getCRMModule().getServicePane().saveService();
            checkCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"))) { // Store Product
            ret = EBISystem.getCRMModule().getEBICRMProductPane().saveProduct();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"))) { // Store Campaign
            ret = EBISystem.getCRMModule().getEBICRMCampaign().saveCampaign();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"))) { // Store ProSol
            ret = EBISystem.getCRMModule().getProsolPane().saveprosol();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"))) { // Store Invoice
            ret = EBISystem.getCRMModule().getInvoicePane().saveInvoice();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"))) { // Store Project
            ret = EBISystem.getCRMModule().getProjectPane().saveProject();
            canSaveCompany = false;
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_LEADS"))) { // Store Leads
            ret = EBISystem.getCRMModule().getLeadPane().saveLeads();
            canSaveCompany = false;
        }
        if (canSaveCompany && ret) {
            EBISystem.getCRMModule().saveCompany(checkCompany);
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

        String title = ebiModule.ebiContainer.getTabInstance().getTitleAt(ebiModule.ebiContainer.getSelectedTab());
        if(EBISystem.getInstance().company != null && EBISystem.getInstance().company.getName().equals(title)){
            ebiModule.deleteCRM();
        }else if (title.equals(EBISystem.i18n("EBI_LANG_C_CONTACT"))) {
            EBISystem.getCRMModule().getContactPane().deleteContact();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ADRESS"))) { // Store address
            EBISystem.getCRMModule().getAddressPane().deleteAddress();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_BANK_DATA"))) { // Store bank
            EBISystem.getCRMModule().getBankdataPane().deleteBank();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL"))) { // Store meeting
            EBISystem.getCRMModule().getMeetingProtocol().deleteMeeting();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ACTIVITIES"))) { // Store activity
            EBISystem.getCRMModule().getActivitiesPane().deleteActivity();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY"))) { // Store opportunity
            EBISystem.getCRMModule().getOpportunityPane().deleteOpportunity();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_OFFER"))) { // Store Offer
            EBISystem.getCRMModule().getOfferPane().deleteOffer();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_ORDER"))) { // Store Order
            EBISystem.getCRMModule().getOrderPane().deleteOrder();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_SERVICE"))) { // Store Service
            EBISystem.getCRMModule().getServicePane().deleteService();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT"))) { // Store Product
            EBISystem.getCRMModule().getEBICRMProductPane().deleteProduct();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_CAMPAIGN"))) { // Store Campaign
            EBISystem.getCRMModule().getEBICRMCampaign().deleteCampaign();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROSOL"))) { // Store ProSol
            EBISystem.getCRMModule().getProsolPane().deleteprosol();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_INVOICE"))) { // Store Invoice
            EBISystem.getCRMModule().getInvoicePane().deleteInvoice();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_TAB_PROJECT"))) { // Store Project
            EBISystem.getCRMModule().getProjectPane().deleteProject();
        } else if (title.equals(EBISystem.i18n("EBI_LANG_C_LEADS"))) { // Store Leads
            EBISystem.getCRMModule().getLeadPane().deleteLead();
        } else if (EBISystem.getInstance().company != null && title.equals(title)) {
            ret = ebiModule.deleteCRM();
        }

        return ret;
    }

}
