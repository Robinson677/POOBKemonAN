package domain;

/**
 * Movimiento con efecto secundario
 */
public class BolaSombra extends MovePoobKemon {

    /**
     * Constructor de BolaSombra
     */
    public BolaSombra() {
        super("Bola Sombra", TypePoobKemon.FANTASMA, Category.ESPECIAL,
                80,
                100,
                24,
                "Reducir la defensa un 20%",
                0);
    }

    /**
     *
     * @param attacker pokemon
     * @param objective pokemon
     */
    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        super.run(attacker, objective);

        if (!objective.isWeakened() && Math.random() < 0.20) {
            objective.lowerSpecialDefence(1);
            System.out.println(objective.getName() + " perdió defensa especial.");
        }
    }
}
