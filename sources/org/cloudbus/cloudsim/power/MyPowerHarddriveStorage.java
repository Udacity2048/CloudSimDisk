/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsim.power;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.MyHarddriveStorage;
import org.cloudbus.cloudsim.ParameterException;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd;
import org.cloudbus.cloudsim.storage.models.harddrives.StorageModelHdd;

/**
 * MyPowerHarddriveStorage enables simulation of power-aware Hard drives.
 * 
 * @author Baptiste Louis
 */
public class MyPowerHarddriveStorage extends MyHarddriveStorage {

	/**
	 * The power model.
	 */
	private PowerModelHdd	powerModelHdd;

	/**
	 * Duration in Active mode.
	 */
	public double			inActiveDuration;

	/**
	 * Idle Duration Intervals history (when the disk is in idle mode).
	 */
	public List<Double>		IdleIntervalsHistory	= new ArrayList<Double>();

	/**
	 * Last starting Idle time
	 */
	public double			LastIdleStartTime;

	/**
	 * Creates a new Hard Drive storage base on a specific HDD Model.
	 * 
	 * @param id
	 * 
	 * @param name
	 *            the name
	 * @param storageModelHdd
	 *            the specific model
	 * @param powerModel
	 *            the power model
	 * @throws ParameterException
	 *             when the name and the capacity are not valid
	 */
	public MyPowerHarddriveStorage(int id, String name, StorageModelHdd storageModelHdd, PowerModelHdd powerModel)
			throws ParameterException {
		super(id, name, storageModelHdd);

		// set HDD characteristics
		setPowerModelHdd(powerModel);

		// set initial parameters
		setInActiveDuration(0.0);
		setLastIdleStartTime(0.0);
	}

	// GETTERs and SETTERs

	/**
	 * Sets the power model.
	 * 
	 * @param powerModelHdd
	 *            the new power model
	 */
	protected void setPowerModelHdd(PowerModelHdd powerModelHdd) {
		this.powerModelHdd = powerModelHdd;
	}

	/**
	 * Gets the power model.
	 * 
	 * @return the power model
	 */
	public PowerModelHdd getPowerModelHdd() {
		return powerModelHdd;
	}

	/**
	 * Gets the power.
	 * 
	 * @param key
	 *            0 for idle, 1 for Active
	 * @return the power
	 */
	public double getPower(int key) {

		// instantiates local variable
		double power = 0;

		// check parameter validity
		if (key != 0 && key != 1) {
			Log.printLine(this.getName() + ".getPower(): Warning - 0 for Idle mode, 1 for Active mode.");
			return power;
		}

		// retrieve power
		switch (key) {
			case 0:
				return getPowerModelHdd().getPowerIdle();
			case 1:
				return getPowerModelHdd().getPowerActive();
			default:
				return power;
		}

	}

	/**
	 * Gets the Active duration.
	 * 
	 * @return the inActiveDuration
	 */
	public double getInActiveDuration() {
		return inActiveDuration;
	}

	/**
	 * Sets the Active duration.
	 * 
	 * @param inActiveDuration
	 *            the inActiveDuration to set
	 */
	public void setInActiveDuration(double inActiveDuration) {
		this.inActiveDuration = inActiveDuration;
	}

	/**
	 * Gets the idle intervals history.
	 * 
	 * @return the idle Intervals history
	 */
	public List<Double> getIdleIntervalsHistory() {
		return IdleIntervalsHistory;
	}

	/**
	 * Gets the last Idle start time.
	 * 
	 * @return the lastIdleStartTime
	 */
	public double getLastIdleStartTime() {
		return LastIdleStartTime;
	}

	/**
	 * Sets the last Idle start time.
	 * 
	 * @param lastIdleStartTime
	 *            the lastIdleModeStartTime to set
	 */
	public void setLastIdleStartTime(double lastIdleStartTime) {
		LastIdleStartTime = lastIdleStartTime;
	}
}
