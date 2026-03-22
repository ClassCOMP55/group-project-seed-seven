import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.util.*;

public abstract class Maze extends GraphicsProgram {
	private static final int EASY = 1;
	private static final int MEDIUM = 2;
	private static final int HARD = 3;
	
	private int rows;
	private int cols;
	private int cellSize;
	
	private int grid[][];
	
	private final int DIRS[][] = {
			{-1, 0}, {1, 0}, {0, -1}, {0, 1}
	};
}

