package ebiCRM.data.control;

import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.*;
import ebiNeutrinoSDK.utils.EBIAbstractTableModel;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.text.NumberFormat;
import java.util.*;

public class EBIDataControlInvoice {

    public Crminvoice invoice = null;
    public boolean isEdit=false;


    public EBIDataControlInvoice() {
        invoice = new Crminvoice();
    }

    public boolean dataStore() {

        try {
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
            if (isEdit == false) {
            	invoice.setCreatedfrom(EBISystem.gui().vpanel("Invoice").getCreatedFrom());
                invoice.setCreateddate(new Date());
            } else {
                createHistory(invoice.getInvoiceid());
                invoice.setChangeddate(new Date());
                invoice.setChangedfrom(EBISystem.ebiUser);
            }

            // Invoice main data
            invoice.setInvoicenr(EBISystem.getCRMModule().getInvoicePane().invoiceNr);
            invoice.setBeginchar(EBISystem.getCRMModule().getInvoicePane().beginChar);
            invoice.setName(EBISystem.gui().textField("invoiceNameText", "Invoice").getText());

            if (EBISystem.gui().combo("invoiceStatusText", "Invoice").getSelectedItem() != null) {
                invoice.setStatus(EBISystem.gui().combo("invoiceStatusText", "Invoice").getSelectedItem().toString());
            }

            if (!isEdit) {
                if (EBISystem.gui().combo("categoryText", "Invoice").getSelectedItem() != null) {
                    invoice.setCategory(EBISystem.gui().combo("categoryText", "Invoice").getSelectedItem().toString());
                }
            }

            if (EBISystem.gui().timePicker("invoiceDateText", "Invoice").getDate() != null) {
                invoice.setDate(EBISystem.gui().timePicker("invoiceDateText", "Invoice").getDate());
            } else {
                invoice.setDate(new Date());
            }

            if (!"".equals(EBISystem.gui().textField("orderText", "Invoice").getText())) {
                invoice.setAssosiation(EBISystem.gui().textField("orderText", "Invoice").getText());
            }

            // Invoice rec
            if (EBISystem.gui().combo("genderText", "Invoice").getSelectedItem() != null) {
                invoice.setGender(EBISystem.gui().combo("genderText", "Invoice").getSelectedItem().toString());
            }

            invoice.setPosition(EBISystem.gui().textField("titleText", "Invoice").getText());
            invoice.setCompanyname(EBISystem.gui().textField("companyNameText", "Invoice").getText());
            invoice.setContactname(EBISystem.gui().textField("nameText", "Invoice").getText());
            invoice.setContactsurname(EBISystem.gui().textField("surnameText", "Invoice").getText());
            invoice.setContactstreet(EBISystem.gui().textField("streetNrText", "Invoice").getText());
            invoice.setContactzip(EBISystem.gui().textField("zipText", "Invoice").getText());
            invoice.setContactlocation(EBISystem.gui().textField("locationText", "Invoice").getText());
            invoice.setContactpostcode(EBISystem.gui().textField("postCodeText", "Invoice").getText());
            invoice.setContactcountry(EBISystem.gui().textField("countryText", "Invoice").getText());
            invoice.setContacttelephone(EBISystem.gui().textField("telefonText", "Invoice").getText());
            invoice.setContactfax(EBISystem.gui().textField("faxText", "Invoice").getText());
            invoice.setContactemail(EBISystem.gui().textField("emailText", "Invoice").getText());
            invoice.setContactweb(EBISystem.gui().textField("internetText", "Invoice").getText());
            invoice.setContactdescription(EBISystem.gui().textArea("recDescription", "Invoice").getText());
            EBISystem.hibernate().session("EBIINVOICE_SESSION").saveOrUpdate(invoice);

            // Position save
            if (!invoice.getCrminvoicepositions().isEmpty()) {
                final Iterator iter = invoice.getCrminvoicepositions().iterator();
                while (iter.hasNext()) {
                    final Crminvoiceposition pos = (Crminvoiceposition) iter.next();
                    if(pos.getPositionid() != null && pos.getPositionid() < 0){
                        pos.setPositionid(null);
                    }
                    pos.setCrminvoice(invoice);
                    EBISystem.hibernate().session("EBIINVOICE_SESSION").saveOrUpdate(pos);
                }
            }

            EBISystem.getInstance().getDataStore("Invoice", "ebiSave");
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();

            if (!isEdit) {
                EBISystem.gui().vpanel("Invoice").setID(invoice.getInvoiceid());
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return true;
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
                EBISystem.gui().vpanel("Invoice").setID(invoice.getInvoiceid());
                EBISystem.hibernate().session("EBIINVOICE_SESSION").refresh(invoice);

                EBISystem.gui().vpanel("Invoice").setCreatedDate(EBISystem.getInstance().getDateToString(invoice.getCreateddate() == null ? new Date() : invoice.getCreateddate()));
                EBISystem.gui().vpanel("Invoice").setCreatedFrom(invoice.getCreatedfrom() == null ? EBISystem.ebiUser : invoice.getCreatedfrom());

                if (invoice.getChangeddate() != null) {
                    EBISystem.gui().vpanel("Invoice").setChangedDate(EBISystem.getInstance().getDateToString(invoice.getChangeddate()));
                    EBISystem.gui().vpanel("Invoice").setChangedFrom(invoice.getChangedfrom());
                } else {
                    EBISystem.gui().vpanel("Invoice").setChangedDate("");
                    EBISystem.gui().vpanel("Invoice").setChangedFrom("");
                }

                EBISystem.getCRMModule().getInvoicePane().invoiceNr = invoice.getInvoicenr() == null ? 0 : invoice.getInvoicenr();
                EBISystem.getCRMModule().getInvoicePane().beginChar = invoice.getBeginchar() == null ? "" : invoice.getBeginchar();
                EBISystem.gui().textField("invoiceNrText", "Invoice").setText(invoice.getBeginchar() + invoice.getInvoicenr());
                EBISystem.gui().textField("invoiceNameText", "Invoice").setText(invoice.getName());

                if (invoice.getStatus() != null) {
                    EBISystem.gui().combo("invoiceStatusText", "Invoice").setSelectedItem(invoice.getStatus());
                }

                if (invoice.getCategory() != null) {
                    EBISystem.gui().combo("categoryText", "Invoice").setSelectedItem(invoice.getCategory());
                }

                if (invoice.getDate() != null) {
                    EBISystem.gui().timePicker("invoiceDateText", "Invoice").setDate(invoice.getDate());
                }
                if (invoice.getAssosiation() != null && !"".equals(invoice.getAssosiation())) {
                    EBISystem.gui().textField("orderText", "Invoice").setText(invoice.getAssosiation());
                    EBISystem.gui().button("selectOrder", "Invoice").setEnabled(true);
                }
                // Invoice rec

                if (invoice.getGender() != null) {
                    EBISystem.gui().combo("genderText", "Invoice").setSelectedItem(invoice.getGender());
                }

                EBISystem.gui().textField("titleText", "Invoice").setText(invoice.getPosition());
                EBISystem.gui().textField("companyNameText", "Invoice").setText(invoice.getCompanyname());
                EBISystem.gui().textField("nameText", "Invoice").setText(invoice.getContactname());
                EBISystem.gui().textField("surnameText", "Invoice").setText(invoice.getContactsurname());
                EBISystem.gui().textField("streetNrText", "Invoice").setText(invoice.getContactstreet());
                EBISystem.gui().textField("zipText", "Invoice").setText(invoice.getContactzip());
                EBISystem.gui().textField("locationText", "Invoice").setText(invoice.getContactlocation());
                EBISystem.gui().textField("postCodeText", "Invoice").setText(invoice.getContactpostcode());
                EBISystem.gui().textField("countryText", "Invoice").setText(invoice.getContactcountry());
                EBISystem.gui().textField("telefonText", "Invoice").setText(invoice.getContacttelephone());
                EBISystem.gui().textField("faxText", "Invoice").setText(invoice.getContactfax());
                EBISystem.gui().textField("emailText", "Invoice").setText(invoice.getContactemail());
                EBISystem.gui().textField("internetText", "Invoice").setText(invoice.getContactweb());
                EBISystem.gui().textArea("recDescription", "Invoice").setText(invoice.getContactdescription());

                EBISystem.getInstance().getDataStore("Invoice", "ebiEdit");
                calculateTotalAmount();

                EBISystem.gui().table("tableTotalInvoice", "Invoice")
                        .changeSelection(EBISystem.gui().table("tableTotalInvoice", "Invoice")
                                        .convertRowIndexToView(
                                                EBISystem.getCRMModule().dynMethod.getIdIndexFormArrayInATable(
                                                        ((EBIAbstractTableModel) EBISystem.gui()
                                                                .table("tableTotalInvoice", "Invoice").getModel()).data,
                                                        9, id)),
                                0, false, false);

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                        .Show(EBIMessage.INFO_MESSAGE);
            }

            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
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
        int srow = EBISystem.gui().table("tableTotalInvoice", "Invoice").getSelectedRow();
        final EBIAbstractTableModel model = (EBIAbstractTableModel) EBISystem.gui()
                .table("tableTotalInvoice", "Invoice").getModel();

        String sName = "";
        if (showID != -1) {
            if (srow > -1 && model.data.length >= srow) {
                sName = model.data[EBISystem.gui().table("tableTotalInvoice", "Invoice")
                        .convertRowIndexToModel(srow)][0].toString();
            }
        }

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
                        srow = EBISystem.gui().table("tableTotalInvoice", "Invoice").convertRowIndexToModel(i);
                    } else if (inv.getInvoiceid() == showID) {
                        srow = EBISystem.gui().table("tableTotalInvoice", "Invoice").convertRowIndexToView(i);
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
                    i++;
                }
            } else {
                model.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", ""}};
            }
            model.fireTableDataChanged();
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        EBISystem.gui().table("tableTotalInvoice", "Invoice").changeSelection(srow, 0, false, false);
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
                                        * EBISystem.getCRMModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));
                    } else {
                        taxTable.put(pos.getTaxtype(), (((pos.getNetamount() * pos.getQuantity().intValue())
                                * EBISystem.getCRMModule().dynMethod.getTaxVal(pos.getTaxtype())) / 100));

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
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return fileName;
    }

    public void dataNew() {
        invoice = new Crminvoice();
        EBISystem.gui().vpanel("Invoice").setID(-1);
        EBISystem.getCRMModule().getInvoicePane().initialize(false);
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
                + (("" + invoice.getInvoicenr()).equals(String.valueOf(EBISystem.getCRMModule().getInvoicePane().invoiceNr)) == true
                ? invoice.getInvoicenr()
                : invoice.getInvoicenr() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": "
                + (invoice.getName().equals(
                EBISystem.gui().textField("invoiceNameText", "Invoice").getText()) == true
                ? invoice.getName()
                : invoice.getName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_STATUS") + ": "
                + (invoice.getStatus()
                .equals(EBISystem.gui().combo("invoiceStatusText", "Invoice")
                        .getSelectedItem().toString()) == true ? invoice.getStatus()
                : invoice.getStatus() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_CATEGORY") + ": "
                + (invoice.getCategory()
                .equals(EBISystem.gui().combo("categoryText", "Invoice")
                        .getSelectedItem().toString()) == true ? invoice.getCategory()
                : invoice.getCategory() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_ORDER") + ": "
                + (String.valueOf(invoice.getAssosiation() == null ? "" : invoice.getAssosiation()).equals(
                EBISystem.gui().textField("orderText", "Invoice").getText()) == true
                ? invoice.getAssosiation()
                : invoice.getAssosiation() + "$"));

        if (invoice.getPosition() != null) {
            list.add(EBISystem.i18n("EBI_LANG_POSITION") + ": "
                    + (invoice.getPosition().equals(
                    EBISystem.gui().textField("titleText", "Invoice").getText()) == true
                    ? invoice.getPosition()
                    : invoice.getPosition() + "$"));
        }
        if (invoice.getCompanyname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_COMPANY_NAME") + ": "
                    + (invoice.getCompanyname().equals(EBISystem.gui()
                    .textField("companyNameText", "Invoice").getText()) == true ? invoice.getCompanyname()
                    : invoice.getCompanyname() + "$"));
        }
        if (invoice.getContactname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_CNAME") + ": "
                    + (invoice.getContactname().equals(
                    EBISystem.gui().textField("nameText", "Invoice").getText()) == true
                    ? invoice.getContactname()
                    : invoice.getContactname() + "$"));
        }
        if (invoice.getContactsurname() != null) {
            list.add(EBISystem.i18n("EBI_LANG_SURNAME") + ": "
                    + (invoice.getContactsurname().equals(
                    EBISystem.gui().textField("surnameText", "Invoice").getText()) == true
                    ? invoice.getContactsurname()
                    : invoice.getContactsurname() + "$"));
        }
        if (invoice.getContactstreet() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_STREET_NR") + ": "
                    + (invoice.getContactstreet().equals(
                    EBISystem.gui().textField("streetNrText", "Invoice").getText()) == true
                    ? invoice.getContactstreet()
                    : invoice.getContactstreet() + "$"));
        }
        if (invoice.getContactzip() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + ": "
                    + (invoice.getContactzip().equals(
                    EBISystem.gui().textField("zipText", "Invoice").getText()) == true
                    ? invoice.getContactzip()
                    : invoice.getContactzip() + "$"));
        }
        if (invoice.getContactlocation() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_ZIP_LOCATION") + ": "
                    + (invoice.getContactlocation().equals(
                    EBISystem.gui().textField("locationText", "Invoice").getText()) == true
                    ? invoice.getContactlocation()
                    : invoice.getContactlocation() + "$"));
        }
        if (invoice.getContactpostcode() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_POST_CODE") + ": "
                    + (invoice.getContactpostcode().equals(
                    EBISystem.gui().textField("postCodeText", "Invoice").getText()) == true
                    ? invoice.getContactpostcode()
                    : invoice.getContactpostcode() + "$"));
        }
        if (invoice.getContactcountry() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_COUNTRY") + ": "
                    + (invoice.getContactcountry().equals(
                    EBISystem.gui().textField("countryText", "Invoice").getText()) == true
                    ? invoice.getContactcountry()
                    : invoice.getContactcountry() + "$"));
        }
        if (invoice.getContacttelephone() != null) {
            list.add(EBISystem.i18n("EBI_LANG_TELEPHONE") + ": "
                    + (invoice.getContacttelephone().equals(
                    EBISystem.gui().textField("telefonText", "Invoice").getText()) == true
                    ? invoice.getContacttelephone()
                    : invoice.getContacttelephone() + "$"));
        }
        if (invoice.getContactfax() != null) {
            list.add(EBISystem.i18n("EBI_LANG_FAX") + ": "
                    + (invoice.getContactfax().equals(
                    EBISystem.gui().textField("faxText", "Invoice").getText()) == true
                    ? invoice.getContactfax()
                    : invoice.getContactfax() + "$"));
        }
        if (invoice.getContactemail() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_EMAIL") + ": "
                    + (invoice.getContactemail().equals(
                    EBISystem.gui().textField("emailText", "Invoice").getText()) == true
                    ? invoice.getContactemail()
                    : invoice.getContactemail() + "$"));
        }
        if (invoice.getContactweb() != null) {
            list.add(EBISystem.i18n("EBI_LANG_INTERNET") + ": "
                    + (invoice.getContactweb().equals(
                    EBISystem.gui().textField("internetText", "Invoice").getText()) == true
                    ? invoice.getContactweb()
                    : invoice.getContactweb() + "$"));
        }
        if (invoice.getContactdescription() != null) {
            list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": "
                    + (invoice.getContactdescription()
                    .equals(EBISystem.gui().textArea("recDescription", "Invoice")
                            .getText()) == true ? invoice.getContactdescription()
                    : invoice.getContactdescription() + "$"));
        }

        list.add(EBISystem.i18n("EBI_LANG_CREATED_DATE") + ": "
                + (EBISystem.getInstance().getDateToString(invoice.getDate())
                .equals(EBISystem.gui().timePicker("invoiceDateText", "Invoice")
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
            EBISystem.getCRMModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(id, "Invoice", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataShowProduct() {
        if (this.invoice.getCrminvoicepositions().size() > 0) {
            EBISystem.getCRMModule().getInvoicePane().tabModProduct.data = new Object[this.invoice.getCrminvoicepositions().size()][9];

            final Iterator itr = invoice.getCrminvoicepositions().iterator();
            int i = 0;

            final NumberFormat currency = NumberFormat.getCurrencyInstance();
            while (itr.hasNext()) {
                final Crminvoiceposition obj = (Crminvoiceposition) itr.next();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][0] = String.valueOf(obj.getQuantity());
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][1] = obj.getProductnr();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][2] = obj.getProductname() == null ? "" : obj.getProductname();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][3] = obj.getCategory() == null ? "" : obj.getCategory();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][4] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][5] = currency.format(obj.getNetamount() == null ? ""
                        : EBISystem.getCRMModule().dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction())));
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][6] = obj.getDeduction().equals("") ? "" : obj.getDeduction() + "%";
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                EBISystem.getCRMModule().getInvoicePane().tabModProduct.data[i][8] = obj.getPositionid();
                i++;
            }
        } else {
            EBISystem.getCRMModule().getInvoicePane().tabModProduct.data = new Object[][]{
                    {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
        }
        EBISystem.getCRMModule().getInvoicePane().tabModProduct.fireTableDataChanged();
    }

    public void dataDeleteProduct(final int id) {
        final Iterator iter = invoice.getCrminvoicepositions().iterator();
        while (iter.hasNext()) {

            final Crminvoiceposition invpro = (Crminvoiceposition) iter.next();

            if (invpro.getPositionid() == id) {
                EBISystem.hibernate().transaction("EBIINVOICE_SESSION").begin();
                EBISystem.hibernate().session("EBIINVOICE_SESSION").delete(invpro);
                EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
                invoice.getCrminvoicepositions().remove(invpro);
                this.dataShowProduct();
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
                EBISystem.getCRMModule().createUI(order.getCompany().getCompanyid(), false);
                ret = true;
            }
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
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
                EBISystem.getCRMModule().createUI(service.getCompany().getCompanyid(), false);
                ret = true;
            }
            EBISystem.hibernate().transaction("EBIINVOICE_SESSION").commit();
        } catch (final Exception ex) {
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

        EBISystem.gui().FormattedField("deductionText", "Invoice").setValue(deduction);
        EBISystem.gui().FormattedField("totalNetAmountText", "Invoice").setValue(amount);
        EBISystem.gui().FormattedField("taxText", "Invoice").setValue(tax);
        EBISystem.gui().FormattedField("totalGrossAmountText", "Invoice")
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
            ex.printStackTrace();
        }
        return val;
    }
}