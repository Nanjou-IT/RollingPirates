package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class LevelThread extends Thread {
	
	private final static String TAG = "LevelThread";
	
    private final SurfaceHolder surfaceHolder;
    private boolean run = false;
    private final Context context;
    private final GamePlateModel model;
    private final Paint paint;

    /**
     * State-tracking constants.
     */
    public static final int STATE_PLAY = 0;
    public static final int STATE_RUNNING = 1;
    public int mState = STATE_PLAY;
    
    public LevelThread(SurfaceHolder surfaceHolder, GamePlateModel model, Context context) {
        this.surfaceHolder = surfaceHolder;
        this.model = model;
        this.context = context;
		model.register(this);
		paint = new Paint();
    }
 
    public void setRunning(boolean run) {
        this.run = run;
    }
 
    @Override
    public void run() {
        Canvas c;
        
        try {
        	while (!Thread.interrupted()) {
        		while (run) {
        			c = null;
        			try {
        				c = surfaceHolder.lockCanvas(null);
        				synchronized (surfaceHolder) {
        					draw(c);
        				}
        			} finally {
        				if (c != null) {
        					surfaceHolder.unlockCanvasAndPost(c);
        					run = false;
        				}
        			}
        		}
        	}
        } finally {
        	model.unregister(this);
        }
    }
    
    public void draw(Canvas canvas) {
    	canvas.drawARGB(255, 255, 255, 255);

    	paint.setColor(Color.BLUE);
		ArrayList<Plate> vplates = model.getVPlates();
		int sizeVplates = vplates.size();
		for (int i = 0; i < sizeVplates; i+=1) {
			ArrayList<Obstacle> obstacles = vplates.get(i).getObstacles();
			
			int sizeObstacle = obstacles.size();
			for (int j = 0; j < sizeObstacle; j+=1) {
				Obstacle o = obstacles.get(j);
				canvas.drawRect(o.x, o.y, o.x + o.width, o.y + o.height, paint);
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
				canvas.drawRect(o.x, o.y, o.x + o.width, o.y + o.height, paint);
			}
		}
		
		
		ArrayList<Pirate> pirates = model.getPirates();
		for (int i = 0; i < pirates.size(); i+=1) {
			Pirate p = pirates.get(i);
			p.draw(canvas);
		}
    }
    
    public void redrawModel() {
    	setRunning(true);
	}
 
}