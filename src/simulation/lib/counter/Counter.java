package simulation.lib.counter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import simulation.lib.statistic.IStatisticObject;

/**
 * Basic counter that counts:
 * * sum power two
 * * sum power one
 * * minimum
 * * maximum
 */
public abstract class Counter implements IStatisticObject{
	/**
	 * Sum of values counted by this counter
	 */
	protected double sumPowerOne;
	/**
	 * Sum of square values counted by this counter
	 */
	protected double sumPowerTwo;
	private double min;
	private double max;
	protected String observedVariable;
	private String counterType;
	private long numSamples;

	/**
	 * Basic constructor
	 * @param variable the variable to observe
	 */
	public Counter(String variable) {
		this(variable, "counter type: base counter");
	}
	
	/**
	 * Internal constructor
	 * @param variable the observed variable
	 * @param type the type of counter
	 */
	public Counter(String variable, String type) {
		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		counterType = type;
		observedVariable = variable;	
	}
	
	/**
	 * Returns the mean of the observed variable
	 * @return the mean
	 */
	public abstract double getMean();
	
	/**
	 * Returns the variance of the observed variable
	 * @return the variance
	 */
	public abstract double getVariance();
	
	/**
	 * Returns the standard deviation of the observed variable
	 * @return the standard deviation
	 */
	public double getStdDeviation() {
		return Math.sqrt(getVariance());
	}
	
	/**
	 * Returns the co-variance of the observed variable 
	 * @return the co-variance
	 */
	public double getCvar() {
		if(getMean() == 0)
			return getStdDeviation()== 0 ? 0 : Double.MAX_VALUE;
		else
			return getStdDeviation() / getMean();
	}
	
	/**
	 * Returns the minimum of the observed variable
	 * @return the minimum
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * Returns the maximum of the observed variable
	 * @return the maximum
	 */
	public double getMax() {
		return max;
	}
	
	/**
	 * Returns the number of counted samples
	 * @return the number of samples
	 */
	public long getNumSamples() {
		return numSamples;
	}
	
	/**
	 * Returns the sum of all counted samples
	 * @return the sum of all samples
	 */
	public double getSumPowerOne() {
		return sumPowerOne;
	}
	
	/**
	 * Adds the given value to the sum of counted samples
	 * @param value the value to add
	 */
	public void increaseSumPowerOne(double value) {
		sumPowerOne += value;
	}
	
	/**
	 * Returns the sum of all counted samples power two
	 * @return the sum of all samples power two
	 */
	public double getSumPowerTwo() {
		return sumPowerTwo;
	}
	
	/**
	 * Adds the given value to the sum of counted samples power two
	 * @param value the value to add
	 */
	public void increaseSumPowerTwo(double value) {
		sumPowerTwo += value;
	}
	
	/**
	 * Counts a new sample (set min/max and increment sample counter)
	 * @param x the value to count
	 */
	public void count(double x) {
		min = (x < min ? x : min);
		max = (x > max ? x : max);
		numSamples++;
	}
	
	/**
	 * Outputs the report of this counter to the command line
	 */
	public String report() {
		String out = "";
		if(observedVariable!= null) out += "observed random variable: " + observedVariable + "\n"; 
		out +=	"\t" + counterType + "\n" +
				"\tnumber of samples: " + numSamples + "\n" +
				"\tmean: " + getMean() + "\n" +
				"\tvariance: " + getVariance() + "\n" +
				"\tstandard deviation: " + getStdDeviation() + "\n" +
				"\tcoefficient of variation: " + getCvar() + "\n" +
				"\tminimum: " + min + "\n" +
				"\tmaximum: " + max;
		return out;
	}
	
	/**
	 * Write Counter details to csv-file.
	 */
	public void csvReport(String outputdir){
		String content = observedVariable + ";" + numSamples + ";" + getMean() + ";" + getVariance()  + ";" + getStdDeviation() + ";" + getCvar() + ";" + getMin() + ";" + getMax() + "\n";
		String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX\n";
		writeCsv(outputdir, content, labels);
	}

	protected void writeCsv(String outputdir, String content, String labels) {
		try {
			String dest= outputdir + "/counters";
			File file = new File(dest);
			file.mkdir();

			boolean fexist = new File(dest + "/" + this.getClass().getSimpleName() + ".csv").exists();
			FileWriter csvwriter = new FileWriter(dest + "/" + this.getClass().getSimpleName()  + ".csv",true);

			if(!fexist){
				content = labels + content;
			}

			csvwriter.append(content);
			csvwriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Resets the counter.
	 */
	public void reset() {
		sumPowerOne = 0;
		sumPowerTwo = 0;
		min = Double.POSITIVE_INFINITY;
		max = Double.NEGATIVE_INFINITY;
		numSamples = 0;
	}
}
