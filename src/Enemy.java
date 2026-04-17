import acm.graphics.*;
import java.util.Random;

public class Enemy extends Entity {
    private int damage;
    private double speed;
    private EnemyType type;
    private Random rand = new Random();
    public static final int MUTANT = 1;

 // 1. Change 'int type' to 'EnemyType type'
    public Enemy(int x, int y, EnemyType type) {
        // 2. Fix the "Implicit super constructor" error by calling super()
        // We pass x, y, and a starting health value (e.g., 100) to the Entity constructor
        super(x, y, 100); 

        this.type = type;
        initializeStats();
        
        // This is where you would normally add your GImage/Sprite
    }

    private void initializeStats() {
        switch (this.type) {
            case SPIDER:
                this.health = 10;
                this.damage = 5;
                this.speed = 3.0f;
                break;
            case SKELETON:
                this.health = 50;
                this.damage = 15;
                this.speed = 1.8f;
                break;
            case ALIEN:
                this.health = 100;
                this.damage = 25;
                this.speed = 2.6f;
                break;
            case MUTANT: // BOSS
                this.health = 500;
                this.damage = 50;
                this.speed = 2.0f;
                break;
        }
    }

    // -- GETTERS FOR TESTING PURPOSES --
    public int getHealth() {
        return this.health;
    }

    public float getSpeed() {
        return (float) this.speed;
    }

    public int getDamage() {
        return this.damage;
    }

    public EnemyType getType() {
        return this.type;
    }

    @Override
    public void move() {
        // Implementation for Entity movement
    }

    // Joshua's original version kept
    public void moveTowardsPlayer(Player player) {
        double playerX = player.getX();
        double playerY = player.getY();

        double dx = playerX - getX();
        double dy = playerY - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double stepX = (dx / distance) * speed;
            double stepY = (dy / distance) * speed;

            setLocation(getX() + stepX, getY() + stepY);
        }
    }

    // Maze-aware version
    public void moveTowardsPlayer(Player player, Maze maze) {
        if (player == null) return;

        double curX = getX();
        double curY = getY();
        double dx = player.getX() - curX;
        double dy = player.getY() - curY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < 1) return;

        // We use a hardcoded speed of 3.0. 
        // This ignores whatever 'this.speed' is set to.
        double stepX = (dx / dist) * 3.0;
        double stepY = (dy / dist) * 3.0;

        // This bypasses maze.canMoveTo entirely.
        // The enemy SHOULD walk through walls straight at you.
        setLocation(curX + stepX, curY + stepY);
    }
    
    private boolean tryMove(
        double nextX,
        double nextY,
        Maze maze,
        double hitboxOffsetX,
        double hitboxOffsetY,
        double hitboxWidth,
        double hitboxHeight
    ) {
        if (maze.canMoveTo(nextX + hitboxOffsetX, nextY + hitboxOffsetY, hitboxWidth - 1, hitboxHeight - 1)) {
            setLocation(nextX, nextY);
            return true;
        }
        return false;
    }

    @Override
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            handleDeath();
        }
    }

    private void handleDeath() {
        Orb droppedOrb = null;
        double roll = rand.nextDouble(); // Generates 0.0 to 1.0

        switch (this.type) {
            case SPIDER:
                if (roll < 0.10) droppedOrb = new Orb(); // 10% chance
                break;
            case SKELETON:
                if (roll < 0.40) droppedOrb = new Orb(); // 40% chance
                break;
            case ALIEN:
                droppedOrb = new Orb(); // 100% chance
                break;
            case MUTANT:
                droppedOrb = new Orb();
                break;
        }

        if (droppedOrb != null) {
            System.out.println(type + " dropped an EXP orb!");
            // Logic to add droppedOrb to the game goes here
        }

        // Remove from parent GCanvas or set inactive
        if (this.getParent() != null) {
            this.getParent().remove(this);
        }
    }
}