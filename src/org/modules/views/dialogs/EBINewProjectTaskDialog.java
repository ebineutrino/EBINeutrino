package org.modules.views.dialogs;

import org.modules.models.ModelProperties;
import org.sdk.EBISystem;
import org.sdk.gui.component.TaskEvent;
import org.sdk.gui.dialogs.EBIDialog;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Crmprojectcost;
import org.sdk.model.hibernate.Crmprojectprop;
import org.sdk.model.hibernate.Crmprojecttask;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Iterator;

public class EBINewProjectTaskDialog implements ChangeListener {

    public boolean saveTask = true;
    private JColorChooser jch = null;
    private ModelProperties tabModCost = null;
    private ModelProperties tabModProperties = null;
    private int selectedCost = -1;
    private int selectedProperties = -1;
    public static String[] taskStatus = null;
    public static String[] taskType = null;
    private Crmprojecttask projectTask = null;

    @Override
    public void stateChanged(final ChangeEvent changeEvent) {
        EBISystem.gui().getPanel("taskColor", "projectTaskDialog").setBackground(jch.getColor());
    }

    public boolean setVisible(final TaskEvent te, final Crmprojecttask task) {
        tabModProperties = new ModelProperties();
        tabModCost = new ModelProperties();
        projectTask = task;

        EBISystem.gui().loadGUI("CRMDialog/newProjectTaskDialog.xml");
        EBISystem.gui().combo("taskStatusText", "projectTaskDialog").setModel(new DefaultComboBoxModel(taskStatus));
        EBISystem.gui().combo("taskTypeText", "projectTaskDialog").setModel(new DefaultComboBoxModel(taskType));
        EBISystem.gui().combo("taskDoneText", "projectTaskDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));

        for (int i = 10; i <= 100; i += 10) {
            EBISystem.gui().combo("taskDoneText", "projectTaskDialog").addItem(i + "%");
        }

        EBISystem.gui().getPanel("taskColor", "projectTaskDialog").setOpaque(true);

        EBISystem.gui().combo("taskDoneText", "projectTaskDialog").setSelectedIndex(te.getReached() / 10);
        EBISystem.gui().textField("taskNameText", "projectTaskDialog").setText(te.getName());
        EBISystem.gui().getEditor("taskDescription", "projectTaskDialog").setText(te.getDescription());

        EBISystem.gui().textField("durationText", "projectTaskDialog").setText(String.valueOf(te.getDuration()));
        EBISystem.gui().combo("taskStatusText", "projectTaskDialog").setSelectedItem(te.getStatus());
        EBISystem.gui().combo("taskTypeText", "projectTaskDialog").setSelectedItem(te.getType());

        EBISystem.gui().getPanel("taskColor", "projectTaskDialog").setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        EBISystem.gui().getPanel("taskColor", "projectTaskDialog").setBackground(te.getBackgroundColor());

        EBISystem.gui().button("chooseTaskColor", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final EBIDialog diaColor = new EBIDialog(null);
                diaColor.setSize(600, 400);
                diaColor.setModal(true);
                diaColor.setName("ProjectColorChooser");
                jch = new JColorChooser();
                jch.getSelectionModel().addChangeListener(EBINewProjectTaskDialog.this);
                diaColor.getContentPane().setLayout(new BorderLayout());
                diaColor.getContentPane().add(jch, BorderLayout.CENTER);
                diaColor.setVisible(true);

            }
        });

