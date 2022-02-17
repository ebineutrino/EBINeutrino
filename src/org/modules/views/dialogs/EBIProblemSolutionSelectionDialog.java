package org.modules.views.dialogs;

import org.modules.models.ModelProblemSolution;
import org.sdk.EBISystem;
import org.sdk.model.hibernate.Companyservicepsol;
import org.sdk.model.hibernate.Crmproblemsolutions;
import org.hibernate.query.Query;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EBIProblemSolutionSelectionDialog {

    public ModelProblemSolution tabModel = null;
    public Set<Crmproblemsolutions> crmSolutionList = new HashSet<Crmproblemsolutions>();
    public Set<Companyservicepsol> serviceSolutionList = null;
    public int selRow = -1;

    public EBIProblemSolutionSelectionDialog(final Set<Companyservicepsol> ssolList) {
        super();

        tabModel = new ModelProblemSolution();
        serviceSolutionList = ssolList;

        EBISystem.builder().loadGUI("CRMDialog/crmSelectionDialog.xml");

        showCollectionList();
    }

    public void setVisible() {
        EBISystem.builder().dialog("abstractSelectionDialog").setTitle(EBISystem.i18n("EBI_LANG_C_PROBLEMSOLUTION_DATA"));
        EBISystem.builder().vpanel("abstractSelectionDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_PROBLEMSOLUTION_DATA"));

        EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }

            @Override
            public void keyPressed(final KeyEvent e) {
                EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").getText()));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                EBISystem.builder().table("abstractTable", "abstractSelectionDialog").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "abstractSelectionDialog").getText()));
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
                if (lsm.getMinSelectionIndex() != -1) {
                    selRow = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(lsm.getMinSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("applyButton", "abstractSelectionDialog").setEnabled(false);
                    selRow = -1;
                } else if (!tabModel.getRow(0)[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    selRow = lsm.getMinSelectionIndex();
                    EBISystem.builder().button("applyButton", "abstractSelectionDialog").setEnabled(true);
                }
            }
        });
        EBISystem.builder().table("abstractTable", "abstractSelectionDialog").addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                if (EBISystem.builder().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()) != -1) {
                    selRow = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(EBISystem.builder().table("abstractTable", "abstractSelectionDialog").rowAtPoint(e.getPoint()));
                } else {
                    return;
                }
                if (e.getClickCount() == 2) {

                    if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                            equals(tabModel.data[selRow][0].toString())) {
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
                if (selRow < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tabModel.data[selRow][0].toString())) {
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

    private void copyCollection(final int[] id) {

        if (crmSolutionList != null) {
            int i = 0;
            final Iterator iter = this.crmSolutionList.iterator();
            while (iter.hasNext()) {
                final Crmproblemsolutions solution = (Crmproblemsolutions) iter.next();
                if (solution.getProsolid() == id[i]) {
                    final Companyservicepsol csol = new Companyservicepsol();
                    csol.setProsolid((serviceSolutionList.size() + 1) * -1);
                    csol.setCompanyservice(EBISystem.getModule().getServicePane().getDataControlService().getcompService());
                    csol.setSolutionnr(solution.getServicenr());
                    csol.setName(solution.getName());
                    csol.setClassification(solution.getClassification());
                    csol.setCategory(solution.getCategory());
                    csol.setType(solution.getType());
                    csol.setStatus(solution.getStatus());
                    csol.setDescription(solution.getDescription());
                    csol.setCreateddate(solution.getCreateddate());
                    csol.setCreatedfrom(solution.getCreatedfrom());
                    csol.setChangeddate(solution.getChangeddate());
                    csol.setChangedfrom(solution.getChangedfrom());
                    serviceSolutionList.add(csol);
                    i++;
                }
            }
        }
    }

    private void fillCollection() {
        final int[] rows = EBISystem.builder().table("abstractTable", "abstractSelectionDialog").getSelectedRows();
        final int[] id = new int[rows.length + 1];
        for (int i = 0; i < rows.length; i++) {
            id[i] = Integer.parseInt(tabModel.data[EBISystem.builder().table("abstractTable", "abstractSelectionDialog").convertRowIndexToModel(rows[i])][7].toString());
        }
        copyCollection(id);
    }

    private void showCollectionList() {

        try {

            final Query query = EBISystem.hibernate().session("EBICRM_SESSION").createQuery("FROM Crmproblemsolutions");
            if (query.list().size() > 0) {
                tabModel.data = new Object[query.list().size()][8];

                final Iterator it = query.iterate();
                int i = 0;

                while (it.hasNext()) {
                    final Crmproblemsolutions comps = (Crmproblemsolutions) it.next();
                    EBISystem.hibernate().session("EBICRM_SESSION").refresh(comps);
                    tabModel.data[i][0] = comps.getServicenr() == null ? "" : comps.getServicenr();
                    tabModel.data[i][1] = comps.getName() == null ? "" : comps.getName();
                    tabModel.data[i][2] = comps.getClassification() == null ? "" : comps.getClassification();
                    tabModel.data[i][3] = comps.getCategory() == null ? "" : comps.getCategory();
                    tabModel.data[i][4] = comps.getType() == null ? "" : comps.getType();
                    tabModel.data[i][5] = comps.getStatus() == null ? "" : comps.getStatus();
                    tabModel.data[i][6] = comps.getDescription() == null ? "" : comps.getDescription();
                    tabModel.data[i][7] = comps.getProsolid();
                    crmSolutionList.add(comps);
                    i++;
                }
            } else {
                tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", ""}};
            }
        } catch (final org.hibernate.HibernateException ex) {
            return;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        tabModel.fireTableDataChanged();
    }
}
