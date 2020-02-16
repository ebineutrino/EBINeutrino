package org.core.run.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

public class EBIProcessStarter {

    private static EBIProcessStarter starter = null;
    private String scriptPath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator + "script"
            + File.separator;
    
    private String actualPath = System.getProperty("user.dir") + File.separator;

    public boolean startScript(String fileName) {
        try {

            ProcessBuilder pb = new ProcessBuilder(scriptPath + fileName);
            Process p = pb.start();

            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                String buff = "";
                while ((line = input.readLine()) != null) {
                    buff += line;
                }
            }

        } catch (Exception err) {
            err.printStackTrace();
        }

        return true;
    }

    public void startEBINeutrino() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java -jar "+actualPath+"EBINeutrino.jar");
            Process p = pb.start();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                String buff = "";
                while ((line = input.readLine()) != null) {
                    buff += line;
                }
                EBIExceptionDialog.getInstance(buff).Show(EBIMessage.INFO_MESSAGE);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static EBIProcessStarter getInstance() {
        if (starter == null) {
            starter = new EBIProcessStarter();
        }
        return starter;
    }
}
