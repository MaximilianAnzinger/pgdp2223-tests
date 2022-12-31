package pgdp.trains.processing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

import java.time.LocalDateTime;
import java.util.List;

public class AdditionalTests {
    int[] delays;
    LocalDateTime scheduled = LocalDateTime.of(2022, 12, 1, 0, 0);
    LocalDateTime actual = LocalDateTime.of(2022, 12, 1, 0, 0);

    public List<TrainConnection> getTrainConnections() {
        return List.of(
                new TrainConnection("ICE 2", "ICE", "2", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                scheduled,
                                actual.plusMinutes(delays[0]),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.NUERNBERG_HBF,
                                scheduled,
                                actual.plusMinutes(delays[1]),
                                TrainStop.Kind.REGULAR)
                )),
                new TrainConnection("ICE 1", "ICE", "1", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                scheduled,
                                actual.plusMinutes(delays[2]),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.NUERNBERG_HBF,
                                scheduled,
                                actual.plusMinutes(delays[3]),
                                TrainStop.Kind.REGULAR)
                )),
                new TrainConnection("ICE 3", "ICE", "3", "DB", List.of(
                        new TrainStop(Station.MUENCHEN_HBF,
                                scheduled,
                                actual.plusMinutes(delays[4]),
                                TrainStop.Kind.REGULAR),
                        new TrainStop(Station.AUGSBURG_HBF,
                                scheduled,
                                actual.plusMinutes(delays[5]),
                                TrainStop.Kind.CANCELLED),
                        new TrainStop(Station.NUERNBERG_HBF,
                                scheduled,
                                actual.plusMinutes(delays[6]),
                                TrainStop.Kind.REGULAR)
                ))
        );
    }
    @Test
    public void averageDelayByHourTest(){
        int[][] multipleDelays = {
                {30, 0, 42, 21, 2, 15, 10},
                {30, 90, 42, 21, 2, 15, 10},
                {0, 0, 0, 0, 0, 0, 0},
                {60, 60, 60, 60, 60, 60, 60},
                {234, 623, 123, 532, 12, 0, 187}
        };
        for(int[] i: multipleDelays) {
            delays = i;
            for (int j = 0; j < 12; j++) {
                double avdel = averageDelays(j);
                if (avdel == -1) {
                    Assertions.assertNull(DataProcessing.averageDelayByHour(getTrainConnections().stream()).get(j));
                } else {
                    Assertions.assertEquals(avdel, DataProcessing.averageDelayByHour(getTrainConnections().stream()).get(j));
                }
            }
        }
    }

    public double averageDelays(int hour){
        int total = 0;
        int counter = 0;
        for(int i: delays){
            if(i >= hour * 60 && i < (hour + 1) * 60) {
                total += i;
                counter++;
            }
        }
        if(counter == 0){
            return -1;
        }
        return ((double)total)/counter;
    }
}
