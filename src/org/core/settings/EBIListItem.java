package org.core.settings;

import javax.swing.*;

public class EBIListItem {

    private ImageIcon icon = null;
    private final String text;

    public EBIListItem(final ImageIcon iconpath, final String text) {
        icon = iconpath;
        this.text = text;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
}
