package ebiNeutrinoSDK.interfaces;

/**
 * Each EBINeutrino business module should implement this interface otherwise the system
 * would't recognize an extension as business module
 *
 */

public interface IEBIExtension {

	boolean ebiMain(Object obj);
	Object ebiRemove();
    void onExit();
    void onLoad();
	
}
