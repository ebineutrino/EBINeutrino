package org.modules.views.dialogs;

import org.sdk.model.hibernate.Crmproduct;
import org.sdk.model.hibernate.Crmprojecttask;
import org.sdk.model.hibernate.Crmproductdimensions;
import org.sdk.model.hibernate.Crmproductdimension;
import org.sdk.model.hibernate.Crmprojectprop;
import org.sdk.model.hibernate.Crmprojectprops;
import org.sdk.model.hibernate.Crmprojectcost;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;

public class EBIDialogProperties {

    private Crmprojecttask projectTask = null;
    public static String[] productDimension = null;
    public static String[] projectCost = null;
    public static String[] projectProperty = null;
    private boolean isProperties = false;
    private boolean isProjectProperties = false;
    private boolean isProjectCost = false;
    public boolean cancel = false;
    private boolean isEdit = false;
    private Crmproduct product = null;
    private Crmproductdimension dimension = null;
    private Crmprojectprop projectProperties = null;
    private Crmprojectcost projectCosts = null;
    private String pack = "";

    public EBIDialogProperties(final Crmproduct prd, final Crmproductdimension dims) {
        EBISystem.gui().loadGUI("CRMDialog/propertiesDialog.xml");
        product = prd;
        EBISystem.hibernate().openHibernateSession("EBI_PROPERTIES");
        EBISystem.hibernate().transaction("EBI_PROPERTIES").begin();
        EBISystem.gui().combo("propertiesText", "propertiesDialog").setModel(new javax.swing.DefaultComboBoxModel(productDimension));
        if (dims != null) {
            dimension = dims;
            EBISystem.gui().textArea("propertiesValueText", "propertiesDialog").setText(dims.getValue());
            EBISystem.gui().combo("propertiesText", "propertiesDialog").setSelectedItem(dims.getName());

            if (EBISystem.gui().combo("propertiesText", "propertiesDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                EBISystem.gui().combo("propertiesText", "propertiesDialog").insertItemAt(dims.getName(), 1);
                EBISystem.gui().combo("propertiesText", "propertiesDialog").setSelectedIndex(1);
            }
            isEdit = true;
        }

        isProperties = true;
    }

    public EBIDialogProperties(final Crmprojecttask task, final Object prop, final boolean isCost) {
        projectTask = task;
        EBISystem.hibernate().openHibernateSession("EBI_PROPERTIES");
        EBISystem.hibernate().transaction("EBI_PROPERTIES").begin();
        if (isCost) {
            projectCosts = (Crmprojectcost) prop;
            EBISystem.gui().loadGUI("CRMDialog/costValueDialog.xml");
            EBISystem.gui().combo("propertiesText", "costValueDialog").setModel(new javax.swing.DefaultComboBoxModel(projectCost));
            isProjectCost = true;
        } else {
            projectProperties = (Crmprojectprop) prop;
            EBISystem.gui().loadGUI("CRMDialog/projectPropertiesDialog.xml");
            EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").setModel(new javax.swing.DefaultComboBoxModel(projectProperty));
            isProjectProperties = true;
        }

        if (prop != null) {
            String name;
            Object value;
            if (isCost) {
                name = ((Crmprojectcost) prop).getName();
                value = ((Crmprojectcost) prop).getValue();
                EBISystem.gui().FormattedField("nameValue", "costValueDialog").setValue(value);
                EBISystem.gui().combo("propertiesText", "costValueDialog").setSelectedItem(name);
                if (EBISystem.gui().combo("propertiesText", "costValueDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().combo("propertiesText", "costValueDialog").insertItemAt(name, 1);
                    EBISystem.gui().combo("propertiesText", "costValueDialog").setSelectedIndex(1);
                }
            } else {
                name = ((Crmprojectprop) prop).getName();
                value = ((Crmprojectprop) prop).getValue();
                EBISystem.gui().textArea("propertiesValueText", "projectPropertiesDialog").setText(value.toString());
                EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").setSelectedItem(name);
                if (EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").insertItemAt(name, 1);
                    EBISystem.gui().combo("propertiesText", "projectPropertiesDialog").setSelectedIndex(1);
                }
            }
            isEdit = true;
        }
    }

    public void setVisible() {

        if (!isProjectCost) {
            pack = "propertiesDialog";
            EBISystem.gui().dialog(pack).setTitle(EBISystem.i18n("EBI_LANG_PROPERTIES"));
            EBISystem.gui().vpanel(pack).setModuleTitle(EBISystem.i18n("EBI_LANG_PROPERTIES"));

            EBISystem.gui().label("value", pack).setText(EBISystem.i18n("EBI_LANG_VALUE"));
            EBISystem.gui().label("properties", pack).setText(EBISystem.i18n("EBI_LANG_PROPERTIES"));

        } else {
            pack = "costValueDialog";
            final NumberFormat taxFormat = NumberFormat.getNumberInstance();
            taxFormat.setMinimumFractionDigits(2);
            taxFormat.setMaximumFractionDigits(3);

            EBISystem.gui().FormattedField("nameValue", pack).setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        }

        EBISystem.gui().button("closeButton", pack).setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.gui().button("closeButton", pack).addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.gui().dialog(pack).setVisible(false);
                cancel = true;
            }
        });

