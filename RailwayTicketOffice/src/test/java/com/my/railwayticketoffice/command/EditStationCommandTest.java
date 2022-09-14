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
 * Tests for methods from {@link EditStationCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditStationCommandTest {

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
     * Test for method execute from {@link EditStationCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String stationId = "1";
        String stationName = "Станція";
        String oldStationName = "Стара назва станції";

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(request.getParameter("stationName")).thenReturn(stationName);
        when(request.getParameter("oldStationName")).thenReturn(oldStationName);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);
        when(stationDAO.checkIfStationExists(connection, oldStationName, null)).thenReturn(1);
        when(stationDAO.checkIfStationExists(connection, stationName, null)).thenReturn(0);

        assertEquals("controller?command=mainPage", new EditStationCommand().execute(request, response));
        verify(stationDAO, times(1)).editStation(connection, Integer.parseInt(stationId), stationName, null);
    }

    /**
     * Test for method execute from {@link EditStationCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        EditStationCommand editStationCommand = new EditStationCommand();

        Field field = editStationCommand.getClass().getDeclaredField("stationService");
        field.setAccessible(true);
        field.set(editStationCommand, stationService);

        assertEquals("controller?command=mainPage", editStationCommand.execute(request, response));
        verifyNoInteractions(stationService);
    }

    /**
     * Test for method execute from {@link EditStationCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new EditStationCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
