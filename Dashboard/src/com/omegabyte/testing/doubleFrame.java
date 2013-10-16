package com.omegabyte.testing;

import java.util.ArrayList;

import javax.swing.JFrame;

import processing.core.PApplet;

import com.omegabyte.dashboard.Button;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;
import com.omegabyte.tools.NewWindow;

@SuppressWarnings({ "serial" })
public class doubleFrame extends PApplet {

	Button left = new Button(this, "left").setSize(50, 50)
			.setPosition(100, 100);
	Button right = new Button(this, "right").setSize(50, 50).setPosition(200,
			100);
	Dashboard dash = new Dashboard(this, "super").add(left).add(right);

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

		dash.update();// update

		left.setActive(f.isOpen());
		right.setActive(g.isOpen());
	}

	public void call_left(final Widget widget) {
		if (f.isOpen())
			f.close();
		else
			f.setPosition(mouseX + 50, mouseY + 50).open();
	}

	public void call_right(final Widget widget) {
		if (g.isOpen())
			g.close();
		else
			g.setPosition(mouseX + 50, mouseY + 50).open();
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

	ArrayList<NewWindow> j = new ArrayList<NewWindow>();
	NewWindow f = new NewWindow(new PhantomExample(), "phatomExample").setSize(
			PhantomExample.CANVAS_WIDTH, PhantomExample.CANVAS_HEIGHT);
	NewWindow g = new NewWindow(new Widget_Example(), "WidgetExample").setSize(
			Widget_Example.CANVAS_WIDTH, Widget_Example.CANVAS_HEIGHT);

}
