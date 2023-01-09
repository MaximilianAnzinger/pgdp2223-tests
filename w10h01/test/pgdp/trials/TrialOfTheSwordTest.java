package pgdp.trials;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrialOfTheSwordTest {


    @Test
    @DisplayName("Artemis example")
    void testExample() {
        TrialOfTheSword.FlatArray<Integer> fa = new TrialOfTheSword.FlatArray<>(Integer.class, 3, 5);
        assertEquals(8, fa.computeIndex(1, 3));
    }

    @Test
    @DisplayName("3-dimensional test")
    void testThreeDimensinal() {
        TrialOfTheSword.FlatArray<Integer> fa = new TrialOfTheSword.FlatArray<>(Integer.class, 5, 8, 7);
        assertEquals(128, fa.computeIndex(2, 2, 2));
    }

    @Test
    @DisplayName("4-dimensinal test")
    void testFourDimensional() {
        TrialOfTheSword.FlatArray<Integer> fa = new TrialOfTheSword.FlatArray<>(Integer.class, 5, 8, 7, 12);
        assertEquals(1694, fa.computeIndex(2, 4, 1, 2));
    }
}
