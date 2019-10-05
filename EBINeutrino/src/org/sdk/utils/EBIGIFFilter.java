package org.sdk.utils;

import java.io.File;

/** Filter to work with JFileChooser to select java file types. **/
public class EBIGIFFilter extends javax.swing.filechooser.FileFilter {
	
  @Override
public boolean accept (final File f) {
    return f.getName ().toLowerCase ().endsWith (".gif") || f.isDirectory();
  }
 
  @Override
public String getDescription () {
    return "GIF (*.gif)";
  }
} // class JavaFilter