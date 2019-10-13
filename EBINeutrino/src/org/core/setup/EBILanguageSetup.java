package org.core.setup;

import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIDialogExt;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.utils.EBIPropertiesRW;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class EBILanguageSetup extends EBIDialogExt {

    private JPanel jContentPane = null;
    private JComboBox jComboBoxLanguage = null;
    private JButton jButton = null;

    public EBILanguageSetup() {
        super(null);
        storeLocation(true);
        storeSize(true);
        initialize();
        parseLanguageFileFromDir();
    }

    private void initialize() {
        this.setSize(460, 220);
        this.setContentPane(getJContentPane());
        setTitle("Language Setup");
        setResizable(false);
        setModal(true);
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = getSize();
        setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            final JLabel jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(10, 65, 80, 25));
            jLabel1.setText("Language:");

            final JLabel jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(10, 5, 370, 40));
            jLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabel.setText("EBI Neutrino R1 Language Setup");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(jLabel, null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(getJComboBoxLanguage(), null);
            jContentPane.add(getJButton(), null);
        }
        return jContentPane;
    }

    private void parseLanguageFileFromDir() {
        String[] value;

        try {

            List<String> files = IOUtils.readLines(getClass().getClassLoader().getResourceAsStream("language"), Charsets.UTF_8);
            String builder = EBISystem.i18n("EBI_LANG_PLEASE_SELECT") + ",";

            Iterator<String> iter = files.iterator();
            int size = files.size();
            int i = 0;
            while (iter.hasNext()) {
                String lName;
                String name = iter.next();

                if ((lName = name.substring(name.lastIndexOf("_") + 1)) != null) {
                    if (!"".equals(lName) && lName != null) {
                        if ((lName = lName.substring(0, lName.lastIndexOf("."))) != null) {
                            if (!"".equals(lName)) {
                                builder += lName;
                                if (i < size) {
                                    builder += ",";
                                }
                            }
                        }
                    }
                }
            }

            value = builder.trim().split(",");
            this.jComboBoxLanguage.setModel(new javax.swing.DefaultComboBoxModel(value));
        } catch (IOException ex) {
            Logger.getLogger(EBILanguageSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JComboBox getJComboBoxLanguage() {
        if (jComboBoxLanguage == null) {
            jComboBoxLanguage = new JComboBox();
            jComboBoxLanguage.setBounds(new Rectangle(95, 65, 335, 30));
            jComboBoxLanguage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!jComboBoxLanguage.getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        jButton.setEnabled(true);
                    } else {
                        jButton.setEnabled(false);
                    }
                }
            });
        }
        return jComboBoxLanguage;
    }

    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setBounds(new Rectangle(325, 118, 105, 30));
            jButton.setText("Apply");
            jButton.setEnabled(false);
            jButton.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(final java.awt.event.ActionEvent e) {

                    if (!validateInput()) {
                        return;
                    }
                    final EBIPropertiesRW properties = EBIPropertiesRW.getEBIProperties();
                    EBISystem.selectedLanguage = jComboBoxLanguage.getSelectedItem().toString();
                    properties.setValue("EBI_Neutrino_Language_File", "language/EBINeutrinoLanguage_"
                            + jComboBoxLanguage.getSelectedItem().toString() + ".properties");
                    properties.saveEBINeutrinoProperties();
                    setVisible(false);
                }
            });
        }
        return jButton;
    }

    private boolean validateInput() {

        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(jComboBoxLanguage.getSelectedItem().toString())) {
            EBIExceptionDialog.getInstance(EBILanguageSetup.this, "Please select a language!")
                    .Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}
