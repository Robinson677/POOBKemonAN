package domain;

/**
 * Un item que puede usar el entrenador para curar su pokemon 20 PS
 */
public class Potion implements Item {

    /**
     * Nombre de la pocion
     * @return String del nombre de la pocion
     */
    @Override public String name() {
        return "POCION";
    }

    /**
     * Verifica si la pocion puede ser aplicada por el entrenador en uno de sus pokemones
     * @param t el entrenador
     * @return si pudo aplicarlo si no es false
     */
    @Override
    public boolean apply(Trainer t) {
        if (t.getPotions() <= 0 || t.getUsedItemCount() >= 2) return false;
        PoobKemon active = t.getPokemonactive();
        active.cure(20);
        t.decrementPotion();
        t.incrementUsedItemCount();
        return true;
    }
}
