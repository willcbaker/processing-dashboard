package com.omegabyte.testing;

import processing.core.PApplet;
import processing.core.PVector;

import com.omegabyte.dashboard.Button;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Phantom;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class PhantomExample extends PApplet {

	public static final int CANVAS_HEIGHT = 500;
	public static final int CANVAS_WIDTH = 500;
	Button button = new Button(this, "button").setSize(50, 50).setPosition(100,
			100);
	Button toggle = new Button(this, "toggle").setSize(50, 50).setPosition(200,
			100);
	Widget object = new Widget(this, "object").setSize(100, 100);
	Widget menuBackground = new Widget(this, "menuBackground")
			.setSize(300, 200);

	Phantom phantom = new Phantom(object, menuBackground);

	Dashboard menuPhantom = new Dashboard(this, "menuPhantom").add(phantom);
	Dashboard menu = new Dashboard(this, "menu").add(toggle)
			.setBackground(menuBackground).nudge(new PVector(-100, 50));
	Dashboard dash = new Dashboard(this, "super").add(button).add(
			object.addMenu(menuPhantom).addMenu(menu));

	// ).add(menuBackground.addMenu(menu));

	// this will run once for setup
	@Override
	public void setup() {
		System.out.println("Phantom Example");
		size(CANVAS_WIDTH, CANVAS_HEIGHT);// processing.opengl.PGraphics3D);
		// dash.add(button).add(menuBackground);
		object.getCenter();
	}

	// this is the looping function
	@Override
	public void draw() {
		background(200);// redraw the background
		menu.setMovable(toggle.isActive());
		if (button.isActive()) {
			dash.show();
		}
		dash.update();// render the hand!
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present", "PhantomExample" });
	}

	public void pickup(final Widget widget) {
		System.out.println("Picked up: " + widget);
	}

	@Override
	public void keyPressed() {
		// Widget.phantom(object, menuBackground)
		phantom.draw();
	}
}
