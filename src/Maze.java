import java.util.ArrayList;

import acm.graphics.*;
import acm.program.GraphicsProgram;

public class Maze extends GraphicsProgram {
    public static final int EASY = 1;
    public static final int MEDIUM = 2;
    public static final int HARD = 3;
    public static final int BOSS = 4;

    private int rows;
    private int cols;
    private int cellSize;
    private int[][] grid;
    private int currentDifficulty = EASY;

    private double offsetX;
    private double offsetY;

    private GRect[][] tileRects;

    public Maze() {
        offsetX = 0;
        offsetY = 0;
    }

    public void run() {
        build(EASY);
        renderTo(this, new ArrayList<GObject>());
    }

    public void setDifficulty(int level) {
        currentDifficulty = level;
    }

    public void generateMaze() {
        build(currentDifficulty);
    }

    public void build(int level) {
        currentDifficulty = level;
        cellSize = 28;

        int[][] selected;

        if (level == EASY) {
            selected = getEasyMaze();
        } else if (level == MEDIUM) {
            selected = getMediumMaze();
        } else if (level == HARD) {
            selected = getHardMaze();
        } else {
            selected = getBossMaze();
        }

        rows = selected.length;
        cols = selected[0].length;

        grid = new int[rows][cols];
        tileRects = new GRect[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = selected[r][c];
            }
        }

        closeExitGate();
    }

    private int[][] getEasyMaze() {
        return new int[][] {
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
    }

    private int[][] getMediumMaze() {
        return new int[][] {
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
    }

    private int[][] getHardMaze() {
        return new int[][] {
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

    private int[][] getBossMaze() {
        return new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,1,1,0,0,0,1,1,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    private void closeExitGate() {
        int gateCol = cols - 1;
        int gateRow = getExitRow();

        setCell(gateRow - 1, gateCol, 1);
        setCell(gateRow, gateCol, 1);
        setCell(gateRow + 1, gateCol, 1);
    }

    public void openExitGate() {
        int gateCol = cols - 1;
        int gateRow = getExitRow();

        setCell(gateRow - 1, gateCol, 0);
        setCell(gateRow, gateCol, 0);
        setCell(gateRow + 1, gateCol, 0);
    }

    private int getSpawnRow() {
        if (currentDifficulty == EASY) return 1;
        if (currentDifficulty == MEDIUM) return 1;
        if (currentDifficulty == HARD) return 1;
        return 7;
    }

    private int getSpawnCol() {
        return 1;
    }

    private int getEnemyRow() {
        if (currentDifficulty == EASY) return 9;
        if (currentDifficulty == MEDIUM) return 13;
        if (currentDifficulty == HARD) return 9;   // cleaner open lane for alien
        return 7;
    }

    private int getEnemyCol() {
        if (currentDifficulty == HARD) return 13;  // pull alien away from right wall
        return cols - 2;
    }

    private int getExitRow() {
        if (currentDifficulty == EASY) return 9;
        if (currentDifficulty == MEDIUM) return 13;
        if (currentDifficulty == HARD) return 15;
        return 7;
    }

    private void setCell(int row, int col, int value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;

        grid[row][col] = value;

        if (tileRects != null && tileRects[row][col] != null) {
            if (value == 1) {
                tileRects[row][col].setFillColor(java.awt.Color.BLACK);
                tileRects[row][col].setColor(java.awt.Color.BLACK);
            } else {
                tileRects[row][col].setFillColor(java.awt.Color.WHITE);
                tileRects[row][col].setColor(java.awt.Color.WHITE);
            }
        }
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

                tileRects[r][c] = tile;
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
        return offsetX + getSpawnCol() * cellSize + 2;
    }

    public double getPlayerSpawnY() {
        return offsetY + getSpawnRow() * cellSize + 2;
    }

    public double getEnemySpawnX() {
        return offsetX + getEnemyCol() * cellSize + 2;
    }

    public double getEnemySpawnY() {
        return offsetY + getEnemyRow() * cellSize + 2;
    }

    public boolean isPlayerAtExit(double playerX, double playerY) {
        double localX = playerX - offsetX;
        double localY = playerY - offsetY;

        int col = (int)(localX / cellSize);
        int row = (int)(localY / cellSize);

        int exitCol = cols - 1;
        int exitRow = getExitRow();

        return col >= exitCol && Math.abs(row - exitRow) <= 1;
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