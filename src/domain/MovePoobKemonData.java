package domain;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Encargada de los datos que deben tener los movimientos
 */
public class MovePoobKemonData extends MovePoobKemon implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MoveData data;
    private transient PoobKemonFight fight;
    private transient BiConsumer<PoobKemon,PoobKemon> secondaryEffect;

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

    /**
     * Reconstruye
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.secondaryEffect = data.effect;
        this.fight = null;
    }

    /**
     * Reinyectamos el combate
     * @param fight la logica del combate
     */
    @Override
    public void setFightReference(PoobKemonFight fight) {
        this.fight = fight;
    }
}
