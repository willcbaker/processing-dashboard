package com.omegabyte.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PVector;

public class Graph extends Widget {

	public static enum Mode {
		CLIP, AUTO, DEFAULT
	};

	public static enum Type {
		POINT, LINE, FILL
	};

	private double max;
	private double min;
	private float xAxisL;
	// private float yAxisL;
	double[] data;
	boolean autoScale = false;
	boolean windowed = false;
	ArrayList<int[]> windows = new ArrayList<int[]>();
	private Type type = Type.POINT;
	private boolean autoColor = false;

	HashMap<Float, String> labels = new HashMap<Float, String>();

	boolean displayLabels = true;

	boolean displayTrace = true;

	private float markerSize = 2;

	public Graph(final PApplet parent) {
		this.parent = parent;
		this.name = "";
		data = new double[100];
		max = calcMax();
		min = calcMin();
		// yAxisL = (float) (max - min);
		xAxisL = data.length;
		textColor = 255;
		titleSize = 10;
	}

	public Graph(final PApplet parent, final String name) {
		this.parent = parent;
		this.name = name;
		data = new double[100];
		max = calcMax();
		min = calcMin();
		// yAxisL = (float) (max - min);
		xAxisL = data.length;
		textColor = 255;
		titleSize = 10;
	}

	public void addLabel(final float value, final String text) {
		labels.put(value, text);
	}

	private double calcMax() {
		max = 0;
		for (final double point : data)
			if (point > max)
				max = point;
		return max;
	}

	private double calcMin() {
		min = max;
		for (final double point : data)
			if (point < min)
				min = point;
		return min;
	}

	@Override
	public void draw() {
		parent.pushMatrix();
		if (autoScale)
			update();
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.fill(50, 125);
		parent.stroke(0);
		parent.rectMode(PApplet.CORNER);
		parent.translate(position.x, position.y);
		parent.rect(0, 0, size.x + markerSize, titleSize + 4);
		parent.rect(0, 0, size.x + markerSize, size.y + titleSize + 2
				* markerSize);
		parent.fill(textColor);
		// parent.textFont(graphFont);
		parent.textSize(titleSize);
		parent.text(title, size.x / 2, titleSize / 2 + 1);
		if (isHover(parent.mouseX, parent.mouseY))
			trace();
		if (isDisplayLabels())
			drawLabels();
		parent.fill(color);
		parent.stroke(color);
		parent.translate(2, titleSize + 6);
		switch (type) {
		case POINT:
			plotPoint();
			break;
		case LINE:
			plotLine();
			break;
		case FILL:
			plotFill();
			break;
		default:
			plotPoint();
			break;
		}
		parent.popMatrix();
	}

	void drawLabels() {
		parent.fill(textColor);
		final Iterator<Entry<Float, String>> iter = labels.entrySet()
				.iterator();

		while (iter.hasNext()) {
			final Entry<Float, String> mEntry = iter.next();
			// System.out.println(mEntry.getKey() + " : " +
			// mEntry.getValue());
			if (autoColor)
				parent.fill((int) (PApplet.map(mEntry.getKey(), (float) min,
						(float) max, 0xFF022020, 0xFF06FFBB)));
			parent.text(
					mEntry.getValue().toString(),
					position.x + size.x / 2,
					position.y
							+ PApplet.map((mEntry.getKey()), (float) this.min,
									(float) this.max, this.size.y, 0));
		}
		// for (java.util.Collection thing : labels.values())
		// for (float value : thing)
	}

	void drawWindows() {
		final int windowColor = 0xFFFFFFFF;// color & #1F0F2F;
		for (final int[] window : windows) {
			parent.noFill();
			parent.stroke(windowColor);
			parent.rect(position.x + window[0] * scale(), position.y, window[1]
					* scale() - window[0] * scale(), size.y);
		}
	}

	void findDelta(final double delta) {
		final ArrayList<int[]> windows = new ArrayList<int[]>();
		int memory = 0;
		for (int i = 1; i < data.length; i++) {
			if (data[i] != data[memory]) {
				for (int j = i; j < data.length; j++) {
					if (data[j] - data[i - 1] >= delta
							|| data[j] - data[i - 1] <= -delta) {
						final int[] found = { i - 1, j };
						memory = i;
						windows.add(found);
						break;
					}
				}
			}
		}
		final ArrayList<int[]> windowsFinal = new ArrayList<int[]>();
		if (!windows.isEmpty()) {
			int[] found = windows.get(0);
			for (int i = 1; i < windows.size(); i++) {
				if (found[1] > windows.get(i)[0]) {
					found[1] = windows.get(i)[1];
				} else {
					windowsFinal.add(found.clone());
					found = windows.get(i);
				}
			}
			windowsFinal.add(found.clone());
		}
		this.windows.clear();
		this.windows.addAll(windowsFinal);
	}

