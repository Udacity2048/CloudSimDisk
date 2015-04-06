/*
 * Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology
 */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * The power model of a Seagate Enterprise ST6000VN0001.
 * 
 * Info source: http://www.storagereview.com/seagate_enterprise_nas_hdd_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddSeagateEnterpriseST6000VN0001 extends PowerModelHdd {
	
	/**
	 * The power consumption of the HDD in idle mode in W.
	 */
	private final double	powerIdle	= 6.9;
	
	/**
	 * The power consumption of the HDD in active mode in W.
	 */
	private final double	powerOpe	= 11.27;
	
	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd#getPowerData(int)
	 */
	@SuppressWarnings("javadoc")
	@Override
	protected double getPowerData(
			int key) {
		if (key == 0) {
			return powerIdle;
		} else {
			return powerOpe;
		}
	}
	
}
