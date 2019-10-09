package org.sdk;

import org.modules.EBIModule;
import org.core.EBIDatabase;
import org.core.EBIMain;
import org.core.gui.component.EBIExtensionContainer;
import org.core.gui.component.EBIToolbar;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIImageViewer;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Company;
import org.sdk.model.hibernate.Ebiuser;
import org.sdk.utils.EBIDBLocking;
import org.sdk.utils.EBIPropertiesLang;
import org.sdk.utils.EBIPropertiesRW;
import org.sdk.utils.Encrypter;
import org.sdk.workflow.security.EBISecurityManagement;
import org.sdk.workflow.security.EBISystemUserRights;
import groovy.lang.Script;
import org.sdk.interfaces.IEBIDatabase;
import org.sdk.interfaces.IEBISystemUserRights;
import org.apache.log4j.Logger;
import org.hibernate.query.Query;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import org.sdk.interfaces.IEBIContainer;
import org.sdk.interfaces.IEBIGUIRenderer;
import org.sdk.interfaces.IEBIHibernate;
import org.sdk.interfaces.IEBIModule;
import org.sdk.interfaces.IEBIReportSystem;
import org.sdk.interfaces.IEBISecurity;
import org.sdk.interfaces.IEBIToolBar;

/**
 * Factory Database, Reporting, GUI, Resources
 */
public class EBISystem {

    public static GregorianCalendar calendar = null;
    public static String ebiUser = null;
    public static boolean USE_ASB2C = false;
    public static String[] systemUsers = null;
    public static String emailSMTPServer = "";
    public static String emailSMTPUser = "";
    public static String emailSMTPPassword = "";
    public static String emailPOPServer = "";
    public static String emailPOPPUser = "";
    public static String emailPOPPPassword = "";
    public static String emailFrom = "";
    public static String emailFromTitle = "";
    public static String emailProtocolName = "";
    public static boolean emailDeleteMessageFromServer = false;
    public static String host = "";
    public static String updateServer = "";
    public static String lastLoggedUser = "";
    public static String DateFormat = "";
    public static String DATABASE_SYSTEM = "";
    public static String selectedLanguage = "";
    @Getter
    @Setter
    private Company company = null;
    public static Logger logger = Logger.getLogger(EBISystem.class.getName());
    public static boolean canRelease = true;
    public static boolean isSaveOrUpdate = false;
    public EBISecurityManagement security = null;
    private IEBIHibernate hibernate = null;
    private IEBIToolBar toolbar = null;
    private IEBIContainer container = null;
    private IEBIDatabase database = null;
    private IEBIReportSystem report = null;
    private IEBIGUIRenderer gui = null;
    public IEBIModule ebiModule = null;
    private EBISystemUserRights userRights = null;
    private IEBISystemUserRights iuserRights = null;
    @Getter
    @Setter
    private EBIExceptionDialog dialogMessage = null;
    private EBIDBLocking plock = null;
    private Hashtable<String, Script> storableFactory = null;
    public static List registeredModule = new ArrayList<String>();
    private EBIMain mainFrame = null;
    private HashMap<String, Object> map = new HashMap<String, Object>();
    private JFileChooser fileDialog = null;
    private static EBISystem ebiSystem = null;
    private HashMap<String, Object> mappedBbeans = new HashMap();
    @Getter @Setter
    private EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

    public EBISystem() {
        calendar = new GregorianCalendar();
        plock = new EBIDBLocking();
        storableFactory = new Hashtable<String, Script>();
        dialogMessage = EBIExceptionDialog.getInstance();
        fileDialog = new JFileChooser();
    }

    protected void getLanguageInstance(final String langFile, final boolean reload) {
        // Read properties file.
        parseLanguageFrom(langFile);
        EBIPropertiesLang.getProperties().loadLanguageProperties(langFile);
    }

    /**
     * Reload EBI Neutrino translation system
     *
     */
    public void reloadTranslationSystem() {
        getLanguageInstance("./" + EBIPropertiesRW.getEBIProperties().getValue("EBI_Neutrino_Language_File"), true);
    }

