package org.modules.controls;

import org.sdk.model.hibernate.Crmproductdimension;
import org.sdk.model.hibernate.Crmproduct;
import org.sdk.model.hibernate.Crmproductdocs;
import org.sdk.model.hibernate.Companyproducttax;
import org.sdk.model.hibernate.Crmproductdependency;
import org.modules.views.dialogs.EBICRMDialogSearchProduct;
import org.modules.views.dialogs.EBIDialogProperties;
import org.modules.utils.EBICRMHistoryCreator;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class ControlProduct {

    public Crmproduct product = null;
    private Crmproductdependency dependency = null;
    private Crmproductdimension dimension = null;
    public boolean isEdit = false;

    public ControlProduct() {
        product = new Crmproduct();
        dimension = new Crmproductdimension();
        dependency = new Crmproductdependency();
        EBISystem.hibernate().openHibernateSession("EBIPRODUCT_SESSION");
    }

    public Integer dataStore() {

        Integer productID=-1;
        
        try {
            EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();
            if (!isEdit) {
                product.setCreateddate(new Date());
                product.setCreatedfrom(EBISystem.ebiUser);
            } else {
                createHistory(product);
                product.setChangeddate(new Date());
                product.setChangedfrom(EBISystem.ebiUser);
            }

            product.setProductnr(EBISystem.gui().textField("ProductNrTex", "Product").getText());
            product.setProductname(EBISystem.gui().textField("ProductNameText", "Product").getText());

            if (EBISystem.gui().combo("ProductCategoryText", "Product").getSelectedItem() != null) {
                product.setCategory(EBISystem.gui().combo("ProductCategoryText", "Product").getSelectedItem().toString());
            }

            if (EBISystem.gui().combo("ProductTypeText", "Product").getSelectedItem() != null) {
                product.setType(EBISystem.gui().combo("ProductTypeText", "Product").getSelectedItem().toString());
            }

            if (EBISystem.gui().combo("productTaxTypeTex", "Product").getSelectedItem() != null) {
                product.setTaxtype(EBISystem.gui().combo("productTaxTypeTex", "Product").getSelectedItem().toString());
            }

            product.setPretax(Double.parseDouble(EBISystem.gui().FormattedField("productGrossText", "Product").getValue() == null ? "0.0" : EBISystem.gui().FormattedField("productGrossText", "Product").getValue().toString()));
            product.setNetamount(Double.parseDouble(EBISystem.gui().FormattedField("productNetamoutText", "Product").getValue() == null ? "0.0" : EBISystem.gui().FormattedField("productNetamoutText", "Product").getValue().toString()));
            product.setSaleprice(Double.parseDouble(EBISystem.gui().FormattedField("salePriceText", "Product").getValue() == null ? "0.0" : EBISystem.gui().FormattedField("salePriceText", "Product").getValue().toString()));

            product.setDescription(EBISystem.gui().textArea("productDescription", "Product").getText());

            EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(product);

            if (!product.getCrmproductdocses().isEmpty()) {
                final Iterator iter = product.getCrmproductdocses().iterator();
                while (iter.hasNext()) {
                    final Crmproductdocs docs = (Crmproductdocs) iter.next();
                    if (docs.getProductdocid() != null & docs.getProductdocid() < 0) {
                        docs.setProductdocid(null);
                    }
                    docs.setCrmproduct(product);
                    EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(docs);
                }
            }

            if (!product.getCrmproductdimensions().isEmpty()) {
                final Iterator itdim = product.getCrmproductdimensions().iterator();
                while (itdim.hasNext()) {
                    final Crmproductdimension dimx = (Crmproductdimension) itdim.next();
                    if (dimx.getDimensionid() != null && dimx.getDimensionid() < 0) {
                        dimx.setDimensionid(null);
                    }
                    dimx.setCrmproduct(product);
                    EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(dimx);
                }
            }

            //show first pic to image view
            if (!product.getCrmproductdocses().isEmpty()) {
                final Iterator iter = product.getCrmproductdocses().iterator();
                while (iter.hasNext()) {
                    final Crmproductdocs docs = (Crmproductdocs) iter.next();
                    if (loadIfImage(docs)) {
                        break;
                    }
                }
            }

            if (!product.getCrmproductdependencies().isEmpty()) {
                final Iterator itdip = product.getCrmproductdependencies().iterator();
                while (itdip.hasNext()) {
                    final Crmproductdependency cdip = (Crmproductdependency) itdip.next();
                    if (cdip.getDependencyid() != null && cdip.getDependencyid() < 0) {
                        cdip.setDependencyid(null);
                    }
                    cdip.setCrmproduct(product);
                    EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(cdip);
                }
            }

            EBISystem.getInstance().getDataStore("Product", "ebiSave");
            EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();

            if (!isEdit) {
                EBISystem.gui().vpanel("Product").setID(product.getProductid());
            }
            productID = product.getProductid();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return productID;
    }

    public Integer dataCopy(final int id) {
        Integer productID=-1;
        Query query;
        try {

            EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();

            query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();

            if (iter.hasNext()) {

                final Crmproduct pro = (Crmproduct) iter.next();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").refresh(pro);

                final Crmproduct pnew = new Crmproduct();
                pnew.setCreateddate(new Date());
                pnew.setCreatedfrom(EBISystem.ebiUser);
                pnew.setProductnr(pro.getProductnr());
                pnew.setProductname(pro.getProductname() + " - (Copy)");
                pnew.setCategory(pro.getCategory());
                pnew.setType(pro.getType());
                pnew.setTaxtype(pro.getTaxtype());

                pnew.setPretax(pro.getPretax());
                pnew.setNetamount(pro.getNetamount());
                pnew.setSaleprice(pro.getSaleprice());

                pnew.setDescription(pro.getDescription());
                pnew.setPicture(pro.getPicture());
                pnew.setPicturename(pro.getPicturename());

                EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(pnew);

                if (!pro.getCrmproductdocses().isEmpty()) {
                    final Iterator itdoc = pro.getCrmproductdocses().iterator();
                    while (itdoc.hasNext()) {
                        final Crmproductdocs docx = (Crmproductdocs) itdoc.next();

                        final Crmproductdocs dc = new Crmproductdocs();
                        dc.setCrmproduct(pnew);
                        dc.setCreateddate(new Date());
                        dc.setCreatedfrom(EBISystem.ebiUser);
                        dc.setFiles(docx.getFiles());
                        dc.setName(docx.getName());
                        pnew.getCrmproductdocses().add(dc);
                        EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(dc);
                    }
                }

                if (!pro.getCrmproductdimensions().isEmpty()) {
                    final Iterator itdim = pro.getCrmproductdimensions().iterator();
                    while (itdim.hasNext()) {
                        final Crmproductdimension dimx = (Crmproductdimension) itdim.next();

                        final Crmproductdimension nd = new Crmproductdimension();
                        nd.setCrmproduct(pnew);
                        nd.setCreateddate(new Date());
                        nd.setCreatedfrom(EBISystem.ebiUser);
                        nd.setName(dimx.getName());
                        nd.setValue(dimx.getValue());
                        pnew.getCrmproductdimensions().add(nd);
                        EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(nd);
                    }
                }

                if (!pro.getCrmproductdependencies().isEmpty()) {
                    final Iterator itdip = pro.getCrmproductdependencies().iterator();
                    while (itdip.hasNext()) {
                        final Crmproductdependency cdip = (Crmproductdependency) itdip.next();

                        final Crmproductdependency d = new Crmproductdependency();
                        d.setCrmproduct(pnew);
                        d.setCreateddate(new Date());
                        d.setCreatedfrom(EBISystem.ebiUser);
                        d.setProductname(cdip.getProductname());
                        d.setProductnr(cdip.getProductnr());
                        pnew.getCrmproductdependencies().add(d);
                        EBISystem.hibernate().session("EBIPRODUCT_SESSION").saveOrUpdate(d);
                    }
                }

                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();
                productID = pnew.getProductid();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return productID;
    }

    public void dataEdit(final int id) {
        Query query;
        try {

            EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();

            query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();

            if (iter.hasNext()) {
                this.product = (Crmproduct) iter.next();
                EBISystem.gui().vpanel("Product").setID(product.getProductid());
                EBISystem.gui().vpanel("Product").setCreatedDate(EBISystem.getInstance().getDateToString(product.getCreateddate() == null ? new Date() : product.getCreateddate()));
                EBISystem.gui().vpanel("Product").setCreatedFrom(product.getCreatedfrom() == null ? EBISystem.ebiUser : product.getCreatedfrom());

                if (product.getChangeddate() != null) {
                    EBISystem.gui().vpanel("Product").setChangedDate(EBISystem.getInstance().getDateToString(product.getChangeddate()));
                    EBISystem.gui().vpanel("Product").setChangedFrom(product.getChangedfrom());
                }
                EBISystem.gui().textField("ProductNrTex", "Product").setText(product.getProductnr());
                EBISystem.gui().textField("ProductNameText", "Product").setText(product.getProductname());

                if (product.getCategory() != null) {
                    EBISystem.gui().combo("ProductCategoryText", "Product").setSelectedItem(product.getCategory());
                }

                if (product.getCategory() != null) {
                    EBISystem.gui().combo("ProductCategoryText", "Product").setSelectedItem(product.getCategory());
                }

                if (product.getType() != null) {
                    EBISystem.gui().combo("ProductTypeText", "Product").setSelectedItem(product.getType());
                }

                if (product.getTaxtype() != null) {
                    EBISystem.gui().combo("productTaxTypeTex", "Product").setSelectedItem(product.getTaxtype());
                }

                EBISystem.gui().FormattedField("productGrossText", "Product").setValue(product.getPretax() == null ? 0 : product.getPretax());
                EBISystem.gui().FormattedField("productNetamoutText", "Product").setValue(product.getNetamount() == null ? 0 : product.getNetamount());
                EBISystem.gui().FormattedField("salePriceText", "Product").setValue(product.getSaleprice() == null ? 0 : product.getSaleprice());
                EBISystem.gui().textArea("productDescription", "Product").setText(product.getDescription() == null ? "" : product.getDescription());

                EBISystem.getInstance().getDataStore("Product", "ebiEdit");
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public void dataDelete(final int id) {
        try {
            final Query query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1").setParameter(1, id);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Crmproduct prd = (Crmproduct) iter.next();
                EBISystem.getInstance().getDataStore("Product", "ebiDelete");
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").delete(prd);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dataShow(Integer id) {
        ResultSet set = null;
        int selRow = EBISystem.gui().table("companyProductTable", "Product").getSelectedRow() + id;
        PreparedStatement ps = null;
        
      
        try {
            ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT PRODUCTID,PRODUCTNR,PRODUCTNAME,CATEGORY,TYPE,DESCRIPTION FROM CRMPRODUCT ORDER BY CREATEDDATE DESC");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            if (set != null) {
                set.last();
                EBISystem.getModule().getEBICRMProductPane().getProductModel().data = new Object[set.getRow()][6];
                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][0] = set.getString("PRODUCTNR") == null ? "" : set.getString("PRODUCTNR");
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][1] = set.getString("PRODUCTNAME") == null ? "" : set.getString("PRODUCTNAME");
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][2] = set.getString("CATEGORY") == null ? "" : set.getString("CATEGORY");
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][3] = set.getString("TYPE") == null ? "" : set.getString("TYPE");
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][4] = set.getString("DESCRIPTION") == null ? "" : set.getString("DESCRIPTION");
                        EBISystem.getModule().getEBICRMProductPane().getProductModel().data[i][5] = set.getInt("PRODUCTID");
                        if(id != -1 && id == set.getInt("PRODUCTID")){
                           selRow = i;
                        }
                        i++;
                    }
                } else {
                    EBISystem.getModule().getEBICRMProductPane().getProductModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
                }
            } else {
                EBISystem.getModule().getEBICRMProductPane().getProductModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                    ps.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
            EBISystem.getModule().getEBICRMProductPane().getProductModel().fireTableDataChanged();
        }
        
        if(selRow > -1){
            selRow= EBISystem.gui().table("companyProductTable", "Product").convertRowIndexToView(selRow);
            EBISystem.gui().table("companyProductTable", "Product").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataShowReport(final int id) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);
        EBISystem.getInstance().getIEBIReportSystemInstance().
                useReportSystem(map,
                        EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_C_PRODUCT")),
                        getProductNamefromId(id));
    }

    public void dataNew() {
        product = new Crmproduct();
        EBISystem.getModule().getEBICRMProductPane().initialize(false);
        EBISystem.getInstance().getDataStore("Product", "ebiNew");
    }

    private void createHistory(final Crmproduct pr) {

        final EBICRMHistoryCreator hcreator = new EBICRMHistoryCreator(EBISystem.getModule());
        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.getInstance().getDateToString(pr.getCreateddate()));
        list.add(pr.getCreatedfrom());

        if (pr.getChangeddate() != null) {
            list.add(EBISystem.getInstance().getDateToString(pr.getChangeddate()));
            list.add(pr.getChangedfrom());
        }
        if (pr.getProductnr() != null) {
            list.add(EBISystem.i18n("EBI_LANG_PRODUCT_NUMBER") + ": " + (pr.getProductnr().equals(EBISystem.gui().textField("ProductNrTex", "Product").getText()) == true ? pr.getProductnr() : pr.getProductnr() + "$"));
        }
        if (pr.getProductname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_NAME") + ": " + (pr.getProductname().equals(EBISystem.gui().textField("ProductNameText", "Product").getText()) == true ? pr.getProductname() : pr.getProductname() + "$"));
        }
        if (pr.getCategory() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": " + (pr.getCategory().equals(EBISystem.gui().combo("ProductCategoryText", "Product").getSelectedItem().toString()) == true ? pr.getCategory() : pr.getCategory() + "$"));
        }
        if (pr.getType() != null) {
            list.add(EBISystem.i18n("EBI_LANG_TYPE") + ": " + (pr.getType().equals(EBISystem.gui().combo("ProductTypeText", "Product").getSelectedItem().toString()) == true ? pr.getType() : pr.getType() + "$"));
        }
        if (pr.getTaxtype() != null) {
            list.add(EBISystem.i18n("EBI_LANG_TAX_TYPE") + ": " + (pr.getTaxtype().equals(EBISystem.gui().combo("productTaxTypeTex", "Product").getSelectedItem().toString()) == true ? pr.getTaxtype() : pr.getTaxtype() + "$"));
        }
        if (pr.getPretax() != null) {
            list.add(EBISystem.i18n("EBI_LANG_PRE_TAX_PRICE") + ": " + (String.valueOf(pr.getPretax()).equals(EBISystem.gui().FormattedField("productGrossText", "Product").getValue().toString()) == true ? String.valueOf(pr.getPretax()) : String.valueOf(pr.getPretax()) + "$"));
        }
        if (pr.getNetamount() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CLEAR_PRICE") + ": " + (String.valueOf(pr.getNetamount()).equals(EBISystem.gui().FormattedField("productNetamoutText", "Product").getValue().toString()) == true ? String.valueOf(pr.getNetamount()) : String.valueOf(pr.getNetamount()) + "$"));
        }
        if (pr.getDescription() != null) {
            list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + (pr.getDescription().equals(EBISystem.gui().textArea("productDescription", "Product").getText()) == true ? pr.getDescription() : pr.getDescription() + "$"));
        }
        list.add("*EOR*"); // END OF RECORD

        if (!pr.getCrmproductdependencies().isEmpty()) {
            final Iterator iter = pr.getCrmproductdependencies().iterator();

            while (iter.hasNext()) {
                final Crmproductdependency dep = (Crmproductdependency) iter.next();
                if (dep.getProductnr() != null) {
                    list.add(dep.getProductnr());
                }
                if (dep.getProductname() != null) {
                    list.add(dep.getProductname());
                }
                list.add("*EOR*"); // END OF RECORD
            }
        }

        if (!pr.getCrmproductdimensions().isEmpty()) {
            final Iterator iter = pr.getCrmproductdimensions().iterator();

            while (iter.hasNext()) {
                final Crmproductdimension dim = (Crmproductdimension) iter.next();
                if (dim.getName() != null) {
                    list.add(dim.getName());
                }
                if (dim.getValue() != null) {
                    list.add(dim.getValue());
                }
                list.add("*EOR*"); // END OF RECORD
            }
        }

        try {
            hcreator.setDataToCreate(new EBICRMHistoryDataUtil(pr.getProductid(), "Product", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Crmproductdocs docs = new Crmproductdocs();
                docs.setProductdocid((product.getCrmproductdocses().size() + 1) * -1);
                docs.setCrmproduct(product);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);
                product.getCrmproductdocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void loadDocsToImageView(final int id) {
        final Iterator iter = this.product.getCrmproductdocses().iterator();
        while (iter.hasNext()) {
            final Crmproductdocs doc = (Crmproductdocs) iter.next();
            if (id == doc.getProductdocid()) {
                loadIfImage(doc);
                break;
            }
        }
    }

    public boolean loadIfImage(final Crmproductdocs doc) {
        String FileName;
        String FileType;
        OutputStream fos;
        boolean ret = false;
        try {
            FileType = doc.getName().substring(doc.getName().lastIndexOf(".")).toLowerCase();
            if (".jpg".equals(FileType) || ".jpeg".equals(FileType) || ".gif".equals(FileType) || ".png".equals(FileType)) {
                // Get the BLOB inputstream
                final String file = doc.getName().replaceAll(" ", "_");
                final byte buffer[] = doc.getFiles();
                FileName = "tmp/" + file;
                fos = new FileOutputStream(FileName);
                fos.write(buffer, 0, buffer.length);
                fos.close();

                EBISystem.gui().label("productPictureLabel", "Product").setText("");

                final JLabel lbx = new JLabel(new ImageIcon(FileName)) {
                    @Override
                    public void paintComponent(final Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(((ImageIcon) getIcon()).getImage(), 0, 0, getWidth(), getHeight(), null);
                    }
                };
                EBISystem.gui().getPanel("picturePanel", "Product").setLayout(new BorderLayout());
                EBISystem.gui().getPanel("picturePanel", "Product").removeAll();
                EBISystem.gui().getPanel("picturePanel", "Product").add(lbx, BorderLayout.CENTER);
                EBISystem.gui().getPanel("picturePanel", "Product").updateUI();
                EBISystem.gui().button("productShowImage", "Product").setEnabled(true);
                ret = true;
            }
        } catch (final FileNotFoundException exx) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        } catch (final IOException exx1) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.INFO_MESSAGE);
        }

        return ret;
    }

    public void dataViewDoc(final int id) {

        String FileName;
        String FileType;
        OutputStream fos;
        try {

            final Iterator iter = this.product.getCrmproductdocses().iterator();
            while (iter.hasNext()) {

                final Crmproductdocs doc = (Crmproductdocs) iter.next();

                if (id == doc.getProductdocid()) {
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
        if (this.product.getCrmproductdocses().size() > 0) {
            EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data = new Object[this.product.getCrmproductdocses().size()][4];

            final Iterator itr = this.product.getCrmproductdocses().iterator();
            int i = 0;
            while (itr.hasNext()) {

                final Crmproductdocs obj = (Crmproductdocs) itr.next();
                EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data[i][3] = obj.getProductdocid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMProductPane().getTabModDoc().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getEBICRMProductPane().getTabModDoc().fireTableDataChanged();
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.product.getCrmproductdocses().iterator();
        while (iter.hasNext()) {
            final Crmproductdocs doc = (Crmproductdocs) iter.next();
            if (doc.getProductdocid() == id) {
                this.product.getCrmproductdocses().remove(doc);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").delete(doc);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();
                break;
            }
        }
    }

    public void dataNewDependency() {
        final EBICRMDialogSearchProduct prod = new EBICRMDialogSearchProduct(product.getCrmproductdependencies());
        prod.setVisible();
    }

    public void dataDeleteDependency(final int id) {
        final Iterator iter = product.getCrmproductdependencies().iterator();
        while (iter.hasNext()) {

            this.dependency = (Crmproductdependency) iter.next();
            if (id == this.dependency.getDependencyid()) {
                this.product.getCrmproductdependencies().remove(this.dependency);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").delete(this.dependency);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();
                break;
            }
        }
    }

    public void dataShowDependency() {
        if (product.getCrmproductdependencies().size() > 0) {
            EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().data = new Object[product.getCrmproductdependencies().size()][3];
            final Iterator iter = product.getCrmproductdependencies().iterator();
            int i = 0;
            while (iter.hasNext()) {
                final Crmproductdependency dip = (Crmproductdependency) iter.next();
                EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().data[i][0] = dip.getProductnr();
                EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().data[i][1] = dip.getProductname();
                EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().data[i][2] = dip.getDependencyid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
        }
        EBISystem.getModule().getEBICRMProductPane().getProductDependencyModel().fireTableDataChanged();
    }

    public void dataNewDimension() {
        final EBIDialogProperties dim = new EBIDialogProperties(product, null);
        dim.setVisible();
    }

    public void dataEditDimension(final int id) {
        final Iterator iter = product.getCrmproductdimensions().iterator();
        while (iter.hasNext()) {
            this.dimension = (Crmproductdimension) iter.next();
            if (id == dimension.getDimensionid()) {
                final EBIDialogProperties dim = new EBIDialogProperties(product, dimension);
                dim.setVisible();
                break;
            }
        }
    }

    public void dataDeleteDimension(final int id) throws Exception {
        final Iterator iter = product.getCrmproductdimensions().iterator();
        while (iter.hasNext()) {
            this.dimension = (Crmproductdimension) iter.next();
            if (id == dimension.getDimensionid()) {
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").begin();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").delete(dimension);
                EBISystem.hibernate().transaction("EBIPRODUCT_SESSION").commit();
                this.product.getCrmproductdimensions().remove(dimension);
                break;
            }
        }
    }

    public void dataShowDimension() {
        if (this.product.getCrmproductdimensions().size() > 0) {
            EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().data = new Object[this.product.getCrmproductdimensions().size()][3];
            final Iterator iter = this.product.getCrmproductdimensions().iterator();
            int i = 0;
            while (iter.hasNext()) {
                final Crmproductdimension dim = (Crmproductdimension) iter.next();
                EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().data[i][0] = dim.getName();
                EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().data[i][1] = dim.getValue();
                EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().data[i][2] = dim.getDimensionid();
                i++;
            }
        } else {
            EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
        }

        EBISystem.getModule().getEBICRMProductPane().getProductModelDimension().fireTableDataChanged();
    }

    public void calculateClearPrice() {
        Query query;
        try {
            query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").
                    createQuery("from Companyproducttax where name=?1 ").setParameter(1,
                    EBISystem.gui().combo("productTaxTypeTex", "Product").getSelectedItem().toString());

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Companyproducttax tax = (Companyproducttax) it.next();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").refresh(tax);
                final double pre = Double.valueOf(EBISystem.gui().FormattedField("productGrossText", "Product").getValue().toString());
                final double mwst = (tax.getTaxvalue() / 100) + 1.0;
                final double clear = (pre / mwst);
                final BigDecimal bd = new BigDecimal(clear);
                final BigDecimal bd_round = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                EBISystem.gui().FormattedField("productNetamoutText", "Product").setValue(new Double(bd_round.doubleValue()));
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void calculatePreTaxPrice() {
        Query query;
        try {
            query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").
                    createQuery("from Companyproducttax where name=?1 ").setParameter(1,
                    EBISystem.gui().combo("productTaxTypeTex", "Product").getSelectedItem().toString());
            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Companyproducttax tax = (Companyproducttax) it.next();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").refresh(tax);
                final double clear = Double.valueOf(EBISystem.gui().FormattedField("productNetamoutText", "Product").getValue().toString());

                final double pre = (clear + ((clear * tax.getTaxvalue()) / 100));

                final BigDecimal bd = new BigDecimal(pre);
                final BigDecimal bd_round = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                EBISystem.gui().FormattedField("productGrossText", "Product").setValue(new Double(bd_round.doubleValue()));
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Crmproduct getProduct() {
        return this.product;
    }

    public void setProduct(final Crmproduct product) {
        this.product = product;
    }

    public Crmproductdimension getDimension() {
        return dimension;
    }

    public void setDimension(final Crmproductdimension dimension) {
        this.dimension = dimension;
    }

    private String getProductNamefromId(final int id) {

        String name = "";
        try {
            final Query query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            while (iter.hasNext()) {
                final Crmproduct pr = (Crmproduct) iter.next();
                EBISystem.hibernate().session("EBIPRODUCT_SESSION").refresh(pr);
                if (pr.getProductid() == id) {
                    name = pr.getProductname();
                    break;
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return name;
    }

    public boolean existProduct(final String ProductNr) {

        boolean exist = false;
        try {
            final Query query = EBISystem.hibernate().session("EBIPRODUCT_SESSION").createQuery(
                    "from Crmproduct where productnr=?1 ").setParameter(1, ProductNr);

            if (query.list().size() > 0) {
                exist = true;
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return exist;
    }

}
