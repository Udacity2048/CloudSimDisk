package org.cloudbus.cloudsim.distributions;

import org.cloudbus.cloudsim.util.WriteToLogFile;

/**
 * @author Baptiste Louis
 * 
 */
public class TesterOfDistribution {
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
