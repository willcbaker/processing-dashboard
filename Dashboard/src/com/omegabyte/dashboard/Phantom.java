package com.omegabyte.dashboard;

import java.io.Serializable;

import processing.core.PVector;

public class Phantom extends Widget implements Cloneable, Serializable {

	private final Widget one;
	private Widget two;

	// TODO: Make this a custom shape (trapezoid?)

	public Phantom(final Widget one, final Widget two) {
		super(one.getParent(), "phantom_" + one.getName() + "_" + two.getName());
		this.one = one;
		this.two = two;
		this.setFixed(true);
		this.setEmpty(true);
		this.setBackground(true);
		this.setAlpha(0);
		// this.setAlpha(50);
		// this.setColor(0xFFFFFF00);
	}

	public Phantom(final Widget one) {
		super(one.getParent(), "phantom_" + one.getName());
		setPosition(one.getPosition());
		this.one = one;
		this.setFixed(true);
		this.setBackground(true);
		this.setAlpha(0);
	}

	@Override
	public void draw() {
		setShowing(true);
		if (two != null) {
			final PVector line = one.getCenter().get();
			line.sub(two.getCenter());

			final PVector center = PVector.add(one.getCenter(),
					PVector.div(line, -2));

			this.setSize(line.mag(), line.mag());
			this.setCenter(center.x, center.y);
			// orientation = line.heading();

		}
		parent.pushMatrix();
		parent.translate(position.x, position.y);
		parent.rotate(-orientation);
		super.drawShape();
		parent.popMatrix();
	};

	@Override
	public void rotate(final float value) {
		orientation += value;
	};
}
