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
package org.cloudbus.cloudsimdisk.distributions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.distributions.ContinuousDistribution;

/**
 * A Basic Number generator. Each sample is read from a Basic Workload file.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyBasicDistr implements ContinuousDistribution {

	/** The path to the Basic Workload file. */
	private final String path;

	/** The request arrival times */
	private List<Double> times;

	/** Reader index */
	private int index;

	/**
	 * Creates a new Basic number generator.
	 * 
	 * @param workloadFileName
	 *            the name of the Basic workload file. It has to be in
	 *            "files/basic" folder.
	 */
	public MyBasicDistr(String workloadFileName) {
		path = "files/" + workloadFileName;
		index = -1;
		times = new ArrayList<Double>();
		init();
	}

	/**
	 * Initializes the Basic generator.
	 */
	public void init() {

		try {

			// initializes a reader
			BufferedReader input = new BufferedReader(new FileReader(path));

			// initializes variables
			String line = "";

			// read line by line
			while ((line = input.readLine()) != null) {

				// add time to the array
				times.add(Double.parseDouble(line));
			}

			// close the reader
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the next sample.
	 * 
	 * @return a time
	 */
	public double sample() {
		index++;
		return (times.get(index));
	}
}
