package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link ReturnTicketParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class ReturnTicketParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link ReturnTicketParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        String key = "ticketNumber";
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "1-1-1-1", "1-1-1-1-1", "1-1-1-111111111111-111111111111"));

        ParameterService<String> returnTicketParameterService = new ReturnTicketParameterService();

        parameters.put(key, values.get(0));
        assertFalse(returnTicketParameterService.check(parameters, session));

        parameters.put(key, values.get(1));
        assertFalse(returnTicketParameterService.check(parameters, session));

        parameters.put(key, values.get(2));
        assertFalse(returnTicketParameterService.check(parameters, session));

        parameters.put(key, values.get(3));
        assertFalse(returnTicketParameterService.check(parameters, session));

        parameters.put(key, values.get(4));
        assertTrue(returnTicketParameterService.check(parameters, session));
    }
}
