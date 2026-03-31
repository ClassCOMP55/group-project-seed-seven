import java.util.ArrayList;
import java.util.List;

// Manages level progression, generates maze, and scales enemy difficulty
// Logic is handled by Joshua and Clark
public class LevelManager {
	private int currentLevel;
	private List<Enemy> enemies;
	private Maze maze;
	
	public LevelManager() {
		this.currentLevel = 1;
		this.enemies = new ArrayList<>();
		this.maze = new Maze();
	}
	
	// Updates level state. If all enemies are defeated, move to the next level.
	public void update(Game game) {
		// Check when all enemies are defeated
		if (enemies.isEmpty()) {
			nextLevel(game);
		}
	}
}
