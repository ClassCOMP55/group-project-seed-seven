import acm.graphics.GOval;
import java.awt.Color;

public class Player extends Entity {

    // Movement speed and currently equipped weapon
    private float speed;
    private Weapon weapon;

    // Movement flags used for WASD input
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    // Used to control how often the player can attack
    private int attackCooldownTimer;

    public Player(int x, int y, int health) {
        super(x, y, health);

        speed = 4.0f;
        weapon = new Weapon("iron sword");
        attackCooldownTimer = 0;

        // Temporary player visual for testing movement/combat
        GOval appearance = new GOval(0, 0, 30, 30);
        appearance.setFilled(true);
        appearance.setFillColor(Color.RED);
        add(appearance);

        setLocation(x, y);
    }

    @Override
    public void move() {
        // Move player based on which keys are currently being pressed
        if (upPressed) {
            y -= speed;
        }
        if (downPressed) {
            y += speed;
        }
        if (leftPressed) {
            x -= speed;
        }
        if (rightPressed) {
            x += speed;
        }

        setLocation(x, y);
    }

    public void updateCombat() {
        // Reduce cooldown over time so the player can attack again later
        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
        }
    }

    public boolean canAttack() {
        return attackCooldownTimer == 0;
    }

    public boolean isEnemyInRange(Enemy enemy) {
        // Check if the enemy is close enough for the current weapon to hit
        if (enemy == null || weapon == null) return false;

        double dx = enemy.getX() - this.getX();
        double dy = enemy.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= weapon.getRange();
    }

    public void attack(Enemy enemy) {
        // Basic combat system: only attack if cooldown is finished and enemy is in range
        if (weapon == null || enemy == null) return;

        if (!canAttack()) {
            System.out.println("Weapon is on cooldown.");
            return;
        }

        if (!isEnemyInRange(enemy)) {
            System.out.println("Enemy is out of range.");
            return;
        }

        weapon.attack(enemy);
        attackCooldownTimer = weapon.getCooldown();
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void takeDamage(int damage) {
        // Reduce player health when attacked by an enemy
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("Player took " + damage + " damage. Health: " + health);
    }

    @Override
    public void setLocation(double x, double y) {
        // Keep both Entity coordinates and on-screen position synchronized
        this.x = (float) x;
        this.y = (float) y;
        super.setLocation(x, y);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        // Allows weapon switching during testing or gameplay
        this.weapon = weapon;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackCooldownTimer() {
        return attackCooldownTimer;
    }

    public void setUpPressed(boolean value) {
        upPressed = value;
    }

    public void setDownPressed(boolean value) {
        downPressed = value;
    }

    public void setLeftPressed(boolean value) {
        leftPressed = value;
    }

    public void setRightPressed(boolean value) {
        rightPressed = value;
    }
}