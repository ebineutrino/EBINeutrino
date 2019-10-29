package org.modules.controls;

import org.modules.utils.EBICRMHistoryDataUtil;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Companyactivities;
import org.sdk.model.hibernate.Companyactivitiesdocs;
import org.hibernate.HibernateException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;

public class ControlActivity {

    private Companyactivities companyActivity = null;
    public boolean isEdit = false;

    public ControlActivity() {
        companyActivity = new Companyactivities();
    }

    public Integer dataStore() {
        
        Integer activityID = -1;
        try {

            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            if (isEdit == true) {
                createHistory(EBISystem.getInstance().getCompany());
                companyActivity.setChangeddate(new Date());
                companyActivity.setChangedfrom(EBISystem.ebiUser);
            } else {
                companyActivity.setCreatedfrom(EBISystem.gui().vpanel("Activity").getCreatedFrom());
                companyActivity.setCreateddate(new Date());
                companyActivity.setCompany(EBISystem.getInstance().getCompany());
            }

            companyActivity.setActivityname(EBISystem.gui().textField("activityNameText", "Activity").getText());
            if (EBISystem.gui().combo("activityTypeText", "Activity").getSelectedItem() != null) {
                companyActivity.setActivitytype(EBISystem.gui().combo("activityTypeText", "Activity").getSelectedItem().toString());
            }
            companyActivity.setTimerdisabled(EBISystem.gui().getCheckBox("timerActiveBox", "Activity").isSelected() ? 1 : 0);

            int tstart = 0;
            if (EBISystem.gui().combo("timerStartText", "Activity").getSelectedItem() != null) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("timerStartText", "Activity").getSelectedItem().toString())) {
                    try {
                        tstart = Integer.parseInt(EBISystem.gui().combo("timerStartText", "Activity").getSelectedItem().toString().split(" ")[0]);
                    } catch (final NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            companyActivity.setTimerstart(tstart);
            if (EBISystem.gui().combo("activityStatusText", "Activity").getSelectedItem() != null) {
                if (EBISystem.gui().combo("activityStatusText", "Activity").getSelectedIndex() != 0) {
                    companyActivity.setActivitystatus(EBISystem.gui().combo("activityStatusText", "Activity").getSelectedItem().toString());
                }
            }

            if (EBISystem.gui().timePicker("activityTODOText", "Activity").getDate() != null) {
                final Calendar eDate = new GregorianCalendar();
                eDate.setTime(EBISystem.gui().timePicker("activityTODOText", "Activity").getDate());
                eDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(EBISystem.gui().getSpinner("dueH", "Activity").getValue().toString()));
                eDate.set(Calendar.MINUTE, Integer.parseInt(EBISystem.gui().getSpinner("dueMin", "Activity").getValue().toString()));
                eDate.set(Calendar.SECOND, 0);
                eDate.set(Calendar.MILLISECOND, 0);
                companyActivity.setDuedate(eDate.getTime());
            }
            companyActivity.setDuration(Integer.parseInt(EBISystem.gui().textField("durationText", "Activity").getText()));
            final StringBuffer color = new StringBuffer();
            color.append(EBISystem.gui().getPanel("colorPanel", "Activity").getBackground().getRed());
            color.append(",");
            color.append(EBISystem.gui().getPanel("colorPanel", "Activity").getBackground().getGreen());
            color.append(",");
            color.append(EBISystem.gui().getPanel("colorPanel", "Activity").getBackground().getBlue());

            companyActivity.setAcolor(color.toString());
            companyActivity.setActivitydescription(EBISystem.gui().textArea("activityDescription", "Activity").getText());

            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(companyActivity);

            if (!companyActivity.getCompanyactivitiesdocses().isEmpty()) {
                final Iterator iter = companyActivity.getCompanyactivitiesdocses().iterator();
                while (iter.hasNext()) {
                    final Companyactivitiesdocs docs = (Companyactivitiesdocs) iter.next();
                    if (docs.getActivitydocid() != null && docs.getActivitydocid() < 0) {
                        docs.setActivitydocid(null);
                    }
                    docs.setCompanyactivities(companyActivity);
                    EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(docs);
                }
            }

            EBISystem.getInstance().getDataStore("Activity", "ebiSave");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
            EBISystem.getModule().allertTimer.setUpAvailableTimer();

            EBISystem.getInstance().getCompany().getCompanyactivitieses().add(companyActivity);
            
            if (!isEdit) {
                EBISystem.gui().vpanel("Activity").setID(companyActivity.getActivityid());
            }
            activityID = companyActivity.getActivityid();
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return activityID;
    }

