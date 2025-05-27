package simulation.lib.event;

/**
 * Interface for observable events.
 * IEventObserver can be registered/unregistered.
 */
public interface IObservableEvent {
	/**
	 * Register observer for notifications
	 * @param obs observer object
	 */
	void register(IEventObserver obs);
	
	/**
	 * Unregister observer from notifications
	 * @param obs observer object
	 */
	void unregister(IEventObserver obs);
}
