package ebiCRM.gui.dialogs;

import ebiCRM.functionality.EBIExportCSV;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBIGUIRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * CSV Export Dialog
 */
public class EBIExportDialog {

    private IEBIGUIRenderer guiRenderer = null;
    private boolean ret = true;

    public EBIExportDialog() {
        guiRenderer = EBISystem.gui();
    }

    /**
     * Show and Initialize the CSV export dialog
     */
    public void setVisible() {
        guiRenderer.loadGUI("CRMDialog/exportDataDialog.xml");
        guiRenderer.dialog("dataExportDialog").setTitle(EBISystem.i18n("EBI_LANG_EXPORT_DIALOG"));
        guiRenderer.label("selectDBTable", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_EXPORT_DATABASE_TABLE"));
        guiRenderer.label("exportPath", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_EXPORT_EXPORT_PATH"));
        guiRenderer.label("delimiter", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_EXPORT_DELIMITER"));
        guiRenderer.label("sortLabel", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_SORT"));
        guiRenderer.getProgressBar("exportProgress", "dataExportDialog").setString(EBISystem.i18n("EBI_LANG_EXPORT_VALUE"));
        guiRenderer.getProgressBar("exportProgress", "dataExportDialog").setStringPainted(true);
        guiRenderer.getList("databaseTableList", "dataExportDialog").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        guiRenderer.combo("sortFieldsNameText", "dataExportDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
        guiRenderer.getList("databaseTableList", "dataExportDialog").
                addListSelectionListener(new ListSelectionListener() {
                    @Override
					public void valueChanged(final ListSelectionEvent evt) {
                        fillFieldList();
                    }
                });

        guiRenderer.getList("fieldListText", "dataExportDialog").
                addListSelectionListener(new ListSelectionListener() {
                    @Override
					public void valueChanged(final ListSelectionEvent evt) {
                        fillFieldCombobox();
                    }
                });
        fillList();
        guiRenderer.combo("sortCSVText", "dataExportDialog").addItem(EBISystem.i18n("EBI_LANG_SORT_ASCENDING_SORT"));
        guiRenderer.combo("sortCSVText", "dataExportDialog").addItem(EBISystem.i18n("EBI_LANG_SORT_DISCENDING_SORT"));

        guiRenderer.button("browsButton", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_EXPORT"));
        guiRenderer.button("browsButton", "dataExportDialog").addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                final File dir = EBISystem.getInstance().getOpenDialog(JFileChooser.DIRECTORIES_ONLY);
                if (dir != null) {
                    guiRenderer.textField("exportPathText", "dataExportDialog").setText(dir.getPath());
                }
            }
        });

        guiRenderer.button("exportButton", "dataExportDialog").addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                if (!validateInputs()) {
                    return;
                }
                exportThread();
            }
        });

        guiRenderer.button("closeExportDialog", "dataExportDialog").setText(EBISystem.i18n("EBI_LANG_CLOSE"));
        guiRenderer.button("closeExportDialog", "dataExportDialog").addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(final ActionEvent e) {
                guiRenderer.dialog("dataExportDialog").setVisible(false);
            }
        });

        guiRenderer.showGUI();

    }

    /**
     * Start thread for export a CSV file
     */
    private void exportThread() {

        final Runnable waitRunner = new Runnable() {

            @Override
			public void run() {

                guiRenderer.getProgressBar("exportProgress", "dataExportDialog").setIndeterminate(true);
                final EBIExportCSV export = new EBIExportCSV();

                try {

                    final String tableName = guiRenderer.getList("databaseTableList", "dataExportDialog").getSelectedValue().toString();

                    ret = export.exportCVS(guiRenderer.textField("exportPathText", "dataExportDialog").getText(),
                            tableName,
                            guiRenderer.getList("fieldListText", "dataExportDialog").getSelectedValues(),
                            guiRenderer.combo("sortFieldsNameText", "dataExportDialog").getSelectedItem().toString(),
                            guiRenderer.combo("sortCSVText", "dataExportDialog").getSelectedIndex() == 0 ? true : false,
                            guiRenderer.textField("delimiterText", "dataExportDialog").getText());

                } catch (final IOException ex) {
                    ret = false;
                    EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                } finally {

                    guiRenderer.getProgressBar("exportProgress", "dataExportDialog").setIndeterminate(false);
                    if (ret) {
                        EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_EXPORT_WAS_SUCCESSFULLY")).Show(EBIMessage.INFO_MESSAGE);
                    } else {
                        return;
                    }
                }
            }
        };

        final Thread loaderThread = new Thread(waitRunner, "ExportCSV");
        loaderThread.start();
    }

    private boolean validateInputs() {

        if (guiRenderer.getList("databaseTableList", "dataExportDialog").isSelectionEmpty()) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_VALUE_FROM_TABLE_LIST")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (guiRenderer.getList("fieldListText", "dataExportDialog").isSelectionEmpty()) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_VALUE_FROM_FIELD_LIST")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if ("".equals(guiRenderer.textField("delimiterText", "dataExportDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_DELIMITED_SHOULD_NOT_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if ("".equals(guiRenderer.textField("exportPathText", "dataExportDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_EXPORT_DIRECTORY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if ("".equals(guiRenderer.combo("sortFieldsNameText", "dataExportDialog").getSelectedItem()) ||
                EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(guiRenderer.combo("sortFieldsNameText", "dataExportDialog").getSelectedItem())) {

            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_ORDER_FIELD")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void fillList() {
        final DefaultListModel listModel = new DefaultListModel();

        try {
            // Gets the database metadata
            final DatabaseMetaData dbmd = EBISystem.getInstance().iDB().getActiveConnection().getMetaData();

            final String[] types = {"TABLE"};
            final ResultSet resultSet = dbmd.getTables(null, null, "%", types);

            while (resultSet.next()) {
                if (!"EBIUSER".equals(resultSet.getString(3).toUpperCase()) && !"MAIL_ACCOUNT".equals(resultSet.getString(3).toUpperCase())
                        && !"MAIL_DELETED".equals(resultSet.getString(3).toUpperCase()) && !"MAIL_INBOX".equals(resultSet.getString(3).toUpperCase())
                        && !"MAIL_OUTBOX".equals(resultSet.getString(3).toUpperCase()) && !"SET_REPORTFORMODULE".equals(resultSet.getString(3).toUpperCase()) && !"SET_REPORTPARAMETER".equals(resultSet.getString(3).toUpperCase())
                        && !"EBIPESSIMISTIC".equals(resultSet.getString(3).toUpperCase()) && !"EBIDATASTORE".equals(resultSet.getString(3).toUpperCase())) {
                    listModel.addElement(resultSet.getString(3).toUpperCase());
                }
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }

        guiRenderer.getList("databaseTableList", "dataExportDialog").setModel(listModel);
    }

    private void fillFieldCombobox() {
        guiRenderer.combo("sortFieldsNameText", "dataExportDialog").setModel(new DefaultComboBoxModel(guiRenderer.getList("fieldListText", "dataExportDialog").getSelectedValues()));
    }

    private void fillFieldList() {

        final DefaultListModel listModel = new DefaultListModel();
        try {
            // Gets the database metadata
            final DatabaseMetaData dbmd = EBISystem.getInstance().iDB().getActiveConnection().getMetaData();
            final ResultSet resultSet = dbmd.getColumns(null, null,
                    guiRenderer.getList("databaseTableList", "dataExportDialog").getSelectedValue().toString(), null);

            while (resultSet.next()) {
                listModel.addElement(resultSet.getString("COLUMN_NAME").toUpperCase());
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        guiRenderer.getList("fieldListText", "dataExportDialog").setModel(listModel);
    }

    public String[] getColumnNames(final ResultSet rs) throws SQLException {

        if (rs == null) {
            return null;
        }
        final ResultSetMetaData rsMetaData = rs.getMetaData();
        final int numberOfColumns = rsMetaData.getColumnCount();
        final String[] columnName = new String[numberOfColumns + 1];
        for (int i = 1; i < numberOfColumns; i++) {
            columnName[i - 1] = rsMetaData.getColumnName(i);
        }
        return columnName;
    }
}