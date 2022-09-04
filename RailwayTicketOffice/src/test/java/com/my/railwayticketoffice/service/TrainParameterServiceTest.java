package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link TrainParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class TrainParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link TrainParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("trainNumber", "trainSeats", "trainDepartureTime", "trainId"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "trainNumber", null, "", "trainSeats", "1", null, "", "trainDepartureTime", "00:00", null, "", "trainId", "1"));

        ParameterService<String> trainParameterService = new TrainParameterService();

        parameters.put(keys.get(0), values.get(2));
        assertFalse(trainParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(6));
        assertFalse(trainParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(10));
        assertTrue(trainParameterService.check(parameters, session));

        int count = 0;

        parameters.put(keys.get(count), values.get(count));
        assertFalse(trainParameterService.check(parameters, session));

        parameters.put(keys.get(count), values.get(1 + count));
        assertFalse(trainParameterService.check(parameters, session));

        parameters.put(keys.get(count), values.get(2 + count));
        assertTrue(trainParameterService.check(parameters, session));

        count = 3;

        for (int i = 1; i < keys.size(); i++) {
            parameters.put(keys.get(i), values.get(count));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(1 + count));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(2 + count));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(3 + count));
            assertTrue(trainParameterService.check(parameters, session));

            count += 4;
        }
    }
}
