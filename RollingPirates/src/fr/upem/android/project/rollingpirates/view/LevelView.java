package fr.upem.android.project.rollingpirates.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import fr.upem.android.project.rollingpirates.controller.FightingPirate;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

	private final Paint paint;
	private SurfaceHolder holder;
	private final char[][] grid;
	private GamePlateModel model = null;
	private boolean gameStarted = false;
	private Point player1 = null; 
	private Point player2 = null;
	
	public LevelView(Context context, char[][] grid) {
		super(context);
		this.grid = grid;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!gameStarted) {
			// TODO : launch dynamic animation 
			gameStarted = true;
			launchControllers();
			return true;
		}
		
		int action = MotionEventCompat.getActionMasked(event);

		// Get the index of the pointer associated with the action.
		int index = MotionEventCompat.getActionIndex(event);

		int xPos = (int) MotionEventCompat.getX(event, index);
		int yPos = (int) MotionEventCompat.getY(event, index);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Point point = new Point(xPos, yPos);
			if (xPos <= model.getSurfaceWidth()/2) {
				// Player 1
				Log.d("LevelView", "PLAYER 1: Pushed on surface.");

				player1 = point;
			} else {
				// Player 2
				Log.d("LevelView", "PLAYER 2: Pushed on surface.");

				player2 = point;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (xPos <= model.getSurfaceWidth()/2) {
				if (player1 == null) {
					break;
				}
				
				// Player 1
				Log.d("LevelView", "PLAYER 1: Unpushed on surface.");
				
				float endX = xPos;
				float endY = yPos;
				float startX = player1.x;
				float startY = player1.y;

				Log.d("LevelView", "PLAYER 1: click zone >  startX:" + startX + "  startY:" + startY + "  endX:" + endX + "  endY:" + endY );

				if (isAClick(startX, endX, startY, endY)) { 
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					
					if (model != null) {
						Pirate pirate1 = model.getPirate(1);
						if (pirate1 != null) {
							pirate1.jump();
						}
					}
					
				}
			} else {
				if (player2 == null) {
					break;
				}
				// Player 2
				Log.d("LevelView", "PLAYER 2: Unpushed on surface.");

				float endX = xPos;
				float endY = yPos;
				float startX = player2.x;
				float startY = player2.y;

				Log.d("LevelView", "PLAYER 2: click zone >  startX:" + startX + "  startY:" + startY + "  endX:" + endX + "  endY:" + endY );

				if (isAClick(startX, endX, startY, endY)) { 
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
				}

			}
			break;
		case MotionEvent.ACTION_OUTSIDE:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}

		return true;
	}
	
	private boolean isAClick(float startX, float endX, float startY, float endY) {
	    float differenceX = Math.abs(startX - endX);
	    float differenceY = Math.abs(startY - endY);
	    if (differenceX > 15 || differenceY > 15) {
	    	return false;
	    }
	    return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = holder.lockCanvas();
		
		Rect surfaceFrame = holder.getSurfaceFrame();
		model = GamePlateModel.init(grid, surfaceFrame.width(), surfaceFrame.height());
		model.register(this);
		
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			Pirate pirate = pirates.get(i);
			pirate.setSkin(getContext());
		}
		
//		cachedMap = Bitmap.createBitmap(surfaceFrame.width(), surfaceFrame.height(), Config.ARGB_8888);
//		Canvas c1 = new Canvas(cachedMap);
//		c.drawBitmap(cachedMap, 0, 0, paint);
		drawModel(c);
		
		
		displayPreStartInit(c);
		holder.unlockCanvasAndPost(c);
		
		Log.d("LevelView", "***************");
		Log.d("LevelView", "Surface CREATED");
		Log.d("LevelView", "Surface CREATED");
		Log.d("LevelView", "Surface CREATED");
		Log.d("LevelView", "Surface CREATED");
		Log.d("LevelView", "Surface CREATED");
		Log.d("LevelView", "***************");
	}

	private void launchControllers() {
		// Launch controller Threads which modify the model (give model reference)
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			FightingPirate fightingPirate = new FightingPirate(model, pirates.get(i));
			new Thread(fightingPirate).start();
			// break;
		}
	}
	
	private void displayPreStartInit(Canvas c) {
		Rect surfaceFrame = holder.getSurfaceFrame();
		float exactCenterX = surfaceFrame.exactCenterX();
		float exactCenterY = surfaceFrame.exactCenterY();
		
		Paint paint2 = new Paint();
		paint2.setColor(Color.GRAY);
		paint2.setAlpha(190);
		c.drawPaint(paint2);
		
		
		paint.setColor(Color.GREEN);
		paint.setAlpha(100);
		c.drawRect(model.getLeftScreen(), paint);
		paint.setColor(Color.YELLOW);
		paint.setAlpha(100);
		c.drawRect(model.getRightScreen(), paint);
		
		
		Path path = new Path();
		path.moveTo(exactCenterX-300, exactCenterY);
		path.lineTo(exactCenterX+300, exactCenterY);
		
		paint2.setColor(Color.WHITE);
		paint2.setTextSize(130);
		c.drawTextOnPath("PRESS TO", path, 0, 0, paint2);
		c.drawTextOnPath("START", path, 100, 150, paint2);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
// 		Do not use for now
//		Canvas c = holder.lockCanvas();
//		
//		holder.unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		model.unregister(this);
		model = null;
	}
	
	private void drawModel(Canvas c) {
		c.drawARGB(255, 255, 255, 255);
		
		paint.setColor(Color.BLUE);
		ArrayList<Plate> vplates = model.getVPlates();
		int sizeVplates = vplates.size();
		for (int i = 0; i < sizeVplates; i+=1) {
			ArrayList<Obstacle> obstacles = vplates.get(i).getObstacles();
			
			int sizeObstacle = obstacles.size();
			for (int j = 0; j < sizeObstacle; j+=1) {
				Obstacle o = obstacles.get(j);
				c.drawRect(o.x, o.y, o.x + o.width, o.y + o.height, paint);
			}
		}
		
		paint.setColor(Color.RED);
		ArrayList<Plate> hplates = model.getHPlates();
		int sizeHplates = hplates.size();
		for (int i = 0; i < sizeHplates; i+=1) {
			ArrayList<Obstacle> obstacles = hplates.get(i).getObstacles();
			
			int sizeObstacle = obstacles.size();
			for (int j = 0; j < sizeObstacle; j+=1) {
				Obstacle o = obstacles.get(j);
				c.drawRect(o.x, o.y, o.x + o.width, o.y + o.height, paint);
			}
		}
		
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			Pirate p = pirates.get(i);
			p.draw(c);
		}
	}
	
	public void redrawModel(Pirate p) {
		Canvas c = holder.lockCanvas(p.getPirateRect());
		drawModel(c);
		holder.unlockCanvasAndPost(c);
	}
}
