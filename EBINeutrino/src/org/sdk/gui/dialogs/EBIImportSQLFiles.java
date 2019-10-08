package org.sdk.gui.dialogs;

import org.sdk.EBISystem;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.utils.EBISQLFilter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EBIImportSQLFiles extends EBIDialogExt {

    private final JProgressBar progress = new JProgressBar();
    private final JLabel title = new JLabel();
    private final JTextField txtFile = new JTextField();
    private final JButton bntLoadFile = new JButton();
    private final JButton bntStartImport = new JButton();
    public boolean isFinish = false;
    private final EBIVisualPanelTemplate pane = new EBIVisualPanelTemplate(false);

    public EBIImportSQLFiles() {
        super(EBISystem.getInstance().getMainFrame());

        pane.setEnableChangeComponent(false);
        pane.setModuleTitle(EBISystem.i18n("EBI_LANG_IMPORT_TILE"));
        pane.setModuleIcon(EBISystem.getInstance().getIconResource("editcopy.png"));
        this.setContentPane(pane);
        this.setModal(false);
        this.setLayout(null);
        this.setResizable(false);
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(310, 170);
        storeLocation(true);
        storeSize(true);

        title.setText(EBISystem.i18n("EBI_LANG_IMPORT_TILE"));
        title.setLocation(10, 10);
        title.setSize(200, 20);
        pane.add(title, null);

        txtFile.setLocation(10, 40);
        txtFile.setSize(200, 20);
        pane.add(txtFile, null);

        bntLoadFile.setText("...");
        bntLoadFile.setLocation(220, 40);
        bntLoadFile.setSize(60, 25);
        bntLoadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                final EBISQLFilter sqlFilter = new EBISQLFilter();
                //EBISystem.getInstance().get.addChoosableFileFilter(sqlFilter);

                final File fs = EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);

                if (fs != null) {
                    txtFile.setText(fs.getAbsolutePath());
                }
            }
        });
        pane.add(bntLoadFile, null);

        progress.setSize(280, 25);
        progress.setLocation(10, 70);
        progress.setBorderPainted(true);
        progress.setStringPainted(true);
        pane.add(progress, null);

        bntStartImport.setText(EBISystem.i18n("EBI_LANG_IMPORT"));
        bntStartImport.setLocation(190, 105);
        bntStartImport.setSize(100, 25);
        bntStartImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                startSQLImport(new String[]{txtFile.getText()});
            }
        });
        pane.add(bntStartImport, null);

    }

    public void startSQLImport(final String[] flx) {
        isFinish = false;
        if (isVisible()) {
            progress.setIndeterminate(true);
            progress.setString(EBISystem.i18n("EBI_LANG_IMPORT_SQL"));
        }

        final Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int x = 0; x < flx.length; x++) {
                        try {
                            Thread.sleep(200);
                            String s;
                            //StringBuffer sb = new StringBuffer();

                            final FileReader fr = new FileReader(new File(ClassLoader.getSystemResource(flx[x]).getFile()));
                            final BufferedReader br = new BufferedReader(fr);
                            String tmp = "";

                            while ((s = br.readLine()) != null) {
                                
                                if (s.endsWith(";")) {
                                    if (!EBISystem.getInstance().iDB().exec(tmp + s)) {
                                        return;
                                    }
                                    tmp = "";
                                } else {
                                    tmp += s;
                                }
                            }
                            br.close();

                            if (isVisible()) {
                                EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_IMPORT_WAS_SUCCESSFULLY")).Show(EBIMessage.INFO_MESSAGE);
                                setVisible(false);
                            }

                        } catch (final FileNotFoundException ex) {
                            ex.printStackTrace();
                            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_FILE_NOT_FOUND") + ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                        } catch (final IOException ex) {
                            ex.printStackTrace();
                            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_FILE_NOT_FOUND") + ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                        } catch (final InterruptedException e) {
                            e.printStackTrace();
                        }finally {
                            if (isVisible()) {
                                progress.setIndeterminate(false);
                            }
                            isFinish = true;
                        }
                    }
                } catch (final SQLException ex) {
                    ex.printStackTrace();
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_LOADING_FILE") + "\n\n" + ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                }
            }
        };

        final Thread start = new Thread(run, "Import SQL");
        start.start();

    }

}
