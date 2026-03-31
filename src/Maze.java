import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Maze extends GraphicsProgram {
	static final int EASY = 1;
	static final int MEDIUM = 2;
	static final int HARD = 3;
	
	private int rows;
	private int cols;
	private int cellSize;
	
	private int grid[][];
	
	private final int DIRS[][] = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}
	};
	
	public void run() {
		setDifficulty(EASY);
		grid = new int [rows][cols];
		generateMaze();
		drawMaze();
	}
	
	public void setDifficulty(int level) { // Changed to public
		switch (level) {
		case EASY:
			rows = 21;
			cols = 21;
			cellSize = 25;
			break;
			
		case MEDIUM:
			rows = 31;
			cols = 31;
			cellSize = 20;
			break;
		
		case HARD:
			rows = 51;
			cols = 51;
			cellSize = 12;
			break;
		}
		// setSize(cols * cellSize + 20, rows *cellSize + 40);
		// removed to avoid the NullPointerException
		this.grid = new int[rows][cols]; // Grid instantiated
	}
	
	void generateMaze() {
		for(int i = 0; i < rows; i++) {
			Arrays.fill(grid[i], 1);
		}
		carve(0, 0);
	}
	
	private void carve(int r, int c) {
		grid[r][c] = 0;
		List<int[]> dirs = new ArrayList<>(Arrays.asList(DIRS));
		Collections.shuffle(dirs);
		
		for (int[] d : dirs) {
            int nr = r + d[0] * 2;
            int nc = c + d[1] * 2;
            
            if (inBounds(nr, nc) && grid[nr][nc] == 1) {
                grid[r + d[0]][c + d[1]] = 0;
                carve(nr, nc);
            }
		}
	}
	
	private boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
	
	private void drawMaze() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == 1) {
                    GRect wall = new GRect(c * cellSize, r * cellSize, cellSize, cellSize);
                    wall.setFilled(true);
                    wall.setFillColor(Color.BLACK);
                    add(wall);
                } else {
                    GRect path = new GRect(c * cellSize, r * cellSize, cellSize, cellSize);
                    path.setFilled(true);
                    path.setFillColor(Color.WHITE);
                    add(path);
                }
            }
        }
    }
	
	public static void main(String[] args) {
	    new Maze().start();
	}
}

