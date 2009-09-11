package ssmith.awt;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.Hashtable;

public class AppletImageCache {

	protected Hashtable<String, Image> hashImages = new Hashtable<String, Image>();
	protected MediaTracker mt;
	protected Applet app;
	//private int id = 0;

	public AppletImageCache(Applet c) {
		app = c;
		mt = new MediaTracker(c);
	}

	public Image loadImage(String filename, boolean wait) {
		try {
			//while (true) {
				Image img = app.getImage(app.getCodeBase(), filename);

				mt.addImage(img, 0);
				if (wait) {
					mt.waitForID(0); // mt.isErrorAny()
					mt.removeImage(img);

					/*if (img.getWidth(app) < 0 || img.getHeight(app) < 0) {
						if (filename.startsWith("..") == false) {
							filename = "../" + filename;
						}
					} else {*/
					//}
				}
			//}
				return img;
		} catch (Exception e) {
			System.err.println("Error loading images: " + e.getMessage());
		}
		return null;
	}

	/*public void putImage(String filename, Image img) {
		hashImages.put(filename, img);
	}*/

	public Image getImage(String filename, boolean wait) {
		Image img = (Image)hashImages.get(filename);
		if (img == null) {
			img = loadImage(filename, wait);
			this.hashImages.put(filename, img);
		}
		return img;
	}

}

