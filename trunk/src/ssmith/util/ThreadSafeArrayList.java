package ssmith.util;

public class ThreadSafeArrayList {

	private Object[] objects;
	private int array_end_pointer;
	private Object lock = new Object();

	public ThreadSafeArrayList() {
		clear();
	}

	public void clear() {
		synchronized (lock) {
			objects = new Object[10];
			array_end_pointer = 0;
		}
	}

	public void add(Object o) {
		synchronized (lock) {
			this.checkSize();
			objects[array_end_pointer] = o;
			array_end_pointer++;
		}
	}

	public void add(int idx, Object o) {
		synchronized (lock) {
			this.checkSize();
			// Shift everything up one
			for (int i=array_end_pointer-1 ; i>=idx ; i--) {
				objects[i+1] = objects[i];
			}
			objects[idx] = o;
			array_end_pointer++;
		}
	}

	public Object get(int i) {
		synchronized (lock) {
			return objects[i];
		}
	}

	public int size() {
		synchronized (lock) {
			return array_end_pointer;
		}
	}

	public void remove(int idx) {
		synchronized (lock) {
			if (idx < array_end_pointer) {
				// Shift everything down one
				for (int i=idx ; i<array_end_pointer ; i++) {
					objects[i] = objects[i+1];
				}
				array_end_pointer--;
			}
		}
	}

	public int indexOf(Object o) {
		synchronized (lock) {
			for (int i=0 ; i<array_end_pointer ; i++) {
				if (objects[i] == o) {
					return i;
				}
			}
			return -1;
		}
	}

	public void remove(Object o) {
		int idx = this.indexOf(o);
		if (idx >= 0) {
			this.remove(idx);
		}
	}

	private void checkSize() {
		while (array_end_pointer >= objects.length-1) { // Note that we need to always have a null at the end for when removing items (i.e. shifting them all down one.)
			this.resize();
		}
	}

	private void resize() {
		// Rebuild array
		Object new_arr[] = new Object[objects.length * 2];
		System.arraycopy(objects, 0, new_arr, 0, objects.length);
		objects = new_arr;
	}

}
