package test;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la clase AITrainer.
 */
public class AITrainerTest {
    private PoobKemonFight fight;
    private AITrainer aiDefensive;
    private AITrainer aiChanging;
    private Trainer human;

    @Before
    public void setUp() {
        human = new Trainer("Albarracin", "azul");

        aiDefensive = new AITrainer(
                "Attacking Trainer",
                "rojo",
                new DefensiveStrategy(),
                "DEFENSIVE"
        );

        aiChanging = new AITrainer(
                "Changing Trainer",
                "azul",
                new ChangingStrategy(),
                "CHANGING"
        );

        fight = new PoobKemonFight(human, aiDefensive, 1);
        fight.nextTurn();
    }

    @Test
    public void shouldAutoSelectTeamPutsSixPokemonsInTrainerTeam() throws POOBKemonException {
        List<String> available = fight.getPokemonKeys();
        assertTrue(available.size() >= 6);
        aiDefensive.autoSelectTeam(fight);
        List<PoobKemon> team = aiDefensive.getTeam();
        assertEquals( 6, team.size());

        for (PoobKemon p : team) {
            assertNotNull(p);
            assertFalse(p.getName().trim().isEmpty());
        }
        assertTrue(fight.ok());
    }

    @Test
    public void shooulAutoChooseMoveReturnsValidIndex() throws POOBKemonException {
        aiDefensive.autoSelectTeam(fight);
        int idx = aiDefensive.autoChooseMove(fight);
        int moveCount = fight.getCurrentPokemon(1).getMovements().size();
        assertTrue(idx >= 0 && idx < moveCount);
        assertTrue(fight.ok());
    }

    @Test
    public void shouldAutoChooseSwitchKeepsActiveWhenNotWeakened() throws POOBKemonException {
        fight = new PoobKemonFight(human, aiDefensive, 1);
        fight.nextTurn();
        aiDefensive.autoSelectTeam(fight);
        int active = aiDefensive.getActiveIndex();
        int choice = aiDefensive.autoChooseSwitch(fight);
        assertEquals(active, choice);
        assertTrue(fight.ok());
    }

    @Test
    public void shouldAutoChooseSwitchChangesOnKO() throws POOBKemonException {
        fight = new PoobKemonFight(human, aiDefensive, 1);
        fight.nextTurn();
        fight.selectPokemon(fight.getPokemonKeys().get(0));
        fight.selectPokemon(fight.getPokemonKeys().get(1));
        PoobKemon first = aiDefensive.getTeam().get(0);
        first.takeDamage(first.getPpMax());
        assertTrue(first.isWeakened());
        int switchIdx = aiDefensive.autoChooseSwitch(fight);
        assertNotEquals("Se deberia cambiar el pokemon si esta KO", 0, switchIdx);
        assertFalse(aiDefensive.getTeam().get(switchIdx).isWeakened());
        assertTrue(fight.ok());
    }

}
