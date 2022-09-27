package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
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
 * Tests for methods from {@link EditStationDataOnTrainRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditStationDataOnTrainRouteCommandTest {

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
     * Test for method execute from {@link EditStationDataOnTrainRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String stationId = "1";
        String timeSinceStart = "00:00";
        String stopTime = "00:00";
        String distanceFromStart = "1";

        Train train = new Train();
        train.setId(Integer.parseInt(trainId));

        Station station = new Station();
        station.setId(Integer.parseInt(stationId));
        Station station1 = new Station();
        station1.setId(2);

        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), "00:10");
        train.getRoute().addDistanceFromStart(station.getId(), 10);

        train.getRoute().addStation(station1);
        train.getRoute().addTimeSinceStart(station1.getId(), "00:20");
        train.getRoute().addDistanceFromStart(station1.getId(), 20);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(request.getParameter("timeSinceStart")).thenReturn(timeSinceStart);
        when(request.getParameter("stopTime")).thenReturn(stopTime);
        when(request.getParameter("distanceFromStart")).thenReturn(distanceFromStart);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrain(connection, Integer.parseInt(trainId))).thenReturn(train);


        assertEquals("controller?command=showRoute&trainId=" + trainId, new EditStationDataOnTrainRouteCommand().execute(request, response));
        verify(trainDAO, times(1)).editStationDataOnTrainRoute(connection, timeSinceStart, stopTime, Integer.parseInt(distanceFromStart), Integer.parseInt(trainId), Integer.parseInt(stationId));
    }

    /**
     * Test for method execute from {@link EditStationDataOnTrainRouteCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        EditStationDataOnTrainRouteCommand editStationDataOnTrainRouteCommand = new EditStationDataOnTrainRouteCommand();

        Field field = editStationDataOnTrainRouteCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(editStationDataOnTrainRouteCommand, trainService);

        assertEquals("controller?command=mainPage", new EditStationDataOnTrainRouteCommand().execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link EditStationDataOnTrainRouteCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new EditStationDataOnTrainRouteCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
