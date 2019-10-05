package org.core.test;

import org.core.gui.component.JEditorExt;

import javax.swing.*;
import java.awt.*;

public class EditorTest {

	private final JEditorExt editor = new JEditorExt();
	
	public void showEditor() {
		final JFrame frm = new JFrame();
		frm.setSize(800, 600);
		frm.setLayout(new BorderLayout());
		frm.getContentPane().add(editor, BorderLayout.CENTER);
		
		frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	
	public static void main(final String[] args) {
		new EditorTest().showEditor();
	}

}
