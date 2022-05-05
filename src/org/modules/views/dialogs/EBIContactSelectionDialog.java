package org.modules.views.dialogs;

import org.sdk.model.hibernate.Companymeetingprotocol;
import org.sdk.model.hibernate.Companyopportunitycontact;
import org.sdk.model.hibernate.Companyopportunity;
import org.sdk.model.hibernate.Companycontacts;
import org.sdk.model.hibernate.Companymeetingcontacts;
import org.modules.EBIModule;
import org.modules.models.ModelCRMContact;
import org.sdk.EBISystem;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.List;

public class EBIContactSelectionDialog {

    private EBIModule ebiModule = null;
    private ModelCRMContact tabModel = null;
    private List<Companycontacts> contactList = null;
    private Companymeetingprotocol meetingProtocol = null;
    private Companyopportunity opportunity = null;
    private boolean isOpportunity = false;
    private int selRow = -1;

    public EBIContactSelectionDialog(final EBIModule module, final List<Companycontacts> list, final Companymeetingprotocol meetpro) {
        super();
        ebiModule = module;
        EBISystem.builder().loadGUI("CRMDialog/crmSelectionDialog.xml");

        tabModel = new ModelCRMContact(ModelCRMContact.MEETING_CONTACT);
        contactList = list;
        meetingProtocol = meetpro;
        isOpportunity = false;
        showCollectionList();
    }

    public EBIContactSelectionDialog(final EBIModule module, final List<Companycontacts> list, final Companyopportunity opport) {
        super();
        ebiModule = module;

        EBISystem.builder().loadGUI("CRMDialog/crmSelectionDialog.xml");

        tabModel = new ModelCRMContact(ModelCRMContact.OPPORTUNITY_CONTACT);
        contactList = list;
        this.opportunity = opport;
        isOpportunity = true;
        showCollectionList();
    }

    public void setVisible() {
        EBISystem.builder().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CONTACT_LIST"));
        EBISystem.builder().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CONTACT_LIST"));

        EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.
                        regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.
                        regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").getText()));
            }
        });

        EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setModel(tabModel);
        EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        EBISystem.builder().table("abstractTable", "abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getLeadSelectionIndex() != -1) {
                    selRow = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("applyButton", "abstractSelectionDialog").setEnabled(false);
                    selRow = -1;
                } else if (!"".equals(tabModel.getValueAt(0,0).toString())) {
                    selRow = lsm.getLeadSelectionIndex();
                    EBISystem.builder().button("applyButton", "abstractSelectionDialog").setEnabled(true);
                }
            }
        });
        EBISystem.builder().table("abstractTable", "abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1) {
                    selRow = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(EBISystem.builder().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()));
                }
                if (e.getClickCount() == 2) {

                    if (selRow < 0 || "".
                        equals(tabModel.getValueAt(selRow, 0))) {
                        return;
                    }
                    EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);
                    fillCollection();

                }
            }
        });

        EBISystem.builder().button("applyButton", "abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_APPLY"));
        EBISystem.builder().button("applyButton", "abstractSelectionDialog").setEnabled(false);
        EBISystem.builder().button("applyButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (selRow < 0 || "".
                        equals(tabModel.getValueAt(selRow, 0))) {
                    return;
                }
                fillCollection();
                EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);
            }
        });

        EBISystem.builder().button("closeButton", "abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.builder().button("closeButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("abstractSelectionDialog").setVisible(false);
            }
        });
        EBISystem.builder().showGUI();
    }

    private void fillCollection() {
        final int[] rows = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            copyCollection(tabModel.getId(EBISystem.builder().table("abstractTable", 
                    "abstractSelectionDialog").convertRowIndexToModel(rows[i])));
        }
    }

    private void copyCollection(final int id) {

        if (!this.contactList.isEmpty()) {
            if (!isOpportunity) {
                final Iterator iter = this.contactList.iterator();

                while (iter.hasNext()) {

                    final Companycontacts act = (Companycontacts) iter.next();

                    if (act.getContactid() == id) {
                        try {
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
                            EBISystem.getModule().getMeetingProtocol().getDataMeetingControl().dataShowContact();
                        } catch (final Exception e) {
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
                        EBISystem.getModule().getOpportunityPane().getDataOpportuniyControl().showOpportunityContacts();
                    }
                }
            }
        }
    }

    private void showCollectionList() {
        tabModel.setAvailableContacts(this.contactList);
        tabModel.fireTableDataChanged();
    }
}
