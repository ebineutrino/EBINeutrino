package org.core.update;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EBISocketDownloader {

    private InputStream is = null;
    public Document xmlOnline = null;
    public Document xmlLocal = null;
    public String localVer = "";
    public String onlineVer = "";
    public List<Object> arrList = null;
    private String[] strArr = null;
    private boolean fileLocalExist = true;
    private boolean fileOnlineExist = true;
    public String SysPath = "";

    public EBISocketDownloader() {
        arrList = new ArrayList<Object>();
    }

    public void setConnection() {

        try {
            // Read file on server
            final URL u = new URL(SysPath + "/update.xml");
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
                xmlLocal = builder1.build(new File("update.xml"));

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
                    if (!xmlLocal.getRootElement().getAttribute("Version").getValue().equals(xmlOnline.getRootElement().getAttribute("Version").getValue())) {
                        onlineVer = xmlOnline.getRootElement().getAttribute("Version").getValue();
                        localVer = xmlLocal.getRootElement().getAttribute("Version").getValue();
                        processElement(xmlOnline.getRootElement());
                        ret = true;
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

        if (!el.getName().equals("EBINeutrinoUpdate")) {
            strArr = new String[4];
        } else {
            return;
        }

        while (atts.hasNext()) {

            final Attribute att = (Attribute) atts.next();
            if (att.getName().equals("Name")) {
                strArr[0] = att.getValue();
            }

            if (att.getName().equals("FileName")) {
                strArr[1] = att.getValue();
            }

            if (att.getName().equals("Version")) {
                strArr[2] = att.getValue();
            }

            if (att.getName().equals("Destination")) {
                strArr[3] = att.getValue();
            }

        }

        if (!el.getName().equals("EBINeutrinoUpdate")) {
            this.arrList.add(strArr);
        }


    }

    public InputStream retriveData(final String fileName) {

        InputStream isDown = null;

        try {

            final URL urlDown = new URL(SysPath + "/" + fileName);
            isDown = urlDown.openStream();

        } catch (final MalformedURLException ex) {

            ex.printStackTrace();

        } catch (final IOException ex) {

            ex.printStackTrace();

        }

        return isDown;
    }

    public void writeFileTo(final InputStream stream, final File pathDest) {

        try {

            final FileOutputStream streamOut = new FileOutputStream(pathDest.getAbsolutePath());

            int c;

            while ((c = stream.read()) != -1) {
                streamOut.write(c);
            }

            stream.close();
            streamOut.close();

        } catch (final FileNotFoundException ex) {

            ex.printStackTrace();

        } catch (final IOException ex) {

            ex.printStackTrace();

        }
    }

}
