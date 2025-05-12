package domain;

/**
 * Interfaz para los items que puede usar el entrenador en combate
 */
public interface Item {
    /**
     * Nombre del item a implementar en combate
     * @return String del nombre del item
     */
    String name();

    /**
     * El item que puede ser aplicado por el entrenador en uno de sus pokemones
     * @param t el entrenador
     * @return si pudo aplicarlo si no es false
     */
    boolean apply(Trainer t);
}
