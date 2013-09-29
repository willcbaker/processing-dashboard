package com.omegabyte.dashboard;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

public class Widget {

	public enum Shape {
		circle, rectangle, custom, image
	}

	protected PApplet parent;
	protected Dashboard owner;

	protected final ArrayList<Dashboard> menus = new ArrayList<Dashboard>();

	private Shape shape = Shape.rectangle;

	protected PVector size = new PVector(0, 0);

	protected PVector position = new PVector(0, 0);;

	protected float minSize = 80;
	protected float maxSize = 200;
	protected boolean selected = false;
	protected boolean movable = true;
	protected boolean rotatable = false;

	protected boolean scalable = false;
	protected boolean selectable = true;
	protected boolean hidden = false;
	protected boolean background = false;
	protected PVector hover = null;
	protected boolean empty = false;
	protected boolean fixed = false;

	protected float alpha = 126;
	protected int color = 0xFFB0B0FB;
	protected int textColor = 0;

	protected float textSize = -1;
	protected float titleSize = -1;

	protected String text = "";
	protected String title = "";
	private PImage texture;
	private PShape pShape;

	float cornerRad = 10;
	String textureFile;

	float orientation = 0;
	float rotateMax = PApplet.radians(5);
	private boolean autoHide = true;
	protected String name;

	public Widget() {
	}

	public Widget(final PApplet app) {
		parent = app;
		setName("");
	}

	public Widget(final PApplet app, final String name) {
		parent = app;
		this.setName(name);
	}

	public Widget addMenu(final Dashboard menu) {
		menu.setOwner(this);
		menus.add(menu);
		return this;// menus.size();
	}

	private void displayAllMenus() {
		for (final Dashboard menu : menus) {
			menu.draw();
		}
	}

	private void displayMenu(final Dashboard menu) {
		if (menu.widgets.isEmpty())
			return;
		// menu.handle(parent.mouseX - position.x, parent.mouseY - position.y,
		// parent.mousePressed);
		if ((!menu.isHidden() && (menu.getBackground().isHover())
				|| this.isHover() || menu.isShowing())) {// ||
															// menu.getOwner().isHover()
			if (isBackground())
				// System.out.println("displayMenu: " + getTitle() + "hover: "
				// + hover + " hidden: " + hidden);

				menu.setHidden(false);
			menu.draw();
		} else {
			// System.out.println("**AUTOHIDE**");
			menu.setHidden(autoHide);
		}
	}

	private void displayMenus() {
		for (final Dashboard menu : menus)
			displayMenu(menu);
	}

	private boolean showing;

	public void setShowing(boolean thisWidget, boolean dashboard) {
		showing = thisWidget;
		for (Dashboard menu : menus) {
			menu.setShowing(dashboard);
		}
	}

	public Widget setShowing(boolean value) {
		showing = value;
		for (Dashboard menu : menus) {
			menu.setShowing(value);
		}
		return this;
	}

	public void draw() {
		if (hidden && !showing)
			return;
		parent.pushStyle();
		parent.pushMatrix();
		parent.translate(getPosition().x, getPosition().y);
		parent.rotate(-orientation);
		drawShape();
		parent.popMatrix();
		parent.popStyle();
		if (selected)
			displayAllMenus();
		else
			displayMenus();
	}

	protected void drawShape() {
		switch (shape) {
		case rectangle:
			parent.rectMode(PApplet.CORNER);
			if (selected && selectable) {
				parent.fill(0, 125);
				parent.noStroke();
				parent.rect(-5, +5, getSize().x, getSize().y, cornerRad);
			}
			parent.noStroke();
			parent.fill(color, getAlpha());
			parent.rect(0, 0, getSize().x, getSize().y, cornerRad);
			drawText();
			break;
		case circle:
			parent.ellipseMode(PApplet.CORNER);
			if (selected) {
				parent.fill(0, 125);
				parent.noStroke();
				parent.ellipse(getPosition().x - 5, getPosition().y + 5,
						getSize().mag(), getSize().mag());
			}
			parent.stroke(0);
			parent.fill(color, getAlpha());
			parent.ellipse(getPosition().x, getPosition().y, getSize().mag(),
					getSize().mag());
			drawText();
			parent.text(title, size.x / 2, size.y / 2);
			break;
		case custom:
			parent.pushMatrix();
			// parent.translate(-size.x / 2, -size.y / 2);
			if (selected && selectable) {
				parent.fill(0, 125);
				parent.noStroke();
				pShape.disableStyle();
				parent.pushMatrix();
				parent.translate(-5, 5);
				parent.shape(pShape);
				parent.popMatrix();
				pShape.enableStyle();
			}
			parent.shape(pShape, 0, 0);
			parent.popMatrix();
			break;
		case image:
			parent.rectMode(PApplet.CORNER);
			if (selected && selectable) {
				parent.fill(0, 125);
				parent.noStroke();
				parent.pushMatrix();
				parent.translate(-size.x / 2, -size.y / 2);
				parent.rect(-5, +5, getSize().x, getSize().y, cornerRad);
				parent.popMatrix();
			}
			parent.noStroke();
			// noFill();
			parent.tint(255, getAlpha());
			parent.image(texture, -getSize().x / 2, -getSize().y / 2);
			// parent.image(getTexture(), 0, 0);
			parent.tint(255, 255);
			break;
		default:
			break;
		}
	}

