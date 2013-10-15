package com.omegabyte.testing;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import processing.core.PApplet;

import com.omegabyte.dashboard.Button;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

@SuppressWarnings({ "serial" })
public class doubleFrame extends PApplet {

	Button button = new Button(this, "button").setSize(50, 50).setPosition(100,
			100);
	Dashboard dash = new Dashboard(this, "super").add(button);

	// ).add(menuBackground.addMenu(menu));

	// this will run once for setup
	@Override
	public void setup() {
		size(500, 500);// processing.opengl.PGraphics3D);
		// dash.add(button).add(menuBackground);

	}

	// this is the looping function
	@Override
	public void draw() {
		background(200);// redraw the background
		if (button.isActive()) {
			dash.show();
		}
		dash.update();// render the hand!
		if (s != null) {
			s.redraw();
		}

	}

	public void call_button(final Widget widget) {
		if (f == null) {
			System.out.println("Start...");
			f = new PFrame();
		} else {
			System.out.println("Kill...");
			f.dispose();
			f = null;
			s = null;
		}
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present", "PhantomExample" });
	}

	public void pickup(final Widget widget) {
		System.out.println("Picked up: " + widget);
	}

	@Override
	public void keyPressed() {
	}

	ArrayList<PFrame> j = new ArrayList<PFrame>();
	PFrame f = null;
	PhantomExample s = null;

	public class PFrame extends JFrame {
		public PFrame() {
			setBounds(mouseX + 50, mouseY + 50, 400, 300);
			s = new PhantomExample();
			add(s);
			s.init();
			setVisible(true);
		}

		public void pullThePlug() {
			final WindowEvent wev = new WindowEvent(this,
					WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		}
	}

	public class secondApplet extends PApplet {
		@Override
		public void setup() {
			size(400, 300);
			noLoop();
		}

		@Override
		public void draw() {

			background(0, 0, 255);
			fill(100);
			rect(10, 20, frameCount, 10);
		}
	}

}
