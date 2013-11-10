package com.omegabyte.animations;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

public class Animation extends Thread {
	boolean running; // Is the thread running? Yes or no?
	int _wait = 18; // How many milliseconds should we wait in between
					// executions?
	boolean recursive = false;

	int count; // counter

	Widget widget;

	Dashboard dash;

	long timeBegin;

	long _duration;
	long timeEnd;
	float alphaCopy;
	PVector positionCopy;

	PVector sizeCopy;

	long _fadeIn = 0;

	long _fadeOut = 0;

	long shaker;
	private float distance;
	private float speed;
	private long timeOut;

	public Animation() {
		super();
	}

	/**
	 * A method to create a delay. this is used for animation sequences
	 * 
	 * @param duration
	 *            the length of time in milliseconds
	 */
	public Animation(final long duration) {
		super();
		this._duration = duration;
		setName("Delay_" + duration);
	}

	public Animation(final Dashboard dash, final long duration) {
		_wait = 20;
		widgets.addAll(dash.getWidgets());
		try {
			widget = dash.getOwner();
			setName(widget.getName());
			this.dash = dash;
			alphaCopy = widget.getAlpha();
			positionCopy = widget.getPosition();
			sizeCopy = widget.getSize();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		count = 0;
		_duration = duration;
		running = false;
	}

	ArrayList<Widget> widgets = new ArrayList<Widget>();

	public Animation(final Widget widget) {
		super();
		widgets.add(widget);
	}

	public Animation(final Dashboard dashboard) {
		super();
		widgets.addAll(dashboard.getWidgets());
	}

	public Animation(final Widget widg, final long duration) {
		widgets.add(widg);
		_wait = 20;
		setName(widg.getName());
		widget = widg;
		alphaCopy = widg.getAlpha();
		positionCopy = widg.getPosition();
		sizeCopy = widg.getSize();
		count = 0;
		_duration = duration;
		running = false;
	}

	public void clear() {
		widget.setHidden(true);
		running = false;
	}

	void fadeIn(final long duration) {
		widget.setHidden(false);
		if (duration < 1)
			return;
		while (running && widget.getParent().millis() < (timeBegin + duration)) {
			// System.out.println(id + ": " + timeBegin + " " + timeEnd + " "
			// + widget.getParent().millis() + " alpha: "
			// + widget.getAlpha() + " alpha: " + alphaCopy + " "
			// + widget.isHidden());
			widget.setAlpha(PApplet.map(widget.getParent().millis(), timeBegin,
					timeBegin + duration, 0, alphaCopy));
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
			} catch (final Exception e) {
			}
		}
	}

