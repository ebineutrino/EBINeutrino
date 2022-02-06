package org.core.test;

import org.beryx.textio.*;
import org.beryx.textio.swing.SwingTextTerminal;

public class TextIOTests {

     public static void main(final String arg[]){
         
         
        SwingTextTerminal textTerminal = new SwingTextTerminal();
        textTerminal.init();
        textTerminal.getFrame().setVisible(false);
        textTerminal.getFrame().setAlwaysOnTop(true);
        textTerminal.getFrame().setTitle("EBI Neutrino Console");
        TextIO textIO = new TextIO(textTerminal);
        
         
        String cmd = "";
        while(!cmd.equals("exit")){
            textIO.getTextTerminal().print(">");
            cmd = textIO.getTextTerminal().read(false);
        }
        
        textIO.getTextTerminal().println(">bye");
    }
    
}
