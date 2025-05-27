package simulation.lib.randVars;

import simulation.lib.rng.RNG;

/**
 * Basis class for random variables (RVs)
 */
public abstract class RandVar {
	/**
	 * Random number generator
	 */
	protected RNG rng;

	/**
	 * Constructor- allows setting a custom random number generator
	 * @param rng random number generator
	 */
	public RandVar(RNG rng) {
		this.rng = rng;
	}
	
	/**
	 * Generate a random value
	 * @return random value
	 */
	public abstract double getRV();

	/**
	 * Generate a long random value
	 * @return long random value
	 */
	public long getLongRV() {
		return (long) Math.ceil(getRV());
	}

	/**
	 * Get mean of the random variable
	 * @return mean 
	 */
	public abstract double getMean();
	
	/**
	 * Get variance of the random variable
	 * @return variance 
	 */
	public abstract double getVariance();
	
	/**
	 * Get standard deviation of the random variable
	 * @return standard deviation
	 */
	public double getStdDeviation() {
		return Math.sqrt(getVariance());
	}
	
	/**
	 * Get covariance of random variable
	 * @return covariance
	 */
	public double getCvar() {
		if(getMean() == 0)
			return getStdDeviation() == 0 ? 0 : Double.POSITIVE_INFINITY;
		else
			return getStdDeviation() / getMean();
	}
	
	/**
	 * Set variance of the random variable
	 * @param v variance
	 */
	public void setVariance(double v) {
		if(v >= 0)
			setStdDeviation(Math.sqrt(v));
		else
			throw new IllegalArgumentException("variance = " + v + " must be positive");
	}
	
	/**
	 * Set covariance of the random variable.
	 * @param c covariance
	 */
	public void setCvar(double c) {
		if(getMean() != 0)
			setStdDeviation(getMean() * c);
		else
			throw new IllegalArgumentException("mean == 0 -> cvar can not be set");
	}
	
	/**
	 * Set mean value of random variable
	 * @param m mean
	 */
	public abstract void setMean(double m);
	
	/**
	 * Set standard deviation of random variable
	 * @param s standard deviation
	 */
	public abstract void setStdDeviation(double s);
	
	/**
	 * Set mean and standard deviation of random variable (if possible).
	 * @param m mean
	 * @param s standard deviation
	 */
	public abstract void setMeanAndStdDeviation(double m, double s);
	
	/**
	 * Set mean and covariance of random variable
	 * @param m mean 
	 * @param c vavariance
	 */
	public void setMeanAndCvar(double m, double c) {
		setMeanAndStdDeviation(m, m * c);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 	"\nanalytical propterties: " + getType() + "\n" +
				"\tmean: " + getMean() + "\n" +
				"\tcvar: " + getCvar() + "\n" +	
				"\tstdandard deviation: " + getStdDeviation() + "\n" +
				"\tvariance: " + getVariance() +"\n";
	}

	public abstract String getType();
}
