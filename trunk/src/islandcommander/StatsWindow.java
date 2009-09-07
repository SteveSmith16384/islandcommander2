package islandcommander;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public final class StatsWindow {

	//private static final int WIDTH = 95, HEIGHT = 50;
	
	private static Font font_normal = new Font("Arial", Font.PLAIN, 14);
	private static Font font_larger = new Font("Arial", Font.PLAIN, 20);

	private Main main;

	public StatsWindow(Main m) {
		main = m;
	}

	public void paint(Graphics g, int x, int y) {
		if (main.game_data != null) {
			g.setColor(Color.cyan);
			//g.drawRect(x, y, WIDTH, HEIGHT);
			g.setFont(font_larger);
			g.drawString("Cash: " + (int)main.game_data.pdata[0].getCash(), x+5, y+15);
			g.setFont(font_normal);
			//g.drawString("Time: " + main.game_data.time / 10, x+5, y+33);
			if (Main.DEBUG_AI) {
				MasterAI ai = main.game_data.pdata[1].getAI();
				g.drawString("Your Mines: " + main.game_data.pdata[0].total_mines, x+5, y+330);
				g.drawString("CPU Cash: " + (int)main.game_data.pdata[1].getCash(), x+5, y+360);
				g.drawString("CPU Mines: " + main.game_data.pdata[1].total_mines, x+5, y+375);
				g.drawString("Max Threat: " + ai.max_map_threat, x+5, y+390);
				g.drawString("Max Target: " + ai.max_map_target, x+5, y+405);
				g.drawString("Buy Mine Chance: 1/" + MasterAI.BUY_MINE_CHANCE, x+5, y+420);
				g.drawString("Buy mine?: " + ai.build_mine, x+5, y+435);
				g.drawString("Att Qty: " + ai.num_attackers_to_build, x+5, y+450);
				g.drawString("Def Qty: " + ai.num_defenders_to_build, x+5, y+465);
				g.drawString("Race: " + main.game_data.pdata[1].race, x+5, y+480);
			}
		}
		
	}

}
