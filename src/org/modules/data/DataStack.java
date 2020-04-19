package org.modules.data;

import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

public class DataStack {
    @Getter
    @Setter
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> dataStack = new ConcurrentHashMap();
    private static DataStack dtStack = null;
    
    public DataStack() {initTimer();}
    
    private void initTimer(){
        new java.util.Timer().scheduleAtFixedRate(new DataTimer(), 3000, 5000);
    }
    
    
    public boolean resolveData(){
        
        return true;
    }
    
    
    public void addData(String pckg){
        
    }
    
    
    public static DataStack getInstace() {
        DataStack r = dtStack;
        if (r == null) {
            synchronized (dtStack) {
                r = dtStack;
                if (r == null) {
                    r = new DataStack();
                    dtStack = r;
                }
            }
        }
        return r;
    }

}
