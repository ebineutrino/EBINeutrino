package org.core.setup;

import org.core.EBINeutrinoSystemInit;

import javax.swing.*;
import java.awt.*;

public class EBISetup extends JFrame {

    private JPanel jContentPane = null;
    private JTabbedPane jTabbedPane = null;
    public EBISetupDB dbSetup = null;
    public boolean DBConfigured = false;
    public EBINeutrinoSystemInit sysINIT = null;

    public EBISetup(final EBINeutrinoSystemInit init) {
        sysINIT = init;
        setResizable(false);
        setName("EBISetup");
        initialize();
        dbSetup = new EBISetupDB(this);
        getJTabbedPane().addTab("EBI Database setup", dbSetup);
        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = getSize();
        setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);
    }

    private void initialize() {
        this.setSize(575, 440);
        this.setContentPane(getJContentPane());
        this.setTitle("EBI Neutrino R1 Database Setup");
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    public JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setName("jTabbedPane");
            jTabbedPane.setTabPlacement(SwingConstants.TOP);
        }
        return jTabbedPane;
    }
}
