/*******************************************************************************
 * Title: CloudSimDisk
 * Description: a module for energy aware storage simulation in CloudSim
 * Author: Baptiste Louis
 * Date: June 2015
 *
 * Address: baptiste_louis@live.fr
 * Source: https://github.com/Udacity2048/CloudSimDisk
 * Website: http://baptistelouis.weebly.com/projects.html
 *
 * Licence: GPL - http://www.gnu.org/copyleft/gpl.html
 * Copyright (c) 2015, Luleå University of Technology, Sweden.
 *******************************************************************************/

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
