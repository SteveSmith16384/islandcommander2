package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import java.awt.Graphics;

public class UnitTrap extends GameUnit {

	private static final int DAMAGE = 10;

	private static final long serialVersionUID = 1L;

	public UnitTrap(Main main, int mx, int my, int side) {
		super(main, "Trap", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.TRAP_VIEW_RANGE, RELOAD_TIME, UnitStats.TRAP_SPEED, UnitStats.TRAP_HEALTH, UnitStats.TRAP_BULLET_DAMAGE, side, UnitStats.TRAP_THREAT, true);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("trap" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof GameUnit) {
			GameUnit gu = (GameUnit)s;
			if (this.side != gu.side) {
				gu.damage(DAMAGE);
				this.remove();
				return true;
			}
		}
		return false;
	}

	public void process() {
		//super.process();
		checkForCollisions();
	}


}
