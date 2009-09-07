package islandcommander;

import islandcommander.units.GameUnit;
import islandcommander.units.Sprite;
import islandcommander.units.UnitJumper;
import islandcommander.units.UnitMiningPlatform;
import islandcommander.units.UnitMissileSilo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import ssmith.lang.Functions;
import ssmith.util.Interval;
import ssmith.util.ThreadSafeArrayList;

public final class MapData  {

	public static final int SQUARE_SIZE = 30;
	//public static final int MAP_SIZE = 20;
	//public static final int OWNER_RANGE = 3;

	public MapSquare map[][];
	private Main main;
	private int width, height;
	private Interval recalc_visible_squares_interval = new Interval(3000, true);
	public Point current_centre = new Point();

	public MapData(Main m, int w, int h) {
		main = m;
		//size = MAP_SIZE;
		width = w;
		height = h;
	}


	public void createMap() {
		MapGen mapgen = new MapGen(main, width, height);
		map = mapgen.getMapData();

	}


	public Point getPixelCoords(int x, int y) {
		return new Point(x*SQUARE_SIZE + (SQUARE_SIZE/2), y*SQUARE_SIZE + (SQUARE_SIZE/2));
	}


	public int getMapHeight() {
		return height;
	}


	public int getMapWidth() {
		return width;
	}


