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
	
	// Handle progression logic and trigger the Boss fight on level 4
	public void nextLevel(Game game) {
		currentLevel++;
		
		if (currentLevel == 4) {
			// Trigger boss fight on level 4
			loadBossLevel();
		} else if (currentLevel < 4) {
			game.endGame();
		} else {
			loadLevel();
		}
	}
	
	// Scales enemy types and counts based on the current level
	public void spawnEnemies() {
		// Clear previous list  to ensure "all enemies defeated" check works
		enemies.clear();
		
		int enemyCount = 2 + currentLevel; // Scaling quantity per level
		for (int i = 0; i < enemyCount; i++) {
			EnemyType type;
			
			// Scaling logic for levels 1-3
			if (currentLevel == 1) {
				type = EnemyType.SPIDER; // Low difficulty
			} else if (currentLevel == 2) {
				type = (i % 2 == 0) ? EnemyType.SPIDER : EnemyType.SKELETON; // Moderate difficulty
			} else {
				type = EnemyType.ALIEN; // High difficulty
			}
			
			// Using coordinates (placeholder 100, 100) and the specific type
			Enemy newEnemy = new Enemy(100.0f, 100.0f, type);
			enemies.add(newEnemy);
		}
	}

	private void loadLevel() {
		maze.generateMaze();
		spawnEnemies();
	}
	
	// Special logic for the MUTANT boss fight on Level 4
	private void loadBossLevel() {
		maze.generateMaze();
		enemies.clear();
		
		// Spawn the MUTANT with 500 HP and 50 offense
		Enemy boss = new Enemy(200.0f, 200.0f, EnemyType.MUTANT);
		enemies.add(boss);
		
		System.out.println("Level 4: Boss Fight Engage!");
		
	}
}
