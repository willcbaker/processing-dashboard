package com.omegabyte.dashboard;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Dashboard {
	private String name;
	private PApplet parent = null;
	private PVector grabbed = new PVector(0, 0);
	private final Handler handle;

	private boolean hidden = false;
	private boolean showing;
	private boolean hasHover;
	private boolean movable = false;
	private boolean grabThroughBackground = false;
	private boolean showWithOwner = false;

	ArrayList<Widget> widgets = new ArrayList<Widget>();
	private Widget empty = null;// set Me!
	private Widget moving = empty;
	private Widget background = empty;
	private Widget owner = empty;
	private Widget interacting = null;
	private boolean fixed = false;

	// TODO:remove moving, for favor of interacting

	public Dashboard(final PApplet app) {
		parent = app;
		empty = new Widget(parent, "EMPTY").setHidden(true).setMovable(false)
				.setTitle("EMPTY").setEmpty(true).setOwner(this);
		moving = empty;// prevents moving objects until selected
		background = empty;
		owner = empty;
		handle = new Handler(empty);
	}

	public Dashboard(final PApplet parent, final String string) {
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

	protected void draw() {
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

	void dropNew() {
		if (interacting != null) {
			// System.out.println("Dropping " + interacting.getName());
			interacting.drop();// selected = false;
			interacting = null;
			// System.out.println("DROPPED.");// Picked up: " +
			// moving.getTitle());
		}
	}

	public Widget getBackground() {
		if (background == null)
			return new Widget(parent).setEmpty(true);
		return background;
	}

	public Widget getMoving() {
		return moving;
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

	public void grab(final PVector grabLocation) {
		// System.out.println("Grabbed: " + grabLocation);
		grabbed = grabLocation;
	}

	/**
	 * Enable/Disable the ability to grab through the background.
	 * 
	 * If enabled, you can act on widgets below a background.
	 * 
	 * @param grabThroughBackground
	 *            default enabled
	 */
	public Dashboard grabThroughBackground(final boolean grabThroughBackground) {
		this.grabThroughBackground = grabThroughBackground;
		return this;
	}

	// TODO: fix hasHover and implement so we can use phantoms again!
	public boolean hasHover() {
		return hasHover;
	}

	public void hasHover(final boolean setValue) {
		hasHover = setValue;
		if (hasHover && !getOwner().isEmpty()) {
			System.out.println(getOwner().getName() + ".hasHover(" + getName()
					+ ")");
			getOwner().hasHover(true);
		}

	}

	public boolean isFixed() {
		return fixed;
	}

	public boolean isGrabThroughBackground() {
		return grabThroughBackground;
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

	public boolean isShowWithOwner() {
		return showWithOwner;
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
		if (fixed || vec.mag() == 0)
			return this;
		for (final Widget widget : widgets) {
			widget.getPosition().add(vec);
			for (final Dashboard menu : widget.getMenus()) {
				menu.move(vec);
			}
		}
		return this;
	}

	public Dashboard nudge(final PVector vec) {
		for (final Widget widget : widgets) {
			widget.getPosition().add(vec);
			// System.out.println("Nudge " + widget.getName() + " to "
			// + widget.getPosition());
		}
		return this;
	}

	void pickup(final Widget widget) {
		// if (widget.getTitle() == "EMPTY")
		// return;
		if (moving == null) {
			if (widget.isBackground()) {
				if (!widget.getOwner().isMovable()) {
					return;
				}
			}
			if (!widget.isEmpty()) {
				widget.pickup();// selected = true;
				// grab(PVector.sub(widget.position, grab));
				moving = widget;
				// System.out.println(widgets.size() + "Picked up: "
				// + widget.getName() + " at " + grab + " " + " relative "
				// + grabbed + " " + widget.position);
			}
		}
	}

	// TODO: replace NEW with OLD
	void pickupNew(final Widget widget, final PVector location) {
		if (interacting == null) {
			// System.out.println("Picking up " + widget.getName());
			// if (!widget.isEmpty()) {
			grab(location);
			widget.pickup();
			interacting = widget;
			// }
		}
	}

	public void print() {
		StringBuilder string = new StringBuilder();
		string.append(" +++" + getName() + "\n");// 0th level
		for (Widget widget : getWidgets()) {
			string.append("  |---" + widget.getName() + "\n");
			for (Dashboard menu : widget.getMenus()) {
				menu.print_rest(2, string);// 1st level
			}
		}
		System.out.println(string);
	}

	private void print_rest(int level, StringBuilder string) {

		if (getOwner() != null && getOwner().getOwner() != null) {
			for (int i = 0; i < level; i++) {
				string.append("  ");
			}
		}
		string.append("|+++" + getName() + "\n");// next level
		for (Widget widget : getWidgets()) {
			if (getOwner() != null && getOwner().getOwner() != null) {
				for (int i = 0; i < level + 1; i++) {
					string.append("  ");
				}
			}
			string.append("|---" + widget.getName() + "\n");
			for (Dashboard menu : widget.getMenus()) {
				menu.print_rest(level + 1, string);// next level
			}
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

	public void setFixed(final boolean fixed) {
		this.fixed = fixed;
	}

	public void setHidden(final boolean b) {
		hidden = b;
	}

	public Dashboard setMovable(final boolean value) {
		movable = value;
		background.setSelectable(value).setFixed(!value);
		return this;
	}

	public Dashboard setName(final String name) {
		this.name = name;
		return this;
	}

	public void setOwner(final Widget widget) {
		// TODO: check for endless loops?
		owner = widget;
	}

	public Dashboard setShowing(final boolean showing) {
		// if (getName() == "menu")
		// System.out.println("showing_" + getName() + ": " + showing);

		this.showing = showing;
		for (final Widget widget : widgets) {
			widget.setShowing(showing, showing);
		}
		return this;
	}

	public void show() {
		showing = true;
		for (final Widget widget : widgets) {
			widget.setShowing(true, true);
		}
	}

	public Dashboard showWithOwner(final boolean value) {
		showWithOwner = value;
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder string = new StringBuilder();
		Dashboard tempOwn = getOwner().getOwner();
		int count = 0;
		while (tempOwn != null && !tempOwn.getOwner().isEmpty()) {
			string.append(">");
			count++;
			System.out.println("Owner: " + tempOwn.getName());
			tempOwn = tempOwn.getOwner().getOwner();
		}

		string.append("Dashboard_" + getName() + " size: " + widgets.size()
				+ "," + (movable ? "moveable," : "")
				+ (hidden ? "hidden," : "") + (hasHover ? "hasHover," : "")
				+ (showing ? "showing," : "")
				+ (grabThroughBackground ? "grabThroughBackground," : "")
				+ (showWithOwner ? "showWithOwner," : "") + "\n");
		for (final Widget widget : widgets) {
			for (int i = 0; i < count + 2; i++) {
				string.append(">");
			}
			string.append(widget.toString());
		}
		System.out.println();
		return string.toString();
	}

	// private void print_rest(int level, StringBuilder string) {
	// // TODO: create a full branch structure
	// Iterator<Widget> wdg = widgets.iterator();
	// while (wdg.hasNext()) {
	// Widget element = wdg.next();
	// // Iterator<Dashboard> mnu = element.getMenus().iterator();
	// System.out.print(element + " ");
	// }
	//
	// if (getOwner() != null && getOwner().getOwner() != null) {
	// for (int i = 0; i < level; i++) {
	// string.append("  ");
	// }
	// }
	// string.append("|+++" + getName() + "\n");// next level
	// for (Widget widget : getWidgets()) {
	// if (getOwner() != null && getOwner().getOwner() != null) {
	// for (int i = 0; i < level + 1; i++) {
	// string.append("  ");
	// }
	// }
	// string.append("|---" + widget.getName() + "\n");
	// for (Dashboard menu : widget.getMenus()) {
	// menu.print_rest(level + 2, string);// next level
	// }
	// }
	//
	// }

	/**
	 * This function will update and draw the dashboard. This is only to be
	 * called from Super, untested for lower dashboards. This will show the
	 * widgets in this dash
	 */
	public void update() {

		final PVector location = new PVector(parent.mouseX, parent.mouseY);
		final boolean grab = parent.mousePressed;
		final boolean rotate = false;
		final boolean scale = false;
		update(location, grab, rotate, 0, scale, 1.0f);
	}

	// private void getShowing() {
	// System.out.println(getName() + " showing: ");
	// for (Widget widget : getWidgets()) {
	// if (widget.isShowing()) {
	// System.out.println("   " + widget.getName());
	// }
	// for (Dashboard menu : widget.getMenus()) {
	// menu.getShowing();
	// }
	// }
	// }

	/**
	 * This function will update and draw the dashboard. This is only to be
	 * called from Super, untested for lower dashboards. This will show the
	 * widgets in this dash
	 * 
	 * @param location
	 * @param grab
	 * @param rotating
	 * @param rotation
	 * @param scaling
	 * @param scale
	 */
	public void update(PVector location, boolean grab, boolean rotating,
			float rotation, boolean scaling, float scale) {

		for (Widget widget : widgets) {// hope this is super
			if (!widget.isHidden())
				widget.setShowing(true);
		}
		draw();

		handle.clear();
		// System.out.println("preLoop");
		// getShowing();
		updateChildren(handle, location);
		// have handled all possible widgets..?
		if (grab) {
			pickupNew(handle.hover, location);
		} else {
			dropNew();
		}
		// if (!handle.hover.isEmpty())
		// handle.hover.hasHover(true);// push upstream hasHover
		if (interacting != null && !interacting.isEmpty()) {// not null
			if (interacting.isSelected()) {// if this is selected
				if (interacting.isBackground()) {// if this is a background
					if (interacting.getOwner() != null// and has an owner
							&& interacting.getOwner().isMovable()) {// and owner
																	// is
																	// movable
						interacting.getOwner().move(
								PVector.sub(location, grabbed));// move to
																// location
						grab(location.get());
					}
				} else if (interacting.isMovable()) {// not a background
					interacting.move(PVector.sub(location, grabbed));// add the
																		// change
					grab(location.get());

				}
			}
			if (rotating && interacting.isRotatable()) {
				interacting.rotate(rotation);
			}
			if (scaling && interacting.isScalable()) {
				interacting.resize(scale);
			}
		}
		// draw();
	}

	public void updateChildren(final Handler handler, final PVector location) {

		// System.out.println("ENTER LOOP: " + getName());
		for (final Widget widget : widgets) {
			// update all dashes in the widgets
			// widget.hasHover(false);

			// back to parent?
			// System.out.println("Updating: " + getName());
			// System.out.println("for: " + widget.getName());

			if (widget.isHover(location)) {
				// System.out.println("hovering: " + widget.getName());
				// hover = true;
				handler.handle(widget);
			} else {

				// System.out.println(widget.getName()
				// + ".setShowing(false,false);");
				// widget.setShowing(false);
			}
			for (final Dashboard menu : widget.getMenus()) {
				// if (hover && !menu.isHidden())
				// menu.setShowing(true);
				menu.updateChildren(handler, location);
			}
		}
	}

}

class Handler {
	Dashboard owner = null;
	Widget hover;// = empty;
	Widget empty;

	Handler(final Widget empty) {
		this.empty = empty;
		hover = empty;
		owner = empty.getOwner();
	}

	public void clear() {
		hover = empty;
	}

	public void handle(final Widget widget) {

		// System.out.println("\n **Got: " + widget.getName() + ", owned by: "
		// + widget.getOwner().getName() + " test against "
		// + this.hover.getName());

		if (widget.isEmpty()) {
			// System.out.println("Widget is EMPTY, rejected.");
			return;
		}
		if (hover.isEmpty() || hover.isBackground()) {
			// System.out.println("hover was EMPTY or BACKGROUND, accepted");
			hover = widget;
			return;
		}
		if (!widget.isBackground() && widget.getOwner() != owner) {
			// System.out.println("not Super, not Background... accepted.");
			hover = widget;
			return;
		}
		if (widget.isBackground()
				&& !widget.getOwner().isGrabThroughBackground()
				&& !widget.belongsTo(hover)) {
			// System.out.println("grab through background disabled... accepted.");
			hover = widget;
			return;
		}
		if (!widget.isBackground() && widget.getOwner() == hover.getOwner()) {
			// System.out.println("same owner, not Background... accepted.");
			hover = widget;
			return;
		}

		// System.out.println("Rejected.");
	}

	@Override
	public String toString() {
		return "Handler [hover=" + hover.getName() + ", empty="
				+ (empty == null ? "NULL" : empty.getName()) + "]";
	}
}