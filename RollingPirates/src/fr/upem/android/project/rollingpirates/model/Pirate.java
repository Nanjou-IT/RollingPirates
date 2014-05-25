package fr.upem.android.project.rollingpirates.model;


import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import fr.upem.android.project.rollingpirates.R;

public class Pirate {
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private static final int PLAYER_ONE = 0;
	private static final int PLAYER_TWO = 1;

	public float x; // point at left
	public float y; // point at top
	private final int playerCounter;
	float width;
	float height;
	private Paint paint;
	private Bitmap bmp;
	private boolean running;

	private RectF pirateRect;
	private Gravity gravity;
	private Orientation orientation;

	private int lives = 3;
	private int speed = 3;

	public Pirate(float x, float y, Orientation o, Gravity g, int playerCounter) {
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.gravity = g;
		this.playerCounter = playerCounter;
		this.paint = new Paint();
		this.paint.setTextSize(42);
		this.paint.setColor(Color.GREEN);
		pirateRect = new RectF(x, y, x+ width, y + height);
	}
	
	public void setSkin(Context c) {
		if (playerCounter == PLAYER_ONE) {
			bmp = BitmapFactory.decodeResource(c.getResources(),R.drawable.bad2);
		} else if (playerCounter == PLAYER_TWO) {
			bmp = BitmapFactory.decodeResource(c.getResources(),R.drawable.bad3);
		}
		
		setWidth(bmp.getWidth()/BMP_COLUMNS);
		setHeight(bmp.getHeight()/BMP_ROWS);
	}
	
