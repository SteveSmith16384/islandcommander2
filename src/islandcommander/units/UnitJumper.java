package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.graphics.Bullet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import ssmith.lang.Functions;
import ssmith.util.Interval;

public class UnitJumper extends GameUnit {

	private static final int JUMP_ANIM_SPEED = 5;

	private static final long serialVersionUID = 1L;

	private Interval jump_interval = new Interval(2000, false);
	private Point jump_dest;
	private Point2D.Float line_end, offset;

	public UnitJumper(Main main, int mx, int my, int side) {
		super(main, "Jumper", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.JUMPER_VIEW_RANGE, RELOAD_TIME, UnitStats.JUMPER_SPEED, UnitStats.JUMPER_HEALTH, UnitStats.JUMPER_BULLET_DAMAGE, side, UnitStats.JUMPER_THREAT, false);
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("jumper" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		if (jump_dest != null) {
			g.setColor(Color.white);
			g.drawLine((int)this.x-centre_x + (MapData.SQUARE_SIZE/2), (int)this.y-centre_y + (MapData.SQUARE_SIZE/2), (int)line_end.x-centre_x + (MapData.SQUARE_SIZE/2), (int)line_end.y-centre_y + (MapData.SQUARE_SIZE/2));
		}

		super.paint(g, centre_x, centre_y);
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof Bullet) {
			return true; 
		} else if (s instanceof UnitJumper) {
			return true; 
		} else {
			return false; // Jumpers can move through other units
		}
	}

	public void process() {
		super.process();

		if (jump_dest != null) {
			int dist1 = (int)Functions.distance(line_end.x, line_end.y, jump_dest.x, jump_dest.y);
			line_end.x += this.offset.x;
			line_end.y += this.offset.y;
			int dist2 = (int)Functions.distance(line_end.x, line_end.y, jump_dest.x, jump_dest.y);
			if (dist2 > dist1) {
				this.x = jump_dest.x;
				this.y = jump_dest.y;
				this.jump_dest = null;
				this.offset = null;
				this.jump_interval.restartTimer();
			}
		} else {
			if (did_shoot == false) {
				// Jump!
				if (jump_interval.hitInterval()) {
					int max_x = (main.game_data.getMapWidth()-1) * MapData.SQUARE_SIZE;
					int max_y = (main.game_data.getMapHeight()-1) * MapData.SQUARE_SIZE;
					int new_x = Functions.rnd(MapData.SQUARE_SIZE, max_x);
					int new_y = Functions.rnd(MapData.SQUARE_SIZE, max_y);
					int mx = new_x / MapData.SQUARE_SIZE;
					int my = new_y / MapData.SQUARE_SIZE;

					if (side == 0) {
						if (main.game_data.map_data.map[mx][my].seen == false) {
							return;
						}
					} else if (side == 0) {
						// Notice that since we don't keep track of what the AI can see, 
						// we just use the inverse of the player's map.
						if (main.game_data.map_data.map[main.game_data.getMapWidth() - mx][main.game_data.getMapHeight() - my].seen == false) {
							return;
						}
					}

					if (main.isAreaClearForNewUnit(new_x, new_y, UnitStats.JUMPER)) {
						//this.x = x;
						//this.y = y;
						jump_dest = new Point(new_x, new_y);
						line_end = new Point2D.Float(this.x, this.y);

						int tx = new_x - (int)x;
						int ty = new_y - (int)y;

						double norm = tx*tx + ty*ty;
						double sqrt = 1 / Math.sqrt(norm);
						float off_x = (float)(tx * sqrt * JUMP_ANIM_SPEED);
						float off_y = (float)(ty * sqrt * JUMP_ANIM_SPEED);

						offset = new Point2D.Float(off_x, off_y);
					}
				}
			}
		}
	}

}


