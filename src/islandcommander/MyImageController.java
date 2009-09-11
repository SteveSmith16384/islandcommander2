package islandcommander;

import java.applet.Applet;
import java.awt.Image;
import java.net.MalformedURLException;

import ssmith.awt.AppletImageCache;
import ssmith.image.ImageFunctions;
import ssmith.lang.Functions;

public final class MyImageController extends AppletImageCache implements Runnable {
	
	private static final int PCENT_INC = 2;

	private volatile boolean images_loaded = false;
	private Thread t;
	public volatile int percent = 0;

	public MyImageController(Main m, Applet app) {
		super(app);
		t = new Thread(this, "MyImageController");
		t.setDaemon(true);
		t.start();
	}
	

	public void run() {
		try {
			// Start all the images loading...
			for (int i=0 ; i<2 ; i++) {
				cacheImage("mine" + i + ".png");

				cacheImage("bomber" + i + ".png");
				cacheImage("tank" + i + ".png");
				cacheImage("silo" + i + ".png");

				cacheImage("nuke" + i + ".png");
				cacheImage("jumper" + i + ".png");
				cacheImage("trap" + i + ".png");

				cacheImage("tangleweed" + i + ".png");
				cacheImage("exploder" + i + ".png");
				cacheImage("sporeshooter" + i + ".png");
			}
			for (int i=0 ; i<4 ; i++) {
				cacheImage("minerals" + i + ".png");
			}

			cacheImage("desert.png");
			cacheImage("large07.png");

			// Wait for them all
			this.mt.waitForAll();
			if (mt.isErrorAny()) {
				/*int err = mt.statusID(0, false);
				boolean b = mt.ERRORED == err;
				b = mt.COMPLETE == err;
				int fgfg = 5465;*/
				throw new RuntimeException("Error loading images");
			}
			
			//Now scale them
			for (int i=0 ; i<2 ; i++) {
				scaleTile("mine" + i + ".png");

				scaleTile("bomber" + i + ".png");
				scaleTile("tank" + i + ".png");
				scaleTile("silo" + i + ".png");
				
				scaleTile("nuke" + i + ".png");
				scaleTile("jumper" + i + ".png");
				scaleTile("trap" + i + ".png");

				scaleTile("tangleweed" + i + ".png");
				scaleTile("exploder" + i + ".png");
				scaleTile("sporeshooter" + i + ".png");
			}
			
			for (int i=0 ; i<4 ; i++) {
				scaleTile("minerals" + i + ".png");
			}

			scaleTile("desert.png");

			images_loaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void cacheImage(String filename) {
		filename = Functions.AppendSlash(Main.IMAGE_LOCATION) + filename;
		Image orig_img = loadImage(filename, false);
		this.hashImages.put(filename, orig_img);

		percent += PCENT_INC;
		
}
	

	/*private void scaleTile(String filename) throws MalformedURLException {
		filename = Functions.AppendSlash(Main.IMAGE_LOCATION) + filename;
		Image orig_img = loadImage(filename, false);
		orig_img = ImageFunctions.scaleImage(orig_img, MapData.SQUARE_SIZE, MapData.SQUARE_SIZE, app);
		putImage(filename, orig_img);
		//System.out.println("scaled " + filename);
	}*/

	
	private void scaleTile(String filename) throws MalformedURLException {
		filename = Functions.AppendSlash(Main.IMAGE_LOCATION) + filename;
		Image orig_img = this.hashImages.get(filename);
		orig_img = ImageFunctions.scaleImage(orig_img, MapData.SQUARE_SIZE, MapData.SQUARE_SIZE, app);
		this.hashImages.remove(filename);
		this.hashImages.put(filename, orig_img);
		//System.out.println("scaled " + filename);
		
		percent += PCENT_INC;
		
	}

	
	public boolean areImagesLoaded() {
		return this.images_loaded;
	}

}
