public class Weapon {

    private String name;
    private int damage;
    private int cooldown;
    private double range;

    public Weapon(String type) {
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

    public void attack(Player player, Enemy enemy) {
        if (enemy == null || player == null) return;

        double dx = enemy.getX() - player.getX();
        double dy = enemy.getY() - player.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= this.range) {
            enemy.takeDamage(damage);
            System.out.println(name + " hit the enemy for " + damage + " damage");
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