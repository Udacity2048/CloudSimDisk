package org.cloudbus.cloudsim.distributions;

import org.cloudbus.cloudsim.Log;

/**
 * @author Baptiste Louis
 *
 */
public class TesterOfDistribution {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyPoissonDistr testStat = new MyPoissonDistr(10);
		// System.out.println(\\\"Testing stat: \\\" + testStat+ \\\" with lambda: \\\" + lambda);
		for (int i = 0; i < 1000; i++) {
			Log.formatLine("%.2f", testStat.sample());
		}
	}
}
