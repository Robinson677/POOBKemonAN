package domain;

public class Forcejeo extends MovePoobKemon {
    public Forcejeo() {
        super("Forcejeo", TypePoobKemon.NORMAL, Category.FISICO,
                50,
                100,
                1,
                null,
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        int dmg = calculateDamage(
                attacker.getMove(),
                objective.getDefense(),
                this.getMoveType(),
                objective.getTypePoobKemon()
        );
        objective.takeDamage(dmg);

        int recoil = Math.max(1, dmg / 2);
        attacker.takeDamage(recoil);

        System.out.printf("%s usó Forcejeo e hizo %d puntos de daño.%n", attacker.getName(), dmg);
        System.out.printf("%s recibió %d puntos de daño por retroceso.%n", attacker.getName(), recoil);
    }
}

