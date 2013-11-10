package com.omegabyte.dashboard;

import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PVector;

public class Button extends Widget implements Cloneable, Serializable {
	boolean active = false;
	boolean stillHover = false;
	boolean highlight = true;
	int colorOn = 255;
	int colorOff = 0;
	int colorHighlight = 110;
	private boolean onRelease = true;

	public Button(final PApplet app) {
		super(app);
		// setFixed(true);
		movable = false;
		rotatable = false;
		scalable = false;
		selectable = false;
	}

	public Button(final PApplet app, final String name) {
		super(app, name);

		movable = false;
		rotatable = false;
		scalable = false;
		selectable = false;
	}

	public Button setColors(final int on, final int off, final int highlight) {
		this.highlight = true;
		colorOn = on;
		colorOff = off;
		colorHighlight = highlight;
		if (active)
			color = colorOn;
		else
			color = colorOff;
		return this;
	}

	public Button setColors(final int on, final int off) {
		this.highlight = false;
		colorOn = on;
		colorOff = off;
		if (active)
			color = colorOn;
		else
			color = colorOff;
		return this;
	}

	public boolean isActive() {
		return active;
	}

	public Button setActive(final boolean active) {
		this.active = active;
		return this;
	}

	/**
	 * The button is only "pressed if it is released while hovering" aka "oops i
	 * clicked the button but havnt let go of the mouse!" Default enabled.
	 * 
	 * @param value
	 *            if enabbled (default) the button is not pressed until you
	 *            release
	 * @return the button
	 */
	public Button setOnRelease(boolean value) {
		onRelease = value;
		return this;
	}

	@Override
	public void drop() {
		selected = false;
		if (isHover() && onRelease) {
			selected = true;
		}
	};

	@Override
	public void setHover(final PVector location) {
		super.setHover(location);
		if (location != null) {
			// we are hovering on TOP
			if ((!stillHover || (colorOn == colorOff)) && highlight)
				setColor(colorHighlight);// set highlight color
		} else
			updateColor();
	}

	private void updateColor() {
		if (active)
			setColor(colorOn);
		else
			setColor(colorOff);
	}

	@Override
	public void pickup() {
		if (!onRelease)
			selected = true;
	}

	@Override
	public boolean isHover(final PVector location) {
		// System.out.println(getName() + ".isHover(***)");
		// if (isHidden() && !isShowing())
		// return false;
		boolean hover = false;
		if (super.isHover(location)) {
			hover = true;
			if (selected) {
				active = !active;
				stillHover = true;
				selected = false;
				invokeCallback();
				updateColor();
			}

		} else {
			stillHover = false;
			updateColor();
		}
		// if (hover && (!stillHover || (colorOn == colorOff)) && highlight)
		// color = colorHighlight;
		return hover;
	}

	@Override
	public Button setAlpha(final float alpha) {
		super.setAlpha(alpha);
		return this;
	}

	@Override
	public Button setTextColor(final int colour) {
		super.setTextColor(colour);
		return this;
	}

	@Override
	public Button setFixed(final boolean fixed) {
		super.setFixed(fixed);
		return this;
	}

	@Override
	public Button setHidden(final boolean val) {
		super.setHidden(val);
		return this;
	}

	@Override
	public Button setRotatable(final boolean val) {
		super.setRotatable(val);
		return this;
	}

	@Override
	public Button setMovable(final boolean val) {
		super.setMovable(val);
		return this;
	}

	@Override
	public Button setPosition(final float x, final float y) {
		super.setPosition(x, y);
		return this;
	}

	@Override
	public Button setPosition(final PVector position) {
		super.setPosition(position);
		return this;
	}

	@Override
	public Button setEmpty(final boolean empty) {
		super.setEmpty(empty);
		return this;
	}

	@Override
	public Button setName(final String name) {
		super.setName(name);
		return this;
	}

	@Override
	public Button setSize(final PVector size) {
		super.setSize(size);
		return this;
	}

	@Override
	public Button setSize(final float x, final float y) {
		super.setSize(x, y);
		return this;
	}

	@Override
	public Button setSize(final float rad) {
		super.setSize(rad);
		return this;
	}

	@Override
	public Button setSelectable(final boolean val) {
		super.setSelectable(val);
		return this;
	}

	@Override
	public Button setShape(final Shape shape) {
		super.setShape(shape);
		return this;
	}

	@Override
	public Button setScalable(final boolean val) {
		super.setScalable(val);
		return this;
	}

	@Override
	public Button setScalable(final float min, final float max) {
		super.setScalable(min, max);
		return this;
	}

	@Override
	public Button setTitle(final String title) {
		super.setTitle(title);
		return this;
	}

	@Override
	public Button setText(final String text) {
		super.setText(text);
		return this;
	}

	@Override
	public Button setCorner(final float cornerRad) {
		super.setCorner(cornerRad);
		return this;
	}

	@Override
	public Button setMaxSize(final float maxSize) {
		super.setMaxSize(maxSize);
		return this;
	}

	@Override
	public Button setMinSize(final float minSize) {
		super.setMinSize(minSize);
		return this;
	}

	@Override
	public Button setTextSize(final float size) {
		super.setTextSize(size);
		return this;
	}

	@Override
	public Button setTitleSize(final float size) {
		super.setTitleSize(size);
		return this;
	}

	@Override
	public Button setOwner(final Dashboard dashboard) {
		super.setOwner(dashboard);
		return this;
	}
}