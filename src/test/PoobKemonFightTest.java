package test;

import static org.junit.Assert.*;

import domain.PoobKemon;
import org.junit.Before;
import org.junit.Test;

import domain.PoobKemonFight;
import domain.Trainer;
import java.util.List;


public class PoobKemonFightTest {

    //Name
    private Trainer t1;
    private Trainer t2;
    private PoobKemonFight fight;

    @Before
    public void setUp() {
        t1 = new Trainer("Ash", "azul");
        t2 = new Trainer("Serena", "rojo");
        fight = new PoobKemonFight(t1, t2, 1);
    }

    @Test
    public void shouldInitializeTrainersAndTurn() {
        assertSame(t1, fight.getTrainer1());
        assertSame(t2, fight.getTrainer2());
        assertEquals("El initial picker index debe ser 0", 0, fight.getInitialPickerIndex());
        assertEquals("El turno que inicia debe ser 0", 0, fight.getInitialTurn());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldReplaceTrainerCorrectly() {
        Trainer nuevo1 = new Trainer("Robinson", "azul");
        Trainer nuevo2 = new Trainer("Sebastian", "rojo");
        fight.addTrainer(nuevo2, 2);
        assertSame(nuevo2, fight.getTrainer2());
        fight.addTrainer(nuevo1, 1);
        assertSame(nuevo1, fight.getTrainer1());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldAlternateTurns() {
        Trainer t1 = new Trainer("Robinson", "Rojo");
        Trainer t2 = new Trainer("Sebastian", "Azul");
        PoobKemonFight fight = new PoobKemonFight(t1, t2, 1);
        assertSame("Inicialmente debe ser Robinson", t1, fight.getCurrentTrainer());
        fight.nextTurn();
        assertSame("El siguiente turno debe ser Sebastian", t2, fight.getCurrentTrainer());
        fight.nextTurn();
        assertSame("El siguiente turno vuelve a ser Robinson", t1, fight.getCurrentTrainer());
        assertEquals(true, fight.ok());
    }

    //Selector


    @Test
    public void shouldAllowEachTrainerToSelectOnePokemon() {
        Trainer t1 = new Trainer("Robinson", "Rojo");
        Trainer t2 = new Trainer("Sebastian", "Azul");
        PoobKemonFight fight = new PoobKemonFight(t1, t2, 0);

        assertTrue("Robinson debe poder seleccionar GENGAR", fight.selectPokemon("GENGAR"));
        assertTrue("Robinson debe haber seleccionado", fight.hasSelected(0));
        fight.nextTurn();
        assertTrue("Sebastian debe poder seleccionar RAICHU", fight.selectPokemon("RAICHU"));
        assertTrue("Sebastian debe haber seleccionado", fight.hasSelected(1));
        assertTrue("Ambos jugadores han seleccionado, debe estar listo para combatir", fight.readyToCombat());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldNotSelectInvalidPokemonKey() {
        assertFalse("Clave invalida no debe seleccionarse", fight.selectPokemon("INVALIDO"));
        assertFalse(fight.hasSelected(0));
        assertTrue(fight.ok());
    }

    @Test
    public void shouldRemovePokemonCorrectly() {
        fight.selectPokemon("GENGAR");
        assertTrue(fight.hasSelected(0));
        assertTrue(fight.removePokemon(t1, fight.getCurrentPokemon(0)));
        assertFalse("Tras eliminar, no debe haber seleccionado", fight.hasSelected(0));
        assertTrue(fight.ok());
    }

    @Test
    public void shouldReportReadyToCombatOnlyWhenBothSelected() {
        assertFalse(fight.readyToCombat());
        fight.selectPokemon("GENGAR");
        assertFalse(fight.readyToCombat());
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        assertTrue(fight.readyToCombat());
        assertTrue(fight.ok());

    }

    //Combate
    @Test
    public void shouldTrackItemUsageAndPreventMoreThanTwo() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        assertTrue(fight.useItem("POCION"));
        assertEquals(1, fight.getUsedItemCount());
        assertTrue(fight.useItem("SUPERPOCION"));
        assertEquals(2, fight.getUsedItemCount());
        assertFalse("No debe dejar usar tercer ítem", fight.useItem("HIPERPOCION"));
        assertTrue(fight.ok());
    }

    @Test
    public void shouldDecreasePotionCountOnUse() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        int initial = fight.getItemCount("POCION");
        assertTrue(fight.useItem("POCION"));
        assertEquals(initial - 1, fight.getItemCount("POCION"));
        assertTrue(fight.ok());
    }


    @Test
    public void shouldNotAllowReviveWhenHpIsAboveZero() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        int revivesBefore = fight.getItemCount("REVIVIR");
        assertFalse("No debe permitir Revivir con PS > 0", fight.useItem("REVIVIR"));
        assertEquals("Contador de Revivir no debe cambiar", revivesBefore, fight.getItemCount("REVIVIR"));
        assertTrue(fight.ok());
    }

