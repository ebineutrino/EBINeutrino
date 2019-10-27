package org.sdk.utils;

import java.io.File;

/** Filter to work with JFileChooser to select jpg file types. **/
public class EBIJPGFilter extends javax.swing.filechooser.FileFilter {
	
  @Override
public boolean accept (final File f) {
    return f.getName ().toLowerCase ().endsWith (".jpg") || f.isDirectory();
  }
 
  @Override
public String getDescription () {
    return "JPG (*.jpg)";
  }
} // class JavaFilter