	private void drawText() {
		parent.fill(textColor, (float) (alpha * 1.1));
		parent.textAlign(PApplet.CENTER);
		parent.textSize(getTextSize());
		parent.text(text, size.x / 2, size.y / 2 + textSize / 2);
		parent.textSize(getTitleSize());
		parent.text(title, size.x / 2, (float) (getTextSize() * 0.1));
	}

	public void drop() {
		selected = false;
	}

	public float getAlpha() {
		return alpha;
	}

	public int getColor() {
		return color;
	}

	public int getTextColor() {
		return textColor;
	}

	public Dashboard getMenu(final String name) {
		for (final Dashboard menu : menus) {
			if (menu.getName() == name)
				return menu;
		}
		return null;
	}

	public ArrayList<Dashboard> getMenus() {
		return menus;
	}

	public String getName() {
		return name;
	}

	public Dashboard getOwner() {
		return owner;
	}

	public PApplet getParent() {
		return parent;
	}

	public PVector getPosition() {
		return position;
	}

	public Shape getShape() {
		return shape;
	}

	public PVector getSize() {
		return size;
	}

	public String getText() {
		return text;
	}

	float getTextSize() {
		return PApplet.abs(textSize);
	}

	public PImage getTexture() {
		return texture;
	}

	public String getTitle() {
		return title;
	}

	float getTitleSize() {
		return PApplet.abs(titleSize);
	}

	protected void invokeCallback() {

		if (parent != null) {
			try {
				parent.getClass()
						.getMethod("call_" + this.getTitle(), Widget.class,
								Dashboard.class)
						.invoke(parent, this, this.owner);
			} catch (final Exception e) {
				PApplet.println(e.getMessage() + " CALLBACK ERROR");
			}
		}
	}

