package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link MainPageCommand}
 *
 * @author Yevhen Pashchenko
 */
public class MainPageCommandTest {

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
     * Test for method execute from {@link MainPageCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        List<Train> trains = new ArrayList<>();
        Train train = new Train();
        trains.add(train);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);
        when(trainDAO.getAllTrains(connection)).thenReturn(trains);

        assertEquals("main.jsp", new MainPageCommand().execute(request, response));
        verify(trainDAO, times(1)).getCarriagesTypes(connection);
    }

    /**
     * Test for method execute from {@link MainPageCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {
        when(DBManager.getInstance().getStationDAO()).thenReturn(stationDAO);

        assertEquals("main.jsp", new MainPageCommand().execute(request, response));
        verifyNoInteractions(trainDAO);
    }
}
