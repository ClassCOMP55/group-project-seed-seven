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
        
        imgIdle.scale(0.35, 0.35);
        imgUp.scale(0.35, 0.35);

        for (GImage img : imgDown) img.scale(0.35, 0.35);
        for (GImage img : imgLeft) img.scale(0.35, 0.35);
        for (GImage img : imgRight) img.scale(0.35, 0.35);

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

    public void move(Maze maze) {
        double currentX = getX();
        double currentY = getY();
        double newX = currentX;
        double newY = currentY;
        
        boolean isMoving = false;

        if (upPressed) {
            newY -= speed;
            facing = "up";
            charDirection(imgUp);
            isMoving = true;
        }
        if (downPressed) {
            newY += speed;
            facing = "down";
            updateAnimation(imgDown);
            isMoving = true;
        }
        if (leftPressed) {
            newX -= speed;
            facing = "left";
            updateAnimation(imgLeft);
            isMoving = true;
        }
        if (rightPressed) {
            newX += speed;
            facing = "right";
            updateAnimation(imgRight);
            isMoving = true;
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

    public void startAttackCooldown() {
        if (weapon != null) {
            attackCooldownTimer = weapon.getCooldown();
        }
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
            return;
        }

        if (!isEnemyInRange(enemy)) {
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