/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * The abstract class of a specific hard drive model. Note: new HDD characteristics can be implemented.
 * 
 * @author Baptiste Louis
 */
public abstract class StorageModelHdd {
	
	/**
	 * Get the name of the hard drive.
	 * 
	 * @return name of the hard drive
	 */
	public String getFullName() {
		return getnominative(0);
	}
	
	/**
	 * Get the reference of the hard drive.
	 * 
	 * @return reference of the hard drive
	 */
	public String getReference() {
		return getnominative(1);
	}
	
	/**
	 * Get the total capacity of the hard drive in MB.
	 * 
	 * @return total capacity of the hard drive in MB
	 */
	public double getCapacity() {
		return getParameter(0);
	}
	
	/**
	 * Get the latency of the hard drive in seconds.
	 * 
	 * @return latency of the hard drive in seconds
	 */
	public double getLatency() {
		return getParameter(1);
	}
	
	/**
	 * Get the average seek time in seconds.
	 * 
	 * @return average seek time in seconds
	 */
	public double getAvgSeekTime() {
		return getParameter(2);
	}
	
	/**
	 * Get the maximum transfer rate in MB/second.
	 * 
	 * @return maximum transfer rate in MB/second
	 */
	public double getMaxTransferRate() {
		return getParameter(3);
	}
	
	/**
	 * Get Parameters of a specific StorageModelHdd : 0 the capacity, 1 the Latency, 2 the avgSeekTime, 3 the
	 * maxTransferRate.
	 * 
	 * @param key
	 *            key value of the parameter
	 * @return the requested parameter
	 */
	protected abstract double getParameter(
			int key);
	
	/**
	 * Get the nominative of the hard drive: 0 the name, 1 the reference.
	 * 
	 * @param key
	 *            key value of the nominative
	 * @return reference of the hard drive
	 */
	protected abstract String getnominative(
			int key);
}
