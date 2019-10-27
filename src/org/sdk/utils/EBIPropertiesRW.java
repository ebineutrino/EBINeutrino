package org.sdk.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.IOException;
import java.util.Properties;

/**
 * configuration properties ebi_neutrino.properties and ebi_neutrino.properties
 *
 */
public class EBIPropertiesRW {

    private static EBIPropertiesRW rwProp = null;
    private Properties properties = null;
    private String propertyPath = System.getProperty("user.dir")
                + File.separator+"resources"
                + File.separator+"config"
                + File.separator+"ebi_neutrino.properties";
    
    public EBIPropertiesRW() {
        try {
            properties = new Properties();
            properties.load(new FileReader(propertyPath));
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Critical Error :Properties file cannot be found !")
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

    public void saveEBINeutrinoProperties() {
        try {
             
            properties.store(new FileWriter(propertyPath), null);
        } catch (final IOException e) {
            EBIExceptionDialog.getInstance("Properties file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public static EBIPropertiesRW getEBIProperties() {
        if (rwProp == null) {
            rwProp = new EBIPropertiesRW();
        }
        return rwProp;
    }
}
