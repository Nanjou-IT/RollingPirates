package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;

import android.graphics.Rect;
import android.graphics.Path.Direction;
import android.util.Log;
import android.widget.Toast;

public class GamePlateModel {
	
	private final static String TAG = "GamePlateModel";
	
	
	private final static int ROW = 28;
	private final static int LINE = 9;
	
	private final int CELL_WIDTH;
	private final int CELL_HEIGHT;
	
	private final float accurateWidthRatio; 
	private final float accurateHeightRatio;
	
	private final int widthRatio; 
	private final int heightRatio;
	
	private ArrayList<Plate> hplates;
	private ArrayList<Plate> vplates;
	
	private ArrayList<Plate> plates;
	private final ArrayList<Pirate> players;
	private final ArrayList<Bonus> bonuses;
	
//	private final Object[][] grid;
	
	public GamePlateModel(int cellWidth, int cellHeight, int surfaceWidth, int surfaceHeight) {
		CELL_WIDTH = cellWidth;
		CELL_HEIGHT = cellHeight;
		
		float cellHNumber = (float)surfaceWidth / (float)cellWidth;   // 1920 / 30 = 64
		float cellVNumber = (float)surfaceHeight / (float)cellHeight; //  885 / 30 = 29.5
		
		accurateWidthRatio  = cellHNumber / (float)ROW;   //  64 / 28 = 2.28571428  
		accurateHeightRatio = cellVNumber / (float)LINE;  // 29.5 / 9 = 3.27777777
		
		widthRatio = (int) accurateWidthRatio;
		heightRatio = (int) accurateHeightRatio;
		
		Log.d(TAG, "+ SurfaceWidth : " + surfaceWidth + " cellWidth : " + cellWidth + " cell horizontal number : " + cellHNumber + " widthRatio : " + accurateWidthRatio);
		Log.d(TAG, "+ SurfaceHeight : " + surfaceHeight + " cellHeigth : " + cellHeight + " cell vertical number : " + cellVNumber + " heightRatio : " + accurateHeightRatio);
		// 1920, 64 horizontal -- ratio 2
		// 885,  29 vertical   -- ratio 3
		
		hplates = new ArrayList<Plate>();
		vplates = new ArrayList<Plate>();
		
		plates = new ArrayList<Plate>();
		players = new ArrayList<Pirate>();
		bonuses = new ArrayList<Bonus>();
	}
	
	public static GamePlateModel init(char[][] level, int surfaceWidth, int surfaceHeight) {
		GamePlateModel game = new GamePlateModel(30, 30, surfaceWidth, surfaceHeight);
		
		game.hplates.addAll(getHorizontalPlates(game, level));
		game.vplates.addAll(getVerticalPlates(game, level));
		
		return game;
	}

	public ArrayList<Plate> getHPlates() {
		return hplates;
	}

	public ArrayList<Plate> getVPlates() {
		return vplates;
	}
	
	private static ArrayList<Plate> getHorizontalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		// Modifications for getting an accurate width / height
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float d = (game.accurateHeightRatio - (float) game.heightRatio) / game.heightRatio;
		if (d > 0.0F) {
			CELL_HEIGHT += CELL_HEIGHT * d;
		}
		