	void findRange(final float[] range) {
		int start = -99;
		for (int i = 1; i < data.length; i++) {
			if (data[i] >= range[0] && data[i] <= range[1]) {// in range
				if (start == -99) {
					start = i;
					// println("i: "+i +"| start: "+start);
				}// start
			} else if (start != -99) {
				final int[] found = { start, i };
				windows.add(found);
				start = -99;
			} else if (start != -99 && i == data.length - 1) {
				final int[] found = { start, i };
				windows.add(found);
			}
		}
	}

	void findThresh(final float value) {
		int start = -99;
		for (int i = 1; i < data.length; i++) {
			if (data[i] > value) {// in range
				if (start == -99) {
					start = i;
				}// start
			} else if (start != -99) {
				final int[] found = { start, i };
				windows.add(found);
				start = -99;
			} else if (start != -99 && i == data.length - 1) {
				final int[] found = { start, i };
				windows.add(found);
			}
		}
	}

	public double[] getData() {
		return data;
	}

	public Type getType() {
		return type;
	}

	public ArrayList<int[]> getWindows() {
		return windows;
	}

	void highlight(final int index, final int colur) {
		final int windowColor = colur;// color & #1F0F2F;
		parent.stroke(windowColor);
		parent.line(position.x + index * scale(), position.y, position.x
				+ index * scale(), position.y + size.y);
	}

	public boolean isAutoColor() {
		return autoColor;
	}

	public boolean isDisplayLabels() {
		return displayLabels;
	}

	public boolean isDisplayText() {
		return displayTrace;
	}

	public void clear() {
		data = new double[data.length];
	}

	boolean isWindowed(final int index) {
		boolean windowed = false;
		for (final int[] window : windows) {
			if (index >= window[0] && index <= window[1])
				windowed = true;
		}
		return windowed;
	}

	public void plot(final Graph.Type type) {

	}

	void plotFill() {
		// normalize to scale
		float index = 0;
		final float by = scale();
		for (final double point : data) {
			parent.line(index, size.y, index, PApplet.map((float) (point),
					(float) min, (float) max, size.y, 0));
			index += by;
		}
	}

	void plotLine() {
		// normalize to scale
		float index = 0;
		final float by = scale();
		parent.pushStyle();
		parent.strokeWeight(2);
		for (int i = 1; i < data.length; i++) {
			parent.line(index, PApplet.map((float) data[i], (float) min,
					(float) max, size.y, 0), index, PApplet.map(
					(float) data[i - 1], (float) min, (float) max, size.y, 0));
			index += by;
		}
		parent.popStyle();
	}

	void plotPoint() {

		// normalize to scale
		float index = 0;
		final float by = scale();
		parent.noStroke();
		for (final double point : data) {

			final float pointY = position.y
					+ PApplet.map((float) (point), (float) this.min,
							(float) this.max, this.size.y, 0);
			if (pointY >= position.y && pointY <= position.y + size.y) {
				if (autoColor)
					parent.fill((int) (PApplet.map((float) point, (float) min,
							(float) max, 0xFF022020, 0xFF06FFBB)));
				parent.ellipse(index, PApplet.map((float) (point), (float) min,
						(float) max, size.y - 2 - markerSize, 0), markerSize,
						markerSize);
			}
			index += by;
		}

		// {
		// if (autoColor)
		// parent.fill((int) (PApplet.map((float) point, (float) min,
		// (float) max, 0xFF022020, 0xFF06FFBB)));
		// parent.ellipse(index, PApplet.map((float) (point), (float) min,
		// (float) max, size.y, 0), markerSize, markerSize);
		// index += by;
		// }
	}

	float scale() {
		return size.x / xAxisL;
	}

	@Override
	public Graph setAlpha(final float alpha) {
		super.setAlpha(alpha);
		return this;
	}

	public void setAutoColor(final boolean autoColor) {
		this.autoColor = autoColor;
	}

	public Graph setAutoScale(final boolean state) {
		autoScale = state;
		return this;
	}

	@Override
	public Graph setColor(final int col) {
		color = col;
		return this;
	}

	@Override
	public Graph setCorner(final float cornerRad) {
		super.setCorner(cornerRad);
		return this;
	}

	public Graph setDataLength(final int size) {
		data = new double[size];
		// yAxisL = (float) (max - min);
		xAxisL = data.length;
		this.scale();
		return this;
	}

