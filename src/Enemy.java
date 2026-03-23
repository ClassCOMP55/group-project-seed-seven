import acm.graphics.*;

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

    public static void main(String[] args) {
        System.out.println("--- FORCED COORDINATE TEST ---");

        // 1. Create objects
        Enemy testEnemy = new Enemy(0, 0, 50, 10, 5.0f);
        Player mockPlayer = new Player(0, 0, 100); 

        // 2. MANUALLY FORCE different locations after creation
        testEnemy.setLocation(10.0, 10.0);
        testEnemy.x = 10.0f; // Manually sync internal float
        testEnemy.y = 10.0f;

        mockPlayer.setLocation(50.0, 50.0);

        // 3. Verify before running logic
        System.out.println("Enemy is at: " + testEnemy.getX() + ", " + testEnemy.getY());
        System.out.println("Player is at: " + mockPlayer.getX() + ", " + mockPlayer.getY());

        double distBefore = Math.sqrt(Math.pow(mockPlayer.getX() - testEnemy.getX(), 2) + 
                                     Math.pow(mockPlayer.getY() - testEnemy.getY(), 2));
        
        System.out.println("Calculated DistBefore: " + distBefore);

        if (distBefore > 0) {
            testEnemy.moveTowardsPlayer(mockPlayer);
            System.out.println("New Enemy Position: " + testEnemy.getX() + ", " + testEnemy.getY());
        } else {
            System.out.println("FAILURE: DistBefore is still 0. Check Player.java getX() implementation.");
        }
    }
}