package org.core;

import org.core.gui.dialogs.EBIReportSelection;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.gui.dialogs.EBIWinWaiting;
import org.sdk.interfaces.IEBIReportSystem;
import org.sdk.utils.EBIPropertiesLang;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Reportsystem class Initialize JasperReport, Managing Reports files
 */
public class EBIReportSystem implements IEBIReportSystem {

    public static Object[] report = null;
    public Map<String, Object> map = null;
    public static String fileName = "";
    public boolean showWindow = false;
    public boolean eMailRecord = false;
    public String strRecs = null;
    private final EBIWinWaiting wait = new EBIWinWaiting(EBISystem.i18n("EBI_LANG_LOAD_REPORT_DATA"));

    @Getter
    @Setter
    private String resourceReportPath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator + "reports"
            + File.separator;

    private String resourceTmpPath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator + "tmp"
            + File.separator;

    public void buildReport(final File reportFile) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                wait.setVisible(true);
            }
        });

        final Thread cmplRep = new Thread(new Runnable() {
            @Override
            public void run() {
                if (reportFile.isFile() && reportFile.getName().endsWith(".jrxml")) {

                    final String jsprFile = reportFile.getAbsolutePath().replace(".jrxml", ".jasper");
                    wait.setString("Compile Report:" + jsprFile);
                    try {
                        Files.deleteIfExists(Paths.get(jsprFile));
                    } catch (final IOException e) {
                        e.printStackTrace();
                        wait.setVisible(false);
                    }
                    // compile report
                    try {
                        JasperCompileManager.compileReportToFile(reportFile.getAbsolutePath(), jsprFile);
                    } catch (final JRException e) {
                        e.printStackTrace();
                        wait.setVisible(false);
                    }
                    wait.setVisible(false);
                }
            }
        });
        cmplRep.start();
    }

    @Override
    public void buildReports() {
        final Thread cmplRep = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    final File[] files
                            = new File(resourceReportPath).listFiles();
                    for (final File file : files) {
                        buildReport(file);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(EBIReportSystem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        cmplRep.start();
    }

    /**
     * Check for all available reports
     *
     * @param map
     * @return
     */
    public Object[] checkForReport(final Map<String, Object> map) {
        EBIReportSelection reportSelection;

        report = new Object[3];
        if (getReportCount() > 0) {
            reportSelection = new EBIReportSelection(this);
            reportSelection.setModal(true);
            reportSelection.setResizable(false);
            reportSelection.setVisible(true);
        } else {
            report[0] = "";
            report[1] = false;
        }
        return report;
    }

    /**
     * Check for available reports with a specified category
     *
     * @param map
     * @param category
     * @return
     */
    public Object[] checkForReport(final Map<String, Object> map, final String category) {
        EBIReportSelection reportSelection;

        report = new Object[3];
        if (getReportCount(category) > 0) {
            reportSelection = new EBIReportSelection(this, category);
            if (getReportCount(category) > 1) {
                reportSelection.setModal(true);
                reportSelection.setResizable(false);
                reportSelection.setVisible(true);
            } else {
                reportSelection.createReport();
            }

        } else {
            report[0] = "";
            report[1] = false;
        }
        return report;
    }

    /**
     * get all available reports
     *
     * @return
     */
    public int getReportCount() {
        int count = 0;
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement(
                    "SELECT * FROM SET_REPORTFORMODULE WHERE ISACTIVE <> 0 ORDER BY REPORTNAME ");
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            set.last();
            count = set.getRow();

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
        return count;
    }

    /**
     * get available reports from a category
     *
     * @param category
     * @return
     */
    public int getReportCount(final String category) {
        int count = 0;
        ResultSet set = null;
        try {
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement(
                    "SELECT * FROM SET_REPORTFORMODULE WHERE ISACTIVE <> 0 and REPORTCATEGORY=? ORDER BY REPORTNAME ");
            ps1.setString(1, category);
            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            set.last();
            count = set.getRow();
        } catch (final SQLException ex) {
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
        return count;
    }

    // overloading method 1
    @Override
    public void useReportSystem(final Map<String, Object> map) {
        this.map = map;
        report = checkForReport(map);

        if (report[0] == null) {
            return;
        }

        useReportSystemExt(map);
    }

    /**
     * Create reports show report select dialog
     *
     * @param map
     */
    public void useReportSystemExt(final Map<String, Object> map) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                wait.setVisible(true);
            }
        });

        final Runnable run = new Runnable() {

            @Override
            public void run() {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (!"".equals(report[0])) {

                                // if no report was selected release this method
                                if ("-1".equals(report[0].toString())) {
                                    return;
                                }

                                addParametertoReport(map);
                                final JasperPrint jasperPrint = JasperFillManager.fillReport(resourceReportPath + report[0].toString(), map,
                                        EBISystem.getInstance().iDB().getActiveConnection());

                                if ((Boolean) report[1] == true) {

                                    final String fileN = report[0].toString().replaceAll("[^\\p{L}\\p{N}]", "");
                                    JasperExportManager.exportReportToPdfFile(jasperPrint, resourceTmpPath + fileN + ".pdf");

                                    EBISystem.getInstance().openPDFReportFile(resourceTmpPath + fileN + ".pdf");

                                } else {
                                    JFrame.setDefaultLookAndFeelDecorated(false);
                                    final JasperViewer view = new JasperViewer(jasperPrint, false);
                                    view.setState(Frame.MAXIMIZED_BOTH);
                                    view.setVisible(true);
                                }

                            } else {
                                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_REPORT_FOUND"))
                                        .Show(EBIMessage.ERROR_MESSAGE);
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                        } finally {
                            if (wait != null) {
                                wait.setVisible(false);
                            }
                        }
                    }
                });
            }
        };

        final Thread loaderThread = new Thread(run, "ShowReportThread");
        loaderThread.start();
    }

    // overloading Method 2
    @Override
    public void useReportSystem(final Map<String, Object> map, final String category, final String fileName) {

        this.map = map;
        EBIReportSystem.fileName = fileName;
        report = checkForReport(map, category);

        if (report[0] == null) {
            return;
        }
        useReportSystemExt(map, category, fileName);
    }

    /**
     * Create and show reports
     *
     * @param map
     * @param category
     * @param fileName
     */
    public void useReportSystemExt(final Map<String, Object> map, final String category,
            final String fileName) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                wait.setVisible(true);
            }
        });

        final Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!"".equals(report[0])) {

                        // if no report was selected release this method
                        if ("-1".equals(report[0].toString())) {
                            return;
                        }

                        addParametertoReport(map);
                        final JasperPrint jasperPrint = JasperFillManager.fillReport(resourceReportPath + report[0].toString(), map,
                                EBISystem.getInstance().iDB().getActiveConnection());

                        if ((Boolean) report[1] == true) {
                            final String fN = fileName.replaceAll("[^\\p{L}\\p{N}]", "");
                            JasperExportManager.exportReportToPdfFile(jasperPrint, resourceTmpPath + fN + ".pdf");
                            EBISystem.getInstance().openPDFReportFile(resourceTmpPath + fN + ".pdf");

                        } else {
                            JFrame.setDefaultLookAndFeelDecorated(false);
                            final JasperViewer view = new JasperViewer(jasperPrint, false);
                            view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            view.setState(Frame.MAXIMIZED_BOTH);
                            view.setVisible(true);
                        }

                    } else {
                        EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_REPORT_FOUND"))
                                .Show(EBIMessage.ERROR_MESSAGE);
                    }
                } catch (final Exception ex) {
                    EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                    ex.printStackTrace();
                } finally {
                    if (wait != null) {
                        wait.setVisible(false);
                    }
                }
            }
        };

        final Thread loaderThread = new Thread(run, "ShowReportThread");
        loaderThread.start();
    }

    // overloading method 3
    @Override
    public String useReportSystem(final Map<String, Object> map, final String category, final String fileName,
            final boolean showWindows, final boolean EMailRecord, final String rec) {

        final Runnable run = new Runnable() {
            @Override
            public void run() {
                EBIReportSystem.this.map = map;
                EBIReportSystem.fileName = fileName;
                showWindow = showWindows;
                eMailRecord = EMailRecord;
                strRecs = rec;
                report = checkForReport(map, category);
                if (report[0] == null) {
                    return;
                }

                // EBIReportSystem.this.fileName = fileName.replaceAll(" ","_");
                // EBIReportSystem.this.fileName = fileName;
                // useReportSystemExt(map, category, fileName, showWindows, EMailRecord, rec);
                useReportSystemExt(map, category, fileName, showWindows, EMailRecord, rec);
            }
        };

        final Thread startReporting = new Thread(run, "Start Reporting");
        startReporting.start();

        return fileName;
    }

    /**
     * Create and show reports
     *
     * @param map
     * @param category
     * @param fileName
     * @param showWindows
     * @param EMailRecord
     * @return
     */
    public String useReportSystemExt(final Map<String, Object> map, final String category, String fileName,
            final boolean showWindows, final boolean EMailRecord, final String rec) {

        String fileToRet;
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    wait.setVisible(true);
                }
            });
            if (report != null && !"".equals(report[0])) {

                // if no report was selected release this method
                if ("-1".equals(report[0].toString())) {
                    return "-1";
                }

                addParametertoReport(map);

                final JasperPrint jasperPrint = JasperFillManager.fillReport(resourceReportPath + report[0].toString(), map,
                        EBISystem.getInstance().iDB().getActiveConnection());

                fileToRet = new File(resourceTmpPath + fileName.replaceAll(" ", "_") + ".pdf").getAbsolutePath();
                if ((Boolean) report[1] == true) {
                    fileName = fileName.replaceAll("[^\\p{L}\\p{N}]", "");
                    JasperExportManager.exportReportToPdfFile(jasperPrint, fileToRet);

                    if (showWindows) {
                        EBISystem.getInstance().openPDFReportFile(fileToRet);
                    }
                } else {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, fileToRet);
                    if (showWindows) {
                        EBISystem.getInstance().openPDFReportFile(fileToRet);
                    }
                }

                if (EMailRecord) {
                    String subject = "";
                    String bodyText = "";

                    if (EBISystem.gui().existView("sendEMailMessage")) {
                        subject = EBISystem.gui().textField("SubjectText", "sendEMailMessage").getText();
                        bodyText = EBISystem.gui().getEditor("MessageAreaText", "sendEMailMessage").getText();
                    }

                    final HashMap<String, String> EMAIL_PARAM = new HashMap<String, String>();
                    EMAIL_PARAM.put("_TO", rec);
                    EMAIL_PARAM.put("_SUBJECT", subject);
                    EMAIL_PARAM.put("_BODY", bodyText);
                    EMAIL_PARAM.put("_ATTACHMENT", fileToRet);

                    EBISystem.gui().addScriptBean("groovy",
                            "Run/sendEMailViaClient.groovy", "groovy", "", "EMail");
                    EBISystem.gui().excScript("EMail", EMAIL_PARAM);
                }

            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_NO_REPORT_FOUND"))
                        .Show(EBIMessage.ERROR_MESSAGE);
                fileToRet = "-1";
            }

        } catch (final Exception ex) {
            wait.setVisible(false);
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            fileToRet = "-1";
        } finally {
            wait.setVisible(false);
        }

        return fileToRet;
    }

    /**
     * Add system param to the report
     *
     * @param map
     */
    public void addParametertoReport(final Map<String, Object> map) {

        ResultSet set = null;
        ResultSet set1 = null;

        map.put("EBI_LANG", EBIPropertiesLang.getProperties().getProperty());
        map.put("EBI_ISB2C", EBISystem.USE_ASB2C);

        try {

            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM COMPANY com "
                    + "LEFT JOIN COMPANYBANK bnk ON com.COMPANYID=bnk.COMPANYID WHERE com.ISACTUAL=? ");

            ps1.setInt(1, 1);

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps1);

            set.last();
            if (set.getRow() > 0) {
                set.beforeFirst();
                set.next();
                map.put("COMPANY_NAME", set.getString("NAME"));
                map.put("COMPANY_NAME1", set.getString("NAME2"));
                map.put("COMPANY_TELEPHONE", set.getString("PHONE"));
                map.put("COMPANY_FAX", set.getString("FAX"));
                map.put("COMPANY_EMAIL", set.getString("EMAIL"));
                map.put("COMPANY_WEB", set.getString("WEB"));

                map.put("COMPANY_BANK_NAME", set.getString("BANKNAME"));
                map.put("COMPANY_BANK_ACCOUNT_NR", set.getString("BANKACCOUNT"));
                map.put("COMPANY_BANK_BSB", set.getString("BANKBSB"));
                map.put("COMPANY_BANK_BIC", set.getString("BANKBIC"));
                map.put("COMPANY_BANK_IBAN", set.getString("BANKIBAN"));
                map.put("COMPANY_BANK_COUNTRY", set.getString("BANKCOUNTRY"));
                map.put("COMPANY_TAX_INFORMATION", set.getString("TAXNUMBER"));
                final int companyID = set.getInt("COMPANYID");
                set.close();

                final PreparedStatement ps2 = EBISystem.getInstance().iDB()
                        .initPreparedStatement("SELECT * FROM COMPANYCONTACTS con "
                                + " LEFT JOIN COMPANYCONTACTADDRESS cadr ON con.CONTACTID=cadr.CONTACTID  WHERE con.COMPANYID=? AND con.MAINCONTACT=? ");
                ps2.setInt(1, companyID);
                ps2.setInt(2, 1);

                set1 = EBISystem.getInstance().iDB().executePreparedQuery(ps2);

                set1.last();
                if (set1.getRow() > 0) {
                    set1.beforeFirst();
                    set1.next();
                    map.put("COMPANY_CONTACT_NAME", set1.getString("NAME") == null ? "" : set1.getString("NAME"));
                    map.put("COMPANY_CONTACT_SURNAME",
                            set1.getString("SURNAME") == null ? "" : set1.getString("SURNAME"));
                    map.put("COMPANY_CONTACT_POSITION",
                            set1.getString("POSITION") == null ? "" : set1.getString("POSITION"));
                    map.put("COMPANY_CONTACT_EMAIL", set1.getString("EMAIL") == null ? "" : set1.getString("EMAIL"));
                    map.put("COMPANY_CONTACT_TELEPHONE",
                            set1.getString("PHONE") == null ? "" : set1.getString("PHONE"));
                    map.put("COMPANY_CONTACT_FAX", set1.getString("FAX") == null ? "" : set1.getString("FAX"));
                    map.put("COMPANY_STR_NR", set1.getString("STREET") == null ? "" : set1.getString("STREET"));
                    map.put("COMPANY_ZIP", set1.getString("ZIP") == null ? "" : set1.getString("ZIP"));
                    map.put("COMPANY_LOCATION", set1.getString("LOCATION") == null ? "" : set1.getString("LOCATION"));
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                set.close();
                if (set1 != null) {
                    set1.close();
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
