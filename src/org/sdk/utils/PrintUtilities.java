package org.sdk.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrintUtilities implements Printable {

    private final Component componentToBePrinted;

    public static void printComponent(final Component c) {
        new PrintUtilities(c).print();
    }

    public PrintUtilities(final Component componentToBePrinted) {
        this.componentToBePrinted = componentToBePrinted;
    }

    public void print() {
        final PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog())
            try {
            printJob.print();
        } catch (final PrinterException pe) {
            System.out.println("Error printing: " + pe);
        }
    }

    @Override
    public int print(final Graphics g, final PageFormat pf, final int pageIndex) {

        pf.setOrientation(PageFormat.LANDSCAPE);

        int response = NO_SUCH_PAGE;
        final Graphics2D g2 = (Graphics2D) g;

        // for faster printing, turn off double buffering
        disableDoubleBuffering(componentToBePrinted);

        final Dimension d = componentToBePrinted.getSize(); //get size of document
        final double panelWidth = d.width; //width in pixels
        final double panelHeight = d.height; //height in pixels
        final double pageHeight = pf.getImageableHeight(); //height of printer page
        final double pageWidth = pf.getImageableWidth(); //width of printer page

        final double scaleX = pageWidth / panelWidth;
        final double scaleY = pageHeight / panelHeight;

        final int totalNumPages = (int) Math.ceil(scaleY * panelHeight / pageHeight);

        // make sure not print empty pages
        if (pageIndex >= totalNumPages) {
            response = NO_SUCH_PAGE;
        } else {
            // shift Graphic to line up with beginning of print-imageable region
            g2.translate(pf.getImageableX(), pf.getImageableY());
            // shift Graphic to line up with beginning of next page to print
            g2.translate(0f, -pageIndex * pageHeight);
            // scale the page so the width fits...
            g2.scale(scaleX, scaleY);
            componentToBePrinted.paint(g2); //repaint the page for printing
            response = Printable.PAGE_EXISTS;
        }
        enableDoubleBuffering(componentToBePrinted);
        return response;
    }

    public static void disableDoubleBuffering(final Component c) {
        final RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void enableDoubleBuffering(final Component c) {
        final RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
