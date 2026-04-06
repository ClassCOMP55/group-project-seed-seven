import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;


public class WelcomePane extends GraphicsPane{
	public WelcomePane(MainApplication mainScreen) {
		this.mainScreen = mainScreen;
	}
	
	@Override
	public void showContent() {
		addPicture();
		addStartButton();
		addSettingsButton();
		addLevelSelectButton();
		addLogo();
	}

	@Override
	public void hideContent() {
		for(GObject item : contents) {
			mainScreen.remove(item);
		}
		contents.clear();
	}
	
	private void addPicture(){
		GImage startImage = new GImage("background.png", 800, 600);
		startImage.scale(1, 1);
		startImage.setLocation(1.33, 1.33);
		
		contents.add(startImage);
		mainScreen.add(startImage);
	}
	
	private void addLogo(){
		GImage logoImage = new GImage("DJlogo.png", 800, 600);
		logoImage.scale(0.6, 0.6);
		logoImage.setLocation((mainScreen.getWidth() - logoImage.getWidth())/ 2, 100);
		
		contents.add(logoImage);
		mainScreen.add(logoImage);
	}
	
	private void addStartButton() {
		GImage startButton = new GImage("s_button.png", 200, 400);
		startButton.scale(0.3, 0.3);
		startButton.setLocation((mainScreen.getWidth() - startButton.getWidth())/ 2, 250);
		
		contents.add(startButton);
		mainScreen.add(startButton);
	}
	
	private void addSettingsButton() {
		GImage settingsButton = new GImage("set_button.png", 200, 400);
		settingsButton.scale(0.18, 0.18);
		settingsButton.setLocation(268, 350);
		
		contents.add(settingsButton);
		mainScreen.add(settingsButton);
	}
	
	private void addLevelSelectButton() {
		GImage levelsButton = new GImage("lev_button.png", 200, 400);
		levelsButton.scale(0.18, 0.18);
		levelsButton.setLocation(405, 350);
		
		contents.add(levelsButton);
		mainScreen.add(levelsButton);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		GObject clicked = mainScreen.getElementAtLocation(e.getX(), e.getY());
		if (clicked == contents.get(1)) {
			mainScreen.switchToGameplayScreen();
		}
		else if (clicked == contents.get(2)) {
			mainScreen.switchToSettingsScreen();
		}
		else if (clicked == contents.get(3)) {
			mainScreen.switchToLevelSelectScreen();
		}
	}

}
