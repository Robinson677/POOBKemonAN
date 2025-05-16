package test;

import static org.junit.Assert.*;

import domain.SurvivalPoobKemonFight;
import domain.Trainer;
import domain.PoobKemon;
import domain.POOBKemonException;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SurvivalPoobKemonTest {

    private Trainer t1;
    private Trainer t2;
    private SurvivalPoobKemonFight fight;

    @Before
    public void setUp() throws POOBKemonException {
        t1 = new Trainer("Albarracin", "azul");
        t2 = new Trainer("Nuñez", "rojo");
        fight = new SurvivalPoobKemonFight(t1, t2, 0);
    }

    @Test
    public void shouldNotAllowMoreThanSixPokemons() {
        List<PoobKemon> team1 = fight.getTrainer1().getTeam();
        List<PoobKemon> team2 = fight.getTrainer2().getTeam();
        assertEquals( 6, team1.size());
        assertEquals(6, team2.size());
        assertFalse("No deberia dejar que un jugador elija manualmente un pokemon",
                fight.selectPokemon("GENGAR")
        );
        assertTrue(fight.ok());
    }

    @Test
    public void shouldSurvivalModeShouldStartWithNoItems() {
        assertEquals(0, fight.getItemCount("POCION"));
        assertEquals(0, fight.getItemCount("SUPERPOCION"));
        assertEquals( 0, fight.getItemCount("REVIVIR"));
        assertTrue(fight.ok());
    }

    @Test
    public void shoulNotSurvivalModeUseItems() {
        assertFalse( fight.useItem("POCION"));
        assertFalse(fight.useItem("REVIVIR"));
        assertTrue(fight.ok());
    }


    @Test
    public void shouldHaveUniqueAndDisjointTeams() {
        List<PoobKemon> team1 = fight.getTrainer1().getTeam();
        List<PoobKemon> team2 = fight.getTrainer2().getTeam();

        Set<String> names1 = team1.stream()
                .map(PoobKemon::getName)
                .collect(Collectors.toSet());
        Set<String> names2 = team2.stream()
                .map(PoobKemon::getName)
                .collect(Collectors.toSet());

        assertEquals("Trainer1 debe tener 6 nombres únicos", 6, names1.size());
        assertEquals("Trainer2 debe tener 6 nombres únicos", 6, names2.size());


        for (String name : names1) {
            assertFalse(
                    "No debe haber solapamiento entre equipos",
                    names2.contains(name)
            );
        }
    }

    /**
     * En supervivencia no hay fase de selección: readyToCombat() debe ser true desde el inicio.
     */
    @Test
    public void shouldBeReadyToCombatImmediately() {
        assertTrue(
                "El Modo Supervivencia debe estar listo para combate de una vez",
                fight.readyToCombat()
        );
        assertTrue(fight.ok());
    }
}

