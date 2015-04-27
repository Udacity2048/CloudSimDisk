/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsimdisk.power.models.hdd;

/**
 * The power model of a Toshiba MG04SCA Enterprise 5TB.
 * 
 * Info source: http://www.storagereview.com/toshiba_mg04sca_enterprise_hdd_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddToshibaEnterpriseMG04SCA500E extends PowerModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsimdisk.power.models.hdd.PowerModelHdd#getPowerData(int) */
	@Override
	protected Object getPowerData(int key) {

		switch (key) {
			case 0:
				return 6.2; // Idle mode, in W.
			case 1:
				return 11.3; // Active mode, in W.
			default:
				return "n/a";
				
				// SCALABILITY: add new mode by adding new CASE.
				//
				// case <KEY_NUMBER>:
				// return <POWER_VALUE>;
		}
	}
}
