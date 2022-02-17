package org.modules.controls;

import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.component.TaskEvent;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.interfaces.IEBIContainer;
import org.sdk.model.hibernate.Crmproject;
import org.sdk.model.hibernate.Crmprojectcost;
import org.sdk.model.hibernate.Crmprojectprop;
import org.sdk.model.hibernate.Crmprojecttask;
import org.sdk.utils.EBIAbstractTableModel;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.awt.*;
import java.text.NumberFormat;
import java.util.*;

public class ControlPlanning {

    private Crmproject project = null;
    public boolean isEdit = false;

    public ControlPlanning() {
        this.project = new Crmproject();
    }

    public Integer dataStore() {
        Integer plannID = -1;
        try {
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").begin();

            if (!isEdit) {
                project.setCreatedfrom(EBISystem.builder().vpanel("Project").getCreatedFrom());
                project.setCreateddate(new Date());
            } else {
                createHistory(project.getProjectid());
                project.setChangedfrom(EBISystem.ebiUser);
                project.setChangeddate(new Date());
            }

            project.setProjectnr(EBISystem.builder().textField("prjNrText", "Project").getText());
            project.setName(EBISystem.builder().textField("prjNameText", "Project").getText());
            project.setManager(EBISystem.builder().textField("prjManagerText", "Project").getText());

            if (EBISystem.builder().combo("prjStatusText", "Project").getEditor().getItem() != null) {
                project.setStatus(EBISystem.builder().combo("prjStatusText", "Project").getEditor().getItem().toString());
            }

            if (EBISystem.builder().timePicker("prjstartDateText", "Project").getDate() != null) {
                project.setValidfrom(EBISystem.builder().timePicker("prjstartDateText", "Project").getDate());
            }

            if (EBISystem.builder().timePicker("prjendDateText", "Project").getDate() != null) {
                project.setValidto(EBISystem.builder().timePicker("prjendDateText", "Project").getDate());
            }

            if (EBISystem.builder().FormattedField("budgetText", "Project").getValue() != null) {
                if (!"".equals(EBISystem.builder().FormattedField("budgetText", "Project").getText())) {
                    project.setBudget(Double.parseDouble(EBISystem.builder().FormattedField("budgetText", "Project").getValue().toString()));
                }
            }

            if (EBISystem.builder().FormattedField("actualCostText", "Project").getValue() != null) {
                if (!"".equals(EBISystem.builder().FormattedField("actualCostText", "Project").getText())) {
                    project.setActualcost(Double.parseDouble(EBISystem.builder().FormattedField("actualCostText", "Project").getValue().toString()));
                }
            }

            /*
			 * if(!"".equals(EBISystem.getGUIRenderer().getFormattedTextfield(
			 * "remainingCost","Project").getText())){
			 * project.setRemaincost(Double.parseDouble(EBISystem.getGUIRenderer().
			 * getFormattedTextfield("remainingCost","Project").getValue().toString())); }
             */
            if (!project.getCrmprojecttasks().isEmpty()) {
                final Iterator iter = project.getCrmprojecttasks().iterator();
                while (iter.hasNext()) {
                    final Crmprojecttask task = (Crmprojecttask) iter.next();
                    if (task.getTaskiid() != null && task.getTaskiid() < 0) {
                        task.setTaskiid(null);
                    }

                    task.setCrmproject(project);
                    EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(task);

                    if (!task.getCrmprojectcosts().isEmpty()) {
                        final Iterator citer = task.getCrmprojectcosts().iterator();
                        while (citer.hasNext()) {
                            final Crmprojectcost cost = (Crmprojectcost) citer.next();
                            if (cost.getCostid() != null && cost.getCostid() < 0) {
                                cost.setCostid(null);
                            }
                            cost.setCrmprojecttask(task);
                            //EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(cost);
                        }
                    }

                    if (!task.getCrmprojectprops().isEmpty()) {
                        final Iterator piter = task.getCrmprojectprops().iterator();
                        while (piter.hasNext()) {
                            final Crmprojectprop prop = (Crmprojectprop) piter.next();
                            if (prop.getPropertiesid() != null && prop.getPropertiesid() < 0) {
                                prop.setPropertiesid(null);
                            }
                            prop.setCrmprojecttask(task);
                            //EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(prop);
                        }
                    }
                }
            }

            EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(project);
            EBISystem.getInstance().getDataStore("Project", "ebiSave");
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").commit();

            if (!isEdit) {
                EBISystem.builder().vpanel("Project").setID(project.getProjectid());
            }

            plannID = project.getProjectid();
            isEdit = true;
        } catch (final Exception e) {
            EBISystem.hibernate().session("EBIPROJECT_SESSION").clear();
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
            return plannID;
        }
        return plannID;
    }

