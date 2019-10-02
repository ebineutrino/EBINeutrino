package ebiCRM.gui.dialogs;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.component.EBIJTextFieldNumeric;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.*;

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
    private Crmcampaign campaign = null;
    private Crminvoice invoice = null;
    private Companyservice service = null;
    private Crmproblemsolutions prosol = null;
    private boolean isOrder = false;
    private boolean isOffer = false;
    private boolean isCampaign = false;
    private boolean isService = false;
    private boolean isProsol = false;
    private boolean isInvoice = false;
    private Companyorderpositions orderPosition = null;
    private Companyofferpositions offerPosition = null;
    private Crmcampaignposition campaignPosition = null;
    private Crminvoiceposition invoicePosition = null;
    private Companyservicepositions servicePosition = null;
    private Crmproblemsolposition prosolPosition = null;
    private NumberFormat currency = null;
    public boolean canSave = false;
    public int productID = 0;

    public EBICRMDialogAddProduct(final Companyoffer offer) {
        offerPosition = new Companyofferpositions();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.offer = offer;
        isOffer = true;
    }

    public EBICRMDialogAddProduct(final Companyorder order) {
        orderPosition = new Companyorderpositions();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.order = order;
        isOrder = true;
    }

    public EBICRMDialogAddProduct(final Crmcampaign campaign) {
        campaignPosition = new Crmcampaignposition();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.campaign = campaign;
        isCampaign = true;
    }

    public EBICRMDialogAddProduct(final Crminvoice invoice) {
        invoicePosition = new Crminvoiceposition();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.invoice = invoice;
        isInvoice = true;
    }

    public EBICRMDialogAddProduct(final Companyservice service) {
        servicePosition = new Companyservicepositions();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.service = service;
        isService = true;
    }

    public EBICRMDialogAddProduct(final Crmproblemsolutions prosol) {
        prosolPosition = new Crmproblemsolposition();
        EBISystem.gui().loadGUI("CRMDialog/productInsertDialog.xml");
        this.prosol = prosol;
        isProsol = true;
    }

    public void setVisible() {

        currency = NumberFormat.getCurrencyInstance();
        EBISystem.gui().dialog("productInsertDialog").setTitle(EBISystem.i18n("EBI_LANG_C_INSERT_PRODUCT"));
        EBISystem.gui().vpanel("productInsertDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_INSERT_PRODUCT"));

        EBISystem.gui().label("deduction", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_DEDUCTION"));
        EBISystem.gui().label("quantity", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_QUANTITY"));

        if (isProsol) {
            EBISystem.gui().label("deduction", "productInsertDialog").setVisible(false);
            EBISystem.gui().label("quantity", "productInsertDialog").setVisible(false);
            EBISystem.gui().textField("quantityText", "productInsertDialog").setVisible(false);
            EBISystem.gui().textField("deductionText", "productInsertDialog").setVisible(false);
            EBISystem.gui().label("%", "productInsertDialog").setVisible(false);
        }

        EBISystem.gui().button("closeButton", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        EBISystem.gui().button("closeButton", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("productInsertDialog").setVisible(false);
            }
        });
        EBISystem.gui().button("applyButton", "productInsertDialog").setEnabled(false);
        EBISystem.gui().button("applyButton", "productInsertDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.gui().button("applyButton", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                savePosistion();
            }
        });

        EBISystem.gui().button("searchProduct", "productInsertDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("find.png")));
        EBISystem.gui().button("searchProduct", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {

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

                if (isCampaign) {
                    prod = new EBICRMDialogSearchProduct(campaignPosition, EBICRMDialogAddProduct.this);
                }

                if (isProsol) {
                    prod = new EBICRMDialogSearchProduct(prosolPosition, EBICRMDialogAddProduct.this);
                }

                if (isInvoice) {
                    prod = new EBICRMDialogSearchProduct(invoicePosition, EBICRMDialogAddProduct.this);
                }

                if (prod != null) {
                    prod.setVisible();
                }
                EBISystem.gui().textField("quantityText", "productInsertDialog").requestFocus();
            }
        });

        EBISystem.gui().textField("quantityText", "productInsertDialog").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.NUMERIC_MINUS));
        EBISystem.gui().textField("quantityText", "productInsertDialog").requestFocus();
        EBISystem.gui().textField("quantityText", "productInsertDialog").registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                savePosistion();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.gui().textField("deductionText", "productInsertDialog").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.NUMERIC));
        EBISystem.gui().textField("deductionText", "productInsertDialog").registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                savePosistion();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        EBISystem.gui().button("newProduct", "productInsertDialog").setIcon(new ImageIcon(getClass().getClassLoader().getResource("new.png")));
        EBISystem.gui().button("newProduct", "productInsertDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                resetFields();
            }
        });

        EBISystem.gui().textField("deductionText", "productInsertDialog").addKeyListener(new KeyListener() {
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

        EBISystem.gui().textField("quantityText", "productInsertDialog").addKeyListener(new KeyListener() {
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
        EBISystem.gui().getEditor("productText", "productInsertDialog").setText(buffer.toString());
        EBISystem.gui().showGUI();
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
            if (isCampaign) {
                saveCampaignPosition();
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
            canSave = false;
        }
    }

    private void saveOfferPosition() {
        if (!validateInput()) {
            return;
        }
        offerPosition.setCompanyoffer(offer);
        offerPosition.setPositionid((offer.getCompanyofferpositionses().size() + 1) * -1);
        offerPosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.gui().textField("quantityText", "productInsertDialog").getText())));
        offerPosition.setDeduction(EBISystem.gui().textField("deductionText", "productInsertDialog").getText());
        offerPosition.setNetamount(offerPosition.getNetamount());
        offer.getCompanyofferpositionses().add(offerPosition);
        EBISystem.getCRMModule().getOfferPane().showProduct();
        resetFields();
    }

    private void saveCampaignPosition() {
        if (!validateInput()) {
            return;
        }
        campaignPosition.setCrmcampaign(this.campaign);
        campaignPosition.setPositionid((campaign.getCrmcampaignpositions().size() + 1) * -1);
        campaignPosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.gui().textField("quantityText", "productInsertDialog").getText())));
        campaignPosition.setDeduction(EBISystem.gui().textField("deductionText", "productInsertDialog").getText());
        campaignPosition.setNetamount(campaignPosition.getNetamount());
        campaign.getCrmcampaignpositions().add(campaignPosition);
        EBISystem.getCRMModule().getEBICRMCampaign().showProduct();
        resetFields();
    }

    private void saveInvoicePosition() {
        if (!validateInput()) {
            return;
        }
        invoicePosition.setCrminvoice(this.invoice);
        invoicePosition.setPositionid((invoice.getCrminvoicepositions().size() + 1) * -1);
        invoicePosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.gui().textField("quantityText", "productInsertDialog").getText())));
        invoicePosition.setDeduction(EBISystem.gui().textField("deductionText", "productInsertDialog").getText());
        invoicePosition.setNetamount(invoicePosition.getNetamount());
        invoice.getCrminvoicepositions().add(invoicePosition);
        EBISystem.getCRMModule().getInvoicePane().dataShowProduct();

        resetFields();
    }

    private void saveServicePosition() {
        if (!validateInput()) {
            return;
        }
        servicePosition.setCompanyservice(this.service);
        servicePosition.setPositionid((service.getCompanyservicepositionses().size() + 1) * -1);
        servicePosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.gui().textField("quantityText", "productInsertDialog").getText())));
        servicePosition.setDeduction(EBISystem.gui().textField("deductionText", "productInsertDialog").getText());
        servicePosition.setNetamount(servicePosition.getNetamount());
        service.getCompanyservicepositionses().add(servicePosition);
        EBISystem.getCRMModule().getServicePane().showProduct();
        resetFields();
    }

    private void saveProsolPosition() {
        prosolPosition.setCrmproblemsolutions(this.prosol);
        prosolPosition.setNetamount(prosolPosition.getNetamount());
        prosolPosition.setPositionid((prosol.getCrmproblemsolpositions().size() + 1) * -1);
        prosol.getCrmproblemsolpositions().add(prosolPosition);
        EBISystem.getCRMModule().getProsolPane().showProsolProduct();
        resetFields();
    }

    private void saveOrderPosition() {
        if (!validateInput()) {
            return;
        }
        orderPosition.setCompanyorder(order);
        orderPosition.setPositionid((order.getCompanyorderpositionses().size() + 1) * -1);
        orderPosition.setQuantity(BigInteger.valueOf(Long.parseLong(EBISystem.gui().textField("quantityText", "productInsertDialog").getText())));
        orderPosition.setDeduction(EBISystem.gui().textField("deductionText", "productInsertDialog").getText());
        orderPosition.setNetamount(orderPosition.getNetamount());
        order.getCompanyorderpositionses().add(orderPosition);
        EBISystem.getCRMModule().getOrderPane().showProduct();
        resetFields();
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(EBISystem.gui().textField("quantityText", "productInsertDialog").getText().replace(',', '.'));
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void resetFields() {
        EBISystem.gui().textField("quantityText", "productInsertDialog").setText("");
        EBISystem.gui().textField("deductionText", "productInsertDialog").setText("");

        if (!isOrder) {
            offerPosition = new Companyofferpositions();
        }

        if (isOrder) {
            orderPosition = new Companyorderpositions();
        }

        if (isCampaign) {
            campaignPosition = new Crmcampaignposition();
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
        EBISystem.gui().getEditor("productText", "productInsertDialog").setText(buffer.toString());
        EBISystem.gui().button("applyButton", "productInsertDialog").setEnabled(false);
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
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(orderPosition.getNetamount(), EBISystem.gui().textField("quantityText", "productInsertDialog").getText(), EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + orderPosition.getDescription() + "</td></tr>");

        } else if (isOffer && offerPosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + offerPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + offerPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + offerPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(offerPosition.getType()) ? "" : offerPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(offerPosition.getTaxtype()) ? "" : offerPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(offerPosition.getNetamount(), EBISystem.gui().textField("quantityText", "productInsertDialog").getText(), EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + offerPosition.getDescription() + "</td></tr>");

        } else if (isCampaign && campaignPosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + campaignPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + campaignPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + campaignPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(campaignPosition.getType()) ? "" : campaignPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(campaignPosition.getTaxtype()) ? "" : campaignPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(campaignPosition.getNetamount(), EBISystem.gui().textField("quantityText", "productInsertDialog").getText(), EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + campaignPosition.getDescription() + "</td></tr>");
        } else if (isService && servicePosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + servicePosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + servicePosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + servicePosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(servicePosition.getType()) ? "" : servicePosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(servicePosition.getTaxtype()) ? "" : servicePosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(servicePosition.getNetamount(), EBISystem.gui().textField("quantityText", "productInsertDialog").getText(), EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + servicePosition.getDescription() + "</td></tr>");
        } else if (isProsol && prosolPosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + prosolPosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + prosolPosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + prosolPosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(prosolPosition.getType()) ? "" : prosolPosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(prosolPosition.getTaxtype()) ? "" : prosolPosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(prosolPosition.getNetamount(), "1", EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + prosolPosition.getDescription() + "</td></tr>");
        } else if (isInvoice && invoicePosition.getProductnr() != null) {
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_PRODUCT_NR") + "</td><td bgcolor='#ebebeb' >" + invoicePosition.getProductnr() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_NAME") + "</td><td bgcolor='#eeeeee'>" + invoicePosition.getProductname() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_CATEGORY") + "</td><td bgcolor='#ebebeb'>" + invoicePosition.getCategory() + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_TYPE") + "</td><td bgcolor='#eeeeee'>" + ("null".equals(invoicePosition.getType()) ? "" : invoicePosition.getType()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#ebebeb>" + EBISystem.i18n("EBI_LANG_TAX_TYPE") + "</td><td bgcolor='#ebebeb'>" + ("null".equals(invoicePosition.getTaxtype()) ? "" : invoicePosition.getTaxtype()) + "</td></tr>");
            buffer.append("<tr><td width=5% bgcolor=#eeeeee>" + EBISystem.i18n("EBI_LANG_SALE_PRICE") + "</td><td bgcolor='#eeeeee'>"
                    + currency.format(EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(invoicePosition.getNetamount(), EBISystem.gui().textField("quantityText", "productInsertDialog").getText(), EBISystem.gui().textField("deductionText", "productInsertDialog").getText())) + "</td></tr>");
            buffer.append("<tr><td  bgcolor=#a0f0ff colspan='2'><b>" + EBISystem.i18n("EBI_LANG_DESCRIPTION") + "</b></td></tr>");
            buffer.append("<tr><td  bgcolor=#eeeeee colspan='2'>" + invoicePosition.getDescription() + "</td></tr>");
        }

        buffer.append("</table>");
        EBISystem.gui().getEditor("productText", "productInsertDialog").setText(buffer.toString());
    }

    public void setQuantityScale() {

        ResultSet set = null;
        PreparedStatement pst = null;

        try {

            pst = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM CRMPRODUCTDIMENSION WHERE PRODUCTID=? AND NAME LIKE ? ORDER BY VALUE DESC ");
            pst.setInt(1, this.productID);
            pst.setString(2, EBISystem.i18n("EBI_LANG_DEDUCTION"));
            set = EBISystem.getInstance().iDB().executePreparedQuery(pst);
            final int count = Integer.parseInt(EBISystem.gui().textField("quantityText", "productInsertDialog").getText());

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {

                    if (set.getString("VALUE") != null || !"".equals(set.getString("VALUE"))) {

                        String[] splt = set.getString("VALUE").split("-");
                        if (splt.length <= 1) {
                            splt = set.getString("VALUE").split(" ");
                            if (splt.length <= 0) {
                                return;
                            }
                        }

                        if (count >= Integer.parseInt(splt[0]) && count <= Integer.parseInt(splt[1])) {
                            EBISystem.gui().textField("deductionText", "productInsertDialog").setText(splt[2].substring(0, splt[2].length() - 1));
                        } else if (count < Integer.parseInt(splt[0]) && count <= Integer.parseInt(splt[1])) {
                            EBISystem.gui().textField("deductionText", "productInsertDialog").setText("");
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
