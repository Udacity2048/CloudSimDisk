/* Title: CloudSim EES Extention Description: CloudSim extention for Energy Efficient Storage Licence: GPL -
 * http://www.gnu.org/copyleft/gpl.html Copyright (c) 2015, Luleå University of Techonology */
package org.cloudbus.cloudsim.power.models.harddrives;

/**
 * Power model based on HGST Ultrastar C10K900 Review (HUC109090CSS600).
 * 
 * Info source: http://www.storagereview.com/hgst_ultrastar_c10k900_review
 * 
 * @author Baptiste Louis
 */
public class PowerModeHddHGSTUltrastarHUC109090CSS600 extends PowerModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.harddrives.PowerModelHdd#getPowerData(int) */
	@Override
	protected double getPowerData(int key) {

		switch (key) {
			case 0:
				return 3.0; // Idle mode, in W.
			case 1:
				return 5.8; // Active mode, in W.
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