	public void displayLabels(final boolean displayLabels) {
		this.displayLabels = displayLabels;
	}

	public void displayTrace(final boolean val) {
		this.displayTrace = val;
	}

	@Override
	public Graph setEmpty(final boolean empty) {
		super.setEmpty(empty);
		return this;
	}

	@Override
	public Graph setFixed(final boolean fixed) {
		super.setFixed(fixed);
		return this;
	}

	@Override
	public Graph setHidden(final boolean val) {
		super.setHidden(val);
		return this;
	}

	public void setMarkerSize(final float size) {
		markerSize = size;
	}

	public Graph setMax(final double val) {
		max = val;
		return this;
	}

	@Override
	public Graph setMaxSize(final float maxSize) {
		super.setMaxSize(maxSize);
		return this;
	}

	public Graph setMin(final double val) {
		min = val;
		return this;
	}

	@Override
	public Graph setMinSize(final float minSize) {
		super.setMinSize(minSize);
		return this;
	}

	@Override
	public Graph setMovable(final boolean val) {
		super.setMovable(val);
		return this;
	}

	@Override
	public Graph setName(final String name) {
		super.setName(name);
		return this;
	}

	@Override
	public Graph setOwner(final Dashboard dashboard) {
		super.setOwner(dashboard);
		return this;
	}

	@Override
	public Graph setPosition(final float x, final float y) {
		super.setPosition(x, y);
		return this;
	}

	@Override
	public Graph setPosition(final PVector position) {
		super.setPosition(position);
		return this;
	}

	@Override
	public Graph setRotatable(final boolean val) {
		super.setRotatable(val);
		return this;
	}

	@Override
	public Graph setScalable(final boolean val) {
		super.setScalable(val);
		return this;
	}

	@Override
	public Graph setScalable(final float min, final float max) {
		super.setScalable(min, max);
		return this;
	}

	@Override
	public Graph setSelectable(final boolean val) {
		super.setSelectable(val);
		return this;
	}

	@Override
	public Graph setShape(final Shape shape) {
		super.setShape(shape);
		return this;
	}

	@Override
	public Graph setSize(final float rad) {
		this.scale();
		super.setSize(rad);
		this.scale();
		return this;
	}

	@Override
	public Graph setSize(final float x, final float y) {
		super.setSize(x, y);
		this.scale();
		return this;
	}

	@Override
	public Graph setSize(final PVector size) {
		super.setSize(size);
		this.scale();
		return this;
	}

	@Override
	public Graph setText(final String text) {
		super.setText(text);
		return this;
	}

	@Override
	public Graph setTextColor(final int colour) {
		super.setTextColor(colour);
		return this;
	}

	@Override
	public Graph setTextSize(final float size) {
		super.setTextSize(size);
		return this;
	}

	@Override
	public Graph setTitle(final String val) {
		title = val;
		return this;
	}

	@Override
	public Graph setTitleSize(final float size) {
		super.setTitleSize(size);
		return this;
	}

	public Graph setType(final Graph.Type type) {
		this.type = type;
		return this;
	}

	private void trace() {
		if (!displayTrace)
			return;
		final String value = String.format("%.2g%n",
				data[(int) ((parent.mouseX - position.x) / scale())]);

		// parent.text(title, size.x / 2, titleSize + 2);
		parent.text(value, size.x - value.length() * titleSize / 2 + 2,
				titleSize + 4);
		// parent.text(value, parent.mouseX - position.x + 15, parent.mouseY -
		// 15);
	}

	public void update() {
		max = calcMax();
		min = calcMin();
		// yAxisL = (float) (max - min);
		xAxisL = data.length;
	}

	public void update(final double newP) {
		windowed = false;
		for (int i = 0; i < data.length - 1; i++)
			data[i] = data[i + 1];
		data[data.length - 1] = newP;
	}

	public void update(double newPoint, final Graph.Mode mode) {
		// windowed = false;
		switch (mode) {
		case CLIP:
			if (newPoint > this.max)
				newPoint = max;
			else if (newPoint < this.min)
				newPoint = min;
			break;
		case AUTO:
			if (newPoint > this.max)
				max = newPoint;
			else if (newPoint < this.min)
				min = newPoint;
			break;
		default:
			break;
		}
		update(newPoint);
	}

	public void update(final double[] hits) {
		for (int i = 0; i < data.length - 1; i++)
			// check
			data[i] = data[i + 1];
		for (int i = data.length - hits.length; i < data.length; i++)
			// check
			data[i] = hits[i - hits.length];
	}

	void window(final float start, final float end) {
	}
}
