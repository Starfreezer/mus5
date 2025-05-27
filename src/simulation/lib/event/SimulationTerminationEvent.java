package simulation.lib.event;

/**
 * Simulation termination event.
 * This events terminates the simulation.
 */
public class SimulationTerminationEvent extends Event {
	
	/**
	 * Constructor
	 * @param eventTime The time of the event occurrence
	 */
	public SimulationTerminationEvent(long eventTime) {
		super(eventTime);
	}


	/**
	 * @see Event#process()
	 */
	@Override
	public void process() {
		fireStopEventNotification();
	}

}
