package org.sdk.utils;

import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * configuration properties ebi_neutrino.properties and dialogstore.properties
 *
 */
public class EBIPropertiesLang {

    private static EBIPropertiesLang rwProp = null;
    private Properties properties = null;
    private String selLang = "";
    
    private String propertyPath = System.getProperty("user.dir")
                + File.separator+"resources"
                + File.separator;
    
    public void loadLanguageProperties(final String lang) {
        try {
            selLang = lang;
            properties = new Properties();
            properties.load(new FileReader(propertyPath+lang));
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Critical Error : Language file cannot be found !")
                    .Show(EBIMessage.ERROR_MESSAGE);
        }
    }
    
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
    

    public void saveProperties() {
        try {
            properties.store(new FileWriter(propertyPath+selLang), null);
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Language file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
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
