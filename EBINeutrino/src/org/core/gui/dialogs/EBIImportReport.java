package org.core.gui.dialogs;

import org.core.EBIMain;
import org.sdk.EBISystem;
import org.sdk.gui.component.EBIVisualPanelTemplate;
import org.sdk.gui.dialogs.EBIDialogExt;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class EBIImportReport extends EBIDialogExt {

    private EBIMain ebiMain = null;
    private final JProgressBar progress = new JProgressBar();
    private final JLabel title = new JLabel();
    private final EBIVisualPanelTemplate pane = new EBIVisualPanelTemplate(false);

    public EBIImportReport(final EBIMain main){
      super(main);
      ebiMain = main;
      setName("InstallReports");
      pane.setEnableChangeComponent(false);
      pane.setModuleTitle(EBISystem.i18n("EBI_LANG_IMPORT_TILE"));
      pane.setModuleIcon(EBISystem.getInstance().getIconResource("editcopy.png"));

      this.setContentPane(pane);
      setSize(310, 120);
      storeLocation(true);
      storeSize(true);  
        
      title.setText(EBISystem.i18n("EBI_LANG_IMPORT_TILE"));
      title.setLocation(10,10);
      title.setSize(200,20);

      pane.add(title,null);
      progress.setSize(280,30);
      progress.setLocation(10,50);
      progress.setBorderPainted(true);
      progress.setIndeterminate(true);
      progress.setStringPainted(true);

      progress.setString(EBISystem.i18n("EBI_LANG_INSTALL_REPORTS"));
      pane.add(progress,null);
    }


    public void startReportImport(){

        setVisible(true);

        final Runnable run = new Runnable(){
            @Override
			public void run(){
                try {
                    Thread.sleep(500);
                    EBISystem.getInstance().iDB().exec("DELETE FROM SET_REPORTFORMODULE ");
                    EBISystem.getInstance().iDB().exec("DELETE FROM SET_REPORTPARAMETER ");

                    BufferedReader myBufferedReader = new BufferedReader(new FileReader("./reports/Reports.sql"));
                    String str="";
                    String line;
                    while(myBufferedReader.ready()){
                        line = myBufferedReader.readLine();
                        if("/".equals(line)){
                            EBISystem.getInstance().iDB().exec(str);
                          str = "";
                        }else{
                          str+= line;
                        }
                    }

                    myBufferedReader = new BufferedReader(new FileReader("./reports/reportParameter.sql"));
                    str = "";
                    while(myBufferedReader.ready()){
                        str += myBufferedReader.readLine().replaceAll(System.getProperty("line.separator"),"");

                    }
                    myBufferedReader.close();
                    EBISystem.getInstance().iDB().exec(str);
                    ebiMain.systemSetting.listName.report.showReports();
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_IMPORT_WAS_SUCCESSFULLY")).Show(EBIMessage.INFO_MESSAGE);

                }catch(final FileNotFoundException ex){
                  EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_FILE_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
                }catch(final IOException ex){
                  EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_FILE_NOT_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }catch(final SQLException ex){
                	ex.printStackTrace();
                }finally{
                   setVisible(false);
                }
            }
        };

        final Thread start = new Thread(run,"Install Reports");
        start.start();

    }

}
