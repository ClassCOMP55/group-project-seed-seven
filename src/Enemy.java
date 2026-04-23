import java.util.Random;

import acm.graphics.GImage;

public class Enemy extends Entity {
    private int damage;
    private float speed;
    private EnemyType type;
    private Random rand = new Random();

    private int attackCooldownTimer;
    private double attackRange;
    
    private GImage appearance;

    private GImage[] walkUp;
    private GImage[] walkDown;
    private GImage[] walkLeft;
    private GImage[] walkRight;

    private int frameIndex = 0;
    private int frameTimer = 0;
    private int frameSpeed = 8;

    private String facing = "down";

    public Enemy(float x, float y, EnemyType type) {
        super(x, y, 0);
        this.type = type;
        initializeStats();
        
        loadAnimationFrames();

        this.setLocation(x, y);
    }

    private void initializeStats() {
        switch (this.type) {
            case SPIDER:
                this.health = 75;
                this.damage = 10;
                this.speed = 1.55f;
                this.attackRange = 24;
                this.attackCooldownTimer = 0;
                break;

            case SKELETON:
                this.health = 95;
                this.damage = 14;
                this.speed = 1.35f;
                this.attackRange = 26;
                this.attackCooldownTimer = 0;
                break;

            case ALIEN:
                this.health = 135;
                this.damage = 20;
                this.speed = 1.45f;
                this.attackRange = 28;
                this.attackCooldownTimer = 0;
                break;

            case MUTANT:
                this.health = 205;
                this.damage = 32;
                this.speed = 1.15f;
                this.attackRange = 30;
                this.attackCooldownTimer = 0;
                break;
        }
    }
    
    private void loadAnimationFrames() {
    	
    	double scaleFactor = 0.35;

        switch (type) {

            case SPIDER:
                walkUp = new GImage[] {
                    new GImage("spider_right.png")
                };
                walkDown = new GImage[] {
                    new GImage("spider_left.png")
                };
                walkLeft = new GImage[] {
                    new GImage("spider_left.png")
                };
                walkRight = new GImage[] {
                    new GImage("spider_right.png")
                };
                scaleFactor = 0.35;
                break;

            case SKELETON:
                walkUp = new GImage[] {
                    new GImage("skl_up.png"),
                    new GImage("skl_up1.png")
                };
                walkDown = new GImage[] {
                    new GImage("skl_left.png"),
                    new GImage("skl_left1.png")
                };
                walkLeft = new GImage[] {
                    new GImage("skl_left.png"),
                    new GImage("skl_left1.png")
                };
                walkRight = new GImage[] {
                    new GImage("skl_right.png"),
                    new GImage("skl_right1.png")
                };
                scaleFactor = 0.35;
                break;

            case ALIEN:
                walkUp = new GImage[] {
                    new GImage("alien_back.png")
                };
                walkDown = new GImage[] {
                    new GImage("alien_idle.png"),
                    new GImage("alien_left1.png")
                };
                walkLeft = new GImage[] {
                    new GImage("alien_idle.png"),
                    new GImage("alien_left1.png")
                };
                walkRight = new GImage[] {
                    new GImage("alien_right.png"),
                    new GImage("alien_right1.png")
                };
                scaleFactor = 0.18;
                break;

            case MUTANT:
                walkUp = new GImage[] {
                    new GImage("mut_left.png"),
                    new GImage("mut_left1.png")
                };
                walkDown = new GImage[] {
                    new GImage("mut_right.png"),
                    new GImage("mut_right1.png")
                };
                walkLeft = new GImage[] {
                    new GImage("mut_left.png"),
                    new GImage("mut_left1.png")
                };
                walkRight = new GImage[] {
                    new GImage("mut_right.png"),
                    new GImage("mut_right1.png")
                };
                scaleFactor = 0.35;
                break;
        }

        for (GImage img : walkUp) img.scale(scaleFactor);
        for (GImage img : walkDown) img.scale(scaleFactor);
        for (GImage img : walkLeft) img.scale(scaleFactor);
        for (GImage img : walkRight) img.scale(scaleFactor);

        appearance = walkDown[0];
        add(appearance);
    }

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

    public double getAttackRange() {
        return this.attackRange;
    }

    @Override
    public void move() {
        // not used directly
    }

