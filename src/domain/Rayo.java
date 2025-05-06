package domain;

public class Rayo extends MovePoobKemon {

    public Rayo() {
        super("Rayo", TypePoobKemon.ELECTRICO, Category.ESPECIAL,
                90,
                100,
                24,
                "PARALIZA un 10%",
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        super.run(attacker, objective);

        if (!objective.isWeakened()
                && !"PARALIZADO".equals(objective.getAlteredState())
                && Math.random() < 0.10) {
            objective.setAlteredState("PARALIZADO");
            System.out.println(objective.getName() + " quedó paralizado.");
        }
    }
}


