package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainSortingByAvailableSeats}
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByAvailableSeatsTest {

    /**
     * Test for method sort from {@link TrainSortingByAvailableSeats}.
     */
    @Test
    public void testSort() {
        Train train = new Train();
        train.setSeats(50);
        Train train1 = new Train();
        train1.setSeats(150);
        Train train2 = new Train();
        train2.setSeats(100);
        Train train3 = new Train();
        train3.setSeats(250);
        Train train4 = new Train();
        train4.setSeats(200);

        List<Train> trains = new ArrayList<>(Arrays.asList(train, train1, train2, train3, train4));
        List<Train> sortedTrains = new ArrayList<>(Arrays.asList(train3, train4, train1, train2, train));

        assertEquals(sortedTrains, new TrainSortingByAvailableSeats().sort(trains, new HashMap<>()));
    }
}
