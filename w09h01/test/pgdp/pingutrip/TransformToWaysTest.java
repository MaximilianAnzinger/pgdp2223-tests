package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TransformToWaysTest {
    @Test
    void testTransformToWays() {
        List<WayPoint> points = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));
        List<OneWay> ways = PinguTrip.transformToWays(points).toList();

        OneWay[] expected = { new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)) };
        OneWay[] actual = ways.toArray(OneWay[]::new);

        assertEquals(expected.length, actual.length,
                "The stream should contain exactly " + expected.length + " values");
    }
}
