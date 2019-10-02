package ebiNeutrino.core.settings;

import ebiCRM.gui.dialogs.EBIExportDialog;
import ebiCRM.gui.dialogs.EBIImportDialog;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIImportSQLFiles;
import ebiNeutrinoSDK.interfaces.IEBISecurity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EBISystemSettingStart extends JPanel {

	public IEBISecurity  iSecurity = null;
	
    /**
     * This is the default constructor
     */
    public EBISystemSettingStart() {
        super();
        iSecurity    = EBISystem.getInstance().getIEBISecurityInstance();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        final JLabel jLabel2=new JLabel();
        jLabel2.setBounds(new java.awt.Rectangle(233, 208, 507, 334));
        jLabel2.setText("");

        final JLabel jLabel1=new JLabel();
        jLabel1.setBounds(new java.awt.Rectangle(38, 29, 150, 55));
        jLabel1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("advancedsettings.png")));
        jLabel1.setOpaque(false);
        jLabel1.setText("");
        final JLabel jLabel=new JLabel();
        jLabel.setBounds(new java.awt.Rectangle(118, 28, 350, 52));
        jLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 26));
        jLabel.setOpaque(false);
        jLabel.setText(EBISystem.i18n("EBI_LANG_SYSTEM_SETTING"));
          
        final JButton importSQL = new JButton(EBISystem.i18n("EBI_LANG_IMPORT_SQL"));
        importSQL.setBounds(new java.awt.Rectangle(20, 220, 200, 40));
        importSQL.addActionListener(importSQLAction());
        
        final JButton importCSV = new JButton(EBISystem.i18n("EBI_LANG_IMPORT_AS_CSV"));
        importCSV.setBounds(new java.awt.Rectangle(225, 220, 220, 40));
        importCSV.addActionListener(importCSVAction());
        
        final JButton exportCSV = new JButton(EBISystem.i18n("EBI_LANG_EXPORT_AS_CSV"));
        exportCSV.setBounds(new java.awt.Rectangle(450, 220, 210, 40));
        exportCSV.addActionListener(exportCSVAction());
        
        this.setLayout(null);
        this.setSize(842, 549);
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(jLabel2, null);
        this.add(importSQL, null);
        this.add(importCSV, null);
        this.add(exportCSV, null);
    }
    
    /**
	 * ActionListener Export CSV
	 * @return
	 */

	protected ActionListener exportCSVAction(){
	   return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
                boolean pass;
				if(EBISystem.getInstance().getIEBISystemUserRights().isCanPrint() ||
                        EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()){
					pass = true;
				}else{
					pass = iSecurity.secureModule();
				}
				if(pass){
                    new EBIExportDialog().setVisible();
                }
			}
		};
	}

     /**
	 * ActionListener Import CSV
	 * @return
	 */

	protected ActionListener importCSVAction(){
	   return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
                boolean pass  ;
				if(EBISystem.getInstance().getIEBISystemUserRights().isCanPrint() ||
                        EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()){
					pass = true;
				}else{
					pass = iSecurity.secureModule();
				}
				if(pass){
                    new EBIImportDialog().setVisible();
                }
			}
		};
	}

    /**
	 * ActionListener Import CSV
	 * @return
	 */

	protected ActionListener importSQLAction(){
	   return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
                boolean pass;
				if(EBISystem.getInstance().getIEBISystemUserRights().isAdministrator()){
					pass = true;
				}else{
					pass = iSecurity.secureModule();
				}
				if(pass){
                    final EBIImportSQLFiles importSQL = new EBIImportSQLFiles();
                    importSQL.setVisible(true);
                }
			}
		};
	}
}  

