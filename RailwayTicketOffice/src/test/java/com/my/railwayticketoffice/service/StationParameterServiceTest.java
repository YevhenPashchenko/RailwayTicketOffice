package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link StationParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class StationParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link StationParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("stationNameUA", "stationNameEN", "stationName", "stationId"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "stationNameUA", null, "", "stationNameEN", null, "", "stationName", null, "", "1"));

        ParameterService<String> stationParameterService = new StationParameterService();

        parameters.put(keys.get(0), values.get(2));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(5));
        assertTrue(stationParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(8));
        parameters.put(keys.get(3), values.get(11));

        int count = 0;

        for (String key : keys) {
            parameters.put(key, values.get(count));
            assertFalse(stationParameterService.check(parameters, session));

            parameters.put(key, values.get(1 + count));
            assertFalse(stationParameterService.check(parameters, session));

            parameters.put(key, values.get(2 + count));
            assertTrue(stationParameterService.check(parameters, session));

            count += 3;
        }
    }
}
