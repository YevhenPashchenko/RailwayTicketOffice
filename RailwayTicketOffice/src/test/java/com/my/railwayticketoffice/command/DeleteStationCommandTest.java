package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.StationParameterService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link DeleteStationCommand}
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final StationDAO stationDAO = mock(StationDAO.class);
    private final ParameterService<String> stationService = mock(StationParameterService.class);

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
     * Test for method execute from {@link DeleteStationCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String stationId = "1";
        String stationName = "Станція";

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(request.getParameter("stationName")).thenReturn(stationName);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);
        when(stationDAO.checkIfStationExists(connection, stationName, null)).thenReturn(1);

        assertEquals("controller?command=mainPage", new DeleteStationCommand().execute(request, response));
        verify(stationDAO, times(1)).deleteStation(connection, Integer.parseInt(stationId));
    }

    /**
     * Test for method execute from {@link DeleteStationCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        DeleteStationCommand deleteStationCommand = new DeleteStationCommand();

        Field field = deleteStationCommand.getClass().getDeclaredField("stationService");
        field.setAccessible(true);
        field.set(deleteStationCommand, stationService);

        assertEquals("controller?command=mainPage", new DeleteStationCommand().execute(request, response));
        verifyNoInteractions(stationService);
    }

    /**
     * Test for method execute from {@link DeleteStationCommand} when incorrect parameters in request.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new DeleteStationCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
