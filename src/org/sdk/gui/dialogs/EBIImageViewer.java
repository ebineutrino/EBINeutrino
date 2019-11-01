package org.sdk.gui.dialogs;

import org.core.EBIMain;
import javax.swing.*;
import java.awt.*;

public class EBIImageViewer extends EBIDialogExt {

    private JPanel jContentPane = null;
    private JScrollPane jScrollPane = null;
    private JLabel imageConstainer = null;
    private ImageIcon image = null;

    public EBIImageViewer(final EBIMain main, final ImageIcon image) {
        super(main);
        this.setResizable(true);
        storeLocation(true);
        storeSize(true);
        setModal(true);
        initialize();
        this.image = image;
    }

    private void initialize() {
        this.setSize(634, 462);
        this.setContentPane(getJContentPane());
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setBackground(Color.black);
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBackground(Color.black);
            jScrollPane.setViewportView(getImageContainer());
        }
        return jScrollPane;
    }

    private JLabel getImageContainer() {
        if (this.imageConstainer == null) {
            imageConstainer = new JLabel();
            imageConstainer.setHorizontalAlignment(SwingConstants.CENTER);
            imageConstainer.setBackground(new Color(20, 17, 17));
        }
        return this.imageConstainer;
    }
    
    @Override
    public void setVisible(boolean visible){
        imageConstainer.setIcon(image);
        super.setVisible(visible);
    }
    
}
