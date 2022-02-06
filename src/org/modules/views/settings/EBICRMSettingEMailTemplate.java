package org.modules.views.settings;

import org.modules.EBIModule;
import org.modules.models.ModelTemplate;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBICRMSettingEMailTemplate {

    private ModelTemplate tabModel = null;
    private boolean isSaveOrUpdate = false;
    private int templateID = 0;
    private int selRow = -1;
    private JEditorPane jEditorMessageView = null;

    public EBICRMSettingEMailTemplate() {
        tabModel = new ModelTemplate();
        EBISystem.gui().loadGUI("CRMDialog/emailTemplateDialog.xml");
        initHTMLView();
        EBISystem.gui().getPanel("emailTemplate", "emailTemplateDialog").setLayout(new BorderLayout());
        EBISystem.gui().getPanel("emailTemplate", "emailTemplateDialog").add(jEditorMessageView, BorderLayout.CENTER);
        availableEmailTemplate();
    }

    public void setVisible() {
        EBISystem.gui().dialog("emailTemplateDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_EMAIL_TEMPLATE_SETTING"));
        EBISystem.gui().vpanel("emailTemplateDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_EMAIL_TEMPLATE_SETTING"));

        EBISystem.gui().label("name", "emailTemplateDialog").setText(EBISystem.i18n("EBI_LANG_NAME"));
        EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").setText(EBISystem.i18n("EBI_LANG_ACTIVE"));

        EBISystem.gui().button("saveValue", "emailTemplateDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.gui().button("saveValue", "emailTemplateDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (isSaveOrUpdate == false) {
                    saveTemplate();
                } else {
                    updateTemplate();
                }
                availableEmailTemplate();
            }
        });

        EBISystem.gui().button("loadHTMLTemplate", "emailTemplateDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                final StringBuffer htmlFile = new StringBuffer();

                final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
                if (fs != null) {
                    try {
                        final FileReader reader = new FileReader(fs);
                        final BufferedReader in = new BufferedReader(reader);
                        String string;

                        while ((string = in.readLine()) != null) {
                            htmlFile.append(string);
                        }
                        in.close();
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }

                    jEditorMessageView.setText(htmlFile.toString());

                }
            }
        });

        EBISystem.gui().button("newBnt", "emailTemplateDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.gui().button("newBnt", "emailTemplateDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newTemplate();
            }
        });

        EBISystem.gui().button("editBnt", "emailTemplateDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.gui().button("editBnt", "emailTemplateDialog").setEnabled(false);
        EBISystem.gui().button("editBnt", "emailTemplateDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!tabModel.data[selRow][0].toString().equals(EBISystem.i18n("EBI_LANG_C_NO_TEMPLATE"))) {
                    showTemplate();
                }
            }
        });

        EBISystem.gui().button("deleteBnt", "emailTemplateDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.gui().button("deleteBnt", "emailTemplateDialog").setEnabled(false);
        EBISystem.gui().button("deleteBnt", "emailTemplateDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                deleteTemplate();
            }
        });

        EBISystem.gui().table("taxValueTable", "emailTemplateDialog").setModel(tabModel);
        EBISystem.gui().table("taxValueTable", "emailTemplateDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                if (lsm.getMinSelectionIndex() != -1) {
                    selRow = EBISystem.gui().table("taxValueTable", "emailTemplateDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }

                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("editBnt", "emailTemplateDialog").setEnabled(false);
                    EBISystem.gui().button("deleteBnt", "emailTemplateDialog").setEnabled(false);
                } else if (!tabModel.getRow(EBISystem.gui().table("taxValueTable", "emailTemplateDialog").getSelectedRow())[0].toString().equals(EBISystem.i18n("EBI_LANG_C_NO_TEMPLATE"))) {
                    EBISystem.gui().button("editBnt", "emailTemplateDialog").setEnabled(true);
                    EBISystem.gui().button("deleteBnt", "emailTemplateDialog").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("taxValueTable", "emailTemplateDialog").addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {

                    if (!tabModel.data[selRow][0].toString().equals(EBISystem.i18n("EBI_LANG_C_NO_TEMPLATE"))) {
                        showTemplate();
                    }
                }
            }
        });

        EBISystem.gui().showGUI();
    }

    private void saveTemplate() {

        if (!validateInput()) {
            return;
        }

        if (EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").isSelected()) {
            removeIsActiveYetEnabled();
        }

        final String query = "INSERT INTO MAIL_TEMPLATE SET "
                + "SETFROM=?,"
                + "SETDATE=?,"
                + "NAME=?,"
                + "TEMPLATE=?,"
                + "ISACTIVE=? ";
        try {
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(query);
            ps.setString(1, EBISystem.ebiUser);
            ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(3, EBISystem.gui().textField("emailTitle", "emailTemplateDialog").getText());
            ps.setString(4, jEditorMessageView.getText());
            ps.setInt(5, EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").isSelected() == true ? 1 : 0);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            this.newTemplate();
        }
    }

    /**
     * Remove the active flag for others template so we have only one active
     * template
     */
    private void removeIsActiveYetEnabled() {

        final String query = "UPDATE MAIL_TEMPLATE SET ISACTIVE=? WHERE ISACTIVE=1";

        try {
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(query);
            ps.setInt(1, 0);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);
        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    private void updateTemplate() {
        if (!validateInput()) {
            return;
        }
        removeIsActiveYetEnabled();

        final String query = "UPDATE MAIL_TEMPLATE SET "
                + "SETFROM=?,"
                + "SETDATE=?,"
                + "NAME=?,"
                + "TEMPLATE=?,"
                + "ISACTIVE=? "
                + " WHERE ID=?";
        try {
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(query);
            ps.setString(1, EBISystem.ebiUser);
            ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(3, EBISystem.gui().textField("emailTitle", "emailTemplateDialog").getText());
            ps.setString(4, jEditorMessageView.getText());
            ps.setInt(5, EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").isSelected() == true ? 1 : 0);
            ps.setInt(6, this.templateID);
            EBISystem.getInstance().iDB().executePreparedStmt(ps);

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            newTemplate();
        }
    }

    private void deleteTemplate() {
        try {
            if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_DELETE_RECORD")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                templateID = Integer.parseInt(tabModel.data[selRow][3].toString());
                final PreparedStatement prs2 = EBISystem.getInstance().iDB().initPreparedStatement("DELETE FROM MAIL_TEMPLATE WHERE ID=?");
                prs2.setInt(1, templateID);
                EBISystem.getInstance().iDB().executePreparedStmt(prs2);

            }
            newTemplate();
            availableEmailTemplate();

        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showTemplate() {
        templateID = Integer.parseInt(tabModel.data[selRow][3].toString());
        ResultSet set = null;
        try {
            final PreparedStatement prs2 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM MAIL_TEMPLATE WHERE ID=?");
            prs2.setInt(1, templateID);
            set = EBISystem.getInstance().iDB().executePreparedQuery(prs2);

            set.next();
            EBISystem.gui().textField("emailTitle", "emailTemplateDialog").setText(set.getString("NAME"));
            jEditorMessageView.setText(set.getString("TEMPLATE"));
            EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").setSelected(set.getInt("ISACTIVE") == 1 ? true : false);
            this.isSaveOrUpdate = true;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void availableEmailTemplate() {
        ResultSet set = null;
        try {
            final PreparedStatement prs2 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM MAIL_TEMPLATE ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(prs2);
            set.last();
            if (set.getRow() > 0) {
                tabModel.data = new Object[set.getRow()][4];
                set.beforeFirst();
                int i = 0;
                while (set.next()) {
                    tabModel.data[i][0] = set.getInt("ISACTIVE") == 1 ? EBISystem.i18n("EBI_LANG_YES") : EBISystem.i18n("EBI_LANG_NO");
                    tabModel.data[i][1] = set.getString("NAME");
                    tabModel.data[i][2] = set.getString("TEMPLATE") == null ? "" : set.getString("TEMPLATE");
                    tabModel.data[i][3] = set.getString("ID");
                    i++;
                }
            } else {
                tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_C_NO_TEMPLATE"), "", "", ""}};
            }
            tabModel.fireTableDataChanged();

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newTemplate() {
        EBISystem.gui().textField("emailTitle", "emailTemplateDialog").setText("");
        jEditorMessageView.setText("");
        EBISystem.gui().getCheckBox("activeTemplate", "emailTemplateDialog").setSelected(false);
        this.isSaveOrUpdate = false;
        this.templateID = 0;
    }

    private boolean validateInput() {
        final boolean ret = true;

        if ("".equals(EBISystem.gui().textField("emailTitle", "emailTemplateDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_TEMPLATE_NAME_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(jEditorMessageView.getText())) {
            EBIExceptionDialog.getInstance("EBI_LANG_C_ERROR_TEMPLATE_TEXT_EMPTY").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return ret;
    }

    public void initHTMLView() {
        try {
            jEditorMessageView = new JEditorPane();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
