package islandcommander.icons;

import java.awt.Graphics;

import islandcommander.Main;

public class IconSpacer extends AbstractIcon {
	
	public IconSpacer(Main m) {
		super(m, "", false);
	}

	@Override
	public boolean execute() {
		// Do nothing
		return true;
	}

	@Override
	public void executeOnMap(int mapx, int mapy) {
		// Do nothing
	}
	
	public void paint(Graphics g, int x, int y) {
		// Do nothing
	}

	@Override
	protected boolean showSelected() {
		return false;
	}

	public boolean isActive() {
		return true;
	}
}
