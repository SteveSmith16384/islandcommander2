package islandcommander.icons;

import islandcommander.Main;
import islandcommander.units.UnitStats;

public class IconGetStats extends AbstractIcon {

	public int type;

	public IconGetStats(Main m, int _type) {
		super(m, UnitStats.GetName(_type) + " stats", false);
		type = _type;
	}

	@Override
	public boolean execute() {
		switch(type) {
		case UnitStats.MINE:
			showStats(UnitStats.GetName(UnitStats.MINE), 0, 0, UnitStats.MINE_HEALTH, UnitStats.MINE_COST, 0, "Notes: Drills for ore to enable construction");
			break;
		case UnitStats.MISSILE_SILO:
			showStats(UnitStats.GetName(UnitStats.MISSILE_SILO), 0, UnitStats.SILO_BULLET_DAMAGE, UnitStats.SILO_HEALTH, UnitStats.SILO_COST, UnitStats.SILO_VIEW_RANGE, "Notes: Immoveable");
			break;
		case UnitStats.TANK:
			showStats(UnitStats.GetName(UnitStats.TANK), UnitStats.TANK_SPEED, UnitStats.TANK_BULLET_DAMAGE, UnitStats.TANK_HEALTH, UnitStats.TANK_COST, UnitStats.TANK_VIEW_RANGE, "Notes: Returns to base if there are no enemies.");
			break;
		case UnitStats.BOMBER:
			showStats(UnitStats.GetName(UnitStats.BOMBER), UnitStats.BOMBER_SPEED, UnitStats.BOMBER_BULLET_DAMAGE, UnitStats.BOMBER_HEALTH, UnitStats.BOMBER_COST, UnitStats.BOMBER_VIEW_RANGE, "");
			break;

		case UnitStats.NUKE:
			showStats(UnitStats.GetName(UnitStats.NUKE), UnitStats.NUKE_SPEED, UnitStats.NUKE_BULLET_DAMAGE, UnitStats.NUKE_HEALTH, UnitStats.NUKE_COST, UnitStats.NUKE_VIEW_RANGE, "A nuke will not shoot, but destroy when it lands.");
			break;
		case UnitStats.JUMPER:
			showStats(UnitStats.GetName(UnitStats.JUMPER), UnitStats.JUMPER_SPEED, UnitStats.JUMPER_BULLET_DAMAGE, UnitStats.JUMPER_HEALTH, UnitStats.JUMPER_COST, UnitStats.JUMPER_VIEW_RANGE, "Jumpers teleport around to confuse the enemy.");
			break;
		case UnitStats.TRAP:
			showStats(UnitStats.GetName(UnitStats.TRAP), UnitStats.TRAP_SPEED, UnitStats.TRAP_BULLET_DAMAGE, UnitStats.TRAP_HEALTH, UnitStats.TRAP_COST, UnitStats.TRAP_VIEW_RANGE, "Traps lay in wait for units to cross them.");
			break;

		case UnitStats.TANGLEWEED:
			showStats(UnitStats.GetName(UnitStats.TANGLEWEED), UnitStats.TANGLEWEED_SPEED, UnitStats.TANGLEWEED_BULLET_DAMAGE, UnitStats.TANGLEWEED_HEALTH, UnitStats.TANGLEWEED_COST, UnitStats.TANGLEWEED_VIEW_RANGE, "This slowly grows and spreads.");
			break;
		case UnitStats.EXPLODER:
			showStats(UnitStats.GetName(UnitStats.EXPLODER), UnitStats.EXPLODER_SPEED, UnitStats.EXPLODER_BULLET_DAMAGE, UnitStats.EXPLODER_HEALTH, UnitStats.EXPLODER_COST, UnitStats.EXPLODER_VIEW_RANGE, "Will explode when destroyed.");
			break;
		case UnitStats.SPORESHOOTER:
			showStats(UnitStats.GetName(UnitStats.SPORESHOOTER), UnitStats.SPORESHOOTER_SPEED, UnitStats.SPORESHOOTER_BULLET_DAMAGE, UnitStats.SPORESHOOTER_HEALTH, UnitStats.SPORESHOOTER_COST, UnitStats.SPORESHOOTER_VIEW_RANGE, "Shoots spores at the enemy.");
			break;

		default:
			throw new RuntimeException("todo");
		}
		return true;
	}

	public void executeOnMap(int pixel_x, int pixel_y) {
		// Do nothing
	}

	private void showStats(String name, float speed, int firepower, int armour, int cost, int view_range, String desc) {
		main.clearLog();
		main.addLogEntry("Stats for " + name);
		if (speed != 0) {
			main.addLogEntry("Speed: " + (int)(speed*10));
		}
		if (firepower != 0) {
			main.addLogEntry("Firepower: " + firepower);
		}
		main.addLogEntry("Armour: " + armour);
		main.addLogEntry("View Range: " + view_range);
		main.addLogEntry("Cost: " + cost);
		if (desc.length() > 0) {
			main.addLogEntry(desc);
		}

	}

	@Override
	protected boolean showSelected() {
		return false;
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return true;
	}


}
