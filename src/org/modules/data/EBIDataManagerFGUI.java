package org.modules.data;

import org.core.guiRenderer.EBIGUIWidgetsBean;

import java.util.Iterator;
import org.sdk.EBISystem;

public class EBIDataManagerFGUI {
    
    public void packageData(final String packg) {
        final EBIGUIWidgetsBean mainWidget = EBISystem.getInstance().getIEBIBuilderInstance().getGUIComponents(packg);
        iterateWidget(mainWidget.getSubWidgets().iterator());
    }

    public void iterateWidget(final Iterator<EBIGUIWidgetsBean> widgetIter) {
        while (widgetIter.hasNext()) {
            final EBIGUIWidgetsBean widget = widgetIter.next();
            if (widget.isStorable()) {
                System.out.println(widget.getName() + ": " + widget.getValue());
            }
            if (widget.getSubWidgets().size() > 0) {
                iterateWidget(widget.getSubWidgets().iterator());
            }
        }
    }
}

