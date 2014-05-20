package fr.upem.android.project.rollingpirates.view;

import java.util.ArrayList;

import fr.upem.android.project.rollingpirates.controller.FightingPirate;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

	private final Paint paint;
	private SurfaceHolder holder;
	private final char[][] grid;
	private GamePlateModel model = null;
	
	// TODO : Use MVC over SurfaceView
	public LevelView(Context context, char[][] grid) {
		super(context);
		this.grid = grid;
		paint = new Paint();
		paint.setColor(Color.BLUE);
		holder = getHolder(); // TODO : Look at the doc & Use this shit
		holder.addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = holder.lockCanvas();
		
		Rect surfaceFrame = holder.getSurfaceFrame();
		model = GamePlateModel.init(grid, surfaceFrame.width(), surfaceFrame.height());
		model.register(this);
		drawModel(c);

		// TODO : Launch controller Threads which modify the model (give model reference)
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			FightingPirate fightingPirate = new FightingPirate(model, pirates.get(i));
			new Thread(fightingPirate).start();
//			break;
		}
		
		
        holder.unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
// 		Do not use for now
//		Canvas c = holder.lockCanvas();
//
//		
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

		int i = 0;
		paint.setColor(Color.BLUE);
		ArrayList<Plate> vplates = model.getVPlates();
		for (Plate p : vplates) {
			i += 1;
			ArrayList<Obstacle> obstacles = p.getObstacles();
			for (Obstacle o : obstacles) {
//				Log.d("LevelView", "  + Draw : " + "left: " + o.getX ()+ "top: " + o.getY() + "right: " + (o.getX() + o.getWidth()) + "bottom: " + (o.getY() + o.getHeight()));
				c.drawRect(o.getX(), o.getY(), o.getX() + o.getWidth(), o.getY() + o.getHeight(), paint);
			}
		}
		
		i = 0;
		paint.setColor(Color.RED);
		ArrayList<Plate> hplates = model.getHPlates();
		for (Plate p : hplates) {
			i += 1;
			ArrayList<Obstacle> obstacles = p.getObstacles();
			for (Obstacle o : obstacles) {
//				Log.d("LevelView", "  + Draw : " + "left: " + o.getX ()+ "top: " + o.getY() + "right: " + (o.getX() + o.getWidth()) + "bottom: " + (o.getY() + o.getHeight()));
				c.drawRect(o.getX(), o.getY(), o.getX() + o.getWidth(), o.getY() + o.getHeight(), paint);
			}
		}
		
		i = 0;
		paint.setColor(Color.GREEN);
		ArrayList<Pirate> pirates = model.getPirates();
		for (Pirate p : pirates) {
			i += 1;
			c.drawRect(p.getX(), p.getY(), p.getX() + p.getWidth(), p.getY() + p.getHeight(), paint);
		}
	}
	
	public void redrawModel() {
		Canvas c = holder.lockCanvas();
		drawModel(c);
		holder.unlockCanvasAndPost(c);
	}
}
