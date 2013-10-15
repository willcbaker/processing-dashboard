package com.omegabyte.testing;

import processing.core.PApplet;

import com.omegabyte.animations.Snap;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class Animation_Advanced extends PApplet {

	Widget widget = new Widget(this, "widget");// create the widget object
	Widget widget2 = new Widget(this, "widget2");// create the second widget
	Dashboard dashboard = new Dashboard(this, "dashboard")// create the
															// dashboard

			.add(widget)// add the widget to the dashboard while creating it
			.add(widget2);// add the widget to the dashboard

	// this will add all the widgets in the dash
	// so it was important to add the widgets first.
	Snap animation = new Snap(dashboard);// create the Snap Animation
											// object

	// this will run once for setup
	@Override
	public void setup() {
		size(300, 300);// processing.opengl.PGraphics3D);
		widget.setSize(50, 50);// set the size of the widget
		widget.setPosition(100, 100);// set the position of the widget
		widget2.setSize(50, 50);// set the size of the widget
		widget2.setPosition(160, 100);// set the position of the widget

		animation.setSnap(widget, widget.getPosition());// set the "Snap-To"
														// position
		animation.setSnap(widget2, widget2.getPosition());// set the "Snap-To"
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

	// The drop callback is referred to by drop_widget
	public void drop(Widget widget) {
		println("Dropped: " + widget.getName());
		widget.trigger(animation);// animation.start();
	}

	// The pickup callback is referred to by pickup_widget
	public void pickup(Widget widget) {
		println("Picked Up: " + widget.getName());
	}
}