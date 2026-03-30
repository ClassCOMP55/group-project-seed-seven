import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class DescriptionPane extends GraphicsPane{
	public DescriptionPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addText();
		addBackButton();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addText() {
		GLabel text = new GLabel("This is an example of a new screen with some description!", 100, 70);
		text.setColor(Color.BLUE);
		text.setFont("DialogInput-PLAIN-24");
		text.setLocation((mainScreen.getWidth() - text.getWidth()) / 2, 70);
		
		contents.add(text);
		mainScreen.add(text);
	}
	
	private void addBackButton() {
		GImage backButton = new GImage("back.jpg", 200, 400);
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
TEMP WASD TEST VERSION OF DESCRIPTIONPANE

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class DescriptionPane extends GraphicsPane {

    private Player player;
    private boolean gameLoopStarted = false;

    public DescriptionPane(MainApplication mainScreen) {
        this.mainScreen = mainScreen;
    }

    @Override
    public void showContent() {
        addText();
        addBackButton();
        addPlayer();
        startGameLoop();
    }

    @Override
    public void hideContent() {
        for (GObject item : contents) {
            mainScreen.remove(item);
        }
        contents.clear();
        gameLoopStarted = false;
    }

    private void addText() {
        GLabel text = new GLabel("WASD movement test", 100, 70);
        text.setColor(Color.BLUE);
        text.setFont("DialogInput-PLAIN-24");
        text.setLocation((mainScreen.getWidth() - text.getWidth()) / 2, 70);

        contents.add(text);
        mainScreen.add(text);
    }

    private void addBackButton() {
        GImage backButton = new GImage("back.jpg", 200, 400);
        backButton.scale(0.3, 0.3);
        backButton.setLocation((mainScreen.getWidth() - backButton.getWidth()) / 2, 400);

        contents.add(backButton);
        mainScreen.add(backButton);
    }

    private void addPlayer() {
        player = new Player(350, 250, 100);
        contents.add(player);
        mainScreen.add(player);
    }

    private void startGameLoop() {
        if (gameLoopStarted) return;
        gameLoopStarted = true;

        new Thread(() -> {
            while (gameLoopStarted) {
                player.move();
                mainScreen.pause(16);
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(1)) {
            mainScreen.switchToWelcomeScreen();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.setUpPressed(true);
        }
        if (key == KeyEvent.VK_S) {
            player.setDownPressed(true);
        }
        if (key == KeyEvent.VK_A) {
            player.setLeftPressed(true);
        }
        if (key == KeyEvent.VK_D) {
            player.setRightPressed(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            player.setUpPressed(false);
        }
        if (key == KeyEvent.VK_S) {
            player.setDownPressed(false);
        }
        if (key == KeyEvent.VK_A) {
            player.setLeftPressed(false);
        }
        if (key == KeyEvent.VK_D) {
            player.setRightPressed(false);
        }
    }
}
*/
