package islandcommander.graphics;

import islandcommander.Main;
import islandcommander.units.Sprite;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import ssmith.image.ImageFunctions;
import ssmith.util.Interval;

public class Explosion extends Sprite {

	private static final int MAX_FRAMES = 15;
	private static final int OFF_WIDTH = 64;
	private static final int OFF_HEIGHT = 48;

	private static final long serialVersionUID = 1L;

	private Interval anim_interval = new Interval(70, false);
	private int anim=0;
	private static BufferedImage[] frames;

	public Explosion(Main main, Sprite s) {
		super(main, s.getMiddleX(), s.getMiddleY(), 0, 0, false, 9999);

		if (frames == null) {
			Image img = main.getImage("large07.png");
			frames = ImageFunctions.ExtractGraphics(img, 8, 2, main);
		}
	}

	public void paint(Graphics g, int centre_x, int centre_y) {
		if (frames != null) {
			if (anim <= MAX_FRAMES) {
				g.drawImage(frames[anim], (int)(x-centre_x-OFF_WIDTH), (int)(y-centre_y-OFF_HEIGHT), main);
			}
		}
	}

	public void process() {
		if (anim_interval.hitInterval()) {
			anim++;
			if (anim > MAX_FRAMES) {
				this.remove();
			}
		}
	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false; // Do nothing
	}


}
