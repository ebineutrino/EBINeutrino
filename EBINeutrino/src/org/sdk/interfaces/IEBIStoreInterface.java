package org.sdk.interfaces;

/**
 * Is used to help save data
 * Also it remembers the user with a message dialog
 */
public interface IEBIStoreInterface {
	/**
	 * ebiSave
	 * @return
	 */
	boolean ebiSave(boolean check);
	/**
	 * ebiUpdate
	 * @return
	 */
	boolean ebiUpdate(boolean check);
	/**
	 * ebiDelete
	 * @return
	 */
	boolean ebiDelete(boolean check);
}
