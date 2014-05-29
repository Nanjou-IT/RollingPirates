package fr.upem.android.project.rollingpirates.model;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import fr.upem.android.project.rollingpirates.R;

public class Bonus {
	
	enum Bonuses { 	
		LIFE('l'), NONE('n'); 
		
		private char charBonus;

		private Bonuses(char bonus) {
			this.charBonus = bonus;
		}
		
		public char getCharBonus() {
			return charBonus;
		}
	}
	
	private final float width;
	private final float height;
	private final float x; // point at left
	private float y; // point at top
	private final Bonuses bonusType;
	private Bitmap bonusSkin;
	private RectF bonusRect;

	public Bonus(float width, float height, float x, float y, Bonuses type) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.bonusType = type;
		this.bonusRect = new RectF(x, y, x+ width, y + height);
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
	
	public RectF getBonusRect() {
		return bonusRect;
	}
	
	public static boolean isBonus(char c) {
		if (c == Bonuses.LIFE.getCharBonus()) {
			Log.d("Bonus", "IS A BONUS");
			return true;
		}
		
		return false;
	}
	
	public static Bonuses getBonus(char c) {
		if (c == Bonuses.LIFE.getCharBonus()) {
			return Bonuses.LIFE;
		}
		
		return Bonuses.NONE;
	}
	
	public void fall(GamePlateModel model) {
		
		boolean collision = false;
		while (!collision) {
			// Update falling status
			y += height/3;
			
			bonusRect = new RectF(x, y, x + width, y + height);
			
			collision = isCollision(model);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) { }
			
			
			model.updateModel();
		}
	}
	
	private boolean isCollision(GamePlateModel model) {
		ArrayList<Plate> hPlates = model.getHPlates();
		int sizeHplates = hPlates.size();
		for (int j = 0; j < sizeHplates; j+=1) {
			Plate p = hPlates.get(j);
			if (bonusRect.intersect(p.getPlateRect())) {
				Log.d("Pirate", "CONNECTED HORIZONTAL");
				
				Log.d("Pirate", "Pirate.Bottom > Plate.Bottom ?   " + bonusRect.bottom  + ">" + p.getPlateRect().bottom); 
				if ((y+height) < p.getPlateRect().bottom) { // from top
					y = p.getPlateRect().top - height;
					Log.d("Pirate", "C1 : YES");
					bonusRect = new RectF(x, y, x + width, y + height);
				}
				
				return true;
			}
		}
		return false;
	}
	
	public void setSkin(Context c) {
		if (bonusType == Bonuses.LIFE) {
			bonusSkin = BitmapFactory.decodeResource(c.getResources(), R.drawable.life_gomu_gomu_no_mi);
		}
	}
	
	public Bitmap getBitmap() {
		return bonusSkin;
	}

	public void draw(Canvas canvas) {
		RectF dst = new RectF(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		
		canvas.drawBitmap(getBitmap(), null, dst, null);	
	}

	public void delete(GamePlateModel model) {
		ArrayList<Bonus> bonuses = model.getBonuses();
		for (int i = 0; i < bonuses.size(); i+=1) {
			Bonus b = bonuses.get(i);
			if (b.equals(this)) {
				bonuses.remove(i);
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Bonus)) {
			return false;
		}
		
		Bonus b = (Bonus)o;
		return b.x == x && b.y == y;
	}
}
