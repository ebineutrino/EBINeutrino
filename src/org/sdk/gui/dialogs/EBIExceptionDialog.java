package org.sdk.gui.dialogs;

import org.sdk.EBISystem;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class EBIExceptionDialog extends EBIDialogExt {

    private JPanel jContentPane = null;
    public static final JTextArea jTextException = new JTextArea();
    private JScrollPane jScrollPane = null;
    public String Msg = "";
    private static final Logger logger = Logger.getLogger(EBIExceptionDialog.class.getName());
    private static EBIExceptionDialog messageDialog = null;
    private Component parentComponent = null;

    public EBIExceptionDialog() {
        super(null);
        setAlwaysOnTop(true);
        jTextException.setBounds(new java.awt.Rectangle(126, 0, 421, 246));
        jTextException.setEditable(false);
        jTextException.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
        jTextException.setText(Msg);
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = getSize();
        this.setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
        storeLocation(true);
        storeSize(true);
        initialize();
        if (logger != null) {
            logger.error("" + Msg);
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(557, 276);
        this.setTitle("EBI system Exception");
        this.setContentPane(getJContentPane());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBackground(java.awt.Color.white);
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane(jTextException);
            jScrollPane.setBounds(new java.awt.Rectangle(129, 0, 421, 249));
        }
        return jScrollPane;
    }

    public boolean Show(final EBIMessage msg) {
        boolean toRet = false;
        if (msg == EBIMessage.NEUTRINO_DEBUG_MESSAGE) {
            setModal(true);
            jTextException.setText(Msg);
            final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            final Dimension frameSize = getSize();
            setLocation((d.width - frameSize.width) / 2, (d.height - frameSize.height) / 2);
            setVisible(true);
        } else {

            if (msg == EBIMessage.INFO_MESSAGE) {
                JOptionPane.showMessageDialog(messageDialog.parentComponent, Msg, EBISystem.i18n("EBI_LANG_INFO") == null ? "Info" : EBISystem.i18n("EBI_LANG_INFO"), JOptionPane.INFORMATION_MESSAGE);
                toRet = false;
            } else if (msg == EBIMessage.INFO_MESSAGE_YESNO) {
                if (JOptionPane.showConfirmDialog(messageDialog.parentComponent, Msg, EBISystem.i18n("EBI_LANG_INFO") == null ? "Info" : EBISystem.i18n("EBI_LANG_INFO"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                    toRet = true;
                } else {
                    toRet = false;
                }
            } else if (msg == EBIMessage.ERROR_MESSAGE) {
                JOptionPane.showMessageDialog(messageDialog.parentComponent, Msg, EBISystem.i18n("EBI_LANG_ERROR") == null ? "Error" : EBISystem.i18n("EBI_LANG_ERROR"), JOptionPane.ERROR_MESSAGE);
                toRet = false;
            } else if (msg == EBIMessage.WARNING_MESSAGE_YESNO) {
                if (JOptionPane.showConfirmDialog(messageDialog.parentComponent, Msg, EBISystem.i18n("EBI_LANG_WARRNING") == null ? "Warning" : EBISystem.i18n("EBI_LANG_WARRNING"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    toRet = true;
                } else {
                    toRet = false;
                }
            }
        }
        dispose();
        return toRet;
    }

    public void info(final String msg) {
        Msg = msg;
        Show(EBIMessage.INFO_MESSAGE);
    }

    public void error(final String msg) {
        Msg = msg;
        Show(EBIMessage.ERROR_MESSAGE);
    }

    public boolean warning(final String msg) {
        Msg = msg;
        return Show(EBIMessage.WARNING_MESSAGE_YESNO);
    }

    public boolean infoYesNo(final String msg) {
        Msg = msg;
        return Show(EBIMessage.INFO_MESSAGE_YESNO);
    }

    public boolean debug(final String msg) {
        Msg = msg;
        return Show(EBIMessage.NEUTRINO_DEBUG_MESSAGE);
    }

    public static EBIExceptionDialog getInstance(final Component parent, final String Message) {
        getInstance().parentComponent = parent;
        getInstance().Msg = Message;
        logger.error(Message);
        return messageDialog;
    }

    public static EBIExceptionDialog getInstance(final String Message) {
        getInstance().Msg = Message;
        logger.error(Message);
        return messageDialog;
    }

    
    public static EBIExceptionDialog getInstance(final String Message, Throwable e) {
        getInstance().Msg = e.getMessage()+" Cause: "+(e.getCause() != null ? e.getCause().getMessage() : "");
        logger.error(Message);
        return messageDialog;
    }
    
    
    
    public static EBIExceptionDialog getInstance() {
        if (messageDialog == null) {
            messageDialog = new EBIExceptionDialog();
        }
        return messageDialog;
    }
}
