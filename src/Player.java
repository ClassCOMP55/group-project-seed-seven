import acm.graphics.GOval;
import java.awt.Color;

public class Player extends Entity {

    private float speed;
    private Weapon weapon;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public Player(int x, int y, int health) {
        super(x, y, health);

        speed = 4.0f;
        weapon = new Weapon("iron sword");

        GOval appearance = new GOval(0, 0, 30, 30);
        appearance.setFilled(true);
        appearance.setFillColor(Color.RED);
        add(appearance);

        setLocation(x, y);
    }

    @Override
    public void move() {
        if (upPressed) {
            y -= speed;
        }
        if (downPressed) {
            y += speed;
        }
        if (leftPressed) {
            x -= speed;
        }
        if (rightPressed) {
            x += speed;
        }

        setLocation(x, y);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("Player took " + damage + " damage. Health: " + health);
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
        super.setLocation(x, y);
    }

    public void attack(Enemy enemy) {
        if (weapon != null) {
            weapon.attack(enemy);
        }
    }

    public void setUpPressed(boolean value) {
        upPressed = value;
    }

    public void setDownPressed(boolean value) {
        downPressed = value;
    }

    public void setLeftPressed(boolean value) {
        leftPressed = value;
    }

    public void setRightPressed(boolean value) {
        rightPressed = value;
    }
}