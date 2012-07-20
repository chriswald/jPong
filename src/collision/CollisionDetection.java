package collision;

import javax.swing.JFrame;

public class CollisionDetection {

	public static Side detectCollisionArea(final JFrame f0, final JFrame f1) {
		try {
			PixelRange pxr0=new PixelRange(f0.getX(), f0.getX() + f0.getWidth(), Orientation.VERTICAL);
			PixelRange pxr1=new PixelRange(f0.getY(), f0.getY() + f0.getHeight(), Orientation.HORIZONTAL);

			if (pxr0.isInRange(f1.getLocation(), f1.getSize())  &&  pxr1.isInRange(f1.getLocation(), f1.getSize())) {

				double base_frac=(f0.getY() + (f0.getHeight() / 2)) / (f0.getX() + (f0.getWidth() / 2));
				double working_frac, T, B;
				T=(f1.getY() + (f1.getHeight() / 2)) - (f0.getY() + (f0.getHeight() / 2));
				B=(f1.getX() + (f1.getWidth() / 2)) - (f0.getX() + (f0.getWidth() / 2));
				working_frac=T / B;

				if ((T > 0  &&  B < 0)  ||  (T < 0  &&  B > 0)) {
					base_frac=-base_frac;
				}

				if (working_frac > base_frac) {
					if (working_frac > 0) {
						return Side.RIGHT;
					}
					return Side.LEFT;
				} else {
					if (T > 0) {
						return Side.BOTTOM;
					} else {
						return Side.TOP;
					}
				}

			} else {
				return Side.NONE;
			}
		} catch (NullPointerException e) {
			return Side.NONE;
		}
	}

	public static Side detectCollisionArea1(final JFrame f0, final JFrame f1) {
		try {


			if (f0.getX() + f0.getWidth()	    > f1.getX() + 1					&&
					f0.getX()					< f1.getX() + f1.getWidth() + 1	&&
					f0.getY() + f0.getHeight()	> f1.getY() + 1					&&
					f0.getY()					< f1.getY()+f1.getHeight() + 1) {

				double base_frac=(f0.getY() + (f0.getHeight() / 2)) / (f0.getX() + (f0.getWidth() / 2));
				double working_frac, T, B;
				T=(f1.getY() + (f1.getHeight() / 2)) - (f0.getY() + (f0.getHeight() / 2));
				B=(f1.getX() + (f1.getWidth() / 2)) - (f0.getX() + (f0.getWidth() / 2));
				working_frac=T / B;

				if ((T > 0  &&  B < 0)  ||  (T < 0  &&  B > 0)) {
					base_frac=-base_frac;
				}

				if (working_frac > base_frac) {
					if (working_frac > 0) {
						return Side.RIGHT;
					} else {
						return Side.LEFT;
					}
				} else {
					if (T > 0) {
						return Side.BOTTOM;
					} else {
						return Side.TOP;
					}
				}

			} else {
				return Side.NONE;
			}
		} catch (NullPointerException e) {
			return Side.NONE;
		}
	}
}
