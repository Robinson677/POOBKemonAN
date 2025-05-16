package domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Un Trainer controlado por IA, que delega la selección de equipo,
 * movimientos y cambios en una BattleStrategy.
 */
public class AITrainer extends Trainer {
    private static final long serialVersionUID = 1L;

    private transient BattleStrategy strategy;
    private String strategyKey;

    /**
     * @param name nombre del entrenador IA
     * @param teamColor color del equipo ("Azul" o "Rojo")
     * @param strategy  la estrategia de IA a usar
     */
    public AITrainer(String name, String teamColor, BattleStrategy strategy, String key) {
        super(name, teamColor);
        this.strategy = strategy;
        this.strategyKey   = key;
    }

    /**
     * Obtiene la IA dependiendo en que estrategia se basa el Trainer
     * @return
     */
    public BattleStrategy getStrategy() {
        return strategy;
    }

    /**
     * Pide a la estrategia que seleccione 6 keys y arma el equipo.
     */
    public void autoSelectTeam(PoobKemonFight fight) throws POOBKemonException {
        List<String> available = fight.getPokemonKeys();
        List<String> chosenKeys = strategy.selectTeam(available);
        for (String key : chosenKeys) {
            if (!fight.selectPokemon(key)) {
                throw new POOBKemonException(
                        String.format(POOBKemonException.INVALID_POKEMON_KEY, key)
                );
            }
        }
    }

    /**
     * Pide a la estrategia que elija movimiento.
     */
    public int autoChooseMove(PoobKemonFight fight) {
        return strategy.chooseMove(fight);
    }

    /**
     * Pide a la estrategia que decida un cambio de Pokémon.
     */
    public int autoChooseSwitch(PoobKemonFight fight) {
        return strategy.choosePokemonSwitch(fight);
    }


    /**
     * Fábrica interna de estrategias IA.
     */
    private static BattleStrategy buildStrategy(String key) {
        return switch (key) {
            case "DEFENSIVE" -> new DefensiveStrategy();
            case "ATTACKING" -> new AttackingStrategy();
            case "CHANGING" -> new ChangingStrategy();
            case "EXPERT" -> new ExpertStrategy();
            default -> new DefensiveStrategy();
        };
    }


    /**
     * Reconstruimos las IA
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        switch (strategyKey) {
            case "DEFENSIVE":
                this.strategy = new DefensiveStrategy();
                break;
            case "ATTACKING":
                this.strategy = new AttackingStrategy();
                break;
            case "CHANGING":
                this.strategy = new ChangingStrategy();
                break;
            case "EXPERT":
                this.strategy = new ExpertStrategy();
                break;
            default:
                this.strategy = new DefensiveStrategy();
        }
    }


    @Override
    public boolean isAI() {
        return true;
    }
}
