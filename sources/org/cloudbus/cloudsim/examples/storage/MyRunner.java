package org.cloudbus.cloudsim.examples.storage;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.PrintFile;

/**
 * @author baplou
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
	 * Runner to run a MyExampleX scenario.
	 * 
	 * @param name
	 * @param type 
	 * @param NumberOfRequest
	 * @param RequestArrivalDistri
	 * @param dataFiles
	 * @param startingFilesList
	 * @throws Exception
	 */
	public MyRunner(
			String name,
			String type,
			int NumberOfRequest,
			String RequestArrivalDistri,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		Log.printLine("Starting simulation \"" + name + "\"\n");
		PrintFile.AddtoFile("Starting simulation \"" + name + "\"\n");
		
		init(NumberOfRequest,
				type,
				RequestArrivalDistri,
				dataFiles,
				startingFilesList);
		start();
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
			String type,
			String RequestArrivalDistri,
			String dataFiles,
			String startingFilesList) throws Exception {
		
		// Entities
		helper.initCloudSim();
		helper.createBroker(type, RequestArrivalDistri);
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
		
		// Logs
		helper.printPersistenStorageDetails();
	}
	
	/**
	 * Start the simulation
	 */
	public void start() {
		endTimeSimulation = CloudSim.startSimulation();
	}
	
	/**
	 * Print the Results and the Request Arrival Times History
	 */
	public void print() {
		helper.printResults(endTimeSimulation);
	}
}
