package domain;

public class PunioSombra extends MovePoobKemon {

    public PunioSombra() {
        super("Puño Sombra", TypePoobKemon.FANTASMA, Category.FISICO,
                60,
                100,
                32,
                null,
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        if (getPowerPoints() <= 0) return;
        use();
        int damage = calculateDamage(
                attacker.getMove(),
                objective.getDefense(),
                this.getMoveType(),
                objective.getTypePoobKemon()
        );

        objective.takeDamage(damage);
        System.out.println(attacker.getName() + " usó Puño Sombra e hizo " + damage + " de daño.");
    }
}
