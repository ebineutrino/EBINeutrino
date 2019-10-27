package org.core.gui.component;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JEditorExt extends JTextPane {
    //protected LineNr txtarea;
    protected UndoManager undo;
    Document doc;

    public JEditorExt() {
        undo = new UndoManager();
        doc = this.getDocument();
        try {
            doc.addUndoableEditListener(new UndoableEditListener() {
                @Override
                public void undoableEditHappened(final UndoableEditEvent evt) {
                    undo.addEdit(evt.getEdit());
                }
            });
        } catch (final Exception e) {
            System.out.println("Undo Exception");
        }
        this.setEditorKit(new highlightKit()); //if I remove this comment undo redo do not work and with this comment highlight do not work
    }

    class HighlightDocument extends DefaultStyledDocument {
        private final Element rootElement;
        private final HashMap<String, Color> keywords;
        private final MutableAttributeSet style;
        private final Color commentColor = Color.gray;
        private final Pattern singleLineCommentDelimter = Pattern.compile("//");

        private Pattern multiLineCommentDelimiterStart = Pattern.compile("/*");
        private Pattern multiLineCommentDelimiterEnd = Pattern.compile("\\*/");

        public HighlightDocument() {
            putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
            rootElement = getDefaultRootElement();
            keywords = new HashMap<String, Color>();
            keywords.put("abstract", Color.blue);
            keywords.put("interface", Color.blue);
            keywords.put("class", Color.blue);
            keywords.put("class", Color.blue);
            keywords.put("extends", Color.blue);
            keywords.put("implements ", Color.blue);
            keywords.put("package ", new Color(0, 200, 0));
            keywords.put("import", new Color(0, 200, 0));
            keywords.put("private", new Color(0, 200, 0));
            keywords.put("protected", new Color(0, 200, 0));
            keywords.put("public", new Color(0, 200, 0));
            keywords.put("void", Color.orange);
            keywords.put("boolean", Color.orange);
            keywords.put("char", Color.orange);
            keywords.put("byte", Color.orange);
            keywords.put("float", Color.orange);
            keywords.put("double", Color.orange);
            keywords.put("long", Color.orange);
            keywords.put("short", Color.orange);
            keywords.put("int", Color.orange);
            keywords.put("true", Color.red);
            keywords.put("false", Color.red);
            keywords.put("const", Color.red);
            keywords.put("null", Color.red);
            keywords.put("break", Color.blue);
            keywords.put("case", Color.blue);
            keywords.put("catch", Color.blue);
            keywords.put("operator", Color.blue);
            keywords.put("continue", Color.blue);
            keywords.put("default", Color.blue);
            keywords.put("do", Color.blue);
            keywords.put("else", Color.blue);
            keywords.put("final", Color.blue);
            keywords.put("finally", Color.blue);
            keywords.put("for", Color.blue);
            keywords.put("if", Color.blue);
            keywords.put("instanceof", Color.red);
            keywords.put("native", Color.red);
            keywords.put("new", Color.red);
            keywords.put("return", Color.blue);
            keywords.put("static", Color.blue);
            keywords.put("super", Color.blue);
            keywords.put("switch", Color.blue);
            keywords.put("this", Color.blue);
            keywords.put("throw", Color.blue);
            keywords.put("throws", Color.blue);
            keywords.put("transient", Color.blue);
            keywords.put("try", Color.blue);
            keywords.put("volatile", Color.blue);
            keywords.put("while", Color.blue);
            style = new SimpleAttributeSet();
        }

        @Override
        public void insertString(final int offset, final String str, final AttributeSet attr) throws BadLocationException {
            super.insertString(offset, str, attr);
            processChangedLines(offset, str.length());
        }

        @Override
        public void remove(final int offset, final int length) throws BadLocationException {
            super.remove(offset, length);
            processChangedLines(offset, length);
        }

        public void processChangedLines(final int offset, final int length) throws BadLocationException {
            final String text = getText(0, getLength());
            highlightString(Color.black, 0, getLength(), true, false);

            Set<String> keyw = keywords.keySet();
            for (String keyword : keyw) {
                Color color = keywords.get(keyword);
                Pattern pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);

                while (matcher.find()) {
                    if (!matcher.group().equals("")) {
                        highlightString(color, matcher.start(), keyword.length(), true, false);
                        System.out.print("Start index: " + matcher.start());
                        System.out.print(" End index: " + matcher.end() + " ");
                        System.out.println(matcher.group());
                    }
                }
            }
        }

        public void highlightString(final Color col, final int begin, final int length, final boolean flag, final boolean bold) {
            StyleConstants.setForeground(style, col);
            StyleConstants.setBold(style, bold);
            setCharacterAttributes(begin, length, style, flag);
        }
    }

    class highlightKit extends StyledEditorKit {
        @Override
        public Document createDefaultDocument() {
            return new HighlightDocument();
        }
    }
} 