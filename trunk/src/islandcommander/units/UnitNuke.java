package islandcommander.units;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.graphics.Bullet;
import islandcommander.graphics.Explosion;

import java.awt.Graphics;
import java.awt.Point;

import ssmith.lang.Functions;
import ssmith.util.ThreadSafeArrayList;

public class UnitNuke extends GameUnit {

	private static final int DAMAGE = 20;
	private static final int DAMAGE_RANGE_SQ = 3;

	private static final long serialVersionUID = 1L;

	private Point ground_zero;

	public UnitNuke(Main main, int mx, int my, int side) {
		super(main, "Nuke", mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, UnitStats.NUKE_VIEW_RANGE, RELOAD_TIME, UnitStats.NUKE_SPEED, UnitStats.NUKE_HEALTH, UnitStats.NUKE_BULLET_DAMAGE, side, UnitStats.NUKE_THREAT, false);

		ground_zero = this.getGroundZero();
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		g.drawImage(main.getImage("nuke" + side + ".png"), (int)(x-centre_x), (int)(y-centre_y), main);

		super.paint(g, centre_x, centre_y);
	}

	public boolean collidedWith(Sprite s) {
		if (s instanceof Bullet) {
			return true; 
		} else if (s instanceof UnitNuke) {
			return true; 
		} else {
			return false; // Nuke can move through other units
		}
	}

	public void process() {
		super.process();

		if (ground_zero != null) {
			float dir_x = Functions.sign(this.ground_zero.x - this.getMapX()) * speed; 
			float dir_y = Functions.sign(this.ground_zero.y - this.getMapY()) * speed; 
			this.x += (dir_x * speed);
			this.y += (dir_y * speed);

			this.checkForCollisions();
			if (this.getMapX() == ground_zero.x && this.getMapY() == ground_zero.y) {
				explode();
			}
		}
	}

	private Point getGroundZero() {
		int[][] threats_intel = new int[main.game_data.getMapWidth()][main.game_data.getMapHeight()];

		ThreadSafeArrayList sprites = main.sprites;
		Sprite sprite;
		for (int i=0 ; i<sprites.size() ; i++) {
			sprite = (Sprite)sprites.get(i);
			if (sprite != null) {
				if (sprite instanceof GameUnit) {
					GameUnit gu = (GameUnit)sprite;
					if (gu.threat_value > 0) {
						if (gu.side != this.side) {
							int mapx = sprite.getMapX();
							int mapy = sprite.getMapY();

							// Update intel based on unit's view range
							int RANGE_SQ = 2;

							for(int y2=mapy-RANGE_SQ ; y2<=mapy+RANGE_SQ ; y2++) {
								for(int x2=mapx-RANGE_SQ ; x2<=mapx+RANGE_SQ ; x2++) {
									double dist = Functions.distance(mapx, mapy, x2, y2);
									if (dist <= RANGE_SQ) {
										try {
											threats_intel[x2][y2] += gu.threat_value;
										} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
											// Do nothing
										}
									}
								}
							}
						}
					}
				}
			}
		}

		Point top_threat_location = new Point();
		int highest_threat = -1;
		for(int y = 0 ; y<main.game_data.getMapHeight() ; y++) {
			for(int x = 0 ; x<main.game_data.getMapWidth() ; x++) {
				if (threats_intel[x][y] > highest_threat) {
					highest_threat = threats_intel[x][y];
					top_threat_location.x = x;
					top_threat_location.y = y;
				}
			}
		}
		return top_threat_location;
	}

	private void explode() {
		new Explosion(main, this);

		ThreadSafeArrayList sprites = main.sprites;
		Sprite sprite;
		for (int i=0 ; i<sprites.size() ; i++) {
			sprite = (Sprite)sprites.get(i);
			if (sprite != null) {
				if (sprite.alive) {
					double dist = Functions.distance(sprite.getMapX(), sprite.getMapY(), this.ground_zero.x, this.ground_zero.y);
					if (dist <= DAMAGE_RANGE_SQ) {
						if (sprite.damage(DAMAGE)) {
							i--;
						}
					}
				}
			}
		}

		this.remove();
	}

}

