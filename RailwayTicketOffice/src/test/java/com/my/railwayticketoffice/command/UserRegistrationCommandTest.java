package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link UserRegistrationCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserRegistrationCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    UserDAO userDAO = mock(UserDAO.class);
    Mail mail = mock(Mail.class);

    /**
     * Test for method execute from {@link UserRegistrationCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("email@com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");
        when(request.getParameter("userSurname")).thenReturn("userSurname");
        when(request.getParameter("userName")).thenReturn("userName");
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("departureDate")).thenReturn("1.1.1");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, "email@com")).thenReturn(user);

        Command userRegistrationCommand = new UserRegistrationCommand();
        Field field = userRegistrationCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(userRegistrationCommand, mail);

        assertEquals("controller?command=getTrains&from=1&to=2&departureDate=1.1.1", userRegistrationCommand.execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand} when not additional parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        User user = new User();
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("email")).thenReturn("email@com");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");
        when(request.getParameter("userSurname")).thenReturn("userSurname");
        when(request.getParameter("userName")).thenReturn("userName");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, "email@com")).thenReturn(user);

        Command userRegistrationCommand = new UserRegistrationCommand();
        Field field = userRegistrationCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(userRegistrationCommand, mail);

        assertEquals("controller?command=mainPage", userRegistrationCommand.execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand} when not user parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserParameters() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("departureDate")).thenReturn("1.1.1");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);

        assertEquals("controller?command=getTrains&from=1&to=2&departureDate=1.1.1", new UserRegistrationCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