    public void updateCombat() {
        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
        }
    }

    public boolean canAttack() {
        return attackCooldownTimer == 0;
    }

    public boolean isPlayerInRange(Player player) {
        if (player == null) return false;

        double enemyCenterX = getX() + 15;
        double enemyCenterY = getY() + 15;
        double playerCenterX = player.getX() + player.getSpriteWidth() / 2.0;
        double playerCenterY = player.getY() + player.getSpriteHeight() / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= attackRange;
    }
    
    private void animateFacing() {
        switch (facing) {
            case "up":
                updateAnimation(walkUp);
                break;
            case "down":
                updateAnimation(walkDown);
                break;
            case "left":
                updateAnimation(walkLeft);
                break;
            case "right":
                updateAnimation(walkRight);
                break;
        }
    }


    public void attack(Player player) {
        if (player == null) return;
        if (!canAttack()) return;
        if (!isPlayerInRange(player)) return;

        player.takeDamage(damage);
        attackCooldownTimer = 30;
        System.out.println(type + " hit player for " + damage + " damage");
    }

    public void moveTowardsPlayer(Player player) {
        if (player == null) return;

        double enemyCenterX = getX() + 15;
        double enemyCenterY = getY() + 15;
        double playerCenterX = player.getX() + player.getSpriteWidth() / 2.0;
        double playerCenterY = player.getY() + player.getSpriteHeight() / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            double stepX = (dx / distance) * speed;
            double stepY = (dy / distance) * speed;

            setLocation(getX() + stepX, getY() + stepY);
        }
    }

    public void moveTowardsPlayer(Player player, Maze maze) {
        if (player == null || maze == null) return;

        double currentX = getX();
        double currentY = getY();

        double enemyCenterX = currentX + 15;
        double enemyCenterY = currentY + 15;
        double playerCenterX = player.getX() + player.getSpriteWidth() / 2.0;
        double playerCenterY = player.getY() + player.getSpriteHeight() / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (Math.abs(dx) > Math.abs(dy)) {
            facing = (dx > 0) ? "right" : "left";
        } else {
            facing = (dy > 0) ? "down" : "up";
        }

        if (distance <= 0) return;

        double stopDistance = 20.0;
        if (distance < stopDistance) return;

        double moveSpeed = speed;
        if (distance < 90) {
            moveSpeed = Math.max(0.75, speed * 0.7);
        }
        if (distance < 45) {
            moveSpeed = Math.max(0.55, speed * 0.45);
        }

        double normX = dx / distance;
        double normY = dy / distance;

        double stepX = normX * moveSpeed;
        double stepY = normY * moveSpeed;

        // smaller centered hitbox so enemy does not snag on walls so much
        double hitboxWidth = 12;
        double hitboxHeight = 12;
        double hitboxOffsetX = 9;
        double hitboxOffsetY = 9;

        if (tryMove(currentX + stepX, currentY + stepY, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
        	animateFacing();

            return;
        }

        boolean xPriority = Math.abs(dx) >= Math.abs(dy);

        if (xPriority) {
            if (tryMove(currentX + stepX, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            	animateFacing();
                return;
            }
            if (tryMove(currentX, currentY + stepY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            	animateFacing();
                return;
            }
        } else {
            if (tryMove(currentX, currentY + stepY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            	animateFacing();
                return;
            }
            if (tryMove(currentX + stepX, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            	animateFacing();
                return;
            }
        }

        double sideStep = Math.max(1.4, moveSpeed * 1.8);

        if (xPriority) {
            if (tryMove(currentX, currentY + sideStep, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
            if (tryMove(currentX, currentY - sideStep, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
        } else {
            if (tryMove(currentX + sideStep, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
            if (tryMove(currentX - sideStep, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                return;
            }
        }

        if (tryMove(currentX + sideStep, currentY + sideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX - sideStep, currentY + sideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX + sideStep, currentY - sideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX - sideStep, currentY - sideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }

        double nudge = 1.0;

        if (tryMove(currentX + nudge, currentY, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX - nudge, currentY, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX, currentY + nudge, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
        if (tryMove(currentX, currentY - nudge, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            return;
        }
    }
    
    private void updateAnimation(GImage[] frames) {
        frameTimer++;

        if (frameTimer >= frameSpeed) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.length;

            appearance.setImage(frames[frameIndex].getImage());
        }
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
        if (maze.canMoveTo(
                nextX + hitboxOffsetX,
                nextY + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight)) {
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
        double roll = rand.nextDouble();

        switch (this.type) {
            case SPIDER:
                if (roll < 0.20) droppedOrb = new Orb();
                break;
            case SKELETON:
                if (roll < 0.40) droppedOrb = new Orb();
                break;
            case ALIEN:
                if (roll < 0.70) droppedOrb = new Orb();
                break;
            case MUTANT:
                droppedOrb = new Orb();
                break;
        }

        if (droppedOrb != null) {
            System.out.println(type + " dropped an EXP orb!");
        }

        if (this.getParent() != null) {
            this.getParent().remove(this);
        }
    }
}