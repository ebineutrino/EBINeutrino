package org.core.guiRenderer;

import java.util.HashMap;
import javax.swing.JComponent;

public class EBIScriptComponentContainer {
    
    private HashMap<String, JComponent>components = new HashMap();
    public EBIScriptComponentContainer(){}
    
    public HashMap<String, JComponent> getComponents(){
        return components;
    }
}
