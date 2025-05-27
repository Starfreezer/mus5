package simulation.lib.counter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This class implements a discrete time autocorrelation counter
 */
public class DiscreteAutocorrelationCounter extends DiscreteCounter {
	private int maxLag;
	private int cycleLength;
	private double[] firstSamples;
	private double[] squaredSums;
	private LinkedList<Double> lastSamples;

	/**
	 * Basic constructor
	 * @param variable the variable to observe
	 * @param maxLag window size of considered samples
	 */
	public DiscreteAutocorrelationCounter(String variable, int maxLag) {
		super(variable, "counter type: discrete-time autocorrelation counter");
		this.maxLag = maxLag;
		reset();
	}
	
	/**
	 * Constructor
	 * @param variable the variable to observe
	 * @param type type of this counter
	 * @param maxLag window size of considered samples
	 */
	public DiscreteAutocorrelationCounter(String variable, String type, int maxLag) {
		super(variable, type);
		this.maxLag = maxLag;
		reset();
	}

	/**
	 * Gets the maximum lag of the counter.
	 * @return maxLag
	 */
	public int getMaxLag() {
		return maxLag;
	}

	/**
	 * Sets the maximum lag and then resets the counter.
	 * @param maxLag
	 */
	public void setMaxLag(int maxLag){
		this.maxLag = maxLag;
		reset();
	}
	
	/**
	 * @see Counter#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		cycleLength = maxLag + 1;
		firstSamples = new double[cycleLength];
		lastSamples = new LinkedList<Double>();
		squaredSums = new double[cycleLength];
	}
	
	/**
	 * @see Counter#count(double x)
	 */
	@Override
	public void count(double x) {
		super.count(x);
		int n = (int) getNumSamples() - 1;
		if(n < cycleLength)
			firstSamples[n] = x;
		lastSamples.push(x);
		if(lastSamples.size() > maxLag + 1)
			lastSamples.pollLast();
		for(int i = 0; i <= (maxLag < n ? maxLag : n); i++) {
			squaredSums[i] = squaredSums[i] + x * lastSamples.get(i);
		}
	}

	/**
	 * Get auto covariance of given lag.
	 * @param lag 
	 * @return auto covariance
	 */
	public double getAutoCovariance(int lag) {
		if (lag <= maxLag && lag <= getNumSamples()){
			double sumOfFirsts = 0;
			double sumOfLasts = 0;
			for(int i = 0; i < lag; i++){
				sumOfFirsts += firstSamples[i];
				sumOfLasts += lastSamples.get(i);
			}
			return (squaredSums[lag] - getMean() * (2 * getSumPowerOne() - sumOfFirsts - sumOfLasts)) / (double)(getNumSamples() - lag) + getMean() * getMean();
		}else{  
			throw new IllegalArgumentException("lag <= " + maxLag + " required");
		}	
	}
	
	/**
	 * Get auto correlation of given lag.
	 * @param lag
	 * @return auto correlation
	 */
	public double getAutoCorrelation(int lag) {
		if (lag <= maxLag){
			if (getVariance() != 0){
				return getAutoCovariance(lag) / getVariance();
			}else{
				return 1;
			}
		}else{
			throw new IllegalArgumentException("lag <= " + maxLag + " required");
		}
	}
	
	/**
	 * @see Counter#report()
	 */
	@Override
	public String report() {
		String out  = super.report();
		out += ("\n\tCorrelation/Covariance:\n");
		for(int i = 0; i <= (getNumSamples() < maxLag ? getNumSamples() : maxLag); i++){
			out += ("\t\tlag = " + i + "   " +
					"covariance = " + getAutoCovariance(i) + "   " +
					"correlation = " + getAutoCorrelation(i)+"\n");
		}
		return out;
	}
	/**
	 * @see Counter#csvReport(String)
	 */
	@Override
	public void csvReport(String outputdir){
	    String content = "";
        for(int i = 0; i <= (getNumSamples() < maxLag ? getNumSamples() : maxLag); i++) {
            content += observedVariable + " (lag=" + i + ")" + ";" + getNumSamples() + ";" + getMean() + ";" +
                    getVariance() + ";" + getStdDeviation() + ";" + getCvar() + ";" + getMin() + ";" + getMax() + ";" +
                    getAutoCovariance(i) + ";" + getAutoCorrelation(i) + "\n";
        }
        String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX; COV; CORR\n";
        writeCsv(outputdir, content, labels);
	}
}
