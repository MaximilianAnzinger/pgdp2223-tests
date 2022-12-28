package pgdp.trains;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import pgdp.trains.processing.*;
import pgdp.trains.connections.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UnitTests {
    @Nested
    @DisplayName("Tests for task 1")
    class Task1Tests {

        private List<TrainConnection> GivenExampleConnections;

        @BeforeEach
        void setup() {
            this.GivenExampleConnections = List.of(
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
        }

        @Test
        @DisplayName("Task1 given example works")
        void GivenExample() {
            List<TrainConnection> trainConnectionsCleaned = List.of(
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
            List<TrainConnection> cleanDataset = DataProcessing.cleanDataset(GivenExampleConnections.stream()).toList();
            assertEquals(trainConnectionsCleaned, cleanDataset);
        }


        @Test
        @DisplayName("Task1 duplicates are removed")
        void Duplicates() {
            List<TrainConnection> ExampleConnections = List.of(
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
                    ))
            );


            List<TrainConnection> trainConnectionsCleaned = List.of(
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

            List<TrainConnection> cleanDataset = DataProcessing.cleanDataset(ExampleConnections.stream()).toList();
            assertEquals(trainConnectionsCleaned, cleanDataset);
        }

        @Test
        @DisplayName("Task1 can handle empty connections")
        void EmptyConnectionList() {
            List<TrainConnection> ExampleConnections = new ArrayList<>();

            Stream<TrainConnection> cleanDataset = DataProcessing.cleanDataset(ExampleConnections.stream());
            assertEquals(Stream.empty().count(), cleanDataset.count());
        }

        @Test
        @DisplayName("Task1 can handle empty connections")
        void RemoveStops() {
            List<TrainConnection> ExampleConnections =  List.of(
                    new TrainConnection("ICE 4", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    TrainStop.Kind.CANCELLED),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.CANCELLED)
                    )),
                    new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    )),
                    new TrainConnection("ICE 2", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 30),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    )),
                    new TrainConnection("ICE 3", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 45),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.ASCHAFFENBURG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 30),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.CANCELLED),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    ))
            );

            List<TrainConnection> trainConnectionsCleaned =  List.of(
                    new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    )),
                    new TrainConnection("ICE 2", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 30),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    )),
                    new TrainConnection("ICE 3", "ICE", "1", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 45),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    LocalDateTime.of(2022, 12, 1, 10, 0),
                                    TrainStop.Kind.REGULAR)
                    )),
                    new TrainConnection("ICE 4", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.REGULAR)
                    ))
            );

            List<TrainConnection> cleanDataset = DataProcessing.cleanDataset(ExampleConnections.stream()).toList();
            System.out.println(cleanDataset);
            assertEquals(trainConnectionsCleaned, cleanDataset);
        }
    }

    @Nested
    @DisplayName("Tests for task 2")
    class Task2Tests {
        private List<TrainConnection> GivenExampleConnections;

        @BeforeEach
        void setup() {
            this.GivenExampleConnections = List.of(
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
        }

        @Test
        @DisplayName("Task2 given example works")
        void GivenExample() {
            TrainConnection worstDelayedTrain = DataProcessing.worstDelayedTrain(GivenExampleConnections.stream());

            TrainConnection expectedTrain = new TrainConnection("ICE 3", "ICE", "3", "DB", List.of(
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
            ));

            assertEquals(expectedTrain,worstDelayedTrain);
        }


        @Test
        @DisplayName("Task2 no connection should return null")
        void NoConnection() {
            List<TrainConnection> ExampleConnections = List.of();

            TrainConnection worstDelayedTrain = DataProcessing.worstDelayedTrain(ExampleConnections.stream());
            assertEquals(null, worstDelayedTrain);
        }

        @Test
        @DisplayName("Task2 returns a train even if no delays")
        void NoDelayTest1() {

            TrainConnection noDelays = new TrainConnection("ICE 1", "ICE", "2", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            TrainStop.Kind.REGULAR)
            ));

            List<TrainConnection> ExampleConnections = List.of(noDelays);

            TrainConnection worstDelayedTrain = DataProcessing.worstDelayedTrain(ExampleConnections.stream());
            assertEquals(noDelays, worstDelayedTrain);
        }

        @Test
        @DisplayName("Task2 returns a train even if no delays")
        void NoDelayTest2() {

            TrainConnection noDelays1 = new TrainConnection("ICE 1", "ICE", "2", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            TrainStop.Kind.REGULAR)
            ));

            TrainConnection noDelays2 = new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                    new TrainStop(Station.MUENCHEN_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            LocalDateTime.of(2022, 12, 1, 11, 0),
                            TrainStop.Kind.REGULAR),
                    new TrainStop(Station.NUERNBERG_HBF,
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            LocalDateTime.of(2022, 12, 1, 11, 30),
                            TrainStop.Kind.REGULAR)
            ));

            List<TrainConnection> ExampleConnections = List.of(noDelays1, noDelays2);

            TrainConnection worstDelayedTrain = DataProcessing.worstDelayedTrain(ExampleConnections.stream());

            try{
                assertEquals(noDelays2, worstDelayedTrain);
            }catch (AssertionError e){
                assertEquals(noDelays1, worstDelayedTrain);
            }
        }
    }


    @Nested
    @DisplayName("Tests for task 3")
    class Task3Tests {
        private List<TrainConnection> GivenExampleConnections;

        @BeforeEach
        void setup() {
            this.GivenExampleConnections = List.of(
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
        }


        @Test
        @DisplayName("Task3 given example works")
        void GivenExample() {
            double percentOfKindStops = DataProcessing.percentOfKindStops(GivenExampleConnections.stream(), TrainStop.Kind.REGULAR);
            assertEquals(85.71428571428571,percentOfKindStops);

            double percentOfKindStops2 = DataProcessing.percentOfKindStops(GivenExampleConnections.stream(), TrainStop.Kind.CANCELLED);
            assertEquals(14.285714285714285,percentOfKindStops2);
        }

        @Test
        @DisplayName("Task3 easy example works")
        void EasyExample() {

            List<TrainConnection> test1 = List.of(
                    new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.REGULAR)
                    ))
            );

            List<TrainConnection> test2 = List.of(
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
                    new TrainConnection("ICE 1", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    TrainStop.Kind.CANCELLED),
                            new TrainStop(Station.NUERNBERG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    TrainStop.Kind.CANCELLED)
                    )),
                    new TrainConnection("ICE 3", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    TrainStop.Kind.ADDITIONAL)
                    ))

            );


            double percentOfKindStops = DataProcessing.percentOfKindStops(test1.stream(), TrainStop.Kind.REGULAR);
            assertEquals(100.0,percentOfKindStops);

            percentOfKindStops = DataProcessing.percentOfKindStops(test1.stream(), TrainStop.Kind.CANCELLED);
            assertEquals(0.0,percentOfKindStops);

            percentOfKindStops = DataProcessing.percentOfKindStops(test1.stream(), TrainStop.Kind.ADDITIONAL);
            assertEquals(0.0,percentOfKindStops);

            percentOfKindStops = DataProcessing.percentOfKindStops(test2.stream(), TrainStop.Kind.REGULAR);
            assertEquals(2.0/5.0*100,percentOfKindStops);

            percentOfKindStops = DataProcessing.percentOfKindStops(test2.stream(), TrainStop.Kind.CANCELLED);
            assertEquals(2.0/5.0*100,percentOfKindStops);

            percentOfKindStops = DataProcessing.percentOfKindStops(test2.stream(), TrainStop.Kind.ADDITIONAL);
            assertEquals(1.0/5.0*100,percentOfKindStops);
        }


        @Test
        @DisplayName("Task3 handle empty connections")
        void EmptyConnection() {
            List<TrainConnection> ExampleConnections = List.of();

            double percentOfKindStops = DataProcessing.percentOfKindStops(ExampleConnections.stream(), TrainStop.Kind.REGULAR);

            //no idea what the correct error handling would be in this case
            try{
                assertEquals(0,percentOfKindStops);
            }catch (AssertionError e){
                assertEquals(null,percentOfKindStops);
            }

        }
    }

    @Nested
    @DisplayName("Tests for task 4")
    class Task4Tests {
        private List<TrainConnection> GivenExampleConnections;

        @BeforeEach
        void setup() {
            this.GivenExampleConnections = List.of(
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
        }


        @Test
        @DisplayName("Task4 given example works")
        void GivenExample() {
            double averageDelayAt = DataProcessing.averageDelayAt(GivenExampleConnections.stream(), Station.NUERNBERG_HBF);
            // averageDelayAt sollte 10.0 sein. (Da dreimal angefahren und einmal 30 Minuten Verspätung).
            assertEquals(10.0,averageDelayAt);

            averageDelayAt = DataProcessing.averageDelayAt(GivenExampleConnections.stream(), Station.MUENCHEN_HBF);
            assertEquals(0.0/3.0,averageDelayAt);

            averageDelayAt = DataProcessing.averageDelayAt(GivenExampleConnections.stream(), Station.AUGSBURG_HBF);
            // according to Zulip we don't have to take into account if a station was cancelled or not
            assertEquals(40.0/1.0,averageDelayAt);
        }

        @Test
        @DisplayName("Task4 another example")
        void AnotherExample() {
            List<TrainConnection> test1 = List.of(
                    new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    TrainStop.Kind.REGULAR))),
                    new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 11, 0),
                                    LocalDateTime.of(2022, 12, 1, 11, 30),
                                    TrainStop.Kind.REGULAR))),
                    new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                            new TrainStop(Station.MUENCHEN_HBF,
                                    LocalDateTime.of(2022, 12, 1, 12, 0),
                                    LocalDateTime.of(2022, 12, 1, 12, 40),
                                    TrainStop.Kind.CANCELLED))),
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
                                    TrainStop.Kind.REGULAR),
                            new TrainStop(Station.ASCHAFFENBURG_HBF,
                                    LocalDateTime.of(2022, 12, 1, 13, 30),
                                    LocalDateTime.of(2022, 12, 1, 13, 50),
                                    TrainStop.Kind.REGULAR)
                    ))
            );


            double averageDelayAt = DataProcessing.averageDelayAt(test1.stream(), Station.ASCHAFFENBURG_HBF);
            assertEquals(20.0/1.0,averageDelayAt);

            averageDelayAt = DataProcessing.averageDelayAt(test1.stream(), Station.MUENCHEN_HBF);
            assertEquals(70.0/4.0,averageDelayAt);

            averageDelayAt = DataProcessing.averageDelayAt(test1.stream(), Station.NUERNBERG_HBF);
            assertEquals(0.0/1.0,averageDelayAt);

        }

        @Test
        @DisplayName("Task4 can handle station not existing in connection stream")
        void StationNotExisting() {
            double averageDelayAt = DataProcessing.averageDelayAt(GivenExampleConnections.stream(), Station.ASCHAFFENBURG_HBF);

            //no idea how to correctly handle that again
            try{
                assertEquals(0.0,averageDelayAt);
            }catch (AssertionError e){
                assertEquals(null,averageDelayAt);
            }

        }


        @Test
        @DisplayName("Task4 handle empty connections")
        void EmptyConnection() {
            List<TrainConnection> ExampleConnections = List.of();

            double averageDelayAt = DataProcessing.averageDelayAt(ExampleConnections.stream(), Station.NUERNBERG_HBF);

            //no idea what the correct error handling would be in this case
            try{
                assertEquals(0.0,averageDelayAt);
            }catch (AssertionError e){
                assertEquals(null,averageDelayAt);
            }
        }
    }
}
