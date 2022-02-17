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
import org.sdk.gui.component.EBIJTextFieldNumeric;

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
        EBISystem.builder().getPanel("colorPanel", "Activity").setBackground(jch.getColor());
    }

    public void initializeAction() {

        EBISystem.builder().textField("dueH", "Activity").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.HOUR));
        EBISystem.builder().textField("dueMin", "Activity").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.MINUTE));
        
        
        EBISystem.builder().getPanel("colorPanel", "Activity").setOpaque(true);
        EBISystem.builder().combo("timerStartText", "Activity").
                setModel(new DefaultComboBoxModel(new String[]{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"),
            "5 min", "10 min", "15 min", "20 min", "25 min", "30 min", "35 min", "40 min", "50 min", "60 min"}));

        
        EBISystem.builder().textField("filterTableText", "Activity").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("tableActivity", "Activity").
                        setRowFilter(RowFilters.regexFilter("(?i)"
                                + EBISystem.builder().textField("filterTableText", "Activity").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("tableActivity", "Activity").
                        setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "Activity").getText()));
            }
        });

        EBISystem.builder().combo("activityTypeText", "Activity").setEditable(true);

        /**
         * ***************************************************************************
         */
        // ACTIVITY TABLE DOC
        /**
         * ***************************************************************************
         */
        EBISystem.builder().table("tableActivityDoc", "Activity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableActivityDoc", "Activity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selectedDocRow = EBISystem.builder().table("tableActivityDoc", "Activity").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("showActivityDoc", "Activity").setEnabled(false);
                    EBISystem.builder().button("deleteActivityDoc", "Activity").setEnabled(false);
                } else if (!tabActDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("showActivityDoc", "Activity").setEnabled(true);
                    EBISystem.builder().button("deleteActivityDoc", "Activity").setEnabled(true);
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
        EBISystem.builder().table("tableActivity", "Activity").setDefaultRenderer(Object.class, new OwnCellRederer(false, true));
        EBISystem.builder().table("tableActivity", "Activity").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("tableActivity", "Activity").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                try {
                    selectedActivityRow = 0;
                    if (lsm.getMinSelectionIndex() != -1) {
                        selectedActivityRow = EBISystem.builder().table("tableActivity", "Activity").convertRowIndexToModel(EBISystem.builder().table("tableActivity", "Activity").getSelectedRow());
                    }

                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("editActivity", "Activity").setEnabled(false);
                        EBISystem.builder().button("deleteActivity", "Activity").setEnabled(false);
                        EBISystem.builder().button("historyActivity", "Activity").setEnabled(false);
                        EBISystem.builder().button("copyActivity", "Activity").setEnabled(false);
                    } else if (!tabModel.data[selectedActivityRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.builder().button("editActivity", "Activity").setEnabled(true);
                        EBISystem.builder().button("deleteActivity", "Activity").setEnabled(true);
                        EBISystem.builder().button("historyActivity", "Activity").setEnabled(true);
                        EBISystem.builder().button("copyActivity", "Activity").setEnabled(true);

                    }
                } catch (final IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });

        EBISystem.builder().table("tableActivity", "Activity").addKeyAction(new EBIUICallback() {
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

        EBISystem.builder().table("tableActivity", "Activity").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("tableActivity", "Activity").getSelectedRow() != -1) {
                    selectedActivityRow = EBISystem.builder().table("tableActivity", "Activity").convertRowIndexToModel(EBISystem.builder().table("tableActivity", "Activity").getSelectedRow());

                    if (selectedActivityRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selectedActivityRow][0].toString())) {
                        return;
                    }
                    editActivity();
                }
            }
        });

        EBISystem.builder().timePicker("activityTODOText", "Activity").getEditor().setText("");
    }

    public void initialize(boolean reload) {

        if (reload) {
            tabActDoc = new ModelDoc();
            tabModel = new ModelActivities();
            EBISystem.builder().table("tableActivity", "Activity").setModel(tabModel);
            EBISystem.builder().table("tableActivityDoc", "Activity").setModel(tabActDoc);
        }

        EBISystem.builder().combo("activityTypeText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(actType));
        EBISystem.builder().combo("activityStatusText", "Activity").setModel(new javax.swing.DefaultComboBoxModel(actStatus));

        EBISystem.builder().vpanel("Activity").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.builder().vpanel("Activity").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Activity").setChangedDate("");
        EBISystem.builder().vpanel("Activity").setChangedFrom("");

        EBISystem.builder().combo("activityStatusText", "Activity").setSelectedIndex(0);
        EBISystem.builder().combo("activityTypeText", "Activity").setSelectedIndex(0);

        EBISystem.builder().textField("dueH", "Activity").setText("");
        EBISystem.builder().textField("dueMin", "Activity").setText("");
        EBISystem.builder().textField("durationText", "Activity").setText("");

        EBISystem.builder().getPanel("colorPanel", "Activity").setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        EBISystem.builder().getPanel("colorPanel", "Activity").setBackground(new Color(5, 125, 255));

        EBISystem.builder().textField("activityNameText", "Activity").setText("");
        EBISystem.builder().textArea("activityDescription", "Activity").setText("");
        EBISystem.builder().timePicker("activityTODOText", "Activity").setDate(null);
        EBISystem.builder().timePicker("activityTODOText", "Activity").setFormats(EBISystem.DateFormat);
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
        int row = EBISystem.builder().table("tableActivity", "Activity").getSelectedRow();
        Integer id = dataControlActivity.dataStore();
        dataControlActivity.dataShow(id);
        dataControlActivity.dataShowDoc();
        EBISystem.builder().table("tableActivity", "Activity").changeSelection(row, 0, false, false);
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
            if (Integer.parseInt(EBISystem.builder().textField("durationText", "Activity").getText()) <= 0) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_VALID_NUMBER")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            } else if ("".equals(EBISystem.builder().textField("activityNameText", "Activity").getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
                ret = false;
            } else if (EBISystem.builder().combo("activityTypeText", "Activity").getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
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
