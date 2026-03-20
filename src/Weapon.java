public class Weapon {

    private String name;
    private int damage;

    public Weapon(String type) {
        switch (type.toLowerCase()) {
            case "laser sword":
                name = "Laser Sword";
                damage = 25;
                break;

            case "laser gun":
                name = "Laser Gun";
                damage = 18;
                break;

            case "iron sword":
                name = "Iron Sword";
                damage = 20;
                break;

            case "bow and arrow":
                name = "Bow and Arrow";
                damage = 15;
                break;

            case "axe":
                name = "Axe";
                damage = 30;
                break;

            default:
                name = "Iron Sword";
                damage = 20;
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
}