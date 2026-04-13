import acm.graphics.*;

public abstract class Entity extends GCompound {
	protected int health;
	
	public Entity(float x, float y, int health) {
		this.health = health;
		setLocation(x, y);
	}
	
	public abstract void move();
	
	public void takeDamage(int amount) {
		health -= amount;
		if (health <= 0) {
			// Handle entity death
		}
	}
}
