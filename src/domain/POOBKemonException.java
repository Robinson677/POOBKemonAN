package domain;

public class POOBKemonException extends Exception {
    public static final String INVALID_POKEMON_KEY       = "Clave de PoobKemon inválida: %s";
    public static final String NO_ACTIVE_POKEMON         = "No hay Pokémon activo para atacar o defender";
    public static final String MAX_ITEMS_USED            = "Ya has usado 2 ítems. No puedes usar más en esta batalla.";
    public static final String NO_ITEMS_LEFT             = "No te quedan %s para usar.";
    public static final String REVIVE_ONLY_IF_KO         = "Solo puedes usar Revivir si tu PoobKemon está debilitado (PS = 0).";
    public static final String NO_CURRENT_POKEMON        = "El entrenador %s no tiene PoobKemon activo";
    public static final String INVALID_MOVE_INDEX        = "Índice de movimiento inválido: %d";
    public static final String NO_PP_LEFT                = "%s no tiene más PP para %s.";
    public static final String ITEM_USE_FAILED           = "Falló al usar %s.";

    public POOBKemonException(String message) {
        super(message);
    }
}