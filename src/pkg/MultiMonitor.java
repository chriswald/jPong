/**
 *     _______    __    __   ________   __________    _______
 *    /\   __ \  /\ \  /\ \ /\  ____ \ /\____  ___\  /\  ____\
 *    \ \ \_/\_\ \ \ \_\_\ \\ \ \___\ \\/___/\ \__/  \ \ \___/
 *     \ \ \\/_/  \ \  ____ \\ \  ___ <     \ \ \     \ \____`\
 *      \ \ \   __ \ \ \__/\ \\ \ \ /\ \     \ \ \     \/___/\ \
 *       \ \ \__\ \ \ \ \ \ \ \\ \ \\ \ \    _\_\ \____   __\_\ \
 *        \ \______\ \ \_\ \ \_\\ \_\\ \_\  /\_________\ /\______\
 *         \/______/  \/_/  \/_/ \/_/ \/_/  \/_________/ \/______/
 *                __      __    ________    __        ______
 *               /\ \    /\ \  /\  ____ \  /\ \      /\  ___`,
 *               \ \ \   \ \ \ \ \ \__/\ \ \ \ \     \ \ \_/\ \
 *                \ \ \   \ \ \ \ \ \_\_\ \ \ \ \     \ \ \\ \ \
 *                 \ \ \  _\ \ \ \ \  ____ \ \ \ \     \ \ \\ \ \
 *                  \ \ \_\ \_\ \ \ \ \__/\ \ \ \ \_____\ \ \\_\ \
 *                   \ \_________\ \ \_\ \ \_\ \ \______\\ \_____/
 *                    \/_________/  \/_/  \/_/  \/______/ \/____/
 *
 *         ->Multi-Monitor
 *         ->Developed By Christopher J. Wald
 *         ->Copyright 2011 (c) All Rights Reserved
 * 
 * 
 * @author		Christopher J. Wald
 * @date		Sep 29, 2010 8:02:14 PM
 * @file		MultiMonitor.java
 * @description	A class for building applications with suport for multiple
 * 				monitors. Provides a framework for retrieving information
 * 				about individual monitors, as well as for the whole monitor
 * 				setup.
 * @license:
 *
 * 	Redistribution and use in source and binary forms, with or without
 * 	modification, are permitted provided that the following conditions
 * 	are met:
 *
 *	- Redistributions of source code must retain the above copyright
 *	  notice, this list of conditions and the following disclaimer.
 *
 *	- Redistributions in binary form must reproduce the above copyright
 *	  notice, this list of conditions and the following disclaimer in the
 *	  documentation and/or other materials provided with the distribution.
 *
 *	- The name of Christopher J. Wald may not be used to endorse or promote
 *	  products derived from this software without specific prior written
 *	  permission.
 *
 * 	THIS SOFTWARE IS PRIVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY
 * 	EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * 	IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * 	ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
 * 	DIRECT, INDIRECT, INCIDENTAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING
 * 	BUT NOT LIMITED TO UNDESIRED ACTION, LOSS OF SECURITY, LOSS OF DATA, LOSS OF
 * 	SLEEP,  LOSS OF HAIR, OR EXPLOSIONS). USE AT YOUR OWN RISK.
 */

package pkg;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

public class MultiMonitor {
	private GraphicsDevice[] gd=null;

	/**
	 * Constructor - populates the GraphicsDevice array
	 */
	public MultiMonitor() {
		this.getMultiMonitor();
		ThreadPrinter.print_ntd(this.toString());
	}

