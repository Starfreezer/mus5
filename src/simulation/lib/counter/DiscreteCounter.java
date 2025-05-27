package simulation.lib.counter;


/**
 * This class implements a discrete time counter
 */
public class DiscreteCounter extends Counter {

	/**
	 * Basic constructor
	 * @param variable the variable to observe
	 */
	public DiscreteCounter(String variable) {
		super(variable, "counter type: discrete-time counter");
	}
	
	/**
	 * Basic constructor
	 * @param variable the variable to observe
	 */
	protected DiscreteCounter(String variable, String type) {
		super(variable, type);
	}	
	
	/**
	 * @see Counter#getMean()
	 */
	@Override
	public double getMean() {
		return getNumSamples() > 0 ? getSumPowerOne() / getNumSamples() : 0;
	}
	
	/**
	 * @see Counter#getVariance()
	 */
	@Override
	public double getVariance() {
		if(getNumSamples() > 0)
			return getNumSamples() / (double)(getNumSamples() - 1) * (getSumPowerTwo() / getNumSamples() - getMean() * getMean()); //Equation 2.30
		else
			return 0;
	}
	
	/**
	 * @see Counter#count(double)
	 */
	@Override
	public void count(double x) {
		super.count(x);
		increaseSumPowerOne(x);
		increaseSumPowerTwo(x * x);
	}
}
