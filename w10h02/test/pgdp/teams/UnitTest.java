package pgdp.teams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
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
        
        assertEquals(Set.of(glen, fatjon, jani, koco), attackers);
        assertEquals(0, defenders.size());
        assertEquals(0, supporters.size());
    }

    @Test
    @DisplayName("computeScore() near Integer.MAX_VALUE")
    public void scoresNearOverflow() {
        var lineup = new Lineup(Set.of(marcel), Set.of(hansuwe), new HashSet<>());

        assertEquals(2_000_000_000, lineup.getTeamSynergy(), "Wrong Synergy near Integer.MAX_VALUE");
        assertEquals(147_483_647, lineup.getTeamSkill(), "Wrong TeamSkill near Integer.MAX_VALUE");
        assertEquals(Integer.MAX_VALUE, lineup.getTeamScore(), "Wrong TeamScore near Integer.MAX_VALUE");
    }

    @Test
    @DisplayName("computeScore() near Integer.MIN_VALUE")
    public void scoresNearUnderflow() {
        var lineup = new Lineup(new HashSet<>(), Set.of(hansuwe), Set.of(thorsten));

        assertEquals(-2_000_000_000, lineup.getTeamSynergy(), "Wrong Synergy near Integer.MIN_VALUE");
        assertEquals(-147483648, lineup.getTeamSkill(), "Wrong TeamSkill near Integer.MIN_VALUE");
        assertEquals(Integer.MIN_VALUE, lineup.getTeamScore(), "Wrong TeamScore near Integer.MIN_VALUE");
    }

    @Test
    @DisplayName("computeScore() double synergy near Integer.MAX_VALUE")
    public void doubleSynergyNearOverflow() {
        var lineup = new Lineup(new HashSet<>(), Set.of(marcel, thorsten), new HashSet<>());

        assertEquals(2_000_000_000, lineup.getTeamSynergy(), "Wrong Synergy near Integer.MAX_VALUE in same team");
        assertEquals(147483647, lineup.getTeamSkill(), "Wrong TeamSkill near Integer.MAX_VALUE in same team");
        assertEquals(Integer.MAX_VALUE, lineup.getTeamScore(), "Wrong TeamScore near Integer.MAX_VALUE in same team");
    }
}
