package domain;

import java.util.List;

/**
 * Datos de las estadisticas de cada Poobkemon
 */
public enum PokemonData {
    RAICHU(
            "Raichu",
            TypePoobKemon.ELECTRICO,
            324, 306, 229, 350, 306, 284,
            "Raichu pertenece a la primera generación de Pokémon, proveniente de Kanto, siendo la evolución de Pikachu. "
                    + "La habilidad de Raichu de contener corrientes eléctricas es impresionante; es común que sus ataques eléctricos tengan "
                    + "10.000 voltios de energía, pero se han registrado casos de hasta 100.000. Incluso cuando no está luchando, el cuerpo "
                    + "de Raichu emite una débil carga eléctrica que le hace luminoso en la oscuridad, y le puede dar una buena descarga a "
                    + "cualquiera que ose asustarle o tocarle sin previo aviso.",
            "Raichu.gif",
            List.of("RAYO", "TRUENO", "COLA_FERREA", "PUNIO_TRUENO"),
            "¡Raaaai-chuuuuu!"
    ),

    GENGAR(
            "Gengar",
            TypePoobKemon.FANTASMA,
            324, 251, 240, 350, 394, 273,
            "Gengar pertenece a la primera generación de Pokémon, proveniente de Kanto, y es la última evolución de Gastly. "
                    + "Gengar está basado en el concepto del Doppelgänger y en la gente sombra. Es un Pokémon con extremidades pequeñas "
                    + "y personalidad siniestra y tenebrosa en estado salvaje. Por las noches sale a espantar y desorientar a los viajeros "
                    + "para robar sus almas.",
            "Gengar.gif",
            List.of("VENDETTA", "BOLA_SOMBRA", "RAYO", "PUNIO_SOMBRA"),
            "¡Geeeeengaaaaar!"
    ),
    GARDEVOIR(
            "Gardevoir",
            TypePoobKemon.PSIQUICO,
            340,
            251,
            251,
            284,
            383,
            361,

            "Gardevoir pertenece a la tercera generación de Pokémon, proveniente de Hoenn, y es la última evolución de Ralts. "
                    + "Gardevoir tiene una habilidad que ningún otro Pokémon posee: captar los sentimientos de su entrenador para ayudarle en caso de peligro, "
                    + "independientemente de si su vida está en riesgo. Además, es capaz prever situaciones futuras. Usando esta capacidad, Gardevoir anticipa "
                    + "cuándo su entrenador estará en peligro y utiliza su energía psicoquinética para protegerlo.",
            "Gardevoir.gif",
            List.of("PSIQUICO", "BOLA_SOMBRA", "PREMONICION", "RAYO_AURORA"),
            "¡Gar-de-voir!"
    ),

    SLAKING(
            "Slaking",
            TypePoobKemon.NORMAL,
            504,
            460,
            328,
            328,
            317,
            251,
            "Slaking es un Pokémon de tipo Normal originario de Hoenn, introducido en la tercera generación basado en un perezoso de gran tamaño con apariencia de gorila. Es conocido como el Pokémon más vago, ya que puede pasar todo el día tumbado en el mismo lugar. "
                    + "Consume toda la hierba a su alrededor mientras está acostado y solo se mueve cuando se queda sin comida, "
                    + "En temporada de frutas, se reúne en grupos debajo de los árboles para atrapar la fruta caída. A pesar de su vaguería, acumula mucha energía y puede liberarla toda de una sola vez.",
            "Slaking.gif",
            List.of("PUNIO_CERTERO", "RAYO", "RAYO_HIELO", "RELAJO"),
            "¡¡Slakiiiing!!"
    ),

    NIDORINO(
            "Nidorino",
            TypePoobKemon.VENENO,
            326,
            267,
            234,
            251,
            229,
            229,
            "Nidorino es un Pokémon de tipo Veneno originario de Kanto, introducido en la primera generación. " +
                    "Es la evolución de Nidoran♂. Aunque mantiene el color y las púas de su espalda, adquiere una apariencia " +
                    "más robusta. Sus incisivos se convierten en colmillos y su cuerno se desarrolla aún más. Su cuerno, de dureza " +
                    "superior al diamante, segrega un veneno potente. Usa su cuerno para partir rocas en busca de piedras lunares " +
                    "para evolucionar.",
            "Nidorino.gif",
            List.of("PUNIO_CERTERO", "GOLPE_CUERPO", "DOBLE_PATADA", "POLUCION"),
            "¡Ni-do-ri-no!"
    ),

    DRAGONITE(
            "Dragonite",
            TypePoobKemon.DRAGON,
            386,
            403,
            317,
            284,
            328,
            328,
            "Dragonite es un Pokémon de tipo Dragón y Volador originario de Kanto. " +
                    "Es la última evolución de Dratini y se le conoce como el 'Avatar del Mar'. " +
                    "Es un Pokémon de buen corazón que disfruta volando por el océano, rescatando náufragos " +
                    "y guiando barcos perdidos a través de tormentas hasta la costa. Con su constitución fuerte, " +
                    "desafía las tempestades más feroces sin sufrir daño.",
            "Dragonite.gif",
            List.of("HIPERRAYO", "GARRA_DRAGON", "LANZALLAMAS", "PUNIO_CERTERO"),
            "¡Dra-gon-ite!"
    ),