	public void setGravity(Gravity gravity) {
		this.gravity = gravity;
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	public static boolean isPirate(char c) {
		return Character.isDigit(c);
	}
	
	public RectF getPirateRect() {
		return pirateRect;
	}
	
	public Bitmap getBitmap() {
		return bmp;
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
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	@Override
	public String toString() {
		return "width: " + width + "  height: " + height + "  x: " + x + "  y: " + y;
	}

	public void draw(Canvas c) {
		float srcX = getWidth();
		float srcY = getHeight();
		Rect src = new Rect((int)srcX, (int)srcY, (int)srcX + (int)getWidth(), (int)srcY + (int)getHeight());
		RectF dst = new RectF(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		c.drawBitmap(getBitmap(), src, dst, null);
		
		// Print lives counter
		int livesX = 30, livesY = 45;
		if (playerCounter == PLAYER_TWO) {
			livesX = c.getWidth() - 200;
		}
		c.drawText("Lives : " + lives, livesX, livesY, paint);
		Log.d("Pirates", "Lives are drawed");
	}
	
//	public void jump2(GamePlateModel model) {
//		if (orientation == Orientation.Vertical) {
//			RectF collisionRect = new RectF(x + getWidth(), y + speed, getWidth(), getHeight());
//			if (speed + y <= p.getMinY()) {
//				y = p.getMinY();
//				if(checkForCollision(model,collisionRect)){
//					changeDirection();
//					return true;
//				}
//			} 
//			if (speed + y >= p.getMaxY() && checkForCollision(model,collisionRect)) {
//				y = p.getMaxY()-this.height;
//				if(checkForCollision(model,collisionRect)){
//					changeDirection();
//					return true;
//				}
//			}
//			return false;
//		} else if (orientation == Orientation.Horizontal){
//			RectF collisionRect = new RectF(x + getWidth() + speed, y, getWidth(), getHeight());
//			if (speed + x <= p.getMinX() && checkForCollision(model,collisionRect)) {
//				x = p.getMinX();
//				if(checkForCollision(model,collisionRect)){
//					changeDirection();
//					return true;
//				}
//			}
//			if (speed + x >= p.getMaxX() && checkForCollision(model,collisionRect)) {
//				x = p.getMaxX()-this.width;
//				if(checkForCollision(model,collisionRect)){
//					changeDirection();
//					return true;
//				}
//			}
//			return false;
//		}
//	}

	public void jump(GamePlateModel model) {
		Log.d("Pirate", "JUMP");
		int i = 0;
		
		boolean directionRight = false;  // if is not direction == left
		boolean directionBottom = false; // if is not direction == top
		boolean ok = true;
		while (ok) {
			if (orientation == Orientation.Horizontal) {
				if (speed > 0) {
					directionRight = true;
				}
			} else {
				if (speed > 0) {
					directionBottom = true;
				}
			}
			
			
			if (gravity == Gravity.TOP) {
				y += 2;
				if (directionRight) {
					x += 1;
				} else {
					x -= 1;
				}
				if (i == 50) {
					gravity = Gravity.DOWN;
				}
			}
			if (gravity == Gravity.DOWN) {
				y -= 2;
				if (directionRight) {
					x += 1;
				} else {
					x -= 1;
				}
//				if (i == 20) {
//					gravity = Gravity.TOP;
//				}
			}
			if (gravity == Gravity.LEFT) {
				x += 2;
				
				if (directionBottom) {
					y += 1;
				} else {
					y -= 1;
				}
				if (i == 50) {
					gravity = Gravity.RIGHT;
				}
			}
			if (gravity == Gravity.RIGHT) {
				x -= 2;
				
				if (directionBottom) {
					y += 1;
				} else {
					y -= 1;
				}
//				if (i == 20) {
//					gravity = Gravity.LEFT;
//				}
			}
			
			
			pirateRect = new RectF(x, y, x + width, y + height);
			
			SurfaceHolder holder = model.getHolder();
			Canvas lockCanvas = holder.lockCanvas();
			lockCanvas.drawRect(pirateRect, paint);
			holder.unlockCanvasAndPost(lockCanvas);
			
			
			ArrayList<Plate> hPlates = model.getHPlates();
			int sizeHplates = hPlates.size();
			for (int j = 0; j < sizeHplates; j+=1) {
				Plate p = hPlates.get(j);
				if (pirateRect.intersect(p.getPlateRect())) {
					Log.d("Pirate", "CONNECTED HORIZONTAL");
					
//					Log.d("Pirate", "y> " + (y+height));
					Log.d("Pirate", "Pirate.Bottom > Plate.Bottom ?   " + pirateRect.bottom  + ">" + p.getPlateRect().bottom); 
					if ((y+height) < p.getPlateRect().bottom) { // from top
						y = p.getPlateRect().top - height;
						gravity = Gravity.DOWN;
						orientation = Orientation.Horizontal;
						Log.d("Pirate", "C1 : YES");
					} else {
						y = p.getPlateRect().bottom;
						gravity = Gravity.TOP;
						orientation = Orientation.Horizontal;
						Log.d("Pirate", "C2 : NO");
					}
					
					ok = false;
//					return; 
//					break;
				}
			}
			
			ArrayList<Plate> vPlates = model.getVPlates();
			int sizeVplates = vPlates.size();
			for (int j = 0; j < sizeVplates; j+=1) {
				Plate p = vPlates.get(j);
				if (pirateRect.intersect(p.getPlateRect())) {
					Log.d("Pirate", "CONNECTED VERTICAL");

					Log.d("Pirate", "Pirate.Left < Plate.Left ?   " + x + "<" + p.getPlateRect().left);
					if (x < p.getPlateRect().left) { // from left
						x = p.getPlateRect().left - width;
						orientation = Orientation.Vertical;
						Log.d("Pirate", "C1 : YES");
					} else {
						x = p.getPlateRect().right;
						orientation = Orientation.Vertical;
						Log.d("Pirate", "C2 : NO");
					}

					ok = false;
					// return;
					// break;
				}
			}
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) { }
			
			model.updateModel();
			i += 1;
		}
	}
	
	public void update(GamePlateModel model) {
		boolean isObstacle = false;
		
		RectF projectedPirate;
		if (orientation == Orientation.Horizontal) {
			if (speed > 0) { // moving to right
				projectedPirate = new RectF(x + width, y, x + width, y + height);
			} else { // moving to left
				projectedPirate = new RectF(x, y, x, y + height);
			}
		} else {
			if (speed > 0) { // moving to down
				projectedPirate = new RectF(x, y + height, x + width, y + height); // 69
				
				Log.d("Pirate", "REAL ME :   > top: " + y + "  > bottom: " + (y + height));
				Log.d("Pirate", "moving to down :   > top: " + projectedPirate.top + "  > bottom: " + projectedPirate.bottom);
			} else { // moving to up
				projectedPirate = new RectF(x, y+speed, x + width, y+speed);
				
				Log.d("Pirate", "REAL ME :   > top: " + y + "  > bottom: " + (y));
				Log.d("Pirate", "moving to up :   > top: " + projectedPirate.top + "  > bottom: " + projectedPirate.bottom);
			}
		}
		
		
		// Check if 'projectedPirate' is part of obstacles -> intersect !
		ArrayList<Plate> vplates = model.getVPlates();
		int sizeVplates = vplates.size();
		for (int i = 0; i < sizeVplates; i+=1) {
			RectF plateRect = vplates.get(i).getPlateRect();
			
			if (RectF.intersects(plateRect, projectedPirate)) {
				isObstacle = true;
				Log.d("Pirate", "Vertical Obstacle");
				break;
			}
		}
		
		ArrayList<Plate> hplates = model.getHPlates();
		int sizeHplates = hplates.size();
		for (int i = 0; i < sizeHplates; i+=1) {
			RectF plateRect = hplates.get(i).getPlateRect();
			
			if (RectF.intersects(plateRect, projectedPirate)) {
				isObstacle = true;
				Log.d("Pirate", "Horizontal Obstacle");
				
				SurfaceHolder holder = model.getHolder();
				Canvas lockCanvas = holder.lockCanvas();
				paint.setColor(Color.RED);
				lockCanvas.drawRect(pirateRect, paint);
				holder.unlockCanvasAndPost(lockCanvas);
				
				break;
			}
		}
		
		
		if (isObstacle) {
			Log.d("Pirate", "Changing direction > " + speed);
			speed *= -1;
		}
		
		if (orientation == Orientation.Vertical) {
			Log.d("Pirate", "Orientation VERTICAL");
			y += speed;
			pirateRect = new RectF(x, y, x + width, y + height);
			model.updateModel();
			return;
		}
		
		Log.d("Pirate", "Orientation HORIZONTAL");
		x += speed;
		pirateRect = new RectF(x, y, x + width, y + height);
		model.updateModel();
	}
	
}
