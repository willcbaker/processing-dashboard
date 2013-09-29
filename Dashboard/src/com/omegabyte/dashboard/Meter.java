package com.omegabyte.dashboard;

import processing.core.PApplet;

public class Meter extends Widget {
	// float radO = 100;
	float radI = 80;
	float thick = 20;
	float start = PApplet.radians(210);
	float end = PApplet.TWO_PI;
	int ticks = 8;
	float value = ticks;
	float minVal = 0;
	float maxVal = 100;
	float spacing = PApplet.QUARTER_PI / 4;
	float slice = PApplet.QUARTER_PI / 2;
	int alpha = 175;
	public int simulating = 0;

	public Meter(final PApplet app) {

		parent = app;
		setShape(Shape.circle);
	}

	@Override
	public float resize(final float scale) {
		// radO =
		super.resize(scale);
		// System.out.println(size.mag());
		radI = size.mag() - thick;
		return size.mag();
	}

	@Override
	public Meter setSize(final float rad) {
		super.setSize(rad, 0);
		// radO = rad;
		radI = size.mag() - thick;
		return this;
	}

	void simulate() {
		if ((parent.frameCount % 10) == 0) {
			final int newv = (int) (PApplet.constrain(parent.random(-10, 10),
					-10, 10));
			set(value + newv);// value=constrain(value+newv,1,ticks);
		}
	}

	void set(final float val) {
		value = PApplet.constrain(val, minVal, maxVal);
	}

	@Override
	public void draw() {
		if (simulating > 0)
			simulate();
		parent.noStroke();
		parent.pushMatrix();
		parent.translate(getPosition().x + size.x, getPosition().y + size.y);
		parent.fill(125, 100);
		parent.ellipse(0, 0, 40, 40);
		parent.pushMatrix();
		parent.rotate(-start);
		for (int it = 0; it < PApplet.map(value, minVal, maxVal, 0, ticks); it++) {
			final int red = (int) (PApplet.map(it, 0, ticks, 150, 255));
			final int green = (int) (PApplet.map(it, 0, ticks, 175, 0));
			parent.fill(red, green, 0, alpha);// map(it,0,ticks,0xF0F0B000,0xF0FF0000));
			final int steps = 50;
			parent.beginShape();
			parent.vertex(PApplet.cos(0) * radI, PApplet.sin(0) * radI);
			for (int i = 0; i < steps; i++) {

				final float theta = PApplet.map(i, 0, steps, 0, slice);
				parent.vertex(PApplet.cos(theta) * size.mag(),
						PApplet.sin(theta) * size.mag());
			}
			parent.vertex(PApplet.cos(slice) * radI, PApplet.sin(slice) * radI);
			for (int i = 0; i < steps; i++) {

				final float theta = PApplet.map(i, steps, 0, 0, slice);
				parent.vertex(PApplet.cos(theta) * radI, PApplet.sin(theta)
						* radI);
			}
			parent.endShape();
			parent.rotate(slice + spacing);
		}
		parent.popMatrix();
		parent.textAlign(PApplet.CENTER);
		parent.textSize(26);
		parent.text((int) (value), 0, 7);
		parent.popMatrix();
	}
}
