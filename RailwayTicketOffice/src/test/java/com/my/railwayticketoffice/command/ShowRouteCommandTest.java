package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
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
 * Tests for methods from {@link ShowRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ShowRouteCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final StationDAO stationDAO = mock(StationDAO.class);

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
     * Test for method execute from {@link ShowRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String from = "1";
        String to = "2";

        Train train = new Train();
        Station station = new Station();
        train.getRoute().addStation(station);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);

        assertEquals("route.jsp", new ShowRouteCommand().execute(request, response));
        verify(stationDAO, times(1)).getStations(connection, null);
    }

    /**
     * Test for method execute from {@link ShowRouteCommand} when user is not admin.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteUserNotAdmin() throws Exception {
        User user = new User();

        String trainId = "1";
        String from = "1";
        String to = "2";

        Train train = new Train();
        Station station = new Station();
        train.getRoute().addStation(station);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);

        assertEquals("route.jsp", new ShowRouteCommand().execute(request, response));
        verifyNoInteractions(stationDAO);
    }

    /**
     * Test for method execute from {@link ShowRouteCommand} when needed parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {

        assertEquals("controller?command=mainPage", new ShowRouteCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
