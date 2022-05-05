package org.modules.views.dialogs;

import org.modules.views.EBICRMProductView;
import org.modules.models.TaxAdministration;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.sdk.model.hibernate.Companyproducttax;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class EBIDialogTaxAdministration {

    private TaxAdministration tabModel = null;
    private Companyproducttax crmTax = null;
    private boolean isEdit = false;
    private int selRow = -1;

    public EBIDialogTaxAdministration() {
        tabModel = new TaxAdministration();
        EBISystem.builder().loadGUI("CRMDialog/taxAdminDialog.xml");

        initialize();
        setProductTax();
        showTax();
    }

    private void initialize() {
        try {
            EBISystem.hibernate().openHibernateSession("EBITAX_SESSION");
        } catch (final Exception e) {
            e.printStackTrace();
        }

        crmTax = new Companyproducttax();
    }

    public void setVisible() {

        EBISystem.builder().dialog("taxAdminDialog").setTitle(EBISystem.i18n("EBI_LANG_C_CRM_TAX_ADMINISTRATION"));
        EBISystem.builder().vpanel("taxAdminDialog").setModuleTitle(EBISystem.i18n("EBI_LANG_C_CRM_TAX_ADMINISTRATION"));
        EBISystem.builder().label("tax", "taxAdminDialog").setText(EBISystem.i18n("EBI_LANG_TAX_TYPE"));
        EBISystem.builder().label("value", "taxAdminDialog").setText(EBISystem.i18n("EBI_LANG_TAX_VALUE"));

        EBISystem.builder().button("saveValue", "taxAdminDialog").setText(EBISystem.i18n("EBI_LANG_SAVE"));
        EBISystem.builder().button("saveValue", "taxAdminDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!validateInput()) {
                    return;
                }
                saveTax();
            }
        });

        EBISystem.builder().button("newBnt", "taxAdminDialog").setIcon(EBISystem.getInstance().getIconResource("new.png"));
        EBISystem.builder().button("newBnt", "taxAdminDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                newTax();
            }
        });

        EBISystem.builder().button("editBnt", "taxAdminDialog").setEnabled(false);
        EBISystem.builder().button("editBnt", "taxAdminDialog").setIcon(EBISystem.getInstance().getIconResource("down.png"));
        EBISystem.builder().button("editBnt", "taxAdminDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                editTax(EBISystem.builder().table("taxValueTable", "taxAdminDialog").getSelectedRow());
            }
        });

        EBISystem.builder().button("deleteBnt", "taxAdminDialog").setEnabled(false);
        EBISystem.builder().button("deleteBnt", "taxAdminDialog").setIcon(EBISystem.getInstance().getIconResource("delete.png"));
        EBISystem.builder().button("deleteBnt", "taxAdminDialog").addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                deleteTax(EBISystem.builder().table("taxValueTable", "taxAdminDialog").getSelectedRow());
            }
        });

        EBISystem.builder().combo("taxCombo", "taxAdminDialog").setModel(new DefaultComboBoxModel(EBICRMProductView.taxType));
        EBISystem.builder().table("taxValueTable", "taxAdminDialog").setModel(tabModel);
        EBISystem.builder().table("taxValueTable", "taxAdminDialog").getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.getLeadSelectionIndex() != -1) {
                    selRow = EBISystem.builder().table("taxValueTable", "taxAdminDialog").convertRowIndexToModel(lsm.getLeadSelectionIndex());
                }
                if (lsm.isSelectionEmpty()) {
                    EBISystem.builder().button("editBnt", "taxAdminDialog").setEnabled(false);
                    EBISystem.builder().button("deleteBnt", "taxAdminDialog").setEnabled(false);
                } else if (!tabModel.getRow(EBISystem.builder().table("taxValueTable", "taxAdminDialog").getSelectedRow())[0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().button("editBnt", "taxAdminDialog").setEnabled(true);
                    EBISystem.builder().button("deleteBnt", "taxAdminDialog").setEnabled(true);
                }
            }
        });

        EBISystem.builder().table("taxValueTable", "taxAdminDialog").addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                selRow = EBISystem.builder().table("taxValueTable", "taxAdminDialog").convertRowIndexToModel(EBISystem.builder().table("taxValueTable", "taxAdminDialog").rowAtPoint(e.getPoint()));
                if (e.getClickCount() == 2) {

                    if (!tabModel.data[selRow][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                        editTax(selRow);
                    }
                }
            }
        });

        EBISystem.builder().textField("taxValue", "taxAdminDialog").addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (!validateInput()) {
                        return;
                    }
                    saveTax();
                }
            }
        });

        EBISystem.builder().showGUI();
    }

    private void setProductTax() {
        PreparedStatement ps = null;
        ResultSet set = null;
        try {
            EBISystem.builder().combo("taxCombo", "taxAdminDialog").removeAllItems();
            ps = EBISystem.db().initPreparedStatement("SELECT NAME FROM COMPANYPRODUCTTAX order by NAME");
            set = ps.executeQuery();

            if (set != null) {
                set.last();
                final int size = set.getRow();
                if (size > 0) {
                    set.beforeFirst();
                    EBISystem.builder().combo("taxCombo", "taxAdminDialog").addItem(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"));
                    while (set.next()) {
                        EBISystem.builder().combo("taxCombo", "taxAdminDialog").addItem(set.getString("NAME"));
                    }
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                set.close();
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInput() {
        if (EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.builder().combo("taxCombo", "taxAdminDialog").getEditor().getItem().toString())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_SELECT_TAX_TYPE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }

        if (!isEdit) {
            for (int i = 0; i < tabModel.data.length; i++) {
                if (tabModel.data[i][0].toString().toLowerCase().equals(EBISystem.builder().combo("taxCombo", "taxAdminDialog").getEditor().getItem().toString().toLowerCase())) {
                    EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_ERROR_SAME_RECORD_EXSIST")).Show(EBIMessage.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        if ("".equals(EBISystem.builder().textField("taxValue", "taxAdminDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_TAX_VALUE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void newTax() {
        isEdit = false;
        this.crmTax = new Companyproducttax();
        EBISystem.builder().textField("taxValue", "taxAdminDialog").setText("");
        EBISystem.builder().combo("taxCombo", "taxAdminDialog").setSelectedIndex(0);
        showTax();
    }

    private void saveTax() {
        try {
            if (!EBISystem.hibernate().transaction("EBITAX_SESSION").isActive()) {
                EBISystem.hibernate().transaction("EBITAX_SESSION").begin();
            }
            
            String taxName = EBISystem.builder().combo("taxCombo", "taxAdminDialog").getEditor().getItem().toString();
            
            if (!isEdit) {
                crmTax = new Companyproducttax();
                crmTax.setCreateddate(new java.util.Date());
                crmTax.setCreatedfrom(EBISystem.ebiUser);
            } else {
                crmTax.setChangeddate(new java.util.Date());
                crmTax.setChangedfrom(EBISystem.ebiUser);
            }
            crmTax.setName(taxName);
            crmTax.setTaxvalue(Double.parseDouble(EBISystem.builder().textField("taxValue", "taxAdminDialog").getText()));
            EBISystem.hibernate().session("EBITAX_SESSION").saveOrUpdate(this.crmTax);
            EBISystem.hibernate().transaction("EBITAX_SESSION").commit();
        } catch (final org.hibernate.HibernateException ex) {
            try {
                EBISystem.hibernate().transaction("EBITAX_SESSION").rollback();
            } catch (final HibernateException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        newTax();
    }

    private void editTax(final int row) {
        if (row < 0) {
            return;
        }
        if (tabModel.data[row][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            return;
        }
        try {
            EBISystem.hibernate().transaction("EBITAX_SESSION").begin();

            final Query query = EBISystem.hibernate().session("EBITAX_SESSION").createQuery("from Companyproducttax where id=?1 ").setParameter(1, tabModel.data[row][2].toString());

            final Iterator iter = query.iterate();
            if (iter.hasNext()) {
                this.crmTax = (Companyproducttax) iter.next();
                EBISystem.hibernate().session("EBITAX_SESSION").refresh(crmTax);
                EBISystem.builder().combo("taxCombo", "taxAdminDialog").setSelectedItem(crmTax.getName());
                EBISystem.builder().textField("taxValue", "taxAdminDialog").setText(String.valueOf(crmTax.getTaxvalue()));
            }

        } catch (final org.hibernate.HibernateException ex) {
            try {
                EBISystem.hibernate().transaction("EBITAX_SESSION").rollback();
            } catch (final HibernateException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        isEdit = true;
    }

    private void deleteTax(final int row) {
        if (row < 0) {
            return;
        }
        if (tabModel.data[row][0].toString().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
            return;
        }

        if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_QUESTION_WOULD_YOU_DELETE_THIS_TYPE")).Show(EBIMessage.INFO_MESSAGE_YESNO)) {

            try {
                EBISystem.hibernate().transaction("EBITAX_SESSION").begin();

                final Query query = EBISystem.hibernate().session("EBITAX_SESSION").createQuery("from Companyproducttax where id=?1 ").setParameter(1, tabModel.data[row][2].toString());

                final Iterator iter = query.iterate();
                if (iter.hasNext()) {
                    this.crmTax = (Companyproducttax) iter.next();
                    EBISystem.hibernate().session("EBITAX_SESSION").delete(this.crmTax);
                    EBISystem.hibernate().transaction("EBITAX_SESSION").commit();
                }

            } catch (final org.hibernate.HibernateException ex) {
                try {
                    EBISystem.hibernate().transaction("EBITAX_SESSION").rollback();
                } catch (final HibernateException e) {
                    e.printStackTrace();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                EBIExceptionDialog.getInstance(EBISystem.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
            newTax();
        }

    }

    private void showTax() {
        try {
            EBISystem.hibernate().transaction("EBITAX_SESSION").begin();
            final Query query = EBISystem.hibernate().session("EBITAX_SESSION").createQuery("from Companyproducttax ");

            if (query.list().size() > 0) {
                tabModel.data = new Object[query.list().size()][3];

                final Iterator iter = query.iterate();
                int i = 0;
                while (iter.hasNext()) {
                    final Companyproducttax comtax = (Companyproducttax) iter.next();
                    EBISystem.hibernate().session("EBITAX_SESSION").refresh(comtax);
                    tabModel.data[i][0] = comtax.getName();
                    tabModel.data[i][1] = comtax.getTaxvalue();
                    tabModel.data[i][2] = comtax.getId();
                    i++;
                }
                EBISystem.hibernate().transaction("EBITAX_SESSION").commit();
            } else {
                tabModel.data = new Object[][]{{EBISystem.i18n("EBI_LANG_PLEASE_SELECT"), ""}};
            }
            tabModel.fireTableDataChanged();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

}
