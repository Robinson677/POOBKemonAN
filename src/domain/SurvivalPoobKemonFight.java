package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modo Supervivencia: Cada team de 6 pokemones para cada jugador sera aleatorio y no se usaran items
 */
public class SurvivalPoobKemonFight extends PoobKemonFight {

    /**
     * Constructor de SurvivalPoobKemonFight
     * @param t1 representa el primer entrenador
     * @param t2 representa el segundo entrenador
     * @param firstPicker El primer jugador en iniciar
     * @throws POOBKemonException lanzamos la excepcion en caso de errores
     */
    public SurvivalPoobKemonFight(Trainer t1, Trainer t2, int firstPicker) throws POOBKemonException {
        super(t1, t2, firstPicker);
        initSurvivalTeams();
        ok();
    }

    /**
     * 6 pokemone aleatiorios para cada entrenador, obtiene la lista de keys disponibles y baraja
     */
    private void initSurvivalTeams() throws POOBKemonException {

        List<String> keys = getPokemonKeys();
        Collections.shuffle(keys);

        List<String> selectedKeys = keys.subList(0, 12);
        Collections.shuffle(selectedKeys);

        List<PoobKemon> team1 = new ArrayList<>();
        List<PoobKemon> team2 = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            team1.add(createPokemon(selectedKeys.get(i)));
        }

        for (int i = 6; i < 12; i++) {
            team2.add(createPokemon(selectedKeys.get(i)));
        }

        //Se limpian los equipos por si ya tienen contenido
        getTrainer1().getTeam().clear();
        getTrainer2().getTeam().clear();

        // Asignamos los equipos
        getTrainer1().getTeam().addAll(team1);
        getTrainer2().getTeam().addAll(team2);
        ok();
    }

    /**
     * En supervivencia no hay items disponibles
     */
    @Override
    public int getItemCount(String item) {
        ok();
        return 0;
    }

    /**
     * No se puede usar items en supervivencia
     */
    @Override
    public boolean useItem(String item) {
        ok();
        return false;
    }
}