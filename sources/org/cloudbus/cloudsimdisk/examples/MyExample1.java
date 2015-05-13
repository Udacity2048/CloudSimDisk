package org.cloudbus.cloudsimdisk.examples;

import org.cloudbus.cloudsimdisk.models.hdd.StorageModelHdd;
import org.cloudbus.cloudsimdisk.power.models.hdd.PowerModelHdd;

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
		String nameOfTheSimulation = "Basic Example 1"; // name of the simulation
		String requestArrivalRateType = "basic"; // type of the workload
		String requestArrivalTimesSource = "basic/example1/ex1RequestArrivalDistri.txt"; // time distribution
		int numberOfRequest = 3; // Number of requests (MAX: 9)
		String requiredFiles = ""; // No files required
		String dataFiles = "basic/example1/ex1DataFiles.txt"; // dataFiles Names and Sizes
		String startingFilesList = ""; // No files to start
		int numberOfDisk = 1; // 1 HDD
		StorageModelHdd hddModel = MyConstants.STORAGE_MODEL_HDD; // model of disks in the persistent storage
		PowerModelHdd hddPowerModel = MyConstants.STORAGE_POWER_MODEL_HDD; // power model of disks

		// Execution
		new MyRunner(nameOfTheSimulation, requestArrivalRateType, numberOfRequest, requestArrivalTimesSource,
				requiredFiles, dataFiles, startingFilesList, numberOfDisk, hddModel, hddPowerModel);
	}

}
