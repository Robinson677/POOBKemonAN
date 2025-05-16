package domain;

import java.util.*;

/**
 * La estrategia de cambio siempre que sea conveniente, cambia al pokemon
 * y su tipo sea distinto al del rival activo para tomar ventaja
 */
public class ChangingStrategy implements BattleStrategy {
    private final Random rnd = new Random();

    /**
     * Toma 6 pokemones aleatorios
     * @param availableKeys
     * @return lista del team de pokemones
     */
    @Override
    public List<String> selectTeam(List<String> availableKeys) {
        Collections.shuffle(availableKeys, rnd);
        return new ArrayList<>(availableKeys.subList(0, 6));
    }

    /**
     * En el combate usa movimientos al azar
     * @param fight representa la logica del combate
     * @return int el indice del movimiento que se puede usar
     */
    @Override
    public int chooseMove(PoobKemonFight fight) {
        int moveCount = fight.getMoveCount();
        return rnd.nextInt(Math.max(moveCount, 1));
    }

    /**
     * Busca cambiar el pokemon por uno diferente al tipo del rival
     * @param fight fight representa la logica del combate
     * @return int el indice pokemon que se puede usar
     */
    @Override
    public int choosePokemonSwitch(PoobKemonFight fight) {
        Trainer ia = fight.getCurrentTrainer();
        List<PoobKemon> team = ia.getTeam();
        int activeIdx = ia.getActiveIndex();
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

        int opponentIdx = (fight.getCurrentTrainer() == fight.getTrainer1()) ? 1 : 0;
        TypePoobKemon rivalType = TypePoobKemon.valueOf(
                fight.getTypeName(fight.getPokemonName(opponentIdx)).toUpperCase()
        );

        double bestEff = -1.0;
        int bestIdx = activeIdx;
        for (int i = 0; i < team.size(); i++) {
            PoobKemon p = team.get(i);
            if (p.getPpCurrent() > 0 && !p.isWeakened()) {
                double eff = TypeEffectivity.get(p.getTypePoobKemon(), rivalType);
                if (eff > bestEff) {
                    bestEff = eff;
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }
}
