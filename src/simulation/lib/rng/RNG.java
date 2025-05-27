package simulation.lib.rng;

/**
 * Random number generator.
 */
public abstract class RNG {
	protected long seed;
	
	/**
	 * Default constructor
	 */
	public RNG() {
		setSeed(0);
	}
	
	/**
	 * Constructor with given seed
	 * @param seed
	 */
	public RNG(long seed) {
		setSeed(seed);
	}
	
	/**
	 * Returns a random number (0,1)
	 * @return random number (0,1)
	 */
	public abstract double rnd();
	
	/**
	 * Set a new seed
	 */
	public abstract void setSeed(long seed);

}
