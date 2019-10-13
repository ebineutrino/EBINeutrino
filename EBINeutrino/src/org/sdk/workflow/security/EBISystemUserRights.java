package org.sdk.workflow.security;

import org.sdk.interfaces.IEBISystemUserRights;

/**
 * This is a new version of the EBINeutrino user management functionality
 *
 */

public class EBISystemUserRights implements IEBISystemUserRights {

	private boolean isAdministrator;
	private boolean canSave;
	private boolean canDelete;
	private boolean canPrint;
	private boolean canView;
	private String userName;
	
	
	public EBISystemUserRights(){
		isAdministrator = true;
		canSave = true;
		canDelete = true;
		canPrint = true;
		canView = true;
		userName = "";
	}

	public void setUserName(final String userName){
	  this.userName =  userName;
	}
	
	@Override
	public String getUserName(){
		return this.userName;
	}

	@Override
	public boolean isAdministrator() {
		return isAdministrator;
	}


	public void setAdministrator(final boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}


	@Override
	public boolean isCanSave() {
		return canSave;
	}


	public void setCanSave(final boolean canSave) {
		this.canSave = canSave;
	}


	@Override
	public boolean isCanDelete() {
		return canDelete;
	}


	public void setCanDelete(final boolean canDelete) {
		this.canDelete = canDelete;
	}


	@Override
	public boolean isCanPrint() {
		return canPrint;
	}


	public void setCanPrint(final boolean canPrint) {
		this.canPrint = canPrint;
	}


	@Override
	public boolean isCanView() {
		return canView;
	}


	public void setCanView(final boolean canView) {
		this.canView = canView;
	}
	
	
}
