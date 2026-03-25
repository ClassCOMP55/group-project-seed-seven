public class Weapon {

    private String name;
    private int damage;
    private int cooldown;

    public Weapon(String type) {
        switch (type.toLowerCase()) {

            case "laser sword":
                name = "Laser Sword";
                damage = 25;
                cooldown = 1;
                break;

            case "laser gun":
                name = "Laser Gun";
                damage = 18;
                cooldown = 2;
                break;

            case "iron sword":
                name = "Iron Sword";
                damage = 20;
                cooldown = 1;
                break;

            case "bow and arrow":
                name = "Bow and Arrow";
                damage = 15;
                cooldown = 2;
                break;

            case "axe":
                name = "Axe";
                damage = 30;
                cooldown = 3;
                break;

            default:
                name = "Iron Sword";
                damage = 20;
                cooldown = 1;
                break;
        }
    }

    public void attack(Enemy enemy) {
        if (enemy == null) return;

        enemy.takeDamage(damage);
        System.out.println(name + " hit for " + damage + " damage");
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
}