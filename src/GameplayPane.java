import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;

public class GameplayPane extends GraphicsPane {

    private Player player;
    private Enemy enemy;
    private GOval enemyMarker;
    private GOval attackEffect;

    private GLabel controlsLabel;
    private GLabel weaponLabel;
    private GLabel statusLabel;

    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private Maze maze;
    private int currentDifficulty = Maze.EASY;

    private GImage backButton;

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
        gameLoopStarted = false;
        loopVersion++;

        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();

        projectiles.clear();
        attackEffect = null;
        enemyMarker = null;
        enemy = null;
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
        double enemyX = maze.getEnemySpawnX();
        double enemyY = maze.getEnemySpawnY();

        EnemyType type = EnemyType.MUTANT;
        if (currentDifficulty == Maze.MEDIUM) type = EnemyType.SKELETON;
        if (currentDifficulty == Maze.HARD) type = EnemyType.ALIEN;

        enemy = new Enemy((float) enemyX, (float) enemyY, type);
        contents.add(enemy);
        mainScreen.add(enemy);

        enemyMarker = new GOval(enemyX, enemyY, 30, 30);
        enemyMarker.setFilled(true);

        if (currentDifficulty == Maze.EASY) {
            enemyMarker.setFillColor(new Color(128, 0, 128));
        } else if (currentDifficulty == Maze.MEDIUM) {
            enemyMarker.setFillColor(Color.BLUE);
        } else {
            enemyMarker.setFillColor(Color.RED);
        }

        enemyMarker.setColor(Color.BLACK);

        contents.add(enemyMarker);
        mainScreen.add(enemyMarker);
        enemyMarker.sendToFront();
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
                if (player != null && maze != null) {
                    player.move(maze);
                    player.updateCombat();
                }

                if (enemy != null && player != null && maze != null && enemy.getParent() != null) {
                    enemy.moveTowardsPlayer(player, maze);
                }

                for (int i = 0; i < projectiles.size(); i++) {
                    Projectile p = projectiles.get(i);
                    p.move();

                    boolean projectileHit = false;

                    if (enemy != null && enemyMarker != null && enemy.getParent() != null) {
                        double px = p.getX();
                        double py = p.getY();

                        double mx = enemyMarker.getX();
                        double my = enemyMarker.getY();
                        double mw = enemyMarker.getWidth();
                        double mh = enemyMarker.getHeight();

                        boolean hitMarker =
                            px >= mx && px <= mx + mw &&
                            py >= my && py <= my + mh;

                        if (hitMarker) {
                            enemy.takeDamage(p.getDamage());
                            projectileHit = true;
                        }
                    }

                    if (projectileHit) {
                        mainScreen.remove(p);
                        contents.remove(p);
                        projectiles.remove(i);
                        i--;
                        continue;
                    }

                    if (p.getX() < 0 || p.getX() > mainScreen.getWidth() ||
                        p.getY() < 0 || p.getY() > mainScreen.getHeight()) {

                        mainScreen.remove(p);
                        contents.remove(p);
                        projectiles.remove(i);
                        i--;
                    }
                }

                if (enemy != null && enemyMarker != null) {
                    if (enemy.getParent() != null) {
                        enemyMarker.setLocation(enemy.getX(), enemy.getY());
                        enemyMarker.sendToFront();
                    } else {
                        mainScreen.remove(enemyMarker);
                        contents.remove(enemyMarker);
                        enemyMarker = null;
                        statusLabel.setLabel("Enemy defeated!");
                    }
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

        if (key == KeyEvent.VK_SPACE && enemy != null) {
            showAttackEffect();
            player.attack(enemy);
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