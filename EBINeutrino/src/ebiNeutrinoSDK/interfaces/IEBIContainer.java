package ebiNeutrinoSDK.interfaces;

import javax.swing.*;

/**
 * Interface that help manipulate the EBIContainer (JTabbedPane)
 *
 */

public interface IEBIContainer {

	/**
	 * Add a component to the EBINeutrino Container
	 * @param  String title 		: Title for the container
	 * @param  JComponent component : Component instance
	 * @param  ImageIcon icon		: Container Icon
	 * @param  int mnemo_key	    : Mnemonic key
	 * @return : void
	 */
	int addContainer(String title,JComponent component,ImageIcon icon,int mnemo_key);

	/**
	 * Add scrollable component to the EBINeutrino Container
	 * @param  String title 		: Container Title
	 * @param  JComponent component : Component instance
	 * @param  ImageIcon icon		: Container Icon
	 * @param  int mnemo_key	    : Mnemonic key
	 * @return index
	 */

	int addScrollableContainer(String title,JComponent component,ImageIcon icon,int mnemo_key);


	/**
	 * Add scrollable closable component to the EBINeutrino Container
	 * @param  String title 		: Container Title
	 * @param  JComponent component : Component instance
	 * @param  ImageIcon icon		: Container Icon
	 * @param  int mnemo_key	    : Mnemonic key
	 * @return
	 */

	int addScrollableClosableContainer(String title,JComponent component,ImageIcon icon,int mnemo_key, CloseableTabbedPaneListener l);


	/**
	 * Remove component from the EBINeutrino Container
	 * @param  index   Index from the container
	 * @return void
	 */
	void removeContainer(int index);

	/**
	 * Remove all from Container
	 * @return
	 */

	void removeAllFromContainer();

	/**
	 * get the selected tab component
	 * @return : int index selected tab
	 */
	int getSelectedTab();

	/**
	 * set selected tab component
     * @param  index   Index from the container
	 * @return : void
	 */

	void setSelectedTab(int index);

	/**
	 * get count
	 * @return : int  number of available tab
	 */

	int getTabCount();

	/**
	 * get the main tabbedPane instance
	 * @return  JTabbedPane
	 */

	JTabbedPane getTabInstance();


    /***
     * Return index by container tile
     * @param title
     * @return
     */

    int getIndexByTitle(String title);

    /***
     * Return the component name of selected tab
     * @param index
     * @return
     */
    String getComponentName(int index);

}