	/**
	 * populates the GraphicsDevice array
	 * can be called multiple times if monitor situations are presumed to have changed
	 */
	private void getMultiMonitor() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.gd = ge.getScreenDevices();
	}

	/**
	 * Returns the GraphicsDevice array
	 * @return GraphicsDevice[] gd
	 */
	public GraphicsDevice[] getGraphicsDeviceList() {
		return this.gd;
	}

	/**
	 * Returns a specific graphic device if available
	 * @param index
	 * @return GraphicsDevice gd
	 */
	public GraphicsDevice getGraphicsDevice(final int index) {
		try {
			return this.gd[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Returns the X coordinate, relative to the base monitor, of the selected device
	 * @param index
	 * @return int offset
	 */
	public int getXOffset(final int index) {
		try {
			return this.gd[index].getConfigurations()[index].getBounds().x;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Returns the Y coordinate, relative to the base monitor, of the selected device
	 * @param index
	 * @return int offset
	 */
	public int getYOffset(final int index) {
		try {
			return this.gd[index].getConfigurations()[index].getBounds().y;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Returns the width of the selected device
	 * @param index
	 * @return int width
	 */
	public int getWidth(final int index) {
		try {
			return this.gd[index].getConfigurations()[index].getBounds().width;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Returns the height of the selected device
	 * @param index
	 * @return int height
	 */
	public int getHeight(final int index) {
		try {
			return this.gd[index].getConfigurations()[index].getBounds().height;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Returns the width of the widest monitor
	 * @return int width
	 */
	public int getMaxMonitorWidth() {
		int width=0;

		for (GraphicsDevice curGd : this.gd) {
			if (curGd.getConfigurations()[0].getBounds().width > width) {
				width=curGd.getConfigurations()[0].getBounds().width;
			}
		}

		return width;
	}

	/**
	 * Returns the height of the tallest monitor
	 * @return int height
	 */
	public int getMaxMonitorHeight() {
		int height=0;

		for (GraphicsDevice curGd : this.gd) {
			if (curGd.getConfigurations()[0].getBounds().y > height) {
				height+=curGd.getConfigurations()[0].getBounds().height;
			}
		}

		return height;
	}

	/**
	 * Returns the width of the narrowest monitor
	 * @return int width
	 */
	public int getMinMonitorWidth() {
		int width=this.gd[0].getConfigurations()[0].getBounds().width;

		for (GraphicsDevice curGd : this.gd) {
			if (curGd.getConfigurations()[0].getBounds().height < width) {
				width=curGd.getConfigurations()[0].getBounds().width;
			}
		}

		return width;
	}

	/**
	 * Returns the width of the shortest monitor
	 * @return int height
	 */
	public int getMinMonitorHeight() {
		int height=this.gd[0].getConfigurations()[0].getBounds().height;

		for (GraphicsDevice curGd : this.gd) {
			if (curGd.getConfigurations()[0].getBounds().height < height) {
				height=curGd.getConfigurations()[0].getBounds().height;
			}
		}

		return height;
	}

	/**
	 * Returns the number of detected monitors
	 * @return int total
	 */
	public int getTotalMonitors() {
		return this.gd.length;
	}

	/**
	 * Returns the device with coordinates farthest to the left of the base monitor
	 * @return GraphicsDevice device
	 */
	public GraphicsDevice getMinCoordinatesMonitor() {
		/*Point pt=new Point(this.gd[0].getConfigurations()[0].getBounds().x, this.gd[0].getConfigurations()[0].getBounds().y);
		double di=0;
		GraphicsDevice minCoord=this.gd[0];
		for (GraphicsDevice curGd : this.gd) {

			double distance = this.distance(0, 0, curGd.getConfigurations()[0].getBounds().x, curGd.getConfigurations()[0].getBounds().y);

			if (pt.x > curGd.getConfigurations()[0].getBounds().x  &&  distance > di) {
				pt=new Point(curGd.getConfigurations()[0].getBounds().x, curGd.getConfigurations()[0].getBounds().y);
				minCoord=curGd;
				di=distance;
			}
		}
		return minCoord;*/

		GraphicsDevice minGd = this.gd[0];
		int x = minGd.getConfigurations()[0].getBounds().x;
		int y = minGd.getConfigurations()[0].getBounds().y;
		double r = this.distance(0, 0, x, y);
		for (GraphicsDevice curGd : this.gd) {
			int temp_x = curGd.getConfigurations()[0].getBounds().x;
			int temp_y = curGd.getConfigurations()[0].getBounds().y;
			double temp_r = this.distance(0, 0, temp_x, temp_y);
			double temp_theta = Math.atan2(this.slope(0, 0, temp_x, temp_y), 1);

			if (temp_r > r  &&  (temp_theta > (Math.PI / 2) && temp_theta < (3 * Math.PI / 2))) {
				minGd = curGd;
				r = temp_r;
			}
		}

		return minGd;

	}

	/**
	 * Returns the coordinates of the device farthest to the left of the base monitor
	 * @return Point coords
	 */
	public Point getMinCoordinates() {
		Rectangle min = this.getMinCoordinatesMonitor().getConfigurations()[0].getBounds();
		return new Point(min.x, min.y);
	}

	/**
	 * Returns the device with the coordinates farthest to the right of the base monitor
	 * @return GraphicsDevice device
	 */
	public GraphicsDevice getMaxCoordinatesMonitor() {
		Point pt=new Point(0, 0);
		double di=0;
		GraphicsDevice maxCoord=this.gd[0];
		for (GraphicsDevice curGd : this.gd) {

			double distance = this.distance(0, 0, curGd.getConfigurations()[0].getBounds().x, curGd.getConfigurations()[0].getBounds().y);

			if (pt.x < curGd.getConfigurations()[0].getBounds().x  &&  distance > di) {
				pt=new Point(curGd.getConfigurations()[0].getBounds().x, curGd.getConfigurations()[0].getBounds().y);
				maxCoord=curGd;
				di=distance;
			}
		}
		return maxCoord;
	}

	/**
	 * Returns the coordinates of the device farthest to the right of the base monitor
	 * @return Point coords
	 */
	public Point getMaxCoordinates() {
		return new Point(this.getMaxCoordinatesMonitor().getConfigurations()[0].getBounds().x + this.getMaxCoordinatesMonitor().getConfigurations()[0].getBounds().width,
				this.getMaxCoordinatesMonitor().getConfigurations()[0].getBounds().y + this.getMaxCoordinatesMonitor().getConfigurations()[0].getBounds().height);
	}



	private double distance (final int x0, final int y0, final int x1, final int y1) {
		return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
	}

	private double slope (final int x0, final int y0, final int x1, final int y1) {
		try {
			return (y1 - y0) / (x1 - x0);
		} catch (ArithmeticException e) {
			return 0;
		}
	}

	@Override
	public String toString() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String str=this.getClass().getSimpleName() + ":";

		for (int i=0; i< ge.getScreenDevices().length; i++) {
			str+="[" + i + ": " + ge.getScreenDevices()[i].getConfigurations()[0].getBounds().x + ", " + ge.getScreenDevices()[i].getConfigurations()[0].getBounds().y + "; " + ge.getScreenDevices()[i].getDisplayMode().getWidth() + "x" + ge.getScreenDevices()[i].getDisplayMode().getHeight() + "]";
		}

		return str;
	}
}
