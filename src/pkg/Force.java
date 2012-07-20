/**
 * @author  Chris Wald
 * @date    Sep 13, 2011 7:56:48 AM
 * @project jPong
 * @file    DisplayConfigs.java
 */

package pkg;

import java.awt.Dimension;

public class Force {

	private double magx=0.0;
	private double magy=0.0;

	public Force(final Dimension magnitude) {
		this.magx=magnitude.width;
		this.magy=magnitude.height;
	}

	public Force(final double xforce, final double yforce) {
		this.magx=xforce;
		this.magy=yforce;
	}

	public double getMagnitudeX() {
		return this.magx;
	}
	public double getMagnitudeY() {
		return this.magy;
	}
}
