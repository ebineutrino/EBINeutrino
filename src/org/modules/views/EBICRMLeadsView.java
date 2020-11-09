package org.modules.views;

import org.modules.controls.ControlLeads;
import org.core.gui.callbacks.EBIUICallback;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIExtendedPanel;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIAbstractTableModel;
import org.sdk.utils.EBIPropertiesRW;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class EBICRMLeadsView {

    @Getter
    @Setter
    private ControlLeads controlLeads = null;
    @Getter
    @Setter
    private int selectedRow = -1;
    @Getter
    @Setter
    private EBIAbstractTableModel tabModel = null;

    public EBICRMLeadsView() {
        controlLeads = new ControlLeads();
    }

    public void initialize(boolean reload) {
        tabModel = (EBIAbstractTableModel) EBISystem.gui().table("leadsTable", "Leads").getModel();
        EBISystem.gui().label("compNameLabel", "Leads").setFont(new Font("Arial", Font.BOLD, 18));
        EBISystem.gui().label("cName", "Leads").setFont(new Font("Arial", Font.BOLD, 10));
        
        try {
            if(reload){
                if (!"null".equals(EBIPropertiesRW.getEBIProperties().getValue("LEADSSEARCH_TEXT")) && !"".equals(EBIPropertiesRW.getEBIProperties().getValue("LEADSSEARCH_TEXT"))) {
                    EBISystem.gui().textField("searchLeadsText", "Leads").setText(EBIPropertiesRW.getEBIProperties().getValue("LEADSSEARCH_TEXT"));
                    controlLeads.dataShow(EBIPropertiesRW.getEBIProperties().getValue("LEADSSEARCH_TEXT"));
                } else {
                    controlLeads.dataShow(-1);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        

        EBISystem.gui().vpanel("Leads").setID(-1);
        EBISystem.gui().vpanel("Leads").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Leads").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Leads").setChangedDate("");
        EBISystem.gui().vpanel("Leads").setChangedFrom("");
        EBISystem.gui().combo("genderText", "Leads").setModel(new DefaultComboBoxModel(EBICRMContactView.gendersList));
        EBISystem.gui().combo("genderText", "Leads").setEditable(true);
        EBISystem.gui().combo("classificationText", "Leads").setModel(new DefaultComboBoxModel(EBICRMCompanyView.classification));

        EBISystem.gui().combo("genderText", "Leads").getEditor().setItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
        EBISystem.gui().textField("titleText", "Leads").setText("");
        EBISystem.gui().textField("positionText", "Leads").setText("");
        EBISystem.gui().textField("contactNameText", "Leads").setText("");
        EBISystem.gui().textField("contactSurnameText", "Leads").setText("");
        EBISystem.gui().textField("telephoneText", "Leads").setText("");
        EBISystem.gui().textField("faxText", "Leads").setText("");
        EBISystem.gui().textField("emailText", "Leads").setText("");
        EBISystem.gui().textField("contactMobileText", "Leads").setText("");

        EBISystem.gui().textField("addressStrNrText", "Leads").setText("");
        EBISystem.gui().textField("addressZipText", "Leads").setText("");
        EBISystem.gui().textField("addressCityText", "Leads").setText("");
        EBISystem.gui().textField("addressCountryText", "Leads").setText("");

        EBISystem.gui().textField("compNameText", "Leads").setText("");
        EBISystem.gui().textField("internetText", "Leads").setText("");
        EBISystem.gui().combo("classificationText", "Leads").setSelectedIndex(0);
        EBISystem.gui().textArea("descriptionText", "Leads").setText("");

        EBISystem.gui().label("cName", "Leads").setText("");
        EBISystem.gui().label("addressLabel", "Leads").setText("");
        EBISystem.gui().label("zipLocationLabel", "Leads").setText("");
        EBISystem.gui().label("phoneLabel", "Leads").setText("");
        EBISystem.gui().label("faxLabel", "Leads").setText("");
        EBISystem.gui().label("mobileLabel", "Leads").setText("");
        EBISystem.gui().label("emailLabel", "Leads").setText("");
        EBISystem.gui().label("compNameLabel", "Leads").setText("");
        EBISystem.gui().label("webLabel", "Leads").setText("");
        EBISystem.gui().label("positionLabel", "Leads").setText("");

        final EBIExtendedPanel businessCard = new EBIExtendedPanel("", "people.png");
        businessCard.setSize(EBISystem.gui().getPanel("businessCard", "Leads").getSize());
        businessCard.setBackground(EBISystem.gui().getPanel("businessCard", "Leads").getBackground());
        businessCard.setBorder(BorderFactory.createLineBorder(Color.gray));
        businessCard.repaint();

        EBISystem.gui().getPanel("businessCard", "Leads").add(businessCard, null);
        EBISystem.gui().getPanel("businessCard", "Leads").invalidate();
        EBISystem.gui().getPanel("businessCard", "Leads").repaint();

    }

    public void initializeAction() {

        EBISystem.gui().table("leadsTable", "Leads").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.gui().table("leadsTable", "Leads").addSelectionListener(new EBIUICallback() {
            @Override
            public void selectionListenerEvent(ListSelectionEvent e) {
                super.selectionListenerEvent(e);
                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (EBISystem.gui().table("leadsTable", "Leads").getSelectedRow() != -1
                        && EBISystem.gui().table("leadsTable", "Leads")
                                .getSelectedRow() < tabModel.data.length) {

                    selectedRow = EBISystem.gui().table("leadsTable", "Leads")
                            .convertRowIndexToModel(EBISystem.gui().table("leadsTable", "Leads")
                                    .getSelectedRow());

                    if (selectedRow < tabModel.data.length) {
                        if (lsm.isSelectionEmpty()) {
                            EBISystem.gui().button("editLeads", "Leads").setEnabled(false);
                            EBISystem.gui().button("deleteLeads", "Leads").setEnabled(false);
                            EBISystem.gui().button("openComp", "Leads").setEnabled(false);
                            EBISystem.gui().button("copyLeads", "Leads").setEnabled(false);
                        } else if (!tabModel.data[selectedRow][0].toString()
                                .equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                            EBISystem.gui().button("editLeads", "Leads").setEnabled(true);
                            EBISystem.gui().button("deleteLeads", "Leads").setEnabled(true);
                            EBISystem.gui().button("openComp", "Leads").setEnabled(true);
                            EBISystem.gui().button("copyLeads", "Leads").setEnabled(true);

                        }
                    }
                }
            }
        });

        EBISystem.gui().table("leadsTable", "Leads").addKeyAction(new EBIUICallback() {
            @Override
            public void tableKeyUp(int selRow) {
                super.tableKeyUp(selRow);
                selectedRow = selRow;
                if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString())) {
                    return;
                }
                editLead();
            }

            @Override
            public void tableKeyDown(int selRow) {
                super.tableKeyDown(selRow);
                selectedRow = selRow;
                if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString())) {
                    return;
                }
                editLead();
            }

            @Override
            public void tableKeyEnter(int selRow) {
                super.tableKeyEnter(selRow);
                selectedRow = selRow;
                if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                        .equals(tabModel.data[selectedRow][0].toString())) {
                    return;
                }
                editLead();
            }
        });

        EBISystem.gui().table("leadsTable", "Leads").setMouseCallback(new MouseAdapter() {
            @Override
            public void mouseReleased(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().table("leadsTable", "Leads").getSelectedRow() != -1) {
                    selectedRow = EBISystem.gui().table("leadsTable", "Leads")
                            .convertRowIndexToModel(EBISystem.gui().table("leadsTable", "Leads").getSelectedRow());
                    if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
                        return;
                    }
                    editLead();
                }
            }
        });

        EBISystem.gui().textField("searchLeadsText", "Leads").addFocusListener(new FocusListener() {
            @Override
            public void focusLost(final FocusEvent e) {
                EBIPropertiesRW.getEBIProperties().setValue("LEADSSEARCH_TEXT", EBISystem.gui().textField("searchLeadsText", "Leads").getText());
                EBIPropertiesRW.getEBIProperties().saveEBINeutrinoProperties();
            }

            @Override
            public void focusGained(final FocusEvent e) {
            }
        });

        EBISystem.gui().textField("searchLeadsText", "Leads").addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(final KeyEvent e) {
                controlLeads.dataShow(((JTextField) e.getSource()).getText());
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectedRow = 0;
                    EBISystem.gui().table("leadsTable", "Leads").changeSelection(0, 0, false, false);
                    if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
                        return;
                    }
                    editLead();
                    EBISystem.gui().table("leadsTable", "Leads").requestFocus();

                }
            }

            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
            }
        });
    }

    public boolean saveLeads() {
        if (!validateInput()) {
            return false;
        }
        EBISystem.showInActionStatus("Leads");
        int row = EBISystem.gui().table("leadsTable", "Leads").getSelectedRow();
        Integer id = controlLeads.dataStore();

        if ("".equals(EBISystem.gui().textField("searchLeadsText", "Leads").getText())) {
            controlLeads.dataShow(id);
        } else {
            controlLeads.dataShow(EBISystem.gui().textField("searchLeadsText", "Leads").getText());
        }
        EBISystem.gui().table("leadsTable", "Leads").changeSelection(row, 0, false, false);
        editLead();
        return true;
    }

    public void deleteLead() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.WARNING_MESSAGE_YESNO) == true) {
            EBISystem.showInActionStatus("Leads");
            controlLeads.dataDelete(Integer.parseInt(tabModel.data[selectedRow][11].toString()));
            controlLeads.dataNew(true);
            controlLeads.dataShow(-1);
            if (EBISystem.getInstance().getCompany() != null) {
                EBISystem.getModule().resetUI(false, true);
            }
            controlLeads.isEdit = false;
        }
    }

    public void editLead() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        int row = EBISystem.gui().table("leadsTable", "Leads").getSelectedRow();
        EBISystem.showInActionStatus("Leads");
        controlLeads.dataNew(false);
        controlLeads.dataEdit(Integer.parseInt(tabModel.data[selectedRow][11].toString()));
        EBISystem.gui().table("leadsTable", "Leads").changeSelection(row, 0, false, false);
    }

    public void newLead() {
        EBISystem.gui().textField("compNameText", "Leads").requestFocus();
        EBISystem.showInActionStatus("Leads");
        controlLeads.dataNew(true);
        controlLeads.isEdit = false;
    }

    public void copyLead() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        EBISystem.showInActionStatus("Leads");
        Integer id = controlLeads.dataCopy(Integer.parseInt(tabModel.data[selectedRow][11].toString()));
        controlLeads.dataEdit(id);
        controlLeads.dataShow(id);
        controlLeads.isEdit = true;
    }

    public void openCompany() {
        if (selectedRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT")
                .equals(tabModel.data[selectedRow][0].toString())) {
            return;
        }
        EBISystem.getModule().createUI(Integer.parseInt(tabModel.data[selectedRow][11].toString()), true);
        EBISystem.getInstance().getIEBIContainerInstance().setSelectedTab(2);
    }

    private boolean validateInput() {
        boolean ret = true;
        if ("".equals(EBISystem.gui().textField("compNameText", "Leads").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_INSERT_NAME")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        } else if ("".equals(EBISystem.gui().combo("genderText", "Leads").getSelectedItem())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_SELECT_GENDER")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }
        return ret;
    }
}
