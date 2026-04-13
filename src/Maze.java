import java.util.ArrayList;

import acm.graphics.*;
import acm.program.GraphicsProgram;

public class Maze extends GraphicsProgram {
	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;

	private int rows;
	private int cols;
	private int cellSize;
	private int[][] grid;
	private int currentLevel;

	private double offsetX;
	private double offsetY;

	public Maze() {
		offsetX = 0;
		offsetY = 0;
		currentLevel = EASY;
	}

	public void run() {
		setDifficulty(EASY);
		generateMaze();
		renderTo(this, new ArrayList<GObject>());
	}

	public void setDifficulty(int level) {
		currentLevel = level;

		
		cellSize = 28;
	}

	public void generateMaze() {
		loadPresetMazeByDifficulty();
	}

	public void build(int level) {
		setDifficulty(level);
		generateMaze();
	}

	private void loadPresetMazeByDifficulty() {
		if (currentLevel == EASY) {
			grid = new int[][] {
				{1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,1,0,0,0,0,0,1},
				{1,0,1,1,1,0,1,0,1,1,1,0,1},
				{1,0,1,0,0,0,0,0,0,0,1,0,1},
				{1,0,1,0,1,1,1,1,1,0,1,0,1},
				{1,0,0,0,1,0,0,0,1,0,0,0,1},
				{1,1,1,0,1,0,1,0,1,1,1,0,1},
				{1,0,0,0,1,0,1,0,0,0,1,0,1},
				{1,0,1,1,1,0,1,1,1,0,1,0,1},
				{1,0,0,0,0,0,0,0,1,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1}
			};
		} else if (currentLevel == MEDIUM) {
			grid = new int[][] {
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
				{1,0,1,1,1,0,1,0,1,1,1,1,1,0,1},
				{1,0,1,0,0,0,1,0,1,0,0,0,1,0,1},
				{1,0,1,0,1,1,1,0,1,0,1,0,1,0,1},
				{1,0,1,0,0,0,0,0,1,0,1,0,0,0,1},
				{1,0,1,1,1,1,1,0,1,0,1,1,1,0,1},
				{1,0,0,0,0,0,1,0,1,0,0,0,1,0,1},
				{1,1,1,1,1,0,1,0,1,1,1,0,1,0,1},
				{1,0,0,0,1,0,1,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,0,1,1,1,0,1,0,1,0,1},
				{1,0,1,0,0,0,0,0,1,0,0,0,1,0,1},
				{1,0,1,1,1,1,1,0,1,1,1,1,1,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
			};
		} else {
			grid = new int[][] {
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,1},
				{1,0,1,1,1,0,1,0,1,1,1,0,1,0,1,0,1},
				{1,0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,1,1,0,1,0,1,1,1,0,1,0,1},
				{1,0,1,0,0,0,0,0,1,0,0,0,1,0,1,0,1},
				{1,0,1,1,1,1,1,0,1,1,1,0,1,0,1,0,1},
				{1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,1},
				{1,1,1,1,1,0,1,1,1,0,1,1,1,1,1,0,1},
				{1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1},
				{1,0,1,0,1,1,1,0,1,1,1,0,1,0,1,1,1},
				{1,0,1,0,0,0,1,0,0,0,1,0,1,0,0,0,1},
				{1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1},
				{1,0,0,0,1,0,0,0,0,0,1,0,0,0,1,0,1},
				{1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,0,1},
				{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
			};
		}

		rows = grid.length;
		cols = grid[0].length;
	}

	public void setRenderPosition(double x, double y) {
		offsetX = x;
		offsetY = y;
	}

	public void renderTo(GraphicsProgram screen, ArrayList<GObject> contents) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				GRect tile = new GRect(
					offsetX + c * cellSize,
					offsetY + r * cellSize,
					cellSize,
					cellSize
				);
				tile.setFilled(true);

				if (grid[r][c] == 1) {
					tile.setFillColor(java.awt.Color.BLACK);
					tile.setColor(java.awt.Color.BLACK);
				} else {
					tile.setFillColor(java.awt.Color.WHITE);
					tile.setColor(java.awt.Color.WHITE);
				}

				contents.add(tile);
				screen.add(tile);
			}
		}
	}

	private boolean isWallAtPixel(double px, double py) {
		double localX = px - offsetX;
		double localY = py - offsetY;

		int col = (int)(localX / cellSize);
		int row = (int)(localY / cellSize);

		if (row < 0 || row >= rows || col < 0 || col >= cols) {
			return true;
		}

		return grid[row][col] == 1;
	}

	public boolean canMoveTo(double x, double y, double width, double height) {
		double left = x;
		double right = x + width - 1;
		double top = y;
		double bottom = y + height - 1;

		return !isWallAtPixel(left, top)
			&& !isWallAtPixel(right, top)
			&& !isWallAtPixel(left, bottom)
			&& !isWallAtPixel(right, bottom);
	}

	public double getPlayerSpawnX() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (grid[r][c] == 0) {
					return offsetX + c * cellSize + 2;
				}
			}
		}
		return offsetX + cellSize;
	}

	public double getPlayerSpawnY() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (grid[r][c] == 0) {
					return offsetY + r * cellSize + 2;
				}
			}
		}
		return offsetY + cellSize;
	}

	public double getEnemySpawnX() {
		for (int r = rows - 1; r >= 0; r--) {
			for (int c = cols - 1; c >= 0; c--) {
				if (grid[r][c] == 0) {
					return offsetX + c * cellSize + 2;
				}
			}
		}
		return offsetX + (cols - 2) * cellSize;
	}

	public double getEnemySpawnY() {
		for (int r = rows - 1; r >= 0; r--) {
			for (int c = cols - 1; c >= 0; c--) {
				if (grid[r][c] == 0) {
					return offsetY + r * cellSize + 2;
				}
			}
		}
		return offsetY + (rows - 2) * cellSize;
	}

	public int getCellSize() {
		return cellSize;
	}

	public int getMazeWidth() {
		return cols * cellSize;
	}

	public int getMazeHeight() {
		return rows * cellSize;
	}
}