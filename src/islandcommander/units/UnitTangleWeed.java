package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import java.awt.Graphics;
import java.util.ArrayList;

import ssmith.lang.Functions;
import ssmith.util.Interval;

public class UnitTangleWeed extends GameUnit {

	private static final long serialVersionUID = 1L;

	private Interval grow_interval = new Interval(1000, false);
	private ArrayList<UnitTangleWeed> weeds;

	public UnitTangleWeed(Main main, int mx, int my, int side, ArrayList<UnitTangleWeed> group) {
		super(main, "TangleWeed", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.TANGLEWEED_VIEW_RANGE, RELOAD_TIME, UnitStats.TANGLEWEED_SPEED, UnitStats.TANGLEWEED_HEALTH, UnitStats.TANGLEWEED_BULLET_DAMAGE, side, UnitStats.TANGLEWEED_THREAT, false);
		weeds = group;
		weeds.add(this);

		if (weeds.size() > main.game_data.getMaxTangleWeeds()) {
			UnitTangleWeed tw = weeds.get(0);
			tw.remove();
			weeds.remove(0);
		}
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("tangleweed" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}
	
	
	public void remove() {
		weeds.remove(this);
		super.remove();
	}
	

	public void process() {
		super.process();

		if (this.grow_interval.hitInterval()) {
			if (Functions.rnd(0, 4) == 0) {
				int x = Functions.rnd(-1, 1);
				int y = Functions.rnd(-1, 1);
				int px = (int)this.x + (x * MapData.SQUARE_SIZE);
				int py = (int)this.y + (y * MapData.SQUARE_SIZE);

				if (x >= 0 && y >= 0 && x< main.game_data.getMapWidth() && y < main.game_data.getMapHeight()) {
					if (main.isAreaClearForNewUnit(px, py, UnitStats.TANGLEWEED)) {
						new UnitTangleWeed(main, px / MapData.SQUARE_SIZE, py / MapData.SQUARE_SIZE, this.side, this.weeds);
					}
				}
			}
		}
	}

}
