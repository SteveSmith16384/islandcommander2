package islandcommander;

import ssmith.lang.Functions;

/**
 * This stores everything that would be saved if the game had a SaveGame facility.  Which it doesn't.
 *
 */
public final class GameData {
	
	//private Main main;
	public Player pdata[];
	public MapData map_data;
	public int time = 0;

	public GameData(Main m, int players_race, int w, int h) {
		//main = m;
		map_data = new MapData(m, w, h);
		this.map_data.createMap();
		
		pdata = new Player[2];
		pdata[0] = new Player(m, 0, players_race, null);
		pdata[1] = new Player(m, 1, Functions.rnd(0, 2), new MasterAI(m, 1, w, h));
	}
	
	public void process() {
		pdata[0].process();
		pdata[1].process();
	}
	
	public int getMapWidth() {
		return map_data.getMapWidth();
	}
	
	public int getMapHeight() {
		return map_data.getMapHeight();
	}
	
	public int getMaxTangleWeeds() {
		return (this.getMapWidth() * this.getMapHeight()) / 50;
	}
	
}
