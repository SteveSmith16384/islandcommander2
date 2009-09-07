package islandcommander;

import islandcommander.units.*;

import java.awt.Point;
import ssmith.lang.Functions;
import ssmith.util.Interval;
import ssmith.util.ThreadSafeArrayList;

public final class MasterAI {

	private static final int DEFENCE_BUY_MULTIPLES = 2;
	public static final int BUY_MINE_CHANCE = Functions.rnd(2, 10);

	private int threats_intel[][];
	private int targets_intel[][];
	private int allied_intel[][];

	private Point best_mine_location = new Point(-1, -1);
	private Point top_threat_location = new Point(-1, -1);
	private Point top_target_location = new Point(-1, -1);

	private Main main;
	private Interval ai_interval = new Interval(1000, false);
	private Interval update_intel_interval = new Interval(10000, false);
	private int side;
	public int num_attackers_to_build = 3;
	public int num_defenders_to_build = DEFENCE_BUY_MULTIPLES;
	private int attack_type_to_build = -1;// = GetRandomUnitType(true);
	private int defence_type_to_build = -1;// = GetRandomUnitType(false);
	public boolean build_mine = true;
	public int max_map_threat, max_map_target, prev_highest_resources;
	private int player_attack_units_threat = 0;
	private int map_width, map_height;

	public MasterAI(Main m, int s, int w, int h) {
		main = m;
		side = s;

		map_width = w;
		map_height = h;

		threats_intel = new int[map_width][map_height];
		targets_intel = new int[map_width][map_height];
		allied_intel = new int[map_width][map_height];

	}

	public void process() {
		if (ai_interval.hitInterval()) {

			if (update_intel_interval.hitInterval()) {
				this.updateIntel();
			}

			if (attack_type_to_build == -1) {
				SelectRandomUnitTypeAndQty(true);
			}
			if (defence_type_to_build == -1) {
				SelectRandomUnitTypeAndQty(false);
			}

			if (max_map_threat <= 1) {
				if (build_mine || max_map_target == 0) {
					if (best_mine_location.x >= 0) {
						if (buyUnit(UnitStats.MINE, best_mine_location, 1, true)) {
							this.build_mine = Functions.rnd(1, BUY_MINE_CHANCE) > 1;
						}
					} else {
						if (Main.DEBUG_AI) {
							main.addLogEntry("No potential mine location found.");
						}
					}
				} else if (max_map_target > 0) {
					if (buyUnit(attack_type_to_build, top_target_location, this.num_attackers_to_build, false)) {
						this.num_attackers_to_build--;
						if (this.num_attackers_to_build == 0) {
							this.build_mine = Functions.rnd(1, BUY_MINE_CHANCE) > 1;
							SelectRandomUnitTypeAndQty(true);
						} else {
							ai_interval.fireInterval(); // SO we buy again quickly!
						}
					}
				}
			} else { // Defend!
				if (buyUnit(defence_type_to_build, top_threat_location, num_defenders_to_build, false)) {
					num_defenders_to_build--;
					if (num_defenders_to_build == 0) {
						SelectRandomUnitTypeAndQty(false);
					} else {
						ai_interval.fireInterval(); // SO we buy again quickly!
					}
				}
			}
		}
	}