    @Test
    public void shouldNotDecreaseReviveCountWhenReviveFails() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        int usedBefore = fight.getUsedItemCount();
        assertFalse(fight.useItem("REVIVIR"));
        assertEquals("No debe incrementar usedItemCount aunque revivir falle",
                usedBefore , fight.getUsedItemCount());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldDealDamageWhenUsingMove() {
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.nextTurn();

        int hpBefore = fight.getPokemonCurrentHp(1);
        assertTrue("Debe permitir usar movimiento valido", fight.useMove(0));
        int hpAfter = fight.getPokemonCurrentHp(1);
        assertTrue("El Pokémon defensor debe perder PS", hpAfter < hpBefore);
        assertTrue(fight.ok());
    }

    @Test
    public void shouldNotAllowMoveWhenPPIsZero() {
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.nextTurn();

        int maxPP = fight.getMovePPMax(0);
        for (int i = 0; i < maxPP; i++) {
            assertTrue(fight.useMove(0));
            fight.drainLog();
            fight.nextTurn();
            fight.useStruggle();
            fight.drainLog();
            fight.nextTurn();
        }
        assertEquals("PP del movimiento debe ser 0", 0, fight.getMovePPCurrent(0));
        assertFalse("No debe permitir usar movimiento con PP en 0", fight.useMove(0));
        assertTrue(fight.ok());
    }


    @Test
    public void shouldUseStruggleWhenNoPPAvailable() {
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.nextTurn();

        int totalMoves = fight.getMoveCount();
        for (int i = 0; i < totalMoves; i++) {
            int pp = fight.getMovePPCurrent(i);
            for (int j = 0; j < pp; j++) {
                fight.useMove(i);
                fight.drainLog();
                fight.nextTurn();
                fight.useStruggle();
                fight.drainLog();
                fight.nextTurn();
            }
        }

        int hpBefore = fight.getPokemonCurrentHp(1);
        assertTrue("Debe permitir usar Struggle si no hay PP", fight.useStruggle());
        assertTrue(fight.ok());
    }


    @Test
    public void shouldLogCombatEvents() {
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.nextTurn();

        fight.useMove(0);
        List<String> log = fight.drainLog();
        assertFalse("El log debe tener eventos luego de ataque", log.isEmpty());
        assertTrue("Debe mencionar el movimiento usado", log.get(0).contains("usó"));
        assertTrue(fight.ok());
    }

    @Test
    public void shouldSwitchTurnsAfterMove() {
        fight.selectPokemon("RAICHU");
        fight.nextTurn();
        fight.selectPokemon("GENGAR");
        fight.nextTurn();

        int initial = fight.getInitialTurn();
        fight.useMove(0);
        fight.nextTurn();
        assertEquals("Debe alternar turno", 1 - initial, fight.getInitialTurn());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldUseStruggleWhenAllMovesAreDepleted() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        int moveCount = fight.getMoveCount();
        for (int i = 0; i < moveCount; i++) {
            while (fight.getMovePPCurrent(i) > 0) {
                assertTrue("Debe poder usar el movimiento hasta agotar PP", fight.useMove(i));
                fight.drainLog();
            }
            assertFalse("No debe dejar usar el movimiento sin PP", fight.useMove(i));
        }

        assertTrue("Con todos los PP agotados, debe poder usar Struggle", fight.useStruggle());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldAdvanceTurnAfterUsingItem() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        int beforeTurn = fight.getInitialTurn();
        assertTrue(fight.useItem("POCION"));
        fight.nextTurn();
        assertNotEquals("El turno debe haber cambiado tras usar un item",
                beforeTurn, fight.getInitialTurn());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldDetectTrainerDefeatedWhenAllPokemonsWeakened() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        PoobKemon attacker = fight.getCurrentPokemon(0);
        PoobKemon defender = fight.getCurrentPokemon(1);
        while (!defender.isWeakened()) {
            attacker.getMovements().get(0).run(attacker, defender);
        }
        assertTrue("Entrenador 2 debe estar derrotado", fight.getTrainer2().isDefeated());
        assertTrue(fight.ok());
    }

    @Test
    public void shouldResetTeamsAndItemCountOnResetSelection() {
        fight.selectPokemon("GENGAR");
        fight.nextTurn();
        fight.selectPokemon("RAICHU");
        fight.nextTurn();

        fight.useItem("POCION");
        fight.useItem("SUPERPOCION");
        assertEquals(2, fight.getUsedItemCount());

        fight.resetSelection();
        assertFalse(fight.hasSelected(0));
        assertFalse(fight.hasSelected(1));
        assertEquals(0, fight.getUsedItemCount());
        assertTrue(fight.ok());
    }

}