    private void parseLanguageFrom(final String name) {
        try {
            String lName;
            if ((lName = name.substring(name.lastIndexOf("_") + 1)) != null) {
                if (!"".equals(lName) && lName != null) {
                    if ((lName = lName.substring(0, lName.lastIndexOf("."))) != null) {
                        if (!"".equals(lName)) {
                            if ("English".equals(lName)) {
                                selectedLanguage = "English";
                            } else if ("Deutsch".equals(lName)) {
                                selectedLanguage = "German";
                            } else if ("Italiano".equals(lName)) {
                                selectedLanguage = "Italian";
                            }
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
        }
    }

    /**
     * Return a selected language word as String
     *
     * @param key
     * @return
     */
    public static String i18n(final String key) {
        String val = "";
        try {
            val = EBIPropertiesLang.getProperties().getValue(key);
            if ("".equals(val)) {
                EBIPropertiesLang.getProperties().setValue(key, val);
                val = key;
                EBIPropertiesLang.getProperties().saveProperties();
            }
        } catch (final NullPointerException ex) {
            logger.error("Exception", ex.fillInStackTrace());
            return key;
        }

        return val;
    }

    
    /**
     * Return the EBINeutrino Properties
     * @return 
     */
    public static EBIPropertiesRW properties(){
        return EBISystem.getInstance().getProperties();
    }
    
    /**
     * Read EMail Setting from a Database
     */
    protected boolean readEMailSettings(final IEBIDatabase idata) {

        ResultSet set = null;

        try {

            final PreparedStatement ps1 = idata.initPreparedStatement("SELECT * FROM MAIL_ACCOUNT WHERE CREATEFROM=? ");
            ps1.setString(1, ebiUser);
            set = idata.executePreparedQuery(ps1);

            if (set != null) {
                set.last();
                if (set.getRow() > 0) {
                    set.beforeFirst();
                    set.next();
                    EBISystem.emailFrom = set.getString("EMAILADRESS");
                    EBISystem.emailFromTitle = set.getString("EMAILS_TITLE");
                    EBISystem.emailProtocolName = set.getString("FOLDER_NAME");
                    EBISystem.emailDeleteMessageFromServer = set.getInt("DELETE_MESSAGE") == 1 ? true : false;
                    EBISystem.emailSMTPServer = set.getString("SMTP_SERVER");
                    EBISystem.emailSMTPUser = set.getString("SMTP_USER");
                    EBISystem.emailSMTPPassword = set.getString("SMTP_PASSWORD");
                    EBISystem.emailPOPServer = set.getString("POP_SERVER");
                    EBISystem.emailPOPPUser = set.getString("POP_USER");
                    EBISystem.emailPOPPPassword = set.getString("POP_PASSWORD");
                }
            } else {
                return false;
            }

        } catch (final SQLException ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    logger.error("Exception", e.fillInStackTrace());
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /**
     * Reload E-Mail Settings from Database
     *
     */
    public void reloadEMailSetting() {
        readEMailSettings(this.iDB());
    }

    /**
     * Format date to String
     *
     * @param source
     * @return
     */
    public String getDateToString(final Date source) {
        String format;
        if (source != null) {
            final DateFormat df = new SimpleDateFormat(DateFormat);
            format = df.format(source);
        } else {
            format = "";
        }
        return format;

    }

    /**
     * Format String to java.util.Date
     *
     * @param source
     * @return if source null return actual as type java.util.date
     */
    public java.util.Date getStringToDate(final String source) {
        Date date = new java.util.Date();
        if (source != null && !"".equals(source)) {
            final DateFormat df = new SimpleDateFormat(DateFormat);

            try {
                date = df.parse(source);
            } catch (final ParseException ex) {
                logger.error("Exception", ex.fillInStackTrace());
                EBIExceptionDialog.getInstance("Date format Error\n " + EBISystem.printStackTrace(ex))
                        .Show(EBIMessage.ERROR_MESSAGE);
            }
        }
        return date;
    }

    /**
     * This Function check for valid user on Database database
     *
     * @param user
     * @param pw
     * @return boolean
     */
    public boolean checkIsValidUser(final String user, final String pw) {

        try {
            hibernate.openHibernateSession("LOGINSESSION");
            registeredModule.clear();
            hibernate.transaction("LOGINSESSION").begin();

            final Query query = hibernate.session("LOGINSESSION")
                    .createQuery("from Ebiuser user where user.ebiuser=?1", Ebiuser.class).setParameter(1, user);

            if (query.list().size() > 0) {
                final Iterator it = query.iterate();
                final Ebiuser User = (Ebiuser) it.next();
                hibernate.session("LOGINSESSION").refresh(User);
                // check password
                final String pass = this.decryptPassword(User.getPasswd() == null ? "" : User.getPasswd());
                if (!pw.equals(pass)) {
                    return false;
                }

                ebiUser = User.getEbiuser();
                userRights = new EBISystemUserRights();
                userRights.setUserName(ebiUser);
                userRights.setCanDelete(User.getCandelete());
                userRights.setCanSave(User.getCansave());
                userRights.setCanPrint(User.getCanprint());
                userRights.setAdministrator(User.getIsAdmin());
                iuserRights = userRights;
                final String[] path = new String[]{"Summary/summaryGUI.xml", "Leads/leadsGUI.xml", "Company/companyGUI.xml",
                    "Contact/contactGUI.xml", "Address/addressGUI.xml", "Bank/bankGUI.xml",
                    "MeetingsCallManagement/meetingcallGUI.xml", "Activity/activityGUI.xml",
                    "Opportunity/opportunityGUI.xml", "Offer/offerGUI.xml", "Order/orderGUI.xml",
                    "CRMService/serviceGUI.xml", "Mailing", "Product/productGUI.xml", "Calendar",
                    "Campaign/campaignGUI.xml", "CRMProblemSolution/problemSolutionGUI.xml",
                    "Invoice/invoiceGUI.xml", "AccountStack/accountGUI.xml", "Project/projectGUI.xml"};

                if (User.getModuleid() != null) {
                    final String[] pt = User.getModuleid().split("_");
                    for (int i = 0; i < pt.length; i++) {
                        registeredModule.add(path[Integer.parseInt(pt[i])]);
                    }
                }
                registeredModule.add("CRMDialog/crmPessimisticView.xml");
                registeredModule.add("CRMToolbar/crmToolbar.xml");
                registeredModule.add("CRMDialog/exportDataDialog.xml");
                registeredModule.add("CRMDialog/crmCompanySearch.xml");
                registeredModule.add("CRMDialog/accountShowPTAX.xml");
                registeredModule.add("CRMDialog/addNewContactDialog.xml");
                registeredModule.add("CRMDialog/addnewReceiverDialogCampaign.xml");
                registeredModule.add("CRMDialog/autoIncNrDialog.xml");
                registeredModule.add("CRMDialog/costValueDialog.xml");
                registeredModule.add("CRMDialog/creditDebitDialog.xml");
                registeredModule.add("CRMDialog/crmContactSearch.xml");
                registeredModule.add("CRMDialog/crmHistoryDialog.xml");
                registeredModule.add("CRMDialog/crmSelectionDialog.xml");
                registeredModule.add("CRMDialog/crmSettingDialog.xml");
                registeredModule.add("CRMDialog/emailTemplateDialog.xml");
                registeredModule.add("CRMDialog/importDataDialog.xml");
                registeredModule.add("CRMDialog/newProjectTaskDialog.xml");
                registeredModule.add("CRMDialog/productInsertDialog.xml");
                registeredModule.add("CRMDialog/productSearchDialog.xml");
                registeredModule.add("CRMDialog/propertiesDialog.xml");
                registeredModule.add("CRMDialog/sendMailDialogGUI.xml");
                registeredModule.add("CRMDialog/taxAdminDialog.xml");
                registeredModule.add("CRMDialog/valueSetDialog.xml");
                registeredModule.add("CRMDialog/csvSetImport.xml");
                registeredModule.add("CRMDialog/insertCSVDataDialog.xml");
                registeredModule.add("CRMDialog/timerDialog.xml");
                registeredModule.add("CRMDialog/printSetup.xml");

            } else {
                return false;
            }
            hibernate.transaction("LOGINSESSION").commit();
            hibernate.removeHibernateSession("LOGINSESSION");

            readEMailSettings(iDB());
            loadStandardCompanyData();
        } catch (final org.hibernate.HibernateException ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
            ex.printStackTrace();
        }

        return true;
    }

    /**
     * Generate EBI Neutirno user password
     *
     * @param password
     * @return generated password as a string
     */
    public String encryptPassword(final String password) {
        Encrypter encrypter = new Encrypter("EBINeutrino");

        // Encrypt
        final String pwd = encrypter.encrypt(password);
        encrypter = null;
        return pwd;
    }

    /**
     * Decrypt EBI Neutirno user password
     *
     * @param password
     * @return generated password as a string
     */
    public String decryptPassword(final String password) {
        Encrypter encrypter = new Encrypter("EBINeutrino");

        // Encrypt
        final String pwd = encrypter.decrypt(password);
        encrypter = null;
        return pwd;
    }

    /**
     * Open report file
     *
     * @param filename
     */
    public void openPDFReportFile(final String filename) {
        try {

            final String pdf_path = EBIPropertiesRW.getEBIProperties().getValue("EBI_Neutrino_PDF");

            if (System.getProperty("os.name").equals("Linux")) {
                Runtime.getRuntime().exec(pdf_path + " " + filename);
            } else {
                Runtime.getRuntime().exec("cmd /C start " + pdf_path + " " + filename);
            }

        } catch (final Exception ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
            EBIExceptionDialog.getInstance(i18n("EBI_LANG_ERROR_PDF_PROG_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    /**
     * Open a Text reader program specified in the ebiNeutrino.properties
     *
     * @param fileName
     */
    public void openTextDocumentFile(final String fileName) {
        try {

            String path_office = EBIPropertiesRW.getEBIProperties().getValue("EBI_Neutrino_TextEditor_Path");

            if (!path_office.equals("System Registry")) {

                if (path_office.indexOf(' ') != -1) {
                    path_office = "\"" + path_office + "\"";
                }

            } else {
                if (System.getProperty("os.name").equals("Linux")) {
                    path_office = "ooffice -writer";
                } else {
                    path_office = "ooffice.exe -writer";
                }
            }

            if (System.getProperty("os.name").equals("Linux")) {
                Runtime.getRuntime().exec(path_office + " " + fileName);
            } else {
                Runtime.getRuntime().exec("cmd /C start " + path_office + " " + fileName);
            }

        } catch (final Exception ex) {
            logger.error("Exception", ex.fillInStackTrace());
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex) + i18n("EBI_LANG_ERROR_TXT_PROG_NOT_FOUND"))
                    .Show(EBIMessage.ERROR_MESSAGE);
        }

    }

    /**
     * return the IEBISystemUserRights Interface
     */
    public IEBISystemUserRights getIEBISystemUserRights() {
        return iuserRights == null ? userRights : iuserRights;
    }

    public static IEBISystemUserRights getUserRight() {
        return EBISystem.getInstance().getIEBISystemUserRights();
    }

    public void setIEBISystemUserRights(final EBISystemUserRights right) {
        this.userRights = right;
    }

    public static IEBIDatabase db() {
        return EBISystem.getInstance().iDB();
    }

    /**
     * return the IEBIDatabase Interface
     */
    public IEBIDatabase iDB() {
        return database;
    }

    /**
     * return the IEBIDatabase Interface
     *
     * @param obj
     */
    public void setIEBIDatabase(final Object obj) {
        this.database = (EBIDatabase) obj;
    }

    public static IEBIHibernate hibernate() {
        return EBISystem.getInstance().getIEBIHibernate();
    }

    public IEBIHibernate getIEBIHibernate() {
        return hibernate;
    }

    public void setIEBIHibernate(final IEBIHibernate hobj) {
        this.hibernate = hobj;
    }

    /**
     * return the IEBISecurity interface
     *
     * @return Initialize the Interface IEBISecurity
     */
    public IEBISecurity getIEBISecurityInstance() {
        if (security == null) {
            security = new EBISecurityManagement();
        }
        return security;
    }

    public static IEBISecurity getSecurity() {
        return EBISystem.getInstance().getIEBISecurityInstance();
    }

    /**
     * return the default IEBIToolBar instance
     *
     * @return IEBIToolBar Interface instance
     */
    public IEBIToolBar getIEBIToolBarInstance() {
        return this.toolbar;
    }

    public static IEBIToolBar getToolbar() {
        return EBISystem.getInstance().getIEBIToolBarInstance();
    }

    /**
     * Set the system default EBIToolbar
     *
     * @param obj
     */
    public void setIEBIToolBarInstance(final Object obj) {
        this.toolbar = (EBIToolbar) obj;
    }

    /**
     * Set the system default EBIExtensionContainer
     *
     * @param obj
     */
    public void setIEBIContainerInstance(final Object obj) {
        this.container = (EBIExtensionContainer) obj;
    }

    public static IEBIContainer getUIContainer() {
        return EBISystem.getInstance().getIEBIContainerInstance();
    }

    /**
     * return the default IEBIContainer instance
     *
     * @return IEBIContainer Interface instance
     */
    public IEBIContainer getIEBIContainerInstance() {
        return this.container;
    }

    /**
     * return the default IEBIReportSystem instance
     *
     * @return IEBIContainer Interface instance
     */
    public IEBIReportSystem getIEBIReportSystemInstance() {
        return this.report;
    }

    public IEBIReportSystem getReportSystem() {
        return EBISystem.getInstance().getIEBIReportSystemInstance();
    }

    /**
     * return the default IEBIGUIRenderer Instance
     *
     * @return
     */
    public IEBIGUIRenderer getIEBIGUIRendererInstance() {
        return this.gui;
    }

    public void setIEBIGUIRendererInstance(final Object renderer) {
        this.gui = (IEBIGUIRenderer) renderer;
    }

    public static IEBIGUIRenderer gui() {
        return EBISystem.getInstance().getIEBIGUIRendererInstance();
    }

    /**
     * Set the system default ReportSystem Interface
     *
     * @param obj
     */
    public void setIEBIReportSystemInstance(final Object obj) {
        this.report = (IEBIReportSystem) obj;
    }

    public void setIEBIModule(final Object module) {
        ebiModule = (IEBIModule) module;
    }

    public static EBIModule getModule() {
        return (EBIModule) EBISystem.getInstance().getIEBIModule();
    }

    public Object getIEBIModule() {
        return ebiModule.getActiveModule();
    }

    public void fillComboWithUser() {
        final PreparedStatement ps1 = iDB().initPreparedStatement("SELECT EBIUSER FROM EBIUSER ");
        final ResultSet resultSet = iDB().executePreparedQuery(ps1);

        if (resultSet != null) {

            try {
                resultSet.last();
                final int size = resultSet.getRow();

                if (size > 0) {
                    systemUsers = new String[size + 1];
                    systemUsers[0] = EBISystem.i18n("EBI_LANG_PLEASE_SELECT");
                    resultSet.beforeFirst();
                    int i = 1;
                    while (resultSet.next()) {
                        systemUsers[i++] = resultSet.getString("EBIUSER");
                    }
                }
            } catch (final SQLException ex) {
                ex.printStackTrace();
                logger.error("Exception", ex.fillInStackTrace());
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            } finally {
                try {
                    resultSet.close();
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                    logger.error("Exception", ex.fillInStackTrace());
                    EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                }
            }
        }
    }

    public void setDataStore(final String packageName, final Script storeAdapter) {
        storableFactory.put(packageName, storeAdapter);
    }

    public void getDataStore(final String packageName, final String method) {
        if (storableFactory.get(packageName) != null) {
            if (storableFactory.get(packageName).getMetaClass().getMetaMethod(method, null) != null) {
                storableFactory.get(packageName).invokeMethod(method, null);
            }
        }
    }

    public void resetDataStore() {
        storableFactory.clear();
    }

    public static void showInActionStatus(final String name) {

        final Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    EBISystem.gui().vpanel(name).drawProgress(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        final Thread tr = new Thread(run, "Save Module");
        tr.start();
    }

    /**
     * Exception to string
     *
     * @param ex
     * @return
     */
    public static String printStackTrace(final Exception ex) {
        final StringBuffer buildTrace = new StringBuffer();
        final StackTraceElement[] element = ex.getStackTrace();

        buildTrace.append("" + ex.getMessage() + "\n");
        buildTrace.append(" Cause :" + ex.getCause() + "\n");

        logger.error(buildTrace.toString(), ex.fillInStackTrace());
        logger.info(buildTrace.toString());
        return buildTrace.toString();
    }

    public static boolean isWindows() {

        final String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);
    }

    public static boolean isMac() {

        final String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {

        final String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    public String convertReportCategoryToIndex(final String category) {
        String index = "0";

        if (EBISystem.i18n("EBI_LANG_PRINT_VIEWS").equals(category)) {
            index = "0";
        } else if (EBISystem.i18n("EBI_LANG_PRINT_INVOICES").equals(category)) {
            index = "1";
        } else if (EBISystem.i18n("EBI_LANG_PRINT_DELIVERIES").equals(category)) {
            index = "2";
        } else if (EBISystem.i18n("EBI_LANG_PRINT_STATISTIC").equals(category)) {
            index = "3";
        } else if (EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL").equals(category)) {
            index = "4";
        } else if (EBISystem.i18n("EBI_LANG_C_OFFER").equals(category)) {
            index = "5";
        } else if (EBISystem.i18n("EBI_LANG_C_ORDER").equals(category)) {
            index = "6";
        } else if (EBISystem.i18n("EBI_LANG_C_OPPORTUNITY").equals(category)) {
            index = "7";
        } else if (EBISystem.i18n("EBI_LANG_C_CAMPAIGN").equals(category)) {
            index = "8";
        } else if (EBISystem.i18n("EBI_LANG_C_PRODUCT").equals(category)) {
            index = "9";
        } else if (EBISystem.i18n("EBI_LANG_C_PROSOL").equals(category)) {
            index = "10";
        } else if (EBISystem.i18n("EBI_LANG_PROJECT").equals(category)) {
            index = "11";
        } else if (EBISystem.i18n("EBI_LANG_C_SERVICE").equals(category)) {
            index = "12";
        } else if (EBISystem.i18n("EBI_LANG_PRINT_ACCOUNT").equals(category)) {
            index = "13";
        } else if (EBISystem.i18n("EBI_LANG_PRINT_OTHERS").equals(category)) {
            index = "14";
        }

        return index;
    }

    public void addMainFrame(final EBIMain frm) {
        this.mainFrame = frm;
    }

    public EBIMain getMainFrame() {
        return this.mainFrame;
    }

    public void loadStandardCompanyData() {

        ResultSet set = null;
        ResultSet set1 = null;

        try {

            final PreparedStatement ps1 = iDB().initPreparedStatement("SELECT * FROM COMPANY com "
                    + "LEFT JOIN COMPANYBANK bnk ON com.COMPANYID=bnk.COMPANYID WHERE com.ISACTUAL=? ");

            ps1.setInt(1, 1);
            set = iDB().executePreparedQuery(ps1);

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

                final PreparedStatement ps2 = iDB().initPreparedStatement("SELECT * FROM COMPANYCONTACTS con "
                        + " LEFT JOIN COMPANYCONTACTADDRESS cadr ON con.CONTACTID=cadr.CONTACTID  WHERE con.COMPANYID=? AND con.MAINCONTACT=? ");
                ps2.setInt(1, companyID);
                ps2.setInt(2, 1);
                set1 = iDB().executePreparedQuery(ps2);

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

        } catch (final SQLException ex) {
            ex.printStackTrace();
            logger.error("Exception", ex.fillInStackTrace());
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (set1 != null) {
                    set1.close();
                }
            } catch (final SQLException ex) {
                logger.error("Exception", ex.fillInStackTrace());
                ex.printStackTrace();
            }
        }
    }

    public String convertReportIndexToCategory(final int index) {

        String category = EBISystem.i18n("EBI_LANG_PRINT_VIEWS");

        if (index == 0) {
            category = EBISystem.i18n("EBI_LANG_PRINT_VIEWS");
        } else if (index == 1) {
            category = EBISystem.i18n("EBI_LANG_PRINT_INVOICES");
        } else if (index == 2) {
            category = EBISystem.i18n("EBI_LANG_PRINT_DELIVERIES");
        } else if (index == 3) {
            category = EBISystem.i18n("EBI_LANG_PRINT_STATISTIC");
        } else if (index == 4) {
            category = EBISystem.i18n("EBI_LANG_C_MEETING_PROTOCOL");
        } else if (index == 5) {
            category = EBISystem.i18n("EBI_LANG_C_OFFER");
        } else if (index == 6) {
            category = EBISystem.i18n("EBI_LANG_C_ORDER");
        } else if (index == 7) {
            category = EBISystem.i18n("EBI_LANG_C_OPPORTUNITY");
        } else if (index == 8) {
            category = EBISystem.i18n("EBI_LANG_C_CAMPAIGN");
        } else if (index == 9) {
            category = EBISystem.i18n("EBI_LANG_C_PRODUCT");
        } else if (index == 10) {
            category = EBISystem.i18n("EBI_LANG_C_PROSOL");
        } else if (index == 11) {
            category = EBISystem.i18n("EBI_LANG_PROJECT");
        } else if (index == 12) {
            category = EBISystem.i18n("EBI_LANG_C_SERVICE");
        } else if (index == 13) {
            category = EBISystem.i18n("EBI_LANG_PRINT_ACCOUNT");
        } else if (index == 14) {
            category = EBISystem.i18n("EBI_LANG_PRINT_OTHERS");
        }

        return category;
    }

    public File getOpenDialog(final int type) {

        fileDialog.setFileSelectionMode(type);
        final String selFile = EBIPropertiesRW.getEBIProperties().getValue("EBI_BROWSE_OLD_PATH");
        if (!"".equals(selFile)) {
            fileDialog.setSelectedFile(new File(selFile));
        }

        final int result = fileDialog.showOpenDialog(getMainFrame());

        if (fileDialog.getSelectedFile() != null) {
            EBIPropertiesRW.getEBIProperties().setValue("EBI_BROWSE_OLD_PATH",
                    fileDialog.getSelectedFile().getAbsolutePath());
        }
        return result == JFileChooser.APPROVE_OPTION ? fileDialog.getSelectedFile() : null;
    }

    public File getSaveDialog(final int type) {

        fileDialog.setFileSelectionMode(type);

        final String selFile = EBIPropertiesRW.getEBIProperties().getValue("EBI_BROWSE_OLD_PATH");
        if (!"".equals(selFile)) {
            fileDialog.setSelectedFile(new File(selFile));
        }

        final int result = fileDialog.showSaveDialog(getMainFrame());

        EBIPropertiesRW.getEBIProperties().setValue("EBI_BROWSE_OLD_PATH",
                fileDialog.getSelectedFile().getAbsolutePath());

        return result == JFileChooser.APPROVE_OPTION ? fileDialog.getSelectedFile() : null;
    }

    public File getSaveDialog(final int type, final String selectedFile) {

        fileDialog.setFileSelectionMode(type);

        if ("".equals(selectedFile)) {
            final String selFile = EBIPropertiesRW.getEBIProperties().getValue("EBI_BROWSE_OLD_PATH");
            if (!"".equals(selFile)) {
                fileDialog.setSelectedFile(new File(selFile));
            }
        } else {
            fileDialog.setSelectedFile(new File(selectedFile));
        }

        final int result = fileDialog.showSaveDialog(getMainFrame());

        EBIPropertiesRW.getEBIProperties().setValue("EBI_BROWSE_OLD_PATH",
                fileDialog.getSelectedFile().getAbsolutePath());

        return result == JFileChooser.APPROVE_OPTION ? fileDialog.getSelectedFile() : null;
    }

    public void resolverType(final String fileName, final String type) {
        if (".jpg".equals(type) || ".gif".equals(type) || ".png".equals(type)) {
            final EBIImageViewer view = new EBIImageViewer(EBISystem.getInstance().getMainFrame(), new javax.swing.ImageIcon(fileName));
            view.setVisible(true);
        } else if (".pdf".equals(type)) {
            EBISystem.getInstance().openPDFReportFile(fileName);
        } else if (".doc".equals(type)) {
            EBISystem.getInstance().openTextDocumentFile(fileName);
        } else {
            EBISystem.getInstance().openTextDocumentFile(fileName);
        }
    }

    public byte[] readFileToByte(final File selFile) {
        final InputStream st = readFileGetBlob(selFile);
        byte inBuf[];
        try {
            final int inBytes = st.available();
            inBuf = new byte[inBytes];
            st.read(inBuf, 0, inBytes);
        } catch (final java.io.IOException ex) {
            return null;
        }
        return inBuf;
    }

    public InputStream readFileGetBlob(final File file) {
        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (final FileNotFoundException ex) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            return null;
        }
        return is;
    }

    public Object getMappedBean(Class cls) {
        return mappedBbeans.get(cls.getCanonicalName());
    }

    public void mapBean(String cannonicalName, Object cls) {
        this.mappedBbeans.put(cannonicalName, cls);
    }

    public ImageIcon getIconResource(String iconPath) {
        ImageIcon icon = null;
        URL url = getClass().getClassLoader().getResource("images/" + iconPath);
        if (url != null) {
            icon = new ImageIcon(url);
        }
        return icon;
    }

    public static EBISystem getInstance() {
        if (ebiSystem == null) {
            ebiSystem = new EBISystem();
        }
        return ebiSystem;
    }
}
