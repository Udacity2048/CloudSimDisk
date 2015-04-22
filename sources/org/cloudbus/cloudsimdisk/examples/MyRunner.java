package org.cloudbus.cloudsimdisk.examples;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsimdisk.models.hdd.StorageModelHdd;
import org.cloudbus.cloudsimdisk.power.models.hdd.PowerModelHdd;
import org.cloudbus.cloudsimdisk.util.WriteToLogFile;
import org.cloudbus.cloudsimdisk.util.WriteToResultFile;

/**
 * A Runner to run storage examples.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyRunner {

	/**
	 * An Helper for the Runner.
	 */
	public Helper	helper				= new Helper();

	/**
	 * End Time of the simulation.
	 */
	public double	endTimeSimulation	= 0.0;

	/**
	 * Create a Runner to run a MyExampleX scenario.
	 * 
	 * @param name
	 * @param type
	 * @param NumberOfRequest
	 * @param RequestArrivalDistri
	 * @param requiredFiles
	 * @param dataFiles
	 * @param startingFilesList
	 * @param NumberOfDisk
	 * @param hddModel 
	 * @param hddPowerModel 
	 * @throws Exception
	 */
	public MyRunner(String name, String type, int NumberOfRequest, String RequestArrivalDistri, String requiredFiles,
			String dataFiles, String startingFilesList, int NumberOfDisk, StorageModelHdd hddModel,
			PowerModelHdd hddPowerModel) throws Exception {

		// BEGIN
		Log.printLine("Starting simulation \"" + name + "\"\n");
		WriteToLogFile.AddtoFile("Starting simulation \"" + name + "\"\n");
		WriteToResultFile.init();

		init(NumberOfRequest, type, RequestArrivalDistri, requiredFiles, dataFiles, startingFilesList, NumberOfDisk,
				hddModel, hddPowerModel);
		start();
		print();

		WriteToResultFile.end();
		Log.printLine("END !");
		// END
	}

	/**
	 * Initialize the simulation.
	 * 
	 * @param NumberOfRequest
	 *            the number of request
	 * @param type
	 *            type of distribution
	 * @param RequestArrivalDistri
	 *            the request distribution
	 * @param requiredFiles
	 * @param dataFiles
	 * @param startingFilesList
	 * @param NumberOfDisk
	 * @param hddModel 
	 * @param hddPowerModel 
	 * @throws Exception
	 */
	public void init(int NumberOfRequest, String type, String RequestArrivalDistri, String requiredFiles,
			String dataFiles, String startingFilesList, int NumberOfDisk, StorageModelHdd hddModel,
			PowerModelHdd hddPowerModel) throws Exception {

		// Entities
		helper.initCloudSim();
		helper.createBroker(type, RequestArrivalDistri);
		helper.createPeList(1);
		helper.createHostList(1);
		helper.createVmList(1);
		helper.createPersistentStorage(NumberOfDisk, hddModel, hddPowerModel);
		helper.createDatacenterCharacteristics();
		helper.createDatacenter();

		// Files
		helper.addFiles(startingFilesList);
		helper.createRequiredFilesList(requiredFiles);
		helper.createDataFilesList(dataFiles);

		// Cloudlets
		helper.createCloudletList(NumberOfRequest);

		// Logs
		helper.printPersistenStorageDetails();
	}

	/**
	 * Start the simulation.
	 */
	public void start() {
		endTimeSimulation = CloudSim.startSimulation();
	}

	/**
	 * Print the Results.
	 */
	public void print() {
		helper.printResults(endTimeSimulation);
	}
}
