package pgdp.trials;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrialOfTheDreamsTest {
    private Function<byte[], Boolean> getLock(byte[] combination) {
        return (byte[] bytes) -> {
            if (bytes.length != combination.length) {return false;}
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] != combination[i]) {return false;}
            }
            return true;
        };
    }

    @Test
    @DisplayName("Combination of Length 1")
    void TestLengthOne() {
        byte[] combination = {Byte.MAX_VALUE};
        Function<byte[], Boolean> lock = getLock(combination);

        byte[] result = TrialOfTheDreams.lockPick(lock);

        for (int i = 0; i < combination.length; i++) {
            assertEquals(combination[i], result[i]);
        }
    }

    @Test
    @DisplayName("Combination of Length 2")
    void TestLengthTwo() {
        byte[] combination = {-128, 127};
        Function<byte[], Boolean> lock = getLock(combination);

        byte[] result = TrialOfTheDreams.lockPick(lock);

        for (int i = 0; i < combination.length; i++) {
            assertEquals(combination[i], result[i]);
        }
    }

    @Test
    @DisplayName("Combination of Length 3")
    void TestLengthThree() {
        byte[] combination = {25, 8, 30};
        Function<byte[], Boolean> lock = getLock(combination);

        byte[] result = TrialOfTheDreams.lockPick(lock);

        for (int i = 0; i < combination.length; i++) {
            assertEquals(combination[i], result[i]);
        }
    }

    @Test
    @DisplayName("Combination of Length 3 with negative numbers")
    void testLengthThreeNegative() {
        byte[] combination = {25, -8, 30};
        Function<byte[], Boolean> lock = getLock(combination);

        byte[] result = TrialOfTheDreams.lockPick(lock);

            for(int i = 0; i < combination.length; i++) {
            assertEquals(combination[i], result[i]);
        }
}

    @Test
    @DisplayName("Empty Array")
    void testEmpty() {
        byte[] combination = new byte[0];
        Function<byte[], Boolean> lock = getLock(combination);

        byte[] result = TrialOfTheDreams.lockPick(lock);

        for (int i = 0; i < combination.length; i++) {
            assertEquals(combination[i], result[i]);
        }
    }
}