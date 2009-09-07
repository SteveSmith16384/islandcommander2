package islandcommander.controls;

import islandcommander.Main;

import java.awt.Color;
import java.awt.Graphics;

public class Button extends AbstractControl {
	
	private static final long serialVersionUID = 1L;

	private String text;
	private int id;

	public Button(Main m, int x, int y, int w, int h, String label, int _id) {
		super(m, x, y, w, h);
		
		text = label;
		id = _id;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);
		
		g.setColor(Color.white);
		g.drawString(text, x+15, y+35);
	}

	@Override
	public void mouseDown(int x, int y) {
		main.startNewGame(id);
	}

}
