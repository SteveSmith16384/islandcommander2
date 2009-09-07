package islandcommander;

import java.awt.Rectangle;

public class MyRectangle extends Rectangle.Float {
	
	private static final long serialVersionUID = 1L;
	
	public MyRectangle(float x, float y, int w, int h) {
		super(x, y, w, h);
	}
	
	public int getIntX() {
		return (int)(this.x);
	}

	public int getIntY() {
		return (int)(this.y);
	}

	public int getMiddleX() {
		return (int)(this.x + (this.width/2));
	}

	public int getMiddleY() {
		return (int)(this.y + (this.height/2));
	}

}
