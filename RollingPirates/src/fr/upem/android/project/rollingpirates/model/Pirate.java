package fr.upem.android.project.rollingpirates.model;


import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
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
	public static final int PLAYER_ONE = 0;
	public static final int PLAYER_TWO = 1;

	float x; // point at left
	float y; // point at top
	private Gravity gravity;
	private Orientation orientation;
	float width;
	float height;
	
	private final PointF startingPoint;
	private final Gravity startingGravity;
	private final Orientation startingOrientation;
	private final int startingSpeed;
	
	private final int playerCounter;
	private final Paint paint;
	private Bitmap bmp;

	private RectF pirateRect;

	private int lives = 3;
	private int speed = 3;
	private volatile boolean secondJump = false;

	public Pirate(float x, float y, Orientation o, Gravity g, int playerCounter) {
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.gravity = g;
		this.startingPoint = new PointF(x, y);
		this.startingGravity = g;
		this.startingOrientation = o;
		this.startingSpeed = speed;
		
		this.playerCounter = playerCounter;
		this.paint = new Paint();
		this.paint.setTextSize(42);
		this.paint.setColor(Color.GREEN);
		
		pirateRect = new RectF(x, y, x+ width, y + height);
	}
	
	public void setSkin(Context c) {
		if (playerCounter == PLAYER_ONE) {
			bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.bad2);
		} else if (playerCounter == PLAYER_TWO) {
			bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.bad3);
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
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setJumpLevel2(boolean b) {
		this.secondJump = b;
	}
	
	private void speedIncrement() {
		if (speed > 0) {
			speed += 1;
		} else {
			speed -= 1;
		}
	}
	
	public static boolean isPirate(char c) {
		return Character.isDigit(c);
	}
	
	public RectF getPirateRect() {
		return pirateRect;
	}
	
	public int getPlayerId() {
		return playerCounter;
	}
	
	public int getSpeed() {
		return speed;
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
	
	@Override
	public String toString() {
		return "width: " + width + "  height: " + height + "  x: " + x + "  y: " + y;
	}

	public void draw(Canvas c) {
		float srcX = getWidth();
		float srcY = getHeight();
		
		// TODO : Handle this properly
		Rect src = new Rect((int)srcX, (int)0, (int)srcX + (int)getWidth(), (int)getHeight());
		RectF dst = new RectF(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		
		c.drawBitmap(getBitmap(), src, dst, null);
		
		// Print lives counter
		int livesX = 30, livesY = 45;
		if (playerCounter == PLAYER_TWO) {
			livesX = c.getWidth() - 200;
		}
		
		paint.setColor(Color.GREEN);
		c.drawText("Lives : " + lives, livesX, livesY, paint);
	}
	
	public void jump(GamePlateModel model) {
		Log.d("Pirate", "JUMP");
		int i = 0;
		
		boolean directionRight = false;  // if is not direction == left
		boolean directionBottom = false; // if is not direction == top
		boolean collision = false;
		while (!collision && !secondJump) {
			directionRight = false;
			directionBottom = false;
			if (orientation == Orientation.Horizontal) {
				if (speed > 0) {
					directionRight = true;
				}
			} else {
				if (speed > 0) {
					directionBottom = true;
				}
			}
			
			updatingGravityAndDirection(i, directionRight, directionBottom);
			pirateRect = new RectF(x, y, x + width, y + height);
			collision = isCollision(model);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) { }
			
			
			i += 1;
			model.updateModel();
		}
		
		if (secondJump) {
			Log.d("Pirate", "Second jump detected");
			while (!collision) {
				Log.d("Pirate", "jumping level 2");
				directionRight = false;
				directionBottom = false;
				if (orientation == Orientation.Horizontal) {
					if (speed > 0) {
						directionRight = true;
					}
				} else {
					if (speed > 0) {
						directionBottom = true;
					}
				}
				
				// // // // TODO : the famous linear jump ?
				if (gravity == Gravity.TOP) {
					y += 2;
				}
				if (gravity == Gravity.DOWN) {
					y -= 2;
				}
				if (gravity == Gravity.LEFT) {
					x += 2;
				}
				if (gravity == Gravity.RIGHT) {
					x -= 2;
				}
				// // // // TODO : the famous linear jump ?
				
				pirateRect = new RectF(x, y, x + width, y + height);
				collision = isCollision(model);
				
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) { }
				
				
				i += 1;
				model.updateModel();
			}
			secondJump = false;
			speedIncrement();
		}
	}
	
	private void updatingGravityAndDirection(int i, boolean directionRight,
			boolean directionBottom) {
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
		} else if (gravity == Gravity.DOWN) {
			y -= 2;
			if (directionRight) {
				x += 1;
			} else {
				x -= 1;
			}
			if (i == 50) {
				gravity = Gravity.TOP;
			}
		} else if (gravity == Gravity.LEFT) {
			x += 2;
			
			if (directionBottom) {
				y += 1;
			} else {
				y -= 1;
			}
			if (i == 50) {
				gravity = Gravity.RIGHT;
			}
		} else if (gravity == Gravity.RIGHT) {
			x -= 2;
			
			if (directionBottom) {
				y += 1;
			} else {
				y -= 1;
			}
			if (i == 50) {
				gravity = Gravity.LEFT;
			}
		}
	}

	private boolean isCollision(GamePlateModel model) {
		ArrayList<Plate> hPlates = model.getHPlates();
		int sizeHplates = hPlates.size();
		for (int j = 0; j < sizeHplates; j+=1) {
			Plate p = hPlates.get(j);
			if (pirateRect.intersect(p.getPlateRect())) {
				Log.d("Pirate", "CONNECTED HORIZONTAL");
				
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
				
				// Setting up connected && out of bounds patch
				if (x+width >= p.getMaxX()) {
					x = p.getMaxX() - width;
					Log.d("Pirate", "HORIZONTAL | RIGHT  : X > bounds");
					if (speed > 0) {
						speed *= -1;
					}
				}
				
				if (x <= p.getMinX()) {
					x = p.getMinX();
					Log.d("Pirate", "HORIZONTAL | LEFT  : X < bounds");
					if (speed < 0) {
						speed *= -1;
					}
				}
				
				return true;
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
					gravity = Gravity.RIGHT;
					Log.d("Pirate", "C1 : YES");
				} else {
					x = p.getPlateRect().right;
					gravity = Gravity.LEFT;
					orientation = Orientation.Vertical;
					Log.d("Pirate", "C2 : NO");
				}
				
				if (y <= p.getMinY()) {
					y = p.getMinY();
					Log.d("Pirate", "VERTICAL | TOP  : Y > bounds   speed=" + speed);
					// change direction
					if (speed < 0) {
						speed *= -1;
					}
				}
				
				if (y+height >= p.getMaxY()) {
					y = p.getMaxY() - height;
					Log.d("Pirate", "VERTICAL | BOTTOM  : Y < bounds");
					if (speed > 0) {
						speed *= -1;
					}
				}
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Allow a pirate to move around the arena. Note: the pirate can loose
	 * 	a live.
	 * @param model
	 * @return current lives of the pirate
	 */
	public int move(GamePlateModel model) {
		boolean isObstacle = false;
		
		RectF projectedPirate;
		if (orientation == Orientation.Horizontal) {
			if (speed > 0) { // moving to right
				projectedPirate = new RectF(x + width, y, x + width, y + height);
			} else { // moving to left
				projectedPirate = new RectF(x, y, x, y + height);
			}
		} else {
			Log.d("Pirate", "Call to move in vertical !  speed" + speed);
			if (speed > 0) { // moving to down
				projectedPirate = new RectF(x, y + height, x + width, y + height);
			} else { // moving to up
				projectedPirate = new RectF(x, y+speed, x + width, y+speed);
			}
		}
		
		
		// Pirate intersection !
		ArrayList<Pirate> otherPirates = model.getOtherPiratesThan(this);
		for (int i = 0; i < otherPirates.size(); i+=1) {
			Pirate pirate = otherPirates.get(i);
			if (pirateRect.intersect(pirate.getPirateRect())) {
				// Collision
				if (Math.abs(pirate.getSpeed()) > Math.abs(speed)) {
					lives -= 1;
					goToStartingPoint();
					break;
				}
				isObstacle = true;
				break;
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
			
			return lives;
		}
		
		Log.d("Pirate", "Orientation HORIZONTAL");
		x += speed;
		pirateRect = new RectF(x, y, x + width, y + height);
		model.updateModel();
		
		return lives;
	}

	private void goToStartingPoint() {
		x = startingPoint.x;
		y = startingPoint.y;
		orientation = startingOrientation;
		gravity = startingGravity;
		speed = startingSpeed;
	}

	public void fall(GamePlateModel model) {
		int i = 0;
		
		boolean directionRight = false;  // if is not direction == left
		boolean directionBottom = false; // if is not direction == top
		boolean collision = false;
		
		while (!collision) {
			if (orientation == Orientation.Horizontal) {
				if (speed > 0) {
					directionRight = true;
				}
			} else {
				if (speed > 0) {
					directionBottom = true;
				}
			}
			
			// Update falling status
			if (gravity == Gravity.TOP || gravity == Gravity.DOWN) {
				if (i == 0 || i == 1) {
					if (directionRight) {
						x += width/2;
					} else {
						x -= width/2;
					}
				} else {
					if (gravity == Gravity.TOP) {
						y -= height/2;
					}
					if (gravity == Gravity.DOWN) {
						y += height/2;
					}
				}
			}
			
			if (gravity == Gravity.LEFT || gravity == Gravity.RIGHT) {
				if (i == 0 || i == 1) {
					if (directionBottom) {
						y += height/2;
					} else {
						y -= height/2;
					}
				} else {
					if (gravity == Gravity.LEFT) {
						x -= width/2;
					}
					if (gravity == Gravity.RIGHT) {
						x += width/2;
					}
				}
			}
			
			pirateRect = new RectF(x, y, x + width, y + height);
			
			collision = isCollision(model);
			
			try {
				Thread.sleep(35);
			} catch (InterruptedException e) { }
			
			
			i += 1;
			model.updateModel();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pirate)) {
			return false;
		}

		Pirate p = (Pirate)o;
		return p.playerCounter == playerCounter; 
	}
}
