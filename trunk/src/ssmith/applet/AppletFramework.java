package ssmith.applet;

import java.applet.Applet;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public abstract class AppletFramework extends Applet implements Runnable, MouseMotionListener, MouseListener, KeyListener  {
	
	private Thread t;
	protected boolean stop_now = false;
	protected Image img_back;
	protected volatile ArrayList<InputEvent> input_msgs = new ArrayList<InputEvent>();
	private int width, height;

	public AppletFramework(int w, int h) {
		super();
		width = w;
		height = h;

		t = new Thread(this, "AppletFramework");

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}
	
	public void init() {
		this.resize(width, height);

		img_back = this.createImage(width, height);

		t.setPriority(Thread.NORM_PRIORITY+1);
		t.start();
	}

	public void destroy() {
		stop_now = true;
	}

	public static void p(String s) {
		System.out.println(s);
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

	public void keyPressed(KeyEvent arg0) {
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent ke) {
	}

	public void addIMsg(InputEvent o) {
		this.input_msgs.add(o);
	}

	public void mouseMoved(MouseEvent evt) {
	}

	public void mouseDragged(MouseEvent e) {
	}

}
