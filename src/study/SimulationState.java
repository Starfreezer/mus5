package study;

import simulation.lib.Customer;
import simulation.lib.SortableQueue;

/**
 * Represents the state of the simulator.
 * Changes of the simulation state happen at discrete instants of time.
 */
public class SimulationState {
	/**
	 * Indicates if server is actually busy (serves a customer). 
	 */
	public boolean serverBusy;
	
	/**
	 * Size of Queue.
	 */
	public long queueSize;
	
	/**
	 * Current customer in service.
	 */
	public Customer customerInService;
	
	/**
	 * Queue of waiting customers waiting for service.
	 */
	public SortableQueue waitingCustomers;

	/**
	 * Number of samples/customers that have been served.
	 */
	public long numSamples;
	
	/**
	 * Number of samples/customers in current batch.
	 */
    public long numSamplesInCurrentBatch;

	private SimulationStudy sims;

	/**
	 * Constructor
	 * @param sims The simulation study
	 */
	public SimulationState(SimulationStudy sims) {
		queueSize = 0;
		serverBusy = false;
		customerInService = null;
		waitingCustomers = new SortableQueue();
        numSamples = 0;
        numSamplesInCurrentBatch = 0;

		this.sims = sims;
	}

	/**
	 * Indicates if transient phase is over. (Initialisation is over)
	 * @return true if phase is over
	 */
	public boolean isTransientPhaseOver() {
		 /*
		 * TODO Problem 5.1.1 - Implement this method
		 */

		return false;
	}

	/**
	 * Increases number of samples
	 */
    public void increaseNumSamplesByOne() {
        numSamples++;

        if(this.isTransientPhaseOver()) {

            numSamplesInCurrentBatch++;

            if(numSamplesInCurrentBatch > sims.batchLength) {
                numSamplesInCurrentBatch = 1;
            }
        }
    }

    /*
	 * TODO You may introduce additional class methods
	 * e.g. for indicating that a new batch was initialized
	 */
}
