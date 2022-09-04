package com.my.railwayticketoffice.service;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Tests for methods from {@link UserParameterService}
 *
 * @author Yevhen Pashchenko
 */
public class UserParameterServiceTest {

    HttpSession session = mock(HttpSession.class);

    /**
     * Test for method check from {@link UserParameterService}.
     */
    @Test
    public void testCheck() {
        Map<String, String> parameters = new HashMap<>();
        List<String> keys = new ArrayList<>(Arrays.asList("password", "surname", "name", "email", "confirmPassword"));
        List<String> values = new ArrayList<>(Arrays.asList(null, "", "password", null, "", "surname", null, "", "name", null, "", "email", "email@com", null, "", "confirmPassword", "password"));

        ParameterService<String> userParameterService = new UserParameterService();

        int count = 0;

        for (int i = 0; i < keys.size() - 2; i++) {
            parameters.put(keys.get(i), values.get(count));
            assertFalse(userParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(1 + count));
            assertFalse(userParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(2 + count));
            assertTrue(userParameterService.check(parameters, session));

            count += 3;
        }

        for (int i = keys.size() - 2; i < keys.size(); i++) {
            parameters.put(keys.get(i), values.get(count));
            assertFalse(userParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(1 + count));
            assertFalse(userParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(2 + count));
            assertFalse(userParameterService.check(parameters, session));

            parameters.put(keys.get(i), values.get(3 + count));
            assertTrue(userParameterService.check(parameters, session));

            count += 4;
        }
    }
}
