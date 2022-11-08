package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pgdp.image.SeamCarving;

import java.util.stream.Stream;

public class UnitTests {

    @ParameterizedTest
    @ValueSource(ints = {0x00, 0x010F2C, 0xFFFFFF})
    void testComputeGradientMagnitudeZero(int v) {
        SeamCarving sc = new SeamCarving();
        Assertions.assertEquals(0, sc.computeGradientMagnitude(v, v));
    }

    @ParameterizedTest
    @MethodSource
    void testComputeGradientMagnitude(int v1, int v2, int magnitude) {
        SeamCarving sc = new SeamCarving();
        Assertions.assertEquals(magnitude, sc.computeGradientMagnitude(v1, v2));
    }

    static Stream<Arguments> testComputeGradientMagnitude() {
        return Stream.of(
                Arguments.arguments(0x000000, 0x0000FF, 65025),
                Arguments.arguments(0x000000, 0x00FF00, 65025),
                Arguments.arguments(0x000000, 0x00FFFF, 130050),
                Arguments.arguments(0x000000, 0xFF0000, 65025),
                Arguments.arguments(0x000000, 0xFF00FF, 130050),
                Arguments.arguments(0x000000, 0xFFFF00, 130050),
                Arguments.arguments(0x000000, 0xFFFFFF, 195075),

                Arguments.arguments(0x0000FF, 0x000000, 65025),
                Arguments.arguments(0x00FF00, 0x000000, 65025),
                Arguments.arguments(0x00FFFF, 0x000000, 130050),
                Arguments.arguments(0xFF0000, 0x000000, 65025),
                Arguments.arguments(0xFF00FF, 0x000000, 130050),
                Arguments.arguments(0xFFFF00, 0x000000, 130050),
                Arguments.arguments(0xFFFFFF, 0x000000, 195075),

                Arguments.arguments(1848583495, 1092857912, 19690),
                Arguments.arguments(1834787328, 1344234444, 49306),
                Arguments.arguments(1982739329, 1234327488, 15265),
                Arguments.arguments(1248734858, 1347878838, 46193),
                Arguments.arguments(1099287432, 1034584483, 7574),
                Arguments.arguments(1000034283, 1329487345, 8881),
                Arguments.arguments(1943784870, 1394745884, 82510)
        );
    }

}
