/**
 * @author  Chris Wald
 * @date    Mar 8, 2011 7:55:24 PM
 * @project jPong
 * @file    DisplayConfigs.java
 */
package pkg;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Point;

public class DisplayConfigs {
	public Dimension		dim				=new Dimension(0,0);
	public GraphicsDevice	base_gd			=null;
	public Point			coords			=new Point(0,0);

	public DisplayConfigs(final Dimension dim, final GraphicsDevice min_monitor, final Point pt) {
		this.dim=dim;
		this.base_gd=min_monitor;
		this.coords=pt;
		System.out.println(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":[" + this.base_gd + "; " + this.coords.x + ", " + this.coords.y + "; " + this.dim.width + "x" + this.dim.height + "]";
	}
}
