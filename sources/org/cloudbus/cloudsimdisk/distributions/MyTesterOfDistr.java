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
package org.cloudbus.cloudsimdisk.distributions;

import org.cloudbus.cloudsimdisk.util.WriteToLogFile;

/**
 * @author Baptiste Louis
 * 
 */
public class MyTesterOfDistr {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MySeekTimeDistr testStat = new MySeekTimeDistr(0.0002, 3 * 0.0085, 0.0085);
		String msg = "";
		for (int i = 0; i < 1000; i++) {
			msg += testStat.sample() + "\n";

		}
		WriteToLogFile.AddtoFile(msg);
	}
}
