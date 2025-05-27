package simulation.lib.histogram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import simulation.lib.statistic.IStatisticObject;

/**
 * Basic class implemented by all types of histograms
 */
public abstract class Histogram implements IStatisticObject{
	private double lowerBound;
	private double upperBound;
	private double delta;
	private int numIntervals;
	private double[] bins;
	private String observedVariable;
	private String histogramType;
	
	/**
	 * Basic constructor
	 * @param variable the observed variable
	 * @param numIntervals number of intervals
	 * @param lowerBound the lower bound of the histogram
	 * @param upperBound the upper bound of the histogram
	 */
	public Histogram(String variable, int numIntervals, double lowerBound, double upperBound) {
		this(variable, numIntervals, lowerBound, upperBound, "histogram type: base histogram");
	}
	
	/**
	 * Internal constructor
	 * @param variable the observed variable
	 * @param numIntervals number of intervals
	 * @param lowerBound the lower bound of the histogram
	 * @param upperBound the upper bound of the histogram
	 * @param histogramType the type of histogram
	 */
	public Histogram(String variable, int numIntervals, double lowerBound, double upperBound, String histogramType) {
		this.observedVariable = variable;
		this.histogramType = histogramType;
		this.numIntervals = numIntervals;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.bins = new double[numIntervals];
		this.delta = (double) (upperBound - lowerBound) / numIntervals;
	}
	
	/**
	 * Counts a new sample
	 * @param x the value to count
	 */
	public abstract void count(double x);

    /**
     * Returns the factor needed to normalize the values
     * @return the normalizing factor
     */
    protected abstract double getNormalizingFactor();

    /**
	 * Returns the number of intervals
	 * @return the number ofer intervals
	 */
	protected long getNumIntervals() {
		return this.numIntervals;
	}
	
	/**
	 * Returns the bin number the value is belonging to
	 * @param x the value
	 * @return the number of the bin
	 */
	protected int getBinNumber(double x) {
        if(x >= this.upperBound)
            return this.numIntervals - 1;
        if(x < this.lowerBound)
            return 0;
        return (int) Math.floor((x - this.lowerBound) / this.delta);
	}
	
	/**
	 * Adds the given value to the selected bin
	 * @param binNumber the bin number
	 * @param x the value to add
	 */
	protected void incrementBin(int binNumber, double x) {
        this.bins[binNumber] = this.bins[binNumber] + x;
	}
	
	/**
	 * Returns the current value of the selected bin
	 * @param binNumber the bin number
	 * @return the current value
	 */
	protected double getBinValue(int binNumber) {
        return this.bins[binNumber];
	}

	/**
	 * Returns the lower bound
	 * @return the lowerBound
	 */
	protected double getLowerBound() {
        return this.lowerBound;
	}

	/**
	 * Returns the upper bound
	 * @return the upperBound
	 */
	protected double getUpperBound() {
		return this.upperBound;
	}

	/**
	 * Returns the delta
	 * @return the delta
	 */
	protected double getDelta() {
        return this.delta;
	}	

    /**
     * Resets the histogram.
     */
    public void reset() {
        this.bins = new double[this.numIntervals];
    }

	/**
	 * Outputs the report of the histogram.
	 */
	public String report() {
		return "";
	}
	
	/**
	 * Output to csv file.
	 */
	public void csvReport(String outputdir){
		try {
			String dest = outputdir + "/histograms";
			File file = new File(dest);
			file.mkdir();
			FileWriter histWriter = new FileWriter(dest + "/" + observedVariable + "_hist.csv");
			FileWriter pdfWriter = new FileWriter(dest + "/" + observedVariable + "_pdf.csv");
			FileWriter distWriter = new FileWriter(dest + "/" + observedVariable + "_dist.csv");
			
			histWriter.append("#" + histogramType + "\n#" + observedVariable + "\n");
			pdfWriter.append("#" + histogramType + "\n#" + observedVariable + "\n");
			distWriter.append("#" + histogramType + "\n#" + observedVariable + "\n");
			
			histWriter.append("#lowerBound ; upperBound ; relative frequency\n");
			pdfWriter.append("#lowerBound ; upperBound ; probability density\n");
			distWriter.append("#(lowerBound+upperBound)/2 ; probability\n");
			
			String hist = "";
			String pdf = "";
			String dist = "";
			
			for (int i = 0; i < getNumIntervals(); i++) {
				hist += (getLowerBound() + i * getDelta()) + ";" + (getLowerBound() + (i + 1) * getDelta()) + ";" + getBinValue(i) / getNormalizingFactor() + "\n";
				pdf += (getLowerBound() + i * getDelta()) + ";" + (getLowerBound() + (i + 1) * getDelta()) + ";" + getBinValue(i) / getNormalizingFactor() / getDelta() + "\n";
				dist += (getLowerBound() + (i + 0.5) * getDelta()) + ";" + getBinValue(i) / getNormalizingFactor() + "\n";
			}
			
			histWriter.append(hist.replace('.', ','));
			pdfWriter.append(pdf.replace('.', ','));
			distWriter.append(dist.replace('.', ','));
			
			histWriter.close();
			pdfWriter.close();
			distWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
