package org.core.settings;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class EBISystemSettingPanel extends JPanel {

    private JPanel jPanelSysAlg = null;
    private JTextField jtextPDFPath = null;
    private JButton jButtonSelectPath = null;
    private JTextField jtextBrowserPath = null;
    private JTextField jtextLogo = null;
    private JLabel logo = null;
    private JButton jButtonBrowserPath = null;
    private JButton jButtonBrowserLogo = null;
    private JPanel generalSettings = null;
    private JPanel panelEMailSettings = null;
    private JTextField jTextEMailhost = null;
    private JTextField jTextEMailBenutzer = null;
    private JPasswordField jTextEMailpassword = null;
    private JTextField jTextEmailfrom = null;
    private JTextField jTextFromTitle = null;
    private JTextField jTextEditorPath = null;
    private JButton jButtonOpenTextEditor = null;
    private JTextField jTextpopServer = null;
    private JComboBox jcomboEMailProtocol = null;
    private JTextField jTextpopUser = null;
    private JPasswordField jTextpopPassword = null;
    private JComboBox jComboBoxLanguage = null;
    private JComboBox jComboDateFormat = null;
    private JLabel passwordPOPLabel = null;
    private JLabel emailUserLabel = null;
    private JLabel emailServerLabel = null;
    private JLabel logoView = null;

    public EBISystemSettingPanel() {
        super();
        initialize();
        parseLanguageFileFromDir();
        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

        if (!"".equals(properties.getValue("EBI_Neutrino_TextEditor_Path"))){
            jTextEditorPath.setText(properties.getValue("EBI_Neutrino_TextEditor_Path"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_PDF"))) {
            jtextPDFPath.setText(properties.getValue("EBI_Neutrino_PDF"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_Browser"))) {
            jtextBrowserPath.setText(properties.getValue("EBI_Neutrino_Browser"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_Language_File"))) {
            parseLanguageFromCombo(properties.getValue("EBI_Neutrino_Language_File"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_TextEditor_Path"))) {
            this.jTextEditorPath.setText(properties.getValue("EBI_Neutrino_TextEditor_Path"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_Date_Format"))) {
            this.jComboDateFormat.getEditor().setItem(properties.getValue("EBI_Neutrino_Date_Format"));
        }
        if (!"".equals(properties.getValue("EBI_Neutrino_Logo"))) {
            this.jtextLogo.setText(properties.getValue("EBI_Neutrino_Logo"));
            loadIconToView();
        }
        loadEMailSetting();
        EBISystemSetting.selectedModule = 0;
    }

    private void parseLanguageFileFromDir() {
        String[] files = EBIPropertiesRW.getEBIProperties().getValue("EBI_Neutrino_Languages").split(",");
        String[] languages = new String[files.length + 1];
        languages[0] = EBISystem.i18n("EBI_LANG_PLEASE_SELECT");
        int i = 1;
        for (String name : files) {
            languages[i] = name;
            i++;
        }
        this.jComboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(languages));
    }

    private void parseLanguageFromCombo(final String name) {
        try {
            String lName;
            if ((lName = name.substring(name.lastIndexOf("_") + 1)) != null) {
                if (!"".equals(lName) && lName != null) {
                    if ((lName = lName.substring(0, lName.lastIndexOf("."))) != null) {
                        if (!"".equals(lName)) {
                            this.jComboBoxLanguage.setSelectedItem(lName);
                        }
                    }
                }
            }
        } catch (final StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    private void initialize() {

        final JLabel systemSettingsLabel = new JLabel();
        systemSettingsLabel.setBounds(new java.awt.Rectangle(90, 14, 303, 38));
        systemSettingsLabel.setText(EBISystem.i18n("EBI_LANG_SYSTEM_SETTING"));
        systemSettingsLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));

        final JLabel systemSettingsImage = new JLabel();
        systemSettingsImage.setBounds(new java.awt.Rectangle(19, 0, 62, 68));
        systemSettingsImage.setIcon(EBISystem.getInstance().getIconResource("advancedsettings.png"));
        systemSettingsImage.setText("");

        logoView = new JLabel();
        logoView.setBounds(new java.awt.Rectangle(450, 85, 200, 100));
        logoView.setText("#Logo");
        logoView.setHorizontalAlignment(SwingConstants.CENTER);
        logoView.setHorizontalTextPosition(SwingConstants.CENTER);
        logoView.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        this.setLayout(null);
        this.setSize(1024, 846);
        this.add(logoView, null);
        this.add(systemSettingsImage, null);
        this.add(systemSettingsLabel, null);
        this.add(getJPanelSysAlg(), null);
        this.add(getJPanelLogo(), null);
        this.add(getJPanelEmailSetting(), null);
    }

    private void loadIconToView() {
        if (!"".equals(jtextLogo.getText())) {
            ImageIcon icon = new ImageIcon(jtextLogo.getText());
            Image image = icon.getImage(); // transform it
            Image newimg = null;
            if(icon.getIconWidth() > logoView.getWidth()){
                newimg = image.getScaledInstance(logoView.getWidth(), logoView.getHeight(), java.awt.Image.SCALE_SMOOTH);
            }else{
                newimg = image;
            }
            logoView.setIcon(new ImageIcon(newimg));
            logoView.updateUI();
            logoView.setText("");
        }
    }

    private JPanel getJPanelSysAlg() {
        if (jPanelSysAlg == null) {
            final JLabel jLabel6 = new JLabel();
            jLabel6.setBounds(new Rectangle(9, 98, 103, 20));
            jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
            jLabel6.setFont(new Font("Dialog", Font.PLAIN, 12));
            jLabel6.setText(EBISystem.i18n("EBI_LANG_TEXT_EDITOR_PATH"));
            final JLabel jLabel3 = new JLabel();
            jLabel3.setBounds(new Rectangle(9, 64, 103, 20));
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel3.setFont(new Font("Dialog", Font.PLAIN, 12));
            jLabel3.setText(EBISystem.i18n("EBI_LANG_BROWSER_PATH"));
            final JLabel jLabel2 = new JLabel();
            jLabel2.setBounds(new Rectangle(9, 27, 103, 20));
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
            jLabel2.setText(EBISystem.i18n("EBI_LANG_PDF_VIEWER_PATH"));
            jPanelSysAlg = new JPanel();
            jPanelSysAlg.setBackground(new Color(240, 240, 242));
            jPanelSysAlg.setOpaque(false);
            jPanelSysAlg.setLayout(null);
            jPanelSysAlg.setBounds(new Rectangle(19, 80, 410, 142));
            jPanelSysAlg.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    EBISystem.i18n("EBI_LANG_SYSTEMS_PATH"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new java.awt.Font("Dialog", java.awt.Font.BOLD, 12)));
            jPanelSysAlg.add(jLabel2, null);
            jPanelSysAlg.add(getJComboAdobePath(), null);
            jPanelSysAlg.add(getJButtonSelectPath(), null);
            jPanelSysAlg.add(jLabel3, null);
            jPanelSysAlg.add(getJComboBrowserPath(), null);
            jPanelSysAlg.add(getJButtonBrowserPath(), null);
            jPanelSysAlg.add(jLabel6, null);
            jPanelSysAlg.add(getJTextEditorPath(), null);
            jPanelSysAlg.add(getJButtonOpenTextEditor(), null);
        }
        return jPanelSysAlg;
    }

    private JTextField getJComboAdobePath() {
        if (jtextPDFPath == null) {
            jtextPDFPath = new JTextField();
            jtextPDFPath.setBounds(new Rectangle(120, 25, 214, 25));
            jtextPDFPath.setEditable(true);
        }
        return jtextPDFPath;
    }

    private JButton getJButtonSelectPath() {
        if (jButtonSelectPath == null) {
            jButtonSelectPath = new JButton();
            jButtonSelectPath.setBounds(new Rectangle(340, 24, 35, 27));
            jButtonSelectPath.setText("...");
            jButtonSelectPath.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final File file = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);

                    if (file != null) {
                        jtextPDFPath.setText(file.getAbsolutePath());
                    }
                }
            });
        }
        return jButtonSelectPath;
    }

    private JTextField getJComboBrowserPath() {
        if (jtextBrowserPath == null) {
            jtextBrowserPath = new JTextField();
            jtextBrowserPath.setEditable(true);
            jtextBrowserPath.setBounds(new Rectangle(120, 62, 214, 25));
        }
        return jtextBrowserPath;
    }

    private JButton getJButtonBrowserPath() {
        if (jButtonBrowserPath == null) {
            jButtonBrowserPath = new JButton();
            jButtonBrowserPath.setBounds(new Rectangle(340, 62, 35, 25));
            jButtonBrowserPath.setText("...");
            jButtonBrowserPath.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final File file = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
                    if (file != null) {
                        jtextBrowserPath.setText(file.getAbsolutePath());
                    }
                }
            });
        }
        return jButtonBrowserPath;
    }

    private JButton getJButtonBrowserLogo() {
        if (jButtonBrowserLogo == null) {
            jButtonBrowserLogo = new JButton();
            jButtonBrowserLogo.setBounds(new Rectangle(335, 15, 35, 25));
            jButtonBrowserLogo.setText("...");
            jButtonBrowserLogo.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final File file = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
                    if (file != null) {
                        jtextLogo.setText(file.getAbsolutePath());
                        loadIconToView();
                    }
                }
            });
        }
        return jButtonBrowserLogo;
    }

    private JTextField getBrowserLogo() {
        if (jtextLogo == null) {
            jtextLogo = new JTextField();
            jtextLogo.setEditable(true);
            jtextLogo.setBounds(new Rectangle(115, 15, 214, 25));
        }
        return jtextLogo;
    }

    public void saveSystemSetting() {
        boolean isSaved = false;
        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
        
        properties.setValue("EBI_Neutrino_PDF", jtextPDFPath.getText());
        properties.setValue("EBI_Neutrino_Browser", jtextBrowserPath.getText());
        if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(this.jComboBoxLanguage.getSelectedItem().toString())) {
            properties.setValue("EBI_Neutrino_Language_File", "language/EBINeutrinoLanguage_"
                    + this.jComboBoxLanguage.getSelectedItem().toString() + ".properties");
            isSaved = true;
        }

        properties.setValue("EBI_Neutrino_TextEditor_Path", this.jTextEditorPath.getText());
        properties.setValue("EBI_Neutrino_Date_Format", this.jComboDateFormat.getEditor().getItem().toString());
        properties.setValue("EBI_Neutrino_Logo", this.jtextLogo.getText());
        
        if (validateInput() == true) {
            saveEMailSetting();
        }

        properties.saveEBINeutrinoProperties();
        if (isSaved == true) {
            EBISystem.getInstance().reloadTranslationSystem();
        }
        EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_INFO_SETTING_SAVED")).Show(EBIMessage.INFO_MESSAGE);
    }

    public void saveEMailSetting() {

        String iuSQL;
        ResultSet s = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB()
                    .initPreparedStatement("SELECT COUNT(ACCOUNTNAME) AS COUNT FROM MAIL_ACCOUNT ");
            s = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            s.next();
            if (s.getInt("COUNT") <= 0) {
                iuSQL = "INSERT";
            } else {
                iuSQL = "UPDATE";
            }
            s.close();

            String sql;
            if ("INSERT".equals(iuSQL)) {
                sql = "INSERT INTO MAIL_ACCOUNT SET " + "CREATEFROM=?," + "CREATEDATE=?," + "ACCOUNTNAME=?,"
                        + "SMTP_SERVER=?," + "SMTP_USER=?," + "SMTP_PASSWORD=?," + "EMAILADRESS=?," + "POP_SERVER=?,"
                        + "POP_USER=?," + "POP_PASSWORD=?," + "EMAILS_TITLE=?, "
                        + "FOLDER_NAME=? ";
            } else {
                sql = "UPDATE MAIL_ACCOUNT SET " + "CREATEFROM=?," + "CREATEDATE=?," + "ACCOUNTNAME=?,"
                        + "SMTP_SERVER=?," + "SMTP_USER=?," + "SMTP_PASSWORD=?," + "EMAILADRESS=?," + "POP_SERVER=?,"
                        + "POP_USER=?," + "POP_PASSWORD=?," + "EMAILS_TITLE=?, "
                        + "FOLDER_NAME=? " + "WHERE ID = 1";
            }

            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
            ps.setString(1, EBISystem.ebiUser);
            ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            ps.setString(3, this.jTextFromTitle.getText());
            ps.setString(4, this.jTextEMailhost.getText());
            ps.setString(5, this.jTextEMailBenutzer.getText());
            ps.setString(6, this.jTextEMailpassword.getText());
            ps.setString(7, this.jTextEmailfrom.getText());
            ps.setString(8, this.jTextpopServer.getText());
            ps.setString(9, this.jTextpopUser.getText());
            ps.setString(10, String.valueOf(this.jTextpopPassword.getPassword()));
            ps.setString(11, this.jTextFromTitle.getText());
            ps.setString(12, jcomboEMailProtocol.getSelectedItem().toString());

            EBISystem.getInstance().iDB().executePreparedStmt(ps);
            EBISystem.getInstance().reloadEMailSetting();
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadEMailSetting() {
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB()
                    .initPreparedStatement("SELECT * FROM MAIL_ACCOUNT WHERE CREATEFROM=?");
            ps1.setString(1, EBISystem.ebiUser);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                while (set.next()) {
                    this.jTextFromTitle.setText(set.getString("EMAILS_TITLE"));
                    this.jTextEMailhost.setText(set.getString("SMTP_SERVER"));
                    this.jTextEMailBenutzer.setText(set.getString("SMTP_USER"));
                    this.jTextEMailpassword.setText(set.getString("SMTP_PASSWORD"));
                    this.jTextEmailfrom.setText(set.getString("EMAILADRESS"));
                    this.jTextpopServer.setText(set.getString("POP_SERVER"));
                    this.jTextpopUser.setText(set.getString("POP_USER"));
                    this.jTextpopPassword.setText(set.getString("POP_PASSWORD"));
                    this.jTextFromTitle.setText(set.getString("EMAILS_TITLE"));
                    this.jcomboEMailProtocol.setSelectedItem(set.getString("FOLDER_NAME"));
                }
            } else {
                resetEMailFields();
            }

        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JPanel getJPanelLogo() {
        if (generalSettings == null) {
            final JLabel logoLabel = new JLabel();
            logoLabel.setBounds(new Rectangle(16, 15, 93, 25));
            logoLabel.setText("Logo");
            logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            logoLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

            final JLabel dateFormatLabel = new JLabel();
            dateFormatLabel.setBounds(new Rectangle(16, 75, 93, 25));
            dateFormatLabel.setText(EBISystem.i18n("EBI_LANG_DATE_FORMAT"));
            dateFormatLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            dateFormatLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            final JLabel languageLabel = new JLabel();
            languageLabel.setBounds(new Rectangle(16, 45, 93, 25));
            languageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            languageLabel.setText(EBISystem.i18n("EBI_LANG_LANGUAGE"));
            languageLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            generalSettings = new JPanel();
            generalSettings.setLayout(null);
            generalSettings.setOpaque(false);
            generalSettings.setBounds(new Rectangle(19, 224, 410, 117));
            generalSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    EBISystem.i18n("EBI_LANG_PANEL_NAME_GENERAL_SETTING"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            generalSettings.add(languageLabel, null);
            generalSettings.add(logoLabel, null);
            generalSettings.add(getJComboBoxLanguage(), null);
            generalSettings.add(dateFormatLabel, null);
            generalSettings.add(getJComboDateFormat(), null);
            generalSettings.add(getJButtonBrowserLogo(), null);
            generalSettings.add(getBrowserLogo(), null);
        }
        return generalSettings;
    }

    private JPanel getJPanelEmailSetting() {
        if (panelEMailSettings == null) {
            passwordPOPLabel = new JLabel();
            passwordPOPLabel.setBounds(new Rectangle(315, 196, 91, 20));
            passwordPOPLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            passwordPOPLabel.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_PASSWORD"));

            emailUserLabel = new JLabel();
            emailUserLabel.setBounds(new Rectangle(315, 164, 91, 20));
            emailUserLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailUserLabel.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_USER"));

            emailServerLabel = new JLabel();
            emailServerLabel.setBounds(new Rectangle(315, 132, 91, 20));
            emailServerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailServerLabel.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_SERVER"));

            final JLabel emailProtocolLabel = new JLabel();
            emailProtocolLabel.setBounds(new Rectangle(315, 98, 91, 20));
            emailProtocolLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailProtocolLabel.setText(EBISystem.i18n("EBI_LANG_EMAIL_PROTOCOL"));

            final JLabel serverConnectionDataLabel = new JLabel();
            serverConnectionDataLabel.setBounds(new Rectangle(16, 100, 264, 20));
            serverConnectionDataLabel.setText(EBISystem.i18n("EBI_LANG_EMAIL_SERVER_CONNECTION_DATA"));
            panelEMailSettings = new JPanel();
            panelEMailSettings.setLayout(null);
            panelEMailSettings.setOpaque(false);
            panelEMailSettings.setBounds(new Rectangle(19, 342, 600, 250));
            panelEMailSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    EBISystem.i18n("EBI_LANG_EMAIL_SETTING"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

            final JLabel emailTitleLabel = new JLabel();
            emailTitleLabel.setBounds(new Rectangle(16, 59, 91, 20));
            emailTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            emailTitleLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailTitleLabel.setText(EBISystem.i18n("EBI_LANG_EMAIL_TITLE"));

            final JLabel emailAddressLabel = new JLabel();
            emailAddressLabel.setBounds(new Rectangle(16, 28, 91, 20));
            emailAddressLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            emailAddressLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailAddressLabel.setText(EBISystem.i18n("EBI_LANG_EMAIL_ADRESS"));

            final JLabel smtpPasswordLabel = new JLabel();
            smtpPasswordLabel.setBounds(new Rectangle(2, 196, 105, 20));
            smtpPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            smtpPasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            smtpPasswordLabel.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_PASSWORD"));

            final JLabel emailSMTPUserLabel = new JLabel();
            emailSMTPUserLabel.setBounds(new Rectangle(16, 164, 91, 20));
            emailSMTPUserLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            emailSMTPUserLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailSMTPUserLabel.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_USER"));

            final JLabel emailSMTPServerLabel = new JLabel();
            emailSMTPServerLabel.setBounds(new Rectangle(16, 132, 91, 20));
            emailSMTPServerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            emailSMTPServerLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
            emailSMTPServerLabel.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_SERVER"));

            panelEMailSettings.add(serverConnectionDataLabel, null);
            panelEMailSettings.add(emailSMTPServerLabel, null);
            panelEMailSettings.add(emailSMTPUserLabel, null);
            panelEMailSettings.add(smtpPasswordLabel, null);
            panelEMailSettings.add(emailProtocolLabel, null);
            panelEMailSettings.add(emailAddressLabel, null);
            panelEMailSettings.add(emailTitleLabel, null);

            panelEMailSettings.add(getJTextEMailhost(), null);
            panelEMailSettings.add(getJTextEMailBenutzer(), null);
            panelEMailSettings.add(getJTextEMailpassword(), null);
            panelEMailSettings.add(getJTextEmailfrom(), null);

            panelEMailSettings.add(getJTextFromTitle(), null);

            panelEMailSettings.add(emailServerLabel, null);
            panelEMailSettings.add(emailUserLabel, null);
            panelEMailSettings.add(passwordPOPLabel, null);
            panelEMailSettings.add(getJTextpopServer(), null);
            panelEMailSettings.add(getEMailProtocol(), null);
            panelEMailSettings.add(getJTextpopUser(), null);
            panelEMailSettings.add(getJTextpopPassword(), null);
        }
        return panelEMailSettings;
    }

    private JTextField getJTextEMailhost() {
        if (jTextEMailhost == null) {
            jTextEMailhost = new JTextField();
            jTextEMailhost.setBounds(new Rectangle(111, 130, 175, 25));
            jTextEMailhost.setFocusTraversalKeysEnabled(false);
            jTextEMailhost.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextEMailBenutzer.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextFromTitle.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextEMailBenutzer.requestFocus();
                    }
                }
            });
        }
        return jTextEMailhost;
    }

    private JTextField getJTextEMailBenutzer() {
        if (jTextEMailBenutzer == null) {
            jTextEMailBenutzer = new JTextField();
            jTextEMailBenutzer.setBounds(new Rectangle(111, 162, 175, 25));
            jTextEMailBenutzer.setFocusTraversalKeysEnabled(false);
            jTextEMailBenutzer.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextEMailpassword.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextEMailhost.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextEMailpassword.requestFocus();
                    }
                }
            });
        }
        return jTextEMailBenutzer;
    }

    private JTextField getJTextEMailpassword() {
        if (jTextEMailpassword == null) {
            jTextEMailpassword = new JPasswordField();
            jTextEMailpassword.setBounds(new Rectangle(111, 195, 175, 25));
            jTextEMailpassword.setFocusTraversalKeysEnabled(false);
            jTextEMailpassword.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextpopServer.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextEMailBenutzer.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextpopServer.requestFocus();
                    }
                }
            });
        }
        return jTextEMailpassword;
    }

    private JTextField getJTextEmailfrom() {
        if (jTextEmailfrom == null) {
            jTextEmailfrom = new JTextField();
            jTextEmailfrom.setBounds(new Rectangle(111, 26, 249, 25));
            jTextEmailfrom.setFocusTraversalKeysEnabled(false);
            jTextEmailfrom.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextFromTitle.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextpopPassword.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextFromTitle.requestFocus();
                    }
                }
            });
        }
        return jTextEmailfrom;
    }

    private void enableEMailFields(final boolean enabled) {
        this.jTextFromTitle.setEnabled(enabled ? false : true);
        this.jTextEMailhost.setEnabled(enabled ? false : true);
        this.jTextEMailBenutzer.setEnabled(enabled ? false : true);
        this.jTextEMailpassword.setEnabled(enabled ? false : true);
        this.jTextEmailfrom.setEnabled(enabled ? false : true);
        this.jTextpopServer.setEnabled(enabled ? false : true);
        this.jTextpopUser.setEnabled(enabled ? false : true);
        this.jTextpopPassword.setEnabled(enabled ? false : true);
        this.jTextFromTitle.setEnabled(enabled ? false : true);
        this.jcomboEMailProtocol.setEnabled(enabled ? false : true);
    }

    private void resetEMailFields() {
        this.jTextFromTitle.setText("");
        this.jTextEMailhost.setText("");
        this.jTextEMailBenutzer.setText("");
        this.jTextEMailpassword.setText("");
        this.jTextEmailfrom.setText("");
        this.jTextpopServer.setText("");
        this.jTextpopUser.setText("");
        this.jTextpopPassword.setText("");
        this.jTextFromTitle.setText("");
        this.jcomboEMailProtocol.setSelectedIndex(0);
    }

    private JTextField getJTextFromTitle() {
        if (jTextFromTitle == null) {
            jTextFromTitle = new JTextField();
            jTextFromTitle.setBounds(new Rectangle(111, 57, 248, 25));
            jTextFromTitle.setFocusTraversalKeysEnabled(false);
            jTextFromTitle.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextEMailhost.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextEmailfrom.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextEMailhost.requestFocus();
                    }
                }
            });
        }
        return jTextFromTitle;
    }

    private JTextField getJTextEditorPath() {
        if (jTextEditorPath == null) {
            jTextEditorPath = new JTextField();
            jTextEditorPath.setBounds(new Rectangle(120, 96, 214, 25));

        }
        return jTextEditorPath;
    }

    private JButton getJButtonOpenTextEditor() {
        if (jButtonOpenTextEditor == null) {
            jButtonOpenTextEditor = new JButton();
            jButtonOpenTextEditor.setBounds(new Rectangle(340, 96, 35, 25));
            jButtonOpenTextEditor.setText("...");
            jButtonOpenTextEditor.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    final File file = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);
                    if (file != null) {
                        jTextEditorPath.setText(file.getAbsolutePath());
                    }
                }
            });
        }
        return jButtonOpenTextEditor;
    }

    private JTextField getJTextpopServer() {
        if (jTextpopServer == null) {
            jTextpopServer = new JTextField();
            jTextpopServer.setBounds(new Rectangle(400, 130, 175, 25));
            jTextpopServer.setFocusTraversalKeysEnabled(false);
            jTextpopServer.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextpopUser.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jcomboEMailProtocol.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextpopUser.requestFocus();
                    }
                }
            });
        }
        return jTextpopServer;
    }

    private JComboBox getEMailProtocol() {
        if (jcomboEMailProtocol == null) {
            jcomboEMailProtocol = new JComboBox();
            jcomboEMailProtocol.addItem("pop3");
            jcomboEMailProtocol.addItem("imaps");
            jcomboEMailProtocol.setBounds(new Rectangle(400, 96, 175, 25));
            jcomboEMailProtocol.setFocusTraversalKeysEnabled(false);
            jcomboEMailProtocol.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (jcomboEMailProtocol.getSelectedItem().toString().equals("imaps")) {
                        passwordPOPLabel.setText(passwordPOPLabel.getText().replaceAll("POP", "IMAP"));
                        emailUserLabel.setText(emailUserLabel.getText().replaceAll("POP", "IMAP"));
                        emailServerLabel.setText(emailServerLabel.getText().replaceAll("POP", "IMAP"));
                    } else {
                        passwordPOPLabel.setText(passwordPOPLabel.getText().replaceAll("IMAP", "POP"));
                        emailUserLabel.setText(emailUserLabel.getText().replaceAll("IMAP", "POP"));
                        emailServerLabel.setText(emailServerLabel.getText().replaceAll("IMAP", "POP"));
                    }
                }
            });

            jcomboEMailProtocol.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextpopServer.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextEMailpassword.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextpopServer.requestFocus();
                    }
                }
            });
        }
        return jcomboEMailProtocol;
    }

    private JTextField getJTextpopUser() {
        if (jTextpopUser == null) {
            jTextpopUser = new JTextField();
            jTextpopUser.setBounds(new Rectangle(400, 162, 175, 25));
            jTextpopUser.setFocusTraversalKeysEnabled(false);
            jTextpopUser.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextpopPassword.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextpopServer.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextpopPassword.requestFocus();
                    }
                }
            });
        }
        return jTextpopUser;
    }

    private JTextField getJTextpopPassword() {
        if (jTextpopPassword == null) {
            jTextpopPassword = new JPasswordField();
            jTextpopPassword.setBounds(new Rectangle(400, 197, 175, 25));
            jTextpopPassword.setFocusTraversalKeysEnabled(false);
            jTextpopPassword.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        jTextEmailfrom.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextpopUser.requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_TAB) {
                        jTextEmailfrom.requestFocus();
                    }
                }
            });
        }
        return jTextpopPassword;
    }

    public boolean validateInput() {
        if (!"".equals(this.jTextEmailfrom.getText()) && !"".equals(this.jTextFromTitle.getText())
                || !"".equals(this.jTextEMailhost.getText()) && !"".equals(this.jTextEMailBenutzer.getText())
                || !"".equals(String.valueOf(this.jTextEMailpassword.getPassword()))) {

            if ("".equals(this.jTextEmailfrom.getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
            if ("".equals(this.jTextFromTitle.getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_TITLE"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
            if ("".equals(this.jTextEMailhost.getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_SMTP_SERVER"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
            if ("".equals(this.jTextEMailBenutzer.getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_SMTP_USER"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
            if ("".equals(this.jTextEMailpassword.getText())) {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_SMTP_PASSWORD"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private JComboBox getJComboBoxLanguage() {
        if (jComboBoxLanguage == null) {
            jComboBoxLanguage = new JComboBox();
            jComboBoxLanguage.setBounds(new Rectangle(115, 45, 214, 25));
        }
        return jComboBoxLanguage;
    }

    private JComboBox getJComboDateFormat() {
        if (jComboDateFormat == null) {
            jComboDateFormat = new JComboBox();
            jComboDateFormat.setBounds(new Rectangle(115, 75, 214, 25));
            jComboDateFormat.setEditable(true);
            jComboDateFormat.addItem("dd.MM.yyyy");
            jComboDateFormat.addItem("MM.dd.yyyy");
            jComboDateFormat.addItem("yyyy.mm.dd");
            jComboDateFormat.addItem("dd/MM/yyyy");
            jComboDateFormat.addItem("MM/dd/yyyy");
            jComboDateFormat.addItem("yyyy/mm/dd");
            final Border line = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(34, 34, 34));
            final Border empty = new EmptyBorder(0, 5, 0, 0);
            final CompoundBorder border = new CompoundBorder(line, empty);
            ((JTextField) jComboDateFormat.getEditor().getEditorComponent()).setBorder(border);

            jComboDateFormat.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    EBISystem.DateFormat = jComboDateFormat.getSelectedItem().toString();
                }
            });
        }
        return jComboDateFormat;
    }
}
