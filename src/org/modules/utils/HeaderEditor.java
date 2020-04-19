package org.modules.utils;

import javax.swing.*;
import java.awt.*;

public class HeaderEditor {

    //private HeaderSelector selector;  
    public String[] items = null;

    public HeaderEditor(final HeaderSelector hs) {
    }

    public Object showEditor(final Component parent, final int col, final String currentValue) {
        Object retVal = "";
        if (items != null) {
            // items[0] = currentValue;  
            final String message = "Value for Column Nr: " + (col + 1) + ":";
            retVal = JOptionPane.showInputDialog(parent,
                    message,
                    "Table Value",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    items,
                    currentValue);
            if (retVal == null) {
                retVal = currentValue;
            }
        }
        return retVal;
    }

}
