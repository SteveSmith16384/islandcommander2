package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import java.awt.Graphics;

public final class UnitMissileSilo extends GameUnit {

	private static final long serialVersionUID = 1L;

	public UnitMissileSilo(Main main, int mx, int my, int side) {
		super(main, "MissileSilo", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.SILO_VIEW_RANGE, RELOAD_TIME, 0, UnitStats.SILO_HEALTH, UnitStats.SILO_BULLET_DAMAGE, side, UnitStats.SILO_THREAT, true);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("silo" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
		
	}

	public void process() {
		super.process();
		
	}

}
