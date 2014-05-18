package fr.upem.android.project.rollingpirates.model;

public class Bonus {
	
	private final float width;
	private final float height;
	private final float x; // point at left
	private final float y; // point at top

	public Bonus(float width, float height, float x, float y) {
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
	
	public static boolean isBonus(char c) {
		return false;
	}
}
