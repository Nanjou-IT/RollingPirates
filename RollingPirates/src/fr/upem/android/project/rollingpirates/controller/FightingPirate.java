package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.util.Log;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;

public class FightingPirate implements Runnable {

	private final GamePlateModel model;
	
	public FightingPirate(GamePlateModel model) {
		this.model = model;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			
			Log.d("FightingPirate", "Pirates nb == " + model.getPirates().size());
			
			ArrayList<Pirate> pirates = model.getPirates();
			int i = 0;
			for (Pirate p : pirates) {
				i += 1;
				// Where to move pirates !
				Log.d("FightingPirate", "Pirates n°" + i);
				Log.d("FightingPirate", "Pirates -- Coords > " + p.toString());
			}
			
			model.updateModel(model);
		}
	}

}
