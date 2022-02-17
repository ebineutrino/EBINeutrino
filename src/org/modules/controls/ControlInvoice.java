package org.modules.controls;

import org.sdk.model.hibernate.Crminvoiceposition;
import org.sdk.model.hibernate.Crminvoice;
import org.sdk.model.hibernate.Companyproducttax;
import org.sdk.model.hibernate.Companyorder;
import org.sdk.model.hibernate.Companyservice;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIAbstractTableModel;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.text.NumberFormat;
import java.util.*;

public class ControlInvoice {

    public Crminvoice invoice = null;
    public boolean isEdit = false;

    public ControlInvoice() {
        invoice = new Crminvoice();
    }

    public Integer dataStore() {
        Integer invoiceID = -1;
        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            if (isEdit == false) {
                invoice.setCreatedfrom(EBISystem.builder().vpanel("Invoice").getCreatedFrom());
                invoice.setCreateddate(new Date());
            } else {
                createHistory(invoice.getInvoiceid());
                invoice.setChangeddate(new Date());
                invoice.setChangedfrom(EBISystem.ebiUser);
            }

            // Invoice main data
            invoice.setInvoicenr(EBISystem.getModule().getInvoicePane().getInvoiceNr());
            invoice.setBeginchar(EBISystem.getModule().getInvoicePane().getBeginChar());
            invoice.setName(EBISystem.builder().textField("invoiceNameText", "Invoice").getText());

            if (EBISystem.builder().combo("invoiceStatusText", "Invoice").getEditor().getItem()  != null) {
                invoice.setStatus(EBISystem.builder().combo("invoiceStatusText", "Invoice").getEditor().getItem().toString());
            }

            if (!isEdit) {
                if (EBISystem.builder().combo("categoryText", "Invoice").getEditor().getItem() != null) {
                    invoice.setCategory(EBISystem.builder().combo("categoryText", "Invoice").getEditor().getItem().toString());
                }
            }

            if (EBISystem.builder().timePicker("invoiceDateText", "Invoice").getDate() != null) {
                invoice.setDate(EBISystem.builder().timePicker("invoiceDateText", "Invoice").getDate());
            } else {
                invoice.setDate(new Date());
            }

            if (!"".equals(EBISystem.builder().textField("orderText", "Invoice").getText())) {
                invoice.setAssosiation(EBISystem.builder().textField("orderText", "Invoice").getText());
            }

            // Invoice rec
            if (EBISystem.builder().combo("genderText", "Invoice").getEditor().getItem() != null) {
                invoice.setGender(EBISystem.builder().combo("genderText", "Invoice").getEditor().getItem().toString());
            }

            invoice.setPosition(EBISystem.builder().textField("titleText", "Invoice").getText());
            invoice.setCompanyname(EBISystem.builder().textField("companyNameText", "Invoice").getText());
            invoice.setContactname(EBISystem.builder().textField("nameText", "Invoice").getText());
            invoice.setContactsurname(EBISystem.builder().textField("surnameText", "Invoice").getText());
            invoice.setContactstreet(EBISystem.builder().textField("streetNrText", "Invoice").getText());
            invoice.setContactzip(EBISystem.builder().textField("zipText", "Invoice").getText());
            invoice.setContactlocation(EBISystem.builder().textField("locationText", "Invoice").getText());
            invoice.setContactpostcode(EBISystem.builder().textField("postCodeText", "Invoice").getText());
            invoice.setContactcountry(EBISystem.builder().textField("countryText", "Invoice").getText());
            invoice.setContacttelephone(EBISystem.builder().textField("telefonText", "Invoice").getText());
            invoice.setContactfax(EBISystem.builder().textField("faxText", "Invoice").getText());
            invoice.setContactemail(EBISystem.builder().textField("emailText", "Invoice").getText());
            invoice.setContactweb(EBISystem.builder().textField("internetText", "Invoice").getText());
            invoice.setContactdescription(EBISystem.builder().textArea("recDescription", "Invoice").getText());
            EBISystem.hibernate().session("EBIINVOICE_SESSION").saveOrUpdate(invoice);

            // Position save
            if (!invoice.getCrminvoicepositions().isEmpty()) {
                final Iterator iter = invoice.getCrminvoicepositions().iterator();
                while (iter.hasNext()) {
                    final Crminvoiceposition pos = (Crminvoiceposition) iter.next();
                    if (pos.getPositionid() != null && pos.getPositionid() < 0) {
                        pos.setPositionid(null);
                    }
                    pos.setCrminvoice(invoice);
                    EBISystem.hibernate().session("EBIINVOICE_SESSION").saveOrUpdate(pos);
                }
            }

            EBISystem.getInstance().getDataStore("Invoice", "ebiSave");
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();

            if (!isEdit) {
                EBISystem.builder().vpanel("Invoice").setID(invoice.getInvoiceid());
            }
            invoiceID = invoice.getInvoiceid();
            isEdit = true;
        } catch (final Exception e) {
            EBISystem.hibernate().session("EBIINVOICE_SESSION").clear();
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return invoiceID;
    }

