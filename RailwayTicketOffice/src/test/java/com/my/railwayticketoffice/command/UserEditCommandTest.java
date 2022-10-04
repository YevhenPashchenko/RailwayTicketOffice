package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.authentication.PasswordAuthentication;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.UserParameterService;
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
 * Tests for methods from {@link UserEditCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserEditCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    MockedStatic<PasswordAuthentication> PasswordAuthenticationMocked = Mockito.mockStatic(PasswordAuthentication.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final UserDAO userDAO = mock(UserDAO.class);
    private final ParameterService<String> userService = mock(UserParameterService.class);

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
     * Test for method execute from {@link UserEditCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String userSurname = "userSurname";
        String userName = "userName";
        String password = "password";
        String confirmPassword = "password";

        User user = new User();
        user.setId(1);
        user.setPassword(password);
        user.setLastName(userSurname);
        user.setFirstName(userName);
        user.setRegistered(true);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("userSurname")).thenReturn(userSurname);
        when(request.getParameter("userName")).thenReturn(userName);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirmPassword);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        PasswordAuthenticationMocked.when(() -> PasswordAuthentication.getSaltedHash(password)).thenReturn(password);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new UserEditCommand().execute(request, response));
        verify(userDAO, times(1)).updateUser(connection, user);
    }

    /**
     * Test for method execute from {@link UserEditCommand} without password change.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecutePasswordNotChange() throws Exception {
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String userEmail = "user@mail.com";
        String userPassword = "password";
        String userSurname = "userSurname";
        String userName = "userName";

        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("userSurname")).thenReturn(userSurname);
        when(request.getParameter("userName")).thenReturn(userName);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, user.getEmail())).thenReturn(user);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new UserEditCommand().execute(request, response));
        verify(userDAO, times(1)).getUser(connection, user.getEmail());
    }

    /**
     * Test for method execute from {@link UserEditCommand} when additional parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        String userEmail = "user@mail.com";
        String userPassword = "password";
        String userSurname = "userSurname";
        String userName = "userName";

        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("userSurname")).thenReturn(userSurname);
        when(request.getParameter("userName")).thenReturn(userName);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, user.getEmail())).thenReturn(user);

        assertEquals("controller?command=mainPage", new UserEditCommand().execute(request, response));
        verify(userDAO, times(1)).getUser(connection, user.getEmail());
    }

    /**
     * Test for method execute from {@link UserEditCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSession() throws Exception {
        User user = new User();

        when((User) session.getAttribute("user")).thenReturn(user);
        assertEquals("controller?command=mainPage", new UserEditCommand().execute(request, response));
        verifyNoInteractions(connection);
    }

    /**
     * Test for method execute from {@link UserEditCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotParameters() throws Exception {
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new UserEditCommand().execute(request, response));
    }

    /**
     * Test for method execute from {@link UserEditCommand} when user is not in session and additional parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSessionAndNotAdditionalParameters() throws Exception {

        UserEditCommand userEditCommand = new UserEditCommand();

        Field field = userEditCommand.getClass().getDeclaredField("userService");
        field.setAccessible(true);
        field.set(userEditCommand, userService);

        assertEquals("controller?command=mainPage", userEditCommand.execute(request, response));
        verifyNoInteractions(userService);
    }
}
