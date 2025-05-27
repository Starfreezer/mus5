package simulation.lib.rng;

import java.util.Random;

/**
 * Standard random number generator
 */
public class StdRNG extends RNG {
	Random rng;
	
	public StdRNG() {
		super();
	}
	/**
	 * Constructor with current milliseconds as seed
	 * @param currentTimeMillis
	 */
	public StdRNG(long currentTimeMillis) {
		super(currentTimeMillis);
	}

	/**
	 * @see RNG#setSeed(long)
	 */
	@Override
	public void setSeed(long seed) {
		if(rng == null)
			rng = new Random(seed);
		else
			rng.setSeed(seed);
	}

	/**
	 * @see RNG#rnd()
	 */
	@Override
	public double rnd() {
		double tmp = 0;

		// get random number except 0 and 1
		for(;(tmp == 0 || tmp == 1); tmp = rng.nextDouble());
		return tmp;
	}
}
