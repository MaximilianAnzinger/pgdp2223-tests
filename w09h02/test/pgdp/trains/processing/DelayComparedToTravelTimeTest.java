package pgdp.trains.processing;

import org.junit.jupiter.api.Test;
import pgdp.trains.processing.utils.JsonStringParser;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DelayComparedToTravelTimeTest {

  @Test
  void shouldHandleNaN() {
    final Map<String, Double> result = DataProcessing.delayComparedToTotalTravelTimeByTransport(Stream.of(
        JsonStringParser.getStr42()
    ));

    final var expected = 0.0;

    assertEquals(expected, result.get("STR"), "Result must not be NaN");
  }
}