    public Integer dataCopy(final int id) {

        Integer activityID = -1;
        
        try {
            if (EBISystem.getInstance().getCompany().getCompanyactivitieses().size() > 0) {
                Companyactivities compActivity = null;
                for (Companyactivities actObj : EBISystem.getInstance().getCompany().getCompanyactivitieses()) {
                    if (actObj.getActivityid() == id) {
                        compActivity = actObj;
                        break;
                    }
                }

                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                final Companyactivities compAct = new Companyactivities();
                compAct.setCreateddate(new Date());
                compAct.setCreatedfrom(EBISystem.ebiUser);

                compAct.setCompany(compActivity.getCompany());
                compAct.setActivityname(compActivity.getActivityname() + " - (Copy)");
                compAct.setActivitytype(compActivity.getActivitytype());

                compAct.setTimerdisabled(compActivity.getTimerdisabled());
                compAct.setTimerstart(compActivity.getTimerstart());
                compAct.setActivitystatus(compActivity.getActivitystatus());
                compAct.setDuedate(compActivity.getDuedate());
                compAct.setDuration(compActivity.getDuration());

                compAct.setAcolor(compActivity.getAcolor());
                compAct.setActivitydescription(compActivity.getActivitydescription());

                EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(compAct);

                if (!compActivity.getCompanyactivitiesdocses().isEmpty()) {
                    final Iterator itd = compActivity.getCompanyactivitiesdocses().iterator();
                    while (itd.hasNext()) {
                        final Companyactivitiesdocs docs = (Companyactivitiesdocs) itd.next();
                        final Companyactivitiesdocs cd = new Companyactivitiesdocs();
                        cd.setCompanyactivities(compAct);
                        cd.setCreateddate(new Date());
                        cd.setCreatedfrom(EBISystem.ebiUser);
                        cd.setFiles(docs.getFiles());
                        cd.setName(docs.getName());
                        compAct.getCompanyactivitiesdocses().add(cd);
                        EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(cd);
                    }
                }
                
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                
                EBISystem.getInstance().getCompany().getCompanyactivitieses().add(compAct);
                activityID = compAct.getActivityid();
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return activityID;
    }

    public void dataEdit(final int id) {
        if (EBISystem.getInstance().getCompany().getCompanyactivitieses().size() > 0) {

            for (Companyactivities actObj : EBISystem.getInstance().getCompany().getCompanyactivitieses()) {
                if (actObj.getActivityid() == id) {
                    companyActivity = actObj;
                    break;
                }
            }

            EBISystem.gui().vpanel("Activity").setID(companyActivity.getActivityid());
            EBISystem.gui().vpanel("Activity").setCreatedDate(EBISystem.getInstance().getDateToString(companyActivity.getCreateddate() == null ? new Date() : companyActivity.getCreateddate()));

            EBISystem.gui().vpanel("Activity").setCreatedFrom(companyActivity.getCreatedfrom() == null ? EBISystem.ebiUser : companyActivity.getCreatedfrom());

            if (companyActivity.getChangeddate() != null) {
                EBISystem.gui().vpanel("Activity").setChangedDate(EBISystem.getInstance().getDateToString(companyActivity.getChangeddate()));
                EBISystem.gui().vpanel("Activity").setChangedFrom(companyActivity.getChangedfrom());
            } else {
                EBISystem.gui().vpanel("Activity").setChangedDate("");
                EBISystem.gui().vpanel("Activity").setChangedFrom("");
            }

            EBISystem.gui().textField("activityNameText", "Activity").setText(companyActivity.getActivityname());
            if (companyActivity.getDuedate() != null) {
                EBISystem.gui().timePicker("activityTODOText", "Activity").setDate(companyActivity.getDuedate());
                EBISystem.gui().timePicker("activityTODOText", "Activity").getEditor().setText(EBISystem.getInstance().getDateToString(companyActivity.getDuedate()));
            }

            if (companyActivity.getTimerdisabled() != null) {
                EBISystem.gui().getCheckBox("timerActiveBox", "Activity").setSelected(companyActivity.getTimerdisabled() == 1 ? true : false);
            }

            if (companyActivity.getTimerstart() != null) {
                EBISystem.gui().combo("timerStartText", "Activity").setSelectedItem(companyActivity.getTimerstart() + " min");
            }

            EBISystem.gui().textField("durationText", "Activity").setText(String.valueOf(companyActivity.getDuration() == null ? 0 : companyActivity.getDuration()));

            int r;
            int g;
            int b;
            if (companyActivity.getAcolor() != null) {
                final String[] splCol = companyActivity.getAcolor().split(",");
                r = Integer.parseInt(splCol[0]);
                g = Integer.parseInt(splCol[1]);
                b = Integer.parseInt(splCol[2]);

                EBISystem.gui().getPanel("colorPanel", "Activity").setBackground(new Color(r, g, b));
            } else {
                EBISystem.gui().getPanel("colorPanel", "Activity").setBackground(Color.gray);
            }

            final GregorianCalendar startDate = new GregorianCalendar();
            startDate.setTime(companyActivity.getDuedate() == null ? new Date() : companyActivity.getDuedate());
            startDate.set(Calendar.SECOND, 0);
            startDate.set(Calendar.MILLISECOND, 0);

            EBISystem.gui().getSpinner("dueH", "Activity").setValue(startDate.get(Calendar.HOUR_OF_DAY));
            EBISystem.gui().getSpinner("dueMin", "Activity").setValue(startDate.get(Calendar.MINUTE));

            if (companyActivity.getActivitytype() != null) {
                EBISystem.gui().combo("activityTypeText", "Activity").setSelectedItem(companyActivity.getActivitytype());
            }

            if (companyActivity.getActivitystatus() != null) {
                EBISystem.gui().combo("activityStatusText", "Activity").setSelectedItem(companyActivity.getActivitystatus());
            }

            EBISystem.gui().textArea("activityDescription", "Activity").setText(companyActivity.getActivitydescription());
            EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
            EBISystem.hibernate().session("EBICRM_SESSION").saveOrUpdate(companyActivity);
            EBISystem.getInstance().getDataStore("Activity", "ebiEdit");
            EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataDelete(final int id) {
        try {
            if (EBISystem.getInstance().getCompany().getCompanyactivitieses().size() > 0) {
                for (Companyactivities actObj : EBISystem.getInstance().getCompany().getCompanyactivitieses()) {
                    if (actObj.getActivityid() == id) {
                        companyActivity = actObj;
                        break;
                    }
                }
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(companyActivity);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                EBISystem.getInstance().getCompany().getCompanyactivitieses().remove(companyActivity);
                EBISystem.getInstance().getDataStore("Activity", "ebiDelete");
            }
        } catch (final HibernateException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void dataShow(Integer id) {

        try {

            int srow = EBISystem.gui().table("tableActivity", "Activity").getSelectedRow() + id;
            final int size = EBISystem.getInstance().getCompany().getCompanyactivitieses().size();

            if (size > 0) {
                EBISystem.getModule().getActivitiesPane().getTabModel().data = new Object[size][8];
                int i = 0;
                final Iterator<Companyactivities> iterAct = EBISystem.getInstance().getCompany().getCompanyactivitieses().iterator();
                while (iterAct.hasNext()) {
                    final Companyactivities act = iterAct.next();

                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][0] = act.getActivityname();
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][1] = act.getActivitytype();

                    final GregorianCalendar startDate = new GregorianCalendar();
                    startDate.setTime(act.getDuedate());
                    startDate.set(Calendar.SECOND, 0);
                    startDate.set(Calendar.MILLISECOND, 0);

                    String min;
                    if (startDate.get(Calendar.MINUTE) < 10) {
                        min = startDate.get(Calendar.MINUTE) + "0";
                    } else {
                        min = startDate.get(Calendar.MINUTE) + "";
                    }

                    String hour;
                    if (startDate.get(Calendar.HOUR_OF_DAY) < 10) {
                        hour = "0" + startDate.get(Calendar.HOUR_OF_DAY);
                    } else {
                        hour = "" + startDate.get(Calendar.HOUR_OF_DAY);
                    }

                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][2] = act.getDuedate() == null ? "" : (EBISystem.getInstance().getDateToString(act.getDuedate()) + " " + hour + ":" + min);
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][3] = act.getDuration() == null ? "" : act.getDuration();
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][4] = act.getAcolor() == null ? "" : act.getAcolor();
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][5] = act.getActivitystatus() == null ? "" : act.getActivitystatus();
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][6] = act.getActivitydescription() == null ? "" : act.getActivitydescription();
                    EBISystem.getModule().getActivitiesPane().getTabModel().data[i][7] = act.getActivityid();
                    if(id != -1 && id == act.getActivityid()){
                       srow = i;
                    }
                    i++;
                }
            } else {
                EBISystem.getModule().getActivitiesPane().getTabModel().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
            }

            EBISystem.getModule().getActivitiesPane().getTabModel().fireTableDataChanged();
            
            if(srow > -1){
                srow = EBISystem.gui().table("tableActivity", "Activity").convertRowIndexToView(srow);
                EBISystem.gui().table("tableActivity", "Activity").changeSelection(srow, 0, false, false);
            }
            
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void createHistory(final Company com) throws Exception {
        final List<String> list = new ArrayList();
        list.add(EBISystem.i18n("EBI_LANG_ADDED") + ": " + EBISystem.getInstance().getDateToString(companyActivity.getCreateddate()));
        list.add(EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + companyActivity.getCreatedfrom());

        if (companyActivity.getChangeddate() != null) {
            list.add(EBISystem.i18n("EBI_LANG_CHANGED") + ": " + EBISystem.getInstance().getDateToString(companyActivity.getChangeddate()));
            list.add(EBISystem.i18n("EBI_LANG_CHANGED_FROM") + ": " + companyActivity.getChangedfrom());
        }

        list.add(EBISystem.i18n("EBI_LANG_NAME") + ": "
                + (companyActivity.getActivityname().equals(
                        EBISystem.gui().textField("activityNameText", "Activity").getText()) == true
                ? companyActivity.getActivityname()
                : companyActivity.getActivityname() + "$"));
        final GregorianCalendar startDate = new GregorianCalendar();
        startDate.setTime(companyActivity.getDuedate());
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        String min;
        if (startDate.get(Calendar.MINUTE) < 10) {
            min = startDate.get(Calendar.MINUTE) + "0";
        } else {
            min = startDate.get(Calendar.MINUTE) + "";
        }

        String hour;
        if (startDate.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + startDate.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = "" + startDate.get(Calendar.HOUR_OF_DAY);
        }
        list.add(EBISystem.i18n("EBI_LANG_DUE_DATE") + ": "
                + (EBISystem.getInstance().getDateToString(companyActivity.getDuedate())
                        .equals(EBISystem.gui().timePicker("activityTODOText", "Activity").getEditor()
                                .getText()) == true
                        ? EBISystem.getInstance().getDateToString(companyActivity.getDuedate()) + hour
                        + ":" + min
                        : EBISystem.getInstance().getDateToString(companyActivity.getDuedate()) + hour
                        + ":" + min + "$"));
        list.add(EBISystem.i18n("EBI_LANG_C_DESCRIPTION") + ": "
                + (companyActivity.getActivitydescription().equals(
                        EBISystem.gui().textArea("activityDescription", "Activity").getText()) == true
                ? companyActivity.getActivitydescription()
                : companyActivity.getActivitydescription() + "$"));
        if (companyActivity.getActivitystatus() != null) {
            list.add(EBISystem.i18n("EBI_LANG_C_STATUS") + ": "
                    + (companyActivity.getActivitystatus()
                            .equals(EBISystem.gui().combo("activityStatusText", "Activity")
                                    .getSelectedItem().toString()) == true ? companyActivity.getActivitystatus()
                            : companyActivity.getActivitystatus() + "$"));
        }
        if (companyActivity.getActivitytype() != null) {
            list.add(EBISystem.i18n("EBI_LANG_TYPE") + ": "
                    + (companyActivity.getActivitytype()
                            .equals(EBISystem.gui().combo("activityTypeText", "Activity")
                                    .getSelectedItem().toString()) == true ? companyActivity.getActivitytype()
                            : companyActivity.getActivitytype() + "$"));
        }
        list.add(EBISystem.i18n("EBI_LANG_DURATION") + ": "
                + (("" + companyActivity.getDuration())
                        .equals(EBISystem.gui().textField("durationText", "Activity").getText()) == true
                ? companyActivity.getDuration()
                : companyActivity.getDuration() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_TIMER_START") + ": "
                + (("" + companyActivity.getTimerstart()).equals(EBISystem.gui()
                        .combo("timerStartText", "Activity").getSelectedItem().toString()) == true
                ? companyActivity.getTimerstart()
                : companyActivity.getTimerstart() + "$"));

        list.add(EBISystem.i18n("EBI_LANG_TIMER_DISABLED") + ": "
                + (("" + companyActivity.getTimerdisabled())
                        .equals(EBISystem.gui().getCheckBox("timerActiveBox", "Activity").isSelected() ? "1"
                                : "0") == true ? companyActivity.getTimerdisabled()
                        : companyActivity.getTimerdisabled() + "$"));

        list.add("*EOR*"); // END OF RECORD

        if (!companyActivity.getCompanyactivitiesdocses().isEmpty()) {
            final Iterator iter = companyActivity.getCompanyactivitiesdocses().iterator();

            while (iter.hasNext()) {
                final Companyactivitiesdocs obj = (Companyactivitiesdocs) iter.next();
                list.add(obj.getName() == null ? EBISystem.i18n("EBI_LANG_FILENAME") + ": " : EBISystem.i18n("EBI_LANG_FILENAME") + ": " + obj.getName());
                list.add(EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " : EBISystem.i18n("EBI_LANG_C_ADDED_DATE") + ": " + EBISystem.getInstance().getDateToString(obj.getCreateddate()));
                list.add(obj.getCreatedfrom() == null ? EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " : EBISystem.i18n("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                list.add("*EOR*"); // END OF RECORD
            }
        }
        EBISystem.getModule().hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Activities", list));
    }

    public void dataNew() {
        companyActivity = new Companyactivities();
        EBISystem.getModule().getActivitiesPane().initialize(false);
        EBISystem.getInstance().getDataStore("Activity", "ebiNew");
    }

    public void dataDeleteDoc(final int id) {
        final Iterator iter = this.companyActivity.getCompanyactivitiesdocses().iterator();
        while (iter.hasNext()) {
            final Companyactivitiesdocs doc = (Companyactivitiesdocs) iter.next();
            if (id == doc.getActivitydocid()) {
                this.companyActivity.getCompanyactivitiesdocses().remove(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                EBISystem.hibernate().session("EBICRM_SESSION").delete(doc);
                EBISystem.hibernate().transaction("EBICRM_SESSION").commit();
                break;
            }
        }
    }

    public void dataShowDoc() {
        if (this.companyActivity.getCompanyactivitiesdocses() != null
                && this.companyActivity.getCompanyactivitiesdocses().size() > 0) {
            EBISystem.getModule().getActivitiesPane().getTabActDoc().data = new Object[this.companyActivity.getCompanyactivitiesdocses().size()][4];
            final Iterator itr = this.companyActivity.getCompanyactivitiesdocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companyactivitiesdocs obj = (Companyactivitiesdocs) itr.next();
                EBISystem.getModule().getActivitiesPane().getTabActDoc().data[i][0] = obj.getName() == null ? "" : obj.getName();
                EBISystem.getModule().getActivitiesPane().getTabActDoc().data[i][1] = EBISystem.getInstance().getDateToString(obj.getCreateddate()) == null ? "" : EBISystem.getInstance().getDateToString(obj.getCreateddate());
                EBISystem.getModule().getActivitiesPane().getTabActDoc().data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                EBISystem.getModule().getActivitiesPane().getTabActDoc().data[i][3] = obj.getActivitydocid();
                i++;
            }
        } else {
            EBISystem.getModule().getActivitiesPane().getTabActDoc().data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        EBISystem.getModule().getActivitiesPane().getTabActDoc().fireTableDataChanged();
    }

    public void dataViewDoc(final int id) {
        String FileName;
        String FileType;
        OutputStream fos;
        try {
            final Iterator iter = this.companyActivity.getCompanyactivitiesdocses().iterator();
            while (iter.hasNext()) {
                final Companyactivitiesdocs doc = (Companyactivitiesdocs) iter.next();
                if (id == doc.getActivitydocid()) {
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

    public void dataNewDoc() {
        final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
        if (fs != null) {
            final byte[] file = EBISystem.getInstance().readFileToByte(fs);
            if (file != null) {
                final Companyactivitiesdocs docs = new Companyactivitiesdocs();
                docs.setActivitydocid((companyActivity.getCompanyactivitiesdocses().size() + 1) * -1);
                docs.setCompanyactivities(companyActivity);
                docs.setName(fs.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBISystem.ebiUser);
                docs.setFiles(file);
                companyActivity.getCompanyactivitiesdocses().add(docs);
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }
}
