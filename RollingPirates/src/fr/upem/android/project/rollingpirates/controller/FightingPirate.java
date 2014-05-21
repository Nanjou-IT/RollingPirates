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
			
			ArrayList<Pirate> pirates = model.getPirates();
			for (Pirate p : pirates) {
				p.update();
			}
			
			model.updateModel(model);
		}
	}
}