	private void SelectRandomUnitTypeAndQty(boolean for_attack) {
		if (for_attack) {
			this.num_attackers_to_build = 0;
		} else {
			this.num_defenders_to_build = 0;
		}

		if (main.game_data.pdata[1].getRace() == Main.RACE_EARTHLINGS) {
			if (for_attack) {
				int type = Functions.rnd(1, 2);
				switch (type) {
				case 1:
					this.attack_type_to_build = UnitStats.BOMBER;
					break;
				case 2:
					this.attack_type_to_build = UnitStats.TANK;
					break;
				default:
					throw new RuntimeException("todo");
				}
			} else { // For defence
				this.defence_type_to_build = UnitStats.MISSILE_SILO;
			}
		} else if (main.game_data.pdata[1].getRace() == Main.RACE_TECHNOVORBS) {
			if (for_attack) {
				int type = Functions.rnd(1, 2);
				switch (type) {
				case 1:
					//if (this.max_map_target > NUKE_THREAT) {
					this.attack_type_to_build = UnitStats.NUKE;
					this.num_attackers_to_build = 1;
					//} else {
					break;
				case 2:
					this.attack_type_to_build = UnitStats.JUMPER;
					break;
				/*case 3:
					this.attack_type_to_build = UnitStats.TRAP;
					break;*/
				default:
					throw new RuntimeException("todo");
				}
			} else { // For defence
				int type = Functions.rnd(1, 2);
				switch (type) {
				case 1:
					this.defence_type_to_build = UnitStats.TRAP;
					break;
				case 2:
					this.defence_type_to_build = UnitStats.JUMPER;
					break;
				default:
					throw new RuntimeException("todo");
				}
			}
		} else if (main.game_data.pdata[1].getRace() == Main.RACE_ORGANACTICS) {
			if (for_attack) {
				int type = Functions.rnd(1, 3);
				switch (type) {
				case 1:
					this.attack_type_to_build = UnitStats.TANGLEWEED;
					this.num_attackers_to_build = 1;
					break;
				case 2:
					this.attack_type_to_build = UnitStats.SPORESHOOTER;
					break;
				case 3:
					this.attack_type_to_build = UnitStats.EXPLODER;
					break;
				default:
					throw new RuntimeException("todo");
				}
			} else { // For defence
				int type = Functions.rnd(1, 2);
				switch (type) {
				case 1:
					this.defence_type_to_build = UnitStats.TANGLEWEED;
					this.num_defenders_to_build = 1;
					break;
				case 2:
					this.defence_type_to_build = UnitStats.SPORESHOOTER;
					break;
				default:
					throw new RuntimeException("todo");
				}
			}
		} else {
			throw new RuntimeException("todo");
		}

		// If we haven't chosen the qty yet, now choose them.
		if (for_attack) {
			if (this.num_attackers_to_build == 0) {
				this.num_attackers_to_build = Functions.rnd(2, 3);
			}
		} else {
			if (this.num_defenders_to_build == 0) {
				this.num_defenders_to_build = DEFENCE_BUY_MULTIPLES;
			}
		}
	}

	/**
	 * qty does not actually buy that qty, but checks that the AI has enough cash to buy that qty.
	 * 
	 * @param type
	 * @param location
	 * @param qty
	 * @param keep_reserve_cash
	 * @return
	 */
	private boolean buyUnit(int type, Point location, int qty, boolean keep_reserve_cash) {
		int cost = UnitStats.GetCost(type) * qty;
		if (keep_reserve_cash) {
			cost += this.player_attack_units_threat * 2;
		}
		if (main.game_data.pdata[side].getCash() >= cost) {
			if (main.isAreaClearForNewUnit(location.x * MapData.SQUARE_SIZE, location.y * MapData.SQUARE_SIZE, type)) {
				UnitStats.CreateUnit(main, type, location.x, location.y, side);
				main.game_data.pdata[side].addCash(-UnitStats.GetCost(type));
				if (Main.DEBUG_AI) {
					main.addLogEntry("AI bought " + UnitStats.GetName(type));
				}
				this.updateIntel();
				return true;
			} else {
				if (Main.DEBUG_AI) {
					main.addLogEntry("Area " + top_threat_location + " not clear");
				}
			}
		}
		return false;
	}


	private void updateIntel() {
		player_attack_units_threat = 0;

		// First, blank the intel rating
		for(int y = 0 ; y<map_height ; y++) {
			for(int x = 0 ; x<map_width ; x++) {
				threats_intel[x][y] = 0;
				targets_intel[x][y] = 0;
				allied_intel[x][y] = 0;
			}
		}

		// Now update the threat of each square
		ThreadSafeArrayList sprites = main.sprites;
		Sprite sprite;
		for (int i=0 ; i<sprites.size() ; i++) {
			sprite = (Sprite)sprites.get(i);
			if (sprite != null) {
				if (sprite instanceof GameUnit) {
					GameUnit gu = (GameUnit)sprite;
					if (gu.threat_value != 0) {
						if (gu.side != this.side) {
							if (gu instanceof UnitBomber || gu instanceof UnitTank) {
								player_attack_units_threat += gu.threat_value;
							}
						}

						int mapx = sprite.getMapX();
						int mapy = sprite.getMapY();

						// Update intel based on unit's view range
						int RANGE_SQ = (GameUnit.SHOT_RANGE / MapData.SQUARE_SIZE);// / 2; DONT HALVE IT SINCE THE AI WILL BULD MONES NEXT TO SILOS!  // Halve it since it's the dist between our enemies and squares owned by us

						for(int y2=mapy-RANGE_SQ ; y2<=mapy+RANGE_SQ ; y2++) {
							for(int x2=mapx-RANGE_SQ ; x2<=mapx+RANGE_SQ ; x2++) {
								double dist = Functions.distance(mapx, mapy, x2, y2);
								if (dist <= RANGE_SQ) {
									try {
										if (gu instanceof UnitMiningPlatform == false) {
											if (gu.side != this.side) {
												threats_intel[x2][y2] += gu.threat_value;
											} else {
												allied_intel[x2][y2] += gu.threat_value;
											}
										} else if (gu instanceof UnitMiningPlatform) {
											if (gu.side != this.side) {
												targets_intel[x2][y2] += gu.threat_value;
											}
										}
									} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
										// Do nothing
									}
								}
							}
						}

						// Update targets
						// Update intel based on unit's view range
						/*int TARGET_RANGE_SQ = (UnitStats.BOMBER_VIEW_RANGE / MapData.SQUARE_SIZE);

						for(int y2=mapy-TARGET_RANGE_SQ ; y2<=mapy+TARGET_RANGE_SQ ; y2++) {
							for(int x2=mapx-TARGET_RANGE_SQ ; x2<=mapx+TARGET_RANGE_SQ ; x2++) {
								double dist = Functions.distance(mapx, mapy, x2, y2);
								if (dist <= TARGET_RANGE_SQ) {
									try {
										if (gu instanceof UnitMiningPlatform) {
											if (gu.side != this.side) {
												targets_intel[x2][y2] += gu.threat_value;
											}
										}
									} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
										// Do nothing
									}
								}
							}
						}*/
					}

				}
			}
		}

