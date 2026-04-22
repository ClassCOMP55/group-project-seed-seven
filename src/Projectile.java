import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.*;

public class Projectile extends GCompound {

	private GOval hitbox;
	private GImage sprite;
    private double dx, dy;   // movement per frame
    private int damage;

    public Projectile(double x, double y, double dx, double dy, int damage, String imageFile) {
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;
        
        sprite = new GImage(imageFile);
        sprite.scale(0.04);

        double angle = Math.toDegrees(Math.atan2(dy, dx));
        sprite.rotate(angle);

        add(sprite);

        hitbox = new GOval(8, 8);
        hitbox.setFilled(true);
        hitbox.setFillColor(new Color(0, 0, 0, 0));
        hitbox.setColor(new Color(0, 0, 0, 0));
        hitbox.setLocation(-4, -4);
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
