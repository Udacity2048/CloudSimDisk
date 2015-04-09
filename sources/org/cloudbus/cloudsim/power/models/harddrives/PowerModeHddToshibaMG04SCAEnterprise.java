/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * The power model of a Toshiba MG04SCA Enterprise.
 * 
 * Info source: http://www.storagereview.com/toshiba_mg04sca_enterprise_hdd_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddToshibaMG04SCAEnterprise extends PowerModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd#getPowerData(int) */
	@Override
	protected double getPowerData(int key) {

		switch (key) {
			case 0:
				return 6.2; // Idle mode, in W.
			case 1:
				return 11.3; // Active mode, in W.
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
