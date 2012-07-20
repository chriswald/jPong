/**
 * @author  Chris Wald
 * @date    Jan 13, 2011 8:16:32 AM
 * @project jPong
 * @file    Event.java
 */
package evt;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class Event {

	public static enum EventType{
		KeyPressed,
		KeyTyped,
		KeyReleased,
		MouseClicked,
		MousePressed,
		MouseReleased,
		MouseExited,
		MouseEntered,
		MouseDragged,
		MouseMoved,
		MouseWheelMoved
	}

	private KeyEvent  key_event=null;
	private MouseEvent mouse_event=null;
	private MouseWheelEvent mouse_wheel_event=null;
	private EventType event_type=null;

	public Event() {}
	public Event(EventType type, KeyEvent evt) {
		this.event_type=type;
		this.key_event=evt;
	}
	public Event(EventType type, MouseEvent evt) {
		this.event_type=type;
		this.mouse_event=evt;
	}
	public Event(EventType type, MouseWheelEvent evt) {
		this.event_type=type;
		this.mouse_wheel_event=evt;
	}

	public void setEventType(EventType type) {
		this.event_type=type;
	}
	public EventType getEventType() {
		return this.event_type;
	}

	public KeyEvent getKeyEvent() {
		if (this.key_event!=null) {
			return this.key_event;
		} else {
			return null;
		}
	}

	public MouseEvent getMouseEvent() {
		if (this.mouse_event!=null) {
			return this.mouse_event;
		} else {
			return null;
		}
	}

	public MouseEvent getMouseWheelEvent() {
		if (this.mouse_wheel_event!=null) {
			return this.mouse_wheel_event;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		String str="";

		switch (this.event_type) {
		case KeyPressed:		str+="Key Pressed";			break;
		case KeyReleased:		str+="Key Release";			break;
		case KeyTyped:			str+="Key Typed";			break;
		case MouseClicked:		str+="Mouse Clicked";		break;
		case MouseDragged:		str+="Mouse Dragged";		break;
		case MouseEntered:		str+="Mouse Entered";		break;
		case MouseExited:		str+="Mouse Exited";		break;
		case MouseMoved:		str+="Mouse Moved";			break;
		case MousePressed:		str+="Mouse Pressed";		break;
		case MouseReleased:		str+="Mouse Released";		break;
		case MouseWheelMoved:	str+="Mouse Wheel Moved";	break;
		default:	break;
		}

		return str;
	}
}
