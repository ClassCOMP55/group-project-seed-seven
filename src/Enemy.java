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
                this.health = 500;
                this.damage = 32;
                this.speed = 2.0f;
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
        appearance.setLocation(0, 0);
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

        double enemyCenterX = getX() + getCenterOffsetX();
        double enemyCenterY = getY() + getCenterOffsetY();
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

        double enemyCenterX = getX() + getCenterOffsetX();
        double enemyCenterY = getY() + getCenterOffsetY();
        double playerCenterX = player.getX() + player.getSpriteWidth() / 2.0;
        double playerCenterY = player.getY() + player.getSpriteHeight() / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            updateFacing(dx, dy);

            double stepX = (dx / distance) * speed;
            double stepY = (dy / distance) * speed;

            setLocation(getX() + stepX, getY() + stepY);
            animateFacing();
        }
    }

    public void moveTowardsPlayer(Player player, Maze maze) {
        if (player == null || maze == null) return;

        // if enemy spawned in a bad spot, snap it into the nearest valid one first
        ensureInsideMaze(maze);

        double currentX = getX();
        double currentY = getY();

        double enemyCenterX = currentX + getCenterOffsetX();
        double enemyCenterY = currentY + getCenterOffsetY();
        double playerCenterX = player.getX() + player.getSpriteWidth() / 2.0;
        double playerCenterY = player.getY() + player.getSpriteHeight() / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= 0) return;

        updateFacing(dx, dy);

        double stopDistance = 18.0;
        if (distance < stopDistance) return;

        double moveSpeed = speed;
        if (distance < 90) {
            moveSpeed = Math.max(0.85, speed * 0.78);
        }
        if (distance < 45) {
            moveSpeed = Math.max(0.65, speed * 0.58);
        }

        double normX = dx / distance;
        double normY = dy / distance;

        double stepX = normX * moveSpeed;
        double stepY = normY * moveSpeed;

        double hitboxWidth = getMoveHitboxWidth();
        double hitboxHeight = getMoveHitboxHeight();
        double hitboxOffsetX = getMoveHitboxOffsetX();
        double hitboxOffsetY = getMoveHitboxOffsetY();

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

        double sideStep = Math.max(2.2, moveSpeed * 2.8);

        if (xPriority) {
            if (tryMove(currentX, currentY + sideStep, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                animateFacing();
                return;
            }
            if (tryMove(currentX, currentY - sideStep, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                animateFacing();
                return;
            }
        } else {
            if (tryMove(currentX + sideStep, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                animateFacing();
                return;
            }
            if (tryMove(currentX - sideStep, currentY, maze,
                    hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
                animateFacing();
                return;
            }
        }

        double diagStep = Math.max(3.0, moveSpeed * 2.4);

        if (tryMove(currentX + diagStep, currentY + diagStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX - diagStep, currentY + diagStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX + diagStep, currentY - diagStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX - diagStep, currentY - diagStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }

        double wideStep = Math.max(4.0, moveSpeed * 3.5);

        if (tryMove(currentX + wideStep, currentY, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX - wideStep, currentY, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX, currentY + wideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
        if (tryMove(currentX, currentY - wideStep, maze,
                hitboxOffsetX, hitboxOffsetY, hitboxWidth, hitboxHeight)) {
            animateFacing();
            return;
        }
    }

    private void ensureInsideMaze(Maze maze) {
        if (isCurrentPositionValid(maze)) {
            return;
        }

        double startX = getX();
        double startY = getY();

        for (int radius = 2; radius <= 60; radius += 2) {
            for (int x = -radius; x <= radius; x += 2) {
                for (int y = -radius; y <= radius; y += 2) {
                    double candidateX = startX + x;
                    double candidateY = startY + y;

                    if (isValidPosition(candidateX, candidateY, maze)) {
                        setLocation(candidateX, candidateY);
                        return;
                    }
                }
            }
        }
    }

    private boolean isCurrentPositionValid(Maze maze) {
        return isValidPosition(getX(), getY(), maze);
    }

    private boolean isValidPosition(double x, double y, Maze maze) {
        return maze.canMoveTo(
            x + getMoveHitboxOffsetX(),
            y + getMoveHitboxOffsetY(),
            getMoveHitboxWidth(),
            getMoveHitboxHeight()
        );
    }

    private void updateFacing(double dx, double dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            facing = (dx > 0) ? "right" : "left";
        } else {
            facing = (dy > 0) ? "down" : "up";
        }
    }

    private double getCenterOffsetX() {
        if (appearance != null) {
            return appearance.getWidth() / 2.0;
        }
        return 12;
    }

    private double getCenterOffsetY() {
        if (appearance != null) {
            return appearance.getHeight() / 2.0;
        }
        return 12;
    }

    private double getMoveHitboxWidth() {
        switch (type) {
            case SPIDER:
                return 10;
            case SKELETON:
                return 12;
            case ALIEN:
                return 5;
            case MUTANT:
                return 16;
            default:
                return 12;
        }
    }

    private double getMoveHitboxHeight() {
        switch (type) {
            case SPIDER:
                return 10;
            case SKELETON:
                return 12;
            case ALIEN:
                return 5;
            case MUTANT:
                return 16;
            default:
                return 12;
        }
    }

    private double getMoveHitboxOffsetX() {
        if (appearance == null) return 8;
        return (appearance.getWidth() - getMoveHitboxWidth()) / 2.0;
    }

    private double getMoveHitboxOffsetY() {
        if (appearance == null) return 8;
        return (appearance.getHeight() - getMoveHitboxHeight()) / 2.0;
    }
    
    private void updateAnimation(GImage[] frames) {
        frameTimer++;

        if (frameTimer >= frameSpeed) {
            frameTimer = 0;
            frameIndex = (frameIndex + 1) % frames.length;
            appearance.setImage(frames[frameIndex].getImage());
            appearance.setLocation(0, 0);
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