package fr.upem.android.project.rollingpirates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class LevelView extends SurfaceView {
	private SurfaceHolder holder;
	private Bitmap bmp;
	
	// TODO : Use MVC over SurfaceView
	public LevelView(Context context) {
		super(context);
		holder = getHolder(); // TODO : Look at the doc & Use this shit
		holder.addCallback(new SurfaceHolder.Callback2() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
			}
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Canvas c = holder.lockCanvas();
				draw(c);
				Log.d("DRAW","la");
		        holder.unlockCanvasAndPost(c);
				invalidate();
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void surfaceRedrawNeeded(SurfaceHolder arg0) {
				// TODO Auto-generated method stub		
			}
		});
		//bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		invalidate();
		Toast.makeText(getContext(), "Surface view is loaded.", Toast.LENGTH_SHORT).show();
		return true;
	}
	public void drawAtPosition(Position p, Canvas canvas) {	
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("DRAW","here");
		Paint p = new Paint(Color.WHITE);
		canvas.drawCircle(100,100, 1000,p );
		
	     /*Paint redPaint = new Paint(Color.RED);
	     redPaint.setTextSize(32);*/
	      //  canvas.drawText("Surface View is inda place", 200, 200, redPaint);
	      //  canvas.drawCircle(100, 100, 30, redPaint);
	}
	class Position {
		private final float x;
		private final float y;
		
		public Position(float x , float y) {
		this.x = x;
		this.y = y;
		}
		public float getX() {
			return x;
		}
		public float getY() {
			return y;
		}
	}
}