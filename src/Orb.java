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

}
