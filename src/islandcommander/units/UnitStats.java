package islandcommander.units;

import java.util.ArrayList;

import islandcommander.Main;
import islandcommander.MapData;

public class UnitStats {
	
	// Types
	public static final int MINE = 0;
	public static final int MISSILE_SILO = 1;
	public static final int TANK = 2;
	public static final int BOMBER = 3;
	public static final int NUKE = 4;
	public static final int JUMPER = 5;
	public static final int TRAP = 6;
	public static final int TANGLEWEED = 7;
	public static final int EXPLODER = 8;
	public static final int SPORESHOOTER = 9;

	// Bomber
	public static final int BOMBER_COST = 38;
	public static final int BOMBER_HEALTH = 5;
	public static final float BOMBER_SPEED = .5f;
	public static final int BOMBER_BULLET_DAMAGE = 1;
	public static final int BOMBER_VIEW_RANGE = 20 * MapData.SQUARE_SIZE;
	public static final int BOMBER_THREAT = 4;

	// Tank
	public static final int TANK_COST = 75;
	public static final int TANK_HEALTH = 10;
	public static final float TANK_SPEED = .2f;
	public static final int TANK_BULLET_DAMAGE = 2;
	public static final int TANK_VIEW_RANGE = 20 * MapData.SQUARE_SIZE;//6 * MapData.SQUARE_SIZE;
	public static final int TANK_THREAT = 4;

	// Misile Silo
	public static final int SILO_COST = 60;
	public static final int SILO_HEALTH = 10;
	public static final int SILO_BULLET_DAMAGE = 2; // Too powerful at attacking with 3 
	public static final int SILO_VIEW_RANGE = GameUnit.SHOT_RANGE;
	public static final int SILO_THREAT = 2;

	// Mine
	public static final int MINE_COST = 9;
	public static final int MINE_HEALTH = 5;
	public static final int MINE_THREAT = 1;

	// Nuke
	public static final int NUKE_COST = 120;
	public static final int NUKE_HEALTH = 200;
	public static final float NUKE_SPEED = .8f;
	public static final int NUKE_BULLET_DAMAGE = 0; // Must be 0 to stop it shooting!
	public static final int NUKE_VIEW_RANGE = MapData.SQUARE_SIZE;
	public static final int NUKE_THREAT = 1;

	// Jumper
	public static final int JUMPER_COST = 25;
	public static final int JUMPER_HEALTH = 8;
	public static final float JUMPER_SPEED = 0;
	public static final int JUMPER_BULLET_DAMAGE = 1;
	public static final int JUMPER_VIEW_RANGE = GameUnit.SHOT_RANGE;
	public static final int JUMPER_THREAT = 2;

	// Trap
	public static final int TRAP_COST = 15;
	public static final int TRAP_HEALTH = 10;
	public static final float TRAP_SPEED = 0;
	public static final int TRAP_BULLET_DAMAGE = 0;
	public static final int TRAP_VIEW_RANGE = GameUnit.SHOT_RANGE;
	public static final int TRAP_THREAT = 2;

	// Tangleweed
	public static final int TANGLEWEED_COST = 50;
	public static final int TANGLEWEED_HEALTH = 1;
	public static final float TANGLEWEED_SPEED = 0;
	public static final int TANGLEWEED_BULLET_DAMAGE = 1;
	public static final int TANGLEWEED_VIEW_RANGE = GameUnit.SHOT_RANGE;
	public static final int TANGLEWEED_THREAT = 1;

	// Exploder
	public static final int EXPLODER_COST = 25;
	public static final int EXPLODER_HEALTH = 4;
	public static final float EXPLODER_SPEED = 0;
	public static final int EXPLODER_BULLET_DAMAGE = 4;
	public static final int EXPLODER_VIEW_RANGE = GameUnit.SHOT_RANGE;
	public static final int EXPLODER_THREAT = 2;

	// SporeShooter
	public static final int SPORESHOOTER_COST = 35;
	public static final int SPORESHOOTER_HEALTH = 10;
	public static final float SPORESHOOTER_SPEED = .1f;
	public static final int SPORESHOOTER_BULLET_DAMAGE = 1;
	public static final int SPORESHOOTER_VIEW_RANGE = 20 * MapData.SQUARE_SIZE;
	public static final int SPORESHOOTER_THREAT = 2;

	
	public static String GetName(int type) {
		switch(type) {
		case MINE:
			return "Mine";
		case MISSILE_SILO:
			return "M/Silo";
		case TANK:
			return "Tank";
		case BOMBER:
			return "Bomber";
		case NUKE:
			return "Nuke";
		case JUMPER:
			return "Jumper";
		case TRAP:
			return "Trap";

		case TANGLEWEED:
			return "Tangleweed";
		case EXPLODER:
			return "Exploder";
		case SPORESHOOTER:
			return "SporeShooter";

		default:
			throw new RuntimeException("todo");
		}
	}

	public static int GetCost(int type) {
		switch(type) {
		case MINE:
			return UnitStats.MINE_COST;
		case MISSILE_SILO:
			return UnitStats.SILO_COST;
		case TANK:
			return UnitStats.TANK_COST;
		case BOMBER:
			return UnitStats.BOMBER_COST;
		case NUKE:
			return UnitStats.NUKE_COST;
		case JUMPER:
			return UnitStats.JUMPER_COST;
		case TRAP:
			return UnitStats.TRAP_COST;
		case TANGLEWEED:
			return UnitStats.TANGLEWEED_COST;
		case EXPLODER:
			return UnitStats.EXPLODER_COST;
		case SPORESHOOTER:
			return UnitStats.SPORESHOOTER_COST;
		default:
			throw new RuntimeException("todo");
		}
	}


	public static void CreateUnit(Main m, int type, int mx, int my, int side) {
		switch(type) {
		case MINE:
			new UnitMiningPlatform(m, mx, my, side);
			return;
		case MISSILE_SILO:
			new UnitMissileSilo(m, mx, my, side);
			return;
		case TANK:
			new UnitTank(m, mx, my, side);
			return;
		case BOMBER:
			new UnitBomber(m, mx, my, side);
			return;

		case NUKE:
			new UnitNuke(m, mx, my, side);
			return;
		case JUMPER:
			new UnitJumper(m, mx, my, side);
			return;
		case TRAP:
			new UnitTrap(m, mx, my, side);
			return;

		case TANGLEWEED:
			new UnitTangleWeed(m, mx, my, side, new ArrayList<UnitTangleWeed>());
			return;
		case EXPLODER:
			new UnitExploder(m, mx, my, side);
			return;
		case SPORESHOOTER:
			new UnitSporeShooter(m, mx, my, side);
			return;

		default:
			throw new RuntimeException("todo");
		}
	}

}
