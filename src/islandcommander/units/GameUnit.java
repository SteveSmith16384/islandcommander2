package islandcommander.units;

import java.awt.Color;
import java.awt.Graphics;
import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.graphics.Bullet;
import islandcommander.graphics.Explosion;
import ssmith.lang.Functions;
import ssmith.util.Interval;

public abstract class GameUnit extends Sprite {

	protected static final int RELOAD_TIME = 1500;// All need to be the same!
	//private static final int CHECK_TARGETS_INTERVAL = 1000;
	public static final int SHOT_RANGE = 4 * MapData.SQUARE_SIZE; // All need to be the same so a tank can't shoot at a missile silo that can't shoot back!

	protected float shoot_dir_x, shoot_dir_y;
	protected Interval shoot_interval, show_range_interval;//, check_target_interval;
	public int view_range, bullet_damage, side;
	protected GameUnit target;
	private String name;
	public int threat_value;
	protected boolean did_shoot = false, expands_area;
	protected float speed;

	public GameUnit(Main main, String nm, int x, int y, int view_rng, long reload, float spd, int health, int dam, int sd, int threat, boolean _expands_area) {
		super(main, x, y, MapData.SQUARE_SIZE, MapData.SQUARE_SIZE, true, health);

		view_range = view_rng;
		name = nm;
		this.bullet_damage = dam;
		shoot_interval = new Interval(reload, true);
		side = sd;
		threat_value = threat;
		speed = spd;
		expands_area = _expands_area;
		if (expands_area) {
			main.check_mapsquare_owners = true;
		}

		show_range_interval = new Interval(3000, false);
		//check_target_interval = new Interval(CHECK_TARGETS_INTERVAL, false);
	}


	public void remove() {
		//new Explosion(main, this); // Can't have this cos TangleWeed doesn't want to explode
		if (this.expands_area) {
			main.check_mapsquare_owners = true;
		}
		super.remove();
	}


	public void paint(Graphics g, int centre_x, int centre_y) {
		g.setFont(font_normal);
		if (this.side == 0) {
			g.setColor(Color.yellow);
		} else {
			g.setColor(Color.white);
		}
		g.drawString("" + this.health, ((int)x+1)-centre_x, ((int)y+10)-centre_y);

		if (show_range_interval != null) {
			if (this.bullet_damage > 0) {
				g.setColor(Color.yellow);
				g.drawOval((int)this.getCenterX()-centre_x-view_range, (int)this.getCenterY()-centre_y-view_range, view_range*2, view_range*2);
				g.setColor(Color.orange);
				g.drawOval((int)this.getCenterX()-centre_x-GameUnit.SHOT_RANGE, (int)this.getCenterY()-centre_y-GameUnit.SHOT_RANGE, GameUnit.SHOT_RANGE*2, GameUnit.SHOT_RANGE*2);
			}
		}
	}


	public void process() {
		if (show_range_interval != null) {
			if (show_range_interval.hitInterval()) {
				show_range_interval = null;
			}
		}

		// This takes too much time!
		/*if (check_target_interval.hitInterval()) {
			target = this.findClosestEnemy();
		}*/

		did_shoot = false;
		if (this.bullet_damage > 0) { // Can we shoot?
			// Check for close enemies
			if (this.target != null) {
				if (this.canSee(target, view_range)) {
					if (this.getDistanceTo(target) <= SHOT_RANGE) {
						if (this.shoot_interval.hitInterval()) {
							this.aimAtTarget(target);
							this.shoot();
						}
						did_shoot = true;
					}
				} else {
					// We can't see our target anymore.
					target = null;
				}
			} else {
				// Look for a target
				if (this.shoot_interval.hitInterval()) {
					target = this.findClosestEnemy();
				}
			}
		}
		//super.process();
	}

	protected void moveTowards(Sprite s) {
		float prev_x = this.x;
		float prev_y = this.y;

		int off_x = Functions.sign(s.getIntX() - this.getIntX());
		int off_y = Functions.sign(s.getIntY() - this.getIntY());

		this.x += off_x * speed;

		if (this.checkForCollisions()) {
			this.x = prev_x;
		}

		this.y += off_y * speed;
		if (this.checkForCollisions()) {
			this.y = prev_y;
		}
	}

	protected GameUnit findClosestEnemy() {
		if (main.sprites != null) {
			Sprite sprite, closest = null;
			float max_dist = this.view_range;
			for (int i=0 ; i<main.sprites.size() ; i++) {
				sprite = (Sprite)main.sprites.get(i);
				if (sprite instanceof GameUnit) {
					GameUnit gu = (GameUnit)sprite;
					// Check they can be seen
					if (gu.side == this.side) {
						continue;
					}
					if (gu.isValidTarget() == false) {
						continue;
					}
					if (main.getMapData().map[sprite.getMapX()][sprite.getMapY()].seen) {
						float d = this.getDistanceTo(sprite); 
						if (d <= GameUnit.SHOT_RANGE) {
							// We can shoot at it, so choose this one to save time!
							return (GameUnit)sprite;
						} else if (d <= max_dist) {
							max_dist = d;
							closest = sprite;
						}
					}
				}
			}
			return (GameUnit)closest;
		}
		return null;
	}


	public boolean isValidTarget() {
		return (this instanceof UnitTrap == false);// && this instanceof UnitExploder == false);
	}

	protected UnitMiningPlatform findClosestMiningPlatform(int side) {
		if (main.sprites != null) {
			Sprite sprite, closest = null;
			float max_dist = 9999;
			for (int i=0 ; i<main.sprites.size() ; i++) {
				sprite = (Sprite)main.sprites.get(i);
				if (sprite instanceof UnitMiningPlatform) {
					UnitMiningPlatform mp = (UnitMiningPlatform)sprite;
					if (mp.side == this.side) {
						float d = this.getDistanceTo(sprite); 
						if (d < max_dist) {
							max_dist = d;
							closest = sprite;
						}
					}
				}
			}
			return (UnitMiningPlatform)closest;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void aimAtTarget(Sprite s) {
		int tx = s.getMiddleX() - this.getMiddleX();
		int ty = s.getMiddleY() - this.getMiddleY();

		double norm = tx*tx + ty*ty;
		double sqrt = 1 / Math.sqrt(norm);
		shoot_dir_x = (float)(tx * sqrt);
		shoot_dir_y = (float)(ty * sqrt);
	}

	public int getRange() {
		return this.view_range;
	}

	protected void shoot() {
		new Bullet(main, this, this.getMiddleX(), this.getMiddleY(), shoot_dir_x, shoot_dir_y, this.bullet_damage);
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof UnitTrap) { // They can move over these.
			return false;
		}
		return true; // Movement blocked by default.
	}

	public void setTarget(GameUnit gu) {
		this.target = gu;
	}

}
