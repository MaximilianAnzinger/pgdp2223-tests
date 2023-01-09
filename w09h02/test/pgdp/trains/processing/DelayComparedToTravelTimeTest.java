package pgdp.trains.processing;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;
import pgdp.trains.processing.utils.JSONParser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
        JSONParser.getTrainConnectionFromJSON(manipulated)));

    final double expected = 0.0;

    assertEquals(expected, result.get("STR"), "Result must not be NaN");
  }

  @Test
  void testEmptyList() {
    Stream<TrainConnection> trainConnections = Stream.empty();

    final Map<String, Double> actual = DataProcessing.delayComparedToTotalTravelTimeByTransport(trainConnections);
    final Map<String, Double> expected = Collections.emptyMap();

    assertEquals(expected, actual, "Result must be empty");
  }

  @Test
  void testTooEarlyTrainsShouldNotCompensateTheDelay() {
    // A list of train connections where a train is too early
    final Stream<TrainConnection> trainConnections = Stream.of(
        new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
            new TrainStop(Station.MUENCHEN_HBF,
                LocalDateTime.of(2022, 12, 1, 10, 0),
                LocalDateTime.of(2022, 12, 1, 10, 0),
                TrainStop.Kind.REGULAR),
            new TrainStop(Station.NUERNBERG_HBF,
                LocalDateTime.of(2022, 12, 1, 17, 00),
                // 1 hour too early
                LocalDateTime.of(2022, 12, 1, 16, 00),
                TrainStop.Kind.REGULAR))),
        new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
            new TrainStop(Station.MUENCHEN_HBF,
                LocalDateTime.of(2022, 12, 1, 10, 0),
                LocalDateTime.of(2022, 12, 1, 10, 0),
                TrainStop.Kind.REGULAR),
            new TrainStop(Station.NUERNBERG_HBF,
                LocalDateTime.of(2022, 12, 1, 13, 0),
                // 1 hour delay
                LocalDateTime.of(2022, 12, 1, 14, 0),
                TrainStop.Kind.REGULAR))));

    // Source:
    // https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/.E2.9C.94.20Zug.20kommt.20zu.20fr.C3.BCh.3F/near/880712
    //
    // Why is 9.09% is the correct answer? The following explanation is a
    // summary from the Zulip post:
    //
    // ICE 1 is scheduled for 7 hours but only needs 6 hours. However, this
    // would be a negative delay and we should ignore negative delays. ICE 2 is
    // scheduled for 3 hours but needs 4 hours (1 hours late).
    //
    // This results into the following calculation:
    // Time scheduled: 7 + 3 = 10
    // Time actually needed: 7 + 4 = 11 (we ignore that ICE 1 is 1 hours too
    // early)
    // Result = (11 - 10) / 11 = 0.0909... 
    final Map<String, Double> expected = Map.of("ICE", 9.090909090909092);
    final Map<String, Double> actual = DataProcessing.delayComparedToTotalTravelTimeByTransport(trainConnections);

    assertEquals(expected, actual);
  }
}
