package pgdp.teams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import static pgdp.teams.Lib.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnitTest {
    @Test
    @DisplayName("Fähigkeits- und Synergiewerte sind im Bereich [Integer.MINVALUE, Integer.MAXVALUE] möglich.")
    public void negativeSynergyTest() {
        var lineup = new Lineup(Set.of(zeynep), Set.of(faid), Set.of());

        assertEquals(-40, lineup.getTeamScore());
        assertEquals(10, lineup.getTeamSkill());
        assertEquals(-50, lineup.getTeamSynergy());
    }

    @Test
    @DisplayName("Die Parameter numberXXX der Methode computeOptimalLineup sind alle >= 0")
    public void optimalLineupZero() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(cedric, jakov, yassine), 0, 0, 0);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(0, attackers.size());
        assertEquals(0, defenders.size());
        assertEquals(0, supporters.size());
    }

    @Test
    @DisplayName("Die Summe der Parameter numberXXX der Methode computeOptimalLineup ist <= der Anzahl der übergebenen Penguins (players.size())")
    public void optimalLineupAllAttackers() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(glen, fatjon, jani, koco), 4, 0, 0);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(4, attackers.size());
        assertEquals(0, defenders.size());
        assertEquals(0, supporters.size());

        assertTrue(attackers.contains(glen));
        assertTrue(attackers.contains(fatjon));
        assertTrue(attackers.contains(jani));
        assertTrue(attackers.contains(koco));
    }
}
