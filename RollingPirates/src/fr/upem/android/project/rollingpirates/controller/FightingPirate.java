package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.util.Log;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Obstacle;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class FightingPirate implements Runnable {

	private final GamePlateModel model;
	private final Pirate pirate;
	private final long FPS = 20;
	
	public FightingPirate(GamePlateModel model, Pirate pirate) {
		this.model = model;
		this.pirate = pirate;
	}
	
	@Override
	public void run() {
		long ticksPS = 1000 / FPS; // 10
        long startTime;
        long sleepTime;
        
		while (!Thread.interrupted()) {
			startTime = System.currentTimeMillis();

			boolean updated = false;
			
			
			ArrayList<Plate> vPlates = model.getVPlates();
			int sizeVplates = vPlates.size();
			for (int i = 0; i < sizeVplates; i+=1) {
				Plate p = vPlates.get(i);
				if (p.isConnectedTo(pirate)) {
					pirate.update(model);
					updated = true;
					break;
				}
			}
			
			ArrayList<Plate> hPlates = model.getHPlates();
			int sizeHplates = hPlates.size();
			for (int i = 0; i < sizeHplates; i+=1) {
				Plate p = hPlates.get(i);
				if (p.isConnectedTo(pirate)) {
					pirate.update(model);
					updated = true;
					break;
				}
			}
			
			
			
//			if (updated) {
//				model.updateModel();
				sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
				try {
					if (sleepTime > 0) {
						Thread.sleep(sleepTime);
					} else {
						Thread.sleep(10);
					}
				} catch (Exception e) {}
//			}
		}
	}
}
