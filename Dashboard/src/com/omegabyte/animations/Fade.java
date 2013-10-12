package com.omegabyte.animations;

import java.util.HashMap;

import processing.core.PApplet;

import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

public class Fade extends Animation {

	public static final int IN = 1;
	public static final int OUT = -1;

	float alphaCopy;
	int direction;

	public Fade(Widget widget) {
		super(widget);
		alpha.put(widget, widget.getAlpha());// save position
		setName(widget.getName());
	}

	HashMap<Widget, Float> alpha = new HashMap<Widget, Float>();

	public Fade(Dashboard dashboard) {
		super(dashboard);
		for (Widget widget : widgets) {
			alpha.put(widget, widget.getAlpha());// save positions
		}
		setName(dashboard.getName());
	}

	public Fade setAlpha(Float value) {
		for (Widget widget : widgets) {
			alpha.put(widget, value);// save to position
		}
		return this;
	}

	public Fade setAlpha(Widget widget, Float value) {
		alpha.put(widget, value);// save to position
		return this;
	}

	public void setFade(int INorOUT) {
		direction = INorOUT;
		setName("Fade_" + ((direction == IN) ? "IN" : "OUT") + "_"
				+ this.getName());
	}

	@Override
	public void run() {
		// System.out.println("Fading " + ((direction == IN) ? "IN" : "OUT"));
		long waited = 1;
		if (direction == OUT)
			waited = _duration - 1;
		else
			for (Widget widget : widgets) {
				widget.setAlpha(0);
				widget.setHidden(false);
			}
		while (waited < _duration && waited > 0) {
			for (Widget widget : widgets) {
				widget.setAlpha(PApplet.map(waited, 0, _duration, 0,
						alpha.get(widget)));
				// System.out.println("Waited: " + waited + ", alpha: "
				// + widget.getAlpha());
			}
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
				waited += _wait * direction;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		for (Widget widget : widgets) {
			if (direction == IN)
				widget.setAlpha(alpha.get(widget));
			else {
				widget.setHidden(true);
				widget.setAlpha(alpha.get(widget));
			}

		}
	}
}
