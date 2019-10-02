package ebiNeutrino.core;

import ebiNeutrino.core.gui.dialogs.EBISplashScreen;
import ebiNeutrino.core.setup.EBILanguageSetup;
import ebiNeutrino.core.setup.EBISetup;
import ebiNeutrinoSDK.EBIHibernateSessionPooling;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.utils.EBIPropertiesRW;
import ebiNeutrinoSDK.utils.Encrypter;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Table;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 * Initialize EBI Neutrino System Check Database connectivity
 *
 */
public class EBINeutrinoSystemInit extends EBISystem {

    public static boolean isConfigured = false;
    private static Configuration cfg = new Configuration();
    private EBIPropertiesRW properties = null;
    private boolean toReturn;

    public EBINeutrinoSystemInit() {
        properties = EBIPropertiesRW.getEBIProperties();
        if (Init(properties.getValue("EBI_Neutrino_Database_Name")) == false) {
            isConfigured = false;
        }
        checkConnection();
    }

    public EBINeutrinoSystemInit(final EBISplashScreen spl) {

        properties = EBIPropertiesRW.getEBIProperties();

        final boolean ret = Init(properties.getValue("EBI_Neutrino_Database_Name"));

        if (ret == false) {
            isConfigured = false;
            if (spl != null) {
                spl.setVisible(false);
            }
            if (checkConnection() == true) {
                if (spl != null) {
                    spl.setVisible(true);
                }
                if (Init(properties.getValue("EBI_Neutrino_Database_Name")) == false) {
                    isConfigured = false;
                    if (spl != null) {
                        spl.setVisible(false);
                    }
                }
            }
        }
    }

    /**
     * Check if we have a connection to the database then a setup dialog will
     * appear
     */
    public boolean checkConnection() {
        if (isConfigured == false) {

            final JFrame frame = new JFrame("EBI Neutrino R1 Setup");
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            if (EBIExceptionDialog
                    .getInstance("No database connection possible\nWould you like to configure a connection?\n")
                    .Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {

                final EBILanguageSetup setLanguage = new EBILanguageSetup();
                setLanguage.setVisible(true);

                final EBISetup application = new EBISetup(this);
                application.setResizable(false);
                application.setVisible(true);

                if (!application.DBConfigured) {
                    System.exit(1);
                }

                frame.dispose();

            } else {
                System.exit(1);
            }
        }
        return true;
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
                    ? "EBINeutrinoLanguage_English.properties"
                    : properties.getValue("EBI_Neutrino_Language_File"), false);

            // Set global database system
            DATABASE_SYSTEM = dbType.toLowerCase();

            if (!"".equals(Driver)) {
                toReturn = EBISystem.getDatabase().connect(Driver, EBISystem.host.trim(), data, password, user, dbType.toLowerCase(), oracSID, useUpperCase);
            } else {
                toReturn = false;
            }

            isConfigured = toReturn;
            if (toReturn == false) {
                return toReturn;
            }

            EBISystem.getDatabase().setAutoCommit(true);
            new Thread(() -> EBISystem.getInstance().fillComboWithUser()).start();

            //configure hibernate
            if ("mysql".equals(dbType.toLowerCase())) {
                cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
            } else if ("oracle".equals(dbType.toLowerCase())) {
                cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
            } else if ("hsqldb".equals(dbType.toLowerCase())) {
                cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            }

            cfg.setProperty("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
            cfg.setProperty("hibernate.connection.url", "jdbc:mysql://" + EBISystem.host.trim() + ":3306/" + data + "?serverTimezone=UTC");
            cfg.setProperty("hibernate.connection.username", user);
            cfg.setProperty("hibernate.connection.password", password);

            cfg.addPackage("ebiNeutrinoSDK.model.hibernate");
            
            Reflections reflections = new Reflections("ebiNeutrinoSDK.model.hibernate");
            Set<Class<?>> entityTypes = reflections.getTypesAnnotatedWith(Table.class);
            for (Class<?> type : entityTypes){
                cfg.addAnnotatedClass(type);
            }
            EBISystem.getInstance().setIEBIHibernate(new EBIHibernateSessionPooling(cfg));

        } catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
            toReturn = false;
        }
        return toReturn;
    }
}
