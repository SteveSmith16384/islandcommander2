package islandcommander.icons;

import islandcommander.IconPanel;
import islandcommander.Main;

import java.awt.Color;
import java.awt.Graphics;

public abstract class AbstractIcon {
	
	private String text;
	protected Main main;
	public boolean toggle;
	private Color deact_col = Color.magenta.darker();
	
	public AbstractIcon(Main m, String s, boolean togg) {
		main = m;
		text = s;
		toggle = togg;
	}
	
	public abstract boolean execute();
	
	public abstract boolean isActive();
	
	public abstract void executeOnMap(int mapx, int mapy);
	
	public void paint(Graphics g, int x, int y) {
		if (isActive()) {
			g.setColor(Color.magenta);
		} else {
			g.setColor(deact_col);
		}
		g.drawRect(x, y, IconPanel.WIDTH, IconPanel.ICON_SPACING-2);
		if (this.toggle) {
			if (showSelected()) {
				g.fillRect(x, y, IconPanel.WIDTH, IconPanel.ICON_SPACING-2);
			}
		}
		if (isActive()) {
		g.setColor(Color.white);
		} else {
			g.setColor(Color.gray);
			
		}
		g.drawString(text, x+4, y+14);
	}
	
	protected abstract boolean showSelected();

}
