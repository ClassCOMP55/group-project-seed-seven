public class Weapon {

    // Basic weapon stats used by the player combat system
    private String name;
    private int damage;
    private int cooldown;
    private double range;

    public Weapon(String type) {
        // Assign weapon stats based on the selected weapon type
        switch (type.toLowerCase()) {
            case "laser sword":
                name = "Laser Sword";
                damage = 22;
                cooldown = 1;
                range = 50;
                break;

            case "laser gun":
                name = "Laser Gun";
                damage = 14;
                cooldown = 2;
                range = 180;
                break;

            case "iron sword":
                name = "Iron Sword";
                damage = 11;
                cooldown = 1;
                range = 45;
                break;

            case "bow and arrow":
                name = "Bow and Arrow";
                damage = 10;
                cooldown = 2;
                range = 160;
                break;

            case "axe":
                name = "Axe";
                damage = 18;
                cooldown = 3;
                range = 40;
                break;

            default:
                // Default weapon if input does not match a known weapon
                name = "Iron Sword";
                damage = 20;
                cooldown = 1;
                range = 45;
                break;
        }
    }

    public void attack(Enemy enemy) {
        // Apply weapon damage directly to the enemy
        if (enemy == null) return;

        enemy.takeDamage(damage);
        System.out.println(name + " hit for " + damage + " damage");
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