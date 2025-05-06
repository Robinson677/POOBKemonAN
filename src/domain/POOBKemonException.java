package domain;

public class POOBKemonException extends Exception {
    public static final String INVALID_POSITION = "Posición de entrenador no válida.";
    public static final String NULL_TRAINER = "El entrenador no puede ser nulo.";
    public static final String NULL_POKEMON = "El PoobKemon no puede ser nulo.";
    public static final String MAX_POKEMON_REACHED = "El entrenador ya tiene el máximo de PoobKemones permitidos.";
    public static final String INVALID_MOVE_INDEX = "Índice de movimiento fuera de rango.";
    public static final String INVALID_POKEMON_KEY = "Clave de PoobKemon no válida.";

    public POOBKemonException(String message) {
        super(message);
    }
}