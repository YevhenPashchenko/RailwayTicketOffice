package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link UserLoginCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserLoginCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    UserDAO userDAO = mock(UserDAO.class);

    /**
     * Test for method execute from {@link UserLoginCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setEmail("email");
        user.setPassword("93M7C3ez6Fjo8_JnCjCNidIyy1hKyxusapdRtntgZOIsJx5Xy0sOt4aFSTrNAvpwhAr-BjYYGjMXOpUt3uhDXk");
        user.setRole("admin");

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("email")).thenReturn("email");
        when(request.getParameter("password")).thenReturn("user1pass");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, "email")).thenReturn(user);
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("datePicker")).thenReturn("1.1.1");

        assertEquals("controller?command=getTrains&from=1&to=2&datePicker=1.1.1", new UserLoginCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserLoginCommand} when not additional parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        User user = new User();
        user.setEmail("email");
        user.setPassword("93M7C3ez6Fjo8_JnCjCNidIyy1hKyxusapdRtntgZOIsJx5Xy0sOt4aFSTrNAvpwhAr-BjYYGjMXOpUt3uhDXk");
        user.setRole("admin");

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("email")).thenReturn("email");
        when(request.getParameter("password")).thenReturn("user1pass");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, "email")).thenReturn(user);

        assertEquals("controller?command=mainPage", new UserLoginCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
