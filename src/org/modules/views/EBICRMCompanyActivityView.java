package org.modules.views;

import org.modules.controls.ControlActivity;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.OwnCellRederer;
import org.modules.models.ModelActivities;
import org.modules.models.ModelDoc;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialog;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import lombok.Getter;
import lombok.Setter;

public class EBICRMCompanyActivityView implements ChangeListener {

    public static String[] actType = null;
    public static String[] actStatus = null;
    @Getter
    @Setter
    private ModelDoc tabActDoc = null;
    @Getter
    @Setter
    private ModelActivities tabModel = null;
    @Getter
    @Setter
    private ControlActivity dataControlActivity = new ControlActivity();
    private int selectedActivityRow = -1;
    private int selectedDocRow = -1;
    private JColorChooser jch = null;

    @Override
    public void stateChanged(final ChangeEvent e) {
        EBISystem.gui().getPanel("colorPanel", "Activity").setBackground(jch.getColor());
    }

    public void initializeAction() {

        EBISystem.gui().getPanel("colorPanel", "Activity").setOpaque(true);
        EBISystem.gui().combo("timerStartText", "Activity").
                setModel(new DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            "5 min", "10 min", "15 min", "20 min", "25 min", "30 min", "35 min", "40 min", "50 min", "60 min"}));

        
        EBISystem.gui().textField("filterTableText", "Activity").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("tableActivity", "Activity").
                        setRowFilter(RowFilters.regexFilter("(?i)"
                                + EBISystem.gui().textField("filterTableText", "Activity").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("tableActivity", "Activity").
                        setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "Activity").getText()));
            }
        });

        EBISystem.gui().combo("activityTypeText", "Activity").setEditable(true);

        /**
         * ***************************************************************************
         */
        // ACTIVITY TABLE DOC
        /**
         * ***************************************************************************
         */
        EBISystem.gui().table("tableActivityDoc", "Activity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableActivityDoc", "Activity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.gui().table("tableActivityDoc", "Activity").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("showActivityDoc", "Activity").setEnabled(false);
                    EBISystem.gui().button("deleteActivityDoc", "Activity").setEnabled(false);
                } else if (!tabActDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().button("showActivityDoc", "Activity").setEnabled(true);
                    EBISystem.gui().button("deleteActivityDoc", "Activity").setEnabled(true);
                }
            }
        });

        /**
         * ***************************************************************************
         */
        // ACTIVITY TABLE
        /**
         * **************************************************************************
         */
        EBISystem.gui().table("tableActivity", "Activity").setDefaultRenderer(Object.class, new OwnCellRederer(false, true));
        EBISystem.gui().table("tableActivity", "Activity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("tableActivity", "Activity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                try {
                    selectedActivityRow = 0;
                    if (lsm.getMinSelectionIndex() != -1) {
                        selectedActivityRow = EBISystem.gui().table("tableActivity", "Activity").convertRowIndexToModel(EBISystem.gui().table("tableActivity", "Activity").getSelectedRow());
                    }

                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("editActivity", "Activity").setEnabled(false);
                        EBISystem.gui().button("deleteActivity", "Activity").setEnabled(false);
                        EBISystem.gui().button("historyActivity", "Activity").setEnabled(false);
                        EBISystem.gui().button("copyActivity", "Activity").setEnabled(false);
                    } else if (!tabModel.data[selectedActivityRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.gui().button("editActivity", "Activity").setEnabled(true);
                        EBISystem.gui().button("deleteActivity", "Activity").setEnabled(true);
                        EBISystem.gui().button("historyActivity", "Activity").setEnabled(true);
                        EBISystem.gui().button("copyActivity", "Activity").setEnabled(true);

                    }
                } catch (final IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });

        EBISystem.gui().table("tableActivity", "Activity").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedActivityRow = selRow;
                editActivity();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedActivityRow = selRow;
                editActivity();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedActivityRow = selRow;
                if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModel.data[selectedActivityRow][0].toString())) {
                    return;
                }
                editActivity();
            }
        });

        EBISystem.gui().table("tableActivity", "Activity").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("tableActivity", "Activity").getSelectedRow() != -1) {
                    selectedActivityRow = EBISystem.gui().table("tableActivity", "Activity").convertRowIndexToModel(EBISystem.gui().table("tableActivity", "Activity").getSelectedRow());

                    if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selectedActivityRow][0].toString())) {
                        return;
                    }
                    editActivity();
                }
            }
        });

        EBISystem.gui().timePicker("activityTODOText", "Activity").getEditor().setText("");
    }

    public void initialize(boolean reload) {

        if (reload) {
            tabActDoc = new ModelDoc();
            tabModel = new ModelActivities();
            EBISystem.gui().table("tableActivity", "Activity").setModel(tabModel);
            EBISystem.gui().table("tableActivityDoc", "Activity").setModel(tabActDoc);
        }

        EBISystem.gui().combo("activityTypeText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(actType));
        EBISystem.gui().combo("activityStatusText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(actStatus));

        EBISystem.gui().vpanel("Activity").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.gui().vpanel("Activity").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Activity").setChangedDate("");
        EBISystem.gui().vpanel("Activity").setChangedFrom("");

        EBISystem.gui().combo("activityStatusText", "Activity").setSelectedIndex(0);
        EBISystem.gui().combo("activityTypeText", "Activity").setSelectedIndex(0);

        EBISystem.gui().getSpinner("dueH", "Activity").setValue(0);
        EBISystem.gui().getSpinner("dueMin", "Activity").setValue(0);
        EBISystem.gui().textField("durationText", "Activity").setText("");

        EBISystem.gui().getPanel("colorPanel", "Activity").setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        EBISystem.gui().getPanel("colorPanel", "Activity").setBackground(new Color(5, 125, 255));

        EBISystem.gui().textField("activityNameText", "Activity").setText("");
        EBISystem.gui().textArea("activityDescription", "Activity").setText("");
        EBISystem.gui().timePicker("activityTODOText", "Activity").setDate(null);
        EBISystem.gui().timePicker("activityTODOText", "Activity").setFormats(EBISystem.DateFormat);
    }

    public void newDocs() {
        dataControlActivity.dataNewDoc();
        dataControlActivity.dataShowDoc();
    }

    public void saveAndShowDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabActDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataControlActivity.dataViewDoc(Integer.parseInt(tabActDoc.data[selectedDocRow][3].toString()));
    }

    public void deleteDoc() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabActDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Activity");
            dataControlActivity.dataDeleteDoc(Integer.parseInt(tabActDoc.data[selectedDocRow][3].toString()));
            dataControlActivity.dataShowDoc();
        }
    }

    public void newActivity() {
        EBISystem.showInActionStatus("Activity");
        dataControlActivity.dataNew();
        dataControlActivity.dataShow(-1);
        dataControlActivity.dataShowDoc();
        dataControlActivity.isEdit = false;
    }

    public void copyActivity() {
        if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedActivityRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Activity");
        Integer id = dataControlActivity.dataCopy(Integer.parseInt(tabModel.data[selectedActivityRow][7].toString()));
        dataControlActivity.dataEdit(id);
        dataControlActivity.dataShow(id);
        dataControlActivity.dataShowDoc();
        dataControlActivity.isEdit = true;
    }

    public boolean saveActivity() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Activity");
        int row = EBISystem.gui().table("tableActivity", "Activity").getSelectedRow();
        Integer id = dataControlActivity.dataStore();
        dataControlActivity.dataShow(id);
        dataControlActivity.dataShowDoc();
        dataControlActivity.isEdit = true;
        EBISystem.gui().table("tableActivity", "Activity").changeSelection(row, 0, false, false);
        return true;
    }

    public void editActivity() {
        if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedActivityRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Activity");
        dataControlActivity.dataNew();
        dataControlActivity.dataEdit(Integer.parseInt(tabModel.data[selectedActivityRow][7].toString()));
        dataControlActivity.dataShowDoc();
        dataControlActivity.isEdit = true;
    }

    public void remoteEditActivity(final int id) {
        EBISystem.showInActionStatus("Activity");
        dataControlActivity.dataNew();
        dataControlActivity.dataShow(-1);
        dataControlActivity.dataEdit(id);
        dataControlActivity.dataShowDoc();
        dataControlActivity.isEdit = true;
    }

    public void deleteActivity() {
        if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedActivityRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Activity");
            dataControlActivity.dataDelete(Integer.parseInt(tabModel.data[selectedActivityRow][7].toString()));
            dataControlActivity.dataNew();
            dataControlActivity.dataShow(-1);
            dataControlActivity.dataShowDoc();
            dataControlActivity.isEdit = false;
        }
    }

    public void historySearch() {
        new EBICRMHistoryView(EBISystem.getModule().
                hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Activities")).setVisible();
    }

    private boolean validateInput() {
        boolean ret = true;
        try {
            if (Integer.parseInt(EBISystem.gui().textField("durationText", "Activity").getText()) <= 0) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            } else if ("".equals(EBISystem.gui().textField("activityNameText", "Activity").getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            } else if (EBISystem.gui().combo("activityTypeText", "Activity").getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            }
        } catch (final NumberFormatException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }

    public void selectColorDialog() {
        final EBIDialog diaColor = new EBIDialog(null);
        diaColor.setSize(600, 400);
        diaColor.setModal(true);
        diaColor.setName("ActivityColorChooser");
        jch = new JColorChooser();
        jch.getSelectionModel().addChangeListener(EBICRMCompanyActivityView.this);
        diaColor.getContentPane().setLayout(new BorderLayout());
        diaColor.getContentPane().add(jch, BorderLayout.CENTER);
        diaColor.setVisible(true);
    }
}
