import acm.graphics.*;

public class Player extends Entity {
	
	private double spriteWidth;
	private double spriteHeight;

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

        speed = 4.0f;
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
        add(appearance);
        
        spriteWidth = appearance.getWidth();
        spriteHeight = appearance.getHeight();

        setLocation(x, y);
    }

    @Override
    public void move() {
        // Move player based on which keys are currently being pressed
    	double newX = getX();
        double newY = getY();

        if (upPressed) {
        	newY -= speed;
        	facing = "up";
        	charDirection(imgUp);
        }
        if (downPressed) {
        	newY += speed;
        	facing = "down";
        	updateAnimation(imgDown);
        }
        if (leftPressed) {
        	newX -= speed;
        	facing = "left";
        	updateAnimation(imgLeft);
        }
        if (rightPressed) {
        	newX += speed;
        	facing = "right";
        	updateAnimation(imgRight);
        }

        setLocation(newX, newY);
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
    public void takeDamage(int damage) {
        // Reduce player health when attacked by an enemy
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("Player took " + damage + " damage. Health: " + health);
    }
    
    public void updateAnimation(GImage[] walk) {
    	frameTimer++;

        if (frameTimer >= frameSpeed) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % walk.length;

            remove(appearance);
            appearance = walk[frameIndex];
            add(appearance);
        }
    }
    
    private void charDirection(GImage frames) {
    	remove(appearance);
        appearance = frames;
        add(appearance);
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