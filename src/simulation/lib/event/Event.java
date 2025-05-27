package simulation.lib.event;

/**
 * Abstract class for events
 * All events have to extend this class
 */
public abstract class Event extends SortableQueueItem implements IObservableEvent {
	private long eventTime;
	private IEventObserver observer;

	/**
	 * Constructor
	 * @param eventTime The time of the event occurrence
	 */
	public Event(long eventTime) {
		this.eventTime = eventTime;
	}
	
	/**
	 * Starts the event
	 * @throws Exception 
	 */
	public abstract void process();	

	/**
	 * Returns the event time
	 * @return event time
	 */
	public long getTime() {
		return eventTime;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SortableQueueItem o) {
		if (o instanceof Event) {
			Event e = (Event) o;
			if(equals(o)) {
				return 0;
			}
			if(eventTime < e.eventTime) {
				return -1;
			}
			if(eventTime > e.eventTime) {
				return 1;
			}
		}
		return 1;
	}
	
	/**
	 * Register EventObserver.
	 * Note in this case there can only be one observer per event.
	 */
	public void register(IEventObserver obs) {
		if(observer != null) {
			throw new IllegalArgumentException("Unregister observer first.");
		}
		else {
			observer = obs;
		}
	}
	
	/**
	 * Unregister the observer
	 */
	public void unregister(IEventObserver obs) {
		observer = null;
	}
	
	/**
	 * Update statistic notification.
	 * @param arg null or current customer
	 */
	protected void fireUpdateStatisticsNotification(Object arg){
		if(observer != null){
			observer.updateStatisticsHandler(this, arg);
		}
	}
	
	/**
	 * Update queue occupancy notification.
	 */
	protected void fireUpdateQueueOccupancyNotification(){
		if(observer != null){
			observer.updateQueueOccupancyHandler(this);
		}
	}
	
	/**
	 * Push new event notification
	 * @param c type of new event (CustomerArrivalEvent, ServiceCompletionEvent or SimulationTerminationEvent)
	 */
	protected void firePushNewEventNotification(Class<?> c){
		if(observer != null){
			observer.pushNewEventHandler(c);
		}
	}
	
	/**
	 * Stop simulation notification.
	 */
	protected void fireStopEventNotification(){
		if(observer != null){
			observer.stopEventHandler(this);
		}
	}
}
