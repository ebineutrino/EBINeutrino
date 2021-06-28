package org.core.run.update;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.core.run.process.EBIProcessStarter;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;

public class EBISocketDownloader {

    private InputStream is = null;
    private Document xmlOnline = null;
    private Document xmlLocal = null;
    private String localVer = "";
    private String onlineVer = "";
    private List<Object> arrList = null;
    private String[] strArr = null;
    private boolean fileLocalExist = true;
    private boolean fileOnlineExist = true;
    private double downloadSize = 0.0;
    private double count = 0.0;
    private double mbcnt = 0.0;
    private String sysPath = "";
    private JProgressBar progress = null;
    private JEditorPane body = null;
    private JLabel title = null;
    private JButton no = null;
    private JButton yes = null;
    private boolean downloadFinish = false;
    private static EBISocketDownloader uploader = null;

    private String updateFilePath = System.getProperty("user.dir")
            + File.separator + "resources"
            + File.separator;

    public EBISocketDownloader() {
        arrList = new ArrayList<Object>();
    }

    public void canUpdate() {

        sysPath = EBISystem.updateServer;
        setConnection();

        if (readConfig()) { // ONLINE UPDATE FUNCTIONALITY

            final JWindow ext = new JWindow();

            ext.setAlwaysOnTop(true);
            ext.setSize(495, 265);
            ext.setLayout(null);

            final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            final Dimension frameSize = ext.getSize();
            ext.setLocation((d.width - frameSize.width) / 2, ((d.height - 150) - frameSize.height) / 2);

            title = new JLabel("<html><body><b>" + EBISystem.i18n(
                    "EBI_LANG_UPDATE_FOR_SYSTEM_AVAILABLE") + "</b></body></html>");

            title.setBounds(170, 10, 260, 30);
            ext.add(title, null);

            body = new JEditorPane();
            body.setContentType("text/html");
            body.setText("<html><body style='color:#cccccc;font-family:Tahoma;font-size:9px'>"
                    + "" + EBISystem.i18n("EBI_LANG_LOCAL_VERSION") + ": " + localVer
                    + "<br>"
                    + "" + EBISystem.i18n("EBI_LANG_ONLINE_VERSION") + ": " + onlineVer
                    + "<br><br>"
                    + "" + EBISystem.i18n("EBI_LANG_WOULD_YOU_UPDATE_YOUR_SYSTEM")
                    + "</body></html>");
            body.setEditable(false);
            body.setBorder(null);
            body.setBounds(170, 50, 260, 100);
            ext.add(body, null);

            progress = new JProgressBar();
            progress.setStringPainted(true);
            progress.setString("");

            progress.setVisible(false);
            progress.setBounds(170, 155, 260, 25);
            ext.add(progress, null);

            JLabel img = new JLabel(EBISystem.getInstance().getIconResource("update.png"));
            img.setBounds(15, 30, 128, 128);
            ext.add(img, null);

            // Action Yes no JButton yes = new
            yes = new JButton(EBISystem.i18n("EBI_LANG_YES"));
            yes.setBounds(220, 200, 100, 25);
            yes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    update();
                }
            });
            ext.add(yes, null);
            no = new JButton(EBISystem.i18n("EBI_LANG_NO"));
            no.setBounds(330, 200, 100, 25);
            no.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!downloadFinish) {
                        ext.setVisible(false);
                    } else {
                        //restart system
                        EBIProcessStarter.getInstance().startEBINeutrino();
                        System.exit(0);
                    }
                }
            });
            ext.add(no, null);
            ext.setVisible(true);
        }
    }

    public void setConnection() {
        try {
            // Read file on server
            final URL u = new URL(sysPath + "/update.xml");
            is = u.openStream();
            try {
                final SAXBuilder builder = new SAXBuilder();
                xmlOnline = builder.build(is);
            } catch (final IOException e) {
                fileOnlineExist = false;
            } catch (final JDOMException ex) {
                fileOnlineExist = false;
            }

            // Read local file
            try {
                final SAXBuilder builder1 = new SAXBuilder();
                xmlLocal = builder1.build(new File(updateFilePath + "update.xml"));
            } catch (final IOException e) {
                fileLocalExist = false;
            } catch (final JDOMException ex) {
                fileLocalExist = false;
            }

        } catch (final MalformedURLException ex) {
        } catch (final IOException ioe) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (final IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public boolean readConfig() {
        boolean ret = false;
        try {
            if (fileLocalExist && fileOnlineExist) {
                if (xmlLocal != null && xmlOnline != null) {
                    try {
                        int localVersion = Integer.parseInt(xmlLocal.getRootElement().getAttribute("version").getValue().replace(".", "").toString());
                        int onlineVersion = Integer.parseInt(xmlOnline.getRootElement().getAttribute("version").getValue().replace(".", "").toString());
                        if (localVersion < onlineVersion) {
                            onlineVer = xmlOnline.getRootElement().getAttribute("version").getValue();
                            localVer = xmlLocal.getRootElement().getAttribute("version").getValue();
                            processElement(xmlOnline.getRootElement());
                            ret = true;
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        ret = false;
                    }
                } else {
                    ret = false;
                }
            } else {
                ret = false;
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }

    private void processElement(final Element el) {
        processAttributes1(el);
        final Iterator children = el.getChildren().iterator();
        while (children.hasNext()) {
            processElement((Element) children.next());
        }
    }

    private void processAttributes1(final Element el) {

        final Iterator atts = el.getAttributes().iterator();

        if (!el.getName().equals("file")) {
            strArr = new String[3];
        }

        boolean isMyOS = false;
        while (atts.hasNext()) {
            final Attribute att = (Attribute) atts.next();

            if (att.getName().equals("os")) {
                String os = att.getValue();
                if (EBISystem.isMac() && EBISystem.isMac() == EBISystem.isMac(os)) {
                    isMyOS = true;
                    strArr[0] = att.getValue();
                } else if (EBISystem.isWindows() && EBISystem.isWindows() == EBISystem.isWindows(os)) {
                    isMyOS = true;
                    strArr[0] = att.getValue();
                } else if (EBISystem.isUnix() && EBISystem.isUnix() == EBISystem.isUnix(os)) {
                    isMyOS = true;
                    strArr[0] = att.getValue();
                }
            }
            if (isMyOS) {
                if (att.getName().equals("name")) {
                    strArr[1] = att.getValue();
                }
                if (att.getName().equals("filename")) {
                    strArr[2] = att.getValue();
                }
            }
        }

        if (!el.getName().equals("file")) {
            this.arrList.add(strArr);
        }
    }

    public void update() {
        Iterator iter = arrList.iterator();

        StringBuffer buffer = new StringBuffer();
        while (iter.hasNext()) {

            String[] arrx = (String[]) iter.next();
            buffer.append("<br> " + arrx[1] + "...");

            yes.setVisible(false);
            no.setVisible(false);

            //write file thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisible(true);
                            progress.setMaximum((int) downloadSize);
                        }
                    });

                    title.setText("<html><body><b>Loading...</b></body></html>");
                    body.setText("<html><body style='color:#cccccc;font-family:Tahoma;font-size:9px'>"
                            + "Downloading File: " + arrx[2] + "...</body></html>");
                    writeFileTo(retriveData(arrx[2]), new File(arrx[2]));
                    progress.setIndeterminate(true);
                    progress.setString("Installing...");
                    extractEBINeutrino(arrx[2]);
                    progress.setIndeterminate(false);
                    //show restart button after sucessfully update the system
                    title.setText("<html><body><b></b></body></html>");
                    body.setText("<html><body style='color:#cccccc;font-family:Tahoma;font-size:9px'>"
                            + "Download done!<br>Please Restart EBI Neutrino!</body></html>");
                    no.setText("Restart");
                    no.setVisible(true);
                    downloadFinish = true;
                }
            }).start();
        }
    }

    public InputStream retriveData(final String fileName) {
        InputStream isDown = null;
        try {
            final URL urlDown = new URL(sysPath + "/" + fileName);
            URLConnection urlConnection = urlDown.openConnection();
            urlConnection.connect();
            downloadSize = urlConnection.getContentLength();
            isDown = urlConnection.getInputStream();
        } catch (final MalformedURLException ex) {
            EBIExceptionDialog.getInstance("Unable to download the Update: " + ex.getMessage());
            ex.printStackTrace();
        } catch (final IOException ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance("Unable to download the Update: " + ex.getMessage());
        }
        return isDown;
    }

    public void writeFileTo(final InputStream stream, final File pathDest) {
        try {
            final FileOutputStream streamOut = new FileOutputStream(pathDest.getAbsolutePath());
            int available = stream.available();

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    progress.setString("%");
                    progress.setVisible(true);
                    progress.setMaximum(100);
                }
            });

            if (available != -1) {
                byte[] buffer = new byte[available];
                count = available;
                while (stream.read(buffer) != -1) {

                    streamOut.write(buffer);
                    int cnt = stream.available();
                    buffer = new byte[cnt];

                    count += cnt;
                    final double a = (count / downloadSize) * 100;
                    progress.setString("%" + (int) a);
                    progress.setValue((int) a);
                }

            } else {
                EBIExceptionDialog.getInstance("Unable to download the Update: No data received from server!");
            }
            stream.close();
            streamOut.close();
        } catch (final FileNotFoundException ex) {
            EBIExceptionDialog.getInstance("Unable to download the Update: " + ex.getMessage());
            ex.printStackTrace();
        } catch (final IOException ex) {
            EBIExceptionDialog.getInstance("Unable to download the Update: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void extractEBINeutrino(String fileZip) {
        try {

            File destDir = new File(".");

            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));

            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static EBISocketDownloader getInstance() {
        if (uploader == null) {
            uploader = new EBISocketDownloader();
        }
        return uploader;
    }

}
