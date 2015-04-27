/* To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor. */
package org.cloudbus.cloudsimdisk.models.hdd;

/**
 * Storage Model based on HGST Ultrastar C10K900 Review (HUC109090CSS600).
 * 
 * Link: http://www.storagereview.com/hgst_ultrastar_c10k900_review
 * 
 * @author Baptiste Louis
 */
public class StorageModelHddHGSTUltrastarHUC109090CSS600 extends StorageModelHdd {

	/* (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPower#getPowerData(int) */
	@Override
	protected Object getCharacteristic(int key) {
		switch (key) {
			case 0:
				return "HGST Western Digital"; // Manufacturer
			case 1:
				return "HUC109090CSS600"; // Model Number
			case 2:
				return 900000; // capacity (MB)
			case 3:
				return 0.003; // Average Rotation Latency (s)
			case 4:
				return 0.004; // Average Seek Time (s)
			case 5:
				return 198.0; // Maximum Internal Data Transfer Rate (MB/s)
			default:
				return "n/a";

				// SCALABILITY: add new characteristic by adding new CASE.
				//
				// case <KEY_NUMBER>:
				// return <PARAMETER_VALUE>;
				//
		}
	}
}
