package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainSortingByDurationTrip}
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByDurationTripTest {

    /**
     * Test for method sort from {@link TrainSortingByDurationTrip}.
     */
    @Test
    public void testSort() {
        LocalTime localTime = LocalTime.of(12, 0);

        Train train = new Train();
        train.setDepartureTime(localTime);
        train.getRoute().addTimeSinceStart(1, "01:00");
        train.getRoute().addTimeSinceStart(2, "32:00");

        Train train1 = new Train();
        train1.setDepartureTime(localTime);
        train1.getRoute().addTimeSinceStart(1, "01:00");
        train1.getRoute().addTimeSinceStart(2, "04:00");

        Train train2 = new Train();
        train2.setDepartureTime(localTime);
        train2.getRoute().addTimeSinceStart(1, "01:00");
        train2.getRoute().addTimeSinceStart(2, "22:00");

        Train train3 = new Train();
        train3.setDepartureTime(localTime);
        train3.getRoute().addTimeSinceStart(1, "01:00");
        train3.getRoute().addTimeSinceStart(2, "02:00");

        Train train4 = new Train();
        train4.setDepartureTime(localTime);
        train4.getRoute().addTimeSinceStart(1, "01:00");
        train4.getRoute().addTimeSinceStart(2, "08:00");

        List<Train> trains = new ArrayList<>(Arrays.asList(train, train1, train2, train3, train4));
        List<Train> sortedTrains = new ArrayList<>(Arrays.asList(train3, train1, train4, train2, train));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("from", "1");
        parameters.put("to", "2");

        assertEquals(sortedTrains, new TrainSortingByDurationTrip().sort(trains, parameters));
    }
}
