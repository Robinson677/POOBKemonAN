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
    private static PoobKemonFight currentFight;

    /**
     * Constructor de MovePoobKemon
     * @param name nombre del movimiento
     * @param type del movimiento varia entre 18
     * @param category puede ser especial, de estado o fisico
     * @param potency el numero de daño que puede hacer
     * @param precision la chance de que se haga el movimiento
     * @param powerPoints el numero de veces que se puede usar un movimiento
     * @param secundaryEffect sobre el pokemon del oponente
     * @param priority si el movimiento tiene mayor prioridad que otros
     */
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


    /**
     * Si se usa un movimiento disminuye su PP
     */
    public void use() {
        if (ppCurrent > 0) ppCurrent--;
    }


    /**
     * Ejecuta el movimiento durante un combat
     * @param attacker El pokemon que usa el movimiento
     * @param objective El pokemon que recibe el ataque
     */
    public void run(PoobKemon attacker, PoobKemon objective) {
        if (ppCurrent <= 0) return;
        use();

        double modPrec = getMultiplier(attacker.getModifyPrecision());
        double modEva  = getMultiplier(objective.getModifyEvasion());
        double precReal = precision * (modPrec / modEva);
        if ((int)(Math.random() * 100) + 1 > precReal) {
            log(attacker.getName() + " falló " + name + "!");
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
                double buff = attacker.consumeAttackBuff();
                dmg = (int)(dmg * buff);
                objective.takeDamage(dmg);
                log(String.format("%s usó %s e hizo %d puntos de daño.",
                        attacker.getName(), name, dmg));
                break;
            }
            case ESPECIAL: {
                int dmg = calculateDamage(
                        attacker.getSpecialAttack(),
                        objective.getSpecialDefence(),
                        this.type,
                        objective.getTypePoobKemon()
                );
                double buff = attacker.consumeAttackBuff();
                dmg = (int)(dmg * buff);
                objective.takeDamage(dmg);
                log(String.format("%s usó %s e hizo %d puntos de daño.",
                        attacker.getName(), name, dmg));
                break;
            }
            case ESTADO:
                if (this instanceof MovePoobKemonData) {
                    ((MovePoobKemonData)this).applySecondaryEffect(attacker, objective);
                }
                break;
        }
    }

    /**
     * Calcula el daño de movimiento
     * @param attack el valor del ataque del pokemon
     * @param defense  el valor de defensa del pokemon
     * @param typeMovement de los 18
     * @param typeDefense el tipo de pokemon defensor
     * @return el daño calculado
     */
    protected int calculateDamage(int attack, int defense,
                                  TypePoobKemon typeMovement,
                                  TypePoobKemon typeDefense) {
        double base = potency * ((double) attack / defense);
        double effectivity = TypeEffectivity.get(typeMovement, typeDefense);
        double modify = effectivity;

        return Math.max(1, (int)(base * modify));
    }

    /**
     * Obtienen el multiplicador de evasion y presicion de modificacion
     * @param level de -6 a 6
     * @return el multiplicador
     */
    protected double getMultiplier(int level) {
        double[] board = {
                3.0/9, 3.0/8, 3.0/7, 3.0/6, 3.0/5, 3.0/4,
                1.0,
                4.0/3, 5.0/3, 6.0/3, 7.0/3, 8.0/3, 9.0/3
        };
        return board[level + 6];
    }

    /**
     * @return el tipo del movimiento
     */
    public TypePoobKemon getMoveType() {
        return type;
    }


    /**
     * @return  el nombre del movimiento
     */
    public String getName() {
        return name;
    }

    /**
     * @return los PP maximos
     */
    public int getMaxPowerPoints() {
        return powerPoints;
    }

    /**
     * @return los PP actuales
     */
    public int getCurrentPowerPoints() {
        return ppCurrent;
    }

    /**
     * Establece la batalla actual entre entrenadores
     * @param fight la logica de la batalla
     */
    public static void setCurrentFight(PoobKemonFight fight) {
        currentFight = fight;
    }

    /**
     * Limpia la batalla
     */
    public static void clearCurrentFight() {
        currentFight = null;
    }

    /**
     * Establece los PP actuales
     * @param pp numero de veces que se puede usar un movimiento
     */
    public void setCurrentPowerPoints(int pp) {
        if (pp > this.powerPoints) {
            this.ppCurrent = this.powerPoints;
        } else if (pp < 0) {
            this.ppCurrent = 0;
        } else {
            this.ppCurrent = pp;
        }
    }


    /**
     * Registra los mensajes en el combate como los movimientos que usa un pokemon
     * @param msg el mensaje
     */
    protected void log(String msg) {
        if (currentFight != null) {
            currentFight.log(msg);
        } else {
            System.out.println(msg);
        }
    }

    /**
     * Para el registro global
     * @param msg el mensaje
     */
    public static void logGlobal(String msg) {
        if (currentFight != null) {
            currentFight.log(msg);
        } else {
            System.out.println(msg);
        }
    }

    /**
     * @return un String de movimiento con el formato que definimos
     */
    @Override
    public String toString() {
        return String.format("%s [%s] PP: %d/%d", name, category, ppCurrent, powerPoints);
    }
}
