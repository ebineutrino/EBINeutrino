package org.sdk.utils;

import java.io.File;

	/** Filter to work with JFileChooser to select pdf file types. **/
public class EBIPDFFilter extends javax.swing.filechooser.FileFilter {
		
	  @Override
	public boolean accept (final File f) {
	    return f.getName ().toLowerCase ().endsWith (".pdf") || f.isDirectory();
	  }
	 
	  @Override
	public String getDescription () {
	    return "PDF (*.pdf)";
	  }
	} // class JavaFilter