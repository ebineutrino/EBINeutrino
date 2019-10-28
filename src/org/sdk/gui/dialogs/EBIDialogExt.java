package org.sdk.gui.dialogs;

import org.core.EBIMain;
import org.sdk.utils.EBIPropertiesDialogRW;

/**
 *
 * Extends the EBIDilaog
 */
public class EBIDialogExt extends EBIDialog {

    public EBIDialogExt(final EBIMain owner) {
        super(owner);
        setFocusableWindowState(true);
        setFocusable(true);
        //pack();
    }

    public EBIDialogExt(final EBIMain owner, final EBIPropertiesDialogRW properties) {
        super(owner, properties);
        setFocusableWindowState(true);
        setFocusable(true);
        //pack();
    }
}
