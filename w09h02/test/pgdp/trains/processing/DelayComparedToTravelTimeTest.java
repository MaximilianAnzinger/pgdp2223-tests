package pgdp.trains.processing;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import pgdp.trains.processing.utils.JSONParser;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DelayComparedToTravelTimeTest {

  private static JSONObject manipulateSTR29() {
    var parsed = JSONParser.getStr29CanceledDataset();

    var stops = parsed.getJSONArray("stops");
    var firstStop = stops.getJSONObject(0);
    var arrival = parsed.getJSONObject("arrival");

    stops.clear();
    stops.put(firstStop);

    arrival.put("scheduledTime", "2022-11-02T19:16:00.000Z");
    arrival.put("time", "2022-11-02T19:16:00.000Z");

    return parsed;
  }

  @Test
  void shouldHandleNaN() {
    final JSONObject manipulated = DelayComparedToTravelTimeTest.manipulateSTR29();

    final Map<String, Double> result = DataProcessing.delayComparedToTotalTravelTimeByTransport(Stream.of(
        JSONParser.getTrainConnectionFromJSON(manipulated)
    ));

    final double expected = 0.0;

    assertEquals(expected, result.get("STR"), "Result must not be NaN");
  }
}
