import acm.graphics.*;

public class Player extends Entity {
	
	private double spriteWidth;
	private double spriteHeight;

    private float speed;
    private Weapon weapon;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    private int attackCooldownTimer;
    
    private GImage appearance;

    private GImage imgIdle;
    private GImage imgUp;
    private GImage[] imgDown;
    private GImage[] imgLeft;
    private GImage[] imgRight;
    
    private int frameIndex = 0;
    private int frameTimer = 0;
    private int frameSpeed = 8;
    
    private String facing = "right";

    public Player(int x, int y, int health) {
        super(x, y, health);

        speed = 1.5f;
        weapon = new Weapon("iron sword");
        attackCooldownTimer = 0;
        
        imgIdle = new GImage("char_idle.png");
        imgUp = new GImage("char_back.png");
        
        imgDown = new GImage[] {
            new GImage("char_walk1.png"),
            new GImage("char_walk2.png")
        };

        imgLeft = new GImage[] {
            new GImage("char_walk1.png"),
            new GImage("char_walk2.png")
        };

        imgRight = new GImage[] {
            new GImage("char_walk1R.png"),
            new GImage("char_walk2R.png")
        };

        appearance = imgIdle;
        appearance.scale(0.35, 0.35);

        add(appearance);
        
        spriteWidth = appearance.getWidth();
        spriteHeight = appearance.getHeight();

        setLocation(x, y);
    }

    @Override
    public void move() {
        double newX = getX();
        double newY = getY();

        if (upPressed) {
            newY -= speed;
            facing = "up";
        }
        if (downPressed) {
            newY += speed;
            facing = "down";
        }
        if (leftPressed) {
            newX -= speed;
            facing = "left";
        }
        if (rightPressed) {
            newX += speed;
            facing = "right";
        }

        setLocation(newX, newY);
    }

    public void move(Maze maze) {
        double currentX = getX();
        double currentY = getY();
        double newX = currentX;
        double newY = currentY;

        if (upPressed) {
            newY -= speed;
            facing = "up";
        }
        if (downPressed) {
            newY += speed;
            facing = "down";
        }
        if (leftPressed) {
            newX -= speed;
            facing = "left";
        }
        if (rightPressed) {
            newX += speed;
            facing = "right";
        }

        double hitboxWidth = 6;
        double hitboxHeight = 6;

        double hitboxOffsetX = (spriteWidth - hitboxWidth) / 2.0;
        double hitboxOffsetY = spriteHeight - hitboxHeight - 1;

        double hitboxX = newX + hitboxOffsetX;
        double hitboxY = newY + hitboxOffsetY;

        if (maze.canMoveTo(hitboxX, hitboxY, hitboxWidth, hitboxHeight)) {
            setLocation(newX, newY);
            return;
        }

        double xOnlyHitboxX = newX + hitboxOffsetX;
        double xOnlyHitboxY = currentY + hitboxOffsetY;

        if (maze.canMoveTo(xOnlyHitboxX, xOnlyHitboxY, hitboxWidth, hitboxHeight)) {
            setLocation(newX, currentY);
            currentX = newX;
        }

        double yOnlyHitboxX = currentX + hitboxOffsetX;
        double yOnlyHitboxY = newY + hitboxOffsetY;

        if (maze.canMoveTo(yOnlyHitboxX, yOnlyHitboxY, hitboxWidth, hitboxHeight)) {
            setLocation(currentX, newY);
        }
    }

    public void updateCombat() {
        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
        }
    }

    public boolean canAttack() {
        return attackCooldownTimer == 0;
    }

    public boolean isEnemyInRange(Enemy enemy) {
        if (enemy == null || weapon == null) return false;

        double dx = enemy.getX() - this.getX();
        double dy = enemy.getY() - this.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= weapon.getRange();
    }

    public void attack(Enemy enemy) {
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
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("Player took " + damage + " damage. Health: " + health);
    }
    
    public void updateAnimation(GImage[] walk) {
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
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

    public String getFacing() {
        return facing;
    }
	
    public double getSpriteWidth() {
        return spriteWidth;
    }

    public double getSpriteHeight() {
        return spriteHeight;
    }
}