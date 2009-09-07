package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.MapSquare;
import java.awt.Graphics;
import ssmith.util.Interval;

public final class UnitMiningPlatform extends GameUnit {
	
	private static final float MINERAL_INC = 0.18f; // Don't reduce too much or else rushing works
	public static int INC_INTERVAL = 1500;

	private static final long serialVersionUID = 1L;
	
	private Interval mine_interval = new Interval(INC_INTERVAL, false);

	public UnitMiningPlatform(Main main, int mx, int my, int side) {
		super(main, "MiningPlatform", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, GameUnit.SHOT_RANGE, RELOAD_TIME, 0, UnitStats.MINE_HEALTH, 0, side, UnitStats.MINE_THREAT, true);
		//main.getMapData().mines_changed.add(new Point(mx, my));
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("mine" + side + ".png"), (int)x-centre_x, (int)y-centre_y, main);

		super.paint(g, centre_x, centre_y);
	}
	
	public void process() {
		super.process();

		if (mine_interval.hitInterval()) {
			MapSquare sq = main.getMapData().map[this.getMapX()][this.getMapY()];
			if (sq.minerals > 0) {
				main.game_data.pdata[this.side].addCash(MINERAL_INC);
				sq.minerals -= MINERAL_INC;
			}
		}
	}

}
