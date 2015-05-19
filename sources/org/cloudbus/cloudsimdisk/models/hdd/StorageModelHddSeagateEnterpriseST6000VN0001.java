/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsimdisk.models.hdd;

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
	@Override
	protected Object getCharacteristic(int key) {
		switch (key) {
			case 0:
				return "Seagate Technology"; // Manufacturer 
			case 1:
				return "ST6000VN0001"; // Model Number
			case 2:
				return 6000000; // capacity (MB)
			case 3:
				return 0.00416; // Average Rotation Latency (s)
			case 4:
				return 0.0085; // Average Seek Time (s)
			case 5:
				return 216.0; // Maximum Internal Data Transfer Rate (MB/s)
			default:
				return "n/a";

				// SCALABILITY: add new characteristics by adding new CASEs.
				//
				// case <KEY_NUMBER>:
				// return <PARAMETER_VALUE>;
				//
		}
	}
}
