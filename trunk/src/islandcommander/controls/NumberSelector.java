package islandcommander.controls;

import islandcommander.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class NumberSelector extends AbstractControl {
	
	private static final int ADJ_WIDTH = 25;
	private static final int NUM_WIDTH = 60;

	private static final long serialVersionUID = 1L;

	private String text;
	private int num, min;
	private Rectangle rect_more, rect_less;

	public NumberSelector(Main m, int x, int y, int w, int h, String label, int _num, int _min) {
		super(m, x, y, w, h);
		
		rect_less = new Rectangle(x + w - (ADJ_WIDTH*2) - NUM_WIDTH, y, ADJ_WIDTH, h);
		rect_more = new Rectangle(x + w - ADJ_WIDTH, y, ADJ_WIDTH, h);
		
		text = label;
		num = _num;
		min = _min;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.red);
		//g.drawRect(x, y, width, height);
		g.drawRect(rect_less.x, rect_less.y, rect_less.width, rect_less.height);
		g.drawRect(rect_more.x, rect_more.y, rect_more.width, rect_more.height);
		
		g.setColor(Color.white);
		g.drawString("-", rect_less.x+5, rect_less.y+35);
		g.drawString("+", rect_more.x+5, rect_more.y+35);

		g.drawString(text, x+15, y+35);
		g.drawString("" + num, x+width - ADJ_WIDTH - NUM_WIDTH+10, y+35);
	}

	@Override
	public void mouseDown(int x, int y) {
		if (rect_more.contains(x, y)) {
			num += 2;
		} else if (rect_less.contains(x, y)) {
			num -= 2;
			if (num < min) {
				num = min;
			}
		}
	}
	
	public int getNumber() {
		return this.num;
	}

}
