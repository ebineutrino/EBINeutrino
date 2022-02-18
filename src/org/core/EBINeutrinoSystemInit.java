package org.core;

import org.core.gui.dialogs.EBISplashScreen;
import org.core.setup.EBILanguageSetup;
import org.core.setup.EBISetup;
import org.sdk.EBIHibernateSessionPooling;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;
import org.sdk.utils.Encrypter;
import org.hibernate.cfg.Configuration;

import java.util.Set;
import javax.persistence.Table;
import org.reflections.Reflections;

/**
 * Initialize EBI Neutrino System Check Database connectivity
 *
 */
public class EBINeutrinoSystemInit extends EBISystem {

    public static boolean isConfigured = false;
    private static Configuration cfg = new Configuration();
    private EBIPropertiesRW properties = null;
    private boolean toReturn;
    private EBISplashScreen splash = null;

    public EBINeutrinoSystemInit() {
        properties = EBIPropertiesRW.getEBIProperties();
        checkDB();
    }

    public EBINeutrinoSystemInit(final EBISplashScreen spl) {
        properties = EBIPropertiesRW.getEBIProperties();
        splash = spl;
        checkDB();
    }
    
    public void checkDB(){
        if ((isConfigured = Init(properties.getValue("EBI_Neutrino_Database_Name"))) == false) {
            splash.setVisible(false);
            if (EBIExceptionDialog
                    .getInstance("No database connection possible\nWould you like to configure a connection?\n")
                    .Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {

                final EBILanguageSetup setLanguage = new EBILanguageSetup();
                setLanguage.setVisible(true);

                final EBISetup application = new EBISetup(this);
                application.setResizable(false);
                application.setVisible(true);
            }else{
                System.exit(0);
            }
        }else{
            if(!splash.isVisible()){
                splash.setVisible(true);
                try{
                    EBISystem.getInstance().getMainFrame().initializeTheSystem();
                }catch(Exception ex){ ex.printStackTrace();}
            }
        }
    }
    

    /**
     * Initialize EBI Neutrino Systems
     *
     * @param data
     * @return void
     */
    public boolean Init(final String data) {

        try {
            final Encrypter encrypter = new Encrypter("EBINeutrino");

            EBISystem.USE_ASB2C = Boolean.parseBoolean(properties.getValue("EBI_Neutrino_UserAsB2C"));
            EBISystem.host = properties.getValue("EBI_Neutrino_Host");

            final String password = encrypter.decrypt(properties.getValue("EBI_Neutrino_Password"));
            final String user = encrypter.decrypt(properties.getValue("EBI_Neutrino_User"));

            EBISystem.updateServer = properties.getValue("EBI_Neutrino_Update_Server");
            EBISystem.DateFormat = "".equals(properties.getValue("EBI_Neutrino_Date_Format")) ? "dd.MM.yyyy"
                    : properties.getValue("EBI_Neutrino_Date_Format");

            final String Driver = properties.getValue("EBI_Neutrino_Database_Driver");
            final String dbType = properties.getValue("EBI_Neutrino_Database");

            final String oracSID = properties.getValue("EBI_Neutrino_Oracle_SID");

            final String useUpperCase = properties.getValue("EBI_Neutrino_Database_UpperCase");
            EBISystem.lastLoggedUser = properties.getValue("EBI_Neutrino_Last_Logged_User");

            getLanguageInstance("".equals(properties.getValue("EBI_Neutrino_Language_File"))
                    ? "language/EBINeutrinoLanguage_English.properties"
                    : properties.getValue("EBI_Neutrino_Language_File"), false);

            // Set global database system
            DATABASE_SYSTEM = dbType.toLowerCase();

            if (!"".equals(Driver)) {
                toReturn = EBISystem.db().connect(Driver, EBISystem.host.trim(), data, password, user, dbType.toLowerCase(), oracSID, useUpperCase);
            } else {
                toReturn = false;
            }

            isConfigured = toReturn;
            if (toReturn == false) {
                return toReturn;
            }
           
            EBISystem.db().setAutoCommit(true);
            toReturn = EBISystem.getInstance().fillComboWithUser(data);
            if (toReturn) {
                //configure hibernate
                if ("mysql".equals(dbType.toLowerCase())) {
                    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
                    cfg.setProperty("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
                    cfg.setProperty("hibernate.connection.url", "jdbc:mysql://" + EBISystem.host.trim() + ":3306/" + data + "?serverTimezone=UTC");
                } else if ("oracle".equals(dbType.toLowerCase())) {
                    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
                }else if ("h2".equals(dbType.toLowerCase())) {
                    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                    cfg.setProperty("javax.persistence.jdbc.driver", "org.h2.Driver");
                    cfg.setProperty("hibernate.connection.url", "jdbc:h2:./"+data.trim()+";DATABASE_TO_UPPER=TRUE;DB_CLOSE_DELAY=-1;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
                }

                cfg.setProperty("hibernate.connection.username", user);
                cfg.setProperty("hibernate.connection.password", password);
                cfg.addPackage("org.sdk.model.hibernate");

                Reflections reflections = new Reflections("org.sdk.model.hibernate");
                Set<Class<?>> entityTypes = reflections.getTypesAnnotatedWith(Table.class);
                for (Class<?> type : entityTypes) {
                    cfg.addAnnotatedClass(type);
                }
                EBISystem.getInstance().setIEBIHibernate(new EBIHibernateSessionPooling(cfg));
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
            toReturn = false;
        }
        return toReturn;
    }
}