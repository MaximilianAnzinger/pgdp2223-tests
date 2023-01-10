package pgdp.teams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import static pgdp.teams.Lib.*;

import org.junit.jupiter.api.Disabled; // do not remove
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

public class ArtemisTest {
    @Test
    @DisplayName("computeScores: kleines Beispiel")
    public void computeScoresSmall() {
        var lineup = new Lineup(Set.of(jonas, anatoly), Set.of(julian), Set.of(simon));

        assertEquals(65, lineup.getTeamScore());
        assertEquals(40, lineup.getTeamSkill());
        assertEquals(25, lineup.getTeamSynergy());
    }

    @Test
    @DisplayName("computeScores: großes Beispiel")
    public void computeScoresLarge() {
        var lineup = new Lineup(Set.of(eve, hanna), Set.of(sachmi, jasmine), Set.of(enrico, jakob));

        assertEquals(34505, lineup.getTeamScore());
        assertEquals(34014, lineup.getTeamSkill());
        assertEquals(491, lineup.getTeamSynergy());
    }

    @Test
    @DisplayName("computeOptimalLineup: kleines Beispiel")
    public void computeOptimalLineupSmall() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(eric, nils, felix, thomas), 2, 1, 1);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(Set.of(nils, eric), attackers);
        assertEquals(Set.of(felix), defenders);
        assertEquals(Set.of(thomas), supporters);
    }

    @Test
    @DisplayName("computeOptimalLineup: großes Beispiel [Die Laufzeit ist für diese Aufgabe nicht besonders wichtig. Solange das große Beispiel für computeOptimalLineup nicht timeoutet bist du auf der sicheren Seite. Lokal solltest du je nach PC für das Beispiel nicht viel mehr als 30 Sekunden benötigen.]")
    // @Disabled // Comment this line in case you don't want to test this function.
    public void computeOptimalLineupLarge() throws NoSuchFieldException, IllegalAccessException {
        System.err.println("You are currently running `computeOptimalLineupLarge()` which can be CPU intensive.");
        long timeBefore = System.currentTimeMillis();

        var lineup = Lineup.computeOptimalLineup(
                Set.of(jan, georg, anton, johannes, konrad, max, oliver, robin, laura, lukas), 2, 3, 5);

        long timeAfter = System.currentTimeMillis();
        long elapsedTime = timeAfter - timeBefore;

        System.out.printf("Test `computeOptimalLineupLarge()` took %sms to execute.\n", elapsedTime);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(Set.of(max, georg), attackers);
        assertEquals(Set.of(jan, anton, konrad), defenders);
        assertEquals(Set.of(lukas, laura, johannes, oliver, robin), supporters);
    }

    @Test
    @DisplayName("This test will always glow green to brighten up your mood. ♡(◕ε◕❀)")
    public void greenTest() {
        assertTrue(true);
    }
}
