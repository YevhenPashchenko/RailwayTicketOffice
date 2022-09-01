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
 * Tests for methods from {@link UserEditCommand}
 *
 * @author Yevhen Pashchenko
 */
public class UserEditCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    UserDAO userDAO = mock(UserDAO.class);

    /**
     * Test for method execute from {@link UserEditCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");
        when(request.getParameter("userSurname")).thenReturn("userSurname");
        when(request.getParameter("userName")).thenReturn("userName");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("1");
        when(request.getParameter("datePicker")).thenReturn("1.1.1");

        assertEquals("controller?command=getTrains&from=1&to=1&datePicker=1.1.1", new UserEditCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserEditCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSession() throws Exception {
        when(request.getSession()).thenReturn(session);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("1");
        when(request.getParameter("datePicker")).thenReturn("1.1.1");

        assertEquals("controller?command=getTrains&from=1&to=1&datePicker=1.1.1", new UserEditCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserEditCommand} when additional parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotAdditionalParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("confirmPassword")).thenReturn("password");
        when(request.getParameter("userSurname")).thenReturn("userSurname");
        when(request.getParameter("userName")).thenReturn("userName");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);

        assertEquals("controller?command=mainPage", new UserEditCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link UserEditCommand} when user is not in session and additional parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSessionAndNotAdditionalParameters() throws Exception {
        when(request.getSession()).thenReturn(session);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);

        assertEquals("controller?command=mainPage", new UserEditCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
