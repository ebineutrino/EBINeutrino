package org.sdk.interfaces;

/**
 *
 * Extension delegation
 */
public interface IEBIExtension {
    boolean ebiMain(Object obj);
    Object ebiRemove();
    void onExit();
    void onLoad();
    void onAfterLoad();
}
