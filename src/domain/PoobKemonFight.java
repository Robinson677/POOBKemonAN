package domain;

import java.util.*;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Clase principal que maneja la seleccion y creacion de entrenadores y PoobKemons
 */
public class PoobKemonFight implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient Map<String, PoobKemonFactory> registry = new HashMap<>();
    private transient Map<PoobKemon, Integer> hpMap = new HashMap<>();
    private String lastPendingItem = null;

    public enum UIState {
        MENU,
        BATTLE,
        BACKPACK,
        POKEMON_SELECT,
        COMBAT,
        ITEM_SELECT,
        MOVE_SELECT,
        LOG,
        PAUSED
    }

    private UIState uiState = UIState.MENU;

    private Trainer trainer1;
    private Trainer trainer2;
    private final int initialPicker;
    private int turn;
    private boolean isOk;


    /**
     * Primer Constructor de PoobKemonFight
     */
    public PoobKemonFight() {
        initRegistry();
        this.initialPicker = 1;
        this.turn = initialPicker;
        this.isOk = true;
    }

    /**
     * Segundo Constructor de PoobKemonFight
     *
     * @param t1 representa el primer entrenador
     * @param t2 representa el segundo entrenador
     * @param firstPicker Indice del jugador que inicia el juego
     */
    public PoobKemonFight(Trainer t1, Trainer t2, int firstPicker) {
        this.trainer1 = t1;
        this.trainer2 = t2;
        int p = (firstPicker == 2 ? 2 : 1);
        this.turn = p;
        this.initialPicker = p;
        initRegistry();
        this.isOk = true;
    }

    //Registros en el juego

    /**
     * Regitra lo pokemones
     */
    private void initRegistry() {
        for (PokemonData pd : PokemonData.values()) {
            registry.put(pd.name(), () -> new PoobKemonData(pd, this));
        }
        ok();
    }

    /**
     * Interfaz que definimos para  poder instanciar los pokemones
     */
    @FunctionalInterface
    private interface PoobKemonFactory {
        PoobKemon create();
    }

    /**
     * Obtiene las keys de los pokemones
     *
     * @return List de los nombre de los pokemones
     */
    public List<String> getPokemonKeys() {
        ok();
        return new ArrayList<>(registry.keySet());
    }

    /**
     * Crea una instancia del pokemon usando la interfaz
     *
     * @param key el nombre en mayuscula del pokemon
     * @return la instancia del pokemon si no es null
     * @throws POOBKemonException si la key no existe
     */
    public PoobKemon createPokemon(String key) throws POOBKemonException {
        PoobKemonFactory f = registry.get(key);
        if (f == null) {
            throw new POOBKemonException(
                    String.format(POOBKemonException.INVALID_POKEMON_KEY, key)
            );
        }
        ok();
        return f.create();
    }

    //Entrenadores

    /**
     * Añade un entrenador segun por como haya quedado sus turnos por la moneda
     *
     * @param trainer  el jugador 1 o 2
     * @param position la posicion que ocupo
     */
    public void addTrainer(Trainer trainer, int position) {
        if (position == 1) {
            this.trainer1 = trainer;
        } else if (position == 2) {
            this.trainer2 = trainer;
        } else {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        ok();
    }

    /**
     * Obtiene el primer entrenador
     *
     * @return Trainer 1
     */
    public Trainer getTrainer1() {
        ok();
        return trainer1;
    }

    /**
     * * Obtiene el segundo entrenador
     *
     * @return Trainer 2
     */
    public Trainer getTrainer2() {
        ok();
        return trainer2;
    }

    /**
     * Obtiene el nombre de alguno de los 2 entrenadores
     *
     * @param idx segun la posicion que tenga el entrenador
     * @return String que representa el nombre del entrenador
     */
    public String getTrainerName(int idx) {
        ok();
        return (idx == 0) ? trainer1.getName() : trainer2.getName();
    }

    /**
     * Obtiene el equipo de  alguno de los 2 entrenadores
     *
     * @param idx segun la posicion que tenga el entrenador
     * @return String que representa el nombre del entrenador
     */
    public String getTrainerTeam(int idx) {
        ok();
        return (idx == 0) ? trainer1.getColorTeam() : trainer2.getColorTeam();
    }


    /**
     * Obtiene el pokemon que eligio para el entrenador especifico
     *
     * @param idx segun la posicion que tenga el entrenador
     * @return PoobKemon el pokemon activo del entrenador
     */
    public PoobKemon getCurrentPokemon(int idx) {
        Trainer t = (idx == 0) ? trainer1 : trainer2;
        int ai = t.getActiveIndex();
        return t.getTeam().get(ai);
    }

    /**
     * Devuelve la lista de keys de los pokemones que eligio el entrenador
     */
    public List<String> getCurrentTrainerPokemonKeys() {
        Trainer t = getCurrentTrainer();
        List<String> keys = new ArrayList<>();
        for (PoobKemon p : t.getTeam()) {
            String found = registry.entrySet().stream()
                    .filter(e -> e.getValue().create().getName().equals(p.getName()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            keys.add(found);
        }
        return keys;
    }

    /**
     * Verifica si el pokemon con la key dada en el equipo del entrenador actual esta debilitado
     *
     * @return true si esta debilitado de lo contrario false
     */
    public boolean isCurrentTrainerPokemonWeakened(String key) {
        Trainer t = getCurrentTrainer();
        String display = getDisplayName(key);
        for (PoobKemon p : t.getTeam()) {
            if (p.getName().equalsIgnoreCase(display)) {
                return p.isWeakened();
            }
        }
        return false;
    }


    /**
     * Indica si el entrenador en la posición 0 o 1 ha sido derrotado
     *
     * @return true si ha sido derrotado de lo contrario false
     */
    public boolean isTrainerDefeated(int idx) {
        Trainer t = (idx == 0) ? trainer1 : trainer2;
        return t != null && t.isDefeated();
    }

    /**
     * Cambia el pokemon activo del entrenador en turno por el que selecciona antes de batalla
     *
     * @param key nombre del pokemon en mayuscula
     * @return true si el intercambio se hizo si no es false
     */
    public boolean switchActivePokemon(String key) {
        Trainer t = getCurrentTrainer();
        List<PoobKemon> team = t.getTeam();
        String display = getDisplayName(key);
        for (int i = 0; i < team.size(); i++) {
            if (team.get(i).getName().equalsIgnoreCase(display)) {
                return t.changePoobKemon(i);
            }
        }
        return false;
    }


    /**
     * Se resetea el juego en caso de que algun jugador huya o le de exit incluido los items
     */
    public void resetSelection() {
        if (trainer1 != null) {
            trainer1.getTeam().clear();
            trainer1.resetUsedItemCount();
        }
        if (trainer2 != null) {
            trainer2.getTeam().clear();
            trainer2.resetUsedItemCount();
        }
        combatLog.clear();
        hpMap.clear();
        this.turn = initialPicker;
        ok();
    }

    /**
     * Elimina un pokemon elegido por un entrenador
     *
     * @param t El entredaor que va a quitar el pokemon
     * @param p El pokemon a quitar
     * @return true si lo quito si no lo dejo en su team
     */
    public boolean removePokemon(Trainer t, PoobKemon p) {
        if (t != null && p != null) {
            ok();
            return t.getTeam().remove(p);
        }
        return false;
    }


    //Turnos del juego

    /**
     * El entrenador que gano la moneda elije su team azul o rojo y se le asigna
     *
     * @param idx segun la posicion que tenga el entrenador
     * @return true si es el primer entrenador si no el segundo
     */
    public boolean hasSelected(int idx) {
        Trainer t = (idx == 0) ? trainer1 : trainer2;
        ok();
        return t.getTeam().size() > 0;
    }


    /**
     * El turno que obtuvo cada jugador
     *
     * @return Trainer
     */
    public Trainer getCurrentTrainer() {
        ok();
        return (turn == 1) ? trainer1 : trainer2;
    }

    /**
     * Avanza al siguiente turno
     */
    public void nextTurn() {
        turn = (turn == 1) ? 2 : 1;
        ok();
    }

    /**
     * Obtiene el indice del jugador que inicio el combate
     *
     * @return 0 para jugador 1, 1 para jugador 2
     */
    public int getInitialPickerIndex() {
        ok();
        return initialPicker - 1;
    }

    /**
     * Obtiene el turno inicial
     *
     * @return 0 para jugador 1, 1 para jugador 2
     */
    public int getInitialTurn() {
        ok();
        return turn - 1;
    }

    /**
     * Reinicia el turno que se obtuvo al principio, cuando se lanzo la moneda
     */
    public void resetTurnToInitial() {
        this.turn = initialPicker;
        ok();
    }


    //Pokemones

    /**
     * Obtiene el nombre del pokemon mediante su key
     *
     * @param key el nombre en mayuscula del pokemon
     * @return String del nombre del pokemon
     */
    public String getDisplayName(String key) {
        try {
            PoobKemon p = createPokemon(key);
            ok();
            return p.getName();
        } catch (POOBKemonException e) {
            return "";
        }
    }

    /**
     * Obtiene el tipo de pokemon de 18 tipos
     *
     * @param key el nombre en mayuscula del pokemon
     * @return String del typo del pokemon
     */
    public String getTypeName(String key) {
        try {
            PoobKemon p = createPokemon(key);
            ok();
            return p.getTypePoobKemon().toString();
        } catch (POOBKemonException e) {
            return "";
        }
    }

    /**
     * Obtiene el gift del pokemon
     *
     * @param key el nombre en mayuscula del pokemon
     * @return String del gift del pokemon
     */

    public String getResourceName(String key) {
        try {
            PoobKemon p = createPokemon(key);
            ok();
            return p.getResourceName();
        } catch (POOBKemonException e) {
            return "";
        }
    }

    /**
     * Obtiene la descripcion de cada pokemon
     *
     * @param key el nombre en mayuscula del pokemon
     * @return String de la descripcion del pokemon
     */
    public String getDescription(String key) {
        try {
            PoobKemon p = createPokemon(key);
            ok();
            return p.getDescription();
        } catch (POOBKemonException e) {
            return "";
        }
    }

    /**
     * Obtiene las stast de cada pokemon
     *
     * @param key el nombre en mayuscula del pokemon
     * @return String de las stats del pokemon
     */
    public String getStats(String key) {
        try {
            PoobKemon p = createPokemon(key);
            return String.format(
                    "PS: %d\n" +
                            "Ataque: %d\n" +
                            "Defensa: %d\n" +
                            "Velocidad: %d\n" +
                            "Atq. Esp.: %d\n" +
                            "Def. Esp.: %d",
                    p.getPpMax(), p.getMove(), p.getDefense(), p.getVelocity(),
                    p.getSpecialAttack(), p.getSpecialDefence()
            );
        } catch (POOBKemonException e) {
            return "";
        }
    }


    /**
     * Obtiene el nombre del pokemon actual para el entrenador especificado.
     * @param idx el indice del pokemon elegido
     * @return String nombre del pokemon si esta el pokemon false si no
     */
    public String getPokemonName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        ok();
        return (p != null) ? p.getName() : "";
    }


    /**
     * Obtiene la imagen del frente del pokemon
     * @param idx el indice del pokemon elegido
     * @return String nombre del gift si esta false si no
     */
    public String getBackImageName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        if (p == null) return null;
        ok();
        return p.getResourceName().replace(".gif", "Espalda.png");
    }

    /**
     * Obtiene la imagen del a espaldas del pokemon
     * @param idx el indice del pokemon elegido
     * @return String nombre del gift si esta false si no
     */
    public String getFrontImageName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        if (p == null) return null;
        ok();
        return p.getResourceName().replace(".gif", "Frente.png");
    }


    /**
     * Obtiene los puntos de salud al maximo del pokemon PS
     * @param idx el indice del pokemon elegido
     * @return int la vida del pokemon si no es null
     */
    public int getPokemonMaxHp(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        ok();
        return (p != null) ? p.getPpMax() : 0;
    }

    /**
     * Obtiene los puntos de salud del pokemon PS en combate
     * @param idx el indice del pokemon elegido
     * @return int la vida del pokemon si no es null
     */
    public int getPokemonCurrentHp(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        ok();
        return (p != null) ? p.getPpCurrent() : 0;
    }



    //Combate

    /**
     * Verifica el numero de pokemones elegidos por cada entrenador no sean mas de 6 en esta entrega 1
     * @param key el nombre en mayuscula del pokemon
     * @return true si ya eligio sus pokemones false si no
     */

    public boolean selectPokemon(String key) {
        try {
            PoobKemon p = createPokemon(key);
            Trainer t = getCurrentTrainer();
            if (t.getTeam().size() >= 6) return false;
            t.addPoobKemon(p);
            ok();
            return true;
        } catch (POOBKemonException e) {
            return false;
        }
    }


    /**
     * Revisa si ambos entrenadores ya elijeron sus pokemones
     * @return true si ya ambos elijeron false si no
     */
    public boolean readyToCombat() {
        ok();
        return hasSelected(0) && hasSelected(1);
    }



    //Movimientos

    /**
     * Obtiene la cantidad de movimientos disponibles para el pokemon actual del jugador en turno
     * @return int de movimientos disponibles, o 0 si no hay pokemon activo
     */
    public int getMoveCount() {
        PoobKemon p = getCurrentPokemon(turn - 1);
        ok();
        return p == null ? 0 : p.getMovements().size();
    }

    /**
     * Obtiene el nombre del movimiento especifico del pokemon en combate
     * @param idx indice del movimineto
     * @return String del nombre del movimiento si no es null
     */
    public String getMoveName(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        ok();
        return (p == null || idx < 0 || idx >= p.getMovements().size()) ? ""
                : p.getMovements().get(idx).getName();
    }

    /**
     * Obtiene los puntos de poder del movimiento en combate
     * @param idx indice del movimineto
     * @return int PP
     */
    public int getMovePPCurrent(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        ok();
        return (p == null || idx < 0 || idx >= p.getMovements().size()) ? 0
                : p.getMovements().get(idx).getCurrentPowerPoints();
    }

    /**
     * Obtiene los puntos de poder maximos del movimiento
     * @param idx indice del movimineto
     * @return int PP
     */
    public int getMovePPMax(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        ok();
        return (p == null || idx < 0 || idx >= p.getMovements().size())
                ? 0
                : p.getMovements().get(idx).getMaxPowerPoints();
    }


    /**
     * Obtiene el tipo de  movimiento de los 18
     * @param idx indice del movimiento
     * @return el tipo de movimiento
     */
    public TypePoobKemon getMoveType(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        ok();
        return (p == null || idx < 0 || idx >= p.getMovements().size())
                ? TypePoobKemon.NORMAL
                : p.getMovements().get(idx).getMoveType();
    }

    /**
     * Ejecuta un movimiento del pokemon atacante contra el pokemon defensor
     * @param idx indice del movimiento
     * @return true si el movimiento se ejecutó y gasto PP si no false
     */
    public boolean useMove(int idx) {
        try {
            doUseMove(idx);
            return true;
        } catch (POOBKemonException ex) {
            log(ex.getMessage());
            return false;
        }
    }

    /**
     * Ejecuta un movimiento del pokemon atacante contra el pokemon defensor
     * @param idx indice del movimiento
     * @throws POOBKemonException NO_ACTIVE_POKEMON, INVALID_MOVE_INDEX, NO_PP_LEFT
     */
    private void doUseMove(int idx) throws POOBKemonException {
        PoobKemon attacker = getCurrentPokemon(turn - 1);
        PoobKemon defender = getCurrentPokemon(turn % 2);
        if (attacker == null || defender == null) {
            throw new POOBKemonException(POOBKemonException.NO_ACTIVE_POKEMON);
        }
        List<MovePoobKemon> movs = attacker.getMovements();
        if (idx < 0 || idx >= movs.size()) {
            throw new POOBKemonException(POOBKemonException.INVALID_MOVE_INDEX);
        }
        MovePoobKemon mov = movs.get(idx);
        if (mov.getCurrentPowerPoints() <= 0) {
            String msg = String.format(
                    POOBKemonException.NO_PP_LEFT,
                    attacker.getName(),
                    mov.getName()
            );
            throw new POOBKemonException(msg);
        }

        MovePoobKemon.setCurrentFight(this);
        mov.run(attacker, defender);
        MovePoobKemon.clearCurrentFight();
        ok();
    }



    /**
     * Fuerza que el Pokemon activo use forcejeo
     */
    public boolean useStruggle() {
        PoobKemon attacker = getCurrentPokemon(turn - 1);
        PoobKemon defender = getCurrentPokemon(turn % 2);
        if (attacker == null|| defender == null) return false;
        MovePoobKemon.setCurrentFight(this);
        new Forcejeo().run(attacker, defender);
        MovePoobKemon.clearCurrentFight();
        ok();
        return true;
    }


    //Items
    /**
     * Permite usar las pociones y el revivir
     * @param item el objeto a usar sobre el pokemon
     * @return true si lo uso si no false
     */
    public boolean useItem(String item) {
        try {
            doUseItem(item);
            return true;
        } catch (POOBKemonException ex) {
            log(ex.getMessage());
            return false;
        }
    }


    /**
     * Permite usar las pociones y el revivir
     * @param item el objeto a usar sobre el pokemon
     * @return true si lo uso si no false
     */
    private void doUseItem(String item) throws POOBKemonException {
        Trainer t = getCurrentTrainer();

        if (t.getUsedItemCount() >= 2) {
            throw new POOBKemonException(POOBKemonException.MAX_ITEMS_USED);
        }

        int avail = getItemCount(item);
        if (avail <= 0) {
            throw new POOBKemonException(String.format(POOBKemonException.NO_ITEMS_LEFT, item));
        }

        if ("REVIVIR".equalsIgnoreCase(item)) {
            PoobKemon active = getCurrentPokemon(turn - 1);
            if (active.getPpCurrent() > 0) {
                throw new POOBKemonException(POOBKemonException.REVIVE_ONLY_IF_KO);
            }
        }

        boolean applied = t.useItem(item);
        if (!applied) {
            throw new POOBKemonException(String.format(POOBKemonException.ITEM_USE_FAILED, item));
        }

        log(String.format("%s usó %s sobre %s.",
                t.getName(),
                item,
                getCurrentPokemon(turn - 1).getName()));
        ok();
    }



    /**
     * Devuelve la cantidad actual del item para el entrenador en turno.
     */
    public int getItemCount(String item) {
        Trainer t = getCurrentTrainer();
        ok();
        switch(item.toUpperCase()) {
            case "POCION":      return t.getPotions();
            case "SUPERPOCION": return t.getSuperPotions();
            case "HIPERPOCION": return t.getHyperPotions();
            case "REVIVIR":     return t.getRevives();
            default:            return 0;
        }
    }


    /**
     * Obtiene el uso de un item
     * @return int el numero de usus del item
     */
    public int getUsedItemCount() {
        ok();
        return getCurrentTrainer().getUsedItemCount();
    }


    /**
     * El ultimo item usado
     * @param item pociones y revivir
     */
    public void setLastPendingItem(String item) {
        this.lastPendingItem = item;
    }

    /**
     * Recuperamos el item que se uso
     * @return el item
     */
    public String getLastPendingItem() {
        return lastPendingItem;
    }



    private List<String> combatLog = new ArrayList<>();


    /**
     * Añade un mensaje al registro del combate
     * @param msg a registrar
     */
    public void log(String msg) {
        ok();
        combatLog.add(msg);
    }

    /**
     * Obtiene y limpia el registro de mensajes del combate
     * @return Una lista de los mensajes acumulados
     */
    public List<String> drainLog() {
        List<String> tmp = new ArrayList<>(combatLog);
        combatLog.clear();
        ok();
        return tmp;
    }

    /**
     * Penaliza al jugador que no uso un movimiento en su turno a tiempo
     */
    public void penalizeNoAction() {
        Trainer t = getCurrentTrainer();
        PoobKemon active = getCurrentPokemon(turn - 1);
        if (active == null) return;

        for (MovePoobKemon mov : active.getMovements()) {
            if (mov.getMoveType() != TypePoobKemon.NORMAL && mov.getCurrentPowerPoints() > 0) {
                mov.setCurrentPowerPoints(mov.getCurrentPowerPoints() - 1);
            }
        }
        log(String.format(
                "%s no jugó en tiempo y cada movimiento especial perdió 1 PP.",
                t.getName()
        ));
        ok();
    }


    //Salvar y guardar la partida
    /**
     * Serializa el estado completo de la partida a un archivo
     */
    public void saveToFile(String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(this);
        }
    }

    /**
     * Carga un estado de juego previamente guardado
     */
    public static PoobKemonFight loadFromFile(String fileName)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (PoobKemonFight) in.readObject();
        }
    }


    /**
     * Se invoca automáticamente al deserializar el objeto.
     */
    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        registry = new HashMap<>();
        initRegistry();
        hpMap = new HashMap<>();
    }

    /**
     * Obtiene el estado de juego
     * @return
     */
    public UIState getUiState() {
        ok();
        return uiState;
    }


    /**
     * Da el estado actual del juego
     * @param uiState
     */
    public void setUiState(UIState uiState) {
        this.uiState = uiState;
        ok();
    }

    /**
     * Verifica que los metodos sean validos
     * @return true si no falla si no es false porque falla
     */
    public boolean ok() {
        return isOk;
    }

}

