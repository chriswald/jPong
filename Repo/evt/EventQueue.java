/**
 * @author  Chris Wald
 * @date    Jan 13, 2011 8:21:56 AM
 * @project jPong
 * @file    EventQueue.java
 */
package evt;

import java.util.Vector;

import pkg.ThreadPrinter;


public class EventQueue {

	private Vector<Event> event_queue=null;
	private Boolean remove_retrieved_event=false;

	public EventQueue() {
		this.event_queue=new Vector<Event>(1);
	}

	public EventQueue(final int size) {
		if (size > 0) {
			this.event_queue=new Vector<Event>(size);
		} else {
			this.event_queue=new Vector<Event>(1);
		}
	}

	public void setRetrievedRemove(final Boolean remove) {
		this.remove_retrieved_event=remove;
	}

	public Event getEventAt(final int index) {
		Event evt;
		try {
			evt=this.event_queue.elementAt(index);
			if (this.remove_retrieved_event) {
				this.event_queue.remove(index);
			}
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			ThreadPrinter.print(AIOOBE.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			evt=null;
		}

		return evt;
	}

	public Event getNextEvent() {
		Event evt;
		try {
			evt=this.event_queue.elementAt(0);
			if (this.remove_retrieved_event) {
				this.event_queue.remove(0);
			}
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			ThreadPrinter.print(AIOOBE.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
			evt=null;
		}

		return evt;
	}

	public void addEvent(final Event evt) {
		this.event_queue.add(0, evt);
	}

	public void addEventAt(final Event evt, final int index) {
		try {
			this.event_queue.add(index, evt);
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			ThreadPrinter.print(AIOOBE.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
	}

	public int getQueueSize() {
		return this.event_queue.size();
	}

	public void removeNextEvent() {
		try {
			this.event_queue.remove(0);
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			ThreadPrinter.print(AIOOBE.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
	}

	public void removeEventAt(final int index) {
		try {
			this.event_queue.remove(index);
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			ThreadPrinter.print(AIOOBE.getMessage() + "\tOn Line " + Thread.currentThread().getStackTrace()[1].getLineNumber());
		}
	}
}
