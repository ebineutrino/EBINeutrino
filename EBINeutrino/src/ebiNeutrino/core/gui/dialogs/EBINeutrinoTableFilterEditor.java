package ebiNeutrino.core.gui.dialogs;

import javax.swing.*;
import java.awt.*;


/**
 * this dialog is not used yet
 * 
 *
 */
public class EBINeutrinoTableFilterEditor extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JList jListAvailableField = null;

	private JButton jButtonCancel = null;

	private JButton jButtonSave = null;

	private JTextField jTextFilterName = null;

	private JEditorPane jEditorListFilter = null;

	private JButton jButtonEqual = null;

	private JButton jButtonLike = null;

	private JButton jButtonGread = null;

	private JButton jButtonGreadEqual = null;

	private JButton jButtonLikeAll = null;

	/**
	 * @param owner
	 */
	public EBINeutrinoTableFilterEditor(final Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(489, 527);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			final JLabel jLabel2=new JLabel();
			jLabel2.setBounds(new Rectangle(8, 331, 129, 21));
			jLabel2.setText("Filter Abfrage:");
			final JLabel jLabel1=new JLabel();
			jLabel1.setBounds(new Rectangle(6, 75, 73, 18));
			jLabel1.setText("Name:");
			final JLabel jLabel=new JLabel();
			jLabel.setBounds(new Rectangle(8, 118, 189, 18));
			jLabel.setText("Von Feld:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJListAvailableField(), null);
			jContentPane.add(getJButtonCancel(), null);
			jContentPane.add(getJButtonSave(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(getJTextFilterName(), null);
			jContentPane.add(getJEditorListFilter(), null);
			jContentPane.add(jLabel2, null);
			jContentPane.add(getJButtonEqual(), null);
			jContentPane.add(getJButtonLike(), null);
			jContentPane.add(getJButtonGread(), null);
			jContentPane.add(getJButtonGreadEqual(), null);
			jContentPane.add(getJButtonLikeAll(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jListAvailableField	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getJListAvailableField() {
		if (jListAvailableField == null) {
			jListAvailableField = new JList();
			jListAvailableField.setBounds(new Rectangle(6, 140, 189, 184));
		}
		return jListAvailableField;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setBounds(new Rectangle(357, 469, 113, 26));
			jButtonCancel.setText("Abbrechen");
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSave() {
		if (jButtonSave == null) {
			jButtonSave = new JButton();
			jButtonSave.setBounds(new Rectangle(237, 469, 114, 26));
			jButtonSave.setText("Speichern");
		}
		return jButtonSave;
	}

	/**
	 * This method initializes jTextFilterName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFilterName() {
		if (jTextFilterName == null) {
			jTextFilterName = new JTextField();
			jTextFilterName.setBounds(new Rectangle(84, 74, 376, 20));
		}
		return jTextFilterName;
	}

	/**
	 * This method initializes jEditorListFilter	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JEditorPane getJEditorListFilter() {
		if (jEditorListFilter == null) {
			jEditorListFilter = new JEditorPane();
			jEditorListFilter.setBounds(new Rectangle(7, 355, 465, 103));
		}
		return jEditorListFilter;
	}

	/**
	 * This method initializes jButtonEqual	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEqual() {
		if (jButtonEqual == null) {
			jButtonEqual = new JButton();
			jButtonEqual.setBounds(new Rectangle(202, 141, 48, 26));
			jButtonEqual.setText("=");
		}
		return jButtonEqual;
	}

	/**
	 * This method initializes jButtonLike	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonLike() {
		if (jButtonLike == null) {
			jButtonLike = new JButton();
			jButtonLike.setBounds(new Rectangle(256, 141, 64, 27));
			jButtonLike.setText("LIKE");
		}
		return jButtonLike;
	}

	/**
	 * This method initializes jButtonGread	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGread() {
		if (jButtonGread == null) {
			jButtonGread = new JButton();
			jButtonGread.setBounds(new Rectangle(202, 175, 48, 27));
			jButtonGread.setText(">");
		}
		return jButtonGread;
	}

	/**
	 * This method initializes jButtonGreadEqual	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonGreadEqual() {
		if (jButtonGreadEqual == null) {
			jButtonGreadEqual = new JButton();
			jButtonGreadEqual.setBounds(new Rectangle(257, 175, 64, 27));
			jButtonGreadEqual.setText(">=");
		}
		return jButtonGreadEqual;
	}

	/**
	 * This method initializes jButtonLikeAll	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonLikeAll() {
		if (jButtonLikeAll == null) {
			jButtonLikeAll = new JButton();
			jButtonLikeAll.setBounds(new Rectangle(325, 141, 92, 27));
			jButtonLikeAll.setText("LIKE %");
		}
		return jButtonLikeAll;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
