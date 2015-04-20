package org.cloudbus.cloudsim.distributions;

import java.util.ArrayList;
import java.util.List;

/**
 * A seekTime generator with min, max and average parameters.
 * 
 * @author Baptiste Louis
 * 
 */
public class MySeekTimeDistr implements ContinuousDistribution {

	/** The num gen1. */
	private final ContinuousDistribution	numGen1;

	/** The num gen2. */
	private final ContinuousDistribution	numGen2;

	/** The mean. */
	private final double					mean;

	/** The max */
	private final double					max;

	/** The min */
	private final double					min;

	/** The epsilon */
	private final double					eps;

	/** The history */
	private final List<Double>				x;

	/** The history */
	private double							temp;

	/**
	 * Creates a new exponential number generator.
	 * 
	 * @param min
	 *            the min for the distribution.
	 * @param max
	 *            the max for the distribution.
	 * @param mean
	 *            the mean for the distribution.
	 */
	public MySeekTimeDistr(double min, double max, double mean) {
		if (mean <= min || mean >= max || min >= max) {
			throw new IllegalArgumentException("Incorect parameters");
		}
		this.numGen1 = new UniformDistr(min, mean);
		this.numGen2 = new UniformDistr(mean, max);
		this.min = min;
		this.max = max;
		this.mean = mean;
		this.eps = 0.001;
		this.x = new ArrayList<Double>();
	}

	/**
	 * Generate a new random number.
	 * 
	 * @return the next random number in the sequence
	 */
	public double sample() {
		if (calculateAverage(x) > mean) {
			temp = numGen1.sample();
			x.add(temp);
			return temp;
		} else {
			temp = numGen2.sample();
			x.add(temp);
			return temp;
		}
	}

	/**
	 * Get average.
	 * 
	 * @param marks
	 * @return the average
	 */
	private double calculateAverage(List<Double> list) {
		Double sum = 0.0;
		if (!list.isEmpty()) {
			for (Double mark : list) {
				sum += mark;
			}
			return sum.doubleValue() / list.size();
		}
		return sum;
	}
}
