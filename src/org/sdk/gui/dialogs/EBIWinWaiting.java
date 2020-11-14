package org.sdk.gui.dialogs;

import org.core.EBIMain;

import javax.swing.*;

public class EBIWinWaiting extends EBIDialogExt {

    private JPanel jPanel = null;
    private JProgressBar jProgressBar = null;
    private String textBackup = "";

    private static EBIWinWaiting instance = null;

    /**
     * This is the default constructor
     */
    public EBIWinWaiting(final String name) {
        super(null);
        initialize();
        setAlwaysOnTop(true);
        setName("EBIWinWaiting");
        setTitle("Loading...");
        if (name != null) {
            getJProgressBar().setString(name);
            textBackup = name;
        }
    }

    public EBIWinWaiting(final String name, final EBIMain owner) {
        super(owner);
        initialize();
        setAlwaysOnTop(true);
        setName("EBIWinWaiting");
        setTitle("Loading...");
        getJProgressBar().setString(name);
        textBackup = name;
    }

    private void initialize() {
        this.setSize(560, 130);
        this.setContentPane(getJPanel());
    }

    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(null);
            jPanel.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.black, 1));
            jPanel.add(getJProgressBar(), null);
        }
        return jPanel;
    }

    public void setString(final String text) {
        getJProgressBar().setString(text);
    }

    private JProgressBar getJProgressBar() {
        if (jProgressBar == null) {
            jProgressBar = new JProgressBar();
            jProgressBar.setBounds(new java.awt.Rectangle(17, 25, 518, 21));
            jProgressBar.setIndeterminate(true);
            jProgressBar.setStringPainted(true);
        }
        return jProgressBar;
    }

    @Override
    public void setVisible(final boolean isVisible) {
        super.setVisible(isVisible);
        if (!isVisible) {
            //restore old
            getJProgressBar().setString(textBackup);
        }
    }

    public static EBIWinWaiting getInstance(final String name) {
        if (instance == null) {
            instance = new EBIWinWaiting(name);
        } else {
            instance.getJProgressBar().setString(name);
        }
        return instance;
    }

    public static EBIWinWaiting getInstance() {
        if (instance == null) {
            instance = new EBIWinWaiting(null);
        }
        return instance;
    }

}
