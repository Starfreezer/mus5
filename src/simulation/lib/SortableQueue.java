package simulation.lib;

import java.util.PriorityQueue;

import simulation.lib.event.SortableQueueItem;
/**
 * Event chain type for the simulator, stores the events.
 * Next element is the one with the lowest associated time
 */
public class SortableQueue {
	private PriorityQueue<SortableQueueItem> chain;

	/**
	 * Default constructor
	 */
	public SortableQueue() {
		chain = new PriorityQueue<SortableQueueItem>();
	}

	/**
	 * Inserts a new event in the event chain
	 * @param e the new event
	 */
	public void pushNewElement(SortableQueueItem e) {
		chain.add(e);
	}

	/**
	 * Returns the next event
	 * @return the next event
	 */
	public SortableQueueItem popNextElement() {
		return chain.poll();
	}

	/**
	 * Returns the current size of the event chain
	 * @return the chain size
	 */
	public int size() {
		return chain.size();
	}

	/**
	 * Removes all events from the event chain
	 */
	public void clear() {
		chain.clear();
	}
}
