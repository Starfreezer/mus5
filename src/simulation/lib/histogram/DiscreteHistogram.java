package simulation.lib.histogram;

/**
 * This class implements a discrete time histogram
 */
public class DiscreteHistogram extends Histogram {
	private long numSamples;
	
	/**
	 * Constructor
	 * @param variable the observed variable
	 * @param numIntervals number of intervals
	 * @param lowerBound the lower bound of the histogram
	 * @param upperBound the upper bound of the histogram
	 */	
	public DiscreteHistogram(String variable, int numIntervals, double lowerBound, double upperBound) {
		super(variable, numIntervals, lowerBound, upperBound, "histogram type: discrete-time histogram");
	}

	/**
	 * @see Histogram#count(double)
	 */
	@Override
	public void count(double x) {
		if(getNumIntervals() > 0) {
			this.incrementBin(this.getBinNumber(x), 1);
			this.numSamples++;
		}
	}
	
	/**
	 * @see Histogram#getNormalizingFactor()
	 */
	@Override
	protected double getNormalizingFactor() {
		return numSamples;
	}
	
	/**
	 * @see Histogram#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		numSamples = 0;
	}
}
