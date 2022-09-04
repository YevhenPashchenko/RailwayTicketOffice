package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link RouteParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class RouteParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link RouteParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("timeSinceStart", "stopTime", "distanceFromStart"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "mm:ss", "00:00", null, "", "mm:ss", "00:00", null, "", "km", "0"));

        ParameterService<String> routeParameterService = new RouteParameterService();

        assertFalse(routeParameterService.check(parameters, session));

        parameters.put(keys.get(0), values.get(3));
        assertFalse(routeParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(7));
        assertFalse(routeParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(11));
        assertTrue(routeParameterService.check(parameters, session));

        int count = 0;

        for (String key : keys) {
            parameters.put(key, values.get(count));
            assertFalse(routeParameterService.check(parameters, session));

            parameters.put(key, values.get(1 + count));
            assertFalse(routeParameterService.check(parameters, session));

            parameters.put(key, values.get(2 + count));
            assertFalse(routeParameterService.check(parameters, session));

            parameters.put(key, values.get(3 + count));
            assertTrue(routeParameterService.check(parameters, session));

            count += 4;
        }
    }
}
