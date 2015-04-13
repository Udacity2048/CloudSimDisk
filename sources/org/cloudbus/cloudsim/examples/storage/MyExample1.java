package org.cloudbus.cloudsim.examples.storage;

import org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * Example 1: this example aims to understand the basic operation of storage example. In this scenario, 1 to 9 requests
 * can be sent to the Datacenter respectively at 0.1, 0.2 ... 0.9 second(s). Each requests adds respectively FileA(1MB),
 * FileB(2MB) ... FileI(9MB) on the persistent storage. No files are retrieved.
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
		int NumberOfRequest = 9; // Number of requests
		String RequestArrivalDistri = "basic/example1/ex1RequestArrivalDistri.txt"; // time distribution
		String requiredFiles = ""; // No files required
		String dataFiles = "basic/example1/ex1DataFiles.txt"; // dataFiles Names and Sizes
		String startingFilesList = ""; // No files to start
		int NumberOfDisk = 1; // 1 HDD
		StorageModelHdd hddModel = MyConstants.STORAGE_MODEL_HDD; // model of disks in the persistent storage
		PowerModelHdd hddPowerModel = MyConstants.STORAGE_POWER_MODEL_HDD; // power model of disks

		// Execution
		new MyRunner(name, type, NumberOfRequest, RequestArrivalDistri, requiredFiles, dataFiles, startingFilesList,
				NumberOfDisk, hddModel, hddPowerModel);
	}

}
