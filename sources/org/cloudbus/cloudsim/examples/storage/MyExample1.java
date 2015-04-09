package org.cloudbus.cloudsim.examples.storage;

/**
 * Example 1: this example aims to understand the basic operation of storage example. In this scenario, 3 requests are
 * sent to the Datacenter respectively at 0.2, 0.4 and 0.7 second(s). Each requests adds respectively FileA(1MB),
 * FileB(2MB) and FileC(3MB) on the persistent storage. No files are retrieved.
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
	public static void main(String[] args) throws Exception {

		// Parameters
		String name = "Basic Example 1"; // name of the simulation
		String type = "basic"; // type of the workload
		int NumberOfRequest = 3; // Number of requests
		String RequestArrivalDistri = "basic/example1/ex1RequestArrivalDistri.txt"; // time distribution
		String requiredFiles = ""; // No files required
		String dataFiles = "basic/example1/ex1DataFiles.txt"; // dataFiles Names and Sizes
		String startingFilesList = ""; // No files to start

		// Execution
		new MyRunner(name, type, NumberOfRequest, RequestArrivalDistri, requiredFiles, dataFiles, startingFilesList);
	}

}
