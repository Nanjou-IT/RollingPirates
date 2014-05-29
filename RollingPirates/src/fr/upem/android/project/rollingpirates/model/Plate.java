package fr.upem.android.project.rollingpirates.model;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
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
	private int plateColor = 0;
	
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
	public int getPlateColor() {
		return plateColor;
	}
	public void setPlateColor(int i){
		this.plateColor = i;
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
	
	public void draw(Canvas canvas,Context c,GamePlateModel model) {
		if(plateRect != null) {
			if( Math.round(maxY) == model.getSurfaceHeight() && Math.round(maxX) == model.getSurfaceWidth() ){
				paint.setARGB(255, 229, 219, 36);//yellow
				//paint.setColor(color.my_yellow);
				canvas.drawRect(plateRect,paint);
				setPlateColor(4);
			}
			else if(((Math.round(minY) == 0 && Math.floor(minX) == 0)
					&& 
				(Math.round(maxX) == model.getSurfaceWidth() && Math.round(maxY) != model.getSurfaceHeight())
			||  (Math.round(maxY)== model.getSurfaceHeight() && Math.floor(minX)==0 && Math.round(minY) != 0))){
				paint.setARGB(255, 136, 186, 255);//blue
				//paint.setColor(color.my_blue);
				canvas.drawRect(plateRect,paint);
				setPlateColor(5);
			}
			else{
				if(getPlateColor() != 0){
					if(getPlateColor() == 1) {
						paint.setARGB(255, 234, 93, 69);//red
						canvas.drawRect(plateRect,paint);
					}
					if(getPlateColor() == 2) {
						paint.setARGB(255, 152, 92, 181);//purple
						canvas.drawRect(plateRect,paint);
					}
					if(getPlateColor() == 3) {
						paint.setARGB(255, 136, 233, 144);//green
						canvas.drawRect(plateRect,paint);
					}					
				}else {
					if(new Random().nextInt(3) + 1 == 1) {
						setPlateColor(1);
						paint.setARGB(255, 234, 93, 69);//red
						canvas.drawRect(plateRect,paint);
					}
					if(new Random().nextInt(3) + 1 == 2) {
						setPlateColor(2);
						paint.setARGB(255, 152, 92, 181);//purple
						canvas.drawRect(plateRect,paint);
					}
					if(new Random().nextInt(3) + 1 == 3) {
						setPlateColor(3);
						paint.setARGB(255, 136, 233, 144);//green
						canvas.drawRect(plateRect,paint);
					}
				}
				
			}
			
		}
		//check L du haut
		
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
