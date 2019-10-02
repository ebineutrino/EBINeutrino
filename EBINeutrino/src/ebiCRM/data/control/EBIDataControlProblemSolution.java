package ebiCRM.data.control;

import ebiCRM.utils.EBICRMHistoryCreator;
import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.Crmproblemsoldocs;
import ebiNeutrinoSDK.model.hibernate.Crmproblemsolposition;
import ebiNeutrinoSDK.model.hibernate.Crmproblemsolutions;
import org.hibernate.query.Query;

import javax.swing.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

public class EBIDataControlProblemSolution {

    public Crmproblemsolutions compProsol = null;
    public boolean isEdit=false;

    public EBIDataControlProblemSolution() {
        this.compProsol = new Crmproblemsolutions();
    }

    public boolean dataStore() {

        try {
            EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
            if (isEdit == false) {
            	compProsol.setCreatedfrom(EBISystem.gui().vpanel("Prosol").getCreatedFrom());
                compProsol.setCreateddate(new Date());
            } else {
                createHistory();
                compProsol.setChangeddate(new Date());
                compProsol.setChangedfrom(EBISystem.ebiUser);
            }

            compProsol.setDescription(EBISystem.gui().textArea("prosolDescriptionText", "Prosol").getText());
            compProsol.setServicenr(EBISystem.gui().textField("prosolNrText", "Prosol").getText());
            compProsol.setName(EBISystem.gui().textField("prosolNameText", "Prosol").getText());

            if (EBISystem.gui().combo("prosolStatusText", "Prosol").getSelectedItem() != null) {
                compProsol.setStatus(EBISystem.gui().combo("prosolStatusText", "Prosol").getSelectedItem().toString());
            }

            if (EBISystem.gui().combo("prosolCategoryText", "Prosol").getSelectedItem() != null) {
                compProsol.setCategory(EBISystem.gui().combo("prosolCategoryText", "Prosol").getSelectedItem().toString());
            }

            if (EBISystem.gui().combo("prosolTypeText", "Prosol").getSelectedItem() != null) {
                compProsol.setType(EBISystem.gui().combo("prosolTypeText", "Prosol").getSelectedItem().toString());
            }

            if (EBISystem.gui().combo("prosolClassificationText", "Prosol").getSelectedItem() != null) {
                compProsol.setClassification(EBISystem.gui().combo("prosolClassificationText", "Prosol").getSelectedItem().toString());
            }

            EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(compProsol);

            //Save docs
            if (!this.compProsol.getCrmproblemsoldocses().isEmpty()) {
                final Iterator iter = this.compProsol.getCrmproblemsoldocses().iterator();
                while (iter.hasNext()) {
                    final Crmproblemsoldocs docs = (Crmproblemsoldocs) iter.next();
                    if(docs.getSolutiondocid() != null && docs.getSolutiondocid() < 0){
                        docs.setSolutiondocid(null);
                    }
                    docs.setCrmproblemsolutions(compProsol);
                    EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(docs);
                }
            }

            //Save positions
            if (!this.compProsol.getCrmproblemsolpositions().isEmpty()) {
                final Iterator iter = this.compProsol.getCrmproblemsolpositions().iterator();
                while (iter.hasNext()) {
                    final Crmproblemsolposition pos = (Crmproblemsolposition) iter.next();
                    if(pos.getPositionid() != null && pos.getPositionid() < 0){
                        pos.setPositionid(null);
                    }
                    pos.setCrmproblemsolutions(compProsol);
                    EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(pos);
                }
            }

            EBISystem.getInstance().getDataStore("Prosol", "ebiSave");
            EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
            if (!isEdit) {
                EBISystem.gui().vpanel("Prosol").setID(compProsol.getProsolid());
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void dataCopy(final int id) {

        Query query;
        try {

            EBISystem.hibernate().transaction("PROSOL_SESSION").begin();

            query = EBISystem.hibernate().session("PROSOL_SESSION").createQuery(
                    "from Crmproblemsolutions where prosolid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                final Crmproblemsolutions csol = (Crmproblemsolutions) iter.next();

                final Crmproblemsolutions psoln = new Crmproblemsolutions();
                psoln.setCreateddate(new Date());
                psoln.setCreatedfrom(EBISystem.ebiUser);
                psoln.setDescription(csol.getDescription());
                psoln.setServicenr(csol.getServicenr());
                psoln.setName(csol.getName() + " - (Copy)");
                psoln.setStatus(csol.getStatus());
                psoln.setCategory(csol.getCategory());
                psoln.setType(csol.getType());
                psoln.setClassification(csol.getClassification());

                EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(psoln);
                //Save docs
                if (!csol.getCrmproblemsoldocses().isEmpty()) {
                    final Iterator itd = csol.getCrmproblemsoldocses().iterator();
                    while (itd.hasNext()) {
                        final Crmproblemsoldocs docs = (Crmproblemsoldocs) itd.next();

                        final Crmproblemsoldocs dc = new Crmproblemsoldocs();
                        dc.setCrmproblemsolutions(psoln);
                        dc.setCreateddate(new Date());
                        dc.setCreatedfrom(EBISystem.ebiUser);
                        dc.setFiles(docs.getFiles());
                        dc.setName(docs.getName());
                        EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(dc);
                    }
                }
                //Save positions
                if (!csol.getCrmproblemsolpositions().isEmpty()) {
                    final Iterator itp = csol.getCrmproblemsolpositions().iterator();
                    while (itp.hasNext()) {
                        final Crmproblemsolposition pos = (Crmproblemsolposition) itp.next();

                        final Crmproblemsolposition p = new Crmproblemsolposition();
                        p.setCrmproblemsolutions(psoln);
                        p.setCategory(pos.getCategory());
                        p.setCreateddate(new Date());
                        p.setCreatedfrom(EBISystem.ebiUser);
                        p.setDescription(pos.getDescription());
                        p.setNetamount(pos.getNetamount());
                        p.setPretax(pos.getPretax());
                        p.setProductname(pos.getProductname());
                        p.setProductnr(pos.getProductnr());
                        p.setTaxtype(pos.getTaxtype());
                        p.setType(pos.getType());
                        EBISystem.hibernate().session("PROSOL_SESSION").saveOrUpdate(p);
                    }
                }

                EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
                EBISystem.gui().table("prosolTable", "Prosol").
                        changeSelection(EBISystem.gui().table("prosolTable", "Prosol").
                                convertRowIndexToView(EBISystem.getCRMModule().dynMethod.
                                        getIdIndexFormArrayInATable(EBISystem.getCRMModule().getProsolPane().tabModProsol.data, 7, psoln.getProsolid())), 0, false, false);

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataEdit(final int id) {

        Query query;
        try {
            EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
            query = EBISystem.hibernate().session("PROSOL_SESSION").createQuery(
                    "from Crmproblemsolutions where prosolid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                this.compProsol = (Crmproblemsolutions) iter.next();
                EBISystem.hibernate().session("PROSOL_SESSION").refresh(this.compProsol);
                EBISystem.gui().vpanel("Prosol").setID(compProsol.getProsolid());
                EBISystem.gui().vpanel("Prosol").setCreatedDate(EBISystem.getInstance().getDateToString(compProsol.getCreateddate() == null ? new Date() : compProsol.getCreateddate()));
                EBISystem.gui().vpanel("Prosol").setCreatedFrom(compProsol.getCreatedfrom() == null ? EBISystem.ebiUser : compProsol.getCreatedfrom());

                if (compProsol.getChangeddate() != null) {
                    EBISystem.gui().vpanel("Prosol").setChangedDate(EBISystem.getInstance().getDateToString(compProsol.getChangeddate()));
                    EBISystem.gui().vpanel("Prosol").setChangedFrom(compProsol.getChangedfrom());
                } else {
                    EBISystem.gui().vpanel("Prosol").setChangedDate("");
                    EBISystem.gui().vpanel("Prosol").setChangedFrom("");
                }

                EBISystem.gui().textField("prosolNameText", "Prosol").setText(compProsol.getName());
                EBISystem.gui().textField("prosolNrText", "Prosol").setText(compProsol.getServicenr() == null ? "" : compProsol.getServicenr());

                if (compProsol.getStatus() != null) {
                    EBISystem.gui().combo("prosolStatusText", "Prosol").setSelectedItem(compProsol.getStatus());
                }

                if (compProsol.getCategory() != null) {
                    EBISystem.gui().combo("prosolCategoryText", "Prosol").setSelectedItem(compProsol.getCategory());
                }

                if (compProsol.getType() != null) {
                    EBISystem.gui().combo("prosolTypeText", "Prosol").setSelectedItem(compProsol.getType());
                }

                if (compProsol.getClassification() != null) {
                    EBISystem.gui().combo("prosolClassificationText", "Prosol").setSelectedItem(compProsol.getClassification());
                }

                EBISystem.gui().textArea("prosolDescriptionText", "Prosol").setText(compProsol.getDescription());

                EBISystem.getInstance().getDataStore("Prosol", "ebiEdit");

                EBISystem.gui().table("prosolTable", "Prosol").
                        changeSelection(EBISystem.gui().table("prosolTable", "Prosol").
                                convertRowIndexToView(EBISystem.getCRMModule().dynMethod.
                                        getIdIndexFormArrayInATable(EBISystem.getCRMModule().getProsolPane().tabModProsol.data, 7, id)), 0, false, false);

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }
            EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataDelete(final int id) {
        Query query;
        try {
            query = EBISystem.hibernate().session("PROSOL_SESSION").createQuery(
                    "from Crmproblemsolutions where prosolid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            while (iter.hasNext()) {
                compProsol = (Crmproblemsolutions) iter.next();
                if (compProsol.getProsolid() == id) {
                    EBISystem.getInstance().getDataStore("Prosol", "ebiDelete");
                    EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
                    EBISystem.hibernate().session("PROSOL_SESSION").delete(compProsol);
                    EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataShow() {
        ResultSet set = null;

        final int srow = EBISystem.gui().table("prosolTable", "Prosol").getSelectedRow();
        PreparedStatement ps1 = null;

        try {

            ps1 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT PROSOLID,SERVICENR,NAME,CLASSIFICATION,CATEGORY,TYPE,STATUS,DESCRIPTION FROM CRMPROBLEMSOLUTIONS ORDER BY CREATEDDATE DESC");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            if (set != null) {
                set.last();
                EBISystem.getCRMModule().getProsolPane().tabModProsol.data = new Object[set.getRow()][8];

                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {

                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][0] = set.getString("SERVICENR") == null ? "" : set.getString("SERVICENR");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][1] = set.getString("NAME") == null ? "" : set.getString("NAME");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][2] = set.getString("CLASSIFICATION") == null ? "" : set.getString("CLASSIFICATION");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][3] = set.getString("STATUS") == null ? "" : set.getString("STATUS");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][4] = set.getString("CATEGORY") == null ? "" : set.getString("CATEGORY");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][5] = set.getString("TYPE") == null ? "" : set.getString("TYPE");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][6] = set.getString("DESCRIPTION") == null ? "" : set.getString("DESCRIPTION");
                        EBISystem.getCRMModule().getProsolPane().tabModProsol.data[i][7] = set.getInt("PROSOLID");
                        i++;
                    }
                } else {
                    EBISystem.getCRMModule().getProsolPane().tabModProsol.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
                }
            } else {
                EBISystem.getCRMModule().getProsolPane().tabModProsol.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                    ps1.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
            EBISystem.getCRMModule().getProsolPane().tabModProsol.fireTableDataChanged();
        }
        EBISystem.gui().table("prosolTable", "Prosol").changeSelection(srow, 0, false, false);
    }

    public void dataShowReport(final int id) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);
        EBISystem.getInstance().getIEBIReportSystemInstance().
                useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_PROSOL")),
                        getprosolNamefromId(id));
    }

    public void dataNew() {
        compProsol = new Crmproblemsolutions();
        EBISystem.gui().vpanel("Prosol").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.gui().vpanel("Prosol").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Prosol").setChangedDate("");
        EBISystem.gui().vpanel("Prosol").setChangedFrom("");

        //todo use initialize

        EBISystem.gui().textField("prosolNameText", "Prosol").setText("");
        EBISystem.gui().combo("prosolStatusText", "Prosol").setSelectedIndex(0);
        EBISystem.gui().combo("prosolCategoryText", "Prosol").setSelectedIndex(0);
        EBISystem.gui().combo("prosolTypeText", "Prosol").setSelectedIndex(0);
        EBISystem.gui().combo("prosolClassificationText", "Prosol").setSelectedIndex(0);
        EBISystem.gui().textArea("prosolDescriptionText", "Prosol").setText("");
        EBISystem.gui().textField("prosolNrText", "Prosol").setText("");
        EBISystem.gui().vpanel("Prosol").setID(-1);
        EBISystem.getInstance().getDataStore("Prosol", "ebiNew");
    }

    private void createHistory() {
        final EBICRMHistoryCreator hcreator = new EBICRMHistoryCreator(EBISystem.getCRMModule());
        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(compProsol.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + compProsol.getCreatedfrom());

        if (compProsol.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(compProsol.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + compProsol.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_PROSOL_NUMBER") + ": " + (compProsol.getServicenr().equals(EBISystem.gui().textField("prosolNrText", "Prosol").getText()) == true ? compProsol.getServicenr() : compProsol.getServicenr() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + (compProsol.getName().equals(EBISystem.gui().textField("prosolNameText", "Prosol").getText()) == true ? compProsol.getName() : compProsol.getName() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_C_STATUS") + ": " + (compProsol.getStatus().equals(EBISystem.gui().combo("prosolStatusText", "Prosol").getSelectedItem().toString()) == true ? compProsol.getStatus() : compProsol.getStatus() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": " + (compProsol.getCategory().equals(EBISystem.gui().combo("prosolCategoryText", "Prosol").getSelectedItem().toString()) == true ? compProsol.getCategory() : compProsol.getCategory() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_TYPE") + ": " + (compProsol.getType().equals(EBISystem.gui().combo("prosolTypeText", "Prosol").getSelectedItem().toString()) == true ? compProsol.getType() : compProsol.getType() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_CLASSIFICATION") + ": " + (compProsol.getClassification().equals(EBISystem.gui().combo("prosolClassificationText", "Prosol").getSelectedItem().toString()) == true ? compProsol.getClassification() : compProsol.getClassification() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + (compProsol.getDescription().equals(EBISystem.gui().textArea("prosolDescriptionText", "Prosol").getText()) == true ? compProsol.getDescription() : compProsol.getDescription() + "$"));
        list.add("*EOR*"); // END OF RECORD


        if (!compProsol.getCrmproblemsoldocses().isEmpty()) {

            final Iterator iter = compProsol.getCrmproblemsoldocses().iterator();
            while (iter.hasNext()) {
                final Crmproblemsoldocs obj = (Crmproblemsoldocs) iter.next();
                list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_FILENAME") + ": " : EBISystem.i18n("EBI_LANG_FILENAME") + ": " + obj.getName());
                list.add(EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " : EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " + EBISystem.getInstance().getDateToString(obj.getCreateddate()));
                list.add(obj.getCreatedfrom() == null ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                list.add("*EOR*");
            }
        }

        if (!compProsol.getCrmproblemsolpositions().isEmpty()) {

            final Iterator iter = compProsol.getCrmproblemsolpositions().iterator();

            while (iter.hasNext()) {
                final Crmproblemsolposition obj = (Crmproblemsolposition) iter.next();
                list.add(EBISystem.i18n("EBI_LANG_PRODUCT_NUMBER") + ": " + obj.getProductnr());
                list.add(obj.getProductname() == null ? EBISystem.i18n("EBI_LANG_NAME") + ":" : EBISystem.i18n("EBI_LANG_NAME") + ": " + obj.getProductname());
                list.add(obj.getCategory() == null ? EBISystem.i18n("EBI_LANG_CATEGORY") + ":" : EBISystem.i18n("EBI_LANG_CATEGORY") + ": " + obj.getCategory());
                list.add(obj.getTaxtype() == null ? EBISystem.i18n("EBI_LANG_TAX") + ":" : EBISystem.i18n("EBI_LANG_TAX") + ": " + obj.getTaxtype());
                list.add(String.valueOf(obj.getPretax()) == null ? EBISystem.i18n("EBI_LANG_PRICE") + ":" : EBISystem.i18n("EBI_LANG_PRICE") + ": " + String.valueOf(obj.getPretax()));
                list.add(obj.getDescription() == null ? EBISystem.i18n("EBI_LANG_DESCRIPTION") + ":" : EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + obj.getDescription());
                list.add("*EOR*");

            }
        }

        try {
            hcreator.setDataToCreate(new EBICRMHistoryDataUtil(compProsol.getProsolid(), "Prosol", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Crmproblemsoldocs docs = new Crmproblemsoldocs();
                docs.setSolutiondocid((compProsol.getCrmproblemsoldocses().size() +1) * -1);
                docs.setCrmproblemsolutions(compProsol);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);

                compProsol.getCrmproblemsoldocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void dataViewDoc(final int id) {

        String FileName;
        String FileType;
        OutputStream fos;
        try {
            final Iterator iter = this.compProsol.getCrmproblemsoldocses().iterator();
            while (iter.hasNext()) {

                final Crmproblemsoldocs doc = (Crmproblemsoldocs) iter.next();

                if (id == doc.getSolutiondocid()) {
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
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        } catch (final IOException exx1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataShowDoc() {
        if (this.compProsol.getCrmproblemsoldocses().size() > 0) {
            EBISystem.getCRMModule().getProsolPane().tabModDoc.data = new Object[this.compProsol.getCrmproblemsoldocses().size()][4];

            final Iterator itr = this.compProsol.getCrmproblemsoldocses().iterator();
            int i = 0;
            while (itr.hasNext()) {

                final Crmproblemsoldocs obj = (Crmproblemsoldocs) itr.next();
                EBISystem.getCRMModule().getProsolPane().tabModDoc.data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getCRMModule().getProsolPane().tabModDoc.data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getCRMModule().getProsolPane().tabModDoc.data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getCRMModule().getProsolPane().tabModDoc.data[i][3] = obj.getSolutiondocid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getProsolPane().tabModDoc.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getCRMModule().getProsolPane().tabModDoc.fireTableDataChanged();
    }

    public void dataShowProduct() {
        if (this.compProsol.getCrmproblemsolpositions().size() > 0) {
            EBISystem.getCRMModule().getProsolPane().tabModProduct.data = new Object[this.compProsol.getCrmproblemsolpositions().size()][7];

            final Iterator itr = this.compProsol.getCrmproblemsolpositions().iterator();
            int i = 0;

            final NumberFormat currency = NumberFormat.getCurrencyInstance();

            while (itr.hasNext()) {

                final Crmproblemsolposition obj = (Crmproblemsolposition) itr.next();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][0] = obj.getProductnr();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][1] = obj.getProductname() == null ? "" : obj.getProductname();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][2] = obj.getCategory() == null ? "" : obj.getCategory();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][3] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][4] = currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(1), String.valueOf(0))) == null ? "" : currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), "1", String.valueOf(0)));
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][5] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getCRMModule().getProsolPane().tabModProduct.data[i][6] = obj.getProductid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getProsolPane().tabModProduct.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
        }
        EBISystem.getCRMModule().getProsolPane().tabModProduct.fireTableDataChanged();
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.compProsol.getCrmproblemsoldocses().iterator();
        while (iter.hasNext()) {
            final Crmproblemsoldocs doc = (Crmproblemsoldocs) iter.next();
            if (doc.getSolutiondocid() == id) {
                this.compProsol.getCrmproblemsoldocses().remove(doc);
                EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
                EBISystem.hibernate().session("PROSOL_SESSION").delete(doc);
                EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
                break;
            }
        }
    }

    public void dataDeleteProduct(final int id) {
        final Iterator iter = this.compProsol.getCrmproblemsolpositions().iterator();
        while (iter.hasNext()) {
            final Crmproblemsolposition prosolpro = (Crmproblemsolposition) iter.next();
            if (prosolpro.getProductid() == id) {
                this.compProsol.getCrmproblemsolpositions().remove(prosolpro);
                EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
                EBISystem.hibernate().session("PROSOL_SESSION").delete(prosolpro);
                EBISystem.hibernate().transaction("PROSOL_SESSION").commit();
                break;
            }
        }
    }

    public Crmproblemsolutions getcompProsol() {
        return this.compProsol;
    }

    public void setcompProsol(final Crmproblemsolutions compProsol) {
        this.compProsol = compProsol;
    }

    public Set<Crmproblemsoldocs> getprosolDocList() {
        return this.compProsol.getCrmproblemsoldocses();
    }

    public Set<Crmproblemsolposition> getprosolPosList() {
        return this.compProsol.getCrmproblemsolpositions();
    }

    private String getprosolNamefromId(final int id) {
        String name = "";
        try {

            EBISystem.hibernate().transaction("PROSOL_SESSION").begin();
            final Query query = EBISystem.hibernate().session("PROSOL_SESSION").createQuery(
                    "from Crmproblemsolutions where prosolid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            while (iter.hasNext()) {
                final Crmproblemsolutions prosol = (Crmproblemsolutions) iter.next();
                EBISystem.hibernate().session("PROSOL_SESSION").refresh(prosol);
                if (prosol.getProsolid() == id) {
                    name = prosol.getName();
                    break;
                }
            }
            EBISystem.hibernate().transaction("PROSOL_SESSION").commit();

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return name;
    }
}
