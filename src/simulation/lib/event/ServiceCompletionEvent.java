package simulation.lib.event;
import simulation.lib.Customer;
import study.SimulationState;

/**
 * Service completion event.
 * Customer's service is finished.
 */
public class ServiceCompletionEvent extends Event {
	private SimulationState state;
	/**
	 * Constructor
	 * @param state simulation state
	 * @param eventTime The time of the event occurrence
	 */
	public ServiceCompletionEvent(SimulationState state, long eventTime) {
		super(eventTime);
		this.state = state;
	}

	/**
	 * @see Event#process()
	 */
	@Override
	public void process() {		
		Customer currentCustomer = state.customerInService;

		// check queue size
		if (state.queueSize > 0) {
			// queue contains customers
			state.customerInService = ((Customer) state.waitingCustomers.popNextElement());
			state.customerInService.serviceStartTime = getTime();
			firePushNewEventNotification(ServiceCompletionEvent.class);
			state.queueSize--;
		} else {
			// queue is empty
			state.serverBusy = false;
			state.customerInService = null;
		}

		// update statistics
        state.increaseNumSamplesByOne();
        fireUpdateStatisticsNotification(currentCustomer);
	}
}
