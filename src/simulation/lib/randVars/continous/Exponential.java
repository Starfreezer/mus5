/**
 * 
 */
package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

/**
 * Expnential distributed random variable.
 */
public class Exponential extends RandVar {

	private double lambda;

	/**
	 * Constructor
	 * @param rng random number generator
	 * @param mean mean
	 */
	public Exponential(RNG rng, double mean) {
		super(rng);
		setMean(mean);
	}

	/**
	 * Constructor
	 * @param rng random number generator
	 * @param mean mean
	 * @param sdev standard deviation
	 */
	public Exponential(RNG rng, double mean, double sdev) {
		super(rng);
		setMeanAndStdDeviation(mean, sdev);
	}

	/**
	 * @see RandVar#getRV()
	 */
	@Override
	public double getRV() {
		return -Math.log(rng.rnd()) / lambda;
	}

	/**
	 * @see RandVar#getMean()
	 */
	@Override
	public double getMean() {
		return 1 / lambda;
	}

	/**
	 * @see RandVar#getVariance()
	 */
	@Override
	public double getVariance() {
		return 1 / (lambda * lambda);
	}

	/**
	 * @see RandVar#setMean(double)
	 */
	@Override
	public void setMean(double mean) {
		if(mean <= 0)
			throw new IllegalArgumentException("mean must not be lower equals 0");
        lambda = (1 / mean);
	}

	/**
	 * @see RandVar#setMeanAndStdDeviation(double, double)
	 */
	@Override
	public void setMeanAndStdDeviation(double mean, double sdev) {
        if (mean != sdev)
            throw new UnsupportedOperationException("Mean must be equal standard deviation!");
        lambda = 1 / mean;
	}

	/**
	 * @see RandVar#setStdDeviation(double)
	 */
	@Override
	public void setStdDeviation(double sdev) {
		lambda = 1 / sdev;
	}

	/**
	 * @see RandVar#getType()
	 */
	@Override
	public String getType() {
		return "Exponential";
	}

	@Override
	public String toString() {
		return super.toString() + 
			"\tparameters:\n" +
			"\t\tlambda: " + lambda + "\n";
	}	
	
}
