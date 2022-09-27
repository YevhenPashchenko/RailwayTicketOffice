package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainFilterByCarriagesFreeSeats}
 *
 * @author Yevhen Pashchenko
 */
public class TrainFilterByCarriagesFreeSeatsTest {

    /**
     * Test for method filter from {@link TrainFilterByCarriagesFreeSeats}.
     */
    @Test
    public void testFilter() {
        List<Train> trains = new ArrayList<>();

        Train train = new Train();
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        train.addCarriage(carriage.getId(), carriage);
        trains.add(train);

        assertEquals(0, new TrainFilterByCarriagesFreeSeats().filter(trains, new HashMap<>()).get(0).getCarriages().size());

        carriage.addSeat(1);
        train.addCarriage(carriage.getId(), carriage);

        assertEquals(1, new TrainFilterByCarriagesFreeSeats().filter(trains, new HashMap<>()).get(0).getCarriages().size());
    }
}
