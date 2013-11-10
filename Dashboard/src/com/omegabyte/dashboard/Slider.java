package com.omegabyte.dashboard;

import java.io.Serializable;

import processing.core.PApplet;

public class Slider extends Widget implements Cloneable, Serializable {
	private float value;
	private boolean horizontal = false;
	private final boolean hidden = false;
	private boolean changing = false;
	private float max;
	private float min;

	public Slider(final PApplet app) {
		super(app);
		max = 100;
		min = 0;
	}

	public Slider(PApplet app, String name) {
		super(app, name);
		max = 100;
		min = 0;
	}

	@Override
	public Slider setSize(final float w, final float h) {
		size.x = w;
		size.y = h;
		if (w > h) {
			horizontal = true;
		}
		value = (max - min) / 2;
		return this;
	}

	void setRange(final float low, final float high) {
		min = low;
		max = high;
	}

	boolean isChanging() {
		return changing;
	}

	@Override
	public void draw() {
		if (!hidden) {
			isMove(parent.mouseX, parent.mouseY);
			parent.rectMode(PApplet.CENTER);
			if (horizontal) {
				parent.stroke(255);
				parent.fill(0);
				parent.rect(position.x, position.y, size.x + 10, size.y);
				parent.noStroke();
				parent.fill(0xffffff00);
				parent.rect(position.x, position.y, size.x - 10, 3);
				parent.stroke(0);
				parent.fill(125);
				parent.rect(position.x - size.x / 2 + value, position.y, 8,
						size.y);
			} else {
				parent.stroke(255);
				parent.fill(0);
				parent.rect(position.x, position.y, size.x, size.y + 10);
				parent.noStroke();
				parent.fill(0xffffff00);
				parent.rect(position.x, position.y, 3, size.y);
				parent.stroke(0);
				parent.fill(125);
				parent.rect(position.x, position.y - size.y / 2 + value,
						size.x, 8);
			}
			parent.rectMode(PApplet.CORNERS);
		}
	}

	void isMove(final float x, final float y) {
		if (horizontal) {
			// println(mouseX + " " + (posX+x-width/2+value+4));
			if (parent.mouseX < position.x + x - size.x / 2 + value + 4
					&& parent.mouseX > position.x + x - size.x / 2 + value - 4
					&& parent.mouseY < position.y + y + size.y / 2
					&& parent.mouseY > position.y + y - size.y / 2
					&& parent.mousePressed)
				changing = true;
			if (changing && parent.mousePressed)
				setValue(parent.mouseX - (position.x + x) + size.x / 2);
			else
				changing = false;
		} else {
			if (parent.mouseX < position.x + x + size.x / 2
					&& parent.mouseX > position.x + x - size.x / 2
					&& parent.mouseY < position.y + y - size.y / 2 + value + 4
					&& parent.mouseY > position.y + y - size.y / 2 + value - 4
					&& parent.mousePressed)
				changing = true;
			if (changing && parent.mousePressed)
				setValue(parent.mouseY - (position.x + y) + size.y / 2);

			else
				changing = false;
		}
	}

	void setValue(final float val) {
		value = PApplet.constrain(val, min, max);
	}

	float getValue() {
		return value;
	}
}