/*******************************************************************************
 * Title: CloudSimDisk
 * Description: a module for energy aware storage simulation in CloudSim
 * Author: Baptiste Louis
 * Date: June 2015
 *
 * Address: baptiste_louis@live.fr
 * Source: https://github.com/Udacity2048/CloudSimDisk
 * Website: http://baptistelouis.weebly.com/projects.html
 *
 * Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * Copyright (c) 2015, Luleå University of Technology, Sweden.
 *******************************************************************************/
package org.cloudbus.cloudsimdisk.examples;

import org.cloudbus.cloudsimdisk.models.hdd.*;
import org.cloudbus.cloudsimdisk.power.models.hdd.*;

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
		String nameOfTheSimulation = "simulationWiki1"; // name of the simulation
		String requestArrivalRateType = "wiki"; // type of the workload
		String requestArrivalTimesSource = "wikipedia/wiki.1190153705"; // wikipedia workload with time-Stamps
		int numberOfRequest = MyConstants.CLOUDLET_NUMBER_WIKI; // Number of requests
		String requiredFiles = ""; // No files required
		String dataFiles = "wikipedia/1000.txt"; // File path of the "Hypothetical" Wikipedia dataFiles.
		String startingFilesList = ""; // No files to start
		int numberOfDisk = 1; // 1 HDD
		StorageModelHdd hddModel = new StorageModelHddHGSTUltrastarHUC109090CSS600(); // model of disks in the
																						// persistent storage
		PowerModelHdd hddPowerModel = new PowerModeHddHGSTUltrastarHUC109090CSS600(); // power model of disks

		// Execution
		new MyRunner(nameOfTheSimulation, requestArrivalRateType, numberOfRequest, requestArrivalTimesSource,
				requiredFiles, dataFiles, startingFilesList, numberOfDisk, hddModel, hddPowerModel);
	}
}
