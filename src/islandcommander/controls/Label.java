package islandcommander.controls;

import islandcommander.Main;

import java.awt.Color;
import java.awt.Graphics;

public class Label extends AbstractControl {

	private static final long serialVersionUID = 1L;

	private String text;

	public Label(Main m, int x, int y, String label) {
		super(m, x, y, 0, 0);
		
		text = label;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.drawString(text, x+15, y+25);
	}

	@Override
	public void mouseDown(int x, int y) {
		// Do nothing
	}

}
