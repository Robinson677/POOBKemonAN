package domain;

public class PunioTrueno extends MovePoobKemon {

    public PunioTrueno() {
        super("Puño Trueno", TypePoobKemon.ELECTRICO, Category.FISICO,
                75,
                100,
                24,
                "PARALIZAR un 10%",
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        if (getPowerPoints() <= 0) return;
        use();

        double modPrec = getMultiplier(attacker.getModifyPrecision());
        double modEva  = getMultiplier(objective.getModifyEvasion());
        double precisionReal = getPrecision() * (modPrec / modEva);

        int chance = (int)(Math.random() * 100) + 1;
        if (chance > precisionReal) {
            System.out.println(attacker.getName() + " falló Puño Trueno.");
            return;
        }

        int damage = calculateDamage(
                attacker.getMove(),
                objective.getDefense(),
                this.getMoveType(),
                objective.getTypePoobKemon()
        );
        objective.takeDamage(damage);
        System.out.println(attacker.getName() + " usó Puño Trueno e hizo " + damage + " de daño.");

        if (!objective.isWeakened()
                && !"PARALIZADO".equals(objective.getAlteredState())
                && Math.random() < 0.10) {
            objective.setAlteredState("PARALIZADO");
            System.out.println(objective.getName() + " quedó paralizado.");
        }
    }
}

