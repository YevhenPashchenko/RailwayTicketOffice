package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.authentication.PasswordAuthentication;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link UserLoginCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserLoginCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    MockedStatic<PasswordAuthentication> PasswordAuthenticationMocked = Mockito.mockStatic(PasswordAuthentication.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    UserDAO userDAO = mock(UserDAO.class);

    @BeforeEach
    void beforeEach() throws Exception {
        when(request.getSession()).thenReturn(session);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
        PasswordAuthenticationMocked.close();
    }

    /**
     * Test for method execute from {@link UserLoginCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        String email = "user@mail.com";
        String password = "password";
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRegistered(true);

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, email)).thenReturn(user);
        PasswordAuthenticationMocked.when(() -> PasswordAuthentication.check(password, user.getPassword())).thenReturn(true);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new UserLoginCommand().execute(request, response));
        verify(session, times(1)).setAttribute("user", user);
    }

    /**
     * Test for method execute from {@link UserLoginCommand} when not additional parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        String email = "user@mail.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRegistered(true);

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, email)).thenReturn(user);
        PasswordAuthenticationMocked.when(() -> PasswordAuthentication.check(password, user.getPassword())).thenReturn(true);

        assertEquals("controller?command=mainPage", new UserLoginCommand().execute(request, response));
        verify(session, times(1)).setAttribute("user", user);
    }

    /**
     * Test for method execute from {@link UserLoginCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotParameters() throws Exception {

        assertEquals("controller?command=mainPage", new UserLoginCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
