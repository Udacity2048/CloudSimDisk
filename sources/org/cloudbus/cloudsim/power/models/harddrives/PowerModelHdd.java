/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html
 * 
 * Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * The abstract class of power models for hard disk drives.
 * 
 * @author Baptiste Louis
 */
public abstract class PowerModelHdd {

	// Abstract Method that need to be implemented.

	/**
	 * Gets the power data for a specific mode.
	 * 
	 * @param key
	 *            0 for Idle mode, 1 for Active mode.
	 * @return the power data
	 */
	protected abstract double getPowerData(int key);

	// Non-abstract Method to retrieve a specific parameter.

	/**
	 * Gets the Power of a specified mode.
	 * 
	 * @param mode
	 *            the mode
	 * 
	 * @return the power
	 */
	public double getPower(int mode) {
		return getPowerData(mode);
	}

	/**
	 * Gets the Power in Idle mode.
	 * 
	 * @return the power
	 */
	public double getPowerIdle() {
		return getPowerData(0);
	}

	/**
	 * Gets the Power in Active mode.
	 * 
	 * @return the power
	 */
	public double getPowerActive() {
		return getPowerData(1);
	}

	// SCALABILITY: create new GETTERs to retrieve Power for additional mode.
	//
	// public <TYPE> getPowerOfYourMode() {
	// return getPowerData(<KEY_NUMBER>);
	// }
	//
}
