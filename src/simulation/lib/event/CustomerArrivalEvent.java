package simulation.lib.event;

import simulation.lib.Customer;
import study.SimulationState;

/**
 * Customer arrival event. Customer arrives at the single server queue simulator.
 */
public class CustomerArrivalEvent extends Event {
	private SimulationState state;

	/**
	 * Constructor
	 * @param eventTime The time of the event occurrence
	 */
	public CustomerArrivalEvent(SimulationState state, long eventTime) {
		super(eventTime);
		this.state = state;
	}

	/**
	 * @see Event#process()
	 */
	@Override
	public void process() {
		// update queue occupancy
		fireUpdateQueueOccupancyNotification();
		Customer customer = new Customer(getTime());

		firePushNewEventNotification(CustomerArrivalEvent.class);

		// check if server is busy
		if (state.serverBusy == true) {
			// server is busy
			state.waitingCustomers.pushNewElement(customer);
			state.queueSize++;
		} else {
			// server not busy
			state.customerInService = customer;
			customer.serviceStartTime = getTime();

			firePushNewEventNotification(ServiceCompletionEvent.class);
			state.serverBusy = true;
		}

		fireUpdateStatisticsNotification(null);
	}
}
