package ebiNeutrino.core.gui.callbacks;

import javax.swing.event.ListSelectionEvent;

public abstract class EBIUICallback {

    public void selectionListenerEvent(final ListSelectionEvent e){}
    public void tableKeyUp(final int selRow){}
    public void tableKeyDown(final int selRow){}
    public void tableKeyEnter(final int selRow){}

}
