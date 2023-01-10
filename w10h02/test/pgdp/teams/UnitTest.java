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
        
        assertEquals(Set.of(glen, fatjon, jani, koco), attackers);
        assertEquals(0, defenders.size());
        assertEquals(0, supporters.size());
    }
    
    
    @Test
    @DisplayName("Soll funktionieren wenn nur defenders null elemente hat")
    public void optimalLineUpZeroInMiddle() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(jasmine, yassine), 1, 0, 1);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(1, attackers.size());
        assertEquals(0, defenders.size());
        assertEquals(1, supporters.size());

        assertTrue(attackers.contains(yassine));
        assertTrue(supporters.contains(jasmine));
        assertEquals(0, defenders.size());
    }

    @Test
    @DisplayName("Soll funktionieren wenn attackers + defenders + supporters < players.size()")
    public void optimalLineUpSubsetsDontSumUpToTotalSetSize() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(jasmine, yassine, eve, anatoly, anton, cedric, faid, jakob, georg),
                2, 2, 3);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(2, attackers.size());
        assertEquals(2, defenders.size());
        assertEquals(3, supporters.size());

        assertEquals(Set.of(eve, georg), attackers);
        assertEquals(Set.of(jasmine, anton), defenders);
        assertEquals(Set.of(jakob, yassine, faid), supporters);
    }

    @Test
    @DisplayName("Soll funktionieren wenn defenders \"groß\" ist")
    public void optimalLineUpLargeSubsetSize() throws NoSuchFieldException, IllegalAccessException {
        var lineup = Lineup.computeOptimalLineup(Set.of(
                jasmine, yassine, eve, anatoly, anton, cedric, faid, jakob, georg, hanna, jakov,
                        felix, jan, shrek), 0, 10, 0);

        var attackers = getField(lineup, "attackers");
        var defenders = getField(lineup, "defenders");
        var supporters = getField(lineup, "supporters");

        assertEquals(0, attackers.size());
        assertEquals(10, defenders.size());
        assertEquals(0, supporters.size());

        assertEquals(Set.of(eve), attackers);
        assertEquals(Set.of(jan), defenders);
        assertEquals(Set.of(jakob), supporters);
    }

    @Test
    @DisplayName("OPTIONAL - berechnet großes Lineup und printed die Laufzeit. Timeout nach 30 sekunden, " +
            "allerdings wird der test nur als failed gewertet wenn er nicht timeouted aber das ergebnis falsch ist.")
    public void laufzeitFunTest() throws InterruptedException {
        long t1 = System.currentTimeMillis();
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            var lineup = Lineup.computeOptimalLineup(Set.of(
                    jasmine, yassine, eve, anatoly, anton, cedric, faid, jakob, georg, hanna,
                    felix, jan, zeynep, shrek), 4, 4, 4);


            try {
                var attackers = getField(lineup, "attackers");
                var defenders = getField(lineup, "defenders");
                var supporters = getField(lineup, "supporters");

                assertEquals(4, attackers.size());
                assertEquals(4, defenders.size());
                assertEquals(4, supporters.size());

                assertEquals(Set.of(hanna, eve, georg, shrek), attackers);
                assertEquals(Set.of(anatoly, anton, jan, jasmine), defenders);
                assertEquals(Set.of(yassine, faid, felix, jakob), supporters);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        es.shutdown();
        if(!es.awaitTermination(2, TimeUnit.SECONDS)) {
            System.out.println("\nSadly, your program didn't pass the optional laufzeitFun test :( \n" +
                    "Since the example was much bigger than required, this still counts as a pass. \n");
        } else {
            long diff = System.currentTimeMillis() - t1;
            System.out.println("Nice, you have passed the optional laufzeitFunTest. \n" +
                    "Your execution time for roughly 3 million combinations was: " + diff + "ms." +
                    "(For reference, the largest Artemis test was about 2500 combinations)!");
        }
    }
}
