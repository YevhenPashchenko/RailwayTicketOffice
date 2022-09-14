package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link TicketParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class TicketParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link TicketParameterService}.
     */
    @Test
    public void testCheck() {
        String carriage = "1";
        String seat = "1";
        String cost = "1";
        String surname = "surname";
        String name = "name";

        Map<String, String[]> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("carriage", "seat", "cost", "surname", "name"));
        List<String[]> values = new ArrayList<>(Arrays.asList(new String[] {null}, new String[] {""}, new String[] {carriage},
                new String[] {null}, new String[] {""}, new String[] {seat},
                new String[] {null}, new String[] {""}, new String[] {cost},
                new String[] {null}, new String[] {""}, new String[] {surname},
                new String[] {null}, new String[] {""}, new String[] {name}));

        ParameterService<String[]> ticketParameterService = new TicketParameterService();

        parameters.put(keys.get(0), values.get(2));
        assertFalse(ticketParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(5));
        assertFalse(ticketParameterService.check(parameters, session));

        parameters.put(keys.get(2), values.get(8));
        assertTrue(ticketParameterService.check(parameters, session));

        parameters.put(keys.get(3), values.get(11));
        assertFalse(ticketParameterService.check(parameters, session));

        parameters.put(keys.get(4), values.get(14));
        assertTrue(ticketParameterService.check(parameters, session));

        int count = 0;

        for (String key : keys) {
            parameters.put(key, values.get(count++));
            assertFalse(ticketParameterService.check(parameters, session));

            parameters.put(key, values.get(count++));
            assertFalse(ticketParameterService.check(parameters, session));

            parameters.put(key, values.get(count++));
            assertTrue(ticketParameterService.check(parameters, session));
        }
    }
}
