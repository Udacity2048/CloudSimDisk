package org.cloudbus.cloudsim.distributions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.cloudbus.cloudsim.examples.storage.MyConstants;

/**
 * @author baplou
 * 
 */
public class MyWikiDistr implements ContinuousDistribution {
	
	/** The path to the Wiki distribution file. */
	private final String	path;
	
	/** The request arrival times */
	private double[]		times;
	
	/** Reader index */
	private int				index;
	
	/**
	 * Creates a new wiki distribution.
	 */
	public MyWikiDistr(
			String RequestArrivalDistri) {
		path = "files/" + RequestArrivalDistri;
		index = -1;
		times = new double[MyConstants.CLOUDLET_NUMBER_WIKI];
		init();
	}
	
	/**
	 * Initializes the distribution times.
	 */
	public void init() {
		
		try {
			
			// instantiate a reader
			BufferedReader input = new BufferedReader(
					new FileReader(
							path));
			
			// local variables
			int i = 0;
			String line;
			String[] lineSplited;
			String fullTimeStamp;
			
			// read line by line
			while ((line = input.readLine()) != null) {
				
				// retrieve timeStamp
				lineSplited = line.split("\\s+"); // regular expression quantifiers for whitespace
				fullTimeStamp = lineSplited[1];
				
				// add time to the array
				fullTimeStamp = fullTimeStamp.substring(6); // remove the first 6 numbers of the TimeStamp
				times[i] = Double.parseDouble(fullTimeStamp);
				i++;
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
		return times[index];
	}
}
