package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainSortingByDepartureTime}
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByDepartureTimeTest {

    /**
     * Test for method sort from {@link TrainSortingByDepartureTime}.
     */
    @Test
    public void testSort() {
        LocalTime localTime = LocalTime.of(12, 0);

        Train train = new Train();
        train.setDepartureTime(localTime);
        train.getRoute().addTimeSinceStart(1, "01:00");

        Train train1 = new Train();
        train1.setDepartureTime(localTime);
        train1.getRoute().addTimeSinceStart(1, "11:00");

        Train train2 = new Train();
        train2.setDepartureTime(localTime);
        train2.getRoute().addTimeSinceStart(1, "13:00");

        Train train3 = new Train();
        train3.setDepartureTime(localTime);
        train3.getRoute().addTimeSinceStart(1, "23:00");

        List<Train> trains = new ArrayList<>(Arrays.asList(train, train1, train2, train3));
        List<Train> sortedTrains = new ArrayList<>(Arrays.asList(train2, train3, train, train1));

        Map<String, String> parameters = new HashMap<>();
        parameters.put("from", "1");
        parameters.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        assertEquals(sortedTrains, new TrainSortingByDepartureTime().sort(trains, parameters));
    }
}
