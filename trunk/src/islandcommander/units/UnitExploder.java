package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.graphics.Bullet;
import java.awt.Graphics;

/**
 * This unit explodes when destroyed
 *
 */
public class UnitExploder extends GameUnit {

	//private static final int DAMAGE = 2;
	//private static final int DAMAGE_RANGE_SQ = (GameUnit.SHOT_RANGE / MapData.SQUARE_SIZE) + 1;
	private static final int BULLETS = 40;

	private static final long serialVersionUID = 1L;

	public UnitExploder(Main main, int mx, int my, int side) {
		super(main, "Exploder", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.EXPLODER_VIEW_RANGE, RELOAD_TIME, UnitStats.EXPLODER_SPEED, UnitStats.EXPLODER_HEALTH, UnitStats.EXPLODER_BULLET_DAMAGE, side, UnitStats.EXPLODER_THREAT, true);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("exploder" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}

	public void process() {
		checkForCollisions();
	}

	public void remove() {
		super.remove(); // Must be first so we don't keep explosing ourselves!
		this.explode();
	}

	private void explode() {
		/*ThreadSafeArrayList sprites = main.sprites;
		Sprite sprite;
		for (int i=0 ; i<sprites.size() ; i++) {
			sprite = (Sprite)sprites.get(i);
			if (sprite != null) {
				if (sprite.alive) {
					if (sprite instanceof GameUnit) {
						GameUnit gu = (GameUnit)sprite;
						if (gu.side == this.side) {
							continue;
						}
					}
					double dist = Functions.distance(sprite.getMapX(), sprite.getMapY(), this.getMapX(), this.getMapY());
					if (dist <= DAMAGE_RANGE_SQ) {
						if (sprite.damage(DAMAGE)) {
							i--;
						}
					}
				}
			}
		}*/

		
		for (int i=0 ; i<360 ; i += (360 /BULLETS)) {
			double rads = Math.toRadians(i);
			float x = (float)(Math.cos(rads));
			float y = (float)(Math.sin(rads));
			//points[i] = new Point(centre.x+x, centre.y+y);
			new Bullet(main, this, this.x, this.y, x, y, UnitStats.EXPLODER_BULLET_DAMAGE);
		}


		//this.remove();
	}


}
