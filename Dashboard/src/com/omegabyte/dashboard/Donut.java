package com.omegabyte.dashboard;

import java.io.Serializable;

import processing.core.PApplet;

public class Donut extends Widget implements Cloneable, Serializable {
	float radO = 100;
	float radI = 80;
	float thick = 5;
	float spacing = PApplet.QUARTER_PI / 4;
	float start = 0;// PApplet.radians(210);
	float end = PApplet.TWO_PI;
	int ticks = 11;

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}

	float slice = PApplet.QUARTER_PI / 2;
	int alpha = 175;

	public Donut(final PApplet app) {
		super(app);
	}

	public Donut(final PApplet app, String name) {
		super(app, name);
	}

	@Override
	public float resize(final float scale) {
		radO = super.resize(scale);
		System.out.println(radO);
		radI = radO - thick;
		return radO;
	}

	public Donut setSize(final float rad) {
		super.setSize(rad, 0);
		radO = rad;
		radI = radO - thick;
		spacing = (float) ((end - start) / (ticks) - slice);
		return this;
	}

	@Override
	public void drawShape() {
		parent.noStroke();
		for (int it = 0; it < ticks; it++) {
			parent.fill(getColor(), alpha);// map(it,0,ticks,0xF0F0B000,0xF0FF0000));
			final int steps = 50;
			parent.beginShape();
			parent.vertex(PApplet.cos(0) * radI, PApplet.sin(0) * radI);
			for (int i = 0; i < steps; i++) {

				final float theta = PApplet.map(i, 0, steps, 0, slice);
				parent.vertex(PApplet.cos(theta) * radO, PApplet.sin(theta)
						* radO);
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
	}
}
