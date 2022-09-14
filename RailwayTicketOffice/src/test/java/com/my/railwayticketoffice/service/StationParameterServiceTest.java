package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        String stationNameUA = "станція";
        String stationNameEN = "station";
        String stationId = "1";

        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("stationNameUA", "stationNameEN", "stationName", "stationId"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", stationNameEN, stationNameUA,
                null, "", stationNameUA, stationNameEN,
                null, "", stationNameUA, stationNameEN,
                null, "", stationId));

        ParameterService<String> stationParameterService = new StationParameterService();

        parameters.put(keys.get(0), values.get(3));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(7));
        assertTrue(stationParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(10));
        assertTrue(stationParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(14));
        assertTrue(stationParameterService.check(parameters, session));

        int count = 0;

        for (int i = 0; i < 2; i++) {
            parameters.put(keys.get(i), values.get(count++));
            assertFalse(stationParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(stationParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(stationParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertTrue(stationParameterService.check(parameters, session));
        }

        parameters.put(keys.get(2), values.get(count++));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(count++));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(count++));
        assertTrue(stationParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(count--));
        assertFalse(stationParameterService.check(parameters, session));

        when(session.getAttribute("locale")).thenReturn("en");
        parameters.put(keys.get(2), values.get(count++));
        assertFalse(stationParameterService.check(parameters, session));

        when(session.getAttribute("locale")).thenReturn("en");
        parameters.put(keys.get(2), values.get(count++));
        assertTrue(stationParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(count++));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(count++));
        assertFalse(stationParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(count));
        assertTrue(stationParameterService.check(parameters, session));
    }
}
