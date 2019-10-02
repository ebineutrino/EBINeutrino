package ebiNeutrinoSDK.interfaces;

import javax.swing.*;

/**
 * EBI Neutrino GUI ToolBar interface#
 * Allow to access the EBI Neutrino Toolbar
 *
 */

public interface IEBIToolBar {

    
    public JToolBar getJToolBar();

    /**
	 * Insert a toolbar button
	 * @param icon
	 * @param listener
	 * @return  Inserted ID
	 */
	public int  addToolButton(ImageIcon icon,java.awt.event.ActionListener listener);
	/**
	 * Insert a custom component
	 * @param component  JComponent parameter
	 * @return  Inserted ID
	 */
	public int  addCustomToolBarComponent(JComponent component);
	
	/**
	 * Insert a toolbar separator
	 */
	public void addButtonSeparator();
	/**
	 * Add ToolTipp text to the ToolBar component ID
	 * @param id
	 * @param text
	 */
	public void setComponentToolTipp(int id, String text);
	/**
	 * Enable or disable the toolbar component
	 * @param id
	 * @param enabled
	 */
	public void setComponentToolBarEnabled(int id, boolean enabled);


    /**
     * Return a toolbar component
     * @param id
     * @return
     */

    public JComponent getToolbarComponent(int id);



    /**
     * Return a toolbar button
     * @param id
     * @return
     */
    public JComponent getToolbarButton(int id);

    /**
	 * Show the toolbar
	 */

    public void showToolBar(boolean mainWindows);
	
	/**
	 * Remove all Component from the toolbar
	 */
	
	public void resetToolBar();
}
