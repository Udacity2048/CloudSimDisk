/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsimdisk.models.hdd;

/**
 * Storage Model based on Toshiba MG04SCA Enterprise HDD Review (MG04SCA500E).
 * 
 * Link: http://www.storagereview.com/toshiba_mg04sca_enterprise_hdd_review
 * 
 * @author Baptiste Louis
 */
public class StorageModelHddToshibaEnterpriseMG04SCA500E extends StorageModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int) */
	@Override
	protected Object getParameter(int key) {
		switch (key) {
			case 0:
				return "Toshiba"; // Manufacturer 
			case 1:
				return "MG04SCA500E"; // Model Number
			case 2:
				return 5000000; // capacity (MB)
			case 3:
				return 0.00417; // Average Rotation Latency (s)
			case 4:
				return 0.009; // Average Seek Time (s)
			case 5:
				return 215.0; // Maximum Internal Data Transfer Rate (MB/s)
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
