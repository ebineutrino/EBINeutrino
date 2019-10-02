package ebiNeutrino.core.gui.dialogs;


import javax.swing.*;
import java.awt.*;

public class EBISplashScreen extends JWindow {

	private JPanel jContentPane = null;
	private JProgressBar jProgressBar = null;
	private static ImageIcon splImg = null;

	public EBISplashScreen() {
		splImg = new ImageIcon(getClass().getClassLoader().getResource("splash.png"));
        initialize();
	    final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    final Dimension frameSize = getSize();
        setAlwaysOnTop(true);
	    setLocation((d.width - frameSize.width) / 2 , ((d.height-150) - frameSize.height) / 2);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		g.drawImage(splImg.getImage(), 0, 0, this);
	}


	private void initialize() {
		this.setContentPane(getJContentPane());
		this.setSize(485, 325);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
            jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJProgressBar(), null);
		}
		return jContentPane;
	}

	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setOpaque(false);
			jProgressBar.setBounds(new Rectangle(17, 255, 250, 5));
			jProgressBar.setBorderPainted(false);
			jProgressBar.setBorder(null);
			jProgressBar.setBackground(new Color(255,255,255,0));
			jProgressBar.setIndeterminate(true);
			jProgressBar.setStringPainted(true);
		}
		return jProgressBar;
	}
}
