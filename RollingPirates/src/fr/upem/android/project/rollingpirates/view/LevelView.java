package fr.upem.android.project.rollingpirates.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import fr.upem.android.project.rollingpirates.controller.LevelController;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

	private final Paint paint;
	private SurfaceHolder holder;
	private final char[][] grid;
	private GamePlateModel model = null;
	private LevelController levelController;
	private int preStartStep = 1;
	
	public LevelView(Context context, char[][] grid) {
		super(context);
		this.grid = grid;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		setWillNotDraw(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return levelController.onTouchEvent(event);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
//		super.setOnClickListener(l);
//		levelController.setOnClickListener(l);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("LevelView", "surfaceCreated()");
		Rect surfaceFrame = holder.getSurfaceFrame();
		model = GamePlateModel.init(grid, surfaceFrame.width(), surfaceFrame.height());
		model.register(this);
		levelController = new LevelController(model, this);
		
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			Pirate pirate = pirates.get(i);
			pirate.setSkin(getContext());
		}
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d("LevelView", "onDraw() from LevelView");
		
		if (!levelController.getGameStatus()) {
			if (preStartStep == 1) {
				canvas.drawARGB(255, 255, 255, 255);
		    	model.draw(canvas,getContext(),model);
				displayPreStartInit(canvas);
				
//				preStartStep += 1;
			} else if (preStartStep == 2) {
				
			}
			return;
		}
		canvas.drawARGB(255, 253, 249, 240);
		model.draw(canvas,getContext(),model);
	}
	
	public void drawAtBeginning() {
		Canvas c = holder.lockCanvas();
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
		model.unregister(this);
		model = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
}
