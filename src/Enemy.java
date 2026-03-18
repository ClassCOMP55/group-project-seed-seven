import acm.graphics.*;

public abstract class Enemy extends Entity {
	private int damage;
	private float speed;
	private GImage appearance;

	public Enemy(float x, float y, int health, int damage, float speed) {
		super(x, y, health);
		this.damage = damage;
		this.speed = speed;
		
		// Visual representation for the GamePanel to draw
		appearance = new GImage("enemy.png");
		add(appearance);
	}
	
	@Override
	public void move() {
		// Basic movement placeholder required by Entity
	}
	
	// Implements the chase algorithm
	// Moves the enemy toward the player's current position.
	public void moveTowardsPlayer(Player player) {
		double playerX = player.getX();
		double playerY = player.getY();
		
		// Calculate direction
		float dx = (float) (playerX - this.x);
		float dy = (float) (playerY = this.y);
		
		// Normalize and move based on speed
		double distance = Math.sqrt(dx * dx + dy * dy);
		if (distance > 0) {
			this.x += (dx / distance) * speed;
			this.y += (dy / distance) * speed;
		}
		
		this.setLocation(this.x, this.y);
	}
	
	public void attack(Player player) {
		// Logic for dealing damage to the player
		player.takeDamage(this.damage);
	}

}
