package islandcommander;

import islandcommander.icons.*;
import islandcommander.units.UnitStats;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public final class IconPanel extends Rectangle {

	public static final int WIDTH = 110;
	public static final int ICON_SPACING = 20;

	private static final long serialVersionUID = 1L;

	private static Font font = new Font("Arial", Font.PLAIN, 12);

	private Main main;
	private ArrayList<AbstractIcon> icons;
	public AbstractIcon last_selected_icon;
	private AbstractIcon shortcuts[] = new AbstractIcon[4];

	public IconPanel(Main m, int x, int y) {
		super(x, y, WIDTH, 0);
		main = m;
		icons = new ArrayList<AbstractIcon>();
	}

	public void clearIcons() {
		icons.clear();
	}

	public void reset() {
		this.icons.clear();
		// The "main menu"
		//this.addIcon(new IconBuildMenu(main));
		addIcon(new IconBuildItem(main, UnitStats.MINE));
		if (main.game_data.pdata[0].getRace() == Main.RACE_EARTHLINGS) {
			addIcon(new IconBuildItem(main, UnitStats.MISSILE_SILO));
			addIcon(new IconBuildItem(main, UnitStats.BOMBER));
			addIcon(new IconBuildItem(main, UnitStats.TANK));
		} else if (main.game_data.pdata[0].getRace() == Main.RACE_TECHNOVORBS) {
			addIcon(new IconBuildItem(main, UnitStats.NUKE));
			addIcon(new IconBuildItem(main, UnitStats.JUMPER));
			addIcon(new IconBuildItem(main, UnitStats.TRAP));
		} else if (main.game_data.pdata[0].getRace() == Main.RACE_ORGANACTICS) {
			addIcon(new IconBuildItem(main, UnitStats.TANGLEWEED));
			addIcon(new IconBuildItem(main, UnitStats.EXPLODER));
			addIcon(new IconBuildItem(main, UnitStats.SPORESHOOTER));
		} else {
			throw new RuntimeException("todo");
		}
		
		// Create shortcuts 
		shortcuts[0] = this.icons.get(this.icons.size() - 4);
		shortcuts[1] = this.icons.get(this.icons.size() - 3);
		shortcuts[2] = this.icons.get(this.icons.size() - 2);
		shortcuts[3] = this.icons.get(this.icons.size() - 1);
		
		this.addIcon(new IconSpacer(main));

		addIcon(new IconGetStats(main, UnitStats.MINE));
		if (main.game_data.pdata[0].getRace() == Main.RACE_EARTHLINGS) {
			addIcon(new IconGetStats(main, UnitStats.MISSILE_SILO));
			addIcon(new IconGetStats(main, UnitStats.BOMBER));
			addIcon(new IconGetStats(main, UnitStats.TANK));
		} else if (main.game_data.pdata[0].getRace() == Main.RACE_TECHNOVORBS) {
			addIcon(new IconGetStats(main, UnitStats.NUKE));
			addIcon(new IconGetStats(main, UnitStats.JUMPER));
			addIcon(new IconGetStats(main, UnitStats.TRAP));
		} else if (main.game_data.pdata[0].getRace() == Main.RACE_ORGANACTICS) {
			addIcon(new IconGetStats(main, UnitStats.TANGLEWEED));
			addIcon(new IconGetStats(main, UnitStats.EXPLODER));
			addIcon(new IconGetStats(main, UnitStats.SPORESHOOTER));
		} else {
			throw new RuntimeException("todo");
		}
		this.addIcon(new IconSpacer(main));

		this.addIcon(new IconPause(main));

	}
	
	public void shortcutSelected(int no) {
		iconSelected(shortcuts[no-1]);
	}

	public IconBuildItem getBuildMenuItem(int type) {
		for(int i=0 ; i<icons.size() ; i++) {
			if (icons.get(i) instanceof IconBuildItem) {
				IconBuildItem ib = (IconBuildItem)icons.get(i);
				if (ib.type == type) {
					return ib;
				}
			}
		}
		return null;
	}

	public void addIcon(AbstractIcon ic) {
		this.icons.add(ic);
	}

	public void removeIcon(AbstractIcon ic) {
		this.icons.remove(ic);
	}

	public void addIcons(ArrayList<AbstractIcon> al) {
		this.icons.addAll(al);
	}

	public void paint(Graphics g) {
		if (this.icons.size() > 0) {
			height = this.icons.size() * ICON_SPACING + 8;
			g.setFont(font);

			for(int i=0 ; i<icons.size() ; i++) {
				AbstractIcon icon = (AbstractIcon)icons.get(i);
				icon.paint(g, x, y+ (i * ICON_SPACING));
			}
		}
	}

	public void mouseDown(int y) {
		y = y / ICON_SPACING;
		if (y < icons.size()) {
			AbstractIcon ic = (AbstractIcon)icons.get(y);
			this.iconSelected(ic);
		}
	}
	
	private void iconSelected(AbstractIcon ic) {
		if (ic.execute()) {
			last_selected_icon = ic; 
			if (last_selected_icon != null) {
				if (!last_selected_icon.toggle) {
					this.last_selected_icon = null;
				}
			}
		}
		
	}

}
