package ebiNeutrinoSDK.interfaces;

import java.io.File;
import java.util.Map;

/**
 * EBI Neutrino to JasperReport interface
 */

public interface IEBIReportSystem {
	
	/**
	 * Build a report file
	 * @param reportFile
	 */
	public void buildReport(final File reportFile);
	
	/**
	 * Build all reports
	 */
	public void buildReports();
	
	/**
	 * Show you a dialog for selecting all available reports
	 */
	public void useReportSystem(Map<String,Object> map);
	
	/**
	 * show the selected report category with report parameters
	 * @param map
	 * @param category
	 */
	public void useReportSystem(Map<String,Object> map,String category, String fileName);

    /**
     * show the specified report with parameter
     * @param map
     * @param category
     * @param fileName
     * @param createOnlyReportDontShowWindow
     * @return
     */
    public String useReportSystem(Map<String,Object> map,String category,String fileName,boolean createOnlyReportDontShowWindow, boolean MailRecord, String recs);
}
