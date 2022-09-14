package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Station station1 = new Station();
        station1.setId(1);
        Station station2 = new Station();
        station2.setId(2);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("from", "1");
        parameters.put("to", "2");
        parameters.put("date", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        List<Train> trains = new ArrayList<>();

        Train train = new Train();
        train.setDepartureTime(time);
        train.getRoute().addDistanceFromStart(station1.getId(), 100);
        train.getRoute().addDistanceFromStart(station2.getId(), 200);
        train.getRoute().addTimeSinceStart(station1.getId(), "00:05");
        train.getRoute().addStation(station1);
        train.getRoute().addStation(station2);

        Train train1 = new Train();
        train1.getRoute().addDistanceFromStart(station1.getId(), 200);
        train1.getRoute().addDistanceFromStart(station2.getId(), 100);
        train1.getRoute().addStation(station1);
        train1.getRoute().addStation(station2);

        Train train2 = new Train();
        train2.setDepartureTime(time);
        train2.getRoute().addDistanceFromStart(station1.getId(), 100);
        train2.getRoute().addDistanceFromStart(station2.getId(), 200);
        train2.getRoute().addTimeSinceStart(station1.getId(), "23:55");
        train2.getRoute().addStation(station1);
        train2.getRoute().addStation(station2);

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

        assertEquals(filteredTrains, new TrainFilterByDirectionAndDepartureTime().filter(trains, parameters));
        parameters.put("date", date.plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        assertEquals(filteredTrains1, new TrainFilterByDirectionAndDepartureTime().filter(trains, parameters));

    }
}
