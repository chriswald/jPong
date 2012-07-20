/**
 * @author  Chris Wald
 * @date    Feb 10, 2011 8:17:42 AM
 * @project jPong
 * @file    PixelRange.java
 */
package collision;

import java.awt.Dimension;
import java.awt.Point;

public class PixelRange extends Range{

	private Orientation	orientation;

	public PixelRange(final int low, final int high, final Orientation or) {
		super(low, high);
		this.orientation=or;
	}

	public boolean isInRange(final int val, final Dimension dim) {
		int newlow;

		if (this.orientation == Orientation.VERTICAL) {
			newlow=this.lowval - dim.width;
		} else {
			newlow=this.lowval - dim.height;
		}

		if (newlow < val  &&  val < this.highval) {
			return true;
		}
		return false;
	}

	public boolean isInRange(final Point pt) {
		if (this.orientation == Orientation.VERTICAL) {
			return this.isInRange(pt.x);
		}
		return this.isInRange(pt.y);
	}

	public boolean isInRange(final Point pt, final Dimension dim) {
		int newlow;
		int val;

		if (this.orientation == Orientation.VERTICAL) {
			newlow=this.lowval - dim.width;
			val=pt.x;
		} else {
			newlow=this.lowval - dim.height;
			val=pt.y;
		}

		if (newlow < val  &&  val < this.highval) {
			return true;
		}
		return false;
	}
}
