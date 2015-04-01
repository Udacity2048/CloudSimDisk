package org.cloudbus.cloudsim.examples.storage;

/**
 * A basic example with 3 requests in a time interval of 1 second.
 * 
 * @author Baptiste Louis
 */
public class MyExample1 {
	
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
		String name = "Basic test example"; // name of the simulation
		String type = "unif"; // type of the workload
		int NumberOfRequest = 3; // Number of requests
		String RequestArrivalDistri = ""; // No file source for time distribution
		String requiredFiles = ""; // No files required
		String dataFiles = "ex1DataFiles.txt"; // Hypothetical Wikipedia dataFiles
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
