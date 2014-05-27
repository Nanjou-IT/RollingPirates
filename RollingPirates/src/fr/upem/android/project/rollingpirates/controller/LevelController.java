package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.view.LevelView;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class LevelController {
	
	private boolean gameStarted = false;
	private final GamePlateModel model;
	private Point player1 = null; 
	private Point player2 = null;
	private final FightingPirate[] fightingPirates;
	private final Thread[] fightingPiratesWorkers;
	private final LevelView levelView;
	
	public LevelController(GamePlateModel model, LevelView levelView) {
		this.model = model;
		this.levelView = levelView;
		
		ArrayList<Pirate> pirates = model.getPirates();
		this.fightingPirates = new FightingPirate[pirates.size()];
		
		for (int i = 0; i < pirates.size(); i+=1) {
			fightingPirates[i] = new FightingPirate(model, pirates.get(i));
		}
		
		this.fightingPiratesWorkers = new Thread[fightingPirates.length]; 
		for (int i = 0; i < fightingPirates.length; i+=1) {
			fightingPiratesWorkers[i] = new Thread(fightingPirates[i]);
		}
	}
	
	public void setOnClickListener(OnClickListener l) {
		Toast.makeText(levelView.getContext(), "MAIS LOL CLICK", Toast.LENGTH_LONG).show();
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (!gameStarted) {
			// TODO : launch dynamic animation 
			gameStarted = true;
			launchControllers();
			return true;
		}
		
		int action = MotionEventCompat.getActionMasked(event);

		// Get the index of the pointer associated with the action.
		int index = MotionEventCompat.getActionIndex(event);

		int xPos = (int) MotionEventCompat.getX(event, index);
		int yPos = (int) MotionEventCompat.getY(event, index);
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Point point = new Point(xPos, yPos);
			if (xPos <= model.getSurfaceWidth()/2) {
				// Player 1
				Log.d("LevelView", "PLAYER 1: Pushed on surface.");

				player1 = point;
			} else {
				// Player 2
				Log.d("LevelView", "PLAYER 2: Pushed on surface.");

				player2 = point;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (xPos <= model.getSurfaceWidth()/2) {
				if (player1 == null) {
					break;
				}
				
				// Player 1
				Log.d("LevelView", "PLAYER 1: Unpushed on surface.");
				
				float endX = xPos;
				float endY = yPos;
				float startX = player1.x;
				float startY = player1.y;

				Log.d("LevelView", "PLAYER 1: click zone >  startX:" + startX + "  startY:" + startY + "  endX:" + endX + "  endY:" + endY );

				if (isAClick(startX, endX, startY, endY)) { 
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P1 : WE HAVE A CLICK!!");
					
					if (model != null) {
						Pirate pirate1 = model.getPirate(1);
						if (pirate1 != null) {
							for (int i = 0; i < fightingPirates.length; i+=1) {
								int pirateId = fightingPirates[i].getPirateId();
								if (pirateId == Pirate.PLAYER_ONE) {
									fightingPirates[i].setJumping(true);
								}
							}
//							pirate1.jump(model);
						}
					}
				}
			} else {
				if (player2 == null) {
					break;
				}
				// Player 2
				Log.d("LevelView", "PLAYER 2: Unpushed on surface.");
				
				float endX = xPos;
				float endY = yPos;
				float startX = player2.x;
				float startY = player2.y;
				
				Log.d("LevelView", "PLAYER 2: click zone >  startX:" + startX + "  startY:" + startY + "  endX:" + endX + "  endY:" + endY );

				if (isAClick(startX, endX, startY, endY)) { 
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					Log.d("LevelView", "P2 : WE HAVE A CLICK!!");
					
					if (model != null) {
						Pirate pirate2 = model.getPirate(2);
						if (pirate2 != null) {
							for (int i = 0; i < fightingPirates.length; i+=1) {
								int pirateId = fightingPirates[i].getPirateId();
								if (pirateId == Pirate.PLAYER_TWO) {
									fightingPirates[i].setJumping(true);
								}
							}
//							pirate2.jump(model);
						}
					}
				}

			}
			break;
		case MotionEvent.ACTION_OUTSIDE:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}

		return true;
	}
	
	private boolean isAClick(float startX, float endX, float startY, float endY) {
	    float differenceX = Math.abs(startX - endX);
	    float differenceY = Math.abs(startY - endY);
	    if (differenceX > 15 || differenceY > 15) {
	    	return false;
	    }
	    return true;
	}
	
	private void launchControllers() {
		// Launch controller Threads which modify the model
		for (int i = 0; i < fightingPirates.length; i+=1) {
			fightingPiratesWorkers[i].start();
		}
	}
	
	public boolean getGameStatus() {
		return gameStarted;
	}
}
