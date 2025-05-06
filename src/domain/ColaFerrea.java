package domain;

public class ColaFerrea extends MovePoobKemon {

    public ColaFerrea() {
        super("Cola Férrea", TypePoobKemon.ACERO, Category.FISICO,
                100,
                75,
                24,
                "Reducir la defensa un 30%",
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        super.run(attacker, objective);

        if (!objective.isWeakened() && Math.random() < 0.30) {
            objective.modifyDefense(-1);
            System.out.println(objective.getName() + " bajó su defensa.");
        }
    }
}
