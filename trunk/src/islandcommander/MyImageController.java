package islandcommander;

import java.applet.Applet;
import java.awt.Image;
import java.net.MalformedURLException;

import ssmith.awt.AppletImageCache;
import ssmith.image.ImageFunctions;
import ssmith.lang.Functions;

public final class MyImageController extends AppletImageCache implements Runnable {
	
	private static final int PCENT_INC = 9;

	private volatile boolean images_loaded = false;
	private Thread t;
	//private Main main;
	public volatile int percent = 0;

	public MyImageController(Main m, Applet app) {
		super(app);
		//main = m;
		t = new Thread(this, "MyImageController");
		t.setDaemon(true);
		t.start();
	}
	

	public void run() {
		try {
			for (int i=0 ; i<2 ; i++) {
				scaleTile("mine" + i + ".png");

				scaleTile("bomber" + i + ".png");
				scaleTile("tank" + i + ".png");
				scaleTile("silo" + i + ".png");

				percent += PCENT_INC;
				
				scaleTile("nuke" + i + ".png");
				scaleTile("jumper" + i + ".png");
				scaleTile("trap" + i + ".png");

				scaleTile("tangleweed" + i + ".png");
				scaleTile("exploder" + i + ".png");
				scaleTile("sporeshooter" + i + ".png");

				percent += PCENT_INC;
			}
			
			for (int i=0 ; i<4 ; i++) {
				scaleTile("minerals" + i + ".png");
				percent += PCENT_INC;
			}

			scaleTile("desert.png");
			percent += PCENT_INC;
			cacheImage("large07.png");
			percent += PCENT_INC;

			images_loaded = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	
	private void cacheImage(String filename) {
		filename = Functions.AppendSlash(Main.IMAGE_LOCATION) + filename;
		Image orig_img = loadImage(filename);
		putImage(filename, orig_img);
	}
	

	private void scaleTile(String filename) throws MalformedURLException {
		filename = Functions.AppendSlash(Main.IMAGE_LOCATION) + filename;
		Image orig_img = loadImage(filename);
		orig_img = ImageFunctions.scaleImage(orig_img, MapData.SQUARE_SIZE, MapData.SQUARE_SIZE, app);
		putImage(filename, orig_img);
		//System.out.println("scaled " + filename);
	}

	
	public boolean areImagesLoaded() {
		return this.images_loaded;
	}

}
