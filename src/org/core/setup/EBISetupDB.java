package org.core.setup;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIImportSQLFiles;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;
import org.sdk.utils.Encrypter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JTextField userNameText;
    private JTextField passwordText;
    private JButton cancelButton;
    private JButton generatingButton;
    private JTextField ipText;
    private JButton testDatabaseButton;
    private EBISetup setup;
    private JButton okButton;
    private JPopupMenu.Separator separatorCmp;
    private JComboBox databaseDriverCombo;
    private JComboBox databaseTypeText;
    private JTextField catalogText;
    private JLabel catalogLabel;
    private JButton importSchemaButton;
    private Connection conn;
    private String databaseType = "";
    private JLabel databaseNameLabel;
    private JLabel databaseDriverLabel;
    private JLabel databaseHostIPLabel;
    private JLabel databaseUserLabel;
    private JLabel passwordLabel;
    private JLabel spriteIconLabel;
    private JLabel headerTitleLabel;

    public EBISetupDB(final EBISetup setUp) {
        super();
        setup = setUp;
        initialize();
        databaseDriverCombo.addItem("com.mysql.cj.jdbc.Driver");
        databaseTypeText.setSelectedItem("mysql");
    }

    private void initialize() {

        catalogLabel = new JLabel();
        catalogLabel.setBounds(new Rectangle(445, 70, 115, 25));
        catalogLabel.setText("SID");
        catalogLabel.setVisible(false);

        databaseNameLabel = new JLabel();
        databaseNameLabel.setBounds(new Rectangle(15, 70, 125, 25));
        databaseNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        databaseNameLabel.setText("Database name:");

        databaseDriverLabel = new JLabel();
        databaseDriverLabel.setBounds(new Rectangle(15, 100, 125, 25));
        databaseDriverLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        databaseDriverLabel.setText("Database driver:");

        databaseHostIPLabel = new JLabel();
        databaseHostIPLabel.setBounds(new Rectangle(15, 130, 125, 25));
        databaseHostIPLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        databaseHostIPLabel.setText("Host/IP:");

        databaseUserLabel = new JLabel();
        databaseUserLabel.setBounds(new Rectangle(15, 160, 125, 25));
        databaseUserLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        databaseUserLabel.setText("User:");

        passwordLabel = new JLabel();
        passwordLabel.setBounds(new Rectangle(15, 190, 125, 25));
        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        passwordLabel.setText("Password:");

        headerTitleLabel = new JLabel();
        headerTitleLabel.setBounds(new Rectangle(100, 10, 315, 35));
        headerTitleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        headerTitleLabel.setText("EBI Neutrino database setup");

        spriteIconLabel = new JLabel();
        spriteIconLabel.setBounds(new Rectangle(20, 5, 80, 50));
        spriteIconLabel.setIcon(EBISystem.getInstance().getIconResource("spire.png"));
        spriteIconLabel.setText("");

        this.setLayout(null);
        this.setSize(570, 360);
        this.add(headerTitleLabel, null);
        this.add(databaseUserLabel, null);
        this.add(passwordLabel, null);
        this.add(getUsernameText(), null);
        this.add(getPasswordText(), null);
        this.add(getCancelButton(), null);
        this.add(getJButtonGenerating(), null);
        this.add(databaseHostIPLabel, null);
        this.add(getJTextIP(), null);
        this.add(getJButtonTest(), null);
        this.add(spriteIconLabel, null);
        this.add(getOkButton(), null);
        this.add(getSeparator(), null);
        this.add(databaseDriverLabel, null);
        this.add(getJComboDatabaseDriver(), null);
        this.add(databaseNameLabel, null);
        this.add(getJTextDatabaseName(), null);
        this.add(getCatalogText(), null);
        this.add(catalogLabel, null);
        this.add(getImportSchemaButton(), null);
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
        g.setColor(new Color(34, 34, 34));
        g.drawLine(0, 60, getWidth(), 60);
        setOpaque(false);
    }

    private JTextField getUsernameText() {
        if (userNameText == null) {
            userNameText = new JTextField();
            userNameText.setBounds(new Rectangle(145, 160, 307, 25));
        }
        return userNameText;
    }

    private JTextField getPasswordText() {
        if (passwordText == null) {
            passwordText = new JTextField();
            passwordText.setBounds(new Rectangle(145, 190, 307, 25));
        }
        return passwordText;
    }

    private JButton getJButtonGenerating() {
        if (generatingButton == null) {
            generatingButton = new JButton();
            generatingButton.setBounds(new Rectangle(191, 250, 135, 30));
            generatingButton.setIcon(EBISystem.getInstance().getIconResource("button_ok.png"));
            generatingButton.setText("Generating");
            generatingButton.setEnabled(false);
            generatingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!checkField()) {
                        return;
                    }
                    try {
                        // Create encrypter/decrypter class
                        Encrypter encrypter = new Encrypter("EBINeutrino");

                        // Encrypt
                        final String Pwdencrypted = encrypter.encrypt(passwordText.getText());
                        final String Usrencrypted = encrypter.encrypt(userNameText.getText().trim());

                        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
                        properties.setValue("EBI_Neutrino_Database_Driver", databaseDriverCombo.getSelectedItem().toString());
                        properties.setValue("EBI_Neutrino_Database", databaseTypeText.getSelectedItem().toString().trim());
                        properties.setValue("EBI_Neutrino_Database_UpperCase", "yes");
                        properties.setValue("EBI_Neutrino_Database_Name", catalogText.getText().trim());
                        properties.setValue("EBI_Neutrino_Password", Pwdencrypted);
                        properties.setValue("EBI_Neutrino_User", Usrencrypted);
                        properties.setValue("EBI_Neutrino_Host", ipText.getText().trim());
                        properties.setValue("EBI_Neutrino_Oracle_SID", catalogText.getText().trim());

                        EBIExceptionDialog.getInstance(setup, "Database connection data, are saved successfully!").Show(EBIMessage.INFO_MESSAGE);
                        okButton.setEnabled(true);
                        importSchemaButton.setEnabled(true);

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
        return generatingButton;
    }

    public void importSQLDemo() {
        final EBIImportSQLFiles impSQL = new EBIImportSQLFiles();
        if (EBISystem.selectedLanguage.toLowerCase().equals("italiano")) {
            impSQL.startSQLImport(new String[]{"reports/ReportsIT.sql", "sql/demoData_ITALIANO.sql", "reports/reportParameter.sql"});
        } else if (EBISystem.selectedLanguage.toLowerCase().equals("deutsch")) {
            impSQL.startSQLImport(new String[]{"reports/ReportsDE.sql", "sql/demoData_DEUTSCH.sql", "reports/reportParameter.sql"});
        } else {
            impSQL.startSQLImport(new String[]{"reports/ReportsEN.sql", "sql/demoData_ENGLISH.sql", "reports/reportParameter.sql"});
        }
    }

    private void checkIfSchemaExist() throws Exception {
        if (EBIExceptionDialog.getInstance(setup, "Would you like to import the EBI Neutrino R1 database schema?").Show(EBIMessage.INFO_MESSAGE_YESNO)) {
            EBISystem.getInstance().iDB().setActiveConnection(conn);
            new EBISchemaImport(databaseType, this.catalogText.getText(), true).setVisible(true);
            EBISystem.getInstance().iDB().getActiveConnection().setCatalog(catalogText.getText().trim());
            importSQLDemo();
        }
    }

    private boolean checkField() {
        if ("".equals(databaseTypeText.getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(setup, "Please select the database!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if (catalogText.isVisible()) {
            if ("".equals(catalogText.getText())) {
                EBIExceptionDialog.getInstance(setup, "Please insert the Catalog or SID for connecting to a database!").Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
        }
        if ("".equals(this.ipText.getText())) {
            EBIExceptionDialog.getInstance(setup, "Please insert the database host!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        if ("".equals(this.userNameText.getText())) {
            EBIExceptionDialog.getInstance(setup, "Please insert a database user!").Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private JTextField getJTextIP() {
        if (ipText == null) {
            ipText = new JTextField();
            ipText.setBounds(new Rectangle(145, 130, 307, 25));
        }
        return ipText;
    }

    private JButton getJButtonTest() {
        if (testDatabaseButton == null) {
            testDatabaseButton = new JButton();
            testDatabaseButton.setBounds(new Rectangle(16, 250, 170, 30));
            testDatabaseButton.setIcon(EBISystem.getInstance().getIconResource("connect_established.png"));
            testDatabaseButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            testDatabaseButton.setText("Test Connection");
            testDatabaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!checkField()) {
                        return;
                    }

                    try {
                        
                        Class.forName(databaseDriverCombo.getSelectedItem().toString());
                        String conn_url = null;
                        final String dbType = databaseTypeText.getSelectedItem().toString().toLowerCase();
                        final String host = ipText.getText();

                        if ("mysql".equals(dbType)) {
                            conn_url = "jdbc:" + dbType + "://" + host
                                    + "/?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";    
                        }  
                        
                        conn = DriverManager.getConnection(conn_url, userNameText.getText(), passwordText.getText());
                        EBIExceptionDialog.getInstance(setup, "Connection is ok!").Show(EBIMessage.INFO_MESSAGE);
                        EBISystem.db().setActiveConnection(conn);
                        generatingButton.setEnabled(true);
                        
                    } catch (final Exception ex) {
                        ex.printStackTrace();
                        EBIExceptionDialog.getInstance(setup,
                                "Connection Error: \nCheck your connection data also check if your database is running! \n\n"
                                + EBISystem.printStackTrace(ex))
                                .Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
                        generatingButton.setEnabled(false);
                    }
                }
            });
        }
        return testDatabaseButton;
    }

    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setBounds(new Rectangle(305, 335, 125, 30));
            okButton.setIcon(EBISystem.getInstance().getIconResource("button_ok.png"));
            okButton.setText("Ok");
            okButton.setEnabled(false);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    setup.setVisible(false);
                }
            });
        }
        return okButton;
    }

    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setBounds(new Rectangle(435, 335, 125, 30));
            cancelButton.setIcon(EBISystem.getInstance().getIconResource("button_cancel.png"));
            cancelButton.setText("Exit");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (EBIExceptionDialog.getInstance(setup, EBISystem.i18n("EBI_LANG_MESSAGE_CLOSE"))
                            .Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                        System.exit(0);
                    }
                }
            });
        }
        return cancelButton;
    }

    private JPopupMenu.Separator getSeparator() {
        if (separatorCmp == null) {
            separatorCmp = new JPopupMenu.Separator();
            separatorCmp.setBounds(new Rectangle(0, 320, 570, 2));
            separatorCmp.setBackground(new Color(34, 34, 34));
            separatorCmp.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34)));
        }
        return separatorCmp;
    }

    private JComboBox getJComboDatabaseDriver() {
        if (databaseDriverCombo == null) {
            databaseDriverCombo = new JComboBox();
            databaseDriverCombo.setBounds(new Rectangle(145, 100, 150, 25));
        }
        return databaseDriverCombo;
    }

    private JComboBox getJTextDatabaseName() {
        if (databaseTypeText == null) {
            databaseTypeText = new JComboBox();
            databaseTypeText.setBounds(new Rectangle(145, 70, 150, 25));
            databaseTypeText.addItem("MySQL");
        }
        return databaseTypeText;
    }

    private JTextField getCatalogText() {
        if (catalogText == null) {
            catalogText = new JTextField();
            catalogText.setBounds(new Rectangle(300, 70, 140, 25));
            catalogText.setText("EBINEUTRINODB");
        }
        return catalogText;
    }

    private JButton getImportSchemaButton() {
        if (importSchemaButton == null) {
            importSchemaButton = new JButton();
            importSchemaButton.setBounds(new Rectangle(332, 250, 174, 30));
            importSchemaButton.setText("DB Schema Import");
            importSchemaButton.setEnabled(false);
            importSchemaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!checkField()) { return; }
                    try {
                        checkIfSchemaExist();
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return importSchemaButton;
    }
}
