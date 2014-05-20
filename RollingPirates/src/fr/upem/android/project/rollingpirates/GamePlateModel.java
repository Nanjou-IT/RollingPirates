package fr.upem.android.project.rollingpirates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import android.graphics.Rect;
import android.graphics.Path.Direction;
import android.util.Log;
import android.widget.Toast;

public class GamePlateModel {
	
	private final static String TAG = "GamePlateModel";
	
	
	private final static int ROW = 28;
	private final static int MIN_LINE = 9;
	private final static int LINE = 15;
	
	private final float CELL_WIDTH;
	private final float CELL_HEIGHT;
	
	private final int widthRatio; 
	private final int heightRatio;
	
	private ArrayList<Plate> hplates;
	private ArrayList<Plate> vplates;
	
	private ArrayList<Plate> plates;
	private final ArrayList<Pirate> players;
	private final ArrayList<Bonus> bonuses;
	
	public GamePlateModel(int cellWidth, int cellHeight, int surfaceWidth, int surfaceHeight) {
		float cellHNumber = (float)surfaceWidth / (float)cellWidth;   	// 1920 / 30 = 64
		float cellVNumber = (float)surfaceHeight / (float)cellHeight; 	// 885 / 30 = 29.5
		
		float accurateWidthRatio  = cellHNumber / (float)ROW;   		// 64 / 28 = 2.28571428  
		float accurateHeightRatio = cellVNumber / (float)LINE;  		// 29.5 / 9 = 3.27777777
		
		widthRatio = (int) accurateWidthRatio;
		heightRatio = (int) accurateHeightRatio;
		
		// Modifications for getting an accurate width / height
		float d = (accurateHeightRatio - (float) heightRatio) / heightRatio;
		CELL_HEIGHT = cellWidth + cellWidth * d;
		d = (accurateWidthRatio - (float) widthRatio) / widthRatio;
		CELL_WIDTH = cellHeight + cellHeight * d;
		
		hplates = new ArrayList<Plate>();
		vplates = new ArrayList<Plate>();
		
		players = new ArrayList<Pirate>();
		bonuses = new ArrayList<Bonus>();
	}
	
	public static GamePlateModel init(char[][] level, int surfaceWidth, int surfaceHeight) {
		GamePlateModel game = new GamePlateModel(30, 30, surfaceWidth, surfaceHeight);
		
		game.hplates.addAll(getHorizontalPlates(game, level));
		game.vplates.addAll(getVerticalPlates(game, level));
		game.players.addAll(getPirates(game, level));
		
		return game;
	}

	public ArrayList<Plate> getHPlates() {
		return hplates;
	}

	public ArrayList<Plate> getVPlates() {
		return vplates;
	}
	
	public ArrayList<Pirate> getPirates() {
		return players;
	}
	
	private static ArrayList<Plate> getHorizontalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 9
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			getLineObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, obstacles);
			
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

	private static void getLineObstacles(GamePlateModel game, char[][] level, float CELL_HEIGHT, 
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
	}
	
	private static ArrayList<Plate> getVerticalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
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
			
			getRowObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, row, obstacles);
			
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

	private static void getRowObstacles(GamePlateModel game, char[][] level,
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
							y += CELL_HEIGHT * game.heightRatio;
							break;
						}
						if (level[line-1][row] != 'x') {
							// ignore non vertical
							y += CELL_HEIGHT * game.heightRatio;
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
	}
	
	private static ArrayList<Pirate> getPirates(GamePlateModel game, char[][] level) {
		ArrayList<Pirate> list = new ArrayList<Pirate>();
		
		// Modifications for getting an accurate width / height
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 15
			getLinePirates(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, list);
			
			x = 0;
			y += game.heightRatio * CELL_HEIGHT; // New line : increment from ratio grid / screen
		}
		
		return list;
	}
	

	private static void getLinePirates(GamePlateModel game, char[][] level,
			float CELL_HEIGHT, float CELL_WIDTH, float x, float y, int line,
			ArrayList<Pirate> pirates) {

		for (int row = 0; row < ROW; row += 1) { // 28
			if (Pirate.isPirate(level[line][row])) {
				for (int i = 0; i < game.widthRatio; i += 1) {
					pirates.add(new Pirate(CELL_WIDTH, CELL_HEIGHT * game.heightRatio, x, y));
					x += CELL_WIDTH;
				}
			} else {
				x += game.widthRatio * CELL_WIDTH;
			}
		}

	}

	/**
	 *  This method will verify some basic properties of the given file (level) :
	 *  	- Same char number per line for each one
	 *  	- Only 9 lines and 28 char per line
	 * 		- No holes on borders
	 * 
	 * It will also resize the given ASCII level, for a better resolution.
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
		
		// Enlarge grid height
		grid = duplicateGridLines(grid);
		
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

	/**
	 * This method will duplicate a line (height oriented), but it will not duplicate 
	 *  horizontal obstacles.
	 * 
	 * @param grid
	 * @param current_line
	 */
	private static char[][] duplicateGridLines(char[][] grid) {
		char newGrid[][] = new char[LINE][ROW];
		
		int newGrid_line = 0;
		for (int current_line = 0; current_line < grid.length; current_line+=1) {
			if (newGrid_line >=  grid.length) {
				break;
			}
			
			newGrid[newGrid_line] = Arrays.copyOf(grid[current_line], grid[current_line].length);
			
			// Modify line if necessary
			ensurePositions(newGrid, newGrid_line);

			if (current_line == 0 || newGrid_line >= LINE-2) {
				newGrid_line += 1;
				continue;
			}
			
			
			newGrid[newGrid_line+1] = Arrays.copyOf(newGrid[newGrid_line], newGrid[newGrid_line].length);
			newGrid_line += 1;
			
			for (int j = 0; j < newGrid[newGrid_line].length; j+=1) {
				boolean erase = false;

				switch (newGrid[newGrid_line][j]) {
				case 'x' :
					if (grid[current_line-1][j] != 'x' && grid[current_line+1][j] != 'x') {
						erase = true;
					}
					break;
				default:
					if (newGrid[newGrid_line][j] != ' ') {
						erase = true;
					}
					break;
				}
				
				if (erase) {
					newGrid[newGrid_line][j] = ' ';
				}
			}
			newGrid_line += 1;
		}
		
		
		return newGrid;
	}

	private static void ensurePositions(char[][] newGrid, int newGrid_line) {
		if (newGrid_line == 0 || newGrid_line >= LINE-2) {
			return;
		}
		
		for (int i = 1; i < newGrid[newGrid_line].length-1; i+=1) {
			char c = newGrid[newGrid_line][i];
			Log.d(TAG, "  >>>>>>>>>>> : " + c);
			if (Pirate.isPirate(c) || Bonus.isBonus(c)) {
				if (newGrid[newGrid_line-1][i] == ' ' && newGrid[newGrid_line][i+1] == ' ' && newGrid[newGrid_line][i-1] == ' ') {
					newGrid[newGrid_line-1][i] = c;
					newGrid[newGrid_line][i] = ' ';
				}
			}
			
		}
		
	}
}
