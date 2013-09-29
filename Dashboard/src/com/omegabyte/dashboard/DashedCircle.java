package com.omegabyte.dashboard;

import processing.core.PApplet;

public class DashedCircle extends Widget {
	float dashWidth = 6;
	float dashSpacing = 4;

	public DashedCircle(PApplet parent) {
		this.parent = parent;
	}

	public void setRadius(float rad) {
		size.set(rad, 0);
		this.setShape(Shape.circle);
	}

	@Override
	protected void drawShape() {
		parent.pushMatrix();
		parent.pushStyle();
		parent.translate(position.x, position.y);
		parent.fill(getColor());
		createCircle();
		parent.popStyle();
		parent.popMatrix();
	}

	public void createCircle() {
		int steps = 200;
		float dashPeriod = dashWidth + dashSpacing;
		boolean lastDashed = false;
		for (int i = 0; i < steps; i++) {
			boolean curDashed = (i % dashPeriod) < dashWidth;
			if (curDashed && !lastDashed) {
				parent.beginShape();
			}
			if (!curDashed && lastDashed) {
				parent.endShape();
			}
			if (curDashed) {
				float theta = PApplet.map(i, 0, steps, 0, PApplet.TWO_PI);
				parent.vertex(PApplet.cos(theta) * size.mag(),
						PApplet.sin(theta) * size.mag());
			}
			lastDashed = curDashed;
		}
		if (lastDashed) {
			parent.endShape();
		}
	}
}