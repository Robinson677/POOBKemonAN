package domain;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * La estrategia defensiva prioriza movimientos defensivos y de debilitamiento de ataque rival
 */
public class DefensiveStrategy implements BattleStrategy {
    private boolean lastWasState = false;
    private final Random rng = new Random();


    /**
     * Toma 6 pokemones con las mayores defensas
     * @param availableKeys
     * @return lista del team de pokemones
     */
    @Override
    public List<String> selectTeam(List<String> availableKeys) {
        List<String> sorted = availableKeys.stream()
                .sorted(Comparator.<String>comparingInt(key -> {
                    try {
                        PoobKemon p = new PoobKemonData(PokemonData.valueOf(key), null);
                        return p.getDefense();
                    } catch (Exception e) {
                        return 0;
                    }
                }).reversed())
                .collect(Collectors.toList());
        return new ArrayList<>(
                sorted.subList(0, Math.min(6, sorted.size()))
        );
    }

    /**
     * Escoge un movimiento que reduzca el ataque o aumente la defensa
     * @param fight representa la logica del combate
     * @return int del indice del movimiento que se puede usar
     */
    @Override
    public int chooseMove(PoobKemonFight fight) {
        PoobKemon attacker = fight.getCurrentTrainer().getPokemonactive();
        List<MovePoobKemon> moves = attacker.getMovements();
        int count = moves.size();
        int randomIdx = rng.nextInt(count);

        if (lastWasState) {
            lastWasState = false;
            return randomIdx;
        } else {
            for (int i = 0; i < count; i++) {
                if (moves.get(i).getCategory() == MovePoobKemon.Category.ESTADO) {
                    lastWasState = true;
                    return i;
                }
            }
            lastWasState = false;
            return randomIdx;
        }
    }

    /**
     * Estratega defensivo no cambia a no ser que sea un KO al pokemon
     * @param fight representa la logica del combate
     * @return int del indice del pokemon que se puede usar
     */
    @Override
    public int choosePokemonSwitch(PoobKemonFight fight) {
        Trainer ia = fight.getCurrentTrainer();
        int activeI = ia.getActiveIndex();
        PoobKemon activeP = ia.getTeam().get(activeI);

        if (activeP.getPpCurrent() == 0 || activeP.isWeakened()) {
            List<PoobKemon> team = ia.getTeam();
            for (int i = 0; i < team.size(); i++) {
                PoobKemon p = team.get(i);
                if (p.getPpCurrent() > 0 && !p.isWeakened()) {
                    return i;
                }
            }
            return activeI;
        }
        return activeI;
    }
}
