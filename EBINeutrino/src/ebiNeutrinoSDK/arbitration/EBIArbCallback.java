package ebiNeutrinoSDK.arbitration;

public abstract class EBIArbCallback {

    private boolean isDone = false;

    public void setDone(final boolean done){
        isDone = done;
    }

    public boolean isDone(){
        return isDone;
    }

    public abstract boolean callback(final Thread currentThread);
}
