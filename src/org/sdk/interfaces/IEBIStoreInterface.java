package org.sdk.interfaces;

public interface IEBIStoreInterface {
    /**
     * ebiSave
     *
     * @return
     */
    boolean ebiSave(boolean check);

    /**
     * ebiUpdate
     *
     * @return
     */
    boolean ebiUpdate(boolean check);

    /**
     * ebiDelete
     *
     * @return
     */
    boolean ebiDelete(boolean check);
}
