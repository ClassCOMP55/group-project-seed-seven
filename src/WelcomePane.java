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
		addDescriptionButton();
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
		startImage.setLocation((mainScreen.getWidth() / mainScreen.getHeight()), (mainScreen.getWidth() / mainScreen.getHeight()));
		
		contents.add(startImage);
		mainScreen.add(startImage);
	}
	
	private void addDescriptionButton() {
		GImage moreButton = new GImage("s_button.png", 200, 400);
		moreButton.scale(0.3, 0.3);
		moreButton.setLocation((mainScreen.getWidth() - moreButton.getWidth())/ 2, 250);
		
		contents.add(moreButton);
		mainScreen.add(moreButton);

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (mainScreen.getElementAtLocation(e.getX(), e.getY()) == contents.get(1)) {
			mainScreen.switchToDescriptionScreen();
		}
	}

}
