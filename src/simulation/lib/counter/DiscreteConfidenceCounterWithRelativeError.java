package simulation.lib.counter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class implements a discrete time confidence counter with relativ error.
 */
public class DiscreteConfidenceCounterWithRelativeError extends DiscreteConfidenceCounter {
	
	/**
	 * Basic constructor
	 * @param variable the variable to observe
	 */
	public DiscreteConfidenceCounterWithRelativeError(String variable) {
		this(variable, 0.05);
	}
	
	/**
	 * Constructor
	 * @param variable observed variable
	 * @param alpha level of significance
	 */
	public DiscreteConfidenceCounterWithRelativeError(String variable, double alpha) {
		super(variable, "counter type: discrete-time confidence counter with relativ error", alpha);
	}
	
	/**
	 * Calculates the maximal relative error of the confidence counter.
	 * @return maximal relative error
	 */
	public double maxRelErr() {
		if((getNumSamples() > 2) && (Math.abs(getMean()) > getBound())){
			return(getBound() / (Math.abs(getMean()) - getBound()));
		}else{
			return Double.POSITIVE_INFINITY;
		}
	}

	/**
	 * Calculates the maximal absolute error of the confidence counter.
	 * @return maximal absolute error
	 */
	public double maxAbsErr() {
        return getBound();
	}

	/**
	 * @see Counter#report()
	 */
	@Override
	public String report() {
		String out  = super.report();
		out += ("\tmaximum relative error = " + maxRelErr() + "\n"
				+ "\tmaximum absolute error = " + maxAbsErr() + "\n");
		return out;
	}
	
	/**
	 * @see Counter#csvReport(String)
	 */
	@Override
	public void csvReport(String outputdir){
	    String content = observedVariable + ";" + getNumSamples() + ";" + getMean() + ";" + getVariance()  + ";" + getStdDeviation() + ";"
                + getCvar() + ";" + getMin() + ";" + getMax() + ";" + alpha + ";" + getZ(getNumSamples()) + ";" + getLowerBound() + ";" +
                ";" + getUpperBound() + ";" + maxRelErr() + ";" + maxAbsErr() + "\n";
	    String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX;"
                + "alpha;tz(1-alpha/2);lowerBound;upperBound;maxRelErr;maxAbsErr\n";
	    writeCsv(outputdir, content, labels);
	}
}
