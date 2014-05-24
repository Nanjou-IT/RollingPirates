package fr.upem.android.project.rollingpirates.model;


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

	private final Rect pirateRect;
	private Gravity gravity;
	private Orientation orientation;

	private int lives = 3;
	private int speed = 1;

	public Pirate(float x, float y, Orientation o, Gravity g, int playerCounter) {
		this.x = x;
		this.y = y;
		this.orientation = o;
		this.gravity = g;
		this.playerCounter = playerCounter;
		this.paint = new Paint();
		this.paint.setTextSize(42);
		this.paint.setColor(Color.GREEN);
		pirateRect = new Rect((int)x, (int)y, (int)x+ (int)width, (int)y + (int)height);
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
	
	public Rect getPirateRect() {
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
		
		int livesX = 30, livesY = 45;
		if (playerCounter == PLAYER_TWO) {
			livesX = c.getWidth() - 200;
		}
		c.drawText("Lives : " + lives, livesX, livesY, paint);
	}
	

	public void jump() {
		Log.d("Pirate", "JUMP");
		y += 3;
	}
	
	public void update(GamePlateModel model) {
//		if ( END OR BEGIN  -- OF THE PLATE ) {
//			speed *= -1;
//		}
		
		if (orientation == Orientation.Vertical) {
			y += speed;
			return;
		}
		
		x += speed;
	}
}
