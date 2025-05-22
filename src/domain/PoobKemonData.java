package domain;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Un PoobKemon parametrizado por datos: nombre, stats, movimientos…
 * Ya no necesitas subclases por cada especie.
 */
public class PoobKemonData extends PoobKemon implements Serializable {
    private static final long serialVersionUID = 1L;

    private final PokemonData data;
    private transient PoobKemonFight fight;

    /**
     * Constructor de PoobKemonData
     * @param data Datos de los pokemones
     * @param fight Logica del combate
     */
    public PoobKemonData(PokemonData data, PoobKemonFight fight) {
        super(
                data.name,
                data.type,
                data.hp,
                data.atk,
                data.def,
                data.vel,
                data.spatk,
                data.spdef,
                data.description,
                data.resourceName
        );
        this.data = data;
        for (String moveKey : data.moveKeys) {
            addMove(MoveFactory.create(moveKey, fight));
        }
    }

    @Override
    public void cry() {
        System.out.println(data.cryText);
    }

    /**
     * Reconstruimos el combate
     * @param in
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.fight = null;
        getMovements().clear();
    }

    /**
     * Para vover a poblar los movimientos
     */
     @Override
     public void setFightReference(PoobKemonFight fight) {
        this.fight = fight;
        getMovements().clear();
        for (String moveKey : data.moveKeys) {
            addMove(MoveFactory.create(moveKey, fight));
        }
    }

}
