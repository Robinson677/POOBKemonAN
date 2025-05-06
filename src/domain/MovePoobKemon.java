package domain;

/**
 * Representa un movimiento que puede usar un Pokémon.
 */
public abstract class MovePoobKemon {
    public enum Category { FISICO, ESPECIAL, ESTADO }

    private String name;
    private TypePoobKemon type;
    private Category category;
    private int potency;
    private int precision;
    private int powerPoints;
    private int ppCurrent;
    public String secundaryEffect;
    private int priority;

    public MovePoobKemon(String name,
                         TypePoobKemon type,
                         Category category,
                         int potency,
                         int precision,
                         int powerPoints,
                         String secundaryEffect,
                         int priority) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.potency = potency;
        this.precision = precision;
        this.powerPoints = powerPoints;
        this.ppCurrent = powerPoints;
        this.secundaryEffect = secundaryEffect;
        this.priority = priority;
    }

    public void use() {
        if (ppCurrent > 0) ppCurrent--;
    }

    public int getPowerPoints() {
        return ppCurrent;
    }

    public void run(PoobKemon attacker, PoobKemon objective) {
        if (ppCurrent <= 0) return;
        use();

        double modPrec = getMultiplier(attacker.getModifyPrecision());
        double modEva  = getMultiplier(objective.getModifyEvasion());
        double precReal = precision * (modPrec / modEva);
        if ((int)(Math.random() * 100) + 1 > precReal) {
            System.out.println(attacker.getName() + " falló " + name + "!");
            return;
        }

        switch (category) {
            case FISICO: {
                int dmg = calculateDamage(
                        attacker.getMove(),
                        objective.getDefense(),
                        this.type,
                        objective.getTypePoobKemon()
                );
                objective.takeDamage(dmg);
                System.out.printf("%s usó %s e hizo %d puntos de daño.%n",
                        attacker.getName(), name, dmg);
                break;
            }
            case ESPECIAL: {
                int dmg = calculateDamage(
                        attacker.getSpecialAttack(),
                        objective.getSpecialDefence(),
                        this.type,
                        objective.getTypePoobKemon()
                );
                objective.takeDamage(dmg);
                System.out.printf("%s usó %s e hizo %d puntos de daño.%n",
                        attacker.getName(), name, dmg);
                break;
            }
            case ESTADO:
                System.out.printf("%s usó %s para aplicar un efecto.%n",
                        attacker.getName(), name);
                break;
        }
    }

    protected int calculateDamage(int attack, int defense,
                                  TypePoobKemon typeMovement,
                                  TypePoobKemon typeDefense) {
        double base = potency * ((double) attack / defense);
        double effectivity = TypeEffectivity.get(typeMovement, typeDefense);
        double modify = effectivity;

        return Math.max(1, (int)(base * modify));
    }

    protected double getMultiplier(int level) {
        double[] board = {
                3.0/9, 3.0/8, 3.0/7, 3.0/6, 3.0/5, 3.0/4,
                1.0,
                4.0/3, 5.0/3, 6.0/3, 7.0/3, 8.0/3, 9.0/3
        };
        return board[level + 6];
    }

    public TypePoobKemon getMoveType() {
        return type;
    }

    public int getPrecision() {
        return precision;
    }

    /**
     * Nombre del movimiento
     * */
    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getPotency() {
        return potency;
    }

    public int getMaxPowerPoints() {
        return powerPoints;
    }


    public int getCurrentPowerPoints() {
        return ppCurrent;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] PP: %d/%d", name, category, ppCurrent, powerPoints);
    }
}
