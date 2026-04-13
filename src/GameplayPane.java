import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class GameplayPane extends GraphicsPane {

    private Player player;
    private Enemy testEnemy;
    private GOval enemyMarker;
    private GOval attackEffect;
    private boolean gameLoopStarted = false;

    private GLabel controlsLabel;
    private GLabel weaponLabel;
    private GLabel statusLabel;

    public GameplayPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }

    @Override
    public void showContent() {
        addBackground();
        addLabels();
        addBackButton();
        addPlayer();
        addEnemy();
        startGameLoop();
    }

    @Override
    public void hideContent() {
        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();
        gameLoopStarted = false;
        attackEffect = null;
        enemyMarker = null;
        testEnemy = null;
    }

    private void addBackground() {
        GImage background = new GImage("background.png");
        background.scale(1, 1);
        background.setLocation(0, 0);

        mainScreen.add(background);
        background.sendToBack();
        contents.add(background);
    }

    private void addLabels() {
        controlsLabel = new GLabel(
            "WASD = move   |   SPACE = attack   |   1 Iron   2 Laser Sword   3 Laser Gun   4 Bow   5 Axe",
            20, 40
        );
        controlsLabel.setColor(Color.WHITE);
        controlsLabel.setFont("DialogInput-BOLD-16");

        weaponLabel = new GLabel("Current Weapon: Iron Sword", 20, 65);
        weaponLabel.setColor(Color.WHITE);
        weaponLabel.setFont("DialogInput-BOLD-16");

        statusLabel = new GLabel("Gameplay screen ready", 20, 90);
        statusLabel.setColor(Color.WHITE);
        statusLabel.setFont("DialogInput-PLAIN-16");

        contents.add(controlsLabel);
        contents.add(weaponLabel);
        contents.add(statusLabel);

        mainScreen.add(controlsLabel);
        mainScreen.add(weaponLabel);
        mainScreen.add(statusLabel);
    }

    private void addBackButton() {
        GImage backButton = new GImage("lev_button.png", 200, 400);
        backButton.scale(0.2, 0.2);
        backButton.setLocation((mainScreen.getWidth() - backButton.getWidth()) / 2, 430);

        contents.add(backButton);
        mainScreen.add(backButton);
    }

    private void addPlayer() {
        player = new Player(200, 250, 100);
        contents.add(player);
        mainScreen.add(player);
    }

    private void addEnemy() {
        testEnemy = new Enemy(500, 250, EnemyType.MUTANT);
        contents.add(testEnemy);
        mainScreen.add(testEnemy);

        // Visible marker so Moses can later replace this with real visuals
        enemyMarker = new GOval(500, 250, 30, 30);
        enemyMarker.setFilled(true);
        enemyMarker.setFillColor(new Color(128, 0, 128));
        enemyMarker.setColor(Color.BLACK);

        contents.add(enemyMarker);
        mainScreen.add(enemyMarker);
        enemyMarker.sendToFront();
    }

    private void startGameLoop() {
        if (gameLoopStarted) return;
        gameLoopStarted = true;

        new Thread(() -> {
            while (gameLoopStarted) {
                player.move();
                player.updateCombat();

                if (testEnemy != null && enemyMarker != null) {
                    if (testEnemy.getParent() != null) {
                        enemyMarker.setLocation(testEnemy.getX(), testEnemy.getY());
                        enemyMarker.sendToFront();
                    } else {
                        mainScreen.remove(enemyMarker);
                        contents.remove(enemyMarker);
                        enemyMarker = null;
                        statusLabel.setLabel("Enemy defeated");
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(4)) {
            mainScreen.switchToWelcomeScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

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

        if (key == KeyEvent.VK_SPACE && testEnemy != null) {
            showAttackEffect();
            player.attack(testEnemy);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) player.setUpPressed(false);
        if (key == KeyEvent.VK_S) player.setDownPressed(false);
        if (key == KeyEvent.VK_A) player.setLeftPressed(false);
        if (key == KeyEvent.VK_D) player.setRightPressed(false);
    }
}