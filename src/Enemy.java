import acm.graphics.*;
import java.util.Random;

public class Enemy extends Entity {
    private int damage;
    private float speed;
    private EnemyType type;
    private Random rand = new Random();
    private double width = 20;
    private double height = 20;

    public Enemy(float x, float y, EnemyType type) {
    	// Initialize with placeholder health, set 
    	// specifically in initializeStats
        super(x, y, 0);
        this.type = type;
        initializeStats();
        this.setLocation(x, y);
    }
    
    private void initializeStats() {
    	this.width = 20;
    	this.height = 20;
    	switch (this.type) {
    	case SPIDER:
    		this.health = 10;
    		this.damage = 5;
    		this.speed = 8.0f;
    		break;
    	case SKELETON:
    		this.health = 50;
    		this.damage = 15;
    		this.speed = 2.0f;
    		break;
    	case ALIEN:
    		this.health = 100;
    		this.damage = 25;
    		this.speed = 5.0f;
    		break;
    	case MUTANT: // BOSS
    		this.health = 500;
    		this.damage = 50;
    		this.speed = 3.5f;
    		break;
    	}
    }
    
    // -- GETTERS FOR TESTING PURPOSES --
    /** @return The current health inherited from Entity */
    public int getHealth() {
        return this.health;
    }

    /** @return The movement speed multiplier */
    public float getSpeed() {
        return this.speed;
    }

    /** @return The attack damage value */
    public int getDamage() {
        return this.damage;
    }

    /** @return The specific EnemyType enum value */
    public EnemyType getType() {
        return this.type;
    }

    @Override
    public void move() {
        // Implementation for Entity movement
    }

    public void moveTowardsPlayer(Player player, Maze maze) {
        double dx = player.getX() - getX();
        double dy = player.getY() - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double stepX = (dx / distance) * speed;
            double stepY = (dy / distance) * speed;

         // Try full diagonal movement first
            if (maze.canMoveTo(getX() + stepX, getY() + stepY, width, height)) {
                setLocation(getX() + stepX, getY() + stepY);
            } 
            // If blocked, try moving ONLY horizontally (Sliding along X)
            else if (maze.canMoveTo(getX() + stepX, getY(), width, height)) {
                setLocation(getX() + stepX, getY());
            } 
            // If still blocked, try moving ONLY vertically (Sliding along Y)
            else if (maze.canMoveTo(getX(), getY() + stepY, width, height)) {
                setLocation(getX(), getY() + stepY);
            }
        }
    }
    
    @Override
    public double getWidth() { return width; }
    @Override
    public double getHeight() { return height; }
    
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