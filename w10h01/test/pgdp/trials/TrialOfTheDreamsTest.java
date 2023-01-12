package pgdp.trials;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
    public void combinationsTest() {

        combinationsTestValues().forEach(combination -> {
            Function<byte[], Boolean> lock = getLock(combination);
            String msg = "For combination: " + Arrays.toString(combination);

            byte[] result = TrialOfTheDreams.lockPick(lock);

            for (int i = 0; i < combination.length; i++) {
                assertEquals(combination[i], result[i], msg);
            }
        });
    }

    private static Stream<byte[]> combinationsTestValues () {
        return Stream.of(
                    new byte[] {Byte.MAX_VALUE},
                    new byte[] {-128, 127},
                    new byte[] {25, 8, 30},
                    new byte[] {25, -8, 30},
                    new byte[0]
                );
    }
}