package org.modules.views.dialogs;

import org.sdk.model.hibernate.Crminvoiceposition;
import org.sdk.model.hibernate.Crmproduct;
import org.sdk.model.hibernate.Crmproblemsolposition;
import org.sdk.model.hibernate.Companyorderpositions;
import org.sdk.model.hibernate.Companyservicepositions;
import org.sdk.model.hibernate.Companyofferpositions;
import org.sdk.model.hibernate.Crmproductdependency;
import org.modules.views.EBICRMProductView;
import org.modules.models.ModelCRMProductSearch;
import org.modules.utils.EBISearchTreeNodeProduct;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class EBICRMDialogSearchProduct {

    private ModelCRMProductSearch tabModel = null;
    private Set<Crmproductdependency> dipendency = null;
    private EBISearchTreeNodeProduct selectedNode = null;
    private boolean isDependency = false;
    private boolean isCRMOrder = false;
    private boolean isCRMOffer = false;
    private boolean isCampaign = false;
    private boolean isCRMService = false;
    private boolean isProsol = false;
    private boolean isInvoice = false;
    private Companyorderpositions orderPosition = null;
    private Companyofferpositions offerPosition = null;
    private Companyservicepositions servicePosition = null;
    private Crmproblemsolposition prosolPosition = null;
    private Crminvoiceposition invoicePosition = null;
    private EBICRMDialogAddProduct addProduct = null;


    public EBICRMDialogSearchProduct(final Set<Crmproductdependency> dipList) {
        dipendency = dipList;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
        isDependency = true;
    }

    public EBICRMDialogSearchProduct(final Companyorderpositions orposition, final EBICRMDialogAddProduct addPro) {
        orderPosition = orposition;
        isCRMOrder = true;
        this.addProduct = addPro;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
    }

    public EBICRMDialogSearchProduct(final Companyofferpositions ofposition, final EBICRMDialogAddProduct addPro) {
        offerPosition = ofposition;
        isCRMOffer = true;
        this.addProduct = addPro;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
    }

    public EBICRMDialogSearchProduct(final Companyservicepositions servicePosition, final EBICRMDialogAddProduct addPro) {
        this.servicePosition = servicePosition;
        isCRMService = true;
        this.addProduct = addPro;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
    }

    public EBICRMDialogSearchProduct(final Crmproblemsolposition prosolPosition, final EBICRMDialogAddProduct addPro) {
        this.prosolPosition = prosolPosition;
        isProsol = true;
        this.addProduct = addPro;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
    }

    public EBICRMDialogSearchProduct(final Crminvoiceposition invPos, final EBICRMDialogAddProduct addPro) {
        invoicePosition = invPos;
        isInvoice = true;
        this.addProduct = addPro;
        tabModel = new ModelCRMProductSearch();
        initialzeDialog();
    }

    private void initialzeDialog() {
        EBISystem.gui().loadGUI("CRMDialog/productSearchDialog.xml");
        try {
            EBISystem.hibernate().openHibernateSession("SEARCH_PRODUCT_SESSION");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void setVisible() {
        EBISystem.gui().dialog("searchProduct").setTitle(EBISystem.i18n("EBI_LANG_PRODUCT_SEARCH"));
        EBISystem.gui().vpanel("searchProduct").setModuleTitle(EBISystem.i18n("EBI_LANG_PRODUCT_SEARCH"));

        EBISystem.gui().label("category", "searchProduct").setText(EBISystem.i18n("EBI_LANG_CATEGORY"));
        EBISystem.gui().label("productName", "searchProduct").setText(EBISystem.i18n("EBI_LANG_NAME"));
        EBISystem.gui().label("productNr", "searchProduct").setText(EBISystem.i18n("EBI_LANG_PRODUCT_NUMBER"));

        EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        EBISystem.gui().textField("productNrText", "searchProduct").requestFocus();
        EBISystem.gui().textField("productNrText", "searchProduct").registerKeyboardAction(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent ev) {
                searchProduct();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.gui().textField("productNameText", "searchProduct").registerKeyboardAction(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent ev) {
                searchProduct();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        final TreeSelectionModel rowSM = EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").getTreeSelectionModel();
        rowSM.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
			public void valueChanged(final TreeSelectionEvent e) {
                try {
                    final TreeSelectionModel lsm = (TreeSelectionModel) e.getSource();

                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("applyButton", "searchProduct").setEnabled(false);
                    } else {

                        final javax.swing.tree.TreePath Node = lsm.getLeadSelectionPath();

                        selectedNode = (EBISearchTreeNodeProduct) Node.getLastPathComponent();

                        if(selectedNode != null && selectedNode.getProductID() != null) {
                            if (!selectedNode.getProductID().equals(
                                    EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                                EBISystem.gui().button("applyButton", "searchProduct").setEnabled(true);
                            }else{
                                EBISystem.gui().button("applyButton", "searchProduct").setEnabled(false);
                            }
                        }
                    }
                } catch (final java.lang.ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });

        EBISystem.gui().button("productSearchButton", "searchProduct").setText(EBISystem.i18n("EBI_LANG_SEARCH"));
        EBISystem.gui().button("productSearchButton", "searchProduct").addActionListener(new java.awt.event.ActionListener() {

            @Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
                searchProduct();
            }
        });

        EBISystem.gui().button("applyButton", "searchProduct").setText(EBISystem.i18n("EBI_LANG_APPLY"));
        EBISystem.gui().button("applyButton", "searchProduct").setEnabled(false);
        EBISystem.gui().button("applyButton", "searchProduct").addActionListener(new java.awt.event.ActionListener() {

            @Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
                applySearch();
            }
        });

        EBISystem.gui().button("cancelButton", "searchProduct").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.gui().button("cancelButton", "searchProduct").addActionListener(new java.awt.event.ActionListener() {

            @Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("searchProduct").setVisible(false);
            }
        });

        EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").setTreeTableModel(tabModel);
        EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
			public void keyPressed(final java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selectedNode != null) {
                        if (!selectedNode.getProductID().equals(
                                EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                            applySearch();

                        }
                    }
                }
            }
        });

        EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
			public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (selectedNode != null && selectedNode.getProductID() != null) {
                        if (!selectedNode.getProductID().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                            applySearch();
                        }
                    }
                }
            }
        });

        EBISystem.gui().combo("categoryText", "searchProduct").setModel(new DefaultComboBoxModel(EBICRMProductView.category));
        EBISystem.gui().combo("categoryText", "searchProduct").registerKeyboardAction(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent ev) {
                searchProduct();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.gui().showGUI();
    }

    private void searchProduct() {

        ResultSet set1 = null;
        ResultSet set = null;

        try {

            final EBISearchTreeNodeProduct root = new EBISearchTreeNodeProduct(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "");

            final StringBuffer strQuery = new StringBuffer();
            strQuery.append("SELECT * FROM CRMPRODUCT");

            boolean param1 = false;
            boolean param2 = false;
            boolean param3 = false;

            if (!"".equals(EBISystem.gui().textField("productNrText", "searchProduct").getText())) {
                param1 = true;
                strQuery.append(" WHERE PRODUCTNR LIKE ? ");
            }

            if (!"".equals(EBISystem.gui().textField("productNameText", "searchProduct").getText())) {
                param2 = true;
                if (param1 == false) {
                    strQuery.append(" WHERE PRODUCTNAME LIKE ? ");
                } else {
                    strQuery.append(" AND PRODUCTNAME LIKE ? ");
                }
            }

            if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                    equals(EBISystem.gui().combo("categoryText", "searchProduct")
                            .getSelectedItem().toString()) &&
                    !"".equals(EBISystem.gui().combo("categoryText", "searchProduct")
                                    .getSelectedItem().toString())) {

                param3 = true;
                if (param1 == false && param2 == false) {
                    strQuery.append(" WHERE CATEGORY LIKE ? ");
                } else {
                    strQuery.append(" AND CATEGORY LIKE ? ");
                }
            }

            if (!param1 && !param2 && !param3) {
                strQuery.append(" order by PRODUCTID desc limit 500");
            }

            final PreparedStatement pst = EBISystem.getInstance().iDB().initPreparedStatement(strQuery.toString());
            if (param1) {
                pst.setString(1, "%" + EBISystem.gui().textField("productNrText", "searchProduct").getText() + "%");
            }
            if (param2) {
                pst.setString(param1 ? 2 : 1, "%" + EBISystem.gui().textField("productNameText", "searchProduct").getText() + "%");
            }
            if (param3) {
                pst.setString((param1 && param2) ? 3 : (param1 || param2) ? 2 : 1,
                        "%" + EBISystem.gui().combo("categoryText", "searchProduct").getSelectedItem().toString() + "%");
            }

            set = EBISystem.getInstance().iDB().executePreparedQuery(pst);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {

                    final EBISearchTreeNodeProduct topLevel = new EBISearchTreeNodeProduct(
                            set.getString("PRODUCTID"),
                            set.getString("PRODUCTNR"),
                            set.getString("PRODUCTNAME"),
                            set.getString("CATEGORY"),
                            set.getString("TYPE"));

                    final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement("SELECT d.PRODUCTID as PID, d.PRODUCTIDID AS PPID,  c.PRODUCTID as PRID, d.PRODUCTNR as PRNR,d.PRODUCTNAME as PRNAME,c.CATEGORY as CAT, c.TYPE as TYPE " +
                            "FROM CRMPRODUCTDEPENDENCY d LEFT JOIN CRMPRODUCT c  ON c.PRODUCTID=d.PRODUCTID WHERE d.PRODUCTID=? ");
                    ps.setString(1, set.getString("PRODUCTID"));
                    set1 = EBISystem.getInstance().iDB().executePreparedQuery(ps);

                    set1.last();
                    if (set1.getRow() > 0) {
                        set1.beforeFirst();
                        while (set1.next()) {
                            // System.out.println(set1.getString("PID")+" "+set1.getString("PRNR")+" "+set1.getString("PRNAME"));
                            // CRMPRODUCTDEPENDENCY
                            final EBISearchTreeNodeProduct topLevel1 = new EBISearchTreeNodeProduct(
                                    set1.getString("PPID"),
                                    set1.getString("PRNR"),
                                    set1.getString("PRNAME"),
                                    set1.getString("CAT"),
                                    set1.getString("TYPE"));
                            topLevel.add(topLevel1);
                        }
                    }
                    ps.close();
                    root.add(topLevel);
                }

                this.tabModel = new ModelCRMProductSearch(root);
                EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").setTreeTableModel(tabModel);
                EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").updateUI();
                EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").changeSelection(0, 0, false, false);
                EBISystem.gui().getTreeTable("treetableProduct", "searchProduct").requestFocus();
                pst.close();
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PRODUCT_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) { e.printStackTrace(); }
            }
            if (set1 != null) {
                try {
                    set1.close();
                } catch (final SQLException e) { e.printStackTrace(); }
            }
        }
    }


    private void applySearch() {
        if (isDependency) {
            fillDipendencyList();
            EBISystem.getModule().getEBICRMProductPane().showDependency();
        } else if (isCRMOrder) {
            fillProductFromOrder();
            this.addProduct.fillHTMLForm();
        } else if (isCRMOffer) {
            fillProductFromOffer();
            this.addProduct.fillHTMLForm();
        } else if (isCRMService) {
            fillProductFromService();
            this.addProduct.fillHTMLForm();
        } else if (isProsol) {
            fillProductFromProsol();
            this.addProduct.fillHTMLForm();
        } else if (isInvoice) {
            fillProductFromInvoice();
            this.addProduct.fillHTMLForm();
        }
        if (EBISystem.gui().button("applyButton", "productInsertDialog") != null) {
            EBISystem.gui().button("applyButton", "productInsertDialog").setEnabled(true);

        }
        if (this.addProduct != null) {
            this.addProduct.canSave = true;
        }
        EBISystem.gui().dialog("searchProduct").setVisible(false);
    }


    private void fillDipendencyList() {

        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                final Crmproductdependency dep = new Crmproductdependency();
                dep.setDependencyid((EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().getProduct().getCrmproductdependencies().size() +1) * -1);
                dep.setProductidid(Integer.parseInt(selectedNode.getProductID()));
                dep.setCreateddate(new Date());
                dep.setCreatedfrom(EBISystem.ebiUser);
                dep.setProductnr(product.getProductnr());
                dep.setProductname(product.getProductname());
                dipendency.add(dep);
                EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().getProduct().getCrmproductdependencies().add(dep);
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProductFromInvoice() {
        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));


            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                invoicePosition.setProductid(product.getProductid());
                invoicePosition.setCreateddate(new java.util.Date());
                invoicePosition.setCreatedfrom(EBISystem.ebiUser);
                if (product.getProductnr() != null) {
                    invoicePosition.setProductnr(product.getProductnr());
                }
                if (product.getProductname() != null) {
                    invoicePosition.setProductname(product.getProductname());
                }
                if (product.getCategory() != null) {
                    invoicePosition.setCategory(product.getCategory());
                }
                if (product.getDescription() != null) {
                    invoicePosition.setDescription(product.getDescription());
                }
                if (product.getSaleprice() != null) {
                    invoicePosition.setNetamount(product.getSaleprice());
                }
                if (product.getPretax() != null) {
                    invoicePosition.setPretax(product.getPretax());
                }
                if (product.getTaxtype() != null) {
                    invoicePosition.setTaxtype(product.getTaxtype());
                }
                if (product.getType() != null) {
                    invoicePosition.setType(product.getType());
                }
                addProduct.productID = product.getProductid();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProductFromOffer() {
        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                offerPosition.setProductid(product.getProductid());
                offerPosition.setCreateddate(new java.util.Date());
                offerPosition.setCreatedfrom(EBISystem.ebiUser);
                if (product.getProductnr() != null) {
                    offerPosition.setProductnr(product.getProductnr());
                }
                if (product.getProductname() != null) {
                    offerPosition.setProductname(product.getProductname());
                }
                if (product.getCategory() != null) {
                    offerPosition.setCategory(product.getCategory());
                }
                if (product.getDescription() != null) {
                    offerPosition.setDescription(product.getDescription());
                }
                if (product.getSaleprice() != null) {
                    offerPosition.setNetamount(product.getSaleprice());
                }
                if (product.getPretax() != null) {
                    offerPosition.setPretax(product.getPretax());
                }
                if (product.getTaxtype() != null) {
                    offerPosition.setTaxtype(product.getTaxtype());
                }
                if (product.getType() != null) {
                    offerPosition.setType(product.getType());
                }
                addProduct.productID = product.getProductid();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProductFromService() {
        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                servicePosition.setProductid(product.getProductid());
                servicePosition.setCreateddate(new java.util.Date());
                servicePosition.setCreatedfrom(EBISystem.ebiUser);
                if (product.getProductnr() != null) {
                    servicePosition.setProductnr(product.getProductnr());
                }
                if (product.getProductname() != null) {
                    servicePosition.setProductname(product.getProductname());
                }
                if (product.getCategory() != null) {
                    servicePosition.setCategory(product.getCategory());
                }
                if (product.getDescription() != null) {
                    servicePosition.setDescription(product.getDescription());
                }
                if (product.getSaleprice() != null) {
                    servicePosition.setNetamount(product.getSaleprice());
                }
                if (product.getPretax() != null) {
                    servicePosition.setPretax(product.getPretax());
                }
                if (product.getTaxtype() != null) {
                    servicePosition.setTaxtype(product.getTaxtype());
                }
                if (product.getType() != null) {
                    servicePosition.setType(product.getType());
                }
                addProduct.productID = product.getProductid();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProductFromProsol() {
        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                prosolPosition.setProductid(product.getProductid());
                prosolPosition.setCreateddate(new java.util.Date());
                prosolPosition.setCreatedfrom(EBISystem.ebiUser);

                if (product.getProductnr() != null) {
                    prosolPosition.setProductnr(product.getProductnr());
                }
                if (product.getProductname() != null) {
                    prosolPosition.setProductname(product.getProductname());
                }
                if (product.getCategory() != null) {
                    prosolPosition.setCategory(product.getCategory());
                }
                if (product.getDescription() != null) {
                    prosolPosition.setDescription(product.getDescription());
                }
                if (product.getSaleprice() != null) {
                    prosolPosition.setNetamount(product.getSaleprice());
                }
                if (product.getPretax() != null) {
                    prosolPosition.setPretax(product.getPretax());
                }
                if (product.getTaxtype() != null) {
                    prosolPosition.setTaxtype(product.getTaxtype());
                }
                if (product.getType() != null) {
                    prosolPosition.setType(product.getType());
                }
                addProduct.productID = product.getProductid();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    private void fillProductFromOrder() {
        Query query;
        try {
            query = EBISystem.hibernate().session("SEARCH_PRODUCT_SESSION").createQuery(
                    "from Crmproduct where productid=?1 ").setParameter(1, Integer.valueOf(selectedNode.getProductID()));

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Crmproduct product = (Crmproduct) it.next();
                orderPosition.setProductid(product.getProductid());
                orderPosition.setCreateddate(new java.util.Date());
                orderPosition.setCreatedfrom(EBISystem.ebiUser);
                if (product.getProductnr() != null) {
                    orderPosition.setProductnr(product.getProductnr());
                }
                if (product.getProductname() != null) {
                    orderPosition.setProductname(product.getProductname());
                }
                if (product.getCategory() != null) {
                    orderPosition.setCategory(product.getCategory());
                }
                if (product.getDescription() != null) {
                    orderPosition.setDescription(product.getDescription());
                }
                if (product.getSaleprice() != null) {
                    orderPosition.setNetamount(product.getSaleprice());
                }
                if (product.getPretax() != null) {
                    orderPosition.setPretax(product.getPretax());
                }
                if (product.getTaxtype() != null) {
                    orderPosition.setTaxtype(product.getTaxtype());
                }
                if (product.getType() != null) {
                    orderPosition.setType(product.getType());
                }
                addProduct.productID = product.getProductid();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}