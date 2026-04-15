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
        return this.speed;
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
        if (player == null || maze == null) return;

        double currentX = getX();
        double currentY = getY();
        double playerX = player.getX();
        double playerY = player.getY();

        double dx = playerX - currentX;
        double dy = playerY - currentY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= 0) return;

        // Prevent the enemy from snapping directly onto the player
        double stopDistance = 14.0;
        if (distance < stopDistance) return;

        // Slow down a bit when the enemy gets close
        double moveSpeed = speed;
        if (distance < 60) {
            moveSpeed = Math.max(1.0, speed * 0.55);
        }

        double normX = dx / distance;
        double normY = dy / distance;

        double stepX = normX * moveSpeed;
        double stepY = normY * moveSpeed;

        // Smaller collision box so enemy fits inside paths
        double hitboxWidth = 18;
        double hitboxHeight = 18;
        double hitboxOffsetX = 6;
        double hitboxOffsetY = 6;

        // 1. direct move
        if (tryMove(currentX + stepX, currentY + stepY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }

        boolean xPriority = Math.abs(dx) >= Math.abs(dy);

        // 2. try dominant axis first
        if (xPriority) {
            if (tryMove(currentX + stepX, currentY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
            if (tryMove(currentX, currentY + stepY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
        } else {
            if (tryMove(currentX, currentY + stepY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
            if (tryMove(currentX + stepX, currentY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
        }

        // 3. corner escape / wall slide attempts
        double nudge = Math.max(1.2, moveSpeed);

        // perpendicular-style attempts help stop sticking on corners
        if (tryMove(currentX + nudge, currentY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        if (tryMove(currentX - nudge, currentY, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        if (tryMove(currentX, currentY + nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        if (tryMove(currentX, currentY - nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;

        // 4. small diagonal escape attempts
        if (tryMove(currentX + nudge, currentY + nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        if (tryMove(currentX - nudge, currentY + nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        if (tryMove(currentX + nudge, currentY - nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) return;
        tryMove(currentX - nudge, currentY - nudge, maze, hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight);
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
        if (maze.canMoveTo(nextX + hitboxOffsetX, nextY + hitboxOffsetY, hitboxWidth, hitboxHeight)) {
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