package domain;

import java.util.function.BiConsumer;
import domain.MovePoobKemon.Category;

/**
 * Datos estáticos de cada movimiento, incluidos sus parámetros
 * y la lógica de efecto secundario.
 */
public enum MoveData {
    RAYO(
            "Rayo",
            TypePoobKemon.ELECTRICO,
            Category.ESPECIAL,
            90, 100, 24,
            "PARALIZA un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened()
                        && !"PARALIZADO".equals(def.getAlteredState())
                        && Math.random() < 0.10
                ) {
                    def.setAlteredState("PARALIZADO");
                }
            }
    ),

    TRUENO(
            "Trueno",
            TypePoobKemon.ELECTRICO,
            Category.ESPECIAL,
            110, 70, 16,
            "PARALIZAR un 30%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened()
                        && !"PARALIZADO".equals(def.getAlteredState())
                        && Math.random() < 0.30
                ) {
                    def.setAlteredState("PARALIZADO");
                }
            }
    ),

    PUNIO_TRUENO(
            "Puño Trueno",
            TypePoobKemon.ELECTRICO,
            Category.FISICO,
            75, 100, 24,
            "PARALIZAR un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened()
                        && !"PARALIZADO".equals(def.getAlteredState())
                        && Math.random() < 0.10
                ) {
                    def.setAlteredState("PARALIZADO");
                }
            }
    ),

    BOLA_SOMBRA(
            "Bola Sombra",
            TypePoobKemon.FANTASMA,
            Category.ESPECIAL,
            80, 100, 24,
            "Reducir la defensa un 20%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.20) {
                    def.lowerSpecialDefence(1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa especial.");
                }
            }
    ),

    COLA_FERREA(
            "Cola Férrea",
            TypePoobKemon.ACERO,
            Category.FISICO,
            100, 75, 24,
            "Reducir la defensa un 30%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),

    PUNIO_SOMBRA(
            "Puño Sombra",
            TypePoobKemon.FANTASMA,
            Category.FISICO,
            60, 100, 32,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    FORCEJEO(
            "Forcejeo",
            TypePoobKemon.NORMAL,
            MovePoobKemon.Category.FISICO,
            50, 100, 1,
            null,
            0,
            false,
            (att, def) -> {
                new Forcejeo().run(att, def);
            }
    ),

    VENDETTA(
            "Vendetta",
            TypePoobKemon.FANTASMA,
            MovePoobKemon.Category.FISICO,
            60,
            100,
            16,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PSIQUICO(
            "Psíquico",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            90,
            100,
            16,
            "Puede bajar la Defensa Especial un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.lowerSpecialDefence(1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa especial.");
                }
            }
    ),
    PREMONICION(
            "Premonición",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            80,
            90,
            15,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    RAYO_AURORA(
            "Rayo Aurora",
            TypePoobKemon.HIELO,
            Category.ESPECIAL,
            65,
            100,
            20,
            "Puede congelar al objetivo un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),

    FERSANCHO(
            "Forsancho",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            70,
            100,
            15,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PUNIO_CERTERO(
            "Puño Certero",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            75,
            100,
            32,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    RAYO_HIELO(
            "Rayo Hielo",
            TypePoobKemon.HIELO,
            Category.ESPECIAL,
            90,
            100,
            16,
            "Puede congelar al objetivo un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),

    RELAJO(
            "Relajo",
            TypePoobKemon.NORMAL,
            Category.ESTADO,
            0,
            100,
            2,
            "Restaura la mitad de los PS máximos del usuario",
            0,
            true,
            (att, def) -> {
                PoobKemon self = att;
                if (!self.isWeakened()) {
                    int heal = self.getPpMax() / 2;
                    self.cure(heal);
                    MovePoobKemon.logGlobal(self.getName() + " recuperó " + heal + " PS con Relajo.");
                }
            }
    ),

    GOLPE_CUERPO(
            "Golpe Cuerpo",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            85,
            100,
            24,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    DOBLE_PATADA(
            "Doble Patada",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            60,
            100,
            48,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),
    POLUCION(
            "Polución",
            TypePoobKemon.VENENO,
            Category.FISICO,
            20,
            100,
            15,
            "20 de daño y 40% de envenenar; +10% ataque siguiente",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.40) {
                    def.setAlteredState("ENVENENADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido envenenado.");
                    att.buffAttack(1.10);
                    // MovePoobKemon.logGlobal(att.getName() + " se siente poderoso tras envenenar.");
                }
            }
    ),
    HIPERRAYO(
            "Hiperrayo",
            TypePoobKemon.NORMAL,
            Category.ESPECIAL,
            150,
            90,
            8,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    GARRA_DRAGON(
            "Garra Dragón",
            TypePoobKemon.DRAGON,
            Category.FISICO,
            80,
            100,
            24,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    LANZALLAMAS(
            "Lanzallamas",
            TypePoobKemon.FUEGO,
            Category.ESPECIAL,
            90,
            100,
            15,
            "Puede quemar al objetivo un 10%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.setAlteredState("QUEMADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido quemado.");
                    att.buffAttack(1.10);
                    //MovePoobKemon.logGlobal(att.getName() + " canalizó el calor y se siente más fuerte para el siguiente ataque.");
                }
            }
    ),

    LLAMARADA(
            "Llamarada",
            TypePoobKemon.FUEGO,
            Category.ESPECIAL,
            120,
            85,
            8,
            "Puede quemar al objetivo un 30%",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.setAlteredState("QUEMADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido quemado por Llamarada.");
                    att.buffAttack(1.10);
                }
            }
    ),

    CUCHILLADA(
            "Cuchillada",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            70,
            100,
            32,
            "Golpe crítico garantizado",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("CRITICO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido afectado por un critico.");
                    att.buffAttack(1.125);
                }
            }
    ),

    ONDA_IGNEA(
            "Onda Ígnea",
            TypePoobKemon.FUEGO,
            Category.ESPECIAL,
            90,
            90,
            16,
            null,
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.setAlteredState("QUEMADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido quemado.");
                    att.buffAttack(1.10);
                }
            }
    ),
    HIDROBOMBA(
            "Hidrobomba",
            TypePoobKemon.AGUA,
            Category.ESPECIAL,
            110,
            80,
            8,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    CABEZAZO(
            "Cabezazo",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            130,
            100,
            16,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PISTOLA_AGUA(
            "Pistola Agua",
            TypePoobKemon.AGUA,
            Category.ESPECIAL,
            40,
            100,
            40,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    DOBLE_FILO(
            "Doble Filo",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            120,
            100,
            24,
            "Inflige 33% de daño de retroceso al usuario",
            0,
            true,
            (att, def) -> {
                int recoil = (int) (att.getPpCurrent() * 0.33);
                att.takeDamage(recoil);
                MovePoobKemon.logGlobal(att.getName() + " recibe " + recoil + " PS de retroceso.");
            }
    ),

    BOMBA_LODO(
            "Bomba Lodo",
            TypePoobKemon.VENENO,
            Category.ESPECIAL,
            90,
            100,
            10,
            "30% de envenenar al objetivo",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.setAlteredState("ENVENENADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido envenenado.");
                    att.buffAttack(1.10);
                }
            }
    ),
    TERREMOTO(
            "Terremoto",
            TypePoobKemon.TIERRA,
            Category.FISICO,
            100,
            100,
            16,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),
    RESIDUOS(
            "Residuos",
            TypePoobKemon.PLANTA,
            Category.ESTADO,
            65,
            100,
            10,
            "Drena 25% de los PS máximos del objetivo para curar al usuario",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.setAlteredState("ENVENENADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido envenenado.");
                    att.buffAttack(1.10);
                }
            }
    ),
    VUELO(
            "Vuelo",
            TypePoobKemon.VOLADOR,
            Category.FISICO,
            90,
            95,
            24,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    HOJA_MAGICA(
            "Hoja Mágica",
            TypePoobKemon.HADA,
            Category.ESPECIAL,
            60,
            100,
            32,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),
    PODER_PASADO(
            "Poder Pasado",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            120,
            100,
            8,
            "Golpea al segundo turno, pero aquí se aplica inmediatamente",
            0,
            true,
            (att, def) -> {
            }
    ),
    TRITURAR(
            "Triturar",
            TypePoobKemon.SINIESTRO,
            Category.FISICO,
            80,
            100,
            24,
            "10% de bajar defensa del objetivo",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.20) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),
    SANA(
            "Sańa",
            TypePoobKemon.ROCA,
            Category.ESTADO,
            120,
            100,
            10,
            "+20% de ataque para el siguiente turno",
            0,
            true,
            (att, def) -> {
                att.buffAttack(1.20);
                MovePoobKemon.logGlobal(att.getName() + " se llena de saña y recibe +20% de ataque.");
            }
    ),
    MEGAPUNIO(
            "Megapuño",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            80,
            85,
            32,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),
    FUERZA(
            "Fuerza",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            80,
            100,
            15,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PUNIO_METEORO(
            "Puño Meteoro",
            TypePoobKemon.ACERO,
            Category.FISICO,
            90,
            100,
            16,
            "30% de reducir defensa especial del objetivo",
            0,
            true,
            (att, def) -> {
                att.buffAttack(1.20);
                MovePoobKemon.logGlobal(att.getName() + " se llena de saña y recibe +20% de ataque.");
            }
    ),

    CABEZAZO_ZEN(
            "Cabezazo Zen",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            80,
            90,
            10,
            "20% de probabilidades de hacer perder el siguiente turno al oponente",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.20) {
                    def.markSkipNextTurn();
                    MovePoobKemon.logGlobal(def.getName()
                            + " está amedrentado y perderá su próximo turno.");
                }
            }
    ),
    DERRIBO(
            "Derribo",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            90,
            85,
            32,
            null,
            0,
            true,
            (att, def) -> {

            }
    ),
    CORNADA(
            "Cornada",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            65,
            100,
            40,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),
    ATIZAR(
            "Atizar",
            TypePoobKemon.TIERRA,
            Category.FISICO,
            80,
            100,
            32,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    GIGAIMPACTO(
            "Gigaimpacto",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            150,
            90,
            8,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PUNIO_DINAMICO(
            "Puño Dinámico",
            TypePoobKemon.LUCHA,
            Category.FISICO,
            100,
            50,
            8,
            "Fuerte probabilidad de causar confusión (20%)",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.20) {
                    def.setAlteredState("CONFUNDIDO");
                    att.buffAttack(1.20);
                    MovePoobKemon.logGlobal(def.getName() + " está confundido tras Puño Dinámico.");
                }
            }
    ),

    GOLPE_KARATE(
            "Golpe Karate",
            TypePoobKemon.LUCHA,
            Category.FISICO,
            50,
            100,
            25,
            "Golpe rápido y preciso, 10% de crítico",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("CRITICO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido afectado por un critico.");
                    att.buffAttack(1.10);
                }
            }


    ),
    SUMISION(
            "Sumisión",
            TypePoobKemon.LUCHA,
            Category.FISICO,
            80,
            80,
            22,
            "Inflige daño y 25% del daño infligido lo recibe el usuario",
            0,
            true,
            (att, def) -> {
                int recoil = (int) (att.getPpCurrent() * 0.33);
                att.takeDamage(recoil);
                MovePoobKemon.logGlobal(att.getName() + " recibe " + recoil + " PS de retroceso.");
            }
    ),
    TAJO_CRUZADO(
            "Tajo Cruzado",
            TypePoobKemon.LUCHA,
            Category.FISICO,
            100,
            80,
            8,
            "Golpe rápido y preciso, 12,5% de crítico",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("CRITICO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido afectado por un critico.");
                    att.buffAttack(1.125);
                }
            }
    ),
    VIENTO_HIELO(
            "Viento Hielo",
            TypePoobKemon.HIELO,
            Category.ESPECIAL,
            55,
            95,
            24,
            "10% de bajar la Velocidad del objetivo",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.modifyEvasion(-1);
                    MovePoobKemon.logGlobal(def.getName() + " ve su velocidad reducida por Viento Hielo.");
                }
            }
    ),
    METEOROS(
            "Meteoros",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            60,
            100,
            10,
            "20% de subir la Defensa del usuario",
            0,
            true,
            (att, def) -> {
                if (Math.random() < 0.20) {
                    att.modifyDefense(1);
                    MovePoobKemon.logGlobal(att.getName() + " ve su defensa aumentada tras Meteoros.");
                }
            }
    ),
    MEGAPATADA(
            "Megapatada",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            120,
            75,
            8,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    TAJO_UMBRIO(
            "Tajo Umbrío",
            TypePoobKemon.SINIESTRO,
            Category.FISICO,
            70,
            100,
            25,
            "Alto ratio de crítico (x1.125)",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("CRITICO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido afectado por un critico.");
                    att.buffAttack(1.125);
                }
            }
    ),

    VIENTO_CORTANTE(
            "Viento Corta",
            TypePoobKemon.NORMAL,
            Category.ESPECIAL,
            80,
            100,
            16,
            "30% de bajar evasión del objetivo",
            0,
            true,
            (att, def) -> {
                if (Math.random() < 0.30) {
                    def.modifyEvasion(-1);
                    MovePoobKemon.logGlobal(def.getName() + " ve reducida su evasión tras Viento Cortante.");
                }
            }
    ),
    PSICORRAYO(
            "Psicorrayo",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            65,
            100,
            15,
            null,
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.setAlteredState("CONFUNDIDO");
                    att.buffAttack(1.40);
                    MovePoobKemon.logGlobal(def.getName() + " está confundido tras Puño Dinámico.");
                }
            }
    ),
    CONFUSION(
            "Confusión",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            50,
            100,
            25,
            "10% de confundir",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.setAlteredState("CONFUNDIDO");
                    att.buffAttack(1.10);
                    MovePoobKemon.logGlobal(def.getName() + " está confundido.");
                }
            }
    ),
    ATAQUE_RAPIDO(
            "Ataque Rápido",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            40,
            100,
            48,
            null,
            1,
            true,
            (att, def) -> {
            }
    ),

    COLMILLO_VENENO(
            "Colmillo Veneno",
            TypePoobKemon.VENENO,
            Category.FISICO,
            50,
            100,
            24,
            "Envenenar gravemente al rival (50%)",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("ENVENENADO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido envenenado.");
                    att.buffAttack(1.30);
                }
            }
    ),

    VENTISCA(
            "Ventisca",
            TypePoobKemon.HIELO,
            Category.ESPECIAL,
            110,
            70,
            8,
            "10% de congelar a todos los oponentes en dobles",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),

    MORDISCO(
            "Mordisco",
            TypePoobKemon.SINIESTRO,
            Category.FISICO,
            60,
            100,
            40,
            "30% de provocar retroceso (flinch)",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.markSkipNextTurn();
                    MovePoobKemon.logGlobal(def.getName()
                            + " está amedrentado y perderá su próximo turno.");
                }
            }
    ),
    ULTIMA_BAZA(
            "Última Baza",
            TypePoobKemon.NORMAL,
            Category.FISICO,
            140,
            100,
            8,
            null,
            0,
            true,
            (att, def) -> {

            }
    ),
    AGUA_COLA(
            "Acuacola",
            TypePoobKemon.AGUA,
            Category.FISICO,
            90,
            90,
            16,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    SURF(
            "Surf",
            TypePoobKemon.AGUA,
            Category.ESPECIAL,
            90,
            100,
            24,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    MEGACUERNO(
            "Megacuerno",
            TypePoobKemon.BICHO,
            Category.FISICO,
            120,
            85,
            16,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    RAYO_SOLAR(
            "Rayo Solar",
            TypePoobKemon.PLANTA,
            Category.ESPECIAL,
            120,
            100,
            10,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    PSICO_ATAQUE(
            "Psicoataque",
            TypePoobKemon.PSIQUICO,
            Category.ESPECIAL,
            140,
            90,
            8,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    CANION_FLASH(
            "Cañón Flash",
            TypePoobKemon.ACERO,
            Category.ESPECIAL,
            80,
            100,
            10,
            "10% de bajar Precisión",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.10) {
                    def.modifyDefense(-1);
                    MovePoobKemon.logGlobal(def.getName() + " perdió defensa.");
                }
            }
    ),

    TIJERA_X(
            "Tijera X",
            TypePoobKemon.BICHO,
            Category.FISICO,
            80,
            100,
            24,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    HOJA_AFILADA(
            "Hoja Afilada",
            TypePoobKemon.PLANTA,
            Category.FISICO,
            90,
            100,
            15,
            null,
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.50) {
                    def.setAlteredState("CRITICO");
                    MovePoobKemon.logGlobal(def.getName() + " ha sido afectado por un critico.");
                    att.buffAttack(1.125);
                }
            }
    ),
    AVALANCHA(
            "Avalancha",
            TypePoobKemon.TIERRA,
            Category.FISICO,
            75,
            90,
            10,
            "30% de amedrantar al objetivo",
            0,
            true,
            (att, def) -> {
                if (!def.isWeakened() && Math.random() < 0.30) {
                    def.markSkipNextTurn();
                    MovePoobKemon.logGlobal(def.getName()
                            + " está amedrentado y perderá su próximo turno.");
                }
            }
    ),

    PICOTAZO(
            "Picotazo",
            TypePoobKemon.VOLADOR,
            Category.FISICO,
            50,
            100,
            35,
            null,
            0,
            true,
            (att, def) -> {
            }
    ),

    ATAQUE_AEREO(
            "Ataque Aéreo",
            TypePoobKemon.VOLADOR,
            Category.FISICO,
            60,
            100,
            25,
            null,
            0,
            true,
            (att, def) -> {
            }
    );



    public final String name;
    public final TypePoobKemon type;
    public final Category category;
    public final int potency, precision, ppMax;
    public final String secondaryEffect;
    public final int priority;
    public final boolean useSuperRun;
    public final BiConsumer<PoobKemon,PoobKemon> effect;

    MoveData(String name,
             TypePoobKemon type,
             Category category,
             int potency,
             int precision,
             int ppMax,
             String secondaryEffect,
             int priority,
             boolean useSuperRun,
             BiConsumer<PoobKemon,PoobKemon> effect)
    {
        this.name = name;
        this.type = type;
        this.category = category;
        this.potency = potency;
        this.precision = precision;
        this.ppMax = ppMax;
        this.secondaryEffect = secondaryEffect;
        this.priority = priority;
        this.useSuperRun = useSuperRun;
        this.effect = effect;
    }
}
