package com.omegabyte.testing;

import processing.core.PApplet;

import com.omegabyte.animations.Snap;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class Animation_Example extends PApplet {

	Widget widget = new Widget(this, "widget");// create the widget object
	Dashboard dashboard = new Dashboard(this, "dashboard");// create the
															// dashboard

	Snap animation = new Snap(widget);// create the Snap Animation
										// object

	// this will run once for setup
	@Override
	public void setup() {
		size(300, 300);// processing.opengl.PGraphics3D);
		widget.setSize(50, 50);// set the size of the widget
		widget.setPosition(100, 100);// set the position of the widget
		dashboard.add(widget);// add the widget to the dashboard

		animation.setSnap(widget, widget.getPosition());// set the "Snap-To"
														// position

		animation.setRecursive(true);// optional adjustment to repeat
		animation.setSpeed(10);// optional adjustment
	}

	// this is the looping function
	@Override
	public void draw() {
		background(200);// redraw the background
		dashboard.update();// don't forget to update the dashboard!
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present",
				"com.example.Animation_Example" });
	}

	// The drop callback is referred to by drop_NAME(...) or drop(...)
	public void drop_widget(Widget widget) {
		println("Dropped: " + widget.getName());
		widget.trigger(animation);// animation.start();
	}

	// The pickup callback is referred to by pickup_name(...) or pickup(...)
	public void pickup_widget(Widget widget) {
		println("Picked Up: " + widget.getName());
	}
}