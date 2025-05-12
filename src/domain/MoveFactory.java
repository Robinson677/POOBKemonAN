package domain;

public class MoveFactory {
    /**
     * Instacia a la llave del pokemon
     */
    public static MovePoobKemon create(String key, PoobKemonFight fight) {
        MoveData md = MoveData.valueOf(key.toUpperCase());
        return new MovePoobKemonData(md, fight);
    }
}