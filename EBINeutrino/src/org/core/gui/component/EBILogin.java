package org.core.gui.component;

import org.core.EBIMain;
import org.core.guiRenderer.EBIButton;
import org.sdk.EBISystem;
import org.sdk.arbitration.EBIArbCallback;
import org.sdk.arbitration.EBIArbitration;
import org.sdk.gui.component.EBIExtendedPanel;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;


public class EBILogin extends JFrame {

    public JTextField jTextUser = null;
    private JPasswordField jPasswordField = null;
    private EBIButton jButtonLogin = null;
    private EBIButton jButtonCancel = null;
    public EBIMain main = null;
    private EBIExtendedPanel jPanelControl = null;

    /**
     * This is the default constructor
     */
    public EBILogin(final EBIMain eb) {
        main = eb;
        initialize();
        initFocusTravesal();
        setTitle("EBI Neutrino R1 Login");
        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();

        if (!"".equals(properties.getValue("EBI_Neutrino_Last_Logged_User")) ||
                !"null".equals(properties.getValue("EBI_Neutrino_Last_Logged_User"))) {

            jTextUser.setFocusable(false);
            jPasswordField.requestFocusInWindow();
            jPasswordField.requestFocus();
            jPasswordField.grabFocus();
            jTextUser.setFocusable(true);
        } else {
            jTextUser.requestFocusInWindow();
            jTextUser.grabFocus();
            jTextUser.requestFocus();
        }
    }

    private void initialize() {
        getContentPane().setLayout(null);
        getContentPane().add(getJPanelControl(), null);
        this.requestFocusInWindow();
    }

    public void initFocusTravesal() {
        final KeyStroke KEYSTROKE_UP = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        final KeyStroke KEYSTROKE_DOWN = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        final KeyStroke KEYSTROKE_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        final KeyStroke KEYSTROKE_SHIFT_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK);
        //forward focus set
        final Set<KeyStroke> forward = new HashSet<KeyStroke>();
        forward.add(KEYSTROKE_DOWN);
        forward.add(KEYSTROKE_TAB);
        this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forward);
        //backward focus set
        final Set<KeyStroke> backward = new HashSet<KeyStroke>();
        backward.add(KEYSTROKE_UP);
        backward.add(KEYSTROKE_SHIFT_TAB);
        this.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backward);
    }

    /**
     * This method initializes jTextUser	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextUser() {
        if (jTextUser == null) {
            jTextUser = new JTextField();
            jTextUser.setBounds(new Rectangle(117, 79, 280, 25));
            jTextUser.setText(EBISystem.lastLoggedUser);
            jTextUser.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                        jPasswordField.requestFocus();
                    }
                }
            });
        }
        return jTextUser;
    }

    /**
     * This method initializes jPasswordField	
     * 	
     * @return javax.swing.JPasswordField	
     */
    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField();
            jPasswordField.setText("ebineutrino");
            jPasswordField.setBounds(new Rectangle(117, 118, 280, 25));
            jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        checkUser();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                        jTextUser.requestFocus();
                    }
                }
            });
        }
        return jPasswordField;
    }

    /**
     * This method initializes jButtonLogin	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonLogin() {
        if (jButtonLogin == null) {
            jButtonLogin = new EBIButton();
            jButtonLogin.setText(EBISystem.i18n("EBI_LANG_LOGIN"));
            jButtonLogin.setBounds(new java.awt.Rectangle(154, 155, 100, 25));
            jButtonLogin.addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    checkUser();
                }
            });
        }
        return jButtonLogin;
    }

    public void checkUser() {

        final String user = jTextUser.getText().replace('\'', ' ');
        final String password = jPasswordField.getText().replace('\'', ' ');
        
        final boolean ret = EBISystem.getInstance().checkIsValidUser(user, password);

        if (ret != false) {
            setVisible(false);
            main.splash.setVisible(true);
            main.showBusinessModule();

            EBIArbitration.arbitrate().begin("AFTER_LOGIN", new EBIArbCallback() {
                @Override
                public boolean callback(final Thread currentThread) {
                    saveLastLoggedUser();
                    EBIArbitration.arbitrate().waitJobDone("INIT_CRM");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            main.splash.setVisible(false);
                        }
                    });
                    return true;
                }
            });
        } else {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_USER_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    /**
     * This method initializes jButtonAbbrechen	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new EBIButton();
            jButtonCancel.setText(EBISystem.i18n("EBI_LANG_CANCEL"));
            jButtonCancel.setBounds(new java.awt.Rectangle(264, 155, 110, 25));
            jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGE_CLOSE")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                        setVisible(false);
                        System.exit(0);
                    }
                }
            });
        }
        return jButtonCancel;
    }

    /**
     * This method initializes jPanelControl	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelControl() {
        if (jPanelControl == null) {
            final JLabel jLabel1=new JLabel();
            jLabel1.setBounds(new Rectangle(30, 118, 82, 25));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel1.setText(EBISystem.i18n("EBI_LANG_PASSWORD"));
            final JLabel jLabel=new JLabel();
            jLabel.setBounds(new Rectangle(31, 78, 81, 25));
            jLabel.setText(EBISystem.i18n("EBI_LANG_USER"));
            jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

            jPanelControl = new EBIExtendedPanel("EBI Neutrino Login","kuser.png");
            jPanelControl.setLayout(null);
            final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            final Dimension frameSize = getSize();
            jPanelControl.setBounds(new java.awt.Rectangle(0, 0, 524, 225));
            jPanelControl.add(jLabel, null);
            jPanelControl.add(jLabel1, null);
            jPanelControl.add(getJButtonCancel(), null);
            jPanelControl.add(getJButtonLogin(), null);
            jPanelControl.add(getJPasswordField(), null);
            jPanelControl.add(getJTextUser(), null);
            final JLabel jLabel4=new JLabel();
            jLabel4.setBounds(new java.awt.Rectangle(404, 115, 24, 24));
            jLabel4.setIcon(EBISystem.getInstance().getIconResource("password.png"));
            jLabel4.setText("");
            final JLabel jLabel3=new JLabel();
            jLabel3.setBounds(new java.awt.Rectangle(404, 75, 25, 27));
            jLabel3.setIcon(EBISystem.getInstance().getIconResource("contact.png"));
            jLabel3.setText("");
            jPanelControl.add(jLabel3, null);
            jPanelControl.add(jLabel4, null);
        }
        return jPanelControl;
    }

    private void saveLastLoggedUser() {
        final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
        properties.setValue("EBI_Neutrino_Last_Logged_User", this.jTextUser.getText());
        properties.saveEBINeutrinoProperties();
    }

    @Override
	public void setVisible(final boolean visible){
        super.setVisible(visible);
        if(main.isVisible() && visible == true){
           main.setVisible(false);
        }
    }
}

