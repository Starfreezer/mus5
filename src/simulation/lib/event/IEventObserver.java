package simulation.lib.event;
/**
 * Interface for event observer.
 * Used to handle notifications by IObservable objects.
 */
public interface IEventObserver {
	/**
	 * Event notification for updating simulation statistics.
	 * @param sender CustomerArrivalEvent, ServiceCompletionEvent
	 * @param arg current customer or null if CustomerArrivalEvent is sender
	 */
	void updateStatisticsHandler(Object sender, Object arg);
	
	/**
	 * Event notification for updating customer queue occupancy 
	 * @param sender CAE, SCE
	 */
	void updateQueueOccupancyHandler(Object sender);
	
	/**
	 * Event notification for pushing new event into event chain.
	 * @param c type of new event that should be pushed CAE, SCE
	 */
	void pushNewEventHandler(Class<?> c);
	
	/**
	 * Event notification for stopping the simulation
	 * @param sender SimulationTerminationEvent
	 */
	void stopEventHandler(Object sender);
}
