package ebiCRM.gui.dialogs;

import ebiCRM.EBICRMModule;
import ebiCRM.table.models.MyTableModelCRMContact;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.model.hibernate.*;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Set;

public class EBIContactSelectionDialog  {

    private EBICRMModule ebiModule = null;
    private MyTableModelCRMContact tabModel = null;
    private Set<Companycontacts> contactList = null;
    private Companymeetingprotocol meetingProtocol = null;
    private Companyopportunity opportunity = null;
    private boolean isOpportunity  = false;
    private int selRow = -1;

    
    public EBIContactSelectionDialog(final EBICRMModule module, final Set<Companycontacts> list, final Companymeetingprotocol meetpro) {
        super();
        ebiModule = module;
        EBISystem.gui().loadGUI("CRMDialog/crmSelectionDialog.xml");

        tabModel = new MyTableModelCRMContact();
        contactList = list;
        meetingProtocol = meetpro;
        isOpportunity = false;
        showCollectionList();
    }

    public EBIContactSelectionDialog(final EBICRMModule module, final Set<Companycontacts> list, final Companyopportunity opport) {
        super();
        ebiModule = module;

        EBISystem.gui().loadGUI("CRMDialog/crmSelectionDialog.xml");

        tabModel = new MyTableModelCRMContact();
        contactList = list;
        this.opportunity = opport;
        isOpportunity = true;
        showCollectionList();
    }

    public void setVisible(){
        EBISystem.gui().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CONTACT_LIST"));
        EBISystem.gui().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CONTACT_LIST"));

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
                    }
                    if (e.getClickCount() == 2) {

                        if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                                equals(tabModel.data[selRow][0].toString())) {
                            return;
                        }
                        EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
                        fillCollection();

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
                    fillCollection();
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

    private void fillCollection() {

        final int[] rows = EBISystem.gui().table("abstractTable","abstractSelectionDialog").getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
          copyCollection(Integer.parseInt(tabModel.data[EBISystem.gui().table("abstractTable","abstractSelectionDialog").convertRowIndexToModel(rows[i])][6].toString()));
        }
    }

    private void copyCollection(final int id) {


        if (!this.contactList.isEmpty()) {
          if(!isOpportunity){
            final Iterator iter = this.contactList.iterator();

            while (iter.hasNext()) {

                final Companycontacts act = (Companycontacts) iter.next();

                if (act.getContactid() == id) {
                    try{
                        EBISystem.hibernate().transaction("EBICRM_SESSION").begin();
                        final Companymeetingcontacts contact = new Companymeetingcontacts();
                        contact.setCompanymeetingprotocol(meetingProtocol);
                        contact.setCreateddate(act.getCreateddate());
                        contact.setCreatedfrom(act.getCreatedfrom());
                        contact.setChangeddate(act.getChangeddate());
                        contact.setChangedfrom(act.getChangedfrom());
                        contact.setGender(act.getGender());
                        contact.setPosition(act.getPosition());
                        contact.setSurname(act.getSurname());
                        contact.setName(act.getName());
                        contact.setBirddate(act.getBirddate());
                        contact.setPhone(act.getPhone());
                        contact.setFax(act.getFax());
                        contact.setMobile(act.getMobile());
                        contact.setEmail(act.getEmail());
                        contact.setDescription(act.getDescription());
                        this.meetingProtocol.getCompanymeetingcontactses().add(contact);
                        EBISystem.getCRMModule().getMeetingProtocol().dataMeetingControl.dataShowContact();
                    }catch(final Exception e){
                        e.printStackTrace();
                    }

                }

            }
        } else {

            final Iterator iter = this.contactList.iterator();
            while (iter.hasNext()) {

                final Companycontacts act1 = (Companycontacts) iter.next();

                if (act1.getContactid() == id) {
                    final Companyopportunitycontact contact1 = new Companyopportunitycontact();
                    contact1.setCompanyopportunity(opportunity);
                    contact1.setCreateddate(act1.getCreateddate());
                    contact1.setCreatedfrom(act1.getCreatedfrom());
                    contact1.setChangeddate(act1.getChangeddate());
                    contact1.setChangedfrom(act1.getChangedfrom());
                    contact1.setGender(act1.getGender());
                    contact1.setPosition(act1.getPosition());
                    contact1.setSurname(act1.getSurname());
                    contact1.setName(act1.getName());
                    contact1.setBirddate(act1.getBirddate());
                    contact1.setPhone(act1.getPhone());
                    contact1.setFax(act1.getFax());
                    contact1.setMobile(act1.getMobile());
                    contact1.setEmail(act1.getEmail());
                    contact1.setDescription(act1.getDescription());
                    this.opportunity.getCompanyopportunitycontacts().add(contact1);
                    EBISystem.getCRMModule().getOpportunityPane().dataOpportuniyControl.showOpportunityContacts();
                }
            }
          }
        }
    }

    private void showCollectionList() {

        tabModel.data = new Object[this.contactList.size()][9];
           if(this.contactList.size() > 0){
                final Iterator itr = this.contactList.iterator();
                int i = 0;
                while (itr.hasNext()) {
                    final Companycontacts obj = (Companycontacts) itr.next();
                    tabModel.data[i][0] = obj.getPosition() == null ? "" : obj.getPosition();
                    tabModel.data[i][1] = obj.getGender() == null ? "" : obj.getGender();
                    tabModel.data[i][2] = obj.getSurname() == null ? "" : obj.getSurname();
                    tabModel.data[i][3] = obj.getName() == null ? "" : obj.getName();
                    tabModel.data[i][4] = obj.getPhone() == null ? "" : obj.getPhone();
                    tabModel.data[i][5] = obj.getMobile() == null ? "" : obj.getMobile();
                    tabModel.data[i][6] = obj.getMobile() == null ? "" : obj.getMobile();
                    tabModel.data[i][7] = obj.getMobile() == null ? "" : obj.getMobile();
                    tabModel.data[i][8] = obj.getContactid();
                    i++;
                }
           }else{
              tabModel.data = new Object[][] {{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "","","" ,""}};
           }
        tabModel.fireTableDataChanged();
    }
}