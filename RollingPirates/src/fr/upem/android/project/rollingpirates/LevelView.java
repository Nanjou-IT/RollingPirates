package fr.upem.android.project.rollingpirates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class LevelView extends SurfaceView implements SurfaceHolder.Callback {

	private final Paint paint;
	private SurfaceHolder holder;
	
	// TODO : Use MVC over SurfaceView
	public LevelView(Context context) {
		super(context);
		paint = new Paint();
		holder = getHolder(); // TODO : Look at the doc & Use this shit
		holder.addCallback(this);
		setFocusable(true);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Toast.makeText(getContext(), "Surface view is loaded.", Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO : Be sure of "How to do"
		Canvas c = holder.lockCanvas();
        c.drawARGB(255, 255, 255, 255);
        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setTextSize(32);
        c.drawText("Surface View is inda place", 200, 200, redPaint);
        c.drawCircle(100, 100, 30, redPaint);
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
