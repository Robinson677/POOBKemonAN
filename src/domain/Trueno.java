package domain;

public class Trueno extends MovePoobKemon {

    public Trueno() {
        super("Trueno", TypePoobKemon.ELECTRICO, Category.ESPECIAL,
                110,
                70,
                16,
                "PARALIZAR un 30%",
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
            System.out.println(attacker.getName() + " falló Trueno.");
            return;
        }


        int damage = calculateDamage(
                attacker.getSpecialAttack(),
                objective.getSpecialDefence(),
                getMoveType(),
                objective.getTypePoobKemon()
        );

        objective.takeDamage(damage);
        System.out.println(attacker.getName() + " usó Trueno e hizo " + damage + " de daño.");

        if (!objective.isWeakened()
                && !"PARALIZADO".equals(objective.getAlteredState())
                && Math.random() < 0.30) {
            objective.setAlteredState("PARALIZADO");
            System.out.println(objective.getName() + " quedó paralizado.");
        }
    }
}

