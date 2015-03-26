package org.cloudbus.cloudsim.examples.storage;

/**
 * A first example.
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
		int NumberOfRequest = 4; // Number of requests
		String RequestArrivalDistri = "expo"; // Exponential Distribution
		String FilesSizes = "1"; // Sizes (MB) of the files to write
		String startingFilesList = "startingFilesListDefault";
		
		new MyRunner(
				NumberOfRequest,
				RequestArrivalDistri,
				FilesSizes,
				startingFilesList);
	}
	
}
