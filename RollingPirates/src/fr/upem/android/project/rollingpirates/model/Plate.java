package fr.upem.android.project.rollingpirates.model;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

enum Gravity { LEFT, TOP, RIGHT, DOWN, ALL; }

enum Orientation { Horizontal, Vertical; }

public class Plate {
	private final ArrayList<Obstacle> obstacles;
	private final Gravity g;
	private final Orientation o;
	private final RectF plateRect;
	private final float minX;
	private final float maxX;
	private final float minY;
	private final float maxY;
	private final Paint paint;
	
	public Plate(ArrayList<Obstacle> obstacles, Gravity gravity, int lineRowNumber, Orientation orientation) {
		this.obstacles = obstacles;
		g = gravity;
		o = orientation;
		
		paint = new Paint();
		
		// Obstacle list cannot be empty
		if (obstacles.size() == 0) {
			throw new IllegalStateException("A plate must have obstacles.");
		}
		
		Obstacle obstacle_init = obstacles.get(0);
		float min_x = obstacle_init.getX(), max_x = obstacle_init.getX(), 
			  min_y = obstacle_init.getY(), max_y = obstacle_init.getY();
		
		for (int i = 0; i < obstacles.size(); i+=1) {
			Obstacle obstacle = obstacles.get(i);
			
			if (obstacle.getX() < min_x) {
				min_x = obstacle.getX(); 
			}
			if ((obstacle.getX() + obstacle.getWidth()) > max_x) {
				max_x = obstacle.getX() + obstacle.getWidth(); 
			}
			
			if (obstacle.getY() < min_y) {
				min_y = obstacle.getY(); 
			}
			if ((obstacle.getY() + obstacle.getHeight()) > max_y) {
				max_y = obstacle.getY() + obstacle.getHeight(); 
			}
		}
		
		maxX = max_x;
		minX = min_x;
		maxY = max_y;
		minY = min_y;
		
		plateRect = new RectF(min_x, min_y, max_x, max_y);
	}
	
	public RectF getPlateRect() {
		return plateRect;
	}
	
	public float getMaxY() {
		return maxY;
	}
	
	public float getMaxX() {
		return maxX;
	}
	
	public float getMinX() {
		return minX;
	}
	
	public float getMinY() {
		return minY;
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
	
	public void draw(Canvas canvas) {
		int sizeObstacle = obstacles.size();
		for (int j = 0; j < sizeObstacle; j+=1) {
			Obstacle o = obstacles.get(j);
			canvas.drawRect(o.x, o.y, o.x + o.width, o.y + o.height, paint);
		}
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

	// TODO : problem when a player is on the top/bot left/right edge of the plate (square angle)  
	public boolean isConnectedTo(Pirate pirate) {
		if (((pirate.x + pirate.width) == minX || pirate.x == maxX) && 
			((pirate.y + pirate.height) <= maxY && pirate.y >= minY)) {
			return true;
		}
		if ((pirate.y == maxY || (pirate.y + pirate.height) == minY)  && 
		   ((pirate.x + pirate.width) <= maxX && pirate.x >= minX)) {
			return true;
		}
		
		return false;
	}
}
