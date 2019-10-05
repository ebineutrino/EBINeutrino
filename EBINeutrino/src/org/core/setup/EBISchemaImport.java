package org.core.setup;

import org.core.EBIDatabase;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialog;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.Encrypter;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EBISchemaImport extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButtonCancel = null;
	private JButton jButtonImport = null;
	private JLabel jLabel1 = null;
	private JProgressBar jProgressBar = null;
	private int availableLine = 0;
	private FileInputStream fstream = null;
	private DataInputStream in = null;
	private double i = 0;
	private StringBuffer toImport = new StringBuffer();
	private boolean failed = true;
	private EBISystem _ebifunction = null;
	private String dbType = "";
	private final StringBuilder errorReport = new StringBuilder();
	private String catalog = "";
	private boolean useUpperCase = false;

	public EBISchemaImport(final EBISystem owner, final String databaseType, final String catalogDB, final boolean upperCase) {
		super();
		EBIDatabase.toUpperCase = upperCase;
		useUpperCase = upperCase;
		_ebifunction = owner;
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
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(11, 50, 455, 20));
			jLabel1.setText("Table:");
			final JLabel jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(132, 9, 266, 28));
			jLabel.setText("EBI Neutrino Database Schema Import");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButtonCancel(), null);
			jContentPane.add(getJButtonImport(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJProgressBar(), null);
		}
		return jContentPane;
	}

	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setBounds(new Rectangle(360, 125, 110, 30));
			jButtonCancel.setText("Cancel");
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

	private JButton getJButtonImport() {
		if (jButtonImport == null) {
			jButtonImport = new JButton();
			jButtonImport.setBounds(new Rectangle(235, 125, 120, 30));
			jButtonImport.setText("Import");
			jButtonImport.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {

					if ("oracle".equals(dbType)) { // Import Oracle schema
						if (!importDDLSchema()) {
							EBIExceptionDialog
									.getInstance(EBISchemaImport.this,
											"Import DDL schema was not successfully, the file format is damage!")
									.Show(EBIMessage.ERROR_MESSAGE);
						}
					} else if ("mysql".equals(dbType)) { // Import MySQL Schema
						if (!importSQLSchema("mysql.sql")) {
							EBIExceptionDialog
									.getInstance(EBISchemaImport.this,
											"Import SQL schema was not successfully, the file format is damage!")
									.Show(EBIMessage.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return jButtonImport;
	}

	private boolean importSQLSchema(final String fileName) {

		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				jButtonImport.setEnabled(false);
				jButtonCancel.setEnabled(false);
				try {

					if (useUpperCase) {
						catalog = catalog.toUpperCase();
					} else {
						catalog = catalog.toLowerCase();
					}

					_ebifunction.iDB().execExt("CREATE DATABASE IF NOT EXISTS " + catalog);
					_ebifunction.iDB().getActiveConnection().setCatalog(catalog);

					errorReport.append("\n");

					// Open the file that is the first command line parameter
					fstream = new FileInputStream(new File("sql/" + fileName));

					// Convert our input stream to a DataInputStream
					in = new DataInputStream(fstream);

					availableLine = in.available();

					boolean isFirstLine = true;
					while (in.available() != 0) {

						final String line = in.readLine();

						// show the first line to the import dialog label
						if (isFirstLine) {
							final String firstLine = line;
							final String tableShow = (firstLine.replace("(", "").replaceAll("create table if not exists", ""))
									.replaceAll("EBINEUTRINODB", catalog);
							jLabel1.setText("Table:" + tableShow);
						}

						if (!"/".equals(line.trim())) { // Not import until end of sql statement

							i += line.length() + 1;
							// Static replace why the hibernate is set as default to generate DATABASE named
							// EBINEUTRINODB
							if (!"EBINEUTRINODB".equals(catalog)) {
								toImport.append(line.replaceAll("EBINEUTRINODB.", catalog + ".") + "\n");
							} else {
								toImport.append(line + "\n");
							}

							// Calculate available line to import set the progressbar to the % result
							jProgressBar.setValue(((int) ((i / availableLine) * 100))); // Irrational result ;)
							isFirstLine = false;

						} else if (!"".equals(line.trim())) { // end of sql statement import to db
							// SQL Import Table
							if (toImport.toString().length() > 1) {
								_ebifunction.iDB().execExt(toImport.toString().toUpperCase());
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
						jButtonImport.setEnabled(true);
					} else {
						createAdminUser();
						jButtonImport.setVisible(false);
						jButtonCancel.setText("Finish");
					}

					jButtonCancel.setEnabled(true);

				} catch (final IOException ex) {
					ex.printStackTrace();
					EBIExceptionDialog.getInstance(EBISchemaImport.this, EBISystem.printStackTrace(ex))
							.Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
					ex.printStackTrace();
					errorReport.append(EBISystem.printStackTrace(ex));
					errorReport.append("\n");
					failed = false;
					setVisible(false);
				} catch (final SQLException ex) {
					ex.printStackTrace();
					errorReport.append(EBISystem.printStackTrace(ex));
					errorReport.append("\n");
					ex.printStackTrace();
					setVisible(false);
				} finally {
					try {
						if (in != null) {
							in.close();
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

	private boolean importDDLSchema() {

		final Runnable runnable = new Runnable() {
			@Override
			public void run() {

				jButtonImport.setEnabled(false);
				jButtonCancel.setEnabled(false);
				try {

					// Open the file that is the first command line parameter
					fstream = new FileInputStream(new File("sql/oracle.ddl"));

					// Convert our input stream to a DataInputStream
					in = new DataInputStream(fstream);

					availableLine = in.available();

					boolean isFirstLine = true;
					while (in.available() != 0) {

						final String line = in.readLine();
						if (isFirstLine) {
							final String firstLine = line;
							jLabel1.setText(
									"Table:" + firstLine.replace("(", "").replaceAll("create table", "").trim());
							isFirstLine = false;
						}
						if (!"/".equals(line.trim())) {

							i += line.length() + 1;
							toImport.append(line.replaceAll("^\\s+", "")); // Remove the begin white space for oracle!
							final double prz = (i / availableLine) * 100;
							jProgressBar.setValue((int) prz);

							try {
								Thread.sleep(20);
							} catch (final InterruptedException ex) {
							}
							isFirstLine = false;

						} else {
							// SQL Import Table
							if (toImport.length() > 1) {

								_ebifunction.iDB().execExt(toImport.toString());
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
						jButtonImport.setEnabled(true);
					} else {
						createAdminUser();
						jButtonImport.setVisible(false);
						jButtonCancel.setText("Finish");
					}

					jButtonCancel.setEnabled(true);
					in.close();

				} catch (final IOException ex) {
					errorReport.append(EBISystem.printStackTrace(ex));
					errorReport.append("\n");
					failed = false;
				} catch (final SQLException ex) {
					ex.printStackTrace();
				}
			}
		};
		// Close the input stream
		final Thread thread = new Thread(runnable);
		thread.start();

		return failed;
	}

	/**
	 * Insert a default EBI Neutrino user after import schema
	 */
	private void createAdminUser() {

		try {
			_ebifunction.iDB().getActiveConnection().setCatalog(catalog);

			final String sqlUsr = "INSERT INTO EBIUSER "
					+ "(ID,EBIUSER,PASSWD,CREATEDDATE,CREATEDFROM,IS_ADMIN,CANSAVE,CANPRINT,CANDELETE) "
					+ "VALUES(?,?,?,?,?,?,?,?,?) ";

			final PreparedStatement ps = _ebifunction.iDB().initPreparedStatement(sqlUsr);
			ps.setInt(1, 1);
			ps.setString(2, "root");
			ps.setString(3, generatePassword("ebineutrino"));
			ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(5, "Installer");
			ps.setInt(6, 1);
			ps.setInt(7, 0);
			ps.setInt(8, 0);
			ps.setInt(9, 0);

			_ebifunction.iDB().executePreparedStmt(ps);

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
		panes2.setViewportView(new JEditorPane("text/html",
				"".equals(errorReport.toString()) ? "<b>No Errors available</b>" : errorReport.toString()));
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

	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(10, 73, 462, 30));
			jProgressBar.setMaximum(100);
			jProgressBar.setMinimum(0);
			jProgressBar.setStringPainted(true);

		}
		return jProgressBar;
	}
}