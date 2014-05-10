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
	
	private final double widthRatio; 
	private final double heightRatio;
	
	private ArrayList<Plate> hplates;
	private ArrayList<Plate> vplates;
	
	private ArrayList<Plate> plates;
	private final ArrayList<Pirate> players;
	private final ArrayList<Bonus> bonuses;
	
//	private final Object[][] grid;
	
	public GamePlateModel(int cellWidth, int cellHeight, int surfaceWidth, int surfaceHeight) {
		CELL_WIDTH = cellWidth;
		CELL_HEIGHT = cellHeight;
		
		int cellHNumber = surfaceWidth / cellWidth;
		int cellVNumber = surfaceHeight / cellHeight;
		
		widthRatio = cellHNumber / ROW;
		heightRatio = cellVNumber / LINE;
		
		Log.d(TAG, "+ SurfaceWidth : " + surfaceWidth + " cellWidth : " + cellWidth + " cell horizontal number : " + cellHNumber + " widthRatio : " + widthRatio);
		Log.d(TAG, "+ SurfaceHeight : " + surfaceHeight + " cellHeigth : " + cellHeight + " cell vertical number : " + cellVNumber + " heightRatio : " + heightRatio);
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
		
		int CELL_HEIGHT = game.CELL_HEIGHT;
		int CELL_WIDTH = game.CELL_WIDTH;
		
		int x = 0, y = 0;
		boolean justDraw = false;
		for (int line = 0; line < LINE; line += 1) { // 9
			
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			Log.d(TAG, "New obstacle list, EMPTY ? " + obstacles.isEmpty());
			
			for (int row = 0; row < ROW-1; row += 1) { // 28
				
				switch (level[line][row]) {
					case 'x' :
						Log.d(TAG, "found : x");
						if (level[line][row+1] != 'x') {
							if (row == 0) {
								// ignore x at beginning
								break;
							}
							if (level[line][row-1] != 'x') {
								// ignore non horizontal
								break;
							}
						}
						
						for (int i = 0; i < game.widthRatio; i += 1) {
							Log.d(TAG, "  add : x at " + x + ":" + y + " WITH " + game.widthRatio);
							obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
							x = x + CELL_WIDTH;
						}
						if (row == ROW-2) {
							obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
							x = x + CELL_WIDTH;
						}
						break;
					default :
						Log.d(TAG, "found : ' '");
						for (int i = 0; i < game.widthRatio; i += 1) {
							x = x + CELL_WIDTH;
						}
						break;
				}
			}
			x = 0;
			for (int i = 0; i < game.heightRatio; i += 1) {
				y += CELL_HEIGHT;
			}
			Log.d(TAG, "found : new line > x=0 & y=" + y);
			
			if (obstacles.isEmpty()) {
				continue;
			}
			
			switch (line) {
				case 0 :
					Plate plate = new Plate(obstacles, Gravity.TOP, line);
					Log.d(TAG, "NUMBER OF HORIZONTAL OBSTACLES : " + obstacles.size());
					list.add(plate);
					break;
				case LINE-1 :
					Plate plate2 = new Plate(obstacles, Gravity.DOWN, line);
					Log.d(TAG, "NUMBER OF HORIZONTAL OBSTACLES : " + obstacles.size());
					list.add(plate2);
					break;
				default : 
					Plate plate3 = new Plate(obstacles, Gravity.ALL, line);
					Log.d(TAG, "NUMBER OF HORIZONTAL OBSTACLES : " + obstacles.size());
					list.add(plate3);
					break;
			}
		}
		
		
		return list;
	}
	
	private static ArrayList<Plate> getVerticalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		int CELL_HEIGHT = game.CELL_HEIGHT;
		int CELL_WIDTH = game.CELL_WIDTH;
		
		int x = 0, y = 0;
		
		for (int row = 0; row < ROW; row += 1) { // 28
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			for (int line = 0; line < LINE-1; line += 1) { // 9
				Log.d(TAG, level[line][row] + "");
				
				switch (level[line][row]) {
					case 'x' :
						if (level[line+1][row] != 'x') {
							if (line == 0) {
								// ignore x at beginning
								break;
							}
							if (level[line-1][row] != 'x') {
								// ignore non verical
								break;
							}
						}
						
						for (int i = 0; i < game.heightRatio; i += 1) {
							Log.d(TAG, "  add : x at " + x + ":" + y + " WITH " + game.heightRatio );
							obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
							y = y + CELL_HEIGHT;
						}
						break;
					default:
//						y = y + (CELL_HEIGHT * game.heightRatio);
						for (int i = 0; i < game.heightRatio; i += 1) {
							y = y + CELL_HEIGHT;
						}
						break;
				}
			}
			for (int i = 0; i < game.widthRatio; i += 1) {
				x += CELL_WIDTH;
			}
			y = 0;
			
			if (obstacles.isEmpty()) {
				continue;
			}
			
			switch (row) {
			case 0 :
				list.add(new Plate(obstacles, Gravity.TOP, row));
				break;
			case LINE-1 :
				list.add(new Plate(obstacles, Gravity.DOWN, row));
				break;
			default : 
				list.add(new Plate(obstacles, Gravity.ALL, row));
				break;
			}
		}
		
		return list;
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
