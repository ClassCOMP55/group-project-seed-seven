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

}
