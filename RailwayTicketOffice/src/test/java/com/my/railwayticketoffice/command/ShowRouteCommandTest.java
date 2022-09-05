package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
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
 * Tests for methods from {@link ShowRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ShowRouteCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final StationDAO stationDAO = mock(StationDAO.class);

    /**
     * Test for method execute from {@link ShowRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        Train train = new Train();

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);

        assertEquals("route.jsp", new ShowRouteCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link ShowRouteCommand} when needed parameters not exists.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotParameters() throws Exception {
        when(request.getSession()).thenReturn(session);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);

        assertEquals("controller?command=mainPage", new ShowRouteCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
