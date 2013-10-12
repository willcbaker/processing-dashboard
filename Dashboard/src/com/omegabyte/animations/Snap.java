package com.omegabyte.animations;

import java.util.HashMap;

import processing.core.PVector;

import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

public class Snap extends Animation {

	private float snapSpeed = 10;

	public Snap(Widget widget) {
		super(widget);
		grid.put(widget, widget.getPosition().get());// save position
		this.start();
	}

	HashMap<Widget, PVector> grid = new HashMap<Widget, PVector>();

	public Snap(Dashboard dashboard) {
		super(dashboard);
		for (Widget widget : widgets) {
			grid.put(widget, widget.getPosition().get());// save positions
		}
		setName("snap_" + dashboard.getName());
		this.start();
	}

	public Snap setSnap(PVector location) {
		for (Widget widget : widgets) {
			grid.put(widget, location.get());// save to position
		}
		return this;
	}

	public Snap setSnap(Widget widget, PVector location) {
		grid.put(widget, location.get());// save to position
		return this;
	}

	public Snap setSnap() {
		for (Widget widget : widgets)
			grid.put(widget, widget.getPosition());// save to position
		return this;
	}

	public float getSpeed() {
		return snapSpeed;
	}

	public Snap setSpeed(float speed) {
		this.snapSpeed = speed;
		return this;
	}

	@Override
	public void run() {
		do {
			while (!running) {

				// Ok, let's wait for however long we should wait
				try {
					sleep((long) (1000 / widgets.get(0).getParent().frameRate));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			// System.out.println("Run commanded");
			boolean done = false;
			while (!done) {
				done = true;
				for (Widget widget : widgets) {
					final PVector direction = PVector.sub(widget.getPosition(),
							grid.get(widget));
					float speed = PVector.dist(widget.getPosition(),
							grid.get(widget))
							* snapSpeed / 100;
					if (speed < 5)
						speed = 5;
					direction.setMag(speed);
					direction.mult(-1);
					// System.out.println("old: " + grid.get(widget));
					// System.out.println("pos: " + widget.getPosition());
					// System.out.println("dir: " + direction);
					if (PVector.dist(widget.getPosition(), grid.get(widget)) > PVector
							.dist(PVector.add(widget.getPosition(), direction),
									grid.get(widget))) {
						done = false;
						widget.move(PVector.add(widget.getPosition(), direction));
					} else if (widget.getPosition() != grid.get(widget)) {
						widget.setPosition(grid.get(widget).get());
					}

				}
				// Ok, let's wait for however long we should wait
				try {
					sleep((long) (1000 / widgets.get(0).getParent().frameRate));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			running = false;
		} while (isRecursive());
	}
}
