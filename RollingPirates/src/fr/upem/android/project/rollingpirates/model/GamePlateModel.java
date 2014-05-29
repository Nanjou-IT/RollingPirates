package fr.upem.android.project.rollingpirates.model;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import fr.upem.android.project.rollingpirates.model.Bonus.Bonuses;
import fr.upem.android.project.rollingpirates.view.LevelView;

public class GamePlateModel {
	
	private final static String TAG = "GamePlateModel";
	
	private final static int ROW = 28;
	private final static int LINE = 15;
	
	private final float CELL_WIDTH;
	private final float CELL_HEIGHT;
	
	private final ArrayList<Plate> hplates;
	private final ArrayList<Plate> vplates;
	private final ArrayList<Pirate> players;
	private final ArrayList<Bonus> bonuses;
	
	private final ArrayList<LevelView> views;
	
	private final RectF leftScreen;
	private final RectF rightScreen;
	
	private final int surfaceWidth;
	private final int surfaceheight;

	public GamePlateModel(int cellWidth, int cellHeight, int surfaceWidth, int surfaceHeight) {
		this.surfaceheight = surfaceHeight;
		this.surfaceWidth = surfaceWidth;
		float cellHNumber = (float)surfaceWidth / (float)cellWidth;   	// 1920 / 30 = 64
		float cellVNumber = (float)surfaceHeight / (float)cellHeight; 	// 885 / 30 = 29.5
		
		float accurateWidthRatio  = cellHNumber / (float)ROW;   		// 64 / 28 = 2.28571428  
		float accurateHeightRatio = cellVNumber / (float)LINE;  		// 29.5 / 9 = 3.27777777
		
		int widthRatio = (int) accurateWidthRatio;
		int heightRatio = (int) accurateHeightRatio;
		
		// Modifications for getting an accurate width / height
		float d = (accurateHeightRatio - (float) heightRatio) / heightRatio;
		CELL_HEIGHT = (cellWidth + cellWidth * d) * heightRatio;
		d = (accurateWidthRatio - (float) widthRatio) / widthRatio;
		CELL_WIDTH = (cellHeight + cellHeight * d) * widthRatio;
		
		hplates = new ArrayList<Plate>();
		vplates = new ArrayList<Plate>();
		
		players = new ArrayList<Pirate>();
		bonuses = new ArrayList<Bonus>();
		views = new ArrayList<LevelView>();
		
		leftScreen = new RectF(CELL_WIDTH * widthRatio, CELL_HEIGHT * heightRatio, surfaceWidth/2, surfaceHeight - (CELL_HEIGHT * heightRatio));
		rightScreen = new RectF(surfaceWidth/2, CELL_HEIGHT * heightRatio, surfaceWidth - (CELL_WIDTH * widthRatio), surfaceHeight - (CELL_HEIGHT * heightRatio));
	}

	public static GamePlateModel init(char[][] level, int surfaceWidth, int surfaceHeight) {
		GamePlateModel game = new GamePlateModel(30, 30, surfaceWidth, surfaceHeight);
		
		game.hplates.addAll(getHorizontalPlates(game, level));
		game.vplates.addAll(getVerticalPlates(game, level));
		game.players.addAll(getPirates(game, level));
		game.bonuses.addAll(getBonuses(game, level));
		
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
	
	public ArrayList<Bonus> getBonuses() {
		return bonuses;
	}
	
	/**
	 * Gives a pirate !
	 * 
	 * @param pirateNumber : from 1 to n
	 * @return pirate if exists or null
	 */
	public Pirate getPirate(int pirateNumber) {
		if (pirateNumber > players.size() || pirateNumber <= 0) {
			return null;
		}
		return players.get(pirateNumber-1);
	}

	public ArrayList<Pirate> getOtherPiratesThan(Pirate pirate) {
		ArrayList<Pirate> pirates = new ArrayList<Pirate>();
		
		for (int i = 0; i < players.size(); i+=1) {
			Pirate otherPirate = players.get(i);
			if (!otherPirate.equals(pirate)) {
				pirates.add(otherPirate);
			}
		}
		return pirates;
	}
	
	public RectF getRightScreen() {
		return rightScreen;
	}
	
	public RectF getLeftScreen() {
		return leftScreen;
	}
	
	public int getSurfaceHeight() {
		return surfaceheight;
	}
	
	public int getSurfaceWidth() {
		return surfaceWidth;
	}
	
	private void modelChanged() {
		for (LevelView v : views) {
			v.postInvalidate();
		}
	}
	
	public void register(LevelView levelView) {
		views.add(levelView);
	}

	public void unregister(LevelView levelView) {
		views.remove(levelView);
	}
	
	/**
	 *  Function used outside of the model for ANY update.
	 */
	public void updateModel() {
		modelChanged();
	}
	
	public void draw(Canvas canvas) {
		int sizeHplates = hplates.size();
		for (int i = 0; i < sizeHplates; i+=1) {
			hplates.get(i).draw(canvas);
		}
		
		int sizeVplates = vplates.size();
		for (int i = 0; i < sizeVplates; i+=1) {
			vplates.get(i).draw(canvas);
		}
		
		int sizePirates = players.size();
		for (int i = 0; i < sizePirates; i+=1) {
			players.get(i).draw(canvas);
		}
		
		int sizeBonuses = bonuses.size();
		for (int i = 0; i < sizeBonuses; i+=1) {
			bonuses.get(i).draw(canvas);
		}
	}
	
	//////////////////////////////
	// HORIZONTAL PLATE PARSING //
	//////////////////////////////
	
	private static ArrayList<Plate> getHorizontalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 9
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			boolean plateBuilt = getLineObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, obstacles, list);
			
