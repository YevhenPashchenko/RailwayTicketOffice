package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
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
 * Tests for methods from {@link AddStationToTrainRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class AddStationToTrainRouteCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ParameterService<String> trainService = mock(TrainParameterService.class);

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
     * Test for method execute from {@link AddStationToTrainRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String timeSinceStart = "00:00";
        String stopTime = "00:00";
        String trainId = "1";
        String stationId = "1";
        String distanceFromStart = "1";

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("timeSinceStart")).thenReturn(timeSinceStart);
        when(request.getParameter("stopTime")).thenReturn(stopTime);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(request.getParameter("distanceFromStart")).thenReturn(distanceFromStart);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);

        assertEquals("controller?command=showRoute&trainId=" + trainId, new AddStationToTrainRouteCommand().execute(request, response));
        verify(trainDAO, times(1)).addStationToTrainRoute(connection, timeSinceStart, stopTime, Integer.parseInt(distanceFromStart), Integer.parseInt(trainId), Integer.parseInt(stationId));
    }

    /**
     *  Test for method execute from {@link AddStationToTrainRouteCommand} when user is not in session.
     *
     *  @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        AddStationToTrainRouteCommand addStationToTrainRouteCommand = new AddStationToTrainRouteCommand();

        Field field = addStationToTrainRouteCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(addStationToTrainRouteCommand, trainService);

        assertEquals("controller?command=mainPage", addStationToTrainRouteCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     *  Test for method execute from {@link AddStationToTrainRouteCommand} when incorrect parameters in request.
     *
     *  @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new AddStationToTrainRouteCommand().execute(request, response));
        verifyNoInteractions(trainDAO);
    }
}
