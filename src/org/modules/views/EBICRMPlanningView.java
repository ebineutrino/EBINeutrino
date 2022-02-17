package org.modules.views;

import org.modules.controls.ControlPlanning;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.views.dialogs.EBINewProjectTaskDialog;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIGRCManagement;
import org.sdk.gui.component.EBIGRCManagementListener;
import org.sdk.gui.component.EBIJTextFieldNumeric;
import org.sdk.gui.component.TaskEvent;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Crmprojectcost;
import org.sdk.model.hibernate.Crmprojecttask;
import org.sdk.utils.EBIAbstractTableModel;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import lombok.Getter;
import lombok.Setter;

public class EBICRMPlanningView {

    @Getter @Setter
    private EBIGRCManagement grcManagement = null;
    @Getter @Setter
    private ControlPlanning dataControlProject = new ControlPlanning();
    @Getter @Setter
    private int selectedProjectRow = -1;
    public static String[] projectStatus = null;
    @Getter @Setter
    private EBINewProjectTaskDialog projTask = null;
    @Getter @Setter
    private EBIAbstractTableModel model = null;

    public void initialize() {

        EBISystem.hibernate().openHibernateSession("EBIPROJECT_SESSION");
        final NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMinimumFractionDigits(2);
        valueFormat.setMaximumFractionDigits(2);

        model = (EBIAbstractTableModel) EBISystem.builder().table("projectTable", "Project").getModel();
        EBISystem.builder().textField("filterTableText", "Project").addKeyListener(
                new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                EBISystem.builder().table("projectTable", "Project").setRowFilter(RowFilters.regexFilter(
                        "(?i)" + EBISystem.builder().textField("filterTableText", "Project").getText()));
            }
        });

        EBISystem.builder().vpanel("Project").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Project")
                .setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));

        EBISystem.builder().vpanel("Project").setChangedFrom("");
        EBISystem.builder().vpanel("Project").setChangedDate("");

        EBISystem.builder().combo("prjStatusText", "Project")
                .setModel(new DefaultComboBoxModel(projectStatus));

        EBISystem.builder().FormattedField("budgetText", "Project").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(valueFormat)));
        EBISystem.builder().FormattedField("budgetText", "Project").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.FLOAT));
        EBISystem.builder().FormattedField("budgetText", "Project").setColumns(10);

        EBISystem.builder().FormattedField("actualCostText", "Project").setEditable(false);
        EBISystem.builder().FormattedField("actualCostText", "Project").setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(valueFormat)));
        EBISystem.builder().FormattedField("actualCostText", "Project").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.FLOAT));
        EBISystem.builder().FormattedField("actualCostText", "Project").setColumns(10);

        /*
		 * EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").
		 * setEditable(false);
		 * EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").
		 * setFormatterFactory(new DefaultFormatterFactory(new
		 * NumberFormatter(valueFormat)));
		 * EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").
		 * setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.FLOAT));
		 * EBISystem.getGUIRenderer().getFormattedTextfield("remainingCost","Project").
		 * setColumns(10);
         */
        // Init project listener control
        projTask = new EBINewProjectTaskDialog();
        grcManagement = new EBIGRCManagement();
        grcManagement.addEventListener(new EBIGRCManagementListener() {
            @Override
            public boolean addNewTaskAction(final TaskEvent event) {

                boolean saveTask = false;
                final Crmprojecttask taskBean = new Crmprojecttask();

                if (projTask.setVisible(event, taskBean)) {
                    taskBean.setTaskiid((dataControlProject.getProjectBean().getCrmprojecttasks().size() + 1) * -1);
                    taskBean.setTaskid("" + event.getId());
                    taskBean.setX(event.getX());
                    taskBean.setY(event.getY());
                    taskBean.setName(event.getName());
                    taskBean.setDone(event.getReached());
                    taskBean.setColor("#" + Integer.toHexString((event.getBackgroundColor().getRGB() & 0x00ffffff)));
                    taskBean.setDuration(event.getDuration());
                    taskBean.setStatus(event.getStatus());
                    taskBean.setType(event.getType());
                    taskBean.setDescription(event.getDescription());
                    dataControlProject.getProjectBean().getCrmprojecttasks().add(taskBean);

                    if (!taskBean.getCrmprojectcosts().isEmpty()) {
                        calculateActualCost();
                    }
                    saveTask = true;
                }

                return saveTask;
            }

            @Override
            public boolean editTaskAction(final TaskEvent event, final boolean isFullEdit) {

                boolean saveTask = false;

                final Iterator iter = dataControlProject.getProjectBean().getCrmprojecttasks().iterator();
                Crmprojecttask taskBean = null;
                while (iter.hasNext()) {
                    taskBean = (Crmprojecttask) iter.next();
                    if (Long.parseLong(taskBean.getTaskid()) == event.getId()) {
                        break;
                    }
                }

                if (isFullEdit) {
                    if (projTask.setVisible(event, taskBean)) {
                        if (taskBean != null) {
                            taskBean.setName(event.getName());
                            taskBean.setX(event.getX());
                            taskBean.setY(event.getY());
                            taskBean.setDone(event.getReached());
                            taskBean.setColor("#" + Integer.toHexString((event.getBackgroundColor().getRGB() & 0x00ffffff)));
                            taskBean.setDuration(event.getDuration());
                            taskBean.setStatus(event.getStatus());
                            taskBean.setType(event.getType());
                            taskBean.setDescription(event.getDescription());
                        }
                        if (taskBean != null) {
                            if (!taskBean.getCrmprojectcosts().isEmpty()) {
                                calculateActualCost();
                            }
                        }
                        saveTask = true;
                    }
                } else {
                    if (taskBean != null && event != null) {
                        taskBean.setDuration(event.getDuration());
                        taskBean.setX(event.getX());
                        taskBean.setY(event.getY());
                    }
                    saveTask = true;
                }

                return saveTask;
            }

            @Override
            public boolean deleteTaskAction(final TaskEvent event) {

                boolean isDelete = false;
                try {
                    if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_DELETE_TASK")).Show(EBIMessage.WARNING_MESSAGE_YESNO)) {
                        final Iterator iter = dataControlProject.getProjectBean().getCrmprojecttasks().iterator();

                        Crmprojecttask taskBean;

                        while (iter.hasNext()) {
                            taskBean = (Crmprojecttask) iter.next();
                            if (Long.parseLong(taskBean.getTaskid()) == event.getId()) {

                                EBISystem.hibernate().session("EBIPROJECT_SESSION").delete(taskBean);
                                dataControlProject.getProjectBean().getCrmprojecttasks().remove(taskBean);
                                if (!taskBean.getCrmprojectcosts().isEmpty()) {
                                    calculateActualCost();
                                }
                                break;
                            }
                        }
                        isDelete = true;
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }

                return isDelete;
            }

            @Override
            public boolean createRelation(final long fromId, final long toId) {
                boolean saveRelation = false;
                final Iterator iter = dataControlProject.getProjectBean().getCrmprojecttasks().iterator();
                Crmprojecttask taskBean = null;
                while (iter.hasNext()) {
                    taskBean = (Crmprojecttask) iter.next();
                    if (Long.parseLong(taskBean.getTaskid()) == toId) {
                        break;
                    }
                }

                if (taskBean != null) {
                    taskBean.setParentstaskid(";" + fromId);
                    saveRelation = true;
                }

                return saveRelation;
            }
        });
        EBISystem.builder().getPanel("taskGraph", "Project").setLayout(new BorderLayout());
        EBISystem.builder().getPanel("taskGraph", "Project").add(grcManagement.getScrollComponent(), BorderLayout.CENTER);
    }

    public void initializeAction() {

        EBISystem.builder().timePicker("prjstartDateText", "Project").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {

                if (EBISystem.builder().timePicker("prjendDateText", "Project").getDate() != null) {
                    if (EBISystem.builder().timePicker("prjendDateText", "Project").getDate()
                            .getTime() > EBISystem.builder().timePicker("prjstartDateText", "Project")
                                    .getDate().getTime()) {

                        grcManagement.setStartEnd(
                                EBISystem.builder().timePicker("prjstartDateText", "Project").getDate(),
                                EBISystem.builder().timePicker("prjendDateText", "Project").getDate());
                    } else {
                        EBIExceptionDialog
                                .getInstance(EBISystem.i18n("EBI_LANG_ERROR_MESSAGE_STARTDATE_SMALL_ENDDATE"))
                                .Show(EBIMessage.ERROR_MESSAGE);
                    }
                }
            }
        });

        EBISystem.builder().timePicker("prjendDateText", "Project").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                if (EBISystem.builder().timePicker("prjstartDateText", "Project").getDate() != null) {
                    if (EBISystem.builder().timePicker("prjendDateText", "Project").getDate()
                            .getTime() > EBISystem.builder().timePicker("prjstartDateText", "Project")
                                    .getDate().getTime()) {

                        grcManagement.setStartEnd(
                                EBISystem.builder().timePicker("prjstartDateText", "Project").getDate(),
                                EBISystem.builder().timePicker("prjendDateText", "Project").getDate());
                    } else {
                        EBIExceptionDialog
                                .getInstance(EBISystem.i18n("EBI_LANG_ERROR_MESSAGE_STARTDATE_SMALL_ENDDATE"))
                                .Show(EBIMessage.ERROR_MESSAGE);
                    }
                }
            }
        });

        /**
         * **********************************************************
         */
        // Available Table
        /**
         * **********************************************************
         */
        final EBIAbstractTableModel model = (EBIAbstractTableModel) EBISystem.builder()
                .table("projectTable", "Project").getModel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final TableColumn col7 = EBISystem.builder().table("projectTable", "Project").getColumnModel().getColumn(7);
                col7.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value,
                            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });

                final TableColumn col6 = EBISystem.builder().table("projectTable", "Project").getColumnModel().getColumn(6);
                col6.setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(final JTable table, final Object value,
                            final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                        final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        myself.setHorizontalAlignment(SwingConstants.RIGHT);
                        myself.setForeground(new Color(255, 60, 60));
                        return myself;
                    }
                });

            }
        });

        EBISystem.builder().table("projectTable", "Project").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        EBISystem.builder().table("projectTable", "Project").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    try {
                        selectedProjectRow = EBISystem.builder().table("projectTable", "Project").convertRowIndexToModel(lsm.getMinSelectionIndex());
                        if (lsm.isSelectionEmpty()) {
                            EBISystem.builder().button("editProject", "Project").setEnabled(false);
                            EBISystem.builder().button("deleteProject", "Project").setEnabled(false);
                            EBISystem.builder().button("historyProject", "Project").setEnabled(false);
                            EBISystem.builder().button("reportProject", "Project").setEnabled(false);
                            EBISystem.builder().button("createProduct", "Project").setEnabled(false);
                            EBISystem.builder().button("copyProject", "Project").setEnabled(false);
                        } else if (!model.data[selectedProjectRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                            EBISystem.builder().button("editProject", "Project").setEnabled(true);
                            EBISystem.builder().button("deleteProject", "Project").setEnabled(true);
                            EBISystem.builder().button("historyProject", "Project").setEnabled(true);
                            EBISystem.builder().button("reportProject", "Project").setEnabled(true);
                            EBISystem.builder().button("createProduct", "Project").setEnabled(true);
                            EBISystem.builder().button("copyProject", "Project").setEnabled(true);
                        }
                    } catch (final IndexOutOfBoundsException ex) {
                    }
                }
            }
        });

        EBISystem.builder().table("projectTable", "Project").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedProjectRow = selRow;
                editProject();
                calculateActualCost();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedProjectRow = selRow;
                editProject();
                calculateActualCost();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedProjectRow = selRow;
                if (selectedProjectRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(model.data[selectedProjectRow][0].toString())) {
                    return;
                }
                editProject();
                calculateActualCost();
            }
        });

        EBISystem.builder().table("projectTable", "Project").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("projectTable", "Project").rowAtPoint(e.getPoint()) != -1) {
                    selectedProjectRow = EBISystem.builder().table("projectTable", "Project")
                            .convertRowIndexToModel(EBISystem.builder().table("projectTable", "Project").rowAtPoint(e.getPoint()));
                }
                if (selectedProjectRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(model.data[selectedProjectRow][0].toString())) {
                    return;
                }
                editProject();
                calculateActualCost();
            }
        });
    }

    public void showProject() {
        dataControlProject.dataShow(-1);
    }

    public void newProject() {
        EBISystem.showInActionStatus("Project");
        dataControlProject.dataNew();
        dataControlProject.dataShow(-1);
        dataControlProject.isEdit = false;
    }

    public boolean saveProject() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Project");
        int row = EBISystem.builder().table("projectTable", "Project").getSelectedRow();
        Integer id = dataControlProject.dataStore();
        dataControlProject.dataShow(id);
        EBISystem.builder().table("projectTable", "Project").changeSelection(row, 0, false, false);
        return false;
    }

    public void editProject() {
        if (selectedProjectRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(model.data[selectedProjectRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Project");
        dataControlProject.dataNew();
        dataControlProject.dataEdit(Integer.parseInt(model.data[selectedProjectRow][9].toString()));
        EBISystem.getModule().getProjectPane().grcManagement.showTasks();
        dataControlProject.isEdit = true;
        calculateActualCost();
    }

    public void copyProject() {
        if (selectedProjectRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(model.data[selectedProjectRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Project");
        Integer id = dataControlProject.dataCopy(Integer.parseInt(model.data[selectedProjectRow][9].toString()));
        dataControlProject.dataEdit(id);
        dataControlProject.dataShow(id);
        dataControlProject.isEdit = true;
    }

    public void deleteProject() {
        if (selectedProjectRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(model.data[selectedProjectRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Project");
            dataControlProject.dataDelete(Integer.parseInt(model.data[selectedProjectRow][9].toString()));
            dataControlProject.dataNew();
            dataControlProject.dataShow(-1);
            dataControlProject.isEdit = false;
        }
    }

    public void createProduct() {
        dataControlProject.createProduct(Integer.parseInt(model.data[selectedProjectRow][9].toString()));
    }

    public void showReport() {
        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {
            dataControlProject.dataShowReport(
                    Integer.parseInt(model.data[selectedProjectRow][9].toString()),
                    model.data[selectedProjectRow][1].toString());
        }
    }

    public void searchHistory() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator
                .retrieveDBHistory(Integer.parseInt(model.data[selectedProjectRow][9].toString()), "Project"))
                .setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.builder().textField("prjNrText", "Project").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROJ_NR")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.builder().textField("prjNameText", "Project").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROJ_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void calculateActualCost() {

        if (EBISystem.builder().FormattedField("budgetText", "Project").getValue() != null) {

            final Iterator iter = dataControlProject.getProjectBean().getCrmprojecttasks().iterator();

            Double value = 0.0;
            while (iter.hasNext()) {

                final Crmprojecttask task = (Crmprojecttask) iter.next();
                final Iterator itc = task.getCrmprojectcosts().iterator();

                while (itc.hasNext()) {
                    final Crmprojectcost cost = (Crmprojectcost) itc.next();
                    value += cost.getValue();
                }

            }
            if (value > Double.parseDouble(EBISystem.builder().FormattedField("budgetText", "Project").getValue().toString())) {
                EBISystem.builder().FormattedField("actualCostText", "Project").setForeground(new Color(255, 0, 0));
            }
            EBISystem.builder().FormattedField("actualCostText", "Project").setValue(value);
        }
    }
}
