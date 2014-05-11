package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;

enum Gravity { LEFT, TOP, RIGHT, DOWN, ALL; }

enum Orientation { Horizontal, Vertical; }

public class Plate {
	private final ArrayList<Obstacle> obstacles;
	private final Gravity g;
	final int lineOrRow;
	private final Orientation o;
	
	public Plate(ArrayList<Obstacle> obstacles, Gravity direction, int lineRowNumber, Orientation orientation) {
		this.obstacles = obstacles;
		g = direction;
		lineOrRow = lineRowNumber + 1;
		o = orientation;
	}
	
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public boolean isHorizontal() {
		return o.ordinal() == 0;
	}
	
	public Gravity getGravity() {
		return g;
	}
	
	@Override
	public String toString() {
		ArrayList<Obstacle> obstacles = getObstacles();
		
		StringBuilder sb = new StringBuilder();
		for (Obstacle o : obstacles) {
			sb.append("  + Draw : " + "left: " + o.getX ()+ "top: " + o.getY() + "right: " + (o.getX() + o.getWidth()) + "bottom: " + (o.getY() + o.getHeight()));
		}
		return sb.toString();
	}
}
