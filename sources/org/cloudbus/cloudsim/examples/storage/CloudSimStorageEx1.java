package org.cloudbus.cloudsim.examples.storage;

/*
 * Title: CloudSim Toolkit Description: CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence: GPL - http://www.gnu.org/copyleft/gpl.html Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenter;

/**
 * A simple example showing how to create a power-aware storage Hard drive, to assign it to a datacenter and to access
 * it during a simulation.
 */
public class CloudSimStorageEx1 {
	
	// Global variable declaration
	
	/** An helper to simplify this class. */
	private static Helper	helper;
	
	/**
	 * The main method is called to run the example. CloudSim is initialized and entities are created. During the
	 * Datacenter creation, a list of storage with one power-aware hard drive is attached. One file named
	 * "TheImitationGame" (3.6 GB) is on this hard drive.
	 * 
	 * @param args
	 *            - the arguments
	 */
	public static void main(String[] args) {
		
		helper = new Helper();
		
		Log.printLine("Starting CloudSimStorageEx1...");
		
		try {
			// MAIN PARAMETERS
			int NumberOfUser = 1;
			int NumberOfCloudlets = 500;
			
			// *FIRST STEP:
			// Initialize CloudSim.
			helper.initCloudSim(NumberOfUser);
			
			// *SECOND STEP:
			// Create one Datacenter entity.
			PowerDatacenter datacenter = helper.createDatacenter("x86", "Linux", "Xen", 10.0, 3.0, 0.05, 0.001, 0.0);
			helper.addFile("TheImitationGame", 3600);
			helper.addFile("shortFile", 10);
			helper.printPersistenStorageDetails(datacenter);
			
			// *THIRD STEP:
			// Create a Broker entity.
			helper.createBroker();
			
			// *FOURTH STEP:
			// Create a virtual machine.
			helper.createVM(50, 1, 512, 1000, 10000, 1, "Xen", new CloudletSchedulerTimeShared(), 1);
			
			// *FIFTH STEP:
			// Create a list of Cloudlets
			
			List<String> requiredFiles = new ArrayList<String>();
			requiredFiles.add("shortFile");
			
			helper.addCloudlet(NumberOfCloudlets, 1, 1, 300, 300, new UtilizationModelFull(), new UtilizationModelFull(),
					new UtilizationModelFull(), new ArrayList<String>(), new ArrayList<File>());
			
			// submit the cloudlet list to the broker.
			helper.getBroker().submitCloudletList(helper.getCloudletList());
			
			// *SIXTH STEP:
			// Define a limit
			CloudSim.terminateSimulation(2000);
			CloudSim.startSimulation();
			
			// Officially stop simulation
			CloudSim.stopSimulation();
			
			// *FINAL STEP: Print results when simulation is over
			helper.printResults();
			//helper.printArrivalRate();
			
			Log.printLine("CloudSimExample1 finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
}