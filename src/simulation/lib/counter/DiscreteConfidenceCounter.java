package simulation.lib.counter;

/**
 * This class implements a discrete time confidence counter
 */
public class DiscreteConfidenceCounter extends DiscreteCounter {
    /*	Row 1: degrees of freedom
     *  Row 2: alpha 0.01
     *  Row 3: alpha 0.05
     *  Row 4: alpha 0.10
     */
    private double zAlphaMatrix[][] = new double[][]{
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 1000000},
            {63.657, 9.925, 5.841, 4.604, 4.032, 3.707, 3.499, 3.355, 3.250, 3.169, 2.845, 2.750, 2.704, 2.678, 2.660, 2.648, 2.639, 2.632, 2.626, 2.576},
            {12.706, 4.303, 3.182, 2.776, 2.571, 2.447, 2.365, 2.306, 2.262, 2.228, 2.086, 2.042, 2.021, 2.009, 2.000, 1.994, 1.990, 1.987, 1.984, 1.960},
            {6.314, 2.920, 2.353, 2.132, 2.015, 1.943, 1.895, 1.860, 1.833, 1.812, 1.725, 1.697, 1.684, 1.676, 1.671, 1.667, 1.664, 1.662, 1.660, 1.645}};

    protected double alpha;

    /**
     * Basic constructor
     *
     * @param variable the variable to observe
     */
    public DiscreteConfidenceCounter(String variable) {
        this(variable, 0.05);
    }

    /**
     * Constructor
     * Note 1 - alpha is called confidence level.
     *
     * @param variable observed variable
     * @param alpha    level of significance
     */
    public DiscreteConfidenceCounter(String variable, double alpha) {
        this(variable, "counter type: discrete-time confidence counter", alpha);
    }

    /**
     * Constructor
     *
     * @param variable observed variable
     * @param type     counter type
     * @param alpha    level of significance
     */
    public DiscreteConfidenceCounter(String variable, String type, double alpha) {
        super(variable, type);
        this.alpha = alpha;
    }

    /**
     * Gets the 1 - alpha/2 quantile of given degrees of freedom.
     *
     * @param degsOfFreedom
     * @return 1-alpha/2 quantile
     */
    public double getZ(long degsOfFreedom) {
        //Mindestens zwei Samples notwendig
        if (degsOfFreedom < 1) return 0;

        int column = 0;
        //Suche nach dem entsprechendem Eintrag im Array nach dem ScanMax-Prinzip
        for (int i = 0; i < 20; i++) {
            if (degsOfFreedom <= zAlphaMatrix[0][i]) {
                column = i;
                break;
            }
        }
        //Exakter z-Wert fuer Sample in der Tabelle?=> Sofort zurueckgeben
        if (zAlphaMatrix[0][column] == degsOfFreedom) return zAlphaMatrix[getRow()][column];

        //Anzahl der Samples ausserhalb des "Wertebereichs" => gebe den groessten Wert zurueck
        if (zAlphaMatrix[0][19] < degsOfFreedom) return zAlphaMatrix[getRow()][19];

        //Anzahl der Samples zwischen zwei Eintraegen in der Tabelle=> lineare Interpolation
        return linearInterpol(zAlphaMatrix[0][column - 1], zAlphaMatrix[0][column],
                zAlphaMatrix[getRow()][column - 1], zAlphaMatrix[getRow()][column], degsOfFreedom);
    }

    /**
     * Interpolates between two quantiles linearly if the degrees of freedom is between to entries.
     *
     * @param dflow         lower degrees of freedom
     * @param dfhigh        higher degrees of freedom
     * @param zlow          lower 1-alpha/2 quantile
     * @param zhigh         higher 1-alpha/2 quantile
     * @param degsOfFreedom degrees of freedom
     * @return
     */
    private double linearInterpol(double dflow, double dfhigh, double zlow, double zhigh, long degsOfFreedom) {
        return zlow - (((zlow - zhigh) / (dfhigh - dflow)) * (degsOfFreedom - dflow));
    }

    /**
     * Gets the row of the quantiles table
     *
     * @return row number
     */
    private int getRow() {
        if (alpha <= 0.011)
            return 1;
        if (alpha <= 0.051)
            return 2;
        else
            return 3;
    }

    /**
     * Gets lower bound of confidence interval.
     *
     * @return lower bound
     */
    public double getLowerBound() {
        return getMean() - getBound();
    }

    /**
     * Gets upper bound of confidence interval.
     *
     * @return upper bound
     */
    public double getUpperBound() {
        return getMean() + getBound();
    }

    /**
     * Gets the bound of an interval.
     *
     * @return bound
     */
    public double getBound() {
        return getZ(getNumSamples() - 1) * Math.sqrt(getVariance() / getNumSamples()); //Equation (4.8)
    }

    /**
     * @see Counter#report()
     */
    @Override
    public String report() {
        String out = super.report();
        out += ("" + "\talpha = " + alpha + "\n" +
                "\tz(1-alpha/2) = " + getZ(getNumSamples() - 1) + "\n" +
                "\tlower bound = " + getLowerBound() + "\n" +
                "\tupper bound = " + getUpperBound());
        return out;
    }

    /**
     * @see Counter#csvReport(String)
     */
    @Override
    public void csvReport(String outputdir) {
        String content = observedVariable + ";" + getNumSamples() + ";" + getMean() + ";" + getVariance() + ";" + getStdDeviation() + ";" +
                getCvar() + ";" + getMin() + ";" + getMax() + ";" + alpha + ";" + getZ(getNumSamples() - 1) + ";" + getLowerBound() + ";" +
                getUpperBound() + "\n";
        String labels = "#counter ; numSamples ; MEAN; VAR; STD; CVAR; MIN; MAX;alpha;tz(1-alpha/2);lowerBound;upperBound\n";
        writeCsv(outputdir, content, labels);
    }

}
