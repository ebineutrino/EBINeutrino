package org.sdk.utils;

import java.io.ByteArrayOutputStream;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            
            System.out.println(System.getProperty("user.dir"));
            
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
            OutputStream out = new ByteArrayOutputStream();
            InputStream stream = ClassLoader.getSystemResourceAsStream("config/ebi_neutrino.properties");

            int av=-1;
            while((av = stream.available()) > 0){
                byte[] bt = new byte[av];
                out.write(stream.read(bt));
            }
            
            properties.store(out, null);
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
