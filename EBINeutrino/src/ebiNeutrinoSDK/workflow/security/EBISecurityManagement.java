package ebiNeutrinoSDK.workflow.security;

import ebiNeutrinoSDK.EBISystem;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.interfaces.IEBISecurity;
import ebiNeutrinoSDK.interfaces.IEBISystemUserRights;

public class EBISecurityManagement implements IEBISecurity {

    private IEBISystemUserRights iuserRights = null;

    /**
     * check if the module has changed.
     *
     * @return : boolean
     */
    @Override
	public boolean checkCanReleaseModules() {
        boolean canProceed;
        iuserRights = EBISystem.getInstance().getIEBISystemUserRights();
        try {
            if (EBISystem.canRelease == false) {
                if (EBIExceptionDialog.getInstance(EBISystem.i18n("EBI_LANG_MESSAGES_HAVE_CHANGES")).Show(EBIMessage.INFO_MESSAGE_YESNO) == true) {
                    if (!iuserRights.isCanSave() || !iuserRights.isAdministrator()) {
                        canProceed = secureModule();
                    } else {
                        canProceed = true;
                    }
                    if (canProceed) {
                        if (EBISystem.getCRMModule().storeAutomate.storeFromSelectedTab()) {
                           EBISystem.canRelease = true;
                        }
                    } else {
                        EBISystem.canRelease = false;
                    }
                } else {
                    EBISystem.canRelease = true;
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return EBISystem.canRelease;
    }

    /**
     * show a password dialog if not superuser
     *
     * @return boolean
     */
    @Override
	public boolean secureModule() {
        boolean passing;
        iuserRights = EBISystem.getInstance().getIEBISystemUserRights();
        if (!this.iuserRights.isAdministrator()) {
            final ebiNeutrinoSDK.gui.dialogs.EBIAdminPassword pwdDialog = new ebiNeutrinoSDK.gui.dialogs.EBIAdminPassword();
            pwdDialog.setVisible(true);

            if (pwdDialog.isOK == true) {
                passing = true;
            } else {
                passing = false;
            }
        } else {
            passing = true;
        }
        return passing;
    }
}
