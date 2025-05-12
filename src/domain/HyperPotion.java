package domain;

/**
 * Un item que puede usar el entrenador para curar su pokemon 200 PS
 */
public class HyperPotion implements Item {

    /**
     * Nombre de la pocion hiper
     * @return String del nombre de la pocion
     */
    @Override public String name() {
        return "HIPERPOCION";
    }

    /**
     * Verifica si la hiperpocion puede ser aplicada por el entrenador en uno de sus pokemones
     * @param t el entrenador
     * @return si pudo aplicarlo si no es false
     */
    @Override
    public boolean apply(Trainer t) {
        if (t.getHyperPotions() <= 0 || t.getUsedItemCount() >= 2) return false;
        PoobKemon active = t.getPokemonactive();
        if (active == null || active.getPpCurrent() == 0) return false;
        active.cure(200);
        t.decrementHyper();
        t.incrementUsedItemCount();
        return true;
    }
}
