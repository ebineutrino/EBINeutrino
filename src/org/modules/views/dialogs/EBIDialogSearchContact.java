package org.modules.views.dialogs;

import org.modules.models.ModelContactSearch;
import org.modules.utils.AbstractTableKeyAction;
import org.modules.utils.JTableActionMaps;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.gui.dialogs.EBIWinWaiting;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EBIDialogSearchContact {

    public List<JComponent> jSetterComponent = null;
    public List<String> jSetterFieldName = null;
    public int jIndexer = 0;
    private ModelContactSearch tabModel = null;
    private int selRowContact = -1;
    private boolean loadCompleteCompany = false;
    final EBIWinWaiting wait = EBIWinWaiting.getInstance(EBISystem.i18n("EBI_LANG_LOAD_COMPANY_DATA"));


    public EBIDialogSearchContact(final boolean load) {
        loadCompleteCompany = load;
        jSetterFieldName = new ArrayList();
        jSetterComponent = new ArrayList();
        EBISystem.builder().loadGUI("CRMDialog/crmContactSearch.xml");
        tabModel = new ModelContactSearch();
        EBISystem.builder().table("searchContactTable","searchCRMContact").setModel(tabModel);
        EBISystem.builder().table("searchContactTable","searchCRMContact").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void setVisible(){

       EBISystem.builder().dialog("searchCRMContact").setTitle(EBISystem.i18n("EBI_LANG_C_SEARCH_CONTACT"));
       EBISystem.builder().vpanel("searchCRMContact").setModuleTitle(EBISystem.i18n("EBI_LANG_C_SEARCH_CONTACT"));

       final KeyAdapter adapt = new java.awt.event.KeyAdapter() {
				@Override
				public void keyPressed(final java.awt.event.KeyEvent e) {
				   if(e.getKeyCode() == KeyEvent.VK_ENTER){
					   createContactBySearchView();
				   }
				}
	  };
	  EBISystem.builder().textField("filterTableText", "searchCRMContact").requestFocus();
      EBISystem.builder().textField("filterTableText", "searchCRMContact").addKeyListener(adapt);

      final ListSelectionModel rowSM = EBISystem.builder().table("searchContactTable","searchCRMContact").getSelectionModel();
      rowSM.addListSelectionListener(new ListSelectionListener() {

            @Override
			public void valueChanged(final ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("applyButton","searchCRMContact").setEnabled(false);
                } else {
                    if(lsm.getMinSelectionIndex() >= 0){
                    	if(lsm.getMinSelectionIndex() > 0){
                    		selRowContact = EBISystem.builder().table("searchContactTable","searchCRMContact").convertRowIndexToModel(lsm.getMinSelectionIndex());
                    	}else{
                    		selRowContact = 0;
                    	}
                        if (tabModel.data[selRowContact][0] != null && !tabModel.data[selRowContact][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                            EBISystem.builder().button("applyButton","searchCRMContact").setEnabled(true);
                        }
                    }
                }
            }
        });

        new JTableActionMaps(EBISystem.builder().table("searchContactTable","searchCRMContact")).setTableAction(new AbstractTableKeyAction() {

                @Override
				public void setArrowDownKeyAction(final int selRow) {
                     selRowContact = selRow;
                }

                @Override
				public void setArrowUpKeyAction(final int selRow) {
                    selRowContact = selRow;
                }

                @Override
				public void setEnterKeyAction(final int selRow) {
                    selRowContact = selRow;
                    if (selRowContact < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selRowContact][0].toString())) {
                        return;
                    }

                  loadData();
                }
         });

        EBISystem.builder().table("searchContactTable","searchCRMContact").addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
				public void mouseClicked(final java.awt.event.MouseEvent e) {

                    if(EBISystem.builder().table("searchContactTable","searchCRMContact").rowAtPoint(e.getPoint()) > -1){
                        selRowContact = EBISystem.builder().table("searchContactTable","searchCRMContact").convertRowIndexToModel(EBISystem.builder().table("searchContactTable","searchCRMContact").rowAtPoint(e.getPoint()));
                    }

                    if (e.getClickCount() == 2) {
                    	loadData();
                    }
                }
        });

        EBISystem.builder().button("searchButton","searchCRMContact").setText(EBISystem.i18n("EBI_LANG_SEARCH"));
        EBISystem.builder().button("searchButton","searchCRMContact").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    createContactBySearchView();
                    EBISystem.builder().table("searchContactTable","searchCRMContact").changeSelection(0, 0, false, false);
                    EBISystem.builder().table("searchContactTable","searchCRMContact").requestFocus();
                }
        });

        EBISystem.builder().button("cancelButton","searchCRMContact").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.builder().button("cancelButton","searchCRMContact").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    EBISystem.builder().dialog("searchCRMContact").setVisible(false);
                }
       });

        EBISystem.builder().button("applyButton","searchCRMContact").setText(EBISystem.i18n("EBI_LANG_APPLY"));
        EBISystem.builder().button("applyButton","searchCRMContact").setEnabled(false);
        EBISystem.builder().button("applyButton","searchCRMContact").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                	loadData();
                }
        });
        
        EBISystem.builder().showGUI();

    }

    public void createContactBySearchView() {

        try {
            EBISystem.builder().table("searchContactTable","searchCRMContact").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "searchCRMContact").getText()));

            final String query = " SELECT * FROM COMPANYCONTACTS LEFT JOIN COMPANY ON " +
                    " COMPANYCONTACTS.COMPANYID=COMPANY.COMPANYID " +
                    " LEFT JOIN COMPANYCONTACTADDRESS ON COMPANYCONTACTADDRESS.CONTACTID = COMPANYCONTACTS.CONTACTID ";


            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(query);
            final ResultSet rs = EBISystem.getInstance().iDB().executePreparedQuery(ps);

            int i = 0;
            rs.last();
            if (rs.getRow() > 0) {
                final Object[][] da = new Object[rs.getRow()][10];
                rs.beforeFirst();
                while (rs.next()) {

                    da[i][0] = rs.getString("COMPANY.NAME") == null ? "" : rs.getString("COMPANY.NAME");
                    da[i][1] = rs.getString("POSITION") == null ? "" : rs.getString("POSITION");
                    da[i][2] = rs.getString("GENDER") == null ? "" : rs.getString("GENDER");
                    da[i][3] = rs.getString("SURNAME") == null ? "" : rs.getString("SURNAME");
                    da[i][4] = rs.getString("NAME") == null ? "" : rs.getString("NAME");
                    da[i][5] = rs.getString("ZIP") == null ? "" : rs.getString("ZIP");
                    da[i][6] = rs.getString("LOCATION") == null ? "" : rs.getString("LOCATION");
                    da[i][7] = rs.getString("EMAIL") == null ? "" : rs.getString("EMAIL");
                    da[i][8] = rs.getString("COMPANYID") == null ? "" : rs.getString("COMPANYID");
                    da[i][9] = rs.getString("CONTACTID");
                    i++;
                }
                if (da.length != 0) {
                    tabModel.data = da;
                } else {
                    tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "","", "", "", "", "", "", ""}};
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
                }
            }
           
            tabModel.fireTableDataChanged();
            EBISystem.builder().table("searchContactTable","searchCRMContact").requestFocus();
            EBISystem.builder().table("searchContactTable","searchCRMContact").changeSelection(0, 0, false, false);
            rs.close();
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
    }

    public void setValueToComponent(final JComponent comp, final String field) {
        jSetterFieldName.add(jIndexer, field);
        jSetterComponent.add(jIndexer, comp);
        jIndexer++;
    }
    
    private void loadData(){

        if((selRowContact < 0 || tabModel.data[0][0] == null ||
                tabModel.data[0][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT")))){
            return;
        }

        EBISystem.builder().dialog("searchCRMContact").setVisible(false);
        wait.setVisible(true);

    	final boolean loadCMP = loadCompleteCompany;
    	final Thread trd1 = new Thread(new Runnable() {
			@Override
			public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if (loadCMP) {
                                if(EBISystem.getInstance().getIEBISecurityInstance().checkCanReleaseModules()) {
                                    EBISystem.getModule().createUI(Integer.parseInt(tabModel.data[selRowContact][8].toString()), false);
                                    if (EBISystem.builder().existView("Contact")) {
                                        EBISystem.getModule().ebiContainer.setSelectedTab(EBISystem.getInstance().getIEBIContainerInstance().getIndexByTitle(EBISystem.i18n("EBI_LANG_C_CONTACT")));
                                        EBISystem.getModule().getContactPane().editRemoteContact(Integer.parseInt(tabModel.data[selRowContact][9].toString()));
                                    }
                                }
                            } else {
                                setContactData(tabModel.data[selRowContact][9].toString(), jSetterFieldName, jSetterComponent);
                            }
                        }catch(final Exception ex){
                            ex.printStackTrace();
                        }finally{
                            wait.setVisible(false);
                        }
                    }
                });
			}
		});
    	trd1.start();
    }

    /**
     * 
     * @param rownr
     * @param nof
     * @param jcp
     * @return boolean
     */
    public  boolean setContactData(final String rownr, final List<String> nof, final List<JComponent> jcp) {
        boolean ret = true;
        try {

            final String query = " SELECT * FROM COMPANY as company, COMPANYCONTACTS as contact " +
                           " LEFT JOIN COMPANYCONTACTADDRESS as contactAddess ON contact.CONTACTID= contactAddess.CONTACTID WHERE contact.CONTACTID=? " +
                           " AND company.COMPANYID=contact.COMPANYID ";
            
            final PreparedStatement ps = EBISystem.getInstance().iDB().initPreparedStatement(query);
            ps.setString(1, rownr);
            final ResultSet rs = EBISystem.getInstance().iDB().executePreparedQuery(ps);
            
            rs.next();
            final Object[] fieldName = nof.toArray();
            final JComponent[] component = jcp.toArray(new JComponent[jcp.size()]);
            for (int i = 0; i < nof.size(); i++) {
                if (component[i] instanceof JTextField) {
                    final JTextField field = (JTextField) component[i];
                    field.setText(rs.getString(fieldName[i].toString().toUpperCase()));
                }else if (component[i] instanceof JComboBox) {
                    final JComboBox field = (JComboBox) component[i];
                    field.setSelectedItem(rs.getString(fieldName[i].toString().toUpperCase()));
                }else if (component[i] instanceof JTextArea) {
                    final JTextArea field = (JTextArea) component[i];
                    field.setText(rs.getString(fieldName[i].toString().toUpperCase()));
                }else if (component[i] instanceof JXDatePicker) {
                	final JXDatePicker field = (JXDatePicker) component[i];
                	field.setDate(rs.getDate(fieldName[i].toString().toUpperCase()));
                }
            }
            rs.close();

        } catch (final SQLException ex) {
        	ex.printStackTrace();
            ret = false;
        }
        return ret;
    }

}

