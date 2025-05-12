package domain;

/**
 * Un item que puede usar el entrenador para revivir su pokemon
 */
public class Revive implements Item {

    /**
     * Nombre del revivir
     * @return String del revivir
     */
    @Override public String name() {
        return "REVIVIR";
    }

    /**
     * Verifica si el revivir puede ser aplicado por el entrenador en uno de sus pokemones
     * @param t el entrenador
     * @return si pudo aplicarlo si no es false
     */
    @Override
    public boolean apply(Trainer t) {
        if (t.getRevives() <= 0 || t.getUsedItemCount() >= 2) return false;
        PoobKemon active = t.getPokemonactive();
        if (active == null || !active.isWeakened()) return false;
        active.revive();
        t.decrementRevive();
        t.incrementUsedItemCount();
        return true;
    }
}

