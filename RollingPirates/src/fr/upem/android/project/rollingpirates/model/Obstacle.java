package fr.upem.android.project.rollingpirates.model;

public class Obstacle {
	public final float width;
	public final float height;
	public final float x; // point at left
	public final float y; // point at top

	public Obstacle(float width, float height, float x, float y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
