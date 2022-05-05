package org.modules.views.dialogs;

import org.sdk.model.hibernate.Crmproduct;
import org.sdk.model.hibernate.Crmprojecttask;
import org.sdk.model.hibernate.Crmproductdimension;
import org.sdk.model.hibernate.Crmprojectprop;
import org.sdk.model.hibernate.Crmprojectcost;
import org.sdk.EBISystem;
import org.sdk.gui.dialogs.EBIExceptionDialog;
import org.sdk.gui.dialogs.EBIMessage;

import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.util.Date;

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
        EBISystem.builder().loadGUI("CRMDialog/propertiesDialog.xml");
        product = prd;
        EBISystem.hibernate().openHibernateSession("EBI_PROPERTIES");
        EBISystem.hibernate().transaction("EBI_PROPERTIES").begin();
        EBISystem.builder().combo("propertiesText", "propertiesDialog").setModel(new javax.swing.DefaultComboBoxModel(productDimension));
        if (dims != null) {
            dimension = dims;
            EBISystem.builder().textArea("propertiesValueText", "propertiesDialog").setText(dims.getDimensionValue());
            EBISystem.builder().combo("propertiesText", "propertiesDialog").setSelectedItem(dims.getDimensionName());

            if (EBISystem.builder().combo("propertiesText", "propertiesDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                EBISystem.builder().combo("propertiesText", "propertiesDialog").insertItemAt(dims.getDimensionName(), 1);
                EBISystem.builder().combo("propertiesText", "propertiesDialog").setSelectedIndex(1);
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
            EBISystem.builder().loadGUI("CRMDialog/costValueDialog.xml");
            EBISystem.builder().combo("propertiesText", "costValueDialog").setModel(new javax.swing.DefaultComboBoxModel(projectCost));
            isProjectCost = true;
        } else {
            projectProperties = (Crmprojectprop) prop;
            EBISystem.builder().loadGUI("CRMDialog/projectPropertiesDialog.xml");
            EBISystem.builder().combo("propertiesText", "projectPropertiesDialog").setModel(new javax.swing.DefaultComboBoxModel(projectProperty));
            isProjectProperties = true;
        }

        if (prop != null) {
            String name;
            Object value;
            if (isCost) {
                name = ((Crmprojectcost) prop).getName();
                value = ((Crmprojectcost) prop).getValue();
                EBISystem.builder().FormattedField("nameValue", "costValueDialog").setValue(value);
                EBISystem.builder().combo("propertiesText", "costValueDialog").setSelectedItem(name);
                if (EBISystem.builder().combo("propertiesText", "costValueDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().combo("propertiesText", "costValueDialog").insertItemAt(name, 1);
                    EBISystem.builder().combo("propertiesText", "costValueDialog").setSelectedIndex(1);
                }
            } else {
                name = ((Crmprojectprop) prop).getName();
                value = ((Crmprojectprop) prop).getValue();
                EBISystem.builder().textArea("propertiesValueText", "projectPropertiesDialog").setText(value.toString());
                EBISystem.builder().combo("propertiesText", "projectPropertiesDialog").setSelectedItem(name);
                if (EBISystem.builder().combo("propertiesText", "projectPropertiesDialog").getEditor().getItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    EBISystem.builder().combo("propertiesText", "projectPropertiesDialog").insertItemAt(name, 1);
                    EBISystem.builder().combo("propertiesText", "projectPropertiesDialog").setSelectedIndex(1);
                }
            }
            isEdit = true;
        }
    }

    public void setVisible() {

        if (!isProjectCost) {
            pack = "propertiesDialog";
            EBISystem.builder().dialog(pack).setTitle(EBISystem.i18n("EBI_LANG_PROPERTIES"));
            EBISystem.builder().vpanel(pack).setModuleTitle(EBISystem.i18n("EBI_LANG_PROPERTIES"));

            EBISystem.builder().label("value", pack).setText(EBISystem.i18n("EBI_LANG_VALUE"));
            EBISystem.builder().label("properties", pack).setText(EBISystem.i18n("EBI_LANG_PROPERTIES"));

        } else {
            pack = "costValueDialog";
            final NumberFormat taxFormat = NumberFormat.getNumberInstance();
            taxFormat.setMinimumFractionDigits(2);
            taxFormat.setMaximumFractionDigits(3);

            EBISystem.builder().FormattedField("nameValue", pack).setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(taxFormat)));
        }

        EBISystem.builder().button("closeButton", pack).setText(EBISystem.i18n("EBI_LANG_CANCEL"));
        EBISystem.builder().button("closeButton", pack).addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                EBISystem.builder().dialog(pack).setVisible(false);
                cancel = true;
            }
        });

        EBISystem.builder().button("applyButton", pack).setText(EBISystem.i18n("EBI_LANG_APPLY"));
        if (!isEdit) {
            EBISystem.builder().button("applyButton", pack).setEnabled(false);
        }

        EBISystem.builder().button("applyButton", pack).addActionListener(new java.awt.event.ActionListener() {

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

        EBISystem.builder().combo("propertiesText", pack).addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(final java.awt.event.ActionEvent e) {
                if (!EBISystem.i18n("EBI_LANG_PLEASE_SELECT").equals(EBISystem.builder().combo("propertiesText", pack).getEditor().getItem().toString())) {
                    EBISystem.builder().button("applyButton", pack).setEnabled(true);
                }
            }
        });
        EBISystem.builder().showGUI();
    }

    private void saveProductProperties() {
        if (!isEdit) {
            dimension = new Crmproductdimension();
            dimension.setDimensionid((product.getCrmproductdimensions().size() + 1) * -1);
        }
        dimension.setCrmproduct(EBISystem.getModule().getEBICRMProductPane().getDataControlProduct().getProduct());
        dimension.setCreateddate(new Date());
        dimension.setCreatedfrom(EBISystem.ebiUser);
        dimension.setDimensionName(EBISystem.builder().combo("propertiesText", "propertiesDialog").getEditor().getItem().toString());
        dimension.setDimensionValue(EBISystem.builder().textArea("propertiesValueText", "propertiesDialog").getText());
        
        if(!isEdit){
             product.getCrmproductdimensions().add(dimension);
        }
        
        EBISystem.getModule().getEBICRMProductPane().showDimension();

        EBISystem.builder().textArea("propertiesValueText", "propertiesDialog").setText("");
        EBISystem.builder().combo("propertiesText", "propertiesDialog").setSelectedIndex(0);
        EBISystem.builder().combo("propertiesText", "propertiesDialog").grabFocus();
    }
 
    private void saveProjectProperties() {
        if (!isEdit) {
            projectProperties = new Crmprojectprop();
            projectProperties.setPropertiesid((projectTask.getCrmprojectprops().size() + 1) * -1);
        }

        projectProperties.setCrmprojecttask(projectTask);
        projectProperties.setCreateddate(new Date());
        projectProperties.setCreatedfrom(EBISystem.ebiUser);
        projectProperties.setName(EBISystem.builder().combo("propertiesText", "propertiesDialog").getEditor().getItem().toString());
        projectProperties.setValue(EBISystem.builder().textArea("propertiesValueText", "propertiesDialog").getText());
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
        projectCosts.setName(EBISystem.builder().combo("propertiesText", "costValueDialog").getEditor().getItem().toString());
        projectCosts.setValue(Double.parseDouble(EBISystem.builder().FormattedField("nameValue", "costValueDialog").getValue().toString()));
        projectTask.getCrmprojectcosts().add(projectCosts);
        EBISystem.getModule().getProjectPane().getProjTask().showCost();
    }

    private boolean validateInput() {
        if ("".equals(EBISystem.builder().textArea("propertiesValueText", "propertiesDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROPERTY_VALUE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateInputCost() {
        if ("".equals(EBISystem.builder().FormattedField("nameValue", "costValueDialog").getText())) {
            EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_PLEASE_INSERT_PROPERTY_VALUE")).Show(EBIMessage.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
