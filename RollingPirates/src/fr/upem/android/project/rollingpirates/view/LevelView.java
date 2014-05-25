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
import fr.upem.android.project.rollingpirates.controller.LevelController;
import fr.upem.android.project.rollingpirates.controller.LevelThread;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

	private final Paint paint;
	private SurfaceHolder holder;
	private final char[][] grid;
	private GamePlateModel model = null;
	private LevelThread levelThread = null;
	private LevelController levelController;
	
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
		return levelController.onTouchEvent(event);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Rect surfaceFrame = holder.getSurfaceFrame();
		model = GamePlateModel.init(grid, surfaceFrame.width(), surfaceFrame.height());
		model.setHolder(holder);
		levelThread = new LevelThread(holder, model, getContext());
		levelController = new LevelController(model, levelThread);
		
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			Pirate pirate = pirates.get(i);
			pirate.setSkin(getContext());
		}
		
		Canvas c = holder.lockCanvas();
		levelThread.draw(c);
		displayPreStartInit(c);
		holder.unlockCanvasAndPost(c);
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
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		levelThread.setRunning(false);
		levelThread.interrupt();
		while (retry) {
			try {
				levelThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		model = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
}
