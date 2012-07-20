/**
 * @author  Chris Wald
 * @date    Feb 28, 2011 7:52:30 PM
 * @project jPong
 * @file    ExceptionQueue.java
 */
package exceptionQueue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import pkg.ThreadPrinter;

public class ExceptionQueue {
	private Vector<Exception>	exceptions	=new Vector<Exception>(1);
	private boolean				disable_log	=false;

	private class Exception{
		private String	message	="";
		private String	info	="";

		public Exception(final String message, final String info) {
			this.message=message;
			this.info=info;
		}

		public String getReport() {
			return this.message + (this.info=="" || this.info==null ? "" : ": " + this.info);
		}
	}

	public ExceptionQueue(){}

	public void addException(final String message, final String info) {
		this.exceptions.add(new Exception(message, info));
		if (!this.disable_log) {
			this.printToFile();
		}
	}

	public int getQueueSize() {
		return this.exceptions.size();
	}

	public String getException(final int index) {
		String tmp=this.exceptions.get(index).getReport();
		this.exceptions.remove(index);
		return tmp;
	}

	private void printToFile() {
		BufferedWriter out=null;

		try {
			out=new BufferedWriter(new FileWriter("Log.txt"));

			String DATE_FORMAT_NOW = "[dd-MM-yyyy HH:mm:ss]";
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

			out.append(sdf.format(cal.getTime()) + " " + this.exceptions.get(this.exceptions.size()-1).getReport() + "\n");

		} catch (IOException IOE) {
			this.disable_log=true;
			ThreadPrinter.print("Exception Logging Disabled");
		} catch (NullPointerException NPE){
			return;
		} finally {
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
	}
}