	public boolean isBackground() {
		return background;
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean isFixed() {
		return fixed;
	}

	public boolean isHidden() {
		return hidden;
	}

	boolean isHover() {
		// if (isBackground())
		// System.out.println("isHover(): " + getTitle() + "hover: " + hover
		// + " hidden: " + hidden);
		boolean isHover = false;
		if (hover != null)
			isHover = true;
		return isHover && !hidden;
	}

	public boolean isHover(final float x, final float y) {
		return isHover(new PVector(x, y));
	}

	public boolean isHover(final PVector location) {
		hover = null;
		switch (shape) {
		case rectangle:
			if (location.x > this.position.x
					&& location.x < this.position.x + this.size.x
					&& location.y > this.position.y
					&& location.y < this.position.y + this.size.y) {
				hover = location;
			}
			break;
		case circle:
			if ((PVector.add(position, size).dist(location)) < (size.mag())) {
				hover = location;
			}
			break;
		case custom:
			if (location.x > this.position.x
					&& location.x < this.position.x + this.size.x
					&& location.y > this.position.y
					&& location.y < this.position.y + this.size.y) {
				hover = location;
			}
			break;
		case image:
			if (location.x > this.position.x - this.size.x / 2
					&& location.x < this.position.x + this.size.x / 2
					&& location.y > this.position.y - this.size.y / 2
					&& location.y < this.position.y + this.size.y / 2) {
				hover = location;
			}
			break;
		default:
			if (location.x > this.position.x
					&& location.x < this.position.x + this.size.x
					&& location.y > this.position.y
					&& location.y < this.position.y + this.size.y) {
				hover = location;
			}
			break;
		}
		// if (isBackground())
		// System.out.println("isHover: " + getTitle() + "hover: " + hover
		// + " hidden: " + hidden);
		return isHover();
	}

	void setHover(final PVector location) {
		hover = location;
	}

	public boolean isRotatable() {
		return rotatable;
	}

	public boolean isScalable() {
		return scalable;
	}

	public boolean isSelected() {
		return selected;
	}

	public Widget loadShape(final String filename) {
		shape = Shape.custom;
		pShape = parent.loadShape(filename);
		return this;
	}

	public Widget loadImage(final String filename) {
		textureFile = filename;
		setImage(parent.loadImage(textureFile));
		final float tempW = getTexture().width;
		final float tempH = getTexture().height;
		final float ratio = tempW / tempH;
		getTexture().resize((int) (getSize().x), (int) (getSize().x / ratio));
		getSize().set(getSize().x, getTexture().height);// = texture.height;
		return this;
	}

	public Widget lock() {
		movable = false;
		rotatable = false;
		scalable = false;
		return this;
	}

	public boolean move(final float x, final float y) {
		if (!movable) {
			return false;
		}
		setPosition(x, y);
		// System.out.println("movingMe " + position.x + "," + position.y);
		return true;
	}

	public boolean move(final PVector vec) {
		if (!movable) {
			return false;
		}
		final PVector temp = position.get();
		position.set(vec);
		for (final Dashboard menu : menus)
			if (menu.background != null)
				menu.move(PVector.add(vec,
						PVector.sub(menu.background.position, temp)));
		return true;
	}

	public void pickup() {
		selected = true;
	}

	public float resize(final float scale) {
		if (!scalable) {
			return getSize().mag();
		}
		// System.out.println(text + " " + scale);
		final float newSize = PVector.mult(getSize(), scale).mag();
		if (newSize > minSize && newSize < maxSize) {
			size.mult(scale);
			if (shape == Shape.custom) {
				pShape.scale(scale);
				// System.out.println("scale:  " + scale);
			}
			if (getTexture() != null) {
				setImage(parent.loadImage(textureFile));
				final float tempW = getTexture().width;
				final float tempH = getTexture().height;
				final float ratio = tempW / tempH;
				getTexture().resize((int) (getSize().x),
						(int) (getSize().x / ratio));
			}
			if (textSize < 1)
				textSize = (float) (size.mag() * 0.1);
		}
		return size.mag();
	}

	public void rotate(final float value) {
		if (!rotatable)
			return;
		orientation = PApplet.constrain(orientation + value, -rotateMax,
				rotateMax);
	}

	public Widget setAlpha(final float alpha) {
		this.alpha = alpha;
		return this;
	}

	public void setAutoHide(final boolean autoHide) {
		this.autoHide = autoHide;
	}

	public Widget setBackground(final boolean background) {
		this.background = background;
		return this;
	}

	public Widget setColor(final int colour) {
		this.color = colour;
		return this;
	}

	public Widget setCorner(final float cornerRad) {
		this.cornerRad = cornerRad;
		return this;
	}

	public Widget setEmpty(final boolean empty) {
		this.empty = empty;
		return this;
	}

	public Widget setFixed(final boolean fixed) {
		this.fixed = fixed;
		if (fixed) {
			movable = false;
			rotatable = false;
			scalable = false;
			selectable = false;
		}
		return this;
	}

	public Widget setHidden(final boolean val) {
		hidden = val;
		return this;
	}

	public Widget setMaxSize(final float maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	public Widget setMinSize(final float minSize) {
		this.minSize = minSize;
		return this;
	}

	public Widget setMovable(final boolean val) {
		movable = val;
		return this;
	}

	public Widget setName(final String name) {
		this.name = name;
		return this;
	}

	public Widget setOwner(final Dashboard dashboard) {
		owner = dashboard;
		return this;
	}

	public void setParent(final PApplet parent) {
		this.parent = parent;
		this.owner = new Dashboard(parent);
	}

	public Widget setPosition(final float x, final float y) {
		return this.setPosition(new PVector(x, y));
	}

	public Widget setPosition(final PVector position) {
		this.position = position;
		for (final Dashboard menu : menus)
			menu.move(position);
		return this;
	}

	public Widget setRotatable(final boolean val) {
		rotatable = val;
		return this;
	}

	public Widget setScalable(final boolean val) {
		scalable = val;
		return this;
	}

	public Widget setScalable(final float min, final float max) {
		scalable = true;
		minSize = min;
		maxSize = max;
		return this;
	}

	public Widget setSelectable(final boolean val) {
		selectable = val;
		return this;
	}

	public Widget setShape(final Shape shape) {
		this.shape = shape;
		return this;
	}

	public Widget setSize(final float rad) {
		size.set(rad, 0);
		return this;
	}

	public Widget setSize(final float x, final float y) {
		size.set(x, y);
		return this;
	}

	public Widget setSize(final PVector size) {
		this.size = size;
		return this;
	}

	public Widget setText(final String text) {
		this.text = text;
		if (textSize < 1)
			textSize = (float) (size.mag() * 0.1);
		return this;
	}

	public Widget setTextColor(final int colour) {
		this.textColor = colour;
		return this;
	}

	public Widget setTextSize(final float size) {
		if (size < 1) {
			textSize = -(float) (this.size.mag() * 0.1);
		} else
			textSize = size;
		return this;
	}

	public Widget setImage(final PImage image) {
		if (image == null) {
			setShape(Shape.rectangle);
		} else {
			setShape(Shape.image);
			this.texture = image;
		}
		return this;
	}

	public Widget setTitle(final String title) {
		this.title = title;
		if (titleSize < 1) {
			titleSize = -(float) (size.mag() * 0.1);
		}
		return this;
	}

	public Widget setTitleSize(final float size) {
		if (size < 1) {
			titleSize = -(float) (this.size.mag() * 0.1);
		} else
			titleSize = size;
		return this;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + ": " + title + ": [shape="
				+ shape + ", size=" + size + ", position=" + position + ","
				+ (movable ? "moveable," : "") + ""
				+ (rotatable ? "rotatable," : "") + ""
				+ (selected ? "selected," : "") + ""
				+ (scalable ? "scalable," : "") + ""
				+ (selectable ? "selectable," : "") + ""
				+ (hidden ? "hidden," : "") + "visible,"
				+ (fixed ? "fixed," : "") + " text=" + text + "]";
	}
}