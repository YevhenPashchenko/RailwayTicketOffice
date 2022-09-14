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
        String trainId = "1";
        String trainNumber = "номер";
        String trainDepartureTime = "00:00";
        String carriageNumber = "1";
        String typeId = "1";
        String carriageType = "тип";
        String newCarriageType = "новий тип";
        String maxSeats = "1";
        List<String> keys = new ArrayList<>(Arrays.asList("trainId", "trainNumber", "trainDepartureTime", "carriageNumber", "typeId", "carriageType", "newCarriageType", "maxSeats"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "trainId", trainId,
                null, "", "number", trainNumber,
                null, "", "trainDepartureTime", trainDepartureTime,
                null, "", "carriageNumber", carriageNumber,
                null, "", "typeId", typeId,
                null, "", "carriageType", carriageType,
                null, "", carriageType, newCarriageType,
                null, "", "maxSeats", maxSeats));

        ParameterService<String> trainParameterService = new TrainParameterService();

        parameters.put(keys.get(0), values.get(3));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(7));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(11));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(15));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(4), values.get(19));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(5), values.get(23));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(6), values.get(27));
        assertTrue(trainParameterService.check(parameters, session));

        parameters.put(keys.get(7), values.get(31));
        assertTrue(trainParameterService.check(parameters, session));

        int count = 0;

        for (int i = 0; i < 4; i++) {
            parameters.put(keys.get(i), values.get(count++));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(trainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertTrue(trainParameterService.check(parameters, session));
        }
    }
}
