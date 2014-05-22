package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.util.Log;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class FightingPirate implements Runnable {

	private final GamePlateModel model;
	private final Pirate pirate;
	
	public FightingPirate(GamePlateModel model, Pirate pirate) {
		this.model = model;
		this.pirate = pirate;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			
//			Log.d("FightingPirate", "Pirate -- Coords > " + pirate.toString());

			boolean updated = false;
			
			ArrayList<Plate> hPlates = model.getHPlates();
			for (Plate p : hPlates) {
				if (p.isConnectedTo(pirate)) {
//					Log.d("FightingPirate", "Pirate -- Connected to horizontal > " + p.toString());
					
					pirate.update(model);
					
					updated = true;
					break;
				}
//				Log.d("FightingPirate", "Plate > " + p.toString());
			}
			
			ArrayList<Plate> vPlates = model.getVPlates();
			for (Plate p1 : vPlates) {
				if (p1.isConnectedTo(pirate)) {
//					Log.d("FightingPirate", "Pirate -- Connected to vertical > " + p.toString());
					
					pirate.update(model);
					
					updated = true;
					break;
				}
//				Log.d("FightingPirate", "Plate > " + p1.toString());
			}
			
			if (updated) {
				model.updateModel();
			}
		}
	}
}
