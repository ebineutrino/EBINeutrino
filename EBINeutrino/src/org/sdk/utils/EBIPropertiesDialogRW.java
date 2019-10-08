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
 * configuration properties dialogstore.properties and dialogstore.properties
 */
public class EBIPropertiesDialogRW {

    private static EBIPropertiesDialogRW rwProp = null;
    private Properties properties = null;

    public EBIPropertiesDialogRW() {
        try {
            properties = new Properties();
            URL url = ClassLoader.getSystemResource("config/dialogstore.properties");
            properties.load(url.openStream());
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Critical Error :Properties file cannot be found !")
                    .Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception e) {
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
            properties.store(new FileOutputStream(new File(ClassLoader.getSystemResource("config/dialogstore.properties").toURI())), null);
        } catch (final IOException e) {
            e.printStackTrace();
            EBIExceptionDialog.getInstance("Properties file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
        } catch (URISyntaxException ex) {
            Logger.getLogger(EBIPropertiesDialogRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static EBIPropertiesDialogRW getProperties() {
        if (rwProp == null) {
            rwProp = new EBIPropertiesDialogRW();
        }
        return rwProp;
    }

}
