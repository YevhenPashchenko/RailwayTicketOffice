package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link TrainSearchParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class TrainSearchParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link TrainSearchParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("from", "to", "date"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "from", "1", null, "", "to", "2", null, "", "dd.MM.yyyy", "1.1.1"));

        ParameterService<String> searchTrainParameterService = new TrainSearchParameterService();

        assertFalse(searchTrainParameterService.check(parameters, session));

        parameters.put(keys.get(0), values.get(3));
        assertFalse(searchTrainParameterService.check(parameters, session));

        parameters.put(keys.get(1), values.get(7));
        assertTrue(searchTrainParameterService.check(parameters, session));

        int count = 0;

        for (int i = 0; i < keys.size() - 1; i++) {
            parameters.put(keys.get(i), values.get(count++));
            assertFalse(searchTrainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(searchTrainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertFalse(searchTrainParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(count++));
            assertTrue(searchTrainParameterService.check(parameters, session));
        }
    }
}
