package pgdp.trials;

import org.junit.jupiter.api.Test;

import pgdp.trials.TrialOfTheMountains.Mountain;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrialOfTheMountainsTest {

    private void addNeighbourTwoWay(Mountain m1, Mountain m2){
        m1.addNeighbour(m2);
        m2.addNeighbour(m1);
    }

    @Test
    public void mountainsTest() {
        Mountain[] mountains = {
                new Mountain(),
                new Mountain(),
                new Mountain(),
                new Mountain(),
                new Mountain(),
                new Mountain()
        };

        addNeighbourTwoWay(mountains[0], mountains[1]);
        addNeighbourTwoWay(mountains[0], mountains[2]);
        addNeighbourTwoWay(mountains[1], mountains[2]);
        addNeighbourTwoWay(mountains[3], mountains[2]);
        addNeighbourTwoWay(mountains[3], mountains[1]);
        
        addNeighbourTwoWay(mountains[4], mountains[5]);



        TrialOfTheMountains.assignBeasts(mountains);

        for (Mountain mountain : mountains) {
            assertNotNull(mountain.getBeast());
            for (Mountain neighbour : mountain.getNeighbours()) {
                assertNotNull(neighbour.getBeast());
                assertNotEquals(neighbour.getBeast().name(), mountain.getBeast().name());
            }
        }
    }
}