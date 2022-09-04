package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link PassengerParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class PassengerParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link PassengerParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String[]> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("surname", "name"));
        List<String[]> values = new ArrayList<>(Arrays.asList(new String[] {null}, new String[] {""}, new String[] {"surname"}, new String[] {null}, new String[] {""}, new String[] {"name"}));

        ParameterService<String[]> passengerParameterService = new PassengerParameterService();

        assertFalse(passengerParameterService.check(parameters, session));

        parameters.put(keys.get(0), values.get(2));
        assertFalse(passengerParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(5));
        assertTrue(passengerParameterService.check(parameters, session));

        int count = 0;

        for (String key : keys) {
            parameters.put(key, values.get(count));
            assertFalse(passengerParameterService.check(parameters, session));

            parameters.put(key, values.get(1 + count));
            assertFalse(passengerParameterService.check(parameters, session));

            parameters.put(key, values.get(2 + count));
            assertTrue(passengerParameterService.check(parameters, session));

            count += 3;
        }
    }
}
