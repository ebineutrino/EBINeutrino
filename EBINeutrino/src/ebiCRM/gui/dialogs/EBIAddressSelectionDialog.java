package ebiCRM.gui.dialogs;

import ebiCRM.table.models.MyTableModelCRMAddress;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.model.hibernate.Companyaddress;
import ebiNeutrinoSDK.model.hibernate.Companycontactaddress;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Set;


public class EBIAddressSelectionDialog {

    private MyTableModelCRMAddress tabModel = null;
    private Set<Companyaddress> caddressList = null;
    private Set<Companycontactaddress> coaddressList = null;
    private int selRow = -1;


    public EBIAddressSelectionDialog(final Set<Companyaddress> clist, final Set<Companycontactaddress> colist) {
        tabModel = new MyTableModelCRMAddress();
        caddressList = clist;
        coaddressList = colist;

        EBISystem.gui().loadGUI("CRMDialog/crmSelectionDialog.xml");
        showCollectionList();
    }

    public void setVisible() {
        EBISystem.gui().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_ADRESS_DATA"));
        EBISystem.gui().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_ADRESS_DATA"));

        EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").addKeyListener(new KeyListener() {
            @Override
			public void keyTyped(final KeyEvent e) {
            }

            @Override
			public void keyPressed(final KeyEvent e) {
                EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").getText()));
            }

            @Override
			public void keyReleased(final KeyEvent e) {
                EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.gui().textField("filterTableText", "abstractSelectionDialog").getText()));
            }
        });

        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setModel(tabModel);
        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
			public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getMinSelectionIndex() != -1) {
                    selRow = EBISystem.gui().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.gui().button("applyButton", "abstractSelectionDialog").setEnabled(false);
                    selRow = -1;
                } else if (!tabModel.getRow(0)[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    selRow = lsm.getMinSelectionIndex();
                    EBISystem.gui().button("applyButton", "abstractSelectionDialog").setEnabled(true);
                }
            }
        });

        EBISystem.gui().table("abstractTable", "abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.gui().
                        table("abstractTable",
                                "abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1) {

                    selRow = EBISystem.gui().table("abstractTable", "abstractSelectionDialog")
                            .convertRowIndexToModel(EBISystem.gui().
                                    table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()));
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

        EBISystem.gui().button("applyButton", "abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_APPLY"));
        EBISystem.gui().button("applyButton", "abstractSelectionDialog").setEnabled(false);
        EBISystem.gui().button("applyButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

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

        EBISystem.gui().button("closeButton", "abstractSelectionDialog").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.gui().button("closeButton", "abstractSelectionDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
			public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog("abstractSelectionDialog").setVisible(false);
            }
        });

        EBISystem.gui().showGUI();
    }

    private void copyCollection(final int[] id) {

        if (caddressList != null) {
            int i = 0;
            final Iterator iter = this.caddressList.iterator();
            while (iter.hasNext()) {
                final Companyaddress address = (Companyaddress) iter.next();
                if (address.getAddressid() == id[i]) {
                    final Companycontactaddress addr = new Companycontactaddress();
                    addr.setAddressid((coaddressList.size() + 1)  * -1);
                    addr.setAddresstype(address.getAddresstype());
                    addr.setCountry(address.getCountry());
                    addr.setStreet(address.getStreet());
                    addr.setZip(address.getZip());
                    addr.setLocation(address.getLocation());
                    addr.setPbox(address.getPbox());
                    addr.setCreateddate(address.getCreateddate());
                    addr.setCreatedfrom(address.getCreatedfrom());
                    addr.setChangeddate(address.getChangeddate());
                    addr.setChangedfrom(address.getChangedfrom());
                    coaddressList.add(addr);
                    i++;
                }
            }
            EBISystem.getCRMModule().getContactPane().controlContact.showCompanyContactAddress();
        }
    }

    private void fillCollection() {
        final int[] rows = EBISystem.gui().table("abstractTable", "abstractSelectionDialog").getSelectedRows();
        final int[] id = new int[rows.length + 1];
        for (int i = 0; i < rows.length; i++) {
            id[i] = Integer.parseInt(tabModel.data[EBISystem.gui().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(rows[i])][6].toString());
        }
        copyCollection(id);
    }

    private void showCollectionList() {
        if (this.caddressList.size() > 0) {
            tabModel.data = new Object[this.caddressList.size()][7];

            final Iterator itr = this.caddressList.iterator();

            int i = 0;
            while (itr.hasNext()) {

                final Companyaddress obj = (Companyaddress) itr.next();

                tabModel.data[i][0] = obj.getAddresstype() == null ? "" : obj.getAddresstype();
                tabModel.data[i][1] = obj.getStreet() == null ? "" : obj.getStreet();
                tabModel.data[i][2] = obj.getZip() == null ? "" : obj.getZip();
                tabModel.data[i][3] = obj.getLocation() == null ? "" : obj.getLocation();
                tabModel.data[i][4] = obj.getPbox() == null ? "" : obj.getPbox();
                tabModel.data[i][5] = obj.getCountry() == null ? "" : obj.getCountry();
                tabModel.data[i][6] = obj.getAddressid();

                i++;
            }
        } else {
            tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", ""}};
        }
        tabModel.fireTableDataChanged();
    }
}