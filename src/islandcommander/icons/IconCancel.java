package islandcommander.icons;

import islandcommander.Main;

public final class IconCancel extends AbstractIcon {
	
	public IconCancel(Main m) {
		super(m, "Return", false);
	}

	@Override
	public boolean execute() {
		main.resetMenu();
		return true;
	}

	@Override
	public void executeOnMap(int mapx, int mapy) {
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