		float CELL_WIDTH = game.CELL_WIDTH;
		d = (game.accurateWidthRatio - (float) game.widthRatio) / game.widthRatio;
		if (d > 0.0F) {
			CELL_WIDTH += CELL_WIDTH * d;
		}
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 9
			// Patch full screen size rendering for last line
//			if (line == LINE-1) { 
//				y += (game.heightRatio-1) * CELL_HEIGHT;
//			}
			
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			x = getLineObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, obstacles);
			
			x = 0;
			y += game.heightRatio * CELL_HEIGHT; // New line : increment from ratio grid / screen
			
			if (obstacles.isEmpty()) {
				continue;
			}
			
			switch (line) {
			case 0 :
				Plate plate = new Plate(obstacles, Gravity.TOP, line, Orientation.Horizontal);
				list.add(plate);
				break;
			case LINE-1 :
				Plate plate2 = new Plate(obstacles, Gravity.DOWN, line, Orientation.Horizontal);
				list.add(plate2);
				break;
			default : 
				Plate plate3 = new Plate(obstacles, Gravity.ALL, line, Orientation.Horizontal);
				list.add(plate3);
				break;
			}
		}
		
		return list;
	}

	private static float getLineObstacles(GamePlateModel game, char[][] level, float CELL_HEIGHT, 
			float CELL_WIDTH, float x, float y, int line,
			ArrayList<Obstacle> obstacles) {
		for (int row = 0; row < ROW; row += 1) { // 28

			switch (level[line][row]) {
			case 'x' :
				if (row == ROW-1) {
					if(line == 0 || line == LINE-1) {
						for (int i = 0; i < game.widthRatio; i += 1) {
							obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT * game.heightRatio, x, y));
							x += CELL_WIDTH;
						}
					}
					break;
				}

				if (level[line][row+1] != 'x') {
					if (row == 0) {
						// ignore x at beginning
						x += game.widthRatio * CELL_WIDTH;
						break;
					}
					if (level[line][row-1] != 'x') {
						// ignore non horizontal
						x += game.widthRatio * CELL_WIDTH;
						break;
					}
					if (level[line-1][row] == 'x' || level[line+1][row] == 'x') {
						if (line > 1 && line < 7) {
							x += game.widthRatio * CELL_WIDTH;
							break;
						}
					}
				}

				for (int i = 0; i < game.widthRatio; i += 1) {
					obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT * game.heightRatio, x, y));
					x += CELL_WIDTH;
				}
				break;
			default :
				x += game.widthRatio * CELL_WIDTH;
				break;
			}
		}
		
		return x;
	}
	
	private static ArrayList<Plate> getVerticalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float d = (game.accurateHeightRatio - (float) game.heightRatio) / game.heightRatio;
		if (d > 0.0F) {
			CELL_HEIGHT += CELL_HEIGHT * d;
		}
		
		float CELL_WIDTH = game.CELL_WIDTH;
		d = (game.accurateWidthRatio - (float) game.widthRatio) / game.widthRatio;
		if (d > 0.0F) {
			CELL_WIDTH += CELL_WIDTH * d;
		}
		
		float x = 0;
		float y = CELL_HEIGHT;
		
		for (int row = 0; row < ROW; row += 1) { // 28
			// Patch ignoring top & bottom obstacles
			if (row == ROW-1) { 
				y += CELL_HEIGHT * game.heightRatio;
			}
			if (row != 0) { 
				y -= CELL_HEIGHT * game.heightRatio;
			}
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			
			y = getRowObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, row, obstacles);
			
			x += CELL_WIDTH * game.widthRatio;
			y = CELL_HEIGHT * game.heightRatio;
			if (row == ROW-2) {
				y = CELL_HEIGHT;
			}
			
			if (obstacles.isEmpty()) {
				continue;
			}
			
			switch (row) {
			case 0 :
				list.add(new Plate(obstacles, Gravity.TOP, row, Orientation.Vertical));
				break;
			case LINE-1 :
				list.add(new Plate(obstacles, Gravity.DOWN, row, Orientation.Vertical));
				break;
			default : 
				list.add(new Plate(obstacles, Gravity.ALL, row, Orientation.Vertical));
				break;
			}
		}
		
		return list;
	}

	private static float getRowObstacles(GamePlateModel game, char[][] level,
			float CELL_HEIGHT, float CELL_WIDTH, float x, float y, int row,
			ArrayList<Obstacle> obstacles) {
		for (int line = 0; line < LINE-1; line += 1) { // 9
			
			switch (level[line][row]) {
				case 'x' :
					if (line == LINE-2) {
						if(row == 0 || row == ROW-1) {
							for (int i = 0; i < game.heightRatio+1; i += 1) {
								obstacles.add(new Obstacle(game.widthRatio * CELL_WIDTH, CELL_HEIGHT, x, y));
								y += CELL_HEIGHT;
							}
						}
						break;
					}
					
					if (level[line+1][row] != 'x' || ((line+1) == 8)) {
						if (line == 0) {
							// ignore x at beginning
							y += CELL_HEIGHT * game.heightRatio; // TODO : 13/05
							break;
						}
						if (level[line-1][row] != 'x') {
							// ignore non vertical
							y += CELL_HEIGHT * game.heightRatio; // TODO : 13/05
							break;
						}
					}

					for (int i = 0; i < game.heightRatio; i += 1) {
						obstacles.add(new Obstacle(game.widthRatio * CELL_WIDTH, CELL_HEIGHT, x, y));
						y += CELL_HEIGHT;
					}
					break;
				default:
					y += CELL_HEIGHT * game.heightRatio;
					break;
			}
		}
		
		return y;
	}
	

	/**
	 *  This method will verify some basic properties of the given file (level) :
	 *  	- Same char number per line for each one
	 *  	- Only 9 lines and 28 char per line
	 * 		- No holes on borders
	 * 
	 * @param byte buffer input
	 * @return grid of ASCII characters representing the arena
	 */
	public static char[][] check(byte[] data) {
		char[][] grid = new char[LINE][ROW];
		
		String decodedData = new String(data);
		char[] buffer = decodedData.toCharArray();
		
		int current_row = 0, current_line = 0;
		for (int i = 0; i < buffer.length; i += 1) {
			
			if (buffer[i] == '\r') {
				continue;
			}
			
			if (buffer[i] == '\n') {
				current_line += 1;
				current_row = 0;
				continue;
			}
			
			// Make sure that there are no holes on RIGHT side
			if (current_row >= ROW) {
				grid[current_line][current_row-1] = 'x';
				continue;
			}			
			
			if (current_line >= LINE) {
				break;
			}
			
			// Make sure that there are no holes on LEFT side
			if (current_row == 0 && buffer[i] != 'x') {
				grid[current_line][current_row] = 'x';
				current_row += 1;
			}

			grid[current_line][current_row] = buffer[i];
			current_row += 1;
		}
		
		// TODO : Check if grid is "hole-safe" and other stuffs
		
		Log.d(TAG, "++++++++++++++++++++++++++++");
		for (char[] c : grid) {
			StringBuilder sb = new StringBuilder();
			for (char c1 : c) {
				sb.append(c1);
			}
			Log.d(TAG, "INFO : " + sb.toString());
		}
		Log.d(TAG, "++++++++++++++++++++++++++++");
		
		Log.d(TAG, "Line : " + grid.length + "  --  Row : " + grid[0].length);
		
		return grid;
	}
}
