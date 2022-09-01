package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
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
 * Tests for methods from {@link EditStationDataOnTrainRouteCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditStationDataOnTrainRouteCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);

    /**
     * Test for method execute from {@link EditStationDataOnTrainRouteCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("timeSinceStart")).thenReturn("00:00");
        when(request.getParameter("stopTime")).thenReturn("00:00");
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("stationId")).thenReturn("1");
        when(request.getParameter("distanceFromStart")).thenReturn("1");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);

        assertEquals("controller?command=showRoute&trainId=1", new EditStationDataOnTrainRouteCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link EditStationDataOnTrainRouteCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSession() throws Exception {

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(null);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);

        assertEquals("controller?command=mainPage", new EditStationDataOnTrainRouteCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
