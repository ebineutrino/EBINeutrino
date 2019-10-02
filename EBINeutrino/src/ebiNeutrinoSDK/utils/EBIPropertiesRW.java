package ebiNeutrinoSDK.utils;

import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

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
            URL url = ClassLoader.getSystemResource("ebi_neutrino.properties");
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
            properties.store(new FileOutputStream("ebi_neutrino.properties"), null);
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
