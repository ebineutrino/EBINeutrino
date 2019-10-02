package ebiNeutrinoSDK.utils;

import java.io.File;

/** Filter to work with JFileChooser to select jasper file types. **/
public class EBIReportFilter extends javax.swing.filechooser.FileFilter {
	
  @Override
public boolean accept (final File f) {
    return f.getName ().toLowerCase ().endsWith (".jasper") || f.isDirectory();
  }
 
  @Override
public String getDescription () {
    return "Reportdatei (*.jasper)";
  }
} // class JavaFilter