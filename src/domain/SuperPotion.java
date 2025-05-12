package domain;

/**
 * Un item que puede usar el entrenador para curar su pokemon 50 PS
 */
public class SuperPotion implements Item {

    /**
     * Nombre de la pocion super
     * @return String del nombre de la pocion
     */
    @Override public String name() {
        return "SUPERPOCION";
    }

    /**
     * Verifica si la superpocion puede ser aplicada por el entrenador en uno de sus pokemones
     * @param t el entrenador
     * @return si pudo aplicarlo si no es false
     */
    @Override
    public boolean apply(Trainer t) {
        if (t.getSuperPotions() <= 0 || t.getUsedItemCount() >= 2) return false;
        PoobKemon active = t.getPokemonactive();
        if (active == null || active.isWeakened()) return false;
        active.cure(50);
        t.decrementSuper();
        t.incrementUsedItemCount();
        return true;
    }
}
