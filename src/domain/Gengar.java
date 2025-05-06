package domain;

public class Gengar extends PoobKemon {
    public Gengar() {
        super(
                "Gengar", TypePoobKemon.FANTASMA,
                324,
                251,
                240,
                350,
                394,
                273,
                "Gengar pertenece a la primera generación de Pokémon, proveniente de Kanto, y es la última evolución de Gastly. "
                        + "Gengar está basado en el concepto del Doppelgänger y en la gente sombra. Es un Pokémon con extremidades pequeñas "
                        + "y personalidad siniestra y tenebrosa en estado salvaje. Por las noches sale a espantar y desorientar a los viajeros "
                        + "para robar sus almas.",
                "Gengar.gif"
        );
        addMove(new Hipnosis());
        addMove(new BolaSombra());
        addMove(new Rayo());
        addMove(new PunioSombra());
    }

    @Override
    public void cry() {
        System.out.println("¡Geeeeengaaaaar!");
    }
}

