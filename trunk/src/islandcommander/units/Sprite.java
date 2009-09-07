package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.MapSquare;
import islandcommander.MyRectangle;
import islandcommander.graphics.Explosion;

import java.awt.Font;
import java.awt.Graphics;
import ssmith.lang.Functions;

public abstract class Sprite extends MyRectangle {

	protected static Font font_normal = new Font("Impact", Font.PLAIN, 11);

	protected Main main;
	public int health;
	public boolean collideable;
	public boolean alive = true;

	public Sprite(Main m, float sx, float sy, int w, int h, boolean coll, int hlth) {
		super(sx, sy, w, h);
		main = m;
		collideable = coll;
		main.sprites.add(this);
		health = hlth;
	}

	public abstract void paint(Graphics g, int centre_x, int centre_y);

	public MapSquare getCurrentMapSquare() {
		return main.getMapData().map[getMapX()][getMapY()];
	}

	public int getMapX() {
		return (int)this.x / MapData.SQUARE_SIZE;
	}

	public int getMapY() {
		return (int)this.y / MapData.SQUARE_SIZE;
	}

	/**
	 * Returns true if movement blocked
	 * @return
	 */
	protected boolean checkForCollisions() {
		Sprite enemy;
		for (int e=0 ; e<main.sprites.size() ; e++) {
			enemy = (Sprite)main.sprites.get(e);
			if (enemy != this) { 
				if (enemy.collideable) {
					if (this.intersects(enemy)) {
						if (this.collidedWith(enemy)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}


	public abstract void process();

	public int getDistanceTo(Sprite s) {
		return (int)Functions.distance(this.getMiddleX(), this.getMiddleY(), s.getMiddleX(),s.getMiddleY());
	}

	public int getDistanceTo(int map_x, int map_y) {
		return (int)Functions.distance(this.getMiddleX(), this.getMiddleY(), (map_x*MapData.SQUARE_SIZE) + (MapData.SQUARE_SIZE/2), (map_y*MapData.SQUARE_SIZE) + (MapData.SQUARE_SIZE/2));
	}

	/**
	 * Returns whether the unit was destroyed.
	 * @param i
	 * @return
	 */
	public boolean damage(int i) {
		health -= i;
		if (health <= 0) {
			if (this instanceof GameUnit) {
				new Explosion(main, this);
			}
			this.remove();
			return true;
		}
		return false;
	}

	public void remove() {
		main.sprites.remove(this);
		this.alive = false;
	}

	/**
	 * Return true if collision occured and further checking should stop.
	 * @param s
	 * @return
	 */
	public abstract boolean collidedWith(Sprite s);

	public boolean canSee(Sprite target, float max_dist) {
		if (target.alive == false) {
			return false;
		}
		int sx = (int)this.getMiddleX();
		int sy = (int)this.getMiddleY();
		int tx = (int)target.getMiddleX();
		int ty = (int)target.getMiddleY();

		float dist = (float)Functions.distance(sx, sy, tx, ty) / (MapData.SQUARE_SIZE/2);
		if (dist > max_dist) {
			return false;
		}
		return true;

		/*
  		float x = sx;
		float y = sy;
		int iterations = (int)dist;
		float x_off = (tx - x) / iterations;
		float z_off = (ty - y) / iterations;
		Point last_sq = new Point(-1, -1);
		Point this_sq = new Point();
		iterations = iterations - 2; // So we don't go too far
		for (int s = 0; s < iterations; s++) {
			x += x_off;
			y += z_off;
			this_sq.x = (int)(x / MapData.SQUARE_SIZE);
			this_sq.y = (int)(y / MapData.SQUARE_SIZE);
			if (last_sq.x != this_sq.x || last_sq.y != this_sq.y) {
				if (main.getMapData().map[this_sq.x][this_sq.y].blocked) {
					return false;
				}
			}
			last_sq.x = this_sq.x;
			last_sq.y = this_sq.y;
		}
		return true;*/

	}


}
