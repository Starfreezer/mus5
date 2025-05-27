package simulation.lib.histogram;

import simulation.lib.Simulator;

/**
 * This class implements a continuous time histogram
 */
public class ContinuousHistogram extends Histogram {
	private double lastSampleSize;
	private long firstSampleTime;
	private long lastSampleTime;
	private Simulator sim;

	/**
	 * Constructor
	 * @param variable the observed variable
	 * @param numIntervals number of intervals
	 * @param lowerBound the lower bound of the histogram
	 * @param upperBound the upper bound of the histogram
	 * @param sim the considered simulator
	 */	
	public ContinuousHistogram(String variable, int numIntervals, int lowerBound, int upperBound, Simulator sim) {
		super(variable, numIntervals, lowerBound, upperBound, "histogram type: continuous-time histogram");
		this.firstSampleTime = 0;
		this.lastSampleTime = 0;
		this.lastSampleSize = 0;
		this.sim = sim;
	}

	/**
	 * @see Histogram#count(double)
	 */
	@Override
	public void count(double x) {
		if(getNumIntervals() > 0) {
			this.incrementBin(this.getBinNumber(this.lastSampleSize), sim.getSimTime() - this.lastSampleTime);
			this.lastSampleTime = sim.getSimTime();
		}
		this.lastSampleSize = x;
	}
	
	/**
	 * @see Histogram#getNormalizingFactor()
	 */
	@Override
	protected double getNormalizingFactor() {
		return this.lastSampleTime - this.firstSampleTime;
	}
	
	/**
	 * @see Histogram#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.firstSampleTime = sim.getSimTime();
		this.lastSampleTime = sim.getSimTime();
		this.lastSampleSize = 0;
	}
}
