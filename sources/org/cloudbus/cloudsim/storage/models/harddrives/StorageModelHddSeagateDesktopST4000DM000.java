/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * Storage Model based on Seagate Desktop HDD.15 Review (ST4000DM000).
 * 
 * Link: http://www.storagereview.com/seagate_desktop_hdd15_review_st4000dm000
 * 
 * @author Baptiste Louis
 */
public class StorageModelHddSeagateDesktopST4000DM000 extends StorageModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int) */
	@Override
	protected Object getParameter(int key) {
		switch (key) {
			case 0:
				return "Seagate Desktop"; // Full name
			case 1:
				return "ST4000DM000"; // Reference
			case 2:
				return 4000000; // capacity (MB)
			case 3:
				return 0.00516; // rotation latency (s)
			case 4:
				return 0.0085; // seekTime (s)
			case 5:
				return 146.0; // maxTransferRate (MB/s)
			default:
				return "No info";

				// SCALABILITY: add new parameters by adding new CASE.
				//
				// case <KEY_NUMBER>:
				// return <PARAMETER_VALUE>;
				//
		}
	}
}
