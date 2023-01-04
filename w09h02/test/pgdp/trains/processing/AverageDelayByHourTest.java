package pgdp.trains.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

public class AverageDelayByHourTest {
    // Example train connections from the `main` method in
    // `DataProcessing.java`.
    final List<TrainConnection> exampleTrainConnections = List.of(
            new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            LocalDateTime.of(2022, 12, 1, 12, 0),
                            TrainStop.Kind.REGULAR))),
            new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 10, 0),
                            LocalDateTime.of(2022, 12, 1, 10, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 10, 30),
                            LocalDateTime.of(2022, 12, 1, 10, 30),
                            TrainStop.Kind.REGULAR))),
            new TrainConnection("ICE 3", "ICE", "3", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 12, 0),
                            LocalDateTime.of(2022, 12, 1, 12, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.AUGSBURG_HBF,
                            LocalDateTime.of(2022, 12, 1, 12, 20),
                            LocalDateTime.of(2022, 12, 1, 13, 0),
                            TrainStop.Kind.CANCELLED),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 13, 30),
                            LocalDateTime.of(2022, 12, 1, 13, 30),
                            TrainStop.Kind.REGULAR))));

    @Test
    public void testExample() {
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        Map<Integer, Double> expected = Map.of(
                10, 0.0,
                11, 0.0,
                12, 15.0,
                13, 20.0);
        Map<Integer, Double> actual = DataProcessing.averageDelayByHour(trainConnections);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should return an empty map if the stream is empty")
    public void testEmpty() {
        Stream<TrainConnection> trainConnections = Stream.empty();

        Map<Integer, Double> expected = Map.of();
        Map<Integer, Double> actual = DataProcessing.averageDelayByHour(trainConnections);

        assertEquals(expected, actual);
    }
}
