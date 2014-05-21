package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.util.Log;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.model.Plate;

public class FightingPirate implements Runnable {

	private final GamePlateModel model;
	private final Pirate pirate;
	private int speed = 4;
	
	public FightingPirate(GamePlateModel model, Pirate pirate) {
		this.model = model;
		this.pirate = pirate;
	}
	
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
			
			Log.d("FightingPirate", "Pirate -- Coords > " + pirate.toString());
			
			ArrayList<Plate> hPlates = model.getHPlates();
			for (Plate p : hPlates) {
				if (p.isConnectedTo(pirate)) {
//					Log.d("FightingPirate", "Pirate -- Connected to horizontal > " + p.toString());
					pirate.x += speed;
					break;
				}
				Log.d("FightingPirate", "Plate > " + p.toString());
			}
			Log.d("FightingPirate", "Horizotal plates > " + hPlates.size());
			
			
			ArrayList<Plate> vPlates = model.getVPlates();
			for (Plate p : vPlates) {
				if (p.isConnectedTo(pirate)) {
//					Log.d("FightingPirate", "Pirate -- Connected to vertical > " + p.toString());
					pirate.y += speed;
					break;
				}
				Log.d("FightingPirate", "Plate > " + p.toString());
			}
			Log.d("FightingPirate", "Vertical plates > " + vPlates.size());
			
			
			model.updateModel();
		}
	}

}