		// Get the top threat square
		int highest_threat = -1;
		int highest_target = -1;
		int highest_resources = -1;
		for (int y = 0 ; y<map_height ; y++) {
			for (int x = 0 ; x<map_width ; x++) {
				if (main.getMapData().map[x][y].owner == side) { // This is where we are going to build something, so it must be our square
					if (threats_intel[x][y] - allied_intel[x][y] > highest_threat) {
						if (main.isAreaClearForNewUnit(x * MapData.SQUARE_SIZE, y * MapData.SQUARE_SIZE, -1)) {
							highest_threat = threats_intel[x][y] - allied_intel[x][y];
							top_threat_location.x = x;
							top_threat_location.y = y;
						}
					}
					if (targets_intel[x][y] > highest_target) {
						if (main.isAreaClearForNewUnit(x * MapData.SQUARE_SIZE, y * MapData.SQUARE_SIZE, -1)) {
							highest_target = targets_intel[x][y];
							top_target_location.x = x;
							top_target_location.y = y;
						}
					}
					if (threats_intel[x][y] == 0) { // Don't build near an enemy!
						int minerals = (int)main.getMapData().map[x][y].minerals;
						if (minerals >= highest_resources) { // Needs to be ">=" otherwise the AI spreads itself all over the map (was: Needs to be ">" so that if there's no resources in starting squares, it moves up-left)
							//if (main.getMapData().map[x][y].owner == side) {
							if (main.isAreaClearForNewUnit(x * MapData.SQUARE_SIZE, y * MapData.SQUARE_SIZE, -1)) {
								highest_resources = minerals;
								best_mine_location.x = x;
								best_mine_location.y = y;
							}
							//}
						}
					}			
				}
			}
		}

		max_map_threat = 0;
		if (top_threat_location.x >= 0) {
			max_map_threat = this.threats_intel[top_threat_location.x][top_threat_location.y] - this.allied_intel[top_threat_location.x][top_threat_location.y];
		}

		max_map_target = 0;
		if (top_target_location.x >= 0) {
			max_map_target = this.targets_intel[top_target_location.x][top_target_location.y];
		}


		// Get the best mine location
		/*int highest_resources = -1;
		for (int y = 0 ; y<map_height ; y++) {
			for (int x = 0 ; x<map_width ; x++) {
				if (threats_intel[x][y] == 0) { // Don't build near an enemy!
					int minerals = (int)main.getMapData().map[x][y].minerals;
					if (minerals >= highest_resources) { // Needs to be ">=" otherwise the AI spreads itself all over the map (was: Needs to be ">" so that if there's no resources in starting squares, it moves up-left)
						if (main.getMapData().map[x][y].owner == side) {
							if (main.isAreaClearForNewUnit(x * MapData.SQUARE_SIZE, y * MapData.SQUARE_SIZE, -1)) {
								highest_resources = minerals;
								best_mine_location.x = x;
								best_mine_location.y = y;
							}
						}
					}
				}
			}
		}
		prev_highest_resources = highest_resources;*/
	}


	public int[][] getThreatIntel() {
		return this.threats_intel;
	}

	public int[][] getTargetIntel() {
		return this.targets_intel;
	}

}

