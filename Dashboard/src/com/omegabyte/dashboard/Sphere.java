package com.omegabyte.dashboard;

import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Sphere extends Widget implements Cloneable, Serializable {

	public float rotationZ;

	public void setRotationZ(final float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public Sphere(final PApplet app, final String name) {
		super(app, name);
		setShape(Shape.sphere);
		setAlpha(255);
	}

	public Sphere(final PApplet app) {
		super(app);
		setShape(Shape.sphere);
		setAlpha(255);
	}

	@Override
	public void drawShape() {
		if (texture != null) {
			parent.pushMatrix();
			parent.translate(size.mag() / 2, size.mag() / 2, 0);// size.mag() /
																// 2);
			parent.rotateY(rotationY);
			parent.rotateX(rotationX);
			parent.rotateX(rotationZ);
			// parent.hint(PApplet.ENABLE_DEPTH_TEST);
			texturedSphere(size.mag(), texture);
			// parent.hint(PApplet.DISABLE_DEPTH_TEST);
			parent.popMatrix();
		} else {
			// parent.hint(PApplet.ENABLE_DEPTH_TEST);
			parent.pushMatrix();
			parent.translate(size.mag() / 2, size.mag() / 2, 0);
			parent.rotateY(rotationY);
			parent.rotateX(rotationX);
			parent.rotateX(rotationZ);

			parent.noStroke();
			parent.fill(color, 255);
			parent.translate(0, 0, size.mag() / 2);
			parent.rotateZ(PApplet.HALF_PI);
			parent.shape(getpShape());
			// parent.rotate(orientation);
			// parent.rotateX(-orientation);
			// parent.sphere(size.mag() / 2);
			parent.popMatrix();
			// parent.directionalLight(0, 255, 0, 0, -1, 0);
			// parent.hint(PApplet.DISABLE_DEPTH_TEST);
		}
	}

	@Override
	public void rotate(final float value) {
		if (!rotatable)
			return;
		System.out.println("Rotation: " + value);
		rotationY += value * 3;
	}

	public void rotateX(final float value) {
		System.out.println("Rotation: " + value);
		rotationX += value;
	}

	public void rotateY(final float value) {
		System.out.println("Rotation: " + value);
		rotationY += value;
	}

	public Sphere setCenter(final PVector center) {
		setPosition(center.x - size.mag() / 2, center.y - size.mag() / 2);
		return this;
	}

	int sDetail = 35; // Sphere detail setting
	float rotationX = 0;
	float rotationY = 0;

	public void setRotationX(final float rotationX) {
		System.out.println("rotX: " + rotationX);
		this.rotationX = rotationX;
	}

	public void setRotationY(final float rotationY) {
		System.out.println("rotY: " + rotationY);
		this.rotationY = rotationY;
	}

	float velocityX = 0;
	float velocityY = 0;
	float pushBack = 0;
	int resolution = 200;

	public Sphere setResolution(final int resolution) {
		this.resolution = resolution;
		initializeSphere(resolution);
		return this;
	}

	float[] cx, cz, sphereX, sphereY, sphereZ;
	float sinLUT[];
	float cosLUT[];
	float SINCOS_PRECISION = (float) 0.5;
	int SINCOS_LENGTH = (int) (360.0 / SINCOS_PRECISION);

	void initializeSphere(final int res) {
		sinLUT = new float[SINCOS_LENGTH];
		cosLUT = new float[SINCOS_LENGTH];

		for (int i = 0; i < SINCOS_LENGTH; i++) {
			sinLUT[i] = (float) Math.sin(i * PApplet.DEG_TO_RAD
					* SINCOS_PRECISION);
			cosLUT[i] = (float) Math.cos(i * PApplet.DEG_TO_RAD
					* SINCOS_PRECISION);
		}

		final float delta = (float) SINCOS_LENGTH / res;
		final float[] cx = new float[res];
		final float[] cz = new float[res];

		// Calc unit circle in XZ plane
		for (int i = 0; i < res; i++) {
			cx[i] = -cosLUT[(int) (i * delta) % SINCOS_LENGTH];
			cz[i] = sinLUT[(int) (i * delta) % SINCOS_LENGTH];
		}

		// Computing vertexlist vertexlist starts at south pole
		final int vertCount = res * (res - 1) + 2;
		int currVert = 0;

		// Re-init arrays to store vertices
		sphereX = new float[vertCount];
		sphereY = new float[vertCount];
		sphereZ = new float[vertCount];
		final float angle_step = (SINCOS_LENGTH * 0.5f) / res;
		float angle = angle_step;

		// Step along Y axis
		for (int i = 1; i < res; i++) {
			final float curradius = sinLUT[(int) angle % SINCOS_LENGTH];
			final float currY = -cosLUT[(int) angle % SINCOS_LENGTH];
			for (int j = 0; j < res; j++) {
				sphereX[currVert] = cx[j] * curradius;
				sphereY[currVert] = currY;
				sphereZ[currVert++] = cz[j] * curradius;
			}
			angle += angle_step;
		}
		sDetail = res;
	}

	// Generic routine to draw textured sphere
	void texturedSphere(float r, final PImage t) {
		r = r / 2;// sizing is weird
		parent.noStroke();
		parent.noFill();
		int v1, v11, v2;
		r = (float) ((r + 240) * 0.33);
		parent.beginShape(PApplet.TRIANGLE_STRIP);
		parent.texture(t);
		final float iu = (float) (t.width - 1) / (sDetail);
		final float iv = (float) (t.height - 1) / (sDetail);
		float u = 0, v = iv;
		for (int i = 0; i < sDetail; i++) {
			parent.vertex(0, -r, 0, u, 0);
			parent.vertex(sphereX[i] * r, sphereY[i] * r, sphereZ[i] * r, u, v);
			u += iu;
		}
		parent.vertex(0, -r, 0, u, 0);
		parent.vertex(sphereX[0] * r, sphereY[0] * r, sphereZ[0] * r, u, v);
		parent.endShape();

		// Middle rings
		int voff = 0;
		for (int i = 2; i < sDetail; i++) {
			v1 = v11 = voff;
			voff += sDetail;
			v2 = voff;
			u = 0;
			parent.beginShape(PApplet.TRIANGLE_STRIP);
			parent.texture(t);
			for (int j = 0; j < sDetail; j++) {
				parent.vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1++]
						* r, u, v);
				parent.vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2++]
						* r, u, v + iv);
				u += iu;
			}

			// Close each ring
			v1 = v11;
			v2 = voff;
			parent.vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1] * r, u,
					v);
			parent.vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2] * r, u,
					v + iv);
			parent.endShape();
			v += iv;
		}
		u = 0;

		// Add the northern cap
		parent.beginShape(PApplet.TRIANGLE_STRIP);
		parent.texture(t);
		for (int i = 0; i < sDetail; i++) {
			v2 = voff + i;
			parent.vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2] * r, u,
					v);
			parent.vertex(0, r, 0, u, v + iv);
			u += iu;
		}
		parent.vertex(sphereX[voff] * r, sphereY[voff] * r, sphereZ[voff] * r,
				u, v);
		parent.endShape();

	}

	@Override
	public Sphere setAlpha(final float alpha) {
		super.setAlpha(alpha);
		return this;
	}

	@Override
	public Sphere setTextColor(final int colour) {
		super.setTextColor(colour);
		return this;
	}

	@Override
	public Sphere setFixed(final boolean fixed) {
		super.setFixed(fixed);
		return this;
	}

	@Override
	public Sphere setHidden(final boolean val) {
		super.setHidden(val);
		return this;
	}

	@Override
	public Sphere setRotatable(final boolean val) {
		super.setRotatable(val);
		return this;
	}

	@Override
	public Sphere setMovable(final boolean val) {
		super.setMovable(val);
		return this;
	}

	@Override
	public Sphere setPosition(final float x, final float y) {
		super.setPosition(x, y);
		return this;
	}

	@Override
	public Sphere setPosition(final PVector position) {
		super.setPosition(position);
		return this;
	}

	@Override
	public Sphere setEmpty(final boolean empty) {
		super.setEmpty(empty);
		return this;
	}

	@Override
	public Sphere setName(final String name) {
		super.setName(name);
		return this;
	}

	@Override
	public Sphere setSize(final PVector size) {
		super.setSize(size);
		return this;
	}

	@Override
	public Sphere setSize(final float x, final float y) {
		super.setSize(x, y);
		return this;
	}

	@Override
	public Sphere setSize(final float rad) {
		super.setSize(rad);
		return this;
	}

	@Override
	public Sphere setSelectable(final boolean val) {
		super.setSelectable(val);
		return this;
	}

	@Override
	public Sphere setShape(final Shape shape) {
		super.setShape(shape);
		return this;
	}

	@Override
	public Sphere setScalable(final boolean val) {
		super.setScalable(val);
		return this;
	}

	@Override
	public Sphere setScalable(final float min, final float max) {
		super.setScalable(min, max);
		return this;
	}

	@Override
	public Sphere setTitle(final String title) {
		super.setTitle(title);
		return this;
	}

	@Override
	public Sphere setText(final String text) {
		super.setText(text);
		return this;
	}

	@Override
	public Sphere setCorner(final float cornerRad) {
		super.setCorner(cornerRad);
		return this;
	}

	@Override
	public Sphere setMaxSize(final float maxSize) {
		super.setMaxSize(maxSize);
		return this;
	}

	@Override
	public Sphere setMinSize(final float minSize) {
		super.setMinSize(minSize);
		return this;
	}

	@Override
	public Sphere setTextSize(final float size) {
		super.setTextSize(size);
		return this;
	}

	@Override
	public Sphere setTitleSize(final float size) {
		super.setTitleSize(size);
		return this;
	}

	@Override
	public Sphere setOwner(final Dashboard dashboard) {
		super.setOwner(dashboard);
		return this;
	}

}
