/**
 * @author  Chris Wald
 * @date    Jan 13, 2011 8:18:22 AM
 * @project jPong
 * @file    EventSystem.java
 */
package evt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import evt.Event.EventType;



public class EventSystem implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

	//private EventBroadcaster event_broadcaster=null;
	private EventQueue event_queue=null;
	private EventConsole console=null;

	@Override
	public void keyPressed(final KeyEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.KeyPressed, evt));
	}
	@Override
	public void keyTyped(final KeyEvent evt) {
		EventSystem.this.event_queue.addEvent(new Event(EventType.KeyTyped, evt));
	}
	@Override
	public void keyReleased(final KeyEvent evt) {
		EventSystem.this.event_queue.addEvent(new Event(EventType.KeyReleased, evt));
	}

	@Override
	public void mouseClicked(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseClicked, evt));
	}
	@Override
	public void mousePressed(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MousePressed, evt));
	}
	@Override
	public void mouseReleased(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseReleased, evt));
	}
	@Override
	public void mouseExited(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseExited, evt));
	}
	@Override
	public void mouseEntered(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseEntered, evt));
	}

	@Override
	public void mouseDragged(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseDragged, evt));
	}
	@Override
	public void mouseMoved(final MouseEvent evt){
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseMoved, evt));
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent evt) {
		EventSystem.this.event_queue.addEvent(new Event(EventType.MouseWheelMoved, evt));
	}

	public EventSystem(final Boolean console) {
		//this.event_broadcaster=new EventBroadcaster();
		this.event_queue=new EventQueue();
		if (console) {
			this.console=new EventConsole();
			Thread t=new Thread(this.console);
			t.start();
		}
	}

	public EventSystem(final EventQueue queue, final Boolean console) {
		this.event_queue=queue;
		if (console) {
			this.console=new EventConsole();
			Thread t=new Thread(this.console);
			t.start();
		}
	}

	public EventQueue getEventQueue() {
		if (this.event_queue.getQueueSize()>0) {
			return this.event_queue;
		} else {
			return null;
		}
	}

	public MouseEvent getMouseEvent() {
		return this.event_queue.getNextEvent().getMouseEvent();
	}

	public KeyEvent getKeyEvent() {
		return this.event_queue.getNextEvent().getKeyEvent();
	}

	public int getKeyCode() {
		return this.event_queue.getNextEvent().getKeyEvent().getKeyCode();
	}

	public Event getNextEvent() {
		if (this.event_queue.getQueueSize() > 0) {
			return this.event_queue.getNextEvent();
		} else {
			return null;
		}
	}

	public EventType getNextEventType() {
		if (this.event_queue.getQueueSize() > 0) {
			return this.event_queue.getNextEvent().getEventType();
		} else {
			return null;
		}
	}
}
