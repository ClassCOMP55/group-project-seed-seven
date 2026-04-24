import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class SettingsPane extends GraphicsPane{
	public SettingsPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addBackground();
		addBackButton();
		addText();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addBackground(){
		GImage startImage = new GImage("background.png", 800, 600);
		startImage.scale(1, 1);
		startImage.setLocation((mainScreen.getWidth() / mainScreen.getHeight()), (mainScreen.getWidth() / mainScreen.getHeight()));
		
		contents.add(startImage);
		mainScreen.add(startImage);
	}
	
	private void addText() {
		GLabel text = new GLabel("Nothing to see here :)", 100, 70);
		text.setColor(Color.BLUE);
		text.setFont("DialogInput-PLAIN-24");
		text.setLocation((mainScreen.getWidth() - text.getWidth()) / 2, 350);
		
		contents.add(text);
		mainScreen.add(text);
	}
	
	private void addBackButton() {
		GImage backButton = new GImage("back_button.png", 200, 400);
		backButton.scale(0.3, 0.3);
		backButton.setLocation((mainScreen.getWidth() - backButton.getWidth())/ 2, 400);
		
		contents.add(backButton);
		mainScreen.add(backButton);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(1)) {
			mainScreen.switchToWelcomeScreen();
		}
	}

}

/*
TEMP WASD + COMBAT TEST VERSION OF DESCRIPTIONPANE
Uncomment only for testing player movement, weapon switching, and combat.

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class DescriptionPane extends GraphicsPane {

    private Player player;
    private Enemy testEnemy;
    private GOval enemyMarker;
    private boolean gameLoopStarted = false;
    private GOval attackEffect;

    private GLabel controlsLabel;
    private GLabel weaponLabel;

    public DescriptionPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }

    @Override
    public void showContent() {
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

    private void addLabels() {
        controlsLabel = new GLabel("WASD move | SPACE attack | 1 Iron | 2 Laser Sword | 3 Laser Gun | 4 Bow | 5 Axe", 20, 50);
        controlsLabel.setColor(Color.BLUE);
        controlsLabel.setFont("DialogInput-PLAIN-18");

        weaponLabel = new GLabel("Current Weapon: Iron Sword", 20, 85);
        weaponLabel.setColor(Color.BLACK);
        weaponLabel.setFont("DialogInput-BOLD-18");

        contents.add(controlsLabel);
        contents.add(weaponLabel);
        mainScreen.add(controlsLabel);
        mainScreen.add(weaponLabel);
    }

    private void addBackButton() {
        GImage backButton = new GImage("back.jpg", 200, 400);
        backButton.scale(0.3, 0.3);
        backButton.setLocation((mainScreen.getWidth() - backButton.getWidth()) / 2, 400);

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
                        System.out.println("Enemy defeated.");
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

        double effectX = player.getX() + 35;
        double effectY = player.getY();

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
        if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(2)) {
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
        }
        if (key == KeyEvent.VK_2) {
            player.setWeapon(new Weapon("laser sword"));
            updateWeaponLabel();
        }
        if (key == KeyEvent.VK_3) {
            player.setWeapon(new Weapon("laser gun"));
            updateWeaponLabel();
        }
        if (key == KeyEvent.VK_4) {
            player.setWeapon(new Weapon("bow and arrow"));
            updateWeaponLabel();
        }
        if (key == KeyEvent.VK_5) {
            player.setWeapon(new Weapon("axe"));
            updateWeaponLabel();
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
*/