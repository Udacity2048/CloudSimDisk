package org.cloudbus.cloudsim.examples.storage;

/**
 * A first example based on Wikipedia workload.
 * 
 * @author Baptiste Louis
 */
public class MyExampleWikipedia1 {
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 */
	public static void main(
			String[] args) throws Exception {
		
		// Parameters (Constant)
		int NumberOfRequest = 5; //MyConstants.CLOUDLET_NUMBER_WIKI; // Number of requests
		
		// Parameters (Variable)
		String RequestArrivalDistri = "wiki"; // Wikipedia Distribution
		String dataFiles = "wikiDataFiles"; // Wikipedia dataFiles
		String startingFilesList = ""; //No files to start
		
		// Execution
		new MyRunner(
				NumberOfRequest,
				RequestArrivalDistri,
				dataFiles,
				startingFilesList);
	}
	
}
