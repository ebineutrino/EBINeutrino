package ebiNeutrino.core.guiRenderer;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;


public final class EBIFocusTraversalPolicy extends FocusTraversalPolicy{

    private HashMap<Integer,Component> focuscomp = null;

    public EBIFocusTraversalPolicy(){
      focuscomp = new HashMap<Integer,Component>();
    }


    public final void addComponent(final int index, final Component comp){
       this.focuscomp.put(index,comp);
    }

    @Override
	public final Component getComponentAfter(final Container focusCycleRoot,
                                       final Component aComponent)
    {
        Component cmp = aComponent;

        if(aComponent.toString().indexOf("ComboBox") != -1 && aComponent.toString().indexOf("JComboBox") == -1){
            cmp = aComponent.getParent();
        }else if(aComponent.toString().indexOf("DatePicker") != -1 ){
            cmp = aComponent.getParent();
        }

        final Iterator iter = this.focuscomp.keySet().iterator();

        while(iter.hasNext()){
            final int idx = (Integer) iter.next();
            
            if(this.focuscomp.get(idx) == cmp){
                cmp = this.focuscomp.get(idx +1);
                break;
            }
        }

        return cmp;
    }

    @Override
	public final Component getComponentBefore(final Container focusCycleRoot,
                                        final Component aComponent)
    {

         Component cmp = aComponent;

        if(aComponent.toString().indexOf("ComboBox") != -1 && aComponent.toString().indexOf("JComboBox") == -1){
            cmp = aComponent.getParent();
        }else if(aComponent.toString().indexOf("DatePicker") != -1 ){
            cmp = aComponent.getParent();
        }

        final Iterator iter = this.focuscomp.keySet().iterator();

        while(iter.hasNext()){
            final int idx = (Integer) iter.next();

            if(this.focuscomp.get(idx) == cmp){
                int id = idx -1;
                if(id < 0){
                    id = 0;
                }
                cmp = this.focuscomp.get(id);
                break;
            }
        }

        return cmp;
    }

    @Override
	public final Component getDefaultComponent(final Container focusCycleRoot) {
      if(focuscomp.get(1) != null){
        focuscomp.get(1).requestFocus();
      }
        return focuscomp.get(1);
    }

    @Override
	public final Component getLastComponent(final Container focusCycleRoot) {
        return focuscomp.get(focuscomp.size()+1);
    }

    @Override
	public final Component getFirstComponent(final Container focusCycleRoot) {
       if(focuscomp.get(1) != null){
        focuscomp.get(1).requestFocus();
       }
        return focuscomp.get(1);
    }

    public final boolean isEmpty(){
        return focuscomp.isEmpty();
    }
}