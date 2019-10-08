package org.sdk.utils;

import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * configuration properties ebi_neutrino.properties and dialogstore.properties
 *
 */
public class EBIPropertiesLang {

    private static EBIPropertiesLang rwProp = null;
    private Properties properties = null;
    private String selLang = "";

    public String getValue(final String key) {
        String val = properties.getProperty(key);
        if (val == null) {
            val = "";
        }
        return val;
    }

    public void setValue(final String key, final String value) {
        properties.setProperty(key, value);
    }

    public void loadLanguageProperties(final String lang) {
        try {
            selLang = lang;
            System.out.println(selLang);
            properties = new Properties();
            URL url = ClassLoader.getSystemResource(lang);
            properties.load(url.openStream());
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Critical Error : Language file cannot be found !")
                    .Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Critical Error : Language file cannot be found !")
                    .Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public void saveProperties() {
        try {
            properties.store(new FileOutputStream(new File(ClassLoader.getSystemResource(selLang).toURI())), null);
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Language file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
        } catch (URISyntaxException ex) {
            Logger.getLogger(EBIPropertiesLang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Properties getProperty() {
        return properties;
    }

    public static EBIPropertiesLang getProperties() {
        if (rwProp == null) {
            rwProp = new EBIPropertiesLang();
        }
        return rwProp;
    }
}
