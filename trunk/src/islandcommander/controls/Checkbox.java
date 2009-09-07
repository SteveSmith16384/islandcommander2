package islandcommander.controls;

import islandcommander.Main;

import java.awt.Graphics;

public class Checkbox extends AbstractControl {

	private static final long serialVersionUID = 1L;

	private String text;

	public Checkbox(Main m, int x, int y, int w, int h, String label) {
		super(m, x, y, w, h);
		
		text = label;
	}

	@Override
	public void paint(Graphics g) {
		g.drawString(text, x, y);
		
	}

	@Override
	public void mouseDown(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
