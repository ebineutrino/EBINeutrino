package org.sdk.arbitration;

import java.util.LinkedHashMap;

final public class EBIArbitration {

    private static EBIArbitration arbitration = null;
    private final LinkedHashMap<String,EBIArbCallback> callbackQueue = new LinkedHashMap();
    private final LinkedHashMap<String,Thread> threadQueue = new LinkedHashMap();

    public final void begin(final String name, final EBIArbCallback arb){
        callbackQueue.put(name, arb);
        final Thread actThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Thread currentThread = Thread.currentThread();
                callbackQueue.get(currentThread.getName()).callback(currentThread);
                callbackQueue.get(currentThread.getName()).setDone(true);
            }
        });
        threadQueue.put(name,actThread);
        actThread.setName(name);
        actThread.start();
    }

    public final boolean isJobDone(final String name){
        boolean isDone = false;
        if(callbackQueue.containsKey(name) && callbackQueue.get(name) != null){
           isDone  =  callbackQueue.get(name).isDone();
        }
        return isDone;
    }

    public final void waitJobDone(final String name){
        while(isJobDone(name) != true){
            try {
                Thread.currentThread();
				Thread.sleep(20);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public final Thread getThread(final String name){
        Thread aThread = null;
        if(threadQueue.containsKey(name) && threadQueue.get(name) != null){
            aThread  =  threadQueue.get(name);
        }
        return aThread;
    }

    public final static EBIArbitration arbitrate(){
        if(arbitration == null){
            arbitration = new EBIArbitration();
        }
        return arbitration;
    }
}
