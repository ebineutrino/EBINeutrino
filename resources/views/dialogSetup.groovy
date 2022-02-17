
import ebiNeutrinoSDK.EBIPGFactory

system.builder.getButton("printSetting","CashRegister").actionPerformed={
        system.builder.loadGUI("CRMDialog/printSetup.xml");
        system.builder.getSpinner("setXText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_X")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_X")))
        system.builder.getSpinner("setYText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_Y")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_Y")))
        system.builder.getSpinner("setWidthText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_WIDTH")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_WIDTH")))
        system.builder.getSpinner("setTextWidthText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH")))
        system.builder.getSpinner("setSpaceMinText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN")))
        system.builder.getSpinner("setSpaceMaxText","printerSetup").setValue("".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX")) ? 0 : 
        Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX")))
        system.builder.getTextarea("descriptionText","printerSetup").setText(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT"))

        system.builder.getCheckBox("showPrinterDialog","printerSetup").setSelected("1".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SHOW_PRINTER_DIALOG")) ? true : false)

        system.builder.getButton("closeDialog","printerSetup").actionPerformed={
             system.builder.getEBIDialog("printerSetup").setVisible(false);
        }

        system.builder.getButton("saveValue","printerSetup").actionPerformed={

            system.properties.setValue("PRINTER_DIALOG_SET_X",system.builder.getSpinner("setXText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_Y",system.builder.getSpinner("setYText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_WIDTH",system.builder.getSpinner("setWidthText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_TEXT_WIDTH",system.builder.getSpinner("setTextWidthText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_SPACE_MIN",system.builder.getSpinner("setSpaceMinText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_SPACE_MAX",system.builder.getSpinner("setSpaceMaxText","printerSetup").getValue().toString())
            system.properties.setValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT",system.builder.getTextarea("descriptionText","printerSetup").getText())
            system.properties.setValue("PRINTER_DIALOG_SHOW_PRINTER_DIALOG",system.builder.getCheckBox("showPrinterDialog","printerSetup").isSelected() ? "1" : "0")

            system.properties.saveProperties()
            
            system.builder.getEBIDialog("printerSetup").setVisible(false);
        }

        system.builder.showGUI();
}

