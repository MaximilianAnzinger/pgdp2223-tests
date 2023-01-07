package pgdp.trains.processing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

public class AverageDelayAtTest {
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
    @DisplayName("Delay for Nürnberg Hbf should 10 minutes with example train connections")
    void testExample() {
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        double expected = 10.0;
        double actual = DataProcessing.averageDelayAt(trainConnections, Station.NUERNBERG_HBF);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delay should be 0.0 when the station is not part of the train connections")
    void testStationIsNotPartOfTrainConnections(){
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        double expected = 0.0;
        double actual = DataProcessing.averageDelayAt(trainConnections, Station.BOCHUM_HBF);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delay for an empty list of train connections should 0.0")
    void testEmptyList() {
        List<TrainConnection> emptyList = List.of();

        // Source:
        // https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/TrainStop.20nicht.20enthalten/near/879038
        double expected = 0.0;
        double actual = DataProcessing.averageDelayAt(emptyList.stream(), Station.MUENCHEN_HBF);

        assertEquals(expected, actual);
    }
}
