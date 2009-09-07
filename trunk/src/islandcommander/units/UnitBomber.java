package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.graphics.Bullet;

import java.awt.Graphics;

public final class UnitBomber extends GameUnit {

	private static final long serialVersionUID = 1L;

	public UnitBomber(Main main, int mx, int my, int side) {
		super(main, "Bomber", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.BOMBER_VIEW_RANGE, RELOAD_TIME, UnitStats.BOMBER_SPEED, UnitStats.BOMBER_HEALTH, UnitStats.BOMBER_BULLET_DAMAGE, side, UnitStats.BOMBER_THREAT, false);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("bomber" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof Bullet) {
			return true; 
		} else if (s instanceof UnitBomber) {
			return true;
		} else {
			return false; // Bombers can move through other units
		}
	}

	public void process() {
		super.process();

		if (target != null) {
			if (this.did_shoot == false) {
				this.moveTowards(target);
			}
		}

	}


}
