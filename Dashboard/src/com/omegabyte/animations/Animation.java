package com.omegabyte.animations;

import processing.core.PApplet;
import processing.core.PVector;

import com.omegabyte.dashboard.Dashboard;
import com.omegabyte.dashboard.Widget;

public class Animation extends Thread {
	boolean running; // Is the thread running? Yes or no?
	int _wait; // How many milliseconds should we wait in between executions?
	String id; // Thread name
	int count; // counter
	Widget widget;
	Dashboard dash;

	public Dashboard getDash() {
		return dash;
	}

	public void setDash(final Dashboard dash) {
		this.dash = dash;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(final Widget widget) {
		this.widget = widget;
	}

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

	public void setShaker(final long val, final float dist) {
		shaker = val;
		distance = dist;
		if (_duration < val)
			_duration = val;
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

	public void setDuration(final long val) {
		_duration = val;
		final long total = _fadeIn + _fadeOut;
		if (_duration < total)
			_duration = total;
	}

	public Animation(final Widget widg, final long duration) {
		_wait = 20;
		id = widg.getName();
		widget = widg;
		alphaCopy = widg.getAlpha();
		positionCopy = widg.getPosition();
		sizeCopy = widg.getSize();
		count = 0;
		_duration = duration;
		running = false;
	}

	public Animation(final Dashboard dash, final long duration) {
		_wait = 20;
		try {
			widget = dash.getOwner();
			id = widget.getTitle();
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

	int getCount() {
		return count;
	}

	public void trigger() {
		if (!this.running) {
			timeBegin = widget.getParent().millis();
			timeEnd = timeBegin + _duration;
			this.running = true;
		}
		System.out.println(id + " Triggered");
	}

	// Overriding "start()"
	@Override
	public void start() {
		// Set running equal to true
		// running = true;
		timeBegin = widget.getParent().millis();
		timeEnd = timeBegin + _duration;
		// Print messages
		// System.out.println("Starting thread (will execute every " + _wait
		// + " milliseconds for " + _duration + " seconds.)");
		// println("Time is " + timeBegin + " go until:  "+timeEnd+".");
		// Do whatever start does in Thread, don't forget this!
		super.start();
	}

	// We must implement run, this gets triggered by start()
	@Override
	public void run() {
		while (true) {
			// System.out.print(getWidget().getTitle() + " ");
			// System.out.println("Time is " + timeBegin + " go until:  "
			// + timeEnd + "." + running);
			while (running && widget.getParent().millis() < timeEnd) {// (running
																		// &&
																		// count
																		// < 10)
				try {
					// System.out.println("running"); // {
					fadeIn(_fadeIn);
					// wait(_duration-_fadeIn-_fadeOut);
					fadeOut(_fadeOut);
					shake(shaker);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
			running = false;
			// Ok, let's wait for however long we should wait
			try {
				sleep((_wait));
			} catch (final Exception e) {

			}
			try {
				sleep((_wait));
			} catch (final Exception e) {
			}
			// System.out.println(id + " thread is done!"); // The thread is
			// done when we get to the end of run()
		}
	}

	// Our method that quits the thread
	void quit() {
		System.out.println("Quitting.");
		running = false; // Setting running to false ends the loop in run()
		// IUn case the thread is waiting. . .
		interrupt();
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
		final long timeOut = (timeEnd - duration);
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

	void wait(final float duration) {
		if (duration < 1)
			return;
		try {
			sleep((long) (duration));
		} catch (final Exception e) {
		}
	}

	public void clear() {
		widget.setHidden(true);
		running = false;
	}

	public void setRadiate(long sendDuration) {
		// TODO Auto-generated method stub

	}
}
