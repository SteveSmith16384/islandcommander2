package islandcommander;

import islandcommander.controls.AbstractControl;
import islandcommander.controls.*;
import islandcommander.icons.AbstractIcon;
import islandcommander.units.GameUnit;
import islandcommander.units.Sprite;
import islandcommander.units.UnitBomber;
import islandcommander.units.UnitMiningPlatform;
import islandcommander.units.UnitStats;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ssmith.applet.AppletFramework;
import ssmith.lang.Functions;
import ssmith.util.Interval;
import ssmith.util.ThreadSafeArrayList;

public final class Main extends AppletFramework {

	public static boolean DEBUG_AI = false;
	private static final long LOOP_DELAY = 45;
	public static final int APPLET_SIZE = 800;
	public static final int SEE_MAP_DIST = 250;
	public static final String TITLE = "Island Commander 2";
	public static final String VERSION = "v0.42";
	public static final String IMAGE_LOCATION = "data/images/";
	private static final int MAP_SCROLL_SPEED = 15;
	public static final boolean FOG_OF_WAR = true;

	// Game stages
	private static final int STAGE_FIRST_TIME = -2;
	//private static final int STAGE_PRE_GAME = -1;
	private static final int STAGE_GAME_MENU = 0;
	private static final int STAGE_PLAYING_GAME = 1;
	private static final int STAGE_GAME_FINISHED = 2;

	// Races - if you add to these, change the "random race" selector for the AI!
	public static final int RACE_EARTHLINGS = 0;
	public static final int RACE_TECHNOVORBS = 1;
	public static final int RACE_ORGANACTICS = 2;

	private static final long serialVersionUID = 1L;

	public static Font font_large = new Font("Impact", Font.PLAIN, 36);
	public static Font font_xlarge = new Font("Impact", Font.PLAIN, 72);

