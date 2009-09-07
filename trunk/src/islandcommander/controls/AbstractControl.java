package islandcommander.controls;

import islandcommander.Main;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class AbstractControl extends Rectangle {
	
	protected Main main;
	
	public AbstractControl(Main m, int x, int y, int w, int h) {
		super(x, y, w, h);
		
		main = m;
	}
	
	public abstract void mouseDown(int x, int y);
	
	public abstract void paint(Graphics g);

}
