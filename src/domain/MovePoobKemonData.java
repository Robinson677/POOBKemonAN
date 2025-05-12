package domain;

import java.util.function.BiConsumer;

/**
 * Encargada de los datos que deben tener los movimientos
 */
public class MovePoobKemonData extends MovePoobKemon {
    private final MoveData data;
    private final PoobKemonFight fight;
    private final BiConsumer<PoobKemon,PoobKemon> secondaryEffect;

    public MovePoobKemonData(MoveData data, PoobKemonFight fight) {
        super(
                data.name,
                data.type,
                data.category,
                data.potency,
                data.precision,
                data.ppMax,
                data.secondaryEffect,
                data.priority
        );
        this.data = data;
        this.fight = fight;
        this.secondaryEffect = data.effect;
    }

    @Override
    public void run(PoobKemon attacker, PoobKemon objective) {
        MovePoobKemon.setCurrentFight(fight);
        fight.log(attacker.getName() + " usó " + data.name + ".");

        if (data.useSuperRun) {
            super.run(attacker, objective);
        }
        data.effect.accept(attacker, objective);
        MovePoobKemon.clearCurrentFight();
    }

    public void applySecondaryEffect(PoobKemon attacker, PoobKemon defender) {
        if (secondaryEffect != null) {
            secondaryEffect.accept(attacker, defender);
        }
    }
}
