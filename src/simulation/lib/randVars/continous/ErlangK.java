package simulation.lib.randVars.continous;

import simulation.lib.randVars.RandVar;
import simulation.lib.rng.RNG;

/**
 * Erlang-k distributed random variable.
 */
public class ErlangK extends RandVar {
    private double lambda;
    private int k;

    /**
     * Constructor
     *
     * @param rng random number generator
     * @param m   mean
     * @param k   k parameter of Erlang-k distribution
     */
    public ErlangK(RNG rng, double m, int k) {
        super(rng);
        lambda = k / m;
        this.k = k;
    }

    /**
     * Constructor
     *
     * @param rng random number generator
     * @param m   mean
     * @param s   standard deviation
     */
    public ErlangK(RNG rng, double m, double s) {
        super(rng);
        this.setMeanAndStdDeviation(m, s);
    }

    /**
     * @see RandVar#getRV()
     */
    @Override
    public double getRV() {
        double s = 1.0;
        for (int i = 0; i < k; i++) {
            s = s * rng.rnd();
        }
        return -Math.log(s) / lambda;
    }

    /**
     * @see RandVar#getMean()
     */
    @Override
    public double getMean() {
        return k / lambda;
    }

    /**
     * @see RandVar#getVariance()
     */
    @Override
    public double getVariance() {
        return k / (lambda * lambda);
    }

    /**
     * @see RandVar#setMean(double)
     */
    @Override
    public void setMean(double m) {
        if (m <= 0)
            throw new IllegalArgumentException("mean must not be lower equals 0");
        lambda = k / m;
    }

    /**
     * @see RandVar#setStdDeviation(double)
     */
    @Override
    public void setStdDeviation(double s) {
        double mean = this.getMean();
        k = (int) (int) Math.floor(Math.pow(mean / s, 2));
        lambda = k / mean;
    }

    /**
     * @see RandVar#setMeanAndStdDeviation(double, double)
     */
    @Override
    public void setMeanAndStdDeviation(double m, double s) {
        this.k = (int) Math.floor(1 / Math.pow(s / m, 2));
        this.lambda = this.k / m;
    }

    /**
     * @see RandVar#getType()
     */
    @Override
    public String getType() {
        return "Erlang-k";
    }

    @Override
    public String toString() {
        return super.toString() +
                "\tparameters:\n" +
                "\t\tlambda: " + lambda + "\n" +
                "\t\tk: " + k + "\n";
    }
}
