package domain;

import java.util.List;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

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
            List.of("DOBLE_FILO", "BOMBA_LODO", "RAYO_SOLAR", "RESIDUOS"),
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
            List.of("TRITURAR", "RELAJO", "MEGAPUNIO", "FUERZA"),
            "¡Snooor-lax!"
    ),

    METAGROSS(
            "Metagross",
            TypePoobKemon.ACERO,
            364,
            405,
            394,
            262,
            317,
            306,
            "Metagross pertenece a la tercera generación de Pokémon, proveniente de Hoenn, y es la última evolución de Beldum. " +
                    "Metagross es el resultado de la fusión de dos Metang; este a su vez es la fusión de dos Beldum. Esto hace que Metagross " +
                    "tenga cuatro cerebros, unidos por una compleja red neuronal, capaces de resolver cálculos complicados más rápido que " +
                    "el mejor superordenador de última generación.",
            "Metagross.gif",
            List.of("PUNIO_METEORO", "CABEZAZO_ZEN", "DERRIBO", "FUERZA"),
            "¡Me-ta-gross!"
    ),

    DONPHAN(
            "Donphan",
            TypePoobKemon.TIERRA,
            384,
            372,
            372,
            218,
            240,
            240,
            "Donphan pertenece a la segunda generación de Pokémon, proveniente de Johto, siendo la evolución de Phanpy. Tiene un par de afilados colmillos, " +
                    "los cuales son más largos en los machos, que tardan mucho en crecer. Es de carácter sociable, tranquilo y vive en manadas donde el estatus " +
                    "se basa en el largo de los colmillos: entre más largos, mayor estatus. En época de celo, rueda sobre sí mismo para demostrar a las hembras su fuerza.",
            "Donphan.gif",
            List.of("CORNADA", "TERREMOTO", "ATIZAR", "GIGAIMPACTO"),
            "¡Don-phaaaaaan!"
    ),
    MACHAMP(
            "Machamp",
            TypePoobKemon.LUCHA,
            384,
            394,
            284,
            229,
            251,
            295,
            "Machamp pertenece a la primera generación de Pokémon, proveniente de Kanto, y es la última evolución de Machop. " +
                    "Machamp es uno de los Pokémon de tipo lucha más fuertes. Es capaz de dar 1000 golpes desde todos los ángulos en tan solo dos segundos, " +
                    "cada uno de estos puñetazos tiene un megatón de potencia, y si toma al rival por los pies con sus cuatro brazos, lo lanzará lo más lejos posible hasta perderse por el horizonte, ganando fácilmente el combate.",
            "Machamp.gif",
            List.of("PUNIO_DINAMICO", "GOLPE_KARATE", "SUMISION", "TAJO_CRUZADO"),
            "¡Ma-chaaaaamp!"
    ),

    DELIBIRD(
            "Delibird",
            TypePoobKemon.HIELO,
            294,
            229,
            207,
            273,
            251,
            207,
            "Delibird pertenece a la segunda generación de Pokémon, proveniente de Johto. " +
                    "Este Pokémon tiene una larga cola, en la cual puede llevar alimento, agua y cualquier cantidad " +
                    "de objetos guardados mientras vuela. Su habilidad principal es la de sacar regalos de su cola en forma " +
                    "de bolsa, algunos de ellos pueden explotar, aunque otros restauran la salud. Lleva siempre comida consigo, " +
                    "si se encuentra con montañeros o Pokémon que vagan perdidos, la comparte.",
            "Delibird.gif",
            List.of("VIENTO_HIELO", "PUNIO_CERTERO", "METEOROS", "MEGAPATADA"),
            "¡De-li-biiiird!"
    ),

    ABSOL(
            "Absol",
            TypePoobKemon.SINIESTRO,
            334,
            394,
            240,
            273,
            273,
            240,
            "Absol es un Pokémon de tipo siniestro introducido en la tercera generación originario de Hoenn. " +
                    "Tiene un cuerno en forma de media luna, tres garras en cada pata, una cola con forma de cimitarra y un pelaje blanco. " +
                    "Absol es conocido como el Pokémon de las desgracias porque su aparición se asocia con catástrofes naturales. Sin embargo, en realidad, predice desastres con su cuerno, lo que lo convierte en un Pokémon que advierte a la gente de los peligros. " +
                    "Debido a su naturaleza pacífica, se refugia en las montañas y puede vivir hasta 100 años.",
            "Absol.gif",
            List.of("PREMONICION", "TAJO_UMBRIO", "VIENTO_CORTANTE", "PUNIO_DINAMICO"),
            "¡Absoooool!"
    ),
    ALAKAZAM(
            "Alakazam",
            TypePoobKemon.PSIQUICO,
            314,
            218,
            207,
            372,
            405,
            317,
            "Alakazam es un Pokémon de tipo psíquico proveniente de Kanto, con un coeficiente intelectual de 5000 y gran poder psíquico. " +
                    "Usa cucharas para amplificar sus habilidades y evita ataques físicos. Cuando se hace amigo de alguien, regala una de sus cucharas, " +
                    "que se dice que hacen que cualquier comida tenga un sabor delicioso.",
            "Alakazam.gif",
            List.of("PSICORRAYO", "PREMONICION", "PSIQUICO", "CONFUSION"),
            "¡A-la-ka-zam!"
    ),

    ARBOK(
            "Arbok",
            TypePoobKemon.VENENO,
            324,
            317,
            260,
            284,
            251,
            282,
            "Arbok es un Pokémon de tipo veneno originario de Kanto. Evoluciona de Ekans y está inspirado en una cobra de anteojos. " +
                    "Tiene una capucha no retráctil, una lengua bífida y patrones en su vientre que puede usar para intimidar a sus enemigos. " +
                    "Arbok es un depredador peligroso que usa su veneno y su fuerza para atrapar y derrotar a sus presas, a menudo persiguiéndolas sin descanso.",
            "Arbok.gif",
            List.of("ATAQUE_RAPIDO", "COLMILLO_VENENO", "BOMBA_LODO", "POLUCION"),
            "¡Ar-bok!"
    ),
    DUSCLOPS(
            "Dusclops",
            TypePoobKemon.FANTASMA,
            284,
            262,
            394,
            163,
            240,
            394,
            "Dusclops es un Pokémon de tipo Fantasma originario de Hoenn, y es la evolución de Duskull. Su cuerpo está cubierto de vendas y está vacío por dentro, " +
                    "contiene una fantasmagórica bola de fuego. Es capaz de absorber todo lo que se acerque a su boca, sin dejar que nada salga jamás. " +
                    "Su movimiento de manos hipnotiza a su oponente, que se ve obligado a obedecerle.",
            "Dusclops.gif",
            List.of("BOLA_SOMBRA", "PUNIO_SOMBRA", "PREMONICION", "PUNIO_CERTERO"),
            "¡Dus-clops!"
    ),
    GLALIE(
            "Glalie",
            TypePoobKemon.HIELO,
            364,
            284,
            284,
            284,
            284,
            284,
            "Glalie es un Pokémon de tipo Hielo originario de Hoenn, introducido en la tercera generación. Es una de las dos evoluciones posibles de Snorunt. Su cuerpo es una esfera de hielo " +
                    "con una armadura tan dura como el acero, que lo protege de ataques. Tiene la capacidad de congelar la humedad a su alrededor, creando figuras de hielo, y en combate, " +
                    "puede congelar a su oponente con el aire frío que expulsa. Es conocido por su predilección por presas congeladas, como los Vanillite.",
            "Glalie.gif",
            List.of("RAYO_HIELO", "TRITURAR", "VENTISCA", "HIDROBOMBA"),
            "¡Gla-lie!"
    ),
    GOLDUCK(
            "Golduck",
            TypePoobKemon.AGUA,
            364,
            289,
            280,
            295,
            317,
            284,
            "Golduck es un Pokémon de tipo Agua originario de Kanto, introducido en la primera generación. Evoluciona de Psyduck. " +
                    "Posee una forma más grande y azul, con cuatro crestas en la cabeza, y una esfera en su frente que le permite usar poderes telequinéticos. " +
                    "Su cuerpo está adaptado para nadar rápidamente gracias a su pico, extremidades palmeadas y larga cola. Golduck es conocido por ser el nadador " +
                    "más rápido entre los Pokémon, capaz de nadar largas distancias incluso en aguas turbulentas.",
            "Golduck.gif",
            List.of("HIDROBOMBA", "PUNIO_CERTERO", "RAYO_HIELO", "GOLPE_CUERPO"),
            "¡Gol-duck!"
    ),
    GRANBULL(
            "Granbull",
            TypePoobKemon.HADA,
            384,
            372,
            273,
            207,
            240,
            240,
            "Granbull es un Pokémon de tipo Hada originario de Johto, introducido en la segunda generación, y es la evolución de Snubbull. " +
                    "Su apariencia recuerda a un perro bulldog, con una musculosa mandíbula y colmillos inferiores que sobresalen de su boca, " +
                    "lo que le da un aspecto intimidante. A pesar de su apariencia ruda, Granbull es un Pokémon muy tímido y asustadizo, " +
                    "que solo muestra su gran fuerza cuando se siente atacado. Es popular entre la gente joven debido a su aspecto y carácter amistoso, pero no es un buen guardián.",
            "Granbull.gif",
            List.of("MORDISCO", "PUNIO_DINAMICO", "CABEZAZO", "HOJA_MAGICA"),
            "¡Gran-bull!"
    ),

    GROUDON(
            "Groudon",
            TypePoobKemon.TIERRA,
            404,
            438,
            416,
            306,
            328,
            306,
            "Groudon es un Pokémon legendario de tipo Tierra originario de Hoenn, introducido en la tercera generación. De color rojo con franjas negras, su apariencia contrasta con Kyogre. Vive en lugares subterráneos, "
                    + "en la caldera de un volcán. Es capaz de destruir islas con un golpe o mover las placas tectónicas, creando volcanes y terremotos. Su habilidad especial, 'Sequía', intensifica el calor "
                    + "solar, evaporando el agua y nutriendo la tierra. Según la mitología, es el creador de los continentes, rival de Kyogre y su enfrentamiento es controlado por Rayquaza.",
            "Groudon.gif",
            List.of("ULTIMA_BAZA", "PUNIO_DINAMICO", "TERREMOTO", "TRITURAR"),
            "¡Grou-don!"
    ),

    GYARADOS(
            "Gyarados",
            TypePoobKemon.VOLADOR,
            394,
            383,
            282,
            287,
            240,
            328,
            "Gyarados es un Pokémon de tipo Volador originario de Kanto, introducido en la primera generación. Evoluciona de Magikarp. " +
                    "Se asemeja a un dragón marino, con un cuerpo azul y una cola que le permite atravesar ríos a contracorriente. " +
                    "Es extremadamente violento, con colmillos capaces de partir rocas y escamas más duras que el acero. Inspirado en la leyenda " +
                    "china de la carpa que se convierte en dragón tras saltar una cascada.",
            "Gyarados.gif",
            List.of("SANA", "AGUA_COLA", "RAYO_HIELO", "SURF"),
            "¡Gya-ra-dos!"
    ),
    HARIYAMA(
            "Hariyama",
            TypePoobKemon.LUCHA,
            492,
            372,
            240,
            218,
            196,
            240,
            "Hariyama es un Pokémon de tipo Lucha originario de Hoenn, introducido en la tercera generación, evolución de Makuhita. Está basado en un luchador de sumo con un cuerpo robusto y enormes manos. " +
                    "Este Pokémon es capaz de emplear fuertes movimientos y resistir ataques. Se le ve practicando sus poderosos empujones, capaces de partir un poste de teléfono o lanzar camiones de 10 toneladas con un solo golpe. " +
                    "Aunque su cuerpo parece gordo, está compuesto principalmente por músculos.",
            "Hariyama.gif",
            List.of("CABEZAZO", "MEGAPUNIO", "FUERZA", "MEGAPATADA"),
            "¡Ha-ri-ya-ma!"
    ),
    HERACROSS(
            "Heracross",
            TypePoobKemon.BICHO,
            364,
            383,
            273,
            295,
            196,
            317,
            "Heracross es un Pokémon de tipo Bicho originario de Johto, introducido en la segunda generación. Es muy fuerte, especialmente su cuerno, capaz de destruir árboles con un solo golpe. "
                    + "Su coraza azul perlado le da una gran defensa contra ataques físicos. Aunque no puede volar largas distancias, utiliza sus pequeñas alas para moverse rápidamente durante los combates. "
                    + "Usa su cuerno para embestir a sus enemigos y puede lanzar objetos mucho más pesados que él.",
            "Heracross.gif",
            List.of("CORNADA", "ATIZAR", "GOLPE_CUERPO", "MEGACUERNO"),
            "¡He-ra-cross!"
    ),
    LATIOS(
            "Latios",
            TypePoobKemon.DRAGON,
            364,
            306,
            284,
            350,
            394,
            350,
            "Latios es un Pokémon legendario de tipo Dragón originario de Hoenn introducido en la tercera generación. " +
                    "Su cuerpo tiene la forma de un ave mezclada con un avión, con un cuello largo, alas como las de un avión y una aleta inferior. " +
                    "Es azul con blanco, con un triángulo rojo en el pecho y tiene ojos rojos. Es muy inteligente, capaz de entender el lenguaje humano " +
                    "y comunicarse por telepatía. También puede volverse invisible gracias a un plumón especial.",
            "Latios.gif",
            List.of("PSICO_ATAQUE","PODER_PASADO", "GARRA_DRAGON", "VUELO"),
            "¡La-ti-os!"
    ),
    MAGNETON(
            "Magneton",
            TypePoobKemon.ELECTRICO,
            304,
            240,
            317,
            262,
            372,
            262,
            "Magneton es un Pokémon de tipo Eléctrico originario de Kanto, introducido en la primera generación. Es la evolución de Magnemite. Cuando tres Magnemite se unen debido a sus cargas magnéticas fuertes, forman un Magneton. " +
                    "Esto hace que sus cerebros se combinen, pero no aumenta su inteligencia. Sin embargo, su poder eléctrico se triplica. Magneton tiene la capacidad de generar ondas magnéticas y tiene una fuerza antigravitacional que le permite flotar.",
            "Magneton.gif",
            List.of("RAYO", "CANION_FLASH", "TRUENO", "RAYO_AURORA"),
            "¡Mag-ne-ton!"
    ),
    MIGHTYENA(
            "Mightyena",
            TypePoobKemon.SINIESTRO,
            344,
            306,
            262,
            262,
            240,
            240,
            "Mightyena es un Pokémon de tipo Siniestro originario de Hoenn, introducido en la tercera generación. Es la evolución de Poochyena. Su pelaje es negro y gris, con ojos rojos y una larga cola. Se parece a una hiena parda o rayada. " +
                    "Este Pokémon es muy sociable y vive en jaurías. Solo obedece a entrenadores que considera superiores, y su principal habilidad es trabajar en equipo. " +
                    "Su ataque principal son sus afilados colmillos, y siempre muestra señales como gruñidos y estiramientos antes de atacar.",
            "Mightyena.gif",
            List.of("MORDISCO", "TRITURAR", "TAJO_UMBRIO", "DOBLE_FILO"),
            "¡My-ty-e-na!"
    ),
    PINSIR(
            "Pinsir",
            TypePoobKemon.BICHO,
            334,
            383,
            328,
            295,
            229,
            262,
            "Pinsir es un Pokémon de tipo Bicho originario de Kanto, introducido en la primera generación, basado en un ciervo volante. " +
                    "Habita en bosques con árboles de savia, de la cual se alimenta. Tiene un temperamento fuerte y posee largos cuernos en forma " +
                    "de pinzas, cubiertos de pinchos. Sus mandíbulas, con más de 20 dientes pequeños, le permiten extraer savia. Sus pinzas son tan " +
                    "afiladas que pueden cortar troncos de árboles y usar sus cuernos para atrapar y lanzar a sus víctimas.",
            "Pinsir.gif",
            List.of("MEGACUERNO", "TIJERA_X", "GOLPE_CUERPO", "DERRIBO"),
            "¡Pin-sir!"
    ),
    RAPIDASH(
            "Rapidash",
            TypePoobKemon.FUEGO,
            334,
            328,
            262,
            339,
            284,
            284,
            "Rapidash es un Pokémon de tipo Fuego originario de Kanto, introducido en la primera generación, basado en un unicornio con llamas. Evoluciona de Ponyta. " +
                    "Con una crin de llamas y un cuerno en su frente. Es muy competitivo, buscando siempre retos de velocidad, " +
                    "siendo capaz de alcanzar los 240 km/h en solo diez trotes. Su velocidad es tan impresionante que puede cruzar regiones enteras en poco tiempo.",
            "Rapidash.gif",
            List.of("LLAMARADA", "LANZALLAMAS", "ATAQUE_RAPIDO", "MEGAPATADA"),
            "¡Ra-pi-dash!"
    ),
    REGIROCK(
            "Regirock",
            TypePoobKemon.ROCA,
            364,
            328,
            548,
            218,
            218,
            328,
            "Regirock es un Pokémon legendario de tipo Roca originario de Hoenn, introducido en la tercera generación, " +
                    "basado en los golems de las leyendas. Su origen y la forma en que tiene vida son un misterio. Si se daña, " +
                    "Regirock busca piedras similares para repararse, incluso utilizando rocas para limar su cuerpo. " +
                    "Este Pokémon ha permanecido aislado por mucho tiempo y no se sabe cómo se alimenta.",
            "Regirock.gif",
            List.of("TERREMOTO", "PUNIO_METEORO", "HIPERRAYO", "SANA"),
            "¡Re-gi-rock!"
    ),
    SCEPTILE(
            "Sceptile",
            TypePoobKemon.PLANTA,
            344,
            295,
            251,
            372,
            339,
            295,
            "Sceptile es un Pokémon de tipo Planta originario de Hoenn, introducido en la tercera generación, la evolución de Grovyle. " +
                    "Tiene una apariencia reptiliana y bípedo, con crestas triangulares en la cabeza y un pico similar al de una tortuga. " +
                    "Sus brazos están equipados con hojas afiladas que utiliza para atacar, y su cola tiene un follaje frondoso. Es muy ágil, " +
                    "prefiriendo saltar entre los árboles en lugar de moverse por tierra. Cuida los árboles que lo rodean y usa las semillas " +
                    "en su espalda para revitalizarlos.",
            "Sceptile.gif",
            List.of("HOJA_AFILADA", "RESIDUOS", "BOMBA_LODO", "RAYO_SOLAR"),
            "¡Scep-ti-lee!"
    ),
    STEELIX(
            "Steelix",
            TypePoobKemon.ACERO,
            354,
            295,
            548,
            174,
            229,
            251,
            "Steelix es un Pokémon de tipo Acero originario de Johto, introducido en la segunda generación, "
                    + "evolución de Onix. Es un gigantesco ofidio con cuerpo segmentado compuesto por roca y aleación de acero, "
                    + "su dureza es superior a la del diamante. Su cuerpo tiene una cabeza de grandes maxilares y colmillos prominentes, "
                    + "y una cola con segmentos que le permiten cavar a gran velocidad. Habita en las profundidades de la tierra, "
                    + "donde su cuerpo se ha comprimido por la presión y el calor.",
            "Steelix.gif",
            List.of("COLA_FERREA", "TERREMOTO", "AVALANCHA", "MORDISCO"),
            "¡Stee-eel-ix!"
    ),
    SWELLOW(
            "Swellow",
            TypePoobKemon.VOLADOR,
            324,
            295,
            240,
            383,
            273,
            218,
            "Swellow es un Pokémon de tipo Volador originario de Hoenn, introducido en la tercera generación, evolución de Taillow. " +
                    "Basado en una golondrina, tiene cuerpo azul, pecho rojo y blanco, y garras grandes. Es muy veloz en vuelo y un hábil cazador, " +
                    "capaz de sumergirse en el agua para atrapar presas. Habita zonas boscosas, pero también se le ve surcando cielos abiertos, buscando climas cálidos.",
            "Swellow.gif",
            List.of("PICOTAZO", "ATAQUE_AEREO", "VUELO", "ATAQUE_RAPIDO"),
            "¡Sweeeellow!"
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

