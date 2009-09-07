package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;

import java.awt.Graphics;

public final class UnitTank extends GameUnit {

	private static final long serialVersionUID = 1L;
	private UnitMiningPlatform mp;
	
	public UnitTank(Main main, int mx, int my, int side) {
		super(main, "Tank", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.TANK_VIEW_RANGE, RELOAD_TIME, UnitStats.TANK_SPEED, UnitStats.TANK_HEALTH, UnitStats.TANK_BULLET_DAMAGE, side, UnitStats.TANK_THREAT, false);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("tank" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}

	public void process() {
		super.process();

		if (this.target == null) {
			if (this.getCurrentMapSquare().owner != this.side) {
				// Bring us home
				if (mp == null) {
					mp = this.findClosestMiningPlatform(this.side);
				}
				if (mp != null) {
					this.moveTowards(mp);
				}
			} else {
				mp = null;
			}
		} else {
			if (this.did_shoot == false) { // We have a target but we're not close enough
				this.moveTowards(target);
			}
		}

	}


}
