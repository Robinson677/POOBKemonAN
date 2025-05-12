package domain;


/**
 * Un PoobKemon parametrizado por datos: nombre, stats, movimientos…
 * Ya no necesitas subclases por cada especie.
 */
public class PoobKemonData extends PoobKemon {
    private final PokemonData data;

    /**
     * @param data Objeto que contiene todos los datos de este Pokémon
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
}