	public boolean isMapSquareTraversable(int x, int y) {
		try {
			return !map[x][y].blocked;
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}


	public float getMapSquareDifficulty(int x, int y) {
		return 1;
	}


	public void paint(Graphics g, int centre_x, int centre_y, boolean show_owners, boolean show_threat, boolean show_targets) {
		current_centre.x = centre_x;
		current_centre.y = centre_y;

		boolean recalc = recalc_visible_squares_interval.hitInterval();
		ArrayList<Sprite> player_units = null;
		if (recalc) {
			player_units = main.getPlayersUnits();
		}

		for(int y = 0 ; y<height ; y++) {
			for(int x = 0 ; x<width ; x++) {
				try {
					if (map[x][y].seen) {
						int x_pos = (x * MapData.SQUARE_SIZE) - centre_x + (Main.APPLET_SIZE/2);
						int y_pos = (y * MapData.SQUARE_SIZE) - centre_y + (Main.APPLET_SIZE/2);


						g.drawImage(main.getImage("desert.png"), x_pos, y_pos, main);
						if (map[x][y].minerals > 10) {
							int v = (int)((map[x][y].minerals / (float)MapGen.MAX_MINERALS) * 3);//(int)((float)199 / MapGen.MAX_MINERALS) * 3;
							//Main.p("Minerals=" + v + "(" + map[x][y].minerals +")");
							if (v < 0 || v > 3) {
								Main.p("Minerals too big");
							}
							g.drawImage(main.getImage("minerals" + v + ".png"), x_pos, y_pos, main);
						}

						if (show_owners) {
							if (map[x][y].owner >= 0) {
								if (map[x][y].owner == 0) {
									g.setColor(Color.green);
								} else if (map[x][y].owner == 1) {
									g.setColor(Color.red);
								}
								try {
									if (map[x-1][y].owner != map[x][y].owner) { // Left
										g.drawLine(x_pos, y_pos, x_pos, y_pos + MapData.SQUARE_SIZE-1);
									}
								} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									// Do nothing
								}
								try {
									if (map[x+1][y].owner != map[x][y].owner) {// Right
										g.drawLine(x_pos+MapData.SQUARE_SIZE-1, y_pos, x_pos+MapData.SQUARE_SIZE-1, y_pos + MapData.SQUARE_SIZE-1);
									}
								} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									// Do nothing
								}
								try {
									if (map[x][y-1].owner != map[x][y].owner) { // Top
										g.drawLine(x_pos, y_pos, x_pos+MapData.SQUARE_SIZE-1, y_pos);
									}
								} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									// Do nothing
								}
								try {
									if (map[x][y+1].owner != map[x][y].owner) { // Bottom
										g.drawLine(x_pos, y_pos+MapData.SQUARE_SIZE-1, x_pos+MapData.SQUARE_SIZE-1, y_pos + MapData.SQUARE_SIZE-1);
									}
								} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									// Do nothing
								}
							}
						}
						if (show_threat) {
							int[][] threat = main.getPlayerData(1).getAI().getThreatIntel();
							if (threat != null) {
								int v = threat[x][y] * 10;
								try {
									g.setColor(new Color(v, 0, 0));
								} catch (java.lang.IllegalArgumentException ex) {
									if (v < 0) {
										g.setColor(Color.black);
									} else {
										g.setColor(Color.red);
									}
								}
								g.fillRect(x_pos, y_pos, MapData.SQUARE_SIZE-1, MapData.SQUARE_SIZE-1);
							}
						} else if (show_targets) {
							int[][] targets = main.getPlayerData(1).getAI().getTargetIntel();
							if (targets != null) {
								int v = targets[x][y] * 5;
								try {
									g.setColor(new Color(0, 0, v));
								} catch (java.lang.IllegalArgumentException ex) {
									if (v < 0) {
										g.setColor(Color.black);
									} else {
										g.setColor(Color.blue);
									}
								}
								g.fillRect(x_pos, y_pos, MapData.SQUARE_SIZE-1, MapData.SQUARE_SIZE-1);
							}
						}
					} else {
						// Check for newly visible squares
						if (player_units != null) {
							for (int i=0 ; i<player_units.size() ; i++) {
								GameUnit punit = (GameUnit)player_units.get(i);
								if (punit instanceof UnitJumper == false) { // Jumpers cannot extend view range
									if (punit.getDistanceTo(x, y) <= Main.SEE_MAP_DIST) {
										map[x][y].seen = true;
										break;
									}
								}
							}

						}
					}
				} catch (java.lang.ArrayIndexOutOfBoundsException e) {
					// Do nothing
				}
			}
		}

	}


	public void recalcOwners() {
		long start_time = System.currentTimeMillis();
		for(int y = 0 ; y<height ; y++) {
			for(int x = 0 ; x<width ; x++) {
				map[x][y].owner = -1;
			}
		}

		main.game_data.pdata[0].total_squares = 0;
		main.game_data.pdata[1].total_squares = 0;

		main.game_data.pdata[0].total_mines = 0;
		main.game_data.pdata[1].total_mines = 0;

		// Loop through sprites
		ThreadSafeArrayList sprites = main.sprites;
		Sprite sprite;
		for (int i=0 ; i<sprites.size() ; i++) {
			sprite = (Sprite)sprites.get(i);
			if (sprite != null) {
				if (sprite instanceof UnitMiningPlatform || sprite instanceof UnitMissileSilo) {
					GameUnit gu = (GameUnit)sprite;
					if (sprite instanceof UnitMiningPlatform) {
						main.game_data.pdata[gu.side].total_mines++;
					}
					int mapx = sprite.getIntX() / MapData.SQUARE_SIZE;
					int mapy = sprite.getIntY() / MapData.SQUARE_SIZE;

					int OWNER_RANGE = main.game_data.pdata[gu.side].getOwnerRange();
					for (int my = mapy-OWNER_RANGE ; my <=mapy+OWNER_RANGE ; my++) {
						for (int mx = mapx-OWNER_RANGE ; mx <=mapx+OWNER_RANGE ; mx++) {
							double dist = Functions.distance(mapx, mapy, mx, my);
							if (dist <= OWNER_RANGE) {
								try {
									if (map[mx][my].owner == -1) {
										map[mx][my].owner = gu.side;
										main.game_data.pdata[gu.side].total_squares++;
									}
								} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
									// Do nothing
								}
							}
						}
					}
				}
			}
		}

		long end = System.currentTimeMillis() - start_time;
		if (end  > 1000) {
			Main.p("Took " + end + " to recalc owners");
		}
	}

}
