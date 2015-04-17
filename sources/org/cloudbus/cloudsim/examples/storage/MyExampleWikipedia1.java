package org.cloudbus.cloudsim.examples.storage;

import org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * Example Wikipedia 1: this example use wikipedia workload for the time distribution of requests. In this scenario,
 * 5000 requests are sent to the Datacenter. Each requests adds 1 File name "wikiFileXXXX" on the persistent storage.
 * The size of the File is variable between [1 ; 10[ MB. No files are retrieved.
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
	public static void main(String[] args) throws Exception {

		// Parameters
		String name = "simulationWiki1"; // name of the simulation
		String type = "wiki"; // type of the workload
		int NumberOfRequest = MyConstants.CLOUDLET_NUMBER_WIKI; // Number of requests
		String RequestArrivalDistri = "wikipedia/wiki.1190153705"; // wikipedia workload with time-Stamps
		String requiredFiles = ""; // No files required
		String dataFiles = "wikipedia/wikiDataFiles.txt"; // File path of the "Hypothetical" Wikipedia dataFiles.
		String startingFilesList = ""; // No files to start
		int NumberOfDisk = 2; // 1 HDD
		StorageModelHdd hddModel = MyConstants.STORAGE_MODEL_HDD; // model of disks in the persistent storage
		PowerModelHdd hddPowerModel = MyConstants.STORAGE_POWER_MODEL_HDD; // power model of disks

		// Execution
		new MyRunner(name, type, NumberOfRequest, RequestArrivalDistri, requiredFiles, dataFiles, startingFilesList,
				NumberOfDisk, hddModel, hddPowerModel);
	}

}
