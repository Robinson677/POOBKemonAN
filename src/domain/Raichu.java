package domain;

public class Raichu extends PoobKemon {
    public Raichu() {
        super(
                "Raichu", TypePoobKemon.ELECTRICO,
                324,
                306,
                229,
                350,
                306,
                284,
                "Raichu pertenece a la primera generación de Pokémon, proveniente de Kanto, siendo la evolución de Pikachu. "
                        + "La habilidad de Raichu de contener corrientes eléctricas es impresionante; es común que sus ataques eléctricos tengan "
                        + "10.000 voltios de energía, pero se han registrado casos de hasta 100.000. Incluso cuando no está luchando, el cuerpo "
                        + "de Raichu emite una débil carga eléctrica que le hace luminoso en la oscuridad, y le puede dar una buena descarga a "
                        + "cualquiera que ose asustarle o tocarle sin previo aviso.",
                "Raichu.gif"
        );

        addMove(new Rayo());
        addMove(new Trueno());
        addMove(new ColaFerrea());
        addMove(new PunioTrueno());
    }

    @Override
    public void cry() {
        System.out.println("¡Raaaai-chuuuuu!");
    }
}
