package fr.upem.android.project.rollingpirates;

public class Obstacle {
	private final int width;
	private final int height;
	private final int x; // point at left
	private final int y; // point at top

	public Obstacle(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
