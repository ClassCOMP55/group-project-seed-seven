import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;

public class LevelSelectPane extends GraphicsPane {
    private GImage backButton;
    private GImage levelOneButton;
    private GImage levelTwoButton;
    private GImage levelThreeButton;
    private GImage levelFourButton;

	public LevelSelectPane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addBackground();
		addText();
		addBackButton();
		addLevelOneButton();
		addLevelTwoButton();
		addLevelThreeButton();
		addLevelFourButton();
	}

	@Override
	public void hideContent() {
		for (GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addBackground() {
		GImage startImage = new GImage("background.png", 800, 600);
		startImage.scale(1, 1);
		startImage.setLocation(
            (mainScreen.getWidth() / mainScreen.getHeight()),
            (mainScreen.getWidth() / mainScreen.getHeight())
        );
		
		contents.add(startImage);
		mainScreen.add(startImage);
	}
	
	private void addText() {
		GLabel text = new GLabel("Choose a level", 100, 70);
		text.setColor(Color.BLUE);
		text.setFont("DialogInput-PLAIN-24");
		text.setLocation((mainScreen.getWidth() - text.getWidth()) / 2, 70);
		
		contents.add(text);
		mainScreen.add(text);
	}
	
	private void addBackButton() {
		backButton = new GImage("back_button.png", 200, 400);
		backButton.scale(0.2, 0.2);
		backButton.setLocation((mainScreen.getWidth() - backButton.getWidth()) / 2, 440);
		
		contents.add(backButton);
		mainScreen.add(backButton);
	}
	
	private void addLevelOneButton() {
		levelOneButton = new GImage("level1.png", 200, 400);
		levelOneButton.scale(0.2, 0.2);
		levelOneButton.setLocation(250, 160);
		
		contents.add(levelOneButton);
		mainScreen.add(levelOneButton);
	}
	
	private void addLevelTwoButton() {
		levelTwoButton = new GImage("level2.png", 200, 400);
		levelTwoButton.scale(0.2, 0.2);
		levelTwoButton.setLocation(400, 160);
		
		contents.add(levelTwoButton);
		mainScreen.add(levelTwoButton);
	}
	
	private void addLevelThreeButton() {
		levelThreeButton = new GImage("level3.png", 200, 400);
		levelThreeButton.scale(0.2, 0.2);
		levelThreeButton.setLocation(250, 300);
		
		contents.add(levelThreeButton);
		mainScreen.add(levelThreeButton);
	}
	
	private void addLevelFourButton() {
		levelFourButton = new GImage("level4.png", 200, 400);
		levelFourButton.scale(0.2, 0.2);
		levelFourButton.setLocation(400, 300);
		
		contents.add(levelFourButton);
		mainScreen.add(levelFourButton);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
        GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());

        if (clicked == backButton) {
            mainScreen.switchToWelcomeScreen();
        } else if (clicked == levelOneButton) {
            mainScreen.switchToGameplayScreen(1);
        } else if (clicked == levelTwoButton) {
            mainScreen.switchToGameplayScreen(2);
        } else if (clicked == levelThreeButton) {
            mainScreen.switchToGameplayScreen(3);
        } else if (clicked == levelFourButton) {
            mainScreen.switchToGameplayScreen(4);
        }
	}
}