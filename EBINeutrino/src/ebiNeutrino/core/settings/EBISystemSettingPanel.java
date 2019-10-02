package ebiNeutrino.core.settings;

import ebiNeutrino.core.EBIMain;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.utils.EBIPropertiesRW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBISystemSettingPanel extends JPanel {

	private JPanel jPanelSysAlg = null;
	private JTextField jtextPDFPath = null;
	private JButton jButtonSelectPath = null;
	private JTextField jtextBrowserPath = null;
	private JComboBox<String> comboForUser = null;
	private JButton jButtonBrowserPath = null;
	private JPanel generalSettings = null;
	private JPanel jPanelEmailSetting = null;
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
	private EBIMain ebiMain = null;
	private JComboBox jComboBoxLanguage = null;
	private JComboBox jComboDateFormat = null;
	private JCheckBox deleteMessage = null;
	private JCheckBox isB2C = null;
	private JLabel jLabel10 = null;
	private JLabel jLabel9 = null;
	private JLabel jLabel8 = null;

	/**
	 * This is the default constructor
	 */
	public EBISystemSettingPanel(final EBIMain main) {
		super();
		ebiMain = main;
		initialize();

		parseLanguageFileFromDir();
		final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

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

		if ("true".equals(properties.getValue("EBI_Neutrino_UserAsB2C"))) {
			this.isB2C.setSelected(true);
			EBISystem.USE_ASB2C = true;
		} else {
			this.isB2C.setSelected(false);
			EBISystem.USE_ASB2C = false;
		}

		loadEMailSetting();
		EBISystemSetting.selectedModule = 2;

	}

	private void parseLanguageFileFromDir() {
		String[] value;

		final File dir = new File("language/");
		final File files[] = dir.listFiles();

		String builder = EBISystem.i18n("EBI_LANG_PLEASE_SELECT") + ",";
		for (int i = 0; i < files.length; i++) {
			try {
				String lName;
				if ((lName = files[i].getName().substring(files[i].getName().lastIndexOf("_") + 1)) != null) {
					if (!"".equals(lName) && lName != null) {
						if ((lName = lName.substring(0, lName.lastIndexOf("."))) != null) {
							if (!"".equals(lName)) {
								builder += lName;
								if (i < files.length) {
									builder += ",";
								}
							}
						}
					}
				}
			} catch (final StringIndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}
		}
		value = builder.trim().split(",");
		this.jComboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(value));
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
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		final JLabel jLabel1 = new JLabel();
		jLabel1.setBounds(new java.awt.Rectangle(83, 14, 303, 38));
		jLabel1.setText(EBISystem.i18n("EBI_LANG_SYSTEM_SETTING"));
		jLabel1.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
		final JLabel jLabel = new JLabel();
		jLabel.setBounds(new java.awt.Rectangle(19, 0, 62, 68));
		jLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("advancedsettings.png")));
		jLabel.setText("");
		this.setLayout(null);
		this.setSize(1024, 846);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(getJPanelSysAlg(), null);
		this.add(getJPanelLogo(), null);
		this.add(getJPanelEmailSetting(), null);
	}

	/**
	 * This method initializes jPanelSysAlg
	 * 
	 * @return javax.swing.JPanel
	 */
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

	/**
	 * This method initializes jComboAdobePath
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JTextField getJComboAdobePath() {
		if (jtextPDFPath == null) {
			jtextPDFPath = new JTextField();
			jtextPDFPath.setBounds(new Rectangle(139, 25, 214, 25));
			jtextPDFPath.setEditable(true);
		}
		return jtextPDFPath;
	}

	/**
	 * This method initializes jButtonSelectPath
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSelectPath() {
		if (jButtonSelectPath == null) {
			jButtonSelectPath = new JButton();
			jButtonSelectPath.setBounds(new Rectangle(358, 24, 35, 27));
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

	/**
	 * This method initializes jComboBrowserPath
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JTextField getJComboBrowserPath() {
		if (jtextBrowserPath == null) {
			jtextBrowserPath = new JTextField();
			jtextBrowserPath.setEditable(true);
			jtextBrowserPath.setBounds(new Rectangle(139, 62, 214, 25));
		}
		return jtextBrowserPath;
	}

	/**
	 * This method initializes jButtonBrowserPath
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonBrowserPath() {
		if (jButtonBrowserPath == null) {
			jButtonBrowserPath = new JButton();
			jButtonBrowserPath.setBounds(new Rectangle(358, 62, 35, 25));
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

	public void saveSystemSetting() {
		boolean isSaved = false;
		final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

		if (!"".equals(jtextPDFPath.getText())) {

			properties.setValue("EBI_Neutrino_PDF", jtextPDFPath.getText());
		}
		if (!"".equals(jtextBrowserPath.getText())) {

			properties.setValue("EBI_Neutrino_Browser", jtextBrowserPath.getText());
		}
		if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(this.jComboBoxLanguage.getSelectedItem().toString())) {
			properties.setValue("EBI_Neutrino_Language_File", "language/EBINeutrinoLanguage_"
					+ this.jComboBoxLanguage.getSelectedItem().toString() + ".properties");
			isSaved = true;
		}
		if (!"".equals(this.jTextEditorPath.getText())) {
			properties.setValue("EBI_Neutrino_TextEditor_Path", this.jTextEditorPath.getText());
		}
		if (this.jComboDateFormat.getEditor().getItem() != null) {
			properties.setValue("EBI_Neutrino_Date_Format", this.jComboDateFormat.getEditor().getItem().toString());
		}

		if (this.isB2C.isSelected()) {
			properties.setValue("EBI_Neutrino_UserAsB2C", "true");
		} else {
			properties.setValue("EBI_Neutrino_UserAsB2C", "false");
		}

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
						+ "POP_USER=?," + "POP_PASSWORD=?," + "EMAILS_TITLE=?, " + "DELETE_MESSAGE=?, "
						+ "FOLDER_NAME=? ";
			} else {
				sql = "UPDATE MAIL_ACCOUNT SET " + "CREATEFROM=?," + "CREATEDATE=?," + "ACCOUNTNAME=?,"
						+ "SMTP_SERVER=?," + "SMTP_USER=?," + "SMTP_PASSWORD=?," + "EMAILADRESS=?," + "POP_SERVER=?,"
						+ "POP_USER=?," + "POP_PASSWORD=?," + "EMAILS_TITLE=?, " + "DELETE_MESSAGE=?, "
						+ "FOLDER_NAME=? " + "WHERE ID = 1";
			}

			final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(sql);
			ps.setString(1, comboForUser.getSelectedItem() == null ? EBISystem.ebiUser
					: comboForUser.getSelectedItem().toString());
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
			ps.setInt(12, this.deleteMessage.isSelected() == true ? 1 : 0);
			ps.setString(13, jcomboEMailProtocol.getSelectedItem().toString());

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
			ps1.setString(1, comboForUser.getSelectedItem() == null ? EBISystem.ebiUser
					: comboForUser.getSelectedItem().toString());
			set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

			set.last();
			if (set.getRow() > 0) {
				set.beforeFirst();
				while (set.next()) {
					this.comboForUser.setSelectedItem(set.getString("CREATEFROM"));
					this.jTextFromTitle.setText(set.getString("EMAILS_TITLE"));
					this.jTextEMailhost.setText(set.getString("SMTP_SERVER"));
					this.jTextEMailBenutzer.setText(set.getString("SMTP_USER"));
					this.jTextEMailpassword.setText(set.getString("SMTP_PASSWORD"));
					this.jTextEmailfrom.setText(set.getString("EMAILADRESS"));
					this.jTextpopServer.setText(set.getString("POP_SERVER"));
					this.jTextpopUser.setText(set.getString("POP_USER"));
					this.jTextpopPassword.setText(set.getString("POP_PASSWORD"));
					this.jTextFromTitle.setText(set.getString("EMAILS_TITLE"));
					this.deleteMessage.setSelected(set.getInt("DELETE_MESSAGE") == 1 ? true : false);
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

	/**
	 * This method initializes generalSettings
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelLogo() {
		if (generalSettings == null) {
			final JLabel jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(35, 56, 93, 25));
			jLabel5.setText(EBISystem.i18n("EBI_LANG_DATE_FORMAT"));
			jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel5.setFont(new Font("Dialog", Font.PLAIN, 12));
			final JLabel jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(35, 27, 93, 25));
			jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel4.setText(EBISystem.i18n("EBI_LANG_LANGUAGE"));
			jLabel4.setFont(new Font("Dialog", Font.PLAIN, 12));
			generalSettings = new JPanel();
			generalSettings.setLayout(null);
			generalSettings.setOpaque(false);
			generalSettings.setBounds(new Rectangle(19, 224, 410, 117));
			generalSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					EBISystem.i18n("EBI_LANG_PANEL_NAME_GENERAL_SETTING"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			generalSettings.add(jLabel4, null);
			generalSettings.add(getJComboBoxLanguage(), null);
			generalSettings.add(jLabel5, null);
			generalSettings.add(getJComboDateFormat(), null);
			generalSettings.add(getUserAsB2C(), null);
		}
		return generalSettings;
	}

	private JPanel getJPanelEmailSetting() {
		if (jPanelEmailSetting == null) {
			jLabel10 = new JLabel();
			jLabel10.setBounds(new Rectangle(289, 185, 91, 20));
			jLabel10.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel10.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_PASSWORD"));
			jLabel9 = new JLabel();
			jLabel9.setBounds(new Rectangle(289, 153, 91, 20));
			jLabel9.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel9.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_USER"));
			jLabel8 = new JLabel();
			jLabel8.setBounds(new Rectangle(289, 121, 91, 20));
			jLabel8.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel8.setText(EBISystem.i18n("EBI_LANG_POP_EMAIL_SERVER"));
			final JLabel jlabelF = new JLabel();
			jlabelF.setBounds(new Rectangle(289, 87, 91, 20));
			jlabelF.setFont(new Font("Dialog", Font.PLAIN, 12));
			jlabelF.setText(EBISystem.i18n("EBI_LANG_EMAIL_FOLDER_NAME"));
			final JLabel jLabel7 = new JLabel();
			jLabel7.setBounds(new Rectangle(16, 89, 264, 20));
			jLabel7.setText(EBISystem.i18n("EBI_LANG_EMAIL_SERVER_CONNECTION_DATA"));
			jPanelEmailSetting = new JPanel();
			jPanelEmailSetting.setLayout(null);
			jPanelEmailSetting.setOpaque(false);
			jPanelEmailSetting.setBounds(new Rectangle(19, 342, 572, 232));
			jPanelEmailSetting.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					EBISystem.i18n("EBI_LANG_EMAIL_SETTING"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			final JLabel jLabely8 = new JLabel();
			jLabely8.setBounds(new Rectangle(16, 59, 91, 20));
			jLabely8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabely8.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabely8.setText(EBISystem.i18n("EBI_LANG_EMAIL_TITLE"));
			final JLabel jLabely7 = new JLabel();
			jLabely7.setBounds(new Rectangle(16, 28, 91, 20));
			jLabely7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabely7.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabely7.setText(EBISystem.i18n("EBI_LANG_EMAIL_ADRESS"));
			final JLabel jLabely5 = new JLabel();
			jLabely5.setBounds(new Rectangle(2, 185, 105, 20));
			jLabely5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabely5.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabely5.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_PASSWORD"));
			final JLabel jLabely4 = new JLabel();
			jLabely4.setBounds(new Rectangle(16, 153, 91, 20));
			jLabely4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabely4.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabely4.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_USER"));
			final JLabel jLabely3 = new JLabel();
			jLabely3.setBounds(new Rectangle(16, 121, 91, 20));
			jLabely3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			jLabely3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabely3.setText(EBISystem.i18n("EBI_LANG_SMTP_EMAIL_SERVER"));
			final JLabel jLabely = new JLabel();
			jLabely.setBounds(new java.awt.Rectangle(35, 39, 0, 0));
			jPanelEmailSetting.add(jLabely, null);
			jPanelEmailSetting.add(jLabely3, null);
			jPanelEmailSetting.add(jLabely4, null);
			jPanelEmailSetting.add(jLabely5, null);
			jPanelEmailSetting.add(jlabelF, null);
			jPanelEmailSetting.add(getJTextEMailhost(), null);
			jPanelEmailSetting.add(getJTextEMailBenutzer(), null);
			jPanelEmailSetting.add(getJTextEMailpassword(), null);
			jPanelEmailSetting.add(jLabely7, null);
			jPanelEmailSetting.add(getDeleteMessage(), null);
			jPanelEmailSetting.add(getJTextEmailfrom(), null);
			jPanelEmailSetting.add(emailForUser(), null);
			jPanelEmailSetting.add(jLabely8, null);
			jPanelEmailSetting.add(getJTextFromTitle(), null);
			jPanelEmailSetting.add(jLabel7, null);
			jPanelEmailSetting.add(jLabel8, null);
			jPanelEmailSetting.add(jLabel9, null);
			jPanelEmailSetting.add(jLabel10, null);
			jPanelEmailSetting.add(getJTextpopServer(), null);
			jPanelEmailSetting.add(getInboxFolder(), null);
			jPanelEmailSetting.add(getJTextpopUser(), null);
			jPanelEmailSetting.add(getJTextpopPassword(), null);
		}
		return jPanelEmailSetting;
	}

	/**
	 * This method initializes jTextEMailhost
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextEMailhost() {
		if (jTextEMailhost == null) {
			jTextEMailhost = new JTextField();
			jTextEMailhost.setBounds(new Rectangle(111, 119, 175, 25));
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

	/**
	 * This method initializes jTextEMailBenutzer
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextEMailBenutzer() {
		if (jTextEMailBenutzer == null) {
			jTextEMailBenutzer = new JTextField();
			jTextEMailBenutzer.setBounds(new Rectangle(111, 151, 175, 25));
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

	/**
	 * This method initializes jTextEMailpassword
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextEMailpassword() {
		if (jTextEMailpassword == null) {
			jTextEMailpassword = new JPasswordField();
			jTextEMailpassword.setBounds(new Rectangle(111, 186, 175, 25));
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

	/**
	 * This method initializes jTextEmailfrom
	 * 
	 * @return javax.swing.JTextField
	 */
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

	private JComboBox emailForUser() {
		if (comboForUser == null) {
			comboForUser = new JComboBox();
			if (EBISystem.systemUsers != null) {
				comboForUser.setModel(new DefaultComboBoxModel<String>(EBISystem.systemUsers));
			}
			comboForUser.setBounds(new Rectangle(384, 26, 175, 25));
			comboForUser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					loadEMailSetting();
				}
			});
		}
		return comboForUser;
	}

	private JCheckBox getDeleteMessage() {
		if (deleteMessage == null) {
			deleteMessage = new JCheckBox();
			deleteMessage.setBounds(new Rectangle(384, 59, 175, 20));
			deleteMessage.setText(EBISystem.i18n("EBI_LANG_DELETE_EMAIL_MESSAGE"));
			deleteMessage.setFocusTraversalKeysEnabled(false);
			deleteMessage.setOpaque(false);
			deleteMessage.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return deleteMessage;
	}

	private JCheckBox getUserAsB2C() {
		if (isB2C == null) {
			isB2C = new JCheckBox();
			isB2C.setBounds(new Rectangle(142, 89, 200, 20));
			isB2C.setText(EBISystem.i18n("EBI_LANG_USE_AS_B2C"));
			isB2C.setFocusTraversalKeysEnabled(false);
			isB2C.setOpaque(false);
			isB2C.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return isB2C;
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
		this.deleteMessage.setEnabled(enabled ? false : true);
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

	/**
	 * This method initializes jTextFromTitle
	 * 
	 * @return javax.swing.JTextField
	 */
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

	/**
	 * This method initializes jTextEditorPath
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextEditorPath() {
		if (jTextEditorPath == null) {
			jTextEditorPath = new JTextField();
			jTextEditorPath.setBounds(new Rectangle(139, 96, 214, 25));

		}
		return jTextEditorPath;
	}

	/**
	 * This method initializes jButtonOpenTextEditor
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonOpenTextEditor() {
		if (jButtonOpenTextEditor == null) {
			jButtonOpenTextEditor = new JButton();
			jButtonOpenTextEditor.setBounds(new Rectangle(358, 96, 35, 25));
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

	/**
	 * This method initializes jTextpopServer
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextpopServer() {
		if (jTextpopServer == null) {
			jTextpopServer = new JTextField();
			jTextpopServer.setBounds(new Rectangle(384, 119, 175, 25));
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

	private JComboBox getInboxFolder() {
		if (jcomboEMailProtocol == null) {
			jcomboEMailProtocol = new JComboBox();
			jcomboEMailProtocol.addItem("pop3");
			jcomboEMailProtocol.addItem("imaps");
			jcomboEMailProtocol.setBounds(new Rectangle(384, 85, 175, 25));
			jcomboEMailProtocol.setFocusTraversalKeysEnabled(false);
			jcomboEMailProtocol.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					if (jcomboEMailProtocol.getSelectedItem().toString().equals("imaps")) {
						jLabel10.setText(jLabel10.getText().replaceAll("POP", "IMAP"));
						jLabel9.setText(jLabel9.getText().replaceAll("POP", "IMAP"));
						jLabel8.setText(jLabel8.getText().replaceAll("POP", "IMAP"));
					} else {
						jLabel10.setText(jLabel10.getText().replaceAll("IMAP", "POP"));
						jLabel9.setText(jLabel9.getText().replaceAll("IMAP", "POP"));
						jLabel8.setText(jLabel8.getText().replaceAll("IMAP", "POP"));
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

	/**
	 * This method initializes jTextpopUser
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextpopUser() {
		if (jTextpopUser == null) {
			jTextpopUser = new JTextField();
			jTextpopUser.setBounds(new Rectangle(384, 151, 175, 25));
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

	/**
	 * This method initializes jTextpopPassword
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextpopPassword() {
		if (jTextpopPassword == null) {
			jTextpopPassword = new JPasswordField();
			jTextpopPassword.setBounds(new Rectangle(384, 186, 175, 25));
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
				&& !"".equals(this.jTextEMailhost.getText()) && !"".equals(this.jTextEMailBenutzer.getText())
				&& !"".equals(String.valueOf(this.jTextEMailpassword.getPassword())) && !"".equals(this.jTextpopServer.getText())
				&& !"".equals(this.jTextpopUser.getText()) && !"".equals(String.valueOf(this.jTextpopPassword.getPassword()))) {

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
			if ("".equals(this.jTextpopServer.getText())) {
				EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_POP_SERVER"))
						.Show(EBIMessage.ERROR_MESSAGE);
				return false;
			}
			if ("".equals(this.jTextpopUser.getText())) {
				EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_POP_USER"))
						.Show(EBIMessage.ERROR_MESSAGE);
				return false;
			}
			if ("".equals(this.jTextpopPassword.getText())) {
				EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_INSERT_EMAIL_POP_PASSWORD"))
						.Show(EBIMessage.ERROR_MESSAGE);
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * This method initializes jComboBoxLanguage
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxLanguage() {
		if (jComboBoxLanguage == null) {
			jComboBoxLanguage = new JComboBox();
			jComboBoxLanguage.setBounds(new Rectangle(142, 27, 214, 25));
		}
		return jComboBoxLanguage;
	}

	/**
	 * This method initializes jComboDateFormat
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboDateFormat() {
		if (jComboDateFormat == null) {
			jComboDateFormat = new JComboBox();
			jComboDateFormat.setBounds(new Rectangle(142, 56, 214, 25));
			jComboDateFormat.setEditable(true);
			jComboDateFormat.addItem("dd.MM.yyyy");
			jComboDateFormat.addItem("MM.dd.yyyy");
			jComboDateFormat.addItem("yyyy.mm.dd");
			jComboDateFormat.addItem("dd/MM/yyyy");
			jComboDateFormat.addItem("MM/dd/yyyy");
			jComboDateFormat.addItem("yyyy/mm/dd");

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