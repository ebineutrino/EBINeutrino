package ebiNeutrinoSDK.utils;


import java.io.File;

/** Filter to work with JFileChooser to select java file types. **/
public class EBIJARFilter extends javax.swing.filechooser.FileFilter {
	
  @Override
public boolean accept (final File f) {
    return f.getName ().toLowerCase ().endsWith (".jar") || f.isDirectory();
  }
 
  @Override
public String getDescription () {
    return "JAR (*.jar)";
  }
} // class JavaFilter
