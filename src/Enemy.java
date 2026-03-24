import acm.graphics.*;
import java.util.Random;

public class Enemy extends Entity {
    private int damage;
    private float speed;
    private EnemyType type;
    private Random rand = new Random();

    public Enemy(float x, float y, EnemyType type) {
    	// Initialize with placeholder health, set 
    	// specifically in initializeStats
        super(x, y, 0);
        this.type = type;
        initializeStats();
        this.setLocation(x, y);
    }
    
    private void initializeStats() {
    	switch (this.type) {
    	case SPIDER:
    		this.health = 10;
    		this.damage = 5;
    		this.speed = 8.0f;
    		break;
    	case SKELETON:
    		this.health = 100;
    		this.damage = 25;
    		this.speed = 2.0f;
    		break;
    	case ALIEN:
    		this.health = 50;
    		this.damage = 15;
    		this.speed = 5.0f;
    		break;
    	case MUTANT: // BOSS
    		this.health = 500;
    		this.damage = 50;
    		this.speed = 3.5f;
    		break;
    	}
    }

    @Override
    public void move() {
        // Implementation for Entity movement
    }

    public void moveTowardsPlayer(Player player) {
        double playerX = player.getX();
        double playerY = player.getY();

        double dx = playerX - this.x;
        double dy = playerY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            this.x += (float)((dx / distance) * speed);
            this.y += (float)((dy / distance) * speed);
            this.setLocation(this.x, this.y);
        }
    }
    
    @Override
    public void takeDamage(int amount) {
    	this.health -= amount;
    	if (this.health <= 0) {
    		handleDeath();
    	}
    }
    
    private void handleDeath() {
    	
    }
}