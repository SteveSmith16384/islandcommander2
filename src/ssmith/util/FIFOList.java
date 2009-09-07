package ssmith.util;

public class FIFOList extends ThreadSafeArrayList {
	
	private int max;
	
	public FIFOList(int mx) {
		super();
		this.max = mx;
	}
	
	public void add(Object o) {
		super.add(o);
		while (this.size() > max) {
			this.remove(0);
		}
	}

}
