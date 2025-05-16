package domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * La estrategia de ataque prioriza movimientos de ataque o que potencien el propio ataque.
 */
public class AttackingStrategy implements BattleStrategy {
    private final Random rnd = new Random();

    /**
     * Selecciona un team aleatorio
     * @param availableKeys los nombres de los pokemones
     * @return la lista del team
     */
    @Override
    public List<String> selectTeam(List<String> availableKeys) {
        List<String> sorted = availableKeys.stream()
                .sorted(Comparator.<String>comparingInt(key -> {
                    try {
                        PoobKemon p = new PoobKemonData(PokemonData.valueOf(key), null);
                        return p.getMove();
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
     * Elige movientos enfocados al ataque y al poder
     * @param fight representa la logica del combate
     * @return int el indice del movimiento que se puede usar
     */
    @Override
    public int chooseMove(PoobKemonFight fight) {
        PoobKemon attacker = fight.getCurrentTrainer().getPokemonactive();
        int rivalIdx = (fight.getCurrentTrainer() == fight.getTrainer1()) ? 1 : 0;
        PoobKemon defender = fight.getCurrentPokemon(rivalIdx);

        List<MovePoobKemon> moves = attacker.getMovements();
        int bestIdx = 0;
        double bestScore = -1.0;

        for (int i = 0; i < moves.size(); i++) {
            MovePoobKemon m = moves.get(i);
            MovePoobKemon.Category cat = m.getCategory();
            if (cat == MovePoobKemon.Category.FISICO || cat == MovePoobKemon.Category.ESPECIAL) {
                int potency = m.getPotency();
                double eff = TypeEffectivity.get(m.getMoveType(), defender.getTypePoobKemon());
                double score = potency * eff;
                if (score > bestScore) {
                    bestScore = score;
                    bestIdx = i;
                }
            }
        }


        if (bestScore < 0 && !moves.isEmpty()) {
            return rnd.nextInt(moves.size());
        }
        return bestIdx;
    }

    /**
     * el estratega de ataque no cambia a no ser que sea un KO al pokemon
     * @param fight representa la logica del combate
     * @return int el indice del pokemon que se puede usar
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
