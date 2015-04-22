package org.cloudbus.cloudsimdisk.distributions;

import org.cloudbus.cloudsimdisk.util.WriteToLogFile;

/**
 * @author Baptiste Louis
 * 
 */
public class MyTesterOfDistribution {
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
