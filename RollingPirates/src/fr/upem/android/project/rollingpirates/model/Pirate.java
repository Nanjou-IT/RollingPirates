package fr.upem.android.project.rollingpirates.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import fr.upem.android.project.rollingpirates.R;

public class Pirate implements Runnable {
	private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int PLAYER_ONE = 0;
	private static final int PLAYER_TWO = 1;

	private float x; // point at left
	private float y; // point at top
	private final int playerCounter;
	private float width;
	private float height;
	private Bitmap bmp;
	private boolean running;
	private GamePlateModel model;
	
	private Gravity gravity;
	private Orientation orientation;
	  
	
	private int speed = 5;
	
	public Pirate(float x, float y, int playerCounter) {
		this.x = x;
		this.y = y;
		this.playerCounter = playerCounter;
	}
	public void setSkin(Context c){
		if(playerCounter == PLAYER_ONE){
			bmp = BitmapFactory.decodeResource(c.getResources(),R.drawable.bad2);
		}
		else if(playerCounter == PLAYER_TWO){
			bmp = BitmapFactory.decodeResource(c.getResources(),R.drawable.bad3);
		}
		setWidth(bmp.getWidth()/BMP_COLUMNS);
		setHeight(bmp.getHeight()/BMP_ROWS);
		
	}
	public void setGravity(Gravity gravity){
		this.gravity = gravity;
	}
	public void setOrientation(Orientation orientation){
		this.orientation = orientation;
	}
	public static boolean isPirate(char c) {
		return Character.isDigit(c);
	}
	public void update() {
		
         if (x > model.getSurfaceWidth() - bmp.getWidth() - speed) {
                speed = -5;
         }
         if (x + speed< 0) {
                speed = 5;
         }
         x = x + speed;
   }
	public Bitmap getBitmap(){
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
	public void setGravity(){
		
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public void setRunning(boolean running){
		this.running = running;
	}
	public void setModel(GamePlateModel model){
		this.model= model; 
	}
	public void draw(Canvas c){
		int srcX =(int) getWidth();
        int srcY =(int) getHeight();
        Rect src = new Rect(srcX, srcY, srcX + (int)getWidth(), srcY + (int)getHeight());
        Rect dst = new Rect((int)getX() , (int)getY() , (int)getX() + (int)getWidth(), (int)getY() + (int)getHeight());
		c.drawBitmap(getBitmap(),src,dst, null);
	}
}