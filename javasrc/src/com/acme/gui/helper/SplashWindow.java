package com.acme.gui.helper;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Window;
import java.net.URL;

import javax.swing.JFrame;

import com.acme.core.Utilities;

/**
 * Unused.
 */

public class SplashWindow extends Window {
	private static final long serialVersionUID = 1L;

	private Image image;

	private SplashWindow(JFrame parent, Image image) {
		super(parent);
		this.image = image;

		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image,0);
		try {
			mt.waitForID(0);
		} catch(InterruptedException ie){

		}

	}


	public static void invokeMain(String className, String[] args) {
		try {
			Class.forName(className).getMethod("main", new Class[] {String[].class}).invoke(null, new Object[] {args});
		} catch (Exception e) {
			InternalError error = new InternalError("Failed to invoke main method");
			error.initCause(e);
			throw error;
		}
	}

	private static SplashWindow instance;

	private boolean paintCalled = false;

	public static void splash(URL imageURL) {
		if (imageURL != null) {
			splash(Utilities.getToolkit().createImage(imageURL));
		}
	}

	@SuppressWarnings("deprecation")
	public static void splash(Image image) {
		if (instance == null && image != null) {
			JFrame f = new JFrame();

			instance = new SplashWindow(f, image);
			instance.show();
			if (! EventQueue.isDispatchThread() 
					&& Runtime.getRuntime().availableProcessors() == 1) {

				synchronized (instance) {
					while (! instance.paintCalled) {
						try {

							instance.wait();

						} catch (InterruptedException e) {

						}
					}
				}
			}
		}

	}

	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);

		if (! paintCalled) {
			paintCalled = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	public static void disposeSplash() {
		instance.setVisible(false);
		instance.dispose();
	}
}