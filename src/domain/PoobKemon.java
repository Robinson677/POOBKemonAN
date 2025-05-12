package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un Pokemon en POOBKemon
 * Todos los pokemon se consideran nivel de 100
 */
public abstract class PoobKemon {
    protected String name;
    protected TypePoobKemon type;
    protected int ppCurrent;
    protected int ppMax;
    protected int move;
    protected int defense;
    protected int specialAttack;
    protected int specialDefence;
    protected int velocity;
    protected int precision;
    protected int evasion;
    protected List<MovePoobKemon> movements;
    protected boolean weakened;
    protected String alteredState = null;
    protected int modifyPrecision = 0;
    protected int modifyEvasion = 0;
    protected int modifyDefense = 0;
    protected String description;
    protected String resourceName;
    protected double attackModifier = 1.0;

    /**
     * Crea un Pokémon de nivel fijo 100.
     */
    public PoobKemon(String name,
                     TypePoobKemon type,
                     int ppMax,
                     int move,
                     int defense,
                     int velocity,
                     int specialAttack,
                     int specialDefence,
                     String description,
                     String resourceName) {
        this.name = name;
        this.type = type;
        this.ppMax = ppMax;
        this.ppCurrent = ppMax;
        this.move = move;
        this.defense = defense;
        this.velocity = velocity;
        this.specialAttack = specialAttack;
        this.specialDefence = specialDefence;
        this.movements = new ArrayList<>();
        this.weakened = false;
        this.description = description;
        this.resourceName = resourceName;
    }

    /**
     * Obtener nombre pokemon
     * @return String del nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Obtener tipo pokemon de los 18
     * @return TypePoobKemon
     */
    public TypePoobKemon getTypePoobKemon() {
        return type;
    }

    /**
     * Los puntos de poder actuales
     * @return int de los restantes
     */
    public int getPpCurrent() {
        return ppCurrent;
    }

    /**
     * Los puntos de poder maximos
     * @return int al maximo
     */
    public int getPpMax() {
        return ppMax;
    }

    /**
     * Obtener la presicion del pokemon
     * @return int de presicion
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Obtener la evasion del pokemon
     * @return int de evasion
     */
    public int getEvasion() {
        return evasion;
    }

    /**
     * Verifica si el pokemon fue derrotado
     * @return true si lo derrotaron false si no
     */
    public boolean isWeakened() {
        return weakened;
    }

    /**
     * Obtiene el movimiento del pokemon
     * @return move
     */
    public int getMove() {
        return move;
    }

    /**
     * Obtiene la defensa del pokemon
     * @return defense
     */
    public int getDefense() {
        return defense;
    }

    /**
     * obtiene el ataque especial del pokemom
     * @return specialAttack
     */
    public int getSpecialAttack() {
        return specialAttack;
    }

    /**
     * obtiene la defensa especial del pokemom
     * @return specialDefence
     */
    public int getSpecialDefence() {
        return specialDefence;
    }

    /**
     * obtiene la velocidad del pokemon
     * @return velocity
     */
    public int getVelocity() {
        return velocity;
    }

    /**
     * Resta vida al Pokemon
     */
    public void takeDamage(int danio) {
        ppCurrent -= danio;
        if (ppCurrent <= 0) {
            ppCurrent = 0;
            weakened = true;
        }
    }


    /**
     * Intenta agregar un nuevo movimiento
     */
    public boolean addMove(MovePoobKemon mov) {
        if (movements.size() < 4) {
            movements.add(mov);
            return true;
        }
        return false;
    }

    /**
     * Cura al pokemon
     */
    public void cure(int points) {
        if (weakened) {
            return;
        }
        ppCurrent += points;
        if (ppCurrent > ppMax) {
            ppCurrent = ppMax;
        }
    }


    /**
     * Obtiene el estado alterado actual del pokemon
     * @return String que representa el estado alterado, o null si no hay estado alterado
     */
    public String getAlteredState() {
        return alteredState;
    }

    /**
     * Establece un nuevo estado alterado para el pokemon
     * @param state Nuevo estado a aplicar
     */
    public void setAlteredState(String state) {
        this.alteredState = state;
    }

    /**
     * Verifica si el pokemon esta actualmente dormido
     * @return true si el estado alterado es "DORMIDO", false en cualquier otro caso
     */
    public boolean isAsSleep() {
        return "DORMIDO".equals(alteredState);
    }

    /**
     * Reduce la defensa especial del pokemon
     * La defensa especial no puede ser menor a 1.
     * @param levels reduce 10 puntos
     */
    public void lowerSpecialDefence(int levels) {
        specialDefence = Math.max(1, specialDefence - (levels * 10));
        System.out.println(name + " perdió defensa especial.");
    }

    /**
     * Reduce la defensa fisica del pokemon
     * La defensa especial no puede ser menor a 1.
     * @param levels reduce 10 puntos
     */
    public void lowerDefence(int levels) {
        defense = Math.max(1, defense - (levels * 10));
        System.out.println(name + " perdió defensa.");
    }

    /**
     * Modifica el modificador de defensa dentro de limites establecidos de -6 a +6
     * @param change la cantidad a modificar
     */
    public void modifyDefense(int change) {
        modifyDefense = Math.max(-6, Math.min(6, modifyDefense + change));
        System.out.println(name + " modificó su defensa (" + modifyDefense + ").");
    }


    /**
     * Obtiene el valor actual del modificador de precision
     * @return int Valor entre -6 y +6 que representa el modificador de precision
     */
    public int getModifyPrecision() {
        return modifyPrecision;
    }

    /**
     * Obtiene el valor actual del modificador de evasion
     * @return int Valor entre -6 y +6 que representa el modificador de evasion
     */
    public int getModifyEvasion() {
        return modifyEvasion;
    }

    /**
     * Ajusta el modificador de precision
     * @param change la cantidad a modificar
     */
    public void modifyPrecision(int change) {
        modifyPrecision = Math.max(-6, Math.min(6, modifyPrecision + change));
    }

    /**
     * Ajusta el modificador de evasion
     * @param change la cantidad a modificar
     */
    public void modifyEvasion(int change) {
        modifyEvasion = Math.max(-6, Math.min(6, modifyEvasion + change));
    }

    /**
     * Obtiene la descripcion de pokemon
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtiene el gift del pokemon
     * @return String resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Obtiene la lista de movimientos disponibles para el pokemon
     * @return List de movimientos
     */
    public List<MovePoobKemon> getMovements() {
        return movements;
    }


    /**
     * Revive a un pokemon debilitado, dejandolo con la mitad de sus PS
     */
    public void revive() {
        if (!weakened) return;
        weakened = false;
        ppCurrent = ppMax / 2;
        System.out.println("DEBUG: Pokémon " + this.getName() + " revive a " + ppCurrent + "/" + ppMax);
    }

    /**
     * Aplica un buff de ataque multiplicativo
     */
    public void buffAttack(double factor) {
        attackModifier *= factor;
    }

    /**
     * Devuelve y resetea el modificador de ataque a 1.0
     */
    public double consumeAttackBuff() {
        double m = attackModifier;
        attackModifier = 1.0;
        return m;
    }

    /**
     * Grito de cada pokemon en el futuro si se puede se pondra el audio
     */
    public abstract void cry();

    /**
     * Genera una representacion de streings del estado actual de pokemon
     * @return String de la info
     */
    @Override
    public String toString() {
        return String.format(
                "%s (Nivel 100) PS: %d/%d - weakened: %b\nMovimientos: %s",
                name, ppCurrent, ppMax, weakened,
                movements.toString()
        );
    }
}
