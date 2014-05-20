package fr.upem.android.project.rollingpirates.model;

public class Pirate {
	public final float width;
	public final float height;
	public float x; // point at left
	public float y; // point at top
	
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
	
	@Override
	public String toString() {
		return "width: " + width + "  height: " + height + "  x: " + x + "  y: " + y;
	}
}
