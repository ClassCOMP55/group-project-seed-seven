import acm.graphics.*;
import java.awt.Color;

/**
 * Represents collectible objects dropped
 * by enemies or found in the maze.
 * Based on the System Design Report's UML and descriptions.
 */
public class Orb extends GCompound {
	
	private int value; // Point or EXP value of the orb
	
	// Default constructor as used in Enemy.java's handleDeath() method.
	public Orb() {
		this(10); // Default value
	}
	
	// Constructs an orb with a specific value.
	public Orb(int value) {
		this.value = value;
		renderOrb();
	}
	
	// Visual representation of the orb using ACM graphics
	private void renderOrb() {
		GOval appearance = new GOval(0, 0, 15, 15);
		appearance.setFilled(true);
		appearance.setFillColor(Color.YELLOW);
		add(appearance);
	}
	
	

}
