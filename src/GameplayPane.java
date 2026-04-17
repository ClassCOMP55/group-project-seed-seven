import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;

public class GameplayPane extends GraphicsPane {

    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<GOval> enemyMarkers = new ArrayList<>();
    private GOval attackEffect;

    private GLabel controlsLabel;
    private GLabel weaponLabel;
    private GLabel statusLabel;
    
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Maze maze;
    private int currentDifficulty = Maze.EASY;

    private GImage backButton;

    // controls the active game loop
    private boolean gameLoopStarted = false;
    private int loopVersion = 0;

    public GameplayPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }

    public void setDifficulty(int difficulty) {
        currentDifficulty = difficulty;
    }

    @Override
    public void showContent() {
        addBackground();
        buildMaze();
        addLabels();
        addBackButton();
        addPlayer();
        addEnemy();
        startGameLoop();
    }

    @Override
    public void hideContent() {
        // stop current loop
        gameLoopStarted = false;
        loopVersion++;

        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();

        enemies.clear();
        enemyMarkers.clear();

        attackEffect = null;
        maze = null;
        backButton = null;
        player = null;
    }

    private void addBackground() {
        GImage background = new GImage("background.png");
        background.setLocation(0, 0);

        mainScreen.add(background);
        background.sendToBack();
        contents.add(background);
    }

    private void buildMaze() {
        maze = new Maze();
        maze.build(currentDifficulty);

        double mazeX = (mainScreen.getWidth() - maze.getMazeWidth()) / 2.0;
        double mazeY = (mainScreen.getHeight() - maze.getMazeHeight()) / 2.0 + 20;

        maze.setRenderPosition(mazeX, mazeY);
        maze.renderTo(mainScreen, contents);
    }

    private void addLabels() {
        controlsLabel = new GLabel(
            "WASD = move | SPACE = attack | 6 Level 1 | 7 Level 2 | 8 Level 3",
            20, 35
        );
        controlsLabel.setColor(Color.RED);
        controlsLabel.setFont("DialogInput-BOLD-16");

        weaponLabel = new GLabel("Current Weapon: Iron Sword", 20, 60);
        weaponLabel.setColor(Color.RED);
        weaponLabel.setFont("DialogInput-BOLD-16");

        statusLabel = new GLabel("Gameplay ready", 20, 85);
        statusLabel.setColor(Color.RED);
        statusLabel.setFont("DialogInput-PLAIN-16");

        contents.add(controlsLabel);
        contents.add(weaponLabel);
        contents.add(statusLabel);

        mainScreen.add(controlsLabel);
        mainScreen.add(weaponLabel);
        mainScreen.add(statusLabel);
    }

    private void addBackButton() {
        backButton = new GImage("back.jpg", 200, 400);
        backButton.scale(0.3, 0.3);
        backButton.setLocation((mainScreen.getWidth() - backButton.getWidth()) / 2, 500);

        contents.add(backButton);
        mainScreen.add(backButton);
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
        enemies.clear();
        enemyMarkers.clear();

        int enemyCount = 1;
        if (currentDifficulty == Maze.MEDIUM) enemyCount = 2;
        if (currentDifficulty == Maze.HARD) enemyCount = 3;

        double startX = maze.getEnemySpawnX();
        double startY = maze.getEnemySpawnY();

        for (int i = 0; i < enemyCount; i++) {
            double finalX = startX + (i * 5);
            double finalY = startY + (i * 5);
            
            if (i == 1) finalX += 12;
            else if (i == 2) finalY += 12;
            
            if (!maze.canMoveTo(finalX, finalY, 30, 30)) {
                finalX = startX;
                finalY = startY;
            }

            // --- NEW CODE: CREATE THE ACTUAL ENEMY ---
            // This adds the logic-bearing object to the list so the game loop can find it
            Enemy newEnemy = new Enemy((int)finalX, (int)finalY, EnemyType.MUTANT);
            enemies.add(newEnemy); 
            // -----------------------------------------

            // Keep your existing marker code for visual tracking
            GOval marker = new GOval(finalX, finalY, 30, 30);
            marker.setFilled(true);
            if (i == 0) marker.setFillColor(new Color(128, 0, 128));
            else if (i == 1) marker.setFillColor(Color.BLUE);
            else marker.setFillColor(Color.RED);
            
            enemyMarkers.add(marker);
            contents.add(marker);
            mainScreen.add(marker);
        }
    }

    private void restartGameplay() {
        hideContent();
        showContent();
    }

    private void startGameLoop() {
        if (gameLoopStarted) return;
        gameLoopStarted = true;
        int currentLoopVersion = loopVersion;

        new Thread(() -> {
            while (gameLoopStarted && currentLoopVersion == loopVersion) {
                // Handle Player Movement
                if (player != null && maze != null) {
                    player.move(maze);
                    player.updateCombat();
                }
                
                // Handle Multiple Enemy Movements
                // Iterating through the list of enemies instead of a single testEnemy
                for (Enemy enemy : enemies) {
                    enemy.moveTowardsPlayer(player, maze);
                }
                
                // Handle Projectile Movement and Collisions
                for (int i = 0; i < projectiles.size(); i++) {
                    Projectile p = projectiles.get(i);
                    p.move();

                    boolean projectileHit = false;
                    // Check collision against all active enemies
                    for (Enemy e : enemies) {
                        if (e.getParent() != null && p.collidesWith(e)) {
                            e.takeDamage(p.getDamage());
                            projectileHit = true;
                            break; // Stop checking other enemies for this specific projectile
                        }
                    }

                    if (projectileHit) {
                        mainScreen.remove(p);
                        contents.remove(p);
                        projectiles.remove(i);
                        i--;
                        continue;
                    }

                    // Remove off-screen projectiles
                    if (p.getX() < 0 || p.getX() > mainScreen.getWidth() ||
                        p.getY() < 0 || p.getY() > mainScreen.getHeight()) {

                        mainScreen.remove(p);
                        contents.remove(p);
                        projectiles.remove(i);
                        i--;
                    }
                }

                // Update Enemy Markers and Defeat Logic
                for (int i = 0; i < enemies.size(); i++) {
                    Enemy e = enemies.get(i);
                    GOval m = enemyMarkers.get(i); // Assumes parallel list or mapping

                    if (e.getParent() != null) {
                        // Update visual marker position
                        m.setLocation(e.getX(), e.getY());
                        m.sendToFront();
                    } else {
                        // Cleanup defeated enemy and its marker
                        mainScreen.remove(m);
                        contents.remove(m);
                        enemies.remove(i);
                        enemyMarkers.remove(i);
                        i--;
                        
                        if (enemies.isEmpty()) {
                            statusLabel.setLabel("All enemies defeated!");
                        }
                    }
                }

                mainScreen.pause(16); // Maintain ~60 FPS
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

        attackEffect = new GOval(effectX, effectY, 20, 20);
        attackEffect.setFilled(true);
        attackEffect.setFillColor(Color.YELLOW);
        attackEffect.setColor(Color.ORANGE);

        contents.add(attackEffect);
        mainScreen.add(attackEffect);
        attackEffect.sendToFront();

        new Thread(() -> {
            GOval currentEffect = attackEffect;
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
        weaponLabel.setLabel("Current Weapon: " + player.getWeapon().getName());
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

        Projectile p = new Projectile(startX, startY, dx, dy, player.getWeapon().getDamage());

        projectiles.add(p);
        contents.add(p);
        mainScreen.add(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == backButton) {
            mainScreen.switchToWelcomeScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (player == null) return;

        if (key == KeyEvent.VK_W) player.setUpPressed(true);
        if (key == KeyEvent.VK_S) player.setDownPressed(true);
        if (key == KeyEvent.VK_A) player.setLeftPressed(true);
        if (key == KeyEvent.VK_D) player.setRightPressed(true);

        if (key == KeyEvent.VK_1) {
            player.setWeapon(new Weapon("iron sword"));
            updateWeaponLabel();
            statusLabel.setLabel("Switched to Iron Sword");
        }
        if (key == KeyEvent.VK_2) {
            player.setWeapon(new Weapon("laser sword"));
            updateWeaponLabel();
            statusLabel.setLabel("Switched to Laser Sword");
        }
        if (key == KeyEvent.VK_3) {
            player.setWeapon(new Weapon("laser gun"));
            updateWeaponLabel();
            statusLabel.setLabel("Switched to Laser Gun");
        }
        if (key == KeyEvent.VK_4) {
            player.setWeapon(new Weapon("bow and arrow"));
            updateWeaponLabel();
            statusLabel.setLabel("Switched to Bow and Arrow");
        }
        if (key == KeyEvent.VK_5) {
            player.setWeapon(new Weapon("axe"));
            updateWeaponLabel();
            statusLabel.setLabel("Switched to Axe");
        }

        if (key == KeyEvent.VK_6) {
            currentDifficulty = Maze.EASY;
            restartGameplay();
            return;
        }
        if (key == KeyEvent.VK_7) {
            currentDifficulty = Maze.MEDIUM;
            restartGameplay();
            return;
        }
        if (key == KeyEvent.VK_8) {
            currentDifficulty = Maze.HARD;
            restartGameplay();
            return;
        }

        if (key == KeyEvent.VK_SPACE && player.getWeapon().isRanged()) {
            shootProjectile();
            return;
        }

        if (key == KeyEvent.VK_SPACE && enemies != null) {
            showAttackEffect();
            player.attack(enemies);
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
    }
}