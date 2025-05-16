package domain;

import java.util.List;

/**
 * Interfaz encargada de dar contraro a los entrenadores IA del combate
 */
public interface BattleStrategy {
    /**
     * Selecciona un equipo de 6 pokemones aleatorios o estrategicos
     * dados los nombres disponibles
     */
    List<String> selectTeam(List<String> availableKeys);

    /**
     * Dado el estado actual del combate, elige el indice de movimiento
     * a ejecutar para el PoobKemon activo.
     */
    int chooseMove(PoobKemonFight fight);

    /**
     * Devuelve el indice del pokemon a colocar activo
     */
    int choosePokemonSwitch(PoobKemonFight fight);
}
