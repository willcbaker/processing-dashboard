package com.omegabyte.dashboard;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Dashboard {
	PApplet parent = null;
	ArrayList<Widget> widgets = new ArrayList<Widget>();
	Widget moving = null;

	public Widget getMoving() {
		return moving;
	}

	Widget empty = null;
	Widget background = null;
	private Widget owner = null;
	private boolean movable = false;
	PVector grabbed = new PVector(0, 0);
	private boolean hidden = false;
	private String name;

	private boolean showing;

	public Dashboard(final PApplet app) {
		parent = app;
		empty = new Widget(parent, "EMPTY").setHidden(true).setMovable(false)
				.setTitle("EMPTY").setEmpty(true);
		moving = empty;// prevents moving objects until selected
		background = empty;
		owner = empty;
	}

	public Dashboard(PApplet parent, String string) {
		this(parent);
		setName(string);

	}

	public Dashboard add(final Widget widget) {
		widgets.add(widget);
		widget.setOwner(this);
		return this;
	}

	public Dashboard bringToFront(final Widget widget) {
		if (widgets.contains(widget) && !widget.isFixed())
			widgets.add(widgets.remove(widgets.indexOf(widget)));
		return this;
	}

	public void draw() {
		// if (getName() == "menu")
		// System.out.println("drawing: " + getName() + " " + !hidden + " "
		// + showing);
		if (hidden && !showing)
			return;
		if (background != empty)
			background.draw();
		for (final Widget widget : widgets) {
			// if (getName() == "menu")
			// if (widget.getName() != null)
			// System.out.println("drawing: " + widget.getName());
			if (widget != background)
				widget.draw();
		}
	}

	void drop() {
		if (moving != null) {
			moving.drop();// selected = false;
			moving = null;
			// System.out.println("DROPPED.");// Picked up: " +
			// moving.getTitle());
		}
	}

	public Widget getBackground() {
		if (background == null)
			return new Widget(parent).setEmpty(true);
		return background;
	}

	public String getName() {
		return name;
	}

	public Widget getOwner() {
		if (owner == null)
			owner = empty;
		return owner;
	}

	public PApplet getParent() {
		return parent;
	}

	public Widget getWidget(final String name) {
		for (final Widget widget : widgets) {
			if (widget.getName() == name)
				return widget;
		}
		return null;
	}

	public ArrayList<Widget> getWidgets() {
		return widgets;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isMovable() {
		return movable;
	}

	public boolean isShowing() {
		return showing;
	}

	public Dashboard lock() {
		for (final Widget widget : widgets) {
			widget.lock();
		}
		return this;
	}

	public Dashboard move(final float scale) {
		for (final Widget widget : widgets) {
			widget.getPosition().mult(scale);
		}
		return this;
	}

	public Dashboard move(final float x, final float y) {
		return move(new PVector(x, y));
	}

	// grabbedAt
	// center
	//
	public Dashboard move(final PVector vec) {
		final PVector temp = background.getPosition();
		// background.setPosition(vec);
		background.setPosition(PVector.add(vec, grabbed));//
		// PVector.add(vec,grabbed));
		for (final Widget widget : widgets) {
			if (widget != background) {
				widget.setPosition(PVector.add(vec,
						PVector.sub(widget.getPosition(), temp)));
			}
		}
		return this;
	}

	public Dashboard nudge(final PVector vec) {
		for (final Widget widget : widgets) {
			widget.setPosition(PVector.add(widget.getPosition(), vec));
		}
		return this;
	}

	void pickup(final Widget widget, final PVector grab) {
		// if (widget.getTitle() == "EMPTY")
		// return;
		if (moving == null) {
			if (widget.isBackground()) {
				if (!widget.getOwner().isMovable()) {
					return;
				}
			}
			widget.pickup();// selected = true;
			grabbed = PVector.sub(widget.position, grab);
			moving = widget;
			// System.out.println(widgets.size() + "Picked up: "
			// + widget.getTitle() + " at " + grab + " " + " relative "
			// + grabbed + " " + widget.position);
		}
	}

	public Dashboard resize(final float scale) {
		for (final Widget widget : widgets) {
			widget.getSize().mult(scale);
			if (widget.getTexture() != null) {
				widget.setImage(parent.loadImage(widget.textureFile));
				final float tempW = widget.getTexture().width;
				final float tempH = widget.getTexture().height;
				final float ratio = tempW / tempH;
				widget.getTexture().resize((int) (widget.getSize().x),
						(int) (widget.getSize().x / ratio));
			}
		}
		return this;
	}

	public Dashboard scale(final float scale) {
		for (final Widget widget : widgets) {
			widget.getSize().mult(scale);
			widget.getPosition().mult(scale);
			if (widget.getTexture() != null) {
				widget.setImage(parent.loadImage(widget.textureFile));
				final float tempW = widget.getTexture().width;
				final float tempH = widget.getTexture().height;
				final float ratio = tempW / tempH;
				widget.getTexture().resize((int) (widget.getSize().x),
						(int) (widget.getSize().x / ratio));
			}
		}
		return this;
	}

	public Dashboard setBackground(final Widget widget) {
		background = widget;
		widget.setOwner(this);
		widget.setBackground(true);
		// UPDATE THIS TO A DEQUE in later revisions
		final ArrayList<Widget> copy = new ArrayList<Widget>();
		copy.addAll(widgets);
		widgets.clear();
		widgets.add(background);
		widgets.addAll(copy);
		return this;
	}

	public void setHidden(final boolean b) {
		hidden = b;
	}

	public Dashboard setMovable(final boolean value) {
		movable = value;
		if (movable)
			background.setSelectable(true).setFixed(false);
		return this;
	}

	public Dashboard setName(final String name) {
		this.name = name;
		return this;
	}

	void setOwner(final Widget widget) {
		owner = widget;
	}

	public Dashboard setShowing(final boolean showing) {
		// System.out.println("showing_" + getName() + ": " + showing);
		this.showing = showing;
		return this;
	}

	public void show() {
		setShowing(true);
		for (final Widget widget : widgets) {
			widget.setShowing(true);
		}
	}

	@Override
	public String toString() {
		String widgs = "Dash size: " + widgets.size() + ","
				+ (movable ? "moveable," : "") + "\n";
		for (final Widget widget : widgets) {
			widgs += widget.toString();
			widgs += "\n";
		}
		return widgs;
	}

	public void update() {
		update(parent.mouseX, parent.mouseY, parent.mousePressed, false, 1,
				false, 0);
	}

	/**
	 * Deprecated
	 */
	public Handler update(final float x, final float y, final boolean grab,
			final boolean scaling, final float scale, final boolean rotating,
			final float rotation) {
		return update(new PVector(x, y), grab, scaling, scale, rotating,
				rotation);

	}

	public void update(final PVector location, final boolean grab) {
		update(location, grab, false, 1, false, 0);
	}

	public Handler update(final PVector location, final boolean grab,
			final boolean scaling, final float scale, final boolean rotating,
			final float rotation) {
		final Handler handler = new Handler(empty);
		for (final Widget widget : this.widgets)
			for (final Dashboard menu : widget.getMenus()) {
				handler.handle((menu.updateDash(location, grab, scaling, scale,
						rotating, rotation)));
			}
		if (handler.hasBackground() || handler.isEmpty())
			handler.handle(this.updateDash(location, grab, scaling, scale,
					rotating, rotation));
		pickup(handler.moving, location);
		// System.out.println(handler);
		if (moving != null) {
			if (moving != empty && !moving.isBackground()) {
				if (!moving.isMovable()) {
					// while (moving.getOwner().getOwner() != null)
					// moving = moving.getOwner().getOwner();
				} else {
					moving.move(PVector.add(location, grabbed));
					bringToFront(moving);
				}
			}
			if (moving.isBackground() && moving.getOwner().isMovable()) {
				moving.getOwner().move(PVector.add(location, grabbed));
			}
		}
		if (handler.scale != empty)
			handler.scale.resize(scale);
		if (handler.rotate != empty)
			handler.rotate.rotate(rotation);
		if (!grab)
			drop();
		return handler;
	}

	public Handler updateDash(final PVector location, final boolean grab,
			final boolean scaling, final float scale, final boolean rotating,
			final float rotation) {

		// if (grab)
		// System.out.println("Grabbed");
		Widget mover = empty;
		Widget scaler = empty;
		Widget rotater = empty;
		Widget hovering = empty;

		for (final Widget widget : widgets) {
			if (widget.isHover(location)) {
				// if (widget.isBackground()){
				// System.out.println("IN_update_" + getName() + ": "
				// + widget.hover + ", hidden: "
				// + !widget.getOwner().isHidden() + ", " + !hidden + ", "
				// + (!getOwner().isHidden() && !hidden));
				hovering = widget;

				if (widget.isShowing())
					widget.setShowing(false);
			}
			widget.setHover(null);
			if (grab) {
				mover = hovering;
			} else {
				if (scaling)
					scaler = hovering;
				else if (rotating)
					rotater = hovering;
			}
		}
		if (isShowing())
			setShowing(false);
		if (!hovering.isEmpty())
			hovering.setHover(location);
		return (new Handler(mover, scaler, rotater));
	}
}

class Handler {
	Widget moving;// = empty;
	Widget scale;// = empty;
	Widget rotate;// = empty;
	Widget hover;// = empty;
	Widget empty;

	Handler(final Widget empty) {
		this.empty = empty;
		moving = empty;
		scale = empty;
		rotate = empty;
		hover = empty;
	}

	Handler(final Widget moving, final Widget scale, final Widget rotate) {
		this.moving = moving;
		this.scale = scale;
		this.rotate = rotate;
		this.hover = rotate;
	}

	public boolean handle(final Handler incoming) {
		if (empty == null) {
			System.out.println("Error, Empty-null");
			return false;
		}
		// System.out.println("\n **Got: \n" + incoming);
		if (!incoming.moving.isEmpty()
				|| (this.moving.isEmpty() && incoming.moving.isBackground()))
			moving = incoming.moving;
		if (!incoming.scale.isEmpty()
				|| (this.scale.isEmpty() && incoming.scale.isBackground()))
			scale = incoming.scale;
		if (!incoming.rotate.isEmpty())
			rotate = incoming.rotate;
		if (incoming.hover.isEmpty())
			hover = incoming.hover;

		// System.out.println("\n ***Output: \n" + this);
		return true;
	}

	public boolean hasBackground() {
		return moving.isBackground() || scale.isBackground()
				|| rotate.isBackground();
	}

	public boolean isEmpty() {
		return moving.isEmpty() && scale.isEmpty() && rotate.isEmpty();
	}

	@Override
	public String toString() {
		return "Handler [moving=" + moving.getName() + ", scale="
				+ scale.getName() + ", rotate=" + rotate.getName() + ", empty="
				+ (empty == null ? "NULL" : empty.getName()) + "]";
	}
}