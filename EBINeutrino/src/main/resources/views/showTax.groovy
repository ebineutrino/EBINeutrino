import org.hibernate.Query

import java.text.NumberFormat

//Add functionality to a show tax button
system.gui.getButton("taxButton","Account").actionPerformed={
    
    system.gui.loadGUI("CRMDialog/accountShowPTAX.xml")
    system.gui.getEditor("viewtaxText","taxViewDialog").setEditable(false)
    
    system.gui.getButton("closeButton","taxViewDialog").actionPerformed={ 
        system.gui.getEBIDialog("taxViewDialog").setVisible(false)   
    }
    
    system.gui.getButton("viewTax","taxViewDialog").actionPerformed={
        if(!validateInput()){
            return
        }
        String toPrint=""
        //Calculate credit tax
        toPrint =calculateTax(toPrint,1); 
        
        toPrint +="<br><b>__________________________________________________________________________</b><br>"
        
        //Calculate debit tax
        String toPrint2 = "";
        toPrint += calculateTax(toPrint2,2);  
       
        system.gui.getEditor("viewtaxText","taxViewDialog").setText(toPrint)       
    }
    
    system.gui.showGUI()
   
}

boolean validateInput(){
    
    if(system.gui.getTimepicker("dateFromText","taxViewDialog").getEditor().getText().equals("")
                        || system.gui.getTimepicker("dateFromText","taxViewDialog").getDate() == null ){
                            
        system.message.error(system.getLANG("EBI_LANG_MESSAGE_ILLEGAL_DATE"))
        
        return false
    }else if(system.gui.getTimepicker("dateToText","taxViewDialog").getEditor().getText().equals("")
                        || system.gui.getTimepicker("dateToText","taxViewDialog").getDate() == null ){
                            
        system.message.error(system.getLANG("EBI_LANG_MESSAGE_ILLEGAL_DATE"))
        
        return false
    }
    
    return true
}

String calculateTax(String toPrint, int type){
  
        Query query;
        try {

            query = system.hibernate.getHibernateSession("EBIACCOUNT_SESSION").createQuery("from Accountstack ac where ac.accountdate between ? and ? and accountType=?");
            query.setDate(0, system.gui.getTimepicker("dateFromText","taxViewDialog").getDate());
            query.setDate(1, system.gui.getTimepicker("dateToText","taxViewDialog").getDate());
            query.setInteger(2, type);

            Hashtable<String,Double> thash = new Hashtable<String,Double>()
            
            if(query.list().size() > 0){
              
                Iterator iter =  query.iterate();
                while(iter.hasNext()){
                    
                    Accountstack act = (Accountstack) iter.next();
                    if(act.getAccountTaxType() != null && !act.getAccountTaxType().equals(system.getLANG("EBI_LANG_PLEASE_SELECT"))
                       && act.getAccountDValue() > 0){
                        if(!thash.containsKey(act.getAccountTaxType())){
                           thash.put(act.getAccountTaxType(), act.getAccountDValue())  
                        }else{
                           thash.put(act.getAccountTaxType(),(thash.get(act.getAccountTaxType()) + act.getAccountDValue()))
                        }
                    }
                }
            
                Iterator itk =  thash.keySet().iterator()
                
                toPrint+= "<br><div style='background-color:#ebebeb; margin-left:10px; margin-top:10px; margin-right:10px;'><div style='margin-left:5px;'><br><b>"+system.getLANG("EBI_LANG_TAX_TYPE")+": "+(type == 1 ? system.getLANG("EBI_LANG_CREDIT") : system.getLANG("EBI_LANG_DEBIT"))+"</b><br><br>"
                toPrint+="<font color='gray'>________________________________________________________________________</font><br>"
                double tTax = 0.0
                while(itk.hasNext()){
                    
                    String str = (String)itk.next()
                    tTax +=thash.get(str)
                    toPrint+= str +" : "+NumberFormat.getCurrencyInstance().format(thash.get(str))+"<br>"
                }
                toPrint+="<br><font color='gray'>________________________________________________________________________</font><br><br>"
                toPrint+= "<b>"+system.getLANG("EBI_LANG_TOTAL_TAX")+" : "+NumberFormat.getCurrencyInstance().format(tTax)+" </b><br><br></div></div><br>"
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return toPrint;
}
