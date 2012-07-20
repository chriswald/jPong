/**
 * @author  Chris Wald
 * @date    Jan 16, 2011 11:17:09 AM
 * @project jPong
 * @file    EventConsole.java
 */
package evt;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import pkg.ThreadPrinter;

public class EventConsole implements Runnable{

	private JFrame frame;
	private JScrollPane scroll_pane;
	private JList list;
	private DefaultListModel list_model;
	private int events_displayed=0;

	public EventConsole() {
		this.list_model		=new DefaultListModel();
		this.frame			=new JFrame("Event Console");
		this.scroll_pane	=new JScrollPane();
		this.list			=new JList(this.list_model);

		this.frame.add(this.scroll_pane);
		this.scroll_pane.add(this.list);
		this.frame.setSize(150, 200);
	}

	@Override
	public void run() {
		this.frame.setVisible(true);
	}

	public void addEvent(Event evt) {
		ThreadPrinter.print("Here");
		this.list_model.add(0, this.events_displayed + ": " + evt.toString());
		this.events_displayed++;
	}
}
