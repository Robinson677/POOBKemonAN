package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa un Pokémon en POOBkemonBattle.
 * Todos los Pokémon se consideran nivel 100, por lo que no se guarda nivel como atributo.
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

    // Getters básicos (sin nivel)
    public String getName() {
        return name;
    }

    public TypePoobKemon getTypePoobKemon() {
        return type;
    }

    public int getPpCurrent() {
        return ppCurrent;
    }

    public int getPpMax() {
        return ppMax;
    }

    public int getPrecision() {
        return precision;
    }

    public int getEvasion() {
        return evasion;
    }

    public boolean isWeakened() {
        return weakened;
    }

    public int getMove() {
        return move;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public int getSpecialDefence() {
        return specialDefence;
    }

    public int getVelocity() {
        return velocity;
    }

    /**
     * Resta vida al Pokemon.
     */
    public void takeDamage(int danio) {
        ppCurrent -= danio;
        if (ppCurrent <= 0) {
            ppCurrent = 0;
            weakened = true;
        }
    }

    /**
     * Usa un movimiento
     */
    public void useMove(int indice, PoobKemon objetivo) {
        if (weakened) return;

        boolean sinMovimientos = noMoveAvailable();

        if (!sinMovimientos) {
            if (indice >= 0 && indice < movements.size()) {
                MovePoobKemon mov = movements.get(indice);
                if (mov != null && mov.getPowerPoints() > 0) {
                    mov.run(this, objetivo);
                    return;
                }
            }
            System.out.println(name + " no pudo usar ese movimiento.");
        } else {
            System.out.println(name + " no tiene más movimientos... ¡usó Forcejeo!");
            new Forcejeo().run(this, objetivo);
        }
    }


    /**
     * Reemplaza o inserta un movimiento en la ranura indicada
     */
    public void learnMove(int indice, MovePoobKemon mov) {
        if (indice < 0 || indice >= 4) {
            return;
        }
        while (movements.size() <= indice) {
            movements.add(null);
        }
        movements.set(indice, mov);
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
     * Cura
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
     * Restaura completamente la salud y quita la condicion
     */
    public void cureComplete() {
        ppCurrent = ppMax;
        weakened = false;
    }

    /**
     * Verifica si no hay movimientos disponibles con PP
     */
    public boolean noMoveAvailable() {
        for (MovePoobKemon mov : movements) {
            if (mov != null && mov.getPowerPoints() > 0) {
                return false;
            }
        }
        return true;
    }

    public String getAlteredState() {
        return alteredState;
    }

    public void setAlteredState(String state) {
        this.alteredState = state;
    }

    public boolean isAsSleep() {
        return "DORMIDO".equals(alteredState);
    }

    public void lowerSpecialDefence(int levels) {
        specialDefence = Math.max(1, specialDefence - (levels * 10));
        System.out.println(name + " perdió defensa especial.");
    }

    public void lowerDefence(int levels) {
        defense = Math.max(1, defense - (levels * 10));
        System.out.println(name + " perdió defensa.");
    }

    public void modifyDefense(int change) {
        modifyDefense = Math.max(-6, Math.min(6, modifyDefense + change));
        System.out.println(name + " modificó su defensa (" + modifyDefense + ").");
    }


    public int getModifyPrecision() {
        return modifyPrecision;
    }

    public int getModifyEvasion() {
        return modifyEvasion;
    }

    public void modifyPrecision(int change) {
        modifyPrecision = Math.max(-6, Math.min(6, modifyPrecision + change));
    }

    public void modifyEvasion(int change) {
        modifyEvasion = Math.max(-6, Math.min(6, modifyEvasion + change));
    }

    public String getDescription() {
        return description;
    }

    public String getResourceName() {
        return resourceName;
    }
    public List<MovePoobKemon> getMovements() {
        return movements;
    }

    public abstract void cry();

    @Override
    public String toString() {
        return String.format(
                "%s (Nivel 100) PS: %d/%d - weakened: %b\nMovimientos: %s",
                name, ppCurrent, ppMax, weakened,
                movements.toString()
        );
    }
}
