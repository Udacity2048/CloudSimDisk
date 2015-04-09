/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsim.storage.models.harddrives;

/**
 * Storage Model based on Seagate Enterprise NAS HDD Review .
 * 
 * Link: http://www.storagereview.com/seagate_enterprise_nas_hdd_review
 * 
 * @author Baptiste Louis
 */
public class StorageModelHddSeagateEnterpriseST6000VN0001 extends StorageModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int) */
	@SuppressWarnings("javadoc")
	@Override
	protected Object getParameter(int key) {
		switch (key) {
			case 0:
				return "Seagate Enterprise"; // Full name
			case 1:
				return "ST6000VN0001"; // Reference
			case 2:
				return 6000000; // capacity (MB)
			case 3:
				return 0.00416; // rotation latency (s)
			case 4:
				return 0.0085; // seekTime (s)
			case 5:
				return 216.0; // maxTransferRate (MB/s)
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
