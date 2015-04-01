package org.cloudbus.cloudsim.examples.storage;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.PrintFile;

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
	 * @throws Exception
	 */
	public MyRunner(
			String name,
			String type,
			int NumberOfRequest,
			String RequestArrivalDistri,
			String requiredFiles,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		// BEGIN
		Log.printLine("Starting simulation \"" + name + "\"\n");
		PrintFile.AddtoFile("Starting simulation \"" + name + "\"\n");
		
		init(NumberOfRequest,
				type,
				RequestArrivalDistri,
				requiredFiles,
				dataFiles,
				startingFilesList);
		start();
		print();
		
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
	 * @throws Exception
	 */
	public void init(
			int NumberOfRequest,
			String type,
			String RequestArrivalDistri,
			String requiredFiles,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		// Entities
		helper.initCloudSim();
		helper.createBroker(type,
				RequestArrivalDistri);
		helper.createPeList(1);
		helper.createHostList(1);
		helper.createVmList(1);
		helper.createPersistentStorage(1);
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
