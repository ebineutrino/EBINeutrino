package org.modules.views.dialogs;

import org.modules.models.ModelOpportunity;
import org.sdk.EBISystem;
import org.sdk.model.hibernate.Companyopportunity;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Set;

public class EBIOpportunitySelectionDialog {

    public ModelOpportunity tabModel = null;
    private Set<Companyopportunity> opList = null;
    public int selRow = -1;
    public boolean shouldSave = false;
    public String name = "";
    public int id = 0;

    
    public EBIOpportunitySelectionDialog(final Set<Companyopportunity> opList) {
        tabModel = new ModelOpportunity();
        this.opList = opList;

        EBISystem.gui().loadGUI("CRMDialog/crmSelectionDialog.xml");

        showCollectionList();
    }

    public void setVisible(){
        EBISystem.gui().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY_LIST"));
        EBISystem.gui().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_OPPORTUNITY_LIST"));
        
        
        EBISystem.gui().textField("filterTableText","abstractSelectionDialog").addKeyListener(new KeyListener(){
            @Override
			public void keyTyped(final KeyEvent e){}

            @Override
			public void keyPressed(final KeyEvent e){
                EBISystem.gui().table("abstractTable","abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)"+EBISystem.gui().textField("filterTableText","abstractSelectionDialog").getText()));
            }
            @Override
			public void keyReleased(final KeyEvent e){
                EBISystem.gui().table("abstractTable","abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)"+EBISystem.gui().textField("filterTableText","abstractSelectionDialog").getText()));
            }
          }); 
        
        
        EBISystem.gui().table("abstractTable","abstractSelectionDialog").setModel(tabModel);
        EBISystem.gui().table("abstractTable","abstractSelectionDialog").setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        EBISystem.gui().table("abstractTable","abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
				public void valueChanged(final ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if(lsm.getMinSelectionIndex() != -1){
                        selRow = EBISystem.gui().table("abstractTable","abstractSelectionDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                    }
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.gui().button("applyButton","abstractSelectionDialog").setEnabled(false);
                        selRow = -1;
                    } else if (!tabModel.getRow(0)[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        selRow = lsm.getMinSelectionIndex();
                        EBISystem.gui().button("applyButton","abstractSelectionDialog").setEnabled(true);
                    }
                }
            });
            EBISystem.gui().table("abstractTable","abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
				public void mouseClicked(final java.awt.event.MouseEvent e) {
                   if(EBISystem.gui().table("abstractTable","abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1){
                        selRow = EBISystem.gui().table("abstractTable","abstractSelectionDialog").convertRowIndexToModel(EBISystem.gui().table("abstractTable","abstractSelectionDialog").rowAtPoint(e.getPoint()));

                        if (e.getClickCount() == 2) {

                            if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                                    equals(tabModel.data[selRow][0].toString())) {
                                return;
                            }

                            EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
                            copyCollection(selRow);
                            shouldSave = true;

                        }
                   }
                }
            });

          EBISystem.gui().button("applyButton","abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_APPLY"));
          EBISystem.gui().button("applyButton","abstractSelectionDialog").setEnabled(false);
          EBISystem.gui().button("applyButton","abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selRow][0].toString())) {
                        return;
                    }
                    copyCollection(selRow);
                    shouldSave = true;
                    EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);

                }
            });

         EBISystem.gui().button("closeButton","abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
         EBISystem.gui().button("closeButton","abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
                }
            });

         EBISystem.gui().showGUI();
    }

    private void copyCollection(final int row) {

        final Iterator itr = this.opList.iterator();
        while (itr.hasNext()) {

            final Companyopportunity obj = (Companyopportunity) itr.next();

            if (Integer.parseInt(tabModel.data[row][7].toString()) == obj.getOpportunityid()) {
                this.name = obj.getName();
                this.id = obj.getOpportunityid();
                break;
            }
        }
    }

    private void showCollectionList() {
      if(this.opList.size() > 0){
        tabModel.data = new Object[this.opList.size()][8];

        final Iterator itr = this.opList.iterator();
        int i = 0;
        while (itr.hasNext()) {
            final Companyopportunity obj = (Companyopportunity) itr.next();
   
            tabModel.data[i][0] = obj.getName() == null ? "" : obj.getName();
            tabModel.data[i][1] = obj.getSalestage() == null ? "" : obj.getSalestage();
            tabModel.data[i][2] = obj.getProbability() == null ? "" : obj.getProbability();
            tabModel.data[i][3] = obj.getBusinesstype() == null ? "" : obj.getBusinesstype();
            tabModel.data[i][4] = obj.getOpportunityvalue() == null ? 0.0 : obj.getOpportunityvalue();

            String isClose = "";
            if (obj.getIsclose() != null) {
                if (obj.getIsclose() == false) {
                    isClose = EBISystem.i18n("EBI_LANG_NO");
                } else {
                    isClose = EBISystem.i18n("EBI_LANG_YES");
                }
            }
            tabModel.data[i][5] = isClose;
            tabModel.data[i][6] = obj.getClosedate() == null ? "" : obj.getClosedate();
            tabModel.data[i][7] = obj.getOpportunityid();
            i++;
        }
      }else{
         tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
      }
      tabModel.fireTableDataChanged();

    }
}