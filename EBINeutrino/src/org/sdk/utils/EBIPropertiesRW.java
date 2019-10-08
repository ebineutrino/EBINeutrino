package org.sdk.utils;

import java.io.File;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * configuration properties ebi_neutrino.properties and ebi_neutrino.properties
 *
 */
public class EBIPropertiesRW {

    private static EBIPropertiesRW rwProp = null;
    private Properties properties = null;

    public EBIPropertiesRW() {

        try {
            properties = new Properties();
            URL url = ClassLoader.getSystemResource("config/ebi_neutrino.properties");
            properties.load(url.openStream());

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
            properties.store(new FileOutputStream(new File(ClassLoader.getSystemResource("config/ebi_neutrino.properties").toURI())), null);
        } catch (final IOException e) {
            EBIExceptionDialog.getInstance("Properties file cannot be found!").Show(EBIMessage.ERROR_MESSAGE);
        } catch (URISyntaxException ex) {
            Logger.getLogger(EBIPropertiesRW.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static EBIPropertiesRW getEBIProperties() {
        if (rwProp == null) {
            rwProp = new EBIPropertiesRW();
        }
        return rwProp;
    }

}
