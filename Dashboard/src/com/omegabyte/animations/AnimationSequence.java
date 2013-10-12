package com.omegabyte.animations;

import java.util.ArrayList;

public class AnimationSequence extends Thread {
	ArrayList<Animation> sequence = new ArrayList<Animation>();

	public AnimationSequence() {
		super();
	}

	public void add(Animation animation) {
		sequence.add(animation);
	}

	public void add(ArrayList<Animation> animations) {
		sequence.addAll(animations);
	}

	@Override
	public void start() {
		for (Animation animation : sequence) {
			animation.setRecursive(false);// cannot be repeatable.
		}
		super.start();
	}

	@Override
	public void run() {
		for (Animation animation : sequence) {
			// System.out.println("Starting " + animation.getName());
			animation.start();
			while (animation.isAlive()) {
				try {
					sleep((animation.getWait()));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		// System.out.println("DONE.");
	}

	public void trigger() {
		this.start();

	}
}
