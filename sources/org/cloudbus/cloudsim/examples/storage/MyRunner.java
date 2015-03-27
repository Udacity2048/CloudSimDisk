package org.cloudbus.cloudsim.examples.storage;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

/**
 * @author baplou
 * 
 */
public class MyRunner {
	
	/**
	 * An Helper for the Runner.
	 */
	public Helper	helper	= new Helper();
	
	/**
	 * Runner to run a MyExampleX scenario.
	 * 
	 * @param NumberOfRequest
	 * @param RequestArrivalDistri
	 * @param dataFiles
	 * @param startingFilesList
	 * @throws Exception
	 */
	public MyRunner(
			int NumberOfRequest,
			String RequestArrivalDistri,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		Log.printLine("Starting ...");
		init(NumberOfRequest,
				RequestArrivalDistri,
				dataFiles,
				startingFilesList);
		start();
		stop();
		print();
		Log.printLine("END !");
	}
	
	/**
	 * Initialize the simulation.
	 * 
	 * @param NumberOfRequest
	 *            the number of request
	 * @param RequestArrivalDistri
	 *            the request distribution
	 * @param dataFiles
	 * @param startingFilesList
	 * @throws Exception
	 */
	public void init(
			int NumberOfRequest,
			String RequestArrivalDistri,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		// Entities
		helper.initCloudSim();
		helper.createBroker(RequestArrivalDistri);
		helper.createPeList(1);
		helper.createHostList(1);
		helper.createVmList(1);
		helper.createPersistentStorage(1);
		helper.createDatacenterCharacteristics();
		helper.createDatacenter();
		
		// Files
		helper.addFiles(startingFilesList);
		helper.createDataFilesList(dataFiles);
		
		// TO IMPROVE: HOW TO get the requiredFiles and dataFiles from a file.
		List<String> requiredFiles = new ArrayList<String>();
		requiredFiles.add("shortFile");
		
		// If wikidataFiles is not working, use that for the moment
		List<File> tempdataFiles = new ArrayList<File>();
		tempdataFiles.add(new File(
				"TheImitationGame",
				3600));
		
		// Cloudlets
		helper.createCloudletList(NumberOfRequest,
				requiredFiles);
		
		// Printing
		helper.printPersistenStorageDetails();
	}
	
	/**
	 * Start the simulation
	 */
	public void start() {
		CloudSim.startSimulation();
	}
	
	/**
	 * Stop the simulation.
	 */
	public void stop() {
		CloudSim.stopSimulation();
	}
	
	/**
	 * Print the Results and the Request Arrival Times History
	 */
	public void print() {
		helper.printResults();
		helper.printArrivalRate();
	}
}
