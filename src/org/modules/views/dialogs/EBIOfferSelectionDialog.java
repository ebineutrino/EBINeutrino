package org.modules.views.dialogs;

import org.modules.models.ModelOffer;
import org.sdk.EBISystem;
import org.sdk.model.hibernate.Companyoffer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Iterator;
import java.util.Set;

public class EBIOfferSelectionDialog  {
    
    public ModelOffer tabModel = null;
    private Set<Companyoffer> opList = null;
    public int selRow = -1;
    public boolean shouldSave = false;
    public String name = "";
    public int id = 0;

    
    public EBIOfferSelectionDialog(final Set<Companyoffer> opList) {
        tabModel = new ModelOffer();
        this.opList = opList;

        EBISystem.builder().loadGUI("CRMDialog/crmSelectionDialog.xml");
        
        showCollectionList();

    }

    public void setVisible(){
        EBISystem.builder().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_OFFER_LIST"));
        EBISystem.builder().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_OFFER_LIST"));
        EBISystem.builder().table("abstractTable","abstractSelectionDialog").setModel(tabModel);
        EBISystem.builder().table("abstractTable","abstractSelectionDialog").setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        EBISystem.builder().table("abstractTable","abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
				public void valueChanged(final ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if(lsm.getLeadSelectionIndex() != -1){
                        selRow = EBISystem.builder().table("abstractTable","abstractSelectionDialog").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                    }
                    if (lsm.isSelectionEmpty()) {
                        EBISystem.builder().button("applyButton","abstractSelectionDialog").setEnabled(false);
                        selRow = -1;
                    } else if (!tabModel.getRow(0)[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        selRow = lsm.getLeadSelectionIndex();
                        EBISystem.builder().button("applyButton","abstractSelectionDialog").setEnabled(true);
                    }
                }
            });
            EBISystem.builder().table("abstractTable","abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
				public void mouseClicked(final java.awt.event.MouseEvent e) {
                   if(EBISystem.builder().table("abstractTable","abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1){
                    selRow = EBISystem.builder().table("abstractTable","abstractSelectionDialog").convertRowIndexToModel(EBISystem.builder().table("abstractTable","abstractSelectionDialog").rowAtPoint(e.getPoint()));
                   }

                    if (e.getClickCount() == 2) {

                        if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                                equals(tabModel.data[selRow][0].toString())) {
                            return;
                        }

                        EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);
                        copyCollection(selRow);
                        shouldSave = true;

                    }
                }
            });

          EBISystem.builder().button("applyButton","abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_APPLY"));
          EBISystem.builder().button("applyButton","abstractSelectionDialog").setEnabled(false);
          EBISystem.builder().button("applyButton","abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selRow][0].toString())) {
                        return;
                    }
                    copyCollection(selRow);
                    shouldSave = true;
                    EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);

                }
            });

         EBISystem.builder().button("closeButton","abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
         EBISystem.builder().button("closeButton","abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

                @Override
				public void actionPerformed(final java.awt.event.ActionEvent e) {
                    EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);
                }
            });

         EBISystem.builder().showGUI();
    }

    private void copyCollection(final int row) {
        final Iterator itr = this.opList.iterator();
        while (itr.hasNext()) {

            final Companyoffer obj = (Companyoffer) itr.next();

            if (Integer.parseInt(tabModel.data[selRow][6].toString()) == obj.getOfferid()) {
                this.name = obj.getName();
                this.id = obj.getOfferid();
                break;
            }
        }
    }

    private void showCollectionList() {
       if(this.opList.size() > 0){
            tabModel.data = new Object[this.opList.size()][7];

            final Iterator itr = this.opList.iterator();
            int i = 0;
            while (itr.hasNext()) {
                final Companyoffer obj = (Companyoffer) itr.next();

                tabModel.data[i][0] = obj.getName() == null ? "" : obj.getName();
                tabModel.data[i][1] = obj.getOfferdate() == null ? "" : obj.getOfferdate();
                tabModel.data[i][2] = obj.getValidto() == null ? "" : EBISystem.getInstance().getDateToString(obj.getValidto());
                tabModel.data[i][3] = obj.getOpportunityid() == null ? 0 : obj.getOpportunityid();
                tabModel.data[i][4] = obj.getStatus() == null ? "" : obj.getStatus();
                tabModel.data[i][5] = obj.getDescription() == null ? "" : obj.getDescription();
                tabModel.data[i][6] = obj.getOfferid();

                i++;
            }
       }else{
          tabModel.data = new Object[][] {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", ""}};
       }
        tabModel.fireTableDataChanged();
    }
}