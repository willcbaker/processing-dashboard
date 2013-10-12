package com.omegabyte.animations;

import java.util.HashMap;

import processing.core.PVector;

import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

public class Shake extends Animation {

	private float distance;
	private float speed;

	public Shake(Widget widget) {
		super(widget);
		grid.put(widget, widget.getPosition().get());// save position
	}

	HashMap<Widget, PVector> grid = new HashMap<Widget, PVector>();

	public Shake(Dashboard dashboard) {
		super(dashboard);
		for (Widget widget : widgets) {
			grid.put(widget, widget.getPosition().get());// save positions
		}
	}

	public Shake setSnap(PVector location) {
		for (Widget widget : widgets) {
			grid.put(widget, location);// save to position
		}
		return this;
	}

	public Shake setSnap(Widget widget, PVector location) {
		grid.put(widget, location);// save to position
		return this;
	}

	public float getSpeed() {
		return speed;
	}

	public Shake setSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public float getDistance() {
		return distance;
	}

	public Shake setDistance(float distance) {
		this.distance = distance;
		return this;
	}

}
