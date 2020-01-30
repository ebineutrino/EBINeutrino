package org.sdk.gui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Format a swing JTextField component
 *
 */
public class EBIJTextFieldNumeric extends PlainDocument {

    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA = LOWERCASE + UPPERCASE;
    public static final String NUMERIC = "0123456789";
    public static final String FLOAT = NUMERIC + ",.";
    public static final String NUMERIC_MINUS = NUMERIC + "-";
    public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;
    public static final String PHONE = NUMERIC + "+"+" ";
    public static final String EMAIL = ALPHA_NUMERIC + ".@";
    public static final String HOUR = NUMERIC;
    public static final String MINUTE = NUMERIC;
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
        if (acceptedChars.equals(NUMERIC)
                || acceptedChars.equals(FLOAT)
                || acceptedChars.equals(ALPHA_NUMERIC)) {
            negativeAccepted = negativeaccepted;
            acceptedChars += "-";
        }
    }

    /**
     * insert a string in the specified offset location
     *
     * @param int offset, String str
     * @param AttributeSet attr
     */
    @Override
    public void insertString(final int offset, String str, final AttributeSet attr) throws BadLocationException {

        if (str == null) {
            return;
        }
        
        if(acceptedChars.equals(HOUR) || acceptedChars.equals(MINUTE)){
            String partialStr = this.getText(0, offset);
            if(partialStr.length() > 1){
                return;
            }
        }
        
        if(acceptedChars.equals(PHONE)){
            String partialStr = this.getText(0, offset);
            partialStr += str;
            if(partialStr.contains("+") && partialStr.indexOf("+") > 0){
                return;
            }
        }
        
        if(acceptedChars.equals(EMAIL)){
            if(this.getText(0, offset).indexOf(".") != -1){
                if(this.getText(0, offset).indexOf("@") == -1){
                    return;
                }
            }
        }

        if (acceptedChars.equals(UPPERCASE)) {
            str = str.toUpperCase();
        } else if (acceptedChars.equals(LOWERCASE)) {
            str = str.toLowerCase();
        }

        if(!acceptedChars.equals(FLOAT)){
            for (int i = 0; i < str.length(); i++) {
                if (acceptedChars.indexOf(String.valueOf(str.charAt(i))) == -1) {
                    return;
                }
            }
        }

        if (negativeAccepted && str.indexOf("-") != -1) {
            if (str.indexOf("-") != 0 || offset != 0) {
                return;
            }
        }
        super.insertString(offset, str, attr);
    }
}
