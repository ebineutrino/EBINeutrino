package ebiCRM.gui.panels;

import ebiCRM.EBICRMModule;
import ebiCRM.gui.dialogs.EBIDialogSearchCompany;
import ebiCRM.table.models.MyTableModelCRMAddress;
import ebiCRM.table.models.MyTableModelCRMContact;
import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.model.hibernate.Companyhirarchie;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EBICRMCompanyPane {

    public static String[] categories = null;
    public static String[] cooperations = null;
    public static String[] classification = null;
    public MyTableModelCRMAddress tabModel = null;
    private Companyhirarchie hComp = null;
    public Set<Companyhirarchie> listH = null;
    public MyTableModelCRMContact ctabModel = null;

    public void initializeAction() {
        tabModel = new MyTableModelCRMAddress();
        ctabModel = new MyTableModelCRMContact();
        //Configure contact table
        EBISystem.gui().table("companyTableContactViewX", "Company").setModel(ctabModel);
        EBISystem.gui().table("companyTableContactViewX", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Configure address table
        EBISystem.gui().table("companyTableAddressView", "Company").setModel(tabModel);
        EBISystem.gui().table("companyTableAddressView", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        EBISystem.gui().vpanel("Company").addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(final ComponentEvent e) {
                EBISystem.gui().textField("rootText", "Company").grabFocus();
            }
        });

        EBISystem.gui().combo("categoryText", "Company").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!EBISystem.gui().combo("categoryText", "Company").getSelectedItem().equals(EBISystem.i18n("EBI_LANG_PLEASE_SELECT"))) {
                    if (!EBICRMModule.isExistCompany) {
                        final Object[] obj = EBISystem.getCRMModule().dynMethod.getInternNumber(EBISystem.gui().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        EBISystem.getCRMModule().beginChar = obj[1].toString();
                        EBISystem.gui().textField("internalNrText", "Company").setText(obj[1].toString() + obj[0].toString());
                    } else if (EBICRMModule.isExistCompany && !EBISystem.gui().combo("categoryText", "Company").getSelectedItem().equals(EBISystem.getInstance().company.getCategory())) {
                        final Object[] obj = EBISystem.getCRMModule().dynMethod.getInternNumber(EBISystem.gui().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        EBISystem.getCRMModule().beginChar = obj[1].toString();
                        EBISystem.gui().textField("internalNrText", "Company").setText(obj[1] + obj[0].toString());
                    } else if (EBICRMModule.isExistCompany && EBISystem.gui().combo("categoryText", "Company").getSelectedItem().equals(EBISystem.getInstance().company.getCategory())) {
                        final Object[] obj = EBISystem.getCRMModule().dynMethod.getInternNumber(EBISystem.gui().combo("categoryText", "Company").getSelectedItem().toString(), false);
                        if (EBISystem.getInstance().company.getBeginchar() == null || "".equals(EBISystem.getInstance().company.getBeginchar())) {
                            EBISystem.getCRMModule().beginChar = obj[1].toString();
                        } else {
                            EBISystem.getCRMModule().beginChar = EBISystem.getInstance().company.getBeginchar();
                        }
                        EBISystem.gui().textField("internalNrText", "Company").setText(EBISystem.getInstance().company.getCompanynumber() == null
                                ? "-1" : EBISystem.getCRMModule().beginChar + String.valueOf(EBISystem.getInstance().company.getCompanynumber() == -1
                                ? obj[0] : EBISystem.getInstance().company.getCompanynumber()));
                    }
                } else {
                    EBISystem.gui().textField("internalNrText", "Company").setText("-1");
                }
            }
        });

    }

    public void initialize() {
        listH = new HashSet();
        hComp = new Companyhirarchie();

        EBISystem.gui().combo("categoryText", "Company").setModel(new DefaultComboBoxModel(categories));
        EBISystem.gui().combo("cooperationText", "Company").setModel(new DefaultComboBoxModel(cooperations));
        EBISystem.gui().combo("classificationText", "Company").setModel(new DefaultComboBoxModel(classification));

        EBISystem.gui().vpanel("Company").setCreatedDate(EBISystem.getInstance().getDateToString(new Date()));
        EBISystem.gui().vpanel("Company").setCreatedFrom(EBISystem.ebiUser);
        EBISystem.gui().vpanel("Company").setChangedDate("");
        EBISystem.gui().vpanel("Company").setChangedFrom("");

        EBISystem.gui().combo("categoryText", "Company").setSelectedIndex(0);
        EBISystem.gui().combo("classificationText", "Company").setSelectedIndex(0);
        EBISystem.gui().combo("cooperationText", "Company").setSelectedIndex(0);
        EBISystem.gui().textField("internalNrText", "Company").setText("");
        EBISystem.gui().textField("rootText", "Company").setText("");
        EBISystem.gui().textField("rootText", "Company").requestFocus();
        EBISystem.gui().textField("custNrText", "Company").setText("");
        EBISystem.gui().textField("nameText", "Company").setText("");
        EBISystem.gui().textField("name1Text", "Company").setText("");
        EBISystem.gui().textField("taxIDText", "Company").setText("");
        EBISystem.gui().textField("employeeText", "Company").setText("");
        EBISystem.gui().textField("telephoneText", "Company").setText("");
        EBISystem.gui().textField("faxText", "Company").setText("");
        EBISystem.gui().textField("internetText", "Company").setText("");
        EBISystem.gui().textField("emailText", "Company").setText("");
        EBISystem.gui().getCheckBox("lockCompany", "Company").setSelected(false);
        EBISystem.gui().textArea("companyDescription", "Company").setText("");
        tabModel = new MyTableModelCRMAddress();
        tabModel.fireTableDataChanged();
        //Configure address table
        EBISystem.gui().table("companyTableAddressView", "Company").setModel(tabModel);
        EBISystem.gui().table("companyTableAddressView", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ctabModel = new MyTableModelCRMContact();
        ctabModel.fireTableDataChanged();

        EBISystem.gui().table("companyTableContactViewX", "Company").setModel(ctabModel);
        EBISystem.gui().table("companyTableContactViewX", "Company").setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (EBISystem.getCRMModule().ebiContainer.companyPOSID != -1) {
            EBISystem.getCRMModule().ebiContainer.getTabInstance().setTitleAt(EBISystem.getCRMModule().ebiContainer.companyPOSID, EBISystem.i18n("EBI_LANG_C_COMPANY"));
        }
    }

    public void selectRoot() {
        new EBIDialogSearchCompany(true, false);
    }

    public boolean setHierarchies(final String name, final int parent) {
        this.hComp.setCompany(EBISystem.getInstance().company);
        this.hComp.setParent(parent);
        this.hComp.setName(name);
        EBISystem.gui().textField("rootText", "Company").setText(this.hComp.getName());
        this.hComp.setCreateddate(new Date());
        this.hComp.setCreatedfrom(EBISystem.ebiUser);
        listH.add(this.hComp);
        return true;
    }

    public void showHierarchies() {
        final Iterator itr = this.listH.iterator();
        while (itr.hasNext()) {
            this.hComp = (Companyhirarchie) itr.next();
            EBISystem.gui().textField("rootText", "Company").setText(this.hComp.getName());
        }
    }
}