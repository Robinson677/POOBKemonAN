package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase principal que maneja la selección y creacion de entrenadores y PoobKemons
 */
public class PoobKemonFight {
    private Trainer trainer1;
    private Trainer trainer2;
    private final int initialPicker;
    private int turn;
    private final Map<PoobKemon, Integer> hpMap = new HashMap<>();
    private Map<String, PoobKemonFactory> registry = new HashMap<>();
    private boolean ok;




    public PoobKemonFight() {
        initRegistry();
        this.initialPicker = 1;
        this.turn = initialPicker;
    }

    public PoobKemonFight(Trainer t1, Trainer t2, int firstPicker) {
        this.trainer1 = t1;
        this.trainer2 = t2;
        int p = (firstPicker == 2 ? 2 : 1);
        this.turn = p;
        this.initialPicker = p;
        initRegistry();
    }

    private void initRegistry() {
        registry.put("RAICHU", Raichu::new);
        registry.put("GENGAR", Gengar::new);
        isOk();
    }

    @FunctionalInterface
    private interface PoobKemonFactory {
        PoobKemon create();
    }

    public void addTrainer(Trainer trainer, int position) {
        if (position == 1) {
            this.trainer1 = trainer;
        } else if (position == 2) {
            this.trainer2 = trainer;
        } else {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    public Trainer getCurrentTrainer() {
        isOk();
        return (turn == 1) ? trainer1 : trainer2;
    }

    public void nextTurn() {
        turn = (turn == 1) ? 2 : 1;
        isOk();
    }

    public void resetTurnToInitial() {
        this.turn = initialPicker;
        isOk();
    }

    public int getInitialPickerIndex() {
        isOk();
        return initialPicker - 1;
    }

    public int getInitialTurn() {
        isOk();
        return turn - 1;
    }

    public String getTrainerName(int idx) {
        isOk();
        return (idx == 0) ? trainer1.getName() : trainer2.getName();
    }

    public String getTrainerTeam(int idx) {
        isOk();
        return (idx == 0) ? trainer1.getColorTeam() : trainer2.getColorTeam();
    }

    public List<String> getPokemonKeys() {
        isOk();
        return new ArrayList<>(registry.keySet());
    }


    public PoobKemon createPokemon(String key) {
        PoobKemonFactory f = registry.get(key);
        isOk();
        return (f != null) ? f.create() : null;
    }


    public String getDisplayName(String key) {
        PoobKemon p = createPokemon(key);
        isOk();
        return (p != null) ? p.getName() : "";
    }


    public String getTypeName(String key) {
        PoobKemon p = createPokemon(key);
        isOk();
        return (p != null) ? p.getTypePoobKemon().toString() : "";
    }


    public String getResourceName(String key) {
        PoobKemon p = createPokemon(key);
        isOk();
        return (p != null) ? p.getResourceName() : "";
    }


    public String getDescription(String key) {
        PoobKemon p = createPokemon(key);
        isOk();
        return (p != null) ? p.getDescription() : "";
    }


    public String getStats(String key) {
        PoobKemon p = createPokemon(key);
        if (p == null) return "";
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
    }


    public boolean hasSelected(int idx) {
        Trainer t = (idx == 0) ? trainer1 : trainer2;
        isOk();
        return t.getTeam().size() > 0;
    }


    public PoobKemon getCurrentPokemon(int idx) {
        Trainer t = (idx == 0) ? trainer1 : trainer2;
        isOk();
        return t.getTeam().isEmpty() ? null : t.getTeam().get(0);
    }



    public boolean removePokemon(Trainer t, PoobKemon p) {
        if (t != null && p != null) {
            isOk();
            return t.getTeam().remove(p);
        }
        return false;
    }


    public void resetSelection() {
        if (trainer1 != null) trainer1.getTeam().clear();
        if (trainer2 != null) trainer2.getTeam().clear();
        this.turn = 1;
        isOk();
    }


    public boolean selectPokemon(String key) {
        PoobKemon p = createPokemon(key);
        isOk();
        if (p == null) return false;

        Trainer t = getCurrentTrainer();
        isOk();
        if (t.getTeam().size() >= 1) return false;

        t.addPoobKemon(p);
        return true;
    }


    public boolean readyToCombat() {
        isOk();
        return hasSelected(0) && hasSelected(1);
    }

    public String getMiniGifName(int turn) {
        PoobKemon current = getCurrentPokemon(turn);
        if (current == null) return null;
        String name = current.getResourceName();
        isOk();
        return "mini" + name;
    }


    public String getPokemonName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        isOk();
        return (p != null) ? p.getName() : "";
    }


    public String getBackImageName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        if (p == null) return null;
        isOk();
        return p.getResourceName().replace(".gif", "Espalda.png");
    }


    public String getFrontImageName(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        if (p == null) return null;
        isOk();
        return p.getResourceName().replace(".gif", "Frente.png");
    }


    public int getPokemonCurrentHp(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        isOk();
        return (p != null) ? p.getPpCurrent() : 0;
    }

    public int getPokemonMaxHp(int idx) {
        PoobKemon p = getCurrentPokemon(idx);
        isOk();
        return (p != null) ? p.getPpMax() : 0;
    }

    public int getMoveCount() {
        PoobKemon p = getCurrentPokemon(turn - 1);
        isOk();
        return p == null ? 0 : p.getMovements().size();
    }


    public String getMoveName(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        isOk();
        return (p == null || idx < 0 || idx >= p.getMovements().size()) ? ""
                : p.getMovements().get(idx).getName();
    }

    public int getMovePPCurrent(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        isOk();
        return (p == null || idx < 0 || idx >= p.getMovements().size()) ? 0
                : p.getMovements().get(idx).getCurrentPowerPoints();
    }

    public int getMovePPMax(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        isOk();
        return (p == null || idx < 0 || idx >= p.getMovements().size())
                ? 0
                : p.getMovements().get(idx).getMaxPowerPoints();
    }


    public TypePoobKemon getMoveType(int idx) {
        PoobKemon p = getCurrentPokemon(turn - 1);
        isOk();
        return (p == null || idx < 0 || idx >= p.getMovements().size())
                ? TypePoobKemon.NORMAL
                : p.getMovements().get(idx).getMoveType();
    }

    public boolean useMove(int idx) {
        PoobKemon attacker = getCurrentPokemon(turn - 1);
        PoobKemon defender = getCurrentPokemon(turn % 2);
        isOk();
        if (attacker == null || defender == null) return false;

        List<MovePoobKemon> movs = attacker.getMovements();
        isOk();
        if (idx < 0 || idx >= movs.size()) return false;

        MovePoobKemon mov = movs.get(idx);
        mov.run(attacker, defender);
        isOk();
        return mov.getCurrentPowerPoints() < mov.getMaxPowerPoints();
    }

    public boolean isOk() {
        return ok;
    }

    public Trainer getTrainer1() { return trainer1; }
    public Trainer getTrainer2() { return trainer2; }
}

