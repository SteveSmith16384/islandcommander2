package islandcommander.graphics;

import islandcommander.Main;
import islandcommander.units.GameUnit;
import islandcommander.units.Sprite;
import islandcommander.units.UnitTrap;
import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Sprite {

	protected static final int SIZE = 6;

	private static final long serialVersionUID = 1L;

	private float dir_x, dir_y;
	protected int speed = 8;
	private int damage;
	protected GameUnit shooter;

	public Bullet(Main main, GameUnit shot_by, float x, float y, float dx, float dy, int dam) {
		super(main, x, y, SIZE, SIZE, true, 0);
		this.shooter = shot_by;
		dir_x = dx;
		dir_y = dy;
		damage = dam;
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		int x2 = (int)x-centre_x;
		int y2 = (int)y-centre_y;
		
		g.setColor(Color.yellow);
		g.fillRect(x2, y2, 2, 2);

		// Trail
		g.drawLine(x2, y2, (int)(x2-(dir_x*SIZE)), (int)(y2-(dir_y * SIZE)));
		g.drawLine(x2+1, y2+1, (int)(x2-(dir_x*SIZE)), (int)(y2-(dir_y * SIZE)));
	}

	public void process() {
		this.x += (dir_x * speed);
		this.y += (dir_y * speed);

		this.checkForCollisions();

		if (main.getMapData().isMapSquareTraversable(this.getMapX(), this.getMapY()) == false) {
			this.remove();
		}
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof UnitTrap) {
			// Do nothing
		/*} else if (s instanceof UnitExploder) {
				// Do nothing*/
		} else if (s instanceof GameUnit) {
			GameUnit gu = (GameUnit)s;
			if (this.shooter.side != gu.side) {
				gu.damage(damage);
				gu.setTarget(shooter); // Make shoot er the target so we target most dangerous enemies first
				this.remove();
				return true;
			}
		}
		return false;
	}

}
