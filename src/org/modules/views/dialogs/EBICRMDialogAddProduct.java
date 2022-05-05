package org.modules.views.dialogs;

import org.sdk.model.hibernate.Crminvoiceposition;
import org.sdk.model.hibernate.Crminvoice;
import org.sdk.model.hibernate.Companyservicepositions;
import org.sdk.model.hibernate.Companyofferpositions;
import org.sdk.model.hibernate.Companyoffer;
import org.sdk.model.hibernate.Companyservice;
import org.sdk.model.hibernate.Crmproblemsolposition;
import org.sdk.model.hibernate.Crmproblemsolutions;
import org.sdk.model.hibernate.Companyorderpositions;
import org.sdk.model.hibernate.Companyorder;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIJTextFieldNumeric;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

public class EBICRMDialogAddProduct {

    private Companyoffer offer = null;
    private Companyorder order = null;
    private Crminvoice invoice = null;
    private Companyservice service = null;
    private Crmproblemsolutions prosol = null;
    private boolean isOrder = false;
    private boolean isOffer = false;
    private boolean isService = false;
    private boolean isProsol = false;
    private boolean isInvoice = false;
    private Companyorderpositions orderPosition = null;
    private Companyofferpositions offerPosition = null;
    private Crminvoiceposition invoicePosition = null;
    private Companyservicepositions servicePosition = null;
    private Crmproblemsolposition prosolPosition = null;
    private NumberFormat currency = null;
    public boolean canSave = false;
    public int productID = 0;

    public EBICRMDialogAddProduct(final Companyoffer offer) {
        offerPosition = new Companyofferpositions();
        EBISystem.builder().loadGUI("CRMDialog/productInsertDialog.xml");
        this.offer = offer;
        isOffer = true;
    }

    public EBICRMDialogAddProduct(final Companyorder order) {
        orderPosition = new Companyorderpositions();
        EBISystem.builder().loadGUI("CRMDialog/productInsertDialog.xml");
        this.order = order;
        isOrder = true;
    } 

    public EBICRMDialogAddProduct(final Crminvoice invoice) {
        invoicePosition = new Crminvoiceposition();
        EBISystem.builder().loadGUI("CRMDialog/productInsertDialog.xml");
        this.invoice = invoice;
        isInvoice = true;
    }

    public EBICRMDialogAddProduct(final Companyservice service) {
        servicePosition = new Companyservicepositions();
        EBISystem.builder().loadGUI("CRMDialog/productInsertDialog.xml");
        this.service = service;
        isService = true;
    }

    public EBICRMDialogAddProduct(final Crmproblemsolutions prosol) {
        prosolPosition = new Crmproblemsolposition();
        EBISystem.builder().loadGUI("CRMDialog/productInsertDialog.xml");
        this.prosol = prosol;
        isProsol = true;
    }

    public void setVisible() {

        currency = NumberFormat.getCurrencyInstance();
        EBISystem.builder().dialog("productInsertDialog").setTitle(EBISystem.i18n("EBI_LANG_C_INSERT_PRODUCT"));
        EBISystem.builder().vpanel("productInsertDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_INSERT_PRODUCT"));

        EBISystem.builder().label("deduction", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_DEDUCTION"));
        EBISystem.builder().label("quantity", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_QUANTITY"));

        if (isProsol) {
            EBISystem.builder().label("deduction", "productInsertDialog").setVisible(false);
            EBISystem.builder().label("quantity", "productInsertDialog").setVisible(false);
            EBISystem.builder().textField("quantityText", "productInsertDialog").setVisible(false);
            EBISystem.builder().textField("deductionText", "productInsertDialog").setVisible(false);
            EBISystem.builder().label("%", "productInsertDialog").setVisible(false);
        }

        EBISystem.builder().button("closeButton", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.builder().button("closeButton", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("productInsertDialog").setVisible(false);
            }
        });
        
        EBISystem.builder().button("applyButton", "productInsertDialog").setEnabled(false);
        EBISystem.builder().button("applyButton", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.builder().button("applyButton", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                savePosistion();
            }
        });

