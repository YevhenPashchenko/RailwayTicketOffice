package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for methods from {@link AvailableSeatFilter}
 *
 * @author Yevhen Pashchenko
 */
public class AvailableSeatFilterTest {

    /**
     * Test for method check from {@link AvailableSeatFilter}.
     */
    @Test
    public void testCheck() {
        Train train = new Train();

        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.addSeat(1);
        train.addCarriage(carriage.getId(), carriage);

        Train.Carriage carriage1 = train.new Carriage();
        carriage1.setId(2);
        carriage1.setNumber(2);
        carriage1.addSeat(1);
        train.addCarriage(carriage1.getId(), carriage1);

        Map<String, String[]> ticketParameters = new HashMap<>();
        ticketParameters.put("carriage", new String[] {"1"});
        ticketParameters.put("seat", new String[] {"1"});

        assertTrue(new AvailableSeatFilter().check(train, ticketParameters));

        ticketParameters.put("carriage", new String[] {"1", "2"});
        ticketParameters.put("seat", new String[] {"1", "2"});

        assertFalse(new AvailableSeatFilter().check(train, ticketParameters));
    }
}
