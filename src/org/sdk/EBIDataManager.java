package org.sdk;

import org.core.guiRenderer.EBIGUIWidgetsBean;

import java.util.Iterator;

public class EBIDataManager {
    
    public void STORE(final String packg, final String TABLE) {
        final EBIGUIWidgetsBean mainWidget = EBISystem.getInstance().getIEBIGUIRendererInstance().getGUIComponents(packg);
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

