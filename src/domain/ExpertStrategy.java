package domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La estrategia experta elige los pokemones con las mejores estadisticas
 * y los movimientos mas potentes
 */
public class ExpertStrategy implements BattleStrategy {

    /**
     * Ordena los pokemones en orden descendente sumando sus estadisticas
     * y toma los primeros 6 mas altos
     */
    @Override
    public List<String> selectTeam(List<String> availableKeys) {
        return availableKeys.stream()
                .sorted(Comparator.<String>comparingInt(key -> {
                    try {
                        PoobKemon p = new PoobKemonData(PokemonData.valueOf(key), null);
                        return p.getMove()
                                + p.getDefense()
                                + p.getVelocity()
                                + p.getSpecialAttack()
                                + p.getSpecialDefence();
                    } catch (Exception e) {
                        return 0;
                    }
                }).reversed())
                .limit(6)
                .collect(Collectors.toList());
    }

    /**
     * Escoge el movimiento con mayor potencia × efectividad
     */
    @Override
    public int chooseMove(PoobKemonFight fight) {
        PoobKemon attacker = fight.getCurrentTrainer().getPokemonactive();
        int defenderIdx = (fight.getCurrentTrainer() == fight.getTrainer1()) ? 1 : 0;
        PoobKemon defender = fight.getCurrentPokemon(defenderIdx);

        List<MovePoobKemon> moves = attacker.getMovements();
        int bestIdx = 0;
        double bestScore = -1.0;

        for (int i = 0; i < moves.size(); i++) {
            MovePoobKemon m = moves.get(i);
            if (m.getCategory() == MovePoobKemon.Category.FISICO
                    || m.getCategory() == MovePoobKemon.Category.ESPECIAL) {

                double score = m.getPotency()
                        * TypeEffectivity.get(m.getMoveType(), defender.getTypePoobKemon());
                if (score > bestScore) {
                    bestScore = score;
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }

    /**
     * Cambia de Pokémon si el activo está KO; de lo contrario,
     * elige el no debilitado con mayor suma de stats.
     */
    @Override
    public int choosePokemonSwitch(PoobKemonFight fight) {
        Trainer me = fight.getCurrentTrainer();
        List<PoobKemon> team = me.getTeam();
        int activeIdx = me.getActiveIndex();
        PoobKemon activeP = team.get(activeIdx);

        if (activeP.getPpCurrent() == 0 || activeP.isWeakened()) {
            for (int i = 0; i < team.size(); i++) {
                PoobKemon p = team.get(i);
                if (p.getPpCurrent() > 0 && !p.isWeakened()) {
                    return i;
                }
            }
            return activeIdx;
        }

        return team.stream()
                .filter(p -> p.getPpCurrent() > 0 && !p.isWeakened())
                .map(p -> new Object[]{
                        team.indexOf(p),
                        p.getMove()
                                + p.getDefense()
                                + p.getVelocity()
                                + p.getSpecialAttack()
                                + p.getSpecialDefence()
                })
                .max(Comparator.comparingInt(o -> (Integer) o[1]))
                .map(o -> (Integer) o[0])
                .orElse(activeIdx);
    }
}
