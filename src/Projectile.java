import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GOval;

public class Projectile extends GCompound {

	private GOval hitbox;
    private double dx, dy;   // movement per frame
    private int damage;

    public Projectile(double x, double y, double dx, double dy, int damage) {
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;

        hitbox = new GOval(10, 10);
        hitbox.setFilled(true);
        hitbox.setFillColor(Color.RED);

        add(hitbox);
        setLocation(x, y);
    }

    public void move() {
        setLocation(getX() + dx, getY() + dy);
    }

    public boolean collidesWith(Enemy enemy) {
        return hitbox.getBounds().intersects(enemy.getBounds());
    }

    public int getDamage() {
        return damage;
    }

}
