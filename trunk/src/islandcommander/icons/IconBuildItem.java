package islandcommander.icons;

import java.util.ArrayList;

import islandcommander.Main;
import islandcommander.MapData;
import islandcommander.units.*;

public final class IconBuildItem extends AbstractIcon {

	public int type;

	public IconBuildItem(Main m, int _type) {
		//super(m, "Build " + UnitStats.GetName(_type) + " (" + UnitStats.GetCost(_type) + ")", true);
		super(m, "Build " + UnitStats.GetName(_type), true);
		type = _type;
	}

	@Override
	public boolean execute() {
		return main.game_data.pdata[0].getCash() >= UnitStats.GetCost(type);
	}

	public void executeOnMap(int pixel_x, int pixel_y) {
		try {
			// Check the map square is owned
			if (main.getMapData().map[pixel_x/MapData.SQUARE_SIZE][pixel_y/MapData.SQUARE_SIZE].owner == 0) {
				// Adjust pos to centre
				int mx = pixel_x  / MapData.SQUARE_SIZE;
				int my = pixel_y / MapData.SQUARE_SIZE;

				if (main.isAreaClearForNewUnit(mx * MapData.SQUARE_SIZE, my * MapData.SQUARE_SIZE, type)) {
					switch(type) {
					case UnitStats.MINE:
						if (main.game_data.pdata[0].getCash() >= UnitStats.MINE_COST) {
							new UnitMiningPlatform(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.MINE_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.MINE_COST);
						}
						break;
					case UnitStats.MISSILE_SILO:
						if (main.game_data.pdata[0].getCash() >= UnitStats.SILO_COST) {
							new UnitMissileSilo(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.SILO_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.SILO_COST);
						}
						break;
					case UnitStats.TANK:
						if (main.game_data.pdata[0].getCash() >= UnitStats.TANK_COST) {
							new UnitTank(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.TANK_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.TANK_COST);
						}
						break;
					case UnitStats.BOMBER:
						if (main.game_data.pdata[0].getCash() >= UnitStats.BOMBER_COST) {
							new UnitBomber(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.BOMBER_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.BOMBER_COST);
						}
						break;
					case UnitStats.NUKE:
						if (main.game_data.pdata[0].getCash() >= UnitStats.NUKE_COST) {
							new UnitNuke(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.NUKE_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.NUKE_COST);
						}
						break;
					case UnitStats.JUMPER:
						if (main.game_data.pdata[0].getCash() >= UnitStats.JUMPER_COST) {
							new UnitJumper(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.JUMPER_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.JUMPER_COST);
						}
						break;
					case UnitStats.TRAP:
						if (main.game_data.pdata[0].getCash() >= UnitStats.TRAP_COST) {
							new UnitTrap(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.TRAP_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.TRAP_COST);
						}
						break;

					case UnitStats.TANGLEWEED:
						if (main.game_data.pdata[0].getCash() >= UnitStats.TANGLEWEED_COST) {
							new UnitTangleWeed(main, mx, my, 0, new ArrayList<UnitTangleWeed>());
							main.game_data.pdata[0].addCash(-UnitStats.TANGLEWEED_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.TANGLEWEED_COST);
						}
						break;
					case UnitStats.EXPLODER:
						if (main.game_data.pdata[0].getCash() >= UnitStats.EXPLODER_COST) {
							new UnitExploder(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.EXPLODER_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.EXPLODER_COST);
						}
						break;
					case UnitStats.SPORESHOOTER:
						if (main.game_data.pdata[0].getCash() >= UnitStats.SPORESHOOTER_COST) {
							new UnitSporeShooter(main, mx, my, 0);
							main.game_data.pdata[0].addCash(-UnitStats.SPORESHOOTER_COST);
						} else {
							main.addTooExpensiveLogEntry(UnitStats.SPORESHOOTER_COST);
						}
						break;

					default:
						throw new RuntimeException("todo");
					}
				} else {
					main.addLogEntry("Area not clear.");
				}
			} else {
				main.addLogEntry("You do not own that square.");
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			// Do nothing
		}
	}

	@Override
	protected boolean showSelected() {
		return this == main.getLastSelectedIcon();
	}

	@Override
	public boolean isActive() {
		return main.game_data.pdata[0].getCash() >= UnitStats.GetCost(type);
	}
}

