package islandcommander;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import ssmith.util.FIFOList;

public final class LogWindow extends FIFOList {
	
	public static final int HEIGHT = 115;
	private static final int LINE_SPACE = 17;
	private static final Color c = new Color(0, 130, 0);
	
	private static Font font_normal = new Font("Arial", Font.PLAIN, 14);

	public LogWindow() {
		super(6);
	}
	
	public void paint(Graphics g, int x, int y) {
		if(this.size() > 0) {
		g.setColor(c);
		g.drawRect(x, y, 450, HEIGHT);
		
		g.setColor(Color.green);
		g.setFont(font_normal);
		for (int i=0 ; i<super.size() ; i++) {
			String s = (String)this.get(i);
			g.drawString(s, x + 10, y+LINE_SPACE +(i*LINE_SPACE));
		}
		}
	}

}
