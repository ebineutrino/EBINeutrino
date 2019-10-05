package org.core.setup;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIImportSQLFiles;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;
import org.sdk.utils.Encrypter;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * Description: This Dialog setup the EBI Neutrino Database
 *
 */
public class EBISetupDB extends JPanel {

    private JTextField jTextUser = null;
    private JTextField jTexPassword = null;
    private JButton jButtonCancel = null;
    private JButton jButtonGenerating = null;
    private JTextField jTextIP = null;
    private JButton jButtonTest = null;
    private EBISetup setup = null;
    private JButton jButtonOK = null;
    private JPopupMenu.Separator jSepara = null;
    private JComboBox jComboDatabaseDriver = null;
    private JComboBox jTextDatabaseType = null;
    private JTextField jTextSIDCatalog = null;
    private JLabel jLabel7 = null;
    private JButton jButtonImportSchema = null;
    private Connection conn = null;
    private String databaseType = "";

    public EBISetupDB(final EBISetup setUp) {
        super();
        setup = setUp;
        initialize();
        jComboDatabaseDriver.addItem("Please select");
    }

    private void initialize() {

        jLabel7 = new JLabel();
        jLabel7.setBounds(new Rectangle(445, 70, 115, 25));
        jLabel7.setText("SID");
        jLabel7.setVisible(false);

        final JLabel jLabel6 = new JLabel();
        jLabel6.setBounds(new Rectangle(15, 70, 125, 25));
        jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel6.setText("Database name:");

        final JLabel jLabel3 = new JLabel();
        jLabel3.setBounds(new Rectangle(15, 100, 125, 25));
        jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel3.setText("Database driver:");

        final JLabel jLabel4 = new JLabel();
        jLabel4.setBounds(new Rectangle(15, 130, 125, 25));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Host/IP:");

        final JLabel jLabel1 = new JLabel();
        jLabel1.setBounds(new Rectangle(15, 160, 125, 25));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("User:");

        final JLabel jLabel2 = new JLabel();
        jLabel2.setBounds(new Rectangle(15, 190, 125, 25));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Password:");

        final JLabel jLabel = new JLabel();
        jLabel.setBounds(new Rectangle(100, 10, 315, 35));
        jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        jLabel.setText("EBI Neutrino database setup");

        final JLabel jLabel5 = new JLabel();
        jLabel5.setBounds(new Rectangle(20, 5, 80, 50));
        jLabel5.setIcon(EBISystem.getInstance().getIconResource("/Icon/spire.png"));
        jLabel5.setText("");

        this.setLayout(null);
        this.setSize(570, 360);
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(jLabel2, null);
        this.add(getJTextUser(), null);
        this.add(getJTexPassword(), null);
        this.add(getJButtonCancel(), null);
        this.add(getJButtonGenerating(), null);
        this.add(jLabel4, null);
        this.add(getJTextIP(), null);
        this.add(getJButtonTest(), null);
        this.add(jLabel5, null);
        this.add(getJButtonOK(), null);
        this.add(getJSepara(), null);
        this.add(jLabel3, null);
        this.add(getJComboDatabaseDriver(), null);
        this.add(jLabel6, null);
        this.add(getJTextDatabaseName(), null);
        this.add(getJTextOracleSID(), null);
        this.add(jLabel7, null);
        this.add(getJButtonImportSchema(), null);
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        // Draw bg top
        final Color startColor = new Color(34, 34, 34);
        final Color endColor = new Color(34, 34, 34);

        // A non-cyclic gradient
        final GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 250, endColor);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), 60);

        final Color sColor = new JPanel().getBackground();
        final Color eColor = sColor;

        // A non-cyclic gradient
        final GradientPaint gradient1 = new GradientPaint(0, 0, sColor, getWidth(), 60, eColor);
        g2.setPaint(gradient1);

        g.fillRect(0, 61, getWidth(), getHeight());

        g2.drawImage(EBISystem.getInstance().getIconResource("theader.gif").getImage(), 0, 0, null);
        g.setColor(new Color(34, 34, 34));
        g.drawLine(0, 60, getWidth(), 60);
        setOpaque(false);
    }

    private JTextField getJTextUser() {
        if (jTextUser == null) {
            jTextUser = new JTextField();
            jTextUser.setBounds(new Rectangle(145, 160, 307, 25));
        }
        return jTextUser;
    }

    private JTextField getJTexPassword() {
        if (jTexPassword == null) {
            jTexPassword = new JTextField();
            jTexPassword.setBounds(new Rectangle(145, 190, 307, 25));
        }
        return jTexPassword;
    }

    private JButton getJButtonGenerating() {
        if (jButtonGenerating == null) {
            jButtonGenerating = new JButton();
            jButtonGenerating.setBounds(new Rectangle(191, 250, 135, 30));
            jButtonGenerating.setIcon(EBISystem.getInstance().getIconResource("button_ok.png"));
            jButtonGenerating.setText("Generating");
            jButtonGenerating.setEnabled(false);
            jButtonGenerating.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (!checkField()) {
                        return;
                    }
                    try {
                        // Create encrypter/decrypter class
                        Encrypter encrypter = new Encrypter("EBINeutrino");

                        // Encrypt
                        final String Pwdencrypted = encrypter.encrypt(jTexPassword.getText());
                        final String Usrencrypted = encrypter.encrypt(jTextUser.getText().trim());

                        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
                        properties.setValue("EBI_Neutrino_Database_Driver",
                                jComboDatabaseDriver.getSelectedItem().toString());
                        properties.setValue("EBI_Neutrino_Database",
                                jTextDatabaseType.getSelectedItem().toString().trim());
                        properties.setValue("EBI_Neutrino_Database_UpperCase", "yes");
                        properties.setValue("EBI_Neutrino_Database_Name", jTextSIDCatalog.getText().trim());
                        properties.setValue("EBI_Neutrino_Password", Pwdencrypted);
                        properties.setValue("EBI_Neutrino_User", Usrencrypted);
                        properties.setValue("EBI_Neutrino_Host", jTextIP.getText().trim());
                        properties.setValue("EBI_Neutrino_Oracle_SID", jTextSIDCatalog.getText().trim());

                        EBIExceptionDialog.getInstance(setup, "Database connection data, are saved successfully!")
                                .Show(EBIMessage.INFO_MESSAGE);
                        jButtonOK.setEnabled(true);
                        jButtonImportSchema.setEnabled(true);
                        configureHibernateFiles();
                        checkIfSchemaExist();

                        encrypter = null;
                        setup.DBConfigured = true;
                        setup.sysINIT.Init(properties.getValue("EBI_Neutrino_Database_Name"));
                        properties.saveEBINeutrinoProperties();
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                    }
                }

            });
        }
        return jButtonGenerating;
    }

    public void importSQLDemo() {
        final EBIImportSQLFiles impSQL = new EBIImportSQLFiles();
        if (EBISystem.selectedLanguage.toLowerCase().equals("italiano")) {
            impSQL.startSQLImport(new String[]{"./reports/ReportsIT.sql", "./sql/demoData_ITALIANO.sql",
                "./reports/reportParameter.sql"});
        } else if (EBISystem.selectedLanguage.toLowerCase().equals("deutsch")) {
            impSQL.startSQLImport(new String[]{"./reports/ReportsDE.sql", "./sql/demoData_DEUTSCH.sql",
                "./reports/reportParameter.sql"});
        } else {
            impSQL.startSQLImport(new String[]{"./reports/ReportsEN.sql", "./sql/demoData_ENGLISH.sql",
                "./reports/reportParameter.sql"});
        }
    }

    public void configureHibernateFiles() {

        final File dir = new File("hibernate/");

        final FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".hbm.xml");
            }
        };

        final String[] children = dir.list(filter);
        if (children == null) {
            EBIExceptionDialog.getInstance(setup, "Critical Error: No Hibernate files exsist system will exit now! ")
                    .Show(EBIMessage.ERROR_MESSAGE);
            System.exit(1);
        } else {

            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                final String filename = children[i];

                try {
                    final File xml = new File("./hibernate/" + filename);
                    final FileReader reader = new FileReader(xml.getAbsolutePath());
                    final BufferedReader in = new BufferedReader(reader);

                    String string;
                    final StringBuffer newFile = new StringBuffer();
                    while ((string = in.readLine()) != null) {
                        int ind;
                        if ((ind = string.indexOf("table=")) != -1) {
                            String tmp = string.substring(ind + 7);
                            tmp = tmp.substring(0, tmp.indexOf("\""));
                            string = string.replace("table=\"" + tmp + "\"", "table=\"" + tmp.toUpperCase() + "\"");
                        }

                        newFile.append(string);
                        newFile.append("\n");

                    }
                    in.close();

                    final FileWriter writer = new FileWriter(xml.getAbsolutePath());
                    writer.write(newFile.toString().replaceAll("catalog=\"(.*)\"",
                            "catalog=\"" + jTextSIDCatalog.getText() + "\""));
                    writer.close();

                } catch (final FileNotFoundException e) {
                    e.printStackTrace();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkIfSchemaExist() throws Exception {

        if (EBIExceptionDialog.getInstance(setup, "Would you like to import the EBI Neutrino R1 database schema?")
                .Show(EBIMessage.INFO_MESSAGE_YESNO)) {
            EBISystem.getInstance().iDB().setActiveConnection(conn);
            new EBISchemaImport(EBISystem.getInstance(), databaseType, this.jTextSIDCatalog.getText(), true)
                    .setVisible(true);
            EBISystem.getInstance().iDB().getActiveConnection().setCatalog(jTextSIDCatalog.getText().trim());
            importSQLDemo();

        }
    }

    private boolean checkField() {

        if ("".equals(jTextDatabaseType.getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(setup, "Please select the database!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(jComboDatabaseDriver.getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(setup, "Please select the database driver!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if (jTextSIDCatalog.isVisible()) {
            if ("".equals(jTextSIDCatalog.getText())) {
                EBIExceptionDialog.getInstance(setup, "Please insert the Catalog or SID for connecting to a database!")
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
        }
        if ("".equals(this.jTextIP.getText())) {
            EBIExceptionDialog.getInstance(setup, "Please insert the database host!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(this.jTextUser.getText())) {
            EBIExceptionDialog.getInstance(setup, "Please insert the database user!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private JTextField getJTextIP() {
        if (jTextIP == null) {
            jTextIP = new JTextField();
            jTextIP.setBounds(new Rectangle(145, 130, 307, 25));
        }
        return jTextIP;
    }

    private JButton getJButtonTest() {
        if (jButtonTest == null) {
            jButtonTest = new JButton();
            jButtonTest.setBounds(new Rectangle(16, 250, 170, 30));
            jButtonTest.setIcon(EBISystem.getInstance().getIconResource("/Icon/connect_established.png"));
            jButtonTest.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            jButtonTest.setText("Test Connection");
            jButtonTest.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (!checkField()) {
                        return;
                    }

                    try {
                        Class.forName(jComboDatabaseDriver.getSelectedItem().toString()).newInstance();
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                        EBIExceptionDialog.getInstance(setup, "ERROR : Database driver was not found! ")
                                .Show(EBIMessage.ERROR_MESSAGE);
                        return;
                    }

                    try {

                        String conn_url = null;
                        final String dbType = jTextDatabaseType.getSelectedItem().toString().toLowerCase();
                        final String host = jTextIP.getText();

                        if ("mysql".equals(jTextDatabaseType.getSelectedItem().toString().toLowerCase())) {
                            conn_url = "jdbc:" + dbType + "://" + host
                                    + "/?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
                        } else if ("oracle".equals(jTextDatabaseType.getSelectedItem().toString().toLowerCase())) {
                            conn_url = "jdbc:" + dbType + ":thin:@" + host + ":" + jTextSIDCatalog.getText();
                        }

                        conn = DriverManager.getConnection(conn_url, jTextUser.getText(), jTexPassword.getText());

                        EBIExceptionDialog.getInstance(setup, "Connection is ok!").Show(EBIMessage.INFO_MESSAGE);
                        jButtonGenerating.setEnabled(true);

                    } catch (final Exception ex) {
                        ex.printStackTrace();
                        EBIExceptionDialog.getInstance(setup,
                                "Connection Error: \nCheck your connection data also check if your database is running! \n\n"
                                + EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                        jButtonGenerating.setEnabled(false);
                    }
                }
            });
        }
        return jButtonTest;
    }

    private JButton getJButtonOK() {
        if (jButtonOK == null) {
            jButtonOK = new JButton();
            jButtonOK.setBounds(new Rectangle(305, 335, 125, 30));
            jButtonOK.setIcon(EBISystem.getInstance().getIconResource("/Icon/button_ok.png"));
            jButtonOK.setText("Ok");
            jButtonOK.setEnabled(false);
            jButtonOK.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    setup.setVisible(false);
                }
            });
        }
        return jButtonOK;
    }

    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setBounds(new Rectangle(435, 335, 125, 30));
            jButtonCancel.setIcon(EBISystem.getInstance().getIconResource("/Icon/button_cancel.png"));
            jButtonCancel.setText("Exit");
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (EBIExceptionDialog.getInstance(setup, EBISystem.i18n("EBI_LANG_MESSAGE_CLOSE"))
                            .Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                        System.exit(0);
                    }
                }
            });
        }
        return jButtonCancel;
    }

    private JPopupMenu.Separator getJSepara() {
        if (jSepara == null) {
            jSepara = new JPopupMenu.Separator();
            jSepara.setBounds(new Rectangle(0, 320, 570, 2));
            jSepara.setBackground(new Color(34, 34, 34));
            jSepara.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34)));
        }
        return jSepara;
    }

    private JComboBox getJComboDatabaseDriver() {
        if (jComboDatabaseDriver == null) {
            jComboDatabaseDriver = new JComboBox();
            jComboDatabaseDriver.setBounds(new Rectangle(145, 100, 150, 25));
        }
        return jComboDatabaseDriver;
    }

    private JComboBox getJTextDatabaseName() {
        if (jTextDatabaseType == null) {
            jTextDatabaseType = new JComboBox();
            jTextDatabaseType.setBounds(new Rectangle(145, 70, 150, 25));
            jTextDatabaseType.addItem("Please select");
            jTextDatabaseType.addItem("Oracle");
            jTextDatabaseType.addItem("MySQL");
            jTextDatabaseType.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if ("oracle".equals(jTextDatabaseType.getSelectedItem().toString().toLowerCase())) {
                        jComboDatabaseDriver.removeAllItems();
                        jComboDatabaseDriver.addItem("oracle.jdbc.driver.OracleDriver");
                        jLabel7.setText("SID");
                        jTextSIDCatalog.setText("");
                        jTextSIDCatalog.setVisible(true);
                        jLabel7.setVisible(true);
                        databaseType = "oracle";
                    } else if ("mysql".equals(jTextDatabaseType.getSelectedItem().toString().toLowerCase())) {
                        jComboDatabaseDriver.removeAllItems();
                        jComboDatabaseDriver.addItem("com.mysql.jdbc.Driver");
                        jTextSIDCatalog.setText("EBINEUTRINODB");
                        jTextSIDCatalog.setVisible(true);
                        jLabel7.setText("Catalog");
                        jLabel7.setVisible(true);
                        databaseType = "mysql";
                    }
                }
            });

        }
        return jTextDatabaseType;
    }

    private JTextField getJTextOracleSID() {
        if (jTextSIDCatalog == null) {
            jTextSIDCatalog = new JTextField();
            jTextSIDCatalog.setBounds(new Rectangle(300, 70, 140, 25));
            jTextSIDCatalog.setVisible(false);
        }
        return jTextSIDCatalog;
    }

    private JButton getJButtonImportSchema() {
        if (jButtonImportSchema == null) {
            jButtonImportSchema = new JButton();
            jButtonImportSchema.setBounds(new Rectangle(332, 250, 174, 30));
            jButtonImportSchema.setText("DB Schema Import ");
            jButtonImportSchema.setEnabled(false);
            jButtonImportSchema.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (!checkField()) {
                        return;
                    }
                    try {
                        checkIfSchemaExist();
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return jButtonImportSchema;
    }
}
