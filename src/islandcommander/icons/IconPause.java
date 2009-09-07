package islandcommander.icons;

import islandcommander.Main;

public class IconPause extends AbstractIcon {
	
	public IconPause(Main m) {
		super(m, "Pause", true);
	}

	public boolean execute() {
		if (this == main.getLastSelectedIcon()) {
			main.clearLastSelectedIcon();
		}
		main.togglePause();
		//this.toggle = !this.toggle;
		return true;
	}

	public void executeOnMap(int mapx, int mapy) {
		//throw new RuntimeException("This should not be called");
	}

	@Override
	protected boolean showSelected() {
		return main.isPaused();
	}

	public boolean isActive() {
		return true;
	}

}
