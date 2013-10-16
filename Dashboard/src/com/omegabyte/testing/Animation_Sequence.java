package com.omegabyte.testing;

import processing.core.PApplet;

import com.omegabyte.animations.Animation;
import com.omegabyte.animations.AnimationSequence;
import com.omegabyte.animations.Fade;
import com.omegabyte.dashboard.Button;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class Animation_Sequence extends PApplet {

	public static final int CANVAS_WIDTH = 300;
	public static final int CANVAS_HEIGHT = 300;

	Widget widget = new Widget(this, "widget");// create the widget object
	Button button = new Button(this, "button");// create the widget object
	Dashboard dashboard = new Dashboard(this, "dashboard");// create the
															// dashboard

	// this will add all the widgets in the dash
	// so it was important to add the widgets first.
	Fade fadeIn = new Fade(widget);// create the Fade Animation
	Animation wait = new Animation(2000);// time in ms
	Fade fadeOut = new Fade(widget);// create the Fade Animation
	AnimationSequence sequence = new AnimationSequence();

	// this will run once for setup
	@Override
	public void setup() {
		size(CANVAS_WIDTH, CANVAS_HEIGHT);// processing.opengl.PGraphics3D);
		widget.setSize(50, 50);// set the size of the widget
		widget.setPosition(100, 100);// set the position of the widget
		button.setSize(100, 40);// set the size of the widget
		button.setPosition(20, 20);// set the position of the widget
		dashboard.add(button);// add the button to the dashboard
		dashboard.add(widget);// add the widget to the dashboard

		fadeIn.setFade(Fade.IN);// set the "Snap-To"
								// position
		fadeIn.setDuration(1000);// optional adjustment

		fadeOut.setFade(Fade.OUT);// set the "Snap-To"
		// position
		fadeOut.setDuration(1000);// optional adjustment

		sequence.add(fadeIn);
		sequence.add(wait);
		sequence.add(fadeOut);
	}

	// this is the looping function
	@Override
	public void draw() {
		background(200);// redraw the background
		button.draw();
		dashboard.update();// don't forget to update the dashboard!
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present",
				"com.example.Animation_Example" });
	}

	// The drop callback is referred to by call_NAME
	public void call_button(final Widget widget) {
		println("Presed: " + widget.getName());
		sequence.trigger();// animation.start();
	}

	// The pickup callback is referred to by pickup_widget
	public void pickup(final Widget widget) {
		println("Picked Up: " + widget.getName());
	}
}