package domain;

import java.util.ArrayList;
import java.util.List;

public class Trainer {
    private String name;
    private String colorTeam;
    private List<PoobKemon> team;
    private int activeIndex;
    private int potions;
    private int superpotions;
    private int hyperpotions;
    private int revive;

    public Trainer(String name, String colorTeam) {
        this.name = name;
        this.colorTeam = colorTeam;
        this.team = new ArrayList<>();
        this.activeIndex = 0;
        this.potions = 0;
        this.superpotions = 0;
        this.hyperpotions = 0;
        this.revive = 0;
    }

    public String getName() {
        return name;
    }

    public String getColorTeam() {
        return colorTeam;
    }

    public List<PoobKemon> getTeam() {
        return team;
    }

    public PoobKemon getPokemonactive() {
        return team.isEmpty() ? null : team.get(activeIndex);
    }

    public void addPoobKemon(PoobKemon p) {
        if (team.size() < 6) {
            team.add(p);
        }
    }

    public boolean changePoobKemon(int nuevoIndice) {
        if (nuevoIndice >= 0 && nuevoIndice < team.size()
                && !team.get(nuevoIndice).isWeakened()) {
            activeIndex = nuevoIndice;
            return true;
        }
        return false;
    }

    public boolean isDefeated() {
        for (PoobKemon p : team) {
            if (!p.isWeakened()) return false;
        }
        return true;
    }

    public boolean useItem(String item) {
        PoobKemon active = getPokemonactive();
        if (active == null) return false;

        switch (item.toUpperCase()) {
            case "POCION":
                if (potions > 0) {
                    active.cure(20);
                    potions--;
                    return true;
                }
                break;
            case "SUPERPOCION":
                if (superpotions > 0) {
                    active.cure(50);
                    superpotions--;
                    return true;
                }
                break;
            case "HIPERPOCION":
                if (hyperpotions > 0) {
                    active.cure(200);
                    hyperpotions--;
                    return true;
                }
                break;
            case "REVIVIR":
                if (revive > 0 && active.isWeakened()) {
                    active.cureComplete();
                    revive--;
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean addItem(String item) {
        switch (item.toUpperCase()) {
            case "POCION":
                if (potions < 2) {
                    potions++;
                    return true;
                }
                break;
            case "SUPERPOCION":
                if (superpotions < 2) {
                    superpotions++;
                    return true;
                }
                break;
            case "HIPERPOCION":
                if (hyperpotions < 2) {
                    hyperpotions++;
                    return true;
                }
                break;
            case "REVIVIR":
                if (revive < 1) {
                    revive++;
                    return true;
                }
                break;
        }
        return false;
    }
}