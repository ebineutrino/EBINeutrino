package org.sdk.utils;

import java.io.File;

		/** Filter to work with JFileChooser to select java file types. **/
public class EBIDocFilter extends javax.swing.filechooser.FileFilter {
			
	@Override
	public boolean accept (final File f) {
	   return f.getName ().toLowerCase ().endsWith (".doc") || f.isDirectory();
	}
		 
    @Override
	public String getDescription () {
	   return "DOC (*.doc)";
	}
}