    public Integer dataCopy(final int id) {
        Query query;
        Integer planID = -1;
        try {

            query = EBISystem.hibernate().session("EBIPROJECT_SESSION")
                    .createQuery("from Crmproject where projectid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();
            if (iter.hasNext()) {

                final Crmproject pro = (Crmproject) iter.next();
                EBISystem.hibernate().transaction("EBIPROJECT_SESSION").begin();

                final Crmproject pnew = new Crmproject();
                pnew.setCreateddate(new Date());
                pnew.setCreatedfrom(EBISystem.ebiUser);

                pnew.setProjectnr(pro.getProjectnr());
                pnew.setName(pro.getName() + " - (Copy)");
                pnew.setManager(pro.getManager());

                pnew.setStatus(pro.getStatus());

                pnew.setValidfrom(pro.getValidfrom());
                pnew.setValidto(pro.getValidto());
                pnew.setBudget(pro.getBudget());
                pnew.setActualcost(pro.getActualcost());

                EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(pnew);

                if (!pro.getCrmprojecttasks().isEmpty()) {
                    final Iterator itt = pro.getCrmprojecttasks().iterator();
                    while (itt.hasNext()) {
                        final Crmprojecttask task = (Crmprojecttask) itt.next();

                        final Crmprojecttask ntask = new Crmprojecttask();
                        ntask.setCrmproject(pnew);
                        ntask.setDescription(task.getDescription());
                        ntask.setDone(task.getDone());
                        ntask.setDuration(task.getDuration());
                        ntask.setName(task.getName());
                        ntask.setColor(task.getColor());
                        ntask.setParentstaskid(task.getParentstaskid());
                        ntask.setStatus(task.getStatus());
                        ntask.setTaskid(task.getTaskid());
                        ntask.setType(task.getType());
                        ntask.setX(task.getX());
                        ntask.setY(task.getY());
                        pnew.getCrmprojecttasks().add(ntask);
                        EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(ntask);

                        if (!task.getCrmprojectcosts().isEmpty()) {
                            final Iterator citer = task.getCrmprojectcosts().iterator();
                            while (citer.hasNext()) {
                                final Crmprojectcost cost = (Crmprojectcost) citer.next();
                                final Crmprojectcost nc = new Crmprojectcost();
                                nc.setCrmprojecttask(ntask);
                                nc.setCreateddate(new Date());
                                nc.setCreatedfrom(EBISystem.ebiUser);
                                nc.setName(cost.getName());
                                nc.setValue(cost.getValue());
                                ntask.getCrmprojectcosts().add(nc);
                                EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(nc);
                            }
                        }

                        if (!task.getCrmprojectprops().isEmpty()) {
                            final Iterator piter = task.getCrmprojectprops().iterator();
                            while (piter.hasNext()) {
                                final Crmprojectprop prop = (Crmprojectprop) piter.next();
                                final Crmprojectprop np = new Crmprojectprop();
                                np.setCreateddate(new Date());
                                np.setCreatedfrom(EBISystem.ebiUser);
                                np.setCrmprojecttask(ntask);
                                np.setName(prop.getName());
                                np.setValue(prop.getValue());
                                ntask.getCrmprojectprops().add(np);
                                EBISystem.hibernate().session("EBIPROJECT_SESSION").saveOrUpdate(np);
                            }
                        }
                    }
                }
                EBISystem.hibernate().transaction("EBIPROJECT_SESSION").commit();
                planID = pnew.getProjectid();
            }
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return planID;
    }

    public void dataEdit(final int id) {
        Query query;
        try {

            query = EBISystem.hibernate().session("EBIPROJECT_SESSION")
                    .createQuery("from Crmproject where projectid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();
            if (iter.hasNext()) {
                project = (Crmproject) iter.next();
                EBISystem.builder().vpanel("Project").setID(project.getProjectid());
                EBISystem.hibernate().session("EBIPROJECT_SESSION").refresh(project);

                EBISystem.builder().vpanel("Project").setCreatedDate(EBISystem.getInstance().getDateToString(project.getCreateddate() == null ? new Date() : project.getCreateddate()));
                EBISystem.builder().vpanel("Project").setCreatedFrom(project.getCreatedfrom() == null ? EBISystem.ebiUser : project.getCreatedfrom());

                if (project.getChangeddate() != null) {
                    EBISystem.builder().vpanel("Project").setChangedDate(EBISystem.getInstance().getDateToString(project.getChangeddate()));
                    EBISystem.builder().vpanel("Project").setChangedFrom(project.getChangedfrom());
                }

                EBISystem.builder().textField("prjNrText", "Project").setText(project.getProjectnr());
                EBISystem.builder().textField("prjNameText", "Project").setText(project.getName());
                EBISystem.builder().textField("prjManagerText", "Project").setText(project.getManager());

                if (project.getStatus() != null) {
                    EBISystem.builder().combo("prjStatusText", "Project").setSelectedItem(project.getStatus());
                }

                if (project.getValidfrom() != null) {
                    EBISystem.builder().timePicker("prjstartDateText", "Project").getEditor().setText(EBISystem.getInstance().getDateToString(project.getValidfrom()));
                }

                if (project.getValidto() != null) {
                    EBISystem.builder().timePicker("prjendDateText", "Project").getEditor().setText(EBISystem.getInstance().getDateToString(project.getValidto()));
                }

                if (project.getValidfrom() != null) {
                    EBISystem.builder().timePicker("prjstartDateText", "Project").setDate(project.getValidfrom());
                }
                if (project.getValidto() != null) {
                    EBISystem.builder().timePicker("prjendDateText", "Project").setDate(project.getValidto());
                }

                if (project.getValidfrom() != null && project.getValidto() != null) {
                    EBISystem.getModule().getProjectPane().getGrcManagement().setStartEnd(project.getValidfrom(), project.getValidto());
                }
                if (project.getBudget() != null) {
                    EBISystem.builder().FormattedField("budgetText", "Project")
                            .setValue(project.getBudget());
                }
                if (project.getActualcost() != null) {
                    EBISystem.builder().FormattedField("actualCostText", "Project")
                            .setValue(project.getActualcost());
                }
                /*
				 * if(project.getRemaincost() != null){
				 * 
				 * EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost",
				 * "Project").setValue(project.getRemaincost()); }
                 */

                if (project.getCrmprojecttasks().size() > 0) {
                    final Iterator itk = project.getCrmprojecttasks().iterator();
                    final HashMap<Long, TaskEvent> mp = new HashMap<Long, TaskEvent>();
                    final HashMap<Long, Long> relation = new HashMap<Long, Long>();
                    while (itk.hasNext()) {
                        final Crmprojecttask ctask = (Crmprojecttask) itk.next();
                        final TaskEvent task = new TaskEvent(EBISystem.getModule().getProjectPane().getGrcManagement());
                        if (ctask.getParentstaskid() != null) {
                            relation.put(Long.parseLong(ctask.getTaskid()), Long.parseLong(ctask.getParentstaskid().split(";")[1]));
                        }
                        task.setId(Long.parseLong(ctask.getTaskid()));
                        task.setName(ctask.getName());
                        task.setLocation(ctask.getX(), ctask.getY());
                        task.setDuration(ctask.getDuration() * 20);
                        task.setDescription(ctask.getDescription());
                        task.setReached(ctask.getDone());
                        task.setStatus(ctask.getStatus() == null ? "" : ctask.getStatus());
                        task.setType(ctask.getType() == null ? "" : ctask.getType());
                        if (ctask.getColor() != null) {
                            task.setBackgroundColor(Color.decode(ctask.getColor().toUpperCase()));
                        }
                        mp.put(Long.parseLong(ctask.getTaskid()), task);
                    }

                    final Iterator rel = relation.keySet().iterator();
                    while (rel.hasNext()) {
                        final Long idr = (Long) rel.next();
                        if (mp.get(relation.get(idr)) != null) {
                            mp.get(relation.get(idr)).setParent(mp.get(idr));
                        }
                    }
                    EBISystem.getModule().getProjectPane().getGrcManagement().setAvailableTask(mp);
                }

                EBISystem.getInstance().getDataStore("Project", "ebiEdit");
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND"))
                        .Show(EBIMessage.INFO_MESSAGE);
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
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").begin();
            query = EBISystem.hibernate().session("EBIPROJECT_SESSION").createQuery("delete from Crmproject where projectid=?1 ").setParameter(1, id);
            query.executeUpdate();
            EBISystem.getInstance().getDataStore("Project", "ebiDelete");
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").commit();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataShow(Integer id) {

        Query query;
        int selRow = EBISystem.builder().table("projectTable", "Project").getSelectedRow() + id;

        try {
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").begin();
            query = EBISystem.hibernate().session("EBIPROJECT_SESSION").createQuery("from Crmproject order by createddate desc ");
            final Iterator iter = query.iterate();

            final EBIAbstractTableModel tabMod = (EBIAbstractTableModel) EBISystem.builder().table("projectTable", "Project").getModel();

            if (query.list().size() > 0) {
                tabMod.data = new Object[query.list().size()][10];
                int i = 0;

                final NumberFormat currency = NumberFormat.getCurrencyInstance();

                while (iter.hasNext()) {
                    final Crmproject pro = (Crmproject) iter.next();
                    tabMod.data[i][0] = pro.getProjectnr() == null ? "" : pro.getProjectnr();
                    tabMod.data[i][1] = pro.getName() == null ? "" : pro.getName();
                    tabMod.data[i][2] = pro.getManager() == null ? "" : pro.getManager();
                    tabMod.data[i][3] = pro.getValidfrom() == null ? "" : EBISystem.getInstance().getDateToString(pro.getValidfrom());
                    tabMod.data[i][4] = pro.getValidto() == null ? "" : EBISystem.getInstance().getDateToString(pro.getValidto());
                    tabMod.data[i][5] = pro.getStatus() == null ? "" : pro.getStatus();
                    tabMod.data[i][6] = pro.getBudget() == null ? 0.0 : currency.format(pro.getBudget());
                    tabMod.data[i][7] = pro.getActualcost() == null ? 0.0 : currency.format(pro.getActualcost());
                    tabMod.data[i][8] = "";
                    tabMod.data[i][9] = pro.getProjectid();
                    if (id != -1 && id == pro.getProjectid()) {
                        selRow = i;
                    }
                    i++;
                }
            } else {
                tabMod.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", ""}};
            }
            tabMod.fireTableDataChanged();
            EBISystem.hibernate().transaction("EBIPROJECT_SESSION").begin();
        } catch (final HibernateException e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
        if (selRow > -1) {
            selRow = EBISystem.builder().table("projectTable", "Project").convertRowIndexToView(selRow);
            EBISystem.builder().table("projectTable", "Project").changeSelection(selRow, 0, false, false);
        }
    }

    public void dataShowReport(final int id, final String name) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", id);

        EBISystem.getInstance().getIEBIReportSystemInstance().useReportSystem(map, EBISystem.getInstance().convertReportCategoryToIndex(EBISystem.i18n("EBI_LANG_PROJECT")), name);
    }

    public void dataNew() {
        project = new Crmproject();

        //todo use initialie()
        EBISystem.builder().vpanel("Project").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Project").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Project").setChangedFrom("");
        EBISystem.builder().vpanel("Project").setChangedDate("");

        EBISystem.builder().textField("prjNrText", "Project").setText("");
        EBISystem.builder().textField("prjNameText", "Project").setText("");
        EBISystem.builder().textField("prjManagerText", "Project").setText("");

        EBISystem.builder().combo("prjStatusText", "Project").setSelectedIndex(0);
        EBISystem.builder().timePicker("prjstartDateText", "Project").getEditor().setText("");
        EBISystem.builder().timePicker("prjendDateText", "Project").getEditor().setText("");
        EBISystem.builder().FormattedField("budgetText", "Project").setText("");
        EBISystem.builder().FormattedField("actualCostText", "Project").setText("");
        // EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").setText("");
        EBISystem.builder().FormattedField("budgetText", "Project").setValue(null);
        EBISystem.builder().FormattedField("actualCostText", "Project").setValue(null);
        // EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").setValue(null);
        EBISystem.getModule().getProjectPane().getGrcManagement().resetComponent();
        EBISystem.builder().vpanel("Project").setID(-1);
        EBISystem.getInstance().getDataStore("Project", "ebiNew");
    }

    public void createHistory(final int id) {
        Query query;
        try {

            query = EBISystem.hibernate().session("EBIPROJECT_SESSION").createQuery("from Crmproject where projectid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();
            if (iter.hasNext()) {
                final Crmproject proj = (Crmproject) iter.next();
                final java.util.List<String> list = new ArrayList<String>();

                list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": "
                        + EBISystem.getInstance().getDateToString(proj.getCreateddate()));
                list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + proj.getCreatedfrom());

                if (proj.getChangeddate() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": "
                            + EBISystem.getInstance().getDateToString(proj.getChangeddate()));
                    list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + proj.getChangedfrom());
                }

                list.add(
                        EBISystem.i18n("EBI_LANG_PROJECT_NR") + ": "
                        + (proj.getProjectnr().equals(EBISystem.builder()
                                .textField("prjNrText", "Project").getText()) ? proj.getProjectnr()
                        : proj.getProjectnr() + "$"));
                list.add(
                        EBISystem.i18n("EBI_LANG_NAME") + ": "
                        + (proj.getName().equals(EBISystem.builder()
                                .textField("prjNameText", "Project").getText()) ? proj.getName()
                        : proj.getName() + "$"));

                if (proj.getActualcost() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_ACTUAL_COST") + ": "
                            + (proj.getActualcost() == Double.parseDouble(EBISystem.builder()
                            .FormattedField("actualCostText", "Project").getValue().toString())
                            ? proj.getActualcost()
                            : proj.getActualcost() + "$"));
                }
                if (proj.getManager() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_PROJECT_MANAGER") + ": "
                            + (proj.getManager().equals(EBISystem.builder()
                                    .textField("prjManagerText", "Project").getText()) ? proj.getManager()
                            : proj.getManager() + "$"));
                }
                if (proj.getValidfrom() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_START_DATE") + ": " + (EBISystem.getInstance()
                            .getDateToString(proj.getValidfrom())
                            .equals(EBISystem.builder().timePicker("prjstartDateText", "Project")
                                    .getEditor().getText())
                                    ? EBISystem.getInstance().getDateToString(proj.getValidfrom())
                                    : EBISystem.getInstance().getDateToString(proj.getValidfrom())
                                    + "$"));
                }
                if (proj.getValidto() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_END_DATE") + ": " + (EBISystem.getInstance()
                            .getDateToString(proj.getValidto())
                            .equals(EBISystem.builder().timePicker("prjendDateText", "Project")
                                    .getEditor().getText())
                                    ? EBISystem.getInstance().getDateToString(proj.getValidto())
                                    : EBISystem.getInstance().getDateToString(proj.getValidto())
                                    + "$"));
                }

                list.add(EBISystem.i18n("EBI_LANG_STATUS") + ": "
                        + (proj.getStatus().equals(EBISystem.builder()
                                .combo("prjStatusText", "Project").getEditor().getItem()) ? proj.getStatus()
                        : proj.getStatus() + "$"));

                if (proj.getBudget() != null) {
                    list.add(EBISystem.i18n("EBI_LANG_PROJECT_BUDGET") + ": "
                            + (proj.getBudget() == Double.parseDouble(EBISystem.builder()
                            .FormattedField("budgetText", "Project").getValue().toString())
                            ? proj.getBudget()
                            : proj.getBudget() + "$"));
                }

                list.add("*EOR*");

                list.add(EBISystem.i18n("EBI_LANG_TASK"));

                final Iterator itr = proj.getCrmprojecttasks().iterator();
                while (itr.hasNext()) {
                    final Crmprojecttask task = (Crmprojecttask) itr.next();

                    list.add(EBISystem.i18n("EBI_LANG_NAME") + ":" + task.getName());
                    list.add(EBISystem.i18n("EBI_LANG_STATUS") + ":" + task.getStatus());
                    list.add(EBISystem.i18n("EBI_LANG_TYPE") + ":" + task.getType());
                    list.add(EBISystem.i18n("EBI_LANG_DURATION") + ":" + task.getDuration());
                    list.add(EBISystem.i18n("EBI_LANG_TASK_DONE") + ":" + task.getDone());
                    list.add(EBISystem.i18n("EBI_LANG_DESCRIPTION") + ":" + task.getDescription());
                    list.add("");
                    list.add(EBISystem.i18n("EBI_LANG_COST"));

                    final Iterator itc = task.getCrmprojectcosts().iterator();
                    while (itc.hasNext()) {
                        final Crmprojectcost cost = (Crmprojectcost) itc.next();
                        list.add(EBISystem.i18n("EBI_LANG_NAME") + ":" + cost.getName());
                        list.add(EBISystem.i18n("EBI_LANG_VALUE") + ":" + cost.getValue());
                        list.add("*EOR*");
                    }

                    list.add("");
                    list.add(EBISystem.i18n("EBI_LANG_PROPERTIES"));

                    final Iterator itp = task.getCrmprojectprops().iterator();
                    while (itp.hasNext()) {
                        final Crmprojectprop prop = (Crmprojectprop) itp.next();
                        list.add(EBISystem.i18n("EBI_LANG_NAME") + ":" + prop.getName());
                        list.add(EBISystem.i18n("EBI_LANG_VALUE") + ":" + prop.getValue());
                        list.add("*EOR*");
                    }
                    list.add("*EOR*");
                }

                EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(id, "Project", list));
            }
        } catch (final HibernateException e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (final Exception e) {
            EBIExceptionDialog.getInstance(e.getMessage(), e.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void createProduct(final int id) {
        Query query;
        try {

            query = EBISystem.hibernate().session("EBIPROJECT_SESSION")
                    .createQuery("from Crmproject where projectid=?1 ").setParameter(1, id);

            final Iterator iter = query.list().iterator();
            if (iter.hasNext()) {

                final Crmproject proj = (Crmproject) iter.next();
                EBISystem.hibernate().session("EBIPROJECT_SESSION").refresh(proj);

                if (!EBISystem.getModule().crmToolBar.isProductEnabled()) {
                    EBISystem.getModule().crmToolBar.enableToolButtonProductModule();
                    EBISystem.getModule().ebiContainer.showClosableProductContainer();
                    EBISystem.getModule().ebiContainer
                            .setSelectedTab(EBISystem.getModule().ebiContainer.getTabInstance().getTabCount() - 1);
                } else {
                    EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().dataNew();
                    EBISystem.getModule().ebiContainer
                            .setSelectedTab(((IEBIContainer) EBISystem.getModule().ebiContainer.getTabInstance())
                                    .getIndexByTitle(EBISystem.i18n("EBI_LANG_C_TAB_PRODUCT")));
                }

                EBISystem.builder().textField("ProductNrTex", "Product").setText(proj.getProjectnr());
                EBISystem.builder().textField("ProductNameText", "Product").setText(proj.getName());
                EBISystem.builder().FormattedField("productGrossText", "Product")
                        .setValue(proj.getActualcost());

            }

        } catch (final Exception ex) {
            EBIExceptionDialog.getInstance(ex.getMessage(), ex.getCause()).Show(EBIMessage.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public Crmproject getProjectBean() {
        return project;
    }
}
