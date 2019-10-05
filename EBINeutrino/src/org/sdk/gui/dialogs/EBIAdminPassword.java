package org.sdk.gui.dialogs;

import org.sdk.EBISystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Root password dialog
 */
public class EBIAdminPassword extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonCancel = null;

    private JButton jButtonOk = null;

    private JPasswordField jPasswordField = null;

    public boolean isOK = false;

    public EBIAdminPassword() {
        super(EBISystem.getInstance().getMainFrame());
        setTitle(EBISystem.i18n("EBI_LANG_ROOT_PASSWORD"));
        this.setResizable(false);
        this.setModal(true);

        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(515, 207);
        this.setLocation((EBISystem.getInstance().getMainFrame().getWidth() / 2) - (getWidth() / 2), (EBISystem.getInstance().getMainFrame().getHeight() / 2) - (getHeight() / 2));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            final JLabel jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(16, 74, 71, 22));
            jLabel1.setText(EBISystem.i18n("EBI_LANG_PASSWORD") + ":");
            final JLabel jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(16, 13, 366, 35));
            jLabel.setText(EBISystem.i18n("EBI_LANG_ROOT_PASSWORD"));
            jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel.setIcon(EBISystem.getInstance().getIconResource("password.png"));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonCancel(), null);
            jContentPane.add(getJButtonOk(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(getJPasswordField(), null);
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
            jButtonCancel.setBounds(new Rectangle(366, 133, 121, 29));
            jButtonCancel.setText(EBISystem.i18n("EBI_LANG_CANCEL"));
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    isOK = false;
                    setVisible(false);
                    dispose();
                }
            });
        }
        return jButtonCancel;
    }

    /**
     * This method initializes jButtonOk
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setBounds(new Rectangle(249, 133, 111, 29));
            jButtonOk.setText(EBISystem.i18n("EBI_LANG_OK"));
            jButtonOk.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {
                    checkRoot();
                }
            });
        }
        return jButtonOk;
    }

    private void checkRoot() {
        ResultSet xset = null;
        try {
            final String passwd = EBISystem.getInstance().encryptPassword(jPasswordField.getText());
            final PreparedStatement ps1 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT * FROM EBIUSER WHERE EBIUSER=? and PASSWD=?");
            ps1.setString(1, "root");
            ps1.setString(2, passwd);
            xset = EBISystem.getInstance().iDB().executePreparedQuery(ps1);
            xset.next();
            if (xset.getRow() > 0) {
                isOK = true;
            } else {
                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PASSWORD_NOT_CORRECT")).Show(EBIMessage.ERROR_MESSAGE);
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
        } finally {
            if (xset != null) {
                try {
                    xset.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        setVisible(false);
    }

    /**
     * This method initializes jPasswordField
     *
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField();
            jPasswordField.setBounds(new Rectangle(90, 75, 408, 21));
            jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        checkRoot();
                    }
                }
            });
        }
        return jPasswordField;
    }

}
