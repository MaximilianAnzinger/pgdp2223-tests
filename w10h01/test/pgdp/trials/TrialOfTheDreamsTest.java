package pgdp.trials;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrialOfTheDreamsTest {
    @Test
    @DisplayName("Combination of Length 1")
    void TestLengthOne() {
        byte[] combination = {25};
        Function<byte[], Boolean> lock = new Function<byte[], Boolean>() {
            @Override
            public Boolean apply(byte[] bytes) {
                if (bytes.length != combination.length) {return false;}
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != combination[i]) {return false;}
                }

                return true;
            }
        };

        assertEquals(combination, TrialOfTheDreams.lockPick(lock));
    }

    @Test
    @DisplayName("Combination of Length 3")
    void TestLengthThree() {
        byte[] combination = {25, 8, 30};
        Function<byte[], Boolean> lock = new Function<byte[], Boolean>() {
            @Override
            public Boolean apply(byte[] bytes) {
                if (bytes.length != combination.length) {return false;}
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != combination[i]) {return false;}
                }

                return true;
            }
        };

        assertEquals(combination, TrialOfTheDreams.lockPick(lock));
    }

    @Test
    @DisplayName("Combination of Length 3 with negative numbers")
    void testLengthThreeNegative() {
        byte[] combination = {25, -8, 30};
        Function<byte[], Boolean> lock = new Function<byte[], Boolean>() {
            @Override
            public Boolean apply(byte[] bytes) {
                if (bytes.length != combination.length) {return false;}
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] != combination[i]) {return false;}
                }

                return true;
            }
        };

        assertEquals(combination, TrialOfTheDreams.lockPick(lock));
    }
}
