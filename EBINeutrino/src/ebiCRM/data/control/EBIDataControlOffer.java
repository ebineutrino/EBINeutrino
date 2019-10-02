package ebiCRM.data.control;

import ebiCRM.gui.dialogs.EBICRMAddContactAddressType;
import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.*;

import javax.swing.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class EBIDataControlOffer {

    public Companyoffer compOffer = null;
    public int opportunityID = 0;
    public boolean isEdit = false;

    public EBIDataControlOffer() {
        compOffer = new Companyoffer();
    }

    public boolean dataStore() {
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();

            if (isEdit == false) {
                compOffer.setCreateddate(new Date());
                compOffer.setCreatedfrom(EBISystem.gui().vpanel("Offer").getCreatedFrom());
                compOffer.setCompany(EBISystem.getInstance().company);
            } else {
                createHistory(EBISystem.getInstance().company);
                compOffer.setChangeddate(new Date());
                compOffer.setChangedfrom(EBISystem.ebiUser);
            }

            if (EBISystem.gui().timePicker("offerReceiverText", "Offer").getDate() != null) {
                compOffer.setOfferdate(EBISystem.gui().timePicker("offerReceiverText", "Offer").getDate());
            }

            if (EBISystem.gui().timePicker("validToText", "Offer").getDate() != null) {
                compOffer.setValidto(EBISystem.gui().timePicker("validToText", "Offer").getDate());
            }

            compOffer.setDescription(EBISystem.gui().textArea("offerDescriptionText", "Offer").getText());
            compOffer.setOffernr(EBISystem.gui().textField("offerNrText", "Offer").getText());
            compOffer.setName(EBISystem.gui().textField("offerNameText", "Offer").getText());

            if (!"".equals(EBISystem.gui().textField("offerOpportunityText", "Offer").getText())){
                compOffer.setOpportunityid(opportunityID);
            }

            if (EBISystem.gui().combo("offerStatusText", "Offer").getSelectedItem() != null) {
                compOffer.setStatus(EBISystem.gui().combo("offerStatusText", "Offer").getSelectedItem().toString());
            }

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(compOffer);

            //Save docs
            if (!this.compOffer.getCompanyofferdocses().isEmpty()){
                final Iterator iter = this.compOffer.getCompanyofferdocses().iterator();
                while (iter.hasNext()){
                    final Companyofferdocs doc = (Companyofferdocs) iter.next();
                    if(doc.getOfferdocid() != null && doc.getOfferdocid() < 0){
                        doc.setOfferdocid(null);
                    }
                    doc.setCompanyoffer(compOffer);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(doc);
                }
            }

            //Save position
            if (!this.compOffer.getCompanyofferpositionses().isEmpty()){
                final Iterator iter = this.compOffer.getCompanyofferpositionses().iterator();
                while (iter.hasNext()){
                    final Companyofferpositions pos = (Companyofferpositions) iter.next();
                    if(pos.getPositionid() != null && pos.getPositionid() < 0){
                        pos.setPositionid(null);
                    }
                    pos.setCompanyoffer(compOffer);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(pos);
                }
            }

            //Save receiver
            if (!this.compOffer.getCompanyofferreceivers().isEmpty()) {
                final Iterator iter = this.compOffer.getCompanyofferreceivers().iterator();
                while (iter.hasNext()) {
                    final Companyofferreceiver rec = (Companyofferreceiver) iter.next();
                    if(rec.getReceiverid() != null & rec.getReceiverid() < 0){
                        rec.setReceiverid(null);
                    }
                    rec.setCompanyoffer(compOffer);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(rec);
                }
            }

            EBISystem.getInstance().getDataStore("Offer", "ebiSave");
            EBISystem.getInstance().company.getCompanyoffers().add(compOffer);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

            if (!isEdit) {
                EBISystem.gui().vpanel("Offer").setID(compOffer.getOfferid());
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public void dataCopy(final int id) {

        try {
            if (EBISystem.getInstance().company.getCompanyoffers().size() > 0) {
            	Companyoffer offer = null;
            	for(Companyoffer offerObj :EBISystem.getInstance().company.getCompanyoffers() ) {
            		if(offerObj.getOfferid() == id) {
            			offer = offerObj;
            			break;
            		}
            	}

                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Companyoffer ofnew = new Companyoffer();
                ofnew.setCreateddate(new Date());
                ofnew.setCreatedfrom(EBISystem.ebiUser);
                ofnew.setCompany(offer.getCompany());
                ofnew.setOfferdate(offer.getOfferdate());
                ofnew.setValidto(offer.getValidto());
                ofnew.setDescription(offer.getDescription());
                ofnew.setOffernr(offer.getOffernr());
                ofnew.setName(offer.getName() + " - (Copy)");
                ofnew.setIsrecieved(offer.getIsrecieved());
                ofnew.setOpportunityid(offer.getOpportunityid());
                ofnew.setStatus(offer.getStatus());

                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(ofnew);

                //Save docs
                if (!offer.getCompanyofferdocses().isEmpty()) {
                    final Iterator itd = offer.getCompanyofferdocses().iterator();
                    while (itd.hasNext()) {
                        final Companyofferdocs doc = (Companyofferdocs) itd.next();

                        final Companyofferdocs dc = new Companyofferdocs();
                        dc.setCompanyoffer(ofnew);
                        dc.setCreateddate(new Date());
                        dc.setCreatedfrom(EBISystem.ebiUser);
                        dc.setFiles(doc.getFiles());
                        dc.setName(doc.getName());
                        offer.getCompanyofferdocses().add(dc);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(dc);
                    }
                }
                //Save position
                if (!offer.getCompanyofferpositionses().isEmpty()) {
                    final Iterator itp = offer.getCompanyofferpositionses().iterator();
                    while (itp.hasNext()) {
                        final Companyofferpositions pos = (Companyofferpositions) itp.next();

                        final Companyofferpositions p = new Companyofferpositions();
                        p.setCompanyoffer(ofnew);
                        p.setCategory(pos.getCategory());
                        p.setCreateddate(new Date());
                        p.setCreatedfrom(EBISystem.ebiUser);
                        p.setDeduction(pos.getDeduction());
                        p.setDescription(pos.getDescription());
                        p.setNetamount(pos.getNetamount());
                        p.setPretax(pos.getPretax());
                        p.setProductid(pos.getProductid());
                        p.setProductname(pos.getProductname());
                        p.setProductnr(pos.getProductnr());
                        p.setQuantity(pos.getQuantity());
                        p.setTaxtype(pos.getTaxtype());
                        p.setType(pos.getType());
                        offer.getCompanyofferpositionses().add(p);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(p);
                    }
                }

                //Save receiver
                if (!offer.getCompanyofferreceivers().isEmpty()) {
                    final Iterator itr = offer.getCompanyofferreceivers().iterator();
                    while (itr.hasNext()) {
                        final Companyofferreceiver rec = (Companyofferreceiver) itr.next();

                        final Companyofferreceiver r = new Companyofferreceiver();
                        r.setCompanyoffer(ofnew);
                        r.setCnum(rec.getCnum());
                        r.setCountry(rec.getCountry());
                        r.setCreateddate(new Date());
                        r.setCreatedfrom(EBISystem.ebiUser);
                        r.setEmail(rec.getEmail());
                        r.setFax(rec.getFax());
                        r.setGender(rec.getGender());
                        r.setLocation(rec.getLocation());
                        r.setMittelname(rec.getMittelname());
                        r.setName(rec.getName());
                        r.setPbox(rec.getPbox());
                        r.setPhone(rec.getPhone());
                        r.setPosition(rec.getPosition());
                        r.setReceivervia(rec.getReceivervia());
                        r.setStreet(rec.getStreet());
                        r.setSurname(rec.getSurname());
                        r.setZip(rec.getZip());
                        offer.getCompanyofferreceivers().add(r);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(r);
                    }
                }
                
                EBISystem.getInstance().company.getCompanyoffers().add(ofnew);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();

                EBISystem.gui().table("companyOfferTable", "Offer").
                        changeSelection(EBISystem.gui().table("companyOfferTable", "Offer").
                                convertRowIndexToView(EBISystem.getCRMModule().dynMethod.
                                        getIdIndexFormArrayInATable(EBISystem.getCRMModule().getOfferPane().tabModoffer.data, 7, ofnew.getOfferid())), 0, false, false);

            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dataEdit(final int id) { 
        if (EBISystem.getInstance().company.getCompanyoffers().size() > 0) {
        	for(Companyoffer offrObj : EBISystem.getInstance().company.getCompanyoffers()) {
        		if(offrObj.getOfferid() == id) {
        			compOffer = offrObj;
        			break;
        		}
        	}

            EBISystem.gui().vpanel("Offer").setID(compOffer.getOfferid());
            EBISystem.gui().vpanel("Offer").setCreatedDate(EBISystem.getInstance().getDateToString(compOffer.getCreateddate() == null ? new Date() : compOffer.getCreateddate()));
            EBISystem.gui().vpanel("Offer").setCreatedFrom(compOffer.getCreatedfrom() == null ? EBISystem.ebiUser : compOffer.getCreatedfrom());

            if (compOffer.getOfferdate() != null) {
                EBISystem.gui().timePicker("offerReceiverText", "Offer").setDate(compOffer.getOfferdate());
                EBISystem.gui().timePicker("offerReceiverText", "Offer").getEditor().setText(EBISystem.getInstance().getDateToString(compOffer.getOfferdate()));
            }

            if (compOffer.getValidto() != null) {
                EBISystem.gui().timePicker("validToText", "Offer").setDate(compOffer.getValidto());
                EBISystem.gui().timePicker("validToText", "Offer").getEditor().setText(EBISystem.getInstance().getDateToString(compOffer.getValidto()));
            }

            if (compOffer.getChangeddate() != null) {
                EBISystem.gui().vpanel("Offer").setChangedDate(EBISystem.getInstance().getDateToString(compOffer.getChangeddate()));
                EBISystem.gui().vpanel("Offer").setChangedFrom(compOffer.getChangedfrom());
            } else {
                EBISystem.gui().vpanel("Offer").setChangedDate("");
                EBISystem.gui().vpanel("Offer").setChangedFrom("");
            }
            
            EBISystem.gui().textField("offerNrText", "Offer").setText(compOffer.getOffernr() == null ? "" : compOffer.getOffernr());
            EBISystem.gui().textField("offerNameText", "Offer").setText(compOffer.getName());

            if (compOffer.getStatus() != null) {
                EBISystem.gui().combo("offerStatusText", "Offer").setSelectedItem(compOffer.getStatus());
            }

            if (compOffer.getOpportunityid() != null) {
                EBISystem.gui().textField("offerOpportunityText", "Offer").setText(getOpportunityName(compOffer.getOpportunityid()));
            }
            
            EBISystem.gui().textArea("offerDescriptionText", "Offer").setText(compOffer.getDescription());
            EBISystem.getInstance().getDataStore("Offer", "ebiEdit");

            EBISystem.gui().table("companyOfferTable", "Offer").
                    changeSelection(EBISystem.gui().table("companyOfferTable", "Offer").
                            convertRowIndexToView(EBISystem.getCRMModule().dynMethod.
                                    getIdIndexFormArrayInATable(EBISystem.getCRMModule().getOfferPane().tabModoffer.data, 7, id)), 0, false, false);
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void createOrderFromOffer(final int id) {

        EBISystem.getCRMModule().getOrderPane().dataControlOrder.dataNew();
        if (EBISystem.getInstance().company.getCompanyoffers().size() > 0) {
        	Companyoffer ofr = null;
        	for(Companyoffer offrObj : EBISystem.getInstance().company.getCompanyoffers()) {
        		if(offrObj.getOfferid() == id) {
        			ofr = offrObj;
        			break;
        		}
        	}

        	EBISystem.gui().textField("orderNrText", "Order").setText(ofr.getOffernr() == null ? "" : ofr.getOffernr());
            EBISystem.gui().textField("orderNameText", "Order").setText(ofr.getName());

            EBISystem.gui().textField("orderOfferText", "Order").setText(ofr.getName());
            EBISystem.getCRMModule().getOrderPane().dataControlOrder.setOfferID(id);

            EBISystem.gui().textArea("orderDescription", "Order").setText(ofr.getDescription());
            EBISystem.getCRMModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_ORDER")));

            if (ofr.getCompanyofferpositionses().size() > 0) {
                final Iterator it = ofr.getCompanyofferpositionses().iterator();
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_IMPORT_PRODUCT")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {

                    Companyofferpositions posi;
                    while (it.hasNext()) {

                        posi = (Companyofferpositions) it.next();
                        final Companyorderpositions ordPos = new Companyorderpositions();
                        ordPos.setProductid(posi.getProductid());
                        ordPos.setCategory(posi.getCategory());
                        ordPos.setDeduction(posi.getDeduction());
                        ordPos.setDescription(posi.getDescription());
                        ordPos.setNetamount(posi.getNetamount());
                        ordPos.setPretax(posi.getPretax());
                        ordPos.setProductname(posi.getProductname());
                        ordPos.setProductnr(posi.getProductnr());
                        ordPos.setQuantity(posi.getQuantity());
                        ordPos.setTaxtype(posi.getTaxtype());
                        ordPos.setType(posi.getType());
                        EBISystem.getCRMModule().getOrderPane().dataControlOrder.getCompOrder().getCompanyorderpositionses().add(ordPos);
                    }
                    EBISystem.getCRMModule().getOrderPane().dataControlOrder.dataShowProduct();
                }
            }

            if (ofr.getCompanyofferreceivers().size() > 0) {
                final Iterator itsr = ofr.getCompanyofferreceivers().iterator();
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_IMPORT_CONTACTS")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {

                    Companyofferreceiver contact;
                    while (itsr.hasNext()) {

                        contact = (Companyofferreceiver) itsr.next();
                        final Companyorderreceiver ordRec = new Companyorderreceiver();
                        ordRec.setReceivervia(contact.getReceivervia());
                        ordRec.setCnum(contact.getCnum());
                        ordRec.setGender(contact.getGender());
                        ordRec.setSurname(contact.getSurname());
                        ordRec.setName(contact.getName());
                        ordRec.setPosition(contact.getPosition());
                        ordRec.setFax(contact.getFax());
                        ordRec.setEmail(contact.getEmail());
                        ordRec.setCountry(contact.getCountry());
                        ordRec.setLocation(contact.getLocation());
                        ordRec.setStreet(contact.getStreet());
                        ordRec.setZip(contact.getZip());
                        ordRec.setPbox(contact.getPbox());
                        EBISystem.getCRMModule().getOrderPane().dataControlOrder.getCompOrder().getCompanyorderreceivers().add(ordRec);

                    }
                    EBISystem.getCRMModule().getOrderPane().dataControlOrder.dataShowReceiver();
                }
            }
            if (ofr.getCompanyofferdocses().size() > 0) {
                final Iterator itr = ofr.getCompanyofferdocses().iterator();
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_IMPORT_DOCUMENT")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {

                    while (itr.hasNext()) {

                        final Companyofferdocs obj = (Companyofferdocs) itr.next();
                        final Companyorderdocs docs = new Companyorderdocs();
                        docs.setName(obj.getName());
                        docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                        docs.setCreatedfrom(EBISystem.ebiUser);
                        docs.setFiles(obj.getFiles());
                        EBISystem.getCRMModule().getOrderPane().dataControlOrder.getCompOrder().getCompanyorderdocses().add(docs);

                    }
                    EBISystem.getCRMModule().getOrderPane().dataControlOrder.dataShowDoc();
                }
            }
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {
        if (EBISystem.getInstance().company.getCompanyoffers().size() > 0) {
        	for(Companyoffer offrObj : EBISystem.getInstance().company.getCompanyoffers()) {
        		if(offrObj.getOfferid() == id) {
        			compOffer = offrObj;
        			break;
        		}
        	}
            EBISystem.getInstance().getDataStore("Offer", "ebiDelete");
            EBISystem.getInstance().company.getCompanyoffers().remove(compOffer);
        	EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").delete(compOffer);
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
        }
    }

    public void dataShow() {

        final int srow = EBISystem.gui().table("companyOfferTable", "Offer").getSelectedRow();
        final int size = EBISystem.getInstance().company.getCompanyoffers().size();

        if (size > 0) {
            EBISystem.getCRMModule().getOfferPane().tabModoffer.data = new Object[size][8];

            final Iterator<Companyoffer> iter = EBISystem.getInstance().company.getCompanyoffers().iterator();

            int i = 0;
            while (iter.hasNext()) {

                final Companyoffer cOffer = iter.next();
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][0] = cOffer.getName() == null ? "" : cOffer.getName();
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][1] = cOffer.getOfferdate() == null ? "" : EBISystem.getInstance().getDateToString(cOffer.getOfferdate());
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][2] = cOffer.getValidto() == null ? "" : EBISystem.getInstance().getDateToString(cOffer.getValidto());
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][3] = String.valueOf(cOffer.getOpportunityid() == null ? "0" : cOffer.getOpportunityid());
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][4] = cOffer.getStatus() == null ? "" : cOffer.getStatus();
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][5] = cOffer.getDescription() == null ? "" : cOffer.getDescription();
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][6] = cOffer.getIsrecieved() == null ? 0 : cOffer.getIsrecieved();
                EBISystem.getCRMModule().getOfferPane().tabModoffer.data[i][7] = cOffer.getOfferid();
                i++;
            }

        } else {
            EBISystem.getCRMModule().getOfferPane().tabModoffer.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }

        EBISystem.getCRMModule().getOfferPane().tabModoffer.fireTableDataChanged();
        EBISystem.gui().table("companyOfferTable", "Offer").changeSelection(srow, 0, false, false);
    }

    public Hashtable<String, Double> getTaxName(final int id) {

        final NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        cashFormat.setMinimumFractionDigits(2);
        cashFormat.setMaximumFractionDigits(3);
        final Hashtable<String, Double> taxTable = new Hashtable();

        if (EBISystem.getInstance().company.getCompanyoffers().size() > 0) {
            
        	Companyoffer of = null;
        	for(Companyoffer offrObj : EBISystem.getInstance().company.getCompanyoffers()) {
        		if(offrObj.getOfferid() == id) {
                    of = offrObj;
        			break;
        		}
        	}

            final Iterator itx = of.getCompanyofferpositionses().iterator();
            while (itx.hasNext()) {
                final Companyofferpositions pos = (Companyofferpositions) itx.next();
                if (pos.getTaxtype() != null) {
                    if (taxTable.containsKey(pos.getTaxtype())) {
                        taxTable.put(pos.getTaxtype(), taxTable.get(pos.getTaxtype()) + (((pos.getNetamount() * pos.getQuantity().intValue()) * EBISystem.getCRMModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));
                    } else {
                        taxTable.put(pos.getTaxtype(), (((pos.getNetamount() * pos.getQuantity().intValue()) * EBISystem.getCRMModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));
                    }
                }
            }
        }

        return taxTable;
    }

    public void dataShowReport(final int id) {
        if (isEdit) {
            if (!dataStore()) {
                return;
            }
        }

        final Map<String, Object> map = new HashMap();
        map.put("ID", id);
        final Hashtable<String, Double> taxTable = getTaxName(id);
        final Iterator itx = taxTable.keySet().iterator();

        String taxTypes = "";
        String taxValues = "";

        final NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        cashFormat.setMinimumFractionDigits(2);
        cashFormat.setMaximumFractionDigits(3);

        while (itx.hasNext()) {
            final String key = ((String) itx.next());
            taxTypes += key + ":\n";
            taxValues += cashFormat.format(taxTable.get(key)) + "\n";
        }

        map.put("TAXDIFF_TEXT", taxTypes);
        map.put("TAXDIFF_VALUE", taxValues);

        EBISystem.getInstance().getIEBIReportSystemInstance().
                useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_OFFER")),
                        getOfferNamefromId(id));

    }

    public String dataShowAndMailReport(final int id, final boolean showWindow) {
        String fileName = "";

        if (isEdit) {
            if (!dataStore()) {
                return null;
            }
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);
        final Hashtable<String, Double> taxTable = getTaxName(id);
        final Iterator itx = taxTable.keySet().iterator();

        String taxTypes = "";
        String taxValues = "";

        final NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        cashFormat.setMinimumFractionDigits(2);
        cashFormat.setMaximumFractionDigits(3);

        while (itx.hasNext()) {
            final String key = ((String) itx.next());
            taxTypes += key + ":\n";
            taxValues += cashFormat.format(taxTable.get(key)) + "\n";
        }

        map.put("TAXDIFF_TEXT", taxTypes);
        map.put("TAXDIFF_VALUE", taxValues);
        String to = "";
        try {
        	Companyoffer offer = null;
        	for(Companyoffer offrObj : EBISystem.getInstance().company.getCompanyoffers()) {
        		if(offrObj.getOfferid() == id) {
        			offer = offrObj;
        			break;
        		}
        	}
 
            if (offer != null) {
                final Iterator ix = offer.getCompanyofferreceivers().iterator();
                int i = 1;

                final int c = offer.getCompanyofferreceivers().size();
                while (ix.hasNext()) {
                    final Companyofferreceiver rec = (Companyofferreceiver) ix.next();
                    to += rec.getEmail();
                    if (i < c - 1) {
                        to += ";";
                    }
                    i++;
                }
                fileName = EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_OFFER")),
                        getOfferNamefromId(id), showWindow, true, to);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_RECEIVER_WAS_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return fileName;
    }

    public void dataNew() {
        compOffer = new Companyoffer();
        EBISystem.getCRMModule().getOfferPane().initialize(false);
        EBISystem.getInstance().getDataStore("Offer", "ebiNew");
        EBISystem.gui().vpanel("Offer").setID(-1);
    }

    private void createHistory(final Company com) {

        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(compOffer.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + compOffer.getCreatedfrom());

        if (compOffer.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(compOffer.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + compOffer.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_OFFER_NUMBER") + ": " + (compOffer.getOffernr().equals(EBISystem.gui().textField("offerNrText", "Offer").getText()) == true ? compOffer.getOffernr() : compOffer.getOffernr() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + (compOffer.getName().equals(EBISystem.gui().textField("offerNameText", "Offer").getText()) == true ? compOffer.getName() : compOffer.getName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_STATUS") + ": " + (compOffer.getStatus().equals(EBISystem.gui().combo("offerStatusText", "Offer").getSelectedItem().toString()) == true ? compOffer.getStatus() : compOffer.getStatus() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_SEND_DATE") + ": " + (EBISystem.getInstance().getDateToString(compOffer.getOfferdate()).equals(EBISystem.gui().timePicker("offerReceiverText", "Offer").getEditor().getText()) == true ? EBISystem.getInstance().getDateToString(compOffer.getOfferdate()) : EBISystem.getInstance().getDateToString(compOffer.getOfferdate()) + "$"));
        list.add(EBISystem.i18n("EBI_LANG_VALID_TO") + ": " + (EBISystem.getInstance().getDateToString(compOffer.getValidto()).equals(EBISystem.gui().timePicker("validToText", "Offer").getEditor().getText()) == true ? EBISystem.getInstance().getDateToString(compOffer.getValidto()) : EBISystem.getInstance().getDateToString(compOffer.getValidto()) + "$"));
        list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + (compOffer.getDescription().equals(EBISystem.gui().textArea("offerDescriptionText", "Offer").getText()) == true ? compOffer.getDescription() : compOffer.getDescription() + "$"));

        list.add("*EOR*"); // END OF RECORD

        if (!compOffer.getCompanyofferdocses().isEmpty()) {

            final Iterator iter = compOffer.getCompanyofferdocses().iterator();
            while (iter.hasNext()) {
                final Companyofferdocs obj = (Companyofferdocs) iter.next();
                list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_FILENAME") + ": " : EBISystem.i18n("EBI_LANG_FILENAME") + ": " + obj.getName());
                list.add(EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " : EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " + EBISystem.getInstance().getDateToString(obj.getCreateddate()));
                list.add(obj.getCreatedfrom() == null ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                list.add("*EOR*");
            }

        }

        if (!compOffer.getCompanyofferpositionses().isEmpty()) {

            final Iterator iter = compOffer.getCompanyofferpositionses().iterator();

            while (iter.hasNext()) {
                final Companyofferpositions obj = (Companyofferpositions) iter.next();
                list.add(EBISystem.i18n("EBI_LANG_QUANTITY") + ": " + String.valueOf(obj.getQuantity()));
                list.add(EBISystem.i18n("EBI_LANG_PRODUCT_NUMBER") + ": " + obj.getProductnr());
                list.add(obj.getProductname() == null ? EBISystem.i18n("EBI_LANG_NAME") + ":" : EBISystem.i18n("EBI_LANG_NAME") + ": " + obj.getProductname());
                list.add(obj.getCategory() == null ? EBISystem.i18n("EBI_LANG_CATEGORY") + ":" : EBISystem.i18n("EBI_LANG_CATEGORY") + ": " + obj.getCategory());
                list.add(obj.getTaxtype() == null ? EBISystem.i18n("EBI_LANG_TAX") + ":" : EBISystem.i18n("EBI_LANG_TAX") + ": " + obj.getTaxtype());
                list.add(String.valueOf(obj.getPretax()) == null ? EBISystem.i18n("EBI_LANG_PRICE") + ":" : EBISystem.i18n("EBI_LANG_PRICE") + ": " + String.valueOf(obj.getPretax()));
                list.add(String.valueOf(obj.getDeduction()) == null ? EBISystem.i18n("EBI_LANG_DEDUCTION") + ":" : EBISystem.i18n("EBI_LANG_DEDUCTION") + ": " + String.valueOf(obj.getDeduction()));
                list.add(obj.getDescription() == null ? EBISystem.i18n("EBI_LANG_DESCRIPTION") + ":" : EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + obj.getDescription());
                list.add("*EOR*");
            }
        }

        if (!compOffer.getCompanyofferreceivers().isEmpty()) {

            final Iterator iter = compOffer.getCompanyofferreceivers().iterator();
            while (iter.hasNext()) {
                final Companyofferreceiver obj = (Companyofferreceiver) iter.next();
                list.add(obj.getReceivervia() == null ? EBISystem.i18n("EBI_LANG_C_SEND_TYPE") + ":" : EBISystem.i18n("EBI_LANG_C_SEND_TYPE") + ": " + obj.getReceivervia());
                list.add(obj.getGender() == null ? EBISystem.i18n("EBI_LANG_C_GENDER") + ":" : EBISystem.i18n("EBI_LANG_C_GENDER") + ": " + obj.getGender());
                list.add(obj.getSurname() == null ? EBISystem.i18n("EBI_LANG_NAME") + ":" : EBISystem.i18n("EBI_LANG_NAME") + ": " + obj.getSurname());
                list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_C_CNAME") + ":" : EBISystem.i18n("EBI_LANG_C_CNAME") + ": " + obj.getName());
                list.add(obj.getPosition() == null ? EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ":" : EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ":" + obj.getPosition());
                list.add(obj.getStreet() == null ? EBISystem.i18n("EBI_LANG_C_STREET_NR") + ":" : EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": " + obj.getStreet());
                list.add(obj.getZip() == null ? EBISystem.i18n("EBI_LANG_C_ZIP") + ":" : EBISystem.i18n("EBI_LANG_C_ZIP") + ": " + obj.getZip());
                list.add(obj.getLocation() == null ? EBISystem.i18n("EBI_LANG_C_LOCATION") + ":" : EBISystem.i18n("EBI_LANG_C_LOCATION") + ": " + obj.getLocation());
                list.add(obj.getPbox() == null ? EBISystem.i18n("EBI_LANG_C_POST_CODE") + ":" : EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": " + obj.getPbox());
                list.add(obj.getCountry() == null ? EBISystem.i18n("EBI_LANG_C_COUNTRY") + ":" : EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": " + obj.getCountry());
                list.add("*EOR*");
            }
        }

        try {
            EBISystem.getCRMModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Offer", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private String getOpportunityName(final int id) {
        String retName = "";

        final Iterator iter = EBISystem.getCRMModule().getOpportunityPane().dataOpportuniyControl.getOppportunityList().iterator();
        while (iter.hasNext()) {
            final Companyopportunity opportunity = (Companyopportunity) iter.next();
            if (opportunity.getOpportunityid() == id) {
                this.opportunityID = opportunity.getOpportunityid();
                retName = opportunity.getName();
                break;
            }
        }
        return retName;
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = compOffer.getCompanyofferdocses().iterator();
        while (iter.hasNext()) {
            final Companyofferdocs doc = (Companyofferdocs) iter.next();
            if (doc.getOfferdocid() == id) {
                this.compOffer.getCompanyofferdocses().remove(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataDeleteReceiver(final int id) {
        final Iterator iter = compOffer.getCompanyofferreceivers().iterator();
        while (iter.hasNext()) {

            final Companyofferreceiver offerrec = (Companyofferreceiver) iter.next();
            if (offerrec.getReceiverid() == id) {
                this.compOffer.getCompanyofferreceivers().remove(offerrec);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(offerrec);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataEditReceiver(final int id) {
        try {
            final Iterator iter = compOffer.getCompanyofferreceivers().iterator();
            while (iter.hasNext()) {

                final Companyofferreceiver offerrec = (Companyofferreceiver) iter.next();

                if (offerrec.getReceiverid() == id) {
                    final EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(this, offerrec);
                    addCo.setVisible();
                    break;
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dataDeleteProduct(final int id) {
        final Iterator iter = compOffer.getCompanyofferpositionses().iterator();
        while (iter.hasNext()) {
            final Companyofferpositions offerpro = (Companyofferpositions) iter.next();
            if (offerpro.getPositionid() == id) {
                this.compOffer.getCompanyofferpositionses().remove(offerpro);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(offerpro);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataShowReceiver() {
        if (this.compOffer.getCompanyofferreceivers().size() > 0) {
            EBISystem.getCRMModule().getOfferPane().tabModReceiver.data = new Object[this.compOffer.getCompanyofferreceivers().size()][12];

            final Iterator itr = this.compOffer.getCompanyofferreceivers().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companyofferreceiver obj = (Companyofferreceiver) itr.next();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][0] = obj.getReceivervia() == null ? "" : obj.getReceivervia();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][1] = obj.getGender() == null ? "" : obj.getGender();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][2] = obj.getSurname() == null ? "" : obj.getSurname();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][3] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][4] = obj.getPosition() == null ? "" : obj.getPosition();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][5] = obj.getStreet() == null ? "" : obj.getStreet();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][6] = obj.getZip() == null ? "" : obj.getZip();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][7] = obj.getLocation() == null ? "" : obj.getLocation();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][8] = obj.getPbox() == null ? "" : obj.getPbox();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][9] = obj.getCountry() == null ? "" : obj.getCountry();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][10] = obj.getEmail() == null ? "" : obj.getEmail();
                EBISystem.getCRMModule().getOfferPane().tabModReceiver.data[i][11] = obj.getReceiverid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getOfferPane().tabModReceiver.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "",""}};
        }
        EBISystem.getCRMModule().getOfferPane().tabModReceiver.fireTableDataChanged();
    }

    public void dataShowProduct() {
        if (this.compOffer.getCompanyofferpositionses().size() > 0) {
            EBISystem.getCRMModule().getOfferPane().tabModProduct.data = new Object[this.compOffer.getCompanyofferpositionses().size()][9];

            final Iterator itr = this.compOffer.getCompanyofferpositionses().iterator();
            int i = 0;
            final NumberFormat currency = NumberFormat.getCurrencyInstance();

            while (itr.hasNext()) {
                final Companyofferpositions obj = (Companyofferpositions) itr.next();

                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][0] = String.valueOf(obj.getQuantity());
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][1] = obj.getProductnr();
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][2] = obj.getProductname() == null ? "" : obj.getProductname();
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][3] = obj.getCategory() == null ? "" : obj.getCategory();
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][4] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][5] = currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction()))) == null ? "" : currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction())));
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][6] = obj.getDeduction().equals("") ? "" : obj.getDeduction() + "%";
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getCRMModule().getOfferPane().tabModProduct.data[i][8] = obj.getPositionid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getOfferPane().tabModProduct.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }
        EBISystem.getCRMModule().getOfferPane().tabModProduct.fireTableDataChanged();
    }

    public void dataShowDoc() {
        if (this.compOffer.getCompanyofferdocses().size() > 0) {
            EBISystem.getCRMModule().getOfferPane().tabModDoc.data = new Object[this.compOffer.getCompanyofferdocses().size()][4];

            final Iterator itr = this.compOffer.getCompanyofferdocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companyofferdocs obj = (Companyofferdocs) itr.next();
                EBISystem.getCRMModule().getOfferPane().tabModDoc.data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getCRMModule().getOfferPane().tabModDoc.data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getCRMModule().getOfferPane().tabModDoc.data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getCRMModule().getOfferPane().tabModDoc.data[i][3] = obj.getOfferdocid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getOfferPane().tabModDoc.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getCRMModule().getOfferPane().tabModDoc.fireTableDataChanged();
    }

    public void dataViewDoc(final int id) {
        String FileName;
        String FileType;
        OutputStream fos;
        try {

            final Iterator iter = this.compOffer.getCompanyofferdocses().iterator();
            while (iter.hasNext()) {

                final Companyofferdocs doc = (Companyofferdocs) iter.next();

                if (id == doc.getOfferdocid()) {
                    // Get the BLOB inputstream
                    final String file = doc.getName().replaceAll(" ", "_");
                    final byte buffer[] = doc.getFiles();
                    FileName = "tmp/" + file;
                    FileType = file.substring(file.lastIndexOf("."));
                    fos = new FileOutputStream(FileName);
                    fos.write(buffer, 0, buffer.length);
                    fos.close();
                    EBISystem.getInstance().resolverType(FileName, FileType);
                    break;
                }
            }
        } catch (final FileNotFoundException exx) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final IOException exx1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public void dataAddDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {

            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Companyofferdocs docs = new Companyofferdocs();
                docs.setOfferdocid((compOffer.getCompanyofferdocses().size() + 1) * -1);
                docs.setCompanyoffer(compOffer);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);
                compOffer.getCompanyofferdocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public Companyoffer getCompOffer() {
        return compOffer;
    }

    public void setCompOffer(final Companyoffer compOffer) {
        this.compOffer = compOffer;
    }

    public Set<Companyofferdocs> getOfferDocList() {
        return compOffer.getCompanyofferdocses();
    }

    public Set<Companyofferpositions> getOfferPosList() {
        return compOffer.getCompanyofferpositionses();
    }

    public Set<Companyofferreceiver> getOfferRecieverList() {
        return compOffer.getCompanyofferreceivers();
    }

    public Set<Companyoffer> getOfferList() {
        return EBISystem.getInstance().company.getCompanyoffers();
    }

    private String getOfferNamefromId(final int id) {
        String name = "";
        final Iterator iter = EBISystem.getInstance().company.getCompanyoffers().iterator();
        while (iter.hasNext()) {
            final Companyoffer offer = (Companyoffer) iter.next();
            if (offer.getOfferid() == id) {
                name = offer.getName();
                break;
            }
        }
        return name;
    }
}