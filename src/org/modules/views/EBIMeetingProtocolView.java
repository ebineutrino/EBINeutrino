package org.modules.views;

import org.modules.controls.ControlMeetingProtocol;
import org.modules.views.dialogs.EBICRMHistoryView;
import org.modules.models.ModelCRMContact;
import org.modules.models.ModelCRMProtocol;
import org.modules.models.ModelDoc;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import lombok.Getter;
import lombok.Setter;

public class EBIMeetingProtocolView {

    @Getter
    @Setter
    private ControlMeetingProtocol dataMeetingControl = new ControlMeetingProtocol();
    @Getter
    @Setter
    private ModelCRMProtocol tableModel = null;
    public static String[] art = null;
    @Getter
    @Setter
    private ModelCRMContact tabModelContact = null;
    @Getter
    @Setter
    private ModelDoc tabmeetingDoc = null;
    @Getter
    @Setter
    private int selectedProtocolRow = -1;
    @Getter
    @Setter
    private int selectedContactRow = -1;
    @Getter
    @Setter
    private int selectedDocRow = -1;

    public void initializeAction() {
        EBISystem.builder().textField("filterTableText", "MeetingCall").addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                EBISystem.builder().table("companyMeetings", "MeetingCall").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "MeetingCall").getText()));
            }
        });

        /**
         * ********************************************************************************
         */
        // BEGIN OF TABLE MEETING DOCUMENT
        /**
         * ********************************************************************************
         */
        EBISystem.builder().table("meetingDoc", "MeetingCall").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("meetingDoc", "MeetingCall").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (EBISystem.builder().table("meetingDoc", "MeetingCall").getSelectedRow() != -1) {
                    selectedDocRow = EBISystem.builder().table("meetingDoc", "MeetingCall")
                            .convertRowIndexToModel(EBISystem.builder().table("meetingDoc", "MeetingCall").getSelectedRow());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("showMeetingDoc", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("deleteMeetingDoc", "MeetingCall").setEnabled(false);
                } else if (!tabmeetingDoc.data[selectedDocRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("showMeetingDoc", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("deleteMeetingDoc", "MeetingCall").setEnabled(true);
                }
            }
        });

        /**
         * ********************************************************************************
         */
        // BEGIN OF TABLE MEETING CONTACT
        /**
         * ********************************************************************************
         */
        EBISystem.builder().table("meetingContact", "MeetingCall").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("meetingContact", "MeetingCall").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getLeadSelectionIndex() != -1) {
                    selectedContactRow = EBISystem.builder().table("meetingContact", "MeetingCall").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("deleteMeetingContact", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("editMeetingContact", "MeetingCall").setEnabled(false);
                } else if (!"".equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
                    EBISystem.builder().button("deleteMeetingContact", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("editMeetingContact", "MeetingCall").setEnabled(true);
                }
            }
        });

        /**
         * ********************************************************************************
         */
        // BEGIN OF TABLE AVAILABLE MEETING 
        /**
         * ********************************************************************************
         */
        EBISystem.builder().table("companyMeetings", "MeetingCall").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("companyMeetings", "MeetingCall").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                selectedProtocolRow = 0;
                if (EBISystem.builder().table("companyMeetings", "MeetingCall").getSelectedRow() != -1) {
                    selectedProtocolRow = EBISystem.builder().table("companyMeetings", "MeetingCall").
                            convertRowIndexToModel(EBISystem.builder().table("companyMeetings", "MeetingCall").getSelectedRow());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editMeeting", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("deleteMeeting", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("historyMeeting", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("reportMeeting", "MeetingCall").setEnabled(false);
                    EBISystem.builder().button("copyMeeting", "MeetingCall").setEnabled(false);
                } else if (!tableModel.data[selectedProtocolRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editMeeting", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("deleteMeeting", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("historyMeeting", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("reportMeeting", "MeetingCall").setEnabled(true);
                    EBISystem.builder().button("copyMeeting", "MeetingCall").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("companyMeetings", "MeetingCall").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedProtocolRow = selRow;
                editMeeting();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedProtocolRow = selRow;
                editMeeting();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedProtocolRow = selRow;
                if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tableModel.data[selectedProtocolRow][0].toString())) {
                    return;
                }
                editMeeting();
            }
        });

        EBISystem.builder().table("companyMeetings", "MeetingCall").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                if (EBISystem.builder().table("companyMeetings", "MeetingCall").rowAtPoint(e.getPoint()) != -1) {
                    selectedProtocolRow = EBISystem.builder().table("companyMeetings", "MeetingCall").convertRowIndexToModel(EBISystem.builder().table("companyMeetings", "MeetingCall").rowAtPoint(e.getPoint()));
                    if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tableModel.data[selectedProtocolRow][0].toString())) {
                        return;
                    }
                    editMeeting();
                }
            }
        });
    }

    public void initialize(boolean reload) {

        if (reload) {
            tabModelContact = new ModelCRMContact(ModelCRMContact.MEETING_CONTACT);
            tableModel = new ModelCRMProtocol();
            tabmeetingDoc = new ModelDoc();

            EBISystem.builder().table("meetingDoc", "MeetingCall").setModel(tabmeetingDoc);
            EBISystem.builder().table("meetingContact", "MeetingCall").setModel(tabModelContact);
            EBISystem.builder().table("companyMeetings", "MeetingCall").setModel(tableModel);
        }

        EBISystem.builder().vpanel("MeetingCall").setCreatedDate(EBISystem.getInstance().getDateToString(new java.util.Date()));
        EBISystem.builder().vpanel("MeetingCall").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("MeetingCall").setChangedDate("");
        EBISystem.builder().vpanel("MeetingCall").setChangedFrom("");

        EBISystem.builder().textField("subjectMeetingText", "MeetingCall").setText("");
        EBISystem.builder().combo("meetingTypeText", "MeetingCall").setEditable(true);
        EBISystem.builder().combo("meetingTypeText", "MeetingCall").setModel(new DefaultComboBoxModel(art));

        EBISystem.builder().textArea("meetingDescription", "MeetingCall").setText("");

        EBISystem.builder().timePicker("dateMeetingText", "MeetingCall").setDate(null);
        EBISystem.builder().timePicker("dateMeetingText", "MeetingCall").setFormats(EBISystem.DateFormat);
    }

    public boolean saveMeeting() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("MeetingCall");
        Integer id = dataMeetingControl.dataStore();
        dataMeetingControl.dataShow(id);
        dataMeetingControl.dataShowContact();
        dataMeetingControl.dataShowDoc();
        return true;
    }

    public void copyMeeting() {
        if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedProtocolRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("MeetingCall");
        Integer id = dataMeetingControl.dataCopy(Integer.parseInt(tableModel.data[selectedProtocolRow][4].toString()));
        dataMeetingControl.dataEdit(id);
        dataMeetingControl.dataShow(id);
        dataMeetingControl.dataShowContact();
        dataMeetingControl.dataShowDoc();
        dataMeetingControl.isEdit = true;
    }

    public void editMeeting() {
        if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedProtocolRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("MeetingCall");
        dataMeetingControl.dataNew();
        dataMeetingControl.dataEdit(Integer.parseInt(tableModel.data[selectedProtocolRow][4].toString()));
        dataMeetingControl.dataShowContact();
        dataMeetingControl.dataShowDoc();
        dataMeetingControl.isEdit = true;
    }

    public void deleteMeeting() {
        if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedProtocolRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("MeetingCall");
            dataMeetingControl.dataDelete(Integer.parseInt(tableModel.data[selectedProtocolRow][4].toString()));
            dataMeetingControl.dataNew();
            dataMeetingControl.dataShow(-1);
            dataMeetingControl.dataShowContact();
            dataMeetingControl.dataShowDoc();
            dataMeetingControl.isEdit = false;
        }
    }

    public void newMeeting() {
        EBISystem.showInActionStatus("MeetingCall");
        dataMeetingControl.dataNew();
        dataMeetingControl.dataShow(-1);
        dataMeetingControl.dataShowContact();
        dataMeetingControl.dataShowDoc();
        dataMeetingControl.isEdit = false;
    }

    private boolean validateInput() {
        boolean ret = true;
        if (EBISystem.builder().combo("meetingTypeText", "MeetingCall").getSelectedIndex() == 0) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_SELECT_REPORT_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (EBISystem.builder().textField("subjectMeetingText", "MeetingCall").getText().trim().equals("")) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SELECT_VALID_REF")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if (dataMeetingControl.isEdit == false) {
            for (int i = 0; i < tableModel.data.length; i++) {
                if (tableModel.data[i][1].toString().equals(EBISystem.builder().textField("subjectMeetingText", "MeetingCall").getText())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_MEETING_DUPLICATE_RECORD_EXIST")).Show(EBIMessage.ERROR_MESSAGE);
                    ret = false;
                }
            }
        }
        return ret;
    }

    public void addContact() {
        dataMeetingControl.dataAddContact();
        EBISystem.showInActionStatus("MeetingCall");
        dataMeetingControl.dataShowContact();
    }

    public void editContact() {
        if (selectedContactRow < 0 || "".
                equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
            return;
        }
        dataMeetingControl.dataEditContact(tabModelContact.getId(selectedContactRow));
        EBISystem.showInActionStatus("MeetingCall");
        dataMeetingControl.dataShowContact();
    }

    public void deleteContact() {
        if (selectedContactRow < 0 ||"".
                equals(tabModelContact.getValueAt(selectedContactRow, 0))) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataMeetingControl.dataDeleteContact(tabModelContact.getId(selectedContactRow));
            EBISystem.showInActionStatus("MeetingCall");
            dataMeetingControl.dataShowContact();
        }
    }

    public void newDocs() {
        dataMeetingControl.dataNewDoc();
        dataMeetingControl.dataShowDoc();
    }

    public void showDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabmeetingDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        dataMeetingControl.dataEditDoc(Integer.parseInt(tabmeetingDoc.data[selectedDocRow][3].toString()));
    }

    public void deleteDocs() {
        if (selectedDocRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tabmeetingDoc.data[selectedDocRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            dataMeetingControl.dataDeleteDoc(Integer.parseInt(tabmeetingDoc.data[selectedDocRow][3].toString()));
            EBISystem.showInActionStatus("MeetingCall");
            dataMeetingControl.dataShowDoc();
        }
    }

    public void reportMeeting() {
        if (selectedProtocolRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                equals(tableModel.data[selectedProtocolRow][0].toString())) {
            return;
        }
        boolean pass;
        if (EBISystem.getInstance().getIEBISystemUserRights().isCanPrint()
                || EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()) {
            pass = true;
        } else {
            pass = EBISystem.getInstance().getIEBISecurityInstance().secureModule();
        }
        if (pass) {

            if (dataMeetingControl.meetingProtocol.getCompanymeetingcontactses() != null
                    && dataMeetingControl.meetingProtocol.getCompanymeetingcontactses().size() == 0) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INSERT_CONTACT")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
            dataMeetingControl.dataShowReport(Integer.parseInt(tableModel.data[selectedProtocolRow][4].toString()));
        }
    }

    public void historyMeeting() {
        new EBICRMHistoryView(EBISystem.getModule().hcreator.retrieveDBHistory(EBISystem.getInstance().getCompany().getCompanyid(), "Meeting")).setVisible();
    }

}
