package org.modules.utils;

import org.modules.EBIModule;
import org.sdk.EBISystem;
import org.sdk.model.hibernate.Ebicrmhistory;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EBICRMHistoryCreator {

    private EBICRMHistoryDataUtil field = new EBICRMHistoryDataUtil();
    private EBIModule ebiModule = null;
    private StringBuffer stringTosave = new StringBuffer();

    /**
     * copy constructor
     * @param
     */
    public EBICRMHistoryCreator(final EBIModule ebiModule) {
        this.ebiModule = ebiModule;
    }

    /**
     * Set an ArrayList of history values 
     * @param list
     * @throws Exception
     */
    public void setDataToCreate(final EBICRMHistoryDataUtil list) throws Exception {
        field = new EBICRMHistoryDataUtil();
        stringTosave = new StringBuffer();
        field = list;
        if (parseList()) {
            saveData();
        } else {
            throw new Exception("Given a bad class list!");
        }
    }

    /**
     * save history value
     */
    protected void saveData() {
        try {
            final Ebicrmhistory crmHistory = new Ebicrmhistory();
            crmHistory.setCompanyid(field.getCompanyId());
            crmHistory.setCategory(field.getCategory());
            splitStringValue();
            crmHistory.setChangedvalue(stringTosave.toString());
            crmHistory.setChangeddate(new java.util.Date());
            crmHistory.setChangedfrom(EBISystem.ebiUser);
            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(crmHistory);

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Parse the history values
     * @return true successfully parsed otherwise false
     * @throws Exception
     */
    protected boolean parseList() throws Exception {

        if (field.getCompanyId() == 0) {
            return false;
        }
        if ("".equals(field.getCategory()) || field.getCategory() == null) {
            return false;
        }

        final Iterator iter = field.getField().iterator();
        int count = 0;
        while (iter.hasNext()) {
            final String str = (String) iter.next();
            if ("".equals(str) || str == null) {
                count++;
            } else {
                break;
            }
        }

        if (count == field.getField().size()) {
            throw new Exception("Object list is empty!");
        }

        return true;
    }

    /**
     * Split the EBICRMHistoryDataUtil into a single string like (Name|Surname|etc.)
     * 
     */
    protected void splitStringValue() {

        final Iterator iter = field.getField().iterator();

        final int i = 0;

        while (iter.hasNext()) {
            final String val = (String) iter.next();

            stringTosave.append(val);
            if (i != field.getField().size()) {
                stringTosave.append("|");
            }

        }

    }

    protected List<String> getListFromString(final String str) {
        final List<String> l = new ArrayList<String>();

        final char[] array = str.toCharArray();
        String toAdd = "";

        for (int i = 0; i < array.length; i++) {

            if (array[i] == '|') {
                l.add(toAdd);
                toAdd = "";
            } else {
                toAdd += String.valueOf(array[i]);
            }

        }

        return l;
    }

    /**
     * Retrieve an history record from the database with the companyID and Category
     * (Category could be "Contact", "Address", "Order", "Offer" etc..) 
     * @param companyID
     * @param category
     * @return
     */
    public List<EBICRMHistoryDataUtil> retrieveDBHistory(final int companyID, final String category) {
        final List<EBICRMHistoryDataUtil> toReturn = new ArrayList<EBICRMHistoryDataUtil>();
        try {
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            final Query query = EBISystem.hibernate().session("EBICRM_SESSION").createQuery("from Ebicrmhistory where companyid=?1 and category=?2 ").setParameter(1, companyID).setParameter(2, category);

            final Iterator it = query.iterate();
            while (it.hasNext()) {
                final Ebicrmhistory crmHistory = (Ebicrmhistory) it.next();
                EBISystem.hibernate().session("EBICRM_SESSION").refresh(crmHistory);
                final EBICRMHistoryDataUtil util = new EBICRMHistoryDataUtil();
                util.setCompanyId(crmHistory.getCompanyid());
                util.setCategory(crmHistory.getCategory());
                util.setChangedDate(EBISystem.getInstance().getDateToString(crmHistory.getChangeddate()));
                util.setChangedFrom(crmHistory.getChangedfrom());
                util.setField(getListFromString(crmHistory.getChangedvalue()));
                toReturn.add(util);
            }

        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }


        return toReturn;
    }
}
