import acm.graphics.*;

public abstract class Entity extends GCompound {
	protected float x, y;
	protected int health;
	
	public Entity(float x, float y, int health) {
		this.x = x;
		this.y = y;
		this.health = health;
	}
}
