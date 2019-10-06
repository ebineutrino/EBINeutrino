import ebiCRM.utils.EBICRMIProduct
import ebiNeutrino.core.EBIMain
import ebiNeutrinoSDK.utils.EBIAbstractTableModel

import javax.swing.*
import java.awt.*
import java.awt.print.*
import java.text.NumberFormat;

double rCash=0.0

class CashRegisterPrinter implements Printable {

    public CashRegisterPrinter(){
        system = factory;
    }

    public int print(Graphics g, PageFormat pf, int page) throws
    PrinterException {
        if (page > 0) {
            /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }

        int x = Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_X"))
        int y = Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_Y"))
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        /* Now we perform our rendering */
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString(system.map.get("COMPANY_NAME"), x, y);
        y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
        g2d.drawString(system.map.get("COMPANY_STR_NR"), x, y);

        y += 15;
        g2d.drawString(system.map.get("COMPANY_ZIP")+" "+system.map.get("COMPANY_LOCATION"), x, y)

        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
        y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"));
        y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));

        g2d.drawString(system.globalVariable.get("CashName"),x,y);

        y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"));

        //Print the products with price, deduction, description, quantity
        Hashtable<String,String> productTable = (Hashtable)system.globalVariable.get("productList");
        Iterator ix = productTable.keySet().iterator();
        FontMetrics fm = g2d.getFontMetrics();
        int co = 0;
        int tmp_y =0;

        while(ix.hasNext()) {
            String key = (String)ix.next();
            y+=15;
            if(fm.stringWidth(key) > Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH"))-17){

                char [] toInsert = key.toCharArray()
                String n= "";

                tmp_y = y;
                for(int t =0; t < toInsert.length; t++ ){
                    n += toInsert[t];
                    if(fm.stringWidth(n) >= (Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH"))-17) ){

                        int diff = n.lastIndexOf(" ");
                        if( diff != -1){
                            g2d.drawString(n.substring(0,diff), x, y);
                            n = n.substring(diff+1);
                            y = y + Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
                            co++;
                        }else{
                            g2d.drawString(n, x, y);
                            y = y + Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
                            n="";
                            co++;
                        }

                    }else if(t+1 >= toInsert.length){
                        g2d.drawString(n, x, y);
                        y = y + Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
                        co++;
                    }
                }

            }else{
                g2d.drawString(key+": ", x, y);
            }

            int newY = y;
            if(co > 0){
                newY = ((y-tmp_y) / (co)) +tmp_y;
            }

            EBICRMIProduct prOtable = (EBICRMIProduct)productTable.get(key)
            g2d.drawString(prOtable.getCalculatedSPriceNet(),Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH"))+15, newY);
            g2d.drawString(prOtable.getQuantity()+"x",Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_TEXT_WIDTH"))-10, newY);
        }

        // Print grossamount
        y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"))
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        def inst = NumberFormat.getCurrencyInstance()
        inst.setMinimumFractionDigits(2)
        inst.setMaximumFractionDigits(3)

        g2d.drawString(system.getLANG("EBI_LANG_TOTAL_NETAMOUNT")+": "+inst.format(system.globalVariable.get("totalAmountCashNetValue")), x, y);
        y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"))
        g2d.drawString(system.getLANG("EBI_LANG_TOTAL_GROSSAMOUNT")+": "+inst.format(system.globalVariable.get("totalAmountCashRValue")), x, y);

        // y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"))

        // Print a different tax
        Hashtable<String,Double> taxTable = (Hashtable)system.globalVariable.get("taxTable");
        Iterator itax = taxTable.keySet().iterator();

        while(itax.hasNext()){
            String key = (String) itax.next();
            y+=15;

            g2d.drawString(key+": "+inst.format(taxTable.get(key)),x,y)
        }
        y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"))
        y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"))


        // Print a tax id number
        g2d.drawString(system.getLANG("EBI_LANG_C_VAT_NR"),x,y)
        y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
        g2d.drawString(system.map.get("COMPANY_TAX_INFORMATION"),x,y)
        y+=Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MAX"))


        //Print a user based description

        if(!"".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT"))){
            String[]splt = EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_DESCRIPTION_TEXT").split("\n");
            for(int l =0; l<splt.length; l++){
                g2d.drawString(splt[l],x,y)
                y += Integer.parseInt(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SET_SPACE_MIN"));
            }
        }

        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
}

int selectedCashRow=-1;
final EBIAbstractTableModel model = (EBIAbstractTableModel)system.gui.getTable("tableCashRegister","CashRegister").getModel();
system.gui.getButton("printCash","CashRegister").setEnabled(false)

system.gui.getTable("tableCashRegister","CashRegister").getSelectionModel().valueChanged={e->
    if (e.getValueIsAdjusting()) {
        return;
    }
    ListSelectionModel lsm = (ListSelectionModel) e.getSource();

    if(lsm.getMinSelectionIndex() != -1){
        try{
            selectedCashRow = system.gui.getTable("tableCashRegister","CashRegister").convertRowIndexToModel(lsm.getMinSelectionIndex());
        }catch(IndexOutOfBoundsException ex){}

    }
    try{
        if (lsm.isSelectionEmpty()) {
            system.gui.getButton("printCash","CashRegister").setEnabled(false);
        } else if (!model.data[selectedCashRow][0].toString().equals(system.getLANG("EBI_LANG_PLEASE_SELECT"))) {
            system.gui.getButton("printCash","CashRegister").setEnabled(true);
        }
    }catch(ArrayIndexOutOfBoundsException ex){
        system.gui.getButton("printCash","CashRegister").setEnabled(false);

    }
}

system.gui.getButton("printCash","CashRegister").actionPerformed={

    PrinterJob job = PrinterJob.getPrinterJob();

    if("1".equals(EBIPGFactory.properties.getValue("PRINTER_DIALOG_SHOW_PRINTER_DIALOG"))){
        boolean ok = job.printDialog();
        job.setPrintable(new CashRegisterPrinter(system));
        if (ok) {
            try {
                ((EBIMain)system.getIEBIModule()).ebiModule.cashRegisterPane.dataControlCashRegister.loadProduct(Integer.parseInt(model.data[selectedCashRow][4].toString()));
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace()
            }
        }
    }else{
        try {
            PageFormat pf = job.defaultPage();
            Paper paper = new Paper();
            paper.setImageableArea(10,10, paper.getWidth(), paper.getHeight());
            pf.setPaper(paper);
            job.setPrintable(new CashRegisterPrinter(system),pf);
            ((EBIMain)system.getIEBIModule()).ebiModule.cashRegisterPane.dataControlCashRegister.loadProduct(Integer.parseInt(model.data[selectedCashRow][4].toString()));
            job.print();
        } catch (PrinterException ex) {
            ex.printStackTrace()
        }
    }
}


system.gui.getButton("bntSave","CashRegister").actionPerformed={ rCash =0.0;      }

public def updateReturnCash(val, text){

    double var = 0.0

    if(val != 0.0){
        var = (val-Double.parseDouble(system.globalVariable.get("totalAmountCashRValue").toString()))
    }

    def inst = NumberFormat.getCurrencyInstance()
    inst.setMinimumFractionDigits(2)
    inst.setMaximumFractionDigits(3)


    def newText = "<br><br><hr><br><h2><font color='#ffffff' face='Verdana'>"+system.getLANG("EBI_LANG_CASH")+" "+inst.format(val)+" &nbsp;&nbsp;&nbsp;&nbsp; "+system.getLANG("EBI_LANG_TO_RETURN")+"   "+ inst.format(var)+"</font></h2>"

    String txt =  system.globalVariable.get("valueEditCashR").toString().replaceAll("</table>","</table>"+newText)

    system.gui.getEditor("cashView","CashRegister").setText(txt.replaceAll("<table>","<table style='color:#ffffff; font-family: Verdana;'>"))

}

system.gui.getButton("cashBnt5","CashRegister").actionPerformed={

    rCash += 5
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}


system.gui.getButton("cashBnt10","CashRegister").actionPerformed={

    rCash += 10
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}


system.gui.getButton("cashBnt50","CashRegister").actionPerformed={

    rCash += 50
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}

system.gui.getButton("cashBnt100","CashRegister").actionPerformed={

    rCash += 100
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}

system.gui.getButton("cashBnt500","CashRegister").actionPerformed={

    rCash += 500
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}

system.gui.getButton("cashBnt1000","CashRegister").actionPerformed={
    rCash += 1000
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}

system.gui.getButton("buttonC","CashRegister").actionPerformed={
    rCash =0.0
    updateReturnCash(rCash,system.gui.getEditor("cashView","CashRegister").getText())
}

/*system.gui.getVisualPanel("CashRegister").getPanel().drawRepeatedImageWidth(new ImageIcon("images/cashBG.png").getImage(),
520,233, system.gui.getVisualPanel("CashRegister").getPanel().getWidth()-10,10,null)
system.gui.getVisualPanel("CashRegister").getPanel().drawRepeatedImageWidth(new ImageIcon("images/cashBG1.png").getImage(),
155,297, 510,10,null)*/


