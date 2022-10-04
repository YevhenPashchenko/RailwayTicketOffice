package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
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
 * Tests for methods from {@link DeleteStationFromTrainRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationFromTrainRouteCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
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
     * Test for method execute from {@link DeleteStationFromTrainRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String stationId = "1";

        Train train = new Train();
        train.setId(Integer.parseInt(trainId));

        Station station = new Station();
        station.setId(Integer.parseInt(stationId));

        train.getRoute().addStation(station);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(trainDAO.getTrain(connection, train.getId())).thenReturn(train);
        when(scheduleDAO.checkIfRecordExists(connection, train.getId())).thenReturn(false);

        assertEquals("controller?command=showRoute&trainId=" + Integer.parseInt(trainId), new DeleteStationFromTrainRouteCommand().execute(request, response));
        verify(trainDAO, times(1)).deleteStationFromTrainRoute(connection, Integer.parseInt(trainId), Integer.parseInt(stationId));
    }

    /**
     * Test for method execute from {@link DeleteStationFromTrainRouteCommand} when train is in schedule.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteTrainIsInSchedule() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String stationId = "1";

        Train train = new Train();
        train.setId(Integer.parseInt(trainId));

        Station station = new Station();
        station.setId(Integer.parseInt(stationId));

        train.getRoute().addStation(station);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("stationId")).thenReturn(stationId);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(trainDAO.getTrain(connection, train.getId())).thenReturn(train);
        when(scheduleDAO.checkIfRecordExists(connection, train.getId())).thenReturn(true);

        assertEquals("controller?command=showRoute&trainId=" + Integer.parseInt(trainId), new DeleteStationFromTrainRouteCommand().execute(request, response));
        verify(scheduleDAO, times(1)).checkIfRecordExists(connection, train.getId());
        verify(trainDAO, times(0)).deleteStationFromTrainRoute(connection, Integer.parseInt(trainId), Integer.parseInt(stationId));
    }

    /**
     * Test for method execute from {@link DeleteStationFromTrainRouteCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        DeleteStationFromTrainRouteCommand deleteStationFromTrainRouteCommand = new DeleteStationFromTrainRouteCommand();

        Field field = deleteStationFromTrainRouteCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(deleteStationFromTrainRouteCommand, trainService);

        assertEquals("controller?command=mainPage", deleteStationFromTrainRouteCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link DeleteStationFromTrainRouteCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new DeleteStationFromTrainRouteCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
