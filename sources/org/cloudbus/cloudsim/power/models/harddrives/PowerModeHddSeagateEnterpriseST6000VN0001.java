/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * The power model of a Seagate Enterprise ST6000VN0001.
 * 
 * Info source: http://www.storagereview.com/seagate_enterprise_nas_hdd_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddSeagateEnterpriseST6000VN0001 extends PowerModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd#getPowerData(int) */
	@Override
	protected double getPowerData(int key) {

		switch (key) {
			case 0:
				return 6.9; // Idle mode, in W.
			case 1:
				return 11.27; // Active mode, in W.
			default:
				return 0.0;

				// SCALABILITY: add new mode by adding new CASE.
				//
				// case <KEY_NUMBER>:
				// return <POWER_VALUE>;
				//
		}

	}
}
