package domain;

public class Hipnosis extends MovePoobKemon {

    public Hipnosis() {
        super("Hipnosis", TypePoobKemon.PSIQUICO, Category.ESTADO,
                0,
                60,
                32,
                null,
                0);
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        if (getPowerPoints() <= 0) return;

        use();

        int chance = (int) (Math.random() * 100) + 1;
        if (chance > getPrecision()) {
            System.out.println(attacker.getName() + " falló Hipnosis.");
            return;
        }

        if ("DORMIDO".equals(objective.getAlteredState())) {
            System.out.println(objective.getName() + " ya está dormido.");
            return;
        }

        objective.setAlteredState("DORMIDO");
        System.out.println(attacker.getName() + " usó Hipnosis. ¡" + objective.getName() + " se durmió!");
    }
}
