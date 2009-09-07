package ssmith.awt;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.Hashtable;

public class AppletImageCache {

	private Hashtable<String, Image> hashImages = new Hashtable<String, Image>();
	private MediaTracker mt;
	protected Applet app;

	public AppletImageCache(Applet c) {
		app = c;
		mt = new MediaTracker(c);
	}

	public Image loadImage(String filename) {
		try {
			while (true) {
				Image img = app.getImage(app.getCodeBase(), filename);

				mt.addImage(img, 1);
				mt.waitForID(1);
				mt.removeImage(img);
				
				if (img.getWidth(app) < 0 || img.getHeight(app) < 0) {
					if (filename.startsWith("..") == false) {
						filename = "../" + filename;
					}
				} else {
					return img;
				}
			}
		} catch (Exception e) {
			System.err.println("Error loading images: " + e.getMessage());
		}
		return null;
	}

	public void putImage(String filename, Image img) {
		hashImages.put(filename, img);
	}

	public Image getImage(String filename) {
		Image img = (Image)hashImages.get(filename);
		if (img == null) {
			img = loadImage(filename);
			this.putImage(filename, img);
		}
		return img;
	}

}

