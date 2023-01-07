package pgdp.trains.processing;

import org.junit.jupiter.api.Test;
import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class OfficialExampleTests {

    // EPSILON of 0.00001 because of https://zulip.in.tum.de/#narrow/stream/1504-PGdP-W09H02/topic/.E2.9C.94.20Rundungsfehler/near/868554
    private final double EPSILON = 0.00001;

    List<TrainConnection> trainConnections = List.of(
            new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            LocalDateTime.of(2022, 12, 1, 12, 0),
                            TrainStop.Kind.REGULAR)
            )),
            new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 10, 0),
                            LocalDateTime.of(2022, 12, 1, 10, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 10, 30),
                            LocalDateTime.of(2022, 12, 1, 10, 30),
                            TrainStop.Kind.REGULAR)
            )),
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
                            TrainStop.Kind.REGULAR)
            ))
    );

    public static <T> void assertStreamEquals(Stream<T> expected, Stream<T> actual) {
        Iterator<T> expectedIterator = expected.iterator(), actualIterator = actual.iterator();
        while (expectedIterator.hasNext() && actualIterator.hasNext())
            assertEquals(expectedIterator.next(), actualIterator.next());
        assert !expectedIterator.hasNext() && !actualIterator.hasNext();
    }

    @Test
    void cleanDataset() {
        List<TrainConnection> cleanedConnections = List.of(
                new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 10, 0),
                                LocalDateTime.of(2022, 12, 1, 10, 0),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 10, 30),
                                LocalDateTime.of(2022, 12, 1, 10, 30),
                                TrainStop.Kind.REGULAR)
                )),
                new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 11, 0),
                                LocalDateTime.of(2022, 12, 1, 11, 0),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 11, 30),
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                TrainStop.Kind.REGULAR)
                )),
                new TrainConnection("ICE 3", "ICE", "3", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                LocalDateTime.of(2022, 12, 1, 12, 0),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.NUERNBERG_HBF,
                                LocalDateTime.of(2022, 12, 1, 13, 30),
                                LocalDateTime.of(2022, 12, 1, 13, 30),
                                TrainStop.Kind.REGULAR)
                ))
        );
        assertStreamEquals(cleanedConnections.stream(), DataProcessing.cleanDataset(trainConnections.stream()));
    }

    @Test
    void delayComparedToTotalTravelTimeByTransport() {
        Map<String, Double> actual = DataProcessing.delayComparedToTotalTravelTimeByTransport(trainConnections.stream());
        assertEquals(16.666666666666668, actual.get("ICE"),  EPSILON);
    }
}