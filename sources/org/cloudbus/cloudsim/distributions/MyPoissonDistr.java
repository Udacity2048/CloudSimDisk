package org.cloudbus.cloudsim.distributions;

import java.util.Random;

/**
 * An poison number generator.
 * 
 * @author Baptiste Louis
 * 
 */
public class MyPoissonDistr implements ContinuousDistribution {

	/** The num gen. */
	private final Random	numGen;

	/** The mean. */
	private final double	mean;

	/**
	 * Creates a new exponential number generator.
	 * 
	 * @param seed
	 *            the seed to be used.
	 * @param mean
	 *            the mean for the distribution.
	 */
	public MyPoissonDistr(long seed, double mean) {
		if (mean <= 0.0) {
			throw new IllegalArgumentException("Mean must be greater than 0.0");
		}
		numGen = new Random(seed);
		this.mean = mean;
	}

	/**
	 * Creates a new exponential number generator.
	 * 
	 * @param mean
	 *            the mean for the distribution.
	 */
	public MyPoissonDistr(double mean) {
		if (mean <= 0.0) {
			throw new IllegalArgumentException("Mean must be greated than 0.0");
		}
		numGen = new Random(System.currentTimeMillis());
		this.mean = mean;
	}

	/**
	 * Generate a new random number.
	 * 
	 * @return the next random number in the sequence
	 */
	public double sample() {
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * numGen.nextDouble();
			k++;
		} while (p > L);
		return k - 1;
	}

}