        EBISystem.builder().button("searchProduct", "productInsertDialog").setIcon(EBISystem.getInstance().getIconResource("find.png"));
        EBISystem.builder().button("searchProduct", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                EBICRMDialogSearchProduct prod = null;
                if (isOffer) {
                    prod = new EBICRMDialogSearchProduct(offerPosition, EBICRMDialogAddProduct.this);
                }
                if (isOrder) {
                    prod = new EBICRMDialogSearchProduct(orderPosition, EBICRMDialogAddProduct.this);
                }
                if (isService) {
                    prod = new EBICRMDialogSearchProduct(servicePosition, EBICRMDialogAddProduct.this);
                }

                if (isProsol) {
                    prod = new EBICRMDialogSearchProduct(prosolPosition, EBICRMDialogAddProduct.this);
                }

                if (isInvoice) {
                    prod = new EBICRMDialogSearchProduct(invoicePosition, EBICRMDialogAddProduct.this);
                }

                if (prod != null) {
                    //macos hack
                    EBISystem.builder().dialog("productInsertDialog").setVisible(false);
                    prod.setVisible();
                    EBISystem.builder().dialog("productInsertDialog").setVisible(true);
                }
                EBISystem.builder().textField("quantityText", "productInsertDialog").requestFocus();
                
            }
        });

        EBISystem.builder().textField("quantityText", "productInsertDialog").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.NUMERIC_MINUS));
        EBISystem.builder().textField("quantityText", "productInsertDialog").requestFocus();
        EBISystem.builder().textField("quantityText", "productInsertDialog").registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                savePosistion();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.builder().textField("deductionText", "productInsertDialog").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.NUMERIC));
        EBISystem.builder().textField("deductionText", "productInsertDialog").registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                savePosistion();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.builder().button("newProduct", "productInsertDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.builder().button("newProduct", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                resetFields();
            }
        });

        EBISystem.builder().textField("deductionText", "productInsertDialog").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                fillHTMLForm();
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                fillHTMLForm();
            }

        });

        EBISystem.builder().textField("quantityText", "productInsertDialog").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                fillHTMLForm();
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                fillHTMLForm();
            }

        });

        final StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        buffer.append("<head><title></title></head>");
        buffer.append("<body bgcolor=#dddddd>");
        buffer.append("<table border=0 width=100%><tr><td bgcolor=#CCCCCC colspan=2><b>" + EBISystem.i18n("EBI_LANG_PRODUCT") + "</b></td></tr>");
        buffer.append("</table></body>");
        EBISystem.builder().getEditor("productText", "productInsertDialog").setText(buffer.toString());
        EBISystem.builder().showGUI();
    }

    /**
     * Save product for specified module
     *
     */
    public void savePosistion() {
        
        if (canSave) {
            
            if (isOffer) {
                saveOfferPosition();
            }
            
            if (isOrder) {
                saveOrderPosition();
            }
            
            if (isService) {
                saveServicePosition();
            }
            if (isProsol) {
                saveProsolPosition();
            }
            if (isInvoice) {
                saveInvoicePosition();
            }
        }
    }

    private void saveOfferPosition() {
        if (!validateInput()) {
            return;
        }
        offerPosition.setCompanyoffer(offer);
        offerPosition.setPositionid((offer.getCompanyofferpositionses().size() + 1) * -1);
        offerPosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.builder().textField("quantityText", "productInsertDialog").getText())));
        offerPosition.setDeduction(EBISystem.builder().textField("deductionText", "productInsertDialog").getText());
        offerPosition.setNetamount(offerPosition.getNetamount());
        offer.getCompanyofferpositionses().add(offerPosition);
        EBISystem.getModule().getOfferPane().showProduct();
        resetFields();
    }

    private void saveInvoicePosition() {
        if (!validateInput()) {
            return;
        }
        invoicePosition.setCrminvoice(this.invoice);
        invoicePosition.setPositionid((invoice.getCrminvoicepositions().size() + 1) * -1);
        invoicePosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.builder().textField("quantityText", "productInsertDialog").getText())));
        invoicePosition.setDeduction(EBISystem.builder().textField("deductionText", "productInsertDialog").getText());
        invoicePosition.setNetamount(invoicePosition.getNetamount());
        invoice.getCrminvoicepositions().add(invoicePosition);
        EBISystem.getModule().getInvoicePane().dataShowProduct();
        resetFields();
    }

    private void saveServicePosition() {
        if (!validateInput()) {
            return;
        }
        servicePosition.setCompanyservice(this.service);
        servicePosition.setPositionid((service.getCompanyservicepositionses().size() + 1) * -1);
        servicePosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.builder().textField("quantityText", "productInsertDialog").getText())));
        servicePosition.setDeduction(EBISystem.builder().textField("deductionText", "productInsertDialog").getText());
        servicePosition.setNetamount(servicePosition.getNetamount());
        service.getCompanyservicepositionses().add(servicePosition);
        EBISystem.getModule().getServicePane().showProduct();
        resetFields();
    }

    private void saveProsolPosition() {
        prosolPosition.setCrmproblemsolutions(this.prosol);
        prosolPosition.setNetamount(prosolPosition.getNetamount());
        prosolPosition.setPositionid((prosol.getCrmproblemsolpositions().size() + 1) * -1);
        prosol.getCrmproblemsolpositions().add(prosolPosition);
        EBISystem.getModule().getProsolPane().showProsolProduct();
        resetFields();
    }

    private void saveOrderPosition() {
        if (!validateInput()) {
            return;
        }
        orderPosition.setCompanyorder(order);
        orderPosition.setPositionid((order.getCompanyorderpositionses().size() + 1) * -1);
        orderPosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.builder().textField("quantityText", "productInsertDialog").getText())));
        orderPosition.setDeduction(EBISystem.builder().textField("deductionText", "productInsertDialog").getText());
        orderPosition.setNetamount(orderPosition.getNetamount());
        order.getCompanyorderpositionses().add(orderPosition);
        EBISystem.getModule().getOrderPane().showProduct();
        resetFields();
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(EBISystem.builder().textField("quantityText", "productInsertDialog").getText().replace(',', '.'));
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")+" : "+EBISystem.i18n("EBI_LANG_QUANTITY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void resetFields() {
        EBISystem.builder().textField("quantityText", "productInsertDialog").setText("");
        EBISystem.builder().textField("deductionText", "productInsertDialog").setText("");

        if (!isOrder) {
            offerPosition = new Companyofferpositions();
        }

        if (isOrder) {
            orderPosition = new Companyorderpositions();
        }
        
        if (isService) {
            servicePosition = new Companyservicepositions();
        }

        if (isProsol) {
            prosolPosition = new Crmproblemsolposition();
        }

        if (isInvoice) {
            invoicePosition = new Crminvoiceposition();
        }

        final StringBuffer buffer = new StringBuffer();
        buffer.append("<body>");
        buffer.append("<table style=\"font-family: Verdana, serif;color:#000;font-size: 10px; border: solid 1px #a0f0ff;\" border=0 width=100%><tr><td bgcolor=#CCCCCC colspan=2><b>" + EBISystem.i18n("EBI_LANG_PRODUCT") + "</b></td></tr>");
        buffer.append("</table></body>");
        EBISystem.builder().getEditor("productText", "productInsertDialog").setText(buffer.toString());
        EBISystem.builder().button("applyButton", "productInsertDialog").setEnabled(false);
        canSave = false;
    }

    public void fillHTMLForm() {
        final StringBuffer buffer = new StringBuffer();

        setQuantityScale();

        buffer.append("<table style=\"font-family: Verdana, serif;color:#000;font-size: 10px; border: solid 1px #a0f0ff;\" border=0 width=100%><tr><td bgcolor=#a0f0ff style=\"border: solid 1px #a0f0ff;\" colspan=2><b>" + EBISystem.i18n("EBI_LANG_PRODUCT") + "</b></td></tr>");
        if (isOrder && orderPosition.getProductnr() != null) {

            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + orderPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + orderPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + orderPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(orderPosition.getType()) ? "" : orderPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(orderPosition.getTaxtype()) ? "" : orderPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(orderPosition.getNetamount(), EBISystem.builder().textField("quantityText", "productInsertDialog").getText(), EBISystem.builder().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + orderPosition.getDescription() + "</td></tr>");

        } else if (isOffer && offerPosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + offerPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + offerPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + offerPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(offerPosition.getType()) ? "" : offerPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(offerPosition.getTaxtype()) ? "" : offerPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(offerPosition.getNetamount(), EBISystem.builder().textField("quantityText", "productInsertDialog").getText(), EBISystem.builder().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + offerPosition.getDescription() + "</td></tr>");

        } else if (isService && servicePosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + servicePosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + servicePosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + servicePosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(servicePosition.getType()) ? "" : servicePosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(servicePosition.getTaxtype()) ? "" : servicePosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(servicePosition.getNetamount(), EBISystem.builder().textField("quantityText", "productInsertDialog").getText(), EBISystem.builder().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + servicePosition.getDescription() + "</td></tr>");
        } else if (isProsol && prosolPosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + prosolPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + prosolPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + prosolPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(prosolPosition.getType()) ? "" : prosolPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(prosolPosition.getTaxtype()) ? "" : prosolPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(prosolPosition.getNetamount(), "1", EBISystem.builder().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + prosolPosition.getDescription() + "</td></tr>");
        } else if (isInvoice && invoicePosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + invoicePosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + invoicePosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + invoicePosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(invoicePosition.getType()) ? "" : invoicePosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(invoicePosition.getTaxtype()) ? "" : invoicePosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getModule().dynMethod.calculatePreTaxPrice(invoicePosition.getNetamount(), EBISystem.builder().textField("quantityText", "productInsertDialog").getText(), EBISystem.builder().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + invoicePosition.getDescription() + "</td></tr>");
        }

        buffer.append("</table>");
        EBISystem.builder().getEditor("productText", "productInsertDialog").setText(buffer.toString());
    }

    public void setQuantityScale() {

        ResultSet set = null;
        PreparedStatement pst = null;

        try {
            pst = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM CRMPRODUCTDIMENSION WHERE PRODUCTID=? AND DIMENSIONNAME LIKE ? ORDER BY DIMENSIONVALUE DESC ");
            pst.setInt(1, this.productID);
            pst.setString(2, EBISystem.i18n("EBI_LANG_DEDUCTION"));
            set = EBISystem.getInstance().iDB().executePreparedQuery(pst);
            final int count = Integer.parseInt(EBISystem.builder().textField("quantityText", "productInsertDialog").getText());
            if(set != null){
                set.last();
                if (set.getRow() > 0) {
                    set.beforeFirst();
                    while (set.next()) {
                        if (set.getString("DIMENSIONVALUE") != null || !"".equals(set.getString("DIMENSIONVALUE"))) {
                            String[] splt = set.getString("DIMENSIONVALUE").split("-");
                            if (splt.length <= 1) {
                                splt = set.getString("DIMENSIONVALUE").split(" ");
                                if (splt.length <= 0) {
                                    return;
                                }
                            }
                            if (count >= Integer.parseInt(splt[0]) && count <= Integer.parseInt(splt[1])) {
                                EBISystem.builder().textField("deductionText", "productInsertDialog").setText(splt[2].substring(0, splt[2].length() - 1));
                            } else if (count < Integer.parseInt(splt[0]) && count <= Integer.parseInt(splt[1])) {
                                EBISystem.builder().textField("deductionText", "productInsertDialog").setText("");
                            }
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            return;
        } finally {
            try {
                set.close();
                pst.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
