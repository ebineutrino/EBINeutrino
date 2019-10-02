package ebiNeutrinoSDK.interfaces;

/**
 * Interface retrieve the user rights information
 *
 */

public interface IEBISystemUserRights {
	public boolean isAdministrator();
	public boolean isCanSave();
	public boolean isCanDelete();
	public boolean isCanPrint();
	public boolean isCanView();
	public String getUserName();
}