    CHARIZARD(
            "Charizard",
            TypePoobKemon.FUEGO,
            360,
            293,
            280,
            328,
            348,
            295,
            "Charizard pertenece a la primera generación de Pokémon, proveniente de Kanto, y es la última evolución de Charmander. " +
                    "Es un dragón erguido sobre sus dos patas traseras que posee unas poderosas alas y un abrasador aliento de fuego. " +
                    "Tiene el cuello largo y una poderosa cola terminada en una llama que arde con más fuerza si ha vivido duros combates; " +
                    "sin embargo, si esta se apaga el Pokémon puede llegar a morir.",
            "Charizard.gif",
            List.of("LANZALLAMAS", "LLAMARADA", "CUCHILLADA", "ONDA_IGNEA"),
            "¡Chari-zaaard!"
    ),

    BLASTOISE(
            "Blastoise",
            TypePoobKemon.AGUA,
            362,
            291,
            328,
            280,
            295,
            339,
            "Blastoise pertenece a la primera generación de Pokémon, proveniente de Kanto, y es la última evolución de Squirtle. " +
                    "Es una enorme tortuga bípeda equipada con dos poderosos cañones situados en su espalda capaces de disparar potentes " +
                    "chorros de agua, con la fuerza suficiente para quebrar muros de hormigón o perforar planchas de acero. " +
                    "Para evitar el retroceso, se planta firmemente sus patas traseras en el suelo y sube deliberadamente de peso.",
            "Blastoise.gif",
            List.of("HIDROBOMBA", "GOLPE_CUERPO", "CABEZAZO", "PISTOLA_AGUA"),
            "¡Bla-stoi-se!"
    ),

    VENUSAUR(
            "Venusaur",
            TypePoobKemon.PLANTA,
            364,
            289,
            291,
            284,
            328,
            328,

            "Venusaur pertenece a la primera generación de Pokémon, proveniente de Kanto, " +
                    "y es la última evolución de Bulbasaur. Es una rana que lleva sobre su lomo " +
                    "el botón de una flor que se abre completamente, dejando ver una enorme flor " +
                    "rosada que se nutre de la luz solar por fotosíntesis. A través de ella, realiza " +
                    "uno de sus ataques más potentes: rayo solar.",
            "Venusaur.gif",
            List.of("DOBLE_FILO", "BOMBA_LODO", "TERREMOTO", "RESIDUOS"),
            "¡Ve-nu-saur!"
    ),

    TOGETIC(
            "Togetic",
            TypePoobKemon.HADA,
            314,
            196,
            295,
            196,
            284,
            339,
            "Togetic pertenece a la segunda generación de Pokémon, proveniente de Johto, siendo la evolución del Pokémon huevo Togepi. " +
                    "Togetic trae la buena suerte y, si detecta a una persona de buen corazón, se aparece frente a ella y la inunda de felicidad " +
                    "esparciendo un plumón luminoso, llamado 'polvillo de la alegría'. Sin embargo, si no está con gente amable, se entristece.",
            "Togetic.gif",
            List.of("VUELO", "HOJA_MAGICA", "PODER_PASADO", "HIPERRAYO"),
            "¡To-ge-tic!"
    ),
    TYRANITAR(
            "Tyranitar",
            TypePoobKemon.ROCA,
            404,
            403,
            350,
            243,
            317,
            328,
            "Tyranitar pertenece a la segunda generación de Pokémon, originaria de Johto, y es la última evolución de Larvitar. " +
                    "Su cuerpo es muy resistente, lo que lo hace casi inmune a los ataques. Le encanta desafiar a sus oponentes y tiene una " +
                    "naturaleza insolente y egoísta. En sus garras tiene el poder suficiente para hacer temblar la tierra y las montañas, " +
                    "cambiando con facilidad el paisaje que le rodea.",
            "Tyranitar.gif",
            List.of("TRITURAR", "TERREMOTO", "HIPERRAYO", "SANA"),
            "¡Ty-ra-ni-tar!"
    ),

    SNORLAX(
            "Snorlax",
            TypePoobKemon.NORMAL,
            524,
            350,
            251,
            174,
            251,
            350,
            "Snorlax pertenece a la primera generación de Pokémon, proveniente de Kanto. Snorlax come muchos kilos de comida al día y, " +
                    "después de comer, lo único que hace es echarse a dormir hasta volver a despertarse por el hambre. Si lo despiertan, se enfadará " +
                    "y atacará ferozmente, aunque después de un rato se volverá a dormir. Tiene la habilidad de hacer que los árboles crezcan más " +
                    "rápidamente después de destruirlos.",
            "Snorlax.gif",
            List.of("TRITURAR", "RELAJO", "MEGAPUNO", "FUERZA"),
            "¡Snooor-lax!"
    ),

    ;

    public final String name;
    public final TypePoobKemon type;
    public final int hp, atk, def, vel, spatk, spdef;
    public final String description, resourceName, cryText;
    public final List<String> moveKeys;

    PokemonData(String name,
                TypePoobKemon type,
                int hp, int atk, int def, int vel, int spatk, int spdef,
                String description,
                String resourceName,
                List<String> moveKeys,
                String cryText)
    {
        this.name         = name;
        this.type         = type;
        this.hp           = hp;
        this.atk          = atk;
        this.def          = def;
        this.vel          = vel;
        this.spatk        = spatk;
        this.spdef        = spdef;
        this.description  = description;
        this.resourceName = resourceName;
        this.moveKeys     = moveKeys;
        this.cryText      = cryText;
    }

    /**
     *  Para inicializar tu registry en PoobKemonFight
     */
    public static List<PokemonData> loadAll() {
        return List.of(values());
    }
}

