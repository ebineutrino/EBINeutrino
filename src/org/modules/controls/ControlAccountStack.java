package org.modules.controls;

import org.sdk.model.hibernate.Crminvoiceposition;
import org.sdk.model.hibernate.Accountstackdocs;
import org.sdk.model.hibernate.Crminvoice;
import org.sdk.model.hibernate.Companyproducttax;
import org.sdk.model.hibernate.Accountstackcd;
import org.sdk.model.hibernate.Accountstack;
import org.modules.views.EBICRMAccountStackView;
import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.gui.dialogs.EBIWinWaiting;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.JFileChooser;

public class ControlAccountStack {

    private Accountstack actStack = null;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();
    public boolean isEdit = false;

    public ControlAccountStack() {
        actStack = new Accountstack();
    }

    public Integer dataStore() {
        Integer acccoundID = -1;
        try {

            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
            if (isEdit == false) {
                actStack.setCreateddate(new Date());
                actStack.setCreatedfrom(EBISystem.gui().vpanel("Account").getCreatedFrom());
            } else {
                createHistory(actStack.getAcstackid());
                actStack.setChangeddate(new Date());
                actStack.setChangedfrom(EBISystem.ebiUser);
            }

            actStack.setAccountdate(EBISystem.gui().timePicker("dateText", "Account").getDate());
            actStack.setAccountnr(EBISystem.gui().textField("numberText", "Account").getText());

            actStack.setAccount(EBISystem.gui().combo("accountTypeText", "Account").getEditor().getItem().toString());
            actStack.setAccountname(EBISystem.gui().textField("nameText", "Account").getText());
            actStack.setAccountvalue(Double.parseDouble(EBISystem.gui().FormattedField("amountText", "Account").getValue().toString()));

            actStack.setAccountDebit(EBISystem.gui().textField("debitText", "Account").getText());
            actStack.setAccountDName(EBISystem.gui().textField("descriptionDebit", "Account").getText());
            actStack.setAccountDValue(Double.valueOf(EBISystem.gui().FormattedField("debitCal", "Account").getValue().toString()));

            actStack.setAccountCredit(EBISystem.gui().textField("creditText", "Account").getText());
            actStack.setAccountCName(EBISystem.gui().textField("descriptionCredit", "Account").getText());
            actStack.setAccountCValue(Double.valueOf(EBISystem.gui().FormattedField("creditCal", "Account").getValue().toString()));

            actStack.setAccountType(EBISystem.getModule().getAccountPane().getAccountDebitCreditType());
            actStack.setAccountTaxType(EBISystem.getModule().getAccountPane().getAccountDebitTaxName());

            actStack.setDescription(EBISystem.gui().textArea("descriptionText", "Account").getText());

            if (!actStack.getAccountstackdocses().isEmpty()) {
                final Iterator iter = actStack.getAccountstackdocses().iterator();
                while (iter.hasNext()) {
                    final Accountstackdocs docs = (Accountstackdocs) iter.next();
                    docs.setAccountstack(actStack);
                    if (docs.getAccountdocid() != null && docs.getAccountdocid() < 0) {
                        docs.setAccountdocid(null);
                    }
                    EBISystem.hibernate().session("EBIACCOUNT_SESSION").saveOrUpdate(docs);
                }
            }

            EBISystem.hibernate().session("EBIACCOUNT_SESSION").saveOrUpdate(actStack);
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
            acccoundID = actStack.getAcstackid();
            isEdit = true;
        } catch (final Exception ex) {
            EBISystem.hibernate().session("EBIACCOUNT_SESSION").clear();
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return acccoundID;
    }

    public void dataEdit(final int id) {
        Query query;
        try {

            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstack where acstackid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {

                actStack = (Accountstack) iter.next();
                EBISystem.gui().vpanel("Account").setID(id);
                EBISystem.hibernate().session("EBIACCOUNT_SESSION").refresh(actStack);

                EBISystem.gui().vpanel("Account").setCreatedDate(EBISystem.getInstance().getDateToString(actStack.getCreateddate() == null ? new Date() : actStack.getCreateddate()));
                EBISystem.gui().vpanel("Account").setCreatedFrom(actStack.getCreatedfrom() == null ? EBISystem.ebiUser : actStack.getCreatedfrom());

                if (actStack.getChangeddate() != null) {
                    EBISystem.gui().vpanel("Account").setChangedDate(EBISystem.getInstance().getDateToString(actStack.getChangeddate()));
                    EBISystem.gui().vpanel("Account").setChangedFrom(actStack.getChangedfrom());
                } else {
                    EBISystem.gui().vpanel("Account").setChangedDate("");
                    EBISystem.gui().vpanel("Account").setChangedFrom("");
                }

                EBISystem.gui().combo("accountTypeText", "Account").getEditor().setItem(actStack.getAccount());
                EBISystem.gui().timePicker("dateText", "Account").setDate(actStack.getAccountdate() == null ? new Date() : actStack.getAccountdate());
                EBISystem.gui().timePicker("dateText", "Account").getEditor().setText(EBISystem.getInstance().getDateToString(actStack.getAccountdate()));
                EBISystem.gui().textField("numberText", "Account").setText(actStack.getAccountnr());
                EBISystem.gui().textField("nameText", "Account").setText(actStack.getAccountname());
                EBISystem.gui().FormattedField("amountText", "Account").setValue(actStack.getAccountvalue() == null ? 0.0 : actStack.getAccountvalue());
                EBISystem.gui().textField("debitText", "Account").setText(actStack.getAccountDebit());
                EBISystem.gui().textField("descriptionDebit", "Account").setText(actStack.getAccountDName());
                EBISystem.gui().FormattedField("debitCal", "Account").setValue(actStack.getAccountDValue() == null ? 0.0 : actStack.getAccountDValue());

                EBISystem.gui().textField("creditText", "Account").setText(actStack.getAccountCredit());
                EBISystem.gui().textField("descriptionCredit", "Account").setText(actStack.getAccountCName());
                EBISystem.gui().FormattedField("creditCal", "Account").setValue(actStack.getAccountCValue() == null ? 0.0 : actStack.getAccountCValue());
                EBISystem.gui().textArea("descriptionText", "Account").setText(actStack.getDescription());
                EBISystem.getModule().getAccountPane().setAccountDebitCreditType(actStack.getAccountType());
                EBISystem.getModule().getAccountPane().setAccountDebitTaxName(actStack.getAccountTaxType());
                EBISystem.getInstance().getDataStore("Account", "ebiEdit");
                EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }
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
            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstack where acstackid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                final Accountstack act = (Accountstack) iter.next();
                if (act.getAcstackid() != null && act.getAcstackid() == id) {
                    try {
                        EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
                        EBISystem.getInstance().getDataStore("Account", "ebiDelete");
                        EBISystem.hibernate().session("EBIACCOUNT_SESSION").delete(act);
                        EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
                    } catch (final HibernateException e) {
                        e.printStackTrace();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void dataNew() {
        EBISystem.getModule().getAccountPane().setAccountDebitCreditType(0);
        EBISystem.getModule().getAccountPane().setAccountDebitTaxName("");
        EBISystem.getModule().getAccountPane().setShowCreditID(-1);
        EBISystem.getModule().getAccountPane().setShowDebitID(-1);
        EBISystem.gui().vpanel("Account").setID(-1);
        actStack = new Accountstack();
        EBISystem.getInstance().getDataStore("Account", "ebiNew");
        EBISystem.getModule().getAccountPane().initialize(false);
    }

    public void dataShow(final String invoiceYear, Integer id) {

        int selRow = EBISystem.gui().table("accountTable", "Account").getSelectedRow() + id;
        Query query;

        try {
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();

            if (!"".equals(invoiceYear) && !"null".equals(invoiceYear)) {
                final Calendar calendar1 = new GregorianCalendar();
                calendar1.set(Calendar.DAY_OF_MONTH, 1);
                calendar1.set(Calendar.MONTH, Calendar.JANUARY);
                calendar1.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                final Calendar calendar2 = new GregorianCalendar();
                calendar2.set(Calendar.DAY_OF_MONTH, 31);
                calendar2.set(Calendar.MONTH, Calendar.DECEMBER);
                calendar2.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION")
                        .createQuery(
                                "from Accountstack where accountdate between ?1 and ?2 order by createddate desc ");
                query.setParameter(1, calendar1.getTime());
                query.setParameter(2, calendar2.getTime());

            } else {
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION")
                        .createQuery("from Accountstack order by createddate desc");
            }

            if (query.list().size() > 0) {
                final Iterator iter = query.iterate();
                EBISystem.getModule().getAccountPane().getTabModAccount().data = new Object[query.list().size()][8];

                int i = 0;
                while (iter.hasNext()) {
                    final Accountstack act = (Accountstack) iter.next();
                    EBISystem.hibernate().session("EBIACCOUNT_SESSION").refresh(act);
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][0] = EBISystem.getInstance().getDateToString(act.getAccountdate());
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][1] = act.getAccount() == null ? "" : act.getAccount();
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][2] = act.getAccountnr() == null ? "" : act.getAccountnr();
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][3] = act.getAccountname() == null ? "" : act.getAccountname();
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][4] = currency.format(act.getAccountvalue()) == null ? "" : currency.format(act.getAccountvalue());
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][5] = act.getAccountDebit() == null ? "" : act.getAccountDebit();
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][6] = act.getAccountCredit() == null ? "" : act.getAccountCredit();
                    EBISystem.getModule().getAccountPane().getTabModAccount().data[i][7] = act.getAcstackid();
                    if (id != -1 && id == act.getAcstackid()) {
                        selRow = i;
                    }
                    i++;
                }
            } else {
                EBISystem.getModule().getAccountPane().getTabModAccount().data = new Object[][]{
                    {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
            }
            EBISystem.getModule().getAccountPane().getTabModAccount().fireTableDataChanged();
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        if (selRow > -1) {
            selRow = EBISystem.gui().table("accountTable", "Account").convertRowIndexToView(selRow);
            EBISystem.gui().table("accountTable", "Account").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataShowReport() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", 0);
        EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map,
                EBISystem.getInstance()
                        .convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_PRINT_ACCOUNT")),
                "Account");
    }

    private void createHistory(final int id) {
        final List<String> list = new ArrayList<String>();

        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                + EBISystem.getInstance().getDateToString(actStack.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + actStack.getCreatedfrom());

        if (actStack.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                    + EBISystem.getInstance().getDateToString(actStack.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + actStack.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_NUMBER") + ": "
                + (actStack.getAccountnr().equals(EBISystem.gui()
                        .combo("accountTypeText", "Account").getEditor().getItem().toString()) == true
                ? actStack.getAccount()
                : actStack.getAccount() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NUMBER") + ": "
                + (actStack.getAccountnr().equals(
                        EBISystem.gui().textField("numberText", "Account").getText()) == true
                ? actStack.getAccountnr()
                : actStack.getAccountnr() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": "
                + (actStack.getAccountname().equals(
                        EBISystem.gui().textField("nameText", "Account").getText()) == true
                ? actStack.getAccountname()
                : actStack.getAccountname() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_TOTAL_AMOUNT") + ": "
                + (actStack.getAccountvalue() == Double.parseDouble(EBISystem.gui()
                .FormattedField("amountText", "Account").getValue().toString())
                ? actStack.getAccountvalue()
                : actStack.getAccountvalue() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_DEBIT") + "1: "
                + (actStack.getAccountDebit()
                        .equals(EBISystem.gui().textField("debitText", "Account").getText())
                ? actStack.getAccountDebit()
                : actStack.getAccountDebit() + "$"));
        list.add(
                EBISystem.i18n("EBI_LANG_DEBIT") + "2: "
                + (actStack.getAccountDName().equals(EBISystem.gui()
                        .textField("descriptionDebit", "Account").getText()) ? actStack.getAccountDName()
                : actStack.getAccountDName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_DEBIT") + "3: "
                + (actStack.getAccountDValue() == Double.parseDouble(EBISystem.gui()
                .FormattedField("debitCal", "Account").getValue().toString())
                ? actStack.getAccountDValue()
                : actStack.getAccountDValue() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_CREDIT") + "1: "
                + (actStack.getAccountCredit()
                        .equals(EBISystem.gui().textField("creditText", "Account").getText())
                ? actStack.getAccountCredit()
                : actStack.getAccountCredit() + "$"));
        list.add(
                EBISystem.i18n("EBI_LANG_CREDIT") + "2: "
                + (actStack.getAccountCName().equals(EBISystem.gui()
                        .textField("descriptionCredit", "Account").getText()) ? actStack.getAccountCName()
                : actStack.getAccountCName() + "$"));
        list.add(EBISystem.i18n("EBI_LANG_CREDIT") + "3: "
                + (actStack.getAccountCValue() == Double.parseDouble(EBISystem.gui()
                .FormattedField("creditCal", "Account").getValue().toString())
                ? actStack.getAccountCValue()
                : actStack.getAccountCValue() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ": "
                + (actStack.getDescription()
                        .equals(EBISystem.gui().textArea("descriptionText", "Account").getText())
                ? actStack.getDescription()
                : actStack.getDescription() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_CREATED_DATE") + ": "
                + (EBISystem.getInstance().getDateToString(actStack.getAccountdate())
                        .equals(EBISystem.gui().timePicker("dateText", "Account").getEditor()
                                .getText()) == true
                        ? EBISystem.getInstance().getDateToString(actStack.getAccountdate())
                        : EBISystem.getInstance().getDateToString(actStack.getAccountdate())
                        + "$"));
        list.add("*EOR*"); // END OF RECORD

        try {
            EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(id, "Account", list));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.actStack.getAccountstackdocses().iterator();
        while (iter.hasNext()) {
            final Accountstackdocs doc = (Accountstackdocs) iter.next();
            if (doc.getAccountdocid() != null && doc.getAccountdocid() == id) {
                this.actStack.getAccountstackdocses().remove(doc);
                if(id > 0){
                    EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
                    EBISystem.hibernate().session("EBIACCOUNT_SESSION").delete(doc);
                    EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
                }
                break;
            }
        }
    }

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Accountstackdocs docs = new Accountstackdocs();
                docs.setAccountdocid((actStack.getAccountstackdocses().size() + 1) * -1);
                docs.setAccountstack(actStack);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);
                actStack.getAccountstackdocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void dataViewDoc(final int id) {
        final Iterator iter = this.actStack.getAccountstackdocses().iterator();
        while (iter.hasNext()) {
            final Accountstackdocs doc = (Accountstackdocs) iter.next();
            if (doc.getAccountdocid() != null && id == doc.getAccountdocid()) {
                // Get the BLOB inputstream
                final String file = doc.getName().replaceAll(" ", "_");
                final byte buffer[] = doc.getFiles();
                EBISystem.getInstance().writeBlobToTmp(file, buffer);
                break;
            }
        }
    }

    public void dataShowDoc() {
        if (this.actStack.getAccountstackdocses().size() > 0) {
            EBISystem.getModule().getAccountPane().getTabModDoc().data = new Object[this.actStack.getAccountstackdocses().size()][4];
            final Iterator itr = this.actStack.getAccountstackdocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Accountstackdocs obj = (Accountstackdocs) itr.next();
                
                if(obj.getAccountdocid() == null){
                    obj.setAccountdocid(( i + 1 ) * -1);
                }
                
                EBISystem.getModule().getAccountPane().getTabModDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getAccountPane().getTabModDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getAccountPane().getTabModDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getAccountPane().getTabModDoc().data[i][3] = obj.getAccountdocid();
                i++;
            }
        } else {
            EBISystem.getModule().getAccountPane().getTabModDoc().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getAccountPane().getTabModDoc().fireTableDataChanged();
    }

    public void dataStoreCreditDebit(final boolean isEdit, final int id) {

        try {
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
            Query query;
            Accountstackcd actcd = null;

            if (isEdit) {
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where accountstackcdid=?1").setParameter(1, id);
                final Iterator it = query.iterate();
                if (it.hasNext()) {
                    actcd = (Accountstackcd) it.next();
                }
            } else {
                if (checkifRecordExist(
                        EBISystem.gui().textField("numberText", "creditDebitDialog").getText(),
                        EBISystem.gui().textField("nameText", "creditDebitDialog").getText())) {
                    return;
                }
                actcd = new Accountstackcd();
            }
            actcd.setCreateddate(new Date());
            actcd.setCreatedfrom(EBISystem.ebiUser);
            actcd.setCreditdebitnumber(EBISystem.gui().textField("numberText", "creditDebitDialog").getText());
            actcd.setCreditdebitname(EBISystem.gui().textField("nameText", "creditDebitDialog").getText());
            actcd.setCreditdebittaxtname(EBISystem.gui().combo("taxTypeText", "creditDebitDialog").getSelectedItem().toString());

            if (EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").getSelectedIndex() == 3) {
                actcd.setCreditdebitvalue(Double.parseDouble(EBISystem.gui().FormattedField("valueText", "creditDebitDialog").getValue() == null ? "0.0"
                        : EBISystem.gui().FormattedField("valueText", "creditDebitDialog").getValue().toString()));
            } else {
                actcd.setCreditdebitvalue(Double.parseDouble(EBISystem.gui()
                        .label("taxValue", "creditDebitDialog").getText().replace("%", "")));
            }

            actcd.setCreditdebittype(EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").getSelectedIndex());
            EBISystem.hibernate().session("EBIACCOUNT_SESSION").saveOrUpdate(actcd);
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
            dataShowCreditDebit();
            dataNewCreditDebit();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public void dataEditCreditDebit(final int id) {
        try {

            final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where accountstackcdid=?1").setParameter(1, id);

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Accountstackcd actcd = (Accountstackcd) it.next();
                EBISystem.gui().textField("numberText", "creditDebitDialog").setText(actcd.getCreditdebitnumber());
                EBISystem.gui().textField("nameText", "creditDebitDialog").setText(actcd.getCreditdebitname());
                if (actcd.getCreditdebittype() == 3) {
                    EBISystem.gui().FormattedField("valueText", "creditDebitDialog").setValue(actcd.getCreditdebitvalue());
                } else {
                    EBISystem.gui().combo("taxTypeText", "creditDebitDialog").setSelectedItem(actcd.getCreditdebittaxtname());
                }
                EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").setSelectedIndex(actcd.getCreditdebittype());

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataDeleteCreditDebit(final int id) {
        try {

            final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where accountstackcdid=?1").setParameter(1, id);

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Accountstackcd actcd = (Accountstackcd) it.next();
                EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
                EBISystem.hibernate().session("EBIACCOUNT_SESSION").delete(actcd);
                EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNewCreditDebit() {
        EBISystem.gui().textField("numberText", "creditDebitDialog").setText("");
        EBISystem.gui().textField("nameText", "creditDebitDialog").setText("");
        EBISystem.gui().combo("taxTypeText", "creditDebitDialog").setSelectedIndex(0);
        EBISystem.gui().label("taxValue", "creditDebitDialog").setText("0.0%");
        EBISystem.gui().combo("creditDebitTypeText", "creditDebitDialog").setSelectedIndex(0);
    }

    private String getAccountNamefromId(final int id) {

        String name = "";
        Query query;

        try {
            EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();
            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION")
                    .createQuery("from Accountstack where acstackid=?1 ").setParameter(1, id);

            final Iterator iter = query.iterate();

            if (iter.hasNext()) {
                final Accountstack act = (Accountstack) iter.next();
                EBISystem.hibernate().session("EBIACCOUNT_SESSION").refresh(act);
                name = act.getAccountname();

            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return name;
    }

    public double getCreditDebitValue(final String cdnr, final int type) {
        Query query;
        double toRet = 0.0;
        try {

            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebitnumber=?1 and creditdebittype=?2");

            query.setParameter(1, cdnr);
            query.setParameter(2, cdnr);

            if (query.list().size() > 0) {
                final Iterator iterx = query.iterate();
                if (iterx.hasNext()) {
                    final Accountstackcd cd = (Accountstackcd) iterx.next();
                    toRet = cd.getCreditdebitvalue();
                }
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return toRet;
    }

    public int getIDFromNumber(final String nr, final boolean isCredit) {

        int toReturn = -1;
        try {

            Query query;
            if (isCredit) {
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebitnumber=?1 and creditdebittype > 1").setParameter(1, nr);

            } else {
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebitnumber=?1 and creditdebittype = 1").setParameter(1, nr);
            }

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Accountstackcd actcd = (Accountstackcd) it.next();
                toReturn = actcd.getAccountstackcdid();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public void storeActualCredit(final String acnr, final double crdValue) {

        try {
            final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebitnumber=?1").setParameter(1, acnr);

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Accountstackcd actcd = (Accountstackcd) it.next();
                actcd.setCreditdebitvalue(crdValue);
                EBISystem.hibernate().session("EBIACCOUNT_SESSION").saveOrUpdate(actcd);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dataCalculateTax(final int idCreditDebit) {
        try {
            final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION")
                    .createQuery("from Accountstackcd where accountstackcdid=?1").setParameter(1, idCreditDebit);

            final Iterator it = query.iterate();
            if (it.hasNext()) {
                final Accountstackcd actcd = (Accountstackcd) it.next();
                double val = 0.0;
                if (EBISystem.gui().FormattedField("amountText", "Account")
                        .getText() != null) {
                    try {
                        val = Double.parseDouble(EBISystem.gui()
                                .FormattedField("amountText", "Account").getText().toString());
                    } catch (final NumberFormatException ex) {
                        val = 0.0;
                    }
                }

                double tax = 0.0;
                double debitValue = 0.0;
                double creditValue = 0.0;

                double dVal = 0.0;
                if (actcd.getCreditdebittype() != EBICRMAccountStackView.DEBIT) {
                    try {
                        dVal = Double.parseDouble(EBISystem.gui()
                                .FormattedField("debitCal", "Account").getText());
                    } catch (final NumberFormatException ex) {
                        dVal = 0.0;
                    }
                } else {
                    tax = (actcd.getCreditdebitvalue() * val) / 100;
                }

                if (EBISystem.gui().combo("accountTypeText", "Account").getSelectedItem().toString().equals(EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_ACTIVE"))) {
                    debitValue = (val + tax);
                    creditValue = (dVal * -1);

                } else if (EBISystem.gui().combo("accountTypeText", "Account").getSelectedItem().toString()
                        .equals(EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_PASSIVE"))) {

                    debitValue = ((val + tax) * -1);
                    creditValue = dVal;

                } else if (EBISystem.gui().combo("accountTypeText", "Account").getSelectedItem().toString().equals(EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_ACCOUNT_EARNING"))) {

                    debitValue = (val + tax);
                    creditValue = (dVal * -1);

                } else if (EBISystem.gui().combo("accountTypeText", "Account").getSelectedItem().toString().equals(EBISystem.i18n("EBI_LANG_DEBIT_CREDIT_LIST_EXPEDITURE"))) {

                    debitValue = ((val + tax) * -1);
                    creditValue = dVal;
                }

                EBISystem.getModule().getAccountPane().setAccountDebitCreditType(actcd.getCreditdebittype());
                EBISystem.getModule().getAccountPane().setAccountDebitTaxName(actcd.getCreditdebittaxtname());

                if (actcd.getCreditdebittype() == EBICRMAccountStackView.DEBIT) {

                    EBISystem.getModule().getAccountPane().setShowDebitID(actcd.getAccountstackcdid());
                    EBISystem.gui().textField("debitText", "Account").setText(actcd.getCreditdebitnumber());
                    EBISystem.gui().textField("descriptionDebit", "Account").setText(actcd.getCreditdebitname());
                    EBISystem.gui().FormattedField("debitCal", "Account").setValue(debitValue);

                } else {
                    EBISystem.getModule().getAccountPane().setShowCreditID(actcd.getAccountstackcdid());
                    EBISystem.gui().textField("creditText", "Account").setText(actcd.getCreditdebitnumber());
                    EBISystem.gui().textField("descriptionCredit", "Account").setText(actcd.getCreditdebitname());
                    EBISystem.gui().FormattedField("creditCal", "Account").setValue(creditValue);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkifRecordExist(final String nr, final String name) {
        boolean exist = false;
        try {
            final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebitnumber=?1 or creditdebitname=?2  ");
            query.setParameter(1, nr);
            query.setParameter(2, name);

            if (query.list().size() > 0) {
                exist = true;
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SAME_RECORD_EXSIST")).Show(EBIMessage.ERROR_MESSAGE);
            }

        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
            exist = true;
        }

        return exist;
    }

    public void dataShowCreditDebitExt(final int type) {
        Query query;
        try {
            // 0 show everythings
            if (type == 0) {
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd ");
            } else {
                // show selected type
                query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd where creditdebittype=?1").setParameter(1, type);
            }

            if (query.list().size() > 0) {
                EBISystem.getModule().getAccountPane().getCreditDebitMod().data = new Object[query.list().size()][3];
                final Iterator it = query.iterate();
                int i = 0;
                while (it.hasNext()) {
                    final Accountstackcd acCD = (Accountstackcd) it.next();
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][0] = acCD.getCreditdebitnumber() == null ? "" : acCD.getCreditdebitnumber();
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][1] = acCD.getCreditdebitname() == null ? "" : acCD.getCreditdebitname();
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][2] = acCD.getAccountstackcdid();
                    i++;
                }
            } else {
                EBISystem.getModule().getAccountPane().getCreditDebitMod().data = new Object[][]{
                    {EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
            }
            EBISystem.getModule().getAccountPane().getCreditDebitMod().fireTableDataChanged();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * fill dialog debit credit
     */
    public void dataShowCreditDebit() {
        Query query;
        try {
            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstackcd");

            if (query.list().size() > 0) {
                EBISystem.getModule().getAccountPane().getCreditDebitMod().data = new Object[query.list().size()][3];
                final Iterator it = query.iterate();
                int i = 0;
                while (it.hasNext()) {
                    final Accountstackcd acCD = (Accountstackcd) it.next();
                    
                    if(acCD.getAccountstackcdid() == null){
                        acCD.setAccountstackcdid((i + 1) * -1);
                    }
                    
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][0] = acCD.getCreditdebitnumber() == null ? "" : acCD.getCreditdebitnumber();
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][1] = acCD.getCreditdebitname() == null ? "" : acCD.getCreditdebitname();
                    EBISystem.getModule().getAccountPane().getCreditDebitMod().data[i][2] = acCD.getAccountstackcdid();
                    i++;
                }
            } else {
                EBISystem.getModule().getAccountPane().getCreditDebitMod().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
            }
            EBISystem.getModule().getAccountPane().getCreditDebitMod().fireTableDataChanged();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public double getTaxValue(final String value) {

        double ret = 0.0;
        Query query;

        try {
            query = EBISystem.hibernate().session("EBIACCOUNT_SESSION")
                    .createQuery("from Companyproducttax where name=?1 ").setParameter(1, value);

            final Iterator it = query.iterate();

            if (it.hasNext()) {
                final Companyproducttax tax = (Companyproducttax) it.next();
                ret = tax.getTaxvalue();
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void imporInvoicetoAccout(final String invoiceYear) {

        final Runnable run = new Runnable() {

            @Override
            public void run() {
                EBIWinWaiting wait = null;
                try {
                    wait = new EBIWinWaiting(EBISystem.i18n("EBI_LANG_LOAD_IMPORT_DATA"));
                    wait.setVisible(true);

                    final Calendar calendar1 = new GregorianCalendar();
                    calendar1.set(Calendar.DAY_OF_MONTH, 1);
                    calendar1.set(Calendar.MONTH, Calendar.JANUARY);
                    calendar1.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                    final Calendar calendar2 = new GregorianCalendar();
                    calendar2.set(Calendar.DAY_OF_MONTH, 31);
                    calendar2.set(Calendar.MONTH, Calendar.DECEMBER);
                    calendar2.set(Calendar.YEAR, Integer.parseInt(invoiceYear));

                    EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").begin();

                    final Query query = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Crminvoice where date between ?1 and ?2 ");
                    query.setParameter(1, calendar1.getTime());
                    query.setParameter(2, calendar2.getTime());

                    final Iterator itx = query.iterate();
                    while (itx.hasNext()) {
                        final Crminvoice inv = (Crminvoice) itx.next();
                        final Iterator ivnIter = inv.getCrminvoicepositions().iterator();

                        while (ivnIter.hasNext()) {

                            final Crminvoiceposition pox = (Crminvoiceposition) ivnIter.next();
                            final String inpNr = "INV" + inv.getBeginchar() + inv.getInvoicenr() + pox.getPositionid();

                            final Query query1 = EBISystem.hibernate().session("EBIACCOUNT_SESSION").createQuery("from Accountstack where accountnr=?1");
                            query1.setParameter(1, inpNr);

                            Accountstack stck;
                            if (query1.list().size() <= 0) {

                                stck = new Accountstack();
                                stck.setCreateddate(new Date());
                                stck.setCreatedfrom(EBISystem.ebiUser);

                                stck.setAccountdate(inv.getDate());
                                stck.setAccountnr(inpNr);
                                stck.setAccountname(inv.getName());
                                final Double amount = (pox.getNetamount().doubleValue() * pox.getQuantity().intValue());
                                stck.setAccountvalue(amount);

                                stck.setAccountDebit("115");
                                stck.setAccountDName(EBISystem.i18n("EBI_LANG_CREDIT_FROM_INVOICE"));
                                final double txy = ((amount * getTaxValue(pox.getTaxtype())) / 100);
                                stck.setAccountDValue(txy);

                                stck.setAccountCredit("1000");
                                stck.setAccountCName("Bank 1");
                                final double nValx = getCreditDebitValue("1000", 3) + (amount + txy);
                                stck.setAccountCValue(nValx);
                                storeActualCredit("1000", nValx);
                                stck.setAccountType(2);
                                stck.setDescription(inv.getContactdescription());
                                EBISystem.hibernate().session("EBIACCOUNT_SESSION").saveOrUpdate(stck);
                            }
                        }
                    }

                    EBISystem.hibernate().transaction("EBIACCOUNT_SESSION").commit();

                } catch (final Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (wait != null) {
                        wait.setVisible(false);
                    }
                }
                dataShow(invoiceYear, -1);
            }
        };
        final Thread impThr = new Thread(run, "Import Invoice");
        impThr.start();
    }
}
