
import ebiNeutrinoSDK.EBIPGFactory

system.gui.getButton("printSetting","CashRegister").actionPerformed={
        system.gui.loadGUI("CRMDialog/printSetup.xml");
        system.gui.getSpinner("setXText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_X")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_X")))
        system.gui.getSpinner("setYText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_Y")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_Y")))
        system.gui.getSpinner("setWidthText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_WIDTH")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_WIDTH")))
        system.gui.getSpinner("setTextWidthText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH")))
        system.gui.getSpinner("setSpaceMinText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN")))
        system.gui.getSpinner("setSpaceMaxText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX")))
        system.gui.getTextarea("descriptionText","printerSetup").setText(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT"))

        system.gui.getCheckBox("showPrinterDialog","printerSetup").setSelected("1".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SHOW_PRINTER_DIALOG")) ? true : false)

        system.gui.getButton("closeDialog","printerSetup").actionPerformed={
             system.gui.getEBIDialog("printerSetup").setVisible(false);
        }

        system.gui.getButton("saveValue","printerSetup").actionPerformed={

            system.properties.setValue("PRINTER_DIALOG_SET_X",system.gui.getSpinner("setXText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_Y",system.gui.getSpinner("setYText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_WIDTH",system.gui.getSpinner("setWidthText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_TEXT_WIDTH",system.gui.getSpinner("setTextWidthText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_SPACE_MIN",system.gui.getSpinner("setSpaceMinText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_SPACE_MAX",system.gui.getSpinner("setSpaceMaxText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT",system.gui.getTextarea("descriptionText","printerSetup").getText())
            system.properties.setValue("PRINTER_DIALOG_SHOW_PRINTER_DIALOG",system.gui.getCheckBox("showPrinterDialog","printerSetup").isSelected() ? "1" : "0")

            system.properties.saveProperties()
            
            system.gui.getEBIDialog("printerSetup").setVisible(false);
        }

        system.gui.showGUI();
}

