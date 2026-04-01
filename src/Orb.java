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
	
	/**
	 * Logic for when a player interacts with/collects the orb,
	 * as defined in the UML and class description.
	 */
	public void collect(Player player) {
		System.out.println("Orb collected! Value: " + value);
		
		/**
		 * Logic to increment the player's EXP
		 * The report mentions the player's collected orbs
		 * and has an "orbsCollected" attribute.
		 */
		
		// Remove the orb from the game world
		if (this.getParent() != null) {
			this.getParent().remove(this);
		}
	}
	
	// The value of this orb.
	public int getValue() {
		return this.value;
	}
}
