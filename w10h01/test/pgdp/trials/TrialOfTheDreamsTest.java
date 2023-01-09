package pgdp.trials;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pgdp.trials.TrialOfTheDreams.lockPick;

public class TrialOfTheDreamsTest {
    // Please note: Method lockPick(Function<Byte[], Boolean> lock, List<Byte> key, int maxlen) has to be set to public
    // for this test to work

    @Test
    @DisplayName("Basic Example")
    void testBasicExample() {
        Function<Byte[], Boolean> testFunction = new Function<Byte[], Boolean>() {
            @Override
            public Boolean apply(Byte[] bytes) {
                return (bytes.length == 2 && bytes[0] == -97 && bytes [1] == 37);
            }
        };
        List<Byte> res = lockPick(testFunction, new ArrayList<>(), 2);
        assertEquals(2, res.size());
        assertEquals((byte) -97, res.get(0));
        assertEquals((byte) 37, res.get(1));
    }

    @Test
    @DisplayName("Should return null if key is longer then maxlen")
    void testNullIfNotFound() {
        Function<Byte[], Boolean> testFunction = new Function<Byte[], Boolean>() {
            @Override
            public Boolean apply(Byte[] bytes) {
                return (bytes.length == 5);
            }
        };
        List<Byte> res = lockPick(testFunction, new ArrayList<>(), 2);
        assertEquals(null, res);
    }

    @Test
    @DisplayName("Tests lockPick(Function<byte[], Boolean> lock, int maxlen)")
    // lockPick(Function<Byte[], Boolean> lock, List<Byte> key, int maxlen) has to be implemented for this test to work.
    void testWrapperFunction() {
        Function<byte[], Boolean> testFunction = new Function<byte[], Boolean>() {
            @Override
            public Boolean apply(byte[] bytes) {
                return (bytes.length == 2 && bytes[0] == -97 && bytes [1] == 37);
            }
        };
        assertArrayEquals(new byte[]{-97, 37}, lockPick(testFunction, 3));
    }
}
