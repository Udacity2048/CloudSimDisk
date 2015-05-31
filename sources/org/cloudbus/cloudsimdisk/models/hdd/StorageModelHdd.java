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

package org.cloudbus.cloudsimdisk.models.hdd;

/**
 * The abstract class of all Hard Disk Drive model. Note: new HDD characteristics can be implemented.
 * 
 * @author Baptiste Louis
 */
public abstract class StorageModelHdd {

	// Abstract Method that need to be implemented.

	/**
	 * Get Parameters of a specific StorageModelHdd : 0 the full name, 1 the reference, 2 the capacity, 
	 * 3 the Latency, 4 the avgSeekTime, 5 the maxTransferRate.
	 * 
	 * @param key
	 *            key value of the parameter
	 * @return the requested parameter
	 */
	protected abstract Object getCharacteristic(int key);

	// Non-abstract Method to retrieve a specific parameter.

	/**
	 * Get the Manufacturer name of the hard drive.
	 * 
	 * @return Manufacturer name
	 */
	public String getManufacturerName() {
		return getCharacteristic(0).toString();
	}

	/**
	 * Get the Model Number of the hard drive.
	 * 
	 * @return Model Number
	 */
	public String getModelNumber() {
		return getCharacteristic(1).toString();
	}

	/**
	 * Get the total capacity of the hard drive in MB.
	 * 
	 * @return total capacity in MB
	 */
	public int getCapacity() {
		return (int) getCharacteristic(2);
	}

	/**
	 * Get the Average Rotation Latency of the hard drive in seconds.
	 * 
	 * @return Average Rotation Latency in seconds
	 */
	public double getAvgRotationLatency() {
		return (Double) getCharacteristic(3);
	}

	/**
	 * Get the Average Seek Time of the hard drive in seconds.
	 * 
	 * @return Average Seek Time in seconds
	 */
	public double getAvgSeekTime() {
		return (Double) getCharacteristic(4);
	}

	/**
	 * Get the Maximum Internal Data Transfer Rate of the hard drive in MB/second.
	 * 
	 * @return Maximum Internal Data Transfer Rate in MB/second
	 */
	public double getMaxInternalDataTransferRate() {
		return (Double) getCharacteristic(5);
	}

	/*---------------------------------------------------------------------
	|  SCALABILITY: create new GETTERs to retrieve additional characteristic.
	|
	|  public <TYPE> getNameOfTheParameter() {
	|      return <CAST_OBJECT> getCharacteristic(<KEY_NUMBER>);
	|  }
	|
	 *-------------------------------------------------------------------*/
}
