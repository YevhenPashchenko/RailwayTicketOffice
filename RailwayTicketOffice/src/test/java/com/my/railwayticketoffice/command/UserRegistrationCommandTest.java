package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link UserRegistrationCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserRegistrationCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final UserDAO userDAO = mock(UserDAO.class);
    private final Mail mail = mock(Mail.class);

    @BeforeEach
    void beforeEach() throws Exception {
        when(request.getSession()).thenReturn(session);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        String email = "user@mail.com";
        String password = "password";
        String confirmPassword = "password";
        String userSurname = "userSurname";
        String userName = "userName";
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        User user = new User();

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirmPassword);
        when(request.getParameter("userSurname")).thenReturn(userSurname);
        when(request.getParameter("userName")).thenReturn(userName);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, email)).thenReturn(user);

        Command userRegistrationCommand = new UserRegistrationCommand();

        Field field = userRegistrationCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(userRegistrationCommand, mail);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, userRegistrationCommand.execute(request, response));
        verify(userDAO, times(1)).addUser(connection, user);
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand} when not additional parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        String email = "user@mail.com";
        String password = "password";
        String confirmPassword = "password";
        String userSurname = "userSurname";
        String userName = "userName";

        User user = new User();

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirmPassword);
        when(request.getParameter("userSurname")).thenReturn(userSurname);
        when(request.getParameter("userName")).thenReturn(userName);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, email)).thenReturn(user);

        Command userRegistrationCommand = new UserRegistrationCommand();
        Field field = userRegistrationCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(userRegistrationCommand, mail);

        assertEquals("controller?command=mainPage", userRegistrationCommand.execute(request, response));
        verify(userDAO, times(1)).addUser(connection, user);
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand} when not user parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserParameters() throws Exception {
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new UserRegistrationCommand().execute(request, response));
        verifyNoInteractions(connection);
    }

    /**
     * Test for method execute from {@link UserRegistrationCommand} when not all request parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAllParameters() throws Exception {

        assertEquals("controller?command=mainPage", new UserRegistrationCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
