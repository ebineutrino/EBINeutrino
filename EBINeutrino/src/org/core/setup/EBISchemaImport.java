package org.core.setup;

import org.core.EBIDatabase;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialog;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.Encrypter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EBISchemaImport extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JButton cancelButton = null;
    private JButton importButton = null;
    private JLabel statusText = null;
    private JProgressBar progressBar = null;
    private long availableLine = 0;
    private double i = 0;
    private StringBuffer toImport = new StringBuffer();
    private boolean failed = true;
    private String dbType = "";
    private final StringBuilder errorReport = new StringBuilder();
    private String catalog = "";
    private boolean useUpperCase = false;

    public EBISchemaImport(final String databaseType,
            final String catalogDB, final boolean upperCase) {
        super();
        EBIDatabase.toUpperCase = upperCase;
        useUpperCase = upperCase;
        dbType = databaseType;
        initialize();
        setTitle("EBI Neutrino database schema import");
        setResizable(false);
        setModal(true);
        setName("EBISchemaImport");
        catalog = catalogDB;
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = getSize();
        setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
    }

    private void initialize() {
        this.setSize(490, 250);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            statusText = new JLabel();
            statusText.setBounds(new Rectangle(11, 50, 455, 20));
            statusText.setText("Table:");
            final JLabel jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(132, 9, 266, 28));
            jLabel.setText("EBI Neutrino Database Schema Import");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonCancel(), null);
            jContentPane.add(getImportButton(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(statusText, null);
            jContentPane.add(getProgressBar(), null);
        }
        return jContentPane;
    }

    private JButton getJButtonCancel() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setBounds(new Rectangle(360, 125, 110, 30));
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return cancelButton;
    }

    private JButton getImportButton() {
        if (importButton == null) {
            importButton = new JButton();
            importButton.setBounds(new Rectangle(235, 125, 120, 30));
            importButton.setText("Import");
            importButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!importSQLSchema("mysql.sql")) {
                        EBIExceptionDialog.getInstance(EBISchemaImport.this,
                                "Import SQL schema was not successfully, the file format is damage!").Show(EBIMessage.ERROR_MESSAGE);
                    }
                }
            });
        }
        return importButton;
    }

    private boolean importSQLSchema(final String fileName) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                importButton.setEnabled(false);
                cancelButton.setEnabled(false);
                BufferedReader br = null;
                try {
                    if (useUpperCase) {
                        catalog = catalog.toUpperCase();
                    } else {
                        catalog = catalog.toLowerCase();
                    }

                    EBISystem.db().execExt("CREATE DATABASE IF NOT EXISTS " + catalog);
                    EBISystem.db().getActiveConnection().setCatalog(catalog);
                    errorReport.append("\n");

                    FileReader fr = new FileReader(new File(getClass().getClassLoader().getResource("sql/" + fileName).getFile()));
                    br = new BufferedReader(fr);

                    availableLine = br.lines().count();

                    boolean isFirstLine = true;
                    String line = "";
                    while ((line = br.readLine()) != null) {

                        // show the first line to the import dialog label
                        if (isFirstLine) {
                            String firstLine = line;
                            final String tableShow = firstLine.replace("(", "").replaceAll("create table if not exists", "create table");
                            statusText.setText("Table:" + tableShow);
                        }

                        if (!"/".equals(line.trim())) { // Not import until end of sql statement
                            i += line.length() + 1;
                            toImport.append(line + "\n");
                            // Calculate available line in %
                            progressBar.setValue(((int) ((i / availableLine) * 100)));
                            isFirstLine = false;
                        } else if (!"".equals(line.trim())) { // end of create sql statement import to db
                            // SQL Import Table
                            if (toImport.toString().length() > 1) {
                                EBISystem.db().execExt(toImport.toString().toUpperCase());
                                errorReport.append("\n");
                                toImport = new StringBuffer();
                                isFirstLine = true;
                            } else {
                                toImport = new StringBuffer();
                                isFirstLine = true;
                            }
                        }
                        i++;
                    }
                    if (!failed) {
                        importButton.setEnabled(true);
                    } else {
                        createAdminUser();
                        importButton.setVisible(false);
                        cancelButton.setText("Finish");
                    }
                    cancelButton.setEnabled(true);
                } catch (final IOException ex) {
                    EBIExceptionDialog.getInstance(EBISchemaImport.this, EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    ex.printStackTrace();
                    errorReport.append(EBISystem.printStackTrace(ex));
                    errorReport.append("\n");
                    failed = false;
                    setVisible(false);
                } catch (SQLException ex) {
                    EBIExceptionDialog.getInstance(EBISchemaImport.this, EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                    ex.printStackTrace();
                    errorReport.append(EBISystem.printStackTrace(ex));
                    errorReport.append("\n");
                    failed = false;
                    setVisible(false);
                } finally {
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (final IOException ex) {
                        EBIExceptionDialog.getInstance(EBISchemaImport.this, EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                        setVisible(false);
                    }
                }
            }
        };
        // Close the input stream
        final Thread thread = new Thread(runnable);
        thread.start();
        return failed;
    }

    /**
     * Insert a default user after import
     */
    private void createAdminUser() {
        try {
            EBISystem.db().getActiveConnection().setCatalog(catalog);

            final String sqlUsr = "INSERT INTO EBIUSER "
                    + "(ID,EBIUSER,PASSWD,CREATEDDATE,CREATEDFROM,IS_ADMIN,CANSAVE,CANPRINT,CANDELETE) "
                    + "VALUES(?,?,?,?,?,?,?,?,?) ";

            final PreparedStatement ps = EBISystem.db().initPreparedStatement(sqlUsr);
            ps.setInt(1, 1);
            ps.setString(2, "root");
            ps.setString(3, generatePassword("ebineutrino"));
            ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(5, "Installer");
            ps.setInt(6, 1);
            ps.setInt(7, 0);
            ps.setInt(8, 0);
            ps.setInt(9, 0);

            EBISystem.db().executePreparedStmt(ps);

        } catch (final SQLException ex) {
            ex.printStackTrace();
            errorReport.append(EBISystem.printStackTrace(ex));
            errorReport.append("\n");
        }

        final EBIDialog repDialog = new EBIDialog(null);
        final JScrollPane panes1 = new JScrollPane();
        repDialog.setModal(true);
        repDialog.setSize(450, 300);
        repDialog.setLocation(getX(), getY());
        repDialog.setTitle("Information dialog");
        repDialog.setName("repDialog");
        final JTabbedPane pane = new JTabbedPane();

        final JPanel panelRep = new JPanel();
        panelRep.setLayout(new BorderLayout());
        panes1.setViewportView(new JEditorPane("text/html",
                "<b>Installer Info:</b><br>EBI Neutrino Installer has created the default user<br><br>"
                + "<b><font color='red'>User:root</font></b><br>"
                + "<b><font color='red'>Password:ebineutrino</font></b><br><br>"
                + "now you are ready to login!<br><br>SECURITY WARNING: Don't forget to change the root password after login!!<br>"));

        panelRep.add(panes1, BorderLayout.CENTER);

        pane.add("Report", panelRep);
        final JScrollPane panes2 = new JScrollPane();
        final JPanel panelRepError = new JPanel();
        panelRepError.setLayout(new BorderLayout());
        panes2.setViewportView(new JEditorPane("text/html", "".equals(errorReport.toString()) ? "<b>No Errors available</b>" : errorReport.toString()));
        panelRepError.add(panes2, BorderLayout.CENTER);

        pane.add("Error Report", panelRepError);
        repDialog.getContentPane().setLayout(new BorderLayout());
        repDialog.getContentPane().add(pane, BorderLayout.CENTER);
        repDialog.setVisible(true);

    }

    private String generatePassword(final String password) {
        final Encrypter encrypter = new Encrypter("EBINeutrino");
        // Encrypt
        final String pwd = encrypter.encrypt(password);

        return pwd;
    }

    private JProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new JProgressBar();
            progressBar.setBounds(new Rectangle(10, 73, 462, 30));
            progressBar.setMaximum(100);
            progressBar.setMinimum(0);
            progressBar.setStringPainted(true);
        }
        return progressBar;
    }
}
