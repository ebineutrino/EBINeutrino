package ebiNeutrino.core.reflection;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class EBIReflect {

    private static EBIReflect reflect = null;

    public void mapBean(String cls){
        try {
            Class c = Class.forName(cls);
            EBISystem.getInstance().mapBean(cls,c.newInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void reflectAction(String path){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    int idx = path.lastIndexOf(".");
                    String method = path.substring(idx+1);
                    String clsName = path.substring(0,idx);
                    Class c = Class.forName(clsName);
                    c.getMethod(method).invoke(EBISystem.getInstance().getMappedBean(c));

                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                    EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    EBIExceptionDialog.getInstance(e.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                }
            }
        });
    }

    public static  EBIReflect getInstance(){
        if(reflect == null){
            reflect = new EBIReflect();
        }
        return reflect;
    }

}
