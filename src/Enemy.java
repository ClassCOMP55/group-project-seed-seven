import acm.graphics.*;
import java.util.Random;

public class Enemy extends Entity {
    private int damage;
    private float speed;

    public Enemy(float x, float y, int health, int damage, float speed) {
        super(x, y, health);
        this.damage = damage;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.setLocation(x, y);
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
}