import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;

public class GameplayPane extends GraphicsPane {

    private Player player;
    private Enemy enemy;
    private GImage attackEffect;

    private GLabel controlsLabel;
    private GLabel weaponLabel;
    private GLabel switchWeaponsLabel;
    private GLabel statusLabel;
    private GLabel playerHealthLabel;
    private GLabel enemyHealthLabel;
    private GLabel stageLabel;

    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Maze maze;
    private int currentStage = 1;

    private boolean gameLoopStarted = false;
    private int loopVersion = 0;

    private boolean gateOpen = false;
    private boolean stageTransitioning = false;
    private boolean campaignComplete = false;
    
    private String imageFile;
    private boolean attackPressed = false;

    public GameplayPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }

    public void setStage(int stage) {
        if (stage < 1) stage = 1;
        if (stage > 4) stage = 4;
        currentStage = stage;
    }

    @Override
    public void showContent() {
        gateOpen = false;
        stageTransitioning = false;
        campaignComplete = false;
        attackPressed = false;

        loadStage(currentStage);
        startGameLoop();
    }

    @Override
    public void hideContent() {
        gameLoopStarted = false;
        loopVersion++;
        attackPressed = false;

        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();

        projectiles.clear();
        attackEffect = null;
        enemy = null;
        maze = null;
        player = null;
    }

    private void clearScreenContents() {
        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();

        projectiles.clear();
        attackEffect = null;
        enemy = null;
        maze = null;
        player = null;
        attackPressed = false;
    }

    private void loadStage(int stage) {
        clearScreenContents();

        addBackground();
        buildMazeForStage(stage);
        addLabels();
        addPlayer();
        addEnemy();
        updateLabels();

        gateOpen = false;
        stageTransitioning = false;
    }

    private void addBackground() {
        GImage background = new GImage("background.png");
        background.setLocation(0, 0);

        mainScreen.add(background);
        background.sendToBack();
        contents.add(background);
    }

    private void buildMazeForStage(int stage) {
        maze = new Maze();

        if (stage == 1) {
            maze.build(Maze.EASY);
        } else if (stage == 2) {
            maze.build(Maze.MEDIUM);
        } else if (stage == 3) {
            maze.build(Maze.HARD);
        } else {
            maze.build(Maze.BOSS);
        }

        double mazeX = (mainScreen.getWidth() - maze.getMazeWidth()) / 2.0;
        double mazeY = (mainScreen.getGCanvas().getHeight() - maze.getMazeHeight()) / 2.0;

        maze.setRenderPosition(mazeX, mazeY);
        maze.renderTo(mainScreen, contents);
    }

    private void addLabels() {
        double leftX = 18;
        int y = 28;
        int gap = 20;

        controlsLabel = new GLabel("WASD move | SPACE attack", leftX, y);
        controlsLabel.setColor(Color.RED);
        controlsLabel.setFont("DialogInput-BOLD-13");
        y += gap;

        weaponLabel = new GLabel("Equipped: Iron Sword", leftX, y);
        weaponLabel.setColor(Color.WHITE);
        weaponLabel.setFont("DialogInput-BOLD-13");
        y += gap;

        switchWeaponsLabel = new GLabel("Switch: 1 Iron", leftX, y);
        switchWeaponsLabel.setColor(Color.CYAN);
        switchWeaponsLabel.setFont("DialogInput-BOLD-13");
        y += gap;

        statusLabel = new GLabel(getStageMessage(), leftX, y);
        statusLabel.setColor(Color.RED);
        statusLabel.setFont("DialogInput-PLAIN-13");
        y += gap;

        playerHealthLabel = new GLabel("Player HP: 100", leftX, y);
        playerHealthLabel.setColor(Color.WHITE);
        playerHealthLabel.setFont("DialogInput-BOLD-13");
        y += gap;

        enemyHealthLabel = new GLabel("Enemy HP: 0", leftX, y);
        enemyHealthLabel.setColor(Color.WHITE);
        enemyHealthLabel.setFont("DialogInput-BOLD-13");
        y += gap;

        stageLabel = new GLabel(getStageTitle(), leftX, y);
        stageLabel.setColor(Color.WHITE);
        stageLabel.setFont("DialogInput-BOLD-13");

        contents.add(controlsLabel);
        contents.add(weaponLabel);
        contents.add(switchWeaponsLabel);
        contents.add(statusLabel);
        contents.add(playerHealthLabel);
        contents.add(enemyHealthLabel);
        contents.add(stageLabel);

        mainScreen.add(controlsLabel);
        mainScreen.add(weaponLabel);
        mainScreen.add(switchWeaponsLabel);
        mainScreen.add(statusLabel);
        mainScreen.add(playerHealthLabel);
        mainScreen.add(enemyHealthLabel);
        mainScreen.add(stageLabel);
    }

    private String getStageTitle() {
        if (currentStage == 1) return "Stage 1";
        if (currentStage == 2) return "Stage 2";
        if (currentStage == 3) return "Stage 3";
        return "Final Boss";
    }

    private String getStageMessage() {
        if (currentStage == 1) return "Defeat the spider to open the gate";
        if (currentStage == 2) return "Defeat the skeleton to open the gate";
        if (currentStage == 3) return "Defeat the alien to open the gate";
        return "Defeat the mutant boss";
    }

    private String getSwitchWeaponsText() {
        if (currentStage == 1) {
            return "Switch: 1 Iron";
        }
        if (currentStage == 2) {
            return "Switch: 1 Iron | 2 Laser | 4 Bow";
        }
        if (currentStage == 3) {
            return "Switch: 1 Iron | 2 Laser | 4 Bow | 5 Axe";
        }
        return "Switch: 1 Iron | 2 Laser | 3 Gun | 4 Bow | 5 Axe";
    }

    private void addPlayer() {
        int spawnX = (int) maze.getPlayerSpawnX();
        int spawnY = (int) maze.getPlayerSpawnY();

        player = new Player(spawnX, spawnY, 100);
        player.setLocation(spawnX, spawnY);

        contents.add(player);
        mainScreen.add(player);
    }

    private void addEnemy() {
        double enemyX = maze.getEnemySpawnX();
        double enemyY = maze.getEnemySpawnY();

        EnemyType type;
        if (currentStage == 1) {
            type = EnemyType.SPIDER;
        } else if (currentStage == 2) {
            type = EnemyType.SKELETON;
        } else if (currentStage == 3) {
            type = EnemyType.ALIEN;
        } else {
            type = EnemyType.MUTANT;
        }

        enemy = new Enemy((float) enemyX, (float) enemyY, type);
        contents.add(enemy);
        mainScreen.add(enemy);
    }

    private void updateLabels() {
        if (player != null) {
            weaponLabel.setLabel("Equipped: " + player.getWeapon().getName());
            playerHealthLabel.setLabel("Player HP: " + player.getHealth());
        }

        switchWeaponsLabel.setLabel(getSwitchWeaponsText());

        if (enemy != null && enemy.getParent() != null) {
            enemyHealthLabel.setLabel(enemy.getType().toString() + " HP: " + enemy.getHealth());
        } else {
            enemyHealthLabel.setLabel("Enemy HP: 0");
        }

        stageLabel.setLabel(getStageTitle());
    }

    private boolean isWeaponUnlocked(String weaponName) {
        if (weaponName.equals("iron sword")) {
            return true;
        }

        if (currentStage >= 2) {
            if (weaponName.equals("laser sword") || weaponName.equals("bow and arrow")) {
                return true;
            }
        }

        if (currentStage >= 3) {
            if (weaponName.equals("axe")) {
                return true;
            }
        }

        if (currentStage >= 4) {
            if (weaponName.equals("laser gun")) {
                return true;
            }
        }

        return false;
    }

    private void equipWeaponIfUnlocked(String weaponType, String displayName) {
        if (!isWeaponUnlocked(weaponType)) {
            statusLabel.setLabel(displayName + " is locked for this stage");
            return;
        }

        player.setWeapon(new Weapon(weaponType));
        updateWeaponLabel();
        statusLabel.setLabel("Switched to " + displayName);
    }

    private void handleStageCleared() {
        if (gateOpen) return;

        gateOpen = true;
        maze.openExitGate();

        if (currentStage == 4) {
            statusLabel.setLabel("Boss defeated! Walk to the exit to finish");
            campaignComplete = true;
        } else if (currentStage == 1) {
            statusLabel.setLabel("Gate opened! Stage 2 unlocks Laser Sword + Bow");
        } else if (currentStage == 2) {
            statusLabel.setLabel("Gate opened! Stage 3 unlocks Axe");
        } else if (currentStage == 3) {
            statusLabel.setLabel("Gate opened! Boss unlocks Laser Gun");
        } else {
            statusLabel.setLabel("Gate opened! Move to the exit");
        }
    }

    private void advanceToNextStage() {
        if (stageTransitioning) return;
        stageTransitioning = true;

        if (currentStage < 4) {
            currentStage++;
            loadStage(currentStage);
        } else {
            statusLabel.setLabel("You beat the game!");
        }
    }

    private void handlePlayerDeath() {
        if (stageTransitioning) return;

        stageTransitioning = true;

        if (currentStage == 4) {
            currentStage = 1;
        }

        loadStage(currentStage);
    }

    private void removeProjectile(int i) {
        Projectile p = projectiles.get(i);
        mainScreen.remove(p);
        contents.remove(p);
        projectiles.remove(i);
    }

    private boolean projectileHitsWall(Projectile p) {
        double px = p.getX();
        double py = p.getY();
        return !maze.canMoveTo(px, py, 6, 6);
    }

    private void startGameLoop() {
        if (gameLoopStarted) return;
        gameLoopStarted = true;
        int currentLoopVersion = loopVersion;

        new Thread(() -> {
            while (gameLoopStarted && currentLoopVersion == loopVersion) {
                if (player != null && maze != null && player.getHealth() > 0) {
                    player.move(maze);
                    player.updateCombat();
                }

                if (enemy != null && player != null && maze != null &&
                    enemy.getParent() != null && player.getHealth() > 0) {
                    enemy.moveTowardsPlayer(player, maze);
                    enemy.updateCombat();
                    enemy.attack(player);
                }

                for (int i = 0; i < projectiles.size(); i++) {
                    Projectile p = projectiles.get(i);
                    p.move();

                    if (projectileHitsWall(p)) {
                        removeProjectile(i);
                        i--;
                        continue;
                    }

                    boolean projectileHit = false;
                    
                    if (enemy != null && enemy.getParent() != null) {
                        double px = p.getX();
                        double py = p.getY();

                        double ex = enemy.getX();
                        double ey = enemy.getY();
                        double ew = enemy.getWidth();
                        double eh = enemy.getHeight();

                        boolean hitEnemy =
                            px >= ex && px <= ex + ew &&
                            py >= ey && py <= ey + eh;

                        if (hitEnemy) {
                            enemy.takeDamage(p.getDamage());
                            projectileHit = true;
                        }
                    }

                    if (projectileHit) {
                        removeProjectile(i);
                        i--;
                        continue;
                    }

                    if (p.getX() < 0 || p.getX() > mainScreen.getWidth() ||
                        p.getY() < 0 || p.getY() > mainScreen.getHeight()) {
                        removeProjectile(i);
                        i--;
                    }
                }

                if (enemy != null && enemy.getParent() == null) {
                    handleStageCleared();
                }

                if (gateOpen && player != null && maze.isPlayerAtExit(player.getX(), player.getY())) {
                    advanceToNextStage();
                }

                updateLabels();

                if (player != null && player.getHealth() <= 0) {
                    statusLabel.setLabel("Player defeated!");
                    mainScreen.pause(700);
                    handlePlayerDeath();
                    continue;
                }

                mainScreen.pause(16);
            }
        }).start();
    }

    private void showAttackEffect() {
        if (attackEffect != null) {
            mainScreen.remove(attackEffect);
            contents.remove(attackEffect);
        }

        double effectX = player.getX();
        double effectY = player.getY();

        double spriteWidth = player.getSpriteWidth();
        double spriteHeight = player.getSpriteHeight();
        
        attackEffect = new GImage("attackeffect_right.png");
        attackEffect.scale(0.1, 0.1);
        attackEffect.setLocation(effectX, effectY);

        switch (player.getFacing()) {
            case "up":
                effectX += spriteWidth / 2 - 10;
                effectY -= 15;
                break;
            case "down":
                effectX += spriteWidth / 2 - 10;
                effectY += spriteHeight - 10;
                break;
            case "left":
                effectX -= 20;
                effectY += spriteHeight / 2 - 10;
                break;
            case "right":
                effectX += spriteWidth;
                effectY += spriteHeight / 2 - 10;
                break;
        }

        contents.add(attackEffect);
        mainScreen.add(attackEffect);
        attackEffect.sendToFront();

        new Thread(() -> {
            GImage currentEffect = attackEffect;
            mainScreen.pause(180);
            if (currentEffect != null) {
                mainScreen.remove(currentEffect);
                contents.remove(currentEffect);
                if (attackEffect == currentEffect) {
                    attackEffect = null;
                }
            }
        }).start();
    }

    private void updateWeaponLabel() {
        weaponLabel.setLabel("Equipped: " + player.getWeapon().getName());
    }

    private void shootProjectile() {
        double startX = player.getX() + player.getSpriteWidth() / 2;
        double startY = player.getY() + player.getSpriteHeight() / 2;

        double dx = 0;
        double dy = 0;

        switch (player.getFacing()) {
            case "up":
                dy = -6;
                break;
            case "down":
                dy = 6;
                break;
            case "left":
                dx = -6;
                break;
            case "right":
                dx = 6;
                break;
        }
        
        if (player.getWeapon().getName().equals("Bow and Arrow")) {
            imageFile = "arrow_right.png";
        } else if (player.getWeapon().getName().equals("Laser Gun")) {
            imageFile = "laser_right.png";
        }

        Projectile p = new Projectile(startX, startY, dx, dy, player.getWeapon().getDamage(), imageFile);

        projectiles.add(p);
        contents.add(p);
        mainScreen.add(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // no back button
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (player == null || player.getHealth() <= 0) return;

        if (key == KeyEvent.VK_W) player.setUpPressed(true);
        if (key == KeyEvent.VK_S) player.setDownPressed(true);
        if (key == KeyEvent.VK_A) player.setLeftPressed(true);
        if (key == KeyEvent.VK_D) player.setRightPressed(true);

        if (key == KeyEvent.VK_1) {
            equipWeaponIfUnlocked("iron sword", "Iron Sword");
        }
        if (key == KeyEvent.VK_2) {
            equipWeaponIfUnlocked("laser sword", "Laser Sword");
        }
        if (key == KeyEvent.VK_3) {
            equipWeaponIfUnlocked("laser gun", "Laser Gun");
        }
        if (key == KeyEvent.VK_4) {
            equipWeaponIfUnlocked("bow and arrow", "Bow and Arrow");
        }
        if (key == KeyEvent.VK_5) {
            equipWeaponIfUnlocked("axe", "Axe");
        }

        if (key == KeyEvent.VK_SPACE) {
            if (attackPressed) return;
            attackPressed = true;

            if (!player.canAttack()) return;

            if (player.getWeapon().isRanged()) {
                shootProjectile();
                player.startAttackCooldown();
                return;
            }

            if (enemy != null && enemy.getParent() != null) {
                showAttackEffect();
                player.attack(enemy);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (player == null) return;

        if (key == KeyEvent.VK_W) player.setUpPressed(false);
        if (key == KeyEvent.VK_S) player.setDownPressed(false);
        if (key == KeyEvent.VK_A) player.setLeftPressed(false);
        if (key == KeyEvent.VK_D) player.setRightPressed(false);
        if (key == KeyEvent.VK_SPACE) attackPressed = false;
    }
}