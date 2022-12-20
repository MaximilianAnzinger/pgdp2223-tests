package pgdp.trains.processing;

import org.junit.jupiter.api.Test;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.processing.utils.JsonStringParser;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CleanDatasetTest {

    @Test
    void shouldRemoveDuplicates() {
        final var result = DataProcessing.cleanDataset(Stream.of(
                JsonStringParser.getRe8(),
                JsonStringParser.getRe8()
        )).toList();

        final var expected = List.of(JsonStringParser.getRe8());

        assertEquals(expected, result);
    }

    @Test
    void shouldKeepNonDuplicates() {
        final var result = DataProcessing.cleanDataset(Stream.of(
                JsonStringParser.getICE881(),
                JsonStringParser.getRe8()
        )).toList();

        final var expected = List.of(JsonStringParser.getICE881(), JsonStringParser.getRe8());
        assertEquals(expected, result);
    }

    /**
     * Ice 881 leaves at 12 and re8 at 17.
     */
    @Test
    void shouldSortByDeparture() {
        final var result = DataProcessing.cleanDataset(Stream.of(
                JsonStringParser.getRe8(),
                JsonStringParser.getICE881()
        )).toList();

        final var expected = List.of(JsonStringParser.getICE881(), JsonStringParser.getRe8());
        assertEquals(expected, result, "Trains are not sorted by departure.");
    }

    @Test
    void shouldRemoveAllStopsThatWereCancelled() {
        final var result = DataProcessing.cleanDataset(Stream.of(
                JsonStringParser.getStr29()
        )).map(TrainConnection::stops).findFirst().get();

        assertEquals(List.of(), result, "Train connection should not include any stops.");
    }

    @Test
    void shouldNotRemoveStopsThatAreNotCancelled() {
        final var ice881 = JsonStringParser.getICE881();
        final var result = DataProcessing.cleanDataset(Stream.of(
                ice881
        )).map(TrainConnection::stops);

        final var actual = result.findFirst().get();
        assertEquals(ice881.stops(), actual);
    }
}
