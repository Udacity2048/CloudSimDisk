package org.cloudbus.cloudsimdisk.examples;

import org.cloudbus.cloudsimdisk.models.hdd.StorageModelHdd;
import org.cloudbus.cloudsimdisk.power.models.hdd.PowerModelHdd;

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
		String nameOfTheSimulation = "Basic Example 0"; // name of the simulation
		String requestArrivalRateType = "basic"; // type of the workload
		String requestArrivalTimesSource = "basic/example0/ex0RequestArrivalDistri.txt"; // time distribution
		int numberOfRequest = 1; // Number of requests
		String requiredFiles = ""; // No files required
		String dataFiles = "basic/example0/ex0DataFiles.txt"; // dataFile Name and Size
		String startingFilesList = ""; // No files to start
		int numberOfDisk = 1; // Number of disk in the persistent storage
		StorageModelHdd hddModel = MyConstants.STORAGE_MODEL_HDD; // model of disks in the persistent storage
		PowerModelHdd hddPowerModel = MyConstants.STORAGE_POWER_MODEL_HDD; // power model of disks

		// Execution
		new MyRunner(nameOfTheSimulation, requestArrivalRateType, numberOfRequest, requestArrivalTimesSource, requiredFiles, dataFiles, startingFilesList,
				numberOfDisk, hddModel, hddPowerModel);
	}

}
