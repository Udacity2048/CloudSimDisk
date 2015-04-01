package org.cloudbus.cloudsim.examples.storage;

/**
 * A Wikipedia example based on Wikipedia workload.
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
		
		// Parameters
		String name = "simulationWiki1"; // name of the simulation
		String type = "wiki"; // type of the workload
		int NumberOfRequest = MyConstants.CLOUDLET_NUMBER_WIKI; // Number of requests
		String RequestArrivalDistri = "wiki.1190153705"; // wikipedia workload with time-Stamps
		String requiredFiles = ""; // No files required
		String dataFiles = "wikipedia/wikiDataFiles.txt"; // File path of the "Hypothetical" Wikipedia dataFiles.
		String startingFilesList = ""; // No files to start
		
		// Execution
		new MyRunner(
				name,
				type,
				NumberOfRequest,
				RequestArrivalDistri,
				requiredFiles,
				dataFiles,
				startingFilesList);
	}
	
}
