package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;

enum Gravity { LEFT, TOP, RIGHT, DOWN, ALL; }

public class Plate {
	private final ArrayList<Obstacle> obstacles;
	private final Gravity orientation;
	final int lineOrRow;
	
	public Plate(ArrayList<Obstacle> obstacles, Gravity direction, int lineRowNumber) {
		this.obstacles = obstacles;
		orientation = direction;
		lineOrRow = lineRowNumber + 1;
	}
	
	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
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
