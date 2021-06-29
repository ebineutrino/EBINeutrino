import org.hibernate.query.Query;
import org.sdk.model.hibernate.Accountstack;

import java.text.NumberFormat

//Add functionality to a show tax button
system.gui.button("taxButton","Account").actionPerformed={
    system.gui.loadGUI("CRMDialog/accountShowPTAX.xml")
    system.gui.getEditor("viewtaxText","taxViewDialog").setEditable(false)

    system.gui.button("closeButton","taxViewDialog").actionPerformed={ 
        system.gui.dialog("taxViewDialog").setVisible(false)   
    }

    system.gui.button("viewTax","taxViewDialog").actionPerformed={
        println "called view tax";
        if(!validateInput()){
            return
        }
        String toPrint=""
        //Calculate credit tax
        toPrint =calculateTax(toPrint,1);
        
        //Calculate debit tax
        String toPrint2 = "";
        toPrint += calculateTax(toPrint2,2);
       
        if(toPrint == ""){
            toPrint = system.i18n("EBI_LANG_RECORD_NOT_FOUND"); 
        }
        system.gui.getEditor("viewtaxText","taxViewDialog").setText(toPrint)
    }
    system.gui.showGUI()
}

boolean validateInput(){
    
    if(system.gui.timePicker("dateFromText","taxViewDialog").getEditor().getText().equals("")
        || system.gui.timePicker("dateFromText","taxViewDialog").getDate() == null ){
        system.message.error(system.i18n("EBI_LANG_MESSAGE_ILLEGAL_DATE"))
        return false
    }else if(system.gui.timePicker("dateToText","taxViewDialog").getEditor().getText().equals("")
        || system.gui.timePicker("dateToText","taxViewDialog").getDate() == null ){
        system.message.error(system.i18n("EBI_LANG_MESSAGE_ILLEGAL_DATE"))        
        return false
    }
    return true
}

String calculateTax(String toPrint, int type){
  
    Query query;
    try {

        query = system.hibernate.session("EBIACCOUNT_SESSION").createQuery("from Accountstack ac where ac.accountdate between ?1 and ?2 and accountType=?3");
        query.setDate(1, system.gui.timePicker("dateFromText","taxViewDialog").getDate());
        query.setDate(2, system.gui.timePicker("dateToText","taxViewDialog").getDate());
        query.setInteger(3, type);

        Hashtable<String,Double> thash = new Hashtable<String,Double>()
            
        if(query.list().size() > 0){
            Iterator iter =  query.iterate();
            while(iter.hasNext()){
                Accountstack act = (Accountstack) iter.next();
                String accountTypeName = "";
                if(act.getAccountType() == 1){
                    if(act.getAccountDebitTaxType() != null 
                        && !act.getAccountDebitTaxType().equals(system.i18n("EBI_LANG_PLEASE_SELECT"))
                        && act.getAccountTaxValue() != null){
                        accountTypeName = act.getAccountDebitTaxType();
                    }
                }else{
                    if(act.getAccountCreditTaxType() != null 
                        && !act.getAccountCreditTaxType().equals(system.i18n("EBI_LANG_PLEASE_SELECT"))
                        && act.getAccountTaxValue() != null){
                        accountTypeName = act.getAccountCreditTaxType();
                    }
                }
                if(!thash.containsKey(accountTypeName)){
                    thash.put(accountTypeName, act.getAccountTaxValue())
                }else{
                    thash.put(accountTypeName,(thash.get(accountTypeName) + act.getAccountTaxValue()))
                }
            }
            
            Iterator itk =  thash.keySet().iterator()
            toPrint+= "<br><div style='margin-left:10px; margin-top:10px; margin-right:10px;'><div style='margin-left:5px;'><br><b>"+system.i18n("EBI_LANG_TAX_TYPE")+": "+(type == 1 ? system.i18n("EBI_LANG_CREDIT") : system.i18n("EBI_LANG_DEBIT"))+"</b><br><br>"
            toPrint+="<font color='gray'>________________________________________________________________________</font><br>"
            double tTax = 0.0
            while(itk.hasNext()){
                String str = (String)itk.next()
                tTax +=thash.get(str)
                toPrint+= str +" : "+NumberFormat.getCurrencyInstance().format(thash.get(str))+"<br>"
            }
            toPrint+="<br><font color='gray'>________________________________________________________________________</font><br><br>"
            toPrint+= "<b>"+system.i18n("EBI_LANG_TOTAL_TAX")+" : "+NumberFormat.getCurrencyInstance().format(tTax)+" </b><br><br></div></div><br>"
                
        }
    }catch(Exception ex){
        ex.printStackTrace();
    }
        
    return toPrint;
}