        EBISystem.gui().button("cancelTask", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                saveTask = false;
                EBISystem.gui().dialog("projectTaskDialog").setVisible(false);
            }
        });

        EBISystem.gui().button("taskSave", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (!validateInput()) {
                    return;
                }
                te.setName(EBISystem.gui().textField("taskNameText", "projectTaskDialog").getText());
                te.setDescription("<html>" + EBISystem.gui().getEditor("taskDescription", "projectTaskDialog").getText() + "<html>");
                te.setBackgroundColor(EBISystem.gui().getPanel("taskColor", "projectTaskDialog").getBackground());

                te.setReached(EBISystem.gui().combo("taskDoneText", "projectTaskDialog").getSelectedIndex() * 10);

                if (EBISystem.gui().combo("taskStatusText", "projectTaskDialog").getEditor().getItem() != null) {
                    te.setStatus(EBISystem.gui().combo("taskStatusText", "projectTaskDialog").getEditor().getItem().toString());
                }

                if (EBISystem.gui().combo("taskTypeText", "projectTaskDialog").getEditor().getItem() != null) {
                    te.setType(EBISystem.gui().combo("taskTypeText", "projectTaskDialog").getEditor().getItem().toString());
                }

                try {
                    te.setDuration(Integer.parseInt(EBISystem.gui().textField("durationText", "projectTaskDialog").getText()) * 20);
                } catch (final NumberFormatException ex) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
                }

                saveTask = true;
                EBISystem.gui().dialog("projectTaskDialog").setVisible(false);
            }
        });

        EBISystem.gui().button("newCost", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.gui().button("newCost", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final EBIDialogProperties properties = new EBIDialogProperties(projectTask, null, true);
                properties.setVisible();
            }
        });

        EBISystem.gui().button("editCost", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.gui().button("editCost", "projectTaskDialog").setEnabled(false);
        EBISystem.gui().button("editCost", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {

                final Iterator iter = projectTask.getCrmprojectcosts().iterator();
                while (iter.hasNext()) {
                    final Crmprojectcost cost = (Crmprojectcost) iter.next();
                    if (cost.getCostid() == Integer.parseInt(tabModCost.data[selectedCost][2].toString())) {
                        final EBIDialogProperties properties = new EBIDialogProperties(projectTask, cost, true);
                        properties.setVisible();
                        showCost();
                        break;
                    }
                }
            }
        });

        EBISystem.gui().button("deleteCost", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.gui().button("deleteCost", "projectTaskDialog").setEnabled(false);
        EBISystem.gui().button("deleteCost", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final Iterator iter = projectTask.getCrmprojectcosts().iterator();
                while (iter.hasNext()) {
                    final Crmprojectcost cost = (Crmprojectcost) iter.next();
                    if (cost.getCostid() == Integer.parseInt(tabModCost.data[selectedCost][2].toString())) {
                        projectTask.getCrmprojectcosts().remove(cost);
                        showCost();
                        break;
                    }
                }
            }
        });

        EBISystem.gui().button("newProperties", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.gui().button("newProperties", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final EBIDialogProperties properties = new EBIDialogProperties(projectTask, null, false);
                properties.setVisible();
                showProperties();
            }
        });

        EBISystem.gui().button("editProperties", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.gui().button("editProperties", "projectTaskDialog").setEnabled(false);
        EBISystem.gui().button("editProperties", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {

                final Iterator iter = projectTask.getCrmprojectprops().iterator();
                while (iter.hasNext()) {
                    final Crmprojectprop prop = (Crmprojectprop) iter.next();
                    if (prop.getPropertiesid() == Integer.parseInt(tabModProperties.data[selectedProperties][2].toString())) {
                        final EBIDialogProperties properties = new EBIDialogProperties(projectTask, prop, false);
                        properties.setVisible();
                        showProperties();
                        break;
                    }
                }
            }
        });

        EBISystem.gui().button("deleteProperties", "projectTaskDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.gui().button("deleteProperties", "projectTaskDialog").setEnabled(false);
        EBISystem.gui().button("deleteProperties", "projectTaskDialog").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                final Iterator iter = projectTask.getCrmprojectprops().iterator();
                while (iter.hasNext()) {
                    final Crmprojectprop prop = (Crmprojectprop) iter.next();
                    if (prop.getPropertiesid() == Integer.parseInt(tabModProperties.data[selectedProperties][2].toString())) {
                        projectTask.getCrmprojectprops().remove(prop);
                        showProperties();
                        break;
                    }
                }
            }
        });

        //Table cost
        EBISystem.gui().table("tableCost", "projectTaskDialog").setModel(tabModCost);
        final TableColumn col7 = EBISystem.gui().table("tableCost", "projectTaskDialog").getColumnModel().getColumn(1);
        col7.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
                final JLabel myself = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                myself.setHorizontalAlignment(SwingConstants.RIGHT);
                myself.setForeground(new Color(255, 60, 60));
                return myself;
            }
        });

        EBISystem.gui().table("tableCost", "projectTaskDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedCost = EBISystem.gui().table("tableCost", "projectTaskDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editCost", "projectTaskDialog").setEnabled(false);
                    EBISystem.gui().button("deleteCost", "projectTaskDialog").setEnabled(false);
                } else if (!tabModCost.data[selectedCost][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editCost", "projectTaskDialog").setEnabled(true);
                    EBISystem.gui().button("deleteCost", "projectTaskDialog").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("taskPropertiesTable", "projectTaskDialog").setModel(tabModProperties);
        EBISystem.gui().table("taskPropertiesTable", "projectTaskDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedProperties = EBISystem.gui().table("taskPropertiesTable", "projectTaskDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editProperties", "projectTaskDialog").setEnabled(false);
                    EBISystem.gui().button("deleteProperties", "projectTaskDialog").setEnabled(false);
                } else if (!tabModProperties.data[selectedProperties][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("editProperties", "projectTaskDialog").setEnabled(true);
                    EBISystem.gui().button("deleteProperties", "projectTaskDialog").setEnabled(true);
                }
            }
        });

        showProperties();
        showCost();
        EBISystem.gui().showGUI();

        return saveTask;
    }

    public void showProperties() {
        tabModProperties.data = new Object[projectTask.getCrmprojectprops().size()][3];

        final Iterator iter = projectTask.getCrmprojectprops().iterator();
        int i = 0;
        if (projectTask.getCrmprojectprops().size() > 0) {
            while (iter.hasNext()) {
                final Crmprojectprop prop = (Crmprojectprop) iter.next();
                tabModProperties.data[i][0] = prop.getName();
                tabModProperties.data[i][1] = prop.getValue();
                tabModProperties.data[i][2] = prop.getPropertiesid();
                i++;
            }
        } else {
            tabModProperties.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        tabModProperties.fireTableDataChanged();
    }

    public void showCost() {
        tabModCost.data = new Object[projectTask.getCrmprojectcosts().size()][3];

        final Iterator iter = projectTask.getCrmprojectcosts().iterator();
        int i = 0;
        if (projectTask.getCrmprojectcosts().size() > 0) {

            final NumberFormat currency = NumberFormat.getCurrencyInstance();

            while (iter.hasNext()) {
                final Crmprojectcost cost = (Crmprojectcost) iter.next();
                tabModCost.data[i][0] = cost.getName() == null ? "" : cost.getName();
                tabModCost.data[i][1] = currency.format(cost.getValue()) == null ? "" : currency.format(cost.getValue());
                tabModCost.data[i][2] = cost.getCostid();
                i++;
            }
        } else {
            tabModCost.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", ""}};
        }
        tabModCost.fireTableDataChanged();
    }

    private boolean validateInput() {

        if ("".equals(EBISystem.gui().textField("taskNameText", "projectTaskDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_TASK_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