	public volatile ThreadSafeArrayList sprites;
	private int i_game_stage = STAGE_FIRST_TIME;
	private Point view_top_left = new Point(255, 360);//330, 315);
	private MyImageController img_cache;
	public GameData game_data;
	private volatile LogWindow log = new LogWindow();
	private StatsWindow stats = new StatsWindow(this);
	private IconPanel icon_panel;
	private Point mouse_pos = new Point();
	private Interval map_update_interval = new Interval(3000, true);
	public boolean check_mapsquare_owners = false; // Do we need to bother recalcing map square owners?
	private boolean show_map_owners = true;
	private boolean show_threats = false;
	private boolean show_targets = false;
	private boolean paused = false, won;
	private ArrayList<AbstractControl> menus;
	private NumberSelector num_w, num_h;

	
	public Main() {
		super(APPLET_SIZE, APPLET_SIZE);
		Cursor hourglassCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		setCursor(hourglassCursor);
	}

	
	public void init() {
		super.init();
		img_cache = new MyImageController(this, this);
		//log.add("Email stephen.carlylesmith@googlemail.com for help & support");
	}

	
	public void run() {
		while (!stop_now) {
			long start = System.currentTimeMillis();

			while (this.input_msgs.size() > 0) {
				Object o = this.input_msgs.get(0);
				this.input_msgs.remove(0);
				if (o instanceof MouseEvent) {
					MouseEvent evt = (MouseEvent)o;
					if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
						if (this.i_game_stage == Main.STAGE_FIRST_TIME) {
							if (this.img_cache.areImagesLoaded()) {
								this.showGameMenu();
							}
						} else if (this.i_game_stage == Main.STAGE_GAME_MENU) {
							//if (evt.getButton() == MouseEvent.BUTTON1) {
							if (menus != null) {
								AbstractControl ac = null;
								for (int i=0 ; i<menus.size() ; i++) {
									ac = (AbstractControl)menus.get(i);
									if (ac.contains(evt.getX(), evt.getY())) {
										ac.mouseDown(evt.getX(), evt.getY());
										break;
									}
								}
							}
						} else if (icon_panel != null && icon_panel.contains(mouse_pos)) {
							icon_panel.mouseDown(evt.getY()-icon_panel.y);
						} else if (icon_panel != null) {
							if (i_game_stage == Main.STAGE_PLAYING_GAME) {
								if (!this.paused) {
									int mouse_x = (int)this.view_top_left.x + evt.getX() - (APPLET_SIZE/2);
									int mouse_y = (int)this.view_top_left.y + evt.getY() - (APPLET_SIZE/2);
									if (icon_panel.last_selected_icon != null) {
										icon_panel.last_selected_icon.executeOnMap(mouse_x, mouse_y);
									}
								} else {
									addPausedLogEntry();
								}
							}
						}
					}
				} else if (o instanceof KeyEvent) {
					KeyEvent evt = (KeyEvent)o;
					if (evt.getKeyCode() == KeyEvent.VK_UP) {
						this.view_top_left.y -= MAP_SCROLL_SPEED;
					} else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
						this.view_top_left.y += MAP_SCROLL_SPEED;
					} else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
						this.view_top_left.x -= MAP_SCROLL_SPEED;
					} else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
						this.view_top_left.x += MAP_SCROLL_SPEED;
					} else if (evt.getKeyCode() == KeyEvent.VK_C) {
						this.game_data.pdata[0].addCash(10);
						this.addLogEntry("CHEAT!");
					} else if (evt.getKeyCode() == KeyEvent.VK_D) {
						Main.DEBUG_AI = !Main.DEBUG_AI;
						this.addLogEntry("Debug toggled");
					/*} else if (evt.getKeyCode() == KeyEvent.VK_F || evt.getKeyCode() == KeyEvent.VK_3) {
						this.icon_panel.last_selected_icon = this.icon_panel.getBuildMenuItem(UnitStats.BOMBER);
					} else if (evt.getKeyCode() == KeyEvent.VK_M || evt.getKeyCode() == KeyEvent.VK_1) {
						this.icon_panel.last_selected_icon = this.icon_panel.getBuildMenuItem(UnitStats.MINE);*/
					} else if (evt.getKeyCode() == KeyEvent.VK_P) {
						togglePause();
					} else if (evt.getKeyCode() == KeyEvent.VK_R) {
						if (!this.paused) {
							if (this.img_cache.areImagesLoaded()) {
								this.showGameMenu();
							} else {
								log.add("Still loading images!  Please wait...");
							}
						} else {
							this.addPausedLogEntry();
						}
					/*} else if (evt.getKeyCode() == KeyEvent.VK_S || evt.getKeyCode() == KeyEvent.VK_2) {
						this.icon_panel.last_selected_icon = this.icon_panel.getBuildMenuItem(UnitStats.MISSILE_SILO);
					} else if (evt.getKeyCode() == KeyEvent.VK_T || evt.getKeyCode() == KeyEvent.VK_4) {
						this.icon_panel.last_selected_icon = this.icon_panel.getBuildMenuItem(UnitStats.TANK);*/
					} else if (evt.getKeyCode() == KeyEvent.VK_X) {
						this.game_data.pdata[1].addCash(10);
						this.addLogEntry("CPU CHEATED!");
					} else if (evt.getKeyCode() == KeyEvent.VK_1) {
						this.icon_panel.shortcutSelected(1);
					} else if (evt.getKeyCode() == KeyEvent.VK_2) {
						this.icon_panel.shortcutSelected(2);
					} else if (evt.getKeyCode() == KeyEvent.VK_3) {
						this.icon_panel.shortcutSelected(3);
					} else if (evt.getKeyCode() == KeyEvent.VK_4) {
						this.icon_panel.shortcutSelected(4);
					} else if (evt.getKeyCode() == KeyEvent.VK_F1) {
						if (this.i_game_stage == Main.STAGE_PLAYING_GAME) {
							this.show_threats = !this.show_threats;
							this.addLogEntry("Show threats? " + this.show_threats);
						}
					} else if (evt.getKeyCode() == KeyEvent.VK_F2) {
						if (this.i_game_stage == Main.STAGE_PLAYING_GAME) {
							this.show_targets = !this.show_targets;
							this.addLogEntry("Show targets? " + this.show_targets);
						}
					}
				} else {
					System.err.println("Unknown event type: " + o.toString());
				}
			}

			if (i_game_stage == Main.STAGE_PLAYING_GAME) {
				if (!this.paused) {
					this.game_data.time++;
					if (map_update_interval.hitInterval()) {
						if (this.check_mapsquare_owners) {
							this.getMapData().recalcOwners();
							this.check_mapsquare_owners = false;
							if (game_data.pdata[0].total_squares == 0) {
								won = false;
								this.addLogEntry("*************");
								this.addLogEntry("YOU HAVE LOST!");
								this.addLogEntry("*************");
								this.i_game_stage = Main.STAGE_GAME_FINISHED;
								//logWinOrLose(won);
							} else if (game_data.pdata[1].total_squares == 0) {
								won = true;
								this.addLogEntry("*************");
								this.addLogEntry("YOU HAVE WON!");
								this.addLogEntry("*************");
								this.i_game_stage = Main.STAGE_GAME_FINISHED;
								//logWinOrLose(won);
							}
						}
					}

					this.game_data.process();

					if (sprites != null) {
						Sprite sprite;
						for (int i=0 ; i<sprites.size() ; i++) {
							sprite = (Sprite)sprites.get(i);
							if (sprite != null) {
								sprite.process();
							}
						}
					}
				}
			}

			this.repaint();

			long wait = LOOP_DELAY - System.currentTimeMillis() + start;
			if (wait < 0) {
				p("Delay: " + wait);
			}
			Functions.delay(wait);
		}
	}


	private void showGameMenu() {
		i_game_stage = Main.STAGE_GAME_MENU;

		menus = new ArrayList<AbstractControl>();
		
		menus.add(new Label(this, 50, 50, TITLE));

		menus.add(new Label(this, 50, 150, "Game Options:"));

		num_w = new NumberSelector(this, 50, 200, 400, 50, "Map Width:", 20, 8);
		menus.add(num_w);
		num_h = new NumberSelector(this, 50, 250, 400, 50, "Map Height:", 20, 8);
		menus.add(num_h);

		menus.add(new Label(this, 50, 450, "Choose your race:"));

		Button btn_g = new Button(this, 50, 500, 220, 50, "Earthlings", Main.RACE_EARTHLINGS);
		menus.add(btn_g);
		Button btn_t = new Button(this, 285, 500, 220, 50, "Technovorbs", Main.RACE_TECHNOVORBS);
		menus.add(btn_t);
		Button btn_o = new Button(this, 520, 500, 220, 50, "Organactics", Main.RACE_ORGANACTICS);
		menus.add(btn_o);
	}

	
	public void startNewGame(int players_race) {
		if (this.img_cache.areImagesLoaded()) {
			i_game_stage = STAGE_PLAYING_GAME;

			//log.add("Game started!");
			log.clear();
			log.add("Use the menu on the left to select what to build.");
			log.add("Build units by clicking on the map.");

			game_data = new GameData(this, players_race, num_w.getNumber(), num_h.getNumber());

			input_msgs.clear();
			icon_panel = new IconPanel(this, 20, 50);
			icon_panel.reset();

			// Create starting units
			sprites = new ThreadSafeArrayList();

			new UnitMiningPlatform(this, 0, 0, 0);
			this.game_data.map_data.map[0][0].minerals = MapGen.MAX_MINERALS;
			int x = num_w.getNumber() - 1;
			int y = num_h.getNumber() - 1;
			new UnitMiningPlatform(this, x, y, 1);
			this.game_data.map_data.map[x][y].minerals = MapGen.MAX_MINERALS;
			
			menus.clear();

			map_update_interval.fireInterval();
		} else {
			log.add("Still loading images!  Please wait...");
		}
	}


	private void addPausedLogEntry() {
		this.addLogEntry("The game is paused!  Press P to unpause");
	}


	public void togglePause() {
		if (this.i_game_stage == Main.STAGE_PLAYING_GAME) {
			this.paused = !paused;
		}
	}


	public boolean isPaused() {
		return this.paused;
	}


	public void update(Graphics g2) {
		try {
			Graphics g = this.img_back.getGraphics();

			g.setColor(Color.black);
			g.fillRect(0, 0, APPLET_SIZE, APPLET_SIZE);

			// Draw map
			if (getMapData() != null) {
				getMapData().paint(g, (int)this.view_top_left.x, (int)this.view_top_left.y, this.show_map_owners, this.show_threats, this.show_targets);
			}

			if (sprites != null) {
				Sprite sprite;
				for (int i=0 ; i<sprites.size() ; i++) {
					sprite = (Sprite)sprites.get(i);
					if (sprite != null) {
						try {
							if (this.getMapData().map[sprite.getMapX()][sprite.getMapY()].seen) {
								sprite.paint(g, (int)this.view_top_left.x - (APPLET_SIZE/2), (int)this.view_top_left.y - (APPLET_SIZE/2));
							}
						} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
							// Do nothing
						}
					}
				}
			}

			if (menus != null) {
				g.setFont(font_large);
				AbstractControl ac = null;
				for (int i=0 ; i<menus.size() ; i++) {
					ac = (AbstractControl)menus.get(i);
					ac.paint(g);
				}
			}

			if (i_game_stage < Main.STAGE_GAME_MENU) {
				g.setColor(Color.yellow);
				g.setFont(font_xlarge);
				g.drawString(TITLE, 50, 200);
				g.setFont(font_large);
				g.drawString(VERSION, 60, 250);
				if (this.img_cache.areImagesLoaded()) {
					if (this.i_game_stage == Main.STAGE_FIRST_TIME) {
						g.drawString("Click to Start!", APPLET_SIZE/2-100, APPLET_SIZE/2+40);
					} else {
						g.drawString("Press S to Start!", APPLET_SIZE/2-100, APPLET_SIZE/2+40);
					}
				} else {
					g.drawString("Please wait... (" + this.img_cache.percent + "%)", APPLET_SIZE/2-100, APPLET_SIZE/2+40);
				}
			} else if (i_game_stage == Main.STAGE_GAME_FINISHED) {
				g.setColor(Color.yellow);
				g.setFont(font_xlarge);
				if (won) {
					g.drawString("YOU HAVE WON!", 150, 200);
				} else {
					g.drawString("You have LOST!", 150, 200);
				}
				g.setFont(font_large);
				g.drawString("Press R to restart", APPLET_SIZE/2, APPLET_SIZE/2+40);
			}

			log.paint(g, 20, APPLET_SIZE - LogWindow.HEIGHT - 20);
			if (i_game_stage >= Main.STAGE_PLAYING_GAME) {
				stats.paint(g, 20, 20);
				if (icon_panel != null) {
					icon_panel.paint(g);
				}
			}

			g2.drawImage(this.img_back, 0, 0, this);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public Player getPlayerData(int side) {
		return this.game_data.pdata[side];
	}


	public MapData getMapData() {
		if (this.game_data != null) {
			return this.game_data.map_data;
		}
		return null;
	}

	public void mousePressed(MouseEvent evt) {
		this.mouse_pos = evt.getPoint();
		this.addIMsg(evt);
	}

	public void mouseReleased(MouseEvent evt) {
		this.mouse_pos = evt.getPoint();
		this.addIMsg(evt);
	}

	public void resetMenu() {
		this.icon_panel.reset();
	}

	public void keyPressed(KeyEvent arg0) {
		this.addIMsg(arg0);
	}

	/*private Sprite getSpriteAt(int x, int y) {
	if (sprites != null) {
		for (int i=0 ; i<sprites.size() ; i++) {
			Sprite s = (Sprite)sprites.get(i);
			if (s.contains(x, y)) {
				return s;
			}
		}
	}
	return null;
}*/


	public ArrayList<Sprite> getPlayersUnits() {
		ArrayList<Sprite> arr = new ArrayList<Sprite>();
		if (sprites != null) {
			for (int i=0 ; i<sprites.size() ; i++) {
				Sprite s = (Sprite)sprites.get(i);
				if (s instanceof GameUnit) {
					GameUnit gu = (GameUnit)s;
					if (gu.side == 0) {
						arr.add(s);
					}
				}
			}
		}
		return arr;
	}


	public void addLogEntry(String s) {
		log.add(s);
	}

	public void addTooExpensiveLogEntry(int cost) {
		log.add("You can't afford " + cost + ".  Wait for the mines to generate more cash.");
	}

	public void clearLog() {
		log.clear();
	}

	public Image getImage(String fname) {
		return this.img_cache.getImage(Main.IMAGE_LOCATION + fname);
	}

	public void addIcon(AbstractIcon ic) {
		this.icon_panel.addIcon(ic);
	}

	public void removeIcons() {
		this.icon_panel.clearIcons();
	}

	public AbstractIcon getLastSelectedIcon() {
		return this.icon_panel.last_selected_icon;
	}

	public void clearLastSelectedIcon() {
		this.icon_panel.last_selected_icon = null;
	}

	
	private static MyRectangle myrect = new MyRectangle(0, 0, MapData.SQUARE_SIZE, MapData.SQUARE_SIZE);
	public boolean isAreaClearForNewUnit(int px, int py, int type_to_be_built) {
		myrect.x = px;
		myrect.y = py;
		
		Sprite s;
		for (int e=0 ; e<sprites.size() ; e++) {
			s = (Sprite)sprites.get(e);
			if (s.collideable) {
				if (s instanceof GameUnit) { // So bullets don't block us
					if (s.intersects(myrect)) {
						if (type_to_be_built == UnitStats.BOMBER) { // Bombers only block Bombers
							if (s instanceof UnitBomber) {
								return false;
							} 
						} else {
							if (s instanceof UnitBomber == false) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}


	public static void main(String[] args) {
		new Main();

	}

}
