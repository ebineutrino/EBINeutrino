package org.core.gui.dialogs;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialogExt;

import javax.swing.*;
import java.awt.*;

public class EBIEMailSendReciveStatus extends EBIDialogExt {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonCancel = null;

    private JProgressBar jProgressBar = null;

    private final String typeAction = "";

    public boolean isBreak = false;

    private JLabel jLabel = null;

    /**
     * @param owner
     */
    public EBIEMailSendReciveStatus(String type) {
        super(null);
        type = typeAction;
        setName("EBIEMailSendReciveStatus");
        storeLocation(true);
        initialize();
        this.setResizable(false);
        this.setTitle(EBISystem.i18n("EBI_LANG_C_EMAIL_SEND_RECIEVE"));

    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(521, 157);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(10, 11, 329, 30));
            jLabel.setText(EBISystem.i18n("EBI_LANG_C_EMAIL_SEND_RECIEVE"));
            jLabel.setIcon(EBISystem.getInstance().getIconResource("internet.png"));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonCancel(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(getJProgressBar(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButtonCancel
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setBounds(new Rectangle(386, 89, 121, 27));
            jButtonCancel.setText(EBISystem.i18n("EBI_LANG_CANCEL"));
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    isBreak = true;
                    setVisible(false);
                }
            });
        }
        return jButtonCancel;
    }

    /**
     * This method initializes jProgressBar
     *
     * @return javax.swing.JProgressBar
     */
    private JProgressBar getJProgressBar() {
        if (jProgressBar == null) {
            jProgressBar = new JProgressBar();
            jProgressBar.setBounds(new Rectangle(11, 46, 488, 22));
            jProgressBar.setStringPainted(true);
            jProgressBar.setString(this.typeAction);
            jProgressBar.setIndeterminate(true);
        }
        return jProgressBar;
    }

    public void setInfoText(final String info) {
        jLabel.setText(info);
    }

}
