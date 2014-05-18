package fr.upem.android.project.rollingpirates.model;

public class Pirate {
	private final float width;
	private final float height;
	private final float x; // point at left
	private final float y; // point at top
	
	public Pirate(float width, float height, float x, float y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public static boolean isPirate(char c) {
		return Character.isDigit(c);
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