			x = 0;
			y += CELL_HEIGHT; // New line : increment from ratio grid / screen
			
			if (!obstacles.isEmpty() && !plateBuilt) {
				buildPlate(list, obstacles, line);
			}
		}
		
		return list;
	}

	private static boolean getLineObstacles(GamePlateModel game, char[][] level, float CELL_HEIGHT, 
			float CELL_WIDTH, float x, float y, int line, ArrayList<Obstacle> obstacles, ArrayList<Plate> list) {
		boolean plateBuilt = false;
		for (int row = 0; row < ROW; row += 1) { // 28

			switch (level[line][row]) {
			case 'x' :
				if (row == ROW-1) {
					if(line == 0 || line == LINE-1) {
						obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
						x += CELL_WIDTH;
					}
					break;
				}

				if (level[line][row+1] != 'x') {
					if (row == 0) {
						// ignore x at beginning
						x += CELL_WIDTH;
						break;
					}
					if (level[line][row-1] != 'x') {
						// ignore non horizontal
						x += CELL_WIDTH; 
						break;
					}
					if (level[line-1][row] == 'x' || level[line+1][row] == 'x') {
						if (line > 1 && line < 7) {
							x += CELL_WIDTH;
							break;
						}
					}
				}

				obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
				x += CELL_WIDTH;
				break;
			default :
				x += CELL_WIDTH;
				
				if (obstacles.isEmpty()) {
					break;
				}
				
				buildPlate(list, obstacles, line);
				plateBuilt = true;
				obstacles = new ArrayList<Obstacle>();
				break;
			}
		}
		return plateBuilt;
	}
	
	
	/////////////////////////////
	// VERTICAL PLATE PARSING //
	////////////////////////////
	
	private static ArrayList<Plate> getVerticalPlates(GamePlateModel game, char[][] level) {
		ArrayList<Plate> list = new ArrayList<Plate>();
		
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = CELL_HEIGHT;
		
		for (int row = 0; row < ROW; row += 1) { // 28
			// Patch ignoring top & bottom obstacles
			if (row == ROW-1) { 
				y += CELL_HEIGHT;
			}
			if (row != 0) { 
				y -= CELL_HEIGHT;
			}
			
			ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
			
			boolean plateBuilt = getRowObstacles(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, row, obstacles, list);
			
			x += CELL_WIDTH;
			y = CELL_HEIGHT;
			if (row == ROW-2) {
				y = CELL_HEIGHT;
			}
			
			if (!obstacles.isEmpty() && !plateBuilt) {
				buildPlate(list, obstacles, row);
			}
		}
		
		return list;
	}

	private static boolean getRowObstacles(GamePlateModel game, char[][] level,
			float CELL_HEIGHT, float CELL_WIDTH, float x, float y, int row,
			ArrayList<Obstacle> obstacles, ArrayList<Plate> list) {
		boolean plateBuilt = false;
		for (int line = 0; line < LINE-1; line += 1) { // 9
			
			switch (level[line][row]) {
				case 'x' :
					if (line == LINE-2) {
						if(row == 0 || row == ROW-1) {
							obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
							y += CELL_HEIGHT;
						}
						break;
					}
					
					if (level[line+1][row] != 'x' || ((line+1) == 8)) {
						if (line == 0) {
							// ignore x at beginning
							y += CELL_HEIGHT;
							break;
						}
						if (level[line-1][row] != 'x') {
							// ignore non vertical
							y += CELL_HEIGHT;
							break;
						}
					}
					
					obstacles.add(new Obstacle(CELL_WIDTH, CELL_HEIGHT, x, y));
					y += CELL_HEIGHT;
					break;
				default:
					y += CELL_HEIGHT;
					
					if (obstacles.isEmpty()) {
						break;
					}
					
					buildPlate(list, obstacles, line);
					plateBuilt = true;
					obstacles = new ArrayList<Obstacle>();
					break;
			}
		}
		return plateBuilt;
	}
	
	
	private static void buildPlate(ArrayList<Plate> list, ArrayList<Obstacle> obstacles, int linerow) {
		switch (linerow) {
		case 0 :
			Plate plate = new Plate(obstacles, Gravity.TOP, linerow, Orientation.Horizontal);
			list.add(plate);
			break;
		case LINE-1 :
			Plate plate2 = new Plate(obstacles, Gravity.DOWN, linerow, Orientation.Horizontal);
			list.add(plate2);
			break;
		default : 
			Plate plate3 = new Plate(obstacles, Gravity.ALL, linerow, Orientation.Horizontal);
			list.add(plate3);
			break;
		}
	}
	
	
	
	private static ArrayList<Bonus> getBonuses(GamePlateModel game, char[][] level) {
		ArrayList<Bonus> bonusList = new ArrayList<Bonus>();
		
		// Modifications for getting an accurate width / height
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 15
			getLineBonuses(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, bonusList);
			
			x = 0;
			y += CELL_HEIGHT; // New line : increment from ratio grid / screen
		}
		
		return bonusList;
	}

	private static void getLineBonuses(GamePlateModel game, char[][] level,
			float CELL_HEIGHT, float CELL_WIDTH, float x, float y, int line,
			ArrayList<Bonus> bonuses) {
		
		for (int row = 0; row < ROW; row += 1) { // 28
			if (Bonus.isBonus(level[line][row])) {
				
				Bonuses bonus = Bonus.getBonus(level[line][row]);
				
				bonuses.add(new Bonus(CELL_WIDTH, CELL_HEIGHT, x, y, bonus));
				x += CELL_WIDTH;
			} else {
				x += CELL_WIDTH;
			}
		}
	}
	
	
	
	
	private static ArrayList<Pirate> getPirates(GamePlateModel game, char[][] level) {
		ArrayList<Pirate> piratesList = new ArrayList<Pirate>();
		
		// Modifications for getting an accurate width / height
		float CELL_HEIGHT = game.CELL_HEIGHT;
		float CELL_WIDTH = game.CELL_WIDTH;
		
		float x = 0;
		float y = 0;
		for (int line = 0; line < LINE; line += 1) { // 15
			getLinePirates(game, level, CELL_HEIGHT, CELL_WIDTH, x, y, line, piratesList);
			
			x = 0;
			y += CELL_HEIGHT; // New line : increment from ratio grid / screen
		}
		
		return piratesList;
	}

	private static void getLinePirates(GamePlateModel game, char[][] level,
			float CELL_HEIGHT, float CELL_WIDTH, float x, float y, int line,
			ArrayList<Pirate> pirates) {
		
		for (int row = 0; row < ROW; row += 1) { // 28
			if (Pirate.isPirate(level[line][row])) {
				
				Orientation orientation = getPiratePlateOrientationAndGravity(level, line, row);
				Gravity gravity = getPirateGravity(orientation, level, line, row);;
				
				pirates.add(new Pirate(x, y, orientation, gravity, pirates.size()));
				x += CELL_WIDTH;
			} else {
				x += CELL_WIDTH;
			}
		}
	}
	
	private static Gravity getPirateGravity(Orientation orientation, char[][] level, int line, int row) {
		Gravity gravity = Gravity.DOWN; // default gravity
		
		if (level[line-1][row] == 'x') {
			gravity = Gravity.TOP;
		}
		
		if (level[line+1][row] == 'x') {
			gravity = Gravity.DOWN;
		}
		
		if (level[line][row+1] == 'x') {
			gravity = Gravity.RIGHT;
		}
		
		if (level[line][row-1] == 'x') {
			gravity = Gravity.LEFT;
		}
		
		return gravity;
	}

	private static Orientation getPiratePlateOrientationAndGravity(char[][] level, int line, int row) {
		Orientation orientation = Orientation.Vertical; // default orientation
		
		if (level[line-1][row] == 'x' || level[line+1][row] == 'x') {
			orientation = Orientation.Horizontal;
		}
		
		return orientation;
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
			if (Pirate.isPirate(c) || Bonus.isBonus(c)) {
				
				if (newGrid[newGrid_line-1][i] == ' ' && newGrid[newGrid_line][i+1] == ' ' && newGrid[newGrid_line][i-1] == ' ') {
					
					newGrid[newGrid_line-1][i] = c;
					newGrid[newGrid_line][i] = ' ';
				}
			}
		}
		
	}
}
