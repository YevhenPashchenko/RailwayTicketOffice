package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainFilterByDirectionAndDepartureTime}.
 *
 * @author Yevhen Pashchenko
 */
public class TrainFilterByDirectionAndDepartureTimeTest {

    /**
     * Test for method filter from {@link TrainFilterByDirectionAndDepartureTime}.
     */
    @Test
    public void testFilter() {

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        List<Train> trains = new ArrayList<>();

        Train train = new Train();
        train.setDepartureTime(time);
        train.getRoute().addDistanceFromStart(1, 100);
        train.getRoute().addDistanceFromStart(2, 200);
        train.getRoute().addTimeSinceStart(1, "00:05");

        Train train1 = new Train();
        train1.getRoute().addDistanceFromStart(1, 200);
        train1.getRoute().addDistanceFromStart(2, 100);

        Train train2 = new Train();
        train2.setDepartureTime(time);
        train2.getRoute().addDistanceFromStart(1, 100);
        train2.getRoute().addDistanceFromStart(2, 200);
        train2.getRoute().addTimeSinceStart(1, "23:55");

        trains.add(train);
        trains.add(train1);
        trains.add(train2);

        List<Train> filteredTrains = new ArrayList<>();

        if (LocalTime.now().compareTo(LocalTime.of(23, 55)) < 0) {
            filteredTrains.add(train);
        } else {
            filteredTrains.add(train2);
        }

        List<Train> filteredTrains1 = new ArrayList<>();
        filteredTrains1.add(train);
        filteredTrains1.add(train2);

        assertEquals(filteredTrains, new TrainFilterByDirectionAndDepartureTime().filter(trains, 1, 2, date));
        assertEquals(filteredTrains1, new TrainFilterByDirectionAndDepartureTime().filter(trains, 1, 2, date.plusDays(1)));

    }
}
