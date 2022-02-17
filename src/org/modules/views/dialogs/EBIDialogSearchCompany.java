package org.modules.views.dialogs;

import org.modules.models.ModelSearchCompany;
import org.modules.utils.AbstractTableKeyAction;
import org.modules.utils.JTableActionMaps;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.gui.dialogs.EBIWinWaiting;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EBIDialogSearchCompany {

    public List<JComponent> jSetterComponent = null;
    public List<String> jSetterFieldName = null;
    public int jIndexer = 0;
    public int selRowSearch = 0;
    private ModelSearchCompany tableModel = null;
    private boolean isHierarchies = false;
    private boolean isSummary = false;
    final EBIWinWaiting wait = EBIWinWaiting.getInstance(EBISystem.i18n("EBI_LANG_LOAD_COMPANY_DATA"));

    /**
     * This is the default constructor
     */
    public EBIDialogSearchCompany(final boolean isH, final boolean isSummary) {
        isHierarchies = isH;
        this.isSummary = isSummary;
        jSetterFieldName = new ArrayList<String>();
        jSetterComponent = new ArrayList<JComponent>();
        tableModel = new ModelSearchCompany();
        EBISystem.builder().loadGUI("CRMDialog/crmCompanySearch.xml");
        setVisible();
    }

    /**
     * Show and Initialize the Company search dialog
     */

    private void setVisible() {
        EBISystem.builder().dialog("searchCRMCompany").setTitle(EBISystem.i18n("EBI_LANG_C_SEARCH_COMPANY"));
        EBISystem.builder().vpanel("searchCRMCompany").setModuleTitle(EBISystem.i18n("EBI_LANG_C_SEARCH_COMPANY"));

        EBISystem.builder().button("compSearchsearchButton", "searchCRMCompany").setText(EBISystem.i18n("EBI_LANG_SEARCH"));
        EBISystem.builder().button("compSearchsearchButton", "searchCRMCompany").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                searchCompany();
            }
        });

        EBISystem.builder().button("compSearchcancelButton", "searchCRMCompany").setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.builder().button("compSearchcancelButton", "searchCRMCompany").addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog("searchCRMCompany").setVisible(false);
            }
        });

        EBISystem.builder().button("compSearchapplyButton", "searchCRMCompany").setText(EBISystem.i18n("EBI_LANG_APPLY"));
        EBISystem.builder().button("compSearchapplyButton", "searchCRMCompany").setEnabled(false);
        EBISystem.builder().button("compSearchapplyButton", "searchCRMCompany").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                applySearch();
            }
        });

        final KeyAdapter adapt = new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(final java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchCompany();
                }
            }
        };

        EBISystem.builder().textField("filterTableText", "searchCRMCompany").addKeyListener(adapt);

        EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").setModel(tableModel);
        EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("compSearchapplyButton", "searchCRMCompany").setEnabled(false);
                } else {
                    if (lsm.getMinSelectionIndex() > 0) {
                        selRowSearch = EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").convertRowIndexToModel(lsm.getMinSelectionIndex());
                    }
                    if (!tableModel.data[selRowSearch][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        EBISystem.builder().button("compSearchapplyButton", "searchCRMCompany").setEnabled(true);
                    }
                }
            }
        });

        new JTableActionMaps(EBISystem.builder().table("searchCompanyTable", "searchCRMCompany")).setTableAction(new AbstractTableKeyAction() {

            @Override
            public void setArrowDownKeyAction(final int selRow) {
                selRowSearch = selRow;
            }

            @Override
            public void setArrowUpKeyAction(final int selRow) {
                selRowSearch = selRow;
            }

            @Override
            public void setEnterKeyAction(final int selRow) {
                selRowSearch = selRow;
                if (selRowSearch < 0 || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").
                        equals(tableModel.data[selRowSearch][0].toString())) {
                    return;
                }


                applySearch();
            }
        });

        EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {

                if (EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").rowAtPoint(e.getPoint()) != -1) {
                    selRowSearch = EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").convertRowIndexToModel(EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").rowAtPoint(e.getPoint()));
                }

                if (e.getClickCount() == 2) {
                    applySearch();
                }
            }
        });
        EBISystem.builder().showGUI();
    }

    private void searchCompany() {

        EBISystem.builder().dialog("searchCRMCompany").setCursor(new Cursor(Cursor.WAIT_CURSOR));

        ResultSet set = null;
        try {
            EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").setRowFilter(RowFilters.regexFilter("(?i)" + EBISystem.builder().textField("filterTableText", "searchCRMCompany").getText()));
            final PreparedStatement ps2 = EBISystem.getInstance().iDB().initPreparedStatement("SELECT COMPANYID,COMPANYNUMBER,CUSTOMERNR,BEGINCHAR,NAME,CATEGORY,COOPERATION,QUALIFICATION,ISLOCK FROM COMPANY");

            set = EBISystem.getInstance().iDB().executePreparedQuery(ps2);

            if (set != null) {
                set.last();
                if (set.getRow() > 0) {
                    tableModel.data = new Object[set.getRow()][8];
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        final String bChar = set.getString("BEGINCHAR") == null ? "" : set.getString("BEGINCHAR");
                        final String coNr = set.getString("COMPANYNUMBER") == null ? "" : set.getString("COMPANYNUMBER");

                        tableModel.data[i][0] = bChar + coNr;
                        tableModel.data[i][1] = set.getString("CUSTOMERNR") == null ? "" : set.getString("CUSTOMERNR");
                        tableModel.data[i][2] = set.getString("NAME") == null ? "" : set.getString("NAME");
                        tableModel.data[i][3] = set.getString("CATEGORY") == null ? "" : set.getString("CATEGORY");
                        tableModel.data[i][4] = set.getString("COOPERATION") == null ? "" : set.getString("COOPERATION");
                        tableModel.data[i][5] = set.getString("QUALIFICATION") == null ? "" : set.getString("QUALIFICATION");
                        tableModel.data[i][6] = set.getInt("ISLOCK") == 0 ? EBISystem.i18n("EBI_LANG_NO") : EBISystem.i18n("EBI_LANG_YES");
                        tableModel.data[i][7] = set.getString("COMPANYID");
                        i++;
                    }

                    tableModel.fireTableDataChanged();
                    EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").changeSelection(0, 0, false, false);
                    EBISystem.builder().table("searchCompanyTable", "searchCRMCompany").requestFocus();

                } else {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_C_ERROR_NO_COMPANY_FOUND")).Show(EBIMessage.INFO_MESSAGE);
                }
            }
            EBISystem.builder().dialog("searchCRMCompany").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (final Exception ex) {
            ex.printStackTrace();
        } finally {

            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void applySearch() {

        if ((tableModel.data[0][0] == null
                || EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(tableModel.data[0][0].toString()))
                || !EBISystem.getInstance().getIEBISecurityInstance().checkCanReleaseModules()) {
            return;
        }

        EBISystem.builder().dialog("searchCRMCompany").setVisible(false);
        wait.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (isHierarchies == false && isSummary == false) {
                                EBISystem.getModule().createUI(Integer.parseInt(tableModel.data[selRowSearch][7].toString()), false);
                            } else if (isHierarchies == true) {
                                EBISystem.getModule().getCompanyPane().setHierarchies(tableModel.data[selRowSearch][1].toString(), Integer.parseInt(tableModel.data[selRowSearch][7].toString()));
                            } else if (isSummary) {
                                EBISystem.getModule().getSummaryPane().setCompanyText(tableModel.data[selRowSearch][1].toString());
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            wait.setVisible(false);
                        }
                    }
                });
            }
        }).start();
    }
}