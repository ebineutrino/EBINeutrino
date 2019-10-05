package org.sdk.gui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * Format a swing JTextField component
 *
 */

public class EBIJTextFieldNumeric extends PlainDocument {
	
	   public static final String LOWERCASE  ="abcdefghijklmnopqrstuvwxyz";
	   public static final String UPPERCASE  ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	   public static final String ALPHA   = LOWERCASE + UPPERCASE;
	   public static final String NUMERIC = "0123456789";
	   public static final String FLOAT = NUMERIC + ",.";
	   public static final String NUMERIC_MINUS = NUMERIC + "-";
	   public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;
	   protected String acceptedChars = null;
	   protected boolean negativeAccepted = false;
	   public static String actualString = "";

	   public EBIJTextFieldNumeric() {
	     this(FLOAT);
	   }
	   
	   public EBIJTextFieldNumeric(final String acceptedchars) {
	     acceptedChars = acceptedchars;
	   }
	   
	   public void setNegativeAccepted(final boolean negativeaccepted) {
	     if (acceptedChars.equals(NUMERIC) ||
	         acceptedChars.equals(FLOAT) ||
	         acceptedChars.equals(ALPHA_NUMERIC)){
	         negativeAccepted = negativeaccepted;
	        acceptedChars += "-";
	        }
	      }
	   
	   /**
	    * insert a string in the specified offset location
	    * @param int offset, String  str
	    * @param AttributeSet attr
	    */

	   @Override
	public void insertString(final int offset, String  str, final AttributeSet attr) throws BadLocationException {
	     if (str == null) return;

	     if (acceptedChars.equals(UPPERCASE))
	        str = str.toUpperCase();
	     else if (acceptedChars.equals(LOWERCASE))
	        str = str.toLowerCase();

	     for (int i=0; i < str.length(); i++) {
	       if (acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1)
	         return;
	       }

	     if(acceptedChars.equals(FLOAT) || (acceptedChars.equals(FLOAT + "-") && negativeAccepted)) {
	        if(str.indexOf(".") != -1) {
	           if(getText(0, getLength()).indexOf(".") != -1) {
	              return;
	              }
	           }
	        }

	     if (negativeAccepted && str.indexOf("-") != -1) {
	        if (str.indexOf("-") != 0 || offset != 0 ) {
	           return;
	           }
	        }  
	     super.insertString(offset, str, attr);
	   }
}
