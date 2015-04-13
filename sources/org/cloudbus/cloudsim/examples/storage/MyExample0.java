package org.cloudbus.cloudsim.examples.storage;

import org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * Example 0: this example aims to understand the basic operation of storage example. In this scenario, 1 request is
 * sent to the Datacenter at 0.5 second and adds FileA(1MB) on the persistent storage. No files are retrieved.
 * 
 * @author Baptiste Louis
 */
public class MyExample0 {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Parameters
		String name = "Basic Example 0"; // name of the simulation
		String type = "basic"; // type of the workload
		int NumberOfRequest = 1; // Number of requests
		String RequestArrivalDistri = "basic/example0/ex0RequestArrivalDistri.txt"; // time distribution
		String requiredFiles = ""; // No files required
		String dataFiles = "basic/example0/ex0DataFiles.txt"; // dataFile Name and Size
		String startingFilesList = ""; // No files to start
		int NumberOfDisk = 1; // Number of disk in the persistent storage
		StorageModelHdd hddModel = MyConstants.STORAGE_MODEL_HDD; // model of disks in the persistent storage
		PowerModelHdd hddPowerModel = MyConstants.STORAGE_POWER_MODEL_HDD; // power model of disks

		// Execution
		new MyRunner(name, type, NumberOfRequest, RequestArrivalDistri, requiredFiles, dataFiles, startingFilesList,
				NumberOfDisk, hddModel, hddPowerModel);
	}

}
