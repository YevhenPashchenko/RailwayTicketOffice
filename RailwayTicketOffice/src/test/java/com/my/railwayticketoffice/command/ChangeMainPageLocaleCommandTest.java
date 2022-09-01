package com.my.railwayticketoffice.command;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link ChangeMainPageLocaleCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ChangeMainPageLocaleCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    /**
     * Test for method execute from {@link ChangeMainPageLocaleCommand}.
     */
    @Test
    public void testExecute() {

        when(request.getParameter("trainsSortedCommand")).thenReturn("trainsSortedCommand");
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("1");
        when(request.getParameter("datePicker")).thenReturn("1.1.1");

        assertEquals("controller?command=trainsSortedCommand&page=1&from=1&to=1&datePicker=1.1.1", new ChangeMainPageLocaleCommand().execute(request, response));
    }

    /**
     * Test for method execute from {@link ChangeMainPageLocaleCommand} when no additional parameters.
     */
    @Test
    public void testExecuteWhenNoAdditionalParameters() {

        assertEquals("controller?command=mainPage", new ChangeMainPageLocaleCommand().execute(request, response));
    }
}
