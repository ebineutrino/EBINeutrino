package org.sdk.utils;

import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.*;
import java.util.Properties;

/**
 * configuration properties dialogstore.properties and dialogstore.properties
 */
public class EBIPropertiesDialogRW {

    private static EBIPropertiesDialogRW rwProp = null;
    private Properties properties = null;
    private String propertyPath = System.getProperty("user.dir")
                + File.separator+"resources"
                + File.separator+"config"
                + File.separator+"dialogstore.properties";

    public EBIPropertiesDialogRW() {
        try {
            properties = new Properties();
            properties.load(new FileReader(propertyPath));
        }catch (final IOException e) {
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

    public void saveProperties() {
        try {
            properties.store(new FileWriter(propertyPath), null);
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Properties "
                    + "file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public static EBIPropertiesDialogRW getProperties() {
        if (rwProp == null) {
            rwProp = new EBIPropertiesDialogRW();
        }
        return rwProp;
    }
}