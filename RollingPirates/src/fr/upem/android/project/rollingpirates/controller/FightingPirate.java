package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.util.Log;
import fr.upem.android.project.rollingpirates.model.Bonus;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class FightingPirate implements Runnable {

	private final GamePlateModel model;
	private final Pirate pirate;
	private volatile boolean isJumping = false;
	private final long FPS = 50;
	private final LevelController levelController;
	private boolean run;

	public FightingPirate(GamePlateModel model, Pirate pirate, LevelController levelController) {
		this.model = model;
		this.pirate = pirate;
		this.levelController = levelController;
		this.run = true;
	}
	
	public int getPirateId() {
		return pirate.getPlayerId();
	}
	
	public void setJumping(boolean b) {
		this.isJumping = b;
	}

	public boolean getJumping() {
		return isJumping;
	}

	public void setRunning(boolean b) {
		this.run = b;
	}
	
	@Override
	public void run() {
		long ticksPS = 1000 / FPS; // 10
        long startTime;
        long sleepTime;
        
        ArrayList<Bonus> bonuses = model.getBonuses();
        int sizeBonuses = bonuses.size();
		for (int i = 0; i < sizeBonuses; i+=1) {
			Bonus b = bonuses.get(i);
			b.fall(model);
		}
        
        while (!Thread.interrupted()) {
        	while (run) {
        		startTime = System.currentTimeMillis();

        		boolean pirateMoved = false;
    			
    			ArrayList<Plate> vPlates = model.getVPlates();
    			int sizeVplates = vPlates.size();
    			for (int i = 0; i < sizeVplates; i+=1) {
    				Plate p = vPlates.get(i);
    				if (p.isConnectedTo(pirate)) {
    					int lives = pirate.move(model);
    					pirateMoved = true;
    					if (lives == 0) {
    						levelController.stopGame(pirate.getPlayerId());
    					}
    					break;
    				}
    			}

    			ArrayList<Plate> hPlates = model.getHPlates();
    			int sizeHplates = hPlates.size();
    			for (int i = 0; i < sizeHplates; i+=1) {
    				Plate p = hPlates.get(i);
    				if (p.isConnectedTo(pirate)) {
    					int lives = pirate.move(model);
    					pirateMoved = true;
    					if (lives == 0) {
    						levelController.stopGame(pirate.getPlayerId());
    					}
    					break;
    				}
        		}

    			// A pirate have to jump
        		if (this.isJumping) {
        			Log.d("FightingPirate", "WOOOOOW : I have to jump ??!!!!!  -- from :" + Thread.currentThread().getName());
    				pirate.jump(model);
    				this.isJumping = false;
        		}
        		
        		if (!pirateMoved) {
    				pirate.fall(model);
    			}

        		
        		sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
        		try {
        			if (sleepTime > 0) {
        				Thread.sleep(sleepTime);
        			} else {
        				Thread.sleep(10);
        			}
        		} catch (Exception e) {}
        	}
		}
	}
}
