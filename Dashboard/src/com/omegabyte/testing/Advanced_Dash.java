package com.omegabyte.testing;

import processing.core.PApplet;
import processing.core.PVector;

import com.omegabyte.dashboard.Button;
import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Phantom;
import com.omegabyte.dashboard.Slider;
import com.omegabyte.dashboard.Widget;

//this IS actually needed

@SuppressWarnings({ "serial" })
public class Advanced_Dash extends PApplet {

	public static final int CANVAS_WIDTH = 640;
	public static final int CANVAS_HEIGHT = 360;

	@Override
	public void setup() {
		size(CANVAS_WIDTH, CANVAS_HEIGHT);

		// The file "bot1.svg" must be in the data folder
		// of the current sketch to load successfully
		// bot = loadShape("bot1.svg");

		shape.loadShape("bot1.svg").setScalable(50, 200);
		shape.resize(0.5f);
		shape.addMenu(menu.move(-100, 55));
		shape.move(new PVector(150, 75));

		phantom.setColor(0xFFFFFF00).setAlpha(100);

		// sliders.setOwner(background);
		// println(dash.toString());
		// println(menu.toString());
		// for (Widget widget : dash.getWidgets()) {
		// System.out.println(">" + widget.getName());
		// for (Dashboard menu : widget.getMenus()) {
		// System.out.println("->" + menu);
		// for (Dashboard menu2 : widget.getMenus()) {
		// System.out.println("->>" + menu2);
		// }
		// }
		// }
		System.out.println();
		dash.print();
	}

	Widget shape = new Widget(this, "shape").setPosition(0, 0)
			.setSize(240, 240).setMovable(true).setTitle("shape");

	Button toggle = new Button(this, "Toggle").setPosition(100, 100).setSize(
			100, 100);

	Dashboard dash = new Dashboard(this, "super").add(toggle).add(shape);

	Button button = new Button(this, "button").setPosition(10, 10)
			.setSize(50, 50).setColors(0xFFFF0000, 0xFF0000FF, 0xFF00FF00)
			.setText("button");

	Widget sliderBackground = new Widget(this, "slidersBackround").setSize(80,
			140);

	Widget background = new Widget(this, "menuBackground").setPosition(0, 0)
			.setSize(500, 200);
	Dashboard sliders = new Dashboard(this, "slidersDash")
			.setBackground(sliderBackground)
			// new Widget(this, "slidersBackround").setSize(80, 140))
			.add(new Slider(this).setPosition(60, 100).setSize(11, 50).lock())
			.add(new Slider(this).setPosition(40, 100).setSize(11, 50).lock())
			.add(new Slider(this).setPosition(20, 100).setSize(11, 50).lock())
			.add(new Button(this, "subButton").setPosition(20, 18)
					.setSize(40, 40)
					.setColors(0xFFFF0000, 0xFF0000FF, 0xFF00FF00))
			// .add(new Slider(this).setPosition(40, 18).setSize(50, 11).lock())
			// .add(new Slider(this).setPosition(40, 38).setSize(50, 11).lock())
			// .add(new Slider(this).setPosition(40, 58).setSize(50, 11).lock())
			.setMovable(true).showWithOwner(true).move(new PVector(400, 30));

	Phantom phantom = new Phantom(background, sliderBackground);

	Dashboard menu = new Dashboard(this, "menu").add(button).add(phantom)
			.setHidden(true)
			// .add(new Widget(this, "slidersBackround").setBackground(true)
			// .setSize(80, 140).addMenu(sliders).move(400, 30)
			// .setFixed(true))
			.setBackground(background.addMenu(sliders))
			// .lock()
			.setMovable(true).grabThroughBackground(false);

	Dashboard menuPhantom = new Dashboard(this, "menuPhantom").add(phantom);

	@Override
	public void draw() {

		if (toggle.isActive()) {
			// System.out.println("SHOW");
			menu.show();
		}

		if (!paused) {
			background(102);
			dash.update();
		}
		menu.setMovable(button.isActive());
		sliders.setMovable(((Button) sliders.getWidget("subButton")).isActive());
	}

	boolean paused = false;

	@Override
	public void keyPressed() {
		if (key == 'd') {
			return;
		}
		if (key == 'p') {
			paused = !paused;
		}

		println(dash.toString());
	}

	// A callback is referred to by call_NAME_OF_WIDGET
	public void pickup(final Widget widget) {
		println("Picked Up: " + widget.getName());
	}

	// A callback is referred to by call_NAME_OF_WIDGET
	public void drop(final Widget widget) {
		println("Dropped: " + widget.getName());
	}

	// A callback is referred to by call_NAME_OF_WIDGET
	public void call_Toggle(final Widget widget) {
		println("Toggle, show(): " + toggle.isActive());
	}

	// A callback is referred to by call_NAME_OF_WIDGET
	public void call_button(final Widget widget) {
		println("Button setMovable(" + button.isActive() + ")");
	}

	public static void main(final String args[]) {
		PApplet.main(new String[] { "--present", "com.example.Advanced_Dash" });
	}
}