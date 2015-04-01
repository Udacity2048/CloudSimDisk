package org.cloudbus.cloudsim.examples.storage;

/**
 * A basic example.
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
		String RequestArrivalDistri = ""; // fileName which contains Times Distributions based on Wikipedia workload format.
		String dataFiles = "ex1DataFiles.txt"; // Hypothetical Wikipedia dataFiles
		String startingFilesList = ""; // No files to start
		
		// Execution
		new MyRunner(
				name,
				type,
				NumberOfRequest,
				RequestArrivalDistri,
				dataFiles,
				startingFilesList);
	}
	
}