    public void dataEdit(final int id) {
        Query query;
        try {
            isEdit = true;
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            query = EBISystem.hibernate().session("EBIINVOICE_SESSION").createQuery("from Crminvoice where invoiceId=?1 ").setParameter(1, id);
            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                invoice = (Crminvoice) iter.next();
                EBISystem.builder().vpanel("Invoice").setID(invoice.getInvoiceid());
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(invoice);

                EBISystem.builder().vpanel("Invoice").setCreatedDate(EBISystem.getInstance().getDateToString(invoice.getCreateddate() == null ? new Date() : invoice.getCreateddate()));
                EBISystem.builder().vpanel("Invoice").setCreatedFrom(invoice.getCreatedfrom() == null ? EBISystem.ebiUser : invoice.getCreatedfrom());

                if (invoice.getChangeddate() != null) {
                    EBISystem.builder().vpanel("Invoice").setChangedDate(EBISystem.getInstance().getDateToString(invoice.getChangeddate()));
                    EBISystem.builder().vpanel("Invoice").setChangedFrom(invoice.getChangedfrom());
                } else {
                    EBISystem.builder().vpanel("Invoice").setChangedDate("");
                    EBISystem.builder().vpanel("Invoice").setChangedFrom("");
                }

                EBISystem.getModule().getInvoicePane().setInvoiceNr(invoice.getInvoicenr() == null ? 0 : invoice.getInvoicenr());
                EBISystem.getModule().getInvoicePane().setBeginChar(invoice.getBeginchar() == null ? "" : invoice.getBeginchar());
                EBISystem.builder().textField("invoiceNrText", "Invoice").setText(invoice.getBeginchar() + invoice.getInvoicenr());
                EBISystem.builder().textField("invoiceNameText", "Invoice").setText(invoice.getName());

                if (invoice.getStatus() != null) {
                    EBISystem.builder().combo("invoiceStatusText", "Invoice").setSelectedItem(invoice.getStatus());
                }

                if (invoice.getCategory() != null) {
                    EBISystem.builder().combo("categoryText", "Invoice").setSelectedItem(invoice.getCategory());
                }

                if (invoice.getDate() != null) {
                    EBISystem.builder().timePicker("invoiceDateText", "Invoice").setDate(invoice.getDate());
                }
                if (invoice.getAssosiation() != null && !"".equals(invoice.getAssosiation())) {
                    EBISystem.builder().textField("orderText", "Invoice").setText(invoice.getAssosiation());
                    EBISystem.builder().button("selectOrder", "Invoice").setEnabled(true);
                }
                // Invoice rec

                if (invoice.getGender() != null) {
                    EBISystem.builder().combo("genderText", "Invoice").setSelectedItem(invoice.getGender());
                }

                EBISystem.builder().textField("titleText", "Invoice").setText(invoice.getPosition());
                EBISystem.builder().textField("companyNameText", "Invoice").setText(invoice.getCompanyname());
                EBISystem.builder().textField("nameText", "Invoice").setText(invoice.getContactname());
                EBISystem.builder().textField("surnameText", "Invoice").setText(invoice.getContactsurname());
                EBISystem.builder().textField("streetNrText", "Invoice").setText(invoice.getContactstreet());
                EBISystem.builder().textField("zipText", "Invoice").setText(invoice.getContactzip());
                EBISystem.builder().textField("locationText", "Invoice").setText(invoice.getContactlocation());
                EBISystem.builder().textField("postCodeText", "Invoice").setText(invoice.getContactpostcode());
                EBISystem.builder().textField("countryText", "Invoice").setText(invoice.getContactcountry());
                EBISystem.builder().textField("telefonText", "Invoice").setText(invoice.getContacttelephone());
                EBISystem.builder().textField("faxText", "Invoice").setText(invoice.getContactfax());
                EBISystem.builder().textField("emailText", "Invoice").setText(invoice.getContactemail());
                EBISystem.builder().textField("internetText", "Invoice").setText(invoice.getContactweb());
                EBISystem.builder().textArea("recDescription", "Invoice").setText(invoice.getContactdescription());

                EBISystem.getInstance().getDataStore("Invoice", "ebiEdit");
                calculateTotalAmount();
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                        .Show(EBIMessage.INFO_MESSAGE);
            }

            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();

        } catch (final HibernateException e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void dataDelete(final int id) {
        Query query;
        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            query = EBISystem.hibernate().session("EBIINVOICE_SESSION").createQuery("from Crminvoice where invoiceId=?1 ").setParameter(1, id);
            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Crminvoice inv = (Crminvoice) iter.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").delete(inv);
                EBISystem.getInstance().getDataStore("Invoice", "ebiDelete");
                EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dataShow(final String invoiceYear, final int showID) {

        int srow = EBISystem.builder().table("tableTotalInvoice", "Invoice").getSelectedRow() + showID;
        final EBIAbstractTableModel model = (EBIAbstractTableModel) EBISystem.builder()
                .table("tableTotalInvoice", "Invoice").getModel();

        String sName = "";
        Query query;
        try {

            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            if (!"".equals(invoiceYear) && !"null".equals(invoiceYear)) {
                final Calendar calendar1 = new GregorianCalendar();
                calendar1.set(Calendar.DAY_OF_MONTH, 1);
                calendar1.set(Calendar.MONTH, Calendar.JANUARY);
                calendar1.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                final Calendar calendar2 = new GregorianCalendar();
                calendar2.set(Calendar.DAY_OF_MONTH, 31);
                calendar2.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar2.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                query = EBISystem.hibernate().session("EBIINVOICE_SESSION").createQuery("from Crminvoice cm where cm.date between ?1 and ?2 order by createddate desc");
                query.setParameter(1, calendar1.getTime());
                query.setParameter(2, calendar2.getTime());

            } else {
                query = EBISystem.hibernate().session("EBIINVOICE_SESSION").createQuery("from Crminvoice order by createddate desc");
            }

            if (query.list().size() > 0) {
                final Iterator iter = query.iterate();
                model.data = new Object[query.list().size()][10];
                int i = 0;
                while (iter.hasNext()) {
                    final Crminvoice inv = (Crminvoice) iter.next();
                    if (("" + inv.getInvoicenr()).equals(sName) && !"".equals(sName)) {
                        srow = EBISystem.builder().table("tableTotalInvoice", "Invoice").convertRowIndexToModel(i);
                    } else if (inv.getInvoiceid() == showID) {
                        srow = EBISystem.builder().table("tableTotalInvoice", "Invoice").convertRowIndexToView(i);
                    }
                    model.data[i][0] = inv.getBeginchar() + inv.getInvoicenr();
                    model.data[i][1] = inv.getName() == null ? "" : inv.getName();
                    model.data[i][2] = inv.getStatus() == null ? "" : inv.getStatus();
                    model.data[i][3] = inv.getCategory() == null ? "" : inv.getCategory();
                    model.data[i][4] = inv.getGender() == null ? "" : inv.getGender();
                    model.data[i][5] = inv.getCompanyname() == null ? "" : inv.getCompanyname();
                    model.data[i][6] = inv.getContactname() == null ? "" : inv.getContactname();
                    model.data[i][7] = inv.getContactsurname() == null ? "" : inv.getContactsurname();
                    model.data[i][8] = inv.getDate() == null ? "" : EBISystem.getInstance().getDateToString(inv.getDate());
                    model.data[i][9] = inv.getInvoiceid();
                    if (showID != -1 && showID == inv.getInvoiceid()) {
                        srow = 0;
                    }
                    i++;
                }
            } else {
                model.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", ""}};
            }
            model.fireTableDataChanged();
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        if (srow > -1) {
            srow = EBISystem.builder().table("tableTotalInvoice", "Invoice").convertRowIndexToView(srow);
            EBISystem.builder().table("tableTotalInvoice", "Invoice").changeSelection(srow, 0, false, false);
        }
    }

    public Hashtable<String, Double> getTaxName(final int id) {
        EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
        final Query y = EBISystem.hibernate().session("EBIINVOICE_SESSION")
                .createQuery("from Crminvoice where invoiceid=?1 ").setParameter(1, id);

        final NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        cashFormat.setMinimumFractionDigits(2);
        cashFormat.setMaximumFractionDigits(3);
        final Hashtable<String, Double> taxTable = new Hashtable<String, Double>();

        if (y.list().size() > 0) {
            final Iterator iter = y.iterate();

            final Crminvoice or = (Crminvoice) iter.next();

            final Iterator itx = or.getCrminvoicepositions().iterator();
            while (itx.hasNext()) {
                final Crminvoiceposition pos = (Crminvoiceposition) itx.next();
                if (pos.getTaxtype() != null) {
                    if (taxTable.containsKey(pos.getTaxtype())) {

                        taxTable.put(pos.getTaxtype(),
                                taxTable.get(pos.getTaxtype()) + (((pos.getNetamount() * pos.getQuantity().intValue())
                                * EBISystem.getModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));
                    } else {
                        taxTable.put(pos.getTaxtype(), (((pos.getNetamount() * pos.getQuantity().intValue())
                                * EBISystem.getModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));

                    }
                }
            }
        }
        EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        return taxTable;
    }

    public void dataShowReport(final int id) {

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

        EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                EBISystem.getInstance().convertReportCategoryToIndex(
                        EBISystem.i18n("EBI_LANG_PRINT_INVOICES")),
                getInvoiceNamefromId(id));
    }

    public String dataShowAndMailReport(final int id, final boolean showWindow) {
        String fileName = "";

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

        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            final Query query = EBISystem.hibernate().session("EBIINVOICE_SESSION").createQuery("from Crminvoice where invoiceId=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Crminvoice inv = (Crminvoice) iter.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(inv);

                if (!"".equals(inv.getContactemail())) {
                    fileName = EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                            EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_PRINT_INVOICES")),
                            getInvoiceNamefromId(id), showWindow, true, inv.getContactemail());
                } else {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_RECEIVER_WAS_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
                }
            }
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
        }
        return fileName;
    }

    public void dataNew() {
        invoice = new Crminvoice();
        EBISystem.builder().vpanel("Invoice").setID(-1);
        EBISystem.getModule().getInvoicePane().initialize(false);
        EBISystem.getInstance().getDataStore("Invoice", "ebiNew");
    }

    private void createHistory(final int id) {

        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                + EBISystem.getInstance().getDateToString(invoice.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + invoice.getCreatedfrom());

        if (invoice.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                    + EBISystem.getInstance().getDateToString(invoice.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + invoice.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_INVOICE_NR") + ": "
                + (("" + invoice.getInvoicenr()).
                        equals(String.valueOf(EBISystem.getModule().getInvoicePane().getInvoiceNr())) == true
                ? invoice.getInvoicenr()
                : invoice.getInvoicenr() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": "
                + (invoice.getName().equals(
                        EBISystem.builder().textField("invoiceNameText", "Invoice").getText()) == true
                ? invoice.getName()
                : invoice.getName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_STATUS") + ": "
                + (invoice.getStatus()
                        .equals(EBISystem.builder().combo("invoiceStatusText", "Invoice")
                                .getSelectedItem().toString()) == true ? invoice.getStatus()
                        : invoice.getStatus() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": "
                + (invoice.getCategory()
                        .equals(EBISystem.builder().combo("categoryText", "Invoice")
                                .getSelectedItem().toString()) == true ? invoice.getCategory()
                        : invoice.getCategory() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_ORDER") + ": "
                + (String.valueOf(invoice.getAssosiation() == null ? "" : invoice.getAssosiation()).equals(
                        EBISystem.builder().textField("orderText", "Invoice").getText()) == true
                ? invoice.getAssosiation()
                : invoice.getAssosiation() + "$"));

        if (invoice.getPosition() != null) {
            list.add(EBISystem.i18n("EBI_LANG_POSITION") + ": "
                    + (invoice.getPosition().equals(
                            EBISystem.builder().textField("titleText", "Invoice").getText()) == true
                    ? invoice.getPosition()
                    : invoice.getPosition() + "$"));
        }
        if (invoice.getCompanyname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_COMPANY_NAME") + ": "
                    + (invoice.getCompanyname().equals(EBISystem.builder()
                            .textField("companyNameText", "Invoice").getText()) == true ? invoice.getCompanyname()
                    : invoice.getCompanyname() + "$"));
        }
        if (invoice.getContactname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_CNAME") + ": "
                    + (invoice.getContactname().equals(
                            EBISystem.builder().textField("nameText", "Invoice").getText()) == true
                    ? invoice.getContactname()
                    : invoice.getContactname() + "$"));
        }
        if (invoice.getContactsurname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_SURNAME") + ": "
                    + (invoice.getContactsurname().equals(
                            EBISystem.builder().textField("surnameText", "Invoice").getText()) == true
                    ? invoice.getContactsurname()
                    : invoice.getContactsurname() + "$"));
        }
        if (invoice.getContactstreet() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": "
                    + (invoice.getContactstreet().equals(
                            EBISystem.builder().textField("streetNrText", "Invoice").getText()) == true
                    ? invoice.getContactstreet()
                    : invoice.getContactstreet() + "$"));
        }
        if (invoice.getContactzip() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + ": "
                    + (invoice.getContactzip().equals(
                            EBISystem.builder().textField("zipText", "Invoice").getText()) == true
                    ? invoice.getContactzip()
                    : invoice.getContactzip() + "$"));
        }
        if (invoice.getContactlocation() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + ": "
                    + (invoice.getContactlocation().equals(
                            EBISystem.builder().textField("locationText", "Invoice").getText()) == true
                    ? invoice.getContactlocation()
                    : invoice.getContactlocation() + "$"));
        }
        if (invoice.getContactpostcode() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": "
                    + (invoice.getContactpostcode().equals(
                            EBISystem.builder().textField("postCodeText", "Invoice").getText()) == true
                    ? invoice.getContactpostcode()
                    : invoice.getContactpostcode() + "$"));
        }
        if (invoice.getContactcountry() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": "
                    + (invoice.getContactcountry().equals(
                            EBISystem.builder().textField("countryText", "Invoice").getText()) == true
                    ? invoice.getContactcountry()
                    : invoice.getContactcountry() + "$"));
        }
        if (invoice.getContacttelephone() != null) {
            list.add(EBISystem.i18n("EBI_LANG_TELEPHONE") + ": "
                    + (invoice.getContacttelephone().equals(
                            EBISystem.builder().textField("telefonText", "Invoice").getText()) == true
                    ? invoice.getContacttelephone()
                    : invoice.getContacttelephone() + "$"));
        }
        if (invoice.getContactfax() != null) {
            list.add(EBISystem.i18n("EBI_LANG_FAX") + ": "
                    + (invoice.getContactfax().equals(
                            EBISystem.builder().textField("faxText", "Invoice").getText()) == true
                    ? invoice.getContactfax()
                    : invoice.getContactfax() + "$"));
        }
        if (invoice.getContactemail() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_EMAIL") + ": "
                    + (invoice.getContactemail().equals(
                            EBISystem.builder().textField("emailText", "Invoice").getText()) == true
                    ? invoice.getContactemail()
                    : invoice.getContactemail() + "$"));
        }
        if (invoice.getContactweb() != null) {
            list.add(EBISystem.i18n("EBI_LANG_INTERNET") + ": "
                    + (invoice.getContactweb().equals(
                            EBISystem.builder().textField("internetText", "Invoice").getText()) == true
                    ? invoice.getContactweb()
                    : invoice.getContactweb() + "$"));
        }
        if (invoice.getContactdescription() != null) {
            list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": "
                    + (invoice.getContactdescription()
                            .equals(EBISystem.builder().textArea("recDescription", "Invoice")
                                    .getText()) == true ? invoice.getContactdescription()
                            : invoice.getContactdescription() + "$"));
        }

        list.add(EBISystem.i18n("EBI_LANG_CREATED_DATE") + ": "
                + (EBISystem.getInstance().getDateToString(invoice.getDate())
                        .equals(EBISystem.builder().timePicker("invoiceDateText", "Invoice")
                                .getEditor().getText()) == true
                        ? EBISystem.getInstance().getDateToString(invoice.getDate())
                        : EBISystem.getInstance().getDateToString(invoice.getDate()) + "$"));
        list.add("*EOR*"); // END OF RECORD

        if (!invoice.getCrminvoicepositions().isEmpty()) {

            final Iterator iter = invoice.getCrminvoicepositions().iterator();

            while (iter.hasNext()) {
                final Crminvoiceposition obj = (Crminvoiceposition) iter.next();
                list.add(EBISystem.i18n("EBI_LANG_QUANTITY") + ": " + String.valueOf(obj.getQuantity()));
                list.add(EBISystem.i18n("EBI_LANG_PRODUCT_NUMBER") + ": " + obj.getProductnr());
                list.add(obj.getProductname() == null ? EBISystem.i18n("EBI_LANG_NAME") + ":"
                        : EBISystem.i18n("EBI_LANG_NAME") + ": " + obj.getProductname());
                list.add(obj.getCategory() == null ? EBISystem.i18n("EBI_LANG_CATEGORY") + ":"
                        : EBISystem.i18n("EBI_LANG_CATEGORY") + ": " + obj.getCategory());
                list.add(obj.getTaxtype() == null ? EBISystem.i18n("EBI_LANG_TAX") + ":"
                        : EBISystem.i18n("EBI_LANG_TAX") + ": " + obj.getTaxtype());
                list.add(String.valueOf(obj.getPretax()) == null ? EBISystem.i18n("EBI_LANG_PRICE") + ":"
                        : EBISystem.i18n("EBI_LANG_PRICE") + ": " + String.valueOf(obj.getPretax()));
                list.add(String.valueOf(obj.getDeduction()) == null ? EBISystem.i18n("EBI_LANG_DEDUCTION") + ":"
                        : EBISystem.i18n("EBI_LANG_DEDUCTION") + ": " + String.valueOf(obj.getDeduction()));
                list.add(obj.getDescription() == null ? EBISystem.i18n("EBI_LANG_DESCRIPTION") + ":"
                        : EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": " + obj.getDescription());
                list.add("*EOR*");

            }
        }

        try {
            EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(id, "Invoice", list));
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void dataShowProduct() {
        if (this.invoice.getCrminvoicepositions().size() > 0) {
            EBISystem.getModule().getInvoicePane().getTabModProduct().data = new Object[this.invoice.getCrminvoicepositions().size()][9];

            final Iterator itr = invoice.getCrminvoicepositions().iterator();
            int i = 0;

            final NumberFormat currency = NumberFormat.getCurrencyInstance();
            while (itr.hasNext()) {
                final Crminvoiceposition obj = (Crminvoiceposition) itr.next();
                
                if(obj.getPositionid() == null){
                    obj.setPositionid((i + 1) * -1);
                }
                
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][0] = String.valueOf(obj.getQuantity());
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][1] = obj.getProductnr();
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][2] = obj.getProductname() == null ? "" : obj.getProductname();
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][3] = obj.getCategory() == null ? "" : obj.getCategory();
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][4] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][5] = currency.format(obj.getNetamount() == null ? ""
                        : EBISystem.getModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction())));
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][6] = obj.getDeduction().equals("") ? "" : obj.getDeduction() + "%";
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getModule().getInvoicePane().getTabModProduct().data[i][8] = obj.getPositionid();
                i++;
            }
        } else {
            EBISystem.getModule().getInvoicePane().getTabModProduct().data = new Object[][]{
                {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }
        EBISystem.getModule().getInvoicePane().getTabModProduct().fireTableDataChanged();
    }

    public void dataDeleteProduct(final int id) {
        final Iterator iter = invoice.getCrminvoicepositions().iterator();
        while (iter.hasNext()) {

            final Crminvoiceposition invpro = (Crminvoiceposition) iter.next();

            if (invpro.getPositionid() != null && invpro.getPositionid() == id) {
                invoice.getCrminvoicepositions().remove(invpro);
                if(id > 0){
                    EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
                    EBISystem.hibernate().session("EBIINVOICE_SESSION").delete(invpro);
                    EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
                }
                break;
            }
        }
    }

    public Crminvoice getInvoice() {
        return invoice;
    }

    private String getInvoiceNamefromId(final int id) {

        String name = "";
        Query query;

        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            query = EBISystem.hibernate().session("EBIINVOICE_SESSION")
                    .createQuery("from Crminvoice where invoiceId=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Crminvoice invoice = (Crminvoice) iter.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(invoice);
                name = invoice.getName();

            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return name;
    }

    public boolean loadCompanyOrder(final int orderId) {
        Query query;
        boolean ret = false;
        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            query = EBISystem.hibernate().session("EBIINVOICE_SESSION")
                    .createQuery("from Companyorder where orderid=?1 ").setParameter(1, orderId);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Companyorder order = (Companyorder) iter.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(order);
                EBISystem.getModule().createUI(order.getCompany().getCompanyid(), false);
                ret = true;
            }
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return ret;
    }

    public boolean loadCompanyService(final int serviceId) {

        Query query;
        boolean ret = false;

        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            query = EBISystem.hibernate().session("EBIINVOICE_SESSION")
                    .createQuery("from Companyservice where serviceid=?1 ").setParameter(1, serviceId);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Companyservice service = (Companyservice) iter.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(service);
                EBISystem.getModule().createUI(service.getCompany().getCompanyid(), false);
                ret = true;
            }
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return ret;
    }

    public void calculateTotalAmount() {
        double amount = 0.0;
        double deduction = 0.0;
        double tax = 0.0;
        final Iterator iter = invoice.getCrminvoicepositions().iterator();
        while (iter.hasNext()) {
            final Crminvoiceposition inv = (Crminvoiceposition) iter.next();
            amount += inv.getNetamount() * inv.getQuantity().intValue();

            if (!"".equals(inv.getDeduction())) {
                deduction += (((inv.getNetamount() * inv.getQuantity().intValue()) * Integer.parseInt(inv.getDeduction())) / 100);
            }

            final double vtax = getTaxVal(inv.getTaxtype());
            if (vtax != 0.0) {
                final double toNet = (inv.getNetamount() * inv.getQuantity().intValue());
                final double todeduct = inv.getDeduction().equals("") ? 0.0
                        : ((toNet * Integer.parseInt(inv.getDeduction())) / 100);
                tax += (((toNet - todeduct) * vtax) / 100);

            }
        }

        amount = amount - deduction;
        deduction = deduction * (-1);

        EBISystem.builder().FormattedField("deductionText", "Invoice").setValue(deduction);
        EBISystem.builder().FormattedField("totalNetAmountText", "Invoice").setValue(amount);
        EBISystem.builder().FormattedField("taxText", "Invoice").setValue(tax);
        EBISystem.builder().FormattedField("totalGrossAmountText", "Invoice")
                .setValue(amount + tax);

    }

    private double getTaxVal(final String cat) {
        double val = 0.0;
        Query query;
        try {

            query = EBISystem.hibernate().session("EBIINVOICE_SESSION")
                    .createQuery("from Companyproducttax where name=?1 ").setParameter(1, cat);

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Companyproducttax tax = (Companyproducttax) it.next();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(tax);
                val = tax.getTaxvalue();
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return val;
    }
}
