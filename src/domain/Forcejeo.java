package domain;

public class Forcejeo extends MovePoobKemon {
    public Forcejeo() {
        super("Forcejeo",
                TypePoobKemon.NORMAL,
                Category.FISICO,
                50,
                100,
                1,
                null,
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        use();

        int dmg = calculateDamage(
                attacker.getMove(),
                objective.getDefense(),
                getMoveType(),
                objective.getTypePoobKemon()
        );
        objective.takeDamage(dmg);
        log(String.format("%s usó Forcejeo e hizo %d puntos de daño.", attacker.getName(), dmg));

        int recoil = Math.max(1, dmg / 2);
        attacker.takeDamage(recoil);
        log(String.format("%s recibió %d puntos de daño por retroceso.", attacker.getName(), recoil));
    }
}

