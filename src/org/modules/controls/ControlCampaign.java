package org.modules.controls;

import org.sdk.model.hibernate.Crmcampaignprop;
import org.sdk.model.hibernate.Crmcampaignposition;
import org.sdk.model.hibernate.Crmcampaign;
import org.sdk.model.hibernate.Crmcampaigndocs;
import org.sdk.model.hibernate.Crmcampaignreceiver;
import org.modules.views.dialogs.EBICRMAddContactAddressType;
import org.modules.views.dialogs.EBIDialogProperties;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.*;

public class ControlCampaign {

    public Crmcampaign campaign = null;
    public int id = 0;
    public boolean isEdit = false;

    public ControlCampaign() {
        campaign = new Crmcampaign();
    }

    public Integer dataStore() {
        Integer campaignID = -1;
        try {
            EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
            if (isEdit == false) {
                campaign.setCreateddate(new Date());
                campaign.setCreatedfrom(EBISystem.gui().vpanel("Campaign").getCreatedFrom());
            } else {
                createHistory(this.campaign);
                campaign.setChangeddate(new Date());
                campaign.setChangedfrom(EBISystem.ebiUser);
            }

            if (EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getDate() != null) {
                campaign.setValidfrom(EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getDate());
            }

            if (EBISystem.gui().timePicker("campaingValidToText", "Campaign").getDate() != null) {
                campaign.setValidto(EBISystem.gui().timePicker("campaingValidToText", "Campaign").getDate());
            }

            if (EBISystem.gui().textField("CampaignNameText", "Campaign").getText() != null) {
                campaign.setName(EBISystem.gui().textField("CampaignNameText", "Campaign").getText());
            }

            campaign.setCampaignnr(EBISystem.gui().textField("CampaignNrTex", "Campaign").getText());

            if (EBISystem.gui().combo("CampaignStatusText", "Campaign").getSelectedItem() != null) {
                campaign.setStatus(EBISystem.gui().combo("CampaignStatusText", "Campaign").getSelectedItem().toString());
            }

            //Save properties
            if (!campaign.getCrmcampaignprops().isEmpty()) {
                final Iterator itrpr = campaign.getCrmcampaignprops().iterator();
                while (itrpr.hasNext()) {
                    final Crmcampaignprop crmprops = (Crmcampaignprop) itrpr.next();
                    crmprops.setCrmcampaign(campaign);
                    if (crmprops.getPropertiesid() != null && crmprops.getPropertiesid() < 0) {
                        crmprops.setPropertiesid(null);
                    }
                    EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(crmprops);
                }
            }

            //Save docs
            if (!campaign.getCrmcampaigndocses().isEmpty()) {
                final Iterator itrpr = campaign.getCrmcampaigndocses().iterator();
                while (itrpr.hasNext()) {
                    final Crmcampaigndocs crmdocs = (Crmcampaigndocs) itrpr.next();
                    crmdocs.setCrmcampaign(campaign);
                    if (crmdocs.getDocid() != null && crmdocs.getDocid() < 0) {
                        crmdocs.setDocid(null);
                    }
                    EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(crmdocs);
                }
            }

            //Save position
            if (!campaign.getCrmcampaignpositions().isEmpty()) {
                final Iterator itrpr = campaign.getCrmcampaignpositions().iterator();
                while (itrpr.hasNext()) {
                    final Crmcampaignposition crmpos = (Crmcampaignposition) itrpr.next();
                    crmpos.setCrmcampaign(campaign);
                    if (crmpos.getPositionid() != null && crmpos.getPositionid() < 0) {
                        crmpos.setPositionid(null);
                    }
                    EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(crmpos);
                }
            }

            //Save receiver
            if (!campaign.getCrmcampaignreceivers().isEmpty()) {
                final Iterator itrpr = campaign.getCrmcampaignreceivers().iterator();
                while (itrpr.hasNext()) {
                    final Crmcampaignreceiver crmrec = (Crmcampaignreceiver) itrpr.next();
                    crmrec.setCrmcampaign(campaign);
                    if (crmrec.getReceiverid() != null && crmrec.getReceiverid() < 0) {
                        crmrec.setReceiverid(null);
                    }
                    EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(crmrec);
                }
            }

            EBISystem.getInstance().getDataStore("Campaign", "ebiSave");
            EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(campaign);
            EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();

            if (!isEdit) {
                EBISystem.gui().vpanel("Campaign").setID(campaign.getCampaignid());
            }
            campaignID = campaign.getCampaignid();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return campaignID;
    }

    public Integer dataCopy(final int id) {
        Integer campID = -1;
        Query query;
        try {
            EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();

            query = EBISystem.hibernate().session("CAMPAIGN_SESSION").createQuery(
                    "from Crmcampaign where campaignid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {

                final Crmcampaign camp = (Crmcampaign) iter.next();
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();

                final Crmcampaign ncamp = new Crmcampaign();
                ncamp.setCreateddate(new Date());
                ncamp.setCreatedfrom(EBISystem.ebiUser);

                ncamp.setValidfrom(camp.getValidfrom());
                ncamp.setValidto(camp.getValidto());
                ncamp.setName(camp.getName() + " - (Copy)");
                ncamp.setCampaignnr(camp.getCampaignnr());
                ncamp.setStatus(camp.getStatus());

                //Save properties
                if (!camp.getCrmcampaignprops().isEmpty()) {
                    final Iterator itrpr = camp.getCrmcampaignprops().iterator();
                    while (itrpr.hasNext()) {
                        final Crmcampaignprop crmprops = (Crmcampaignprop) itrpr.next();

                        final Crmcampaignprop prop = new Crmcampaignprop();
                        prop.setCrmcampaign(ncamp);
                        prop.setCreateddate(new Date());
                        prop.setCreatedfrom(EBISystem.ebiUser);
                        prop.setName(crmprops.getName());
                        prop.setValue(crmprops.getValue());
                        ncamp.getCrmcampaignprops().add(prop);
                        EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(prop);
                    }
                }
                //Save docs
                if (!camp.getCrmcampaigndocses().isEmpty()) {
                    final Iterator itrpr = camp.getCrmcampaigndocses().iterator();
                    while (itrpr.hasNext()) {
                        final Crmcampaigndocs crmdocs = (Crmcampaigndocs) itrpr.next();

                        final Crmcampaigndocs dc = new Crmcampaigndocs();
                        dc.setCrmcampaign(ncamp);
                        dc.setCreateddate(new Date());
                        dc.setCreatedfrom(EBISystem.ebiUser);
                        dc.setFiles(crmdocs.getFiles());
                        dc.setName(crmdocs.getName());
                        ncamp.getCrmcampaigndocses().add(dc);
                        EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(dc);
                    }
                }
                //Save position
                if (!camp.getCrmcampaignpositions().isEmpty()) {
                    final Iterator itrpr = camp.getCrmcampaignpositions().iterator();
                    while (itrpr.hasNext()) {
                        final Crmcampaignposition crmpos = (Crmcampaignposition) itrpr.next();

                        final Crmcampaignposition p = new Crmcampaignposition();
                        p.setCrmcampaign(ncamp);
                        p.setCategory(crmpos.getCategory());
                        p.setCreateddate(new Date());
                        p.setCreatedfrom(EBISystem.ebiUser);
                        p.setDeduction(crmpos.getDeduction());
                        p.setDescription(crmpos.getDescription());
                        p.setNetamount(crmpos.getNetamount());
                        p.setPretax(crmpos.getPretax());
                        p.setProductid(crmpos.getProductid());
                        p.setProductname(crmpos.getProductname());
                        p.setProductnr(crmpos.getProductnr());
                        p.setQuantity(crmpos.getQuantity());
                        p.setTaxtype(crmpos.getTaxtype());
                        p.setType(crmpos.getType());
                        ncamp.getCrmcampaignpositions().add(p);
                        EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(p);
                    }
                }
                //Save receiver
                if (!camp.getCrmcampaignreceivers().isEmpty()) {
                    final Iterator itrpr = camp.getCrmcampaignreceivers().iterator();
                    while (itrpr.hasNext()) {
                        final Crmcampaignreceiver crmrec = (Crmcampaignreceiver) itrpr.next();

                        final Crmcampaignreceiver r = new Crmcampaignreceiver();
                        r.setCrmcampaign(ncamp);
                        r.setCnum(crmrec.getCnum());
                        r.setCountry(crmrec.getCountry());
                        r.setCreateddate(new Date());
                        r.setCompanyname(crmrec.getCompanyname());
                        r.setCompanynumber(crmrec.getCompanynumber());
                        r.setReceivervia(crmrec.getReceivervia());
                        r.setCreatedfrom(EBISystem.ebiUser);
                        r.setEmail(crmrec.getEmail());
                        r.setFax(crmrec.getFax());
                        r.setGender(crmrec.getGender());
                        r.setLocation(crmrec.getLocation());
                        r.setMittelname(crmrec.getMittelname());
                        r.setName(crmrec.getName());
                        r.setPbox(crmrec.getPbox());
                        r.setPhone(crmrec.getPhone());
                        r.setPosition(crmrec.getPosition());
                        r.setStreet(crmrec.getStreet());
                        r.setSurname(crmrec.getSurname());
                        r.setZip(crmrec.getZip());
                        ncamp.getCrmcampaignreceivers().add(r);
                        EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(r);
                    }
                }
                EBISystem.hibernate().session("CAMPAIGN_SESSION").saveOrUpdate(ncamp);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
                campID = ncamp.getCampaignid();
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return campID;
    }

    public void dataEdit(final int id) {
        EBISystem.gui().vpanel("Campaign").setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Query query;
        try {

            EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
            query = EBISystem.hibernate().session("CAMPAIGN_SESSION").createQuery(
                    "from Crmcampaign where campaignid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                this.id = id;

                // load campaign values
                this.campaign = (Crmcampaign) iter.next();
                EBISystem.gui().vpanel("Campaign").setID(campaign.getCampaignid());
                EBISystem.gui().vpanel("Campaign").setCreatedDate(EBISystem.getInstance().getDateToString(campaign.getCreateddate() == null ? new Date() : campaign.getCreateddate()));
                EBISystem.gui().vpanel("Campaign").setCreatedFrom(campaign.getCreatedfrom() == null ? EBISystem.ebiUser : campaign.getCreatedfrom());

                if (campaign.getValidfrom() != null) {
                    EBISystem.gui().timePicker("campaignValidFromText", "Campaign").setDate(campaign.getValidfrom());
                    EBISystem.gui().timePicker("campaignValidFromText", "Campaign")
                            .getEditor().setText(EBISystem.getInstance().getDateToString(campaign.getValidfrom()));

                }

                if (campaign.getValidto() != null) {
                    EBISystem.gui().timePicker("campaingValidToText", "Campaign").setDate(campaign.getValidto());
                    EBISystem.gui().timePicker("campaingValidToText", "Campaign")
                            .getEditor().setText(EBISystem.getInstance().getDateToString(campaign.getValidto()));
                }

                if (this.campaign.getChangeddate() != null) {
                    EBISystem.gui().vpanel("Campaign").setChangedDate(EBISystem.getInstance().getDateToString(campaign.getChangeddate()));
                    EBISystem.gui().vpanel("Campaign").setChangedFrom(campaign.getChangedfrom());
                } else {
                    EBISystem.gui().vpanel("Campaign").setChangedDate("");
                    EBISystem.gui().vpanel("Campaign").setChangedFrom("");
                }
                EBISystem.gui().textField("CampaignNrTex", "Campaign").setText(campaign.getCampaignnr());
                EBISystem.gui().textField("CampaignNameText", "Campaign").setText(campaign.getName());

                if (campaign.getStatus() != null) {
                    EBISystem.gui().combo("CampaignStatusText", "Campaign").setSelectedItem(campaign.getStatus());
                }

                EBISystem.getInstance().getDataStore("Campaign", "ebiEdit");
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            EBISystem.gui().vpanel("Campaign").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void dataDelete(final int id) {
        try {
            EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();

            final Query query = EBISystem.hibernate().session("CAMPAIGN_SESSION").createQuery(
                    "from Crmcampaign where campaignid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                final Crmcampaign cam = (Crmcampaign) iter.next();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").delete(cam);
                EBISystem.getInstance().getDataStore("Campaign", "ebiDelete");
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        }

    }

    public void dataShow(Integer id) {
        ResultSet set = null;
        int selRow = EBISystem.gui().table("companyCampaignTable", "Campaign").getSelectedRow();
        PreparedStatement ps1 = null;
      
        try {

            ps1 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM CRMCAMPAIGN ORDER BY CREATEDDATE DESC ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            if (set != null) {
                set.last();
                EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data = new Object[set.getRow()][5];

                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data[i][0] = set.getString("NAME") == null ? "" : set.getString("NAME");
                        EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data[i][1] = set.getString("STATUS") == null ? "" : set.getString("STATUS");
                        EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data[i][2] = set.getDate("VALIDFROM") == null ? "" : EBISystem.getInstance().getDateToString(set.getDate("VALIDFROM"));
                        EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data[i][3] = set.getDate("VALIDTO") == null ? "" : EBISystem.getInstance().getDateToString(set.getDate("VALIDTO"));
                        EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().data[i][4] = set.getInt("CAMPAIGNID");
                        if(id != -1 && id == set.getInt("CAMPAIGNID")){
                            selRow = i;
                        }
                        i++;
                    }
                } else {
                    EBISystem.getModule().getEBICRMCampaign().
                            getTabModelCampaign().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", ""}};
                }
            }
            EBISystem.getModule().getEBICRMCampaign().getTabModelCampaign().fireTableDataChanged();

        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    ps1.close();
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(selRow > -1){
            selRow = EBISystem.gui().table("companyCampaignTable", "Campaign").convertRowIndexToView(selRow);
            EBISystem.gui().table("companyCampaignTable", "Campaign").changeSelection(selRow, 0, false, false);
        }
    }

    public String dataShowAndMailReport(final int id, final boolean showWindow) {
        String fileName = "";
        Query query;
        try {
            query = EBISystem.hibernate().session("CAMPAIGN_SESSION").createQuery(
                    "from Crmcampaign where campaignid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {

                final Crmcampaign camp = (Crmcampaign) iter.next();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").refresh(camp);
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", camp.getCampaignid());

                final Iterator it = camp.getCrmcampaignreceivers().iterator();

                String to = "";
                int i = 1;
                final int c = camp.getCrmcampaignreceivers().size();
                while (it.hasNext()) {
                    final Crmcampaignreceiver rec = (Crmcampaignreceiver) it.next();
                    to += rec.getEmail();
                    if (i < c - 1) {
                        to += ";";
                    }
                    i++;
                }

                fileName = EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_CAMPAIGN")),
                        camp.getName(), showWindow, true, to);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_RECEIVER_WAS_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void dataShowReport(final int id) {

        Query query;

        try {
            query = EBISystem.hibernate().session("CAMPAIGN_SESSION").createQuery(
                    "from Crmcampaign where campaignid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            while (iter.hasNext()) {

                this.campaign = (Crmcampaign) iter.next();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").refresh(this.campaign);
                final Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", this.campaign.getCampaignid());

                EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_CAMPAIGN")),
                        this.campaign.getName());

            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNew() {
        campaign = new Crmcampaign();
        EBISystem.gui().vpanel("Campaign").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.gui().vpanel("Campaign").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Campaign").setChangedDate("");
        EBISystem.gui().vpanel("Campaign").setChangedFrom("");

        //todo remove this use initialize instead
        EBISystem.gui().textField("CampaignNrTex", "Campaign").setText("");
        EBISystem.gui().textField("CampaignNameText", "Campaign").setText("");
        EBISystem.gui().combo("CampaignStatusText", "Campaign").setSelectedIndex(0);
        EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getEditor().setText("");
        EBISystem.gui().timePicker("campaingValidToText", "Campaign").getEditor().setText("");
        id = -1;
        EBISystem.getInstance().getDataStore("Campaign", "ebiNew");
    }

    public void dataShowReciever() {

        if (this.campaign.getCrmcampaignreceivers().size() > 0) {
            EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data
                    = new Object[this.campaign.getCrmcampaignreceivers().size()][14];

            final Iterator itr = this.campaign.getCrmcampaignreceivers().iterator();
            int i = 0;
            while (itr.hasNext()) {

                final Crmcampaignreceiver obj = (Crmcampaignreceiver) itr.next();

                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][0] = obj.getReceivervia() == null ? "" : obj.getReceivervia();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][1] = obj.getCompanynumber() == null ? "" : obj.getCompanynumber();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][2] = obj.getCompanyname() == null ? "" : obj.getCompanyname();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][3] = obj.getGender() == null ? "" : obj.getGender();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][4] = obj.getSurname() == null ? "" : obj.getSurname();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][5] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][6] = obj.getPosition() == null ? "" : obj.getPosition();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][7] = obj.getStreet() == null ? "" : obj.getStreet();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][8] = obj.getZip() == null ? "" : obj.getZip();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][9] = obj.getLocation() == null ? "" : obj.getLocation();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][10] = obj.getPbox() == null ? "" : obj.getPbox();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][11] = obj.getCountry() == null ? "" : obj.getCountry();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][12] = obj.getEmail() == null ? "" : obj.getEmail();
                EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data[i][13] = obj.getReceiverid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", ""}};
        }
        EBISystem.getModule().getEBICRMCampaign().getTabModReceiver().fireTableDataChanged();
    }

    public void dataShowProduct() {
        if (this.campaign.getCrmcampaignpositions().size() > 0) {
            EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data = new Object[this.campaign.getCrmcampaignpositions().size()][9];

            final Iterator itr = this.campaign.getCrmcampaignpositions().iterator();
            int i = 0;
            final NumberFormat currency = NumberFormat.getCurrencyInstance();

            while (itr.hasNext()) {

                final Crmcampaignposition obj = (Crmcampaignposition) itr.next();

                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][0] = obj.getQuantity();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][1] = obj.getProductnr();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][2] = obj.getProductname() == null ? "" : obj.getProductname();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][3] = obj.getCategory() == null ? "" : obj.getCategory();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][4] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][5] = currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction()))) == null ? "" : currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction())));
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][6] = obj.getDeduction().equals("") ? "" : obj.getDeduction() + "%";
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data[i][8] = obj.getPositionid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMCampaign().getTabModProduct().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }
        EBISystem.getModule().getEBICRMCampaign().getTabModProduct().fireTableDataChanged();
    }

    private void createHistory(final Crmcampaign com) throws Exception {

        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(campaign.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + campaign.getCreatedfrom());

        if (campaign.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(campaign.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + campaign.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_C_CAMPAIGN_NR") + ": " + (campaign.getCampaignnr().equals(EBISystem.gui().textField("CampaignNrTex", "Campaign").getText()) == true ? campaign.getCampaignnr() : campaign.getCampaignnr() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_VALID_FROM") + ": " + (EBISystem.getInstance().getDateToString(campaign.getValidfrom()).equals(EBISystem.gui().timePicker("campaignValidFromText", "Campaign").getEditor().getText()) == true ? EBISystem.getInstance().getDateToString(campaign.getValidfrom()) : EBISystem.getInstance().getDateToString(campaign.getValidfrom()) + "$"));
        list.add(EBISystem.i18n("EBI_LANG_VALID_TO") + ": " + (EBISystem.getInstance().getDateToString(campaign.getValidto()).equals(EBISystem.gui().timePicker("campaingValidToText", "Campaign").getEditor().getText()) == true ? EBISystem.getInstance().getDateToString(campaign.getValidto()) : EBISystem.getInstance().getDateToString(campaign.getValidto()) + "$"));

        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + (campaign.getName().equals(EBISystem.gui().textField("CampaignNameText", "Campaign").getText()) == true ? campaign.getName() : campaign.getName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_STATUS") + ": " + (campaign.getStatus().equals(EBISystem.gui().combo("CampaignStatusText", "Campaign").getSelectedItem().toString()) == true ? campaign.getStatus() : campaign.getStatus() + "$"));
        list.add("*EOR*"); // END OF RECORD

        if (!campaign.getCrmcampaigndocses().isEmpty()) {
            final Iterator iter = campaign.getCrmcampaigndocses().iterator();

            while (iter.hasNext()) {
                final Crmcampaigndocs obj = (Crmcampaigndocs) iter.next();
                if (obj.getDocid() != null && obj.getDocid() > -1) {
                    list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_FILENAME") + ": " : EBISystem.i18n("EBI_LANG_FILENAME") + ": " + obj.getName());
                    list.add(EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " : EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " + EBISystem.getInstance().getDateToString(obj.getCreateddate()));
                    list.add(obj.getCreatedfrom() == null ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                    list.add("*EOR*"); // END OF RECORD
                }
            }
        }

        if (!campaign.getCrmcampaignpositions().isEmpty()) {
            final Iterator iter = campaign.getCrmcampaignpositions().iterator();

            while (iter.hasNext()) {
                final Crmcampaignposition obj = (Crmcampaignposition) iter.next();
                if (obj.getPositionid() != null && obj.getPositionid() > -1) {
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
        }

        if (!campaign.getCrmcampaignreceivers().isEmpty()) {

            final Iterator iter = campaign.getCrmcampaignreceivers().iterator();

            while (iter.hasNext()) {
                final Crmcampaignreceiver obj = (Crmcampaignreceiver) iter.next();
                if (obj.getReceiverid() != null && obj.getReceiverid() > -1) {
                    list.add(obj.getReceivervia() == null ? EBISystem.i18n("EBI_LANG_C_SEND_TYPE") + ":" : EBISystem.i18n("EBI_LANG_C_SEND_TYPE") + ": " + obj.getReceivervia());
                    list.add(obj.getGender() == null ? EBISystem.i18n("EBI_LANG_C_GENDER") + ":" : EBISystem.i18n("EBI_LANG_C_GENDER") + ": " + obj.getGender());
                    list.add(obj.getSurname() == null ? EBISystem.i18n("EBI_LANG_NAME") + ":" : EBISystem.i18n("EBI_LANG_NAME") + ": " + obj.getSurname());
                    list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_C_CNAME") + ":" : EBISystem.i18n("EBI_LANG_C_CNAME") + ": " + obj.getName());
                    list.add(obj.getPosition() == null ? EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ":" : EBISystem.i18n("EBI_LANG_CONTACT_POSITION") + ": " + obj.getPosition());
                    list.add(obj.getStreet() == null ? EBISystem.i18n("EBI_LANG_C_STREET_NR") + ":" : EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": " + obj.getStreet());
                    list.add(obj.getZip() == null ? EBISystem.i18n("EBI_LANG_C_ZIP") + ":" : EBISystem.i18n("EBI_LANG_C_ZIP") + ": " + obj.getZip());
                    list.add(obj.getLocation() == null ? EBISystem.i18n("EBI_LANG_C_LOCATION") + ":" : EBISystem.i18n("EBI_LANG_C_LOCATION") + ": " + obj.getLocation());
                    list.add(obj.getPbox() == null ? EBISystem.i18n("EBI_LANG_C_POST_CODE") + ":" : EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": " + obj.getPbox());
                    list.add(obj.getCountry() == null ? EBISystem.i18n("EBI_LANG_C_COUNTRY") + ":" : EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": " + obj.getCountry());
                    list.add("*EOR*");
                }
            }
        }

        if (!campaign.getCrmcampaignprops().isEmpty()) {

            final Iterator iter = campaign.getCrmcampaignprops().iterator();

            while (iter.hasNext()) {
                final Crmcampaignprop dim = (Crmcampaignprop) iter.next();
                if (dim.getPropertiesid() != null && dim.getPropertiesid() > -1) {
                    list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + dim.getName());
                    list.add(EBISystem.i18n("EBI_LANG_VALUE") + ": " + dim.getValue());
                    list.add("*EOR*"); // END OF RECORD
                }
            }
        }

        EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCampaignid(), "CRMCampaign", list));
    }

    public void dataAddProperties() {
        final EBIDialogProperties dim = new EBIDialogProperties(campaign, null);
        dim.setVisible();
    }

    public void dataEditPoperties(final int id) {
        final Iterator iter = this.campaign.getCrmcampaignprops().iterator();
        while (iter.hasNext()) {
            final Crmcampaignprop properties = (Crmcampaignprop) iter.next();
            if (id == properties.getPropertiesid()) {
                final EBIDialogProperties dim = new EBIDialogProperties(campaign, properties);
                dim.setVisible();
                break;
            }
        }
    }

    public void dataDeleteProperties(final int id) {
        final Iterator iter = this.campaign.getCrmcampaignprops().iterator();
        while (iter.hasNext()) {

            final Crmcampaignprop properties = (Crmcampaignprop) iter.next();

            if (id == properties.getPropertiesid()) {
                if (id >= 0) {
                    try {
                        EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
                        EBISystem.hibernate().session("CAMPAIGN_SESSION").delete(properties);
                        EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                campaign.getCrmcampaignprops().remove(properties);
                break;
            }
        }
    }

    public void dataShowProperties() {
        if (this.campaign.getCrmcampaignprops().size() > 0) {
            EBISystem.getModule().getEBICRMCampaign().getTabModProperties().data = new Object[this.campaign.getCrmcampaignprops().size()][3];
            final Iterator iter = this.campaign.getCrmcampaignprops().iterator();
            int i = 0;
            while (iter.hasNext()) {
                final Crmcampaignprop dim = (Crmcampaignprop) iter.next();
                EBISystem.getModule().getEBICRMCampaign().getTabModProperties().data[i][0] = dim.getName() == null ? "" : dim.getName();
                EBISystem.getModule().getEBICRMCampaign().getTabModProperties().data[i][1] = dim.getValue() == null ? "" : dim.getValue();
                EBISystem.getModule().getEBICRMCampaign().getTabModProperties().data[i][2] = dim.getPropertiesid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMCampaign().getTabModProperties().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getEBICRMCampaign().getTabModProperties().fireTableDataChanged();
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Crmcampaigndocs docs = new Crmcampaigndocs();
                docs.setDocid((campaign.getCrmcampaigndocses().size() + 1) * -1);
                docs.setCrmcampaign(campaign);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);
                campaign.getCrmcampaigndocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void dataShowDoc() {
        if (this.campaign.getCrmcampaigndocses().size() > 0) {
            EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data = new Object[this.campaign.getCrmcampaigndocses().size()][4];
            final Iterator itr = this.campaign.getCrmcampaigndocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Crmcampaigndocs obj = (Crmcampaigndocs) itr.next();
                EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data[i][3] = obj.getDocid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMCampaign().getTabModDoc().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getEBICRMCampaign().getTabModDoc().fireTableDataChanged();
    }

    public void dataViewDoc(final int id) {

        String FileName;
        String FileType;
        OutputStream fos;
        try {

            final Iterator iter = this.campaign.getCrmcampaigndocses().iterator();
            while (iter.hasNext()) {

                final Crmcampaigndocs docs = (Crmcampaigndocs) iter.next();

                if (id == docs.getDocid()) {
                    // Get the BLOB inputstream 

                    final String file = docs.getName().replaceAll(" ", "_");
                    final byte buffer[] = docs.getFiles();
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
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        } catch (final IOException exx1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.campaign.getCrmcampaigndocses().iterator();
        while (iter.hasNext()) {
            final Crmcampaigndocs doc = (Crmcampaigndocs) iter.next();
            if (id == doc.getDocid()) {
                this.campaign.getCrmcampaigndocses().remove(doc);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").delete(doc);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
                break;
            }
        }
    }

    public void dataDeleteReceiver(final int id) {
        final Iterator iter = this.campaign.getCrmcampaignreceivers().iterator();
        while (iter.hasNext()) {
            final Crmcampaignreceiver campRec = (Crmcampaignreceiver) iter.next();
            if (campRec.getReceiverid() == id) {
                campaign.getCrmcampaignreceivers().remove(campRec);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").delete(campRec);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
                break;
            }
        }
    }

    public void dataEditReceiver(final int id) {
        final Iterator iter = this.campaign.getCrmcampaignreceivers().iterator();
        while (iter.hasNext()) {
            final Crmcampaignreceiver campRec = (Crmcampaignreceiver) iter.next();
            if (campRec.getReceiverid() == id) {
                final EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(this, campRec);
                addCo.setVisible();
                break;
            }
        }
    }

    public void dataDeleteProduct(final int id) {
        final Iterator iter = this.campaign.getCrmcampaignpositions().iterator();
        while (iter.hasNext()) {
            final Crmcampaignposition camPro = (Crmcampaignposition) iter.next();
            if (camPro.getPositionid() == id) {
                campaign.getCrmcampaignpositions().remove(camPro);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").begin();
                EBISystem.hibernate().session("CAMPAIGN_SESSION").delete(camPro);
                EBISystem.hibernate().transaction("CAMPAIGN_SESSION").commit();
                break;
            }
        }
    }

    public Crmcampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(final Crmcampaign campaign) {
        this.campaign = campaign;
    }

    public Set<Crmcampaigndocs> getCampaignDocList() {
        return campaign.getCrmcampaigndocses();
    }

    public Set<Crmcampaignposition> getCampaignPosList() {
        return campaign.getCrmcampaignpositions();
    }

    public Set<Crmcampaignreceiver> getCampaignReceiverList() {
        return campaign.getCrmcampaignreceivers();
    }

    public Set<Crmcampaignprop> getPropertiesList() {
        return campaign.getCrmcampaignprops();
    }
}
