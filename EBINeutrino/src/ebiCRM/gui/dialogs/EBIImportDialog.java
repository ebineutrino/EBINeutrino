package ebiCRM.gui.dialogs;

import ebiCRM.functionality.EBIExportCSV;
import ebiCRM.utils.HeaderSelector;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBIGUIRenderer;
import ebiNeutrinoSDK.utils.EBIAbstractTableModel;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;



/**
 * CSV Import Dialog
 */
public class EBIImportDialog {

    private IEBIGUIRenderer guiRenderer = null;
    private boolean ret = true;
    private HeaderSelector hSelector = null;
    private EBIAbstractTableModel model=null;
    private int countInsert=0;
    private int countUpdate=0;
    private String[] cols=null;
    private String[][] rows=null;
    
    public EBIImportDialog(){
        this.guiRenderer = EBISystem.gui();
    }

    /**
     * Show and Initialize the CSV import dialog
     */

    public void setVisible(){
       guiRenderer.loadGUI("CRMDialog/importDataDialog.xml");

       guiRenderer.getProgressBar("importProgress","dataImportDialog").setString(EBISystem.i18n("EBI_LANG_IMPORT_VALUE"));
       guiRenderer.getProgressBar("importProgress","dataImportDialog").setStringPainted(true);
       guiRenderer.button("browsButton","dataImportDialog").addActionListener(new ActionListener(){

			public void actionPerformed(final ActionEvent e){

                 final File fs =EBISystem.getInstance().getOpenDialog(JFileChooser.FILES_ONLY);

                 if(fs != null){
                    guiRenderer.textField("importPathText","dataImportDialog").setText(fs.getAbsolutePath());
                 }
             }
        });

       guiRenderer.button("importButton","dataImportDialog").addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent e){
                 if(!validateInputs()){
                     return;
                 }

                 importThread();
             }
        });

       guiRenderer.button("closeImportDialog","dataImportDialog").addActionListener(new ActionListener(){
			public void actionPerformed(final ActionEvent e){
                guiRenderer.dialog("dataImportDialog").setVisible(false);
             }
        });

       guiRenderer.showGUI();
    }


    private boolean validateInputs() {

        if("".equals(guiRenderer.textField("delimiterText","dataImportDialog").getText())){
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_DELIMITED_SHOULD_NOT_EMPTY")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if("".equals(guiRenderer.textField("importPathText","dataImportDialog") .getText())){
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_IMPORT_FILE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Start thread for import a CSV file
     */
    private void importThread(){

        final Runnable waitRunner = new Runnable(){

			public void run() {
               
                   	guiRenderer.getProgressBar("importProgress","dataImportDialog").setIndeterminate(true);
                    final EBIExportCSV  importCSV = new EBIExportCSV();

                    try {
                       ret = importCSV.importCVS(guiRenderer.textField("importPathText","dataImportDialog").getText(),guiRenderer.textField("delimiterText","dataImportDialog").getText());
                    } catch (final IOException ex) {
                        EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
                    }finally{

                       guiRenderer.getProgressBar("importProgress","dataImportDialog").setIndeterminate(false);
                        
                        if(ret){
                        	guiRenderer.dialog("dataImportDialog").setVisible(false);
                        }
                    }
                    
                    
                    if(ret){
                      SwingUtilities.invokeLater(new Runnable(){
						public void run(){
                    		guiRenderer.loadGUI("CRMDialog/csvSetImport.xml");
                            model = (EBIAbstractTableModel)guiRenderer.table("valueTable", "csvSetImportDialog").getModel();
                         	
                         	hSelector = new HeaderSelector(guiRenderer.table("valueTable", "csvSetImportDialog"));
                         	guiRenderer.table("valueTable", "csvSetImportDialog").getTableHeader().addMouseListener(hSelector);
                         	
                         	model.columnNames = importCSV.columnNames;
                         	
                         	model.data = importCSV.data;
                         	model.fireTableStructureChanged();

                         	fillList();

                           guiRenderer.getCheckBox("removeHeader","csvSetImportDialog").addActionListener(new ActionListener() {
                                 public void actionPerformed(final ActionEvent e) {

                                     if(EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_WOULD_YOU_LIKE_TO_REMOVE_HEADER")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true){
                                        guiRenderer.getCheckBox("removeHeader","csvSetImportDialog").setVisible(false);
                                         final Object[][] newData = new Object[model.data.length-1][model.columnNames.length];
                                         for(int i=0; i<model.data.length; i++){
                                             if(i >= 1){
                                                 newData[i-1] = model.data[i];
                                             }
                                         }
                                         model.data = newData;
                                         model.fireTableDataChanged();
                                     }
                                 }
                             });
                         	
                         	guiRenderer.combo("dataTableText", "csvSetImportDialog").addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
								   if(guiRenderer.combo("dataTableText", "csvSetImportDialog").getSelectedIndex() > 0){
									   fillFieldList(guiRenderer.combo("dataTableText", "csvSetImportDialog").getSelectedItem().toString());
									  guiRenderer.getCheckBox("insertUpdate", "csvSetImportDialog").setEnabled(true);
									  guiRenderer.combo("keyText", "csvSetImportDialog").removeAllItems();
									  guiRenderer.combo("keyText", "csvSetImportDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
								   }
								}
							});
                         	
                         	guiRenderer.getCheckBox("insertUpdate", "csvSetImportDialog").addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									if(((JCheckBox)e.getSource()).isSelected()){
										guiRenderer.combo("keyText", "csvSetImportDialog").setEnabled(true);
									}else{
										guiRenderer.combo("keyText", "csvSetImportDialog").setEnabled(false);
									}
								}
							});
                         	

                         	guiRenderer.getCheckBox("insertUpdate", "csvSetImportDialog").setEnabled(false);
                         	guiRenderer.combo("keyText", "csvSetImportDialog").setEnabled(false);
                         	
                         	guiRenderer.button("saveValue", "csvSetImportDialog").addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
                                    final List<TableColumn> column =guiRenderer.table("valueTable", "csvSetImportDialog").getColumns();

                                    final Iterator itx = column.iterator();
                                    cols = new String[column.size()];
                                    int i=0;

                                    while(itx.hasNext()){
                                        final TableColumn col = (TableColumn)itx.next();
                                        cols[i] = col.getHeaderValue().toString();
                                        i++;
                                    }

                                    rows = new String[guiRenderer.table("valueTable", "csvSetImportDialog").getRowCount()][guiRenderer.table("valueTable", "csvSetImportDialog").getColumnCount()];

                                    for(int x = 0; x<guiRenderer.table("valueTable", "csvSetImportDialog").getRowCount(); x++){
                                        for(int j = 0; j<guiRenderer.table("valueTable", "csvSetImportDialog").getColumnCount(); j++){
                                            rows[x][j] =guiRenderer.table("valueTable", "csvSetImportDialog").getValueAt(x,j).toString();
                                        }
                                    }

								    if(!validateInput()){
									    return;
									}

									guiRenderer.loadGUI("CRMDialog/insertCSVDataDialog.xml");
									guiRenderer.getProgressBar("importProgress","insertCSVDialog").setMaximum(
																guiRenderer.table("valueTable", "csvSetImportDialog").getRowCount()-1);
									guiRenderer.getProgressBar("importProgress","insertCSVDialog").setStringPainted(true);
									
									guiRenderer.showGUI(); 
									
									final Runnable run = new Runnable(){
										public void run(){
											// DO INSERT OR UPDATE HERE update ui
                                           try{ 
											boolean ret= true;
											for(int i=0; i<model.data.length; i++){
												
												 if(!(ret = setDBCSVTOObject(
														 		guiRenderer.combo("dataTableText", "csvSetImportDialog").getSelectedItem().toString(),
                                                                cols,
                                                                rows[i],guiRenderer.getCheckBox("insertUpdate", "csvSetImportDialog").isSelected(),guiRenderer.combo("keyText", "csvSetImportDialog").getSelectedIndex()-1
														 ))){
													 EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_UNSUPORTED_FORMAT_EXCEPTION")).Show(EBIMessage.ERROR_MESSAGE);
													 break;
												 }

												guiRenderer.label("importPath", "insertCSVDialog").setText(model.data[i][0].toString()+" "+model.data[i][1].toString()+" "+model.data[i][2].toString()+".........");
												guiRenderer.getProgressBar("importProgress","insertCSVDialog").setValue(i+1);
											}
											if(ret){
												EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_RECORD_SUCCESSFULLY_IMPORTED")).Show(EBIMessage.INFO_MESSAGE);
											}
                                           }catch(final Exception ex){
                                                   ex.printStackTrace();
                                           }
										}
										
									};
									
									final Thread thread = new Thread(run,"Insert CSV to DB");
									thread.start();

								}
							});
                         	
                         	guiRenderer.button("closeDialog", "csvSetImportDialog").addActionListener(new ActionListener() {
								public void actionPerformed(final ActionEvent e) {
									guiRenderer.dialog("csvSetImportDialog").setVisible(false);
								}
							});
                         	
                         	guiRenderer.showGUI(); 
                    	 }
                      });	

                    }
              }
          };

      final Thread loaderThread = new Thread(waitRunner, "importCSVThread");
      loaderThread.start();
    }

    
    public boolean validateInput(){

    	if(EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
    				equals(guiRenderer.combo("dataTableText", "csvSetImportDialog").getSelectedItem().toString())){
    		
    		EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_PLEASE_SELECT_VALUE_FROM_TABLE_LIST")).Show(EBIMessage.ERROR_MESSAGE);
    		return false;
    	}
    	
    	if(guiRenderer.getCheckBox("insertUpdate", "csvSetImportDialog").isSelected() && EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
				equals(guiRenderer.combo("keyText", "csvSetImportDialog").getSelectedItem().toString())){
		
			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_ASSIGN_RIGHT_DATABASE_FIELD_TO_COLUMN")).Show(EBIMessage.ERROR_MESSAGE);
			return false;
    	}

    	for(int i=0; i<cols.length; i++){
    		if(("Col "+i).equals(cols[i])){
    			EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_ASSIGN_RIGHT_DATABASE_FIELD_TO_COLUMN")).Show(EBIMessage.ERROR_MESSAGE);
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    
    private void fillList() {

        try {
            // Gets the database metadata
            final DatabaseMetaData dbmd =EBISystem.getInstance().iDB().getActiveConnection().getMetaData();
            final String[] types = {"TABLE"};
            
            final ResultSet resultSet = dbmd.getTables(null, null, "%", types);
           guiRenderer.combo("dataTableText", "csvSetImportDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
            
            while (resultSet.next()) {
                if(!"EBIUSER".equals(resultSet.getString(3).toUpperCase()) && !"MAIL_ACCOUNT".equals(resultSet.getString(3).toUpperCase())
                        && !"MAIL_DELETED".equals(resultSet.getString(3).toUpperCase()) && !"MAIL_INBOX".equals(resultSet.getString(3).toUpperCase())
                        && !"MAIL_OUTBOX".equals(resultSet.getString(3).toUpperCase()) && !"SET_REPORTFORMODULE".equals(resultSet.getString(3).toUpperCase()) && !"SET_REPORTPARAMETER".equals(resultSet.getString(3).toUpperCase())
                        && !"EBIPESSIMISTIC".equals(resultSet.getString(3).toUpperCase()) && !"EBIDATASTORE".equals(resultSet.getString(3).toUpperCase())){
            	   guiRenderer.combo("dataTableText", "csvSetImportDialog").addItem(resultSet.getString(3).toUpperCase());
                }
            }
        } catch (final SQLException ex) {
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }

     }
    
    
    private void fillFieldList(final String table){

        try {
          // Gets the database metadata
          final DatabaseMetaData dbmd =EBISystem.getInstance().iDB().getActiveConnection().getMetaData();

          final ResultSet resultSet = dbmd.getColumns(null, null,table, null);
          
          resultSet.last();
          if(resultSet.getRow() > 0){
        	  hSelector.editor.items = new String[resultSet.getRow()];
        	  resultSet.beforeFirst();
        	  int i=0;
	          while (resultSet.next()) {
	        	  hSelector.editor.items[i] = resultSet.getString("COLUMN_NAME").toUpperCase();
	        	  i++;
	          }
          }

      } catch (final SQLException ex) {
          EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
      }
    
  }
    
    
    /**
     * Import CSV help method, parse, create, insert or update the sql query
     * @param tableName
     * @param header
     * @param values
     * @param
     * @return true success otherwise false
     */
    private boolean setDBCSVTOObject(final String tableName,final String[] header,final Object[] values, final boolean isUpdate, final int pos){

         String insertUpdate;
         boolean ret=true;

             final Object[] headerValue = parseSyntax(header, values,isUpdate,pos);
             if(headerValue != null){
            	 
                 if(!Boolean.valueOf(headerValue[2].toString())){
                    insertUpdate  = "INSERT INTO "+tableName.toUpperCase()+" ( "+headerValue[0].toString()+" ) " +
                                                                           " VALUES("+headerValue[1].toString()+")";
                    countInsert++;
                 }else{
                    insertUpdate  = "UPDATE "+tableName.toUpperCase()+" SET "+headerValue[0].toString();
                    countUpdate++;
                 }


                 try{
                     if(!EBISystem.getInstance().iDB().exec(insertUpdate)){
                        return false;
                     }
                 }catch(final Exception ex){
                	 EBIExceptionDialog.getInstance(ex.getMessage()).Show(EBIMessage.ERROR_MESSAGE);
                     ex.printStackTrace();
                     ret = false;
                 }
                 
             }else{
               ret = false;
             }
        return ret;
    }

    /**
     * Import CSV help method parse and assert SQL syntax
     * @return true success otherwise false
     */
    private Object[] parseSyntax(final String[] splH, final Object[] splV, final boolean insertUpdate, final int position){

        String[] returnValue = new String[3];
        boolean ret = true;
        final ResultSet resultSet = null;
        
        try{

           boolean isOracle = false;

           if("oracle".equals(EBISystem.DATABASE_SYSTEM)){
               isOracle = true;
           }

           
           // Build a SQL Query
           if(insertUpdate && (!"".equals(splV[position]) || splV[position] != null)){  // Update SQL

                String where="";
                returnValue[0] = "";
                for(int i=0; i<splH.length; i++){
                  if(i == position){
                      where = " WHERE "+splH[i]+"='"+StringEscapeUtils.escapeSql(splV[i].toString())+"'";
                  }else{
                    	  try{
                    		  returnValue[0] +=  splH[i]+"="+Integer.parseInt(splV[i].toString());
                    	  }catch(final NumberFormatException ex){
                    		  returnValue[0] +=  splH[i]+"='"+StringEscapeUtils.escapeSql(splV[i] == null ? "" : splV[i].toString())+"'"; 
                    	  }
	                    	  
                          if(i < splH.length-1){
                        	   
                              returnValue[0] += ",";
                          }
                  }

                }               
                returnValue[0] += where;

           } else{     // Insert SQL

              returnValue[0] = "";
              for(int i=0; i<splH.length; i++){
            	  if(i != position){
	                  returnValue[0] += splH[i];
	                  if(i < splH.length-1){
	                      returnValue[0] += ",";
	                  }
            	  }
              }

              returnValue[1] = "";
              for(int i=0; i<splV.length; i++){
            	 if(i != position){
	            	  try{
	            		  returnValue[1] += Integer.parseInt(splV[i].toString()); 
	            	  }catch(final NumberFormatException ex){
	            		  returnValue[1] += "'"+StringEscapeUtils.escapeSql(splV[i] == null ? "" : splV[i].toString())+"'";
	            	  }
	                  if(i < splV.length-1){
	                      returnValue[1] += ",";
	                  }
            	 }
              }
           }
           
           returnValue[0] = returnValue[0];

           if(returnValue[1]!=null){
               returnValue[1] = returnValue[1];
           }
           returnValue[2] = String.valueOf((insertUpdate && (!"".equals(splV[position]) || splV[position] != null)));

        }catch (final Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_UNSUPORTED_FORMAT_EXCEPTION")).Show(EBIMessage.ERROR_MESSAGE);
            ret = false;
        }finally{
            try {
                if(resultSet!= null){resultSet.close();}
            } catch (final SQLException ex) {
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            }
            if(ret == false){
                returnValue = null;
            }
        }

       return returnValue;
    }

}
