
package collision;

public class Range {
	protected int	lowval, highval;

	public Range(final int low, final int high) {
		this.lowval=low;
		this.highval=high;
	}

	public boolean isInRange(final int val) {
		if (this.lowval < val  &&  val < this.highval) {
			return true;
		}
		return false;
	}
}
