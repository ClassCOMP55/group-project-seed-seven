import java.util.ArrayList;

public class Weapon {

    // Basic weapon stats used by the player combat system
    private String name;
    private int damage;
    private int cooldown;
    private double range;

    public Weapon(String type) {
        // Balanced values
        switch (type.toLowerCase()) {

            case "laser sword":
                name = "Laser Sword";
                damage = 22;
                cooldown = 1;
                range = 50;
                break;

            case "laser gun":
                name = "Laser Gun";
                damage = 16;
                cooldown = 2;
                range = 180;
                break;

            case "iron sword":
                name = "Iron Sword";
                damage = 14;
                cooldown = 1;
                range = 45;
                break;

            case "bow and arrow":
                name = "Bow and Arrow";
                damage = 12;
                cooldown = 2;
                range = 160;
                break;

            case "axe":
                name = "Axe";
                damage = 28;
                cooldown = 3;
                range = 40;
                break;

            default:
                name = "Iron Sword";
                damage = 14;
                cooldown = 1;
                range = 45;
                break;
        }
    }

    public void attack(Player player, ArrayList<Enemy> enemies) {
        if (enemies == null || player == null) return;

        for (Enemy e : enemies) {
            double dx = e.getX() - player.getX();
            double dy = e.getY() - player.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Only damage enemies within the specific weapon's range
            if (distance <= this.range) {
                e.takeDamage(damage);
                System.out.println(name + " hit an enemy for " + damage + " damage");
            }
        }
    }
    
    public boolean isRanged() {
        return name.equals("Laser Gun") || name.equals("Bow and Arrow");
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getRange() {
        return range;
    }
}