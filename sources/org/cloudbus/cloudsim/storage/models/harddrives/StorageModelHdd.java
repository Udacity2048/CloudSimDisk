/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * The abstract class of a specific hard drive model. Note: new HDD characteristics can be implemented.
 * 
 * @author Baptiste Louis
 */
public abstract class StorageModelHdd {

	// Abstract Method that need to be implemented.

	/**
	 * Get Parameters of a specific StorageModelHdd : 0 the full name, 1 the reference, 2 the capacity, 3 the Latency, 4
	 * the avgSeekTime, 5 the maxTransferRate.
	 * 
	 * @param key
	 *            key value of the parameter
	 * @return the requested parameter
	 */
	protected abstract Object getParameter(int key);

	// Non-abstract Method to retrieve a specific parameter.

	/**
	 * Get the Manufacturer name of the hard drive.
	 * 
	 * @return Manufacturer name
	 */
	public String getManufacturerName() {
		return getParameter(0).toString();
	}

	/**
	 * Get the Model Number of the hard drive.
	 * 
	 * @return Model Number
	 */
	public String getModelNumber() {
		return getParameter(1).toString();
	}

	/**
	 * Get the total capacity of the hard drive in MB.
	 * 
	 * @return total capacity in MB
	 */
	public double getCapacity() {
		return (int) getParameter(2);
	}

	/**
	 * Get the Average Rotation Latency of the hard drive in seconds.
	 * 
	 * @return Average Rotation Latency in seconds
	 */
	public double getAvgRotationLatency() {
		return (Double) getParameter(3);
	}

	/**
	 * Get the Average Seek Time of the hard drive in seconds.
	 * 
	 * @return Average Seek Time in seconds
	 */
	public double getAvgSeekTime() {
		return (Double) getParameter(4);
	}

	/**
	 * Get the Maximum Internal Data Transfer Rate of the hard drive in MB/second.
	 * 
	 * @return Maximum Internal Data Transfer Rate in MB/second
	 */
	public double getMaxInternalDataTransferRate() {
		return (Double) getParameter(5);
	}

	/*---------------------------------------------------------------------
	|  SCALABILITY: create new GETTERs to retrieve additional parameters.
	|
	|  public <TYPE> getNameOfTheParameter() {
	|      return <CAST_OBJECT> getParameter(<KEY_NUMBER>);
	|  }
	|
	 *-------------------------------------------------------------------*/
}
