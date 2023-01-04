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

public class PercentOfKindStopsTest {
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
    @DisplayName("Percent of cancelled stops with the example trains connections should be 14.2%")
    void testExampleCancelled() {
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        double exepcted = 14.285714285714285;
        double actual = DataProcessing.percentOfKindStops(trainConnections,
                TrainStop.Kind.CANCELLED);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of regular stops with the example trains connections should be 85.7%")
    void testExampleRegular() {
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        double exepcted = 85.71428571428571;
        double actual = DataProcessing.percentOfKindStops(trainConnections,
                TrainStop.Kind.REGULAR);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of additional stops with the example trains connections should be 0.0%")
    void testExampleAdditional() {
        Stream<TrainConnection> trainConnections = exampleTrainConnections.stream();

        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections,
                TrainStop.Kind.ADDITIONAL);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of additional stops with a list of train connections with only additional stops should be 100.0%")
    void testAdditionalAllStops() {
        // A list of train connections with only additional stops.
        List<TrainConnection> trainConnections = trainConnectionsWithOnly(TrainStop.Kind.ADDITIONAL);

        double exepcted = 100.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.ADDITIONAL);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of cancelled stops with a list of train connections with only cancelled stops should be 100.0%")
    void testCancelledAllStops() {
        // A list of train connections with only cancelled stops.
        List<TrainConnection> trainConnections = trainConnectionsWithOnly(TrainStop.Kind.CANCELLED);

        double exepcted = 100.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.CANCELLED);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of cancelled stops with a list of train connections with only regular stops should be 0.0%")
    void testCanelledZeroStops() {
        // A list of train connections with only regular stops.
        List<TrainConnection> trainConnections = trainConnectionsWithOnly(TrainStop.Kind.REGULAR);

        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.CANCELLED);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of regular stops with a list of train connections with only regular stops should be 100.0%")
    void testRegularAllStops() {
        // A list of train connections with only regular stops.
        List<TrainConnection> trainConnections = trainConnectionsWithOnly(TrainStop.Kind.REGULAR);

        double exepcted = 100.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.REGULAR);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of regular stops with a list of train connections with only cancelled stops should be 0.0%")
    void testRegularZeroStops() {
        // A list of train connections with only regular stops.
        List<TrainConnection> trainConnections = trainConnectionsWithOnly(TrainStop.Kind.CANCELLED);

        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.REGULAR);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of regular stops with an empty list of train connections should be 0.0%")
    void testRegularEmptyStops() {
        List<TrainConnection> trainConnections = List.of();

        // Source: https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/.E2.9C.94.20percentKindofStops.28.29.20Prozent/near/860094
        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.REGULAR);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of cancelled stops with an empty list of train connections should be 0.0%")
    void testCancelledEmptyStops() {
        List<TrainConnection> trainConnections = List.of();

        // Source: https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/.E2.9C.94.20percentKindofStops.28.29.20Prozent/near/860094
        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.CANCELLED);

        assertEquals(exepcted, actual);
    }

    @Test
    @DisplayName("Percent of additional stops with an empty list of train connections should be 0.0%")
    void testAdditionalEmptyStops() {
        List<TrainConnection> trainConnections = List.of();

        // Source: https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/.E2.9C.94.20percentKindofStops.28.29.20Prozent/near/860094
        double exepcted = 0.0;
        double actual = DataProcessing.percentOfKindStops(trainConnections.stream(),
                TrainStop.Kind.ADDITIONAL);

        assertEquals(exepcted, actual);
    }

    /**
     * Returns a list of train connections with only the given kind of stops.
     * 
     * @param kind the kind of stops
     * @return a list of train connections with only the given kind of stops
     */
    private List<TrainConnection> trainConnectionsWithOnly(TrainStop.Kind kind) {
        return List.of(
                new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 10, 0),
                                LocalDateTime.of(2022, 12, 1, 10, 0),
                                kind),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 10, 30),
                                LocalDateTime.of(2022, 12, 1, 10, 30),
                                kind))),
                new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 11, 0),
                                LocalDateTime.of(2022, 12, 1, 11, 0),
                                kind),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 11, 30),
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                kind))),
                new TrainConnection("ICE 3", "ICE", "3", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                kind),
                        new TrainStop(Station.AUGSBURG_HBF,
                                LocalDateTime.of(2022, 12, 1, 12, 20),
                                LocalDateTime.of(2022, 12, 1, 13, 0),
                                kind),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 13, 30),
                                LocalDateTime.of(2022, 12, 1, 13, 30),
                                kind))));
    }
}