        EBISystem.gui().button("applyButton", pack).setText(EBISystem.i18n("EBI_LANG_APPLY"));
        if (!isEdit) {
            EBISystem.gui().button("applyButton", pack).setEnabled(false);
        }

        EBISystem.gui().button("applyButton", pack).addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {

                if (!isProjectCost) {
                    if (!validateInput()) {
                        return;
                    }
                } else {
                    if (!validateInputCost()) {
                        return;
                    }
                }
                if (isProperties) {
                    saveProductProperties();
                } else if (isProjectCost) {
                    saveProjectCost();
                } else if (isProjectProperties) {
                    saveProjectProperties();
                }
            }
        });

        EBISystem.gui().combo("propertiesText", pack).addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.gui().combo("propertiesText", pack).getEditor().getItem().toString())) {
                    EBISystem.gui().button("applyButton", pack).setEnabled(true);
                }
            }
        });
        EBISystem.gui().showGUI();
    }

    private void saveProductProperties() {
        if (!isEdit) {
            dimension = new Crmproductdimension();
            dimension.setDimensionid((product.getCrmproductdimensions().size() + 1) * -1);
        }
        dimension.setCrmproduct(EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().getProduct());
        dimension.setCreateddate(new Date());
        dimension.setCreatedfrom(EBISystem.ebiUser);
        dimension.setName(EBISystem.gui().combo("propertiesText", "propertiesDialog").getEditor().getItem().toString());
        dimension.setValue(EBISystem.gui().textArea("propertiesValueText", "propertiesDialog").getText());
        product.getCrmproductdimensions().add(dimension);
        EBISystem.getModule().getEBICRMProductPane().showDimension();

        EBISystem.gui().textArea("propertiesValueText", "propertiesDialog").setText("");
        EBISystem.gui().combo("propertiesText", "propertiesDialog").setSelectedIndex(0);
        EBISystem.gui().combo("propertiesText", "propertiesDialog").grabFocus();
    }
 
    private void saveProjectProperties() {
        if (!isEdit) {
            projectProperties = new Crmprojectprop();
            projectProperties.setPropertiesid((projectTask.getCrmprojectprops().size() + 1) * -1);
        }

        projectProperties.setCrmprojecttask(projectTask);
        projectProperties.setCreateddate(new Date());
        projectProperties.setCreatedfrom(EBISystem.ebiUser);
        projectProperties.setName(EBISystem.gui().combo("propertiesText", "propertiesDialog").getEditor().getItem().toString());
        projectProperties.setValue(EBISystem.gui().textArea("propertiesValueText", "propertiesDialog").getText());
        projectTask.getCrmprojectprops().add(projectProperties);
        EBISystem.getModule().getProjectPane().getProjTask().showProperties();
    }

    private void saveProjectCost() {
        if (!isEdit) {
            projectCosts = new Crmprojectcost();
            projectCosts.setCostid((projectTask.getCrmprojectcosts().size() + 1) * -1);
        }
        projectCosts.setCrmprojecttask(projectTask);
        projectCosts.setCreateddate(new Date());
        projectCosts.setCreatedfrom(EBISystem.ebiUser);
        projectCosts.setName(EBISystem.gui().combo("propertiesText", "costValueDialog").getEditor().getItem().toString());
        projectCosts.setValue(Double.parseDouble(EBISystem.gui().FormattedField("nameValue", "costValueDialog").getValue().toString()));
        projectTask.getCrmprojectcosts().add(projectCosts);
        EBISystem.getModule().getProjectPane().getProjTask().showCost();
    }

    private boolean validateInput() {
        if ("".equals(EBISystem.gui().textArea("propertiesValueText", "propertiesDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROPERTY_VALUE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateInputCost() {
        if ("".equals(EBISystem.gui().FormattedField("nameValue", "costValueDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROPERTY_VALUE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
