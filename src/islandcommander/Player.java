package islandcommander;

import islandcommander.units.UnitMiningPlatform;
import islandcommander.units.UnitStats;
import ssmith.util.Interval;

public final class Player {

	private static final int CASH_INC = 7;

	private MasterAI ai;
	private Interval mine_interval = new Interval(UnitMiningPlatform.INC_INTERVAL, false);
	private Main main;
	private int side;
	public int race;
	private float cash = UnitStats.MINE_COST * 8; // Give the AI enough creds to do something straight away.
	public int total_squares;
	public int total_mines, owner_range;

	public Player(Main m, int _side, int _race, MasterAI _ai) {
		main = m;
		side = _side;
		race = _race;

		ai = _ai;
		
		//if (side == 0) {
			if (race == Main.RACE_EARTHLINGS) {
				owner_range = 3;
				//main.addLogEntry("You are the Earthlings!");
			} else if (race == Main.RACE_TECHNOVORBS) {
				owner_range = 3;
				//main.addLogEntry("You are the Technovorbs!");
			} else if (race == Main.RACE_ORGANACTICS) {
				owner_range = 5;
				//main.addLogEntry("You are the Organactics!");
			} else {
				throw new RuntimeException("Todo");
			}
		//}
	}

	public void process() {
		if (ai != null) {
			ai.process();
		}
		if (mine_interval.hitInterval()) {
			main.game_data.pdata[side].addCash(CASH_INC);
		}
	}

	public MasterAI getAI() {
		return this.ai;
	}

	public void addCash(float amt) {
		cash += amt;
	}

	public float getCash() {
		return cash;
	}

	public float getRace() {
		return race;
	}
	
	public int getOwnerRange() {
		return this.owner_range;
	}

}
