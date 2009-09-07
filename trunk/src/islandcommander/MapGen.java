package islandcommander;

import ssmith.lang.Functions;

public final class MapGen {
	
	public static final int MAX_MINERALS = 200;

	private int width, height;
	private MapSquare map[][];
	//private Main main;

	public MapGen(Main m, int w, int h) {
		//main = m;
		width = w;
		height = h;
		map = new MapSquare[width][height];

		// Fill in all
		for(int y = 0 ; y<height ; y++) {
			for(int x = 0 ; x<width ; x++) {
				map[x][y] = new MapSquare();
			}
		}

		// Put some minerals on all squares, in case AI gets stranded at the start.
		for(int y = 0 ; y<height ; y++) {
			for(int x = 0 ; x<width ; x++) {
				map[x][y].minerals = 1;
			}
		}

		// Create minerals
		this.addMineralsAtCoords(2, 2);
		this.addMineralsAtCoords(width - 3, height - 3);
		
		for(int i = 0 ; i<4 ; i++) {
			int mapx = Functions.rnd(2, width - 3);
			int mapy = Functions.rnd(2, height - 3);

			this.addMineralsAtCoords(mapx, mapy);
			this.addMineralsAtCoords(width - mapx - 1, height - mapy - 1);
		}

	}
	
	private void addMineralsAtCoords(int mapx, int mapy) {
		int max_range_x = width / 4;
		int max_range_y = height / 4;
		//Main.print(mapx + ", " + mapy);
		for(int y2=mapy-max_range_y ; y2<mapy+max_range_y ; y2++) {
			for(int x2=mapx-max_range_x ; x2<mapx+max_range_x ; x2++) {
				// todo - is this right?
				int dist_scalar = (int)(max_range_x / Functions.distance(mapx, mapy, x2, y2));
				int diff = (int)(dist_scalar * (MAX_MINERALS / max_range_x));
				try {
					map[x2][y2].minerals += diff;
					if (map[x2][y2].minerals > MAX_MINERALS) {
						map[x2][y2].minerals = MAX_MINERALS;
					}
				} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
					// Do nothing
				}
			}
		}
		// Make the centre the max
		map[mapx][mapy].minerals = MAX_MINERALS;

	}

	public MapSquare[][] getMapData() {
		return map;
	}

}
