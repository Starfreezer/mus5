package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;
import static java.lang.Math.*;

/**
 * Hyperexponential distributed random variable.
 */
public class HyperExponential extends RandVar {

	private double lambda1;
	private double lambda2;
	private double p1;
	private double p2;
	private double mean;
	
	/**
	 * Basic constructor
	 * @param rng random number generator
	 */
	public HyperExponential(RNG rng) {
		this(rng, 1, 2);
	}

	/**
	 * Constructor
	 * @param rng random number generator
	 * @param mean mean
	 * @param Cvar coefficient of variance
	 */
	public HyperExponential(RNG rng, double mean, double Cvar) {
		super(rng);
		this.mean = mean;
		setCvar(Cvar);
	}

    /**
     * @see RandVar#getRV()
     */
    @Override
    public double getRV() {
        double u1 = rng.rnd();
        if(p1 <= u1){
			return -(Math.log(rng.rnd()) * (1/lambda2));
		}else{
			return -(Math.log(rng.rnd()) * (1/lambda1));
        }

    }

	/**
	 * @see RandVar#getMean()
	 */
	@Override
	public double getMean() {
		return (p1 / lambda1 + p2 / lambda2);
	}

	/**
	 * @see RandVar#getVariance()
	 */
	@Override
	public double getVariance() {
		double m1Square = this.getMean() * this.getMean();
		double m2 = 2 * p1 * 1 / Math.pow(lambda1, 2) + 2 * p2 * 1 / Math.pow(lambda2, 2);

		return m2 - m1Square;
	}

	/**
	 * @see RandVar#setMean(double)
	 */
	@Override
	public void setMean(double m) {
		if(m <= 0)
			throw new IllegalArgumentException("mean must not be lower equals 0");
		double Cvar = (double) Math.round(this.getCvar() * 100000d) / 100000d; // avoiding .999... decimals
        mean =  m;
		setCvar(Cvar);
	}

	/**
	 * @see RandVar#setMeanAndStdDeviation(double, double)
	 */
	@Override
	public void setMeanAndStdDeviation(double m, double s) {
        throw new UnsupportedOperationException();
	}

	/**
	 * @see RandVar#setStdDeviation(double)
	 */
	@Override
	public void setStdDeviation(double s) {
        throw new UnsupportedOperationException();
	}
	/**
	 * @see RandVar#setCvar(double)
	 */
	@Override
	public void setCvar(double c) {
		double helper;
		if(mean != 0) {
			helper =((c * c) - 1) / ((c * c) + 1);
			helper = sqrt(helper);
			lambda1 = 1 / mean * (1 + helper);
			lambda2 = 1 / mean * (1 - helper);
		} else {
            throw new IllegalArgumentException("mean == 0 -> cvar can not be set");
        }
		p2 = lambda2 / (lambda1 + lambda2);
		p1 = 1 - p2;
	}

    /**
     * @see RandVar#getType()
     */
    @Override
    public String getType() {
        return "HyperExponential";
    }
	
	@Override
	public String toString() {
		return super.toString() + 
			"\tparameters:\n" +
			"\t\tlambda1: " + lambda1 + "\n"+
			"\t\tlambda2: " + lambda2 + "\n"+
			"\t\tp1: " + p1 + "\n"+
			"\t\tp2: " + p2 + "\n";
	}	
	
}