	void fadeOut(final long duration) {
		if (duration < 1)
			return;
		while (running && widget.getParent().millis() < timeEnd) {
			// println(id + ": " +millis() + " alpha: " + widget.alpha+
			// " alpha: " + alphaCopy + " " + widget.hidden);
			widget.setAlpha(PApplet.map(PApplet.constrain(widget.getParent()
					.millis(), timeOut, timeEnd), timeOut, timeEnd, alphaCopy,
					0));
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
			} catch (final Exception e) {
			}
		}
		widget.setHidden(true);
	}

	int getCount() {
		return count;
	}

	public Dashboard getDash() {
		return dash;
	}

	public Widget getWidget() {
		return widget;
	}

	public boolean isRecursive() {
		return recursive;
	}

	void move(final float speed) {
		final PVector direction = PVector.sub(widget.getPosition(),
				positionCopy);
		direction.setMag(speed);
		direction.mult(-1);
		// System.out.println("old: " + positionCopy);
		// System.out.println("pos: " + widget.getPosition());
		// System.out.println("dir: " + direction);
		while (running
				&& PVector.dist(widget.getPosition(), positionCopy) > speed + 1) {
			widget.move(PVector.add(widget.getPosition(), direction));
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
			} catch (final Exception e) {
			}
		}
		widget.setPosition(positionCopy.get());
		running = false;
	}

	// Our method that quits the thread
	void quit() {
		System.out.println("Quitting.");
		running = false; // Setting running to false ends the loop in run()
		// IUn case the thread is waiting. . .
		// TODO: finish();
		interrupt();
	}

	// We must implement run, this gets triggered by start()
	@Override
	public void run() {
		if (widgets.isEmpty()) {
			wait2(_duration);
		} else {
			do {
				while (!running) {

					// Ok, let's wait for however long we should wait
					try {
						sleep((long) (1000 / widgets.get(0).getParent().frameRate));
					} catch (final Exception e) {
						// e.printStackTrace();
					}
				}
				// System.out.print("RUNNING?");
				// System.out.print(getWidget().getName() + " ");
				// System.out.println("Time is " + timeBegin + " go until:  "
				// + timeEnd + "." + running);
				while (running && widget.getParent().millis() < timeEnd) {// (running
																			// &&
																			// count
																			// <
																			// 10)
					try {
						// System.out.println("running"); // {
						fadeIn(_fadeIn);
						// wait2(_duration - _fadeIn - _fadeOut);
						fadeOut(_fadeOut);
						shake(shaker);
						// move(speed);
					} catch (final Exception e) {
						e.printStackTrace();
					}
					running = false;
				}
				// System.out.println(getName() + " thread is done!"); // The
				// thread is
				// done when we get to the end of run()
			} while (isRecursive());
		}
	}

	public void setDash(final Dashboard dash) {
		this.dash = dash;
	}

	public void setDuration(final long val) {
		_duration = val;
		final long total = _fadeIn + _fadeOut;
		if (_duration < total)
			_duration = total;
	}

	public void setFadeIn(final long val) {
		_fadeIn = val;
		final long total = _fadeIn + _fadeOut;
		if (_duration < total)
			_duration = total;
	}

	public void setFadeOut(final long val) {
		_fadeOut = val;
		final long total = _fadeIn + _fadeOut;
		if (_duration < total)
			_duration = total;
	}

	public void setRadiate(final long sendDuration) {
		// TODO Auto-generated method stub

	}

	public Animation setRecursive(final boolean recursive) {
		this.recursive = recursive;
		return this;
	}

	public void setShaker(final long val, final float dist) {
		shaker = val;
		distance = dist;
		if (_duration < val)
			_duration = val;
	}

	public void setSnap(final float speed) {
		this.speed = speed;

	}

	public void setWidget(final Widget widget) {
		this.widget = widget;
		setName(widget.getName());
		alphaCopy = widget.getAlpha();
		positionCopy = widget.getPosition().get();
		System.out.println("copy " + positionCopy);
		sizeCopy = widget.getSize();
	}

	void shake(final long duration) {
		if (duration < 1)
			return;
		// final long timeOut = (timeEnd - duration);
		if (dash == null)
			dash = widget.getOwner();
		final PVector move = new PVector(0, 0);
		while ((running && dash.getParent().millis() < timeEnd)
				|| move.mag() != 0) {
			// int direction = (count % 2) < 1 ? -1 : 1;
			final boolean B = ((count & 0x4) > 0);
			final boolean C = ((((count & 0x2) >> 1) ^ (count & 0x1)) & 0x1) > 0;
			final PVector shake = new PVector((B ? 0 : 1) * (C ? -1 : 1),
					(B ? 1 : 0) * (C ? -1 : 1));

			move.add(shake);
			shake.setMag(distance);
			dash.nudge(shake);
			count++;
			// PApplet.map(PApplet.constrain(widget.getParent()
			// .millis(), timeOut, timeEnd), timeOut, timeEnd, alphaCopy,
			// 0));
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
			} catch (final Exception e) {
			}
		}
	}

	public void trigger() {
		if (!this.running) {
			timeBegin = widget.getParent().millis();
			timeEnd = timeBegin + _duration;
			timeOut = (timeEnd - _fadeOut);
			this.running = true;
		} else {
			timeEnd = widget.getParent().millis() + _duration;
			timeOut = (timeEnd - _fadeOut);
		}
		System.out.println(getName() + " Triggered");
	}

	void wait2(final float duration) {
		// System.out.println("Waiting... " + _duration);
		if (duration < 1)
			return;
		try {
			sleep((long) (duration));
		} catch (final Exception e) {
		}
	}

	public long getWait() {
		return _wait;
	}
}
