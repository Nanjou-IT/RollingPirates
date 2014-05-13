package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;

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
//		Toast.makeText(getContext(), "Surface view is loaded -> height=" + getHeight()/9 + "  width=" + getWidth()/28, Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO : Be sure of "How to do"
		Canvas c = holder.lockCanvas();
		
		Rect surfaceFrame = holder.getSurfaceFrame();

		GamePlateModel model = GamePlateModel.init(grid, surfaceFrame.width(), surfaceFrame.height());
		
        c.drawARGB(255, 255, 255, 255);

		int i = 0;
		ArrayList<Plate> vplates = model.getVPlates();
		for (Plate p : vplates) {
			i += 1;
			Log.d("LevelView", "Plate n°" + p.lineOrRow + "   i = " + i);
			ArrayList<Obstacle> obstacles = p.getObstacles();
			for (Obstacle o : obstacles) {
				Log.d("LevelView", "  + Draw : " + "left: " + o.getX ()+ "top: " + o.getY() + "right: " + (o.getX() + o.getWidth()) + "bottom: " + (o.getY() + o.getHeight()));
				c.drawRect(o.getX(), o.getY(), o.getX() + o.getWidth(), o.getY() + o.getHeight(), paint);
			}
		}
		
		i = 0;
		paint.setColor(Color.RED);
		ArrayList<Plate> hplates = model.getHPlates();
		for (Plate p : hplates) {
			i += 1;
			Log.d("LevelView", "Plate n°" + p.lineOrRow + "   i = " + i);
			ArrayList<Obstacle> obstacles = p.getObstacles();
			for (Obstacle o : obstacles) {
				Log.d("LevelView", "  + Draw : " + "left: " + o.getX ()+ "top: " + o.getY() + "right: " + (o.getX() + o.getWidth()) + "bottom: " + (o.getY() + o.getHeight()));
				c.drawRect(o.getX(), o.getY(), o.getX() + o.getWidth(), o.getY() + o.getHeight(), paint);
			}
		}
		
        holder.unlockCanvasAndPost(c);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
