package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import java.awt.Graphics;

/**
 * This unit just shoots
 * @author stevec
 *
 */
public class UnitSporeShooter extends GameUnit {

	private static final long serialVersionUID = 1L;
	
	public UnitSporeShooter(Main main, int mx, int my, int side) {
		super(main, "SporeShooter", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.SPORESHOOTER_VIEW_RANGE, RELOAD_TIME, UnitStats.SPORESHOOTER_SPEED, UnitStats.SPORESHOOTER_HEALTH, UnitStats.SPORESHOOTER_BULLET_DAMAGE, side, UnitStats.SPORESHOOTER_THREAT, false);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("sporeshooter" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
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
