package fr.upem.android.project.rollingpirates.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import fr.upem.android.project.rollingpirates.LoadLevelActivity;
import fr.upem.android.project.rollingpirates.MainActivity;
import fr.upem.android.project.rollingpirates.R;
import fr.upem.android.project.rollingpirates.model.GamePlateModel;
import fr.upem.android.project.rollingpirates.model.Pirate;
import fr.upem.android.project.rollingpirates.view.LevelView;

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
			fightingPirates[i] = new FightingPirate(model, pirates.get(i), this);
		}
		
		this.fightingPiratesWorkers = new Thread[fightingPirates.length]; 
		for (int i = 0; i < fightingPirates.length; i+=1) {
			fightingPiratesWorkers[i] = new Thread(fightingPirates[i]);
		}
	}

	public static OnClickListener onClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("Controller", "HUHU click Handled ! ");
			}
		};
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
									if (fightingPirates[i].getJumping()) {
										pirate1.setJumpLevel2(true);
									} else {
										fightingPirates[i].setJumping(true);
									}
//									pirate1.setJumpLevel2(false);
								}
							}
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
									if (fightingPirates[i].getJumping()) {
										pirate2.setJumpLevel2(true);
									} else {
										fightingPirates[i].setJumping(true);
									}
								}
							}
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
	
	public void stopControllers() {
		Log.d("Controller", "Stopping controllers");
		// Stop controller Threads which modify the model
		for (int i = 0; i < fightingPirates.length; i+=1) {
			fightingPirates[i].setRunning(false);
			fightingPiratesWorkers[i].interrupt();
		}
	}
	
	public boolean getGameStatus() {
		return gameStarted;
	}

	public void stopGame(final int pirateLooserId) {
		stopControllers();
		
		
		((Activity)levelView.getContext()).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				final Animation fadeIn = new AlphaAnimation(0.2f, 1);
				fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
				fadeIn.setDuration(3000);

				Animation fadeOut = new AlphaAnimation(1, 0.2f);
				fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
				fadeOut.setDuration(2000);
				
				AnimationSet animation = new AnimationSet(false); //change to false
				animation.addAnimation(fadeOut);
				
				levelView.startAnimation(animation);
				
				fadeOut.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation animation) {
						levelView.startAnimation(fadeIn);
					}

					@Override
					public void onAnimationStart(Animation animation) {	}

					@Override
					public void onAnimationRepeat(Animation animation) { }
				});
				
				fadeIn.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation animation) {
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						Log.d("Controller", "Annimation start !");
						LayoutInflater layoutInflater = LayoutInflater.from(levelView.getContext()); 
						View popupView = layoutInflater.inflate(R.layout.popup, null);  
						
						TextView winnerText = (TextView) popupView.findViewById(R.id.textCongratulation);
						
						int pirateWinner = 1;
						if (pirateLooserId == Pirate.PLAYER_ONE) {
							pirateWinner = 2;
						}
						winnerText.setText("Congratz Player "+ pirateWinner +" won !");
						
			            final PopupWindow popupWindow = new PopupWindow(popupView, model.getSurfaceWidth()/2, model.getSurfaceHeight()/3);  
			            
			            
			            Log.d("Controller", "Show popup dimension :  width;" + popupWindow.getWidth()+ " height:" + popupWindow.getHeight());
			            
			            int xpos = model.getSurfaceWidth()/2 - popupWindow.getWidth()/2;
			            int ypos = -(model.getSurfaceHeight()/2 + popupWindow.getHeight()/2);
			            Log.d("Controller", "Show popup at x: " + xpos + " y:" + ypos);
			            
			            
			            Button btnRestart = (Button)popupView.findViewById(R.id.restartButton);
			            btnRestart.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								popupWindow.dismiss();
								Intent i = new Intent(levelView.getContext().getApplicationContext() ,LoadLevelActivity.class);
								((Activity)levelView.getContext()).finish();
								levelView.getContext().startActivity(i);
							}
						});
			            
			            Button btnMenu = (Button)popupView.findViewById(R.id.backMainMenuButton);
			            btnMenu.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								popupWindow.dismiss();
								// TODO : Quit properly
								Intent i = new Intent(levelView.getContext().getApplicationContext() ,MainActivity.class);
								((Activity)levelView.getContext()).finish();
								levelView.getContext().startActivity(i);
							}
						});
			            
			            
			            popupWindow.showAsDropDown(levelView, xpos, ypos);
					}

					@Override
					public void onAnimationRepeat(Animation animation) { }
				});
			}
		});
	}
}
