package org.modules.views;

import org.modules.EBIModule;
import org.modules.views.dialogs.EBIDialogSearchCompany;
import org.modules.models.ModelCRMAddress;
import org.modules.models.ModelCRMContact;
import org.sdk.EBISystem;
import org.sdk.model.hibernate.Companyhirarchie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.sdk.gui.component.EBIJTextFieldNumeric;

public class EBICRMCompanyView {

    public static String[] categories = null;
    public static String[] cooperations = null;
    public static String[] classification = null;
   
    private Companyhirarchie hComp = null;
    public Set<Companyhirarchie> listH = null;
    
    @Getter @Setter
    private ModelCRMAddress tabModel = null;
    @Getter @Setter
    public ModelCRMContact ctabModel = null;

    public void initializeAction() {
        tabModel = new ModelCRMAddress();
        ctabModel = new ModelCRMContact(ModelCRMContact.CRM_CONTACT);

        EBISystem.builder().textField("telephoneText", "Company").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.builder().textField("faxText", "Company").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.PHONE));
        EBISystem.builder().textField("emailText", "Company").setDocument(new EBIJTextFieldNumeric(EBIJTextFieldNumeric.EMAIL));

        //Configure contact table
        EBISystem.builder().table("companyTableContactViewX", "Company").setModel(ctabModel);
        EBISystem.builder().table("companyTableContactViewX", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Configure address table
        EBISystem.builder().table("companyTableAddressView", "Company").setModel(tabModel);
        EBISystem.builder().table("companyTableAddressView", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        EBISystem.builder().vpanel("Company").addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent e) {
                EBISystem.builder().textField("rootText", "Company").grabFocus();
            }
        });

        EBISystem.builder().combo("categoryText", "Company").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.builder().combo("categoryText", "Company")
                        .getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    if (!EBIModule.isExistCompany) {
                        final Object[] obj = EBISystem.getModule().dynMethod.getInternNumber(EBISystem.builder().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        EBISystem.getModule().beginChar = obj[1].toString();
                        EBISystem.builder().textField("internalNrText", "Company").setText(obj[1].toString() + obj[0].toString());
                    } else if (EBIModule.isExistCompany && !EBISystem.builder().combo("categoryText", "Company").getSelectedItem().equals(EBISystem.getInstance().getCompany().getCategory())) {
                        final Object[] obj = EBISystem.getModule().dynMethod.getInternNumber(EBISystem.builder().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        EBISystem.getModule().beginChar = obj[1].toString();
                        EBISystem.builder().textField("internalNrText", "Company").setText(obj[1] + obj[0].toString());
                    } else if (EBIModule.isExistCompany && EBISystem.builder().combo("categoryText", "Company").getSelectedItem().equals(EBISystem.getInstance().getCompany().getCategory())) {
                        final Object[] obj = EBISystem.getModule().dynMethod.getInternNumber(EBISystem.builder().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        if (EBISystem.getInstance().getCompany().getBeginchar() == null || "".equals(EBISystem.getInstance().getCompany().getBeginchar())) {
                            EBISystem.getModule().beginChar = obj[1].toString();
                        } else {
                            EBISystem.getModule().beginChar = EBISystem.getInstance().getCompany().getBeginchar();
                        }
                        EBISystem.builder().textField("internalNrText", "Company").setText(EBISystem.getInstance().getCompany().getCompanynumber() == null
                                ? "-1" : EBISystem.getModule().beginChar + String.valueOf(EBISystem.getInstance().getCompany().getCompanynumber() == -1
                                ? obj[0] : EBISystem.getInstance().getCompany().getCompanynumber()));
                    }
                } else {
                    EBISystem.builder().textField("internalNrText", "Company").setText("-1");
                }
            }
        });

    }

    public void initialize() {
        listH = new HashSet();
        hComp = new Companyhirarchie();

        EBISystem.builder().combo("categoryText", "Company").setModel(new DefaultComboBoxModel(categories));
        EBISystem.builder().combo("cooperationText", "Company").setModel(new DefaultComboBoxModel(cooperations));
        EBISystem.builder().combo("classificationText", "Company").setModel(new DefaultComboBoxModel(classification));

        EBISystem.builder().vpanel("Company").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.builder().vpanel("Company").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.builder().vpanel("Company").setChangedDate("");
        EBISystem.builder().vpanel("Company").setChangedFrom("");

        EBISystem.builder().combo("categoryText", "Company").setSelectedIndex(0);
        EBISystem.builder().combo("classificationText", "Company").setSelectedIndex(0);
        EBISystem.builder().combo("cooperationText", "Company").setSelectedIndex(0);
        EBISystem.builder().textField("internalNrText", "Company").setText("");
        EBISystem.builder().textField("rootText", "Company").setText("");
        EBISystem.builder().textField("rootText", "Company").requestFocus();
        EBISystem.builder().textField("custNrText", "Company").setText("");
        EBISystem.builder().textField("nameText", "Company").setText("");
        EBISystem.builder().textField("name1Text", "Company").setText("");
        EBISystem.builder().textField("taxIDText", "Company").setText("");
        EBISystem.builder().textField("employeeText", "Company").setText("");
        EBISystem.builder().textField("telephoneText", "Company").setText("");
        EBISystem.builder().textField("faxText", "Company").setText("");
        EBISystem.builder().textField("internetText", "Company").setText("");
        EBISystem.builder().textField("emailText", "Company").setText("");
        EBISystem.builder().getCheckBox("lockCompany", "Company").setSelected(false);
        EBISystem.builder().textArea("companyDescription", "Company").setText("");
        tabModel = new ModelCRMAddress();
        tabModel.fireTableDataChanged();
        //Configure address table
        EBISystem.builder().table("companyTableAddressView", "Company").setModel(tabModel);
        EBISystem.builder().table("companyTableAddressView", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ctabModel = new ModelCRMContact(ModelCRMContact.CRM_CONTACT);
        ctabModel.fireTableDataChanged();

        EBISystem.builder().table("companyTableContactViewX", "Company").setModel(ctabModel);
        EBISystem.builder().table("companyTableContactViewX", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (EBISystem.getModule().ebiContainer.companyPOSID != -1) {
            EBISystem.getModule().ebiContainer.getTabInstance().setTitleAt(EBISystem.getModule().ebiContainer.companyPOSID, EBISystem.i18n("EBI_LANG_C_COMPANY"));
        }
    }

    public void selectRoot() {
        new EBIDialogSearchCompany(true, false);
    }

    public boolean setHierarchies(final String name, final int parent) {
        this.hComp.setCompany(EBISystem.getInstance().getCompany());
        this.hComp.setParent(parent);
        this.hComp.setName(name);
        EBISystem.builder().textField("rootText", "Company").setText(this.hComp.getName());
        this.hComp.setCreateddate(new Date());
        this.hComp.setCreatedfrom(EBISystem.ebiUser);
        listH.add(this.hComp);
        return true;
    }

    public void showHierarchies() {
        final Iterator itr = this.listH.iterator();
        while (itr.hasNext()) {
            this.hComp = (Companyhirarchie) itr.next();
            EBISystem.builder().textField("rootText", "Company").setText(this.hComp.getName());
        }
    }
}
