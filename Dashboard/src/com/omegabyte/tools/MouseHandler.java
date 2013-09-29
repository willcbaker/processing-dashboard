package com.omegabyte.tools;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class MouseHandler {
	PVector location;
	boolean mouseToggle = false;
	boolean mouseReleased = false;
	private final PApplet parent;
	private boolean grab;

	public MouseHandler(final PApplet app) {
		parent = app;
		location = new PVector(parent.mouseX, parent.mouseY);
		parent.registerMethod("draw", this);
		parent.registerMethod("mouseEvent", this);
	}

	private void mouseDragged() {
		System.out.println("Dragged");
		mouseToggle = false;
	}

	private void mouseReleased() {
		System.out.println("Released");
		mouseReleased = true;
	}

	private void mouseMoved() {
		System.out.println("Moved");
		mouseToggle = false;
	}

	private void mousePressed() {
		System.out.println("Pressed");
		mouseToggle = true;
		mouseReleased = false;
	}

	private void mouseClicked() {
		System.out.println("Clicked");
		// mouseToggle = true;
		// mouseReleased = false;
	}

	public void draw() {
		if (mouseToggle && mouseReleased) {
			mouseToggle = false;

			System.out.println("GRAB");
			grab = true;
		} else
			grab = false;
	}

	public boolean isGrab() {
		if (grab)
			System.out.println("*GRAB*");
		return grab;
	}

	public PVector get() {
		return location;
	}

	public void mouseEvent(final MouseEvent event) {
		location.x = event.getX();
		location.y = event.getY();

		switch (event.getAction()) {
		case MouseEvent.PRESS:
			mousePressed();
			break;
		case MouseEvent.RELEASE:
			mouseReleased();
			break;
		case MouseEvent.CLICK:
			mouseClicked();
			break;
		case MouseEvent.DRAG:
			mouseDragged();
			break;
		case MouseEvent.MOVE:
			mouseMoved();
			break;
		}
	}
}
