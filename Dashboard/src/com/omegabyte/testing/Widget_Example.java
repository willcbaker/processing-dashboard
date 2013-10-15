package com.omegabyte.testing;

import processing.core.PApplet;

import com.omegabyte.animations.Snap;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class Widget_Example extends PApplet {

	Widget widget = new Widget(this, "widget");// create the widget object
	Snap animation = new Snap(widget);// create the Snap Animation object

	// this will run once for setup
	@Override
	public void setup() {
		size(500, 500);// processing.opengl.PGraphics3D);
		widget.setSize(50, 50);// set the size of the widget
		widget.setPosition(100, 100);// set the position of the widget
	}

	// this is the looping function
	@Override
	public void draw() {
		background(200);// redraw the background
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present",
				"com.example.Animation_Example" });
	}
}