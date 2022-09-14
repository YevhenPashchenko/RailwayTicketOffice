package com.my.railwayticketoffice.command;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link ConfirmRegistrationCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ConfirmRegistrationCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final UserDAO userDAO = mock(UserDAO.class);

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
     * Test for method execute from {@link ConfirmRegistrationCommand}.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        String email = "email@mail.com";

        User user = new User();
        user.setRegistered(false);

        when(request.getParameter("email")).thenReturn(email);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(userDAO.getUser(connection, email)).thenReturn(user);

        assertEquals("controller?command=mainPage", new ConfirmRegistrationCommand().execute(request, response));
        verify(userDAO, times(1)).updateUser(connection, user);
    }

    /**
     * Test for method execute from {@link ConfirmRegistrationCommand} when incorrect parameters in request.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        when(request.getParameter("email")).thenReturn(null);

        assertEquals("controller?command=mainPage", new ConfirmRegistrationCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
