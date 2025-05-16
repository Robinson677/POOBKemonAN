package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;


/**
 * Los entrendadores luchan entre ellos usando sus pokemones y items
 */
public class Trainer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String colorTeam;
    private List<PoobKemon> team;
    private int activeIndex = 0;
    private int usedItemCount;
    private transient Map<String, Item> items = new HashMap<>();
    private int potions    = 2;
    private int superPotions = 2;
    private int hyperPotions = 2;
    private int revive       = 1;


    /**
     * Constructor de Trainer
     * @param name de entrenador
     * @param colorTeam al que pertenece azul o rojo
     */
    public Trainer(String name, String colorTeam) {
        this.name = name;
        this.colorTeam = colorTeam;
        this.team = new ArrayList<>();
        this.usedItemCount = 0;
        items.put("POCION", new Potion());
        items.put("SUPERPOCION", new SuperPotion());
        items.put("HIPERPOCION", new HyperPotion());
        items.put("REVIVIR", new Revive());

    }



    /**
     * Obtiene el nombre del entrenador
     * @return String del nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el color del team del entrenador
     * @return String del team
     */
    public String getColorTeam() {
        return colorTeam;
    }

    /**
     * Obtiene la lista del team de pokemones del entrenador
     * @return List del team de pokemones
     */
    public List<PoobKemon> getTeam() {
        return team;
    }

    /**
     * Obtiene el pokemon que esta activo en combate
     * @return PoobKemon activo
     */
    public PoobKemon getPokemonactive() {
        return team.isEmpty() ? null : team.get(activeIndex);
    }

    /**
     * Añade un pokemon a su team
     * @param p PoobKemon activo
     */
    public void addPoobKemon(PoobKemon p) {
        if (team.size() < 6) {
            team.add(p);
        }
    }

    /**
     * Cambia el PoobKemon activo
     * @param newIndex
     * @return
     */
    public boolean changePoobKemon(int newIndex) {
        if (newIndex >= 0 && newIndex < team.size()
                && !team.get(newIndex).isWeakened()) {
            this.activeIndex = newIndex;
            return true;
        }
        return false;
    }

    /**
     * Indice del entrenador activo
     * @return int del indice
     */
    public int getActiveIndex() {
        return activeIndex;
    }

    /**
     * Verifica si el entrenador es derrotado
     * @return true si fue derrotado de lo contrario false
     */
    public boolean isDefeated() {
        for (PoobKemon p : team) {
            if (!p.isWeakened()) return false;
        }
        return true;
    }


    /**
     * Obtiene el contador del item
     * @return int del item
     */
    public int getUsedItemCount() {
        return usedItemCount;
    }

    /**
     * Resetea el contador de los items
     */
    public void resetUsedItemCount() {
        this.usedItemCount = 0;
    }


    /**
     * Usa un item por nombre y verifica si tuvo efecto
     * @return boolean true si se uso el item false si no
     */
    public boolean useItem(String itemName) {
        Item it = items.get(itemName.toUpperCase());
        if (it == null) return false;
        boolean applied = it.apply(this);
        return applied;
    }


    /**
     * Incrementa el contador del Item
     */
    public void incrementUsedItemCount() {
        usedItemCount++;
    }

    /**
     * Decrementan el stock de la pocion normal
     */
    void decrementPotion() {
        this.potions--;
    }

    /**
     * Decrementan el stock de la superpocion
     */
    void decrementSuper() {
        this.superPotions--;
    }

    /**
     * Decrementan el stock de la hiperpocion
     */
    void decrementHyper() {
        this.hyperPotions--;
    }

    /**
     * Decrementan el unico revivir
     */
    void decrementRevive() {
        this.revive--;
    }

    /**
     * Obtiene la pocion normal
     * @return int numero de pociones
     */
    public int getPotions() {
        return potions;
    }

    /**
     * Obtiene la superpocion
     * @return int de superpociones
     */
    public int getSuperPotions() {
        return superPotions;
    }

    /**
     * Obtiene la hiperpocion
     * @return int numero de hiperpociones
     */
    public int getHyperPotions() {
        return hyperPotions;
    }

    /**
     * Obtiene el revivir
     * @return int del revivit
     */
    public int getRevives() {
        return revive;
    }

    /**
     * Selecciona un equipo aleatoriamente
     */
    public void autoSelectTeam(PoobKemonFight fight) throws POOBKemonException {
    }

    /**
     * Identifica si este entrenador es IA
     */
    public boolean isAI() {
        return false;
    }

    /**
     * Reconstruimos el map
     * @param in
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        items = new HashMap<>();
        items.put("POCION", new Potion());
        items.put("SUPERPOCION", new SuperPotion());
        items.put("HIPERPOCION", new HyperPotion());
        items.put("REVIVIR", new Revive());
    }

}